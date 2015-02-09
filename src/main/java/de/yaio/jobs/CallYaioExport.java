/**
 * <h4>FeatureDomain:</h4>
 *     Collaboration
 *
 * <h4>FeatureDescription:</h4>
 *     software for projectmanagement and documentation
 * 
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.jobs;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import de.yaio.app.CallYaioInstance;
import de.yaio.app.Configurator;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     job to call yaio-instance to export nodes from db
 * 
 * @package de.yaio.jobs
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class CallYaioExport extends CallYaioInstance {

    private static final Logger LOGGER =
        Logger.getLogger(CallYaioExport.class);

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     job to to export nodes from db
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the application
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     * @param args the command line arguments
     */
    public CallYaioExport(String[] args) {
        super(args);
    }

    @Override
    protected Options addAvailiableCmdLineOptions() throws Throwable {
        Options availiableCmdLineOptions = super.addAvailiableCmdLineOptions();
        
        // endpoint for export
        Option formatOption = new Option(null, "format", true,
                "exportformat (endpoint like wiki,csv,html ...)");
        formatOption.setRequired(true);
        availiableCmdLineOptions.addOption(formatOption);

        // sysuid for export
        Option sysuidOption = new Option(null, "sysuid", true,
                "SysUID of Masternode to export");
        sysuidOption.setRequired(true);
        availiableCmdLineOptions.addOption(sysuidOption);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        
        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() throws Throwable {
        // get options
        String sysUID = Configurator.getInstance().getCommandLine().getOptionValue("sysuid");
        String format = Configurator.getInstance().getCommandLine().getOptionValue("format");
        
        // call url
        StringBuffer result = this.callGetUrl("/exports/" + format + "/"+ sysUID, null);
        
        System.out.println(result);
    }

    // #############
    // common functions
    // #############

    /**
     * <h4>FeatureDomain:</h4>
     *     Logging
     * <h4>FeatureDescription:</h4>
     *     get the Class-logger
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Logger - use it !!!!
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Logging
     * @return logger - the logger
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     CLI
     * <h4>FeatureDescription:</h4>
     *     Main-method to start the application
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the application
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CallYaioExport me = new CallYaioExport(args);
        me.startJobProcessing();
    }
}
