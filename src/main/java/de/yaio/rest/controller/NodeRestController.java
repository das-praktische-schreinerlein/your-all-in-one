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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.yaio.core.dbservice.BaseNodeDBService;
import de.yaio.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.EventNode;
import de.yaio.core.node.InfoNode;
import de.yaio.core.node.SymLinkNode;
import de.yaio.core.node.TaskNode;
import de.yaio.core.node.UrlResNode;
import de.yaio.core.nodeservice.BaseNodeService;
import de.yaio.utils.Calculator;

/**
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     the controller for RESTful Web Services for BaseNodes<br>
 *      
 * @package de.yaio.webapp.controller
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Controller
@RequestMapping("/nodes")
public class NodeRestController {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(NodeRestController.class);
    

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     create an response.obj for the node with state OK and the corresponding message<br>
     *     it will automaticaly set the parentHierarchy
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK) with the node, hierarchy and OK-message
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice 
     * @param node - the node for the response
     * @param okMsg - the message
     * @return NodeResponse (OK) with the node, hierarchy and OK-message
     */
    public static NodeActionResponse createResponseObj(final BaseNode node, final String okMsg) {
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
        NodeActionResponse response = new NodeActionResponse(
                        "OK", okMsg, 
                        node, parentIdHierarchy, null, null);
        
        return response;
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     common function to search childnodes of sysUID and return them as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with matching nodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param curPage - current page in result to display
     * @param pageSize - max items per page
     * @param sortConfig - use sort
     * @param sysUID - sysUID to filter as perentNode
     * @param fulltext - fulltext search string
     * @return NodeSearchResponse (OK, FAILED, ERROR) with matching nodes
     */
    public NodeSearchResponse commonSearchNode(final Long curPage,
          final Long pageSize, final String sortConfig, final String sysUID, final String fulltext) {
        // create default response
        NodeSearchResponse response = new NodeSearchResponse(
                        "OK", "no node found", 
                        null, curPage, pageSize, 0L);

        // find the baseNode
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        BaseNode node = BaseNode.findBaseNode(sysUID);
        if (node != null) {
            // search nodes
            List<BaseNode> resultList = baseNodeDBService.findFulltextBaseNodeEntries(fulltext,
                            sortConfig, ((curPage.intValue() - 1) * pageSize.intValue()), 
                            pageSize.intValue());

            // create response
            response.setCount(baseNodeDBService.countFulltextBaseNodes(fulltext));
            
            // add node
            response.setNodes(resultList);
            
            // set state
            response.setStateMsg("node found");
        }
        
        return response;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to search childnodes of sysUID and return them as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with matching nodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param curPage - current page in result to display
     * @param pageSize - max items per page
     * @param sortConfig - use sort
     * @param sysUID - sysUID to filter as perentNode
     * @param fulltext - fulltext search string
     * @return NodeSearchResponse (OK, FAILED, ERROR) with matching nodes
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search/{curPage}/{pageSize}/{sortConfig}/{sysUID}/{fulltext}/")
    public @ResponseBody NodeSearchResponse searchNodeFulltext(
           @PathVariable(value = "curPage") final Long curPage,
           @PathVariable(value = "pageSize") final Long pageSize,
           @PathVariable(value = "sortConfig") final String sortConfig,
           @PathVariable(value = "sysUID") final String sysUID,
           @PathVariable(value = "fulltext") final String fulltext
           ) {
        return commonSearchNode(curPage, pageSize, sortConfig, sysUID, fulltext);
    }
    

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to search childnodes of sysUID and return them as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with matching nodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param curPage - current page in result to display
     * @param pageSize - max items per page
     * @param sortConfig - use sort
     * @param sysUID - sysUID to filter as perentNode
     * @return NodeSearchResponse (OK, FAILED, ERROR) with matching nodes
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search/{curPage}/{pageSize}/{sortConfig}/{sysUID}/")
    public @ResponseBody NodeSearchResponse searchNode(
           @PathVariable(value = "curPage") final Long curPage,
           @PathVariable(value = "pageSize") final Long pageSize,
           @PathVariable(value = "sortConfig") final String sortConfig,
           @PathVariable(value = "sysUID") final String sysUID
           ) {
        return commonSearchNode(curPage, pageSize, sortConfig, sysUID, "");
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to read the node for sysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.GET, value = "/show/{sysUID}")
    public @ResponseBody NodeActionResponse getNodeWithChildren(
           @PathVariable(value = "sysUID") final String sysUID) {
        // create default response
        NodeActionResponse response = new NodeActionResponse(
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
     *     Request to read the SymLinkRef-node for sysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param symLinkRef - symLinkRef to filter
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.GET, value = "/showsymlink/{symLinkRef}")
    public @ResponseBody NodeActionResponse getSymLinkRefNodeWithChildren(
           @PathVariable(value = "symLinkRef") final String symLinkRef) {
        // create default response
        NodeActionResponse response = new NodeActionResponse(
                        "ERROR", "node '" + symLinkRef + "' doesnt exists", 
                        null, null, null, null);

        // find a specific node
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        List<BaseNode> nodes = baseNodeDBService.findSymLinkBaseNode(symLinkRef);
        if (nodes != null && nodes.size() > 0) {
            // symLinkref found
            
            // check if it is unique
            if (nodes.size() > 1) {
                response = new NodeActionResponse(
                                "ERROR", "node '" + symLinkRef + "' is not unique", 
                                null, null, nodes, null);
                return response;

            }
            
            // use the first one
            BaseNode node = nodes.get(0);
            
            // read the childnodes only 1 level
            node.initChildNodesFromDB(0);
            
            // create response
            response = createResponseObj(node, "node '" + symLinkRef + "' found");
            
            // add children
            response.childNodes = new ArrayList<BaseNode>(node.getChildNodes());
        }
        
        return response;
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to delete the node for sysUID and return its parent with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the parentNode for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to delete
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{sysUID}")
    public @ResponseBody NodeActionResponse deleteNodeWithChildren(
           @PathVariable(value = "sysUID") final String sysUID) {
        // create default response
        NodeActionResponse response = new NodeActionResponse(
                        "ERROR", "node '" + sysUID + "' doesnt exists", 
                        null, null, null, null);

        // find a specific node
        BaseNode node = BaseNode.findBaseNode(sysUID);
        if (node != null) {
            // read parent
            BaseNode parent = node.getParentNode();
            
            // delete the node
            node.removeChildNodesFromDB();
            node.remove();
            
            try {
                // recalc parent
                updateMeAndMyParents(parent);
            } catch (Exception e) {
                LOGGER.error("violationerrors while deleting node '" 
                                + sysUID + "':", e);
                LOGGER.error("error deleting node '" 
                                + node);
                e.printStackTrace();
            }
            
            // create response
            response = createResponseObj(parent, "node '" + sysUID + "' deleted");
            
            // add children
            response.childNodes = new ArrayList<BaseNode>(parent.getChildNodes());
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
    public boolean mapNodeData(final BaseNode origNode, final BaseNode newNode) 
                    throws IllegalAccessException {
        boolean flgChange = false;
        
        // check class
        if (! origNode.getClassName().equals(newNode.getClassName())) {
            // class differ!!
            throw new IllegalAccessException("cant map origNode (" + origNode.getClassName() + "):" 
                            + origNode.getSysUID() + " with newNode:" + newNode.getClassName());
        }
        
        // common-fields

        // check for new name
        if (Calculator.compareValues(
                        origNode.getName(), newNode.getName()) 
                        != Calculator.CONST_COMPARE_EQ) {
            origNode.setName(newNode.getName());
            flgChange = true;
        }
        // check for type
        if (Calculator.compareValues(
                        origNode.getType(), newNode.getType()) 
                        != Calculator.CONST_COMPARE_EQ) {
            origNode.setType(newNode.getType());
            flgChange = true;
        }
        // check for nodeDesc
        if (Calculator.compareValues(
                        origNode.getNodeDesc(), newNode.getNodeDesc()) 
                        != Calculator.CONST_COMPARE_EQ) {
            origNode.setNodeDesc(newNode.getNodeDesc());
            flgChange = true;
        }
        
        // check for special nodedata recursively
        
        // Task+EventNodes
        if (   TaskNode.class.isInstance(origNode) 
            || EventNode.class.isInstance(origNode)) {
            TaskNode newTaskNode = (TaskNode) newNode;
            TaskNode origTaskNode = (TaskNode) origNode;
            
            // get state from type
            origNode.setState(origNode.getType());
            // check for Plan aufwand
            if (Calculator.compareValues(
                            origTaskNode.getPlanAufwand(), newTaskNode.getPlanAufwand()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setPlanAufwand(newTaskNode.getPlanAufwand());
                flgChange = true;
            }
            // check for Plan datestart
            if (Calculator.compareValues(
                            origTaskNode.getPlanStart(), newTaskNode.getPlanStart()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setPlanStart(newTaskNode.getPlanStart());
                flgChange = true;
            }
            // check for Plan dateend
            if (Calculator.compareValues(
                            origTaskNode.getPlanEnde(), newTaskNode.getPlanEnde()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setPlanEnde(newTaskNode.getPlanEnde());
                flgChange = true;
            }
            // check for Ist stand
            if (Calculator.compareValues(
                            origTaskNode.getIstStand(), newTaskNode.getIstStand()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setIstStand(newTaskNode.getIstStand());
                flgChange = true;
            }
            // check for Ist aufwand
            if (Calculator.compareValues(
                            origTaskNode.getIstAufwand(), newTaskNode.getIstAufwand()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setIstAufwand(newTaskNode.getIstAufwand());
                flgChange = true;
            }
            // check for Ist datestart
            if (Calculator.compareValues(
                            origTaskNode.getIstStart(), newTaskNode.getIstStart()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setIstStart(newTaskNode.getIstStart());
                flgChange = true;
            }
            // check for Ist dateend
            if (Calculator.compareValues(
                            origTaskNode.getIstEnde(), newTaskNode.getIstEnde()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setIstEnde(newTaskNode.getIstEnde());
                flgChange = true;
            }
        }
        
        // InfoNodes
        if (InfoNode.class.isInstance(origNode)) {
            InfoNode newInfoNode = (InfoNode) newNode;
            InfoNode origInfoNode = (InfoNode) origNode;

            // get state from type
            origNode.setState(origNode.getType());
            // check for DocLayoutTagCommand
            if (Calculator.compareValues(
                            origInfoNode.getDocLayoutTagCommand(), newInfoNode.getDocLayoutTagCommand()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origInfoNode.setDocLayoutTagCommand(newInfoNode.getDocLayoutTagCommand());
                flgChange = true;
            }
            // check for DocLayoutShortName
            if (Calculator.compareValues(
                            origInfoNode.getDocLayoutShortName(), newInfoNode.getDocLayoutShortName()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origInfoNode.setDocLayoutShortName(newInfoNode.getDocLayoutShortName());
                flgChange = true;
            }
            // check for DocLayoutAddStyleClass
            if (Calculator.compareValues(
                            origInfoNode.getDocLayoutAddStyleClass(), newInfoNode.getDocLayoutAddStyleClass()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origInfoNode.setDocLayoutAddStyleClass(newInfoNode.getDocLayoutAddStyleClass());
                flgChange = true;
            }
            // check for DocLayoutFlgCloseDiv
            if (Calculator.compareValues(
                            origInfoNode.getDocLayoutFlgCloseDiv(), newInfoNode.getDocLayoutFlgCloseDiv()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origInfoNode.setDocLayoutFlgCloseDiv(newInfoNode.getDocLayoutFlgCloseDiv());
                flgChange = true;
            }
        }

        // UrlResNode
        if (UrlResNode.class.isInstance(origNode)) {
            UrlResNode newUrlResNode = (UrlResNode) newNode;
            UrlResNode origUrlResNode = (UrlResNode) origNode;

            // get state from type
            origNode.setState(origNode.getType());

            // check for ResLocRef
            if (Calculator.compareValues(
                            origUrlResNode.getResLocRef(), newUrlResNode.getResLocRef())
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setResLocRef(newUrlResNode.getResLocRef());
                flgChange = true;
            }
            // check for ResLocName
            if (Calculator.compareValues(
                            origUrlResNode.getResLocName(), newUrlResNode.getResLocName()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setResLocName(newUrlResNode.getResLocName());
                flgChange = true;
            }
            // check for ResLocTags
            if (Calculator.compareValues(
                            origUrlResNode.getResLocTags(), newUrlResNode.getResLocTags()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setResLocTags(newUrlResNode.getResLocTags());
                flgChange = true;
            }
        }
        
        // SymLinkNode
        if (SymLinkNode.class.isInstance(origNode)) {
            SymLinkNode newUrlResNode = (SymLinkNode) newNode;
            SymLinkNode origUrlResNode = (SymLinkNode) origNode;

            // get state from type
            origNode.setState(origNode.getType());

            // check for ResLocRef
            if (Calculator.compareValues(
                            origUrlResNode.getSymLinkRef(), newUrlResNode.getSymLinkRef()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setSymLinkRef(newUrlResNode.getSymLinkRef());
                flgChange = true;
            }
            // check for SymLinkName
            if (Calculator.compareValues(
                            origUrlResNode.getSymLinkName(), newUrlResNode.getSymLinkName()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setSymLinkName(newUrlResNode.getSymLinkName());
                flgChange = true;
            }
            // check for SymLinkTags
            if (Calculator.compareValues(
                            origUrlResNode.getSymLinkTags(), newUrlResNode.getSymLinkTags()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setSymLinkTags(newUrlResNode.getSymLinkTags());
                flgChange = true;
            }
        }
        
        // validate
        Set<ConstraintViolation<BaseNode>> violations = newNode.validateMe();
        if (violations.size() > 0) {
            ConstraintViolationException ex = new ConstraintViolationException(
                            "error while validating newNode" + newNode.getNameForLogger(), 
                            new HashSet<ConstraintViolation<?>>(violations));
            throw ex;
        }

        return flgChange;
    }
    
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     recalc and merge the node and its parents recursively
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>List - list of the recalced and saved parenthierarchy
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice 
     * @param node - the node to recalc and merge
     * @return List - list of the recalced and saved parenthierarchy
     */
    protected List<BaseNode> updateMeAndMyParents(final BaseNode node) throws Exception {
        return BaseNodeDBServiceImpl.getInstance().updateMeAndMyParents(node);
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     update the node sysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    public NodeActionResponse updateNode(final String sysUID, final BaseNode newNode) {
        NodeActionResponse response = new NodeActionResponse(
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
            // map data
            flgChange = mapNodeData(node, newNode);

            // check for needed update
            if (flgChange) {
                // recalc 
                updateMeAndMyParents(node);
                node.setFlgForceUpdate(true);
            } else {
                // read children
                node.initChildNodesFromDB(0);
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
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error("violationerrors while updating node '" 
                                + sysUID + "':", ex);
                LOGGER.error("error updating node '" 
                                + node);
            }

            // create response
            response = new NodeActionResponse(
                            "ERROR", "violationerrors while updating node '" + sysUID + "':" + ex, 
                            null, null, null, violations);
            
            
        } catch (Throwable ex) {
            // errorhandling
            ex.printStackTrace();
            LOGGER.error("error while updating node '" 
                            + sysUID + "':", ex);
            LOGGER.error("error updating node '" 
                            + node);
            response = new NodeActionResponse(
                            "ERROR", "error while updating node '" + sysUID + "':" + ex, 
                            null, null, null, null);
        }
        
        return response;
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     create the node with parent parentSysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the new node for parentSysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param parentSysUID - sysUID of the parent to filter
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    public NodeActionResponse createNode(final String parentSysUID, final BaseNode newNode) {
        NodeActionResponse response = new NodeActionResponse(
                        "ERROR", "parentnode '" + parentSysUID + "' doesnt exists", 
                        null, null, null, null);

        // find a specific node
        BaseNode parentNode = BaseNode.findBaseNode(parentSysUID);
        if (parentNode == null) {
            // node not found
            return response;
        }

        try {
            // read children for parent
            parentNode.initChildNodesFromDB(0);
            
            // init some vars and map NodeData
            newNode.setEbene(parentNode.getEbene() + 1);
            mapNodeData(newNode, newNode);
            
            // add new Node
            newNode.setParentNode(parentNode);
            
            // recalc data
            newNode.recalcData(BaseNodeService.CONST_RECURSE_DIRECTION_ONLYME);
            
            // save
            newNode.persist();

            // recalc 
            updateMeAndMyParents(newNode);

            // create response
            response = createResponseObj(newNode, "node '" + newNode.getSysUID() 
                            + "' created for parentNode=" + parentSysUID);

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
            LOGGER.error("violationerrors while creating node for parent '" 
                            + parentSysUID + "':", ex);
            LOGGER.error("error creating node '" 
                            + newNode);
            response = new NodeActionResponse(
                            "ERROR", "violationerrors while creating node for parent '" 
                            + parentSysUID + "':" + ex, 
                            null, null, null, violations);
            
            
        } catch (Throwable ex) {
            // errorhandling
            ex.printStackTrace();
            LOGGER.error("error while creating node for parent '" 
                            + parentSysUID + "':" + ex, ex);
            LOGGER.error("error creating node '" 
                            + newNode);
            response = new NodeActionResponse(
                            "ERROR", "error while creating node for parent '" 
                            + parentSysUID + "':" + ex, 
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
     *     <li>NodeResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.PATCH, value = "/update/BaseNode/{sysUID}")
    public @ResponseBody NodeActionResponse updateBaseNode(
                @PathVariable(value = "sysUID") final String sysUID, 
                @RequestBody final BaseNode newNode) {
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
     *     <li>NodeResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.PATCH, value = "/update/TaskNode/{sysUID}")
    public @ResponseBody NodeActionResponse updateTaskNode(
                @PathVariable(value = "sysUID") final String sysUID, 
                @RequestBody final TaskNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to create the new TaskNode with parentSysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the new node for parentSysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param parentSysUID - parentSysUID to add the newNode
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create/TaskNode/{parentSysUID}")
    public @ResponseBody NodeActionResponse createTaskNode(
                @PathVariable(value = "parentSysUID") final String parentSysUID, 
                @RequestBody final TaskNode newNode) {
        // create default response
        return this.createNode(parentSysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to update the EventNode sysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.PATCH, value = "/update/EventNode/{sysUID}")
    public @ResponseBody NodeActionResponse updateEventNode(
                @PathVariable(value = "sysUID") final String sysUID, 
                @RequestBody final EventNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to create the new EventNode with parentSysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the new node for parentSysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param parentSysUID - parentSysUID to add the newNode
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create/EventNode/{parentSysUID}")
    public @ResponseBody NodeActionResponse createEventNode(
                @PathVariable(value = "parentSysUID") final String parentSysUID, 
                @RequestBody final EventNode newNode) {
        // create default response
        return this.createNode(parentSysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to update the UrlresNode sysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.PATCH, value = "/update/UrlResNode/{sysUID}")
    public @ResponseBody NodeActionResponse updateUrlResNode(
                @PathVariable(value = "sysUID") final String sysUID, 
                @RequestBody final UrlResNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to create the new UrlResNode with parentSysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the new node for parentSysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param parentSysUID - parentSysUID to add the newNode
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create/UrlResNode/{parentSysUID}")
    public @ResponseBody NodeActionResponse createUrlResNode(
                @PathVariable(value = "parentSysUID") final String parentSysUID, 
                @RequestBody final UrlResNode newNode) {
        // create default response
        return this.createNode(parentSysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to update the InfoNode sysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.PATCH, value = "/update/InfoNode/{sysUID}")
    public @ResponseBody NodeActionResponse updateInfoNode(
                @PathVariable(value = "sysUID") final String sysUID, 
                @RequestBody final InfoNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to create the new InfoNode with parentSysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the new node for parentSysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param parentSysUID - parentSysUID to add the newNode
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create/InfoNode/{parentSysUID}")
    public @ResponseBody NodeActionResponse createInfoNode(
                @PathVariable(value = "parentSysUID") final String parentSysUID, 
                @RequestBody final InfoNode newNode) {
        // create default response
        return this.createNode(parentSysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to update the SymLinkNode sysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.PATCH, value = "/update/SymLinkNode/{sysUID}")
    public @ResponseBody NodeActionResponse updateSymLinkNode(
                @PathVariable(value = "sysUID") final String sysUID, 
                @RequestBody final SymLinkNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to create the new SymLinkNode with parentSysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the new node for parentSysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param parentSysUID - parentSysUID to add the newNode
     * @param newNode - the node created from request-data
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create/SymLinkNode/{parentSysUID}")
    public @ResponseBody NodeActionResponse createSymLinkNode(
                @PathVariable(value = "parentSysUID") final String parentSysUID, 
                @RequestBody final SymLinkNode newNode) {
        // create default response
        return this.createNode(parentSysUID, newNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to move the node sysUID to newParentSysUID and return it with children as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>NodeResponse (OK, ERROR) with the node for sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to filter
     * @param newParentSysUID - sysUID of the new parent
     * @param newSortPos - the new position in the list
     * @return NodeResponse (OK, ERROR) with the node for sysUID
     */
    @Transactional
    @RequestMapping(method = RequestMethod.PATCH, value = "/move/{sysUID}/{newParentSysUID}/{newSortPos}")
    public @ResponseBody NodeActionResponse moveNode(
                @PathVariable(value = "sysUID") final String sysUID,
                @PathVariable(value = "newParentSysUID") final String newParentSysUID,
                @PathVariable(value = "newSortPos") final Integer newSortPos) {
        // create default response
        NodeActionResponse response = new NodeActionResponse(
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
            response = new NodeActionResponse(
                            "ERROR", "new parentNode '" + newParentSysUID 
                            + "' for node '" + sysUID + "' doesnt exists", 
                            null, null, null, null);
            return response;
        }
        // map the data
        boolean flgChangedParent = false;
        boolean flgChangedPosition = false;

        try {
            // read children for old parent
            BaseNode oldParent = node.getParentNode();
            oldParent.initChildNodesFromDB(0);

            // read children for both parents
            newParent.initChildNodesFromDB(0);

            // check for new parent
            if (newParent.getSysUID() != oldParent.getSysUID()) {
                // reset sortpos
                node.setSortPos(null);
                flgChangedPosition = true;

                // set new parent
                flgChangedParent = true;
                node.setParentNode(newParent);
            } else {
                // check if position changed
                if (node.getSortPos().intValue() != newSortPos.intValue()) {
                    flgChangedPosition = true;
                }
            }

            // check for needed update
            if (flgChangedParent || flgChangedPosition) {
                // recalc the position
                newParent.moveChildToSortPos(node, newSortPos);
                
                // save children of newParent
                newParent.saveChildNodesToDB(0, true);

                // recalc and save
                updateMeAndMyParents(node);
                
                // renew old parent only if changed
                if (flgChangedParent) {
                    // renew oldParent
                    oldParent = BaseNode.findBaseNode(oldParent.getSysUID());
                    
                    // recalc old parent
                    updateMeAndMyParents(oldParent);
                }
            } else {
                // read children for node
                node.initChildNodesFromDB(0);
            }

            // create response
            response = createResponseObj(node, "node '" + sysUID 
                            + "' moved to " + newParentSysUID);

        } catch (Throwable ex) {
            // errorhandling
            ex.printStackTrace();
            LOGGER.error("error while moving node  '" 
                         + sysUID + "' to '" + newParentSysUID + "'-" + newSortPos 
                         + ":", ex);
            LOGGER.error("error moving node '" + node);
            response = new NodeActionResponse(
                            "ERROR", "error while moving node '" + sysUID + "':" + ex, 
                            null, null, null, null);
        }
        
        return response;
    }
}
