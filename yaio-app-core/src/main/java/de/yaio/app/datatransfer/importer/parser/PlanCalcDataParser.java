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
package de.yaio.app.datatransfer.importer.parser;

import de.yaio.app.core.datadomain.PlanCalcData;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.importer.ImportOptions;

/** 
 * interface with service-functions for parsing of dataDomain: PlanCalcData
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer.parser
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface PlanCalcDataParser extends Parser {

    /** 
     * parses DataDomain: PlanCalcData from the nodename
     * @param node                   DataDomain to parse
     * @param options                ImportOptionen for the parser
     * @return                       count elements found
     * @throws ParserException       parser-Exceptions possible
     */
    int parsePlanCalcDataFromName(PlanCalcData node, ImportOptions options) throws ParserException;
}
