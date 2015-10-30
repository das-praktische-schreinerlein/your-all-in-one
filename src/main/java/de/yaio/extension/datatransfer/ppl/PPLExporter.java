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
package de.yaio.extension.datatransfer.ppl;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.ExporterImpl;
import de.yaio.datatransfer.exporter.OutputOptions;

/** 
 * export of Nodes in PPL-format
 * 
 * @FeatureDomain                DatenExport
 * @package                      de.yaio.extension.datatransfer..ppl
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class PPLExporter extends ExporterImpl {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(PPLExporter.class);


    /** 
     * service functions to export nodes as PPL
     * @FeatureDomain                Constructor
     * @FeatureResult                initialize the exporter
     * @FeatureKeywords              Constructor
     */
    public PPLExporter() {
        super();
    }

    ////////////////
    // service-functions to generate PPL from node
    ////////////////
    @Override
    public String getMasterNodeResult(final DataDomain masterNode,
            final OutputOptions oOptions) throws Exception {
        // Parameter pruefen
        if (masterNode == null) {
            throw new IllegalArgumentException("Masternode must not be null: '" + masterNode + "'");
        }

        return this.getNodeResult(masterNode, "", oOptions).toString();
    }

    @Override
    public StringBuffer getNodeResult(final DataDomain node,
            final String pPraefix, final OutputOptions oOptions) throws Exception {
        // Parameter pruefen
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null: '" + node + "'");
        }
        String praefix = pPraefix;

        // Daten formatieren
        StringBuffer res = new StringBuffer();
        oOptions.setFlgShowDescWithUe(true);
        formatNodeDataDomains(node, res, oOptions);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Praefix for node=" + node.getNameForLogger() 
                    + " praefix=" + praefix);
        }
        
        // escape and reinit res
        String dummyText = res.toString();
        dummyText = dummyText.replaceAll("\n", "<WLBR>");
        dummyText = dummyText.replaceAll("\t", "<WLTAB>");
        //dummyText = dummyText.replaceAll("\\", "<WLESC>");
        res = new StringBuffer(dummyText);

        // Praefix voranstellen
        if (praefix.length() > 0) {
            res.insert(0, praefix + PPLService.DEFAULT_ENTRY_DELIMITER);
        }

        // Praefix neu berechnen
        praefix = res.toString();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Praefix for Children of node=" + node.getNameForLogger()
                    + " praefix=" + praefix);
        }

        // Childs iterieren
        res.append("\n");
        for (BaseNode childNode : node.getChildNodes()) {
            res.append(getNodeResult(childNode, praefix, oOptions));
        }

        return res;
    }
}
