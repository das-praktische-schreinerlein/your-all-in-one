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
package de.yaio.datatransfer.importer.parser;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.importer.ImportOptions;

/**
 * <h4>FeatureDomain:</h4>
 *     import
 * <h4>FeatureDescription:</h4>
 *     interface with service-functions for parsing of dataDomain: DataDomain
 * 
 * @package de.yaio.datatransfer.importer.parser
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface BaseDataParser extends Parser {

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     parses DataDomain: DataDomain from the nodename
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue int - count elements found
     *     <li>updates memberVariable node.name - found Pattern are deleted
     *     <li>updates memberVariable node.* - found Pattern are set at MemberVars of the DataDomain
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Parser
     * @param node - DataDomain to parse
     * @param options - ImportOptionen for the parser
     * @return count elements found
     * @throws Exception - parser-Exceptions possible
     */
    int parseBaseDataFromName(DataDomain node, ImportOptions options) throws Exception;
}
