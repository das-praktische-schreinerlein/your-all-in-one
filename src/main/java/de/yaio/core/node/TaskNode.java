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
package de.yaio.core.node;
import java.util.Date;
import java.util.Map;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.yaio.core.datadomain.ExtendedWorkflowData;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.core.nodeservice.TaskNodeService;

/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 *     Persistence
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     bean for (projects, tasks, todos) with TaskNode-data like (Plan, is) 
 *     and matching businesslogic
 * 
 * @package de.yaio.core.node
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class TaskNode extends BaseNode implements ExtendedWorkflowData {
    @Transient
    protected static NodeService nodeDataService = new TaskNodeService();

    @XmlTransient
    @JsonIgnore
    public NodeService getNodeService() {
        return nodeDataService;
    }
    public static void setNodeDataService(final NodeService newNodeDataService) {
        nodeDataService = newNodeDataService;
    }

    @XmlTransient
    @JsonIgnore
    public static NodeService getConfiguredNodeService() {
        return nodeDataService;
    }

    @Override
    @XmlTransient
    @JsonIgnore
    public Map<String, Object> getConfigState() {
        return TaskNodeService.CONST_MAP_NODETYPE_IDENTIFIER;
    }
    
    @Override
    @XmlTransient
    @JsonIgnore
    public Map<String, WorkflowState> getConfigWorkflowState() {
        return TaskNodeService.CONST_MAP_STATE_WORKFLOWSTATE;
    }
    
    @Override
    public WorkflowState getWorkflowState() {
        WorkflowState masterState = super.getWorkflowState();
        if (masterState == WorkflowState.NOWORKFLOW) {
            // default if empty
            
            // calc from state
            if (this.getState() != null) {
                WorkflowState newState = this.getWorkflowStateForState(this.getState());
                if (newState != null) {
                    return newState;
                }
            }
            
            // return default
            return WorkflowState.NOTPLANED;
        }
        return masterState;
    };

    @Override
    @XmlTransient
    @JsonIgnore
    public WorkflowState getWorkflowStateForState(final String state)  throws IllegalStateException {
        // get WorkflowState for state
        WorkflowState wfState = getConfigWorkflowState().get(state);
        
        if (wfState == null) {
            // if null: second try - normalize state
            String newState = (String) getConfigState().get(state);
            wfState = getConfigWorkflowState().get(newState);
        }
        
        // unknown state
        if (wfState == null) {
            throw new IllegalStateException("No WorkflowState found for state=" + state 
                            + " node=" + this.getNameForLogger());
        }
        return wfState;
    };
    
    @Override
    @XmlTransient
    @JsonIgnore
    public String getStateForWorkflowState(final WorkflowState workflowState)  throws IllegalStateException {
        // workflowState must be set
        if (workflowState == null) {
            throw new IllegalStateException("workflowState must be set node=" + this.getNameForLogger());
        }
        
        // unknown state
        String state = TaskNodeService.CONST_MAP_WORKFLOWSTATE_STATE.get(workflowState);
        if (state == null) {
            throw new IllegalStateException("No state found for workflowState=" + workflowState 
                            + " node=" + this.getNameForLogger());
        }
        
        return state;
    };

    @Override
    @XmlTransient
    @JsonIgnore
    public Date getCurrentStart() {
        // wenn belegt IST-Daten benutzen, sonst Plandaten
        Date curStart = getIstStart();
        if (curStart != null) {
            return curStart;
        }

        return getPlanStart();
    }

    @Override
    @XmlTransient
    @JsonIgnore
    public Date getCurrentEnde() {
        // wenn belegt IST-Daten benutzen, sonst Plandaten
        Date curEnde = getIstEnde();
        Date curPlanEnde = this.getPlanEnde();
        Date curIstStart = getIstStart();
        Date curIstEnde = getIstEnde();
        Double curIstStan = getIstStand();
        if (curEnde != null) {
            if (curPlanEnde != null && curIstStart != null) {
                // wenn Istdaten vor Plandaten und noch nicht fertig, dann Plandaten
                if (curIstEnde.before(curPlanEnde) && curIstStan < 100) {
                    return curPlanEnde;
                }
            }
            return curIstEnde;
        }

        return curPlanEnde;
    }
    
}
