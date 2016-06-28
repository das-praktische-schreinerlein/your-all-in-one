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
package de.yaio.app.utils;

import de.yaio.app.utils.config.Configuration;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;


/**
 * baseclass for configuration
 *
 * @FeatureDomain                Configuration
 * @package                      de.yaio.utils
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @copyright                    Copyright (c) 2013, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class CmdLineHelper {
    private static final Logger LOGGER = Logger.getLogger(CmdLineHelper.class);

    // must be instantiated after LOGGER because it is used in constructor
    protected static final CmdLineHelper instance = new CmdLineHelper();
    
    protected CommandLine commandLine;
    protected String[] cmdLineArgs;
    protected Options availiableCmdLineOptions;

    protected Configuration configuration;
    
    protected CmdLineHelper() {
        initConfigurator();
    }
    
    /** 
     * Bugfix-Class because Options.getOptions returns only shortoptions
     *
     * @FeatureDomain                Configuration
     * @package                      base
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @copyright                    Copyright (c) 2013, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     *
     * This Source Code Form is subject to the terms of the Mozilla Public
     * License, v. 2.0. If a copy of the MPL was not distributed with this
     * file, You can obtain one at http://mozilla.org/MPL/2.0/.
     */
    public static class CommandlineOptions extends Options {
        
        /** a protected (not private!!!) map of the options with the long key */
        protected Map<String, Option> mylongOpts = new HashMap<String, Option>();

        private static final long serialVersionUID = 1L;

        /**
         * create CommandlineOptions 
         * Bugfix-Class because Options.getOptions returns only shortoptions
                 */
        public CommandlineOptions() {
            super();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("new FixedOptions");
            }
        }

        /**
         * Retrieve a read-only list of options in this set
         * override original because this only retrieved the shortoptions
         *
         * @return                       read-only Collection of {@link Option} objects in this descriptor
         */
        @SuppressWarnings("unchecked")
        @Override
        public Collection<Option> getOptions() {
            // get the hacked Options
            Collection<Option> myCollection = new ArrayList<Option>(super.getOptions());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("getOptions: "
                                + "orig " + myCollection.size() + ": " + myCollection);
            } 
            
            // add my LongOptions
            myCollection.addAll(mylongOpts.values());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("getOptions: "
                                + "new " + myCollection.size() + ": " + myCollection);
            } 
            
            return Collections.unmodifiableCollection(myCollection);
        }

         /**
         * Adds an option instance 
         * if it has only longopt, add it to mylongOpts
         *
         * @param opt                    the option that is to be added
         * @return                       the resulting Options instance
         */
        @Override
        public Options addOption(final Option opt) {
            super.addOption(opt);
            
            // add to my longopts
            if (opt.getOpt() == null || "".equals(opt.getOpt()) && opt.hasLongOpt()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("addOption: "
                                    + "toMyLongOption " + mylongOpts.size() + ": " + opt);
                } 
                mylongOpts.put(opt.getLongOpt(), opt);
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("addOption: "
                                    + "only to shortOption because of " + opt.getOpt() + " : " + opt);
                } 
            }
            return this;
        }

    }
    
    
    /** 
     * return new Instance of CommandlineOptions
     * @return                       a new instance to define CommandlineOptions
     */
    public static CommandlineOptions getNewOptionsInstance() {
        return new CommandlineOptions();
    }


    /**
     * return the current static CmdLineHelper-instance
     * @return                       the current CmdLineHelper-instance
     * @throws Exception             parse/io-Exceptions possible
     */
    public static CmdLineHelper getInstance() {
        return instance;
    }

    /**
     * return current CMD-Args
     * @return                       String[] - current CMD-Args
     */
    public String[] getCmdLineArgs() {
        return this.cmdLineArgs;
    }


    /** 
     * set current CMD-Args (throws exception if already set)- updates cmdLineArgs
     * @param cmdLineArgs            current CMD-Args
     */
    public void setCmdLineArgs(final String[] cmdLineArgs) {
        if (this.cmdLineArgs != null) {
            throw new IllegalStateException("initCommandLine: "
                            + "cant set commandLine cmdLineArgs, "
                            + "because they are already set");
        }
        this.cmdLineArgs = cmdLineArgs;
    }
    
    /** 
     * checks weather Commandline is valid
     * @return                       boolean - valid Commandline?
     * @throws Exception             parse-Exceptions possible
     */
    public boolean validateCmdLine() throws Exception {
        return true;
    }

    /** 
     * return current parsed CLI-Commandline (if not set, call initCommandLine)
     * @return                       CommandLine - current CLI-Commandline
     * @throws Exception             parse-Exceptions possible
     */
    public CommandLine getCommandLine() throws Exception {
        // check weather exists
        if (commandLine == null) {
            this.initCommandLine();
        }
        
        return commandLine;
    }

    /**
     * <h1>Bereich:</h1>
     *     Tools - CLI-Handling
     * <h1>Funktionalitaet:</h1>
     *     liefert die verfuegbaren CLI-Optionen
     * <h1>Nebenwirkungen:</h1>
     *     Rueckgabe als Options
     * @return                       Options
     */
    public Options getAvailiableCmdLineOptions() {
        return this.availiableCmdLineOptions;
    }

    /**
     * <h1>Bereich:</h1>
     *     Tools - CLI-Config
     * <h1>Funktionalitaet:</h1>
     *     konfiguriert die verfuegbaren Base-CLI-Optionen
     * <h1>Nebenwirkungen:</h1>
     *     aktualisiert availiableCmdLineOptions
     * @param availiableCmdLineOptions Options
     */
    public void addAvailiableBaseCmdLineOptions(final Options availiableCmdLineOptions) {
        // Config-File
        Option configOption = new Option(null, "config", true,
                "comma separated list of JobConfig property files");
        configOption.setRequired(true);
        availiableCmdLineOptions.addOption(configOption);

        // Hilfe-ConfigurationOption
        Option helpOption = new Option("h", "help", false, "usage");
        helpOption.setRequired(false);
        availiableCmdLineOptions.addOption(helpOption);

        // debug-ConfigurationOption
        Option debugOption = new Option(null, "debug", false, "debug");
        debugOption.setRequired(false);
        availiableCmdLineOptions.addOption(debugOption);
    }


    /**
     * <h1>Bereich:</h1>
     *     Tools - CLI-Handling
     * <h1>Funktionalitaet:</h1>
     *     erzeugt aus den CMD-Args ein CLI-Commandline-Object
     * <h1>Nebenwirkungen:</h1>
     *     Rueckgabe als CommandLine
     * @param cmdArgs                Parameter aus z.B. main
     * @param availiableCmdLineOptions verfuegbare CLI-Optionen
     * @return                       CommandLine
     * @throws ParseException        pase-Exceptions possible
     */
    public static CommandLine createCommandLineFromCmdArgs(final String[] cmdArgs,
                                                           final Options availiableCmdLineOptions) throws ParseException {
        CommandLineParser parser = new PosixParser();
        return parser.parse(availiableCmdLineOptions, cmdArgs);
    }

    /**
     * <h1>Bereich:</h1>
     *     Tools - CLI-Config
     * <h1>Funktionalitaet:</h1>
     *     konfiguriert die verfuegbaren CLI-Optionen
     * <h1>Nebenwirkungen:</h1>
     *     Rueckgabe als Options
     * @param newAvailiableCmdLineOptions cmd-options to add to availiableCmdLineOptions 
     * @throws Exception             parseExceptions possible
     */
    public void addAvailiableCmdLineOptions(final Options newAvailiableCmdLineOptions) throws Exception {
        if (commandLine != null) {
            throw new IllegalStateException("addAvailiableCmdLineOptions: "
                            + "cant add availiableCmdLineOptions "
                            + "because commandLine already set");
        }
        
        if (newAvailiableCmdLineOptions != null) {
            // add new Options
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("addAvailiableCmdLineOptions: "
                                + "add commandLineOptions:" + newAvailiableCmdLineOptions.getOptions().size()); 
                LOGGER.debug("addAvailiableCmdLineOptions: "
                                + "add commandLineOptions:" + newAvailiableCmdLineOptions.getOptions()); 
                LOGGER.debug("addAvailiableCmdLineOptions: "
                                + "add commandLineOptions:" + newAvailiableCmdLineOptions.getRequiredOptions()); 
                LOGGER.debug("addAvailiableCmdLineOptions: "
                                + " details:" + newAvailiableCmdLineOptions);
            }
            for (Object newOption : newAvailiableCmdLineOptions.getOptions()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("addAvailiableCmdLineOptions: "
                                    + "add commandLineOption: " + (Option) newOption);
                }
                availiableCmdLineOptions.addOption((Option) newOption);
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("done addAvailiableCmdLineOptions: "
                                + "with commandLineOptions:" + newAvailiableCmdLineOptions);
            }
        }
        
    }

    /**
     * read the properties from the given filepath (first by filesystem,
     * if failed by classpath)
     * @param filePath               path to the file (filesystem or classressource)
     * @return                       the properties read from propertyfile
     * @throws Exception             parse/io-Exceptions possible
     */
    public Properties readProperties(final String filePath) throws Exception {
        Properties prop = new Properties();

        // first try it from fileystem
        try {
            InputStream in = new FileInputStream(new File(filePath));
            prop.load(in);
            in.close();
            //CHECKSTYLE.OFF: IllegalCatch - Much more readable than catching x exceptions
        } catch (Throwable ex) {
            //CHECKSTYLE.ON: IllegalCatch
            // try it from jar
            try {
                InputStream in = instance.getClass().getResourceAsStream(filePath);
                prop.load(in);
                in.close();
                //CHECKSTYLE.OFF: IllegalCatch - Much more readable than catching x exceptions
            } catch (Throwable ex2) {
                //CHECKSTYLE.ON: IllegalCatch
                throw new Exception("cant read propertiesfile: " + filePath
                        + " Exception1:" + ex
                        + " Exception2:" + ex2);
            }
        }
        return prop;
    }

    protected void initConfigurator() {
        this.createAvailiableCmdLineOptions();
    }

    /**
     * <h1>Bereich:</h1>
     *     Tools - CLI-Config
     * <h1>Funktionalitaet:</h1>
     *     konfiguriert die verfuegbaren CLI-Optionen
     * <h1>Nebenwirkungen:</h1>
     *     updates availiableCmdLineOptions
     */
    protected void createAvailiableCmdLineOptions() {
        if (availiableCmdLineOptions != null) {
            throw new IllegalStateException("createAvailiableCmdLineOptions: "
                    + "cant create availiableCmdLineOptions "
                    + "because availiableCmdLineOptions already set");
        }
        availiableCmdLineOptions = getNewOptionsInstance();
        this.addAvailiableBaseCmdLineOptions(availiableCmdLineOptions);
    }



    protected void initCommandLine() throws Exception {
        //check
        if (commandLine != null) {
            throw new IllegalStateException("initCommandLine: "
                            + "commandline already set");
        }
        if (cmdLineArgs == null) {
            throw new IllegalStateException("initCommandLine: "
                            + "cant init commandLine "
                            + "because cmdLineArgs not set");
        }
        if (availiableCmdLineOptions == null) {
            throw new IllegalStateException("initCommandLine: "
                            + "cant init commandLine because "
                            + "availiableCmdLineOptions not set");
        }
        
        // create commandline
        this.commandLine = createCommandLineFromCmdArgs(cmdLineArgs, 
                        availiableCmdLineOptions);
        
        // validate
        this.validateCmdLine();
    }

    /**
     * return the current configFile from option config
     * @return                       the configFile from option config
     * @throws Exception             parse/io-Exceptions possible
     */
    public String getConfigFile() throws Exception {
        // check
        if (commandLine == null) {
            throw new IllegalStateException("getConfigFile: cant get configfile because commandLine is not set");
        }
        String configPath = commandLine.getOptionValue("config");
        return configPath;
    }

}
