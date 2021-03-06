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
import de.yaio.app.core.datadomain.IstData;
import de.yaio.app.core.utils.Calculator;
import de.yaio.app.datatransfer.exporter.Exporter;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import org.apache.log4j.Logger;

import java.util.Date;

/** 
 * service-functions for formatting of dataDomain: IstData
 * 
 * @FeatureDomain                Praesentation
 * @package                      de.yaio.datatransfer.exporter.formatter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
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
     * add me as formatter for formatting DataDomains with (Exporter.formatNodeDataDomains) 
     * to the Exporter-Config
     * @param exporter               instance of the Exporter which will use me
     */
    public static void configureDataDomainFormatter(final Exporter exporter) {
        Formatter formatter = new IstDataFormatterImpl();
        exporter.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(final DataDomain node, final StringBuffer nodeOutput, 
                       final OutputOptions options) {
        // Check if node is compatibel
        if (node != null) {
            if (!IstData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        formatIstData((IstData) node, nodeOutput, options);
    }

    @Override
    public void formatIstData(final IstData node, final StringBuffer nodeOutput, 
                              final OutputOptions oOptions) {
        // exit if Flg not set
        if (oOptions == null || !oOptions.isFlgShowIst()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: isFlgShowIst not set for node:" 
                           + node.getNameForLogger());
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
        Double stand = node.getIstStand();
        Double aufwand = node.getIstAufwand();
        Date start = node.getIstStart();
        Date ende = node.getIstEnde();
        String task = node.getIstTask();

        // Ausgabe erzeugen
        if (Calculator.isAufwand(aufwand)
            || Calculator.isStand(stand)
            || start != null
            || ende != null
            || task != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Do: IstDataFormatter for Node:" + node.getNameForLogger());
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
            nodeOutput.append("Ist: ")
                .append(labelIntend)
                .append(this.intendLeft(intStand, (oOptions.isFlgDoIntend() ? 3 : 0)) + "%");
            
            // set aufwand=0 if null and if start or end is set
            if (aufwand == null && (start != null || ende != null)) {
                aufwand = 0.0; 
            }
            if (aufwand != null) {
                nodeOutput.append(" " + this.intendLeft(
                   this.formatNumber(aufwand, 0, 2), (oOptions.isFlgDoIntend() ? 2 : 0)) + "h");
            }
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
            LOGGER.debug("SKIP: IstDataFormatter not IstData for Node:" + node.getNameForLogger());
        }
        
    }
}
