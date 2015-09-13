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
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.OutputOptions;

/**
 * <h4>FeatureDomain:</h4>
 *     Persistence
 * <h4>FeatureDescription:</h4>
 *     dbservices for BaseNodes
 * 
 * @package de.yaio.core.node
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@RooService(domainTypes = { de.yaio.core.node.BaseNode.class })
public interface BaseNodeDBService {

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     recalc and merge the node and its parents recursively
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>List - list of the recalced and saved parenthierarchy
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice 
     * @param node - the node to recalc and merge
     * @return List - list of the recalced and saved parenthierarchy
     * @throws Exception - io/DB-Exceptions possible
     */
    List<BaseNode> updateMeAndMyParents(BaseNode node) throws Exception;

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
    List<BaseNode> findChildNodes(String sysUID);

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
    long countFulltextBaseNodes(String fulltext);
    
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
     * @param sortConfig - use sort
     * @param firstResult - resultrange for pagination
     * @param maxResults - resultrange for pagination
     * @return List of matching nodes
     */
    List<BaseNode> findFulltextBaseNodeEntries(String fulltext, 
                    String sortConfig, int firstResult, int maxResults);
    
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
     * @param oOptions - outputoptions with additional filter
     * @return total of matching nodes
     */
    long countExtendedSearchBaseNodes(final String fulltext, final OutputOptions oOptions);

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
     * @param oOptions - outputoptions with additional filter
     * @param sortConfig - use sort
     * @param firstResult - resultrange for pagination
     * @param maxResults - resultrange for pagination
     * @return List of matching nodes
     */
    List<BaseNode> findExtendedSearchBaseNodeEntries(final String fulltext, final OutputOptions oOptions,
                    final String sortConfig, final int firstResult, final int maxResults);

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
    List<BaseNode> findSymLinkBaseNode(String symLinkRef);

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     delete all nodes an create a new Masternode
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue BaseNode - the new BaseNode
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence JPA
     * @return new Masternode
     */
    BaseNode resetYaio();

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     saves the children to database (childNodes) recursivly<br>
     *     check if entityManger contains objects<br>
     *     if not: do persist
     *     if yes or flgForceMerge is set: do merge
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>saves memberfields childNodes to database
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @param basenode - parentnode
     * @param pRecursionLevel - how many recursion-level will be saved to DB (0 = only my children)
     * @param flgForceMerge - force merge not persists
     * @throws Exception - ioExceptions possible
     */
    void saveChildNodesToDB(final BaseNode basenode, final int pRecursionLevel, 
                            final boolean flgForceMerge) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     remove the children from database recursivly
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>deletes all children with this,.getSysUID recursivly from db
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @param basenode - parentnode
     */
    void removeChildNodesFromDB(final BaseNode basenode);
}
