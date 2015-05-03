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
package de.yaio.core.dbservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import de.yaio.core.node.BaseNode;
import de.yaio.core.node.TaskNode;
import de.yaio.core.nodeservice.BaseNodeService;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.core.nodeservice.TaskNodeService;


/**
 * <h4>FeatureDomain:</h4>
 *     Persistence
 * <h4>FeatureDescription:</h4>
 *     implementation of dbservices for BaseNodes
 * 
 * @package de.yaio.core.node
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class BaseNodeDBServiceImpl implements BaseNodeDBService {
    protected static final Map<String, String> CONST_AVAILIABLE_SORTS = new HashMap<String, String>();
    static {
        CONST_AVAILIABLE_SORTS.put("default", "");
        CONST_AVAILIABLE_SORTS.put("createdUp", "sysCreateDate asc");
        CONST_AVAILIABLE_SORTS.put("createdDown", "sysCreateDate desc");
        CONST_AVAILIABLE_SORTS.put("istEndeUp", "istChildrenSumEnde asc");
        CONST_AVAILIABLE_SORTS.put("istEndeDown", "istChildrenSumEnde desc");
        CONST_AVAILIABLE_SORTS.put("istStartUp", "istChildrenSumStart asc");
        CONST_AVAILIABLE_SORTS.put("istStartDown", "istChildrenSumStart desc");
        CONST_AVAILIABLE_SORTS.put("lastChangeUp", "sysChangeDate asc");
        CONST_AVAILIABLE_SORTS.put("lastChangeDown", "sysChangeDate desc");
        CONST_AVAILIABLE_SORTS.put("nameUp", "name asc");
        CONST_AVAILIABLE_SORTS.put("nameDown", "name desc");
        CONST_AVAILIABLE_SORTS.put("nodeNumberUp", "metaNodePraefix asc, metaNodeNummer asc");
        CONST_AVAILIABLE_SORTS.put("nodeNumberDown", "metaNodePraefix desc, metaNodeNummer desc");
        CONST_AVAILIABLE_SORTS.put("planEndeUp", "planChildrenSumEnde asc");
        CONST_AVAILIABLE_SORTS.put("planEndeDown", "planChildrenSumEnde desc");
        CONST_AVAILIABLE_SORTS.put("planStartUp", "planChildrenSumStart asc");
        CONST_AVAILIABLE_SORTS.put("planStartDown", "planChildrenSumStart desc");
        CONST_AVAILIABLE_SORTS.put("typeUp", "type asc");
        CONST_AVAILIABLE_SORTS.put("typeDown", "type desc");
        CONST_AVAILIABLE_SORTS.put("workflowStateUp", "workflowState asc");
        CONST_AVAILIABLE_SORTS.put("workflowStateDown", "workflowState desc");
    }
    
    protected static BaseNodeDBService instance = new BaseNodeDBServiceImpl();
    
    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(BaseNodeDBServiceImpl.class);

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     return the instance of BaseNodeDBService
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>ReturnValue BaseNodeDBService - the instance of BaseNodeDBService
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @return - the instance of BaseNodeDBService
     */
    public static BaseNodeDBService getInstance() {
        return instance;
    }
    
    @Override
    public List<BaseNode> updateMeAndMyParents(final BaseNode node) throws Exception {
        List<BaseNode> parentHierarchy = null;
        if (node != null) {
            try {
                // init Children from DB
                node.initChildNodesFromDB(0);

                // recalc me
                node.recalcData(BaseNodeService.CONST_RECURSE_DIRECTION_ONLYME);

                // save me
                node.merge();
                //CHECKSTYLE.OFF: IllegalCatch - Much more readable than catching x exceptions
            } catch (Exception ex) {
                //CHECKSTYLE.ON: IllegalCatch
                LOGGER.error("errors while updating node '" 
                                + node.getNameForLogger() + "':", ex);
                LOGGER.error("error saving node '" 
                                + node);
                throw ex;
            }            
            
            // update my parents
            if (node.getParentNode() != null) {
                parentHierarchy = updateMeAndMyParents(node.getParentNode());
            } else {
                // i am root -> create hierarchylist
                parentHierarchy = new ArrayList<BaseNode>();
            }
            
            // add me to hierarchylist
            parentHierarchy.add(node);
        }
        return parentHierarchy;
    }

    @Override
    public List<BaseNode> findChildNodes(final String sysUID) {
        return BaseNode.entityManager().createQuery(
                        "SELECT o FROM BaseNode o where parent_node = :sysUID order by sort_pos asc", 
                        BaseNode.class
                        ).setParameter("sysUID", sysUID).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public long countFulltextBaseNodes(final String fulltext) {
        TypedQuery<Long> query = (TypedQuery<Long>) this.createFulltextQuery(true, fulltext, null);
        return query.getSingleResult();
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<BaseNode> findFulltextBaseNodeEntries(final String fulltext, final String sortConfig,
                    final int firstResult, final int maxResults) {
        TypedQuery<BaseNode> query = (TypedQuery<BaseNode>) this.createFulltextQuery(false, fulltext, sortConfig);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        
        return query.getResultList();
    }
    
    @Override
    public List<BaseNode> findSymLinkBaseNode(final String symLinkRef) {
        return BaseNode.entityManager().createQuery(
                        "SELECT o FROM BaseNode o where sysUID = :symLinkRef"
                        + " or CONCAT(metaNodePraefix, metaNodeNummer) = :symLinkRef"
                        + " order by sort_pos asc", 
                        BaseNode.class
                        ).setParameter("symLinkRef", symLinkRef).getResultList();
    }

    @Override
    public BaseNode resetYaio() {
        // delete all nodes
        BaseNode.entityManager().createNativeQuery("delete from BASE_NODE").executeUpdate();
        
        TaskNode masterNode = new TaskNode();
        masterNode.setEbene(0);
        masterNode.setMetaNodeNummer("1");
        masterNode.setMetaNodePraefix("Masterplan");
        masterNode.setName("Masterplan");
        masterNode.setNodeDesc("Masternode of the Masterplan");
        masterNode.setSrcName("Masterplan");
        masterNode.setState(TaskNodeService.CONST_NODETYPE_IDENTIFIER_RUNNNING);
        masterNode.setSysUID("MasterplanMasternode1");
        masterNode.setType(TaskNodeService.CONST_NODETYPE_IDENTIFIER_RUNNNING);
        
        masterNode.persist();
        return masterNode;
    }
    
    @Override
    public void saveChildNodesToDB(final BaseNode baseNode, final int pRecursionLevel, 
                                   final boolean flgForceMerge) throws Exception {
        // set new level if it is not -1
        int recursionLevel = pRecursionLevel;
        recursionLevel = recursionLevel > 0 ? recursionLevel-- : recursionLevel;

        // interate children
        for (BaseNode childNode : baseNode.getChildNodes()) {
            // validate data
//            if (childNode.getMetaNodeNummer() == null) {
//                childNode.initMetaData();
//            }
            if (childNode.getSysUID() == null) {
                childNode.initSysData();
            }

            // persist to DB
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("persistChildNodesToDB from " + baseNode.getNameForLogger() 
                               + " child:" + childNode.getNameForLogger());
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("childNode:" + childNode.getName() + " pos: " + childNode.getSortPos());
                }
                
                // check if persist or merge
                if (BaseNode.entityManager().contains(childNode) || flgForceMerge) {
                    childNode.merge();
                } else {
                    childNode.persist();
                }
                //CHECKSTYLE.OFF: IllegalCatch - Much more readable than catching x exceptions
            } catch (Exception ex) {
                //CHECKSTYLE.ON: IllegalCatch
                LOGGER.error("errors while saving childnode for '" 
                                + baseNode.getSysUID() + "':", ex);
                LOGGER.error("error saving node '" 
                                + childNode);
                throw ex;
            }            
//            boolean flgOK = true;
//            try {
//                childNode.persist();
//            } catch (Exception ex) {
//                LOGGER.error("persistChildNodesToDB error for childnode " 
//                           + childNode.getMetaNodePraefix() 
//                           + "," + childNode.getMetaNodeNummer() 
//                           + " SysUID: " + childNode.getSysUID() 
//                           + " Name: " + childNode.getName() 
//                           + " ex:" + ex);
////                LOGGER.error("persistChildNodesToDB error for parent " 
////                        + this.getSysUID() + " Name: " + this.getName());
////                LOGGER.error("persistChildNodesToDB error for childnodedetails " + childNode.getNameForLogger());
////                LOGGER.error("persistChildNodesToDB error for parentdetails " + this.getNameForLogger());
//                flgOK = false;
////                throw new Exception(ex);
//            }
            
            // check recursionLevel
            if ((recursionLevel == NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN) 
                 || (recursionLevel > 0)) {
                // recurse
//                if (flgOK)
                childNode.saveChildNodesToDB(recursionLevel, flgForceMerge);
            }
        }
    }

    @Override
    public void removeChildNodesFromDB(final BaseNode baseNode) {
        // interate children on db
        for (BaseNode childNode : baseNode.getBaseNodeDBService().findChildNodes(baseNode.getSysUID())) {
            // persist to DB
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("removeChildNodesFromDB from " + baseNode.getNameForLogger() 
                           + " child:" + childNode.getNameForLogger());
            }
            // recurse
            childNode.removeChildNodesFromDB();
            
            // remove this child
            childNode.remove();
        }
    }
    

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected TypedQuery createFulltextQuery(final boolean flgCount, final String pfulltext, final String sortConfig) {
        // setup class
        Class resClass = BaseNode.class;
        if (flgCount) {
            resClass = Long.class;
        }

        // tokenize words
        String filter = "";
        String[] searchWords = null;
        if (pfulltext != null && pfulltext.length() > 0) {
            String fulltext = pfulltext.replace("  ", " ");
            searchWords = fulltext.split(" ");
            if (searchWords.length > 0) {
                int idx = 0;
                filter = " where (lower(name) like lower(:fulltext" + idx + ")"
                                + " or lower(node_desc) like lower(:fulltext" + idx + ")"
                                + " or lower(sym_link_ref) like lower(:fulltext" + idx + ")"
                                + " or lower(sym_link_name) like lower(:fulltext" + idx + ")"
                                + " or lower(res_loc_name) like lower(:fulltext" + idx + ")"
                                + " or lower(res_loc_ref) like lower(:fulltext" + idx + ")"
                                + ")";
                for (; idx < searchWords.length; idx++) {
                    filter += " and (lower(name) like lower(:fulltext" + idx + ")"
                                    + " or lower(node_desc) like lower(:fulltext" + idx + ")"
                                    + " or lower(sym_link_ref) like lower(:fulltext" + idx + ")"
                                    + " or lower(sym_link_name) like lower(:fulltext" + idx + ")"
                                    + " or lower(res_loc_name) like lower(:fulltext" + idx + ")"
                                    + " or lower(res_loc_ref) like lower(:fulltext" + idx + ")"
                                    + ")";
                }
            }
        }

        // setup sort
        String sort = "";
        if (sortConfig != null) {
            sort = CONST_AVAILIABLE_SORTS.get(sortConfig);
            if (sort != null) {
                if (sort.length() > 0) {
                    sort = sort + ", ";
                }
            } else {
                throw new IllegalArgumentException("Unknown sort:" + sortConfig);
            }
        }
        // setup order
        String order = "asc";
        
        // setup select
        String select = "SELECT o FROM BaseNode o"
                      + filter
                      + " order by " + sort 
                      + " ebene " + order 
                      + ", parent_node " + order 
                      + ", sort_pos " + order;
        if (flgCount) {
            select = "SELECT COUNT(o) FROM BaseNode o"
                   + filter;
        }
        
        // create query
        TypedQuery query = BaseNode.entityManager().createQuery(
                        select, resClass);
        
        // add parameters
        if (searchWords != null && searchWords.length > 0) {
            int idx = 0;
            for (; idx < searchWords.length; idx++) {
                query.setParameter("fulltext" + idx, "%" + searchWords[idx] + "%");
            }
        }
        
        return query;
    }
}
