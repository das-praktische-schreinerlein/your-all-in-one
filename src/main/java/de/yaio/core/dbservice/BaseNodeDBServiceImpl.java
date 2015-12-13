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
package de.yaio.core.dbservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.yaio.core.datadomain.BaseWorkflowData.WorkflowState;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.TaskNode;
import de.yaio.core.nodeservice.BaseNodeService;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.core.nodeservice.TaskNodeService;


/** 
 * implementation of dbservices for BaseNodes
 * 
 * @FeatureDomain                Persistence
 * @package                      de.yaio.core.node
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class BaseNodeDBServiceImpl implements BaseNodeDBService {
    protected static final Map<String, String> CONST_AVAILIABLE_SORTS = new HashMap<String, String>();
    static {
        CONST_AVAILIABLE_SORTS.put("default", "");
        CONST_AVAILIABLE_SORTS.put("createdUp", sortNullBehind("sysCreateDate", "asc"));
        CONST_AVAILIABLE_SORTS.put("createdDown", sortNullBehind("sysCreateDate", "desc"));
        CONST_AVAILIABLE_SORTS.put("istEndeUp", sortNullBehind("istChildrenSumEnde", "asc"));
        CONST_AVAILIABLE_SORTS.put("istEndeDown", sortNullBehind("istChildrenSumEnde", "desc"));
        CONST_AVAILIABLE_SORTS.put("istStartUp", sortNullBehind("istChildrenSumStart", "asc"));
        CONST_AVAILIABLE_SORTS.put("istStartDown", sortNullBehind("istChildrenSumStart", "desc"));
        CONST_AVAILIABLE_SORTS.put("lastChangeUp", sortNullBehind("sysChangeDate", "asc"));
        CONST_AVAILIABLE_SORTS.put("lastChangeDown", sortNullBehind("sysChangeDate", "desc"));
        CONST_AVAILIABLE_SORTS.put("nameUp", "name asc");
        CONST_AVAILIABLE_SORTS.put("nameDown", "name desc");
        CONST_AVAILIABLE_SORTS.put("nodeNumberUp", "metaNodePraefix asc, metaNodeNummer asc");
        CONST_AVAILIABLE_SORTS.put("nodeNumberDown", "metaNodePraefix desc, metaNodeNummer desc");
        CONST_AVAILIABLE_SORTS.put("planEndeUp", sortNullBehind("planEnde", "asc"));
        CONST_AVAILIABLE_SORTS.put("planEndeDown", sortNullBehind("planEnde", "desc"));
        CONST_AVAILIABLE_SORTS.put("planStartUp", sortNullBehind("planStart", "asc"));
        CONST_AVAILIABLE_SORTS.put("planStartDown", sortNullBehind("planStart", "desc"));
        CONST_AVAILIABLE_SORTS.put("planChildrenSumEndeUp", sortNullBehind("planChildrenSumEnde", "asc"));
        CONST_AVAILIABLE_SORTS.put("planChildrenSumEndeDown", sortNullBehind("planChildrenSumEnde", "desc"));
        CONST_AVAILIABLE_SORTS.put("planChildrenSumStartUp", sortNullBehind("planChildrenSumStart", "asc"));
        CONST_AVAILIABLE_SORTS.put("planChildrenSumStartDown", sortNullBehind("planChildrenSumStart", "desc"));
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
     * return the instance of BaseNodeDBService
     * @FeatureDomain                Persistence
     * @FeatureResult                ReturnValue BaseNodeDBService - the instance of BaseNodeDBService
     * @FeatureKeywords              Persistence
     * @return                       the instance of BaseNodeDBService
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
    
    @SuppressWarnings("unchecked")
    @Override
    public long countExtendedSearchBaseNodes(final String fulltext, final SearchOptions searchOptions) {
        TypedQuery<Long> query = 
                        (TypedQuery<Long>) this.createExtendedSearchQuery(true, fulltext, searchOptions, null);
        return query.getSingleResult();
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<BaseNode> findExtendedSearchBaseNodeEntries(final String fulltext, final SearchOptions searchOptions,
                    final String sortConfig, final int firstResult, final int maxResults) {
        TypedQuery<BaseNode> query = (TypedQuery<BaseNode>) this.createExtendedSearchQuery(
                        false, fulltext, searchOptions, sortConfig);
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
    
    
    protected List<DBFilter> createMapStringFilter(final String fieldName, final Set<String> values) {
        int idx = 0;
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();
        if (values != null) {
            List<String> sqlList = new ArrayList<String>();
            List<DBFilter.Parameter> parameters = new ArrayList<DBFilter.Parameter>();
            for (String value : values) {
                sqlList.add("(" + "lower(" + fieldName + ") = lower(:mapStringFilter" + fieldName + idx + ")" + ")");
                parameters.add(new DBFilter.Parameter("mapStringFilter" + fieldName + idx, value));
                idx++;
            }
            dbFilters.add(new DBFilter("(" + StringUtils.join(sqlList, " or ") + ")", parameters));
        }
        return dbFilters;
    }

    protected List<DBFilter> createMapIntFilter(final String fieldName, final Set<Integer> values) {
        int idx = 0;
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();
        if (values != null) {
            List<String> sqlList = new ArrayList<String>();
            List<DBFilter.Parameter> parameters = new ArrayList<DBFilter.Parameter>();
            for (Integer value : values) {
                sqlList.add("(" + fieldName + " = :mapIntFilter" + fieldName + idx + ")");
                parameters.add(new DBFilter.Parameter("mapIntFilter" + fieldName + idx, value));
                idx++;
            }
            dbFilters.add(new DBFilter("(" + StringUtils.join(sqlList, " or ") + ")", parameters));
        }
        return dbFilters;
    }
    
    protected List<DBFilter> createSearchOptionsFilter(final SearchOptions searchOptions) {
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();
        if (searchOptions == null) {
            return dbFilters;
        }

        // create filters for maps
        dbFilters.addAll(createMapStringFilter("state", 
                        searchOptions.getMapStateFilter() != null ? searchOptions.getMapStateFilter().keySet() : null));
        dbFilters.addAll(createMapStringFilter("dtype", 
                        searchOptions.getMapClassFilter() != null ? searchOptions.getMapClassFilter().keySet() : null));
        dbFilters.addAll(createMapStringFilter("type", 
                        searchOptions.getMapTypeFilter() != null ? searchOptions.getMapTypeFilter().keySet() : null));
        
        // create filter for wfstate (convert enum to integer)
        Map<String, WorkflowState> wfStateMap = searchOptions.getMapWorkflowStateFilter();
        if (MapUtils.isNotEmpty(wfStateMap)) {
            List<WorkflowState> wfStates = Arrays.asList(WorkflowState.values());
            Set<Integer>wfStateValues = new HashSet<Integer>();
            for (WorkflowState state : wfStateMap.values()) {
                wfStateValues.add(wfStates.indexOf(state));
            }
            dbFilters.addAll(createMapIntFilter("workflow_state", wfStateValues));
        }
        
        // create filter for ebene
        String sql = "(ebene <= :ltmaxEbene)";
        List<DBFilter.Parameter> parameters = new ArrayList<DBFilter.Parameter>();
        parameters.add(new DBFilter.Parameter("ltmaxEbene", new Integer(searchOptions.getMaxEbene())));
        dbFilters.add(new DBFilter(sql, parameters));

        return dbFilters;
    }

    protected List<DBFilter> createFulltextFilter(final String pfulltext) {
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();

        // tokenize words
        String[] searchWords = null;
        if (!StringUtils.isEmpty(pfulltext)) {
            String fulltext = pfulltext.replace("  ", " ");
            searchWords = fulltext.split(" ");
            for (int idx = 0; idx < searchWords.length; idx++) {
                String sql = "(lower(name) like lower(:fulltext" + idx + ")"
                                + " or lower(node_desc) like lower(:fulltext" + idx + ")"
                                + " or lower(sym_link_ref) like lower(:fulltext" + idx + ")"
                                + " or lower(sym_link_name) like lower(:fulltext" + idx + ")"
                                + " or lower(res_loc_name) like lower(:fulltext" + idx + ")"
                                + " or lower(res_loc_ref) like lower(:fulltext" + idx + ")"
                                + ")";
                List<DBFilter.Parameter> parameters = new ArrayList<DBFilter.Parameter>();
                parameters.add(new DBFilter.Parameter("fulltext" + idx, "%" + searchWords[idx] + "%"));
                dbFilters.add(new DBFilter(sql, parameters));
            }
        }
        return dbFilters;
    }
    
    protected List<DBFilter> createNotNodePraefixFilter(final String pfulltext) {
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();

        // tokenize words
        String[] searchWords = null;
        if (!StringUtils.isEmpty(pfulltext)) {
            String fulltext = pfulltext.replace("  ", " ");
            searchWords = fulltext.split(" ");
            for (int idx = 0; idx < searchWords.length; idx++) {
                String sql = "not (lower(meta_node_praefix) like lower(:notnodepraefix" + idx + ")"
                                + ")";
                List<DBFilter.Parameter> parameters = new ArrayList<DBFilter.Parameter>();
                parameters.add(new DBFilter.Parameter("notnodepraefix" + idx, searchWords[idx]));
                dbFilters.add(new DBFilter(sql, parameters));
            }
        }
        return dbFilters;
    }

    protected List<DBFilter> createConcreteTodosOnlyFilter(final Integer pFlgConcreteTodosOnly) {
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();

        // tokenize words
        if (pFlgConcreteTodosOnly != null && pFlgConcreteTodosOnly > 0) {
            String sql = "planAufwand > 0";
            List<DBFilter.Parameter> parameters = new ArrayList<DBFilter.Parameter>();
            dbFilters.add(new DBFilter(sql, parameters));
        }
        return dbFilters;
    }

    protected String createSort(final String sortConfig) {
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
        
        return " order by " + sort
            + " statWorkflowTodoCount asc"
            + ", ebene desc"
            + ", parent_node " + order 
            + ", sort_pos " + order;
    }

    protected TypedQuery<?> createFulltextQuery(final boolean flgCount, final String pfulltext, 
                    final String sortConfig) {
        return createExtendedSearchQuery(flgCount, pfulltext, null, sortConfig);
    }

    protected TypedQuery<?> createExtendedSearchQuery(final boolean flgCount, final String pfulltext, 
                                                   final SearchOptions searchOptions, final String sortConfig) {
        // setup class
        Class<?> resClass = BaseNode.class;
        if (flgCount) {
            resClass = Long.class;
        }
        
        // create filter
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();
        dbFilters.addAll(createFulltextFilter(pfulltext));
        dbFilters.addAll(createNotNodePraefixFilter(searchOptions.getStrNotNodePraefix()));
        dbFilters.addAll(createSearchOptionsFilter(searchOptions));
        dbFilters.addAll(createConcreteTodosOnlyFilter(searchOptions.getFlgConcreteToDosOnly()));
        String filter = "";
        if (dbFilters.size() > 0) {
            List<String>sqlList = new ArrayList<String>();
            for (DBFilter dbFilter : dbFilters) {
                sqlList.add(dbFilter.getSql());
            }
            filter = " where " + StringUtils.join(sqlList, " and ");
        }

        // create sort
        String sort = createSort(sortConfig);
        
        // setup select
        String select = "SELECT o FROM BaseNode o"
                      + filter
                      + sort;
        if (flgCount) {
            select = "SELECT COUNT(o) FROM BaseNode o"
                   + filter;
        }
        LOGGER.info(select);
        
        // create query
        TypedQuery<?> query = BaseNode.entityManager().createQuery(select, resClass);
        
        // add parameters
        if (dbFilters.size() > 0) {
            for (DBFilter dbFilter : dbFilters) {
                for (DBFilter.Parameter parameter : dbFilter.getParameters()) {
                    query.setParameter(parameter.getName(), parameter.getValue());
                }
            }
        }
        
        return query;
    }
    
    protected static String sortNullBehind(final String fieldName, final String order) {
        return "CASE WHEN " + fieldName + " IS NULL THEN 1 ELSE 0 END, " + fieldName + " " + order;
    }
}
