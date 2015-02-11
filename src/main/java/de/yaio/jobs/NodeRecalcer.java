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
package de.yaio.jobs;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import de.yaio.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.core.node.BaseNode;
import de.yaio.core.nodeservice.NodeService;

/**
 * <h4>FeatureDomain:</h4>
 *     Updater
 * <h4>FeatureDescription:</h4>
 *     recalc nodedata
 * 
 * @package de.yaio.jobs
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class NodeRecalcer {
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     service functions to recalc nodedata
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the recalcer
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     */
    public NodeRecalcer() {
        super();
    }

    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(NodeRecalcer.class);

    /**
     * <h4>FeatureDomain:</h4>
     *     Job
     * <h4>FeatureDescription:</h4>
     *     read and recalc the masternode, all of its children and the parents and save it to db
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>ReturnValue String - a result message
     *     <li>Updates node in db
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Job
     * @param sysUID - sysUID of the masternode to read and recalc with alls children + parents
     * @return - result-message
     * @throws Exception - possible io/db/recalc-Exceptions
     */
    @Transactional()
    public String findAndRecalcMasternode(final String sysUID)
            throws Exception {
        // look for this masternode in DB
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("read node:" + sysUID);
        }
        BaseNode masterDbNode = BaseNode.findBaseNode(sysUID);
        if (masterDbNode != null) {
            // read masternode with all children
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("read all children for node:" + sysUID);
            }
            masterDbNode.initChildNodesFromDB(
                            NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN);

            // update masternode with all children
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("recalc all children for node:" + sysUID);
            }
            masterDbNode.recalcData(NodeService.CONST_RECURSE_DIRECTION_CHILDREN);

            // recalc+save masterNode and parents
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("recalc+save existing master+parents for:" + masterDbNode.getNameForLogger());
            }
            BaseNodeDBServiceImpl.getInstance().updateMeAndMyParents(masterDbNode);

            // save children
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("recalc+save existing master+parents for:" + masterDbNode.getNameForLogger());
            }
            masterDbNode.saveChildNodesToDB(NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN, true);
        } else {
            LOGGER.error("masternode not found:" + sysUID);
            throw new IllegalArgumentException("masternode not found:" + sysUID);
        }
        // add it to masterDBNode
        return "update node done: " + sysUID + "\n";
    }
}
