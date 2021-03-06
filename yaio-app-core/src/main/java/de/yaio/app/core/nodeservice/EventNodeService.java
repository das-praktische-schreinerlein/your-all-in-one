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
package de.yaio.app.core.nodeservice;

import de.yaio.app.core.datadomain.WorkflowState;

import java.util.HashMap;
import java.util.Map;


/** 
 * businesslogic for entity: EventNode
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class EventNodeService extends TaskNodeService {

    // Daten
    /** nodetype-identifier for parser/formatter on Eventnode Ist=0 and planstart=null */
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_NOTPLANED = "EVENT_NOTPLANED";
    /** nodetype-identifier for parser/formatter on Eventnode Ist=0 and planstart>today */
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_PLANED = "EVENT_PLANED";
    /** nodetype-identifier for parser/formatter on Eventnode Ist=0 and planstart>today */
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_CONFIRMED = "EVENT_CONFIRMED";
    /** nodetype-identifier for parser/formatter on Eventnode Ist=0 and planstart<today */
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_LATE = "EVENT_LATE";
    /** nodetype-identifier for parser/formatter on Eventnode Ist>0 and planende<today */
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_SHORT = "EVENT_SHORT";
    /** nodetype-identifier for parser/formatter on Eventnode Ist>0 and planende>today */
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING = "EVENT_RUNNING";
    /** nodetype-identifier for parser/formatter on Eventnode Ist=100 and planende<today */
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_DONE = "EVENT_ERLEDIGT";
    /** nodetype-identifier for parser/formatter on Eventnode canceled */
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED = "EVENT_VERWORFEN";
    
    // Status-Konstanten
    private static final Map<String, String> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, String>();
    private static final Map<String, WorkflowState> CONST_MAP_STATE_WORKFLOWSTATE = 
                    new HashMap<String, WorkflowState>();
    private static final Map<WorkflowState, String> CONST_MAP_WORKFLOWSTATE_STATE = 
                    new HashMap<WorkflowState, String>();

    static {
        // define WorkflowStates
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_EVENT_NOTPLANED, WorkflowState.NOTPLANED);
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_EVENT_PLANED, WorkflowState.OPEN);
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING, WorkflowState.RUNNING);
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_EVENT_LATE, WorkflowState.LATE);
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_EVENT_SHORT, WorkflowState.WARNING);
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_EVENT_DONE, WorkflowState.DONE);
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED, WorkflowState.CANCELED);
        // backlink states
        for (String state : CONST_MAP_STATE_WORKFLOWSTATE.keySet()) {
            CONST_MAP_WORKFLOWSTATE_STATE.put(CONST_MAP_STATE_WORKFLOWSTATE.get(state), state);
        }

        // Defaults
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_NOTPLANED, 
                        CONST_NODETYPE_IDENTIFIER_EVENT_NOTPLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_PLANED, 
                        CONST_NODETYPE_IDENTIFIER_EVENT_PLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING, 
                        CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_LATE, CONST_NODETYPE_IDENTIFIER_EVENT_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_SHORT, CONST_NODETYPE_IDENTIFIER_EVENT_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_DONE, CONST_NODETYPE_IDENTIFIER_EVENT_DONE);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED, 
                        CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED);
        // Abarten
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_UNGEPLANT", CONST_NODETYPE_IDENTIFIER_EVENT_NOTPLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_NOTPLANED", CONST_NODETYPE_IDENTIFIER_EVENT_NOTPLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_NOPLAN", CONST_NODETYPE_IDENTIFIER_EVENT_NOTPLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_PLANED", CONST_NODETYPE_IDENTIFIER_EVENT_PLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_GREPLANT", CONST_NODETYPE_IDENTIFIER_EVENT_PLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_OPEN", CONST_NODETYPE_IDENTIFIER_EVENT_PLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_OFFEN", CONST_NODETYPE_IDENTIFIER_EVENT_PLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_RUNNING", CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_LAUFEND", CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_LATE", CONST_NODETYPE_IDENTIFIER_EVENT_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_OVERDUE", CONST_NODETYPE_IDENTIFIER_EVENT_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_VERSPÄTET", CONST_NODETYPE_IDENTIFIER_EVENT_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_VERSPAETET", CONST_NODETYPE_IDENTIFIER_EVENT_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_SHORT", CONST_NODETYPE_IDENTIFIER_EVENT_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_ÜBERFÄLLIG", CONST_NODETYPE_IDENTIFIER_EVENT_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_UEBERFAELLIG", CONST_NODETYPE_IDENTIFIER_EVENT_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_WARNING", CONST_NODETYPE_IDENTIFIER_EVENT_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_DONE", CONST_NODETYPE_IDENTIFIER_EVENT_DONE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_ERLEDIGT", CONST_NODETYPE_IDENTIFIER_EVENT_DONE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_CANCELED", CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_VERWORFEN", CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_GELOESCHT", CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_ABGEBROCHEN", CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED);
    }

    private static EventNodeService instance = new EventNodeService();
    
    /** 
     * return the main instance of this service
     * @return                       the main instance of this service
     */
    public static EventNodeService getInstance() {
        return instance;
    }

    @Override
    public Map<String, String> getConfigState() {
        return CONST_MAP_NODETYPE_IDENTIFIER;
    }
    @Override
    public Map<String, WorkflowState> getConfigWorkflowState() {
        return CONST_MAP_STATE_WORKFLOWSTATE;
    }
    @Override
    public Map<WorkflowState, String> getConfigWorkflowStateState() {
        return CONST_MAP_WORKFLOWSTATE_STATE;
    }
    
    @Override
    public boolean isWFStatus(final String state) {
        if (CONST_NODETYPE_IDENTIFIER_EVENT_NOTPLANED.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_EVENT_PLANED.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_EVENT_CONFIRMED.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_EVENT_SHORT.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_EVENT_LATE.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_EVENT_DONE.equalsIgnoreCase(state)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isWFStatusDone(final String state) {
        if (CONST_NODETYPE_IDENTIFIER_EVENT_DONE.equalsIgnoreCase(state)) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isWFStatusCanceled(final String state) {
        if (CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED.equalsIgnoreCase(state)) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isWFStatusOpen(final String state) {
        if (CONST_NODETYPE_IDENTIFIER_EVENT_NOTPLANED.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_EVENT_PLANED.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_EVENT_CONFIRMED.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_EVENT_SHORT.equalsIgnoreCase(state)) {
            return true;
        } else if (CONST_NODETYPE_IDENTIFIER_EVENT_LATE.equalsIgnoreCase(state)) {
            return true;
        }

        return false;
    }
}
