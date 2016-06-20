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
package de.yaio.app.extension.dms.jobs;

import de.yaio.app.extension.dms.services.ResContentDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
public class JobUrlWebShots {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(JobUrlWebShots.class);
    
    @Autowired
    private ResContentDataService resContentService;

    @Scheduled(
               fixedDelayString = "${yaio.dms-client.jobs.urlwebshots.fixedDelay}",
//               fixedRateString = "${yaio.dms-client.jobs.urlwebshots.fixedRate}",
               initialDelayString = "${yaio.dms-client.jobs.urlwebshots.initialDelay}"
//               cron = "${yaio.dms-client.jobs.urlwebshots.cron}"
               )
    public void doSearchAndTrigger() throws Exception {
        LOGGER.info("start job");
        resContentService.doSearchAndTrigger();
        LOGGER.info("end job");
    }
}
