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
import de.yaio.core.datadomain.DescData;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;

/**
 * <h4>FeatureDomain:</h4>
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     service-functions for formatting of dataDomain: DescData
 * 
 * @package de.yaio.datatransfer.exporter.formatter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class DescDataFormatterImpl extends FormatterImpl implements DescDataFormatter {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(DescDataFormatterImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return DescData.class;
    }

    @Override
    public int getTargetOrder() {
        return DescData.CONST_ORDER;
    }


    public static void configureDataDomainFormatter(Exporter nodeFactory) {
        Formatter formatter = new DescDataFormatterImpl();
        nodeFactory.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(DataDomain node, StringBuffer nodeOutput, OutputOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (! DescData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        formatDescData((DescData)node, nodeOutput, options);
    }

    @Override
    public void formatDescData(DescData node, StringBuffer nodeOutput, 
            OutputOptions oOptions) throws Exception {
        // exit if Flg not set
        if (! oOptions.isFlgShowDesc()) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("SKIP: isFlgShowDesc not set for node:" + node.getNameForLogger());
            return;
        }

        // lets roll

        String dummyText = node.getNodeDesc();
        if (dummyText != null) {
            // Zeilenumbruch
            if (oOptions.isFlgShowDescInNextLine()) {
                nodeOutput.append("\n");
            }
            
            // Sonderzeichen entfernen
            if (dummyText != null && oOptions.isFlgReEscapeDesc()) {
                dummyText = dummyText.replaceAll("<WLESC>", "\\");
                dummyText = dummyText.replaceAll("<WLBR>", "\n");
                dummyText = dummyText.replaceAll("<WLTAB>", "\t");
            }
    
            // Trimmen
            if (dummyText != null && oOptions.isFlgTrimDesc()) {
                dummyText = dummyText.replaceAll("\n\n\n", "\n\n");
                dummyText = dummyText.replaceAll("\n\n\n", "\n\n");
                dummyText = dummyText.replaceAll("\n\n\n", "\n\n");
                dummyText = dummyText.replaceAll("\n\n\n", "\n\n");
                dummyText = dummyText.replaceAll("\n\n\n", "\n\n");
                dummyText = dummyText.trim();
            }
    
            // Ue voranstellen
            if (dummyText != null && dummyText.length() > 0) {
                if (oOptions.isFlgShowDescWithUe()) {
                    dummyText = "ProjektDesc: " + dummyText;
                }
            }
            
            nodeOutput.append(dummyText);
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SKIP: DescDataFormatter not DescData for Node:" + node.getNameForLogger());
        }
    }
}
