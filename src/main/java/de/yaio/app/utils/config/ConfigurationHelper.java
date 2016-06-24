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
package de.yaio.app.utils.config;

import de.yaio.app.utils.CmdLineHelper;
import org.apache.log4j.Logger;

import java.util.Properties;


public abstract class ConfigurationHelper {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationHelper.class);

    protected ConfigurationHelper() {
    }

    public Configuration initConfiguration() throws Exception {
        return initConfiguration(CmdLineHelper.getInstance());
    }

    public Configuration initConfiguration(final CmdLineHelper cmdLineHelper) throws Exception {
        String configFile = cmdLineHelper.getConfigFile();
        return initConfiguration(cmdLineHelper, configFile);
    }

    public Configuration initConfiguration(final CmdLineHelper cmdLineHelper, final String configFile) throws Exception {
        Properties props = cmdLineHelper.readProperties(configFile);
        return initConfiguration(cmdLineHelper, props);
    }

    public Configuration initConfiguration(final CmdLineHelper cmdLineHelper, final Properties props) throws Exception {
        Configuration localConfiguration = getConfigurationInstance();
        localConfiguration.putArgs(cmdLineHelper.getCommandLine().getArgs());
        localConfiguration.putCliOptions(cmdLineHelper.getCommandLine().getOptions());
        localConfiguration.putProperties(props);
        initCalcedProperties(cmdLineHelper);

        return localConfiguration;
    }

    public abstract Configuration getConfigurationInstance() throws Exception;

    protected void initCalcedProperties(final CmdLineHelper cmdLineHelper) throws Exception {
    }
}
