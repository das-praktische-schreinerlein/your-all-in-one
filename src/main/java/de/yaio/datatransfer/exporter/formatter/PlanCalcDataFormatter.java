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

import de.yaio.core.datadomain.PlanCalcData;
import de.yaio.datatransfer.exporter.OutputOptions;

/**
 * <h4>FeatureDomain:</h4>
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     interface with service-functions for formatting of dataDomain: PlanCalcData
 * 
 * @package de.yaio.datatransfer.exporter.formatter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface PlanCalcDataFormatter extends Formatter{

    /**
     * <h4>FeatureDomain:</h4>
     *     Praesentation
     * <h4>FeatureDescription:</h4>
     *     formats DomainData: PlanCalcData and appends output to StringBuffer nodeOutput
     * <h4>FeatureConditions:</h4>
     *     formatter runs only if options.flgShowCalc is set<br>
     *     show brackets if options.flgShowBrackets is set<br>
     *     fill with whitespace to options.intendFuncArea
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>appends to nodeOutput
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param node - node to be formatted
     * @param nodeOutput - to append the output
     * @param options - options for formatter
     * @throws Exception - parser/format-Exceptions possible
     */
    void formatPlanCalcData(PlanCalcData node, 
            StringBuffer nodeOutput, OutputOptions options) throws Exception;
}