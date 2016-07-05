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
package de.yaio.app.utils.cli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;


/** 
 * baseclass for commandlinejobs<br>
 * must be implemented:<br>
 * <ul>
 * <li>validateCmdLine
 * </ul>
 * to configure:<br>
 * <ul>
 * <li>genAvailiableCmdLineOptions
 * </ul>
 *
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
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
    
    /**
     * baseclass for CommandLineJobs
     * @param args                   the command line arguments
     */
    public CmdLineJob(final String[] args) {
        // set args
        this.getCmdLineHelper().setCmdLineArgs(args);
    }

    /** 
     * returns jobname (Classname without package)
     * @return                       String - jobname (Classname without package)
     */
    protected String getJobName() {
        return this.getClass().getName();
    }

    /** 
     * prints usage of current commandlineoption son STDOUT
     */
    protected void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(this.getJobName(), 
                        this.getCmdLineHelper().getAvailiableCmdLineOptions());
    }

    protected CmdLineHelper getCmdLineHelper() {
        return CmdLineHelper.getInstance();
    }

    /**
     * <h1>Bereich:</h1>
     *     Tools - CLI-Config
     * <h1>Funktionalitaet:</h1>
     *     konfiguriert die verfuegbaren CLI-Optionen -> muss ueberladen werden
     * <h1>Nebenwirkungen:</h1>
     *     Rueckgabe als Options
     * @return                       Options
     */
    protected abstract Options addAvailiableCmdLineOptions();

    /** 
     * do the jobprocessing
     * <ul>
     *    <li>initialize CmdLineHelper and Commandline
     *    <li>call initJob
     *    <li>call doJob
     *    <li>call cleanUpAfterJob
     * </ul>
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

            this.getCmdLineHelper().addAvailiableCmdLineOptions(newAvailiableCmdLineOptions);

            // parse cmdArgs
            LOGGER.info("initCommandLine");
            this.getCmdLineHelper().getCommandLine();

            // check for unknown Args
            String strCmdLineArgs = "";
            for (String arg : this.getCmdLineHelper().getCmdLineArgs()) {
                strCmdLineArgs += ", " + arg;
            }
            LOGGER.info("used CmdLineArgs: " 
                            + strCmdLineArgs);
            if (this.getCmdLineHelper().getCommandLine() != null) {
                strCmdLineArgs = "";
                for (String arg : this.getCmdLineHelper().getCommandLine().getArgs()) {
                    strCmdLineArgs += ", " + arg;
                }
                LOGGER.info("unknown CmdLineArgs: " 
                            + strCmdLineArgs);
                strCmdLineArgs = "";
                for (Option option : this.getCmdLineHelper().getCommandLine().getOptions()) {
                    strCmdLineArgs += ", " + option.toString();
                }
                LOGGER.info("used Options: " + strCmdLineArgs);
            }

            // validate cmdLine
            LOGGER.info("validate CmdLine");
            if (!this.getCmdLineHelper().validateCmdLine()) {
                logErrorMsg("Illegal CmdArgs: print Usage");
                this.printUsage();
                LOGGER.info("Exit: 1");
                System.exit(CONST_EXITCODE_FAILED_ARGS);
            }

            // print Usage
            if (this.getCmdLineHelper().getCommandLine().hasOption("h")) {
                LOGGER.info("print Usage");
                this.printUsage();
                LOGGER.info("Exit: " + CONST_EXITCODE_OK);
                System.exit(CONST_EXITCODE_OK);
            }

            // set debug
            if (this.getCmdLineHelper().getCommandLine().hasOption("debug")) {
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
     * log errormessages
     * @param errorMsg               the errormsg for output on System.out and Logger
     */
    public static void logErrorMsg(final String errorMsg) {
        System.out.println(errorMsg);
        LOGGER.fatal(errorMsg);
    }

    /** 
     * handle throable (log errorme4ssages and exit if flgExit is set)
     * @param e                      the exceptuiion/error...
     * @param flgExit                do exit if it is set
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

    protected  abstract void initJob();

    protected abstract void doJob();

    protected  abstract void cleanUpAfterJob();
}
