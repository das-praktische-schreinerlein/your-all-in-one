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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.SymLinkData;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.NodeFactory;

/** 
 * service-functions for parsing of dataDomain: SymLinkData
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer.parser
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class SymLinkDataParserImpl  extends ParserImpl implements SymLinkDataParser {

    // Patterns
    protected static final String CONST_PATTERN_SEG_STRING1 = "[^,:\\[\\]]";
    protected static final String CONST_PATTERN_SEG_SYMREF = "[A-Za-z]+[0-9]+";
    protected static final String CONST_PATTERN_SEG_SYMLINK =
        "SymLink: (" + CONST_PATTERN_SEG_SYMREF + ")?," 
               + "(" + CONST_PATTERN_SEG_STRING1 + "*)?,"
               + "(" + CONST_PATTERN_SEG_TAGS + "*)?";
    private static final Pattern CONST_PATTERN_SYMLINK =
        Pattern.compile("(.*)" + CONST_PATTERN_SEG_SYMLINK + "(.*)", Pattern.UNICODE_CHARACTER_CLASS);

    // Logger
    private static final Logger LOGGER = Logger.getLogger(SymLinkDataParserImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return SymLinkData.class;
    }

    @Override
    public int getTargetOrder() {
        return SymLinkData.CONST_ORDER;
    }

    /** 
     * hängt den Parser für das spätere Extrahieren der NodeDaten aus dem 
     * Namen (NodeFactory.parseNodeDataDomains) in die Parserliste
     * @param nodeFactory            instance of the nodeFactory which will use the parser
     */
    public static void configureDataDomainParser(final NodeFactory nodeFactory) {
        nodeFactory.addDataDomainParser(new SymLinkDataParserImpl());
    }

    @Override
    public int parseFromName(final DataDomain node, final ImportOptions options) throws Exception {
        if (node == null) {
            return 0;
        }
        // Check if node is compatibel
        if (!SymLinkData.class.isInstance(node)) {
            throw new IllegalArgumentException();
        }
        return parseSymLinkDataFromName((SymLinkData) node, options);
    }

    @Override
    public int parseSymLinkDataFromName(final SymLinkData node, final ImportOptions options) throws Exception {
        int found = 0;

        // Check for valid data
        if (node.getName() == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern SymLink dosnt match because node has no name for node:" 
                        + node.getNameForLogger());
            }
            return found;
        }

        // SymLinkdaten auslesen
        Pattern pattern = CONST_PATTERN_SYMLINK;
        Matcher matcher = pattern.matcher(node.getName());
        int matcherindex = 0;
        if (matcher.matches()) {
            // Bereich davor/dahinter
            this.trimNodeName(node, pattern, matcher, 1, 5);

            // Reference
            matcherindex = 2;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_SEG_SYMREF + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setSymLinkRef(matcher.group(matcherindex));
            }
            // Label
            matcherindex = 3;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_SEG_STRING1 + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setSymLinkName(matcher.group(matcherindex));
            }
            // Tags
            matcherindex = 4;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_SEG_TAGS + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setSymLinkTags(matcher.group(matcherindex));
            }

            found++;
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pattern SymLink dosnt match: " + CONST_PATTERN_SYMLINK
                    + " for node:" + node.getNameForLogger());
        }

        return found;
    }
}
