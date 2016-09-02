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
package de.yaio.app.cli.converter;

import de.yaio.app.cli.YaioCmdLineJob;
import de.yaio.app.cli.importer.ExtendedCommonImporter;
import de.yaio.app.config.ContextHelper;
import de.yaio.app.config.JobConfig;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.exporter.Exporter;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.app.extension.datatransfer.wiki.WikiExporter;
import de.yaio.commons.cli.CmdLineHelper;
import de.yaio.commons.config.ConfigurationOption;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

/** 
 * job to import nodes in PPL-Format and output as Wiki
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public class JobNodes2Wiki extends YaioCmdLineJob {

    private static final Logger LOGGER = Logger.getLogger(JobNodes2Wiki.class);

    /**
     * the converter to format the masternode-data
     */
    protected Exporter exporter;
    protected ExtendedCommonImporter commonImporter;
    
    /** 
     * job to import nodes in PPL-Format and output as Wiki
     * @param args                   the command line arguments
     */
    public JobNodes2Wiki(final String[] args) {
        super(args);
        createCommonImporter();
    }

    @Override
    protected Options addAvailiableCmdLineOptions() {
        Options availiableCmdLineOptions = 
                        CmdLineHelper.getNewOptionsInstance();

        // add Options
        commonImporter.addAvailiableCommonCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableExcelCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableWikiCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableJPACmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiablePPLCmdLineOptions(availiableCmdLineOptions);

        // my OutputOptions
        this.addAvailiableCommonOutputCmdLineOptions(availiableCmdLineOptions);
        this.addAvailiableOutputCmdLineOptions(availiableCmdLineOptions);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }

    /** 
     * add common output-options to the availiableCmdLineOptions
     * @param availiableCmdLineOptions the container with the availiableCmdLineOptions
     */
    protected Options addAvailiableCommonOutputCmdLineOptions(final Options availiableCmdLineOptions) {
        // Mastername
        Option masternameOption = new Option("m", "mastername", true, "Name of Masternode (default Master)");
        masternameOption.setRequired(false);
        availiableCmdLineOptions.addOption(masternameOption);

        // Maxebene
        Option maxEbeneOption = new Option("e", "maxEbene", true, "Max Darstellungsebene (default 9999)");
        maxEbeneOption.setRequired(false);
        availiableCmdLineOptions.addOption(maxEbeneOption);

        return availiableCmdLineOptions;
    }

    /** 
     * add special wiki output-options to the availiableCmdLineOptions
     * @param availiableCmdLineOptions the container with the availiableCmdLineOptions
     */
    protected Options addAvailiableOutputCmdLineOptions(final Options availiableCmdLineOptions) {

        // Show ChildrenSum
        Option flgShowChildrenSumOption = new Option("", "calcsum", false, "Show ChildrenSum (default false)");
        flgShowChildrenSumOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowChildrenSumOption);

        // Recalc data 
        Option flgRecalcOption = new Option("", "recalc", false, "recalc data (default false)");
        flgRecalcOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgRecalcOption);

        // Show DescData
        Option flgShowDescOption = new Option("d", "showdesc", false, "Show DescData (default false)");
        flgShowDescOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowDescOption);

        // Trim DescData
        Option flgTrimDescOption = new Option("t", "trim", false, "Trim DescData (default false)");
        flgTrimDescOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgTrimDescOption);

        // Show PlanData
        Option flgShowPlanOption = new Option("p", "showplan", false, "Show PlanData (default false)");
        flgShowPlanOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowPlanOption);

        // Show IstData
        Option flgShowIstOption = new Option("i", "showist", false, "Show IstData (default false)");
        flgShowIstOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowIstOption);

        // Show State
        Option flgShowState = new Option("s", "showstate", false, "Show State/type (default false)");
        flgShowState.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowState);

        // Intend
        Option itendOption = new Option("", "intend", true, "Einrueckung der Daten-Bloecke Leerzeichen (default 0)");
        itendOption.setRequired(false);
        availiableCmdLineOptions.addOption(itendOption);

        // IntendLi
        Option itendLiOption = new Option("", "intendli", true, "Einrueckung Unternodes - Leerzeichen (default 0)");
        itendLiOption.setRequired(false);
        availiableCmdLineOptions.addOption(itendLiOption);

        // IntendSys
        Option itendSysOption = new Option("", "intendsys", true,
                "Einrueckung Sys-Bloecke hinter den Datenbloecken zusaetzlich zu intend - Leerzeichen (default 80)");
        itendSysOption.setRequired(false);
        availiableCmdLineOptions.addOption(itendSysOption);

        // MaxUeebene
        Option maxUeEbeneOption = new Option("U", "maxUeEbene", true, "Max Ue-Darstellungsebene (default 0 - keine Ue)");
        maxUeEbeneOption.setRequired(false);
        availiableCmdLineOptions.addOption(maxUeEbeneOption);

        // Show DocLayout
        Option flgShowDocLayoutOption = new Option("l", "showdoclayout", false, "Show DocLayout (default false)");
        flgShowDocLayoutOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowDocLayoutOption);

        // ProcessDocLayout-ConfigurationOption
        Option flgProcessDocLayoutOption = new Option("", "processdoclayout", false, "Process dochtml layout command");
        flgProcessDocLayoutOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgProcessDocLayoutOption);

        // ProcessMarkdown-ConfigurationOption
        Option flgProcessMarkdownOption = new Option("", "processmarkdown", false, "Process Markdown layout command");
        flgProcessMarkdownOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgProcessMarkdownOption);

        // Dont Show Sysdata
        Option flgShowNoSysDataOption = new Option("", "shownosysdata", false, "Dont Show SysData (default false)");
        flgShowNoSysDataOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowNoSysDataOption);

        // Dont Show Metadata
        Option flgShowNoMetaDataOption = new Option("", "shownometadata", false, "Dont Show MetaData (default false)");
        flgShowNoMetaDataOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowNoMetaDataOption);

        // InputState
        Option flgShowIfStateList = new Option("", "onlyifstateinlist", true, "Show only if State in List (CSV)");
        flgShowIfStateList.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowIfStateList);

        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() {
        // init
        createExporter();
        
        // Mastername extrahieren
        String masterName = ConfigurationOption.stringValueOf(this.getConfiguration().getCliOption("m", "Master"));
        DataDomain masterNode;
        try {
            masterNode = createMasternode(masterName);
        } catch (ParserException ex) {
            throw new IllegalArgumentException("cant create masternode - error while parsing masternode", ex);
        }

        // Output-Options parsen
        OutputOptions oOptions =
                this.getOutputOptions();

        // Daten parsen
        importDataToMasterNode(masterNode);

        // Masternode filtern
        masterNode = exporter.filterNodes(masterNode, oOptions);

        // Masternode ausgeben
        try {
            this.publishResult(exporter, masterNode, oOptions);
        } catch (ConverterException ex) {
            throw new IllegalArgumentException("cant pubish masternode - error while converting masternode", ex);
        }
    }

    // #############
    // common functions
    // #############

    /** 
     * create the masternode on which all other nodes are added
     * @param name                   name of the masternode
     * @return                       masternode - the masternode on which all other nodes are added
     * @throws ParserException       parse/io-Exceptions possible
     */
    public DataDomain createMasternode(final String name) throws ParserException {
        return commonImporter.getPPLImporter().createNodeObjFromText(1, name, name, null);
    }

    /** 
     * import the data from source configured by cmdline-options and add 
     * them to the masterNode 
     * @param masterNode             the masternode on which all other nodes are added
     */
    public void importDataToMasterNode(final DataDomain masterNode) {
        commonImporter.importDataToMasterNode(masterNode);
    }

    /** 
     * publish the masternode and all children with the help of converter
     * @param exporter               converter to format the output
     * @param masterNode             the masternode to export
     * @param oOptions               Outputoptions
     * @throws ConverterException             parse/io-Exceptions possible
     */
    public void publishResult(final Exporter exporter, final DataDomain masterNode, 
            final OutputOptions oOptions) throws ConverterException {
        System.out.println(exporter.getMasterNodeResult(masterNode, oOptions));
    }

    /** 
     * get the Outputoptions for export from commandline
     * @return                       oOptions - Outputoptions
     */
    public OutputOptions getOutputOptions() {
        // Konfiguration
        CommandLine cmdLine = this.getCmdLineHelper().getCommandLine();
        OutputOptions oOptions = new OutputOptionsImpl();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("DefaultOutputOptions: " + oOptions);
        }
        oOptions.setFlgShowPlan(this.getConfiguration().hasCliOption("p"));
        oOptions.setFlgShowIst(this.getConfiguration().hasCliOption("i"));
        oOptions.setFlgShowState(this.getConfiguration().hasCliOption("s"));
        oOptions.setFlgShowType(this.getConfiguration().hasCliOption("s"));
        oOptions.setFlgShowDesc(this.getConfiguration().hasCliOption("d"));
        oOptions.setFlgShowChildrenSum(this.getConfiguration().hasCliOption("calcsum"));
        oOptions.setFlgShowDocLayout(this.getConfiguration().hasCliOption("l"));
        oOptions.setFlgShowSysData(!this.getConfiguration().hasCliOption("shownosysdata"));
        oOptions.setFlgShowMetaData(!this.getConfiguration().hasCliOption("shownometadata"));
        oOptions.setFlgRecalc(this.getConfiguration().hasCliOption("recalc"));
        oOptions.setFlgTrimDesc(this.getConfiguration().hasCliOption("t"));
        oOptions.setMaxEbene(Integer.parseInt(ConfigurationOption.stringValueOf(
                this.getConfiguration().getCliOption("e", Integer.toString(oOptions.getMaxEbene())))));
        oOptions.setMaxUeEbene(ConfigurationOption.integerValueOf(
                this.getConfiguration().getCliOption("U", Integer.toString(oOptions.getMaxUeEbene()))));
        oOptions.setIntend(ConfigurationOption.integerValueOf(
                this.getConfiguration().getCliOption("intend", Integer.toString((oOptions.getIntend())))));
        oOptions.setIntendLi(ConfigurationOption.integerValueOf(
                this.getConfiguration().getCliOption("intendli", Integer.toString(oOptions.getIntendLi()))));
        oOptions.setIntendSys(ConfigurationOption.integerValueOf(
                this.getConfiguration().getCliOption("intendsys", Integer.toString(oOptions.getIntendSys()))));
        oOptions.setFlgProcessDocLayout(this.getConfiguration().hasCliOption("processdoclayout"));
        oOptions.setStrReadIfStatusInListOnly(ConfigurationOption.stringValueOf(
                this.getConfiguration().getCliOption("onlyifstateinlist")));
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("OutputOptions after parsing: " + oOptions);
        }
        return oOptions;
    }

    @Override
    protected void configureContext() {
        String sourceType = ConfigurationOption.stringValueOf(this.getConfiguration().getCliOption("sourcetype", ""));
        if ("jpa".equalsIgnoreCase(sourceType)) {
            ContextHelper.getInstance().addSpringConfig(JobConfig.class);
            // initApplicationContext
            ContextHelper.getInstance().getSpringApplicationContext();
            ContextHelper.getInstance().autowireService(commonImporter);
            ContextHelper.getInstance().autowireService(exporter);
        }
    }


    // ######################
    // specific functions
    // ######################
    
    /** 
     * create the converter for the export with publishResult
     */
    public void createExporter() {
        exporter = new WikiExporter();
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
     * get the Class-logger
     * @return                       logger - the logger
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    /** 
     * Main-method to start the application
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        JobNodes2Wiki me = new JobNodes2Wiki(args);
        me.startJobProcessing();
    }
}
