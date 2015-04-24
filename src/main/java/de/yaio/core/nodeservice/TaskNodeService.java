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
package de.yaio.core.nodeservice;

import java.util.HashMap;
import java.util.Map;

import de.yaio.core.datadomain.BaseWorkflowData.WorkflowState;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.TaskNode;
import de.yaio.utils.DataUtils;


/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for entity: TaskNode
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class TaskNodeService extends BaseNodeService {

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
        CONST_MAP_STATE_WORKFLOWSTATE.put(BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN, WorkflowState.NOTPLANED);
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
    
    private static TaskNodeService instance = new TaskNodeService();
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     return the main instance of this service
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return the main instance of this service
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @return the main instance of this service
     */
    public static TaskNodeService getInstance() {
        return instance;
    }
    

    @Override
    public boolean isWFStatus(final String state) {
        if (CONST_NODETYPE_IDENTIFIER_OPEN.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_RUNNNING.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_SHORT.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_LATE.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_DONE.equalsIgnoreCase(state)) {
            return true;
        }

        
        return false;
    }

    @Override
    public boolean isWFStatusDone(final String state) {
        if (CONST_NODETYPE_IDENTIFIER_DONE.equalsIgnoreCase(state)) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isWFStatusCanceled(final String state) {
        if (CONST_NODETYPE_IDENTIFIER_CANCELED.equalsIgnoreCase(state)) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isWFStatusOpen(final String state) {
        if (CONST_NODETYPE_IDENTIFIER_OPEN.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_RUNNNING.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_SHORT.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_LATE.equalsIgnoreCase(state)) {
            return true;
        }

        return false;
    }

    @Override
    public String getDataBlocks4CheckSum(final DataDomain baseNode) throws Exception {
        TaskNode node = (TaskNode) baseNode;

        // Content erzeugen
        StringBuffer data = new StringBuffer();
        data.append(super.getDataBlocks4CheckSum(node))
            .append(" istStand=").append(node.getIstStand())
            .append(" istStart=").append(DataUtils.getNewDate(node.getIstStart()))
            .append(" istEnde=").append(DataUtils.getNewDate(node.getIstEnde()))
            .append(" istAufwand=").append(node.getIstAufwand())
            .append(" istTask=").append(node.getIstTask())
            .append(" planStart=").append(DataUtils.getNewDate(node.getPlanStart()))
            .append(" planEnde=").append(DataUtils.getNewDate(node.getPlanEnde()))
            .append(" planAufwand=").append(node.getPlanAufwand())
            .append(" planTask=").append(node.getPlanTask());
        return data.toString();
    }
}
