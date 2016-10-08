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
package de.yaio.app.extension.autoformatter.jobs;

import de.yaio.app.extension.autoformatter.services.DescAutoFormatterService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * job to format nodeDesc
 *
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Component
@ConditionalOnProperty("yaio.autoformatter.jobs.descformatter.enable")
public class JobDescAutoFormatter {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(JobDescAutoFormatter.class);

    @Autowired
    private DescAutoFormatterService descFormatterService;

    @Scheduled(
            fixedDelayString = "${yaio.autoformatter.jobs.descformatter.fixedDelay}",
//               fixedRateString = "${yaio.autoformatter.jobs.descformatter.fixedRate}",
            initialDelayString = "${yaio.autoformatter.jobs.descformatter.initialDelay}"
//               cron = "${yaio.autoformatter.jobs.descformatter.cron}"
    )
    public void doSearchAndTrigger() {
        LOGGER.info("start job");
        descFormatterService.doSearchAndTrigger();
        LOGGER.info("end job");
    }
}
