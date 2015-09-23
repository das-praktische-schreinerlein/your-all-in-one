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

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.extension.datatransfer.common.DatatransferUtils;
import de.yaio.extension.datatransfer.ical.ICalDBExporter;
import de.yaio.extension.datatransfer.mindmap.MindMapExporter;
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
    /** API-Version **/
    public static final String API_VERSION = "1.0.0";

    @Autowired
    protected ConverterUtils converterUtils;
    
    @Autowired
    protected DatatransferUtils datatransferUtils;

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

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/ical",
                    produces = "application/ical")
    public String convertToICal(@RequestParam(value = "source") final String source, 
                                final HttpServletResponse response) {
        Exporter exporter = new ICalDBExporter();
        return commonComnvertSource(exporter, ".ics", source, response);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/mindmap",
                    produces = "application/mindmap")
    public String convertToMindmap(@RequestParam(value = "source") final String source, 
                                   final HttpServletResponse response) {
        Exporter exporter = new MindMapExporter();
        return commonComnvertSource(exporter, ".mm", source, response);
    }


    protected String commonComnvertSource(final Exporter exporter,
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
