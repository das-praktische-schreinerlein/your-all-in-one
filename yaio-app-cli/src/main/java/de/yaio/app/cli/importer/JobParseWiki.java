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
package de.yaio.app.cli.importer;

import de.yaio.app.cli.YaioCmdLineJob;
import de.yaio.app.config.ContextHelper;
import de.yaio.app.config.JobConfig;
import de.yaio.app.config.YaioConfiguration;
import de.yaio.commons.cli.CmdLineHelper;
import de.yaio.commons.config.ConfigurationOption;
import org.apache.commons.cli.Options;

/** 
 * job for import of Nodes in Wiki-format and output as PPL
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.extension.datatransfer.wiki
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobParseWiki extends YaioCmdLineJob {
    
    protected ExtendedCommonImporter commonImporter;

    /** 
     * job to import nodes in Wiki-Format and output as PPL
     * @param args                   the command line arguments
     */
    public JobParseWiki(final String[] args) {
        super(args);
        createCommonImporter();
    }
    
    @Override
    protected Options addAvailiableCmdLineOptions() {
        Options availiableCmdLineOptions = 
                        CmdLineHelper.getNewOptionsInstance();
        
        // add Options
        commonImporter.addAvailiableCommonCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableWikiCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableProductiveImportCmdLineOptions(availiableCmdLineOptions);

        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() {
        // parse PPL-source
        if (YaioConfiguration.getInstance().getArgNames().size() <= 0) {
            throw new IllegalArgumentException("Import from Wiki-File requires filename.");
        }
        String srcFile = ConfigurationOption.stringValueOf(YaioConfiguration.getInstance().getArg(0));
        String pplSource = commonImporter.extractDataFromWiki(srcFile);
        System.out.println(pplSource);
    }

    @Override
    protected void configureContext() {
        String sourceType = ConfigurationOption.stringValueOf(this.getConfiguration().getCliOption("sourcetype", ""));
        if ("jpa".equalsIgnoreCase(sourceType)) {
            ContextHelper.getInstance().addSpringConfig(JobConfig.class);
            // initApplicationContext
            ContextHelper.getInstance().getSpringApplicationContext();
        }
    }

    /**
     * create the commonly used importer to imports the data from differenet 
     * sourcetypes
     */
    protected void createCommonImporter() {
        // create commonImporter
        commonImporter = new ExtendedCommonImporter("ppl");
    }

    /** 
     * Main-method to start the application
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        JobParseWiki me = new JobParseWiki(args);
        me.startJobProcessing();
    }
}
