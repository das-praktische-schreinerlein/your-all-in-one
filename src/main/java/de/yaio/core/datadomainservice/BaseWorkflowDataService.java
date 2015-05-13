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

import java.util.Date;

import de.yaio.core.datadomain.BaseWorkflowData;
import de.yaio.core.datadomain.ExtendedWorkflowData;

/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for dataDomain: BaseWorkflowData
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface BaseWorkflowDataService {

    /** the position in the recalc-order at which the recalcer will run */
    int CONST_RECALC_ORDER = 10;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     calcs the initial plandata
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates membervars - 
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic Workflow
     * @param node - node
     * @throws Exception - parser/format-Exceptions possible
     */
    void calcPlanData(BaseWorkflowData node) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     recalcs the workflowdata
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates membervars - 
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic Workflow
     * @param node - node
     * @throws Exception - parser/format-Exceptions possible
     */
    void recalcWorkflowData(BaseWorkflowData node) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     recalcs the statedata
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates membervars - 
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic Workflow
     * @param node - node
     * @throws Exception - parser/format-Exceptions possible
     */
    void recalcStateData(BaseWorkflowData node) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     calcs the currentStart for the node (prio: ist before plan)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returns date
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic Workflow
     * @param node - node
     * @return - current start
     * @throws Exception - parser/format-Exceptions possible
     */
    Date calcCurrentStart(ExtendedWorkflowData node);

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     calcs the currentEnde for the node (prio: ist before plan)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returns date
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic Workflow
     * @param node - node
     * @return - current end
     * @throws Exception - parser/format-Exceptions possible
     */
    Date calcCurrentEnde(ExtendedWorkflowData node);
}
