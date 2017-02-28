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

import de.yaio.commons.cli.CmdLineJob;
import de.yaio.commons.config.ConfigurationOption;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** 
 * job to call yaio-instance to import nodes to db
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.cli
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
    protected Options addAvailiableCmdLineOptions() {
        Options availiableCmdLineOptions = super.addAvailiableCmdLineOptions();
        
        // file to import
        Option formatOption = new Option(null, "importfile", true, "file to import (in wikiformat)");
        formatOption.setRequired(true);
        availiableCmdLineOptions.addOption(formatOption);

        // sysuid for import
        Option parentsysuidOption = new Option(null, "parentsysuid", true,
                "SysUID of Masternode to append importfile");
        parentsysuidOption.setRequired(true);
        availiableCmdLineOptions.addOption(parentsysuidOption);

        // filetype for import
        Option importTypeOption = new Option(null, "importtype", true,
                "importtype of the importfile (wiki(default)/json/mail/file)");
        importTypeOption.setRequired(false);
        availiableCmdLineOptions.addOption(importTypeOption);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() {
        // get options
        String parentsysUID = ConfigurationOption.stringValueOf(this.getConfiguration().getCliOption("parentsysuid"));
        String importfile = ConfigurationOption.stringValueOf(this.getConfiguration().getCliOption("importfile"));
        String importType = ConfigurationOption.stringValueOf(this.getConfiguration().getCliOption("importtype"));

        // check options
        String url;
        if (StringUtils.isEmpty(importType) || "wiki".equals(importType.toLowerCase())) {
            url = "/imports/wiki/";
        } else if ("json".equals(importType.toLowerCase())) {
            url = "/imports/json/";
        } else if ("mail".equals(importType.toLowerCase())) {
            url = "/imports/mail/";
        } else if ("file".equals(importType.toLowerCase())) {
            url = "/imports/file/";
        } else {
            throw new RuntimeException("illegal importtype:" + importType);
        }

        // call url
        Map<String, String> files = new HashMap<String, String>();
        files.put("file", importfile);
        byte[] result;
        try {
            result = this.callPostUrl(url + parentsysUID, null, files);
        } catch (IOException ex) {
            throw new RuntimeException("error while calling importUrl", ex);
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
        CallYaioImport me = new CallYaioImport(args);
        me.startJobProcessing();
        System.exit(CmdLineJob.CONST_EXITCODE_OK);
    }
}
