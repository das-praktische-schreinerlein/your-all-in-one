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
package de.yaio.webapp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     configure static examples
 *      
 * @package de.yaio.webapp
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Configuration
public class YAIOStaticRessourceConfigurator extends WebMvcConfigurerAdapter {
    
    /** property: file location of the external examples-directory to map for tomcat**/
    public static String CONST_PROPNAME_EXAMPLES_LOCATION = 
                    "yaio.exportcontroller.examples.location";
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        
        // and add me
        if (!registry.hasMappingForPattern("/examples/**")) {
            String examplePath = System.getProperty(CONST_PROPNAME_EXAMPLES_LOCATION);
            registry.addResourceHandler("/examples/**").addResourceLocations(
                            examplePath);
        }

        // and add tests
        if (!registry.hasMappingForPattern("/test/jstests/**")) {
            String testsPath = "classpath:/jstests/";
            registry.addResourceHandler("/test/jstests/**").addResourceLocations(
                            testsPath);
        }
    }
}


