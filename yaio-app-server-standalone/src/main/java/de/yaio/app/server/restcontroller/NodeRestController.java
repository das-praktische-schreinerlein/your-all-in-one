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
package de.yaio.app.server.restcontroller;

import de.yaio.app.core.dbservice.BaseNodeDBService;
import de.yaio.app.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.app.core.dbservice.SearchOptions;
import de.yaio.app.core.dbservice.SearchOptionsImpl;
import de.yaio.app.core.node.*;
import de.yaio.app.core.nodeservice.BaseNodeService;
import de.yaio.app.core.nodeservice.UrlResNodeService;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.common.DatatransferUtils;
import de.yaio.app.extension.datatransfer.common.ExtendedDatatransferUtils;
import de.yaio.app.extension.dms.services.ResContentDataService;
import de.yaio.app.server.controller.CommonApiConfig;
import de.yaio.commons.io.IOExceptionWithCause;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.*;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

/** 
 * the controller for RESTful Web Services for BaseNodes<br>
 *  
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

    //@Autowired TODO: failed on unitstest...
    protected DatatransferUtils datatransferUtils = new ExtendedDatatransferUtils();

    // Logger
    private static final Logger LOGGER = Logger.getLogger(NodeRestController.class);

    /** 
     * create an response.obj for the node with state OK and the corresponding message<br>
     * it will automaticaly set the parentHierarchy
     * @param node                   the node for the response
     * @param okMsg                  the message
     * @param addChildren            add the children of the node to the response
     * @return                       NodeResponse (OK) with the node, hierarchy and OK-message
     */
    public static NodeActionResponse createResponseObj(final BaseNode node, final String okMsg, boolean addChildren) {
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

        if (addChildren) {
            // add children
            response.setChildNodes(new ArrayList<>(node.getChildNodes()));
        }
        
        return response;
    }
    
    
    /** 
     * Request to search childnodes of sysUID and return them as JSON
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
            response = createResponseObj(node, "node '" + sysUID + "' found", true);
        }
        
        return response;
    }

    /** 
     * Request to read the SymLinkRef-node for sysUID and return it with children as JSON
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
            response = createResponseObj(node, "node '" + symLinkRef + "' found", true);
        }
        
        return response;
    }
    
    
    /** 
     * Request to delete the node for sysUID and return its parent with children as JSON
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
            } catch (RuntimeException e) {
                LOGGER.info("violationerrors while deleting node '" + sysUID + "':", e);
                LOGGER.info("error deleting node '" + node);
                e.printStackTrace();
            }
            
            // create response
            response = createResponseObj(parent, "node '" + sysUID + "' deleted", true);
        }
        
        return response;
    }

    /** 
     * Request to update the BaseNode sysUID and return it with children as JSON
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
        Map<String, MultipartFile> addFileParams = new HashMap<>();
        Map<String, Object> addParams = new HashMap<>();
        addFileParams.put("uploadFile", uploadFile);
        LOGGER.info("updateUrlResNode with file:" + addFileParams.get("uploadFile") 
                        + " for node:" + newNode.getNameForLogger());
        return this.updateNode(sysUID, newNode, addParams, addFileParams);
    }

    /** 
     * Request to create the new UrlResNode with parentSysUID and return it with children as JSON
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
        Map<String, MultipartFile> addFileParams = new HashMap<>();
        addFileParams.put("uploadFile", uploadFile);
        Map<String, Object> addParams = new HashMap<>();
        LOGGER.info("createUrlResNode with file:" + addFileParams.get("uploadFile") 
                        + " for node:" + newNode.getNameForLogger());
        return this.createNode(parentSysUID, new UrlResNode(), newNode, addParams, addFileParams);
    }

    /** 
     * Request to update the InfoNode sysUID and return it with children as JSON
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
                                       @PathVariable(value = "newSortPos") final Integer newSortPos)
            throws ConverterException {
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

        // move node
        node = datatransferUtils.moveNode(node, newParent, newSortPos);

        // create response
        response = createResponseObj(node, "node '" + sysUID + "' moved to " + newParentSysUID, false);

        return response;
    }

    /** 
     * Request to copy the node sysUID to newParentSysUID and return it with children as JSON
     * @param sysUID                 sysUID to filter
     * @param newParentSysUID        sysUID of the new parent
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, 
                    value = "/copy/{sysUID}/{newParentSysUID}")
    public NodeActionResponse copyNode(@PathVariable(value = "sysUID") final String sysUID,
                                       @PathVariable(value = "newParentSysUID") final String newParentSysUID)
            throws ConverterException {
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

        // copy node
        datatransferUtils.copyNode(node, newParent);

        // create response
        response = createResponseObj(node, "node '" + sysUID + "' copied to " + newParentSysUID, false);


        return response;
    }

    @ExceptionHandler(ConverterException.class)
    public String handleCustomException(final HttpServletRequest request, final ConverterException e,
                                        final HttpServletResponse response) {
        LOGGER.info("ConverterException while running request:" + request.toString(), e);
        response.setStatus(SC_BAD_REQUEST);
        return "cant do action on node => " + e.getMessage();
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public String handleAllException(final HttpServletRequest request, final Exception e,
                                     final HttpServletResponse response) {
        LOGGER.warn("error while running request:" + request.toString(), e);
        response.setStatus(SC_INTERNAL_SERVER_ERROR);
        return "cant do action on node";
    }


    /** 
     * update the node sysUID and return it with children as JSON
     * @param sysUID                 sysUID to filter
     * @param newNode                the node created from request-data
     * @return                       NodeResponse (OK, ERROR) with the node for sysUID
     */
    protected NodeActionResponse updateNode(final String sysUID, final BaseNode newNode) {
        return this.updateNode(sysUID, newNode, null, null);
    }
    
    /** 
     * update the node sysUID and return it with children as JSON
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
            try {
                flgChange = datatransferUtils.mapNodeData(node, newNode);
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException("cant map nodedata", ex);
            }

            // handle uploads for UrlResNode
            if (commonApiConfig != null && commonApiConfig.dmsAvailable
                    && UrlResNode.class.isInstance(node)
                    && MapUtils.isNotEmpty(addFileParams)
                    && addFileParams.containsKey("uploadFile") 
                    && addFileParams.get("uploadFile") != null
                    && !addFileParams.get("uploadFile").isEmpty()) {
                LOGGER.info("got handleUploadFile for " + addFileParams.get("uploadFile") 
                                + " for node:" + node.getNameForLogger());
                try {
                    flgChange = this.handleUploadFile((UrlResNode) node, addFileParams.get("uploadFile")) || flgChange;
                } catch (IOExceptionWithCause ex) {
                    LOGGER.info("IOExceptionWithCause on handleUploadFile while updating node", ex);
                    LOGGER.info("error updating node '" + node);
                    return new NodeActionResponse(
                            "ERROR", "error on handleUploadFile while updating node",
                            null, null, null, Collections.singletonList(new NodeViolation("uploadFile",
                            "uploadFile cant be handled",
                            ex.getMessage())));
                } catch (IOException ex) {
                    LOGGER.warn("IOException on handleUploadFile while updating node", ex);
                    LOGGER.warn("error updating node: '" + node);
                    return new NodeActionResponse(
                            "ERROR", "error on handleUploadFile while updating node",
                            null, null, null, Collections.singletonList(new NodeViolation("uploadFile",
                            "uploadFile cant be handled",
                            "internal error")));
                }
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
            response = createResponseObj(node, "node '" + sysUID + "' updated", false);

        } catch (ConstraintViolationException ex) {
            // validation errors
            Set<ConstraintViolation<?>> cViolations = ex.getConstraintViolations();
            
            // convert to Violation
            List<NodeViolation>violations = new ArrayList<>();
            for (ConstraintViolation<?> cViolation : cViolations) {
                violations.add(
                      new NodeViolation(cViolation.getPropertyPath().toString(), 
                                      cViolation.getMessage(),
                                      cViolation.getMessageTemplate()));
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("violationerrors while updating node", ex);
                LOGGER.info("error updating node '" + node);
            }

            // create response
            response = new NodeActionResponse(
                            "ERROR", "violationerrors while updating node",
                            null, null, null, violations);
        }
        
        return response;
    }
    
    
    /** 
     * create the node with parent parentSysUID and return it with children as JSON
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
            try {
                datatransferUtils.mapNodeData(origNode, newNode);
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException("cant map nodedata", ex);
            }

            // add new Node
            origNode.setParentNode(parentNode);
            
            // recalc data
            origNode.recalcData(BaseNodeService.RecalcRecurseDirection.ONLYME);
            
            // save
            origNode.persist();

            // handle uploads for UrlResNode
            if (commonApiConfig != null && commonApiConfig.dmsAvailable
                    && UrlResNode.class.isInstance(origNode)
                    && MapUtils.isNotEmpty(addFileParams)
                    && addFileParams.containsKey("uploadFile") 
                    && addFileParams.get("uploadFile") != null
                    && !addFileParams.get("uploadFile").isEmpty()) {
                LOGGER.info("got handleUploadFile for " + addFileParams.get("uploadFile")
                                + " for node:" + origNode.getNameForLogger());
                try {
                    this.handleUploadFile((UrlResNode) origNode, addFileParams.get("uploadFile"));
                } catch (IOExceptionWithCause ex) {
                    LOGGER.info("IOExceptionWithCause on handleUploadFile while creating node", ex);
                    LOGGER.info("IOExceptionWithCause creating node for parent " + parentSysUID + ":" + origNode);
                    return new NodeActionResponse(
                            "ERROR", "error on handleUploadFile while creating node",
                            null, null, null, Collections.singletonList(new NodeViolation("uploadFile",
                            "uploadFile cant be handled",
                            ex.getMessage())));
                } catch (IOException ex) {
                    LOGGER.warn("IOException on handleUploadFile while creating node", ex);
                    LOGGER.warn("IOException creating node for parent " + parentSysUID + ":" + origNode);
                    return new NodeActionResponse(
                            "ERROR", "error on handleUploadFile while creating node",
                            null, null, null, Collections.singletonList(new NodeViolation("uploadFile",
                            "uploadFile cant be handled",
                            "internal error")));
                }
            }

            // recalc 
            updateMeAndMyParents(origNode);

            // create response
            response = createResponseObj(origNode, "node '" + origNode.getSysUID() +
                    "' created for parentNode=" + parentSysUID, false);

        } catch (ConstraintViolationException ex) {
            // validation errors
            Set<ConstraintViolation<?>> cViolations = ex.getConstraintViolations();

            // convert to Violation
            List<NodeViolation> violations = new ArrayList<>();
            for (ConstraintViolation<?> cViolation : cViolations) {
                violations.add(
                        new NodeViolation(cViolation.getPropertyPath().toString(),
                                cViolation.getMessage(),
                                cViolation.getMessageTemplate()));
            }

            // create response
            LOGGER.info("violationerrors while creating node for parent '" + parentSysUID + "':", ex);
            LOGGER.info("error creating node '" + origNode);
            response = new NodeActionResponse("ERROR", "violationerrors while creating node",
                    null, null, null, violations);
        }
            
            
        return response;
    }

    protected boolean handleUploadFile(final UrlResNode node, final MultipartFile uploadFile)
            throws IOExceptionWithCause, IOException {
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
     * @param node                   the node to recalc and merge
     * @return                       List - list of the recalced and saved parenthierarchy
     */
    protected List<BaseNode> updateMeAndMyParents(final BaseNode node) {
        return BaseNodeDBServiceImpl.getInstance().updateMeAndMyParents(node);
    }

    /** 
     * common function to search childnodes of sysUID and return them as JSON
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

        // search nodes
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        List<BaseNode> resultList = baseNodeDBService.findExtendedSearchBaseNodeEntries(fulltext, sysUID, searchOptions,
                        sortConfig, (curPage.intValue() - 1) * pageSize.intValue(),
                        pageSize.intValue());

        // create response
        response.setCount(baseNodeDBService.countExtendedSearchBaseNodes(fulltext, sysUID, searchOptions));

        // add node
        response.setNodes(resultList);

        // set state
        response.setStateMsg("node found");

        return response;
    }
}
