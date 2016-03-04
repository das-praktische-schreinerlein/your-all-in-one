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

import de.yaio.core.datadomain.ResLocData;
import de.yaio.datatransfer.exporter.OutputOptions;

/** 
 * interface with service-functions for formatting of dataDomain: ResLocData
 * 
 * @FeatureDomain                Praesentation
 * @package                      de.yaio.datatransfer.exporter.formatter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface ResLocDataFormatter extends Formatter {

    /** 
     * formats DomainData: ResLocData and appends output to StringBuffer nodeOutput
     * @param node                   node to be formatted
     * @param nodeOutput             to append the output
     * @param options                options for formatter
     * @throws Exception             parser/format-Exceptions possible
     */
    void formatResLocData(ResLocData node, StringBuffer nodeOutput, OutputOptions options) throws Exception;
}
