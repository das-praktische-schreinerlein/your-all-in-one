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
import de.yaio.core.datadomain.ResLocData;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.NodeFactory;

/**
 * <h4>FeatureDomain:</h4>
 *     import
 * <h4>FeatureDescription:</h4>
 *     service-functions for parsing of dataDomain: ResLocData
 * 
 * @package de.yaio.datatransfer.importer.parser
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ResLocDataParserImpl  extends ParserImpl implements ResLocDataParser {

    Calendar calDate = new GregorianCalendar();
    Calendar calTime = new GregorianCalendar();

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(ResLocDataParserImpl.class);

    // Patterns
    protected static final String CONST_PATTERN_SEG_STRING1 = "[^,:\\[\\]]";
    protected static final String CONST_PATTERN_SEG_URL =
        "[-a-zA-Z0-9\\.:%&=_?/#\\~\\\\+\\(\\)!]+";
    protected static final String CONST_PATTERN_SEG_URLRES =
        "ResLoc: *(" + CONST_PATTERN_SEG_URL + ")?," 
               + "(" + CONST_PATTERN_SEG_STRING1 + "*)?,"
               + "(" + CONST_PATTERN_SEG_TAGS + "*)?";
    private static final Pattern CONST_PATTERN_URLRES =
        Pattern.compile("(.*)" + CONST_PATTERN_SEG_URLRES + "(.*)", Pattern.UNICODE_CHARACTER_CLASS);

    @Override
    public Class<?> getTargetClass() {
        return ResLocData.class;
    }

    @Override
    public int getTargetOrder() {
        return ResLocData.CONST_ORDER;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     hängt den Parser für das spätere Extrahieren der NodeDaten aus dem 
     *     Namen (NodeFactory.parseNodeDataDomains) in die Parserliste
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param nodeFactory - instance of the nodeFactory which will use the parser 
     */
    public static void configureDataDomainParser(final NodeFactory nodeFactory) {
        nodeFactory.addDataDomainParser(new ResLocDataParserImpl());
    }

    @Override
    public int parseFromName(final DataDomain node, final ImportOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (!ResLocData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        return parseResLocDataFromName((ResLocData) node, options);
    }

    @Override
    public int parseResLocDataFromName(final ResLocData node, final ImportOptions options) throws Exception {
        int found = 0;

        // Check for valid data
        if (node.getName() == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern ResLoc dosnt match because node has no name for node:" 
                        + node.getNameForLogger());
            }
            return found;
        }

        // ResLocdaten auslesen
        Pattern pattern = CONST_PATTERN_URLRES;
        Matcher matcher = pattern.matcher(node.getName());
        int matcherindex = 0;
        if (matcher.matches()) {
            // Bereich davor/dahinter
            this.trimNodeName(node, pattern, matcher, 1, 5);

            // Reference
            matcherindex = 2;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_SEG_URL + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setResLocRef(matcher.group(matcherindex));
            }
            // Label
            matcherindex = 3;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_SEG_STRING1 + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setResLocName(matcher.group(matcherindex));
            }
            // Tags
            matcherindex = 4;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_SEG_TAGS + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setResLocTags(matcher.group(matcherindex));
            }

            found++;
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pattern ResLoc dosnt match: " + CONST_PATTERN_URLRES
                    + " for node:" + node.getNameForLogger());
        }

        return found;
    }
}
