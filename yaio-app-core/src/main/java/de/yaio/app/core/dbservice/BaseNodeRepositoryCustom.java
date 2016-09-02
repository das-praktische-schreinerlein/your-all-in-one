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
import de.yaio.app.utils.db.DBFilter;

import javax.persistence.TypedQuery;
import java.util.List;

/** 
 * dbservices for BaseNodes
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public interface BaseNodeRepositoryCustom {

    /** 
     * recalc and merge the node and its parents recursively
     * @param node                   the node to recalc and merge
     * @return                       List - list of the recalced and saved parenthierarchy
     */
    List<BaseNode> updateMeAndMyParents(BaseNode node);

    /** 
     * read the children for the sysUID from database
     * @param sysUID                 sysUID for the filter on parent_node
     * @return                       List of childnodes for basenode with sysUID
     */
    List<BaseNode> findChildNodes(String sysUID);

    /**
     * read the matching nodes for the symLinkRef from database
     * @param symLinkRef             symLinkRef for the filter on node
     * @return                       List of machting nodes for symLinkRef
     */
    List<BaseNode> findSymLinkBaseNode(String symLinkRef);


    /**
     * delete all nodes an create a new Masternode
     * @return                       new Masternode
     */
    BaseNode resetYaio();

    /** 
     * saves the children to database (childNodes) recursivly<br>
     * check if entityManger contains objects<br>
     * if not: do persist
     * if yes or flgForceMerge is set: do merge
     * @param basenode               parentnode
     * @param pRecursionLevel        how many recursion-level will be saved to DB (0 = only my children)
     * @param flgForceMerge          force merge not persists
     */
    void saveChildNodesToDB(final BaseNode basenode, final int pRecursionLevel,
                            final boolean flgForceMerge);

    /** 
     * remove the children from database recursivly
     * @param basenode               parentnode
     */
    void removeChildNodesFromDB(final BaseNode basenode);


    TypedQuery<BaseNode> createTypedQuery(Class<?> resClass, List<DBFilter> dbFilters, String sort);

    long countBaseNodes();

    void delete(BaseNode baseNode);

    BaseNode findBaseNode(String id);

    void save(BaseNode baseNode);

    BaseNode update(BaseNode baseNode);
}
