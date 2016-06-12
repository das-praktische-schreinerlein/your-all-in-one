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
package de.yaio.utils.db;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/** 
 * factory to create common dbfilter
 * 
 * @FeatureDomain                Persistence
 * @package                      de.yaio.utils.db
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class DBFilterFactory {
    public static List<DBFilter> createDateFilter(final String filterName, final String fieldName, final Date value, final String command) {
        List<DBFilter> dbFilters = new ArrayList<>();
        if (value != null) {
            String sql = fieldName + " " + command + " :createDateFilter" + filterName;
            List<DBFilter.Parameter> parameters = new ArrayList<>();
            parameters.add(new DBFilter.Parameter("createDateFilter"  + filterName, value));
            dbFilters.add(new DBFilter("(" + sql + ")", parameters));
        }
        return dbFilters;
    }

    public static List<DBFilter> createIsNullFilter(final String filterName, final String fieldName, final String value) {
        List<DBFilter> dbFilters = new ArrayList<>();
        if ("true".equalsIgnoreCase(value)) {
            String sql = fieldName + " is null ";
            List<DBFilter.Parameter> parameters = new ArrayList<>();
            dbFilters.add(new DBFilter("(" + sql + ")", parameters));
        } else if ("false".equalsIgnoreCase(value)) {
            String sql = fieldName + " is not null ";
            List<DBFilter.Parameter> parameters = new ArrayList<>();
            dbFilters.add(new DBFilter("(" + sql + ")", parameters));
        }

        return dbFilters;
    }

    public static List<DBFilter> createMapStringContainsFilter(final String fieldName, final Set<String> values) {
        int idx = 0;
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();
        if (values != null) {
            List<String> sqlList = new ArrayList<String>();
            List<DBFilter.Parameter> parameters = new ArrayList<DBFilter.Parameter>();
            for (String value : values) {
                sqlList.add("(" + "lower(" + fieldName + ") like lower(:mapStringContainsFilter" + fieldName + idx + ")" + ")");
                parameters.add(new DBFilter.Parameter("mapStringContainsFilter" + fieldName + idx, "%"+ value + "%"));
                idx++;
            }
            dbFilters.add(new DBFilter("(" + StringUtils.join(sqlList, " or ") + ")", parameters));
        }
        return dbFilters;
    }

    public static List<DBFilter> createMapStringFilter(final String fieldName, final Set<String> values) {
        int idx = 0;
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();
        if (values != null) {
            List<String> sqlList = new ArrayList<String>();
            List<DBFilter.Parameter> parameters = new ArrayList<DBFilter.Parameter>();
            for (String value : values) {
                sqlList.add("(" + "lower(" + fieldName + ") = lower(:mapStringFilter" + fieldName + idx + ")" + ")");
                parameters.add(new DBFilter.Parameter("mapStringFilter" + fieldName + idx, value));
                idx++;
            }
            dbFilters.add(new DBFilter("(" + StringUtils.join(sqlList, " or ") + ")", parameters));
        }
        return dbFilters;
    }

    public static List<DBFilter> createMapIntFilter(final String fieldName, final Set<Integer> values) {
        int idx = 0;
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();
        if (values != null) {
            List<String> sqlList = new ArrayList<String>();
            List<DBFilter.Parameter> parameters = new ArrayList<DBFilter.Parameter>();
            for (Integer value : values) {
                sqlList.add("(" + fieldName + " = :mapIntFilter" + fieldName + idx + ")");
                parameters.add(new DBFilter.Parameter("mapIntFilter" + fieldName + idx, value));
                idx++;
            }
            dbFilters.add(new DBFilter("(" + StringUtils.join(sqlList, " or ") + ")", parameters));
        }
        return dbFilters;
    }

}
