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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;



/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     baseclass for configuration
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
public class Configurator {

    private final static Logger LOGGER = Logger.getLogger(Configurator.class);
    protected static String CONST_DEFAULT_APPLICATIONCONFIG_PATH = 
                    "/META-INF/spring/applicationContext.xml";
    
    
    protected static Configurator instance = new Configurator();

    
    
    protected ApplicationContext applicationContext;
    protected CommandLine commandLine;
    protected String[] cmdLineArgs;
    protected Options availiableCmdLineOptions;
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Configuration
     * <h4>FeatureDescription:</h4>
     *     Bugfix-Class because Options.getOptions returns only shortoptions
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
    public static class CommandlineOptions extends Options {
        
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * <h4>FeatureDomain:</h4>
         *     Configuration
         * <h4>FeatureDescription:</h4>
         *     create CommandlineOptions 
         *     Bugfix-Class because Options.getOptions returns only shortoptions
         * <h4>FeatureKeywords:</h4>
         *     Configuration
         */
        public CommandlineOptions() {
            super();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("new FixedOptions"); 
        }
        
        /** a protected (not private!!!) map of the options with the long key */
        protected Map<String, Option> mylongOpts = new HashMap<String, Option>();
        
        /**
         * Retrieve a read-only list of options in this set
         * override original because this only retrieved the shortoptions
         *
         * @return read-only Collection of {@link Option} objects in this descriptor
         */
        @SuppressWarnings("unchecked")
        @Override
        public Collection<Option> getOptions() {
            // get the hacked Options
            Collection<Option> myCollection = new ArrayList<Option>(super.getOptions());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getOptions: "
                                + "orig " + myCollection.size() + ": " + myCollection); 
            
            // add my LongOptions
            myCollection.addAll(mylongOpts.values());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getOptions: "
                                + "new " + myCollection.size() + ": " + myCollection); 
            
            return Collections.unmodifiableCollection(myCollection);
        }

         /**
         * Adds an option instance 
         * if it has only longopt, add it to mylongOpts
         *
         * @param opt the option that is to be added
         * @return the resulting Options instance
         */
        @Override
        public Options addOption(Option opt) {
            super.addOption(opt);
            
            // add to my longopts
            if (opt.getOpt() == null || opt.getOpt() == "" && opt.hasLongOpt()) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("addOption: "
                                    + "toMyLongOption " + mylongOpts.size() + ": " + opt); 
                mylongOpts.put(opt.getLongOpt(), opt);
            } else {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("addOption: "
                                    + "only to shortOption because of " + opt.getOpt() + " : " + opt); 
            }
            return this;
        }

    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - CLI-Handling
     * <h4>FeatureDescription:</h4>
     *     return new Instance of CommandlineOptions
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue CommandlineOptions - a new instance to define CommandlineOptions
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI-Handling
     * @return a new instance to define CommandlineOptions
     */
    public static CommandlineOptions getNewOptionsInstance() {
        return new CommandlineOptions();
    }
    
    
    /* 
     ***********************
     ***********************
     * base
     ***********************
     ***********************
     */
    protected Configurator() {
        initConfigurator();
    }
    
    
    protected void initConfigurator() {
        this.createAvailiableCmdLineOptions();
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Configuration
     * <h4>FeatureDescription:</h4>
     *     return the current static Configurator-instance
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue the current Configurator-instance for the app
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Configuration CLI-Handling
     * @return the current Configurator-instance
     * @throws Exception - parse/io-Exceptions possible
     */
    public static Configurator getInstance() {
        return instance;
    }
    

    /* 
     ***********************
     ***********************
     * SpringApplicationContext
     ***********************
     ***********************
     */
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - CLI-Handling
     * <h4>FeatureDescription:</h4>
     *     return the current SpringApplicationContext if is not set call ApplicationContext.initSpringApplicationContext()
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue the current SpringApplicationContext
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI-Handling
     * @return the current SpringApplicationContext
     * @throws Exception - parse/io-Exceptions possible
     */
    public ApplicationContext getSpringApplicationContext() throws Exception {
        // check weather exists
        if (applicationContext == null) {
            this.initSpringApplicationContext();
        }
        
        return applicationContext;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - CLI-Handling
     * <h4>FeatureDescription:</h4>
     *     return the current configFile from option config
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue the current configFile
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI-Handling
     * @return the configFile from option config
     * @throws Exception - parse/io-Exceptions possible
     */
    public String getConfigFile() throws Exception {
        // check
        if (commandLine == null) {
            throw new IllegalStateException("initSpringApplicationContext: "
                            + "cant instantiate SpringApplicationContext "
                            + "because commandLine is not set");
        }
        String configPath = commandLine.getOptionValue("config");
        return configPath;
    }

    protected void initSpringApplicationContext() throws Exception {
        // check
        if (applicationContext != null) {
            throw new IllegalStateException("initSpringApplicationContext: "
                            + "applicationContext already set");
        }
        
        // get Configpath
        String configPath = this.getConfigFile();
        
        // read properties
        Properties prop = readProperties(configPath);
        String applicationConfigPath = 
                        prop.getProperty("config.spring.applicationconfig.path", 
                                        CONST_DEFAULT_APPLICATIONCONFIG_PATH);
        
        // init applicationContext
        try {
            applicationContext = new FileSystemXmlApplicationContext(applicationConfigPath);
        } catch (BeansException ex) {
            // try it from jar
            try {
                applicationContext = new ClassPathXmlApplicationContext(applicationConfigPath);
            } catch (BeansException ex2) {
                throw new Exception("cant instantiate Application - "
                                + "read applicationConfigPath: " + applicationConfigPath 
                                + " Exception1:" + ex
                                + " Exception2:" + ex2);
            }
        }
    }

    /* 
     ***********************
     ***********************
     * CommandLine
     ***********************
     ***********************
     */

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
     */
    public String[] getCmdLineArgs() {
        return this.cmdLineArgs;
    }


    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - CLI-Handling
     * <h4>FeatureDescription:</h4>
     *     set current CMD-Args (throws exception if already set)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates cmdLineArgs
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI-Handling
     * @param cmdLineArgs - current CMD-Args
     */
    public void setCmdLineArgs(String[] cmdLineArgs) {
        if (this.cmdLineArgs != null) {
            throw new IllegalStateException("initCommandLine: "
                            + "cant set commandLine cmdLineArgs, "
                            + "because they are already set");
        }
        this.cmdLineArgs = cmdLineArgs;
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
     * @throws Exception - parse-Exceptions possible
     */
    public boolean validateCmdLine() throws Exception {
        return true;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - CLI-Handling
     * <h4>FeatureDescription:</h4>
     *     return current parsed CLI-Commandline (if not set, call initCommandLine)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue CommandLine - current CLI-Commandline
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI-Handling
     * @return CommandLine - current CLI-Commandline
     * @throws Exception - parse-Exceptions possible
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
     * @return Options
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
    public void addAvailiableBaseCmdLineOptions(Options availiableCmdLineOptions) {
        // Config-File
        Option configOption = new Option("", "config", true,
                "comma separated list of JobConfig property files");
        configOption.setRequired(true);
        availiableCmdLineOptions.addOption(configOption);

        // Hilfe-Option
        Option helpOption = new Option("h", "help", false, "usage");
        helpOption.setRequired(false);
        availiableCmdLineOptions.addOption(helpOption);

        // debug-Option
        Option debugOption = new Option("", "debug", false, "debug");
        debugOption.setRequired(false);
        availiableCmdLineOptions.addOption(debugOption);
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
    
    /**
     * <h1>Bereich:</h1>
     *     Tools - CLI-Config
     * <h1>Funktionalitaet:</h1>
     *     konfiguriert die verfuegbaren CLI-Optionen
     * <h1>Nebenwirkungen:</h1>
     *     Rueckgabe als Options
     * @param newAvailiableCmdLineOptions - cmd-options to add to availiableCmdLineOptions 
     * @throws Throwable - parseExceptions possible
     */
    public void addAvailiableCmdLineOptions(Options newAvailiableCmdLineOptions) throws Throwable {
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
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("addAvailiableCmdLineOptions: "
                                    + "add commandLineOption: " + (Option)newOption);
                availiableCmdLineOptions.addOption((Option)newOption);
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("done addAvailiableCmdLineOptions: "
                                + "with commandLineOptions:" + newAvailiableCmdLineOptions);
        }
        
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
    
    
    /* 
     ***********************
     ***********************
     * public service-functions
     ***********************
     ***********************
     */
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Configuration
     * <h4>FeatureDescription:</h4>
     *     read the properties from the given filepath (first by filesystem, 
     *     if failed by classpath)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Properties - the properties read from propertyfile
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Configuration
     * @param filePath - path to the file (filesystem or classressource)
     * @return the properties read from propertyfile
     * @throws Exception - parse/io-Exceptions possible
     */
    public static Properties readProperties(String filePath) throws Exception {
        Properties prop = new Properties();
        
        // first try it from fileystem
        try {
            InputStream in = new FileInputStream(new File(filePath));
            prop.load(in);
            in.close();
        } catch (Throwable ex) {
            // try it from jar
            try {
                InputStream in = instance.getClass().getResourceAsStream(filePath);
                prop.load(in);
                in.close();
            } catch (Throwable ex2) {
                throw new Exception("cant read propertiesfile: " + filePath 
                                + " Exception1:" + ex
                                + " Exception2:" + ex2);
            }
        }
        return prop;
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
     * @throws ParseException - pase-Exceptions possible
     */
    public static CommandLine createCommandLineFromCmdArgs(String[] cmdArgs,
            Options availiableCmdLineOptions) throws ParseException {
        CommandLineParser parser = new PosixParser();
        return parser.parse(availiableCmdLineOptions, cmdArgs);
    }
    
}
