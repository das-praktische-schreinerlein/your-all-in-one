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
package de.yaio.webapp.controller;

import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.extension.datatransfer.common.ExtendedDatatransferUtils;
import de.yaio.extension.datatransfer.ical.ICalDBExporter;
import de.yaio.extension.datatransfer.mindmap.MindMapExporter;
import de.yaio.webapp.restcontroller.NodeActionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

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
@Controller
@RequestMapping("/converters")
public class ConverterController {
    /** API-Version **/
    public static final String API_VERSION = "1.0.0";

    @Autowired
    protected ConverterUtils converterUtils;
    
    @Autowired
    protected ExtendedDatatransferUtils datatransferUtils;

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
                                 final HttpServletResponse response) {
        try {
                OutputOptions oOptions = new OutputOptionsImpl();
                BaseNode masterNode = datatransferUtils.parseInlineNodesFromString(source);
                String tplFile = "/static/exporttemplates/projektplan-export.html";
                return converterUtils.commonExportNodeAsHtml(masterNode, oOptions, response, tplFile);
            } catch (Exception e) {
                e.printStackTrace();
                return new NodeActionResponse(
                    "ERROR", 
                    "cant convert source => " + e, 
                    null, null, null, null).getStateMsg();
            }
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
                                final HttpServletResponse response) {
        Exporter exporter = new ICalDBExporter();
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
                                   final HttpServletResponse response) {
        Exporter exporter = new MindMapExporter();
        return commonConvertSource(exporter, ".mm", source, response);
    }

    /** 
     * parses the yaio-wiki-source to nodes and converts it with help of the exporter
     * @param exporter               exporter to use (mindmap, html...)
     * @param extension              file-extension
     * @param source                 yaio-wiki-source to parse 
     * @param response               response-obj to set encoding, headers...
     * @return                       Converter-result of the parsed yaio-wiki-source
     */
    protected String commonConvertSource(final Exporter exporter,
                                         final String extension,
                                         final String source,
                                         final HttpServletResponse response) {
        try {
            OutputOptions oOptions = new OutputOptionsImpl();

            BaseNode masterNode = datatransferUtils.parseInlineNodesFromString(source);
            String res = converterUtils.exportNode(masterNode, exporter, oOptions, extension, response);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            return new NodeActionResponse(
                            "ERROR", 
                            "cant convert source => " + e, 
                            null, null, null, null).getStateMsg();
        }
    }
}
