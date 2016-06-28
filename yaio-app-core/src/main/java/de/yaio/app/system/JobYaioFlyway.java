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

import de.yaio.app.config.ContextHelper;
import de.yaio.app.config.JobConfig;
import de.yaio.app.config.YaioConfigurationHelper;
import de.yaio.app.utils.CmdLineHelper;
import de.yaio.app.utils.CmdLineJob;
import de.yaio.app.utils.config.Configuration;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

/**
 * job to do flyway on db
 * 
 */
public class JobYaioFlyway extends CmdLineJob {

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
        Options availiableCmdLineOptions = CmdLineHelper.getNewOptionsInstance();

        // add default-Options
        this.getCmdLineHelper().addAvailiableBaseCmdLineOptions(availiableCmdLineOptions);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }

    @Override
    protected void initJob() throws Exception {
        YaioConfigurationHelper configurationHelper = YaioConfigurationHelper.getInstance();
        Configuration config = configurationHelper.initConfiguration();
        config.publishProperties();

        ContextHelper.getInstance().addSpringConfig(JobConfig.class);
        ContextHelper.getInstance().getSpringApplicationContext();

        LOGGER.info("start job with args:" + config.argsAsList() +
                " options:" + config.optionsAsProperties() +
                " properties:" + config.propertiesAsProperties() +
                " contextConfigs:" + ContextHelper.getInstance().getSpringConfig());
    };

    @Override
    public void doJob() throws Exception {
        System.out.println(YaioFlyway.doFlyway());
    }

    @Override
    protected void cleanUpAfterJob() throws Exception {
        // TODO: hack to close HSLDB-connection -> Hibernate doesn't close the
        //       database and so the content is not written to file
        org.hsqldb.DatabaseManager.closeDatabases(0);
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
