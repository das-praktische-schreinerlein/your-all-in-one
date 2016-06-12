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

import de.yaio.core.node.BaseNode;
import de.yaio.utils.db.DBFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/** 
 * factory to create queries for BaseNode
 * 
 * @FeatureDomain                Persistence
 * @package                      de.yaio.core.dbservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class BaseNodeQueryFactory {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(BaseNodeQueryFactory.class);

    public static TypedQuery<?> createFulltextQuery(final boolean flgCount, final String pfulltext,
                                                final String sortConfig) {
        return createExtendedSearchQuery(flgCount, pfulltext, null, null, sortConfig);
    }

    public static TypedQuery<?> createExtendedSearchQuery(final boolean flgCount, final String pfulltext,
                                                      final String rootSysUID, final SearchOptions searchOptions,
                                                      final String sortConfig) {
        // setup class
        Class<?> resClass = BaseNode.class;
        if (flgCount) {
            resClass = Long.class;
        }

        // create filter
        List<DBFilter> dbFilters = new ArrayList<>();
        dbFilters.addAll(BaseNodeFilterFactory.createRootSysUIDFilter(rootSysUID));
        dbFilters.addAll(BaseNodeFilterFactory.createFulltextFilter(pfulltext));
        dbFilters.addAll(BaseNodeFilterFactory.createNotNodePraefixFilter(searchOptions.getStrNotNodePraefix()));
        dbFilters.addAll(BaseNodeFilterFactory.createSearchOptionsFilter(searchOptions));
        dbFilters.addAll(BaseNodeFilterFactory.createConcreteTodosOnlyFilter(searchOptions.getFlgConcreteToDosOnly()));
        String filter = "";
        if (dbFilters.size() > 0) {
            List<String>sqlList = new ArrayList<>();
            for (DBFilter dbFilter : dbFilters) {
                sqlList.add(dbFilter.getSql());
            }
            filter = " where " + StringUtils.join(sqlList, " and ");
        }

        // create sort
        String sort = BaseNodeSortFactory.createSort(sortConfig);

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
}
