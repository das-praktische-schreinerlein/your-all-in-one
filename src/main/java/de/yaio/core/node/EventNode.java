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
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
        return EventNodeService.CONST_MAP_NODETYPE_IDENTIFIER;
    }
    
    @Override
    @XmlTransient
    @JsonIgnore
    public Map<String, WorkflowState> getConfigWorkflowState() {
        return EventNodeService.CONST_MAP_STATE_WORKFLOWSTATE;
    }
    
    @Override
    @XmlTransient
    @JsonIgnore
    public String getStateForWorkflowState(final WorkflowState workflowState)  throws IllegalStateException {
        return EventNodeService.CONST_MAP_WORKFLOWSTATE_STATE.get(workflowState);
    };
}
