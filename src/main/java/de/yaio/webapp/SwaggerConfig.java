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
package de.yaio.webapp;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import de.yaio.webapp.controller.AdminController;
import de.yaio.webapp.controller.ApiConfigController;
import de.yaio.webapp.controller.ConverterController;
import de.yaio.webapp.controller.ExportController;
import de.yaio.webapp.controller.ImportController;
import de.yaio.webapp.restcontroller.NodeRestController;

/** 
 * configure swagger
 *  
 * @FeatureDomain                Webservice Config
 * @package                      de.yaio.webapp
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * configure the swagger group
     * @return                       the configured swagger-group
     */
    @Bean
    public Docket exportsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("Exports")
            .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/exports.*"))
                .build()
            .apiInfo(apiInfo("YAIO's Export-API", 
                            "Export nodes from YAIO-s database in special formats like wiki, mindmap...", 
                            ExportController.API_VERSION));
    }

    /**
     * configure the swagger group
     * @return                       the configured swagger-group
     */
    @Bean
    public Docket importsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("Imports")
            .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/imports.*"))
                .build()
            .apiInfo(apiInfo("YAIO's Import-API", 
                            "Import nodes to YAIO-s database in special formats like wiki, json...", 
                            ImportController.API_VERSION));
    }

    /**
     * configure the swagger group
     * @return                       the configured swagger-group
     */
    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("Rest")
            .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/nodes.*"))
                .build()
            .apiInfo(apiInfo("YAIO's Node-REST-API", 
                            "Manipulate nodes in YAIO-s database.", 
                            NodeRestController.API_VERSION));
    }

    /**
     * configure the swagger group
     * @return                       the configured swagger-group
     */
    @Bean
    public Docket convertersApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("Converters")
            .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/converters.*"))
                .build()
            .apiInfo(apiInfo("YAIO's Converters-API", 
                            "Offline-Convert nodes from/to diffrent formats without database.", 
                            ConverterController.API_VERSION));
    }

    /**
     * configure the swagger group
     * @return                       the configured swagger-group
     */
    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("Admin")
            .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/admin.*"))
                .build()
            .apiInfo(apiInfo("YAIO's Admin-API", 
                                "Administrate the YAIO-database.", 
                                AdminController.API_VERSION));
    }

    /**
     * configure the swagger group
     * @return                       the configured swagger-group
     */
    @Bean
    public Docket apiconfigApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("ApiConfig")
            .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/apiconfig.*"))
                .build()
            .apiInfo(apiInfo("YAIO's APIConfig-API", 
                                "Get the current API-Config for the client.", 
                                ApiConfigController.API_VERSION));
    }

    private ApiInfo apiInfo(final String name, final String desc, final String version) {
        ApiInfo apiInfo = new ApiInfo(
                        name,
                        desc
                        + "<br />\nFeatureDomain: Collaboration<br />\n"
                        + "FeatureDescription: software for projectmanagement and documentation<br />\n"
                        + "@author Michael Schreiner &lt;michael.schreiner@your-it-fellow.de&gt;<br />\n"
                        + "@category collaboration<br />\n"
                        + "@copyright Copyright (c) 2014, Michael Schreiner<br />\n"
                        + "@license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0<br />\n",
                        version,
                        "THE API IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND, EXPRESS OR<br />\n"
                        + "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,<br />\n"
                        + "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE<br />\n"
                        + "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER<br />\n"
                        + "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,<br />\n"
                        + "OUT OF OR IN CONNECTION WITH THE API OR THE USE OR OTHER DEALINGS IN<br />\n"
                        + "THE API.",
                        "ich@your-all-in-one.de",
                        "This Source Code Form is subject to the terms of the Mozilla Public<br />\n"
                        + "License, v. 2.0. If a copy of the MPL was not distributed with this<br />\n"
                        + "file, You can obtain one at http://mozilla.org/MPL/2.0/.",
                        "http://mozilla.org/MPL/2.0/");
        return apiInfo;
    }
}
