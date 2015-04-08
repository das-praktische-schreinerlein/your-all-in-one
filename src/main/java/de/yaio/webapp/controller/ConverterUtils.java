/**
 * <h4>FeatureDomain:</h4>
 *     Collaboration
 *
 * <h4>FeatureDescription:</h4>
 *     software for projectmanagement and documentation
 * 
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.webapp.controller;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.extension.datatransfer.html.HtmlExporter;
import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.extension.datatransfer.wiki.InlineWikiImporter;
import de.yaio.extension.datatransfer.wiki.WikiImportOptions;
import de.yaio.extension.datatransfer.wiki.WikiImporter;
import de.yaio.extension.datatransfer.wiki.WikiImporter.WikiStructLine;

/**
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     Services to parse text to nodes and convert them in different 
 *     formats (wiki, ppl, excel..)
 *      
 * @package de.yaio.webapp.controller
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Service
public class ConverterUtils {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(ExportController.class);

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     read the node and use the exporter to convert it with all children to 
     *     exporter-format<br>
     *     set headers (contentype, disposition) on the response-obj
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - wiki-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Export
     * @param sysUID - sysUID to export
     * @param exporter - the exporter to use
     * @param oOptions - the outputoptions for the exporter
     * @param extension - the fileextension for the Content-Disposition
     * @param response - the response-Obj to set contenttype and headers
     * @return String - export-format of the node
     */
    public String exportNode(final String sysUID, final Exporter exporter, 
                             final OutputOptions oOptions, final String extension,
                             final HttpServletResponse response) {
        // find a specific node
        try {
            BaseNode node = BaseNode.findBaseNode(sysUID);
            if (node != null) {
                node.initChildNodesFromDB(-1);
            }
            
            return exportNode(node, exporter, oOptions, extension, response);
        } catch (Exception ex) {
            LOGGER.error("error while exporting sysUID:" + sysUID, ex);
            return "";
        }
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     use the exporter to convert it with all children to 
     *     exporter-format<br>
     *     set headers (contentype, disposition) on the response-obj
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - wiki-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Export
     * @param node - node to export
     * @param exporter - the exporter to use
     * @param oOptions - the outputoptions for the exporter
     * @param extension - the fileextension for the Content-Disposition
     * @param response - the response-Obj to set contenttype and headers
     * @return String - export-format of the node
     * @throws Exception - possible Exceptions
     */
    public String exportNode(final BaseNode node, final Exporter exporter, 
                             final OutputOptions oOptions, final String extension,
                             final HttpServletResponse response) throws Exception {
        // find a specific node
        String res = "";
        if (node != null) {
            // export node with exporter
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("run export with oOptions=" + oOptions);
            }
            res = exporter.getMasterNodeResult(node, oOptions);
            //                    res = res.getBytes(StandardCharsets.UTF_8.name()).toString();
        }

        // set headers to force download
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", 
                        "attachment; filename=converted" + extension);
        
        // TODO FIXME: a awful hack
        response.setCharacterEncoding("ISO-8859-15");
//        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     read the node for sysUID and return it in html-format with all children<br>
     *     use the setting of the output-options and set contect-header on response-obj
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - html-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param oOptions - the outputOptions 
     * @param response - the response-Obj to set contenttype and headers
     * @param pHeaderFile - path to headerFile-resource (if null=defaultfile will used; if empty=ignored)
     * @param pFooterFile - path to footerFile-resource (if null=defaultfile will used; if empty=ignored)
     * @return String - html-format of the node
     */
    public String commonExportNodeAsHtml(final String sysUID,
                                         final OutputOptions oOptions,
                                         final HttpServletResponse response,
                                         final String pHeaderFile,
                                         final String pFooterFile) {
        try {
            // read node
            BaseNode node = BaseNode.findBaseNode(sysUID);
            if (node != null) {
                node.initChildNodesFromDB(-1);
            }
            
            return commonExportNodeAsHtml(node, oOptions, response, pHeaderFile, pFooterFile);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     read the node for sysUID and return it in html-format with all children<br>
     *     use the setting of the output-options and set contect-header on response-obj
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - html-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param basenode - basenode to export
     * @param oOptions - the outputOptions 
     * @param response - the response-Obj to set contenttype and headers
     * @param pHeaderFile - path to headerFile-resource (if null=defaultfile will used; if empty=ignored)
     * @param pFooterFile - path to footerFile-resource (if null=defaultfile will used; if empty=ignored)
     * @return String - html-format of the node
     * @throws Exception - IOException and Parser-Exceptions possible
     */
    public String commonExportNodeAsHtml(final BaseNode basenode,
                                         final OutputOptions oOptions,
                                         final HttpServletResponse response,
                                         final String pHeaderFile,
                                         final String pFooterFile) throws Exception {
        Exporter exporter = new HtmlExporter();
        String res = "";
        String headerFile = pHeaderFile;
        String footerFile = pFooterFile;
        
        // check headerFile
        if (headerFile == null) {
            headerFile = "/static/html/projektplan-export-header.html";
        }
        if (footerFile == null) {
            footerFile = "/static/html/projektplan-export-footer.html";
        }
        // run export
        res = this.exportNode(basenode, exporter, oOptions, ".html", response);
        if (headerFile != "" && footerFile != "") {
            // read header
            InputStream in = this.getClass().getResourceAsStream(headerFile);
            res = IOUtils.toString(in) + res;

            // add footer
            in = this.getClass().getResourceAsStream(footerFile);
            res = res + IOUtils.toString(in);
        }

        // set baseref 
        String baseref = null;
        if (oOptions.isFlgUsePublicBaseRef()) {
            // set baseref to publicbaseref if outputoption is set
            baseref = System.getProperty("yaio.exportcontroller.replace.publicbaseref");
        }
        if (StringUtils.isEmpty(baseref)) {
            // set baseref to default baseref if already empty but property defined
            baseref = System.getProperty("yaio.exportcontroller.replace.baseref");
        }
        if (StringUtils.isNotEmpty(baseref)) {
            // replace inactive baseref if baseref is set
            res = res.replace("<!--<base href=\"/\" />-->", 
                              "<base href=\"" + baseref + "\" />");
        }

        // change headers to display html in browser
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "");
        
        return res;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     WikiImporter
     * <h4>FeatureDescription:</h4>
     *     parse wikiSrc with an InlineWikiImporter and add it to an InfoNode named Masternode<br>
     *     metaNodePraefix will be Inline, metaNodeNumber will start by 1 and increment
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>BaseNode - masternode with the children from wikiSrc
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     WikiImporter
     * @param wikiSrc - wikiSrc to parse with InlineWikiImporter
     * @return BaseNode - masternode with the children from wikiSrc
     * @throws Exception - ParserExceptions possible
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
     * <h4>FeatureDomain:</h4>
     *     WikiImporter
     * <h4>FeatureDescription:</h4>
     *     parse wikiSrc with an InlineWikiImporter and add it to the masternode<br>
     *     metaNodePraefix will be Inline, metaNodeNumber will start by 1 and increment
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>adds children from wikiSrc to masterNode
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     WikiImporter
     * @param masterNode - baseNode to add the children
     * @param wikiSrc - wikiSrc to parse with InlineWikiImporter
     * @throws Exception - ParserExceptions possible
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
     * <h4>FeatureDomain:</h4>
     *     WikiImporter
     * <h4>FeatureDescription:</h4>
     *     parse wikiSrc with an WikiImporter and add it to the masternode
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>adds children from wikiSrc to masterNode
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     WikiImporter
     * @param masterNode - baseNode to add the children
     * @param wikiSrc - wikiSrc to parse with WikiImporter
     * @throws Exception - ParserExceptions possible
     */
    public void parseNodesFromString(final BaseNode masterNode, 
                                     final String wikiSrc) throws Exception {
        // Parser+Options anlegen
        WikiImportOptions inputOptions = new WikiImportOptions();
        inputOptions.setFlgReadList(true);
        inputOptions.setFlgReadUe(true);
        WikiImporter wikiImporter = new WikiImporter(inputOptions);
        
        parseNodesFromWiki(wikiImporter, inputOptions, masterNode, wikiSrc);
        
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     WikiImporter
     * <h4>FeatureDescription:</h4>
     *     parse wikiSrc with WikiImporter and WikiImportOptions, and add it to the masternode
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>adds children from wikiSrc to masterNode
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     WikiImporter
     * @param wikiImporter - wikiImporter for parsing
     * @param inputOptions - importOptions for wikiImporter
     * @param masterNode - baseNode to add the children
     * @param wikiSrc - wikiSrc to parse with WikiImporter
     * @throws Exception - ParserExceptions possible
     */
    protected void parseNodesFromWiki(final WikiImporter wikiImporter, 
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
}
