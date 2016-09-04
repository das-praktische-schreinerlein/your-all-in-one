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

import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.node.TaskNode;
import de.yaio.app.core.nodeservice.BaseNodeService;
import de.yaio.app.core.nodeservice.NodeService;
import de.yaio.app.core.nodeservice.TaskNodeService;
import de.yaio.app.utils.db.DBFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;


/** 
 * implementation of dbservices for BaseNodes
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Service
public class BaseNodeRepositoryImpl implements BaseNodeRepositoryCustom {
    @PersistenceContext
    protected transient EntityManager entityManager;

    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(BaseNodeRepositoryImpl.class);

    @Override
    @Transactional
    public List<BaseNode> updateMeAndMyParents(final BaseNode node) {
        List<BaseNode> parentHierarchy = null;
        if (node != null) {
            try {
                // init Children from DB
                node.initChildNodesFromDB(0);

                // recalc me
                node.recalcData(BaseNodeService.RecalcRecurseDirection.ONLYME);

                // save me
                update(node);
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
        return entityManager().createQuery(
                        "SELECT o FROM BaseNode o where parent_node = :sysUID order by sort_pos asc", 
                        BaseNode.class
                        ).setParameter("sysUID", sysUID).getResultList();
    }

    @Override
    public List<BaseNode> findSymLinkBaseNode(final String symLinkRef) {
        return entityManager().createQuery(
                        "SELECT o FROM BaseNode o where sysUID = :symLinkRef"
                        + " or CONCAT(metaNodePraefix, metaNodeNummer) = :symLinkRef"
                        + " order by sort_pos asc", 
                        BaseNode.class
                        ).setParameter("symLinkRef", symLinkRef).getResultList();
    }

    @Override
    @Transactional
    public BaseNode resetYaio() {
        // delete all nodes
        entityManager().createNativeQuery("delete from BASE_NODE").executeUpdate();
        
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

        save(masterNode);
        return masterNode;
    }
    
    @Override
    @Transactional
    public void saveChildNodesToDB(final BaseNode baseNode, final int pRecursionLevel,
                                   final boolean flgForceMerge) {
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
                if (entityManager().contains(childNode) || flgForceMerge) {
                    update(childNode);
                } else {
                    save(childNode);
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
                saveChildNodesToDB(childNode, recursionLevel, flgForceMerge);
            }
        }
    }

    @Override
    public void removeChildNodesFromDB(final BaseNode baseNode) {
        // interate children on db
        for (BaseNode childNode : findChildNodes(baseNode.getSysUID())) {
            // persist to DB
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("removeChildNodesFromDB from " + baseNode.getNameForLogger() 
                           + " child:" + childNode.getNameForLogger());
            }
            // recurse
            removeChildNodesFromDB(childNode);
            
            // remove this child
            delete(childNode);
        }
    }

    @Override
    public TypedQuery<BaseNode> createTypedQuery(Class<?> resClass, List<DBFilter> dbFilters, String sort) {
        // create filter
        String filter = "";
        if (dbFilters.size() > 0) {
            List<String>sqlList = new ArrayList<String>();
            for (DBFilter dbFilter : dbFilters) {
                sqlList.add(dbFilter.getSql());
            }
            filter = " where " + StringUtils.join(sqlList, " and ");
        }

        // setup select
        String select = "SELECT o FROM BaseNode o"
                + filter + " " + sort;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("select with filters:" + select + " dbFilters:" + dbFilters);
        }

        // create query
        TypedQuery<BaseNode> query = (TypedQuery<BaseNode>)entityManager().createQuery(select, resClass);

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


    @Override
    public void save(BaseNode baseNode) {
        this.persist(baseNode);
    }

    @Override
    public BaseNode update(BaseNode baseNode) {
        return this.merge(baseNode);
    }

    @Override
    public void delete(BaseNode baseNode) {
        this.remove(baseNode);
    }

    @Override
    public long countBaseNodes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BaseNode o", Long.class).getSingleResult();
    }

    @Override
    public BaseNode findBaseNode(String sysUID) {
        if (sysUID == null || sysUID.length() == 0) return null;
        return entityManager().find(BaseNode.class, sysUID);
    }

    /**
     * active record
     */
    protected final EntityManager entityManager() {
        EntityManager em = entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    @Transactional
    protected void persist(BaseNode baseNode) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(baseNode);
    }

    @Transactional
    protected void remove(BaseNode baseNode) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(baseNode)) {
            this.entityManager.remove(baseNode);
        } else {
            BaseNode attached = findBaseNode(baseNode.getSysUID());
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    protected BaseNode merge(BaseNode baseNode) {
        if (this.entityManager == null) this.entityManager = entityManager();
        BaseNode merged = this.entityManager.merge(baseNode);
        this.entityManager.flush();
        return merged;
    }
}
