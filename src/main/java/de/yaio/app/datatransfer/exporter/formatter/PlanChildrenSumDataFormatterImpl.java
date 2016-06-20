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

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.datadomain.PlanChildrenSumData;
import de.yaio.app.datatransfer.exporter.Exporter;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.utils.Calculator;
import org.apache.log4j.Logger;

import java.util.Date;

/** 
 * service-functions for formatting of dataDomain:PlanChildrenSumData
 * 
 * @FeatureDomain                Praesentation
 * @package                      de.yaio.datatransfer.exporter.formatter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class PlanChildrenSumDataFormatterImpl extends FormatterImpl 
    implements PlanChildrenSumDataFormatter {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(PlanChildrenSumDataFormatterImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return PlanChildrenSumData.class;
    }

    @Override
    public int getTargetOrder() {
        return PlanChildrenSumData.CONST_ORDER;
    }


    /** 
     * add me as formatter for formatting DataDomains with (Exporter.formatNodeDataDomains) 
     * to the Exporter-Config
     * @param exporter               instance of the Exporter which will use me
     */
    public static void configureDataDomainFormatter(final Exporter exporter) {
        Formatter formatter = new PlanChildrenSumDataFormatterImpl();
        exporter.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(final DataDomain node, final StringBuffer nodeOutput,
                       final OutputOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (!PlanChildrenSumData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        formatPlanChildrenSumData((PlanChildrenSumData) node, nodeOutput, options);
    }

    @Override
    public void formatPlanChildrenSumData(final PlanChildrenSumData node, 
            final StringBuffer nodeOutput, final OutputOptions oOptions) throws Exception {
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
        Double aufwand = node.getPlanChildrenSumAufwand();
        Date start = node.getPlanChildrenSumStart();
        Date ende = node.getPlanChildrenSumEnde();

        // Ausgabe erzeugen
        if ((aufwand != null && aufwand >= Calculator.CONST_DOUBLE_NULL)
            || start != null || ende != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Do: PlanChildrenSumDataFormatter for Node:" + node.getNameForLogger());
            }

            // Abstand
            if (nodeOutput.length() > 0) {
                nodeOutput.append(" ");
            }

            // Einrueckung
            if (oOptions.getIntendFuncArea() > 0) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Do: IntendPlanPos Output " + nodeOutput.toString() 
                            + " for Node:" + node.getNameForLogger());
                }
                while (nodeOutput.length() < oOptions.getIntendFuncArea()) {
                    nodeOutput.append(" ");
                }
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("[");
            }
            nodeOutput.append("PlanSum: ")
            .append(labelIntend)
            .append(this.intendLeft(this.formatNumber(aufwand, 0, 2), (oOptions.isFlgDoIntend() ? 6 : 0)) + "h");
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
            LOGGER.debug("SKIP: PlanChildrenSumDataFormatter not PlanChildrenSumData for Node:" 
                    + node.getNameForLogger());
        }
    }
}
