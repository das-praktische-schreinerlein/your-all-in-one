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
package de.yaio.core.datadomainservice;

import java.util.Map;

import de.yaio.core.datadomain.MetaData;

/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     interface with businesslogic for dataDomain: MetaData to manage Nodenumbers
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface NodeNumberService {
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     get next human readable Node-number for this node-hierarchy (praefix)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Nodenumber - next available Node-number for this node-hierarchy (praefix)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param node - node (praefix)
     * @return next available Node-number for this node-hierarchy (praefix)
     * @throws Exception - io-Exceptions possible
     */
    Object getNextNodeNumber(MetaData node) throws Exception;
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     initialize next human readable Node-number for this node-hierarchy (praefix)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberVar - MAP_CUR_NEXTNODEID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param praefix - node-praefix
     * @param number - next available Node-number for this node-hierarchy (praefix)
     * @throws Exception - io-Exceptions possible
     */
    void initNextNodeNumber(String praefix, Integer number) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     get map with next human readable Node-number per node-hierarchy (praefix)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Map<String, Integer> - map <praefix, nextId>
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return map <praefix, nextId>
     * @throws Exception - io-Exceptions possible
     */
    Map<String, Integer> getNextNodeNumberMap() throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     exports map with next human readable Node-number to file
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param strPathIdDB - filename
     * @throws Exception - io-Exceptions possible
     */
    void exportNextNodeNumbersToFile(String strPathIdDB) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     initialize next human readable Node-number from file
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberVar - MAP_CUR_NEXTNODEID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param strPathIdDB - parth to the idFile
     * @throws Exception - parser/format/io-Exceptions possible
     */
    void initNextNodeNumbersFromFile(String strPathIdDB) throws Exception;
}

