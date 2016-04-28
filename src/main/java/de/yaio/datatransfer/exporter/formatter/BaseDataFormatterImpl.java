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

import de.yaio.core.datadomain.BaseData;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import org.apache.log4j.Logger;

/** 
 * service-functions for formatting of dataDomain: DataDomain
 * 
 * @FeatureDomain                Praesentation
 * @package                      de.yaio.datatransfer.exporter.formatter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
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
     * add me as formatter for formatting DataDomains with (Exporter.formatNodeDataDomains) 
     * to the Exporter-Config
     * @param exporter               instance of the Exporter which will use me
     */
    public static void configureDataDomainFormatter(final Exporter exporter) {
        Formatter formatter = new BaseDataFormatterImpl();
        exporter.addDataDomainFormatter(formatter);
    }

    @Override
    public void format(final DataDomain node, final StringBuffer nodeOutput, 
                       final OutputOptions options) throws Exception {
        formatBaseData((BaseData) node, nodeOutput, options);
    }

    @Override
    public void formatBaseData(final BaseData node, final StringBuffer nodeOutput, 
                               final OutputOptions options) throws Exception {
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
