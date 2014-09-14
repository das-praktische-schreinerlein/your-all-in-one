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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.junit.internal.matchers.ThrowableCauseMatcher;
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
public abstract class Configurator {

    protected static String CONST_DEFAULT_APPLICATIONCONFIG_PATH = 
    "/META-INF/spring/applicationContext.xml";
    
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
                InputStream in = Configurator.class.getResourceAsStream(filePath);
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
    
    
    public static ApplicationContext createApplicationContext(CommandLine commandLine) throws Exception {
        // get Configpath
        String configPath = commandLine.getOptionValue("config");
        
        // read properties
        Properties prop = readProperties(configPath);
        String applicationConfigPath = 
                        prop.getProperty("config.spring.applicationconfig.path", 
                                        CONST_DEFAULT_APPLICATIONCONFIG_PATH);
        
        // init applicationContext
        ApplicationContext applicationContext;
        try {
            applicationContext = new FileSystemXmlApplicationContext(applicationConfigPath);
        } catch (BeansException ex) {
            // try it from jar
            try {
                applicationContext = new ClassPathXmlApplicationContext(applicationConfigPath);
            } catch (BeansException ex2) {
                throw new Exception("cant instiate Application - read applicationConfigPath: " + applicationConfigPath 
                                + " Exception1:" + ex
                                + " Exception2:" + ex2);
            }
        }
        return applicationContext;
    }


}
