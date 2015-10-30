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
package de.yaio.rest.controller;

import java.util.List;

import de.yaio.core.node.BaseNode;
import de.yaio.extension.datatransfer.json.CommonJSONResponse;

/** 
 * the responseobject for crud-functions (show, create, delete) of the 
 * NodeController for RESTfull-services<br>
 *  
 * @FeatureDomain                Webservice
 * @package                      de.yaio.webapp.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class NodeActionResponse extends CommonJSONResponse {
    
    /** the list of the childNodes from the resulting {@link NodeActionResponse#node} **/
    public List<BaseNode> childNodes;
    /** a list {@link NodeViolation} while updating/creating/moving the resulting {@link NodeActionResponse#node} **/
    public List<NodeViolation> violations;

    /** 
     * create a response-obj
     * @FeatureDomain                Constructor
     * @FeatureResult                a response-obj with the resulting node-data
     * @FeatureKeywords              Constructor
     * @param state                  the resulting state of the request OK/ERROR
     * @param stateMsg               the corresponding message to the state
     * @param node                   the resulting node for the request (update/insert/move/show)
     * @param parentIdHierarchy      the list of the parentSysUID from the resulting {@link NodeActionResponse#node}
     * @param childNodes             the list of the childNodes from the resulting {@link NodeActionResponse#node}
     * @param violatons              a list {@link NodeViolation} while updating/creating/moving the resulting {@link NodeActionResponse#node}
     */
    public NodeActionResponse(final String state, final String stateMsg, final BaseNode node,
                           final List<String> parentIdHierarchy, 
                           final List<BaseNode> childNodes,
                           final List<NodeViolation> violatons) {
        super(state, stateMsg, node, parentIdHierarchy);
        this.childNodes = childNodes;
        this.violations = violatons;
    }

    /**
     * @return                       the {@link NodeActionResponse#violations}
     */
    public List<NodeViolation> getViolations() {
        return this.violations;
    }

    /**
     * @param violations             the {@link NodeActionResponse#violations} to set
     */
    public void setViolations(final List<NodeViolation> violations) {
        this.violations = violations;
    }

    /**
     * @return                       the {@link NodeActionResponse#childNodes}
     */
    public List<BaseNode> getChildNodes() {
        return this.childNodes;
    }

    /**
     * @param childNodes             the {@link NodeActionResponse#childNodes} to set
     */
    public void setChildNodes(final List<BaseNode> childNodes) {
        this.childNodes = childNodes;
    }
}
