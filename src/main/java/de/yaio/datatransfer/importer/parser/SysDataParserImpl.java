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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.SysData;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.NodeFactory;

/**
 * <h4>FeatureDomain:</h4>
 *     import
 * <h4>FeatureDescription:</h4>
 *     interface with service-functions for parsing of dataDomain: SysData
 * 
 * @package de.yaio.datatransfer.importer.parser
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class SysDataParserImpl  extends ParserImpl implements SysDataParser {

    Calendar calDate = new GregorianCalendar();
    Calendar calTime = new GregorianCalendar();

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(SysDataParserImpl.class);

    // Patterns
    // Pattern fuer NodeSys: UID,CreateDatum Uhrzeit, Checksum, ChangeDatum Uhrzeit,ChangeCount)
    public static final String CONST_PATTERN_SEG_NODESYS =
        "NodeSys:\\W*"
               + "(" + CONST_PATTERN_SEG_UID + "*)?,"
               + "("+ CONST_PATTERN_SEG_DATUM + " " + CONST_PATTERN_SEG_TIME + ")?,"
               + "(" + CONST_PATTERN_SEG_CHECKSUM + "*)?,"
               + "("+ CONST_PATTERN_SEG_DATUM + " "+ CONST_PATTERN_SEG_TIME + ")?,"
               + "("+ CONST_PATTERN_SEG_INT + "*)?";
    public static final Pattern CONST_PATTERN_NODESYS =
        Pattern.compile("(.*)" + CONST_PATTERN_SEG_NODESYS + "(.*)");

    @Override
    public Class<?> getTargetClass() {
        return SysData.class;
    }

    @Override
    public int getTargetOrder() {
        return SysData.CONST_ORDER;
    }

    public static void configureDataDomainParser(NodeFactory nodeFactory) {
        nodeFactory.addDataDomainParser(new SysDataParserImpl());
    }

    @Override
    public int parseFromName(DataDomain node, ImportOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (! SysData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        return parseSysDataFromName((SysData)node, options);
    }

    @Override
    public int parseSysDataFromName(SysData node, ImportOptions options) throws Exception {
        int found = 0;

        // Check for valid data
        if (node.getName() == null) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Pattern Sys dosnt match because node has no name for node:" 
                        + node.getNameForLogger());
            return found;
        }

        // Sysdaten auslesen
        Pattern pattern = CONST_PATTERN_NODESYS;
        Matcher matcher = pattern.matcher(node.getName());
        int matcherindex = 0;
        if (matcher.matches()) {
            // Bereich davor/dahinter
            this.trimNodeName(node, pattern, matcher, 1, 7);

            // UID
            matcherindex = 2;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            if (matcher.group(matcherindex) != null) {
                node.setSysUID(matcher.group(matcherindex));
            }
            // CreateDate
            matcherindex = 3;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            if (matcher.group(matcherindex) != null) {
                node.setSysCreateDate(DTF.parse(matcher.group(matcherindex)));
            }
            // Checksum
            matcherindex = 4;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            if (matcher.group(matcherindex) != null) {
                node.setSysCurChecksum(matcher.group(matcherindex));
            }
            // ChangeDate
            matcherindex = 5;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            if (matcher.group(matcherindex) != null) {
                node.setSysChangeDate(DTF.parse(matcher.group(matcherindex)));
            }
            // ChangeCount
            matcherindex = 6;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            if (matcher.group(matcherindex) != null) {
                node.setSysChangeCount(new Integer(matcher.group(matcherindex)).intValue());
            }
            found++;
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pattern Sys dosnt match: " + CONST_PATTERN_NODESYS 
                    + " for node:" + node.getNameForLogger());
        }

        return found;
    }
}
