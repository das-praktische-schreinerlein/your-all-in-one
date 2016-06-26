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
import de.yaio.app.datatransfer.exporter.OutputOptions;

/** 
 * interface with service-functions for formatting of dataDomains
 * 
 * @FeatureDomain                Praesentation
 * @package                      de.yaio.datatransfer.exporter.formatter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface Formatter extends Comparable<Formatter> {
    /** if second of time are set to this value -> then ignore the seconds */
    int CONST_FLAG_NODATE_SECONDS = 59;
    
    /** 
     * returns the class of the DataDomain for which the formatter runs
     * @return                       DataDomain for which the formatter runs
     */
    Class<?> getTargetClass();

    /** 
     * returns position in the formatter-queue
     * @return                       position in the formatter-queue
     */
    int getTargetOrder();

    /** 
     * formats DomainData and appends output to StringBuffer nodeOutput
     * @param node                   node to be formatted
     * @param nodeOutput             to append the output
     * @param options                options for formatter
     * @throws Exception             parser/format-Exceptions possible
     */
    void format(DataDomain node, StringBuffer nodeOutput, OutputOptions options) throws Exception;
}
