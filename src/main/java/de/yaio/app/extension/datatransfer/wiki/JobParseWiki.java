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
package de.yaio.app.extension.datatransfer.wiki;

import de.yaio.app.config.Configurator;
import de.yaio.app.extension.datatransfer.common.ExtendedCommonImporter;
import de.yaio.app.jobs.utils.CmdLineJob;
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
public class JobParseWiki extends CmdLineJob {
    
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
