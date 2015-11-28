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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.yaio.extension.dms.services.ResIndexDataService;

/** 
 * job to index the file/url-documents
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.extension.dms
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Component
public class JobDmsIndexer {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(JobDmsIndexer.class);
    
    @Autowired
    private ResIndexDataService resIndexService;

    @Scheduled(
               fixedDelayString = "${yaio.dms-client.jobs.indexer.fixedDelay}",
//               fixedRateString = "${yaio.dms-client.jobs.indexer.fixedRate}",
               initialDelayString = "${yaio.dms-client.jobs.indexer.initialDelay}"
//               cron = "${yaio.dms-client.jobs.indexer.cron}"
               )
    public void doSearchAndTrigger() throws Exception {
        LOGGER.info("start job");
        resIndexService.doSearchAndTrigger();
        LOGGER.info("end job");
    }
}
