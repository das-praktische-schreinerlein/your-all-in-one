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

import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import java.io.IOException;

/** 
 * job to call yaio-instance to reset db
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.cli
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class CallYaioReset extends CallYaioInstance {

    private static final Logger LOGGER =
        Logger.getLogger(CallYaioReset.class);

    /** 
     * job to reste the db of an yaio-instance
     * @param args                   the command line arguments
     */
    public CallYaioReset(final String[] args) {
        super(args);
    }

    @Override
    protected Options addAvailiableCmdLineOptions() {
        Options availiableCmdLineOptions = super.addAvailiableCmdLineOptions();
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() {
        // call url
        byte[] result;
        try {
            result = this.callGetUrl("/admin/reset", null);
        } catch (IOException ex) {
            throw new RuntimeException("error while calling resetUrl", ex);
        }


        try {
            System.out.write(result);
        } catch (IOException ex) {
            throw new RuntimeException("error while writing to stdout", ex);
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
        CallYaioReset me = new CallYaioReset(args);
        me.startJobProcessing();
    }
}
