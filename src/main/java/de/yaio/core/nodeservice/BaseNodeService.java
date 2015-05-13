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
package de.yaio.core.nodeservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.SingletonMap;
import org.apache.log4j.Logger;

import de.yaio.core.datadomain.BaseWorkflowData;
import de.yaio.core.datadomain.BaseWorkflowData.WorkflowState;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomainservice.BaseWorkflowDataServiceImpl;
import de.yaio.core.datadomainservice.MetaDataServiceImpl;
import de.yaio.core.datadomainservice.StatDataServiceImpl;
import de.yaio.core.datadomainservice.SysDataServiceImpl;
import de.yaio.core.node.BaseNode;


/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for entity: BaseNode
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class BaseNodeService extends NodeServiceImpl {
    
    /**
     * step for next position in list
     */
    public static final int CONST_CURSORTIDX_STEP = 5;
    
    private static final Logger LOGGER = Logger.getLogger(NodeServiceImpl.class);

    public static final String CONST_NODETYPE_IDENTIFIER_UNKNOWN = "UNKNOWN";

    private static final Map<String, String> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, String>();
    private static final Map<String, WorkflowState> CONST_MAP_STATE_WORKFLOWSTATE = 
                    new SingletonMap(CONST_NODETYPE_IDENTIFIER_UNKNOWN, WorkflowState.NOWORKFLOW);
    private static final Map<WorkflowState, String> CONST_MAP_WORKFLOWSTATE_STATE = 
                    new SingletonMap(WorkflowState.NOWORKFLOW, CONST_NODETYPE_IDENTIFIER_UNKNOWN);
    static {
        // Defaults
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_UNKNOWN, CONST_NODETYPE_IDENTIFIER_UNKNOWN);
        // Abarten
        CONST_MAP_NODETYPE_IDENTIFIER.put("UNKNOWN", CONST_NODETYPE_IDENTIFIER_UNKNOWN);
        CONST_MAP_NODETYPE_IDENTIFIER.put("UNBEKANNT", CONST_NODETYPE_IDENTIFIER_UNKNOWN);
        
    }
    
    private static BaseNodeService instance = new BaseNodeService();
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     return the main instance of this service
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return the main instance of this service
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @return the main instance of this service
     */
    public static BaseNodeService getInstance() {
        return instance;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     service-functions for the BaseNode
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the service-class
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     */
    public BaseNodeService() {
        configureDataDomainRecalcer();
    }
    
    
    /////////////////////////
    // Configuration
    /////////////////////////
    
    @Override
    public void configureDataDomainRecalcer() {
        MetaDataServiceImpl.configureDataDomainRecalcer(this);
        SysDataServiceImpl.configureDataDomainRecalcer(this);
        BaseWorkflowDataServiceImpl.configureDataDomainRecalcer(this);
        StatDataServiceImpl.configureDataDomainRecalcer(this);
    }

    @Override
    public Map<String, String> getConfigState() {
        return CONST_MAP_NODETYPE_IDENTIFIER;
    }
    protected Map<String, WorkflowState> getConfigWorkflowStateNoWorkflow() {
        return CONST_MAP_STATE_WORKFLOWSTATE;
    }
    protected Map<WorkflowState, String> getConfigWorkflowStateStateNoWorkflow() {
        return CONST_MAP_WORKFLOWSTATE_STATE;
    }
    @Override
    public Map<String, WorkflowState> getConfigWorkflowState() {
        return this.getConfigWorkflowStateNoWorkflow();
    }
    @Override
    public Map<WorkflowState, String> getConfigWorkflowStateState() {
        return getConfigWorkflowStateStateNoWorkflow();
    }

    ///////////////////////
    // recalc
    ///////////////////////
    @Override
    public void recalcData(final DataDomain baseNode, final int recursionDirection) throws Exception {
        this.doRecalc(baseNode, recursionDirection);
    }

    ///////////////////////
    // Hierarchy-data
    ///////////////////////
    @Override
    public void setParentNode(final DataDomain baseNode, final DataDomain parentNode, 
            final boolean flgRenewParent) {
        // Parentnode setzen, falls geaendert
        boolean flgParentChanged = false;
        if (baseNode.getParentNode() != parentNode) {
            baseNode.setParentNodeOnly(parentNode);
            flgParentChanged = true;
        }

        // Ebene aktualisieren, falls Aenderung erfolgt oder Renew gewuenscht
        boolean flgEbeneChanged = false;
        if (flgParentChanged || flgRenewParent) {
            int newEbene = baseNode.getEbene();
            if (parentNode != null) {
                if (!flgRenewParent) {
                    // SKIP
                } else if (baseNode.hasChildNode(parentNode)) {
                    // SKIP gehoere schon zur Liste
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SKIP: Gehoere schon zur Liste: Me=" 
                            + baseNode.getNameForLogger() 
                            + " Parent=" + parentNode.getNameForLogger());
                    }
                } else {
                    // nur anhaengen, wenn renew-Flag gesetzt und noch nicht vorhanden
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("add2AsChild Me=" 
                                + baseNode.getNameForLogger() 
                                + " Parent=" + parentNode.getNameForLogger());
                    }
                    parentNode.addChildNode(baseNode);
                }
                newEbene = parentNode.getEbene() + 1;
            } else {
                newEbene = 1;
            }
            
            // Ebene nur setzen, wenn geaendert
            if (baseNode.getEbene().intValue() != newEbene) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Ebene change to " + newEbene + "Me=" 
                                + baseNode.getNameForLogger());
                }
                baseNode.setEbene(newEbene);
                flgEbeneChanged = true;

            }
        }

        // falls Ebene geaendert: alle Kinder aktualisieren (Ebene usw.)
        if (flgEbeneChanged) {
            Map<String, DataDomain> lstChidNodes = baseNode.getChildNodesByNameMap();
            if (lstChidNodes != null) {
                for (String nodeName : lstChidNodes.keySet()) {
                    DataDomain node =
                        (DataDomain) lstChidNodes.get(nodeName);
                    node.setParentNode((BaseNode) baseNode);
                }
            }
        }
    }

    @Override
    public List<BaseNode> getParentHierarchy(final DataDomain baseNode) {
        List<BaseNode> parentHierarchy = new ArrayList<BaseNode>();
        BaseNode parent = baseNode.getParentNode();
        while (parent != null) {
            parentHierarchy.add(parent);
            parent = parent.getParentNode();
        }
        return parentHierarchy;
    }

    @Override
    public List<String> getParentIdHierarchy(final DataDomain baseNode) {
        List<String> parentIdHierarchy = new ArrayList<String>();
        for (BaseNode parent: getParentHierarchy(baseNode)) {
            parentIdHierarchy.add(parent.getSysUID());
        }
        
        return parentIdHierarchy;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     add the child in childlist
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates childNodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @param baseNode - basenode to add child
     * @param childNode - the child to add
     */
    public void addChildNode(final BaseNode baseNode, final DataDomain childNode) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("add child:" + childNode.getNameForLogger() + " to " + baseNode.getNameForLogger());
        }
        if (childNode != null) {
            if (childNode.getSortPos() != null) {
                // preserve sortpos of the child
                if (childNode.getSortPos() > baseNode.getCurSortIdx()) {
                    // update idx
                    baseNode.setCurSortIdx(childNode.getSortPos() + BaseNodeService.CONST_CURSORTIDX_STEP);
                }
            } else {
                // set new sortpos for the child
                childNode.setSortPos(baseNode.getCurSortIdx());
                baseNode.setCurSortIdx(baseNode.getCurSortIdx() + BaseNodeService.CONST_CURSORTIDX_STEP);
            }
            baseNode.getChildNodesByNameMap().put(childNode.getIdForChildByNameMap(), childNode);
            baseNode.getChildNodes().add((BaseNode) childNode);
        }
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     move the child in childlist to the sortPos
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>reorder childNodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @param baseNode - basenode to add child
     * @param child - the child to move in list
     * @param newSortPos - the new position
     */
    public void moveChildToSortPos(final BaseNode baseNode, final BaseNode child, 
                                   final Integer newSortPos) {
        // check data
        if (child == null) {
            throw new IllegalArgumentException("child must not be null");
        }
        if (newSortPos == null) {
            throw new IllegalArgumentException("newSortPos must not be null");
        }
        if (!baseNode.getChildNodes().contains(child)) {
            throw new IllegalArgumentException("child is no member of my childlist");
        }
        
        // iterate childlist and look for child
        boolean flgChildWaiting = true;
        
        // preserve the childnodes in order
        Set<BaseNode> tmpChildNodes = new LinkedHashSet<BaseNode>();
        for (BaseNode curChild : baseNode.getChildNodes()) {
            // add the other child
            tmpChildNodes.add(curChild);
        }
        
        // clear the  orig list
        baseNode.getChildNodes().clear();
        baseNode.setCurSortIdx(0);
        
        // if the child moves down, then we have to realize that it is no more in list: so we have to sub the idx 
        int newPos = newSortPos.intValue();
        
        // add the childs to the list
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("I " + child.getName() + " want newPos:" + newPos);
        }
        for (BaseNode curChild : tmpChildNodes) {
            // if sortPos of curChild > newSortPos, then insert it here
            if (flgChildWaiting && curChild.getSortPos().intValue() > newPos) {
                baseNode.addChildNode(child);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("added me " + child.getName() 
                                 + " and got " + child.getSortPos().intValue());
                }
                flgChildWaiting = false;
            }
            if (child.equals(curChild) || child.getSysUID().equalsIgnoreCase(curChild.getSysUID())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("bullshit iam in List already " 
                                 + curChild.getSortPos().intValue() 
                                 + " at " + baseNode.getChildNodes().size());
                // hey i'm already here
                }
            } else {
                // add the other child
                baseNode.addChildNode(curChild);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("added other child:" + curChild.getName() 
                                 + " new:" + curChild.getSortPos().intValue() 
                                 + " at " + baseNode.getChildNodes().size());
                }
            }
            
        }

        // recalc the sortidx
        baseNode.setCurSortIdx(0);
        for (BaseNode curChild : baseNode.getChildNodes()) {
            curChild.setSortPos(baseNode.getCurSortIdx());
            baseNode.setCurSortIdx(baseNode.getCurSortIdx() + BaseNodeService.CONST_CURSORTIDX_STEP);
        }
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns the max level of children for this node 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return maximum childLevel
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param baseNode - node
     * @return maximum childLevel
     */
    public int getMaxChildEbene(final DataDomain baseNode) {
        // TODO cache this
        int maxEbene = baseNode.getEbene();

        // alle Kinder durchsuchen
        if (baseNode.getChildNodes() != null) {
            for (BaseNode node : baseNode.getChildNodes()) {
                int maxEbeneChild = node.getBaseNodeService().getMaxChildEbene(node);
                if (maxEbeneChild > maxEbene) {
                    maxEbene = maxEbeneChild;
                }
            }
        }
        return maxEbene;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     initialize the Children from database (childNodes and childNodesByNameMapMap)
     *     recursivly
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberfields childNodes
     *     <li>updates memberfields childNodesByNameMapMap
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @param baseNode node
     * @param pRecursionLevel - how many recursion-level will be read from DB
     */
    public void initChildNodesFromDB(final BaseNode baseNode, final int pRecursionLevel) {
        int recursionLevel = pRecursionLevel;
        // clear the children
        baseNode.getChildNodes().clear();
        baseNode.getChildNodesByNameMap().clear();
        
        // read my childNodes
        List<BaseNode> tmpChildNodes = baseNode.getBaseNodeDBService().findChildNodes(baseNode.getSysUID());
        
        // set new level if it is not -1
        recursionLevel = recursionLevel > 0 ? recursionLevel-- : recursionLevel;

        // interate children
        for (BaseNode childNode : tmpChildNodes) {
            // add to childrenMaps
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("initChildNodesFromDB add to " + baseNode.getNameForLogger() 
                           + " child:" + childNode.getNameForLogger());
            }
            baseNode.addChildNode(childNode);
            
            // check recursionLevel
            if ((recursionLevel == NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN) 
                || (recursionLevel > 0)) {
                // recurse
                childNode.initChildNodesFromDB(recursionLevel);
            }
        }
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns parenthierarchy
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return parenthierarchy
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param baseNode - node
     * @param pdelimiter - text to delimit the diffrent parentnames
     * @param directionForward - if set reverse the order
     * @return parenthierarchy
     */
    public String getParentNameHirarchry(final DataDomain baseNode,
                                         final String pdelimiter, 
                                         final boolean directionForward) {
        String parentNames = "";
        String delimiter = pdelimiter == null ? "" : pdelimiter;

        if (baseNode.getParentNode() != null) {
            if (directionForward) {
                parentNames = baseNode.getParentNode().getParentNameHirarchry(delimiter,
                    directionForward)
                    + delimiter
                    + baseNode.getParentNode().getName();
            } else {
                parentNames = baseNode.getParentNode().getName()
                    + delimiter
                    + baseNode.getParentNode().getParentNameHirarchry(delimiter,
                        directionForward);
            }
        }

        return parentNames;
    }

    ///////////////////////
    // Name+Id-Services
    ///////////////////////
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns WorkingId to identify in process (parsing...)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return WorkingId to identify in process (parsing...)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param baseNode - node
     * @return WorkingId to identify in process (parsing...)
     */
    public String getWorkingId(final DataDomain baseNode) {
        String res = "UNKNOWN";
        BaseNode node = (BaseNode) baseNode;
        if (node.getMetaNodeNummer() != null && (node.getMetaNodeNummer().length() > 0)) {
            res = node.getMetaNodePraefix() + node.getMetaNodeNummer();
        } else if (node.getImportTmpId() != null) {
            res = node.getImportTmpId().toString();
        }

        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns name for logging
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return name for logging
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param baseNode - node
     * @return name for logging
     */
    public String getNameForLogger(final DataDomain baseNode) {
        BaseNode node = (BaseNode) baseNode;
        String nameForLogger = "sysUID_" + node.getSysUID() 
                        + "_name_" + node.getName() + "_srcName_" + node.getSrcName();
        return nameForLogger;    
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns the data to create a checksum from
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return datastring
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param baseNode - node
     * @return datastring
     * @throws Exception - possible Exception
     */
    public String getDataBlocks4CheckSum(final DataDomain baseNode) throws Exception {
        BaseNode node = (BaseNode) baseNode;

        // Content erzeugen
        StringBuffer data = new StringBuffer();
        data.append(node.getType())
            .append(node.getState())
            .append(" name=").append(node.getName())
// TODO difference DB + PPL-Import  .append(" parentNode=").append((node.getParentNode() != null ? getParentNode().getSysUID() : null))
// TODO difference DB + PPL-Import  .append(" sortPos=").append(node.getSortPos())
//            .append(" ebene=").append(node.getEbene())
//            .append(" istStandChildrenSum=").append(node.getIstChildrenSumStand())
//            .append(" istStartChildrenSum=").append(node.getIstChildrenSumStart())
//            .append(" istEndeChildrenSum=").append(node.getIstChildrenSumEnde())
//            .append(" istAufwandChildrenSum=").append(node.getIstChildrenSumAufwand())
//            .append(" planStartChildrenSum=").append(node.getPlanChildrenSumStart())
//            .append(" planEndeChildrenSum=").append(node.getPlanChildrenSumEnde())
//            .append(" planAufwandChildrenSum=").append(node.getPlanChildrenSumAufwand())
            .append(" docLayoutTagCommand=").append(node.getDocLayoutTagCommand())
            .append(" docLayoutAddStyleClass=").append(node.getDocLayoutAddStyleClass())
            .append(" docLayoutShortName=").append(node.getDocLayoutShortName())
            .append(" docLayoutFlgCloseDiv=").append(node.getDocLayoutFlgCloseDiv())
            .append(" metaNodePraefix=").append(node.getMetaNodePraefix())
            .append(" metaNodeNummer=").append(node.getMetaNodeNummer())
            .append(" metaNodeTypeTags=").append(node.getMetaNodeTypeTags())
            .append(" metaNodeSubTypeTags=").append(node.getMetaNodeSubTypeTags())
            .append(" desc=").append(node.getNodeDesc());
        return data.toString();
    }
    
    
    /////////////////////////
    // Workflow
    /////////////////////////
    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     checks weather the state is a configurated workflow-state
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - workflow-state yes/no
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param state - state to check
     * @return workflow-state yes/no
     */
    public boolean isWFStatus(final String state) {
        return false;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     checks weather the state is a configurated workflow-state for DONE
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - workflow-DONE yes/no
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param state - state to check
     * @return workflow-DONE yes/no
     */
    public boolean isWFStatusDone(final String state) {
        return false;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     checks weather the state is a configurated workflow-state for OPEN
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - workflow-OPEN yes/no
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param state - state to check
     * @return workflow-OPEN yes/no
     */
    public boolean isWFStatusOpen(final String state) {
        return false;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     checks weather the state is a configurated workflow-state for CANCELED
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - workflow-CANCELED yes/no
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param state - state to check
     * @return workflow-CANCELED yes/no
     */
    public boolean isWFStatusCanceled(final String state) {
        return false;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     returns the workflowstate of the node. 
     *     if empty and no state do: NOWORKFLOW of Unplaned if it is a task/evvent
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue workflowstate
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param node - node to get the workflow state from
     * @return workflowstate
     * @throws IllegalStateException - if illegal state 
     */
    public WorkflowState getWorkflowState(final BaseWorkflowData node) throws IllegalStateException {
        return WorkflowState.NOWORKFLOW;
    };

    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     returns the workflowstate for the state of the node. 
     *     if empty and no state do: NOWORKFLOW of Unplaned if it is a task/event
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue workflowstate
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param node - node to get the workflow state from
     * @return workflowstate
     * @throws IllegalStateException - if illegal state 
     */
    public WorkflowState getWorkflowStateForState(final BaseWorkflowData node) throws IllegalStateException {
        return WorkflowState.NOWORKFLOW;
    };

    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     returns the state for the workflowstate of the node. 
     *     if empty and no workflowstate: return the given state of the node
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue state
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param node - node to get the workflow state from
     * @return state
     * @throws IllegalStateException - if illegal state 
     */
    public String getStateForWorkflowState(final BaseWorkflowData node) throws IllegalStateException {
        return node.getState();
    };
}
