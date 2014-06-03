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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Formatter;
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
    public static final String CONST_PATTERN_SEG_TASK = "__[A-Za-z]+?[0-9]+?__";
    public static final String CONST_PATTERN_SEG_HOURS = "[0-9]?\\.?[0-9.]+";
    public static final String CONST_PATTERN_SEG_STAND = "[0-9]?\\.?[0-9.]+";
    public static final String CONST_PATTERN_SEG_DATUM = "\\d\\d\\.\\d\\d.\\d\\d\\d\\d";
    public static final String CONST_PATTERN_SEG_STRING = "[-0-9ÜÖÄüöäß/A-Za-z+_\\*\\. ]";
    public static final String CONST_PATTERN_SEG_FLAG = "[-0-9ÜÖÄüöäßA-Za-z+_]";
    public static final String CONST_PATTERN_SEG_INT = "[0-9]";
    public static final String CONST_PATTERN_SEG_UID = "[0-9A-Za-z]";
    public static final String CONST_PATTERN_SEG_ID = "[0-9]";
    public static final String CONST_PATTERN_SEG_TAGS = "[-0-9ÜÖÄüöäß/A-Za-z+_\\*\\.;]";
    public static final String CONST_PATTERN_SEG_PRAEFIX = "[A-Za-z]";
    public static final String CONST_PATTERN_SEG_CHECKSUM = "[0-9A-Za-z]";
    public static final String CONST_PATTERN_SEG_TIME = "\\d\\d\\:\\d\\d";

    public static DateFormat DF = new SimpleDateFormat("dd.MM.yyyy");
    public static DateFormat TF = new SimpleDateFormat("HH:mm");
    public static DateFormat DTF = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    public static DateFormat UIDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static Formatter NF = new Formatter();

    public static final int CONST_FLAG_NODATE_SECONDS = 59;

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
    public Class<?> getTargetClass();

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
    public int getTargetOrder();

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
     * @throws Exception
     */
    public int parseFromName(DataDomain node, ImportOptions options) throws Exception;

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
     * @throws Exception
     */
    public boolean trimNodeName(DataDomain node, Pattern pattern, 
            Matcher matcher, int first, int last) throws Exception;
}
