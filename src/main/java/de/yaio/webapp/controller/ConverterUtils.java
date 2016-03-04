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

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import de.yaio.app.Configurator;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.datatransfer.json.JSONFullExporter;
import de.yaio.extension.datatransfer.html.HtmlExporter;

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
public class ConverterUtils {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(ConverterUtils.class);

    /** 
     * read the node and use the exporter to convert it with all children to 
     * exporter-format<br>
     * set headers (contentype, disposition) on the response-obj
     * @param sysUID                 sysUID to export
     * @param exporter               the exporter to use
     * @param oOptions               the outputoptions for the exporter
     * @param extension              the fileextension for the Content-Disposition
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - export-format of the node
     */
    public String exportNode(final String sysUID, final Exporter exporter, 
                             final OutputOptions oOptions, final String extension,
                             final HttpServletResponse response) {
        // find a specific node
        try {
            BaseNode node = BaseNode.findBaseNode(sysUID);
            if (node != null && !exporter.hasOwnNodeReader()) {
                node.initChildNodesFromDB(-1);
            }
            
            return exportNode(node, exporter, oOptions, extension, response);
        } catch (Exception ex) {
            LOGGER.error("error while exporting sysUID:" + sysUID, ex);
            return "";
        }
    }

    /** 
     * use the exporter to convert it with all children to 
     * exporter-format<br>
     * set headers (contentype, disposition) on the response-obj
     * @param node                   node to export
     * @param exporter               the exporter to use
     * @param oOptions               the outputoptions for the exporter
     * @param extension              the fileextension for the Content-Disposition
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - export-format of the node
     * @throws Exception             possible Exceptions
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
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        return res;
    }

    /** 
     * read the node for sysUID and return it in html-format with all children<br>
     * use the setting of the output-options and set contect-header on response-obj
     * @param sysUID                 sysUID to export
     * @param oOptions               the outputOptions 
     * @param response               the response-Obj to set contenttype and headers
     * @param pTplFile               path to tplFile-resource (if null=defaultfile will used; if empty=ignored)
     * @return                       String - html-format of the node
     */
    public String commonExportNodeAsHtml(final String sysUID,
                                         final OutputOptions oOptions,
                                         final HttpServletResponse response,
                                         final String pTplFile) {
        try {
            // read node
            BaseNode node = BaseNode.findBaseNode(sysUID);
            if (node != null) {
                node.initChildNodesFromDB(-1);
            }
            
            return commonExportNodeAsHtml(node, oOptions, response, pTplFile);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /** 
     * read the node for sysUID and return it in html-format with all children<br>
     * use the setting of the output-options and set contect-header on response-obj
     * @param basenode               basenode to export
     * @param oOptions               the outputOptions 
     * @param response               the response-Obj to set contenttype and headers
     * @param pTplFile               path to tplFile-resource (if null=defaultfile will used; if empty=ignored)
     * @return                       String - html-format of the node
     * @throws Exception             IOException and Parser-Exceptions possible
     */
    public String commonExportNodeAsHtml(final BaseNode basenode,
                                         final OutputOptions oOptions,
                                         final HttpServletResponse response,
                                         final String pTplFile) throws Exception {
        Exporter exporter = new HtmlExporter();
        String res = "";
        String tplFile = pTplFile;
        
        // check tplFile
        if (tplFile == null) {
            tplFile = "/static/exporttemplates/projektplan-export.html";
        }
        // run export
        res = this.exportNode(basenode, exporter, oOptions, ".html", response);
        if (tplFile != "") {
            // read tpl
            InputStream in = this.getClass().getResourceAsStream(tplFile);
            String content = IOUtils.toString(in);
            in.close();

            Pattern pattern = Pattern.compile("\\<\\!-- REPLACECONTENT_START --\\>.*<\\!-- REPLACECONTENT_END --\\>", 
                            Pattern.DOTALL);
            String replacement = "<!-- REPLACECONTENT_START -->" + res + "<!-- REPLACECONTENT_END -->";
            Matcher matcher = pattern.matcher(content);
            StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(buffer, "");
                buffer.append(replacement);
            }
            matcher.appendTail(buffer);
            res =  buffer.toString();
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
     * read the node for sysUID, return it in Json-format with all children<br>
     * include it into the yaioOfflineApp and set contect-header on response-obj
     * @param sysUID                 basenode to export
     * @param response               the response-Obj to set contenttype and headers
     * @param pTplFile               path to tplFile-resource (if null=defaultfile will used; if empty=ignored)
     * @param flgExport              if set it will be prepared as export (baseref will be set and static is default datasource)
     * @return                       String - html-format of the node
     * @throws Exception             IOException and Parser-Exceptions possible
     */
    public String commonExportNodeAsYaioApp(final String sysUID,
                                         final HttpServletResponse response,
                                         final String pTplFile,
                                         final boolean flgExport) {
        // configure
        Exporter exporter = new JSONFullExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        String tplFile = pTplFile;
        if (tplFile == null) {
            tplFile = "/static/yaio-explorerapp/yaio-explorerapp.html";
        }

        // run export
        String res = "{ state: \"OK\","
                   + " stateMsg: \"node found\","
                   + " node: { sysUID: \"Dummynode\", name: \"Dummynode\", className:\"TaskNode\", childNodes: []},"
                   + " parentIdHierarchy: [],"
                   + " childNodes: []}";
        
        List<String> addResBaseUrls = new ArrayList<String>();
        try {
            
            if (!StringUtils.isEmpty(sysUID)) {
                res = this.exportNode(sysUID, exporter, oOptions, ".json", response);
            }

            if (tplFile != "") {
                // read tpl
                InputStream in = this.getClass().getResourceAsStream(tplFile);
                String content = IOUtils.toString(in);
                in.close();

                // Include static json
                Pattern pattern = Pattern.compile("\\<\\!-- INCLUDESTATICJSON_SNIP --\\>.*\\<\\!-- INCLUDESTATICJSON_SNAP --\\>", Pattern.DOTALL);
                String replacement = "<!-- INCLUDESTATICJSON_SNIP -->\n"
                                + "<script type=\"text/javascript\">\n" 
                                + "window.yaioUseStaticJson = " + (flgExport ? "true" : " false") + ";\n"
                                + "window.yaioStaticJSON = " + res.replaceAll("/", "\\\\/") + ";\n"
                                + "</script>\n"
                                + "<!-- INCLUDESTATICJSON_SNAP -->\n";
                Matcher matcher = pattern.matcher(content);
                StringBuffer buffer = new StringBuffer();
                while (matcher.find()) {
                    matcher.appendReplacement(buffer, "");
                    buffer.append(replacement);
                }
                matcher.appendTail(buffer);
                content =  buffer.toString();

                // include yaioinstances
                res = "";
                for (String name : Configurator.getInstance().getKnownYaioInstances().keySet()) {
                    Map<String, String> yaioInstance = Configurator.getInstance().getKnownYaioInstances().get(name);
                    String url = yaioInstance.get(Configurator.CONST_PROPNAME_YAIOINSTANCES_URL);
                    String desc = yaioInstance.get(Configurator.CONST_PROPNAME_YAIOINSTANCES_DESC);
                    res += "// add " + url + "\n";
                    res += "yaioAppBase.configureService('Yaio.ServerNodeDBDriver_" + url + "', function() { return Yaio.ServerNodeDBDriver(yaioAppBase, Yaio.ServerNodeDBDriverConfig('" + url + "', '" + name + "',  '" + desc + "')); });\n";
                    res += "yaioAppBase.configureService('YaioServerNodeDBDriver_" + url + "', function() { return yaioAppBase.get('Yaio.ServerNodeDBDriver_" + url + "'); });\n";
                    res += "yaioAppBase.config.datasources.push('YaioServerNodeDBDriver_" + url + "');\n";
                    res += "yaioAppBase.get('YaioDataSourceManager').addConnection('YaioServerNodeDBDriver_" + url + "', function () { return yaioAppBase.get('YaioServerNodeDBDriver_" + url + "'); });\n" ;
                    addResBaseUrls.add(url);
                }
                pattern = Pattern.compile("\\/\\/ CONFIGUREDATASOURCES_SNIP.*\\/\\/ CONFIGUREDATASOURCES_SNAP", Pattern.DOTALL);
                replacement = "// CONFIGUREDATASOURCES_SNIP\n"
                                + "\n" + res + "\n"
                                + "yaioAppBase.configureService('Yaio.StaticNodeDBDriver', function() { return Yaio.StaticNodeDBDriver(yaioAppBase, Yaio.StaticNodeDBDriverConfig('', 'Statische InApp-Daten für \"" + sysUID + "\"', 'Die statisch in der App hinterlegten Daten werden geladen.')); });\n"
                                + "yaioAppBase.config.datasources.push('YaioStaticNodeDBDriver');\n" 
                                + "yaioAppBase.get('YaioDataSourceManager').addConnection('YaioStaticNodeDBDriver', function () { return yaioAppBase.get('YaioStaticNodeDBDriver'); });\n"
                                + "yaioAppBase.config.datasources.push('YaioFileNodeDBDriver');\n" 
                                + "yaioAppBase.get('YaioDataSourceManager').addConnection('YaioFileNodeDBDriver', function () { return yaioAppBase.get('YaioFileNodeDBDriver'); });\n"
                                // skip localhost-server on export 
                                + (!flgExport ? "yaioAppBase.config.datasources.push('YaioServerNodeDBDriver_Local');\n" : "") 
                                + (!flgExport ? "yaioAppBase.get('YaioDataSourceManager').addConnection('YaioServerNodeDBDriver_Local', function () { return yaioAppBase.get('YaioServerNodeDBDriver_Local'); });\n" : "") 
                                + "datasourceKey = 'YaioServerNodeDBDriver_Local';\n"
                                + "// CONFIGUREDATASOURCES_SNAP\n";
                matcher = pattern.matcher(content);
                buffer = new StringBuffer();
                while (matcher.find()) {
                    matcher.appendReplacement(buffer, "");
                    buffer.append(replacement);
                }
                matcher.appendTail(buffer);
                res =  buffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        // add additional resBaseUrls for CORS
        String addResBase = "";
        if (addResBaseUrls.size() > 0) {
            addResBase = "yaioAppBase.config.addResBaseUrls = ['";
            addResBase += StringUtils.join(addResBaseUrls, "', '");
            addResBase += "'];";
            res = res.replaceAll("yaioAppBase.config.resBaseUrl = (.*?);", 
                                 "yaioAppBase.config.resBaseUrl = $1;\n" + addResBase + "\n");
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
        if (flgExport && StringUtils.isNotEmpty(baseref)) {
            // replace inactive baseref if baseref is set
//            res = res.replace("<!--<base href=\"/\" />-->", 
//                              "<base href=\"" + baseref + "/yaio-explorerapp/yaio-explorerapp.html\" />");
            res = res.replaceAll("=\"../dist/", 
                            "=\"" + baseref + "/yaio-explorerapp/../dist/");
            res = res.replaceAll("yaioAppBase.config.resBaseUrl = .*?;", 
                                 "yaioAppBase.config.resBaseUrl = \"" + baseref + "/yaio-explorerapp/\";\n" + addResBase + "\n");
            

        }

        // change headers to display html in browser
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "");
        
        return res;
    }
}
