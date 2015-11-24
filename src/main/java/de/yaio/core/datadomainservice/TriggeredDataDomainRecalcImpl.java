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
package de.yaio.core.datadomainservice;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.yaio.core.dbservice.DBFilter;
import de.yaio.core.node.BaseNode;

/** 
 * baseservice for businesslogic of datadomains
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public abstract class TriggeredDataDomainRecalcImpl implements TriggeredDataDomainRecalc {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(TriggeredDataDomainRecalcImpl.class);

    @Override
    public void doSearchAndTrigger() throws Exception {
        int maxPerRun = 500;

        // repeat until no more entries available
        boolean flgAllDone = false;
        while (!flgAllDone) {
            // create query and read
            TypedQuery<BaseNode> query = (TypedQuery<BaseNode>)this.createQuery();
            query.setFirstResult(1);
            query.setMaxResults(500);
            List<BaseNode> results = query.getResultList();
            
            // ietarte all node
            for (BaseNode node : results) {
                this.doRecalcWhenTriggered(node);
            }
            
            // stop if no more entries available
            if (results.size() < maxPerRun) {
                flgAllDone = true;
            }
        }
    }

    protected TypedQuery<?> createQuery() {
        // setup class
        Class<?> resClass = BaseNode.class;

        // create filter
        List<DBFilter> dbFilters = this.getDBTriggerFilter();
        String filter = "";
        if (dbFilters.size() > 0) {
            List<String>sqlList = new ArrayList<String>();
            for (DBFilter dbFilter : dbFilters) {
                sqlList.add(dbFilter.getSql());
            }
            filter = " where " + StringUtils.join(sqlList, " and ");
        }

        // create sort
        String sort = "";

        // setup select
        String select = "SELECT o FROM BaseNode o"
                        + filter;
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
