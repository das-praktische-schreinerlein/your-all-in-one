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
package de.yaio.app.jobs.clients;

import de.yaio.app.config.Configurator;
import de.yaio.app.jobs.utils.CallYaioInstance;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.Writer;

/** 
 * job to call yaio-instance to export nodes from db
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.jobs
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class CallYaioExport extends CallYaioInstance {

    private static final Logger LOGGER =
        Logger.getLogger(CallYaioExport.class);

    /** 
     * job to export nodes from db
     * @param args                   the command line arguments
     */
    public CallYaioExport(final String[] args) {
        super(args);
    }

    @Override
    protected Options addAvailiableCmdLineOptions() throws Exception {
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

        // sysuid for export
        Option outfileNameOption = new Option(null, "outfile", true,
                "Filename to write");
        sysuidOption.setRequired(false);
        availiableCmdLineOptions.addOption(outfileNameOption);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() throws Exception {
        // get options
        String sysUID = Configurator.getInstance().getCommandLine().getOptionValue("sysuid");
        String format = Configurator.getInstance().getCommandLine().getOptionValue("format");
        String outfileName = Configurator.getInstance().getCommandLine().getOptionValue("outfile");
        
        // call url
        byte[] result = this.callGetUrl("/exports/" + format + "/" + sysUID, null);
        
        if (!StringUtils.isEmpty(outfileName)) {
            // write to file
            Writer output = new FileWriter(outfileName);
            IOUtils.write(result, output);
            output.close();
        } else {
            // write to stdout
            System.out.write(result);
        }
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
        CallYaioExport me = new CallYaioExport(args);
        me.startJobProcessing();
    }
}
