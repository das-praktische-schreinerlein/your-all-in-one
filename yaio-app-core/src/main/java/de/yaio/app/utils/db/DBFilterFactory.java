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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/** 
 * factory to create common dbfilter
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public class DBFilterFactory {

    /**
     * create a date-filter of form "{fieldName} {command} :createDateFilter{filterName}"
     * @param filterName            name of the filter as suffix for parameters
     * @param fieldName             name of the dbfield to filter
     * @param value                 value of the dbfilter (used as parameter)
     * @param command               command to use (=, <, >, <=, >=, like)
     * @return                      a dbfilter
     */
    public static List<DBFilter> createDateFilter(final String filterName, final String fieldName,
                                                  final Date value, final String command) {
        List<DBFilter> dbFilters = new ArrayList<>();
        if (value != null) {
            String sql = fieldName + " " + command + " :createDateFilter" + filterName;
            List<DBFilter.Parameter> parameters = new ArrayList<>();
            parameters.add(new DBFilter.Parameter("createDateFilter"  + filterName, value));
            dbFilters.add(new DBFilter("(" + sql + ")", parameters));
        }
        return dbFilters;
    }

    /**
     * create a isnull-filter of form "{fieldName} is null" if value=true or "{fieldName} is not null" if value=false
     * @param filterName            name of the filter as suffix for parameters
     * @param fieldName             name of the dbfield to filter
     * @param value                 value of the dbfilter (true, false)
     * @return                      a dbfilter
     */
    public static List<DBFilter> createIsNullFilter(final String filterName, final String fieldName,
                                                    final String value) {
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

    /**
     * create or-joined string-filters for every value "lower({fieldName}) like lower(:mapStringContainsFilter{fieldName}{idx})"
     * @param fieldName             name of the dbfield to filter
     * @param values                value of the dbfilter (used as parameter)
     * @return                      a dbfilter
     */
    public static List<DBFilter> createMapStringContainsFilter(final String fieldName, final Set<String> values) {
        return DBFilterFactory.createMapStringLikeFilter(fieldName, values, true);
    }

    /**
     * create or-joined string-filters for every value "lower({fieldName}) like lower(:mapStringContainsFilter{fieldName}{idx})"
     * @param fieldName             name of the dbfield to filter
     * @param values                value of the dbfilter (used as parameter)
     * @param flgContains           if true add % infront and after value
     * @return                      a dbfilter
     */
    public static List<DBFilter> createMapStringLikeFilter(final String fieldName, final Set<String> values,
                                                           final boolean flgContains) {
        int idx = 0;
        List<DBFilter> dbFilters = new ArrayList<>();
        if (values != null) {
            List<String> sqlList = new ArrayList<>();
            List<DBFilter.Parameter> parameters = new ArrayList<>();
            for (String value : values) {
                String valueStr = value;
                String filterName = "createMapStringLikeFilter";
                if (flgContains) {
                    valueStr = "%" + valueStr + "%";
                    filterName = "createMapStringContainsFilter";
                }
                sqlList.add("(" + "lower(" + fieldName + ") like " +
                        "lower(:" + filterName + fieldName + idx + ")" + ")");
                parameters.add(new DBFilter.Parameter(filterName + fieldName + idx, valueStr));
                idx++;
            }
            dbFilters.add(new DBFilter("(" + StringUtils.join(sqlList, " or ") + ")", parameters));
        }
        return dbFilters;
    }

    public static List<DBFilter> createStringContainsFilter(final String filterName, final String fieldName,
                                                            final String value) {
        List<DBFilter> dbFilters = new ArrayList<>();
        if (!StringUtils.isEmpty(value)) {
            String sql = "(" + "lower(" + fieldName + ") like " +
                        "lower(:" + filterName + fieldName + ")" + ")";
            List<DBFilter.Parameter> parameters = new ArrayList<>();
            parameters.add(new DBFilter.Parameter(filterName + fieldName, "%"+ value + "%"));
            dbFilters.add(new DBFilter(sql, parameters));
        }
        return dbFilters;
    }

    /**
     * create or-joined string-filters for every value "lower({fieldName}) = lower(:mapStringFilter{fieldName}{idx})"
     * @param fieldName             name of the dbfield to filter
     * @param values                value of the dbfilter (used as parameter)
     * @return                      a dbfilter
     */
    public static List<DBFilter> createMapStringFilter(final String fieldName, final Set<String> values) {
        int idx = 0;
        List<DBFilter> dbFilters = new ArrayList<>();
        if (values != null) {
            List<String> sqlList = new ArrayList<>();
            List<DBFilter.Parameter> parameters = new ArrayList<>();
            for (String value : values) {
                sqlList.add("(" + "lower(" + fieldName + ") = lower(:mapStringFilter" + fieldName + idx + ")" + ")");
                parameters.add(new DBFilter.Parameter("mapStringFilter" + fieldName + idx, value));
                idx++;
            }
            dbFilters.add(new DBFilter("(" + StringUtils.join(sqlList, " or ") + ")", parameters));
        }
        return dbFilters;
    }

    /**
     * create or-joined int-filters for every value "{fieldName} = :mapIntFilter{fieldName}{idx}"
     * @param fieldName             name of the dbfield to filter
     * @param values                value of the dbfilter (used as parameter)
     * @return                      a dbfilter
     */
    public static List<DBFilter> createMapIntFilter(final String fieldName, final Set<Integer> values) {
        int idx = 0;
        List<DBFilter> dbFilters = new ArrayList<>();
        if (values != null) {
            List<String> sqlList = new ArrayList<>();
            List<DBFilter.Parameter> parameters = new ArrayList<>();
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
