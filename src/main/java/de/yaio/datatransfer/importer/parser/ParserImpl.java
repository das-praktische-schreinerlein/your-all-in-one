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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.yaio.core.datadomain.DataDomain;

/**
 * <h4>FeatureDomain:</h4>
 *     import
 * <h4>FeatureDescription:</h4>
 *     service-functions for parsing of dataDomains
 * 
 * @package de.yaio.datatransfer.importer.parser
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public abstract class ParserImpl implements Parser {

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
            if (! name.equals(newPraefix)) {
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
