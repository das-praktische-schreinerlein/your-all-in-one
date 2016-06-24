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
package de.yaio.app.clients;

import de.yaio.app.config.ContextHelper;
import de.yaio.app.config.YaioConfiguration;
import de.yaio.app.config.YaioConfigurationHelper;
import de.yaio.app.utils.CmdLineHelper;
import de.yaio.app.utils.CmdLineJob;
import de.yaio.app.utils.config.Configuration;
import de.yaio.app.utils.config.ConfigurationHelper;
import de.yaio.app.utils.config.ConfigurationOption;
import de.yaio.commons.data.DataUtils;
import de.yaio.commons.http.HttpUtils;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/** 
 * job to call admin-int5erface of yaio-instances
 * 
 * @FeatureDomain                Administration
 * @package                      de.yaio.app
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public abstract class CallYaioInstance extends CmdLineJob {

    private static final Logger LOGGER =
                    Logger.getLogger(CallYaioInstance.class);
                
    protected String password;
    protected String username;
    protected URL yaioInstanceUrl;

    YaioConfigurationHelper configurationHelper = YaioConfigurationHelper.getInstance();

    /** 
     * job to call yaio-instances for admin-purposes
     * @param args                   the command line arguments
     */
    public CallYaioInstance(final String[] args) {
        super(args);
    }

    @Override
    protected Options addAvailiableCmdLineOptions() throws Exception {
        Options availiableCmdLineOptions = CmdLineHelper.getNewOptionsInstance();

        // add default-Options
        Option yaioinstanceOption = new Option(null, "yaioinstance", true,
                        "Url of the yaio-instance (http://yaio-prod.local");
        yaioinstanceOption.setRequired(true);
        availiableCmdLineOptions.addOption(yaioinstanceOption);

        Option usernameOption = new Option(null, "username", true, "admin-username for login");
        usernameOption.setRequired(true);
        availiableCmdLineOptions.addOption(usernameOption);

        Option passwordOption = new Option(null, "password", true, "admin-password for login");
        passwordOption.setRequired(true);
        availiableCmdLineOptions.addOption(passwordOption);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }

    protected YaioConfiguration getConfiguration() throws Exception {
        return configurationHelper.getYaioConfigurationInstance();
    }

    @Override
    protected void initJob() throws Exception {
        // init configuration without config-file
        Configuration config = configurationHelper.initConfiguration(this.getCmdLineHelper(), new Properties());
        config.publishProperties();

        // configure common parameters
        password = ConfigurationOption.stringValueOf(this.getConfiguration().getCliOption("password"));
        username = ConfigurationOption.stringValueOf(this.getConfiguration().getCliOption("username"));
        String yaioInstance = ConfigurationOption.stringValueOf(this.getConfiguration().getCliOption("yaioinstance"));
        yaioInstanceUrl = DataUtils.extractWebUrl(yaioInstance);
        LOGGER.info("connectData:" + yaioInstanceUrl + " user:" + username + " from:" + yaioInstance);
        if (yaioInstanceUrl == null) {
            throw new IllegalArgumentException("cant parse yaioinstance:" + yaioInstance);
        }

        LOGGER.info("call instance with args:" + getConfiguration().argsAsList() +
                " options:" + getConfiguration().optionsAsProperties() +
                " properties:" + getConfiguration().propertiesAsProperties());
    }

    protected void cleanUpAfterJob() throws Exception {
    }

    /** 
     * execute GET-Request for yaio-url with params
     * @param route                  the route behind the yaioInstanceUrl to call
     * @param params                 params for the request
     * @return                       Response-Text as ByteArray
     * @throws IOException           possible Exception if Request-state <200 > 299 
     */
    protected byte[] callGetUrl(final String route, 
                                final Map<String, String> params) throws IOException {
        String url = yaioInstanceUrl.toExternalForm() + route;
        return HttpUtils.callGetUrl(url, username, password, params);
    }

    
    
    /** 
     * execute POST-Request for yaio-url with params
     * @param route                  the route behind the yaioInstanceUrl to call
     * @param params                 params for the request
     * @param fileParams             files to upload
     * @return                       Response-Text as ByteArray
     * @throws IOException           possible Exception if Request-state <200 > 299 
     */
    protected byte[] callPostUrl(final String route, 
                                 final Map<String, String> params, 
                                 final Map<String, String> fileParams) throws IOException {
        // create request
        String url = yaioInstanceUrl.toExternalForm() + route;
        return HttpUtils.callPostUrl(url, username, password, params, fileParams, null);
    }
}
