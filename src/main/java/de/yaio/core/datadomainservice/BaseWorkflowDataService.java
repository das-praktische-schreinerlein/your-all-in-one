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

import de.yaio.core.datadomain.BaseWorkflowData;

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

    public static final int CONST_RECALC_ORDER = 10;

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
     * @throws Exception
     */
    public void recalcWorkflowData(BaseWorkflowData node) throws Exception;

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
     * @throws Exception
     */
    public void recalcStateData(BaseWorkflowData node) throws Exception;
}