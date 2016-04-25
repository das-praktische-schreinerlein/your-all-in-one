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
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import de.yaio.core.node.BaseNode;

/** 
 * dbservices for BaseNodes
 * 
 * @FeatureDomain                Persistence
 * @package                      de.yaio.core.node
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@RooService(domainTypes = { de.yaio.core.node.BaseNode.class })
public interface BaseNodeDBService {

    /** 
     * recalc and merge the node and its parents recursively
     * @param node                   the node to recalc and merge
     * @return                       List - list of the recalced and saved parenthierarchy
     * @throws Exception             io/DB-Exceptions possible
     */
    List<BaseNode> updateMeAndMyParents(BaseNode node) throws Exception;

    /** 
     * read the children for the sysUID from database
     * @param sysUID                 sysUID for the filter on parent_node
     * @return                       List of childnodes for basenode with sysUID
     */
    List<BaseNode> findChildNodes(String sysUID);

    /** 
     * count the basenodes which match fulltext
     * @param fulltext               fulltext to search in desc and name
     * @return                       total of matching nodes
     */
    long countFulltextBaseNodes(String fulltext);
    
    /** 
     * read the basenodes which match fulltext
     * @param fulltext               fulltext to search in desc and name
     * @param sortConfig             use sort
     * @param firstResult            resultrange for pagination
     * @param maxResults             resultrange for pagination
     * @return                       List of matching nodes
     */
    List<BaseNode> findFulltextBaseNodeEntries(String fulltext, 
                    String sortConfig, int firstResult, int maxResults);
    
    /** 
     * count the basenodes which match fulltext
     * @param fulltext               fulltext to search in desc and name
     * @param rootSysUID             sysUID of the rootElement
     * @param searchOptions          outputoptions with additional filter
     * @return                       total of matching nodes
     */
    long countExtendedSearchBaseNodes(final String fulltext, final String rootSysUID, final SearchOptions searchOptions);

    /** 
     * read the basenodes which match fulltext
     * @param fulltext               fulltext to search in desc and name
     * @param rootSysUID             sysUID of the rootElement
     * @param searchOptions          outputoptions with additional filter
     * @param sortConfig             use sort
     * @param firstResult            resultrange for pagination
     * @param maxResults             resultrange for pagination
     * @return                       List of matching nodes
     */
    List<BaseNode> findExtendedSearchBaseNodeEntries(final String fulltext, final String rootSysUID, final SearchOptions searchOptions,
                    final String sortConfig, final int firstResult, final int maxResults);

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
     * @throws Exception             ioExceptions possible
     */
    void saveChildNodesToDB(final BaseNode basenode, final int pRecursionLevel, 
                            final boolean flgForceMerge) throws Exception;

    /** 
     * remove the children from database recursivly
     * @param basenode               parentnode
     */
    void removeChildNodesFromDB(final BaseNode basenode);
}
