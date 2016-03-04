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
import de.yaio.core.datadomain.DescData;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.NodeFactory;

/** 
 * service-functions for parsing of dataDomain: DescData
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer.parser
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class DescDataParserImpl  extends ParserImpl implements DescDataParser {

    // Pattern fuer die Projectbeschreibung
    protected static final String CONST_PATTERN_SEG_DESC =
        "ProjektDesc: *(.*)";
    protected static final Pattern CONST_PATTERN_DESC =
        Pattern.compile("(.*)" + CONST_PATTERN_SEG_DESC + "(.*)", 
                        Pattern.UNICODE_CHARACTER_CLASS);

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(DescDataParserImpl.class);


    @Override
    public Class<?> getTargetClass() {
        return DescData.class;
    }

    @Override
    public int getTargetOrder() {
        return DescData.CONST_ORDER;
    }

    /** 
     * hängt den Parser für das spätere Extrahieren der NodeDaten aus dem 
     * Namen (NodeFactory.parseNodeDataDomains) in die Parserliste
     * @param nodeFactory            instance of the nodeFactory which will use the parser
     */
    public static void configureDataDomainParser(final NodeFactory nodeFactory) {
        nodeFactory.addDataDomainParser(new DescDataParserImpl());
    }

    @Override
    public int parseFromName(final DataDomain node, final ImportOptions options) throws Exception {
        if (node == null) {
            return 0;
        }

        // Check if node is compatibel
        if (!DescData.class.isInstance(node)) {
            throw new IllegalArgumentException();
        }
        return parseDescDataFromName((DescData) node, options);
    }

    @Override
    public int parseDescDataFromName(final DescData node, final ImportOptions options) throws Exception {
        int found = 0;

        // Check for valid data
        if (node.getName() == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern Desc dosnt match because node has no name for node:" 
                        + node.getNameForLogger());
            }
            return found;
        }

        // Descdaten auslesen
        Pattern pattern = CONST_PATTERN_DESC;
        Matcher matcher = pattern.matcher(node.getName());
        int matcherindex = 0;
        if (matcher.matches()) {
            // Bereich davor/dahinter
            this.trimNodeName(node, pattern, matcher, 1, 3);

            // Desc
            matcherindex = 2;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_SEG_DESC + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                // reescape desc
                String dummyText = matcher.group(matcherindex);
//                if (dummyText != null) {
//                    dummyText = dummyText.replaceAll("<WLESC>", "\\");
//                    dummyText = dummyText.replaceAll("<WLBR>", "\n");
//                    dummyText = dummyText.replaceAll("<WLTAB>", "\t");
//                }
                
                // set new desc
                node.setNodeDesc(dummyText);
            }

            found++;
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pattern Desc dosnt match: " + CONST_PATTERN_DESC
                    + " for node:" + node.getNameForLogger());
        }

        return found;
    }
}
