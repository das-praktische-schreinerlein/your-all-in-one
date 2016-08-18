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

import de.yaio.app.config.YaioConfiguration;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.exporter.Exporter;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.app.datatransfer.json.JSONFullExporter;
import de.yaio.app.extension.datatransfer.html.HtmlExporter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * Services to parse text to nodes and convert them in different 
 * formats (wiki, ppl, excel..)
 *  
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Service
public class ConverterUtils {

    // Logger
    private static final Logger LOGGER = Logger.getLogger(ConverterUtils.class);

    /** 
     * read the node and use the converter to convert it with all children to
     * converter-format<br>
     * set headers (contentype, disposition) on the response-obj
     * @param sysUID                 sysUID to export
     * @param exporter               the converter to use
     * @param oOptions               the outputoptions for the converter
     * @param extension              the fileextension for the Content-Disposition
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - export-format of the node
     */
    public String exportNode(final String sysUID, final Exporter exporter,
                             final OutputOptions oOptions, final String extension,
                             final HttpServletResponse response) throws ConverterException {
        // find a specific node
        BaseNode node = BaseNode.findBaseNode(sysUID);
        if (node == null) {
            throw new ConverterException("cant find node for sysUID", sysUID,
                    new IllegalArgumentException("sysUID not found"));
        }
        if (!exporter.hasOwnNodeReader()) {
            node.initChildNodesFromDB(-1);
        }

        return exportNode(node, exporter, oOptions, extension, response);
    }

    /** 
     * use the converter to convert it with all children to
     * converter-format<br>
     * set headers (contentype, disposition) on the response-obj
     * @param node                   node to export
     * @param exporter               the converter to use
     * @param oOptions               the outputoptions for the converter
     * @param extension              the fileextension for the Content-Disposition
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - export-format of the node
     * @throws ConverterException    possible Exceptions
     */
    public String exportNode(final BaseNode node, final Exporter exporter, 
                             final OutputOptions oOptions, final String extension,
                             final HttpServletResponse response) throws ConverterException {
        // find a specific node
        String res = "";
        if (node != null) {
            // export node with converter
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
                                         final String pTplFile) throws ConverterException {
        // read node
        BaseNode node = BaseNode.findBaseNode(sysUID);
        if (node == null) {
            throw new ConverterException("cant find node for sysUID", sysUID,
                    new IllegalArgumentException("sysUID not found"));
        }
        if (node != null) {
            node.initChildNodesFromDB(-1);
        }

        return commonExportNodeAsHtml(node, oOptions, response, pTplFile);
    }

    /** 
     * read the node for sysUID and return it in html-format with all children<br>
     * use the setting of the output-options and set contect-header on response-obj
     * @param basenode               basenode to export
     * @param oOptions               the outputOptions 
     * @param response               the response-Obj to set contenttype and headers
     * @param pTplFile               path to tplFile-resource (if null=defaultfile will used; if empty=ignored)
     * @return                       String - html-format of the node
     * @throws ConverterException    IOException and Parser-Exceptions possible
     */
    public String commonExportNodeAsHtml(final BaseNode basenode,
                                         final OutputOptions oOptions,
                                         final HttpServletResponse response,
                                         final String pTplFile) throws ConverterException {
        Exporter exporter = new HtmlExporter();
        String res = "";
        String tplFile = pTplFile;
        
        // check tplFile
        if (tplFile == null) {
            tplFile = "/static/exporttemplates/projektplan-export.html";
        }
        // run export
        res = this.exportNode(basenode, exporter, oOptions, ".html", response);
        if (!StringUtils.isEmpty(tplFile)) {
            // read tpl
            String content = readTplFileAsStream(tplFile);

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
     * @throws ConverterException    IOException and Parser-Exceptions possible
     */
    public String commonExportNodeAsYaioApp(final String sysUID,
                                         final HttpServletResponse response,
                                         final String pTplFile,
                                         final boolean flgExport) throws ConverterException {
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
        if (!StringUtils.isEmpty(sysUID)) {
            res = this.exportNode(sysUID, exporter, oOptions, ".json", response);
        }

        if (!StringUtils.isEmpty(tplFile)) {
            // read tpl
            String content = readTplFileAsStream(tplFile);

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
            Map<String, Map<String, String>> knownYaioInstances = YaioConfiguration.getInstance().getKnownYaioInstances();
            for (String name : knownYaioInstances.keySet()) {
                Map<String, String> yaioInstance = knownYaioInstances.get(name);
                String url = yaioInstance.get(YaioConfiguration.CONST_PROPNAME_YAIOINSTANCES_URL);
                String desc = yaioInstance.get(YaioConfiguration.CONST_PROPNAME_YAIOINSTANCES_DESC);
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
                            + "yaioAppBase.config.datasources.push('YaioUrlDownloadNodeDBDriver');\n"
                            + "yaioAppBase.get('YaioDataSourceManager').addConnection('YaioUrlDownloadNodeDBDriver', function () { return yaioAppBase.get('YaioUrlDownloadNodeDBDriver'); });\n"
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

    protected String readTplFileAsStream(final String tplFile) {
        InputStream in = this.getClass().getResourceAsStream(tplFile);
        String content;
        try {
            content = IOUtils.toString(in);
        }  catch (IOException ex) {
            throw new IllegalArgumentException("cant read tplFile for htmlExport", ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return content;
    }
}
