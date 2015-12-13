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
package de.yaio.extension.datatransfer.common;

import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import de.yaio.app.Configurator;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomainservice.NodeNumberService;
import de.yaio.datatransfer.common.CommonImporter;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.ImportOptionsImpl;
import de.yaio.extension.datatransfer.excel.ExcelImporter;
import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.extension.datatransfer.wiki.WikiImportOptions;
import de.yaio.extension.datatransfer.wiki.WikiImporter;
import de.yaio.extension.datatransfer.wiki.WikiImporter.WikiStructLine;

/** 
 * class for import of Nodes
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.common
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ExtendedCommonImporter extends CommonImporter {
    
    // Logger
    private static final Logger LOGGER = Logger.getLogger(ExtendedCommonImporter.class);

    protected String defaultSourceType = "ppl";
    protected PPLImporter pplImporter = null;
    
    /** 
     * create importer-object to import nodes
     * @FeatureDomain                Constructor
     * @FeatureResult                initialize the importer
     * @FeatureKeywords              Constructor
     * @param defaultSourceType      the default sourcetype if commandline-option not set
     */
    public ExtendedCommonImporter(final String defaultSourceType) {
        super(defaultSourceType);
        createPPLImporter();
    }
    
    

    /*
     * ##############
     * avaliable Commandline-Options
     * ##############
     */
    
    @Override
    public void addAvailiableCommonCmdLineOptions(final Options availiableCmdLineOptions) {
        // sourceType
        Option sourceType = new Option("", "sourcetype", true,
                "Source to read from (jpa,ppl,wiki,excel).");
        sourceType.setRequired(false);
        availiableCmdLineOptions.addOption(sourceType);
    
        // Hirarchy-Delimiter  
        Option delimiterOption = new Option("", "delimiter", true,
                "Hirarchy-Delimiter (default TAB)");
        delimiterOption.setRequired(false);
        availiableCmdLineOptions.addOption(delimiterOption);
    }
    

    /** 
     * add Wiki-import-options to the availiableCmdLineOptions
     * @FeatureDomain                CLI
     * @FeatureResult                update availiableCmdLineOptions
     * @FeatureKeywords              CLI
     * @param availiableCmdLineOptions the container with the availiableCmdLineOptions
     */
    public void addAvailiableWikiCmdLineOptions(final Options availiableCmdLineOptions) {
        // Show State
        Option flgShowState = new Option("", "onlyifstate", false,
                "Show Only if State/Type set (default false)");
        flgShowState.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowState);

        // Show State
        Option flgShowWFState = new Option("", "onlyifwfstate", false,
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
        Option flgShowList = new Option("", "dontparselist", false,
                "Parse no List (default false)");
        flgShowList.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowList);
    }
    
    /** 
     * add Excel-import-options to the availiableCmdLineOptions
     * @FeatureDomain                CLI
     * @FeatureResult                update availiableCmdLineOptions
     * @FeatureKeywords              CLI
     * @param availiableCmdLineOptions the container with the availiableCmdLineOptions
     */
    public void addAvailiableExcelCmdLineOptions(final Options availiableCmdLineOptions) {
    }

    /** 
     * add PPL-import-options to the availiableCmdLineOptions
     * @FeatureDomain                CLI
     * @FeatureResult                update availiableCmdLineOptions
     * @FeatureKeywords              CLI
     * @param availiableCmdLineOptions the container with the availiableCmdLineOptions
     */
    public void addAvailiablePPLCmdLineOptions(final Options availiableCmdLineOptions) {
    }

    /** 
     * add Production-import-options to the availiableCmdLineOptions
     * @FeatureDomain                CLI
     * @FeatureResult                update availiableCmdLineOptions
     * @FeatureKeywords              CLI
     * @param availiableCmdLineOptions the container with the availiableCmdLineOptions
     */
    public void addAvailiableProductiveImportCmdLineOptions(final Options availiableCmdLineOptions) {
        // Id-File
        Option pathIdDB = new Option("", "pathiddb", true,
                "Pfad zur ID-Datenbank");
        pathIdDB.setRequired(true);
        availiableCmdLineOptions.addOption(pathIdDB);
    }
    
    /*
     * ##############
     * import-logic
     * ##############
     */

    /** 
     * import the data from PPL-File configured by cmdline-options and add 
     * them to the masterNode 
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                updates masternode
     * @FeatureKeywords              BusinessLogic
     * @param masterNode             the masternode on which all other nodes are added
     * @throws Exception             parse/io-Exceptions possible
     */
    public void importDataToMasterNodeFromPPLFile(final DataDomain masterNode) throws Exception {
        // check srcFile
        if (Configurator.getInstance().getCommandLine().getArgs().length <= 0) {
            throw new IllegalArgumentException("Import from PPL-File requires filename.");
        }
        String srcFile = Configurator.getInstance().getCommandLine().getArgs()[0];
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read from PPL file:" + srcFile);
        }

        // config
        String delimiter = 
                        Configurator.getInstance().getCommandLine().getOptionValue(
                                        "delimiter", "\t");
        
        // export PPL 
        pplImporter.extractNodesFromFile(masterNode, srcFile, delimiter);
    }

    /** 
     * import the data from Excel-File configured by cmdline-options and add 
     * them to the masterNode 
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                updates masternode
     * @FeatureKeywords              BusinessLogic
     * @param masterNode             the masternode on which all other nodes are added
     * @throws Exception             parse/io-Exceptions possible
     */
    public void importDataToMasterNodeFromExcel(final DataDomain masterNode) throws Exception {
        // config
        String delimiter = 
                        Configurator.getInstance().getCommandLine().getOptionValue(
                                        "delimiter", "\t");

        // create Importer
        String pplSource = this.extractDataFromExcel();

        // parse PPL-source
        pplImporter.extractNodesFromLines(masterNode, pplSource, delimiter);
    }

    /** 
     * import the nodes from Excel-File configured by cmdline-options and 
     * return them as PPL-String
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                returnValue String in PPL-format
     * @FeatureKeywords              BusinessLogic
     * @return                       String with nodes in PPL-format
     * @throws Exception             parse/io-Exceptions possible
     */
    public String extractDataFromExcel() throws Exception {
        // check srcFile
        if (Configurator.getInstance().getCommandLine().getArgs().length <= 0) {
            throw new IllegalArgumentException("Import from Excel-File requires filename.");
        }
        String srcFile = Configurator.getInstance().getCommandLine().getArgs()[0];
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read from PPL file:" + srcFile);
        }


        // init importer
        ImportOptions inputOptions = new ImportOptionsImpl();
        ExcelImporter excelImporter = new ExcelImporter(inputOptions);

        // gets NodeNumberService
        NodeNumberService nodeNumberService = 
                        excelImporter.getNodeFactory().getMetaDataService().getNodeNumberService();

        // Id-Datei einlesen
        String strPathIdDB = 
                        Configurator.getInstance().getCommandLine().getOptionValue(
                                        "pathiddb", null);
        if (strPathIdDB != null) {
            nodeNumberService.initNextNodeNumbersFromFile(strPathIdDB, false);
        }

        // parse excel-file
        List<String> lstPPLLines = excelImporter.fromExcel(srcFile);

        // Ids speichern
        if (strPathIdDB != null) {
            // save to file
            nodeNumberService.exportNextNodeNumbersToFile(strPathIdDB);
        }

        // add to PPL-source
        StringBuffer resBuf = new StringBuffer();
        if (lstPPLLines != null) {
            for (String line : lstPPLLines) {
                resBuf.append(line).append("\n");
            }
        }
        return resBuf.toString();
    }

    /** 
     * import the data from Wiki-File configured by cmdline-options and add 
     * them to the masterNode 
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                updates masternode
     * @FeatureKeywords              BusinessLogic
     * @param masterNode             the masternode on which all other nodes are added
     * @throws Exception             parse/io-Exceptions possible
     */
    public void importDataToMasterNodeFromWiki(final DataDomain masterNode) throws Exception {
        // config
        String delimiter = 
                        Configurator.getInstance().getCommandLine().getOptionValue(
                                        "delimiter", "\t");

        // parse PPL-source
        String pplSource = this.extractDataFromWiki();
        pplImporter.extractNodesFromLines(masterNode, pplSource, delimiter);
    }
    
    
    /** 
     * import the nodes from Wiki-File configured by cmdline-options and 
     * return them as PPL-String
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                returnValue String in PPL-format
     * @FeatureKeywords              BusinessLogic
     * @return                       String with nodes in PPL-format
     * @throws Exception             parse/io-Exceptions possible
     */
    public String extractDataFromWiki() throws Exception {
        // check srcFile
        if (Configurator.getInstance().getCommandLine().getArgs().length <= 0) {
            throw new IllegalArgumentException("Import from Wiki-File requires filename.");
        }
        String srcFile = Configurator.getInstance().getCommandLine().getArgs()[0];
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read from PPL file:" + srcFile);
        }

        // Parser+Options anlegen
        WikiImportOptions inputOptions = new WikiImportOptions();
        inputOptions.setFlgReadList(true);
        inputOptions.setFlgReadUe(true);
        inputOptions.setFlgReadWithStatusOnly(
                        Configurator.getInstance().getCommandLine().hasOption("s"));
        inputOptions.setFlgReadWithWFStatusOnly(
                        Configurator.getInstance().getCommandLine().hasOption("w"));
        if (Configurator.getInstance().getCommandLine().hasOption("l")) {
            inputOptions.setFlgReadList(false);
        }
        if (Configurator.getInstance().getCommandLine().hasOption("u")) {
            inputOptions.setFlgReadUe(false);
        }
        inputOptions.setStrReadIfStatusInListOnly(
                Configurator.getInstance().getCommandLine().getOptionValue(
                                "onlyifstateinlist", null));
        WikiImporter wikiImporter = new WikiImporter(inputOptions);
        
        // gets NodeNumberService
        NodeNumberService nodeNumberService = 
                pplImporter.getNodeFactory().getMetaDataService().getNodeNumberService();
        
        // Id-Datei einlesen
        String strPathIdDB = 
                        Configurator.getInstance().getCommandLine().getOptionValue(
                                        "pathiddb", null);
        if (strPathIdDB != null) {
            nodeNumberService.initNextNodeNumbersFromFile(strPathIdDB, false);
        }

        // parse file
        List<WikiStructLine> lstWikiLines;
        lstWikiLines = wikiImporter.extractWikiStructLinesFromFile(srcFile, inputOptions);

        // Ids speichern
        if (strPathIdDB != null) {
            // save to file
            nodeNumberService.exportNextNodeNumbersToFile(strPathIdDB);
        }

        // add to PPL-source
        StringBuffer resBuf = new StringBuffer();
        if (lstWikiLines != null) {
            for (WikiStructLine wk : lstWikiLines) {
                resBuf.append(wk.getHirarchy()).append("\n");
            }
        }
        return resBuf.toString();
    }
    
    @Override
    public void importDataToMasterNode(final DataDomain masterNode) throws Exception {
        // check datasource
        String sourceType = 
                        Configurator.getInstance().getCommandLine().getOptionValue(
                                        "sourcetype", defaultSourceType);
        if ("excel".equalsIgnoreCase(sourceType)) {
            // from excel
            this.importDataToMasterNodeFromExcel(masterNode);
        } else if ("wiki".equalsIgnoreCase(sourceType)) {
            // from wiki
            this.importDataToMasterNodeFromWiki(masterNode);
        } else if ("ppl".equalsIgnoreCase(sourceType)) {
            // default: ppl
            this.importDataToMasterNodeFromPPLFile(masterNode);
        } else {
            super.importDataToMasterNode(masterNode);
        }
    }

    /*
     * ##############
     * service-functions
     * ##############
     */
    
    
    /** 
     * create the commonly used PPLimporter
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                updates MemberVar pplImporter - for the import
     * @FeatureKeywords              BusinessLogic
     */
    protected void createPPLImporter() {
        // create commonImporter
        pplImporter = new PPLImporter(null);
    }
    

    /** 
     * get the commonly used PPLimporter
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                returnValue an instance of PPLImporter
     * @FeatureKeywords              BusinessLogic
     * @return                       an instance of PPLImporter
     */
    public PPLImporter getPPLImporter() {
        return pplImporter;
    }
}
