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

package de.yaio.core.datadomain;

import java.util.Date;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/** 
 * interface for BaseWorkflow (service-functions) of the Node
 * 
 * @FeatureDomain                DataDefinition
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface BaseWorkflowData extends DataDomain, IstChildrenSumData, 
    PlanCalcData, PlanDependencieData, PlanChildrenSumData {
    
    /** minimum of accepted dates 1970*/
    @Transient
    @XmlTransient
    @JsonIgnore
    Date CONST_MINDATE = new Date(0);
    /** maximum of accepted dates: 2038*/
    @Transient
    @XmlTransient
    @JsonIgnore
    Date CONST_MAXDATE = new Date(2147483647000L);
    
    @XmlTransient
    public static enum WorkflowState {
        // the order is important for the calculation of workflow!!!!
        NOWORKFLOW, 
        NOTPLANED, CANCELED, DONE, 
        OPEN, CONFIRMED, 
        RUNNING, 
        LATE, WARNING
    }

    String getState();
    void setState(String state);
    
    WorkflowState getWorkflowState();
    void setWorkflowState(WorkflowState istState);
    
    void resetBaseWorkflowData();

    /** 
     * checks weather the state is a configurated workflow-state
     * @FeatureDomain                Workflow
     * @FeatureResult                returnValue boolean - workflow-state yes/no
     * @FeatureKeywords              Worflow
     * @param state                  state to check
     * @return                       workflow-state yes/no
     */
    boolean isWFStatus(String state);

    /** 
     * checks weather the state is a configurated workflow-state for DONE
     * @FeatureDomain                Workflow
     * @FeatureResult                returnValue boolean - workflow-DONE yes/no
     * @FeatureKeywords              Worflow
     * @param state                  state to check
     * @return                       workflow-DONE yes/no
     */
    boolean isWFStatusDone(String state);

    /** 
     * checks weather the state is a configurated workflow-state for OPEN
     * @FeatureDomain                Workflow
     * @FeatureResult                returnValue boolean - workflow-OPEN yes/no
     * @FeatureKeywords              Worflow
     * @param state                  state to check
     * @return                       workflow-OPEN yes/no
     */
    boolean isWFStatusOpen(String state);

    /** 
     * checks weather the state is a configurated workflow-state for CANCELED
     * @FeatureDomain                Workflow
     * @FeatureResult                returnValue boolean - workflow-CANCELED yes/no
     * @FeatureKeywords              Worflow
     * @param state                  state to check
     * @return                       workflow-CANCELED yes/no
     */
    boolean isWFStatusCanceled(String state);
}
