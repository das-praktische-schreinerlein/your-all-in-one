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
package de.yaio.app.server.restcontroller;

import de.yaio.app.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.app.core.dbservice.BaseNodeQueryFactory;
import de.yaio.app.core.dbservice.SearchOptionsImpl;
import de.yaio.app.core.dbservice.BaseNodeDBService;
import de.yaio.app.server.controller.CommonApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/** 
 * the controller gor getting statistics
 *  
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    
    /** API-Version **/
    public static final String API_VERSION = "1.0.0";

    @Autowired
    private CommonApiConfig commonApiConfig;

    /**
     * calc the effort (ist) for all tasks matching the filter between start/end and return for every day
     * a record with date, count of tasks running/planned and sum of effort per day on this date
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param fulltext               filter: optional fulltext the tasks must contain
     * @param sysUID                 filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with values as List of List(NR, DATE, TASKCOUNT, EFFORTSUM)
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
                    value = "/istAufwandPerDay/{start}/{end}/{sysUID}/{fulltext}/")
    public StatisticsResponse istAufwandPerDay(@PathVariable(value = "start") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date start,
                                               @PathVariable(value = "end") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date end,
                                               @PathVariable(value = "sysUID") final String sysUID,
                                               @PathVariable(value = "fulltext") final String fulltext,
                                               @RequestBody final SearchOptionsImpl searchOptions) {
        // create default response
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        List values = baseNodeDBService.calcAufwandPerDayStatistic(BaseNodeQueryFactory.IstPlanPerDayStatisticQueryType.IST,
                start, end, fulltext, sysUID, searchOptions);
        StatisticsResponse response = new StatisticsResponse(
                "OK", "data fetched", values);

        return response;
    }

    /**
     * calc the effort (planned) for all tasks matching the filter between start/end and return for every day
     * a record with date, count of tasks running/planned and sum of effort per day on this date
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param fulltext               filter: optional fulltext the tasks must contain
     * @param sysUID                 filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with values as List of List(NR, DATE, TASKCOUNT, EFFORTSUM)
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
            value = "/planAufwandPerDay/{start}/{end}/{sysUID}/{fulltext}/")
    public StatisticsResponse planAufwandPerDay(@PathVariable(value = "start") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date start,
                                               @PathVariable(value = "end") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date end,
                                               @PathVariable(value = "sysUID") final String sysUID,
                                               @PathVariable(value = "fulltext") final String fulltext,
                                               @RequestBody final SearchOptionsImpl searchOptions) {
        // create default response
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        List values = baseNodeDBService.calcAufwandPerDayStatistic(BaseNodeQueryFactory.IstPlanPerDayStatisticQueryType.PLAN,
                start, end, fulltext, sysUID, searchOptions);
        StatisticsResponse response = new StatisticsResponse(
                "OK", "data fetched", values);

        return response;
    }

    /**
     * read count of tasks done this day matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param fulltext               filter: optional fulltext the tasks must contain
     * @param sysUID                 filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with values as List of List(NR, DATE, TASKCOUNT)
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
            value = "/istDonePerDay/{start}/{end}/{sysUID}/{fulltext}/")
    public StatisticsResponse istDonePerDay(@PathVariable(value = "start") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date start,
                                             @PathVariable(value = "end") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date end,
                                             @PathVariable(value = "sysUID") final String sysUID,
                                             @PathVariable(value = "fulltext") final String fulltext,
                                             @RequestBody final SearchOptionsImpl searchOptions) {
        // create default response
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        List values = baseNodeDBService.calcDonePerDayStatistic(start, end, fulltext, sysUID, searchOptions);
        StatisticsResponse response = new StatisticsResponse(
                "OK", "data fetched", values);

        return response;
    }

    /**
     * read count of tasks real running this day matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param fulltext               filter: optional fulltext the tasks must contain
     * @param sysUID                 filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with values as List of List(NR, DATE, TASKCOUNT)
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
            value = "/istRunningPerDay/{start}/{end}/{sysUID}/{fulltext}/")
    public StatisticsResponse istRunningPerDay(@PathVariable(value = "start") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date start,
                                            @PathVariable(value = "end") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date end,
                                            @PathVariable(value = "sysUID") final String sysUID,
                                            @PathVariable(value = "fulltext") final String fulltext,
                                            @RequestBody final SearchOptionsImpl searchOptions) {
        // create default response
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        List values = baseNodeDBService.calcRunningPerDayStatistic(BaseNodeQueryFactory.IstPlanPerDayStatisticQueryType.IST,
                start, end, fulltext, sysUID, searchOptions);
        StatisticsResponse response = new StatisticsResponse(
                "OK", "data fetched", values);

        return response;
    }

    /**
     * read count of tasks planned running this day matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param fulltext               filter: optional fulltext the tasks must contain
     * @param sysUID                 filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with values as List of List(NR, DATE, TASKCOUNT)
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
            value = "/planRunningPerDay/{start}/{end}/{sysUID}/{fulltext}/")
    public StatisticsResponse planRunningPerDay(@PathVariable(value = "start") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date start,
                                               @PathVariable(value = "end") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date end,
                                               @PathVariable(value = "sysUID") final String sysUID,
                                               @PathVariable(value = "fulltext") final String fulltext,
                                               @RequestBody final SearchOptionsImpl searchOptions) {
        // create default response
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        List values = baseNodeDBService.calcRunningPerDayStatistic(BaseNodeQueryFactory.IstPlanPerDayStatisticQueryType.PLAN,
                start, end, fulltext, sysUID, searchOptions);
        StatisticsResponse response = new StatisticsResponse(
                "OK", "data fetched", values);

        return response;
    }

    /**
     * read count of tasks real started this day matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param fulltext               filter: optional fulltext the tasks must contain
     * @param sysUID                 filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with values as List of List(NR, DATE, TASKCOUNT)
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
            value = "/istStartPerDay/{start}/{end}/{sysUID}/{fulltext}/")
    public StatisticsResponse istStartPerDay(@PathVariable(value = "start") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date start,
                                               @PathVariable(value = "end") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date end,
                                               @PathVariable(value = "sysUID") final String sysUID,
                                               @PathVariable(value = "fulltext") final String fulltext,
                                               @RequestBody final SearchOptionsImpl searchOptions) {
        // create default response
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        List values = baseNodeDBService.calcCountPerDayStatistic(BaseNodeQueryFactory.IstPlanPerDayStatisticQueryType.IST,
                BaseNodeQueryFactory.StartEndPerDayStatisticQueryType.START,
                start, end, fulltext, sysUID, searchOptions);
        StatisticsResponse response = new StatisticsResponse(
                "OK", "data fetched", values);

        return response;
    }

    /**
     * read count of tasks start planned this day matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param fulltext               filter: optional fulltext the tasks must contain
     * @param sysUID                 filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with values as List of List(NR, DATE, TASKCOUNT)
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
            value = "/planStartPerDay/{start}/{end}/{sysUID}/{fulltext}/")
    public StatisticsResponse planStartPerDay(@PathVariable(value = "start") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date start,
                                             @PathVariable(value = "end") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date end,
                                             @PathVariable(value = "sysUID") final String sysUID,
                                             @PathVariable(value = "fulltext") final String fulltext,
                                             @RequestBody final SearchOptionsImpl searchOptions) {
        // create default response
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        List values = baseNodeDBService.calcCountPerDayStatistic(BaseNodeQueryFactory.IstPlanPerDayStatisticQueryType.PLAN,
                BaseNodeQueryFactory.StartEndPerDayStatisticQueryType.START,
                start, end, fulltext, sysUID, searchOptions);
        StatisticsResponse response = new StatisticsResponse(
                "OK", "data fetched", values);

        return response;
    }

    /**
     * read count of tasks end planned this day matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param fulltext               filter: optional fulltext the tasks must contain
     * @param sysUID                 filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with values as List of List(NR, DATE, TASKCOUNT)
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
            value = "/planEndPerDay/{start}/{end}/{sysUID}/{fulltext}/")
    public StatisticsResponse planEndPerDay(@PathVariable(value = "start") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date start,
                                              @PathVariable(value = "end") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date end,
                                              @PathVariable(value = "sysUID") final String sysUID,
                                              @PathVariable(value = "fulltext") final String fulltext,
                                              @RequestBody final SearchOptionsImpl searchOptions) {
        // create default response
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        List values = baseNodeDBService.calcCountPerDayStatistic(BaseNodeQueryFactory.IstPlanPerDayStatisticQueryType.PLAN,
                BaseNodeQueryFactory.StartEndPerDayStatisticQueryType.ENDE,
                start, end, fulltext, sysUID, searchOptions);
        StatisticsResponse response = new StatisticsResponse(
                "OK", "data fetched", values);

        return response;
    }

    /**
     * read count of tasks last working this day matching the filter between start/end and return
     * a record with date and count of tasks on this date
     * @param start                  start of the interval to calc
     * @param end                    end of the interval to calc
     * @param fulltext               filter: optional fulltext the tasks must contain
     * @param sysUID                 filter: SysUID of the root element whose children have to read
     * @param searchOptions          filter: optional searchOptions
     * @return                       NodeSearchResponse (OK, FAILED, ERROR) with values as List of List(NR, DATE, TASKCOUNT)
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
            value = "/istEndPerDay/{start}/{end}/{sysUID}/{fulltext}/")
    public StatisticsResponse istEndPerDay(@PathVariable(value = "start") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date start,
                                            @PathVariable(value = "end") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) final Date end,
                                            @PathVariable(value = "sysUID") final String sysUID,
                                            @PathVariable(value = "fulltext") final String fulltext,
                                            @RequestBody final SearchOptionsImpl searchOptions) {
        // create default response
        BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
        List values = baseNodeDBService.calcCountPerDayStatistic(BaseNodeQueryFactory.IstPlanPerDayStatisticQueryType.IST,
                BaseNodeQueryFactory.StartEndPerDayStatisticQueryType.ENDE,
                start, end, fulltext, sysUID, searchOptions);
        StatisticsResponse response = new StatisticsResponse(
                "OK", "data fetched", values);

        return response;
    }
}
