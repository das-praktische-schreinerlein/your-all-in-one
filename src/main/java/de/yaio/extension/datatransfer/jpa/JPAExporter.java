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
package de.yaio.extension.datatransfer.jpa;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.core.node.BaseNode;
import de.yaio.core.nodeservice.BaseNodeService;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.extension.datatransfer.wiki.WikiExporter;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     export of Nodes to JPA
 * 
 * @package de.yaio.extension.datatransfer.jpa
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JPAExporter extends WikiExporter {
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     service functions to export nodes to JPA
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the exporter
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     */
    public JPAExporter() {
        super();
    }

    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(JPAExporter.class);

    @Override
    public void initDataDomainFormatter() {
    };

    @Override
    @Transactional()
    public String getMasterNodeResult(DataDomain tmpMasterNode, OutputOptions oOptions)
            throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("save node:" + tmpMasterNode.getNameForLogger());
        }
        
        //map masternode
        BaseNode masterNode = (BaseNode)tmpMasterNode;

        // initial recalc data
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("recalc children for:" + masterNode.getNameForLogger());
        }
        masterNode.recalcData(BaseNodeService.CONST_RECURSE_DIRECTION_CHILDREN);

        // iterate the new children, look for them and delete them in db
        for (BaseNode newChildNode : masterNode.getChildNodes()) {
            BaseNode childDbNode = BaseNode.findBaseNode((newChildNode).getSysUID());
            if (childDbNode != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("delete dbchildren for:" + masterNode.getNameForLogger() 
                                    + " child:" + childDbNode.getNameForLogger());
                }
                // delete the old dbNode with all children
                childDbNode.removeChildNodesFromDB();
                childDbNode.remove();
            }
        }
        
        // look for this masternode in DB
        BaseNode masterDbNode = BaseNode.findBaseNode(masterNode.getSysUID());
        if (masterDbNode != null) {
            // read masternode with all children
            masterDbNode.initChildNodesFromDB(0);
            
            // iterate the new children, an add them to masterDBNode
            for (BaseNode newChildNode : masterNode.getChildNodes()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("add newchildren for existing master:" + masterNode.getNameForLogger() 
                                    + " child:" + newChildNode.getNameForLogger());
                }
                // add to masterNode
                newChildNode.setParentNode(masterDbNode);
                
                // save newChildNode
                newChildNode.persist();
                newChildNode.saveChildNodesToDB(NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN, false);
            }
            
            // recalc+save masterNode and parents
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("recalc+save existing master+parents for:" + masterNode.getNameForLogger());
            }
            BaseNodeDBServiceImpl.getInstance().updateMeAndMyParents(masterDbNode);
            
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("save new master+ranets for:" + masterNode.getNameForLogger());
            }
            // create masterDBNode and persist children
            masterNode.persist();
            
            // save the children
            masterNode.saveChildNodesToDB(NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN, false);
        }

        // add it to masterDBNode
        return "saved node: " + masterNode.getName() + "\n";
    }

    @Override
    public StringBuffer getNodeResult(DataDomain node,  String praefix,
            OutputOptions oOptions) throws Exception {
        throw new IllegalStateException("This function should not be used, but you used it for " + node);
    }
}
