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
package de.yaio.rest.controller;

import java.util.List;

import de.yaio.core.node.BaseNode;

/**
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     the responseobject for NodeController the RESTful Web Services for BaseNodes<br>
 *      
 * @package de.yaio.rest.controller
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class NodeResponse {
    
    /** the resulting state of the request OK/ERROR **/
    public String state;
    /** the corresponding message to the state **/
    public String stateMsg;
    /** the resulting node for the request (update/insert/move/show) **/
    public BaseNode node;
    /** the list of the parentSysUID from the resulting {@link NodeResponse#node} **/
    public List<String> parentIdHierarchy;
    /** the list of the childNodes from the resulting {@link NodeResponse#node} **/
    public List<BaseNode> childNodes;
    /** a list {@link NodeViolation} while updating/creating/moving the resulting {@link NodeResponse#node} **/
    public List<NodeViolation> violations;

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     create a response-obj
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>a response-obj with the resulting node-data
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     * @param state - the resulting state of the request OK/ERROR
     * @param stateMsg - the corresponding message to the state
     * @param node - the resulting node for the request (update/insert/move/show)
     * @param parentIdHierarchy - the list of the parentSysUID from the resulting {@link NodeResponse#node}
     * @param childNodes - the list of the childNodes from the resulting {@link NodeResponse#node}
     * @param violatons - a list {@link NodeViolation} while updating/creating/moving the resulting {@link NodeResponse#node}
     */
    public NodeResponse(String state, String stateMsg, BaseNode node,
                           List<String> parentIdHierarchy, 
                           List<BaseNode> childNodes,
                           List<NodeViolation> violatons) {
        super();
        this.state = state;
        this.stateMsg = stateMsg;
        this.node = node;
        this.parentIdHierarchy = parentIdHierarchy;
        this.childNodes = childNodes;
        this.violations = violatons;
    }

    /**
     * @return the {@link NodeResponse#state}
     */
    public String getState() {
        return this.state;
    }

    /**
     * @param state the {@link NodeResponse#state} to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the {@link NodeResponse#stateMsg}
     */
    public String getStateMsg() {
        return this.stateMsg;
    }

    /**
     * @param stateMsg the {@link NodeResponse#stateMsg} to set
     */
    public void setStateMsg(String stateMsg) {
        this.stateMsg = stateMsg;
    }

    /**
     * @return the {@link NodeResponse#node}
     */
    public BaseNode getNode() {
        return this.node;
    }

    /**
     * @param node the {@link NodeResponse#node} to set
     */
    public void setNode(BaseNode node) {
        this.node = node;
    }

    /**
     * @return the {@link NodeResponse#parentIdHierarchy}
     */
    public List<String> getParentIdHierarchy() {
        return this.parentIdHierarchy;
    }

    /**
     * @param parentIdHierarchy the {@link NodeResponse#parentIdHierarchy} to set
     */
    public void setParentIdHierarchy(List<String> parentIdHierarchy) {
        this.parentIdHierarchy = parentIdHierarchy;
    }

    /**
     * @return the {@link NodeResponse#violations}
     */
    public List<NodeViolation> getViolations() {
        return this.violations;
    }

    /**
     * @param violations the {@link NodeResponse#violations} to set
     */
    public void setViolations(List<NodeViolation> violations) {
        this.violations = violations;
    }

    /**
     * @return the {@link NodeResponse#childNodes}
     */
    public List<BaseNode> getChildNodes() {
        return this.childNodes;
    }

    /**
     * @param childNodes the {@link NodeResponse#childNodes} to set
     */
    public void setChildNodes(List<BaseNode> childNodes) {
        this.childNodes = childNodes;
    }
}