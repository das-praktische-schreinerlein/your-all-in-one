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

import java.util.Date;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.PlanData;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.utils.Calculator;

/** 
 * service-functions for formatting of dataDomain:PlanData
 * 
 * @FeatureDomain                Praesentation
 * @package                      de.yaio.datatransfer.exporter.formatter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class PlanDataFormatterImpl extends FormatterImpl implements PlanDataFormatter {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(PlanDataFormatterImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return PlanData.class;
    }

    @Override
    public int getTargetOrder() {
        return PlanData.CONST_ORDER;
    }


    /** 
     * add me as formatter for formatting DataDomains with (Exporter.formatNodeDataDomains) 
     * to the Exporter-Config
     * @FeatureDomain                DataExport Presentation
     * @FeatureKeywords              Config
     * @param exporter               instance of the Exporter which will use me
     */
    public static void configureDataDomainFormatter(final Exporter exporter) {
        Formatter formatter = new PlanDataFormatterImpl();
        exporter.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(final DataDomain node, final StringBuffer nodeOutput, 
                       final OutputOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (!PlanData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        formatPlanData((PlanData) node, nodeOutput, options);
    }

    @Override
    public void formatPlanData(final PlanData node, final StringBuffer nodeOutput, 
                               final OutputOptions oOptions) throws Exception {
        // exit if Flg not set
        if (oOptions == null || !oOptions.isFlgShowPlan()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: isFlgShowPlan not set for node:" + node.getNameForLogger());
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
        Double aufwand = node.getPlanAufwand();
        Date start = node.getPlanStart();
        Date ende = node.getPlanEnde();
        String task = node.getPlanTask();

        // Ausgabe erzeugen
        if (Calculator.isAufwand(aufwand)
            || start != null
            || ende != null
            || task != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Do: PlanDataFormatter for Node:" + node.getNameForLogger());
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
            nodeOutput.append("Plan: ")
                .append(labelIntend)
                .append(this.intendLeft(
                                this.formatNumber(aufwand, 0, 2), 
                                (oOptions.isFlgDoIntend() ? 6 : 0)) + "h");
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
            if (task != null && !"".equals(task) && !" ".equals(task)) {
                nodeOutput.append(" " + task);
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("]");
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SKIP: PlanDataFormatter not PlanData for Node:" + node.getNameForLogger());
        }
        
    }
}
