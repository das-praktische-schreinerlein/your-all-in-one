/** 
 * software for projectmanagement and documentation
 * 
 * @FeatureDomain                Collaboration 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.app.extension.datatransfer.common;

import de.yaio.app.config.ContextHelper;
import de.yaio.app.core.dbservice.BaseNodeRepository;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.common.DatatransferUtils;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.exporter.Exporter;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.app.datatransfer.importer.ImportOptions;
import de.yaio.app.datatransfer.jpa.JPAExporter;
import de.yaio.app.extension.datatransfer.ppl.PPLImporter;
import de.yaio.app.extension.datatransfer.wiki.InlineWikiImporter;
import de.yaio.app.extension.datatransfer.wiki.WikiExporter;
import de.yaio.app.extension.datatransfer.wiki.WikiImportOptions;
import de.yaio.app.extension.datatransfer.wiki.WikiImporter;
import de.yaio.app.extension.datatransfer.wiki.WikiImporter.WikiStructLine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 
 * Services to parse text to nodes and convert them in different 
 * formats (wiki, ppl, excel..)
 *  
 * @FeatureDomain                Webservice
 * @package                      de.yaio.webapp.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Service
public class ExtendedDatatransferUtils extends DatatransferUtils {
    @Autowired
    protected BaseNodeRepository baseNodeDBService;

    @Autowired
    private ApplicationContext appContext;

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(ExtendedDatatransferUtils.class);

    /** 
     * copy the node and all children to the new parent (recalc all and save to db)
     * @param node                   node to copy
     * @param newParent              parent for new node
     * @throws ConverterException    ParserExceptions possible
     */
    @Transactional
    @Override
    public void copyNode(final BaseNode node, final BaseNode newParent) throws ConverterException {
        // read old parent
        BaseNode oldParent = node.getParentNode();

        // read children for new parent
        newParent.initChildNodesFromDB(0);

        //
        // export node
        //
        node.initChildNodesFromDB(-1);
        WikiExporter exporter = new WikiExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setFlgShowIst(false);
        oOptions.setFlgShowSysData(false);
        oOptions.setFlgShowMetaData(true);
        String wikiSrc = exporter.getMasterNodeResult(node, oOptions);

        // create dummy masternode
        BaseNode masterNode = createTemporaryMasternode(
                        newParent.getSysUID(), newParent.getMetaNodePraefix(), newParent.getMetaNodeNummer());
        masterNode.setCachedParentHierarchy(newParent.getCachedParentHierarchy());

        // Parser+Options anlegen
        WikiImportOptions inputOptions = new WikiImportOptions();
        inputOptions.setFlgReadList(true);
        inputOptions.setFlgReadUe(true);
        inputOptions.setStrDefaultMetaNodePraefix(masterNode.getMetaNodePraefix());
        WikiImporter wikiImporter = new WikiImporter(inputOptions);
        try {
            parseNodesFromWiki(wikiImporter, inputOptions, masterNode, wikiSrc);
        } catch (ParserException ex) {
            throw new ConverterException("cant parse node to copy from wiki", wikiSrc, ex);
        }

        // check hierarchy
        WikiExporter wiki = new WikiExporter();
        String wikiStr = wiki.getMasterNodeResult(node, oOptions);
        LOGGER.info("wikiStr:" + wikiStr);

        // remove empty default-nodes
        masterNode = clearEmptyDefaultNodes(masterNode);

        // reset data
        ImportOptions importOptions = createCopyImportOptions();
        resetRestrictedData(masterNode, importOptions, true);

        // JPA-Exporter
        JPAExporter jpaExporter = getJPAExporter();
        jpaExporter.getMasterNodeResult(masterNode, null);

        // renew old parent only if different from newParent
        if (!newParent.getSysUID().equals(oldParent.getSysUID())) {
            // renew oldParent
            oldParent = getBaseNodeRepository().findBaseNode(oldParent.getSysUID());
            oldParent.initChildNodesFromDB(0);
            
            // recalc old parent
            getBaseNodeRepository().updateMeAndMyParents(oldParent);
        }
    }

    /** 
     * parse wikiSrc with an InlineWikiImporter and add it to an InfoNode named Masternode<br>
     * metaNodePraefix will be Inline, metaNodeNumber will start by 1 and increment
     * @param wikiSrc                wikiSrc to parse with InlineWikiImporter
     * @return                       BaseNode - masternode with the children from wikiSrc
     * @throws ParserException       ParserExceptions possible
     */
    public BaseNode parseInlineNodesFromString(final String wikiSrc) throws ParserException {
        // PPL-Importer
        PPLImporter pplImporter = new PPLImporter(null);
        // create dummy masternode
        BaseNode masterNode = (BaseNode) pplImporter.createNodeObjFromText(1, 
                        "INFO - Masternode", "INFO - Masternode", null);
        masterNode.setName("Masternode");
        masterNode.setMetaNodePraefix("Inline");
        masterNode.setMetaNodeNummer("Master");
        
        // parse
        parseInlineNodesFromString(masterNode, wikiSrc);
        
        return masterNode;
    }

    
    /** 
     * parse wikiSrc with an InlineWikiImporter and add it to the masternode<br>
     * metaNodePraefix will be Inline, metaNodeNumber will start by 1 and increment
     * @param masterNode             baseNode to add the children
     * @param wikiSrc                wikiSrc to parse with InlineWikiImporter
     * @throws ParserException       ParserExceptions possible
     */
    public void parseInlineNodesFromString(final BaseNode masterNode, 
                                           final String wikiSrc) throws ParserException {
        
        // Parser+Options anlegen
        WikiImportOptions inputOptions = new WikiImportOptions();
        inputOptions.setFlgReadList(true);
        inputOptions.setFlgReadUe(true);
        WikiImporter wikiImporter = new InlineWikiImporter(inputOptions, "Inline");
        
        parseNodesFromWiki(wikiImporter, inputOptions, masterNode, wikiSrc);
    }

    /** 
     * parse wikiSrc with an WikiImporter and add it to the masternode
     * @param masterNode             baseNode to add the children
     * @param wikiSrc                wikiSrc to parse with WikiImporter
     * @throws ParserException       ParserExceptions possible
     */
    public void parseNodesFromString(final BaseNode masterNode, 
                                     final String wikiSrc) throws ParserException {
        // Parser+Options anlegen
        WikiImportOptions inputOptions = new WikiImportOptions();
        inputOptions.setFlgReadList(true);
        inputOptions.setFlgReadUe(true);
        inputOptions.setStrDefaultMetaNodePraefix(masterNode.getMetaNodePraefix());
        WikiImporter wikiImporter = new WikiImporter(inputOptions);
        
        parseNodesFromWiki(wikiImporter, inputOptions, masterNode, wikiSrc);
        
    }
    
    /** 
     * parse wikiSrc with WikiImporter and WikiImportOptions, and add it to the masternode
     * @param wikiImporter           wikiImporter for parsing
     * @param inputOptions           importOptions for wikiImporter
     * @param masterNode             baseNode to add the children
     * @param wikiSrc                wikiSrc to parse with WikiImporter
     * @throws ParserException       ParserExceptions possible
     */
    public void parseNodesFromWiki(final WikiImporter wikiImporter, 
                                      final WikiImportOptions inputOptions,
                                      final BaseNode masterNode, 
                                      final String wikiSrc) throws ParserException {
        // parse src
        List<WikiStructLine> lstWikiLines;
        lstWikiLines = wikiImporter.extractWikiStructLinesFromSrc(wikiSrc, inputOptions);

        // add to PPL-source
        StringBuffer resBuf = new StringBuffer();
        if (lstWikiLines != null) {
            for (WikiStructLine wk : lstWikiLines) {
                resBuf.append(wk.getHirarchy()).append("\n");
            }
        }
        String pplSource = resBuf.toString();
        
        // PPL-IMporter
        PPLImporter pplImporter = new PPLImporter(null);
        pplImporter.extractNodesFromLines(masterNode, pplSource, "\t");
    }

    /** 
     * parse jsonSrc with JsonImporter, export as wiki and reimport from wiki to ensure valid syntax, 
     * and add it to the masternode
     * @param inputOptions           importOptions for wikiImporter
     * @param masterNode             baseNode to add the children
     * @param jsonSrc                jsonSrc to parse with JsonImporter
     * @throws ParserException       ParserExceptions possible
     */
    public void parseValidatedNodesFromJson(final WikiImportOptions inputOptions,
                                    final BaseNode masterNode, 
                                    final String jsonSrc) throws ParserException {
        // parse json to dummy-masternode
        BaseNode tmpMasterNode = 
             createTemporaryMasternode("dummy", masterNode.getMetaNodePraefix(), masterNode.getMetaNodeNummer());
        this.parseNodesFromJson(inputOptions, tmpMasterNode, jsonSrc);
        
        // export as wiki
        Exporter exporter = new WikiExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        String wikiSrc;
        try {
            wikiSrc = exporter.getMasterNodeResult(tmpMasterNode, oOptions);
        } catch (ConverterException ex) {
            throw new ParserException("error while converting json to wiki", jsonSrc, ex);
        }
        
        // import from wiki
        WikiImportOptions tmpInputOptions = new WikiImportOptions();
        inputOptions.setFlgReadList(true);
        inputOptions.setFlgReadUe(true);
        inputOptions.setStrDefaultMetaNodePraefix(masterNode.getMetaNodePraefix());
        WikiImporter wikiImporter = new WikiImporter(tmpInputOptions);
        this.parseNodesFromWiki(wikiImporter, tmpInputOptions, masterNode, wikiSrc);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("masternode after wiki:" 
                         + masterNode.getBaseNodeService().visualizeNodeHierarchy("", masterNode));
        }
    }

    protected BaseNodeRepository getBaseNodeRepository() {
        return baseNodeDBService;
    }

    protected JPAExporter getJPAExporter() {
        JPAExporter exporter = new JPAExporter();
        ContextHelper.getInstance().autowireService(appContext, exporter);
        return exporter;
    }
}
