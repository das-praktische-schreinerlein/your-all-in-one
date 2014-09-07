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
package de.yaio.extension.datatransfer.common;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomainservice.NodeNumberService;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.ImportOptionsImpl;
import de.yaio.extension.datatransfer.excel.ExcelImporter;
import de.yaio.extension.datatransfer.jpa.JPAImporter;
import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.extension.datatransfer.wiki.WikiImportOptions;
import de.yaio.extension.datatransfer.wiki.WikiImporter;
import de.yaio.extension.datatransfer.wiki.WikiImporter.WikiStructLine;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     class for import of Nodes
 * 
 * @package de.yaio.extension.datatransfer.common
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class CommonImporter {
    
    protected String defaultSourceType = "ppl";
    protected CommandLine cmdLine = null;
    protected PPLImporter pplImporter = null;
    
    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(CommonImporter.class);


    public CommonImporter(String defaultSourceType) {
        this.defaultSourceType = defaultSourceType;
        createPPLImporter();
    }
    
    

    /*
     * ##############
     * avaliable Commandline-Options
     * ##############
     */
    
    public void addAvailiableCommonCmdLineOptions(Options availiableCmdLineOptions) throws Throwable {
        // Hilfe-Option
        Option helpOption = new Option("h", "help", false, "usage");
        helpOption.setRequired(false);
        availiableCmdLineOptions.addOption(helpOption);

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
    

    public void addAvailiableWikiCmdLineOptions(Options availiableCmdLineOptions) throws Throwable {
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
    
    public void addAvailiableExcelCmdLineOptions(Options availiableCmdLineOptions) throws Throwable {
    }

    public void addAvailiablePPLCmdLineOptions(Options availiableCmdLineOptions) throws Throwable {
    }

    public void addAvailiableJPACmdLineOptions(Options availiableCmdLineOptions) throws Throwable {
        // exportsysuid
        Option exportSysUid = new Option("", "exportsysuid", true,
                "SysUId of the masterNode to export.");
        exportSysUid.setRequired(false);
        availiableCmdLineOptions.addOption(exportSysUid);
    }

    public void addAvailiableProductiveImportCmdLineOptions(Options availiableCmdLineOptions) throws Throwable {
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

    public void importDataToMasterNodeFromPPLFile(DataDomain masterNode) throws Exception {
        // check srcFile
        if (cmdLine.getArgs().length <= 0) {
            throw new IllegalArgumentException("Import from PPL-File requires filename.");
        }
        String srcFile = cmdLine.getArgs()[0];
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read from PPL file:" + srcFile);
        }

        // config
        String delimiter = cmdLine.getOptionValue("delimiter", "\t");
        
        // export PPL 
        pplImporter.extractNodesFromFile(masterNode, srcFile, delimiter);
    }

    public void importDataToMasterNodeFromJPA(DataDomain masterNode) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read from JPA");
        }
        
        // check exportsysuid
        String exportSysUID = cmdLine.getOptionValue("exportsysuid");
        if (exportSysUID == null || "".equalsIgnoreCase(exportSysUID)) {
            throw new IllegalArgumentException("For sourcetype=jpa a exportsysuid is expected");
        }
 
        // create own importer
        JPAImporter jpaImporter = new JPAImporter(null);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read from JPA exportsysuid: '" + exportSysUID + "'");
        }

        // read data
        DataDomain jpaNode = 
                        jpaImporter.getBaseNodeBySysUID(exportSysUID);
        BaseNode baseNode = ((BaseNode)jpaNode);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("result from JPA  for sysUID: '" + exportSysUID + "' " + 
                            baseNode.getNameForLogger());
        }
        
        // add to masternode
        baseNode.setParentNode(masterNode);
    }
    
    public void importDataToMasterNodeFromExcel(DataDomain masterNode) throws Exception {
        // config
        String delimiter = cmdLine.getOptionValue("delimiter", "\t");

        // create Importer
        String pplSource = this.extractDataFromExcel();

        // parse PPL-source
        pplImporter.extractNodesFromLines(masterNode, pplSource, delimiter);
    }



    public String extractDataFromExcel() throws Exception {
        // check srcFile
        if (cmdLine.getArgs().length <= 0) {
            throw new IllegalArgumentException("Import from Excel-File requires filename.");
        }
        String srcFile = cmdLine.getArgs()[0];
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
        String strPathIdDB = cmdLine.getOptionValue("pathiddb", null);
        if (strPathIdDB != null) {
            nodeNumberService.initNextNodeNumbersFromFile(strPathIdDB);
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

    public void importDataToMasterNodeFromWiki(DataDomain masterNode) throws Exception {
        // config
        String delimiter = cmdLine.getOptionValue("delimiter", "\t");

        // parse PPL-source
        String pplSource = this.extractDataFromWiki();
        pplImporter.extractNodesFromLines(masterNode, pplSource, delimiter);
    }
    
    
    public String extractDataFromWiki() throws Exception {
        // check srcFile
        if (cmdLine.getArgs().length <= 0) {
            throw new IllegalArgumentException("Import from Wiki-File requires filename.");
        }
        String srcFile = cmdLine.getArgs()[0];
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read from PPL file:" + srcFile);
        }

        // Parser+Options anlegen
        WikiImportOptions inputOptions = new WikiImportOptions();
        inputOptions.flgReadList = true;
        inputOptions.flgReadUe = true;
        inputOptions.flgReadWithStatusOnly = cmdLine.hasOption("s");
        inputOptions.flgReadWithWFStatusOnly = cmdLine.hasOption("w");
        if (cmdLine.hasOption("l")) {
            inputOptions.flgReadList = false;
        }
        if (cmdLine.hasOption("u")) {
            inputOptions.flgReadUe = false;
        }
        inputOptions.strReadIfStatusInListOnly = 
                cmdLine.getOptionValue("onlyifstateinlist", null);
        WikiImporter wikiImporter = new WikiImporter(inputOptions);
        
        // gets NodeNumberService
        NodeNumberService nodeNumberService = 
                pplImporter.getNodeFactory().getMetaDataService().getNodeNumberService();
        
        // Id-Datei einlesen
        String strPathIdDB = cmdLine.getOptionValue("pathiddb", null);
        if (strPathIdDB != null) {
            nodeNumberService.initNextNodeNumbersFromFile(strPathIdDB);
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
                resBuf.append(wk.hirarchy).append("\n");
            }
        }
        return resBuf.toString();
    }
    
    public void importDataToMasterNode(DataDomain masterNode) throws Exception {
        // check datasource
        String sourceType = cmdLine.getOptionValue("sourcetype", defaultSourceType);
        if (sourceType.equalsIgnoreCase("jpa")) {
            // from jpa
            this.importDataToMasterNodeFromJPA(masterNode);
        } else if (sourceType.equalsIgnoreCase("excel")) {
            // from excel
            this.importDataToMasterNodeFromExcel(masterNode);
        } else if (sourceType.equalsIgnoreCase("wiki")) {
            // from wiki
            this.importDataToMasterNodeFromWiki(masterNode);
        } else if (sourceType.equalsIgnoreCase("ppl")) {
            // default: ppl
            this.importDataToMasterNodeFromPPLFile(masterNode);
        } else {
            throw new IllegalArgumentException("if sourcetype is set ist must be jpa,ppl,wiki or excel");
        }
    }

    /*
     * ##############
     * service-functions
     * ##############
     */
    protected void createPPLImporter() {
        // create commonImporter
        pplImporter = new PPLImporter(null);
    }
    

    public PPLImporter getPPLImporter() {
        return pplImporter;
    }
    
    public void setCmdLine(CommandLine cmdLine) {
        this.cmdLine = cmdLine;
    }
}
