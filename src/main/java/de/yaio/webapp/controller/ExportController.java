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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.EmptyOutputOptionsImpl;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.extension.datatransfer.csv.CSVExporter;
import de.yaio.extension.datatransfer.excel.ExcelExporter;
import de.yaio.extension.datatransfer.excel.ExcelOutputOptions;
import de.yaio.extension.datatransfer.html.HtmlExporter;
import de.yaio.extension.datatransfer.ical.ICalExporter;
import de.yaio.extension.datatransfer.mindmap.MindMapExporter;
import de.yaio.extension.datatransfer.ppl.PPLExporter;
import de.yaio.extension.datatransfer.wiki.WikiExporter;

/**
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     controller with Download-Services to export BaseNodes in different 
 *     formats (wiki, excel..)
 *      
 * @package de.yaio.webapp.controller
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Controller
@RequestMapping("/exports")
public class ExportController {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(ExportController.class);
    
    /** replaceent to do after processing a node in documentation-context **/
    public static final Map<String, String> PostProcessorReplacements_documentation = 
                    new LinkedHashMap<String, String>();
    
    
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
        BaseNode node = BaseNode.findBaseNode(sysUID);
        String res = "";
        if (node != null) {
            // read all childnodes
            node.initChildNodesFromDB(-1);
            
            // export node with exporter
            try {
                // renew oOptions
                oOptions.initFilterMaps();
                
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("run export with oOptions=" + oOptions);
                }
                res = exporter.getMasterNodeResult(node, oOptions);
            } catch (Exception e) {
                LOGGER.error("error while export of node:" + sysUID 
                                + " with:" + exporter.getClass().getName(), e);
                e.printStackTrace();
            }
        }

        // set headers to force download
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", 
                        "attachment; filename=" + sysUID + extension);
        
        // TODO FIXME: a awful hack
        response.setCharacterEncoding(StandardCharsets.ISO_8859_1.name());
//        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in wiki-format with all children<br>
     *     use the settings of the default-output-options
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
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/wiki/{sysUID}", 
                    produces = "application/wiki")
    public @ResponseBody String exportNodeAsWiki(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
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
     *     Request to read the node for sysUID and return it in wiki-format with all children<br>
     *     use the setting of the output-options from request<br>
     *     requires an post-form application/x-www-form-urlencoded
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - wiki-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param oOptions - the outputOptions 
     * @param response - the response-Obj to set contenttype and headers
     * @return String - wiki-format of the node
     */
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/wikiuseoptions/{sysUID}", 
                    produces = "application/wiki",
                    consumes = "application/x-www-form-urlencoded")
    public @ResponseBody String exportNodeAsWiki(
           @PathVariable(value = "sysUID") final String sysUID,
           @ModelAttribute final EmptyOutputOptionsImpl oOptions,
           final HttpServletResponse response) {
        // configure
        Exporter exporter = new WikiExporter();
        
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
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/mindmap/{sysUID}", 
                    produces = "application/mindmap")
    public @ResponseBody String exportNodeAsMindMap(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
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
     *     Request to read the node for sysUID and return it in mindmap-format with all children<br>
     *     use the setting of the output-options from request<br>
     *     requires an post-form application/x-www-form-urlencoded
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - mindmap-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param oOptions - the outputOptions 
     * @param response - the response-Obj to set contenttype and headers
     * @return String - mindmap-format of the node
     */
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/mindmapuseoptions/{sysUID}", 
                    produces = "application/mindmap",
                    consumes = "application/x-www-form-urlencoded")
    public @ResponseBody String exportNodeAsMindMap(
           @PathVariable(value = "sysUID") final String sysUID,
           @ModelAttribute final EmptyOutputOptionsImpl oOptions,
           final HttpServletResponse response) {
        // configure
        Exporter exporter = new MindMapExporter();
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".mm", response);
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in csv-format with all children
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - csv-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param response - the response-Obj to set contenttype and headers
     * @return String - csv-format of the node
     */
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/csv/{sysUID}", 
                    produces = "application/csv")
    public @ResponseBody String exportNodeAsCsv(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
        // configure
        Exporter exporter = new CSVExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".csv", response);
        return res;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in csv-format with all children<br>
     *     use the setting of the output-options from request<br>
     *     requires an post-form application/x-www-form-urlencoded
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - csv-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param oOptions - the outputOptions 
     * @param response - the response-Obj to set contenttype and headers
     * @return String - csv-format of the node
     */
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/csvuseoptions/{sysUID}", 
                    produces = "application/csv",
                    consumes = "application/x-www-form-urlencoded")
    public @ResponseBody String exportNodeAsCsv(
           @PathVariable(value = "sysUID") final String sysUID,
           @ModelAttribute final EmptyOutputOptionsImpl oOptions,
           final HttpServletResponse response) {
        // configure
        Exporter exporter = new CSVExporter();
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".csv", response);
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in ppl-format with all children
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - ppl-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param response - the response-Obj to set contenttype and headers
     * @return String - ppl-format of the node
     */
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/ppl/{sysUID}", 
                    produces = "application/ppl")
    public @ResponseBody String exportNodeAsPpl(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
        // configure
        Exporter exporter = new PPLExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".ppl", response);
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in ppl-format with all children<br>
     *     use the setting of the output-options from request<br>
     *     requires an post-form application/x-www-form-urlencoded
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - ppl-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param oOptions - the outputOptions 
     * @param response - the response-Obj to set contenttype and headers
     * @return String - ppl-format of the node
     */
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/ppluseoptions/{sysUID}", 
                    produces = "application/ppl",
                    consumes = "application/x-www-form-urlencoded")
    public @ResponseBody String exportNodeAsPpl(
           @PathVariable(value = "sysUID") final String sysUID,
           @ModelAttribute final EmptyOutputOptionsImpl oOptions,
           final HttpServletResponse response) {
        // configure
        Exporter exporter = new PPLExporter();
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".ppl", response);
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
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/ical/{sysUID}", 
                    produces = "application/ical")
    public @ResponseBody String exportNodeAsICal(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
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
     *     Request to read all Events after node=sysUID and return it and all 
     *     children that matches className=EventNode in ICal-format 
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
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/icalevents/{sysUID}", 
                    produces = "application/ical")
    public @ResponseBody String exportNodeAsICalOnlyEvents(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
        // configure
        Exporter exporter = new ICalExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        oOptions.setStrClassFilter("EventNode");
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".ics", response);
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read all Tasks after node=sysUID and return it and all 
     *     children that matches className=TaskNode in ICal-format 
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
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/icaltasks/{sysUID}", 
                    produces = "application/ical")
    public @ResponseBody String exportNodeAsICalOnlyTasks(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
        // configure
        Exporter exporter = new ICalExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        oOptions.setStrClassFilter("TaskNode");
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".ics", response);
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read all Open Tasks after node=sysUID and return it and 
     *     all children that matches className=TaskNode and 
     *     type=OFFEN,RUNNING,LATE,WARNING in ICal-format 
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
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/icaltaskstodo/{sysUID}", 
                    produces = "application/ical")
    public @ResponseBody String exportNodeAsICalOnlyTasksTodo(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
        // configure
        Exporter exporter = new ICalExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        oOptions.setStrClassFilter("TaskNode");;
        oOptions.setStrTypeFilter("OFFEN,RUNNING,LATE,WARNING");;
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".ics", response);
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read all Late Tasks after node=sysUID and return it and 
     *     all children that matches className=TaskNode and type=LATE,WARNING 
     *     in ICal-format 
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
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/icaltaskslate/{sysUID}", 
                    produces = "application/ical")
    public @ResponseBody String exportNodeAsICalOnlyTasksLate(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
        // configure
        Exporter exporter = new ICalExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        oOptions.setStrClassFilter("TaskNode");;
        oOptions.setStrTypeFilter("LATE,WARNING");;
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".ics", response);
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in ical-format with all children<br>
     *     use the setting of the output-options from request<br>
     *     requires an post-form application/x-www-form-urlencoded
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - ical-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param oOptions - the outputOptions 
     * @param response - the response-Obj to set contenttype and headers
     * @return String - ical-format of the node
     */
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/icaluseoptions/{sysUID}", 
                    produces = "application/ical",
                    consumes = "application/x-www-form-urlencoded")
    public @ResponseBody String exportNodeAsICal(
           @PathVariable(value = "sysUID") final String sysUID,
           @ModelAttribute final EmptyOutputOptionsImpl oOptions,
           final HttpServletResponse response) {
        // configure
        Exporter exporter = new ICalExporter();
        
        // run
        String res = this.exportNode(sysUID, exporter, oOptions, ".ics", response);
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
     * @param pHeaderFile - path to headerFile-resource (if null=defaultfile will used; if empfty=ignored)
     * @param pFooterFile - path to footerFile-resource (if null=defaultfile will used; if empfty=ignored)
     * @return String - html-format of the node
     */
    public String commonExportNodeAsHtml(final String sysUID,
                                         final OutputOptions oOptions,
                                         final HttpServletResponse response,
                                         final String pHeaderFile,
                                         final String pFooterFile) {
        Exporter exporter = new HtmlExporter();
        String res = null;
        String headerFile = pHeaderFile;
        String footerFile = pFooterFile;
        
        // check headerFile
        if (headerFile == null) {
            headerFile = "/static/html/projektplan-export-header.html";
        }
        if (footerFile == null) {
            footerFile = "/static/html/projektplan-export-footer.html";
        }
        try {
            // run export
            res = this.exportNode(sysUID, exporter, oOptions, ".html", response);
            
            if (headerFile != "" && footerFile != "") {
                // read header
                InputStream in = this.getClass().getResourceAsStream(headerFile);
                res = IOUtils.toString(in) + res;

                // add footer
                in = this.getClass().getResourceAsStream(footerFile);
                res = res + IOUtils.toString(in);
            }
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
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/html/{sysUID}", 
                    produces = "text/html")
    public @ResponseBody String exportNodeAsHtml(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
        // configure
        OutputOptions oOptions = new OutputOptionsImpl();
        return this.commonExportNodeAsHtml(sysUID, oOptions, response, null, null);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in layout-html-format 
     *     with all children, but without header/footer-file.
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
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/htmllayoutfragment/{sysUID}", 
                    produces = "text/html")
    public @ResponseBody String exportNodeAsHtmlFragment(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
        // configure
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setFlgProcessDocLayout(true);
        oOptions.setFlgProcessMarkdown(true);
        oOptions.setMaxUeEbene(-1);
        return this.commonExportNodeAsHtml(sysUID, oOptions, response, "", "");
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in layout-html-format 
     *     with all children, but without header/footer-file.
     *     replaces all /documentation/-urls to /yaio-explorerapp/yaio-explorerapp.html#/frontpage/
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
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/htmlfrontpagefragment/{sysUID}", 
                    produces = "text/html")
    public @ResponseBody String exportNodeAsHtmlFrontpageFragment(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
        // configure
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setFlgProcessDocLayout(true);
        oOptions.setFlgProcessMarkdown(true);
        oOptions.setMaxUeEbene(-1);
        String res = this.commonExportNodeAsHtml(sysUID, oOptions, response, "", "");
        
        // replace urls to frontpage
        res = res.replaceAll("\"/exports/documentation/", 
                             "/yaio-explorerapp/yaio-explorerapp.html#/frontpage/");
        
        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in html-format with all children<br>
     *     use the setting of the output-options from request<br>
     *     requires an post-form application/x-www-form-urlencoded
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - html-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param oOptions - the outputOptions 
     * @param response - the response-Obj to set contenttype and headers
     * @return String - html-format of the node
     */
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/htmluseoptions/{sysUID}", 
                    produces = "text/html",
                    consumes = "application/x-www-form-urlencoded")
    public @ResponseBody String exportNodeAsHtml(
           @PathVariable(value = "sysUID") final String sysUID,
           @ModelAttribute final EmptyOutputOptionsImpl oOptions,
           final HttpServletResponse response) {
        return this.commonExportNodeAsHtml(sysUID, oOptions, response, null, null);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in documentation-html-format with all children
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - documentation-html-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param response - the response-Obj to set contenttype and headers
     * @return String - documentation-html-format of the node
     */
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/documentation/{sysUID}", 
                    produces = "text/html")
    public @ResponseBody String exportDocumentationNodeAsHtml(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
        // set Options
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setAllFlgShow(false);
        oOptions.setFlgProcessDocLayout(true);
        oOptions.setFlgProcessMarkdown(true);
        oOptions.setFlgReEscapeDesc(true);
        oOptions.setFlgShowDesc(true);
        oOptions.setFlgShowName(true);
        oOptions.setMaxUeEbene(-1);
        
        // generate
        String res = this.commonExportNodeAsHtml(sysUID, oOptions, response, 
                        "/static/html/documentation-export-header.html", 
                        "/static/html/documentation-export-footer.html");
        // replace static pathes...
        for (String pattern : PostProcessorReplacements_documentation.keySet()) {
            res = res.replace(pattern, PostProcessorReplacements_documentation.get(pattern));
        }
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
     * @param oOptions - the outputOptions
     * @param response - the response-Obj to set contenttype and headers
     * @return ByteArrayOutputStream - excel-format of the node
     */
    public String commonExportNodeAsExcel(final String sysUID, 
                                          final ExcelOutputOptions oOptions,
                                          final HttpServletResponse response) {
        ServletOutputStream out;
        HSSFWorkbook wb;

        // configure
        ExcelExporter exporter = new ExcelExporter();

        // find a specific node
        BaseNode node = BaseNode.findBaseNode(sysUID);

        if (node != null) {
            // read all childnodes
            node.initChildNodesFromDB(-1);
            
            // export node with exporter
            try {
                // renew oOptions
                oOptions.initFilterMaps();

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
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/excel/{sysUID}", 
                    produces = "application/excel")
    public @ResponseBody String exportNodeAsExcel(
           @PathVariable(value = "sysUID") final String sysUID, final HttpServletResponse response) {
        ExcelOutputOptions oOptions = new ExcelOutputOptions(new OutputOptionsImpl());
        return this.commonExportNodeAsExcel(sysUID, oOptions, response);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it in html-format with all children<br>
     *     use the setting of the output-options from request<br>
     *     requires an post-form application/x-www-form-urlencoded
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - html-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to export
     * @param oOptions - the outputOptions 
     * @param response - the response-Obj to set contenttype and headers
     * @return String - html-format of the node
     */
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/exceluseoptions/{sysUID}", 
                    produces = "application/excel",
                    consumes = "application/x-www-form-urlencoded")
    public @ResponseBody String exportNodeAsExcel(
           @PathVariable(value = "sysUID") final String sysUID,
           @ModelAttribute final ExcelOutputOptions oOptions,
           final HttpServletResponse response) {
        return this.commonExportNodeAsExcel(sysUID, oOptions, response);
    }
}
