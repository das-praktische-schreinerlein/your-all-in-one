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
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.exporter.Exporter;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.app.extension.datatransfer.common.ExtendedDatatransferUtils;
import de.yaio.app.extension.datatransfer.ical.ICalDBExporter;
import de.yaio.app.extension.datatransfer.mindmap.MindMapExporter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

/** 
 * Services to parse text to nodes and convert them in different 
 * formats (wiki, ppl, excel..)
 *  
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Controller
@RequestMapping("/converters")
public class ConverterController {
    /** API-Version **/
    public static final String API_VERSION = "1.0.0";

    @Autowired
    protected ConverterUtils converterUtils;
    
    @Autowired
    protected ExtendedDatatransferUtils datatransferUtils;

    @Autowired
    private ApplicationContext appContext;

    // Logger
    private static final Logger LOGGER = Logger.getLogger(ConverterController.class);

    /**
     * parses the yaio-wiki-source to nodes and converts it to Html
     * @param source                 yaio-wiki-source to parse
     * @param response               response-obj to set encoding, headers...
     * @return                       resulting Html of the parsed yaio-wiki-source
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/html",
                    produces = "text/html")
    public String convertToIHtml(@RequestParam(value = "source") final String source, 
                                 final HttpServletResponse response) throws ParserException, ConverterException {
        OutputOptions oOptions = new OutputOptionsImpl();
        BaseNode masterNode = datatransferUtils.parseInlineNodesFromString(source);
        String tplFile = "/static/exporttemplates/projektplan-export.html";
        return converterUtils.commonExportNodeAsHtml(masterNode, oOptions, response, tplFile);
    }

    /** 
     * parses the yaio-wiki-source to nodes and converts it to ICal
     * @param source                 yaio-wiki-source to parse
     * @param response               response-obj to set encoding, headers...
     * @return                       resulting ICal of the parsed yaio-wiki-source
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/ical",
                    produces = "application/ical")
    public String convertToICal(@RequestParam(value = "source") final String source, 
                                final HttpServletResponse response) throws ParserException, ConverterException {
        Exporter exporter = getICalDBExporter();
        return commonConvertSource(exporter, ".ics", source, response);
    }

    /** 
     * parses the yaio-wiki-source to nodes and converts it to Mindmap
     * @param source                 yaio-wiki-source to parse
     * @param response               response-obj to set encoding, headers...
     * @return                       resulting Mindmap of the parsed yaio-wiki-source
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/mindmap",
                    produces = "application/mindmap")
    public String convertToMindmap(@RequestParam(value = "source") final String source, 
                                   final HttpServletResponse response) throws ParserException, ConverterException {
        Exporter exporter = new MindMapExporter();
        return commonConvertSource(exporter, ".mm", source, response);
    }

    /** 
     * parses the yaio-wiki-source to nodes and converts it with help of the converter
     * @param exporter               converter to use (mindmap, html...)
     * @param extension              file-extension
     * @param source                 yaio-wiki-source to parse 
     * @param response               response-obj to set encoding, headers...
     * @return                       Converter-result of the parsed yaio-wiki-source
     */
    private String commonConvertSource(final Exporter exporter,
                                         final String extension,
                                         final String source,
                                         final HttpServletResponse response) throws ParserException, ConverterException {
        OutputOptions oOptions = new OutputOptionsImpl();

        BaseNode masterNode = datatransferUtils.parseInlineNodesFromString(source);
        return converterUtils.exportNode(masterNode, exporter, oOptions, extension, response);
    }

    @ExceptionHandler(ParserException.class)
    public String handleCustomException(final HttpServletRequest request, final ParserException e,
                                        final HttpServletResponse response) {
        LOGGER.info("ParserException while running request:" + request.toString(), e);
        response.setStatus(SC_BAD_REQUEST);
        return "cant parse source => " + e.getMessage();
    }

    @ExceptionHandler(ConverterException.class)
    public String handleCustomException(final HttpServletRequest request, final ConverterException e,
                                        final HttpServletResponse response) {
        LOGGER.info("ConverterException while running request:" + request.toString(), e);
        response.setStatus(SC_BAD_REQUEST);
        return "cant convert parsed source => " + e.getMessage();
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public String handleAllException(final HttpServletRequest request, final Exception e,
                                     final HttpServletResponse response) {
        LOGGER.warn("error while running request:" + request.toString(), e);
        response.setStatus(SC_INTERNAL_SERVER_ERROR);
        return "cant convert source";
    }

    protected ICalDBExporter getICalDBExporter() {
        ICalDBExporter exporter = new ICalDBExporter();
        ContextHelper.getInstance().autowireService(appContext, exporter);
        return exporter;
    }
}
