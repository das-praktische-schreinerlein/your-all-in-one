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
import de.yaio.app.config.ContextHelper;
import de.yaio.app.config.JobConfig;
import de.yaio.app.core.recalcer.NodeRecalcer;
import de.yaio.commons.cli.CmdLineHelper;
import de.yaio.commons.config.ConfigurationOption;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

/** 
 * job to recalc nodes in db
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public class JobRecalcNodes extends YaioCmdLineJob {

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
    protected Options addAvailiableCmdLineOptions() {
        Options availiableCmdLineOptions = CmdLineHelper.getNewOptionsInstance();

        // add dfeault-Options
        this.getCmdLineHelper().addAvailiableBaseCmdLineOptions(availiableCmdLineOptions);
        
        // sysuid for export
        Option sysuidOption = new Option(null, "sysuid", true, "SysUID of Masternode to recalc");
        sysuidOption.setRequired(true);
        availiableCmdLineOptions.addOption(sysuidOption);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }

    @Override
    protected void configureContext() {
        ContextHelper.getInstance().addSpringConfig(JobConfig.class);
    }

    @Override
    public void doJob() {
        // initApplicationContext
        ContextHelper.getInstance().getSpringApplicationContext();
        
        // extract sysUID
        String sysUID = ConfigurationOption.stringValueOf(this.getConfiguration().getCliOption("sysuid"));

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
