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
package de.yaio.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;



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
public class Configurator {

    /** property: file location exportcontroller.replacer.config **/
    public static final String CONST_PROPNAME_EXPORTCONTROLLER_REPLACER = 
                    "yaio.exportcontroller.replacerdef.location";
    /** property:  exportcontroller.replacer documentation pattern**/
    public static final String CONST_PROPNAME_EXPORTCONTROLLER_REPLACER_DOCUMENTATION_SRC = 
                    "replacer.documentation.pattern";
    /** property:  exportcontroller.replacer documentation target**/
    public static final String CONST_PROPNAME_EXPORTCONTROLLER_REPLACER_DOCUMENTATION_TARGET = 
                    "replacer.documentation.target";
    
    
    /** property:  yaioinstances.name **/
    public static final String CONST_PROPNAME_YAIOINSTANCES_NAME = 
                    "yaio.knowninstances.name";
    /** property:  yaioinstances.desc **/
    public static final String CONST_PROPNAME_YAIOINSTANCES_DESC = 
                    "yaio.knowninstances.desc";
    /** property:  yaioinstances.url **/
    public static final String CONST_PROPNAME_YAIOINSTANCES_URL = 
                    "yaio.knowninstances.url";
    
    /** property: masterid to export for static datasource */
    public static final String CONST_PROPNAME_YAIOEXPORT_STATIC_MASTERID = 
                    "yaio.staticdatasource.mastersysuid";
    

    /** property: file location of the spring-application-config **/
    public static final String CONST_PROPNAME_APPLICATIONCONFIG_PATH = 
                    "config.spring.applicationconfig.path";
    protected static final String CONST_DEFAULT_APPLICATIONCONFIG_PATH = 
                    "/META-INF/spring/applicationContext.xml";
    
    private static final Logger LOGGER = Logger.getLogger(Configurator.class);

    // must be instantiated after LOGGER because it is used in constructor
    protected static final Configurator instance = new Configurator();
    
    /** the configured yaioInstances to allow XFrameHeader and to construct in sourceselector of app */
    protected final Map<String, Map<String, String>> knownYaioInstances = new LinkedHashMap<String, Map<String, String>>();

    /** replacements to do after processing a node in documentation-context **/
    public static final Map<String, String> PostProcessorReplacements_documentation = 
                    new LinkedHashMap<String, String>();
    
    protected ApplicationContext applicationContext;
    protected CommandLine commandLine;
    protected String[] cmdLineArgs;
    protected Options availiableCmdLineOptions;
    
    protected Configurator() {
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
        
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /** a protected (not private!!!) map of the options with the long key */
        protected Map<String, Option> mylongOpts = new HashMap<String, Option>();
        
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
    
    
    /* 
     ***********************
     ***********************
     * base
     ***********************
     ***********************
     */
    protected void initConfigurator() {
        this.createAvailiableCmdLineOptions();
    }
    
    
    /** 
     * return the current static Configurator-instance
     * @return                       the current Configurator-instance
     * @throws Exception             parse/io-Exceptions possible
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
     * return the current SpringApplicationContext 
     * if is not set call ApplicationContext.initSpringApplicationContext()
     * @return                       the current SpringApplicationContext
     * @throws Exception             parse/io-Exceptions possible
     */
    public ApplicationContext getSpringApplicationContext() throws Exception {
        // check weather exists
        if (applicationContext == null) {
            this.initSpringApplicationContext();
        }
        
        return applicationContext;
    }

    /** 
     * return the current configFile from option config
     * @return                       the configFile from option config
     * @throws Exception             parse/io-Exceptions possible
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
        Properties props = readProperties(configPath);

        // add all properties to system
        for (String propName : props.stringPropertyNames()) {
            System.setProperty(propName, props.getProperty(propName));
                LOGGER.info("set System.prop:" + propName + "=" + props.getProperty(propName));
        }
        
        // load PostProcessorReplacements
        String replacerConfigPath = props.getProperty(
                        CONST_PROPNAME_EXPORTCONTROLLER_REPLACER);
        if (replacerConfigPath != null) {
            Properties replacerConfig = readProperties(replacerConfigPath);
            
            // load defined
            int count = replacerConfig.size() / 2;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("check PostProcessorReplacements_documentation found:" 
                    + count + " in file:" + replacerConfigPath + " props:" + replacerConfig);
            }
            for (int zaehler = 0; zaehler <= count; zaehler++) {
                String keyName = CONST_PROPNAME_EXPORTCONTROLLER_REPLACER_DOCUMENTATION_SRC + "." + (zaehler + 1);
                String pattern = replacerConfig.getProperty(keyName);
                String valueName = CONST_PROPNAME_EXPORTCONTROLLER_REPLACER_DOCUMENTATION_TARGET + "." + (zaehler + 1);
                String target = replacerConfig.getProperty(valueName);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.info("check PostProcessorReplacements_documentation:" 
                        + zaehler + " " + keyName + "=" + valueName 
                        + " / " + pattern + "=" + target);
                }
                if (pattern != null) {
                    PostProcessorReplacements_documentation.put(
                                    pattern, 
                                    target != null ? target : "");
                        LOGGER.info("set PostProcessorReplacements_documentation:" 
                                    + pattern + "=" + target);
                }
            }
        }
        
       // load defined
        int count = props.size() / 3;
        for (int zaehler = 0; zaehler <= count; zaehler++) {
            String keyName = CONST_PROPNAME_YAIOINSTANCES_NAME + "." + (zaehler + 1);
            String name = props.getProperty(keyName);
            String descName = CONST_PROPNAME_YAIOINSTANCES_DESC + "." + (zaehler + 1);
            String desc = props.getProperty(descName);
            String urlName = CONST_PROPNAME_YAIOINSTANCES_URL + "." + (zaehler + 1);
            String url = props.getProperty(urlName);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("check YaioInstances:" 
                    + zaehler + " " + keyName + "=" + name 
                    + " / " + urlName + "=" + url
                    + " / " + descName + "=" + desc);
            }
            if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(url)) {
                Map<String, String> data = new HashMap<String, String>();
                data.put(CONST_PROPNAME_YAIOINSTANCES_NAME, name);
                data.put(CONST_PROPNAME_YAIOINSTANCES_DESC, desc);
                data.put(CONST_PROPNAME_YAIOINSTANCES_URL, url);
                this.knownYaioInstances.put(name, data);
                    LOGGER.info("set YaioInstances:" + name + "=" + url + " " + desc);
            }
        }

        // define the applicationConfigPath
        String applicationConfigPath = 
                        props.getProperty(CONST_PROPNAME_APPLICATIONCONFIG_PATH, 
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

        // Hilfe-Option
        Option helpOption = new Option("h", "help", false, "usage");
        helpOption.setRequired(false);
        availiableCmdLineOptions.addOption(helpOption);

        // debug-Option
        Option debugOption = new Option(null, "debug", false, "debug");
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
     * read the properties from the given filepath (first by filesystem, 
     * if failed by classpath)
     * @param filePath               path to the file (filesystem or classressource)
     * @return                       the properties read from propertyfile
     * @throws Exception             parse/io-Exceptions possible
     */
    public static Properties readProperties(final String filePath) throws Exception {
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
     * @return                       the {@link Configurator#knownYaioInstances}
     */
    public final Map<String, Map<String, String>> getKnownYaioInstances() {
        return this.knownYaioInstances;
    }
}
