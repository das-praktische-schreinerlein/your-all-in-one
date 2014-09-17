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
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.yaio.core.node.BaseNode;
import de.yaio.core.node.EventNode;
import de.yaio.core.node.InfoNode;
import de.yaio.core.node.TaskNode;
import de.yaio.core.node.UrlResNode;
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

    public class NodeViolation {
        public String path;
        public String message;
        public String messageTemplate;
        public NodeViolation(String path, String message, String messageTemplate) {
            super();
            this.path = path;
            this.message = message;
            this.messageTemplate = messageTemplate;
        }
    }
    
    public class NodeResponse {
        
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
        public String state;
        public String stateMsg;
        public BaseNode node;
        public List<String> parentIdHierarchy;
        public List<BaseNode> childNodes;
        public List<NodeViolation> violations;
    }
    
    protected NodeResponse createResponseObj(BaseNode node, String okMsg) {
        // extract parents
        List<String> parentIdHierarchy = node.getParentIdHierarchy();
        
        // reverse
        Collections.reverse(parentIdHierarchy);
        
        // add me
        parentIdHierarchy.add(node.getSysUID());

        // remove master
        if (parentIdHierarchy.size() > 0) {
            parentIdHierarchy.remove(0);
        }
        
        // set response
        NodeResponse response = new NodeResponse(
                        "OK", okMsg, 
                        node, parentIdHierarchy, null, null);
        
        return response;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeControllerResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @return NodeControllerResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method=RequestMethod.GET, value = "/show/{sysUID}")
    public @ResponseBody NodeResponse getNodeWithChildren(
           @PathVariable(value="sysUID") String sysUID) {
        // create default response
        NodeResponse response = new NodeResponse(
                        "ERROR", "node '" + sysUID + "' doesnt exists", 
                        null, null, null, null);

        // find a specific node
        BaseNode node = BaseNode.findBaseNode(sysUID);
        if (node != null) {
            // read the childnodes only 1 level
            node.initChildNodesFromDB(0);
            
            // create response
            response = createResponseObj(node, "node '" + sysUID + "' found");
            
            // add children
            response.childNodes = new ArrayList<BaseNode>(node.getChildNodes());
        }
        
        return response;
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     map the nodeData from newNode to origNode
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>boolean flgChange - true if data changed
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param origNode - 
     * @param newNode - the new node created from request-data
     * @return true if data changed
     * @throws IllegalAccessException - thrown if class of origNode!=newNode
     */
    public boolean mapNodeData(BaseNode origNode, BaseNode newNode) 
                    throws IllegalAccessException {
        boolean flgChange = false;
        
        // check class
        if (! origNode.getClassName().equals(newNode.getClassName())) {
            // class differ!!
            throw new IllegalAccessException("cant map origNode (" + origNode.getClassName() + "):" 
                            + origNode.getSysUID() + " with newNode:" + newNode.getClassName());
        }
        // check for new name
        if (newNode.getName() != null) {
            origNode.setName(newNode.getName());
            flgChange = true;
        }
        
        return flgChange;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     update the node sysUID and return it with children as JSON
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
    public NodeResponse updateNode(String sysUID, BaseNode newNode) {
        NodeResponse response = new NodeResponse(
                        "ERROR", "node '" + sysUID + "' doesnt exists", 
                        null, null, null, null);

        // find a specific node
        BaseNode node = BaseNode.findBaseNode(sysUID);
        if (node == null) {
            // node not found
            return response;
        }

        // map the data
        boolean flgChange = false;

        try {
            // read children
            BaseNode parent = null;
            node.initChildNodesFromDB(0);
            node.initChildNodesForParentsFromDB();
            
            // map data
            flgChange = mapNodeData(node, newNode);

            // check for needed update
            if (flgChange) {
                // recalc 
                node.recalcData(BaseNodeService.CONST_RECURSE_DIRECTION_PARENT);

                // save
                node.merge();
                parent = node.getParentNode();
                while (parent != null) {
                    parent.merge();
                    parent = parent.getParentNode();
                }
            }

            // create response
            response = createResponseObj(node, "node '" + sysUID + "' updated");

        } catch (ConstraintViolationException ex) {
            // validation errors
            Set<ConstraintViolation<?>> cViolations = ex.getConstraintViolations();
            
            // convert to Violation
            List<NodeViolation>violations = new ArrayList<NodeViolation>();
            for (ConstraintViolation<?> cViolation : cViolations) {
                violations.add(
                      new NodeViolation(cViolation.getPropertyPath().toString(), 
                                      cViolation.getMessage(),
                                      cViolation.getMessageTemplate()));
            }
            
            // create response
            response = new NodeResponse(
                            "ERROR", "violationerrors while updating node '" + sysUID + "':" + ex, 
                            null, null, null, violations);
            
            
        } catch (Throwable ex) {
            // errorhandling
            ex.printStackTrace();
            response = new NodeResponse(
                            "ERROR", "error while updating node '" + sysUID + "':" + ex, 
                            null, null, null, null);
        }
        
        return response;
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to update the BaseNode sysUID and return it with children as JSON
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
    @RequestMapping(method=RequestMethod.PATCH, value = "/update/BaseNode/{sysUID}")
    public @ResponseBody NodeResponse updateBaseNode(
                @PathVariable(value="sysUID") String sysUID, 
                @RequestBody BaseNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to update the EventNode sysUID and return it with children as JSON
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
    @RequestMapping(method=RequestMethod.PATCH, value = "/update/TaskNode/{sysUID}")
    public @ResponseBody NodeResponse updateTaskNode(
                @PathVariable(value="sysUID") String sysUID, 
                @RequestBody TaskNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to update the TaskNode sysUID and return it with children as JSON
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
    @RequestMapping(method=RequestMethod.PATCH, value = "/update/EventNode/{sysUID}")
    public @ResponseBody NodeResponse updateEventNode(
                @PathVariable(value="sysUID") String sysUID, 
                @RequestBody EventNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to update the TaskNode sysUID and return it with children as JSON
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
    @RequestMapping(method=RequestMethod.PATCH, value = "/update/UrlResNode/{sysUID}")
    public @ResponseBody NodeResponse updateEventNode(
                @PathVariable(value="sysUID") String sysUID, 
                @RequestBody UrlResNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to update the InfoNode sysUID and return it with children as JSON
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
    @RequestMapping(method=RequestMethod.PATCH, value = "/update/InfoNode/{sysUID}")
    public @ResponseBody NodeResponse updateEventNode(
                @PathVariable(value="sysUID") String sysUID, 
                @RequestBody InfoNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to move the node sysUID to newParentSysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeControllerResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @param newParentSysUID - sysUID of the new parent
     * @param newNode - the node created from request-data
     * @return NodeControllerResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method=RequestMethod.PATCH, value = "/move/{sysUID}/{newParentSysUID}")
    public @ResponseBody NodeResponse moveNode(
                @PathVariable(value="sysUID") String sysUID,
                @PathVariable(value="newParentSysUID") String newParentSysUID) {
        // create default response
        NodeResponse response = new NodeResponse(
                        "ERROR", "node '" + sysUID + "' doesnt exists", 
                        null, null, null, null);

        // find a specific node
        BaseNode node = BaseNode.findBaseNode(sysUID);
        if (node == null) {
            return response;
        }
        
        // check new parent
        BaseNode newParent = BaseNode.findBaseNode(newParentSysUID);
        if (newParent == null) {
            response = new NodeResponse(
                            "ERROR", "new parentNode '" + newParentSysUID 
                            + "' for node '" + sysUID + "' doesnt exists", 
                            null, null, null, null);
            return response;
        }
        // map the data
        boolean flgChangedParent = false;

        try {
            // read children
            BaseNode parent = null;
            node.initChildNodesFromDB(0);
            node.initChildNodesForParentsFromDB();

            // check for new parent
            BaseNode oldParent = node.getParentNode();
            if (newParent !=  null) {
                // got parent
                if (newParent.getSysUID() != oldParent.getSysUID()) {
                    // set new parent
                    flgChangedParent = true;
                    node.setParentNode(newParent);
                }
            }

            // check for needed update
            if (flgChangedParent) {
                // recalc 
                node.recalcData(BaseNodeService.CONST_RECURSE_DIRECTION_PARENT);

                // save
                node.merge();
                parent = node.getParentNode();
                while (parent != null) {
                    parent.merge();
                    parent = parent.getParentNode();
                }

                // renew oldParent
                oldParent = BaseNode.findBaseNode(oldParent.getSysUID());
                
                // reinit Children (only 1 level
                oldParent.initChildNodesFromDB(0);
                oldParent.initChildNodesForParentsFromDB();

                // recalc old parent 
                parent = oldParent;
                parent.recalcData(BaseNodeService.CONST_RECURSE_DIRECTION_PARENT);
                while (parent != null) {
                    parent.merge();
                    parent = parent.getParentNode();
                }
            }

            // create response
            response = createResponseObj(node, "node '" + sysUID 
                            + "' moved to " + newParentSysUID);

        } catch (Throwable ex) {
            // errorhandling
            ex.printStackTrace();
            response = new NodeResponse(
                            "ERROR", "error while moving node '" + sysUID + "':" + ex, 
                            null, null, null, null);
        }
        
        return response;
    }
}