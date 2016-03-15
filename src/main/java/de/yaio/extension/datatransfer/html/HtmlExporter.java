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
package de.yaio.extension.datatransfer.html;

import de.yaio.commons.converter.YmfMarkdownProvider;
import de.yaio.commons.data.DataUtils;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.InfoNode;
import de.yaio.core.node.SymLinkNode;
import de.yaio.core.node.UrlResNode;
import de.yaio.core.nodeservice.UrlResNodeService;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.datatransfer.exporter.formatter.*;
import de.yaio.extension.datatransfer.wiki.WikiExporter;
import org.apache.log4j.Logger;
import org.pegdown.JshConfig;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * export of Nodes as Html
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.html
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class HtmlExporter extends WikiExporter {
    
    protected static final String CONST_LAYOUT_TAG_DIV = "DIV";
    protected static final String CONST_LAYOUT_TAG_UE = "UE";
    protected static final String CONST_LAYOUT_TAG_P = "P";
    protected static final String CONST_LAYOUT_TAG_LI = "LI";
    protected static final String CONST_LAYOUT_TAG_TR = "TR";
    protected static final String CONST_LAYOUT_TAG_DIRECT = "DIRECT";
    protected static final String CONST_LAYOUT_TAG_ENDDIV = "ENDDIV";
    protected static final String CONST_LAYOUT_TAG_DESCONLY = "DESC";

    
    protected static final String CONST_FORMATTER_DESC = DescDataFormatterImpl.class.getName();
    protected static final String CONST_FORMATTER_IST = IstDataFormatterImpl.class.getName();
    protected static final String CONST_FORMATTER_PLAN = PlanDataFormatterImpl.class.getName();
    protected static final String CONST_FORMATTER_ISTCHILDRENSUM = IstChildrenSumDataFormatterImpl.class.getName();
    protected static final String CONST_FORMATTER_PLANCHILDRENSUM = PlanChildrenSumDataFormatterImpl.class.getName();

    protected YmfMarkdownProvider markdownProvider = new YmfMarkdownProvider();

    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(HtmlExporter.class);
    
    protected int htmlElementId = 1;
    
    /** 
     * service functions to export nodes as Html
     */
    public HtmlExporter() {
        super();
    }
    
    @Override
    public String getMasterNodeResult(final DataDomain masterNode,
            final OutputOptions poOptions) throws Exception {
        OutputOptions oOptions = genOutputOptionsForHtml(poOptions);
        return super.getMasterNodeResult(masterNode, oOptions);
    }
    
    
    /** 
     * generate helper-OutputOptions for generation of the html
     * @param baseOOptions           Default OutputOptions to override
     * @return                       OuputOptions for generation of the html
     */
    public OutputOptions genOutputOptionsForHtml(final OutputOptions baseOOptions) {
        OutputOptions options = new OutputOptionsImpl(baseOOptions);

        // activate desc
        options.setFlgReEscapeDesc(true);
        options.setFlgTrimDesc(false);
        options.setFlgShowDescInNextLine(false);
        options.setFlgShowBrackets(false);
        options.setIntendFuncArea(0);
        options.setIntendSys(0);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("gen new oOptions for export=" + options);
        }


        return options;
    }

    @Override
    public StringBuffer getNodeResult(final DataDomain node,  final String praefix,
            final OutputOptions oOptions) throws Exception {
        StringBuffer res = new StringBuffer();

        // Template-Nodes ignorieren
//        if (TemplateNode.class.isInstance(curNode)) {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("SKIP TemplateNode node:" + curNode.getNameForLogger());
//            }
//            return res;
//        }
        
        BaseNode curNode = (BaseNode) node;

        // Anfang
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node: start processing" + curNode.getNameForLogger());
        }

        // max. Ebene pruefen
        if (curNode.getEbene() > oOptions.getMaxEbene()) {
            return res;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("run export with oOptions=" + oOptions);
        }
        

        // Result erzeugen
        if ((InfoNode.class.isInstance(curNode) || UrlResNode.class.isInstance(curNode))
            && oOptions.isFlgProcessDocLayout()
            && curNode.getEbene() > oOptions.getMaxUeEbene()) {
            // Layout-Ausgabe der Infossätze
            res.append(this.genHtmlDokuLayoutForNode(curNode, oOptions));
        } else {
            // Projekt-Ausgabe aller Sätze
            res.append(this.genHtmlProjektLayoutForNode(curNode, oOptions));
        }

        return res;
    }

    /** 
     * formats recursively node in html-docu-format
     * @param curNode                node for output recursively
     * @param oOptions               options for output (formatter)
     * @return                       formatted output of node-hierarchy and DataDomains
     * @throws Exception             parser/format-Exceptions possible
     */
    public String genHtmlDokuLayoutForNode(final BaseNode curNode,
        final OutputOptions oOptions) throws Exception {
        String res = "";

        // max. Ebene pruefen
        if (curNode.getEbene() > oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + curNode.getSysUID() 
                        + " ignore:" + curNode.getEbene() + ">" + oOptions.getMaxEbene());
            }
            return res;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getSysUID() + " start processing");
        }

        // alle Kindselemente durchlaufen
        String blockChildren = "";
        boolean flgChildMatched = false;
        if (curNode.getEbene() < oOptions.getMaxEbene() 
                && curNode.getChildNodes().size() > 0) {
            // Elternblock: Zweig
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + curNode.getSysUID() + " iterate subnodes:" 
                        + curNode.getChildNodes().size());
            }

            // Childnodes iterieren
            boolean flgListStarted = false;
            boolean flgTableStarted = false;
            boolean flgCloseNode = false;
            for (String nodeName : curNode.getChildNodesByNameMap().keySet()) {
                BaseNode node = (BaseNode) curNode.getChildNodesByNameMap().get(nodeName);

                // eventuelles Divs des vorherigen Nodes schließen
                if (flgCloseNode) {
                    blockChildren += "</div></div>\n\n\n";
                    flgCloseNode = false;
                }
                
                String command = this.getDocLayoutTagCommand(node);
                String docLayoutFlgCloseDiv = node.getDocLayoutFlgCloseDiv();
                docLayoutFlgCloseDiv = docLayoutFlgCloseDiv == null ? "" : docLayoutFlgCloseDiv;

                //eventuelle Tabelle starten
                if (CONST_LAYOUT_TAG_TR.equalsIgnoreCase(command)) {
                    // aktuelle ChildNode ist Table
                    if (!flgTableStarted) {
                        // Liste starten
                        String addStyle = node.getDocLayoutAddStyleClass();
                        blockChildren += "<table class='yaio-doc-table"
                                      + (addStyle.length() > 0
                                          ? "yaio-doc-table-" + addStyle : "")
                                      +   "' id='table_" + node.getSysUID() + "'>\n";
                        flgTableStarted = true;
                    }
                } else if (flgTableStarted) {
                    // kein Tableelement beendet Table
                    blockChildren += "</table>\n";
                    flgTableStarted = false;
                }

                // eventuell Liste starten
                if (CONST_LAYOUT_TAG_LI.equalsIgnoreCase(command)) {
                    // aktuelle ChildNode ist Liste
                    if (!flgListStarted) {
                        // Liste starten
                        String addStyle = node.getDocLayoutAddStyleClass();
                        blockChildren += "<ul class='yaio-doc-ul"
                                      + (addStyle.length() > 0
                                          ? " yaio-doc-ul-" + addStyle : "")
                                      +   "' id='ul_" + node.getSysUID() + "'>\n";
                        flgListStarted = true;
                    }
                } else if (flgListStarted) {
                    // kein Listenelement beendet Liste
                    blockChildren += "</ul>\n";
                    flgListStarted = false;
                }

                // aktuelle Node parsen
                blockChildren += this.getNodeResult(node, "",  oOptions);

                // pruefne ob hinter Node div geschlossen wird
                if (docLayoutFlgCloseDiv.length() > 0) {
                    flgCloseNode = true;
                }
            }

            if (flgTableStarted) {
                // angefangene Tabellen beenden
                blockChildren += "</table>\n";
                flgTableStarted = false;
            }

            if (flgListStarted) {
                // angefangene Listen beenden
                blockChildren += "</ul>\n";
                flgListStarted = false;
            }

            // eventuelles Divs des vorherigen Nodes schließen
            if (flgCloseNode) {
                blockChildren += "</div></div>\n\n\n";
                flgCloseNode = false;
            }
        } else {
            // Blatt
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + curNode.getSysUID() + " no subnodes");
            }
        }
        
        // check if children matches (childRes filled)
        if (blockChildren.length() > 0) {
            flgChildMatched = true;
        }
        // check if I'am matching
        boolean flgMatchesFilter = this.isNodeMatchingFilter(curNode, oOptions);
        if (!(flgMatchesFilter || flgChildMatched)) {
            // sorry me and my children didnt match
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sorry me and my children didnt match"
                                + " - node:" + curNode.getSysUID() 
                                + " flgMatchesFilter=" + flgMatchesFilter
                                + " flgChildMatched=" + flgChildMatched);
            }
            return res;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getSysUID() + " do processing"
                            + " flgMatchesFilter=" + flgMatchesFilter
                            + " flgChildMatched=" + flgChildMatched);
        }

        // Namen erzeugen
        String name = curNode.getName();
        //name = name.replaceAll("\"", "'");
        // Html-Escapen
        name = name.replaceAll("<WLESC>", "\\");
        name = name.replaceAll("<WLTAB>", "\t");
        name = DataUtils.htmlEscapeText(name);

        // Desc
        StringBuffer tmpBuffer = new StringBuffer();
        this.formatNodeDataDomain(curNode, 
                this.getDataDomainFormatterByClassName(CONST_FORMATTER_DESC), 
                tmpBuffer, oOptions);
        String descFull = tmpBuffer.toString();
        if (descFull != null && descFull.length() > 0 && oOptions.isFlgShowDesc()) {
            // Html-Escapen
            descFull = this.formatTextAsMarkdown(descFull);
//            descFull = DataUtils.htmlEscapeText(descFull);
//            descFull = descFull.replaceAll("\n", "<br>");
        }

        // Layout konfigurieren
        String layoutCommand = this.getDocLayoutTagCommand(curNode);
        String addStyle = curNode.getDocLayoutAddStyleClass();
        addStyle = addStyle == null ? "" : addStyle;
        String shortName = curNode.getDocLayoutShortName();
        shortName = shortName == null ? "" : shortName;

        // Layout-Result erzeugen
        res = "";
        if (CONST_LAYOUT_TAG_DIRECT.equalsIgnoreCase(layoutCommand)) {
            // Direct
            res = name;
        } else {
            // Tags

            // TagEeben ab Ueberschriftsebene rechnen
            int tagEbene = curNode.getEbene() - oOptions.getMaxUeEbene();
            if (tagEbene < 1) {
                tagEbene = 1;
            }

            // Ue+Content extrahieren
            //name = name + curNode.getDocLayout(oOptions) + " XX:" + curNode.getDocLayoutFlgCloseDiv();
            String ue = name;
            String content = "";
            int pos = name.indexOf(":");
            if (pos > 0) {
                ue = name.substring(0, pos + 1);
                content = name.substring(pos + 1, name.length());
            }
            
            // skip empty ue
            ue = ue.trim();
            if (":".equals(ue)) {
                ue = "&nbsp;";
            }

            // check for UrlRes
            if (UrlResNode.class.isInstance(curNode)) {
                // URLRes
                UrlResNode urlResNode = (UrlResNode) curNode;
                
                // set label
                String label = urlResNode.getResLocName();
                
                // Label: if empty set to name
                if (label == null || label.length() < 1) {
                    label = name;
                }
                label = label.trim();
                
                // Label: if empty set to link
                if (label == null || label.length() < 1) {
                    label = urlResNode.getResLocRef();
                }
                
                if (UrlResNodeService.CONST_NODETYPE_IDENTIFIER_IMAGERES.equals(curNode.getState())) {
                    // Image
                    content += "<a href='" + urlResNode.getResLocRef() + "' target='_blank_'"
                        +   " class='a-img" + tagEbene + "-urlres "
                        +     (addStyle.length() > 0
                                ? " " + "a-img" + tagEbene + "-urlres-" + addStyle : "") + "'"
                        +   " id='a_" + curNode.getSysUID() + "'>" 
                        + "<img src='" + urlResNode.getResLocRef() + "' "
                        + " class='img" + tagEbene + "-urlres "
                        + (addStyle.length() > 0
                            ? " " + "img" + tagEbene + "-urlres-" + addStyle : "") + "'"
                        +   " id='img_" + curNode.getSysUID() + "' alt='" + label + "' title='" + label + "'></a>";
                } else {
                    // Url
                    content += "<a href='" + urlResNode.getResLocRef() + "' "
                        + " target='_blank' "
                        + " class='a" + tagEbene + "-urlres "
                        + (addStyle.length() > 0
                            ? " " + "a" + tagEbene + "-urlres-" + addStyle : "") + "'"
                        +   " id='a_" + curNode.getSysUID() + "'>"
                        + label + "</a> ";
                }
            }

            if (CONST_LAYOUT_TAG_UE.equalsIgnoreCase(layoutCommand)) {
                // Ueberschrift
                String tag = "h" + tagEbene;
                res += "<" + tag + " class='yaio-doc-" + tag
                    + (addStyle.length() > 0 ? " yaio-doc-" + tag + addStyle : "") + "'"
                    +   " id='" + tag + "_" + curNode.getSysUID() + "'>"
                    + ue
                    + "</" + tag + ">\n";

                // optionaler Contentbereich
                if (content.length() > 0 || (descFull != null && descFull.length() > 0)) {
                    tag = "p";
                    res += "<" + tag + " class='yaio-doc-" + tag
                        + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-" + addStyle : "") + "'"
                        +   " id='" + tag + "_" + curNode.getSysUID() + "'>";
                    
                    // content
                    if (content.length() > 0) {
                        res += "<span class='yaio-doc-" + tag + "-desc"
                                        + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-desc-" + addStyle : "") + "'"
                                        +   " id='pSpanDesc_" + curNode.getSysUID() + "'>"
                                        + content
                                        + "</span>";
                    }

                    // eventuelles SPAN fuer Detailbeschreibung
                    if (descFull != null && descFull.length() > 0) {
                        res +=  "<span class='yaio-doc-" + tag + "-descdetail"
                            + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-descdetail-" + addStyle : "") + "'"
                            +   " id='pSpanDescDetail_" + curNode.getSysUID() + "'>"
                            + descFull
                            + "</span>";

                    }
                    res +=  "</" + tag + ">\n";
                }
            } else if (CONST_LAYOUT_TAG_LI.equalsIgnoreCase(layoutCommand)) {
                // Aufzaehlung
                String tag = "li";
                res += "<" + tag + " class='yaio-doc-" + tag + ""
                    + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-" + addStyle : "") + "'"
                    +   " id='li_" + curNode.getSysUID() + "'>"
                    + "<span class='yaio-doc-" + tag + "-ue"
                    + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-ue-" + addStyle : "") + "'"
                    +   " id='liSpanUe_" + curNode.getSysUID() + "'>"
                    + ue
                    + "</span>"
                    + "<span class='yaio-doc-" + tag + "-desc"
                    + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-desc-" + addStyle : "") + "'"
                    +   " id='liSpanDesc_" + curNode.getSysUID() + "'>"
                    + content
                    + "</span>";

                // eventuelles SPAN fuer Detailbeschreibung
                if (descFull != null && descFull.length() > 0) {
                    res +=  "<span class='yaio-doc-" + tag + "-descdetail"
                        + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-descdetail-" + addStyle : "") + "'"
                        +   " id='liSpanDescDetail_" + curNode.getSysUID() + "'>"
                        + descFull
                        + "</span>";

                }
                res +=  "</" + tag + ">\n";
            } else if (CONST_LAYOUT_TAG_TR.equalsIgnoreCase(layoutCommand)) {
                // Tabelle

                // TR Zeile oeffnen
                String tag = "tr";
                res += "<" + tag + " class='yaio-doc-" + tag + ""
                    + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-" + addStyle : "") + "'"
                    +   " id='tr_" + curNode.getSysUID() + "'>";

                // TD Spalten einfuegen
                tag = "td";
                String[] lstContent = name.split("\\|");
                for (int zaehler = 0; zaehler < lstContent.length; zaehler++) {
                    // Formeln parsen
                    String tdId = "td_" + curNode.getSysUID() + "_" + zaehler;
                    String tdContent = lstContent[zaehler];
                    tdContent = tdContent.replaceAll("=SUMCOL=",
                        "<script>yaioAppBase.get('YaioExportedData').calcColumns('" + tdId + "', 'SUM', '', '');</script>");

                    // TD-Spalte erzeugen
                    res += "<td class='yaio-doc-" + tag + " yaio-doc-" + tag + "-" + zaehler
                            + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-" + addStyle
                            + " yaio-doc-" + tag + "-" + zaehler + "-" + addStyle : "") + "'"
                            + " id='" + tdId + "'>"
                            + tdContent
                            + "</td>";
                }

                // TR Zeile schliessen
                tag = "tr";
                res += "</" + tag + ">\n";
            } else if (CONST_LAYOUT_TAG_DIV.equalsIgnoreCase(layoutCommand)) {
                // DIV
                res += "<div class='box yaio-doc-box add2toc-h1"
                    + (addStyle.length() > 0 ? " yaio-doc-box-" + addStyle : "") + "'"
                    + (shortName.length() > 0 ? " toclabel='" + shortName + "'" : "")
                    + " id='box_" + curNode.getSysUID() + "'>"
                    + "<div class='boxline boxline-ue" + tagEbene + " yaio-doc-h" + tagEbene
                    + (addStyle.length() > 0 ? " yaio-doc-h" + tagEbene + "-" + addStyle : "") + "'"
                    + " id='ue_" + curNode.getSysUID() + "'>"
                    + ue
                    + "</div>\n"
                    + "<div class='togglecontainer"
                    + (addStyle.length() > 0 ? " togglecontainer-" + addStyle : "") + "'"
                    + " id='detail_" + curNode.getSysUID() + "'>\n";
                
                // optionaler Contentbereich
                if (content.length() > 0 || (descFull != null && descFull.length() > 0)) {
                    String tag = "p";
                    res += "<" + tag + " class='yaio-doc-" + tag + ""
                        + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-" + addStyle : "") + "'"
                        +   " id='" + tag + "_" + curNode.getSysUID() + "'>";
                    
                    // content
                    if (content.length() > 0) {
                        res += "<span class='yaio-doc-" + tag + "-desc"
                                        + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-desc-" + addStyle : "") + "'"
                                        +   " id='pSpanDesc_" + curNode.getSysUID() + "'>"
                                        + content
                                        + "</span>";
                    }

                    // eventuelles SPAN fuer Detailbeschreibung
                    if (descFull != null && descFull.length() > 0) {
                        res +=  "<span class='yaio-doc-" + tag + "-descdetail"
                            + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-descdetail-" + addStyle : "") + "'"
                            +   " id='pSpanDescDetail_" + curNode.getSysUID() + "'>"
                            + descFull
                            + "</span>";

                    }
                    res +=  "</" + tag + ">\n";
                }
            } else if (CONST_LAYOUT_TAG_ENDDIV.equalsIgnoreCase(layoutCommand)) {
                // ENDDIV
                res += "</div></div>\n\n\n";
            } else if (CONST_LAYOUT_TAG_P.equalsIgnoreCase(layoutCommand)) {
                String tag = "p";
                res += "<" + tag + " class='yaio-doc-" + tag + ""
                        + (addStyle.length() > 0
                        ? " yaio-doc-" + tag + "-" + addStyle : "") + "'"
                        +   " id='p_" + curNode.getSysUID() + "'>"
                        + "<span class='yaio-doc-" + tag + "-ue"
                        + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-ue-" + addStyle : "") + "'"
                        + " id='pSpanUe_" + curNode.getSysUID() + "'>"
                        + ue
                        + "</span>"
                        + "<span class='yaio-doc-" + tag + "-desc"
                        + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-desc-" + addStyle : "") + "'"
                        + " id='pSpanDesc_" + curNode.getSysUID() + "'>"
                        + content
                        + "</span>";

                // eventuelles SPAN fuer Detailbeschreibung
                if (descFull != null && descFull.length() > 0) {
                    res +=  "<span class='yaio-doc-" + tag + "-descdetail"
                            + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-descdetail-" + addStyle : "") + "'"
                            +   " id='pSpanDescDetail_" + curNode.getSysUID() + "'>"
                            + descFull
                            + "</span>";

                }
                res +=  "</" + tag + ">\n";
            } else {
                // DEFAULT: Block
                String tag = "div";
                res += "<" + tag + " class='yaio-doc-" + tag + ""
                    + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-" + addStyle : "") + "'"
                    + " id='div_" + curNode.getSysUID() + "'>";
                if (!CONST_LAYOUT_TAG_DESCONLY.equalsIgnoreCase(layoutCommand)) {
                    res += "<div class='yaio-doc-" + tag + "-ue-container"
                            + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-ue-container" + addStyle : "") + "'"
                            + " id='divDivUe_" + curNode.getSysUID() + "'>"
                            + "<span class='yaio-doc-" + tag + "-ue"
                            + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-ue-" + addStyle : "") + "'"
                            + " id='divSpanUe_" + curNode.getSysUID() + "'>"
                            + ue
                            + "</span>"
                            + "<span class='yaio-doc-" + tag + "-desc"
                            + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-desc-" + addStyle : "") + "'"
                            + " id='divSpanDesc_" + curNode.getSysUID() + "'>"
                            + content
                            + "</span>"
                            + "</div>";
                }

                // eventuelles DIV fuer Detailbeschreibung
                if (descFull != null && descFull.length() > 0) {
                    res +=  "<div class='yaio-doc-" + tag + "-descdetail"
                        + (addStyle.length() > 0 ? " yaio-doc-" + tag + "-descdetail-" + addStyle : "") + "'"
                        + " id='divDivDescDetail_" + curNode.getSysUID() + "'>"
                        + descFull
                        + "</div>";

                }
                res +=  "</" + tag + ">\n";
            }
        }

        // Kinds-Elemente anhaengen
        res += blockChildren;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getSysUID() + " return datalength:" + res.length());
        }

        return res;
    }

    /** 
     * formats recursively node in html-project-format
     * @param curNode                node for output recursively
     * @param oOptions               options for output (formatter)
     * @return                       formatted output of node-hierarchy and DataDomains
     * @throws Exception             parser/format-Exceptions possible
     */
    public String genHtmlProjektLayoutForNode(final BaseNode curNode,
        final OutputOptions oOptions) throws Exception {
        String res = "";

        // max. Ebene pruefen
        if (curNode.getEbene() > oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + curNode.getSysUID() 
                        + " ignore:" + curNode.getEbene() + ">" + oOptions.getMaxEbene());
            }
            return res;
        }

        // Anfang
        String styleClassesName = "";
        String styleClassesBlock = "";
        String styleClassesState = "";
        String styleClassesStateShort = "";
        String styleClassesNameContainer2 = "";
        String styleClassesUe = "";
        String labelState = "";
        String blockName = "";
        String blockIst = "";
        String blockIstCalcSum = "";
        String blockPlan = "";
        String blockPlanCalcSum = "";
        String blockDesc = "";
        String blockChildren = "";

        // Status
        if (oOptions.isFlgShowState()) {
            labelState = curNode.getState();
            styleClassesState = "yaio-node-state-" + labelState;
        }
        styleClassesStateShort = "yaio-node-state-" + labelState;

        // pruefen ob innerhalb der Ue-Ebenen-Begrenzung
        if (curNode.getEbene() <= oOptions.getMaxUeEbene()) {
            // als Ue darstellen
            styleClassesUe = "yaio-pj-node-style-ue yaio-pj-node-style-ue" + curNode.getEbene();
        } else {
            // als Aufzaehlung darstellen
            styleClassesUe = "yaio-pj-node-style-list yaio-pj-node-style-list" + curNode.getEbene();
        }

        // alle Kindselemente durchlaufen
        // generate children 
        boolean flgChildMatched = false;
        if (curNode.getEbene() < oOptions.getMaxEbene() && curNode.getChildNodes().size() > 0) {
            // Elternblock: Zweig
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + curNode.getSysUID() 
                        + " iterate subnodes:" + curNode.getChildNodes().size());
            }
            blockChildren = "";
            for (String nodeName : curNode.getChildNodesByNameMap().keySet()) {
                BaseNode node = (BaseNode) curNode.getChildNodesByNameMap().get(nodeName);
                blockChildren += this.getNodeResult(node, "", oOptions);
            }
            if (blockChildren.length() > 0) {
                blockChildren = "<div id='node_" + curNode.getSysUID() +  "_cildren' class='yaio-pj-node-block-children'>\n"
                                + blockChildren
                                + "</div>\n";
            }

            styleClassesUe += " yaio-pj-node-type-twig yaio-pj-node-type-twig" + curNode.getEbene();
            styleClassesBlock += " yaio-pj-node-block-twig yaio-pj-node-block-twig" + curNode.getEbene();
        } else {
            // Blatt
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + curNode.getSysUID() + " no subnodes");
            }
            styleClassesUe += " yaio-pj-node-type-leaf yaio-pj-node-type-leaf" + curNode.getEbene();
            styleClassesBlock += " yaio-pj-node-block-leaf yaio-pj-node-block-leaf" + curNode.getEbene();
        }
        // check if children matches (childRes filled)
        if (blockChildren.length() > 0) {
            flgChildMatched = true;
        }
        // check if I'am matching
        boolean flgMatchesFilter = this.isNodeMatchingFilter(curNode, oOptions);
        if (!(flgMatchesFilter || flgChildMatched)) {
            // sorry me and my children didnt match
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sorry me and my children didnt match"
                                + " - node:" + curNode.getSysUID() 
                                + " flgMatchesFilter=" + flgMatchesFilter
                                + " flgChildMatched=" + flgChildMatched);
            }
            return res;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getSysUID() + " start processing"
                            + " flgMatchesFilter=" + flgMatchesFilter
                            + " flgChildMatched=" + flgChildMatched);
        }

        // Namen erzeugen
        String name = curNode.getName();
        name = name.replaceAll("<WLESC>", "\\");
        name = name.replaceAll("<WLTAB>", "\t");
        name = DataUtils.htmlEscapeText(name);
        blockName = name + " (" + curNode.getWorkingId() + ")";
        styleClassesNameContainer2 = " yaio-pj-node-level" + curNode.getEbene();
        if (UrlResNode.class.isInstance(curNode)) {
            // URLRes
            UrlResNode urlResNode = (UrlResNode) curNode;
            
            // Label belegen
            String suffix = name;
            String label = urlResNode.getResLocName();
            
            // Label: falls leer mit Name belegen
            if (label == null || label.length() < 1) {
                suffix = "";
                label = name;
            }
            label = label.trim();
            suffix = suffix.trim();
            
            // Label: falls leer mit Link belegen
            if (label == null || label.length() < 1) {
                suffix = "";
                label = urlResNode.getResLocRef();
            }
            
            blockName = "<a href='" + urlResNode.getResLocRef() + "' "
                            + " target='_blank' class='yaio-pj-node-a-urlres'>"
                            + label + "</a>";
            if (UrlResNodeService.CONST_NODETYPE_IDENTIFIER_FILERES.equals(urlResNode.getType())) {
//                url = "/dms/download/" + urlResNode.getResContentDMSId();
                blockName = urlResNode.getResLocRef() + " ";
            }
            blockName += suffix + " (" + curNode.getWorkingId() + ")";
        } else if (SymLinkNode.class.isInstance(curNode)) {
            // Symlink
            SymLinkNode symlinkNode = (SymLinkNode) curNode;
            blockName = name + " (" + curNode.getWorkingId() + ")"
                + " &gt; " + "<a onclick=\"javascript:yaioAppBase.get('YaioExportedData').openNode('" 
                +    symlinkNode.getSymLinkRef() + "');return false;\""
                + " class='yaio-pj-node-a-symlink'>"
                + symlinkNode.getSymLinkRef() + symlinkNode.getSymLinkName() + "</a>";
        }

        // Desc
        StringBuffer tmpBuffer = new StringBuffer();
        this.formatNodeDataDomain(curNode, 
                this.getDataDomainFormatterByClassName(CONST_FORMATTER_DESC), 
                tmpBuffer, oOptions);
        String descFull = tmpBuffer.toString();
        
        if (descFull != null && descFull.length() > 0 && oOptions.isFlgShowDesc()) {
            // Html-Escapen
            descFull = this.formatTextAsMarkdown(descFull);
//            descFull = DataUtils.htmlEscapeText(descFull);
//            descFull = descFull.replaceAll("\n", "<br>");
            blockDesc = this.genHtmlDataBlock(curNode, descFull, "desc", oOptions.isFlgShowDesc());
        }

        // generate workflowdata
        if (curNode.isWFStatus(curNode.getState())) {
            tmpBuffer = new StringBuffer();
            this.formatNodeDataDomain(curNode, 
                    this.getDataDomainFormatterByClassName(CONST_FORMATTER_IST), 
                    tmpBuffer, oOptions);
            String ist = tmpBuffer.toString();

            tmpBuffer = new StringBuffer();
            this.formatNodeDataDomain(curNode, 
                    this.getDataDomainFormatterByClassName(CONST_FORMATTER_PLAN), 
                    tmpBuffer, oOptions);
            String plan = tmpBuffer.toString();

            tmpBuffer = new StringBuffer();
            this.formatNodeDataDomain(curNode, 
                    this.getDataDomainFormatterByClassName(CONST_FORMATTER_ISTCHILDRENSUM), 
                    tmpBuffer, oOptions);
            String istCalcSum = tmpBuffer.toString();

            tmpBuffer = new StringBuffer();
            this.formatNodeDataDomain(curNode, 
                    this.getDataDomainFormatterByClassName(CONST_FORMATTER_PLANCHILDRENSUM), 
                    tmpBuffer, oOptions);
            String planCalcSum = tmpBuffer.toString();

            // Ist-Daten
            blockIst = this.genHtmlDataBlock(curNode, ist, "ist", oOptions.isFlgShowIst());
            if (curNode.getChildNodes().size() > 0) {
                // nur Anzeigen, wenn auch Kindselemente vorhanden
                blockIstCalcSum = this.genHtmlDataBlock(curNode,
                    istCalcSum, "istcalcsum", oOptions.isFlgShowIst() && oOptions.isFlgShowChildrenSum());
            }

            // Plan-Daten
            blockPlan = this.genHtmlDataBlock(curNode, plan, "plan", oOptions.isFlgShowPlan());
            if (curNode.getChildNodes().size() > 0) {
                // nur Anzeigen, wenn auch Kindselemente vorhanden
                blockPlanCalcSum = this.genHtmlDataBlock(curNode,
                    planCalcSum, "plancalcsum", oOptions.isFlgShowPlan() && oOptions.isFlgShowChildrenSum());
            }
            // Block-Layout anpassen
            if ((ist == null || ist.length() <= 0)  
                && (plan == null || plan.length() <= 0)) {
                blockIst = "";
                blockPlan = "";
            }
            if ((istCalcSum == null || istCalcSum.length() <= 0) 
                && (planCalcSum == null || planCalcSum.length() <= 0)) {
                blockIstCalcSum = "";
                blockPlanCalcSum = "";
            }
        }

        // Result erzeugen
        res = "<div id='node_" + curNode.getSysUID() + "_master' data-pjebene='" + curNode.getEbene() + "' class=' yaio-pj-node-block " + styleClassesBlock + "'>\n"
            + "  <div id='node_" + curNode.getSysUID() + "_datacontainer' class=' yaio-pj-node-line-ue " + styleClassesUe + "'>\n"
            + "    <div id='node_" + curNode.getSysUID() + "_namecontainer' class=' yaio-pj-node-area-name-container'>\n"
            + "      <div id='node_" + curNode.getSysUID() + "_namecontainer2' class=' yaio-pj-node-area-name-container2 " + styleClassesNameContainer2 + "'>\n"
            + "        <div id='node_" + curNode.getSysUID() + "_stateshort' class=' yaio-pj-node-area-stateshort " + styleClassesStateShort + "'>-</div>\n"
            + "        <div id='node_" + curNode.getSysUID() + "_name' class=' yaio-pj-node-area-name " + styleClassesName + "'>"
            +            blockName + "</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "    <div id='node_" + curNode.getSysUID() + "_state' class=' yaio-pj-node-area-state " + styleClassesState + "'>" + labelState + "</div>\n"
//                    + "    <div id='node_" + curNode.getSysUID() + "_name' class=' yaio-pj-node-area-name " + styleClassesState + " " + styleClassesName + "'>" + labelState + " - " + blockName + "</div>"
            + "    <div id='node_" + curNode.getSysUID() + "_istplan' class=' yaio-pj-node-area-istplan " + styleClassesState + "'>" + blockIstCalcSum + blockPlanCalcSum + blockIst + blockPlan + "</div>\n"
            + "  </div>\n"
            + "  <div id='node_" + curNode.getSysUID() + "_desccontainer' class=' yaio-pj-node-container-desc'>" + blockDesc + "</div>\n";

        // Children einfuegen
        if (curNode.getChildNodes().size() > 0) {
            res +=  "  <div id='node_" + curNode.getSysUID() + "_childrencontainer' data-pjebene='" + curNode.getEbene() + "' class=' yaio-pj-node-container-children'>" + blockChildren + "</div>\n";
        }

//                // Trenner einfuegen
//                if (curNode.getLstChildProjektNodes().size() > 0) {
//                    res += "  <div id='node_" + curNode.getSysUID() + "_datacontainer_dummy' class=' yaio-pj-node-line-ue yaio-pj-node-line-trenner '>"
//                        + "    <div id='node_" + curNode.getSysUID() + "_state_dummy' class=' yaio-pj-node-area-state yaio-pj-node-trenner-area-state'>&nbsp;</div>"
//                        + "    <div id='node_" + curNode.getSysUID() + "_name_dummy' class=' yaio-pj-node-area-name " + styleClassesName + " yaio-pj-node-trenner-area-name'><span class=' yaio-pj-node-trenner-area-intend'>&nbsp;</span></div>"
//                        + "    <div id='node_" + curNode.getSysUID() + "_istplan' class=' yaio-pj-node-area-istplan '><span class=' yaio-pj-node-trenner-area-intend'>&nbsp;</span></div>"
//                        + "  </div>\n";
//                }

        // Block beenden
        res += "</div>\n";

        // falls Children: Toggler anfuegen
        if (curNode.getChildNodes().size() > 0 
            && blockChildren != null && blockChildren.length() > 0) {
            res += "<script type='text/javascript'>"
                +  "jMATService.getPageLayoutService().appendBlockToggler('node_" + curNode.getSysUID() + "_stateshort', 'node_" + curNode.getSysUID() + "_childrencontainer');"
                +  "</script>\n";
        }
        // falls Desc belegt: Toggler
        if (blockDesc.length() > 0) {
            res += "<script type='text/javascript'>"
                +  "jMATService.getPageLayoutService().appendBlockToggler('node_" + curNode.getSysUID() + "_name', 'node_" + curNode.getSysUID() + "_desccontainer');"
                +  "</script>\n";
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getSysUID() + " return datalength:" + res.length());
        }

        return res;
    }


    /** 
     * generate a html-block for the node
     * @param curNode                the node
     * @param pData                  the formatted data to export in html-block
     * @param dataName               the name for idFields
     * @param flgShow                true/false show the block or ignore it 
     * @return                       htmlblock of the node
     * @throws Exception             parser/format-Exceptions possible
     */
    public String genHtmlDataBlock(final BaseNode curNode, final String pData, 
                                   final String dataName, final boolean flgShow) {
        // Plan-Daten
        String res = "";
        String data = pData;
        if (flgShow) {
            String styleClasses = "";
            if (data != null && data.length() > 0) {
                styleClasses = "yaio-pj-node-block-" + dataName + "-set";
//                data = data.replaceAll(" ", "&nbsp;");
            } else {
                styleClasses = "yaio-pj-node-block-" + dataName + "-empty";
                data = "&nbsp;";
            }
            res = "<div id='node_" + curNode.getSysUID() +  "_" + dataName + "' "
                + " class=' yaio-pj-node-data-block yaio-pj-node-block-" + dataName + " " + styleClasses + "'>"
                + data + "</div>";
        }
        return res;
    }
        
    /** 
     * return the DocLoayoutTagCommand configured in the node<br>
     * if it is not set: CONST_LAYOUT_TAG_P<br>
     * if is has children: CONST_LAYOUT_TAG_UE
     * @param curNode                node for output recursively
     * @return                       the tagcommand
     * @throws Exception             parser/format-Exceptions possible
     */
    public String getDocLayoutTagCommand(final BaseNode curNode) {
        String command = curNode.getDocLayoutTagCommand();
        if (command == null || (!(command.length() > 0))) {
            // kein Tag definiert (Standard-Tags)
//            command = CONST_LAYOUT_TAG_P;
            command = "";
            if (curNode.getChildNodes().size() > 0) {
                // falls Kindselemente: Ue
//                command = CONST_LAYOUT_TAG_UE;
            }
        }
        return command;
    }
    
    
    /** 
     * format the descText as Markdown
     * @param descText               the string to format
     * @return                       formatted markdown
     * @throws IOException           IOException-Exceptions possible
     */
    public String formatTextAsMarkdown(final String descText) throws IOException {
        JshConfig config = new JshConfig();
        config.setStylePrefix("jsh-");
        config.setAppBaseVarName("ymfAppBase");

        String newDescText = markdownProvider.convertMarkdownToHtml(config, descText);
        // replace yaio-links
        newDescText = newDescText.replaceAll("href=\"yaio:",
                "href=\"" + "/yaio-explorerapp/yaio-explorerapp.html#/showByAllIds/");

        return newDescText;
    }

    /**
     * prepare the text to format as markdown
     * prefix empty lines inline code-segs (```) so that they will interprewted as codeline by markdown-parser
     * @param descText               the string to prepare
     * @return                       prpeared text to format as markdown
     */
    public String prepareTextForMarkdown(final String descText) {
        // prepare descText
        String newDescText = "";
        String newDescTextRest = DataUtils.htmlEscapeTextLazy(descText);
        newDescTextRest = newDescTextRest.replaceAll("\\&lt;br\\&gt;", "<br>");
        int codeStart = newDescTextRest.indexOf("```");
        while (codeStart >= 0) {
            // splice start and add to newDescText
            newDescText += newDescTextRest.substring(0, codeStart + 3);
            newDescTextRest = newDescTextRest.substring(codeStart + 3);
            
            int codeEnd = newDescTextRest.indexOf("```");
            if (codeEnd >= 0) {
                // splice all before ending ```
                String code = newDescTextRest.substring(0, codeEnd);
                newDescTextRest = newDescTextRest.substring(codeEnd);
                
                // replace empty lines in code
                code = code.replaceAll("\r\n", "\n");
                code = code.replaceAll("\n\r", "\n");
                code = code.replaceAll("\n[ \t]*\n", "\n.\n");
                code = code.replaceAll("\n\n", "\n.\n");
                
                // add code to newDescText
                newDescText += code;
                
                // extract ending ``` and add it to newDescText
                newDescText += newDescTextRest.substring(0, 3);
                newDescTextRest = newDescTextRest.substring(3);
            }
            codeStart = newDescTextRest.indexOf("```");
        }
        // add rest to newDescText
        newDescText += newDescTextRest;
        
        return newDescText;
    }

    /** 
     * search for the pattern and replace it with the replacementhead + htmlId + replacementTail
     * @param text                   the haystack
     * @param patternString          the needle to replace
     * @param replacementHead        the head before the new htmlelement-id
     * @param replacementTail        the tail after the new htmlelement-id
     * @return                       formatted diagramm-markdown
     */
    protected StringBuffer replaceDiagrammPattern(final String text, 
                                               final String patternString, 
                                               final String replacementHead, final String replacementTail) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, replacementHead + new Integer(htmlElementId++) + replacementTail);
        }
        matcher.appendTail(result);
        
        return result;
    }
}
