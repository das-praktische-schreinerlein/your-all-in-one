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

import de.yaio.app.CallYaioInstance;
import de.yaio.app.Configurator;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/** 
 * job to call yaio-instance to import nodes to db
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.jobs
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class CallYaioImport extends CallYaioInstance {

    private static final Logger LOGGER =
        Logger.getLogger(CallYaioImport.class);

    /** 
     * job to import nodes to db
     * @param args                   the command line arguments
     */
    public CallYaioImport(final String[] args) {
        super(args);
    }

    @Override
    protected Options addAvailiableCmdLineOptions() throws Exception {
        Options availiableCmdLineOptions = super.addAvailiableCmdLineOptions();
        
        // file to import
        Option formatOption = new Option(null, "importfile", true,
                "file to import (in wikiformat)");
        formatOption.setRequired(true);
        availiableCmdLineOptions.addOption(formatOption);

        // sysuid for import
        Option parentsysuidOption = new Option(null, "parentsysuid", true,
                "SysUID of Masternode to append importfile");
        parentsysuidOption.setRequired(true);
        availiableCmdLineOptions.addOption(parentsysuidOption);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() throws Exception {
        // get options
        String parentsysUID = Configurator.getInstance().getCommandLine().getOptionValue("parentsysuid");
        String importfile = Configurator.getInstance().getCommandLine().getOptionValue("importfile");
        
        // call url
        Map<String, String> files = new HashMap<String, String>();
        files.put("file", importfile);
        byte[] result = this.callPostUrl("/imports/wiki/" + parentsysUID, null, files);
        
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
        CallYaioImport me = new CallYaioImport(args);
        me.startJobProcessing();
    }
}
