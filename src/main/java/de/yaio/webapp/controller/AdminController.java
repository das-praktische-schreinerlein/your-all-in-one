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
package de.yaio.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.yaio.core.node.BaseNode;
import de.yaio.jobs.NodeRecalcer;
import de.yaio.rest.controller.NodeActionResponse;

/**
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     Admin-Services to recalc, reset...
 *      
 * @package de.yaio.webapp.controller
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to recalc the specified node
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param sysUID - sysUID to recalc
     * @return text-message
     */
    @RequestMapping(method=RequestMethod.GET, 
                    value = "/recalc/{sysUID}", 
                    produces="text/html")
    public @ResponseBody String importNodeAsWiki(
           @PathVariable(value="sysUID") String sysUID) {
        NodeActionResponse response = new NodeActionResponse(
                        "ERROR", "node '" + sysUID + "' doesnt exists", 
                        null, null, null, null);

        // find the parentnode
        BaseNode node = BaseNode.findBaseNode(sysUID);
        if (node == null) {
            return response.stateMsg;
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
}