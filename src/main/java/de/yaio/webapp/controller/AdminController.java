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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.yaio.core.dbservice.BaseNodeDBService;
import de.yaio.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.core.node.BaseNode;
import de.yaio.jobs.NodeRecalcer;
import de.yaio.rest.controller.NodeActionResponse;

/** 
 * Admin-Services to recalc, reset...
 *  
 * @FeatureDomain                Webservice
 * @package                      de.yaio.webapp.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    /** API-Version **/
    public static final String API_VERSION = "1.0.0";

    /** 
     * Request to recalc the specified node
     * @FeatureDomain                Webservice
     * @FeatureKeywords              Webservice Admin
     * @param sysUID                 sysUID to recalc
     * @return                       text-message
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/recalc/{sysUID}", 
                    produces = "text/html")
    public String recalcNode(@PathVariable(value = "sysUID") final String sysUID) {
        NodeActionResponse response = new NodeActionResponse(
                        "ERROR", "node '" + sysUID + "' doesnt exists", 
                        null, null, null, null);

        // find the parentnode
        BaseNode node = BaseNode.findBaseNode(sysUID);
        if (node == null) {
            return response.getStateMsg();
        }
        
        try {
            // create recalcer
            NodeRecalcer nodeRecalcer = new NodeRecalcer();

            // recalc
            String result = nodeRecalcer.findAndRecalcMasternode(sysUID);
            response = new NodeActionResponse(
                            "OK", "node '" + sysUID + "' recalced: " + result, 
                            null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new NodeActionResponse(
                            "ERROR", 
                            "You failed to recalc node '" 
                                            + sysUID + "' => " + e, 
                                            null, null, null, null).getStateMsg();
        }
        
        return response.getStateMsg();
    }

    /** 
     * reset the yaio-instance
     * @FeatureDomain                Webservice
     * @FeatureKeywords              Webservice Admin
     * @return                       text-message
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/reset", 
                    produces = "text/html")
    public String reset() {
        NodeActionResponse response = new NodeActionResponse(
                        "ERROR", "it is not allowed to reset this yaio-instance: change yaio.demo.allow-reset=true", 
                        null, null, null, null);

        if ("true".equalsIgnoreCase(System.getProperty("yaio.demo.allow-reset", "false"))) {
            // reset if option allows it
            try {
                // reset
                BaseNodeDBService dbService = new BaseNodeDBServiceImpl();
                BaseNode masterNode = dbService.resetYaio();
                
                response = new NodeActionResponse(
                                "OK", "yaio-instance resetet", 
                                masterNode, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
                return new NodeActionResponse(
                                "ERROR", 
                                "You failed to reset yaio-instance" + e, 
                                                null, null, null, null).getStateMsg();
            }
        }
        
        return response.getStateMsg();
    }
}
