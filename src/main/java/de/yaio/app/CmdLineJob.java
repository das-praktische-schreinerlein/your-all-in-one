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
package de.yaio.app;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;


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
 * @package de.yaio.utils
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @copyright Copyright (c) 2013, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public abstract class CmdLineJob {

    /** exitcode for shell */
    public static final int CONST_EXITCODE_OK = 0;
    /** exitcode for shell */
    public static final int CONST_EXITCODE_FAILED_ARGS = 1;
    /** exitcode for shell */
    public static final int CONST_EXITCODE_FAILED_CONFIG = 2;
    /** exitcode for shell */
    public static final int CONST_EXITCODE_FAILED_JOB = 3;

    private static final Logger LOGGER = Logger.getLogger(CmdLineJob.class);
    
    protected String jobConfFile = null;

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     baseclass for CommandLineJobs
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the application
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     * @param args the command line arguments
     */
    public CmdLineJob(final String[] args) {
        // set args
        Configurator.getInstance().setCmdLineArgs(args);
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
     * @throws Exception - possible Exception
     */
    protected String getJobName() throws Exception  {
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
     * @throws Exception - possible Exception
     */
    protected void printUsage() throws Exception  {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(this.getJobName(), 
                        Configurator.getInstance().getAvailiableCmdLineOptions());
    }

    /**
     * <h1>Bereich:</h1>
     *     Tools - CLI-Config
     * <h1>Funktionalitaet:</h1>
     *     konfiguriert die verfuegbaren CLI-Optionen -> muss ueberladen werden
     * <h1>Nebenwirkungen:</h1>
     *     Rueckgabe als Options
     * @return Options
     * @throws Exception - possible Exception
     */
    protected abstract Options addAvailiableCmdLineOptions() throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     Jobhandling
     * <h4>FeatureDescription:</h4>
     *     do the jobprocessing
     *     <ul>
     *        <li>initialize Configurator and Commandline
     *        <li>call initJob
     *        <li>call doJob
     *        <li>call cleanUpAfterJob
     *     </ul>
     * <h4>FeatureKeywords:</h4>
     *     Jobhandling
     */
    public void startJobProcessing() {
        try {
            // config cmdArgs
            LOGGER.info("configure CmdArgs");
            Options newAvailiableCmdLineOptions = this.addAvailiableCmdLineOptions();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("startJobProcessing: "
                                + "add commandLineOptions:" + newAvailiableCmdLineOptions);
            }

            Configurator.getInstance().addAvailiableCmdLineOptions(newAvailiableCmdLineOptions);

            // parse cmdArgs
            LOGGER.info("initCommandLine");
            Configurator.getInstance().getCommandLine();

            // check for unknown Args
            String strCmdLineArgs = "";
            for (String arg : Configurator.getInstance().getCmdLineArgs()) {
                strCmdLineArgs += ", " + arg;
            }
            LOGGER.info("used CmdLineArgs: " 
                            + strCmdLineArgs);
            if (Configurator.getInstance().getCommandLine() != null) {
                strCmdLineArgs = "";
                for (String arg : Configurator.getInstance().getCommandLine().getArgs()) {
                    strCmdLineArgs += ", " + arg;
                }
                LOGGER.info("unknown CmdLineArgs: " 
                            + strCmdLineArgs);
                strCmdLineArgs = "";
                for (Option option : Configurator.getInstance().getCommandLine().getOptions()) {
                    strCmdLineArgs += ", " + option.toString();
                }
                LOGGER.info("used Options: " + strCmdLineArgs);
            }

            // validate cmdLine
            LOGGER.info("validate CmdLine");
            if (!Configurator.getInstance().validateCmdLine()) {
                logErrorMsg("Illegal CmdArgs: print Usage");
                this.printUsage();
                LOGGER.info("Exit: 1");
                System.exit(CONST_EXITCODE_FAILED_ARGS);
            }

            // print Usage
            if (Configurator.getInstance().getCommandLine().hasOption("h")) {
                LOGGER.info("print Usage");
                this.printUsage();
                LOGGER.info("Exit: " + CONST_EXITCODE_OK);
                System.exit(CONST_EXITCODE_OK);
            }

            // set debug
            if (Configurator.getInstance().getCommandLine().hasOption("debug")) {
                LOGGER.info("activate debug");
                Logger.getRootLogger().setLevel(org.apache.log4j.Level.ALL);
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
        } catch (Throwable e) {
            // Catch Error
            try {
                LOGGER.info("start cleanUpAfterJob when Error");
                this.cleanUpAfterJob();
                LOGGER.info("done cleanUpAfterJob when Error");
            } catch (Throwable e2) {
                // Log Error while cleanUp
                this.handleThrowable(e2, false);
            }

            // Log Error
            this.handleThrowable(e, true);
        }
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Jobhandling
     * <h4>FeatureDescription:</h4>
     *     log errormessages
     * <h4>FeatureKeywords:</h4>
     *     Jobhandling
     * @param errorMsg - the errormsg for output on System.out and Logger
     */
    public static void logErrorMsg(final String errorMsg) {
        System.out.println(errorMsg);
        LOGGER.fatal(errorMsg);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Jobhandling
     * <h4>FeatureDescription:</h4>
     *     handle throable (log errorme4ssages and exit if flgExit is set)
     * <h4>FeatureKeywords:</h4>
     *     Jobhandling
     * @param e - the exceptuiion/error...
     * @param flgExit - do exit if it is set
     */
    public void handleThrowable(final Throwable e, final boolean flgExit) {

        String errorMsg = "JOB - Exception:\n" + e.getMessage();
        e.printStackTrace();

        logErrorMsg(errorMsg);

        if (flgExit) {
            int exitCode = CONST_EXITCODE_FAILED_JOB;
            logErrorMsg("Exit: " + exitCode);
            System.exit(exitCode);
        }
    }

    protected void initJob() throws Exception {
    }

    protected abstract void doJob() throws Exception;



    protected void cleanUpAfterJob() throws Exception {
        // TODO: hack to close HSLDB-connection -> Hibernate doesn't close the 
        //       database and so the content is not written to file
        org.hsqldb.DatabaseManager.closeDatabases(0);
    }
}
