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
package de.yaio.app.cli.batch;

import de.yaio.app.cli.YaioCmdLineJob;
import de.yaio.app.cli.YaioCmdLineHelper;
import de.yaio.app.system.YaioFlyway;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

/**
 * job to do flyway on db
 * 
 */
public class JobYaioFlyway extends YaioCmdLineJob {

    private static final Logger LOGGER =
        Logger.getLogger(JobYaioFlyway.class);

    /**
     * job to do flyway on db
     * @param args                   the command line arguments
     */
    public JobYaioFlyway(final String[] args) {
        super(args);
    }

    @Override
    protected Options addAvailiableCmdLineOptions() throws Exception {
        Options availiableCmdLineOptions = 
                        YaioCmdLineHelper.getNewOptionsInstance();

        // add default-Options
        this.getCmdLineHelper().addAvailiableBaseCmdLineOptions(
                        availiableCmdLineOptions);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() throws Exception {
        System.out.println(YaioFlyway.doFlyway());
    }

    // #############
    // common functions
    // #############

    /** 
     * get the Class-logger
     * @return                       logger - the logger
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    /** 
     * Main-method to start the application
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        JobYaioFlyway me = new JobYaioFlyway(args);
        me.startJobProcessing();
    }
}
