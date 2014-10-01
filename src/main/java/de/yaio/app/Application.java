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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.servlet.MultipartConfigElement;

import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
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
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     a spring boot application
 * 
 * @package de.yaio.app
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("de.yaio")
public class Application {
    protected static ApplicationContext springApplicationContext;
    
    private final static Logger LOGGER = Logger.getLogger(Application.class);
    protected static NodeNumberService nodeNumberService;
    protected static String strPathIdDB;
    
    /**
     * <h4>FeatureDomain:</h4>
     *     CLI
     * <h4>FeatureDescription:</h4>
     *     Main-method to start the application
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the application
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // parse cmdArgs
            LOGGER.info("initCommandLine");
            Option pathIdDB = new Option("", "pathiddb", true,
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
            if (! Configurator.getInstance().validateCmdLine()) {
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
                nodeNumberService.initNextNodeNumbersFromFile(strPathIdDB);
            }

            // inform spring about configfile
            List<String> newArgs = new ArrayList<String>(Arrays.asList(args));
            
            // initApp
            LOGGER.info("start application with args:" + newArgs);
            SpringApplication.run(Application.class, newArgs.toArray(new String[0]));
            LOGGER.info("done application");
        } catch (Throwable ex) {
            // catch Exception
            System.out.println(ex);
            LOGGER.fatal(ex);
            ex.printStackTrace();
            LOGGER.info("Exit: 1");
            try {
                cleanUpAfterJob();
            } catch (Throwable ex2) {
                System.out.println(ex2);
                ex2.printStackTrace();
                LOGGER.fatal(ex2);
                LOGGER.info("Exit: 1");
            }
            System.exit(CmdLineJob.CONST_EXITCODE_FAILED_ARGS);
        }
    }
    
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("128KB");
        factory.setMaxRequestSize("128KB");
        return factory.createMultipartConfig();
    }
    
    
    @PreDestroy
    protected static void cleanUpAfterJob() throws Throwable {
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