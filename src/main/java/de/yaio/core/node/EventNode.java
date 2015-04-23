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
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.yaio.core.datadomainservice.MetaDataService;
import de.yaio.core.datadomainservice.MetaDataServiceImpl;
import de.yaio.core.datadomainservice.SysDataService;
import de.yaio.core.datadomainservice.SysDataServiceImpl;
import de.yaio.core.nodeservice.EventNodeService;
import de.yaio.core.nodeservice.NodeService;

/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 *     Persistence
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     bean with EventNode-data (calendar-events) and matching businesslogic
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
public class EventNode extends TaskNode {
    
    // Daten
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
    public static final Map<String, Object> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, Object>();
    public static final Map<String, WorkflowState> CONST_MAP_STATE_WORKFLOWSTATE = new HashMap<String, WorkflowState>();
    public static final Map<WorkflowState, String> CONST_MAP_WORKFLOWSTATE_STATE = new HashMap<WorkflowState, String>();
    static {
        // define WorkflowStates
        CONST_MAP_STATE_WORKFLOWSTATE.put(CONST_NODETYPE_IDENTIFIER_UNKNOWN, WorkflowState.NOTPLANED);
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

    protected static SysDataService sysDataService = new SysDataServiceImpl();
    protected static MetaDataService metaDataService = new MetaDataServiceImpl();
    protected static NodeService nodeDataService = new EventNodeService();

    @XmlTransient
    @JsonIgnore
    public NodeService getNodeService() {
        return nodeDataService;
    }
    @XmlTransient
    @JsonIgnore
    public static void setNodeDataService(final NodeService newNodeDataService) {
        nodeDataService = newNodeDataService;
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
    @XmlTransient
    @JsonIgnore
    public String getStateForWorkflowState(final WorkflowState workflowState)  throws IllegalStateException {
        return CONST_MAP_WORKFLOWSTATE_STATE.get(workflowState);
    };
}
