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

import de.yaio.core.datadomain.MetaData;

/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for dataDomain: MetaData
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface MetaDataService {

    /** the position in the recalc-order at which the recalcer will run */
    public static final int CONST_RECALC_ORDER = 1;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     inititialize the MetaData-Fields of the node (id, praefix) 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberVariable node.meta*
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param node - node to initialize
     * @throws Exception - parser/format-Exceptions possible
     */
    public void initMetaData(MetaData node) throws Exception;

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
    public Object getNextNodeNumber(MetaData node) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     sets the NodeNumberService
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param nodeNumberService - service to get nodeNumbers
     * @throws Exception - io-Exceptions possible
     */
    public void setNodeNumberService(NodeNumberService nodeNumberService) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns the NodeNumberService
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue nodeNumberService - service to get nodeNumbers
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return service to get nodeNumbers
     * @throws Exception - io-Exceptions possible
     */
    public NodeNumberService getNodeNumberService() throws Exception;
}