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
package de.yaio.utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * <h4>FeatureDomain:</h4>
 *     Tools - CLI-Handling
 * <h4>FeatureDescription:</h4>
 *     baseclass for commandlinejobs
 * <h4>must be implemented:</h4>
 *     <ul>
 *     <li>validateCmdLine
 *     </ul>
 * <h1>to configure:</h1>
 *     <ul>
 *     <li>genAvailiableCmdLineOptions
 *     </ul>
 *
 * @package base
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @copyright Copyright (c) 2013, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public abstract class CmdLineJob {

    private final static Logger LOGGER = Logger.getLogger(CmdLineJob.class);

    public static ApplicationContext springApplicationContext = null;
    
    public static final int CONST_EXITCODE_OK = 0;
    public static final int CONST_EXITCODE_FAILED_ARGS = 1;
    public static final int CONST_EXITCODE_FAILED_CONFIG = 2;
    public static final int CONST_EXITCODE_FAILED_JOB = 3;

    protected String[] cmdLineArgs = null;
    protected Options availiableCmdLineOptions = null;
    protected CommandLine cmdLine = null;
    protected String jobConfFile = null;

    public CmdLineJob(String[] args) {
        this.cmdLineArgs = args;
    }

    /**
     * <h1>Bereich:</h1>
     *     Tools - CLI-Config
     * <h1>Funktionalitaet:</h1>
     *     konfiguriert die verfuegbaren CLI-Optionen -> sollte ueberladen werden
     * <h1>Nebenwirkungen:</h1>
     *     Rueckgabe als Options
     * @return Options
     * @throws Throwable
     */
    protected Options genAvailiableCmdLineOptions() throws Throwable {

        Options availiableCmdLineOptions = new Options();

        // Config-File
        Option configOption = new Option("c", "config", true,
                "comma separated list of JobConfig property files");
        configOption.setRequired(true);
        availiableCmdLineOptions.addOption(configOption);

        // Hilfe-Option
        Option helpOption = new Option("h", "help", false, "usage");
        helpOption.setRequired(false);
        availiableCmdLineOptions.addOption(helpOption);

        return availiableCmdLineOptions;
    }

    /**
     * <h1>Bereich:</h1>
     *     Tools - CLI-Handling
     * <h1>Funktionalitaet:</h1>
     *     liefert die verfuegbaren CLI-Optionen
     * <h1>Nebenwirkungen:</h1>
     *     Rueckgabe als Options
     * @return Options
     * @throws Throwable
     */
    protected Options getAvailiableCmdLineOptions() throws Throwable {
        return this.availiableCmdLineOptions;
    }

    /**
     * <h1>Bereich:</h1>
     *     Tools - CLI-Handling
     * <h1>Funktionalitaet:</h1>
     *     erzeugt aus den CMD-Args ein CLI-Commandline-Object
     * <h1>Nebenwirkungen:</h1>
     *     Rueckgabe als CommandLine
     * @param cmdArgs - Parameter aus z.B. main
     * @param availiableCmdLineOptions - verfuegbare CLI-Optionen
     * @return CommandLine
     * @throws Throwable
     */
    protected CommandLine genCommandLineFromCmdArgs(String[] cmdArgs,
            Options availiableCmdLineOptions) throws Throwable {
        CommandLineParser parser = new PosixParser();
        return parser.parse(availiableCmdLineOptions, cmdArgs);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - CLI-Handling
     * <h4>FeatureDescription:</h4>
     *     return current CLI-Commandline
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue CommandLine - current CLI-Commandline
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI-Handling
     * @return CommandLine - current CLI-Commandline
     * @throws Throwable
     */
    public CommandLine getCmdLine() throws Throwable {
        return this.cmdLine;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - CLI-Handling
     * <h4>FeatureDescription:</h4>
     *     return current CMD-Args
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String[] - current CMD-Args
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI-Handling
     * @return String[] - current CMD-Args
     * @throws Throwable
     */
    public String[] getCmdLineArgs() throws Throwable  {
        return this.cmdLineArgs;
    }


    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - CLI-Handling
     * <h4>FeatureDescription:</h4>
     *     checks weather Commandline is valid
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - valid Commandline?
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI-Handling
     * @return boolean - valid Commandline?
     * @throws Throwable
     */
    protected boolean validateCmdLine(CommandLine cmdLine) throws Throwable {
        return true;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - CLI-Handling
     * <h4>FeatureDescription:</h4>
     *     returns jobname (Classname without package)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - jobname (Classname without package)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI-Handling
     * @return String - jobname (Classname without package)
     * @throws Throwable
     */
    protected String getJobName() throws Throwable  {
        return this.getClass().getName();
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - CLI-Handling
     * <h4>FeatureDescription:</h4>
     *     prints usage of current commandlineoptions
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>prints on STDOUT - usage
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI-Handling
     * @throws Throwable
     */
    protected void printUsage() throws Throwable  {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(this.getJobName(), this.availiableCmdLineOptions);
    }

    protected boolean loadConfigFile(String confFile) throws Throwable {
        ///ToDo
        return true;
    }

    public void startJobProcessing() {
        try {
            // config cmdArgs
            LOGGER.info("configure CmdArgs");
            this.availiableCmdLineOptions = this.genAvailiableCmdLineOptions();

            // parse cmdArgs
            LOGGER.info("parse CmdArgs");
            this.cmdLine = this.genCommandLineFromCmdArgs(this.cmdLineArgs, this.availiableCmdLineOptions);

            // check for unknown Args
            LOGGER.info("used CmdLineArgs: " + this.cmdLineArgs);
            if (this.cmdLine != null) {
                LOGGER.info("unknown CmdLineArgs: " + this.cmdLine.getArgs());
            }

            // validate cmdLine
            LOGGER.info("validate CmdLine");
            if (! this.validateCmdLine(this.cmdLine)) {
                logErrorMsg("Illegal CmdArgs: print Usage");
                this.printUsage();
                LOGGER.info("Exit: 1");
                System.exit(CONST_EXITCODE_FAILED_ARGS);
            }

            // print Usage
            if (this.cmdLine.hasOption("h")) {
                LOGGER.info("print Usage");
                this.printUsage();
                LOGGER.info("Exit: " + CONST_EXITCODE_OK);
                System.exit(CONST_EXITCODE_OK);
            }

            // load Config
            LOGGER.info("load Config");
            this.jobConfFile = this.cmdLine.getOptionValue("c");
            if (this.jobConfFile != null) {
                if (!loadConfigFile(this.jobConfFile)) {
                    logErrorMsg("cant load Config: " + this.jobConfFile);
                    logErrorMsg("Exit: " + CONST_EXITCODE_FAILED_CONFIG);
                    System.exit(CONST_EXITCODE_FAILED_CONFIG);
                }
            }

            LOGGER.info("start initJob");
            this.initJob();
            LOGGER.info("done initJob");

            LOGGER.info("start doJob");
            this.doJob();
            LOGGER.info("done doJob");

            LOGGER.info("start cleanUpAfterJob");
            this.cleanUpAfterJob();
            LOGGER.info("done cleanUpAfterJob");

        } catch(Throwable e) {
            // Catch Error
            try {
                LOGGER.info("start cleanUpAfterJob when Error");
                this.cleanUpAfterJob();
                LOGGER.info("done cleanUpAfterJob when Error");
            } catch (Throwable e2){
                // Log Error while cleanUp
                this.handleThrowable(e2, false);
            }

            // Log Error
            this.handleThrowable(e, true);
        }
    }

    public static void logErrorMsg(String errorMsg) {
        System.out.println(errorMsg);
        LOGGER.fatal(errorMsg);
    }

    public void handleThrowable(Throwable e, boolean flgExit) {

        String errorMsg = "JOB - Exception:\n" + e.getMessage();
        e.printStackTrace();

        logErrorMsg(errorMsg);

        if (flgExit) {
            int exitCode = CONST_EXITCODE_FAILED_JOB;
            logErrorMsg("Exit: " + exitCode);
            System.exit(exitCode);
        }
    }

    public static void initApplicationContext() {
        springApplicationContext = 
            new ClassPathXmlApplicationContext("/META-INF/spring/applicationContext.xml");
    }

    protected void initJob() throws Throwable {
    }

    protected abstract void doJob() throws Throwable;



    protected void cleanUpAfterJob() throws Throwable {
        // TODO: hack to close HSLDB-connection -> Hibernate don't close the 
        //       database and so the content is not written to file
        org.hsqldb.DatabaseManager.closeDatabases(CONST_EXITCODE_OK);
    }
}
