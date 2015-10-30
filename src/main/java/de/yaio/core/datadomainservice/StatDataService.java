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
package de.yaio.core.datadomainservice;

import de.yaio.core.datadomain.StatData;

/** 
 * businesslogic for dataDomain: StatData
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface StatDataService {
    
    /** the position in the recalc-order at which the recalcer will run */
    int CONST_RECALC_ORDER = 15;

    /** 
     * update the childrenCount-Fields of the node 
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                updates memberVariable node.childNodeCount
     * @FeatureKeywords              BusinessLogic
     * @param node                   node to initialize
     * @throws Exception             parser/format-Exceptions possible
     */
    void updateChildrenCount(StatData node) throws Exception;
}
