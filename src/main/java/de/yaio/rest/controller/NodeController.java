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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.yaio.core.node.BaseNode;
import de.yaio.core.nodeservice.BaseNodeService;

/**
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     the controller for RESTful Web Services for BaseNodes<br>
 *      
 * @package de.yaio.rest.controller
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Controller
@RequestMapping("/nodes")
public class NodeController {

    public class NodeResponse {
        
        public NodeResponse(String state, String stateMsg, BaseNode node,
                               List<String> parentIdHierarchy) {
            super();
            this.state = state;
            this.stateMsg = stateMsg;
            this.node = node;
            this.parentIdHierarchy = parentIdHierarchy;
        }
        public String state;
        public String stateMsg;
        public BaseNode node;
        public List<String> parentIdHierarchy;
    }
    
    protected NodeResponse createResponseObj(BaseNode node, String okMsg) {
        // extract parents
        List<String> parentIdHierarchy = new ArrayList<String>();
        parentIdHierarchy.add(node.getSysUID());
        BaseNode parent = node.getParentNode();
        while (parent != null) {
            parentIdHierarchy.add(parent.getSysUID());
            parent = parent.getParentNode();
        }
        
        // reverse
        Collections.reverse(parentIdHierarchy);
        
        // remove master
        parentIdHierarchy.remove(0);
        
        // set response
        NodeResponse response = new NodeResponse(
                        "OK", okMsg, 
                        node, parentIdHierarchy);
        
        return response;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     read the node for sysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeControllerResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @return NodeControllerResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method=RequestMethod.GET, value = "/{sysUID}")
    public @ResponseBody NodeResponse getNodeWithChildren(
           @PathVariable(value="sysUID") String sysUID) {
        // create default response
        NodeResponse response = new NodeResponse(
                        "ERROR", "node '" + sysUID + "' doest exists", 
                        null, null);

        // find a specific node
        BaseNode node = BaseNode.findBaseNode(sysUID);
        if (node != null) {
            // read the childnodes only 1 level
            node.initChildNodesFromDB(0);
            
            // create response
            response = createResponseObj(node, "node '" + sysUID + "' found");
        }
        
        return response;
    }
    
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     update the node for sysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeControllerResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @param newNode - the node created from request-data
     * @return NodeControllerResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method=RequestMethod.PATCH, value = "/{sysUID}")
    public @ResponseBody NodeResponse updateNode(
                @PathVariable(value="sysUID") String sysUID, 
                @RequestBody BaseNode newNode) {
        // create default response
        NodeResponse response = new NodeResponse(
                        "ERROR", "node '" + sysUID + "' doest exists", 
                        null, null);

        // find a specific node
        BaseNode node = BaseNode.findBaseNode(sysUID);
        if (node != null) {
            // map the data
            boolean flgChange = false;
            boolean flgChangedParent = false;

            try {
                // read children
                BaseNode parent = null;
                node.initChildNodesFromDB(0);

                // check for new name
                if (newNode.getName() != null) {
                    node.setName(newNode.getName());
                    flgChange = true;
                }

                // check for new parent
                BaseNode oldParent = node.getParentNode();
                if (newNode.getParentNode() !=  null) {
                    // got parent
                    if (newNode.getParentNode() != oldParent) {
                        // set new parent
                        flgChangedParent = true;
                        node.setParentNode(newNode.getParentNode());
                    }
                }

                // check for needed update
                if (flgChange || flgChangedParent) {
                    // recalc 
                    // TODO recalcParents
                    node.recalcData(BaseNodeService.CONST_RECURSE_DIRECTION_ONLYME);

                    // save
                    node.merge();
                    parent = node.getParentNode();
                    while (parent != null) {
                        parent.merge();
                        parent = parent.getParentNode();
                    }
                }

                if (flgChangedParent) {
                    // reinit Children (only 1 level
                    oldParent.initChildNodesFromDB(0);

                    // recalc old parent 
                    // TODO recalcParents
                    parent = oldParent;
//                    parent.recalcData(BaseNodeService.CONST_RECURSE_DIRECTION_PARENT);
                    while (parent != null) {
                        parent.merge();
                        parent = parent.getParentNode();
                    }
                }

                // create response
                response = createResponseObj(node, "node '" + sysUID + "' updated");

            } catch (Throwable ex) {
                // errorhandling
                ex.printStackTrace();
                response = new NodeResponse(
                                "ERROR", "erro while updating node '" + sysUID + "':" + ex, 
                                null, null);
            }
        }
        
        return response;
    }
}