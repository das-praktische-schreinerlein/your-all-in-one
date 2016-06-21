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
package de.yaio.app.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/** 
 * configure static examples
 *  
 * @FeatureDomain                Webservice
 * @package                      de.yaio.server
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Configuration
public class YAIOStaticRessourceConfigurator extends WebMvcConfigurerAdapter {
    
    /** property: file location of the external examples-directory to map for tomcat**/
    public static final String CONST_PROPNAME_EXAMPLES_LOCATION = 
                    "yaio.exportcontroller.examples.location";
    
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        
        // and add me
        if (!registry.hasMappingForPattern("/examples/**")) {
            String examplePath = System.getProperty(CONST_PROPNAME_EXAMPLES_LOCATION);
            registry.addResourceHandler("/examples/**").addResourceLocations(
                            examplePath);
        }

        // and add tests
        if (!registry.hasMappingForPattern("/test/unit/**")) {
            String testsPath = "classpath:/unit/";
            registry.addResourceHandler("/test/unit/**").addResourceLocations(
                            testsPath);
        }
    }
}


