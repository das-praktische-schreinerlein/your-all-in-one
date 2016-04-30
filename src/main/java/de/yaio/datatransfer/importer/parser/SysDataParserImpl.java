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

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.SysData;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.NodeFactory;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class SysDataParserImpl  extends ParserImpl implements SysDataParser {

    // Patterns
    // Pattern fuer NodeSys: UID,CreateDatum Uhrzeit, Checksum, ChangeDatum Uhrzeit,ChangeCount)
    protected static final String CONST_PATTERN_SEG_NODESYS =
        "NodeSys:\\W*"
               + "(" + CONST_PATTERN_SEG_UID + "*)?,"
               + "(" + CONST_PATTERN_SEG_DATUM + " " + CONST_PATTERN_SEG_TIME + ")?,"
               + "(" + CONST_PATTERN_SEG_CHECKSUM + "*)?,"
               + "(" + CONST_PATTERN_SEG_DATUM + " " + CONST_PATTERN_SEG_TIME + ")?,"
               + "(" + CONST_PATTERN_SEG_INT + "*)?";
    protected static final Pattern CONST_PATTERN_NODESYS =
        Pattern.compile("(.*)" + CONST_PATTERN_SEG_NODESYS + "(.*)", Pattern.UNICODE_CHARACTER_CLASS);

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(SysDataParserImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return SysData.class;
    }

    @Override
    public int getTargetOrder() {
        return SysData.CONST_ORDER;
    }

    /** 
     * hängt den Parser für das spätere Extrahieren der NodeDaten aus dem 
     * Namen (NodeFactory.parseNodeDataDomains) in die Parserliste
     * @param nodeFactory            instance of the nodeFactory which will use the parser
     */
    public static void configureDataDomainParser(final NodeFactory nodeFactory) {
        nodeFactory.addDataDomainParser(new SysDataParserImpl());
    }

    @Override
    public int parseFromName(final DataDomain node, final ImportOptions options) throws Exception {
        if (node == null) {
            return 0;
        }
        // Check if node is compatibel
        if (!SysData.class.isInstance(node)) {
            throw new IllegalArgumentException();
        }
        return parseSysDataFromName((SysData) node, options);
    }

    @Override
    public int parseSysDataFromName(final SysData node, final ImportOptions options) throws Exception {
        int found = 0;

        // Check for valid data
        if (node.getName() == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern Sys dosnt match because node has no name for node:" 
                        + node.getNameForLogger());
            }
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
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setSysUID(matcher.group(matcherindex));
            }
            // CreateDate
            matcherindex = 3;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setSysCreateDate(DTF.parse(matcher.group(matcherindex)));
            }
            // Checksum
            matcherindex = 4;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setSysCurChecksum(matcher.group(matcherindex));
            }
            // ChangeDate
            matcherindex = 5;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setSysChangeDate(DTF.parse(matcher.group(matcherindex)));
            }
            // ChangeCount
            matcherindex = 6;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null && matcher.group(matcherindex).length() > 0) {
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
