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
package de.yaio.datatransfer.exporter.formatter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.utils.DataUtils;

/**
 * <h4>FeatureDomain:</h4>
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     service-functions for formatting of dataDomains
 * 
 * @package de.yaio.datatransfer.exporter.formatter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public abstract class FormatterImpl implements Formatter {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(FormatterImpl.class);

    /** dateformat-instance for german date dd.MM.yyyy  */
    protected final DateFormat DF = DataUtils.getDF();
    /** dateformat-instance for german time HH:mm */
    protected final DateFormat TF = DataUtils.getTF();
    /** dateformat-instance for german datetime dd.MM.yyyy HH:mm */
    protected final DateFormat DTF = DataUtils.getDTF();
    /** dateformat-instance for UID yyyyMMddHHmmssSSS */
    protected final DateFormat UIDF = DataUtils.getUIDF();

    Calendar calTime = new GregorianCalendar();

    @Override
    public int compareTo(final Formatter o) {
        Integer myOrder = this.getTargetOrder();
        Integer oOrder = o.getTargetOrder();
        return myOrder.compareTo(oOrder);
    }

    @Override
    public abstract void format(DataDomain node, StringBuffer nodeOutput, OutputOptions options) throws Exception;


    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     intend the string with " " on the left side till newLength
     * <h4>FeatureKeywords:</h4>
     *     Format
     * @param src - the string to format
     * @param newLength - max places behind comma
     * @return - the formated string
     */
    public StringBuffer intendLeft(final Object src, final int newLength) {

        // Parameter pruefen
        if (src == null) {
            return null;
        }

        StringBuffer res = new StringBuffer();
        res.append(src.toString());
        while (res.length() < newLength) {
            res.insert(0, " ");
        }

        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     format the number to string (trim trailing 0)
     * <h4>FeatureKeywords:</h4>
     *     Format
     * @param src - the number to format
     * @param minStellen - min places behind comma
     * @param maxStellen - max places behind comma
     * @return - the formated numberstring
     */
    public String formatNumber(final Double src, final int minStellen, final int maxStellen) {
        String res = "";

        // Parameter pruefen
        if (src == null) {
            return res;
        }

        // falls Kommastellen, diese abschneiden
        int decimalPlaces = minStellen;

        if (minStellen == maxStellen) {
            // feste Stellenvorgabe
            decimalPlaces = minStellen;
        } else if (Math.round(src) != src.doubleValue()) {
            // Stellenbereich

            // alle Stellen kontrollieren, bis gleich
            double faktor = 1;
            double tmp1;
            double tmp2;
            for (int zaehler = minStellen; zaehler <= maxStellen; zaehler++) {
                decimalPlaces = zaehler;

                // auf Min Stelle
                faktor = Math.pow(10, zaehler);
                tmp1 = src * faktor;
                tmp1 = Math.round(tmp1);
                tmp1 = tmp1 / faktor;

                // auf Max Stellen
                faktor = Math.pow(10, maxStellen);
                tmp2 = src * faktor;
                tmp2 = Math.round(tmp2);
                tmp2 = tmp2 / faktor;

                LOGGER.debug("formatNumber check: src" + src + " zaehler:" + zaehler 
                                + " tmp1:" + tmp1 + " <> tmp2:" + tmp2);
                if (tmp1 != tmp2) {
                    // 1 Stelle kleiner 2 Stellen: 2 Stellen ausgeben
                } else {
                    // Werte gleich: 1 Stelle ausgeben
                    break;
                }
            }
        } else {
            // ganze Zahl
            decimalPlaces = minStellen;
        }

        // setScale is immutable
        BigDecimal bd = new BigDecimal(src);
        bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        res = bd.toString();
        LOGGER.debug("formatNumber: src" + src + " decimalPlaces:" + decimalPlaces + " Res:" + res);

        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     format the date to string
     * <h4>FeatureKeywords:</h4>
     *     Format
     * @param src - the date to format
     * @return - the formated datestring
     */
    public String formatDate(final Date src) {
        String res = "";
        if (src != null) {
            res += DF.format(src);

            // Zeitanteil setzen
            calTime.setTime(src);
            if (calTime.get(Calendar.SECOND) == Formatter.CONST_FLAG_NODATE_SECONDS) {
                // wenn Sekunde gesetzt, dann keine Uhrzeit angegeben
            } else {
                // Uhrzeit des Datums benutzen
                res += " " + TF.format(src);
            }
        }

        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     escape nonlatin chars to html-unicode sequences
     * <h4>FeatureKeywords:</h4>
     *     Format
     * @param src - the date to format
     * @param out - the outputappender
     * @return - the escaped string
     * @throws java.io.IOException - io-exception on outputappender possible
     */
    public static <T extends Appendable> T escapeNonLatin(final CharSequence src,
                                                          final T out) throws java.io.IOException {
           for (int i = 0; i < src.length(); i++) {
               char ch = src.charAt(i);
               if (Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.BASIC_LATIN) {
                   out.append(ch);
               } else {
                   int codepoint = Character.codePointAt(src, i);
                   // handle supplementary range chars
                   i += Character.charCount(codepoint) - 1;
                   // emit entity
                   out.append("&#x");
                   out.append(Integer.toHexString(codepoint));
                   out.append(";");
               }
           }
           return out;
       }

}
