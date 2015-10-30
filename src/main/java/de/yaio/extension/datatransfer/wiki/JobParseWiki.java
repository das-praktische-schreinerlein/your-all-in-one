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
package de.yaio.extension.datatransfer.wiki;

import org.apache.commons.cli.Options;

import de.yaio.app.CmdLineJob;
import de.yaio.app.Configurator;
import de.yaio.extension.datatransfer.common.CommonImporter;

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
public class JobParseWiki extends CmdLineJob {
    
    protected CommonImporter commonImporter;

    /** 
     * job to import nodes in Wiki-Format and output as PPL
     * @FeatureDomain                Constructor
     * @FeatureResult                initialize the application
     * @FeatureKeywords              Constructor
     * @param args                   the command line arguments
     */
    public JobParseWiki(final String[] args) {
        super(args);
        createCommonImporter();
    }
    
    @Override
    protected Options addAvailiableCmdLineOptions() throws Exception {
        Options availiableCmdLineOptions = 
                        Configurator.getNewOptionsInstance();
        
        // add Options
        commonImporter.addAvailiableCommonCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableWikiCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableProductiveImportCmdLineOptions(availiableCmdLineOptions);

        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() throws Exception {
        // parse PPL-source
        String pplSource = commonImporter.extractDataFromWiki();
        System.out.println(pplSource);
    }

    /** 
     * create the commonly used importer to imports the data from differenet 
     * sourcetypes
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                updates MemberVar commonImporter - for the import
     * @FeatureKeywords              BusinessLogic
     */
    protected void createCommonImporter() {
        // create commonImporter
        commonImporter = new CommonImporter("ppl");
    }

    /** 
     * Main-method to start the application
     * @FeatureDomain                CLI
     * @FeatureResult                initialize the application
     * @FeatureKeywords              CLI
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        JobParseWiki me = new JobParseWiki(args);
        me.startJobProcessing();
    }
}
