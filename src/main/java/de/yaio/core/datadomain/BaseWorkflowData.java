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

package de.yaio.core.datadomain;

import java.util.Date;
import java.util.Map;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 * <h4>FeatureDescription:</h4>
 *     interface for BaseWorkflow (service-functions) of the Node
 * 
 * @package de.yaio.core.datadomain
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface BaseWorkflowData extends DataDomain, IstChildrenSumData, 
    PlanChildrenSumData {
    
    /** minimum of accepted dates 1970*/
    @Transient
    @XmlTransient
    @JsonIgnore
    public static Date CONST_MINDATE = new Date(0);
    /** maximum of accepted dates: 2038*/
    @Transient
    @XmlTransient
    @JsonIgnore
    public static Date CONST_MAXDATE = new Date(2147483647000l);

    public Map<String, Object> getConfigState();
    public String getState();
    public void setState(String state);

    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     checks weather the state is a configurated workflow-state
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - workflow-state yes/no
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param state - state to check
     * @return workflow-state yes/no
     */
    public boolean isWFStatus (String state);

    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     checks weather the state is a configurated workflow-state for DONE
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - workflow-DONE yes/no
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param state - state to check
     * @return workflow-DONE yes/no
     */
    public boolean isWFStatusDone (String state);

    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     checks weather the state is a configurated workflow-state for OPEN
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - workflow-OPEN yes/no
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param state - state to check
     * @return workflow-OPEN yes/no
     */
    public boolean isWFStatusOpen (String state);

    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     checks weather the state is a configurated workflow-state for CANCELED
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - workflow-CANCELED yes/no
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param state - state to check
     * @return workflow-CANCELED yes/no
     */
    public boolean isWFStatusCanceled(String state);
}
