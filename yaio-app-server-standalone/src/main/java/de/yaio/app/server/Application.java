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
package de.yaio.app.server;

import de.yaio.app.config.ContextHelper;
import de.yaio.app.config.PersistenceConfig;
import de.yaio.app.config.YaioConfigurationHelper;
import de.yaio.app.server.config.MvcConfig;
import de.yaio.app.system.YaioFlyway;
import de.yaio.commons.cli.CmdLineHelper;
import de.yaio.commons.cli.CmdLineJob;
import de.yaio.commons.config.ConfigurationHelper;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.*;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.security.AuthenticationManagerConfiguration;
import org.springframework.boot.autoconfigure.security.BootGlobalAuthenticationConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 
 * the yaio-app as spring boot application
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Configuration
@Lazy
@EnableSpringConfigured // <context:spring-configured/>
@EnableJpaRepositories(basePackages = {"de.yaio.app.core"})
@ComponentScan(basePackages = {"de.yaio.app.core", "de.yaio.app.datatransfer",
                               "de.yaio.app.extension", "de.yaio.app.server",
                               "de.yaio.services.webshot", "de.yaio.services.dms",
                               "de.yaio.services.plantuml", "de.yaio.services.metaextract"},
                excludeFilters = {
                    @Filter(type = FilterType.ASSIGNABLE_TYPE, value = {
                        de.yaio.app.config.JobConfig.class
                    }),
                    @Filter(type=FilterType.REGEX, pattern={
                            "de.yaio.services.dms.server.configuration.*",
                            "de.yaio.services.metaextract.server.configuration.*",
                            "de.yaio.services.plantuml.server.configuration.*",
                            "de.yaio.services.webshot.server.configuration.*"
                    })
                })
@EnableScheduling
// extract while running server with "-Ddebug"
@Import({PersistenceConfig.class,
        MvcConfig.class,
        AopAutoConfiguration.class,
        AopAutoConfiguration.JdkDynamicAutoProxyConfiguration.class,
        AuditAutoConfiguration.class,
        AuthenticationManagerConfiguration.class,
        BootGlobalAuthenticationConfiguration.class,
        DispatcherServletAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class,
        EndpointAutoConfiguration.class,
        EndpointMBeanExportAutoConfiguration.class,
        EndpointWebMvcAutoConfiguration.class,
        EndpointWebMvcManagementContextConfiguration.class,
        ErrorMvcAutoConfiguration.class,
        HealthIndicatorAutoConfiguration.class,
        HealthIndicatorAutoConfiguration.DataSourcesHealthIndicatorConfiguration.class,
        HealthIndicatorAutoConfiguration.DiskSpaceHealthIndicatorConfiguration.class,
        HttpEncodingAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        JmxAutoConfiguration.class,
        ManagementServerPropertiesAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class,
        MetricExportAutoConfiguration.class,
        MetricFilterAutoConfiguration.class,
        MetricRepositoryAutoConfiguration.class,
        MultipartAutoConfiguration.class,
        PersistenceExceptionTranslationAutoConfiguration.class,
        PropertyPlaceholderAutoConfiguration.class,
        PublicMetricsAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class,
        ServerPropertiesAutoConfiguration.class,
        TraceRepositoryAutoConfiguration.class,
        TraceWebFilterAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.class,
        WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.FaviconConfiguration.class
})
public class Application {
    private static final Logger LOGGER = Logger.getLogger(Application.class);

    /**
     * Main-method to start the application
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        try {
            CmdLineHelper cmdLineHelper = CmdLineHelper.getInstance();
            ConfigurationHelper configurationHelper = YaioConfigurationHelper.getInstance();

            // parse cmdArgs
            LOGGER.info("initCommandLine");
            Option pathIdDB = new Option(null, "pathiddb", true,
                    "Pfad zur ID-Datenbank");
            pathIdDB.setRequired(true);
            cmdLineHelper.getAvailiableCmdLineOptions().addOption(pathIdDB);
            cmdLineHelper.setCmdLineArgs(args);
            cmdLineHelper.getCommandLine();

            // check for unknown Args
            LOGGER.info("used CmdLineArgs: " + cmdLineHelper.getCmdLineArgs());
            if (cmdLineHelper.getCommandLine() != null) {
                LOGGER.info("unknown CmdLineArgs: " + cmdLineHelper.getCommandLine().getArgs());
            }

            // validate cmdLine
            LOGGER.info("validate CmdLine");
            if (!cmdLineHelper.validateCmdLine()) {
                LOGGER.info("Illegal CmdArgs Exit: 1");
                System.exit(CmdLineJob.CONST_EXITCODE_FAILED_ARGS);
            }

            // init configuration
            de.yaio.commons.config.Configuration config = configurationHelper.initConfiguration();
            config.publishProperties();

            LOGGER.info("start application with args:" + config.argsAsList() +
                    " options:" + config.optionsAsProperties() +
                    " properties:" + config.propertiesAsProperties() +
                    " contextConfigs:" + ContextHelper.getInstance().getSpringConfig());

            // do flyway
            String flyWayRes = YaioFlyway.doFlyway();
            LOGGER.info(flyWayRes);

            // inform spring about configfile
            List<String> newArgs = new ArrayList<>(Arrays.asList(args));

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

    @PreDestroy
    protected static void cleanUpAfterJob() {
        // TODO: hack to close HSLDB-connection -> Hibernate doesn't close the
        //       database and so the content is not written to file
        LOGGER.info("hack: close hsqldb");
        org.hsqldb.DatabaseManager.closeDatabases(0);
        LOGGER.info("cleanUpAfterJob done");
    }
    
}
