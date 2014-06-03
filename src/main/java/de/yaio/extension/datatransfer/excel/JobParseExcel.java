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
public class JobParseExcel extends CmdLineJob {


    public JobParseExcel(String[] args) {
        super(args);
    }


    @Override
    protected Options genAvailiableCmdLineOptions() throws Throwable {
        Options availiableCmdLineOptions = new Options();

        // Hilfe-Option
        Option helpOption = new Option("h", "help", false, "usage");
        helpOption.setRequired(false);
        availiableCmdLineOptions.addOption(helpOption);

        // Id-File
        Option pathIdDB = new Option("", "pathiddb", true,
                "Pfad zur ID-Datenbank");
        pathIdDB.setRequired(true);
        availiableCmdLineOptions.addOption(pathIdDB);

        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() throws Throwable {
        // Parser+Options anlegen
        ImportOptions inputOptions = new ImportOptionsImpl();
        ExcelImporter importer = new ExcelImporter(inputOptions);
        
        // gets NodeNumberService
        NodeNumberService nodeNumberService = 
                importer.getNodeFactory().getMetaDataService().getNodeNumberService();
        
        // Id-Datei einlesen
        String strPathIdDB = this.cmdLine.getOptionValue("pathiddb", null);
        if (strPathIdDB != null) {
            nodeNumberService.initNextNodeNumbersFromFile(strPathIdDB);
        }

        // parse excel-file
        List<String> lstPPLLines = importer.fromExcel(this.cmdLine.getArgs()[0]);

        // Ids speichern
        if (strPathIdDB != null) {
            // save to file
            nodeNumberService.exportNextNodeNumbersToFile(strPathIdDB);
        }

        // PPLListe ausgeben
        if (lstPPLLines != null) {
            for (String line : lstPPLLines) {
                System.out.println(line);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JobParseExcel me = new JobParseExcel(args);
        me.startJobProcessing();
    }
}
