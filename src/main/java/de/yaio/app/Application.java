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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.servlet.MultipartConfigElement;

import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.yaio.core.datadomainservice.NodeNumberService;
import de.yaio.core.node.BaseNode;

/** 
 * a spring boot application
 * 
 * @FeatureDomain                Webservice
 * @package                      de.yaio.app
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("de.yaio")
public class Application {
    protected static ApplicationContext springApplicationContext;
    protected static NodeNumberService nodeNumberService;
    protected static String strPathIdDB;
    private static final Logger LOGGER = Logger.getLogger(Application.class);
    
    /** 
     * Main-method to start the application
     * @FeatureDomain                CLI
     * @FeatureResult                initialize the application
     * @FeatureKeywords              CLI
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        try {
            // parse cmdArgs
            LOGGER.info("initCommandLine");
            Option pathIdDB = new Option(null, "pathiddb", true,
                            "Pfad zur ID-Datenbank");
                    pathIdDB.setRequired(true);
            Configurator.getInstance().getAvailiableCmdLineOptions().addOption(pathIdDB);
            Configurator.getInstance().setCmdLineArgs(args);
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
            if (!Configurator.getInstance().validateCmdLine()) {
                LOGGER.info("Illegal CmdArgs Exit: 1");
                System.exit(CmdLineJob.CONST_EXITCODE_FAILED_ARGS);
            }
            
            // initApplicationContext
            Configurator.getInstance().getSpringApplicationContext();
            
            // gets NodeNumberService
            nodeNumberService = 
                            BaseNode.getConfiguredMetaDataService().getNodeNumberService();
            
            // Id-Datei einlesen
            strPathIdDB = Configurator.getInstance().getCommandLine().getOptionValue(
                                            "pathiddb", null);
            if (strPathIdDB != null) {
                nodeNumberService.initNextNodeNumbersFromFile(strPathIdDB, false);
            }

            // inform spring about configfile
            List<String> newArgs = new ArrayList<String>(Arrays.asList(args));
            
            // initApp
            LOGGER.info("start application with args:" + newArgs);
            SpringApplication.run(Application.class, newArgs.toArray(new String[0]));
            LOGGER.info("done application");
        //CHECKSTYLE.OFF:
        } catch (Throwable ex) {
        //CHECKSTYLE.ON: 
            // catch Exception
            System.out.println(ex);
            LOGGER.fatal(ex);
            ex.printStackTrace();
            LOGGER.info("Exit: 1");
            try {
                cleanUpAfterJob();
            //CHECKSTYLE.OFF:
            } catch (Throwable ex2) {
            //CHECKSTYLE.ON: 
                System.out.println(ex2);
                ex2.printStackTrace();
                LOGGER.fatal(ex2);
                LOGGER.info("Exit: 1");
            }
            System.exit(CmdLineJob.CONST_EXITCODE_FAILED_ARGS);
        }
    }
    
    @Bean
    MultipartConfigElement configureMultipartConfigElement() {
        // spring-config
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(System.getProperty("yaio.server.maxfilesize", "128kb"));
        factory.setMaxRequestSize(System.getProperty("yaio.server.maxrequestsize", "128kb"));
        MultipartConfigElement config = factory.createMultipartConfig();
        
        // tomcat-config
        FileUploadBase tomcatConfig = new FileUpload();
        tomcatConfig.setFileSizeMax(config.getMaxFileSize());
        tomcatConfig.setSizeMax(config.getMaxFileSize());

        return config;
    }

    @PreDestroy
    protected static void cleanUpAfterJob() throws Exception {
        // Ids speichern
        LOGGER.info("cleanUpAfterJob start");
        if (strPathIdDB != null && nodeNumberService != null) {
            // save to file
            LOGGER.info("cleanUpAfterJob export nextNodeNumbers to " + strPathIdDB);
            nodeNumberService.exportNextNodeNumbersToFile(strPathIdDB);
        }

        // TODO: hack to close HSLDB-connection -> Hibernate doesn't close the 
        //       database and so the content is not written to file
        LOGGER.info("hack: close hsqldb");
        org.hsqldb.DatabaseManager.closeDatabases(0);
        LOGGER.info("cleanUpAfterJob done");
    }
    
}
