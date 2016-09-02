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

import java.util.List;

/** 
 * dbservices for BaseNodes
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public interface BaseNodeSearchDBService {

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

}
