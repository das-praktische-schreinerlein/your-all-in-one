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

import de.yaio.app.config.YaioConfiguration;
import de.yaio.commons.cli.CmdLineHelper;
import de.yaio.commons.config.ConfigurationOption;
import org.apache.commons.cli.Options;

/** 
 * job for import of Nodes in Excel-format and output as PPL
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.extension.datatransfer.excel
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobParseExcel extends JobParseWiki {

    /** 
     * job to import nodes in excel-format and output as PPL
     * @param args                   the command line arguments
     */
    public JobParseExcel(final String[] args) {
        super(args);
    }
    
    @Override
    protected void createCommonImporter() {
        // create commonImporter
        commonImporter = new ExtendedCommonImporter("excel");
    }

    @Override
    protected Options addAvailiableCmdLineOptions() {
        Options availiableCmdLineOptions = CmdLineHelper.getNewOptionsInstance();
        
        // add Options
        commonImporter.addAvailiableCommonCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableExcelCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableProductiveImportCmdLineOptions(availiableCmdLineOptions);

        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() {
        // check srcFile
        if (YaioConfiguration.getInstance().getArgNames().size() <= 0) {
            throw new IllegalArgumentException("Import from Excel-File requires filename.");
        }
        String srcFile = ConfigurationOption.stringValueOf(YaioConfiguration.getInstance().getArg(0));

        // parse PPL-source
        String pplSource = commonImporter.extractDataFromExcel(srcFile);
        System.out.println(pplSource);
    }

    /** 
     * Main-method to start the application
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        JobParseExcel me = new JobParseExcel(args);
        me.startJobProcessing();
    }
}
