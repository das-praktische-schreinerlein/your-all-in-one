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
package de.yaio.datatransfer.exporter.formatter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.MetaData;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;

/** 
 * service-functions for formatting of dataDomain: MetaData
 * 
 * @FeatureDomain                Praesentation
 * @package                      de.yaio.datatransfer.exporter.formatter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class MetaDataFormatterImpl extends FormatterImpl implements MetaDataFormatter {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(MetaDataFormatterImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return MetaData.class;
    }

    @Override
    public int getTargetOrder() {
        return MetaData.CONST_ORDER;
    }


    /** 
     * add me as formatter for formatting DataDomains with (Exporter.formatNodeDataDomains) 
     * to the Exporter-Config
     * @param exporter               instance of the Exporter which will use me
     */
    public static void configureDataDomainFormatter(final Exporter exporter) {
        Formatter formatter = new MetaDataFormatterImpl();
        exporter.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(final DataDomain node, final StringBuffer nodeOutput, 
                       final OutputOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (!MetaData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        formatMetaData((MetaData) node, nodeOutput, options);
    }

    @Override
    public void formatMetaData(final MetaData node, final StringBuffer nodeOutput, 
                               final OutputOptions oOptions) throws Exception {
        // exit if Flg not set
        if (oOptions == null || !oOptions.isFlgShowMetaData()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: isFlgShowMetaData not set for node:" + node.getNameForLogger());
            }
            return;
        }

        // lets roll

        // Label-Itented fuer IstSum konfiurieren
        String labelIntend = "";
        if (oOptions.isFlgIntendSum() && oOptions != null && oOptions.isFlgShowChildrenSum()) {
            labelIntend = "   ";
        }

        // Daten einlesen
        String praefix = node.getMetaNodePraefix();
        String id = node.getMetaNodeNummer();
        String nodeType = node.getMetaNodeTypeTags();
        String nodeSubType = node.getMetaNodeSubTypeTags();

        // Ausgabe erzeugen
        if (!StringUtils.isEmpty(praefix)
            || !StringUtils.isEmpty(id)
            || !StringUtils.isEmpty(nodeType)
            || !StringUtils.isEmpty(nodeSubType)
            ) {
            // Abstand
            if (nodeOutput.length() > 0) {
                nodeOutput.append(" ");
            }

            // Einrueckung
            if (oOptions.getIntendSys() > 0) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Do: IntendMetaPos Output " + nodeOutput.toString() 
                            + " for Node:" + node.getNameForLogger());
                }
                while (nodeOutput.length() < oOptions.getIntendSys()) {
                    nodeOutput.append(" ");
                }
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("[");
            }
            nodeOutput.append("NodeMeta: ").append(labelIntend);
            if (praefix != null) {
                nodeOutput.append(praefix);
            }
            nodeOutput.append(",");
            if (id != null) {
                nodeOutput.append(id);
            }
            nodeOutput.append(",");
            if (nodeType != null) {
                nodeOutput.append(nodeType);
            }
            nodeOutput.append(",");
            if (nodeSubType != null) {
                nodeOutput.append(nodeSubType);
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("]");
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SKIP: MetaDataFormatter not MetaData for Node:" + node.getNameForLogger());
        }
        
    }
}
