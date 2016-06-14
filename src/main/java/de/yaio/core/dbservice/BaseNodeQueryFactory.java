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
import de.yaio.utils.db.DBFilterFactory;
import de.yaio.utils.db.DBFilterUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
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

    public enum IstPlanPerDayStatisticQueryType {
        PLAN,
        IST
    }

    public enum StartEndPerDayStatisticQueryType {
        START,
        ENDE
    }

    /**
     * create query to read tasks matching the filter
     * @param flgCount               if true select only the count else select the whole BaseNodes
     * @param pfulltext              filter: optional fulltext the tasks must contain
     * @param rootSysUID             filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       query generated for the parameters
     */
    public static TypedQuery<?> createExtendedSearchQuery(final boolean flgCount, final String pfulltext,
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
        LOGGER.info(select);

        TypedQuery<?> query = BaseNode.entityManager().createQuery(select, resClass);

        DBFilterUtils.addFilterParameterToQuery(query, dbFilters);

        return query;
    }

    /**
     * create query to calc the effort (planned or ist) of tasks matching the filter between start/end and return
     * for every day a record with date, count of tasks running/planned and sum of effort per day on this date
     * @param type                   type of effort to read IST/PLAN
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param pfulltext              filter: optional fulltext the tasks must contain
     * @param rootSysUID             filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       query generated for the parameters
     */
    public static Query createAufwandPerDayStatisticQuery(final IstPlanPerDayStatisticQueryType type, final Date start,
                                                          final Date end, final String pfulltext,
                                                          final String rootSysUID, final SearchOptions searchOptions) {
        // create filter
        List<DBFilter> dbFilters = BaseNodeFilterFactory.createCommonFilter(pfulltext, rootSysUID, searchOptions);
        String filter = DBFilterUtils.generateFilterSql(dbFilters);
        //filter += (StringUtils.isEmpty(filter) ? " where " : " or ") + " D is not null ";

        String prefix = (type == IstPlanPerDayStatisticQueryType.IST ? "IST" : "PLAN");
        String select = "SELECT" +
                "  I, D, count(" + prefix + "_AUFWAND)," +
                "  sum(" + prefix + "_AUFWAND/(DATEDIFF ( 'day', trunc(" + prefix + "_START), trunc(" + prefix + "_ENDE))+1))" +
                " FROM" +
                "  BASE_NODE o" +
                "  RIGHT OUTER JOIN" +
                "  UNNEST(SEQUENCE_ARRAY(trunc(DATE '" + DateFormatUtils.format(start, "yyyy-MM-dd") + "'), " +
                "                        trunc(DATE '" + DateFormatUtils.format(end, "yyyy-MM-dd") + "'), 1 DAY)) WITH ORDINALITY AS T(D, I)" +
                "    ON ((trunc(" + prefix + "_START) <= trunc(D)) and (trunc(" + prefix + "_ENDE) >= trunc(D)))" +
                filter +
                " group by I, D" +
                " ORDER BY D";
        LOGGER.info(select);

        Query query = BaseNode.entityManager().createNativeQuery(select);
        query.setFirstResult(0);
        query.setMaxResults(9999);

        DBFilterUtils.addFilterParameterToQuery(query, dbFilters);

        return query;
    }

    /**
     * create query to read count of tasks done matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param pfulltext              filter: optional fulltext the tasks must contain
     * @param rootSysUID             filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       query generated for the parameters
     */
    public static Query createDonePerDayStatisticQuery(final Date start, final Date end, final String pfulltext,
                                                       final String rootSysUID, final SearchOptions searchOptions) {
        List<DBFilter> dbFilters = BaseNodeFilterFactory.createCommonFilter(pfulltext, rootSysUID, searchOptions);
        String filter = DBFilterUtils.generateFilterSql(dbFilters);
        //filter += (StringUtils.isEmpty(filter) ? " where " : " or ") + " D is not null ";

        String select = "SELECT" +
                "  I, D, count(IST_ENDE)" +
                " FROM" +
                "  BASE_NODE o" +
                "  RIGHT OUTER JOIN" +
                "  UNNEST(SEQUENCE_ARRAY(trunc(DATE '" + DateFormatUtils.format(start, "yyyy-MM-dd") + "'), " +
                "                        trunc(DATE '" + DateFormatUtils.format(end, "yyyy-MM-dd") + "'), 1 DAY)) WITH ORDINALITY AS T(D, I)" +
                "    ON (trunc(IST_ENDE) = trunc(D) and ist_stand = 100)" +
                filter +
                " group by I, D" +
                " ORDER BY D";
        LOGGER.info(select);

        Query query = BaseNode.entityManager().createNativeQuery(select);
        query.setFirstResult(0);
        query.setMaxResults(9999);

        DBFilterUtils.addFilterParameterToQuery(query, dbFilters);

        return query;
    }

    /**
     * create a query to read count of planned/real tasks matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param type                   type of event to read IST/PLAN
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param pfulltext              filter: optional fulltext the tasks must contain
     * @param rootSysUID             filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       query generated for the parameters
     */
    public static Query createRunningPerDayStatisticQuery(final IstPlanPerDayStatisticQueryType type, final Date start,
                                                        final Date end, final String pfulltext,
                                                        final String rootSysUID, final SearchOptions searchOptions) {
        List<DBFilter> dbFilters = BaseNodeFilterFactory.createCommonFilter(pfulltext, rootSysUID, searchOptions);
        String filter = DBFilterUtils.generateFilterSql(dbFilters);
        //filter += (StringUtils.isEmpty(filter) ? " where " : " or ") + " D is not null ";

        String prefix = (type == IstPlanPerDayStatisticQueryType.IST ? "IST" : "PLAN");
        String fieldNameStart = prefix + "_START";
        String fieldNameEnde = prefix + "_ENDE";
        String select = "SELECT" +
                "  I, D, count(" + fieldNameStart + ")" +
                " FROM" +
                "  BASE_NODE o" +
                "  RIGHT OUTER JOIN" +
                "  UNNEST(SEQUENCE_ARRAY(trunc(DATE '" + DateFormatUtils.format(start, "yyyy-MM-dd") + "'), " +
                "                        trunc(DATE '" + DateFormatUtils.format(end, "yyyy-MM-dd") + "'), 1 DAY)) WITH ORDINALITY AS T(D, I)" +
                "    ON (trunc(" + fieldNameStart + ") <= trunc(D) and trunc(" + fieldNameEnde + ") >= trunc(D))" +
                filter +
                " group by I, D" +
                " ORDER BY D";
        LOGGER.info(select);

        Query query = BaseNode.entityManager().createNativeQuery(select);
        query.setFirstResult(0);
        query.setMaxResults(9999);

        DBFilterUtils.addFilterParameterToQuery(query, dbFilters);

        return query;
    }


    /**
     * read count of planned/real start/end tasks matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param type                   type of event to read IST/PLAN
     * @param timeType               type of event to read START/END
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param pfulltext              filter: optional fulltext the tasks must contain
     * @param rootSysUID             filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       query generated for the parameters
     */
    public static Query createCountPerDayStatisticQuery(final IstPlanPerDayStatisticQueryType type,
                                                        final StartEndPerDayStatisticQueryType timeType, final Date start,
                                                        final Date end, final String pfulltext,
                                                        final String rootSysUID, final SearchOptions searchOptions) {
        List<DBFilter> dbFilters = BaseNodeFilterFactory.createCommonFilter(pfulltext, rootSysUID, searchOptions);
        String filter = DBFilterUtils.generateFilterSql(dbFilters);
        //filter += (StringUtils.isEmpty(filter) ? " where " : " or ") + " D is not null ";

        String prefix = (type == IstPlanPerDayStatisticQueryType.IST ? "IST" : "PLAN");
        String suffix = (timeType == StartEndPerDayStatisticQueryType.START ? "START" : "ENDE");
        String fieldName = prefix + "_" + suffix;
        String select = "SELECT" +
                "  I, D, count(" + fieldName + ")" +
                " FROM" +
                "  BASE_NODE o" +
                "  RIGHT OUTER JOIN" +
                "  UNNEST(SEQUENCE_ARRAY(trunc(DATE '" + DateFormatUtils.format(start, "yyyy-MM-dd") + "'), " +
                "                        trunc(DATE '" + DateFormatUtils.format(end, "yyyy-MM-dd") + "'), 1 DAY)) WITH ORDINALITY AS T(D, I)" +
                "    ON (trunc(" + fieldName + ") = trunc(D))" +
                filter +
                " group by I, D" +
                " ORDER BY D";
        LOGGER.info(select);

        Query query = BaseNode.entityManager().createNativeQuery(select);
        query.setFirstResult(0);
        query.setMaxResults(9999);

        DBFilterUtils.addFilterParameterToQuery(query, dbFilters);

        return query;
    }
}
