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
package de.yaio.app.core.dbservice;

import de.yaio.app.core.node.TaskNode;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.nodeservice.BaseNodeService;
import de.yaio.app.core.nodeservice.NodeService;
import de.yaio.app.core.nodeservice.TaskNodeService;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.*;


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
    protected static BaseNodeDBService instance = new BaseNodeDBServiceImpl();
    
    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(BaseNodeDBServiceImpl.class);

    /** 
     * return the instance of BaseNodeDBService
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
                node.recalcData(BaseNodeService.RecalcRecurseDirection.ONLYME);

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
                parentHierarchy = new ArrayList<>();
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
    public long countExtendedSearchBaseNodes(final String fulltext, final String rootSysUID, final SearchOptions searchOptions) {
        TypedQuery<Long> query = 
                        (TypedQuery<Long>) BaseNodeQueryFactory.createExtendedSearchQuery(true, fulltext, rootSysUID, searchOptions, null);
        return query.getSingleResult();
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<BaseNode> findExtendedSearchBaseNodeEntries(final String fulltext, final String rootSysUID, final SearchOptions searchOptions,
                    final String sortConfig, final int firstResult, final int maxResults) {
        TypedQuery<BaseNode> query = (TypedQuery<BaseNode>) BaseNodeQueryFactory.createExtendedSearchQuery(
                        false, fulltext, rootSysUID, searchOptions, sortConfig);
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
    public List<List> calcAufwandPerDayStatistic(final BaseNodeQueryFactory.IstPlanPerDayStatisticQueryType type,
                                           final Date start, final Date end, final String pfulltext,
                                           final String rootSysUID, final SearchOptions searchOptions) {
        return BaseNodeQueryFactory.createAufwandPerDayStatisticQuery(type, start, end, pfulltext, rootSysUID, searchOptions).getResultList();
    }

    @Override
    public List<List> calcCountPerDayStatistic(final BaseNodeQueryFactory.IstPlanPerDayStatisticQueryType type,
                                         final BaseNodeQueryFactory.StartEndPerDayStatisticQueryType timeType,
                                         final Date start, final Date end, final String pfulltext,
                                         final String rootSysUID, final SearchOptions searchOptions) {
        return BaseNodeQueryFactory.createCountPerDayStatisticQuery(type, timeType, start, end, pfulltext, rootSysUID, searchOptions).getResultList();
    }

    @Override
    public List<List> calcDonePerDayStatistic(final Date start, final Date end, final String pfulltext,
                                        final String rootSysUID, final SearchOptions searchOptions) {
        return BaseNodeQueryFactory.createDonePerDayStatisticQuery(start, end, pfulltext, rootSysUID, searchOptions).getResultList();
    }

    @Override
    public List<List> calcRunningPerDayStatistic(final BaseNodeQueryFactory.IstPlanPerDayStatisticQueryType type,
                                           final Date start, final Date end, final String pfulltext,
                                           final String rootSysUID, final SearchOptions searchOptions) {
        return BaseNodeQueryFactory.createRunningPerDayStatisticQuery(type, start, end, pfulltext, rootSysUID, searchOptions).getResultList();
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
                childNode.initSysData(true);
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
}
