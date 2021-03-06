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
package de.yaio.app.server.controller;

import de.yaio.app.config.ContextHelper;
import de.yaio.app.config.YaioConfiguration;
import de.yaio.app.core.dbservice.BaseNodeRepository;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.exporter.EmptyOutputOptionsImpl;
import de.yaio.app.datatransfer.exporter.Exporter;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.app.datatransfer.json.JSONFullExporter;
import de.yaio.app.extension.datatransfer.csv.CSVExporter;
import de.yaio.app.extension.datatransfer.excel.ExcelExporter;
import de.yaio.app.extension.datatransfer.excel.ExcelOutputOptions;
import de.yaio.app.extension.datatransfer.ical.ICalDBExporter;
import de.yaio.app.extension.datatransfer.mindmap.MindMapExporter;
import de.yaio.app.extension.datatransfer.ppl.PPLExporter;
import de.yaio.app.extension.datatransfer.wiki.WikiExporter;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

/** 
 * controller with Download-Services to export BaseNodes in different 
 * formats (wiki, excel..)
 *  
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Controller
@RequestMapping("/exports")
public class ExportController {
    
    /** API-Version **/
    public static final String API_VERSION = "1.0.0";

    @Autowired
    protected ConverterUtils converterUtils;

    @Autowired
    protected BaseNodeRepository baseNodeDBService;

    @Autowired
    private ApplicationContext appContext;

    // Logger
    private static final Logger LOGGER = Logger.getLogger(ExportController.class);

    /** 
     * Request to read the node for sysUID and return it in wiki-format with all children<br>
     * use the settings of the default-output-options
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - wiki-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/wiki/{sysUID}", 
                    produces = "application/wiki")
    public String exportNodeAsWiki(@PathVariable(value = "sysUID") final String sysUID, 
                                   final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = new WikiExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".wiki", response);
    }

    /** 
     * Request to read the node for sysUID and return it in wiki-format with all children<br>
     * use the setting of the output-options from request<br>
     * requires an post-form application/x-www-form-urlencoded
     * @param sysUID                 sysUID to export
     * @param oOptions               the outputOptions 
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - wiki-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/wikiuseoptions/{sysUID}", 
                    produces = "application/wiki",
                    consumes = "application/x-www-form-urlencoded")
    public String exportNodeAsWiki(@PathVariable(value = "sysUID") final String sysUID,
                                   @ModelAttribute final EmptyOutputOptionsImpl oOptions,
                                   final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = new WikiExporter();
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".wiki", response);
    }

    /** 
     * Request to read the node for sysUID and return it in mindmap-format with all children
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - mindmap-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/mindmap/{sysUID}", 
                    produces = "application/mindmap")
    public String exportNodeAsMindMap(@PathVariable(value = "sysUID") final String sysUID, 
                                      final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = new MindMapExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".mm", response);
    }
    /** 
     * Request to read the node for sysUID and return it in mindmap-format with all children<br>
     * use the setting of the output-options from request<br>
     * requires an post-form application/x-www-form-urlencoded
     * @param sysUID                 sysUID to export
     * @param oOptions               the outputOptions 
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - mindmap-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/mindmapuseoptions/{sysUID}", 
                    produces = "application/mindmap",
                    consumes = "application/x-www-form-urlencoded")
    public String exportNodeAsMindMap(@PathVariable(value = "sysUID") final String sysUID,
                                      @ModelAttribute final EmptyOutputOptionsImpl oOptions,
                                      final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = new MindMapExporter();
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".mm", response);
    }

    /** 
     * Request to read the node for sysUID and return it in csv-format with all children
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - csv-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/csv/{sysUID}", 
                    produces = "application/csv")
    public String exportNodeAsCsv(@PathVariable(value = "sysUID") final String sysUID, 
                                  final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = new CSVExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".csv", response);
    }
    
    /** 
     * Request to read the node for sysUID and return it in csv-format with all children<br>
     * use the setting of the output-options from request<br>
     * requires an post-form application/x-www-form-urlencoded
     * @param sysUID                 sysUID to export
     * @param oOptions               the outputOptions 
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - csv-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/csvuseoptions/{sysUID}", 
                    produces = "application/csv",
                    consumes = "application/x-www-form-urlencoded")
    public String exportNodeAsCsv(@PathVariable(value = "sysUID") final String sysUID,
                                  @ModelAttribute final EmptyOutputOptionsImpl oOptions,
                                  final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = new CSVExporter();
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".csv", response);
    }

    /** 
     * Request to read the node for sysUID and return it in json-format with all children
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - csv-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/json/{sysUID}", 
                    produces = "application/json")
    public String exportNodeAsJson(@PathVariable(value = "sysUID") final String sysUID, 
                                  final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = new JSONFullExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".json", response);
    }
    
    /** 
     * Request to read the node for sysUID and return it in ppl-format with all children
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - ppl-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/ppl/{sysUID}", 
                    produces = "application/ppl")
    public String exportNodeAsPpl(@PathVariable(value = "sysUID") final String sysUID, 
                                  final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = new PPLExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".ppl", response);
    }

    /** 
     * Request to read the node for sysUID and return it in ppl-format with all children<br>
     * use the setting of the output-options from request<br>
     * requires an post-form application/x-www-form-urlencoded
     * @param sysUID                 sysUID to export
     * @param oOptions               the outputOptions 
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - ppl-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/ppluseoptions/{sysUID}", 
                    produces = "application/ppl",
                    consumes = "application/x-www-form-urlencoded")
    public String exportNodeAsPpl(@PathVariable(value = "sysUID") final String sysUID,
                                  @ModelAttribute final EmptyOutputOptionsImpl oOptions,
                                  final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = new PPLExporter();
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".ppl", response);
    }

    /** 
     * Request to read the node for sysUID and return it in ICal-format with all children
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - ical-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/ical/{sysUID}", 
                    produces = "application/ical")
    public String exportNodeAsICal(@PathVariable(value = "sysUID") final String sysUID, 
                                   final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = getICalDBExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // exclude system-nodes
        oOptions.setStrNotNodePraefix(System.getProperty("yaio.exportcontroller.excludenodepraefix", ""));
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".ics", response);
    }

    /** 
     * Request to read all Events after node=sysUID and return it and all 
     * children that matches className=EventNode in ICal-format 
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - ical-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/icalevents/{sysUID}", 
                    produces = "application/ical")
    public String exportNodeAsICalOnlyEvents(@PathVariable(value = "sysUID") final String sysUID, 
                                             final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = getICalDBExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // exclude system-nodes
        oOptions.setStrNotNodePraefix(System.getProperty("yaio.exportcontroller.excludenodepraefix", ""));
        oOptions.setStrClassFilter("EventNode");
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".ics", response);
    }

    /** 
     * Request to read all Tasks after node=sysUID and return it and all 
     * children that matches className=TaskNode in ICal-format 
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - ical-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/icaltasks/{sysUID}", 
                    produces = "application/ical")
    public String exportNodeAsICalOnlyTasks(@PathVariable(value = "sysUID") final String sysUID, 
                                            final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = getICalDBExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // exclude system-nodes
        oOptions.setStrNotNodePraefix(System.getProperty("yaio.exportcontroller.excludenodepraefix", ""));
        oOptions.setStrClassFilter("TaskNode");
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".ics", response);
    }

    /** 
     * Request to read all Open Tasks after node=sysUID and return it and 
     * all children that matches className=TaskNode and 
     * type=OFFEN,RUNNING,LATE,WARNING in ICal-format 
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - ical-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/icaltaskstodo/{sysUID}", 
                    produces = "application/ical")
    public String exportNodeAsICalOnlyTasksTodo(@PathVariable(value = "sysUID") final String sysUID, 
                                                final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = getICalDBExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // exclude system-nodes
        oOptions.setStrNotNodePraefix(System.getProperty("yaio.exportcontroller.excludenodepraefix", ""));
        oOptions.setStrClassFilter("TaskNode");
        oOptions.setStrTypeFilter("OFFEN,RUNNING,LATE,WARNING");
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".ics", response);
    }

    /** 
     * Request to read all Late Tasks after node=sysUID and return it and 
     * all children that matches className=TaskNode and type=LATE,WARNING 
     * in ICal-format 
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - ical-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/icaltaskslate/{sysUID}", 
                    produces = "application/ical")
    public String exportNodeAsICalOnlyTasksLate(@PathVariable(value = "sysUID") final String sysUID, 
                                                final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = getICalDBExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        
        // exclude system-nodes
        oOptions.setStrNotNodePraefix(System.getProperty("yaio.exportcontroller.excludenodepraefix", ""));
        oOptions.setStrClassFilter("TaskNode");
        oOptions.setStrTypeFilter("LATE,WARNING");
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".ics", response);
    }

    /** 
     * Request to read the node for sysUID and return it in ical-format with all children<br>
     * use the setting of the output-options from request<br>
     * requires an post-form application/x-www-form-urlencoded
     * @param sysUID                 sysUID to export
     * @param oOptions               the outputOptions 
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - ical-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/icaluseoptions/{sysUID}", 
                    produces = "application/ical",
                    consumes = "application/x-www-form-urlencoded")
    public String exportNodeAsICal(@PathVariable(value = "sysUID") final String sysUID,
                                   @ModelAttribute final EmptyOutputOptionsImpl oOptions,
                                   final HttpServletResponse response) throws ConverterException {
        // configure
        Exporter exporter = getICalDBExporter();
        
        // run
        return converterUtils.exportNode(sysUID, exporter, oOptions, ".ics", response);
    }
    
    
    
    /** 
     * Request to read the node for sysUID and return it in html-format with all children
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - html-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/html/{sysUID}", 
                    produces = "text/html")
    public String exportNodeAsHtml(@PathVariable(value = "sysUID") final String sysUID, 
                                   final HttpServletResponse response) throws ConverterException {
        // configure
        OutputOptions oOptions = new OutputOptionsImpl();
        return converterUtils.commonExportNodeAsHtml(sysUID, oOptions, response, null);
    }

    /** 
     * Request to read the node for sysUID and return it in layout-html-format 
     * with all children, but without header/footer-file.
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - html-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/htmllayoutfragment/{sysUID}", 
                    produces = "text/html")
    public String exportNodeAsHtmlFragment(@PathVariable(value = "sysUID") final String sysUID, 
                                           final HttpServletResponse response) throws ConverterException {
        // configure
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setFlgProcessDocLayout(true);
        oOptions.setMaxUeEbene(-1);
        return converterUtils.commonExportNodeAsHtml(sysUID, oOptions, response, "");
    }

    /** 
     * Request to read the node for sysUID and return it in layout-html-format 
     * with all children, but without header/footer-file.
     * replaces all /documentation/-urls to /yaio-explorerapp/yaio-explorerapp.html#/frontpage/
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - html-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/htmlfrontpagefragment/{sysUID}", 
                    produces = "text/html")
    public String exportNodeAsHtmlFrontpageFragment(@PathVariable(value = "sysUID") final String sysUID, 
                                                    final HttpServletResponse response) throws ConverterException {
        // configure
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setFlgProcessDocLayout(true);
        oOptions.setMaxUeEbene(-1);
        String res = converterUtils.commonExportNodeAsHtml(sysUID, oOptions, response, "");
        
        // replace urls to frontpage
        res = res.replaceAll("\"/exports/documentation/", 
                             "/yaio-explorerapp/yaio-explorerapp.html#/frontpage/");
        
        return res;
    }

    /** 
     * Request to read the node for sysUID and return it in html-format with all children<br>
     * use the setting of the output-options from request<br>
     * requires an post-form application/x-www-form-urlencoded
     * @param sysUID                 sysUID to export
     * @param oOptions               the outputOptions 
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - html-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/htmluseoptions/{sysUID}", 
                    produces = "text/html",
                    consumes = "application/x-www-form-urlencoded")
    public String exportNodeAsHtml(@PathVariable(value = "sysUID") final String sysUID,
                                   @ModelAttribute final EmptyOutputOptionsImpl oOptions,
                                   final HttpServletResponse response) throws ConverterException {
        return converterUtils.commonExportNodeAsHtml(sysUID, oOptions, response, null);
    }

    /** 
     * Request to read the node for sysUID and return it in documentation-html-format with all children
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - documentation-html-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/documentation/{sysUID}", 
                    produces = "text/html")
    public String exportDocumentationNodeAsHtml(@PathVariable(value = "sysUID") final String sysUID, 
                                                final HttpServletResponse response) throws ConverterException {
        // set Options
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setAllFlgShow(false);
        oOptions.setFlgProcessDocLayout(true);
        oOptions.setFlgReEscapeDesc(true);
        oOptions.setFlgShowDesc(true);
        oOptions.setFlgShowName(true);
        oOptions.setMaxUeEbene(-1);
        
        // generate
        String res = converterUtils.commonExportNodeAsHtml(sysUID, oOptions, response, 
                        "/static/exporttemplates/documentation-export.html");
        // replace static pathes...
        Map<String, String> replacements = YaioConfiguration.getInstance().getPostProcessorReplacements();
        for (String pattern : replacements.keySet()) {
            res = res.replace(pattern, replacements.get(pattern));
        }
        return res;
    }

    /** 
     * Request to read the node for sysUID and return it in yaioOfflineApp-html-format with all children
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - yaioOfflineApp-html-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/yaioapp/{sysUID}", 
                    produces = "text/html")
    public String exportNodeAsYaioApp(@PathVariable(value = "sysUID") final String sysUID, 
                                                final HttpServletResponse response) throws ConverterException {
        String res = converterUtils.commonExportNodeAsYaioApp(sysUID, response, null, true);
        // replace static pathes...
        Map<String, String> replacements = YaioConfiguration.getInstance().getPostProcessorReplacements();
        for (String pattern : replacements.keySet()) {
            res = res.replace(pattern, replacements.get(pattern));
        }
        
        return res;
    }

    /**
     * Request to read the node for sysUID and return it in excel-format with all children
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @return                       ByteArrayOutputStream - excel-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/excel/{sysUID}", 
                    produces = "application/excel")
    public String exportNodeAsExcel(@PathVariable(value = "sysUID") final String sysUID, 
                                    final HttpServletResponse response) throws ConverterException {
        ExcelOutputOptions oOptions = new ExcelOutputOptions(new OutputOptionsImpl());
        return this.commonExportNodeAsExcel(sysUID, oOptions, response);
    }

    /** 
     * Request to read the node for sysUID and return it in html-format with all children<br>
     * use the setting of the output-options from request<br>
     * requires an post-form application/x-www-form-urlencoded
     * @param sysUID                 sysUID to export
     * @param oOptions               the outputOptions 
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - html-format of the node
     * @throws ConverterException    if something went wrong
     */
    @ResponseBody
    @RequestMapping(method = {RequestMethod.POST}, 
                    value = "/exceluseoptions/{sysUID}", 
                    produces = "application/excel",
                    consumes = "application/x-www-form-urlencoded")
    public String exportNodeAsExcel(@PathVariable(value = "sysUID") final String sysUID,
                                    @ModelAttribute final ExcelOutputOptions oOptions,
           final HttpServletResponse response) throws ConverterException {
        return this.commonExportNodeAsExcel(sysUID, oOptions, response);
    }

    @ExceptionHandler(ConverterException.class)
    public String handleCustomException(final HttpServletRequest request, final ConverterException e,
                                        final HttpServletResponse response) {
        LOGGER.info("ConverterException while running request:" + request.toString(), e);
        response.setStatus(SC_BAD_REQUEST);
        return "cant export node => " + e.getMessage();
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public String handleAllException(final HttpServletRequest request, final Exception e,
                                     final HttpServletResponse response) {
        LOGGER.warn("error while running request:" + request.toString(), e);
        response.setStatus(SC_INTERNAL_SERVER_ERROR);
        return "cant export node";
    }


    /**
     * Request to read the node for sysUID and return it in excel-format with all children
     * @param sysUID                 sysUID to export
     * @param oOptions               the outputOptions
     * @param response               the response-Obj to set contenttype and headers
     * @return                       ByteArrayOutputStream - excel-format of the node
     * @throws ConverterException    if something went wrong
     */
    protected String commonExportNodeAsExcel(final String sysUID,
                                             final ExcelOutputOptions oOptions,
                                             final HttpServletResponse response) throws ConverterException {
        ServletOutputStream out = null;
        HSSFWorkbook wb;

        // configure
        ExcelExporter exporter = new ExcelExporter();

        // find a specific node
        BaseNode node = baseNodeDBService.findBaseNode(sysUID);

        if (node == null) {
            throw new ConverterException("cant find node for sysUID", sysUID,
                    new IllegalArgumentException("sysUID not found"));
        }


        try {
            // read all childnodes
            node.initChildNodesFromDB(-1);

            // export node with converter
            // renew oOptions
            oOptions.initFilterMaps();

            // WorkBook erzeugen
            wb = exporter.toExcel(node, oOptions);
        }  catch (Exception ex) {
            throw new ConverterException("error while generating excel", sysUID, ex);
        }

        try {
            // set headers to force download
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename=" + sysUID + ".xls");

            // get outputstream
            out = response.getOutputStream();

            // write outputstream
            wb.write(out);
            out.close();
        }  catch (IOException ex) {
            throw new IllegalArgumentException("cant write excelexport to outputstream", ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return null;
    }

    protected ICalDBExporter getICalDBExporter() {
        ICalDBExporter exporter = new ICalDBExporter();
        ContextHelper.getInstance().autowireService(appContext, exporter);
        return exporter;
    }
}
