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
package de.yaio.webapp.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.yaio.app.Configurator;

/** 
 * controller to publish the baseapp
 *  
 * @FeatureDomain                Webservice
 * @package                      de.yaio.webapp.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Controller
@RequestMapping("/yaio-explorerapp/yaio-explorerapp.html")
public class BaseAppController {
    
    @Autowired
    protected ConverterUtils converterUtils;
    
    /** 
     * Request to read the yaioApp-html-format with all Sys1-children
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - yaioApp-html-format of the node
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, produces = "text/html")
    public String exportNodeAsYaioApp(final HttpServletResponse response) {
        String sysUID = System.getProperty(Configurator.CONST_PROPNAME_YAIOEXPORT_STATIC_MASTERID);
        String res = converterUtils.commonExportNodeAsYaioApp(sysUID, response, "/static/yaio-explorerapp/yaio-explorerapp.html", false);
        
        return res;
    }
}
