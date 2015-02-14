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
 *     the responseobject for crud-functions (show, create, delete) of the 
 *     NodeController for RESTfull-services<br>
 *      
 * @package de.yaio.webapp.controller
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class NodeActionResponse {
    
    /** the resulting state of the request OK/ERROR **/
    public String state;
    /** the corresponding message to the state **/
    public String stateMsg;
    /** the resulting node for the request (update/insert/move/show) **/
    public BaseNode node;
    /** the list of the parentSysUID from the resulting {@link NodeActionResponse#node} **/
    public List<String> parentIdHierarchy;
    /** the list of the childNodes from the resulting {@link NodeActionResponse#node} **/
    public List<BaseNode> childNodes;
    /** a list {@link NodeViolation} while updating/creating/moving the resulting {@link NodeActionResponse#node} **/
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
     * @param parentIdHierarchy - the list of the parentSysUID from the resulting {@link NodeActionResponse#node}
     * @param childNodes - the list of the childNodes from the resulting {@link NodeActionResponse#node}
     * @param violatons - a list {@link NodeViolation} while updating/creating/moving the resulting {@link NodeActionResponse#node}
     */
    public NodeActionResponse(final String state, final String stateMsg, final BaseNode node,
                           final List<String> parentIdHierarchy, 
                           final List<BaseNode> childNodes,
                           final List<NodeViolation> violatons) {
        super();
        this.state = state;
        this.stateMsg = stateMsg;
        this.node = node;
        this.parentIdHierarchy = parentIdHierarchy;
        this.childNodes = childNodes;
        this.violations = violatons;
    }

    /**
     * @return the {@link NodeActionResponse#state}
     */
    public String getState() {
        return this.state;
    }

    /**
     * @param state the {@link NodeActionResponse#state} to set
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * @return the {@link NodeActionResponse#stateMsg}
     */
    public String getStateMsg() {
        return this.stateMsg;
    }

    /**
     * @param stateMsg the {@link NodeActionResponse#stateMsg} to set
     */
    public void setStateMsg(final String stateMsg) {
        this.stateMsg = stateMsg;
    }

    /**
     * @return the {@link NodeActionResponse#node}
     */
    public BaseNode getNode() {
        return this.node;
    }

    /**
     * @param node the {@link NodeActionResponse#node} to set
     */
    public void setNode(final BaseNode node) {
        this.node = node;
    }

    /**
     * @return the {@link NodeActionResponse#parentIdHierarchy}
     */
    public List<String> getParentIdHierarchy() {
        return this.parentIdHierarchy;
    }

    /**
     * @param parentIdHierarchy the {@link NodeActionResponse#parentIdHierarchy} to set
     */
    public void setParentIdHierarchy(final List<String> parentIdHierarchy) {
        this.parentIdHierarchy = parentIdHierarchy;
    }

    /**
     * @return the {@link NodeActionResponse#violations}
     */
    public List<NodeViolation> getViolations() {
        return this.violations;
    }

    /**
     * @param violations the {@link NodeActionResponse#violations} to set
     */
    public void setViolations(final List<NodeViolation> violations) {
        this.violations = violations;
    }

    /**
     * @return the {@link NodeActionResponse#childNodes}
     */
    public List<BaseNode> getChildNodes() {
        return this.childNodes;
    }

    /**
     * @param childNodes the {@link NodeActionResponse#childNodes} to set
     */
    public void setChildNodes(final List<BaseNode> childNodes) {
        this.childNodes = childNodes;
    }
}
