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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.PlanCalcData;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.NodeFactory;

/**
 * <h4>FeatureDomain:</h4>
 *     import
 * <h4>FeatureDescription:</h4>
 *     service-functions for parsing of dataDomain: PlanCalcData
 * 
 * @package de.yaio.datatransfer.importer.parser
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class PlanCalcDataParserImpl  extends ParserImpl implements PlanCalcDataParser {

    Calendar calDate = new GregorianCalendar();
    Calendar calTime = new GregorianCalendar();

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(PlanCalcDataParserImpl.class);

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

    @Override
    public Class<?> getTargetClass() {
        return PlanCalcData.class;
    }

    @Override
    public int getTargetOrder() {
        return PlanCalcData.CONST_ORDER;
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
        nodeFactory.addDataDomainParser(new PlanCalcDataParserImpl());
    }

    @Override
    public int parseFromName(final DataDomain node, final ImportOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (! PlanCalcData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        return parsePlanCalcDataFromName((PlanCalcData) node, options);
    }

    @Override
    public int parsePlanCalcDataFromName(final PlanCalcData node, final ImportOptions options) throws Exception {
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
                calDate.setTime(DF.parse(matcher.group(matcherindex)));
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
                timeOffsett = TF.parse(matcher.group(matcherindex));

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
                calDate.setTime(DF.parse(matcher.group(matcherindex)));
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
                timeOffsett = TF.parse(matcher.group(matcherindex));

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
