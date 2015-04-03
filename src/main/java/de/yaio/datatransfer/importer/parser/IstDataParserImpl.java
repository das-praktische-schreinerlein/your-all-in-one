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
import de.yaio.core.datadomain.IstData;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.NodeFactory;

/**
 * <h4>FeatureDomain:</h4>
 *     import
 * <h4>FeatureDescription:</h4>
 *     service-functions for parsing of dataDomain: IstData
 * 
 * @package de.yaio.datatransfer.importer.parser
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class IstDataParserImpl  extends ParserImpl implements IstDataParser {

    protected Calendar calDate = new GregorianCalendar();
    protected Calendar calTime = new GregorianCalendar();

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(IstDataParserImpl.class);

    // Patterns
    protected static final String CONST_PATTERN_SEG_OPTIONAL_DATETIME = 
            //          "("+ CONST_PATTERN_SEG_DATUM + ")?\\s?("+ CONST_PATTERN_SEG_TIME + ")?" 
            //        + "-?("+ CONST_PATTERN_SEG_DATUM + ")?\\s?("+ CONST_PATTERN_SEG_TIME + ")?";
            // TODO: TASK aif TIME umsetzen und schauen was das Problem ist
            "(" + CONST_PATTERN_SEG_DATUM + ")?[ ]?(" + CONST_PATTERN_SEG_TIME + ")?" 
            + "-?(" + CONST_PATTERN_SEG_DATUM + ")?[ ]?(" + CONST_PATTERN_SEG_TIME + ")?";        

    // Ist: Stand% Stunden Start-Ende Task
    protected static final String CONST_PATTERN_SEG_IST =
        "Ist:\\W*"
               + "(" + CONST_PATTERN_SEG_STAND + ")%[ ]*"
               + "(" + CONST_PATTERN_SEG_HOURS + ")?h?[ ]*"
               + CONST_PATTERN_SEG_OPTIONAL_DATETIME
               + "[ ]*(" + CONST_PATTERN_SEG_TASK + ")?";
    protected static final Pattern CONST_PATTERN_IST =
        Pattern.compile("(.*)" + CONST_PATTERN_SEG_IST + "(.*)", Pattern.UNICODE_CHARACTER_CLASS);

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
        nodeFactory.addDataDomainParser(new IstDataParserImpl());
    }

    @Override
    public Class<?> getTargetClass() {
        return IstData.class;
    }

    @Override
    public int getTargetOrder() {
        return IstData.CONST_ORDER;
    }

    @Override
    public int parseFromName(final DataDomain node, final ImportOptions options) throws Exception {
        if (node == null) {
            return 0;
        }
        // Check if node is compatibel
        if (!IstData.class.isInstance(node)) {
            throw new IllegalArgumentException();
        }
        return parseIstDataFromName((IstData) node, options);
    }

    @Override
    public int parseIstDataFromName(final IstData node, final ImportOptions options) throws Exception {
        int found = 0;

        // Check for valid data
        if (node.getName() == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern Ist dosnt match because node has no name for node:" 
                        + node.getNameForLogger());
            }
            return found;
        }

        // Helpers
        Date timeOffsett = null;

        // Istdaten auslesen
        Pattern pattern = CONST_PATTERN_IST;
        Matcher matcher = pattern.matcher(node.getName());
        int matcherindex = 0;
        if (matcher.matches()) {
            // Bereich davor/dahinter
            this.trimNodeName(node, pattern, matcher, 1, 9);

            // Ist-Prozent
            matcherindex = 2;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_IST + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                node.setIstStand(new Double(matcher.group(matcherindex)));
            }

            // Ist-h
            matcherindex = 3;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_IST + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null) {
                node.setIstAufwand(new Double(matcher.group(matcherindex)));
            }
            // Ist-Startdatum
            matcherindex = 4;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_IST + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null) {
                calDate.setTime(DF.parse(matcher.group(matcherindex)));
                calDate.set(Calendar.SECOND, CONST_FLAG_NODATE_SECONDS);
                node.setIstStart(calDate.getTime());
            }
            // Ist-Startzeit
            matcherindex = 5;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_IST + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null && node.getIstStart() != null) {
                calDate.setTime(node.getIstStart());
                timeOffsett = TF.parse(matcher.group(matcherindex));

                // zum Datum addieren
                calTime.setTime(timeOffsett);
                calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
                calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
                calDate.set(Calendar.SECOND, 0);
                node.setIstStart(calDate.getTime());
            }
            // Ist-Enddatum
            matcherindex = 6;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_IST + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null) {
                calDate.setTime(DF.parse(matcher.group(matcherindex)));
                calDate.set(Calendar.SECOND, CONST_FLAG_NODATE_SECONDS);
                node.setIstEnde(calDate.getTime());
            }
            // Ist-Endzeit
            matcherindex = 7;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_IST + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null && node.getIstEnde() != null) {
                calDate.setTime(node.getIstEnde());
                timeOffsett = TF.parse(matcher.group(matcherindex));

                // zum Datum addieren
                calTime.setTime(timeOffsett);
                calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
                calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
                calDate.set(Calendar.SECOND, 0);
                node.setIstEnde(calDate.getTime());
            }
            // Ist-Task
            matcherindex = 8;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + CONST_PATTERN_IST + " " 
                        + matcherindex + ":" + matcher.group(matcherindex)
                        + " for node:" + node.getNameForLogger());
            }
            if (matcher.group(matcherindex) != null) {
                node.setIstTask(matcher.group(matcherindex));
            }

            found++;
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pattern Ist dosnt match: " + CONST_PATTERN_SEG_IST 
                    + " for node:" + node.getNameForLogger());
        }

        return found;
    }
}
