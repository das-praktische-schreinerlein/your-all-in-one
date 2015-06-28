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

import java.io.File;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import de.yaio.core.node.BaseNode;
import de.yaio.extension.datatransfer.jpa.JPAExporter;
import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.rest.controller.NodeActionResponse;
import de.yaio.rest.controller.NodeRestController;

/**
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     Upload-Services to import BaseNodes in different 
 *     formats (wiki, ppl, excel..)
 *      
 * @package de.yaio.webapp.controller
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Controller
@RequestMapping("/imports")
public class ImportController {
    
    @Autowired
    protected ConverterUtils converterUtils;
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     Request to import the uploaded wiki-file from param "file" to the 
     *     node parentSysUID returning a simple message
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice Query
     * @param parentSysUID - sysUID to append the new nodes
     * @param file - the uploaded file stream with the data to import
     * @return text-message
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/wiki/{parentSysUID}", 
                    produces = "text/html")
    public String importNodeAsWiki(@PathVariable(value = "parentSysUID") final String parentSysUID,
                                   @RequestParam("file") final MultipartFile file) {
        NodeActionResponse response = new NodeActionResponse(
                        "ERROR", "node '" + parentSysUID + "' doesnt exists", 
                        null, null, null, null);

        // find the parentnode
        BaseNode node = BaseNode.findBaseNode(parentSysUID);
        if (node == null) {
            return response.stateMsg;
        }
        
        // check file
        if (!file.isEmpty()) {
            try {
                // copy to tmpFile
                File tmpFile = File.createTempFile("upload", "wiki");
                tmpFile.deleteOnExit();
                file.transferTo(tmpFile);
                
                // read filecontent + delete
                String wikiSrc = PPLImporter.readFromInput(tmpFile);
                tmpFile.delete();

                // read the childnodes only 1 level
                node.initChildNodesFromDB(0);
                
                // create dummy masternode
                BaseNode masterNode = new BaseNode();
                masterNode.setSysUID(parentSysUID);
                masterNode.setMetaNodePraefix(node.getMetaNodePraefix());
                masterNode.setMetaNodeNummer(node.getMetaNodeNummer());
                masterNode.setEbene(0);
                
                // parse Wiki
                converterUtils.parseNodesFromString(masterNode, wikiSrc);
                
                // JPA-Exporter
                JPAExporter jpaExporter = new JPAExporter();
                jpaExporter.getMasterNodeResult(masterNode, null);
                
                // create new response
                response = NodeRestController.createResponseObj(
                                node, "data for node '" + parentSysUID + "' imported");
                
                // add children
                response.childNodes = new ArrayList<BaseNode>(node.getChildNodes());

            } catch (Exception e) {
                e.printStackTrace();
                return new NodeActionResponse(
                    "ERROR", 
                    "You failed to upload data to node '" 
                        + parentSysUID + "' => " + e, 
                    null, null, null, null).getStateMsg();
            }
        } else {
            return new NodeActionResponse(
                    "ERROR", 
                    "You failed to upload to node '" 
                        + parentSysUID + "' because the file was empty.", 
                     null, null, null, null).getStateMsg();
        }            
        
        return response.getStateMsg();
    }
}
