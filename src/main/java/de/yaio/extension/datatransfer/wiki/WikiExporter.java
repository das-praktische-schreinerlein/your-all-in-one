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
package de.yaio.extension.datatransfer.wiki;

import java.util.Map;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.datatransfer.exporter.ExporterImpl;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     export of Nodes in Wiki-Format
 * 
 * @package de.yaio.extension.datatransfer.wiki
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class WikiExporter extends ExporterImpl {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(WikiExporter.class);


    public WikiExporter() {
        super();
    }

    ////////////////
    // service-functions to configure
    ////////////////
    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     * <h4>FeatureDescription:</h4>
     *     generate helper-OutputOptions for generation of the Wiki-namearea (show only name+state)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue OutputOptions - OuputOptions for generation of the Wiki-namearea
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Cofiguration helper
     * @param baseOOptions - Default OutputOptions to override
     * @return OuputOptions for generation of the Wiki-namearea
     */
    public OutputOptions genOutputOptionsForNameArea(OutputOptions baseOOptions) {
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
     * <h4>FeatureDomain:</h4>
     *     DataExport
     * <h4>FeatureDescription:</h4>
     *     generate helper-OutputOptions for generation of the Wiki-dataarea (show all, hide name+state)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue OutputOptions - OuputOptions for generation of the Wiki-dataarea
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Cofiguration helper
     * @param baseOOptions - Default OutputOptions to override
     * @return OuputOptions for generation of the Wiki-dataarea
     */
    public OutputOptions genOutputOptionsForDataArea(OutputOptions baseOOptions) {
        OutputOptions options = new OutputOptionsImpl(baseOOptions);

        // Name+Status ausschalten
        options.setFlgShowName(false);
        options.setFlgShowState(false);
        options.setFlgShowType(false);
        options.setFlgShowDescInNextLine(true);

        return options;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     * <h4>FeatureDescription:</h4>
     *     generate helper-OutputOptions for generation of the Wiki-descarea (show desc, hide all)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue OutputOptions - OuputOptions for generation of the Wiki-descarea
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Cofiguration helper
     * @param baseOOptions - Default OutputOptions to override
     * @return OuputOptions for generation of the Wiki-descarea
     */
    public OutputOptions genOutputOptionsForDescArea(OutputOptions baseOOptions) {
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
    public String getMasterNodeResult(DataDomain masterNode,
            OutputOptions oOptions) throws Exception {
        // Parameter pruefen
        if (masterNode == null) {
            throw new IllegalArgumentException("Masternode must not be null: '" 
        + masterNode + "'");
        }

        // Mastennode falls leer löschen
        Map<String, DataDomain> masterChilds = masterNode.getChildNodesByNameMap();
        if (masterChilds.size() == 1) {
            masterNode = (DataDomain)masterChilds.values().toArray()[0];
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("set ParentNode empty for new Masternode " 
                        + masterNode.getNameForLogger());
            }
            masterNode.setParentNode(null);
        }

        // recalcData
        masterNode.recalcData(NodeService.CONST_RECURSE_DIRECTION_CHILDREN);

        return this.getNodeResult(masterNode, "", oOptions).toString();
    }


    ////////////////
    // service-functions to generate Wiki from node
    ////////////////
    @Override
    public StringBuffer getNodeResult(DataDomain curNode,  String praefix,
            OutputOptions oOptions) throws Exception {
        StringBuffer res = new StringBuffer();

        // max. Ebene pruefen
        if (curNode.getEbene() > oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled())
                    LOGGER.debug("SKIP: Ebene " + curNode.getEbene() 
                            + " > MaxEbene " + oOptions.getMaxEbene()
                            + " for " + curNode.getNameForLogger());
            return res;
        }
        
        // prepare for Export
        this.prepareNodeForExport(curNode, oOptions);

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
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Do UE: Ebene " + curNode.getEbene() 
                        + " <= MaxUeEbene " + oOptions.getMaxUeEbene()
                        + " for " + curNode.getNameForLogger());
            for (int zaehler=1; zaehler <= curNode.getEbene(); zaehler++) {
                res.insert(0, "=");
                // falls > 1 einruecken
                if (zaehler > 1) {
                    //                    strIntendPraefix = strItend + strIntendPraefix;
                }
            }
            // Einrueckung voranstellen
            res.insert(0, strIntendPraefix);
        } else {
            // als Aufzaehlung darstellen
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Do LI: Ebene " + curNode.getEbene() 
                        + " > MaxUeEbene " + oOptions.getMaxUeEbene()
                        + " for " + curNode.getNameForLogger());
            for (int zaehler=oOptions.getMaxUeEbene()+1; zaehler <= curNode.getEbene(); zaehler++) {
                res.insert(0, "*");

                // falls > oOptions.maxUeEbene+1 einruecken
                if (zaehler > oOptions.getMaxUeEbene()+1) {
                    strIntendPraefix = strItend + strIntendPraefix;
                }
            }
            // Einrueckung voranstellen
            res.insert(0, strIntendPraefix);
        }

        // Daten ausgeben
        OutputOptions dataOOptions = genOutputOptionsForDataArea(oOptions);
        this.formatNodeDataDomains(curNode, res, dataOOptions);
        res.append("\n");
        
        // bei Ue Zeilenumbruch voranstellen
        if (flgUe)
            res.insert(0, "\n");

        // alle Kindselemente durchlaufen
        if (curNode.getEbene() < oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Do Childs: Ebene " + curNode.getEbene() 
                        + " >= MaxEbene " + oOptions.getMaxEbene() 
                        + " Count:" + curNode.getChildNodesByNameMap().size() 
                        + " for " + curNode.getNameForLogger());
            for (String nodeName : curNode.getChildNodesByNameMap().keySet()) {
                DataDomain childNode = curNode.getChildNodesByNameMap().get(nodeName);
                res.append(this.getNodeResult(childNode, "", oOptions));
            }
        } else {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("SKIP Childs: Ebene " + curNode.getEbene() 
                        + " >= MaxEbene " + oOptions.getMaxEbene()
                        + " for " + curNode.getNameForLogger());
        }

        return res;
    }
}
