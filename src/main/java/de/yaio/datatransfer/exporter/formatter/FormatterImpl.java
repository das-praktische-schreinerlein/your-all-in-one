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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.exporter.OutputOptions;

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

    Calendar calTime = new GregorianCalendar();

    @Override
    public int compareTo(Formatter o) {
        Integer myOrder = this.getTargetOrder();
        Integer oOrder = o.getTargetOrder();
        return myOrder.compareTo(oOrder);
    }

    @Override
    public abstract void format(DataDomain node, StringBuffer nodeOutput, OutputOptions options) throws Exception;


    public StringBuffer intendLeft(Object src, int newLength) {

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

    public String formatNumber(Double src, int minStellen, int maxStellen) {
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
            double tmp1, tmp2;
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

                LOGGER.debug("formatNumber check: src" + src + " zaehler:" + zaehler + " tmp1:" + tmp1 + " <> tmp2:" + tmp2);
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

    public String formatDate(Date src) {
        String res = "";
        if (src != null) {
            res += DF.format(src);

            // Zeitanteil setzen
            calTime.setTime(src);
            if (calTime.get(Calendar.SECOND) == Formatter.CONST_FLAG_NODATE_SECONDS) {
                // wenn Sekunde gesetzt, dann keine Uhrzeit angegeben
            } else {
                // Uhrzeit des Datums benutzen
                res += " " + Formatter.TF.format(src);
            }
        }

        return res;
    }

}
