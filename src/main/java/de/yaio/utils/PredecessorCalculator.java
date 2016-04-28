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
package de.yaio.utils;

import de.yaio.core.datadomain.PlanDependencieData.DurationMeasure;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

/** 
 * Utils for calculating predecessor
 * @FeatureDomain                Utils
 * @package                      de.yaio.utils
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     Utils
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class PredecessorCalculator {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(PredecessorCalculator.class);
    

    public static int mapDurationMatureToCalendarCodes(final DurationMeasure myDurationMeasure) {
        int measure = Calendar.DAY_OF_MONTH;
        if (myDurationMeasure == null) {
            // default
            measure = Calendar.DAY_OF_MONTH;
        } else if (myDurationMeasure == DurationMeasure.m) {
            // month
            measure = Calendar.MONTH;
        } else if (myDurationMeasure == DurationMeasure.w) {
            // week
            measure = Calendar.WEEK_OF_MONTH;
        } else if (myDurationMeasure == DurationMeasure.d) {
            // day
            measure = Calendar.DAY_OF_MONTH;
        } else if (myDurationMeasure == DurationMeasure.h) {
            // hour
            measure = Calendar.HOUR_OF_DAY;
        } else if (myDurationMeasure == DurationMeasure.min) {
            // hour
            measure = Calendar.MINUTE;
        }
        
        return measure;
    }
    
    public static Date addDurationToDate(final Date baseDate, final boolean flgAdd, 
                                         final Integer duration, final DurationMeasure myDurationMeasure) {
        Date date = baseDate;
        if (baseDate != null && duration != null && duration.intValue() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int measure = PredecessorCalculator.mapDurationMatureToCalendarCodes(
                                            myDurationMeasure);
            calendar.add(measure, (flgAdd ? 1 : -1) * duration);
            date = calendar.getTime();
        }
        
        return date;
    }
}
