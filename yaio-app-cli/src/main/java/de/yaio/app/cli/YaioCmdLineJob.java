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
package de.yaio.app.cli;

import de.yaio.app.cli.importer.CommonImporter;
import de.yaio.app.config.ContextHelper;
import de.yaio.app.config.YaioConfiguration;
import de.yaio.app.config.YaioConfigurationHelper;
import de.yaio.app.utils.CmdLineJob;
import de.yaio.app.utils.config.Configuration;
import org.apache.log4j.Logger;


/** 
 * baseclass for commandlinejobs
 *
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public abstract class YaioCmdLineJob extends CmdLineJob {
    // Logger
    private static final Logger LOGGER = Logger.getLogger(YaioCmdLineJob.class);

    protected YaioConfigurationHelper configurationHelper = YaioConfigurationHelper.getInstance();

    /**
     * baseclass for CommandLineJobs
     * @param args                   the command line arguments
     */
    public YaioCmdLineJob(final String[] args) {
        // set args
        super(args);
    }

    protected YaioConfiguration getConfiguration() {
        return configurationHelper.getYaioConfigurationInstance();
    }

    protected abstract void configureContext();

    protected void initJob() {
        // init configuration
        Configuration config = configurationHelper.initConfiguration();
        config.publishProperties();

        configureContext();

        LOGGER.info("start job with args:" + getConfiguration().argsAsList() +
                " options:" + getConfiguration().optionsAsProperties() +
                " properties:" + getConfiguration().propertiesAsProperties() +
                " contextConfigs:" + ContextHelper.getInstance().getSpringConfig());
    };

    protected void cleanUpAfterJob() {
        // TODO: hack to close HSLDB-connection -> Hibernate doesn't close the
        //       database and so the content is not written to file
        org.hsqldb.DatabaseManager.closeDatabases(0);
    }
}
