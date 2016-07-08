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
package de.yaio.app.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/** 
 * Services to export the current webconfig
 *  
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Controller
@RequestMapping("/apiconfig")
public class ApiConfigController {
    /** API-Version **/
    public static final String API_VERSION = "1.0.0";
    
    @Autowired
    CommonApiConfig commonApiConfig;

    /** 
     * serve the common API-Config from application.properties
     * @return                       CommonApiConfig - json of the CommonApiConfig
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET,
                    value = "/commonApiConfig")
    public CommonApiConfig commonApiConfig() {
        return commonApiConfig;
    }
}
