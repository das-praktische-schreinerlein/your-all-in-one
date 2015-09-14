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
import de.yaio.extension.datatransfer.html.HtmlExporter;
import de.yaio.extension.datatransfer.json.JSONFullExporter;
import de.yaio.extension.datatransfer.json.JSONFullImporter;
import de.yaio.extension.datatransfer.json.JSONResponse;
import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.extension.datatransfer.wiki.InlineWikiImporter;
import de.yaio.extension.datatransfer.wiki.WikiExporter;
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
            Logger.getLogger(ConverterUtils.class);

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
     * @param pTplFile - path to tplFile-resource (if null=defaultfile will used; if empty=ignored)
     * @return String - html-format of the node
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
     * @param pTplFile - path to tplFile-resource (if null=defaultfile will used; if empty=ignored)
     * @return String - html-format of the node
     * @throws Exception - IOException and Parser-Exceptions possible
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
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     read the node for sysUID, return it in Json-format with all children<br>
     *     include it into the yaioOfflineApp and set contect-header on response-obj
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>String - yaioOfflineApp-html-format of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - basenode to export
     * @param response - the response-Obj to set contenttype and headers
     * @param pTplFile - path to tplFile-resource (if null=defaultfile will used; if empty=ignored)
     * @param flgExport - if set it will be prepared as export (baseref will be set and static is default datasource)
     * @return String - html-format of the node
     * @throws Exception - IOException and Parser-Exceptions possible
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
                    res += "yaioAppBase.configureService(\"Yaio.ServerNodeDataService_" + url + "\", function() { return Yaio.ServerNodeDataService(yaioAppBase, Yaio.ServerNodeDataServiceConfig(\"" + url + "\", \"" + name + "\",  \"" + desc + "\")); });\n";
                    res += "yaioAppBase.configureService(\"YaioServerNodeData_" + url + "\", function() { return yaioAppBase.get(\"Yaio.ServerNodeDataService_" + url + "\"); });\n";
                    res += "yaioAppBase.config.datasources.push(\"YaioServerNodeData_" + url + "\");\n";
                    addResBaseUrls.add(url);
                }
                pattern = Pattern.compile("\\/\\/ CONFIGUREDATASOURCES_SNIP.*\\/\\/ CONFIGUREDATASOURCES_SNAP", Pattern.DOTALL);
                replacement = "// CONFIGUREDATASOURCES_SNIP\n"
                                + "yaioAppBase.configureService(\"Yaio.StaticNodeDataService\", function() { return Yaio.StaticNodeDataService(yaioAppBase, Yaio.StaticNodeDataServiceConfig(\"\", \"Statische InApp-Daten für '" + sysUID + "'\", \"Die statisch in der App hinterlegten Daten werden geladen.\")); });\n"
                                + "yaioAppBase.config.datasources.push(\"YaioStaticNodeData\");\n" 
                                + "yaioAppBase.config.datasources.push(\"YaioFileNodeData\");\n" 
                                // skip localhost-server on export 
                                + (!flgExport ? "yaioAppBase.config.datasources.push(\"YaioServerNodeData_Local\");\n" : "") 
                                + "\n" + res + "\n"
                                + "// CONFIGUREDATASOURCES_SNIP\n";
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
        inputOptions.setStrDefaultMetaNodePraefix(masterNode.getMetaNodePraefix());
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

    /**
     * <h4>FeatureDomain:</h4>
     *     JsonImporter
     * <h4>FeatureDescription:</h4>
     *     parse jsonSrc with JsonImporter and WikiImportOptions, and add it to the masternode
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>adds children from jsonSrc to masterNode
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     JsonImporter
     * @param inputOptions - importOptions for wikiImporter
     * @param masterNode - baseNode to add the children
     * @param jsonSrc - jsonSrc to parse with JsonImporter
     * @throws Exception - ParserExceptions possible
     */
    protected void parseNodesFromJson(final WikiImportOptions inputOptions,
                                      final BaseNode masterNode, 
                                      final String jsonSrc) throws Exception {
        // extract 
        JSONFullImporter jsonImporter = new JSONFullImporter(inputOptions);
        JSONResponse response = jsonImporter.parseJSONResponse(jsonSrc);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("parse Response:" + response);
        }
        BaseNode baseNode = (BaseNode) response.getNode();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("parse Response.node:" + baseNode);
        }
        if ("MasterplanMasternode1".equals(baseNode.getSysUID()) 
            || masterNode.getSysUID().equals(baseNode.getSysUID())) {
            // dont import masternode and parents twice: copy children
            for (BaseNode childNode : baseNode.getChildNodes()) {
                childNode.setParentNode(masterNode);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("add Child:" + childNode.getNameForLogger());
                }
            }
        } else {
            // add masternode
            baseNode.setParentNode(masterNode);
        }
        LOGGER.info("masterNode after json:" + masterNode.getBaseNodeService().visualizeNodeHierarchy("", masterNode));
        //if inoutOptions resetSysUID(masterNode, true);
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     JsonImporter
     * <h4>FeatureDescription:</h4>
     *     parse jsonSrc with JsonImporter, export as wiki and reimport from wiki to ensure valid syntax, 
     *     and add it to the masternode
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>adds children from jsonSrc to masterNode
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     JsonImporter
     * @param inputOptions - importOptions for wikiImporter
     * @param masterNode - baseNode to add the children
     * @param jsonSrc - jsonSrc to parse with JsonImporter
     * @throws Exception - ParserExceptions possible
     */
    public void parseValidatedNodesFromJson(final WikiImportOptions inputOptions,
                                    final BaseNode masterNode, 
                                    final String jsonSrc) throws Exception {
        // parse json to dummy-masternode
        BaseNode tmpMasterNode = 
             createTemporaryMasternode("dummy", masterNode.getMetaNodePraefix(), masterNode.getMetaNodeNummer());
        this.parseNodesFromJson(inputOptions, tmpMasterNode, jsonSrc);
        
        // export as wiki
        Exporter exporter = new WikiExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        String wikiSrc = exporter.getMasterNodeResult(tmpMasterNode, oOptions);
        
        // import from wiki
        WikiImportOptions tmpInputOptions = new WikiImportOptions();
        inputOptions.setFlgReadList(true);
        inputOptions.setFlgReadUe(true);
        inputOptions.setStrDefaultMetaNodePraefix(masterNode.getMetaNodePraefix());
        WikiImporter wikiImporter = new WikiImporter(tmpInputOptions);
        this.parseNodesFromWiki(wikiImporter, tmpInputOptions, masterNode, wikiSrc);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("masternode after wiki:" + masterNode.getBaseNodeService().visualizeNodeHierarchy("", masterNode));
        }
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Importer
     * <h4>FeatureDescription:</h4>
     *     create a masternode for import (temporary)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returns BaseNode
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Importer
     * @param sysUID         sysUID
     * @param nodePraefix    metaNodePraefix
     * @param nodeNummer     metaNodeNumber
     * @return               the Basenode to use as masternode 
     */
    public BaseNode createTemporaryMasternode(final String sysUID, final String nodePraefix, final String nodeNummer) {
        BaseNode tmpMasterNode = new BaseNode();
        tmpMasterNode.setSysUID(sysUID);
        tmpMasterNode.setMetaNodePraefix(nodePraefix);
        tmpMasterNode.setMetaNodeNummer(nodeNummer);
        tmpMasterNode.setEbene(0);
        return tmpMasterNode;
    }

    protected void resetSysUID(final BaseNode node, final boolean childrenOnly) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("resetSysUID:" + node.getNameForLogger());
        }
        if (!childrenOnly) {
            node.setSysUID(null);
        }
        for (BaseNode childNode : node.getChildNodes()) {
            resetSysUID(childNode, false);
        }
    }
    
}
