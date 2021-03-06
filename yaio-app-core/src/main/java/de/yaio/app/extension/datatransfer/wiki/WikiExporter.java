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
package de.yaio.app.extension.datatransfer.wiki;

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.nodeservice.NodeService;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.exporter.ExporterImpl;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.datatransfer.exporter.OutputOptionsImpl;
import org.apache.log4j.Logger;

import java.util.Map;

/** 
 * export nodes in Wiki-Format
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.wiki
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class WikiExporter extends ExporterImpl {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(WikiExporter.class);


    /** 
     * service functions to export nodes as Wiki
     */
    public WikiExporter() {
        super();
    }

    ////////////////
    // service-functions to configure
    ////////////////
    /** 
     * generate helper-OutputOptions for generation of the Wiki-namearea (show only name+state)
     * @param baseOOptions           Default OutputOptions to override
     * @return                       OuputOptions for generation of the Wiki-namearea
     */
    public OutputOptions genOutputOptionsForNameArea(final OutputOptions baseOOptions) {
        OutputOptions options = new OutputOptionsImpl(baseOOptions);

        // alle Show ausschalten
        options.setAllFlgShow(false);

        // Name setzen, Status anhand Defaultwert
        options.setFlgShowName(true);
        options.setFlgShowState(baseOOptions.isFlgShowState());
        options.setFlgShowType(baseOOptions.isFlgShowType());

        return options;
    }

    /** 
     * generate helper-OutputOptions for generation of the Wiki-dataarea (show all, hide name+state)
     * @param baseOOptions           Default OutputOptions to override
     * @return                       OuputOptions for generation of the Wiki-dataarea
     */
    public OutputOptions genOutputOptionsForDataArea(final OutputOptions baseOOptions) {
        OutputOptions options = new OutputOptionsImpl(baseOOptions);

        // Name+Status ausschalten
        options.setFlgShowName(false);
        options.setFlgShowState(false);
        options.setFlgShowType(false);
        options.setFlgShowDescInNextLine(true);

        return options;
    }
    
    /** 
     * generate helper-OutputOptions for generation of the Wiki-descarea (show desc, hide all)
     * @param baseOOptions           Default OutputOptions to override
     * @return                       OuputOptions for generation of the Wiki-descarea
     */
    public OutputOptions genOutputOptionsForDescArea(final OutputOptions baseOOptions) {
        OutputOptions options = new OutputOptionsImpl(baseOOptions);

        // alle Show einschalten
        options.setAllFlgShow(false);

        // Name+Status ausschalten
        options.setFlgShowDesc(true);
        options.setFlgReEscapeDesc(false);
        options.setFlgTrimDesc(false);
        options.setFlgShowDescInNextLine(false);

        return options;
    }

    ////////////////
    // common export-functions
    ////////////////
    @Override
    public String getMasterNodeResult(final DataDomain pMasterNode,
            final OutputOptions oOptions) throws ConverterException {
        DataDomain masterNode = pMasterNode;
        // Parameter pruefen
        if (masterNode == null) {
            throw new IllegalArgumentException("Masternode must not be null: '" + masterNode + "'");
        }

        // Mastennode falls leer löschen
        Map<String, DataDomain> masterChilds = masterNode.getChildNodesByNameMap();
        if (masterChilds.size() == 1) {
            masterNode = (DataDomain) masterChilds.values().toArray()[0];
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("set ParentNode empty for new Masternode " + masterNode.getNameForLogger());
            }
            masterNode.setParentNode(null);
        }

        // recalcData
        if (oOptions.isFlgRecalc()) {
            // if set do it for all
            masterNode.recalcData(NodeService.RecalcRecurseDirection.CHILDREN);
        }
        

        return this.getNodeResult(masterNode, "", oOptions).toString();
    }


    ////////////////
    // service-functions to generate Wiki from node
    ////////////////
    @Override
    public StringBuffer getNodeResult(final DataDomain curNode,  final String praefix,
            final OutputOptions oOptions) throws ConverterException {
        StringBuffer res = new StringBuffer();

        // max. Ebene pruefen
        if (curNode.getEbene() > oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: Ebene " + curNode.getEbene() 
                        + " > MaxEbene " + oOptions.getMaxEbene()
                        + " for " + curNode.getNameForLogger());
            }
            return res;
        }
        
        // prepare for Export
        this.prepareNodeForExport(curNode, oOptions);
        
        // generate children 
        StringBuffer childRes = new StringBuffer();
        boolean flgChildMatched = false;
        if (curNode.getEbene() < oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Do Childs: Ebene " + curNode.getEbene() 
                        + " >= MaxEbene " + oOptions.getMaxEbene() 
                        + " Count:" + curNode.getChildNodesByNameMap().size() 
                        + " for " + curNode.getNameForLogger());
            }
            for (String nodeName : curNode.getChildNodesByNameMap().keySet()) {
                DataDomain childNode = curNode.getChildNodesByNameMap().get(nodeName);
                childRes.append(this.getNodeResult(childNode, "", oOptions));
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP Childs: Ebene " + curNode.getEbene() 
                        + " >= MaxEbene " + oOptions.getMaxEbene()
                        + " for " + curNode.getNameForLogger());
            }
        }
        // check if children matches (childRes filled)
        if (childRes.length() > 0) {
            flgChildMatched = true;
        }
        // check if I'am matching
        boolean flgMatchesFilter = this.isNodeMatchingFilter(curNode, oOptions);
        if (!(flgMatchesFilter || flgChildMatched)) {
            // sorry me and my children didnt match
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sorry me and my children didnt match"
                                + " - node:" + ((BaseNode) curNode).getWorkingId() 
                                + " flgMatchesFilter=" + flgMatchesFilter
                                + " flgChildMatched=" + flgChildMatched);
            }
            return res;
        }

        // Juhu we match
        
        // Namen generieren
        OutputOptions nameOOptions = genOutputOptionsForNameArea(oOptions);
        this.formatNodeDataDomains(curNode, res, nameOOptions);

        // falls Kindselemente und keine Ue dann hervorheben
        if (curNode.getEbene() < oOptions.getMaxEbene() 
                && curNode.getEbene() > oOptions.getMaxUeEbene()) {
            if (curNode.getChildNodesByNameMap().size() >= WikiService.CONST_MIN4UE
                    && curNode.getParentNode() != null) {
                res.insert(0, "**").append("**");
            }
        }

        // Einrueckung vorbereiten
        String strIntendPraefix = "";
        String strItend = "";
        for (int zaehler = 1; zaehler <= oOptions.getIntendLi(); zaehler++) {
            strItend = strItend + " ";
        }

        // Ue/Aufzaehlung voranstellen
        res.insert(0, " ");

        // pruefen ob innerhalb der Ue-Ebenen-Begrenzung
        boolean flgUe = false;
        if (curNode.getEbene() <= oOptions.getMaxUeEbene()) {
            // als Ue darstellen
            flgUe = true;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Do UE: Ebene " + curNode.getEbene() 
                        + " <= MaxUeEbene " + oOptions.getMaxUeEbene()
                        + " for " + curNode.getNameForLogger());
            }
            for (int zaehler = 1; zaehler <= curNode.getEbene(); zaehler++) {
                res.insert(0, "=");
                // falls > 1 einruecken
//                if (zaehler > 1) {
//                    strIntendPraefix = strItend + strIntendPraefix;
//                }
            }
            // Einrueckung voranstellen
            res.insert(0, strIntendPraefix);
        } else {
            // als Aufzaehlung darstellen
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Do LI: Ebene " + curNode.getEbene() 
                        + " > MaxUeEbene " + oOptions.getMaxUeEbene()
                        + " for " + curNode.getNameForLogger());
            }
            for (int zaehler = oOptions.getMaxUeEbene() + 1; zaehler <= curNode.getEbene(); zaehler++) {
                res.insert(0, "*");

                // falls > oOptions.maxUeEbene+1 einruecken
                if (zaehler > oOptions.getMaxUeEbene() + 1) {
                    strIntendPraefix = strItend + strIntendPraefix;
                }
            }
            // Einrueckung voranstellen
            res.insert(0, strIntendPraefix);
        }
        
        // TODO reescape name
//        res = res.replaceAll("<WLESC>", "\\");
//        res = res.replaceAll("<WLTAB>", "\t");

        // Daten ausgeben
        OutputOptions dataOOptions = genOutputOptionsForDataArea(oOptions);
        this.formatNodeDataDomains(curNode, res, dataOOptions);
        res.append("\n");
        
        // bei Ue Zeilenumbruch voranstellen
        if (flgUe) {
            res.insert(0, "\n");
        }
        
        // append generated children
        res.append(childRes);

        return res;
    }
}
