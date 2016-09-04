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

import java.util.Date;
import java.util.List;

/** 
 * dbservices for BaseNodes
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public interface BaseNodeStatisticsDBService {
    enum IstPlanPerDayStatisticQueryType {
        PLAN,
        IST
    }

    enum StartEndPerDayStatisticQueryType {
        START,
        ENDE
    }

    /**
     * calc the effort (planned or ist) of tasks matching the filter between start/end and return for every day
     * a record with date, count of tasks running/planned and sum of effort per day on this date
     * @param type                   type of effort to read IST/PLAN
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param pfulltext              filter: optional fulltext the tasks must contain
     * @param rootSysUID             filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       List of List(NR, DATE, TASKCOUNT, EFFORTSUM)
     */
    List<List> calcAufwandPerDayStatistic(final IstPlanPerDayStatisticQueryType type,
                                          final Date start, final Date end, final String pfulltext,
                                          final String rootSysUID, final SearchOptions searchOptions);


    /**
     * read count of planned/real start/end of tasks matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param type                   type of event to read IST/PLAN
     * @param timeType               type of event to read START/END
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param pfulltext              filter: optional fulltext the tasks must contain
     * @param rootSysUID             filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       List of List(NR, DATE, TASKCOUNT)
     */
    List<List> calcCountPerDayStatistic(final IstPlanPerDayStatisticQueryType type,
                                        final StartEndPerDayStatisticQueryType timeType,
                                        final Date start, final Date end, final String pfulltext,
                                        final String rootSysUID, final SearchOptions searchOptions);

    /**
     * read count of tasks done matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param pfulltext              filter: optional fulltext the tasks must contain
     * @param rootSysUID             filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       List of List(NR, DATE, TASKCOUNT)
     */
    List<List> calcDonePerDayStatistic(final Date start, final Date end, final String pfulltext,
                                       final String rootSysUID, final SearchOptions searchOptions);


    /**
     * read count of planned/real tasks matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param type                   type of event to read IST/PLAN
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param pfulltext              filter: optional fulltext the tasks must contain
     * @param rootSysUID             filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       List of List(NR, DATE, TASKCOUNT)
     */
    List<List> calcRunningPerDayStatistic(final IstPlanPerDayStatisticQueryType type,
                                          final Date start, final Date end, final String pfulltext,
                                          final String rootSysUID, final SearchOptions searchOptions);
}
