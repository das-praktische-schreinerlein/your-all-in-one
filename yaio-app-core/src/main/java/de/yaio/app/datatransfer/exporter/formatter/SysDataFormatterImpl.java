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
import de.yaio.app.core.datadomain.SysData;
import de.yaio.app.datatransfer.exporter.Exporter;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;

/** 
 * service-functions for formatting of dataDomain: SysData
 * 
 * @FeatureDomain                Praesentation
 * @package                      de.yaio.datatransfer.exporter.formatter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class SysDataFormatterImpl extends FormatterImpl implements SysDataFormatter {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(SysDataFormatterImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return SysData.class;
    }

    @Override
    public int getTargetOrder() {
        return SysData.CONST_ORDER;
    }

    /** 
     * add me as formatter for formatting DataDomains with (Exporter.formatNodeDataDomains) 
     * to the Exporter-Config
     * @param exporter               instance of the Exporter which will use me
     */
    public static void configureDataDomainFormatter(final Exporter exporter) {
        Formatter formatter = new SysDataFormatterImpl();
        exporter.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(final DataDomain node, final StringBuffer nodeOutput,
                       final OutputOptions options) {
        // Check if node is compatibel
        if (node != null) {
            if (!SysData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        formatSysData((SysData) node, nodeOutput, options);
    }

    @Override
    public void formatSysData(final SysData node, final StringBuffer nodeOutput, 
                              final OutputOptions oOptions) {
        // exit if Flg not set
        if (!oOptions.isFlgShowSysData()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: isFlgShowSysData not set for node:" + node.getNameForLogger());
            }
            return;
        }

        // lets roll

        // Daten einlesen
        String uid = node.getSysUID();
        Date created = node.getSysCreateDate();
        String checksum = node.getSysCurChecksum();
        Date changed = node.getSysChangeDate();
        Integer changedCount = node.getSysChangeCount();

        // Ausgabe erzeugen
        boolean flgEver = false;
        if (!StringUtils.isEmpty(uid)
            || (created != null)
            || !StringUtils.isEmpty(checksum)
            || (changed != null)
            || flgEver
            ) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Do: SysDataFormatter for Node:" + node.getNameForLogger());
            }

            // Abstand
            if (nodeOutput.length() > 0) {
                nodeOutput.append(" ");
            }

            // Einrueckung
            if (oOptions.getIntendSys() > 0) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Do: IntendSys Output " + nodeOutput.toString() 
                            + " for Node:" + node.getNameForLogger());
                }
                while (nodeOutput.length() < oOptions.getIntendSys()) {
                    nodeOutput.append(" ");
                }
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("[");
            }
            nodeOutput.append("NodeSys: "); 
            if (uid != null) {
                nodeOutput.append(uid);
            }
            nodeOutput.append(",");
            if (created != null) {
                nodeOutput.append(DTF.format(created));
            }
            nodeOutput.append(",");
            if (checksum != null) {
                nodeOutput.append(checksum);
            }
            nodeOutput.append(",");
            if (changed != null) {
                nodeOutput.append(DTF.format(changed));
            }
            nodeOutput.append(",");
            if (changedCount != null) {
                nodeOutput.append(changedCount);
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("]");
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SKIP: SysDataFormatter not SysData for Node:" + node.getNameForLogger());
        }
    }
}
