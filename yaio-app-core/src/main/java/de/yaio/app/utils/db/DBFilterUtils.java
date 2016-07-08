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
package de.yaio.app.utils.db;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/** 
 * utils to create common dbfilter
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public class DBFilterUtils {
    /**
     * create filter-sql-part of form "where {dbFilters}" for the dbFilters  - joined with "and"
     * @param dbFilters             filters to generate sql from
     * @return                      generated sql
     */
    public static String generateFilterSql(List<DBFilter> dbFilters) {
        String filter = "";
        if (dbFilters.size() > 0) {
            List<String>sqlList = new ArrayList<>();
            for (DBFilter dbFilter : dbFilters) {
                sqlList.add(dbFilter.getSql());
            }
            filter = " where " + StringUtils.join(sqlList, " and ");
        }
        return filter;
    }

    /**
     * add the parameters defined in dbFilters to the query
     * @param query                 the query to add the parameter
     * @param dbFilters             filters to get parametrs from
     */
    public static void addFilterParameterToQuery(Query query, List<DBFilter> dbFilters) {
        if (dbFilters.size() > 0) {
            for (DBFilter dbFilter : dbFilters) {
                for (DBFilter.Parameter parameter : dbFilter.getParameters()) {
                    query.setParameter(parameter.getName(), parameter.getValue());
                }
            }
        }
    }
}
