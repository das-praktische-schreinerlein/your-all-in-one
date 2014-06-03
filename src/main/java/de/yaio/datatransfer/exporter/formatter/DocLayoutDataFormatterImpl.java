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

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.DocLayoutData;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;

/**
 * <h4>FeatureDomain:</h4>
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     service-functions for formatting of dataDomain: DocLayoutData
 * 
 * @package de.yaio.datatransfer.exporter.formatter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
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


    public static void configureDataDomainFormatter(Exporter nodeFactory) {
        Formatter formatter = new DocLayoutDataFormatterImpl();
        nodeFactory.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(DataDomain node, StringBuffer nodeOutput, OutputOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (! DocLayoutData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        formatDocLayoutData((DocLayoutData)node, nodeOutput, options);
    }

    @Override
    public void formatDocLayoutData(DocLayoutData node, StringBuffer nodeOutput, OutputOptions oOptions) throws Exception {
        // exit if Flg not set
        if (! oOptions.isFlgShowDocLayout()) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("SKIP: isFlgShowDocLayout not set for node:" + node.getNameForLogger());
            return;
        }

        // lets roll

        // Daten einlesen
        String docLayoutTagCommand = node.getDocLayoutTagCommand();
        String docLayoutAddStyleClass = node.getDocLayoutAddStyleClass();
        String docLayoutShortName = node.getDocLayoutShortName();
        String docLayoutFlgCloseDiv = node.getDocLayoutFlgCloseDiv();

        // Ausgabe erzeugen
        if (   (docLayoutTagCommand != null && docLayoutTagCommand.length() > 0)
                || (docLayoutAddStyleClass != null && docLayoutAddStyleClass.length() > 0)
                || (docLayoutShortName != null && docLayoutShortName.length() > 0)
                || (docLayoutFlgCloseDiv != null && docLayoutFlgCloseDiv.length() > 0)
                ) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Do: DocLayoutDataFormatter for Node:" + node.getNameForLogger());

            // Abstand
            if (nodeOutput.length() > 0)
                nodeOutput.append(" ");

            // Einrueckung
            if (oOptions.getIntendFuncArea() > 0) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Do: IntendDocLayoutPos Output " + nodeOutput.toString() 
                            + " for Node:" + node.getNameForLogger());
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
