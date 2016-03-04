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
package de.yaio.jobs;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import de.yaio.app.CmdLineJob;
import de.yaio.app.Configurator;

/** 
 * job to recalc nodes in db
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.jobs
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobRecalcNodes extends CmdLineJob {

    private static final Logger LOGGER =
        Logger.getLogger(JobRecalcNodes.class);

    /** 
     * job to update the nodes in db
     * @param args                   the command line arguments
     */
    public JobRecalcNodes(final String[] args) {
        super(args);
    }

    @Override
    protected Options addAvailiableCmdLineOptions() throws Exception {
        Options availiableCmdLineOptions = 
                        Configurator.getNewOptionsInstance();

        // add dfeault-Options
        Configurator.getInstance().addAvailiableBaseCmdLineOptions(
                        availiableCmdLineOptions);
        
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
        // initApplicationContext
        Configurator.getInstance().getSpringApplicationContext();
        
        // extract sysUID
        String sysUID = Configurator.getInstance().getCommandLine().getOptionValue("sysuid");

        // create recalcer
        NodeRecalcer nodeRecalcer = new NodeRecalcer();
        
        // recalc
        System.out.println(nodeRecalcer.findAndRecalcMasternode(sysUID));
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
        JobRecalcNodes me = new JobRecalcNodes(args);
        me.startJobProcessing();
    }
}
