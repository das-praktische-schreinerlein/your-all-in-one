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

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;


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

    protected String jobConfFile = null;

    public CmdLineJob(String[] args) {
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
     * @throws Throwable
     */
    protected abstract Options addAvailiableCmdLineOptions() throws Throwable;

    public void startJobProcessing() {
        try {
            // config cmdArgs
            LOGGER.info("configure CmdArgs");
            Options newAvailiableCmdLineOptions = this.addAvailiableCmdLineOptions();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("startJobProcessing: "
                                + "add commandLineOptions:" + newAvailiableCmdLineOptions);

            Configurator.getInstance().addAvailiableCmdLineOptions(newAvailiableCmdLineOptions);

            // parse cmdArgs
            LOGGER.info("initCommandLine");
            Configurator.getInstance().getCommandLine();

            // check for unknown Args
            LOGGER.info("used CmdLineArgs: " 
                            + Configurator.getInstance().getCmdLineArgs());
            if (Configurator.getInstance().getCommandLine() != null) {
                LOGGER.info("unknown CmdLineArgs: " 
                            + Configurator.getInstance().getCommandLine().getArgs());
            }

            // validate cmdLine
            LOGGER.info("validate CmdLine");
            if (! Configurator.getInstance().validateCmdLine()) {
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

    protected void initJob() throws Throwable {
    }

    protected abstract void doJob() throws Throwable;



    protected void cleanUpAfterJob() throws Throwable {
        // TODO: hack to close HSLDB-connection -> Hibernate doesn't close the 
        //       database and so the content is not written to file
        org.hsqldb.DatabaseManager.closeDatabases(0);
    }
}
