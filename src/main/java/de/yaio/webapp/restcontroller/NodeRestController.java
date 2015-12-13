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
package de.yaio.webapp.restcontroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import de.yaio.core.dbservice.BaseNodeDBService;
import de.yaio.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.core.dbservice.SearchOptions;
import de.yaio.core.dbservice.SearchOptionsImpl;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.EventNode;
import de.yaio.core.node.InfoNode;
import de.yaio.core.node.SymLinkNode;
import de.yaio.core.node.TaskNode;
import de.yaio.core.node.UrlResNode;
import de.yaio.core.nodeservice.BaseNodeService;
import de.yaio.core.nodeservice.UrlResNodeService;
import de.yaio.datatransfer.common.DatatransferUtils;
import de.yaio.extension.datatransfer.common.ExtendedDatatransferUtils;
import de.yaio.extension.dms.services.ResContentDataService;
import de.yaio.webapp.controller.CommonApiConfig;

/** 
 * the controller for RESTful Web Services for BaseNodes<br>
 *  
 * @FeatureDomain                Webservice
 * @package                      de.yaio.webapp.restcontroller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Controller
@RequestMapping("/nodes")
public class NodeRestController {
    
    /** API-Version **/
    public static final String API_VERSION = "1.0.0";

    @Autowired
    private ResContentDataService resContentService;

    @Autowired
    private CommonApiConfig commonApiConfig;

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(NodeRestController.class);
    

    //@Autowired TODO: failed on unitstest...
    protected DatatransferUtils datatransferUtils = new ExtendedDatatransferUtils();
    
    /** 
     * create an response.obj for the node with state OK and the corresponding message<br>
     * it will automaticaly set the parentHierarchy
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK) with the node, hierarchy and OK-message
     * @FeatureKeywords              Webservice 
     * @param node                   the node for the response
     * @param okMsg                  the message
     * @return                       NodeResponse (OK) with the node, hierarchy and OK-message
     */
    public static NodeActionResponse createResponseObj(final BaseNode node, final String okMsg) {
        // extract parents
        List<String> parentIdHierarchy = node.getBaseNodeService().getParentIdHierarchy(node);
        
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
     * Request to search childnodes of sysUID and return them as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with matching nodes
     * @FeatureKeywords              Webservice Query
     * @param curPage                current page in result to display
     * @param pageSize               max items per page
     * @param sortConfig             use sort
     * @param sysUID                 sysUID to filter as perentNode
     * @param fulltext               fulltext search string
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with matching nodes
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/search/{curPage}/{pageSize}/{sortConfig}/{sysUID}/{fulltext}/")
    public NodeSearchResponse searchNodeFulltext(@PathVariable(value = "curPage") final Long curPage,
                                                 @PathVariable(value = "pageSize") final Long pageSize,
                                                 @PathVariable(value = "sortConfig") final String sortConfig,
                                                 @PathVariable(value = "sysUID") final String sysUID,
                                                 @PathVariable(value = "fulltext") final String fulltext) {
        return commonSearchNode(curPage, pageSize, sortConfig, sysUID, fulltext, null);
    }
    
    /** 
     * Request to search childnodes of sysUID and return them as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with matching nodes
     * @FeatureKeywords              Webservice Query
     * @param curPage                current page in result to display
     * @param pageSize               max items per page
     * @param sortConfig             use sort
     * @param sysUID                 sysUID to filter as perentNode
     * @param fulltext               fulltext search string
     * @param searchOptions          searchOptions
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with matching nodes
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/search/{curPage}/{pageSize}/{sortConfig}/{sysUID}/{fulltext}/")
    public NodeSearchResponse searchNodeExtended(@PathVariable(value = "curPage") final Long curPage,
                                                 @PathVariable(value = "pageSize") final Long pageSize,
                                                 @PathVariable(value = "sortConfig") final String sortConfig,
                                                 @PathVariable(value = "sysUID") final String sysUID,
                                                 @PathVariable(value = "fulltext") final String fulltext,
                                                 @RequestBody final SearchOptionsImpl searchOptions) {
        return commonSearchNode(curPage, pageSize, sortConfig, sysUID, fulltext, searchOptions);
    }

    /** 
     * Request to search childnodes of sysUID and return them as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with matching nodes
     * @FeatureKeywords              Webservice Query
     * @param curPage                current page in result to display
     * @param pageSize               max items per page
     * @param sortConfig             use sort
     * @param sysUID                 sysUID to filter as perentNode
     * @param searchOptions          searchOptions
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with matching nodes
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/search/{curPage}/{pageSize}/{sortConfig}/{sysUID}/")
    public NodeSearchResponse searchNode(@PathVariable(value = "curPage") final Long curPage,
                                         @PathVariable(value = "pageSize") final Long pageSize,
                                         @PathVariable(value = "sortConfig") final String sortConfig,
                                         @PathVariable(value = "sysUID") final String sysUID,
                                         @RequestBody final SearchOptionsImpl searchOptions) {
        return commonSearchNode(curPage, pageSize, sortConfig, sysUID, "", searchOptions);
    }

    /** 
     * Request to read the node for sysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the node for sysUID
     * @FeatureKeywords              Webservice Query
     * @param sysUID                 sysUID to filter
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/show/{sysUID}")
    public NodeActionResponse getNodeWithChildren(@PathVariable(value = "sysUID") final String sysUID) {
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
     * Request to read the SymLinkRef-node for sysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the node for sysUID
     * @FeatureKeywords              Webservice Query
     * @param symLinkRef             symLinkRef to filter
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/showsymlink/{symLinkRef}")
    public NodeActionResponse getSymLinkRefNodeWithChildren(
                              @PathVariable(value = "symLinkRef") final String symLinkRef) {
        // create default response
        NodeActionResponse response = new NodeActionResponse(
                        "ERROR", "node '" + symLinkRef + "' doesnt exists", 
                        null, null, null, null);

        // find a specific node
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        List<BaseNode> nodes = baseNodeDBService.findSymLinkBaseNode(symLinkRef);
        if (CollectionUtils.isNotEmpty(nodes)) {
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
     * Request to delete the node for sysUID and return its parent with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the parentNode for sysUID
     * @FeatureKeywords              Webservice Query
     * @param sysUID                 sysUID to delete
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, 
                    value = "/delete/{sysUID}")
    public NodeActionResponse deleteNodeWithChildren(@PathVariable(value = "sysUID") final String sysUID) {
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
     * Request to update the BaseNode sysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the node for sysUID
     * @FeatureKeywords              Webservice Query
     * @param sysUID                 sysUID to filter
     * @param newNode                the node created from request-data
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, 
                    value = "/update/BaseNode/{sysUID}")
    public NodeActionResponse updateBaseNode(@PathVariable(value = "sysUID") final String sysUID, 
                                             @RequestBody final BaseNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /** 
     * Request to update the TaskNode sysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the node for sysUID
     * @FeatureKeywords              Webservice Query
     * @param sysUID                 sysUID to filter
     * @param newNode                the node created from request-data
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, 
                    value = "/update/TaskNode/{sysUID}")
    public NodeActionResponse updateTaskNode(@PathVariable(value = "sysUID") final String sysUID, 
                                             @RequestBody final TaskNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /** 
     * Request to create the new TaskNode with parentSysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the new node for parentSysUID
     * @FeatureKeywords              Webservice Query
     * @param parentSysUID           parentSysUID to add the newNode
     * @param newNode                the node created from request-data
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/create/TaskNode/{parentSysUID}")
    public NodeActionResponse createTaskNode(@PathVariable(value = "parentSysUID") final String parentSysUID, 
                                             @RequestBody final TaskNode newNode) {
        // create default response
        return this.createNode(parentSysUID, new TaskNode(), newNode);
    }

    /** 
     * Request to update the EventNode sysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the node for sysUID
     * @FeatureKeywords              Webservice Query
     * @param sysUID                 sysUID to filter
     * @param newNode                the node created from request-data
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, 
                    value = "/update/EventNode/{sysUID}")
    public NodeActionResponse updateEventNode(@PathVariable(value = "sysUID") final String sysUID, 
                                              @RequestBody final EventNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /** 
     * Request to create the new EventNode with parentSysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the new node for parentSysUID
     * @FeatureKeywords              Webservice Query
     * @param parentSysUID           parentSysUID to add the newNode
     * @param newNode                the node created from request-data
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/create/EventNode/{parentSysUID}")
    public NodeActionResponse createEventNode(@PathVariable(value = "parentSysUID") final String parentSysUID, 
                                              @RequestBody final EventNode newNode) {
        // create default response
        return this.createNode(parentSysUID, new EventNode(), newNode);
    }

    /** 
     * Request to update the UrlresNode sysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the node for sysUID
     * @FeatureKeywords              Webservice Query
     * @param sysUID                 sysUID to filter
     * @param newNode                the node created from request-data
     * @param uploadFile             if it is a FileRes an optional uploaded file
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/update/UrlResNode/{sysUID}",
                    consumes = {"multipart/form-data"})
    public NodeActionResponse updateUrlResNode(@PathVariable(value = "sysUID") final String sysUID, 
                                               @RequestPart("node") final UrlResNode newNode,
                                               @RequestPart(value = "uploadFile", required = false) final MultipartFile uploadFile) {
        // create default response
        Map<String, MultipartFile> addFileParams = new HashMap<String, MultipartFile>();
        Map<String, Object> addParams = new HashMap<String, Object>();
        addFileParams.put("uploadFile", uploadFile);
        LOGGER.info("updateUrlResNode with file:" + addFileParams.get("uploadFile") 
                        + " for node:" + newNode.getNameForLogger());
        return this.updateNode(sysUID, newNode, addParams, addFileParams);
    }

    /** 
     * Request to create the new UrlResNode with parentSysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the new node for parentSysUID
     * @FeatureKeywords              Webservice Query
     * @param parentSysUID           parentSysUID to add the newNode
     * @param newNode                the node created from request-data
     * @param uploadFile             if it is a FileRes an optional uploaded file
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/create/UrlResNode/{parentSysUID}",
                    consumes = {"multipart/form-data"})
    public NodeActionResponse createUrlResNode(@PathVariable(value = "parentSysUID") final String parentSysUID, 
                                               @RequestPart("node") final UrlResNode newNode,
                                               @RequestPart(value = "uploadFile", required = false) final MultipartFile uploadFile) {
        // create default response
        Map<String, MultipartFile> addFileParams = new HashMap<String, MultipartFile>();
        addFileParams.put("uploadFile", uploadFile);
        Map<String, Object> addParams = new HashMap<String, Object>();
        LOGGER.info("createUrlResNode with file:" + addFileParams.get("uploadFile") 
                        + " for node:" + newNode.getNameForLogger());
        return this.createNode(parentSysUID, new UrlResNode(), newNode, addParams, addFileParams);
    }

    /** 
     * Request to update the InfoNode sysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the node for sysUID
     * @FeatureKeywords              Webservice Query
     * @param sysUID                 sysUID to filter
     * @param newNode                the node created from request-data
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, 
                    value = "/update/InfoNode/{sysUID}")
    public NodeActionResponse updateInfoNode(@PathVariable(value = "sysUID") final String sysUID, 
                                             @RequestBody final InfoNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /** 
     * Request to create the new InfoNode with parentSysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the new node for parentSysUID
     * @FeatureKeywords              Webservice Query
     * @param parentSysUID           parentSysUID to add the newNode
     * @param newNode                the node created from request-data
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/create/InfoNode/{parentSysUID}")
    public NodeActionResponse createInfoNode(@PathVariable(value = "parentSysUID") final String parentSysUID, 
                                             @RequestBody final InfoNode newNode) {
        // create default response
        return this.createNode(parentSysUID, new InfoNode(), newNode);
    }

    /** 
     * Request to update the SymLinkNode sysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the node for sysUID
     * @FeatureKeywords              Webservice Query
     * @param sysUID                 sysUID to filter
     * @param newNode                the node created from request-data
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, 
                    value = "/update/SymLinkNode/{sysUID}")
    public NodeActionResponse updateSymLinkNode(@PathVariable(value = "sysUID") final String sysUID, 
                                                @RequestBody final SymLinkNode newNode) {
        // create default response
        return this.updateNode(sysUID, newNode);
    }

    /** 
     * Request to create the new SymLinkNode with parentSysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the new node for parentSysUID
     * @FeatureKeywords              Webservice Query
     * @param parentSysUID           parentSysUID to add the newNode
     * @param newNode                the node created from request-data
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/create/SymLinkNode/{parentSysUID}")
    public NodeActionResponse createSymLinkNode(@PathVariable(value = "parentSysUID") final String parentSysUID, 
                                                @RequestBody final SymLinkNode newNode) {
        // create default response
        return this.createNode(parentSysUID, new SymLinkNode(), newNode);
    }

    /** 
     * Request to move the node sysUID to newParentSysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the node for sysUID
     * @FeatureKeywords              Webservice Query
     * @param sysUID                 sysUID to filter
     * @param newParentSysUID        sysUID of the new parent
     * @param newSortPos             the new position in the list
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, 
                    value = "/move/{sysUID}/{newParentSysUID}/{newSortPos}")
    public NodeActionResponse moveNode(@PathVariable(value = "sysUID") final String sysUID,
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

        try {
            // move node
            datatransferUtils.moveNode(node, newParent, newSortPos);

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

    /** 
     * Request to copy the node sysUID to newParentSysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the node for sysUID
     * @FeatureKeywords              Webservice Query
     * @param sysUID                 sysUID to filter
     * @param newParentSysUID        sysUID of the new parent
     * @param newSortPos             the new position in the list
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, 
                    value = "/copy/{sysUID}/{newParentSysUID}")
    public NodeActionResponse copyNode(@PathVariable(value = "sysUID") final String sysUID,
                                       @PathVariable(value = "newParentSysUID") final String newParentSysUID) {
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
        try {
            // copy node
            datatransferUtils.copyNode(node, newParent);

            // create response
            response = createResponseObj(node, "node '" + sysUID 
                            + "' copied to " + newParentSysUID);

        } catch (Throwable ex) {
            // errorhandling
            ex.printStackTrace();
            LOGGER.error("error while copying node  '" 
                         + sysUID + "' to '" + newParentSysUID + "'" 
                         + ":", ex);
            LOGGER.error("error copying node '" + node);
            response = new NodeActionResponse(
                            "ERROR", "error while copying node '" + sysUID + "':" + ex, 
                            null, null, null, null);
        }
        
        return response;
    }

    /** 
     * update the node sysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the node for sysUID
     * @FeatureKeywords              Webservice Query
     * @param sysUID                 sysUID to filter
     * @param newNode                the node created from request-data
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    protected NodeActionResponse updateNode(final String sysUID, final BaseNode newNode) {
        return this.updateNode(sysUID, newNode, null, null);
    }
    
    /** 
     * update the node sysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the node for sysUID
     * @FeatureKeywords              Webservice Query
     * @param sysUID                 sysUID to filter
     * @param newNode                the node created from request-data
     * @param addParams              map with additional params not set on the newNode
     * @param addFileParams          map with additional uploadFiles not set on the newNode
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    protected NodeActionResponse updateNode(final String sysUID, final BaseNode newNode,
                                            final Map<String, Object> addParams, 
                                            final Map<String, MultipartFile> addFileParams) {
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
            flgChange = datatransferUtils.mapNodeData(node, newNode);

            // handle uploads for UrlResNode
            if (commonApiConfig != null && commonApiConfig.dmsAvailable == true
                    && UrlResNode.class.isInstance(node)
                    && MapUtils.isNotEmpty(addFileParams)
                    && addFileParams.containsKey("uploadFile") 
                    && addFileParams.get("uploadFile") != null
                    && !addFileParams.get("uploadFile").isEmpty()) {
                LOGGER.info("got handleUploadFile for " + addFileParams.get("uploadFile") 
                                + " for node:" + node.getNameForLogger());
                flgChange = this.handleUploadFile((UrlResNode) node, addFileParams.get("uploadFile")) || flgChange;
            }

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
     * create the node with parent parentSysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the new node for parentSysUID
     * @FeatureKeywords              Webservice Query
     * @param parentSysUID           sysUID of the parent to filter
     * @param origNode               the empty node to fill with the newNode-data
     * @param newNode                the node created from request-data
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    protected NodeActionResponse createNode(final String parentSysUID, final BaseNode origNode, final BaseNode newNode) {
        return this.createNode(parentSysUID, origNode, newNode, null, null);
    }

    /** 
     * create the node with parent parentSysUID and return it with children as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with the new node for parentSysUID
     * @FeatureKeywords              Webservice Query
     * @param parentSysUID           sysUID of the parent to filter
     * @param origNode               the empty node to fill with the newNode-data
     * @param newNode                the node created from request-data
     * @param addParams              map with additional params not set on the newNode
     * @param addFileParams          map with additional uploadFiles not set on the newNode
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    protected NodeActionResponse createNode(final String parentSysUID, final BaseNode origNode,
                                            final BaseNode newNode, 
                                            final Map<String, Object> addParams, 
                                            final Map<String, MultipartFile> addFileParams) {
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
            origNode.setEbene(parentNode.getEbene() + 1);
            datatransferUtils.mapNodeData(origNode, newNode);
            
            // add new Node
            origNode.setParentNode(parentNode);
            
            // recalc data
            origNode.recalcData(BaseNodeService.CONST_RECURSE_DIRECTION_ONLYME);
            
            // save
            origNode.persist();

            // handle uploads for UrlResNode
            if (commonApiConfig != null && commonApiConfig.dmsAvailable == true
                    && UrlResNode.class.isInstance(origNode)
                    && MapUtils.isNotEmpty(addFileParams)
                    && addFileParams.containsKey("uploadFile") 
                    && addFileParams.get("uploadFile") != null
                    && !addFileParams.get("uploadFile").isEmpty()) {
                LOGGER.info("got handleUploadFile for " + addFileParams.get("uploadFile")
                                + " for node:" + origNode.getNameForLogger());
                this.handleUploadFile((UrlResNode) origNode, addFileParams.get("uploadFile"));
            }

            // recalc 
            updateMeAndMyParents(origNode);

            // create response
            response = createResponseObj(origNode, "node '" + origNode.getSysUID() 
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
                            + origNode);
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
                            + origNode);
            response = new NodeActionResponse(
                            "ERROR", "error while creating node for parent '" 
                            + parentSysUID + "':" + ex, 
                            null, null, null, null);
        }
        
        return response;
    }

    protected boolean handleUploadFile(final UrlResNode node, final MultipartFile uploadFile) throws Exception {
        String type = node.getType();
        if (uploadFile != null && !uploadFile.isEmpty()) {
            // check for uploadfile
            if (!StringUtils.isEmpty(type)
                            && UrlResNodeService.CONST_NODETYPE_IDENTIFIER_FILERES.equals(type)) {
                resContentService.uploadResContentToDMS(node, uploadFile.getOriginalFilename(), 
                                uploadFile.getInputStream());
                return true;
            }
        }
        return false;
    }

    /** 
     * recalc and merge the node and its parents recursively
     * @FeatureDomain                Webservice
     * @FeatureResult                List - list of the recalced and saved parenthierarchy
     * @FeatureKeywords              Webservice 
     * @param node                   the node to recalc and merge
     * @return                       List - list of the recalced and saved parenthierarchy
     */
    protected List<BaseNode> updateMeAndMyParents(final BaseNode node) throws Exception {
        return BaseNodeDBServiceImpl.getInstance().updateMeAndMyParents(node);
    }

    /** 
     * common function to search childnodes of sysUID and return them as JSON
     * @FeatureDomain                Webservice
     * @FeatureResult                NodeResponse (OK, ERROR) with matching nodes
     * @FeatureKeywords              Webservice Query
     * @param curPage                current page in result to display
     * @param pageSize               max items per page
     * @param sortConfig             use sort
     * @param sysUID                 sysUID to filter as perentNode
     * @param fulltext               fulltext search string
     * @param searchOptions          searchoptions (additional filter...)
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with matching nodes
     */
    protected NodeSearchResponse commonSearchNode(final Long curPage, final Long pageSize, 
                                               final String sortConfig, final String sysUID, final String fulltext, 
                                               final SearchOptions searchOptions) {
        // create default response
        NodeSearchResponse response = new NodeSearchResponse(
                        "OK", "no node found", 
                        null, curPage, pageSize, 0L);

        // find the baseNode
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        BaseNode node = BaseNode.findBaseNode(sysUID);
        if (node != null) {
            // search nodes
            List<BaseNode> resultList = baseNodeDBService.findExtendedSearchBaseNodeEntries(fulltext, searchOptions,
                            sortConfig, (curPage.intValue() - 1) * pageSize.intValue(), 
                            pageSize.intValue());

            // create response
            response.setCount(baseNodeDBService.countExtendedSearchBaseNodes(fulltext, searchOptions));
            
            // add node
            response.setNodes(resultList);
            
            // set state
            response.setStateMsg("node found");
        }
        
        return response;
    }
}
