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

/** 
 * businesslogic for dataDomain: MetaData
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface MetaDataService {

    /** the position in the recalc-order at which the recalcer will run */
    int CONST_RECALC_ORDER = 5;

    /** 
     * inititialize the MetaData-Fields of the node (id, praefix) 
     * @param node                   node to initialize
     */
    void initMetaData(MetaData node);

    /** 
     * get next human readable Node-number for this node-hierarchy (praefix)
     * @param node                   node (praefix)
     * @return                       next available Node-number for this node-hierarchy (praefix)
     */
    Object getNextNodeNumber(MetaData node);

    /** 
     * sets the NodeNumberService
     * @param nodeNumberService      service to get nodeNumbers
     */
    void setNodeNumberService(NodeNumberService nodeNumberService);

    /** 
     * returns the NodeNumberService
     * @return                       service to get nodeNumbers
     */
    NodeNumberService getNodeNumberService();
}
