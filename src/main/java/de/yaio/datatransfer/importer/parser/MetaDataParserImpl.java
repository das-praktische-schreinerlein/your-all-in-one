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
import de.yaio.core.datadomain.MetaData;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.NodeFactory;

/**
 * <h4>FeatureDomain:</h4>
 *     import
 * <h4>FeatureDescription:</h4>
 *     service-functions for parsing of dataDomain: MetaData
 * 
 * @package de.yaio.datatransfer.importer.parser
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class MetaDataParserImpl  extends ParserImpl implements MetaDataParser {

    Calendar calDate = new GregorianCalendar();
    Calendar calTime = new GregorianCalendar();

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(MetaDataParserImpl.class);

    // Patterns
    // Pattern fuer NodeMeta: Praefix,Id,NodeTyp-Tags,NodeSubType-Tags
    protected static final String CONST_PATTERN_SEG_NODEMETA =
        "NodeMeta:\\W*"
               + "(" + CONST_PATTERN_SEG_PRAEFIX + "*)?,"
               + "("+ CONST_PATTERN_SEG_ID + "*)?,"
               + "(" + CONST_PATTERN_SEG_TAGS + "*)?,"
               + "(" + CONST_PATTERN_SEG_TAGS + "*)? *";
    protected static final Pattern CONST_PATTERN_NODEMETA =
        Pattern.compile("(.*)" + CONST_PATTERN_SEG_NODEMETA + "(.*)", Pattern.UNICODE_CHARACTER_CLASS);

    @Override
    public Class<?> getTargetClass() {
        return MetaData.class;
    }

    @Override
    public int getTargetOrder() {
        return MetaData.CONST_ORDER;
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
        nodeFactory.addDataDomainParser(new MetaDataParserImpl());
    }

    @Override
    public int parseFromName(final DataDomain node, final ImportOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (! MetaData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        return parseMetaDataFromName((MetaData) node, options);
    }

    @Override
    public int parseMetaDataFromName(final MetaData node, final ImportOptions options) throws Exception {
        int found = 0;

        // Check for valid data
        if (node.getName() == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern Meta dosnt match because node has no name for node:" 
                        + node.getNameForLogger());
            }
            return found;
        }

        // Metadaten auslesen
        Pattern pattern = CONST_PATTERN_NODEMETA;
        Matcher matcher = pattern.matcher(node.getName());
        int matcherindex = 0;
        if (matcher.matches()) {
            // Bereich davor/dahinter
            this.trimNodeName(node, pattern, matcher, 1, 6);

            // Praefix
            matcherindex = 2;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setMetaNodePraefix(matcher.group(matcherindex));
            }
            // Id
            matcherindex = 3;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setMetaNodeNummer(matcher.group(matcherindex));
            }
            // Checksum
            matcherindex = 4;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setMetaNodeTypeTags(matcher.group(matcherindex));
            }
            // ChangeDate
            matcherindex = 5;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setMetaNodeSubTypeTags(matcher.group(matcherindex));
            }

            found++;
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pattern Meta dosnt match: " + CONST_PATTERN_SEG_NODEMETA 
                    + " for node:" + node.getNameForLogger());
        }

        return found;
    }
}
