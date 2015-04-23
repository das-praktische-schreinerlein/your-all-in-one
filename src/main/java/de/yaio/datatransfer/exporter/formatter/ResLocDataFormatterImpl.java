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
import de.yaio.core.datadomain.ResLocData;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;

/**
 * <h4>FeatureDomain:</h4>
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *    service-functions for formatting of dataDomain: ResLocData
 * 
 * @package de.yaio.datatransfer.exporter.formatter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ResLocDataFormatterImpl extends FormatterImpl implements ResLocDataFormatter {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(ResLocDataFormatterImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return ResLocData.class;
    }

    @Override
    public int getTargetOrder() {
        return ResLocData.CONST_ORDER;
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
    public static void configureDataDomainFormatter(final Exporter exporter) {
        Formatter formatter = new ResLocDataFormatterImpl();
        exporter.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(final DataDomain node, final StringBuffer nodeOutput, 
                       final OutputOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (!ResLocData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        formatResLocData((ResLocData) node, nodeOutput, options);
    }

    @Override
    public void formatResLocData(final ResLocData node, final StringBuffer nodeOutput, 
                                 final OutputOptions oOptions) throws Exception {
        // exit if Flg not set
        if (!oOptions.isFlgShowResLoc()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: isFlgShowResLoc not set for node:" + node.getNameForLogger());
            }
            return;
        }

        // lets roll

        // Daten einlesen
        String resLocRef = node.getResLocRef();
        String resLocName = node.getResLocName();
        String resLocTags = node.getResLocTags();

        // Ausgabe erzeugen
        if (   (resLocRef != null && resLocRef.length() > 0)
                || (resLocName != null && resLocName.length() > 0)
                || (resLocTags != null && resLocTags.length() > 0)
                ) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Do: ResLocDataFormatter for Node:" + node.getNameForLogger());
            }

            // Abstand
            if (nodeOutput.length() > 0) {
                nodeOutput.append(" ");
            }

            // Einrueckung
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("[");
            }
            nodeOutput.append("ResLoc: ");
            if (resLocRef != null) {
                nodeOutput.append(resLocRef);
            }
            nodeOutput.append(",");
            if (resLocName != null) {
                nodeOutput.append(resLocName);
            }
            nodeOutput.append(",");
            if (resLocTags != null) {
                nodeOutput.append(resLocTags);
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("]");
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SKIP: ResLocDataFormatter not ResLocData for Node:" + node.getNameForLogger());
        }
        
    }
}
