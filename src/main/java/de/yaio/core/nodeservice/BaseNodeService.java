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
import java.util.List;
import java.util.Map;

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
    @Override
    public Map<String, WorkflowState> getConfigWorkflowState() {
        return this.getConfigWorkflowState();
    }
    public Map<WorkflowState, String> getConfigWorkflowStateState() {
        return CONST_MAP_WORKFLOWSTATE_STATE;
    }
    public Map<String, WorkflowState> getConfigWorkflowStateNoWorkflow() {
        return CONST_MAP_STATE_WORKFLOWSTATE;
    }
    public Map<WorkflowState, String> getConfigWorkflowStateStateNoWorkflow() {
        return CONST_MAP_WORKFLOWSTATE_STATE;
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
    public void recalcData(final DataDomain baseNode, final int recursionDirection) throws Exception {
        this.doRecalc(baseNode, recursionDirection);
    }

    @Override
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
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     get a List of the parent-hierarchy
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue List<BaseNode> - list of the parents, start with my own parent (not me)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @param baseNode node
     * @return list of the parents, start with my own parent (not baseNode)
     */
    public List<BaseNode> getParentHierarchy(final DataDomain baseNode) {
        List<BaseNode> parentHierarchy = new ArrayList<BaseNode>();
        BaseNode parent = baseNode.getParentNode();
        while (parent != null) {
            parentHierarchy.add(parent);
            parent = parent.getParentNode();
        }
        return parentHierarchy;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     get a List of the Ids of parent-hierarchy 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue List<String> - list of the parent-sysUIDs, start with my own parent (not me)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @param baseNode node
     * @return list of the parent-sysUIDs, start with my own parent (not baseNode)
     */
    public List<String> getParentIdHierarchy(final DataDomain baseNode) {
        List<String> parentIdHierarchy = new ArrayList<String>();
        for (BaseNode parent: getParentHierarchy(baseNode)) {
            parentIdHierarchy.add(parent.getSysUID());
        }
        
        return parentIdHierarchy;
    }

    @Override
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

    @Override
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

    @Override
    public String getNameForLogger(final DataDomain baseNode) {
        BaseNode node = (BaseNode) baseNode;
        String nameForLogger = "sysUID_" + node.getSysUID() 
                        + "_name_" + node.getName() + "_srcName_" + node.getSrcName();
        return nameForLogger;    
    }
    
    @Override
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
    public WorkflowState getWorkflowState(final BaseWorkflowData node)  throws IllegalStateException {
        return WorkflowState.NOWORKFLOW;
    };

    public WorkflowState getWorkflowStateForState(final BaseWorkflowData node)  throws IllegalStateException {
        return WorkflowState.NOWORKFLOW;
    };

    public String getStateForWorkflowState(final BaseWorkflowData node)  throws IllegalStateException {
        return node.getState();
    };
}
