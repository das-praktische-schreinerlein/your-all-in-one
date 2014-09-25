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
package de.yaio.rest.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.extension.datatransfer.excel.ExcelExporter;
import de.yaio.extension.datatransfer.excel.ExcelOutputOptions;
import de.yaio.extension.datatransfer.html.HtmlExporter;
import de.yaio.extension.datatransfer.ical.ICalExporter;
import de.yaio.extension.datatransfer.mindmap.MindMapExporter;
import de.yaio.extension.datatransfer.wiki.WikiExporter;

/**
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     controller with Download-Services to export BaseNodes in different 
 *     formats (wiki, excel..)
 *      
 * @package de.yaio.rest.controller
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Controller
@RequestMapping("/exports")
public class ExportController {


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
    public String exportNode(String sysUID, Exporter exporter, 
                             OutputOptions oOptions, String extension,
                             HttpServletResponse response) {
        // find a specific node
        BaseNode node = BaseNode.findBaseNode(sysUID);
        String res = "";
        if (node != null) {
            // read all childnodes
            node.initChildNodesFromDB(-1);
            
            // export node with exporter
            try {
                res = exporter.getMasterNodeResult(node, oOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // set headers to force download
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", 
                        "attachment; filename=" + sysUID + extension);
        
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in wiki-format with all children
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - wiki-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param response - the response-Obj to set contenttype and headers
     * @return String - wiki-format of the node
     */
    @RequestMapping(method=RequestMethod.GET, value = "/wiki/{sysUID}", produces="application/wiki")
    public @ResponseBody String exportNodeAsWiki(
           @PathVariable(value="sysUID") String sysUID, HttpServletResponse response) {
        // configure
        Exporter exporter = new WikiExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".wiki", response);
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in mindmap-format with all children
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - mindmap-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param response - the response-Obj to set contenttype and headers
     * @return String - mindmap-format of the node
     */
    @RequestMapping(method=RequestMethod.GET, value = "/mindmap/{sysUID}", produces="application/mindmap")
    public @ResponseBody String exportNodeAsMindMap(
           @PathVariable(value="sysUID") String sysUID, HttpServletResponse response) {
        // configure
        Exporter exporter = new MindMapExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".mm", response);
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in ICal-format with all children
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - ICal-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param response - the response-Obj to set contenttype and headers
     * @return String - ical-format of the node
     */
    @RequestMapping(method=RequestMethod.GET, value = "/ical/{sysUID}", produces="application/ical")
    public @ResponseBody String exportNodeAsICal(
           @PathVariable(value="sysUID") String sysUID, HttpServletResponse response) {
        // configure
        Exporter exporter = new ICalExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".ics", response);
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in html-format with all children
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - html-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param response - the response-Obj to set contenttype and headers
     * @return String - html-format of the node
     */
    @RequestMapping(method=RequestMethod.GET, value = "/html/{sysUID}", produces="text/html")
    public @ResponseBody String exportNodeAsHtml(
           @PathVariable(value="sysUID") String sysUID, HttpServletResponse response) {
        // configure
        Exporter exporter = new HtmlExporter();
        OutputOptions oOptions = new OutputOptionsImpl();

        String res = null;
        try {
            // read header
            InputStream in = this.getClass().getResourceAsStream("/static/html/projektplan-export-header.html");
            res = IOUtils.toString(in);

            // run export
            res += this.exportNode(sysUID, exporter, oOptions, ".html", response);
            
            // add footer
            in = this.getClass().getResourceAsStream("/static/html/projektplan-export-footer.html");
            res = res + IOUtils.toString(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        // change headers to display html in browser
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "");
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in excel-format with all children
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - excel-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param response - the response-Obj to set contenttype and headers
     * @return ByteArrayOutputStream - excel-format of the node
     */
    @RequestMapping(method=RequestMethod.GET, value = "/excel/{sysUID}", produces="application/excel")
    public @ResponseBody String exportNodeAsExcel(
           @PathVariable(value="sysUID") String sysUID, HttpServletResponse response) {
        ServletOutputStream out;
        HSSFWorkbook wb;

        // configure
        ExcelExporter exporter = new ExcelExporter();
        ExcelOutputOptions oOptions = new ExcelOutputOptions(new OutputOptionsImpl());

        // find a specific node
        BaseNode node = BaseNode.findBaseNode(sysUID);

        if (node != null) {
            // read all childnodes
            node.initChildNodesFromDB(-1);
            
            // export node with exporter
            try {
             // WorkBook erzeugen
                wb = exporter.toExcel(node, oOptions);
                
                // set headers to force download
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", 
                                "attachment; filename=" + sysUID + ".xls");

                // get outputstream
                out = response.getOutputStream();
                
                // write outputstream
                wb.write(out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }

        return null;
    }
}