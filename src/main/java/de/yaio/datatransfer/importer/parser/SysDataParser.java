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
package de.yaio.datatransfer.importer.parser;

import de.yaio.core.datadomain.SysData;
import de.yaio.datatransfer.importer.ImportOptions;

/** 
 * interface with service-functions for parsing of dataDomain: SysData
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer.parser
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface SysDataParser extends Parser {

    /** 
     * parses DataDomain: SysData from the nodename
     * @FeatureDomain                DataImport
     * @FeatureResult                returnValue int - count elements found
     * @FeatureResult                updates memberVariable node.name - found Pattern are deleted
     * @FeatureResult                updates memberVariable node.sys* - found Pattern are set at MemberVars of the DataDomain
     * @FeatureKeywords              Parser
     * @param node                   DataDomain to parse
     * @param options                ImportOptionen for the parser
     * @return                       count elements found
     * @throws Exception             parser-Exceptions possible
     */
    int parseSysDataFromName(SysData node, ImportOptions options) throws Exception;
}
