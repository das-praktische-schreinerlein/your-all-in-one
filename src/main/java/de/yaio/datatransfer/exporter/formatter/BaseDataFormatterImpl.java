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

import de.yaio.core.datadomain.BaseData;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;

/**
 * <h4>FeatureDomain:</h4>
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     service-functions for formatting of dataDomain: DataDomain
 * 
 * @package de.yaio.datatransfer.exporter.formatter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class BaseDataFormatterImpl extends FormatterImpl implements BaseDataFormatter {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(BaseDataFormatterImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return DataDomain.class;
    }

    @Override
    public int getTargetOrder() {
        return DataDomain.CONST_ORDER;
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
        Formatter formatter = new BaseDataFormatterImpl();
        exporter.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(final DataDomain node, final StringBuffer nodeOutput, final OutputOptions options) throws Exception {
        formatBaseData((BaseData) node, nodeOutput, options);
    }

    @Override
    public void formatBaseData(final BaseData node, final StringBuffer nodeOutput, final OutputOptions options) throws Exception {
        // exit if Flg not set
        if (!options.isFlgShowName() && !options.isFlgShowType() && !options.isFlgShowState()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: isFlgShowName and isFlgShowState not set for node:" 
                        + node.getNameForLogger());
            }
            return;
        }
        
        // lets roll
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Do: BaseDataFormatter for Node:" + node.getNameForLogger());
        }
        if (options.isFlgShowName() && options.isFlgShowType() 
            && node.getName() != null && node.getType() != null) {
                nodeOutput.append(node.getType()).append(" - ").append(node.getName());
        } else if (options.isFlgShowName() && node.getName() != null) {
            nodeOutput.append(node.getName());
        } else if (options.isFlgShowType() && node.getType() != null) {
            nodeOutput.append(node.getType());
        }
    }

}
