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
package de.yaio.app.datatransfer.importer.parser;

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.datadomain.PlanCalcData;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.importer.ImportOptions;
import de.yaio.app.datatransfer.importer.NodeFactory;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * service-functions for parsing of dataDomain: PlanCalcData
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer.parser
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class PlanCalcDataParserImpl  extends ParserImpl implements PlanCalcDataParser {

    // Patterns
    protected static final String CONST_PATTERN_SEG_OPTIONAL_DATETIME = 
            "(" + CONST_PATTERN_SEG_DATUM + ")?[ ]?(" + CONST_PATTERN_SEG_TIME + ")?" 
            + "-(" + CONST_PATTERN_SEG_DATUM + ")?[ ]?(" + CONST_PATTERN_SEG_TIME + ")?";        

    // Plan: Stunden Start-Ende Task
    protected static final String CONST_PATTERN_SEG_PLANCALC =
            "PlanCalc:\\W*"
                    + CONST_PATTERN_SEG_OPTIONAL_DATETIME + "";
    protected static final Pattern CONST_PATTERN_PLANCALC =
            Pattern.compile("(.*)" + CONST_PATTERN_SEG_PLANCALC + "(.*)", Pattern.UNICODE_CHARACTER_CLASS);

    // Logger
    private static final Logger LOGGER = Logger.getLogger(PlanCalcDataParserImpl.class);

    @Override
    public Class<?> getTargetClass() {
        return PlanCalcData.class;
    }

    @Override
    public int getTargetOrder() {
        return PlanCalcData.CONST_ORDER;
    }

    /** 
     * hängt den Parser für das spätere Extrahieren der NodeDaten aus dem 
     * Namen (NodeFactory.parseNodeDataDomains) in die Parserliste
     * @param nodeFactory            instance of the nodeFactory which will use the parser
     */
    public static void configureDataDomainParser(final NodeFactory nodeFactory) {
        nodeFactory.addDataDomainParser(new PlanCalcDataParserImpl());
    }

    @Override
    public int parseFromName(final DataDomain node, final ImportOptions options) throws ParserException {
        if (node == null) {
            return 0;
        }

        // Check if node is compatibel
        if (!PlanCalcData.class.isInstance(node)) {
            throw new IllegalArgumentException();
        }
        return parsePlanCalcDataFromName((PlanCalcData) node, options);
    }

    @Override
    public int parsePlanCalcDataFromName(final PlanCalcData node, final ImportOptions options) throws ParserException {
        int found = 0;

        // Check for valid data
        if (node.getName() == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern PlanCalc dosnt match because node has no name for node:" 
                        + node.getNameForLogger());
            }
            return found;
        }

        // Helpers
        Date timeOffsett = null;

        // Plandaten auslesen
        Pattern pattern = CONST_PATTERN_PLANCALC;
        Matcher matcher = pattern.matcher(node.getName());
        int matcherindex = 0;
        if (matcher.matches()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("found PlanCalc in: " + node.getName()
                        + " for node:" + node.getNameForLogger());
            }

            // Bereich davor/dahinter
            this.trimNodeName(node, pattern, matcher, 1, 6);

            // PlanCalc-Startdatum
            matcherindex = 2;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_PLANCALC + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null) {
                try {
                    calDate.setTime(DF.parse(matcher.group(matcherindex)));
                } catch (ParseException ex) {
                    throw new ParserException("cant parse planCalcStart", node.getName(), ex);
                }
                calDate.set(Calendar.SECOND, CONST_FLAG_NODATE_SECONDS);
                node.setPlanCalcStart(calDate.getTime());
            }
            // PlanCalc-Startzeit
            matcherindex = 3;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_PLANCALC + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null && node.getPlanCalcStart() != null) {
                calDate.setTime(node.getPlanCalcStart());
                try {
                    timeOffsett = TF.parse(matcher.group(matcherindex));
                } catch (ParseException ex) {
                    throw new ParserException("cant parse timeOffsett for planCalcStart", node.getName(), ex);
                }

                // zum Datum addieren
                calTime.setTime(timeOffsett);
                calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
                calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
                calDate.set(Calendar.SECOND, 0);
                node.setPlanCalcStart(calDate.getTime());
            }
            // PlanCalc-Enddatum
            matcherindex = 4;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_PLANCALC + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null) {
                try {
                    calDate.setTime(DF.parse(matcher.group(matcherindex)));
                } catch (ParseException ex) {
                    throw new ParserException("cant parse planCalcEnd", node.getName(), ex);
                }
                calDate.set(Calendar.SECOND, CONST_FLAG_NODATE_SECONDS);
                node.setPlanCalcEnde(calDate.getTime());
            }
            // PlanCalc-Endzeit
            matcherindex = 5;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_PLANCALC + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null && node.getPlanCalcEnde() != null) {
                calDate.setTime(node.getPlanCalcEnde());
                try {
                    timeOffsett = TF.parse(matcher.group(matcherindex));
                } catch (ParseException ex) {
                    throw new ParserException("cant parse timeoffset for planCalcEnd", node.getName(), ex);
                }

                // zum Datum addieren
                calTime.setTime(timeOffsett);
                calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
                calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
                calDate.set(Calendar.SECOND, 0);
                node.setPlanCalcEnde(calDate.getTime());
            }

            found++;

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("NewName: " + node.getName()
                        + " for node:" + node.getNameForLogger());
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pattern PlanCalc dosnt match: " + CONST_PATTERN_SEG_PLANCALC 
                    + " for node:" + node.getNameForLogger());
        }

        return found;
    }
}
