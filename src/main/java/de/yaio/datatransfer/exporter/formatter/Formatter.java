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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.exporter.OutputOptions;

/**
 * <h4>FeatureDomain:</h4>
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     interface with service-functions for formatting of dataDomains
 * 
 * @package de.yaio.datatransfer.exporter.formatter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface Formatter extends Comparable<Formatter> {
    public static DateFormat DF = new SimpleDateFormat("dd.MM.yyyy");
    public static DateFormat TF = new SimpleDateFormat("HH:mm");
    public static DateFormat DTF = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    public static java.util.Formatter NF = new java.util.Formatter();

    public static final int CONST_FLAG_NODATE_SECONDS = 59;
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Praesentation
     * <h4>FeatureDescription:</h4>
     *     returns the class of the DataDomain for which the formatter runs
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Class - DataDomain for which the formatter runs
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return DataDomain for which the formatter runs
     */
    public Class<?> getTargetClass();

    /**
     * <h4>FeatureDomain:</h4>
     *     Praesentation
     * <h4>FeatureDescription:</h4>
     *     returns position in the formatter-queue
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue int - position in the formatter-queue
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return position in the formatter-queue
     */
    public int getTargetOrder();

    /**
     * <h4>FeatureDomain:</h4>
     *     Praesentation
     * <h4>FeatureDescription:</h4>
     *     formats DomainData and appends output to StringBuffer nodeOutput
     * <h4>FeatureConditions:</h4>
     *     formatter runs only if options.flgShow* for DataDomain is set<br>
     *     must not be implemented direct, but should call separate function
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>appends to nodeOutput
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param node - node to be formatted
     * @param nodeOutput - to append the output
     * @param options - options for formatter
     * @throws Exception
     */
    public void format(DataDomain node, StringBuffer nodeOutput, OutputOptions options) throws Exception;
}
