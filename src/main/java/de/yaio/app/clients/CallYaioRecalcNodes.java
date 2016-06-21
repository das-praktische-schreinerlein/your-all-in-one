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

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

/** 
 * job to call yaio-instance to recalc nodes in db
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.cli
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class CallYaioRecalcNodes extends CallYaioInstance {

    private static final Logger LOGGER =
        Logger.getLogger(CallYaioRecalcNodes.class);

    /** 
     * job to update the nodes in db
     * @param args                   the command line arguments
     */
    public CallYaioRecalcNodes(final String[] args) {
        super(args);
    }

    @Override
    protected Options addAvailiableCmdLineOptions() throws Exception {
        Options availiableCmdLineOptions = super.addAvailiableCmdLineOptions();
        
        // sysuid for export
        Option sysuidOption = new Option(null, "sysuid", true,
                "SysUID of Masternode to recalc");
        sysuidOption.setRequired(true);
        availiableCmdLineOptions.addOption(sysuidOption);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() throws Exception {
        // get options
        String sysUID = this.getCmdLineHelper().getCommandLine().getOptionValue("sysuid");
        
        // call url
        byte[] result = this.callGetUrl("/admin/recalc/" + sysUID, null);
        
        System.out.write(result);
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
        CallYaioRecalcNodes me = new CallYaioRecalcNodes(args);
        me.startJobProcessing();
    }
}
