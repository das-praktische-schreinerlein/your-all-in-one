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
package de.yaio.app.jobs.cron;

import de.yaio.app.core.datadomainservice.WorkflowRecalcDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 
 * job to recalc workflowData
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.jobs
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Component
public class JobWorkflowRecalcer {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(JobWorkflowRecalcer.class);
    
    @Autowired
    private WorkflowRecalcDataService workflowDataService;

    @Scheduled(
               fixedDelayString = "${yaio.jobs.workflowRecalcer.fixedDelay}",
//               fixedRateString = "${yaio.jobs.workflowRecalcer.fixedRate}",
               initialDelayString = "${yaio.jobs.workflowRecalcer.initialDelay}"
//               cron = "${yaio.jobs.workflowRecalcer.cron}"
               )
    public void doSearchAndTrigger() throws Exception {
        LOGGER.info("start job");
        workflowDataService.doSearchAndTrigger();
        LOGGER.info("end job");
    }
}
