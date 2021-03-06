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

import de.yaio.app.core.datadomain.BaseWorkflowData;
import de.yaio.app.core.datadomain.ExtendedWorkflowData;

import java.util.Date;

/** 
 * businesslogic for dataDomain: BaseWorkflowData
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface BaseWorkflowDataService {

    /** the position in the recalc-order at which the recalcer will run */
    int CONST_RECALC_ORDER = 10;

    /** 
     * calcs the initial plandata
     * @param node                   node
     */
    void calcPlanData(BaseWorkflowData node);

    /** 
     * recalcs the workflowdata
     * @param node                   node
     */
    void recalcWorkflowData(BaseWorkflowData node);

    /** 
     * recalcs the statedata
     * @param node                   node
     */
    void recalcStateData(BaseWorkflowData node);

    /** 
     * calcs the currentStart for the node (prio: ist before plan)
     * @param node                   node
     * @return                       current start
     * @throws Exception             parser/format-Exceptions possible
     */
    Date calcCurrentStart(ExtendedWorkflowData node);

    /** 
     * calcs the currentEnde for the node (prio: ist before plan)
     * @param node                   node
     * @return                       current end
     * @throws Exception             parser/format-Exceptions possible
     */
    Date calcCurrentEnde(ExtendedWorkflowData node);
}
