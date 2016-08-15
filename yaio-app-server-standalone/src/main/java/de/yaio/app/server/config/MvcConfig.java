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

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/** 
 * configure mvc
 *  
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Configuration
public class MvcConfig {
    @Bean
    MultipartConfigElement multipartConfigElement() {
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
}
