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
package de.yaio.app.datatransfer.exporter.formatter;

import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.datadomain.IstChildrenSumData;
import de.yaio.app.datatransfer.exporter.Exporter;
import de.yaio.app.core.utils.Calculator;
import org.apache.log4j.Logger;

import java.util.Date;

/** 
 * service-functions for formatting of dataDomain: IstChildrenSumData
 * 
 * @FeatureDomain                Praesentation
 * @package                      de.yaio.datatransfer.exporter.formatter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class IstChildrenSumDataFormatterImpl extends FormatterImpl 
    implements IstChildrenSumDataFormatter {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(IstChildrenSumDataFormatterImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return IstChildrenSumData.class;
    }

    @Override
    public int getTargetOrder() {
        return IstChildrenSumData.CONST_ORDER;
    }


    /** 
     * add me as formatter for formatting DataDomains with (Exporter.formatNodeDataDomains) 
     * to the Exporter-Config
     * @param exporter               instance of the Exporter which will use me
     */
    public static void configureDataDomainFormatter(final Exporter exporter) {
        Formatter formatter = new IstChildrenSumDataFormatterImpl();
        exporter.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(final DataDomain node, final StringBuffer nodeOutput, 
                       final OutputOptions options) {
        // Check if node is compatibel
        if (node != null) {
            if (!IstChildrenSumData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        formatIstChildrenSumData((IstChildrenSumData) node, nodeOutput, options);
    }

    @Override
    public void formatIstChildrenSumData(final IstChildrenSumData node, 
            final StringBuffer nodeOutput, final OutputOptions oOptions) {
        // exit if Flg not set
        if (oOptions == null || !oOptions.isFlgShowChildrenSum()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: isFlgShowChildrenSum not set for node:" + node.getNameForLogger());
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
        Double stand = node.getIstChildrenSumStand();
        Double aufwand = node.getIstChildrenSumAufwand();
        Date start = node.getIstChildrenSumStart();
        Date ende = node.getIstChildrenSumEnde();

        // Ausgabe erzeugen
        if (Calculator.isAufwand(aufwand)
            || Calculator.isStand(stand)
            || start != null
            || ende != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Do: IstChildrenSumDataFormatter for Node:" + node.getNameForLogger());
            }

            // Abstand
            if (nodeOutput.length() > 0) {
                nodeOutput.append(" ");
            }

            // Einrueckung
            if (oOptions.getIntendFuncArea() > 0) {
                while (nodeOutput.length() < oOptions.getIntendFuncArea()) {
                    nodeOutput.append(" ");
                }
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("[");
            }
            int intStand = 0;
            if (stand != null) {
                intStand = stand.intValue();
            }
            nodeOutput.append("IstSum: ")
                .append(labelIntend)
                .append(this.intendLeft(intStand, (oOptions.isFlgDoIntend() ? 3 : 0)) + "% ")
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
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("]");
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SKIP: IstChildrenSumDataFormatter not IstData for Node:" + node.getNameForLogger());
        }
    }
}
