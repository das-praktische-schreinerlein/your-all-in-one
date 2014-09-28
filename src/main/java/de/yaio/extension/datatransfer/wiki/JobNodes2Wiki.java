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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import de.yaio.app.CmdLineJob;
import de.yaio.app.Configurator;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.extension.datatransfer.common.CommonImporter;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     job to import nodes in PPL-Format and output as Wiki
 * 
 * @package de.yaio.extension.datatransfer.wiki
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobNodes2Wiki extends CmdLineJob {

    /**
     * the exporter to format the masternode-data 
     */
    public Exporter exporter;
    protected CommonImporter commonImporter;
    
    private static final Logger LOGGER =
        Logger.getLogger(JobNodes2Wiki.class);

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     job to import nodes in PPL-Format and output as Wiki
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the application
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     * @param args the command line arguments
     */
    public JobNodes2Wiki(String[] args) {
        super(args);
        createCommonImporter();
    }

    @Override
    protected Options addAvailiableCmdLineOptions() throws Throwable {
        Options availiableCmdLineOptions = 
                        Configurator.getNewOptionsInstance();

        // add Options
        commonImporter.addAvailiableCommonCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableExcelCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableWikiCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableJPACmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiablePPLCmdLineOptions(availiableCmdLineOptions);

        // my OutputOptions
        this.addAvailiableCommonOutputCmdLineOptions(availiableCmdLineOptions);
        this.addAvailiableOutputCmdLineOptions(availiableCmdLineOptions);
        
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        
        return availiableCmdLineOptions;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     CLI
     * <h4>FeatureDescription:</h4>
     *     add common output-options to the availiableCmdLineOptions
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>update availiableCmdLineOptions
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI
     * @param availiableCmdLineOptions - the container with the availiableCmdLineOptions
     */
    protected Options addAvailiableCommonOutputCmdLineOptions(Options availiableCmdLineOptions) throws Throwable {
        // Mastername
        Option masternameOption = new Option("m", "mastername", true,
                "Name of Masternode (default Master)");
        masternameOption.setRequired(false);
        availiableCmdLineOptions.addOption(masternameOption);

        // Maxebene
        Option maxEbeneOption = new Option("e", "maxEbene", true,
                "Max Darstellungsebene (default 9999)");
        maxEbeneOption.setRequired(false);
        availiableCmdLineOptions.addOption(maxEbeneOption);

        return availiableCmdLineOptions;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     CLI
     * <h4>FeatureDescription:</h4>
     *     add special wiki output-options to the availiableCmdLineOptions
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>update availiableCmdLineOptions
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI
     * @param availiableCmdLineOptions - the container with the availiableCmdLineOptions
     */
    protected Options addAvailiableOutputCmdLineOptions(Options availiableCmdLineOptions) throws Throwable {

        // Show ChildrenSum
        Option flgShowChildrenSumOption = new Option("", "calcsum", false,
                "Show ChildrenSum (default false)");
        flgShowChildrenSumOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowChildrenSumOption);

        // Calc+Show 
        Option flgReChildrenSumOption = new Option("C", "recalcsum", false,
                "Calc+Show ChildrenSum (default false)");
        flgReChildrenSumOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgReChildrenSumOption);

        // Show DescData
        Option flgShowDescOption = new Option("d", "showdesc", false,
                "Show DescData (default false)");
        flgShowDescOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowDescOption);

        // Trim DescData
        Option flgTrimDescOption = new Option("t", "trim", false,
                "Trim DescData (default false)");
        flgTrimDescOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgTrimDescOption);

        // Show PlanData
        Option flgShowPlanOption = new Option("p", "showplan", false,
                "Show PlanData (default false)");
        flgShowPlanOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowPlanOption);

        // Show IstData
        Option flgShowIstOption = new Option("i", "showist", false,
                "Show IstData (default false)");
        flgShowIstOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowIstOption);

        // Show State
        Option flgShowState = new Option("s", "showstate", false,
                "Show State/type (default false)");
        flgShowState.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowState);

        // Intend
        Option itendOption = new Option("", "intend", true,
                "Einrueckung der Daten-Bloecke Leerzeichen (default 0)");
        itendOption.setRequired(false);
        availiableCmdLineOptions.addOption(itendOption);

        // IntendLi
        Option itendLiOption = new Option("", "intendli", true,
                "Einrueckung Unternodes - Leerzeichen (default 0)");
        itendLiOption.setRequired(false);
        availiableCmdLineOptions.addOption(itendLiOption);

        // IntendSys
        Option itendSysOption = new Option("", "intendsys", true,
                "Einrueckung Sys-Bloecke hinter den Datenbloecken zusaetzlich zu intend - Leerzeichen (default 80)");
        itendSysOption.setRequired(false);
        availiableCmdLineOptions.addOption(itendSysOption);

        // MaxUeebene
        Option maxUeEbeneOption = new Option("U", "maxUeEbene", true,
                "Max Ue-Darstellungsebene (default 0 - keine Ue)");
        maxUeEbeneOption.setRequired(false);
        availiableCmdLineOptions.addOption(maxUeEbeneOption);

        // Show DocLayout
        Option flgShowDocLayoutOption = new Option("l", "showdoclayout", false,
                "Show DocLayout (default false)");
        flgShowDocLayoutOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowDocLayoutOption);

        // ProcessDocLayout-Option
        Option flgProcessDocLayoutOption = new Option("", "processdoclayout", 
                false, "Process dochtml layout command");
        flgProcessDocLayoutOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgProcessDocLayoutOption);

        // Dont Show Sysdata
        Option flgShowNoSysDataOption = new Option("", "shownosysdata", false,
                "Dont Show SysData (default false)");
        flgShowNoSysDataOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowNoSysDataOption);

        // Dont Show Metadata
        Option flgShowNoMetaDataOption = new Option("", "shownometadata", false,
                "Dont Show MetaData (default false)");
        flgShowNoMetaDataOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowNoMetaDataOption);

        // InputState
        Option flgShowIfStateList = new Option("", "onlyifstateinlist", true,
                "Show only if State in List (CSV)");
        flgShowIfStateList.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowIfStateList);

        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() throws Throwable {
        // init
        createExporter();
        
        // Mastername extrahieren
        String masterName = Configurator.getInstance().getCommandLine().getOptionValue("m", "Master");
        DataDomain masterNode = createMasternode(masterName);

        // Output-Options parsen
        OutputOptions oOptions = 
                this.getOutputOptions();

        // Daten parsen
        importDataToMasterNode(masterNode);;

        // Masternode filtern
        masterNode = exporter.filterNodes(masterNode, oOptions);

        // Masternode ausgeben
        this.publishResult(exporter, masterNode, oOptions);
    }

    // #############
    // common functions
    // #############

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     create the masternode on which all other nodes are added
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue masternode
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param name - name of the masternode
     * @return masternode - the masternode on which all other nodes are added
     * @throws Throwable - parse/io-Exceptions possible
     */
    public DataDomain createMasternode(String name) throws Throwable {
        DataDomain masterNode  = commonImporter.getPPLImporter().createNodeObjFromText(1, name, name, null);
        return masterNode;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     import the data from source configured by cmdline-options and add 
     *     them to the masterNode 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates masternode
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param masterNode - the masternode on which all other nodes are added
     * @throws Exception - parse/io-Exceptions possible
     */
    public void importDataToMasterNode(DataDomain masterNode) throws Exception {
        commonImporter.importDataToMasterNode(masterNode);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     publish the masternode and all children with the help of exporter
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>prints on STDOUT
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param exporter - exporter to format the output
     * @param masterNode - the masternode to export
     * @param oOptions - Outputoptions
     * @throws Exception - parse/io-Exceptions possible
     */
    public void publishResult(Exporter exporter, DataDomain masterNode, 
            OutputOptions oOptions) throws Exception {
        System.out.println(
                exporter.getMasterNodeResult(masterNode, oOptions));
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     get the Outputoptions for export from commandline
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>Returnvalue OutputOptions - the parsed options from commandline
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @return oOptions - Outputoptions
     * @throws Exception  - parse-Exceptions possible
     */
    public OutputOptions getOutputOptions() throws Exception {
        // Konfiguration
        CommandLine cmdLine = Configurator.getInstance().getCommandLine();
        OutputOptions oOptions = new OutputOptionsImpl();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("DefaultOutputOptions: " + oOptions);
        oOptions.setFlgShowPlan(cmdLine.hasOption("p"));
        oOptions.setFlgShowIst(cmdLine.hasOption("i"));
        oOptions.setFlgShowState(cmdLine.hasOption("s"));
        oOptions.setFlgShowType(cmdLine.hasOption("s"));
        oOptions.setFlgShowDesc(cmdLine.hasOption("d"));
        oOptions.setFlgShowChildrenSum(cmdLine.hasOption("calcsum"));
        oOptions.setFlgShowDocLayout(cmdLine.hasOption("l"));
        oOptions.setFlgShowSysData(cmdLine.hasOption("shownosysdata") == false);
        oOptions.setFlgShowMetaData(cmdLine.hasOption("shownometadata") == false);
        oOptions.setFlgChildrenSum(cmdLine.hasOption("C"));
        oOptions.setFlgTrimDesc(cmdLine.hasOption("t"));
        oOptions.setMaxEbene(Integer.parseInt(
                cmdLine.getOptionValue("e", 
                        new Integer(oOptions.getMaxEbene()).toString())));
        oOptions.setMaxUeEbene(Integer.parseInt(
                cmdLine.getOptionValue("U", 
                        new Integer(oOptions.getMaxUeEbene()).toString())));
        oOptions.setIntend(Integer.parseInt(
                cmdLine.getOptionValue("intend", 
                        new Integer(oOptions.getIntend()).toString())));
        oOptions.setIntendLi(Integer.parseInt(
                cmdLine.getOptionValue("intendli", 
                        new Integer(oOptions.getIntendLi()).toString())));
        oOptions.setIntendSys(Integer.parseInt(
                cmdLine.getOptionValue("intendsys", 
                        new Integer(oOptions.getIntendSys()).toString())));
        oOptions.setFlgProcessDocLayout(cmdLine.hasOption("processdoclayout"));
        oOptions.setStrReadIfStatusInListOnly(cmdLine.getOptionValue("onlyifstateinlist", null));
        
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("OutputOptions after parsing: " + oOptions);
        return oOptions;
    }

    // ######################
    // specific functions
    // ######################
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     create the exporter for the export with publishResult
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates MemberVar exporter - to format the output
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     */
    public void createExporter() {
        exporter = new WikiExporter();
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     create the commonly used importer to imports the data from differenet 
     *     sourcetypes
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates MemberVar commonImporter - for the import
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     */
    protected void createCommonImporter() {
        // create commonImporter
        commonImporter = new CommonImporter("ppl");
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Logging
     * <h4>FeatureDescription:</h4>
     *     get the Class-logger
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Logger - use it !!!!
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Logging
     * @return logger - the logger
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     CLI
     * <h4>FeatureDescription:</h4>
     *     Main-method to start the application
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the application
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JobNodes2Wiki me = new JobNodes2Wiki(args);
        me.startJobProcessing();
    }
}
