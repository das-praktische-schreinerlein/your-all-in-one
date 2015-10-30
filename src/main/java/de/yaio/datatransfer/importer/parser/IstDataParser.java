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

import de.yaio.core.datadomain.IstData;
import de.yaio.datatransfer.importer.ImportOptions;

/** 
 * interface with service-functions for parsing of dataDomain: IstData
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer.parser
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface IstDataParser extends Parser {

    /** 
     * parses DataDomain: IstData from the nodename
     * @FeatureDomain                DataImport
     * @FeatureResult                returnValue int - count elements found
     * @FeatureResult                updates memberVariable node.name - found Pattern are deleted
     * @FeatureResult                updates memberVariable node.ist* - found Pattern are set at MemberVars of the DataDomain
     * @FeatureKeywords              Parser
     * @param node                   DataDomain to parse
     * @param options                ImportOptionen for the parser
     * @return                       count elements found
     * @throws Exception             parser-Exceptions possible
     */
    int parseIstDataFromName(IstData node, ImportOptions options) throws Exception;
}
