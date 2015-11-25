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
package de.yaio.extension.dms.jobs;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.yaio.extension.dms.services.ResContentDataServiceImpl;

/** 
 * job to do webshots and upload to dms
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.extension.dms
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Component
public class JobUrlWebShots extends ResContentDataServiceImpl {
    
    private static JobUrlWebShots instance = new JobUrlWebShots();
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(JobUrlWebShots.class);

    /** 
     * return the main instance of this service
     * @FeatureDomain                Persistence
     * @FeatureResult                return the main instance of this service
     * @FeatureKeywords              Persistence
     * @return                       the main instance of this service
     */
    public static JobUrlWebShots getInstance() {
        return instance;
    }

    @Override
    @Scheduled(
               fixedDelayString = "${yaio.dms-client.jobs.urlwebshots.fixedDelay}",
//               fixedRateString = "${yaio.dms-client.jobs.urlwebshots.fixedRate}",
               initialDelayString = "${yaio.dms-client.jobs.urlwebshots.initialDelay}"
//               cron = "${yaio.dms-client.jobs.urlwebshots.cron}"
               )
    public void doSearchAndTrigger() throws Exception {
        LOGGER.info("start job");
        super.doSearchAndTrigger();
        LOGGER.info("end job");
    }
}
