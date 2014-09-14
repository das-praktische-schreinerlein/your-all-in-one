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
package de.yaio.rest;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.yaio.utils.CmdLineJob;

/**
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     a spring boot application for RESTful Web Services
 * 
 * @package de.yaio.rest
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("de.yaio.rest")
public class Application {
    protected static ApplicationContext springApplicationContext;
    
    private final static Logger LOGGER = Logger.getLogger(Application.class);

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
            // init data
            Options availiableCmdLineOptions = new Options();
            CmdLineJob.addAvailiableBaseCmdLineOptions(availiableCmdLineOptions);
            LOGGER.debug("valiable:" + availiableCmdLineOptions);
            CommandLine cmdLine = CmdLineJob.genCommandLineFromCmdArgs(args, 
                            availiableCmdLineOptions);
            CmdLineJob.initApplicationContext(cmdLine);

            // initApp
            LOGGER.info("start application");
            SpringApplication.run(Application.class, args);
            LOGGER.info("done application");
            
            // cleanupApp
//            cleanUpAfterJob();
        } catch (Throwable ex) {
            // catch Exception
            System.out.println(ex);
            LOGGER.fatal(ex);
            LOGGER.info("Exit: 1");
            System.exit(CmdLineJob.CONST_EXITCODE_FAILED_ARGS);
        }
    }
    
    protected static void cleanUpAfterJob() throws Throwable {
        // TODO: hack to close HSLDB-connection -> Hibernate doesn't close the 
        //       database and so the content is not written to file
        org.hsqldb.DatabaseManager.closeDatabases(0);
    }
    
}