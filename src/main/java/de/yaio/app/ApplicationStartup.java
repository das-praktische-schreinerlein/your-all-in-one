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
package de.yaio.app;

import de.yaio.jobs.StatDataRecalcer;
import de.yaio.jobs.SysDataRecalcer;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * do jobs on applicationStartup
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = Logger.getLogger(ApplicationStartup.class);

    /*
     * This method is called during Spring's startup.
     *
     * @param event Event raised when an ApplicationContext gets initialized or
     * refreshed.
     */
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        recalcSysData();
        recalcStatData();
    }

    /**
     * recalc the sysData on startup
     */
    protected void recalcSysData() {
        try {
            SysDataRecalcer recalcer = new SysDataRecalcer();
            String res = recalcer.recalcSysData();
            LOGGER.info("recalcing SysData done:" + res);
        } catch (Exception ex) {
            LOGGER.error("ERROR while recalcSysData on startup", ex);
        }
    }

    /**
     * recalc the statData on startup
     */
    protected void recalcStatData() {
        try {
            StatDataRecalcer recalcer = new StatDataRecalcer();
            String res = recalcer.recalcStatData();
            LOGGER.info("recalcing StatData done:" + res);
        } catch (Exception ex) {
            LOGGER.error("ERROR while recalcStatData on startup", ex);
        }
    }
}