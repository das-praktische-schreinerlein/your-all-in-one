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
import de.yaio.commons.data.DataUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * service-functions for parsing of dataDomains
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer.parser
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public abstract class ParserImpl implements Parser {

    protected final Calendar calDate = new GregorianCalendar();
    protected final Calendar calTime = new GregorianCalendar();

    /** dateformat-instance for german date dd.MM.yyyy  */
    protected final DateFormat DF = DataUtils.getDF();
    /** dateformat-instance for german time HH:mm */
    protected final DateFormat TF = DataUtils.getTF();
    /** dateformat-instance for german datetime dd.MM.yyyy HH:mm */
    protected final DateFormat DTF = DataUtils.getDTF();
    /** dateformat-instance for UID yyyyMMddHHmmssSSS */
    protected final DateFormat UIDF = DataUtils.getUIDF();

    @Override
    public int compareTo(final Parser o) {
        Integer myOrder = this.getTargetOrder();
        Integer oOrder = o.getTargetOrder();
        return myOrder.compareTo(oOrder);
    }
    
    @Override
    public boolean trimNodeName(final DataDomain node, final Pattern pattern,
                                final Matcher matcher, final int first, final int last) {
        boolean flgBrackets = false;
        String name = node.getName();

        // Bereich davor
        if (matcher.group(first) != null) {
            name = matcher.group(first);
            name = name.trim();

            // fuehrende ([ entfernen
            String newPraefix = name.replaceAll("[\\[\\(]$", ""); 
            if (!name.equals(newPraefix)) {
                name = newPraefix;
                flgBrackets = true;
            }
        }

        // Bereich dahinter
        if (matcher.group(last) != null) {
            String rest = matcher.group(last);
            rest = rest.trim();
            if (flgBrackets) {
                // nachlaufende ]) entfernen
                rest = rest.trim().replaceFirst("^[\\]]", "");
            }
            name = name.trim();
            name += rest;
        }

        node.setName(name);

        return flgBrackets;
    }
}
