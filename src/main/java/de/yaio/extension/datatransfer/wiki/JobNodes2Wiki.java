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

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.utils.CmdLineJob;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     job for import of Nodes in PPL-Format and output in Wiki-format
 * 
 * @package de.yaio.extension.datatransfer.wiki
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobNodes2Wiki extends CmdLineJob {

    public Exporter exporter;
    public PPLImporter importer;


    public JobNodes2Wiki(String[] args) {
        super(args);
        createExporter();
        createImporter();
    }


    @Override
    protected Options genAvailiableCmdLineOptions() throws Throwable {
        Options availiableCmdLineOptions = new Options();

        // Hilfe-Option
        Option helpOption = new Option("h", "help", false, "usage");
        helpOption.setRequired(false);
        availiableCmdLineOptions.addOption(helpOption);

        // Hirarchy-Delimiter  
        Option delimiterOption = new Option("", "delimiter", true,
                "Hirarchy-Delimiter (default TAB)");
        delimiterOption.setRequired(false);
        availiableCmdLineOptions.addOption(delimiterOption);

        // Show DescData
        Option flgShowChildrenSumOption = new Option("c", "calcsum", false,
                "Calc+Show ChildrenSum (default false)");
        flgShowChildrenSumOption.setRequired(false);
        availiableCmdLineOptions.addOption(flgShowChildrenSumOption);

        // Show DescData
        Option flgReChildrenSumOption = new Option("C", "recalcsum", false,
                "Show ChildrenSum (default false)");
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

        // Intend
        Option itendOption = new Option("", "intend", true,
                "Einr�ckung der Daten-Bl�cke Leerzeichen (default 0)");
        itendOption.setRequired(false);
        availiableCmdLineOptions.addOption(itendOption);

        // IntendLi
        Option itendLiOption = new Option("", "intendli", true,
                "Einr�ckung Unternodes - Leerzeichen (default 0)");
        itendLiOption.setRequired(false);
        availiableCmdLineOptions.addOption(itendLiOption);

        // IntendSys
        Option itendSysOption = new Option("", "intendsys", true,
                "Einr�ckung Sys-Bl�cke hinter den Datenbl�cken zus�tzlich zu intend - Leerzeichen (default 80)");
        itendSysOption.setRequired(false);
        availiableCmdLineOptions.addOption(itendSysOption);

        // MaxUeebene
        Option maxUeEbeneOption = new Option("U", "maxUeEbene", true,
                "Max Ue-Darstellungsebene (default 0 - keine Ue)");
        maxEbeneOption.setRequired(false);
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
        // initialsieren
        createExporter();
        createImporter();
        
        // Mastername extrahieren
        String masterName = this.cmdLine.getOptionValue("m", "Master");
        DataDomain masterNode = createMasternode(masterName);

        // Output-Options parsen
        OutputOptions oOptions = 
                this.getOutputOptions(this.cmdLine);

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
    public DataDomain createMasternode(String name) throws Throwable {
        DataDomain masterNode  = importer.createNodeObjFromText(1, name, name, null);
        return masterNode;
    }

    public void importDataToMasterNode(DataDomain masterNode) throws Exception {
        // Delimiter
        String delimiter = this.cmdLine.getOptionValue("delimiter", "\t");

        // aus Datei oder nur Test ??
        if (this.cmdLine.getArgs().length > 0) {
            // aus datei
            importer.extractNodesFromFile(masterNode, this.cmdLine.getArgs()[0], delimiter);
        } else {
            // nur Test
            importer.extractNodesFromLines(masterNode, "", delimiter); // TODO
        }
    }

    public void publishResult(Exporter exporter, DataDomain masterNode, 
            OutputOptions oOptions) throws Exception {
        System.out.println(
                exporter.getMasterNodeResult(masterNode, oOptions));
    }

    public OutputOptions getOutputOptions(
            CommandLine cmdLine) {
        // Konfiguration
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setFlgShowPlan(cmdLine.hasOption("p"));
        oOptions.setFlgShowIst(cmdLine.hasOption("i"));
        oOptions.setFlgShowState(cmdLine.hasOption("s"));
        oOptions.setFlgShowType(cmdLine.hasOption("s"));
        oOptions.setFlgShowDesc(cmdLine.hasOption("d"));
        oOptions.setFlgShowChildrenSum(cmdLine.hasOption("c"));
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
        oOptions.setStrReadIfStatusInListOnly(this.cmdLine.getOptionValue("onlyifstateinlist", null));

        return oOptions;
    }

    // ######################
    // specific functions
    // ######################
    public void createExporter() {
        exporter = new WikiExporter();
    }

    public void createImporter() {
        importer = new PPLImporter(null);
    }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JobNodes2Wiki me = new JobNodes2Wiki(args);
        me.startJobProcessing();
    }
}
