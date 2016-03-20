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

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.nodeservice.NodeService;

/** 
 * interface for baseservice with businesslogic of datadomains
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface DataDomainRecalc extends Comparable<DataDomainRecalc> {
    
    /** the position in the recalc-order at which the recalcer will run */
    int CONST_ORDER_NOOP = -1;
    
    /** 
     * recalcs the DataDomain connected to the Recalcer before the children 
     * are recalced
     * @param node                   node to recalc
     * @param recurseDirection       Type of recursion (parent, me, children) RecalcRecurseDirection.*
     * @throws Exception             parser/format-Exceptions possible
     */
    void doRecalcBeforeChildren(DataDomain node, NodeService.RecalcRecurseDirection recurseDirection) throws Exception;

    /** 
     * recalcs the DataDomain connected to the Recalcer after the children 
     * are recalced
     * @param node                   node to recalc
     * @param recurseDirection       Type of recursion (parent, me, children) RecalcRecurseDirection.*
     * @throws Exception             parser/format-Exceptions possible
     */
    void doRecalcAfterChildren(DataDomain node, NodeService.RecalcRecurseDirection recurseDirection) throws Exception;

    /** 
     * returns the class of the DataDomain for which the service runs
     * @return                       DataDomain for which the service runs
     */
    Class<?> getRecalcTargetClass();

    /** 
     * returns position in the service-queue
     * @return                       position in the service-queue
     */
    int getRecalcTargetOrder();
}
