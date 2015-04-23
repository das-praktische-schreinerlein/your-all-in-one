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
import java.util.HashMap;
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
import de.yaio.utils.DataUtils;

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
    // Daten
    /** nodetype-identifier for parser/formatter on Tasknode Ist=0% */
    public static final String CONST_NODETYPE_IDENTIFIER_OPEN = "OFFEN";
    /** nodetype-identifier for parser/formatter on Tasknode Ist=0 and planstart<today */
    public static final String CONST_NODETYPE_IDENTIFIER_LATE = "LATE";
    /** nodetype-identifier for parser/formatter on Tasknode Ist>0 */
    public static final String CONST_NODETYPE_IDENTIFIER_RUNNNING = "RUNNING";
    /** nodetype-identifier for parser/formatter on Tasknode Ist>0 and planende<today */
    public static final String CONST_NODETYPE_IDENTIFIER_SHORT = "WARNING";
    /** nodetype-identifier for parser/formatter on Tasknode Ist=100 */
    public static final String CONST_NODETYPE_IDENTIFIER_DONE = "ERLEDIGT";
    /** nodetype-identifier for parser/formatter on Tasknode canceled */
    public static final String CONST_NODETYPE_IDENTIFIER_CANCELED = "VERWORFEN";

    // Status-Konstanten
    public static final Map<String, Object> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, Object>();
    public static final Map<String, WorkflowState> CONST_MAP_STATE_WORKFLOWSTATE = new HashMap<String, WorkflowState>();
    public static final Map<WorkflowState, String> CONST_MAP_WORKFLOWSTATE_STATE = new HashMap<WorkflowState, String>();
    static {
        // define WorkflowStates
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_UNKNOWN, WorkflowState.NOTPLANED);
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_OPEN, WorkflowState.OPEN);
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_RUNNNING, WorkflowState.RUNNING);
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_LATE, WorkflowState.LATE);
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_SHORT, WorkflowState.WARNING);
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_DONE, WorkflowState.DONE);
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_CANCELED, WorkflowState.CANCELED);
        // backlink states
        for (String state : CONST_MAP_STATE_WORKFLOWSTATE.keySet()) {
            CONST_MAP_WORKFLOWSTATE_STATE.put(CONST_MAP_STATE_WORKFLOWSTATE.get(state), state);
        }

        // Defaults
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_OPEN, CONST_NODETYPE_IDENTIFIER_OPEN);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_RUNNNING, CONST_NODETYPE_IDENTIFIER_RUNNNING);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_LATE, CONST_NODETYPE_IDENTIFIER_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_SHORT, CONST_NODETYPE_IDENTIFIER_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_DONE, CONST_NODETYPE_IDENTIFIER_DONE);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_CANCELED, CONST_NODETYPE_IDENTIFIER_CANCELED);
        // Abarten
        CONST_MAP_NODETYPE_IDENTIFIER.put("OPEN", CONST_NODETYPE_IDENTIFIER_OPEN);
        CONST_MAP_NODETYPE_IDENTIFIER.put("OFFEN", CONST_NODETYPE_IDENTIFIER_OPEN);
        CONST_MAP_NODETYPE_IDENTIFIER.put("RUNNING", CONST_NODETYPE_IDENTIFIER_RUNNNING);
        CONST_MAP_NODETYPE_IDENTIFIER.put("LAUFEND", CONST_NODETYPE_IDENTIFIER_RUNNNING);
        CONST_MAP_NODETYPE_IDENTIFIER.put("LATE", CONST_NODETYPE_IDENTIFIER_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("OVERDUE", CONST_NODETYPE_IDENTIFIER_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("VERSPÄTET", CONST_NODETYPE_IDENTIFIER_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("VERSPAETET", CONST_NODETYPE_IDENTIFIER_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("SHORT", CONST_NODETYPE_IDENTIFIER_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("ÜBERFÄLLIG", CONST_NODETYPE_IDENTIFIER_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("UEBERFAELLIG", CONST_NODETYPE_IDENTIFIER_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("WARNING", CONST_NODETYPE_IDENTIFIER_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("DONE", CONST_NODETYPE_IDENTIFIER_DONE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("ERLEDIGT", CONST_NODETYPE_IDENTIFIER_DONE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("CANCELED", CONST_NODETYPE_IDENTIFIER_CANCELED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("VERWORFEN", CONST_NODETYPE_IDENTIFIER_CANCELED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("GELOESCHT", CONST_NODETYPE_IDENTIFIER_CANCELED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("ABGEBROCHEN", CONST_NODETYPE_IDENTIFIER_CANCELED);
    }

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
        return CONST_MAP_NODETYPE_IDENTIFIER;
    }
    
    @Override
    @XmlTransient
    @JsonIgnore
    public Map<String, WorkflowState> getConfigWorkflowState() {
        return CONST_MAP_STATE_WORKFLOWSTATE;
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
        String state = CONST_MAP_WORKFLOWSTATE_STATE.get(workflowState);
        if (state == null) {
            throw new IllegalStateException("No state found for workflowState=" + workflowState 
                            + " node=" + this.getNameForLogger());
        }
        
        return state;
    };

    @Override
    @XmlTransient
    @JsonIgnore
    public String getDataBlocks4CheckSum() throws Exception {
        // Content erzeugen
        StringBuffer data = new StringBuffer();
        
        data.append(super.getDataBlocks4CheckSum())
            .append(" istStand=").append(getIstStand())
            .append(" istStart=").append(DataUtils.getNewDate(getIstStart()))
            .append(" istEnde=").append(DataUtils.getNewDate(getIstEnde()))
            .append(" istAufwand=").append(getIstAufwand())
            .append(" istTask=").append(getIstTask())
            .append(" planStart=").append(DataUtils.getNewDate(getPlanStart()))
            .append(" planEnde=").append(DataUtils.getNewDate(getPlanEnde()))
            .append(" planAufwand=").append(getPlanAufwand())
            .append(" planTask=").append(getPlanTask());
        return data.toString();
    }
    
    
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
