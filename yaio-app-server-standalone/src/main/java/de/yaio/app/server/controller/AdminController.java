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

import de.yaio.app.core.dbservice.BaseNodeDBService;
import de.yaio.app.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.recalcer.NodeRecalcer;
import de.yaio.app.server.restcontroller.NodeActionResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

/** 
 * Admin-Services to recalc, reset...
 *  
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    /** API-Version **/
    public static final String API_VERSION = "1.0.0";

    // Logger
    private static final Logger LOGGER = Logger.getLogger(AdminController.class);

    /**
     * Request to recalc the specified node
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
        
        // create recalcer
        NodeRecalcer nodeRecalcer = new NodeRecalcer();

        // recalc
        String result = nodeRecalcer.findAndRecalcMasternode(sysUID);
        response = new NodeActionResponse(
                        "OK", "node '" + sysUID + "' recalced: " + result,
                        null, null, null, null);

        return response.getStateMsg();
    }

    /** 
     * reset the yaio-instance
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
            BaseNodeDBService dbService = new BaseNodeDBServiceImpl();
            BaseNode masterNode = dbService.resetYaio();

            response = new NodeActionResponse(
                            "OK", "yaio-instance resetet",
                            masterNode, null, null, null);
        }
        
        return response.getStateMsg();
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public String handleAllException(final HttpServletRequest request, final Exception e,
                                     final HttpServletResponse response) {
        LOGGER.warn("error while running request:" + request.toString(), e);
        response.setStatus(SC_INTERNAL_SERVER_ERROR);
        return "cant do admin request";
    }

}
