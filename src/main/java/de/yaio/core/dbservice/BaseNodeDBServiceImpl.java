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
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import de.yaio.core.node.BaseNode;
import de.yaio.core.nodeservice.BaseNodeService;


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
    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(BaseNodeDBServiceImpl.class);

    
    
    protected static BaseNodeDBService instance = new BaseNodeDBServiceImpl();
    
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
    public List<BaseNode> updateMeAndMyParents(BaseNode node) throws Exception {
        List<BaseNode> parentHierarchy = null;
        if (node != null) {
            try {
                // init Cildren from DB
                node.initChildNodesFromDB(0);

                // recalc me
                node.recalcData(BaseNodeService.CONST_RECURSE_DIRECTION_ONLYME);

                // save me
                node.merge();
            } catch (Exception ex) {
                LOGGER.error("errors while updating node '" 
                                + node.getNameForLogger() + "':" + ex);
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
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     read the children for the sysUID from database
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue List<BaseNode> - list of the children
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence JPA
     * @param sysUID - sysUID for the filter on parent_node
     * @return List of childnodes for basenode with sysUID
     */
    public List<BaseNode> findChildNodes(String sysUID) {
        return BaseNode.entityManager().createQuery(
                        "SELECT o FROM BaseNode o where parent_node = :sysUID order by sort_pos asc", 
                        BaseNode.class
                        ).setParameter("sysUID", sysUID).getResultList();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected TypedQuery createFulltextQuery(boolean flgCount, String fulltext) {
        // setup class
        Class resClass = BaseNode.class;
        if (flgCount)
            resClass = Long.class;

        // tokenize words
        String filter = "";
        String[] searchWords = null;
        fulltext.replace("  ", " ");
        if (fulltext != null && fulltext.length() > 0) {
            searchWords = fulltext.split(" ");
            if (searchWords.length > 0) {
                int idx=0;
                filter = " where (lower(name) like lower(:fulltext" + idx + ")"
                                + " or lower(node_desc) like lower(:fulltext" + idx + "))";
                for (; idx < searchWords.length; idx++) {
                    filter += " and (lower(name) like lower(:fulltext" + idx + ")"
                                    + " or lower(node_desc) like lower(:fulltext" + idx + "))";
                }
            }
        }
        // setup select
        String select = "SELECT o FROM BaseNode o"
                      + filter
                      + " order by ebene asc, parent_node asc, sort_pos asc";
        if (flgCount)
            select = "SELECT COUNT(o) FROM BaseNode o"
                   + filter;

        // create query
        TypedQuery query = BaseNode.entityManager().createQuery(
                        select, resClass);
        
        // add parameters
        if (searchWords != null && searchWords.length > 0) {
            int idx=0;
            for (; idx < searchWords.length; idx++) {
                query.setParameter("fulltext" + idx, "%" + searchWords[idx] + "%");
            }
        }
        
        return query;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     count the basenodes which match fulltext
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue List<BaseNode> - list of the nodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence JPA
     * @param fulltext - fulltext to search in desc and name
     * @return total of matching nodes
     */
    @SuppressWarnings("unchecked")
    public long countFulltextBaseNodes(String fulltext) {
        TypedQuery<Long> query = (TypedQuery<Long>)this.createFulltextQuery(true, fulltext);
        return query.getSingleResult();
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     read the basenodes which match fulltext
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue List<BaseNode> - list of the nodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence JPA
     * @param fulltext - fulltext to search in desc and name
     * @param firstResult - resutrange for pagination
     * @param maxResults - resutrange for pagination
     * @return List of matching nodes
     */
    @SuppressWarnings("unchecked")
    public List<BaseNode> findFulltextBaseNodeEntries(String fulltext, 
                    int firstResult, int maxResults) {
        TypedQuery<BaseNode> query = (TypedQuery<BaseNode>)this.createFulltextQuery(false, fulltext);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        
        return query.getResultList();
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     read the matching nodes for the symLinkRef from database
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue List<BaseNode> - list of the the children
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence JPA
     * @param symLinkRef - symLinkRef for the filter on node
     * @return List of machting nodes for symLinkRef
     */
    public List<BaseNode> findSymLinkBaseNode(String symLinkRef) {
        return BaseNode.entityManager().createQuery(
                        "SELECT o FROM BaseNode o where sysUID = :symLinkRef"
                        + " or CONCAT(metaNodePraefix, metaNodeNummer) = :symLinkRef"
                        + " order by sort_pos asc", 
                        BaseNode.class
                        ).setParameter("symLinkRef", symLinkRef).getResultList();
    }
    
    
}
