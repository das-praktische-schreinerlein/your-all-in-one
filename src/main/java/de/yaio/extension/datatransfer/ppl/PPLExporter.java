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
package de.yaio.extension.datatransfer.ppl;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.ExporterImpl;
import de.yaio.datatransfer.exporter.OutputOptions;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 * <h4>FeatureDescription:</h4>
 *     export of Nodes in PPL-format
 * 
 * @package de.yaio.extension.datatransfer..ppl
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class PPLExporter extends ExporterImpl {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(PPLExporter.class);


    public PPLExporter() {
        super();
    }

    ////////////////
    // service-functions to generate PPL from node
    ////////////////
    @Override
    public String getMasterNodeResult(DataDomain masterNode,
            OutputOptions oOptions) throws Exception {
        // Parameter pruefen
        if (masterNode == null) {
            throw new IllegalArgumentException("Masternode must not be null: '" + masterNode + "'");
        }

        return this.getNodeResult(masterNode, "", oOptions).toString();
    }

    @Override
    public StringBuffer getNodeResult(DataDomain node,
            String praefix, OutputOptions oOptions) throws Exception {
        // Parameter pruefen
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null: '" + node + "'");
        }

        // Daten formatieren
        StringBuffer res = new StringBuffer();
        formatNodeDataDomains(node, res, oOptions);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Praefix for node=" + node.getNameForLogger() 
                    + " praefix=" + praefix);

        // Praefix voranstellen
        if (praefix.length() > 0 ) {
            res.insert(0, praefix + PPLService.DEFAULT_ENTRY_DELIMITER);
        }

        // Praefix neu berechnen
        praefix = res.toString();

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Praefix for Children of node=" + node.getNameForLogger()
                    + " praefix=" + praefix);

        // Childs iterieren
        res.append("\n");
        for (BaseNode childNode : node.getChildNodes()) {
            res.append(getNodeResult(childNode, praefix, oOptions));
        }

        return res;
    }
}
