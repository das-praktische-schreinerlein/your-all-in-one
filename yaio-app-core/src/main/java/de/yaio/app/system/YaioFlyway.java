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
package de.yaio.app.system;

import de.yaio.app.config.YaioConfiguration;
import de.yaio.app.utils.config.Configuration;
import de.yaio.app.utils.config.ConfigurationOption;
import org.apache.log4j.Logger;
import org.flywaydb.core.Flyway;

/** 
 * do flyway on db
 */
public class YaioFlyway {

    // Logger
    private static final Logger LOGGER = Logger.getLogger(YaioFlyway.class);


    /**
     * do flyway on db
     * @return                       result-message
     * @throws Exception             possible io/db/recalc-Exceptions
     */
    public static String doFlyway() throws Exception {
        // do flyWay before applicationContext
        Configuration config = YaioConfiguration.getInstance();
        if ("true".equalsIgnoreCase(
                ConfigurationOption.stringValueOf(config.getProperty("yaio.flyway.enabled", "false")))) {
            Flyway flyWay = new Flyway();
            flyWay.configure(config.propertiesAsProperties());
            int migrationsDone = flyWay.migrate();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("flyway: " + migrationsDone + " done");
            }
            return "flyway: " + migrationsDone + " done";
        }
        return "flyway: skipped";
    }
}
