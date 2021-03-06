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
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.datadomainservice.NodeNumberService;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.importer.ImportOptions;
import de.yaio.app.datatransfer.importer.ImportOptionsImpl;
import de.yaio.app.extension.datatransfer.excel.ExcelImporter;
import de.yaio.app.extension.datatransfer.ppl.PPLImporter;
import de.yaio.app.extension.datatransfer.wiki.WikiImportOptions;
import de.yaio.app.extension.datatransfer.wiki.WikiImporter;
import de.yaio.app.extension.datatransfer.wiki.WikiImporter.WikiStructLine;
import de.yaio.commons.config.ConfigurationOption;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

/** 
 * class for import of Nodes
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public class ExtendedCommonImporter extends CommonImporter {
    
    // Logger
    private static final Logger LOGGER = Logger.getLogger(ExtendedCommonImporter.class);

    protected PPLImporter pplImporter = null;
    
    /** 
     * create importer-object to import nodes
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
     * @param availiableCmdLineOptions the container with the availiableCmdLineOptions
     */
    public void addAvailiableExcelCmdLineOptions(final Options availiableCmdLineOptions) {
    }

    /** 
     * add PPL-import-options to the availiableCmdLineOptions
     * @param availiableCmdLineOptions the container with the availiableCmdLineOptions
     */
    public void addAvailiablePPLCmdLineOptions(final Options availiableCmdLineOptions) {
    }

    /** 
     * add Production-import-options to the availiableCmdLineOptions
     * @param availiableCmdLineOptions the container with the availiableCmdLineOptions
     */
    @Override
    public void addAvailiableProductiveImportCmdLineOptions(final Options availiableCmdLineOptions) {
        // Id-File
        Option pathIdDB = new Option("", "pathiddb", true, "Pfad zur ID-Datenbank");
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
     * @param masterNode             the masternode on which all other nodes are added
     * @throws ParserException       parse/io-Exceptions possible
     */
    public void importDataToMasterNodeFromPPLFile(final DataDomain masterNode) {
        // check srcFile
        if (YaioConfiguration.getInstance().getArgNames().size() <= 0) {
            throw new IllegalArgumentException("Import from PPL-File requires filename.");
        }
        String srcFile = ConfigurationOption.stringValueOf(YaioConfiguration.getInstance().getArg(0));
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read from PPL file:" + srcFile);
        }

        // config
        String delimiter = ConfigurationOption.stringValueOf(
                YaioConfiguration.getInstance().getCliOption("delimiter", "\t"));
        
        // export PPL 
        try {
            pplImporter.extractNodesFromFile(masterNode, srcFile, delimiter);
        } catch (IOException ex) {
            throw new IllegalArgumentException("error while reading pplfile", ex);
        } catch (ParserException ex) {
            throw new IllegalArgumentException("error while parsing pplfile", ex);
        }
    }

    /** 
     * import the data from Excel-File configured by cmdline-options and add 
     * them to the masterNode 
     * @param masterNode             the masternode on which all other nodes are added
     * @throws Exception             parse/io-Exceptions possible
     */
    public void importDataToMasterNodeFromExcel(final DataDomain masterNode) {
        // config
        String delimiter = ConfigurationOption.stringValueOf(
                YaioConfiguration.getInstance().getCliOption("delimiter", "\t"));

        // check srcFile
        if (YaioConfiguration.getInstance().getArgNames().size() <= 0) {
            throw new IllegalArgumentException("Import from Excel-File requires filename.");
        }
        String srcFile = ConfigurationOption.stringValueOf(YaioConfiguration.getInstance().getArg(0));
        String pplSource = this.extractDataFromExcel(srcFile);
        try {
            // parse PPL-source
            pplImporter.extractNodesFromLines(masterNode, pplSource, delimiter);
        } catch (ParserException ex) {
            throw new IllegalArgumentException("error while parsing excelfile", ex);
        }
    }

    /** 
     * import the nodes from Excel-File configured by cmdline-options and 
     * return them as PPL-String
     * @return                       String with nodes in PPL-format
     */
    public String extractDataFromExcel(final String srcFile) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read from Excel file:" + srcFile);
        }

        // init importer
        ImportOptions inputOptions = new ImportOptionsImpl();
        ExcelImporter excelImporter = new ExcelImporter(inputOptions);

        // gets NodeNumberService
        NodeNumberService nodeNumberService = 
                        excelImporter.getNodeFactory().getMetaDataService().getNodeNumberService();

        // Id-Datei einlesen
        String strPathIdDB = ConfigurationOption.stringValueOf(YaioConfiguration.getInstance().getCliOption("pathiddb"));
        if (!StringUtils.isEmpty(strPathIdDB)) {
            nodeNumberService.initNextNodeNumbersFromFile(strPathIdDB, false);
        }

        // parse excel-file
        List<String> lstPPLLines;
        try {
            lstPPLLines = excelImporter.fromExcel(srcFile);
        } catch (ParserException ex) {
            throw new IllegalArgumentException("cant extract data from excel - error while parsing srcFile", ex);
        }

        // Ids speichern
        if (!StringUtils.isEmpty(strPathIdDB)) {
            // save to file
            nodeNumberService.exportNextNodeNumbersToFile(strPathIdDB);
        }

        // add to PPL-source
        StringBuilder resBuf = new StringBuilder();
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
     * @param masterNode             the masternode on which all other nodes are added
     */
    public void importDataToMasterNodeFromWiki(final DataDomain masterNode) {
        // config
        String delimiter = ConfigurationOption.stringValueOf(
                YaioConfiguration.getInstance().getCliOption("delimiter", "\t"));
        // check srcFile
        if (YaioConfiguration.getInstance().getArgNames().size() <= 0) {
            throw new IllegalArgumentException("Import from Wiki-File requires filename.");
        }
        String srcFile = ConfigurationOption.stringValueOf(YaioConfiguration.getInstance().getArg(0));

        // parse PPL-source
        String pplSource = this.extractDataFromWiki(srcFile);
        try {
            // parse PPL-source
            pplImporter.extractNodesFromLines(masterNode, pplSource, delimiter);
        } catch (ParserException ex) {
            throw new IllegalArgumentException("error while parsing wikifile", ex);
        }
    }
    
    
    /** 
     * import the nodes from Wiki-File configured by cmdline-options and 
     * return them as PPL-String
     * @return                       String with nodes in PPL-format
     */
    public String extractDataFromWiki(final String srcFile) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read from Wiki file:" + srcFile);
        }

        // Parser+Options anlegen
        WikiImportOptions inputOptions = new WikiImportOptions();
        inputOptions.setFlgReadList(true);
        inputOptions.setFlgReadUe(true);
        inputOptions.setFlgReadWithStatusOnly(YaioConfiguration.getInstance().hasCliOption("s"));
        inputOptions.setFlgReadWithWFStatusOnly(YaioConfiguration.getInstance().hasCliOption("w"));
        if (YaioConfiguration.getInstance().hasCliOption("l")) {
            inputOptions.setFlgReadList(false);
        }
        if (YaioConfiguration.getInstance().hasCliOption("u")) {
            inputOptions.setFlgReadUe(false);
        }
        inputOptions.setStrReadIfStatusInListOnly(
                ConfigurationOption.stringValueOf(YaioConfiguration.getInstance().getCliOption("onlyifstateinlist")));
        WikiImporter wikiImporter = new WikiImporter(inputOptions);
        
        // gets NodeNumberService
        NodeNumberService nodeNumberService = 
                pplImporter.getNodeFactory().getMetaDataService().getNodeNumberService();
        
        // Id-Datei einlesen
        String strPathIdDB = ConfigurationOption.stringValueOf(YaioConfiguration.getInstance().getCliOption("pathiddb"));
        if (!StringUtils.isEmpty(strPathIdDB)) {
            nodeNumberService.initNextNodeNumbersFromFile(strPathIdDB, false);
        }

        // parse file
        List<WikiStructLine> lstWikiLines;
        try {
            lstWikiLines = wikiImporter.extractWikiStructLinesFromFile(srcFile, inputOptions);
        } catch (IOException ex) {
            throw new IllegalArgumentException("cant extract data from wiki - error while reading srcFile", ex);
        } catch (ParserException ex) {
            throw new IllegalArgumentException("cant extract data from wiki - error while parsing srcFile", ex);
        }

        // Ids speichern
        if (!StringUtils.isEmpty(strPathIdDB)) {
            // save to file
            nodeNumberService.exportNextNodeNumbersToFile(strPathIdDB);
        }

        // add to PPL-source
        StringBuilder resBuf = new StringBuilder();
        if (lstWikiLines != null) {
            for (WikiStructLine wk : lstWikiLines) {
                resBuf.append(wk.getHirarchy()).append("\n");
            }
        }
        return resBuf.toString();
    }
    
    @Override
    public void importDataToMasterNode(final DataDomain masterNode) {
        // check datasource
        String sourceType = ConfigurationOption.stringValueOf(
                YaioConfiguration.getInstance().getCliOption("sourcetype", defaultSourceType));
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
     */
    protected void createPPLImporter() {
        // create commonImporter
        pplImporter = new PPLImporter(null);
    }
    

    /** 
     * get the commonly used PPLimporter
     * @return                       an instance of PPLImporter
     */
    public PPLImporter getPPLImporter() {
        return pplImporter;
    }
}
