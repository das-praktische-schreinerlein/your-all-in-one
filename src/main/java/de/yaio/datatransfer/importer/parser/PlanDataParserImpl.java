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
import de.yaio.core.datadomain.PlanData;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.NodeFactory;

/**
 * <h4>FeatureDomain:</h4>
 *     import
 * <h4>FeatureDescription:</h4>
 *     service-functions for parsing of dataDomain: PlanData
 * 
 * @package de.yaio.datatransfer.importer.parser
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class PlanDataParserImpl  extends ParserImpl implements PlanDataParser {

    Calendar calDate = new GregorianCalendar();
    Calendar calTime = new GregorianCalendar();

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(PlanDataParserImpl.class);

    // Patterns
    protected static String CONST_PATTERN_SEG_OPTIONAL_DATETIME = 
            //          "("+ CONST_PATTERN_SEG_DATUM + ")?\\s?("+ CONST_PATTERN_SEG_TIME + ")?" 
            //        + "-?("+ CONST_PATTERN_SEG_DATUM + ")?\\s?("+ CONST_PATTERN_SEG_TIME + ")?";
            // TODO: TASK aif TIME umsetzen und schauen was das Problem ist
            "("+ CONST_PATTERN_SEG_DATUM + ")?[ ]?("+ CONST_PATTERN_SEG_TIME + ")?" 
            + "-?("+ CONST_PATTERN_SEG_DATUM + ")?[ ]?("+ CONST_PATTERN_SEG_TIME + ")?";        

    // Plan: Stunden Start-Ende Task
    protected static final String CONST_PATTERN_SEG_PLAN =
            "Plan:\\W*"
                    + "(" + CONST_PATTERN_SEG_HOURS + ")h[ ]*"
                    + CONST_PATTERN_SEG_OPTIONAL_DATETIME
                    + "[ ]*("+ CONST_PATTERN_SEG_TASK + ")?";
    protected static final Pattern CONST_PATTERN_PLAN =
            Pattern.compile("(.*)" + CONST_PATTERN_SEG_PLAN + "(.*)", Pattern.UNICODE_CHARACTER_CLASS);

    @Override
    public Class<?> getTargetClass() {
        return PlanData.class;
    }

    @Override
    public int getTargetOrder() {
        return PlanData.CONST_ORDER;
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
        nodeFactory.addDataDomainParser(new PlanDataParserImpl());
    }

    @Override
    public int parseFromName(final DataDomain node, final ImportOptions options) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (! PlanData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        return parsePlanDataFromName((PlanData) node, options);
    }

    @Override
    public int parsePlanDataFromName(final PlanData node, final ImportOptions options) throws Exception {
        int found = 0;

        // Check for valid data
        if (node.getName() == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern Plan dosnt match because node has no name for node:" 
                        + node.getNameForLogger());
            }
            return found;
        }

        // Helpers
        Date timeOffsett = null;

        // Plandaten auslesen
        Pattern pattern = CONST_PATTERN_PLAN;
        Matcher matcher = pattern.matcher(node.getName());
        int matcherindex = 0;
        if (matcher.matches()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("found Plan in: " + node.getName()
                        + " for node:" + node.getNameForLogger());
            }

            // Bereich davor/dahinter
            this.trimNodeName(node, pattern, matcher, 1, 8);

            // Plan-h
            matcherindex = 2;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_PLAN + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null) {
                node.setPlanAufwand(new Double(matcher.group(matcherindex)));
            }
            // Plan-Startdatum
            matcherindex = 3;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_PLAN + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null) {
                calDate.setTime(DF.parse(matcher.group(matcherindex)));
                calDate.set(Calendar.SECOND, CONST_FLAG_NODATE_SECONDS);
                node.setPlanStart(calDate.getTime());
            }
            // Plan-Startzeit
            matcherindex = 4;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_PLAN + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null && node.getPlanStart() != null) {
                calDate.setTime(node.getPlanStart());
                timeOffsett = TF.parse(matcher.group(matcherindex));

                // zum Datum addieren
                calTime.setTime(timeOffsett);
                calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
                calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
                calDate.set(Calendar.SECOND, 0);
                node.setPlanStart(calDate.getTime());
            }
            // Plan-Enddatum
            matcherindex = 5;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_PLAN + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null) {
                calDate.setTime(DF.parse(matcher.group(matcherindex)));
                calDate.set(Calendar.SECOND, CONST_FLAG_NODATE_SECONDS);
                node.setPlanEnde(calDate.getTime());
            }
            // Plan-Endzeit
            matcherindex = 6;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_PLAN + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null && node.getPlanEnde() != null) {
                calDate.setTime(node.getPlanEnde());
                timeOffsett = TF.parse(matcher.group(matcherindex));

                // zum Datum addieren
                calTime.setTime(timeOffsett);
                calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
                calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
                calDate.set(Calendar.SECOND, 0);
                node.setPlanEnde(calDate.getTime());
            }
            // Plan-Task
            matcherindex = 7;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_PLAN + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null) {
                node.setPlanTask(matcher.group(matcherindex));
            }

            found++;

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("NewName: " + node.getName()
                        + " for node:" + node.getNameForLogger());
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pattern Plan dosnt match: " + CONST_PATTERN_SEG_PLAN 
                    + " for node:" + node.getNameForLogger());
        }

        return found;
    }
}
