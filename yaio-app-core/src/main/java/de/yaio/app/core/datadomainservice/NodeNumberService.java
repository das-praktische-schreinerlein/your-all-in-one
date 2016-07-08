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
package de.yaio.app.core.datadomainservice;

import de.yaio.app.core.datadomain.MetaData;

import java.util.Map;

/** 
 * interface with businesslogic for dataDomain: MetaData to manage Nodenumbers
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface NodeNumberService {
    
    /** 
     * get next human readable Node-number for this node-hierarchy (praefix)
     * @param node                   node (praefix)
     * @return                       next available Node-number for this node-hierarchy (praefix)
     */
    Object getNextNodeNumber(MetaData node);

    boolean isInitialised();

    /**
     * initialize next human readable Node-number for this node-hierarchy (praefix) - updates memberVar - MAP_CUR_NEXTNODEID
     * @param praefix                node-praefix
     * @param number                 next available Node-number for this node-hierarchy (praefix)
     */
    void initNextNodeNumber(String praefix, Integer number);

    /** 
     * get map with next human readable Node-number per node-hierarchy (praefix)
     * @return                       map <praefix, nextId>
     */
    Map<String, Integer> getNextNodeNumberMap();

    /** 
     * exports map with next human readable Node-number to file
     * @param strPathIdDB            filename
     */
    void exportNextNodeNumbersToFile(String strPathIdDB);

    /** 
     * initialize next human readable Node-number from file - updates memberVar - MAP_CUR_NEXTNODEID
     * @param strPathIdDB            path to the idFile
     * @param forceReload            force reload
     */
    void initNextNodeNumbersFromFile(String strPathIdDB, boolean forceReload);
}

