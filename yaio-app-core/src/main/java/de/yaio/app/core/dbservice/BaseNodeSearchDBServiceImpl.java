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
import de.yaio.app.utils.db.DBFilterUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


/** 
 * implementation of dbservices for BaseNodes
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Service
public class BaseNodeSearchDBServiceImpl implements BaseNodeSearchDBService {
    @PersistenceContext
    protected transient EntityManager entityManager;

    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(BaseNodeSearchDBServiceImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public long countExtendedSearchBaseNodes(final String fulltext, final String rootSysUID,
                                             final SearchOptions searchOptions) {
        TypedQuery<Long> query = (TypedQuery<Long>) createExtendedSearchQuery(true, fulltext,
                rootSysUID, searchOptions, null);
        return query.getSingleResult();
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<BaseNode> findExtendedSearchBaseNodeEntries(final String fulltext, final String rootSysUID,
                                                            final SearchOptions searchOptions,
                                                            final String sortConfig, final int firstResult,
                                                            final int maxResults) {
        TypedQuery<BaseNode> query = (TypedQuery<BaseNode>) createExtendedSearchQuery(
                        false, fulltext, rootSysUID, searchOptions, sortConfig);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        
        return query.getResultList();
    }

    /**
     * create query to read tasks matching the filter
     * @param flgCount               if true select only the count else select the whole BaseNodes
     * @param pfulltext              filter: optional fulltext the tasks must contain
     * @param rootSysUID             filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       query generated for the parameters
     */
    public TypedQuery<?> createExtendedSearchQuery(final boolean flgCount, final String pfulltext,
                                                          final String rootSysUID, final SearchOptions searchOptions,
                                                          final String sortConfig) {
        // setup class
        Class<?> resClass = BaseNode.class;
        if (flgCount) {
            resClass = Long.class;
        }

        List<DBFilter> dbFilters = BaseNodeFilterFactory.createCommonFilter(pfulltext, rootSysUID, searchOptions);
        String filter = DBFilterUtils.generateFilterSql(dbFilters);

        String sort = BaseNodeSortFactory.createSort(sortConfig);

        String select = "SELECT o FROM BaseNode o"
                + filter
                + sort;
        if (flgCount) {
            select = "SELECT COUNT(o) FROM BaseNode o"
                    + filter;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("select with filters:" + select + " dbFilters:" + dbFilters);
        }

        TypedQuery<?> query = entityManager().createQuery(select, resClass);

        DBFilterUtils.addFilterParameterToQuery(query, dbFilters);

        return query;
    }

    protected final EntityManager entityManager() {
        EntityManager em = entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
}
