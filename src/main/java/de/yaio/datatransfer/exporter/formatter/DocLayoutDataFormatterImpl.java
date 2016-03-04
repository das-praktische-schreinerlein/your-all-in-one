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
import de.yaio.core.datadomain.DocLayoutData;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;

/** 
 * service-functions for formatting of dataDomain: DocLayoutData
 * 
 * @FeatureDomain                Praesentation
 * @package                      de.yaio.datatransfer.exporter.formatter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class DocLayoutDataFormatterImpl extends FormatterImpl implements DocLayoutDataFormatter {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(DocLayoutDataFormatterImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return DocLayoutData.class;
    }

    @Override
    public int getTargetOrder() {
        return DocLayoutData.CONST_ORDER;
    }


    /** 
     * add me as formatter for formatting DataDomains with (Exporter.formatNodeDataDomains) 
     * to the Exporter-Config
     * @param exporter               instance of the Exporter which will use me
     */
    public static void configureDataDomainFormatter(final Exporter exporter) {
        Formatter formatter = new DocLayoutDataFormatterImpl();
        exporter.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(final DataDomain node, final StringBuffer nodeOutput, 
                       final OutputOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (!DocLayoutData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        formatDocLayoutData((DocLayoutData) node, nodeOutput, options);
    }

    @Override
    public void formatDocLayoutData(final DocLayoutData node, final StringBuffer nodeOutput, 
                                    final OutputOptions oOptions) throws Exception {
        // exit if Flg not set
        if (!oOptions.isFlgShowDocLayout()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: isFlgShowDocLayout not set for node:" + node.getNameForLogger());
            }
            return;
        }

        // lets roll

        // Daten einlesen
        String docLayoutTagCommand = node.getDocLayoutTagCommand();
        String docLayoutAddStyleClass = node.getDocLayoutAddStyleClass();
        String docLayoutShortName = node.getDocLayoutShortName();
        String docLayoutFlgCloseDiv = node.getDocLayoutFlgCloseDiv();

        // Ausgabe erzeugen
        if (!StringUtils.isEmpty(docLayoutTagCommand)
            || !StringUtils.isEmpty(docLayoutAddStyleClass)
            || !StringUtils.isEmpty(docLayoutShortName)
            || !StringUtils.isEmpty(docLayoutFlgCloseDiv)
            ) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Do: DocLayoutDataFormatter for Node:" + node.getNameForLogger());
            }

            // Abstand
            if (nodeOutput.length() > 0) {
                nodeOutput.append(" ");
            }

            // Einrueckung
            if (oOptions.getIntendFuncArea() > 0) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Do: IntendDocLayoutPos Output " + nodeOutput.toString() 
                            + " for Node:" + node.getNameForLogger());
                }
                while (nodeOutput.length() < oOptions.getIntendFuncArea()) {
                    nodeOutput.append(" ");
                }
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("[");
            }
            nodeOutput.append("DocLayout: ");
            if (docLayoutTagCommand != null) {
                nodeOutput.append(docLayoutTagCommand);
            }
            nodeOutput.append(",");
            if (docLayoutAddStyleClass != null) {
                nodeOutput.append(docLayoutAddStyleClass);
            }
            nodeOutput.append(",");
            if (docLayoutShortName != null) {
                nodeOutput.append(docLayoutShortName);
            }
            nodeOutput.append(",");
            if (docLayoutFlgCloseDiv != null) {
                nodeOutput.append(docLayoutFlgCloseDiv);
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("]");
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SKIP: DocLayoutDataFormatter not DocLayoutData for Node:" + node.getNameForLogger());
        }
        
    }
}
