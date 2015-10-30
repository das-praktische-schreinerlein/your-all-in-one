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
package de.yaio.extension.datatransfer.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.yaio.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.EventNode;
import de.yaio.core.node.InfoNode;
import de.yaio.core.node.SymLinkNode;
import de.yaio.core.node.TaskNode;
import de.yaio.core.node.UrlResNode;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.extension.datatransfer.jpa.JPAExporter;
import de.yaio.extension.datatransfer.json.JSONFullImporter;
import de.yaio.extension.datatransfer.json.JSONResponse;
import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.extension.datatransfer.wiki.InlineWikiImporter;
import de.yaio.extension.datatransfer.wiki.WikiExporter;
import de.yaio.extension.datatransfer.wiki.WikiImportOptions;
import de.yaio.extension.datatransfer.wiki.WikiImporter;
import de.yaio.extension.datatransfer.wiki.WikiImporter.WikiStructLine;
import de.yaio.utils.Calculator;

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
public class DatatransferUtils {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(DatatransferUtils.class);

    /** 
     * copy the node and all children to the new parent (recalc all and save to db)
     * @FeatureDomain                WikiImporter
     * @FeatureResult                copies and save ne node
     * @FeatureKeywords              WikiImporter
     * @param node                   node to copy
     * @param newParent              parent for new node
     * @throws Exception             ParserExceptions possible
     */
    @Transactional
    public void copyNode(BaseNode node, BaseNode newParent) throws Exception {
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
        oOptions.setFlgShowMetaData(false);
        String wikiSrc = exporter.getMasterNodeResult(node, oOptions);

        // create dummy masternode
        BaseNode masterNode = createTemporaryMasternode(
                        newParent.getSysUID(), newParent.getMetaNodePraefix(), newParent.getMetaNodeNummer());
        
        // Parser+Options anlegen
        WikiImportOptions inputOptions = new WikiImportOptions();
        inputOptions.setFlgReadList(true);
        inputOptions.setFlgReadUe(true);
        inputOptions.setStrDefaultMetaNodePraefix(masterNode.getMetaNodePraefix());
        WikiImporter wikiImporter = new WikiImporter(inputOptions);
        parseNodesFromWiki(wikiImporter, inputOptions, masterNode, wikiSrc);
        
        // JPA-Exporter
        JPAExporter jpaExporter = new JPAExporter();
        jpaExporter.getMasterNodeResult(masterNode, null);

        // renew old parent only if different from newParent
        if (newParent.getSysUID() != oldParent.getSysUID()) {
            // renew oldParent
            oldParent = BaseNode.findBaseNode(oldParent.getSysUID());
            oldParent.initChildNodesFromDB(0);
            
            // recalc old parent
            BaseNodeDBServiceImpl.getInstance().updateMeAndMyParents(oldParent);
        }
    }
    
    
    /** 
     * move the node and all children to the new parent (recalc all and save to db)
     * @FeatureDomain                WikiImporter
     * @FeatureResult                copies and save ne node
     * @FeatureKeywords              WikiImporter
     * @param node                   node to move
     * @param newParent              new parent for node
     * @param newSortPos             position for node  in childlist of newParent
     * @throws Exception             ParserExceptions possible
     */
    @Transactional
    public void moveNode(BaseNode node, BaseNode newParent, Integer newSortPos) throws Exception {
        // map the data
        boolean flgChangedParent = false;
        boolean flgChangedPosition = false;

        // read children for old parent
        BaseNode oldParent = node.getParentNode();
        oldParent.initChildNodesFromDB(0);

        // read children for both parents
        newParent.initChildNodesFromDB(0);

        // check for new parent
        if (newParent.getSysUID() != oldParent.getSysUID()) {
            // reset sortpos
            node.setSortPos(null);
            flgChangedPosition = true;

            // set new parent
            flgChangedParent = true;
            node.setParentNode(newParent);
        } else {
            // check if position changed
            if (node.getSortPos().intValue() != newSortPos.intValue()) {
                flgChangedPosition = true;
            }
        }

        // check for needed update
        if (flgChangedParent || flgChangedPosition) {
            // recalc the position
            newParent.getBaseNodeService().moveChildToSortPos(newParent, node, newSortPos);

            // save children of newParent
            newParent.saveChildNodesToDB(0, true);

            // recalc and save
            BaseNodeDBServiceImpl.getInstance().updateMeAndMyParents(node);

            // renew old parent only if changed
            if (flgChangedParent) {
                // renew oldParent
                oldParent = BaseNode.findBaseNode(oldParent.getSysUID());

                // recalc old parent
                BaseNodeDBServiceImpl.getInstance().updateMeAndMyParents(oldParent);
            }
        } else {
            // read children for node
            node.initChildNodesFromDB(0);
        }
    }

    /** 
     * map the nodeData from newNode to origNode
     * @FeatureDomain                Webservice
     * @FeatureResult                boolean flgChange - true if data changed
     * @FeatureKeywords              Webservice Query
     * @param origNode               
     * @param newNode                the new node created from request-data
     * @return                       true if data changed
     * @throws IllegalAccessException thrown if class of origNode!=newNode
     */
    public boolean mapNodeData(final BaseNode origNode, final BaseNode newNode) 
                    throws IllegalAccessException {
        boolean flgChange = false;
        
        // check class
        if (!origNode.getClassName().equals(newNode.getClassName())) {
            // class differ!!
            throw new IllegalAccessException("cant map origNode (" + origNode.getClassName() + "):" 
                            + origNode.getSysUID() + " with newNode:" + newNode.getClassName());
        }
        
        // common-fields

        // check for new name
        if (Calculator.compareValues(
                        origNode.getName(), newNode.getName()) 
                        != Calculator.CONST_COMPARE_EQ) {
            origNode.setName(newNode.getName());
            flgChange = true;
        }
        // check for type
        if (Calculator.compareValues(
                        origNode.getType(), newNode.getType()) 
                        != Calculator.CONST_COMPARE_EQ) {
            origNode.setType(newNode.getType());
            flgChange = true;
        }
        // check for nodeDesc
        if (Calculator.compareValues(
                        origNode.getNodeDesc(), newNode.getNodeDesc()) 
                        != Calculator.CONST_COMPARE_EQ) {
            origNode.setNodeDesc(newNode.getNodeDesc());
            flgChange = true;
        }
        
        // check for special nodedata recursively
        
        // Task+EventNodes
        if (TaskNode.class.isInstance(origNode) 
            || EventNode.class.isInstance(origNode)) {
            TaskNode newTaskNode = (TaskNode) newNode;
            TaskNode origTaskNode = (TaskNode) origNode;
            
            // get state from type
            origNode.setState(origNode.getType());
            // check for Plan aufwand
            if (Calculator.compareValues(
                            origTaskNode.getPlanAufwand(), newTaskNode.getPlanAufwand()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setPlanAufwand(newTaskNode.getPlanAufwand());
                flgChange = true;
            }
            // check for Plan datestart
            if (Calculator.compareValues(
                            origTaskNode.getPlanStart(), newTaskNode.getPlanStart()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setPlanStart(newTaskNode.getPlanStart());
                flgChange = true;
            }
            // check for Plan dateend
            if (Calculator.compareValues(
                            origTaskNode.getPlanEnde(), newTaskNode.getPlanEnde()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setPlanEnde(newTaskNode.getPlanEnde());
                flgChange = true;
            }
            // check for Ist stand
            if (Calculator.compareValues(
                            origTaskNode.getIstStand(), newTaskNode.getIstStand()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setIstStand(newTaskNode.getIstStand());
                flgChange = true;
            }
            // check for Ist aufwand
            if (Calculator.compareValues(
                            origTaskNode.getIstAufwand(), newTaskNode.getIstAufwand()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setIstAufwand(newTaskNode.getIstAufwand());
                flgChange = true;
            }
            // check for Ist datestart
            if (Calculator.compareValues(
                            origTaskNode.getIstStart(), newTaskNode.getIstStart()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setIstStart(newTaskNode.getIstStart());
                flgChange = true;
            }
            // check for Ist dateend
            if (Calculator.compareValues(
                            origTaskNode.getIstEnde(), newTaskNode.getIstEnde()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setIstEnde(newTaskNode.getIstEnde());
                flgChange = true;
            }
        }
        
        // InfoNodes
        if (InfoNode.class.isInstance(origNode)) {
            InfoNode newInfoNode = (InfoNode) newNode;
            InfoNode origInfoNode = (InfoNode) origNode;

            // get state from type
            origNode.setState(origNode.getType());
            // check for DocLayoutTagCommand
            if (Calculator.compareValues(
                            origInfoNode.getDocLayoutTagCommand(), newInfoNode.getDocLayoutTagCommand()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origInfoNode.setDocLayoutTagCommand(newInfoNode.getDocLayoutTagCommand());
                flgChange = true;
            }
            // check for DocLayoutShortName
            if (Calculator.compareValues(
                            origInfoNode.getDocLayoutShortName(), newInfoNode.getDocLayoutShortName()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origInfoNode.setDocLayoutShortName(newInfoNode.getDocLayoutShortName());
                flgChange = true;
            }
            // check for DocLayoutAddStyleClass
            if (Calculator.compareValues(
                            origInfoNode.getDocLayoutAddStyleClass(), newInfoNode.getDocLayoutAddStyleClass()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origInfoNode.setDocLayoutAddStyleClass(newInfoNode.getDocLayoutAddStyleClass());
                flgChange = true;
            }
            // check for DocLayoutFlgCloseDiv
            if (Calculator.compareValues(
                            origInfoNode.getDocLayoutFlgCloseDiv(), newInfoNode.getDocLayoutFlgCloseDiv()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origInfoNode.setDocLayoutFlgCloseDiv(newInfoNode.getDocLayoutFlgCloseDiv());
                flgChange = true;
            }
        }

        // UrlResNode
        if (UrlResNode.class.isInstance(origNode)) {
            UrlResNode newUrlResNode = (UrlResNode) newNode;
            UrlResNode origUrlResNode = (UrlResNode) origNode;

            // get state from type
            origNode.setState(origNode.getType());

            // check for ResLocRef
            if (Calculator.compareValues(
                            origUrlResNode.getResLocRef(), newUrlResNode.getResLocRef())
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setResLocRef(newUrlResNode.getResLocRef());
                flgChange = true;
            }
            // check for ResLocName
            if (Calculator.compareValues(
                            origUrlResNode.getResLocName(), newUrlResNode.getResLocName()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setResLocName(newUrlResNode.getResLocName());
                flgChange = true;
            }
            // check for ResLocTags
            if (Calculator.compareValues(
                            origUrlResNode.getResLocTags(), newUrlResNode.getResLocTags()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setResLocTags(newUrlResNode.getResLocTags());
                flgChange = true;
            }
        }
        
        // SymLinkNode
        if (SymLinkNode.class.isInstance(origNode)) {
            SymLinkNode newUrlResNode = (SymLinkNode) newNode;
            SymLinkNode origUrlResNode = (SymLinkNode) origNode;

            // get state from type
            origNode.setState(origNode.getType());

            // check for ResLocRef
            if (Calculator.compareValues(
                            origUrlResNode.getSymLinkRef(), newUrlResNode.getSymLinkRef()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setSymLinkRef(newUrlResNode.getSymLinkRef());
                flgChange = true;
            }
            // check for SymLinkName
            if (Calculator.compareValues(
                            origUrlResNode.getSymLinkName(), newUrlResNode.getSymLinkName()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setSymLinkName(newUrlResNode.getSymLinkName());
                flgChange = true;
            }
            // check for SymLinkTags
            if (Calculator.compareValues(
                            origUrlResNode.getSymLinkTags(), newUrlResNode.getSymLinkTags()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setSymLinkTags(newUrlResNode.getSymLinkTags());
                flgChange = true;
            }
        }
        
        // validate
        Set<ConstraintViolation<BaseNode>> violations = newNode.validateMe();
        if (violations.size() > 0) {
            ConstraintViolationException ex = new ConstraintViolationException(
                            "error while validating newNode" + newNode.getNameForLogger(), 
                            new HashSet<ConstraintViolation<?>>(violations));
            throw ex;
        }

        return flgChange;
    }
    

    /** 
     * parse wikiSrc with an InlineWikiImporter and add it to an InfoNode named Masternode<br>
     * metaNodePraefix will be Inline, metaNodeNumber will start by 1 and increment
     * @FeatureDomain                WikiImporter
     * @FeatureResult                BaseNode - masternode with the children from wikiSrc
     * @FeatureKeywords              WikiImporter
     * @param wikiSrc                wikiSrc to parse with InlineWikiImporter
     * @return                       BaseNode - masternode with the children from wikiSrc
     * @throws Exception             ParserExceptions possible
     */
    public BaseNode parseInlineNodesFromString(final String wikiSrc) throws Exception {
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
     * @FeatureDomain                WikiImporter
     * @FeatureResult                adds children from wikiSrc to masterNode
     * @FeatureKeywords              WikiImporter
     * @param masterNode             baseNode to add the children
     * @param wikiSrc                wikiSrc to parse with InlineWikiImporter
     * @throws Exception             ParserExceptions possible
     */
    public void parseInlineNodesFromString(final BaseNode masterNode, 
                                           final String wikiSrc) throws Exception {
        
        // Parser+Options anlegen
        WikiImportOptions inputOptions = new WikiImportOptions();
        inputOptions.setFlgReadList(true);
        inputOptions.setFlgReadUe(true);
        WikiImporter wikiImporter = new InlineWikiImporter(inputOptions, "Inline");
        
        parseNodesFromWiki(wikiImporter, inputOptions, masterNode, wikiSrc);
    }

    /** 
     * parse wikiSrc with an WikiImporter and add it to the masternode
     * @FeatureDomain                WikiImporter
     * @FeatureResult                adds children from wikiSrc to masterNode
     * @FeatureKeywords              WikiImporter
     * @param masterNode             baseNode to add the children
     * @param wikiSrc                wikiSrc to parse with WikiImporter
     * @throws Exception             ParserExceptions possible
     */
    public void parseNodesFromString(final BaseNode masterNode, 
                                     final String wikiSrc) throws Exception {
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
     * @FeatureDomain                WikiImporter
     * @FeatureResult                adds children from wikiSrc to masterNode
     * @FeatureKeywords              WikiImporter
     * @param wikiImporter           wikiImporter for parsing
     * @param inputOptions           importOptions for wikiImporter
     * @param masterNode             baseNode to add the children
     * @param wikiSrc                wikiSrc to parse with WikiImporter
     * @throws Exception             ParserExceptions possible
     */
    public void parseNodesFromWiki(final WikiImporter wikiImporter, 
                                      final WikiImportOptions inputOptions,
                                      final BaseNode masterNode, 
                                      final String wikiSrc) throws Exception {
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
     * parse jsonSrc with JsonImporter and WikiImportOptions, and add it to the masternode
     * @FeatureDomain                JsonImporter
     * @FeatureResult                adds children from jsonSrc to masterNode
     * @FeatureKeywords              JsonImporter
     * @param inputOptions           importOptions for wikiImporter
     * @param masterNode             baseNode to add the children
     * @param jsonSrc                jsonSrc to parse with JsonImporter
     * @throws Exception             ParserExceptions possible
     */
    public void parseNodesFromJson(final WikiImportOptions inputOptions,
                                      final BaseNode masterNode, 
                                      final String jsonSrc) throws Exception {
        // extract 
        JSONFullImporter jsonImporter = new JSONFullImporter(inputOptions);
        JSONResponse response = jsonImporter.parseJSONResponse(jsonSrc);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("parse Response:" + response);
        }
        BaseNode baseNode = (BaseNode) response.getNode();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("parse Response.node:" + baseNode);
        }
        if ("MasterplanMasternode1".equals(baseNode.getSysUID()) 
            || masterNode.getSysUID().equals(baseNode.getSysUID())) {
            // dont import masternode and parents twice: copy children
            for (BaseNode childNode : baseNode.getChildNodes()) {
                childNode.setParentNode(masterNode);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("add Child:" + childNode.getNameForLogger());
                }
            }
        } else {
            // add masternode
            baseNode.setParentNode(masterNode);
        }
        LOGGER.info("masterNode after json:" + masterNode.getBaseNodeService().visualizeNodeHierarchy("", masterNode));
        //if inoutOptions resetSysUID(masterNode, true);
    }
    
    /** 
     * parse jsonSrc with JsonImporter, export as wiki and reimport from wiki to ensure valid syntax, 
     * and add it to the masternode
     * @FeatureDomain                JsonImporter
     * @FeatureResult                adds children from jsonSrc to masterNode
     * @FeatureKeywords              JsonImporter
     * @param inputOptions           importOptions for wikiImporter
     * @param masterNode             baseNode to add the children
     * @param jsonSrc                jsonSrc to parse with JsonImporter
     * @throws Exception             ParserExceptions possible
     */
    public void parseValidatedNodesFromJson(final WikiImportOptions inputOptions,
                                    final BaseNode masterNode, 
                                    final String jsonSrc) throws Exception {
        // parse json to dummy-masternode
        BaseNode tmpMasterNode = 
             createTemporaryMasternode("dummy", masterNode.getMetaNodePraefix(), masterNode.getMetaNodeNummer());
        this.parseNodesFromJson(inputOptions, tmpMasterNode, jsonSrc);
        
        // export as wiki
        Exporter exporter = new WikiExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        String wikiSrc = exporter.getMasterNodeResult(tmpMasterNode, oOptions);
        
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
    
    /** 
     * create a masternode for import (temporary)
     * @FeatureDomain                Importer
     * @FeatureResult                returns BaseNode
     * @FeatureKeywords              Importer
     * @param sysUID                 sysUID
     * @param nodePraefix            metaNodePraefix
     * @param nodeNummer             metaNodeNumber
     * @return                       the Basenode to use as masternode 
     */
    public BaseNode createTemporaryMasternode(final String sysUID, final String nodePraefix, final String nodeNummer) {
        BaseNode tmpMasterNode = new BaseNode();
        tmpMasterNode.setSysUID(sysUID);
        tmpMasterNode.setMetaNodePraefix(nodePraefix);
        tmpMasterNode.setMetaNodeNummer(nodeNummer);
        tmpMasterNode.setEbene(0);
        return tmpMasterNode;
    }

    public void resetSysUID(final BaseNode node, final boolean childrenOnly) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("resetSysUID:" + node.getNameForLogger());
        }
        if (!childrenOnly) {
            node.setSysUID(null);
        }
        for (BaseNode childNode : node.getChildNodes()) {
            resetSysUID(childNode, false);
        }
    }
    
}
