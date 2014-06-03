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
package de.yaio.extension.datatransfer.wiki;

import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import de.yaio.core.datadomainservice.NodeNumberService;
import de.yaio.extension.datatransfer.wiki.WikiImporter.WikiStructLine;
import de.yaio.utils.CmdLineJob;

/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *     job for import of Nodes in Wiki-format and output as PPL
 * 
 * @package de.yaio.extension.datatransfer.wiki
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobParseWiki extends CmdLineJob {


    public JobParseWiki(String[] args) {
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

        // Show State
        Option flgShowState = new Option("s", "onlyifstate", false,
                "Show Only if State/Type set (default false)");
        flgShowState.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowState);

        // Show State
        Option flgShowWFState = new Option("w", "onlyifwfstate", false,
                "Show Only if WFState set (default false)");
        flgShowWFState.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowWFState);

        // InputState
        Option flgShowIfStateList = new Option("", "onlyifstateinlist", true,
                "Show only if State in List (CSV)");
        flgShowIfStateList.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowIfStateList);

        // Dont Parse Ue
        Option flgShowUe = new Option("u", "dontparseue", false,
                "Parse no Ue (default false)");
        flgShowUe.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowUe);

        // Dont Parse List
        Option flgShowList = new Option("l", "dontparselist", false,
                "Parse no List (default false)");
        flgShowList.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowList);

        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() throws Throwable {
        // Parser+Options anlegen
        WikiImportOptions inputOptions = new WikiImportOptions();
        inputOptions.flgReadList = true;
        inputOptions.flgReadUe = true;
        inputOptions.flgReadWithStatusOnly = this.cmdLine.hasOption("s");
        inputOptions.flgReadWithWFStatusOnly = this.cmdLine.hasOption("w");
        if (this.cmdLine.hasOption("l")) {
            inputOptions.flgReadList = false;
        }
        if (this.cmdLine.hasOption("u")) {
            inputOptions.flgReadUe = false;
        }
        inputOptions.strReadIfStatusInListOnly = 
                this.cmdLine.getOptionValue("onlyifstateinlist", null);
        WikiImporter importer = new WikiImporter(inputOptions);
        
        // gets NodeNumberService
        NodeNumberService nodeNumberService = 
                importer.getNodeFactory().getMetaDataService().getNodeNumberService();
        
        // Id-Datei einlesen
        String strPathIdDB = this.cmdLine.getOptionValue("pathiddb", null);
        if (strPathIdDB != null) {
            nodeNumberService.initNextNodeNumbersFromFile(strPathIdDB);
        }

        // aus Datei oder nur Test ??
        List<WikiStructLine> lstWikiLines;
        if (this.cmdLine.getArgs().length > 0) {
            // aus datei
            lstWikiLines =
                    importer.extractWikiStructLinesFromFile(this.cmdLine.getArgs()[0], inputOptions);
        } else {
            // nur Test
            lstWikiLines =
                    importer.extractWikiStructLinesFromSrc(WikiImporter.testStr, inputOptions);
        }

        // Ids speichern
        if (strPathIdDB != null) {
            // save to file
            nodeNumberService.exportNextNodeNumbersToFile(strPathIdDB);
        }

        // WikiListe ausgeben
        if (lstWikiLines != null) {
            for (WikiStructLine wk : lstWikiLines) {
                System.out.println(wk.hirarchy);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JobParseWiki me = new JobParseWiki(args);
        me.startJobProcessing();
    }
}
