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

import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import de.yaio.app.CallYaioInstance;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     job to call yaio-instance to reset db
 * 
 * @package de.yaio.jobs
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class CallYaioReset extends CallYaioInstance {

    private static final Logger LOGGER =
        Logger.getLogger(CallYaioReset.class);

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     job to reste the db of an yaio-instance
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the application
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     * @param args the command line arguments
     */
    public CallYaioReset(final String[] args) {
        super(args);
    }

    @Override
    protected Options addAvailiableCmdLineOptions() throws Exception {
        Options availiableCmdLineOptions = super.addAvailiableCmdLineOptions();
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() throws Exception {
        // call url
        byte[] result = this.callGetUrl("/admin/reset", null);
        
        System.out.write(result);
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
    public static void main(final String[] args) {
        CallYaioReset me = new CallYaioReset(args);
        me.startJobProcessing();
    }
}
