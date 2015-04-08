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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.importer.ImportOptions;

/**
 * <h4>FeatureDomain:</h4>
 *     import
 * <h4>FeatureDescription:</h4>
 *     interface with service-functions for parsing of dataDomains
 * 
 * @package de.yaio.datatransfer.importer.parser
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface Parser extends Comparable<Parser> {
    
    /** Pattern to parse Task-segments */
    String CONST_PATTERN_SEG_TASK = "__[A-Za-z]+?[0-9]+?__";
    /** Pattern to parse Aufwand-segments */
    String CONST_PATTERN_SEG_HOURS = "[0-9]?\\.?[0-9.]+";
    /** Pattern to parse Stand-segments */
    String CONST_PATTERN_SEG_STAND = "[0-9]?\\.?[0-9.]+";
    /** Pattern to parse Date-segments */
    String CONST_PATTERN_SEG_DATUM = "\\d\\d\\.\\d\\d.\\d\\d\\d\\d";
    /** Pattern to parse common String-segments */
    String CONST_PATTERN_SEG_STRING = "[-0-9\\p{L}/+_\\*\\. ]";
    /** Pattern to parse Flag-segments */
    String CONST_PATTERN_SEG_FLAG = "[-0-9\\p{L}+_]";
    /** Pattern to parse Integer-segments */
    String CONST_PATTERN_SEG_INT = "[0-9]";
    /** Pattern to parse UID-segments */
    String CONST_PATTERN_SEG_UID = "[0-9A-Za-z]";
    /** Pattern to parse ID-segments */
    String CONST_PATTERN_SEG_ID = "[0-9]";
    /** Pattern to parse Tag-segments */
    String CONST_PATTERN_SEG_TAGS = "[-0-9\\p{L}+_\\*\\.;]";
    /** Pattern to parse ID-Praefix-segments */
    String CONST_PATTERN_SEG_PRAEFIX = "[A-Za-z]";
    /** Pattern to parse Checksum-segments */
    String CONST_PATTERN_SEG_CHECKSUM = "[0-9A-Za-z]";
    /** Pattern to parse Time-segments */
    String CONST_PATTERN_SEG_TIME = "\\d\\d\\:\\d\\d";

    /** Pattern to validate Layoutcommands */
    String CONST_PATTERN_SEG_LAYOUTCOMMAND = "[-0-9A-Za-z_\\.]";
    /** Pattern to validate name */
    String CONST_PATTERN_SEG_NAME = "[\\p{L}\\p{M}\\{Z}\\p{S}\\p{N}\\p{P}\\p{Print}\\{Punct}\\p{Graph}\\p{Blank}]";
    /** Pattern to validate desc */
    String CONST_PATTERN_SEG_DESC = "[\\p{L}\\p{M}\\{Z}\\p{S}\\p{N}\\p{P}\\p{Print}\\{Punct}\\p{Graph}\\p{Blank}\\n\\r]";
    /** Pattern to validate styleclass */
    String CONST_PATTERN_SEG_STYLECLASS = "[-0-9A-Za-z_\\.]";
    /** Pattern to validate shortname */
    String CONST_PATTERN_SEG_SHORTNAME = "[-0-9\\p{L}+_\\*\\.,;' \\/\\?]";
    /** Pattern to validate state */
    String CONST_PATTERN_SEG_STATE = "[-A-Za-z_]";
    /** Pattern to validate type */
    String CONST_PATTERN_SEG_TYPE = "[-A-Za-z_]";;

    /** if second of time are set to this value -> then ignore the seconds */
    int CONST_FLAG_NODATE_SECONDS = 59;

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     returns the class of the DataDomain for which the parser runs
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Class - DataDomain for which the parser runs
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return DataDomain for which the parser runs
     */
    Class<?> getTargetClass();

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     returns position in the parser-queue
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue int - position in the parser-queue
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return position in the parser-queue
     */
    int getTargetOrder();

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     parses DataDomain-data from the nodename
     * <h4>FeatureConditions:</h4>
     *     must not be implemented direct, but should call separate function
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
    int parseFromName(DataDomain node, ImportOptions options) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     delete the matchingArea between first and last Matcher from node.name 
     *     -> node.name = first + last
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - brackets found?
     *     <li>updates memberVariable node.name - delete matching pattern
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Parser
     * @param node - node to search and update
     * @param pattern - Pattern
     * @param matcher - Matcher
     * @param first - first Matcher to set as new name
     * @param last - last Matcher to append to new name
     * @return brackets found ?
     * @throws Exception - parser-Exceptions possible
     */
    boolean trimNodeName(DataDomain node, Pattern pattern, 
            Matcher matcher, int first, int last) throws Exception;
}
