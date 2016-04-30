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

import de.yaio.commons.data.DataUtils;
import de.yaio.commons.http.HttpUtils;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

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

    /** 
     * job to call yaio-instances for admin-purposes
     * @param args                   the command line arguments
     */
    public CallYaioInstance(final String[] args) {
        super(args);
    }

    @Override
    protected Options addAvailiableCmdLineOptions() throws Exception {
        Options availiableCmdLineOptions = 
                        Configurator.getNewOptionsInstance();

        // add default-Options
        Option yaioinstanceOption = new Option(null, "yaioinstance", true,
                        "Url of the yaio-instance (http://yaio-prod.local");
        yaioinstanceOption.setRequired(true);
        availiableCmdLineOptions.addOption(yaioinstanceOption);

        Option usernameOption = new Option(null, "username", true,
                        "admin-username for login");
        usernameOption.setRequired(true);
        availiableCmdLineOptions.addOption(usernameOption);

        Option passwordOption = new Option(null, "password", true,
                        "admin-password for login");
        passwordOption.setRequired(true);
        availiableCmdLineOptions.addOption(passwordOption);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }
    
    @Override
    protected void initJob() throws Exception {
        password = Configurator.getInstance().getCommandLine().getOptionValue("password");
        username = Configurator.getInstance().getCommandLine().getOptionValue("username");
        yaioInstanceUrl = DataUtils.extractWebUrl(
                        Configurator.getInstance().getCommandLine().getOptionValue("yaioinstance"));
        LOGGER.info("connectData:" + yaioInstanceUrl + " user:" + username 
                        + " from:" + Configurator.getInstance().getCommandLine().getOptionValue("yaioinstance"));
        if (yaioInstanceUrl == null) {
            throw new IllegalArgumentException("cant parse yaioinstance:" + 
                            Configurator.getInstance().getCommandLine().getOptionValue("yaioinstance"));
        }
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
