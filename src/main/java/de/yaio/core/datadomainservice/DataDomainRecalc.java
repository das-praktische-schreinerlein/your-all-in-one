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

import de.yaio.core.datadomain.DataDomain;


/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     interface for baseservice with businesslogic of datadomains
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface DataDomainRecalc extends Comparable<DataDomainRecalc> {
    
    /** the position in the recalc-order at which the recalcer will run */
    public static final int CONST_ORDER_NOOP = -1;
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     recalcs the DataDomain connected to the Recalcer before the children 
     *     are recalced
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberVariables
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BsuinessLogic
     * @param node - node to recalc
     * @param recurceDirection - Type of recursion (parent, me, children) NodeService.CONST_RECURSE_DIRECTION_*
     * @throws Exception - parser/format-Exceptions possible
     */
    public void doRecalcBeforeChildren(DataDomain node, int recurceDirection) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     recalcs the DataDomain connected to the Recalcer after the children 
     *     are recalced
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberVariables
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BsuinessLogic
     * @param node - node to recalc
     * @param recurceDirection - Type of recursion (parent, me, children) NodeService.CONST_RECURSE_DIRECTION_*
     * @throws Exception - parser/format-Exceptions possible
     */
    public void doRecalcAfterChildren(DataDomain node, int recurceDirection) throws Exception;
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns the class of the DataDomain for which the service runs
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Class - DataDomain for which the service runs
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return DataDomain for which the service runs
     */
    public Class<?> getRecalcTargetClass();

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns position in the service-queue
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue int - position in the service-queue
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return position in the service-queue
     */
    public int getRecalcTargetOrder();
    
    
}
