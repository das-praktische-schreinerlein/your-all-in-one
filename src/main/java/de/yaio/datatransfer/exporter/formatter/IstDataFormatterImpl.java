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
package de.yaio.datatransfer.exporter.formatter;

import java.util.Date;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.IstData;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.utils.Calculator;

/**
 * <h4>FeatureDomain:</h4>
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     service-functions for formatting of dataDomain: IstData
 * 
 * @package de.yaio.datatransfer.exporter.formatter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class IstDataFormatterImpl extends FormatterImpl implements IstDataFormatter {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(IstDataFormatterImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return IstData.class;
    }

    @Override
    public int getTargetOrder() {
        return IstData.CONST_ORDER;
    }


    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     add me as formatter for formatting DataDomains with (Exporter.formatNodeDataDomains) 
     *     to the Exporter-Config
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param exporter - instance of the Exporter which will use me
     */
    public static void configureDataDomainFormatter(Exporter exporter) {
        Formatter formatter = new IstDataFormatterImpl();
        exporter.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(DataDomain node, StringBuffer nodeOutput, 
                       OutputOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (! IstData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        formatIstData((IstData)node, nodeOutput, options);
    }

    @Override
    public void formatIstData(IstData node, StringBuffer nodeOutput, 
                              OutputOptions oOptions) throws Exception {
        // exit if Flg not set
        if (! oOptions.isFlgShowIst()) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("SKIP: isFlgShowIst not set for node:" 
                           + node.getNameForLogger());
            return;
        }

        // lets roll

        // Label-Itented fuer IstSum konfiurieren
        String labelIntend = "";
        if (oOptions.isFlgIntendSum() && oOptions != null && oOptions.isFlgShowChildrenSum()) {
            labelIntend = "   ";
        }

        // Daten einlesen
        Double stand = node.getIstStand();
        Double aufwand = node.getIstAufwand();
        Date start = node.getIstStart();
        Date ende = node.getIstEnde();
        String task = node.getIstTask();

        // Ausgabe erzeugen
        if ( (aufwand != null && aufwand >= Calculator.CONST_DOUBLE_NULL)
                || (stand != null && stand >= Calculator.CONST_DOUBLE_NULL)
                || start != null
                || ende != null
                || task != null) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Do: IstDataFormatter for Node:" + node.getNameForLogger());

            // Abstand
            if (nodeOutput.length() > 0)
                nodeOutput.append(" ");

            // Einrueckung
            if (oOptions.getIntendFuncArea() > 0) {
                while (nodeOutput.length() < oOptions.getIntendFuncArea()) {
                    nodeOutput.append(" ");
                }
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("[");
            }
            nodeOutput.append("Ist: ")
            .append(labelIntend)
            .append(this.intendLeft(stand.intValue(), (oOptions.isFlgDoIntend() ? 3 : 0)) + "% ")
            .append(this.intendLeft(this.formatNumber(aufwand, 0, 2), (oOptions.isFlgDoIntend() ? 2 : 0)) + "h");
            if (start != null || ende != null) {
                nodeOutput.append(" ");
                if (start != null) {
                    nodeOutput.append(formatDate(start));
                } else if (oOptions.isFlgDoIntend()) {
                    nodeOutput.append("          ");
                }
                if (ende != null) {
                    nodeOutput.append("-" + formatDate(ende));
                } else if (oOptions.isFlgDoIntend()) {
                    nodeOutput.append("-          ");
                }
            }
            if (task != null) {
                nodeOutput.append(" " + task);
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("]");
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SKIP: IstDataFormatter not IstData for Node:" + node.getNameForLogger());
        }
        
    }
}
