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
package de.yaio.extension.datatransfer.html;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.InfoNode;
import de.yaio.core.node.SymLinkNode;
import de.yaio.core.node.UrlResNode;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.datatransfer.exporter.formatter.DescDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.IstChildrenSumDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.IstDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.PlanChildrenSumDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.PlanDataFormatterImpl;
import de.yaio.extension.datatransfer.wiki.WikiExporter;
import de.yaio.utils.DataUtils;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     export of Nodes as Html
 * 
 * @package de.yaio.extension.datatransfer.html
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class HtmlExporter extends WikiExporter {
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     service functions to export nodes as Html
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the exporter
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     */
    public HtmlExporter() {
        super();
    }
    
    protected static String CONST_LAYOUT_TAG_DIV = "DIV";
    protected static String CONST_LAYOUT_TAG_UE = "UE";
    protected static String CONST_LAYOUT_TAG_P = "P";
    protected static String CONST_LAYOUT_TAG_LI = "LI";
    protected static String CONST_LAYOUT_TAG_TR = "TR";
    protected static String CONST_LAYOUT_TAG_DIRECT = "DIRECT";
    protected static String CONST_LAYOUT_TAG_ENDDIV = "ENDDIV";
    
    protected static String CONST_FORMATTER_DESC = DescDataFormatterImpl.class.getName();
    protected static String CONST_FORMATTER_IST = IstDataFormatterImpl.class.getName();
    protected static String CONST_FORMATTER_PLAN = PlanDataFormatterImpl.class.getName();
    protected static String CONST_FORMATTER_ISTCHILDRENSUM = IstChildrenSumDataFormatterImpl.class.getName();
    protected static String CONST_FORMATTER_PLANCHILDRENSUM = PlanChildrenSumDataFormatterImpl.class.getName();
    

    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(HtmlExporter.class);
    
    @Override
    public String getMasterNodeResult(DataDomain masterNode,
            OutputOptions oOptions) throws Exception {
        oOptions = genOutputOptionsForHtml(oOptions);
        return super.getMasterNodeResult(masterNode, oOptions);
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     * <h4>FeatureDescription:</h4>
     *     generate helper-OutputOptions for generation of the html
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue OutputOptions - OuputOptions for generation of the html
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Cofiguration helper
     * @param baseOOptions - Default OutputOptions to override
     * @return OuputOptions for generation of the html
     */
    public OutputOptions genOutputOptionsForHtml(OutputOptions baseOOptions) {
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
    public StringBuffer getNodeResult(DataDomain node,  String praefix,
            OutputOptions oOptions) throws Exception {
        StringBuffer res = new StringBuffer();

        // Template-Nodes ignorieren
//        if (TemplateNode.class.isInstance(curNode)) {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("SKIP TemplateNode node:" + curNode.getNameForLogger());
//            }
//            return res;
//        }
        
        BaseNode curNode = (BaseNode)node;

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
            && oOptions.isFlgProcessDocLayout() == true
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
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     formats recursively node in html-docu-format
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - formatted output of node-hirarchy
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param curNode - node for output recursively
     * @param oOptions - options for output (formatter)
     * @return - formatted output of node-hierarchy and DataDomains
     * @throws Exception - parser/format-Exceptions possible
     */
    public String genHtmlDokuLayoutForNode(BaseNode curNode,
        OutputOptions oOptions) throws Exception {
        String res = "";

        // max. Ebene pruefen
        if (curNode.getEbene() > oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + curNode.getWorkingId() 
                        + " ignore:" + curNode.getEbene() + ">" + oOptions.getMaxEbene());
            }
            return res;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getWorkingId() + " start processing");
        }

        // alle Kindselemente durchlaufen
        String blockChildren = "";
        boolean flgChildMatched = false;
        if (curNode.getEbene() < oOptions.getMaxEbene() 
                && curNode.getChildNodes().size() > 0) {
            // Elternblock: Zweig
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + curNode.getWorkingId() + " iterate subnodes:" 
                        + curNode.getChildNodes().size());
            }

            // Childnodes iterieren
            boolean flgListStarted = false;
            boolean flgTableStarted = false;
            boolean flgCloseNode = false;
            for (String nodeName : curNode.getChildNodesByNameMap().keySet()) {
                BaseNode node = (BaseNode)curNode.getChildNodesByNameMap().get(nodeName);

                // eventuelles Divs des vorherigen Nodes schließen
                if (flgCloseNode) {
                    blockChildren += "</div></div>\n\n\n";
                    flgCloseNode = false;
                }
                
                String command = this.getDocLayoutTagCommand(node);
                String docLayoutFlgCloseDiv = node.getDocLayoutFlgCloseDiv();
                docLayoutFlgCloseDiv = 
                        (docLayoutFlgCloseDiv == null ? "" : docLayoutFlgCloseDiv);

                //eventuelle Tabelle starten
                if (CONST_LAYOUT_TAG_TR.equalsIgnoreCase(command)) {
                    // aktuelle ChildNode ist Table
                    if (! flgTableStarted) {
                        // Liste starten
                        String addStyle = node.getDocLayoutAddStyleClass();
                        blockChildren += "<table class='table-portdesc"
                                      + (addStyle.length() > 0
                                          ? " table-portdesc-" + addStyle : "")
                                      +   "' id='table_" + node.getWorkingId() + "'>\n";
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
                    if (! flgListStarted) {
                        // Liste starten
                        String addStyle = node.getDocLayoutAddStyleClass();
                        blockChildren += "<ul class='ul-portdesc"
                                      + (addStyle.length() > 0
                                          ? " ul-portdesc-" + addStyle : "")
                                      +   "' id='ul_" + node.getWorkingId() + "'>\n";
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
                LOGGER.debug("node:" + curNode.getWorkingId() + " no subnodes");
            }
        }
        
        // check if children matches (childRes filled)
        if (blockChildren.length() > 0) {
            flgChildMatched = true;
        }
        // check if I'am matching
        boolean flgMatchesFilter = this.isNodeMatchingFilter(curNode, oOptions);
        if (! (flgMatchesFilter || flgChildMatched)) {
            // sorry me and my children didnt match
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sorry me and my children didnt match"
                                + " - node:" + curNode.getWorkingId() 
                                + " flgMatchesFilter=" + flgMatchesFilter
                                + " flgChildMatched=" + flgChildMatched);
            }
            return res;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getWorkingId() + " do processing"
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
            descFull = DataUtils.htmlEscapeText(descFull);
            descFull = descFull.replaceAll("\n", "<br>");
        }

        // Layout konfigurieren
        String layoutCommand = this.getDocLayoutTagCommand(curNode);
        String addStyle = curNode.getDocLayoutAddStyleClass();
        addStyle = (addStyle == null ? "" : addStyle);
        String shortName = curNode.getDocLayoutShortName();
        shortName = (shortName == null ? "" : shortName);

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
                ue = name.substring(0, pos+1);
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
                UrlResNode urlResNode = ((UrlResNode)curNode);
                
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
                
                if (UrlResNode.CONST_NODETYPE_IDENTIFIER_IMAGERES.equals(curNode.getState())) {
                    // Image
                    content += "<a href='" + urlResNode.getResLocRef() + "' target='_blank_'"
                        +   " class='a-img" + tagEbene + "-urlres "
                        +     (addStyle.length() > 0
                                ? " " + "a-img" + tagEbene + "-urlres-" + addStyle : "") + "'"
                        +   " id='a_" + curNode.getWorkingId() + "'>" 
                        + "<img src='" + urlResNode.getResLocRef() + "' "
                        + " class='img" + tagEbene + "-urlres "
                        + (addStyle.length() > 0
                            ? " " + "img" + tagEbene + "-urlres-" + addStyle : "") + "'"
                        +   " id='img_" + curNode.getWorkingId() + "' alt='" + label + "' title='" + label + "'></a>";
                } else {
                    // Url
                    content += "<a href='" + urlResNode.getResLocRef() + "' "
                        + " target='_blank' "
                        + " class='a" + tagEbene + "-urlres "
                        + (addStyle.length() > 0
                            ? " " + "a" + tagEbene + "-urlres-" + addStyle : "") + "'"
                        +   " id='a_" + curNode.getWorkingId() + "'>"
                        + label + "</a> ";
                }
            }

            if (CONST_LAYOUT_TAG_UE.equalsIgnoreCase(layoutCommand)) {
                // Ueberschrift
                String tag = "h" + tagEbene;
                res += "<" + tag + " class='" + tag + "-portdesc"
                    + (addStyle.length() > 0
                        ? " " + tag + "-portdesc-" + addStyle : "") + "'"
                    +   " id='" + tag + "_" + curNode.getWorkingId() + "'>"
                    + ue
                    + "</" + tag + ">\n";

                // optionaler Contentbereich
                if (content.length() > 0) {
                    tag = "p";
                    res += "<" + tag + " class='" + tag + "-portdesc"
                        + (addStyle.length() > 0
                            ? " " + tag + "-portdesc-" + addStyle : "") + "'"
                        +   " id='" + tag + "_" + curNode.getWorkingId() + "'>"
                        + "<span class='" + tag + "-portdesc-desc"
                        + (addStyle.length() > 0
                            ? " " + tag + "-portdesc-desc-" + addStyle : "") + "'"
                        +   " id='pSpanDesc_" + curNode.getWorkingId() + "'>"
                        + content
                        + "</span>";

                        // eventuelles SPAN fuer Detailbeschreibung
                        if (descFull != null && descFull.length() > 0) {
                            res +=  "<span class='" + tag + "-portdesc-descdetail"
                                + (addStyle.length() > 0
                                    ? " " + tag + "-portdesc-descdetail-" + addStyle : "") + "'"
                                +   " id='pSpanDescDetail_" + curNode.getWorkingId() + "'>"
                                + descFull
                                + "</span>";

                        }
                        res +=  "</" + tag + ">\n";
                }
            } else if (CONST_LAYOUT_TAG_LI.equalsIgnoreCase(layoutCommand)) {
                // Aufzaehlung
                String tag = "li";
                res += "<" + tag + " class='" + tag + "-portdesc"
                    + (addStyle.length() > 0
                        ? " " + tag + "-portdesc-" + addStyle : "") + "'"
                    +   " id='li_" + curNode.getWorkingId() + "'>"
                    + "<span class='" + tag + "-portdesc-ue"
                    + (addStyle.length() > 0
                        ? " " + tag + "-portdesc-ue-" + addStyle : "") + "'"
                    +   " id='liSpanUe_" + curNode.getWorkingId() + "'>"
                    + ue
                    + "</span>"
                    + "<span class='" + tag + "-portdesc-desc"
                    + (addStyle.length() > 0
                        ? " " + tag + "-portdesc-desc-" + addStyle : "") + "'"
                    +   " id='liSpanDesc_" + curNode.getWorkingId() + "'>"
                    + content
                    + "</span>";

                // eventuelles SPAN fuer Detailbeschreibung
                if (descFull != null && descFull.length() > 0) {
                    res +=  "<span class='" + tag + "-portdesc-descdetail"
                        + (addStyle.length() > 0
                            ? " " + tag + "-portdesc-descdetail-" + addStyle : "") + "'"
                        +   " id='liSpanDescDetail_" + curNode.getWorkingId() + "'>"
                        + descFull
                        + "</span>";

                }
                res +=  "</" + tag + ">\n";
            } else if (CONST_LAYOUT_TAG_TR.equalsIgnoreCase(layoutCommand)) {
                // Tabelle

                // TR Zeile oeffnen
                String tag = "tr";
                res += "<" + tag + " class='" + tag + "-portdesc"
                    + (addStyle.length() > 0
                        ? " " + tag + "-portdesc-" + addStyle : "") + "'"
                    +   " id='tr_" + curNode.getWorkingId() + "'>";

                // TD Spalten einfuegen
                tag = "td";
                String[] lstContent = name.split("\\|");
                for (int zaehler=0; zaehler < lstContent.length; zaehler++) {
                    // Formeln parsen
                    String tdId = "td_" + curNode.getWorkingId() + "_" + zaehler;
                    String tdContent = lstContent[zaehler];
                    tdContent = tdContent.replaceAll("=SUMCOL=",
                        "<script>calcColumns('" + tdId + "', 'SUM', '', '');</script>");

                    // TD-Spalte erzeugen
                    res += "<td class='"
                            + tag + "-portdesc " + tag + "-portdesc-" + zaehler
                            + (addStyle.length() > 0
                                ? " " + tag + "-portdesc-" + addStyle + " "
                                  + tag + "-portdesc-"+ zaehler + "-" + addStyle : "") + "'"
                        + " id='" + tdId + "'>"
                        + tdContent
                        + "</td>";
                }

                // TR Zeile schliessen
                tag = "tr";
                res += "</" + tag + ">\n";
            } else if (CONST_LAYOUT_TAG_DIV.equalsIgnoreCase(layoutCommand)) {
                // DIV
                res += "<div class='box box-portdesc add2toc-h1"
                    + (addStyle.length() > 0
                        ? " box-portdesc-" + addStyle : "") + "'"
                    + (shortName.length() > 0 ? " toclabel='" + shortName + "'" : "")
                    + " id='box_"+ curNode.getWorkingId() + "'>"
                    + "<div class='boxline boxline-ue" + tagEbene + " h" + tagEbene + "-portdesc"
                    + (addStyle.length() > 0
                        ? " h" + tagEbene + "-portdesc-" + addStyle : "") + "'"
                    + " id='ue_" + curNode.getWorkingId() + "'>"
                    + ue
                    + "</div>\n"
                    + "<div class='togglecontainer"
                    + (addStyle.length() > 0 ? " togglecontainer-" + addStyle : "") + "'"
                    + " id='detail_"+ curNode.getWorkingId() + "'>\n"
                    ;
            } else if (CONST_LAYOUT_TAG_ENDDIV.equalsIgnoreCase(layoutCommand)) {
                // ENDDIV
                res += "</div></div>\n\n\n";
            } else { 
                //if (CONST_LAYOUT_TAG_P.equalsIgnoreCase(layoutCommand) || 1 == 1) {
                // DEFAULT: Block
                String tag = "p";
                res += "<" + tag + " class='" + tag + "-portdesc"
                    + (addStyle.length() > 0
                        ? " " + tag + "-portdesc-" + addStyle : "") + "'"
                    +   " id='p_" + curNode.getWorkingId() + "'>"
                    + "<span class='" + tag + "-portdesc-ue"
                    + (addStyle.length() > 0
                        ? " " + tag + "-portdesc-ue-" + addStyle : "") + "'"
                    +   " id='pSpanUe_" + curNode.getWorkingId() + "'>"
                    + ue
                    + "</span>"
                    + "<span class='" + tag + "-portdesc-desc"
                    + (addStyle.length() > 0
                        ? " " + tag + "-portdesc-desc-" + addStyle : "") + "'"
                    +   " id='pSpanDesc_" + curNode.getWorkingId() + "'>"
                    + content
                    + "</span>"
                    ;

                // eventuelles SPAN fuer Detailbeschreibung
                if (descFull != null && descFull.length() > 0) {
                    res +=  "<span class='" + tag + "-portdesc-descdetail"
                        + (addStyle.length() > 0
                            ? " " + tag + "-portdesc-descdetail-" + addStyle : "") + "'"
                        +   " id='pSpanDescDetail_" + curNode.getWorkingId() + "'>"
                        + descFull
                        + "</span>";

                }
                res +=  "</" + tag + ">\n";
            }
        }

        // Kinds-Elemente anhaengen
        res += blockChildren;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getWorkingId() + " return datalength:" + res.length());
        }

        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     formats recursively node in html-project-format
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - formatted output of node-hirarchy
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param curNode - node for output recursively
     * @param oOptions - options for output (formatter)
     * @return - formatted output of node-hierarchy and DataDomains
     * @throws Exception - parser/format-Exceptions possible
     */
    public String genHtmlProjektLayoutForNode(BaseNode curNode,
        OutputOptions oOptions) throws Exception {
        String res = "";

        // max. Ebene pruefen
        if (curNode.getEbene() > oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + curNode.getWorkingId() 
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
            styleClassesState = "node-state-" + labelState;
        }
        styleClassesStateShort = "node-state-" + labelState;

        // pruefen ob innerhalb der Ue-Ebenen-Begrenzung
        if (curNode.getEbene() <= oOptions.getMaxUeEbene()) {
            // als Ue darstellen
            styleClassesUe = "node-style-ue node-style-ue" + curNode.getEbene();
        } else {
            // als Aufzaehlung darstellen
            styleClassesUe = "node-style-list node-style-list" + curNode.getEbene();
        }

        // alle Kindselemente durchlaufen
        // generate children 
        boolean flgChildMatched = false;
        if (curNode.getEbene() < oOptions.getMaxEbene() && curNode.getChildNodes().size() > 0) {
            // Elternblock: Zweig
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + curNode.getWorkingId() 
                        + " iterate subnodes:" + curNode.getChildNodes().size());
            }
            blockChildren = "";
            for (String nodeName : curNode.getChildNodesByNameMap().keySet()) {
                BaseNode node = (BaseNode) curNode.getChildNodesByNameMap().get(nodeName);
                blockChildren += this.getNodeResult(node, "", oOptions);
            }
            if (blockChildren.length() > 0) {
                blockChildren = "<div id='node_" + curNode.getWorkingId() +  "_cildren' class='node-block-children'>\n"
                                + blockChildren
                                + "</div>\n";
            }

            styleClassesUe += " node-type-twig node-type-twig" + curNode.getEbene();
            styleClassesBlock += " node-block-twig node-block-twig" + curNode.getEbene();
        } else {
            // Blatt
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + curNode.getWorkingId() + " no subnodes");
            }
            styleClassesUe += " node-type-leaf node-type-leaf" + curNode.getEbene();
            styleClassesBlock += " node-block-leaf node-block-leaf" + curNode.getEbene();
        }
        // check if children matches (childRes filled)
        if (blockChildren.length() > 0) {
            flgChildMatched = true;
        }
        // check if I'am matching
        boolean flgMatchesFilter = this.isNodeMatchingFilter(curNode, oOptions);
        if (! (flgMatchesFilter || flgChildMatched)) {
            // sorry me and my children didnt match
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sorry me and my children didnt match"
                                + " - node:" + curNode.getWorkingId() 
                                + " flgMatchesFilter=" + flgMatchesFilter
                                + " flgChildMatched=" + flgChildMatched);
            }
            return res;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getWorkingId() + " start processing"
                            + " flgMatchesFilter=" + flgMatchesFilter
                            + " flgChildMatched=" + flgChildMatched);
        }

        // Namen erzeugen
        String name = curNode.getName();
        name = name.replaceAll("<WLESC>", "\\");
        name = name.replaceAll("<WLTAB>", "\t");
        name = DataUtils.htmlEscapeText(name);
        blockName = name + " (" + curNode.getWorkingId() + ")";
        styleClassesNameContainer2 = " node-level" +  + curNode.getEbene();
        if (UrlResNode.class.isInstance(curNode)) {
            // URLRes
            UrlResNode symlinkNode = ((UrlResNode)curNode);
            
            // Label belegen
            String suffix = name;
            String label = symlinkNode.getResLocName();
            
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
                label = symlinkNode.getResLocRef();
            }
            
            blockName = "<a href='" + symlinkNode.getResLocRef() + "' "
                + " target='_blank' class='a-node-urlres'>"
                + label + "</a> " + suffix + " (" + curNode.getWorkingId() + ")";
        } else if (SymLinkNode.class.isInstance(curNode)) {
            // Symlink
            SymLinkNode symlinkNode = ((SymLinkNode)curNode);
            blockName = name + " (" + curNode.getWorkingId() + ")"
                + " &gt; " + "<a onclick=\"javascript:openNode('" 
                +    symlinkNode.getSymLinkRef() + "');return false;\""
                + " class='a-node-symlink'>"
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
            descFull = DataUtils.htmlEscapeText(descFull);
            descFull = descFull.replaceAll("\n", "<br>");
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
            if (   (ist == null || ist.length() <= 0)  
                && (plan == null || plan.length() <= 0)) {
                blockIst = "";
                blockPlan = "";
            }
            if (   (istCalcSum == null || istCalcSum.length() <=0) 
                && (planCalcSum == null || planCalcSum.length() <=0)) {
                blockIstCalcSum = "";
                blockPlanCalcSum = "";
            }
        }

        // Result erzeugen
        res = "<div id='node_" + curNode.getWorkingId() + "_master' data-pjebene='" + curNode.getEbene() + "' class='node-block " + styleClassesBlock + "'>\n"
            + "  <div id='node_" + curNode.getWorkingId() + "_datacontainer' class='node-line-ue " + styleClassesUe + "'>\n"
            + "    <div id='node_" + curNode.getWorkingId() + "_namecontainer' class='node-area-name-container'>\n"
            + "      <div id='node_" + curNode.getWorkingId() + "_namecontainer2' class='node-area-name-container2 " + styleClassesNameContainer2 + "'>\n"
            + "        <div id='node_" + curNode.getWorkingId() + "_stateshort' class='node-area-stateshort " + styleClassesStateShort + "'>-</div>\n"
            + "        <div id='node_" + curNode.getWorkingId() + "_name' class='node-area-name " + styleClassesName + "'>"
            +            blockName + "</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "    <div id='node_" + curNode.getWorkingId() + "_state' class='node-area-state " + styleClassesState + "'>" + labelState + "</div>\n"
//                    + "    <div id='node_" + curNode.getWorkingId() + "_name' class='node-area-name " + styleClassesState + " " + styleClassesName + "'>" + labelState + " - " + blockName + "</div>"
            + "    <div id='node_" + curNode.getWorkingId() + "_istplan' class='node-area-istplan " + styleClassesState + "'>" + blockIstCalcSum + blockPlanCalcSum + blockIst + blockPlan + "</div>\n"
            + "  </div>\n"
            + "  <div id='node_" + curNode.getWorkingId() + "_desccontainer' class='node-container-desc'>" + blockDesc + "</div>\n";

        // Children einfuegen
        if (curNode.getChildNodes().size() > 0) {
            res +=  "  <div id='node_" + curNode.getWorkingId() + "_childrencontainer' data-pjebene='" + curNode.getEbene() + "' class='node-container-children'>" + blockChildren + "</div>\n";
        }

//                // Trenner einfuegen
//                if (curNode.getLstChildProjektNodes().size() > 0) {
//                    res += "  <div id='node_" + curNode.getWorkingId() + "_datacontainer_dummy' class='node-line-ue node-line-trenner '>"
//                        + "    <div id='node_" + curNode.getWorkingId() + "_state_dummy' class='node-area-state node-trenner-area-state'>&nbsp;</div>"
//                        + "    <div id='node_" + curNode.getWorkingId() + "_name_dummy' class='node-area-name " + styleClassesName + " node-trenner-area-name'><span class='node-trenner-area-intend'>&nbsp;</span></div>"
//                        + "    <div id='node_" + curNode.getWorkingId() + "_istplan' class='node-area-istplan '><span class='node-trenner-area-intend'>&nbsp;</span></div>"
//                        + "  </div>\n";
//                }

        // Block beenden
        res += "</div>\n";

        // falls Children: Toggler anfuegen
        if (   curNode.getChildNodes().size() > 0 
            && blockChildren != null && blockChildren.length() > 0) {
            res += "<script type='text/javascript'>"
                +  "jMATService.getPageLayoutService().appendBlockToggler('node_" + curNode.getWorkingId() + "_stateshort', 'node_" + curNode.getWorkingId() + "_childrencontainer');"
                +  "</script>\n";
        }
        // falls Desc belegt: Toggler
        if (blockDesc.length() > 0) {
            res += "<script type='text/javascript'>"
                +  "jMATService.getPageLayoutService().appendBlockToggler('node_" + curNode.getWorkingId() + "_name', 'node_" + curNode.getWorkingId() + "_desccontainer');"
                +  "</script>\n";
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getWorkingId() + " return datalength:" + res.length());
        }

        return res;
    }


    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     generate a html-block for the node
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - htmlblock of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param curNode - the node
     * @param data - the formatted data to export in html-block
     * @param dataName - the name for idFields
     * @param flgShow - true/false show the block or ignore it 
     * @return - htmlblock of the node
     * @throws Exception - parser/format-Exceptions possible
     */
    public String genHtmlDataBlock(BaseNode curNode, String data, String dataName, boolean flgShow) {
        // Plan-Daten
        String res = "";
        if (flgShow) {
            String styleClasses = "";
            if (data != null && data.length() > 0) {
                styleClasses = "node-block-" + dataName + "-set";
//                data = data.replaceAll(" ", "&nbsp;");
            } else {
                styleClasses = "node-block-" + dataName + "-empty";
                data = "&nbsp;";
            }
            res = "<div id='node_" + curNode.getWorkingId() +  "_" + dataName + "' "
                + " class='node-data-block node-block-" + dataName + " " + styleClasses + "'>"
                + data + "</div>";
        }
        return res;
    }
        
    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     return the DocLoayoutTagCommand configured in the node<br>
     *     if it is not set: CONST_LAYOUT_TAG_P<br>
     *     if is has children: CONST_LAYOUT_TAG_UE
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - tagcommand
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param curNode - node for output recursively
     * @return - the tagcommand
     * @throws Exception - parser/format-Exceptions possible
     */
    public String getDocLayoutTagCommand(BaseNode curNode) {
        String command = curNode.getDocLayoutTagCommand();
        if (command == null || (! (command.length() > 0))) {
            // kein Tag definiert (Standard-Tags)
            command = CONST_LAYOUT_TAG_P;
            if (curNode.getChildNodes().size() > 0) {
                // falls Kindselemente: Ue
                command = CONST_LAYOUT_TAG_UE;
            }
        }
        return command;
    }
        
}
