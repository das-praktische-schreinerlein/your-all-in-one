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
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.extension.datatransfer.html.HtmlExporter;
import de.yaio.extension.datatransfer.ical.ICalExporter;
import de.yaio.extension.datatransfer.mindmap.MindMapExporter;
import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.extension.datatransfer.wiki.InlineWikiImporter;
import de.yaio.extension.datatransfer.wiki.WikiImportOptions;
import de.yaio.extension.datatransfer.wiki.WikiImporter;
import de.yaio.extension.datatransfer.wiki.WikiImporter.WikiStructLine;
import de.yaio.rest.controller.NodeActionResponse;

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
@Controller
@RequestMapping("/converters")
public class ConverterController {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(ExportController.class);

    @RequestMapping(method = RequestMethod.GET, 
                    value = "/html",
                    produces = "text/html")
    public @ResponseBody String convertToIHtml(
           @RequestParam(value = "source") final String source, final HttpServletResponse response) {
        try {
                Exporter exporter = new HtmlExporter();
                OutputOptions oOptions = new OutputOptionsImpl();
                
                BaseNode masterNode = parseNode(source);
                String res = this.exportNode(masterNode, exporter, oOptions, ".html", response);
                String headerFile = "/static/html/projektplan-export-header.html";
                String footerFile = "/static/html/projektplan-export-footer.html";
                // read header
                InputStream in = this.getClass().getResourceAsStream(headerFile);
                res = IOUtils.toString(in) + res;

                // add footer
                in = this.getClass().getResourceAsStream(footerFile);
                res = res + IOUtils.toString(in);

                // change headers to display html in browser
                response.setContentType("text/html");
                response.setHeader("Content-Disposition", "");
                return res;
            } catch (Exception e) {
                e.printStackTrace();
                return new NodeActionResponse(
                    "ERROR", 
                    "cant convert source => " + e, 
                    null, null, null, null).getStateMsg();
            }
    }

    @RequestMapping(method = RequestMethod.GET, 
                    value = "/ical",
                    produces = "application/ical")
    public @ResponseBody String convertToICal(
           @RequestParam(value = "source") final String source, final HttpServletResponse response) {
        try {
                Exporter exporter = new ICalExporter();
                OutputOptions oOptions = new OutputOptionsImpl();
                
                BaseNode masterNode = parseNode(source);
                String res = this.exportNode(masterNode, exporter, oOptions, ".ics", response);
                return res;

            } catch (Exception e) {
                e.printStackTrace();
                return new NodeActionResponse(
                    "ERROR", 
                    "cant convert source => " + e, 
                    null, null, null, null).getStateMsg();
            }
    }

    @RequestMapping(method = RequestMethod.GET, 
                    value = "/mindmap",
                    produces = "application/mindmap")
    public @ResponseBody String convertToMindmap(
           @RequestParam(value = "source") final String source, final HttpServletResponse response) {
        try {
                Exporter exporter = new MindMapExporter();
                OutputOptions oOptions = new OutputOptionsImpl();
                
                BaseNode masterNode = parseNode(source);
                String res = this.exportNode(masterNode, exporter, oOptions, ".mm", response);
                return res;

            } catch (Exception e) {
                e.printStackTrace();
                return new NodeActionResponse(
                    "ERROR", 
                    "cant convert source => " + e, 
                    null, null, null, null).getStateMsg();
            }
    }

    public BaseNode parseNode(final String wikiSrc) throws Exception {
        
        // Parser+Options anlegen
        WikiImportOptions inputOptions = new WikiImportOptions();
        inputOptions.setFlgReadList(true);
        inputOptions.setFlgReadUe(true);
        WikiImporter wikiImporter = new InlineWikiImporter(inputOptions, "Inline");
        
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
        
        // PPL-Importer
        PPLImporter pplImporter = new PPLImporter(null);
        // create dummy masternode
        BaseNode masterNode = (BaseNode) pplImporter.createNodeObjFromText(1, 
                        "INFO - Masternode", "INFO - Masternode", null);
        masterNode.setName("Masternode");
        masterNode.setMetaNodePraefix("Inline");
        masterNode.setMetaNodeNummer("Master");
        pplImporter.extractNodesFromLines(masterNode, pplSource, "\t");
        
        return masterNode;
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
     * @throws Exception 
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

    
}
