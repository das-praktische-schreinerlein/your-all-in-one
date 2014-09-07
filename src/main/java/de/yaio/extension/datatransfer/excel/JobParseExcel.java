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
package de.yaio.extension.datatransfer.excel;

import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import de.yaio.core.datadomainservice.NodeNumberService;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.ImportOptionsImpl;
import de.yaio.extension.datatransfer.common.CommonImporter;
import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.extension.datatransfer.wiki.JobParseWiki;
import de.yaio.utils.CmdLineJob;

/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *     job for import of Nodes in Excel-format and output as PPL
 * 
 * @package de.yaio.extension.datatransfer.excel
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobParseExcel extends JobParseWiki {

    protected CommonImporter commonImporter;

    public JobParseExcel(String[] args) {
        super(args);
    }
    
    @Override
    protected void createCommonImporter() {
        // create commonImporter
        commonImporter = new CommonImporter("excel");
    }

    @Override
    protected Options genAvailiableCmdLineOptions() throws Throwable {
        Options availiableCmdLineOptions = new Options();
        
        // add Options
        commonImporter.addAvailiableCommonCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableExcelCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableProductiveImportCmdLineOptions(availiableCmdLineOptions);

        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() throws Throwable {
        // parse PPL-source
        String pplSource = commonImporter.extractDataFromExcel();
        System.out.println(pplSource);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JobParseExcel me = new JobParseExcel(args);
        me.startJobProcessing();
    }
}
