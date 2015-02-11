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
import de.yaio.core.datadomain.SymLinkData;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;

/**
 * <h4>FeatureDomain:</h4>
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     service-functions for formatting of dataDomain: SymLinkData
 * 
 * @package de.yaio.datatransfer.exporter.formatter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class SymLinkDataFormatterImpl extends FormatterImpl implements SymLinkDataFormatter {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(SymLinkDataFormatterImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return SymLinkData.class;
    }

    @Override
    public int getTargetOrder() {
        return SymLinkData.CONST_ORDER;
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
        Formatter formatter = new SymLinkDataFormatterImpl();
        exporter.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(final DataDomain node, final StringBuffer nodeOutput, final OutputOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (! SymLinkData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        formatSymLinkData((SymLinkData) node, nodeOutput, options);
    }

    @Override
    public void formatSymLinkData(final SymLinkData node, final StringBuffer nodeOutput, final OutputOptions oOptions) throws Exception {
        // exit if Flg not set
        if (! oOptions.isFlgShowSymLink()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: isFlgShowSymLink not set for node:" + node.getNameForLogger());
            }
            return;
        }

        // lets roll

        // Daten einlesen
        String symLinkRef = node.getSymLinkRef();
        String symLinkName = node.getSymLinkName();
        String symLinkTags = node.getSymLinkTags();

        // Ausgabe erzeugen
        if (   (symLinkRef != null && symLinkRef.length() > 0)
                || (symLinkName != null && symLinkName.length() > 0)
                || (symLinkTags != null && symLinkTags.length() > 0)
                ) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Do: SymLinkDataFormatter for Node:" + node.getNameForLogger());
            }

            // Abstand
            if (nodeOutput.length() > 0) {
                nodeOutput.append(" ");
            }

            // Einrueckung
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("[");
            }
            nodeOutput.append("SymLink: ");
            if (symLinkRef != null) {
                nodeOutput.append(symLinkRef);
            }
            nodeOutput.append(",");
            if (symLinkName != null) {
                nodeOutput.append(symLinkName);
            }
            nodeOutput.append(",");
            if (symLinkTags != null) {
                nodeOutput.append(symLinkTags);
            }
            if (oOptions.isFlgShowBrackets()) {
                nodeOutput.append("]");
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SKIP: SymLinkDataFormatter not SymLinkData for Node:" + node.getNameForLogger());
        }
        
    }
}
