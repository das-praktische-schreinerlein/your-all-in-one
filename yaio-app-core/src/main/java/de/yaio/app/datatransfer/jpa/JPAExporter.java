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
package de.yaio.app.datatransfer.jpa;

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.dbservice.BaseNodeRepository;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.nodeservice.BaseNodeService;
import de.yaio.app.core.nodeservice.NodeService;
import de.yaio.app.datatransfer.exporter.ExporterImpl;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/** 
 * export of Nodes to JPA
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public class JPAExporter extends ExporterImpl {
    
    // Logger
    private static final Logger LOGGER = Logger.getLogger(JPAExporter.class);

    @Autowired
    protected BaseNodeRepository baseNodeDBService;

    /** 
     * service functions to export nodes to JPA
     */
    public JPAExporter() {
        super();
    }

    @Override
    public void initDataDomainFormatter() {
    }

    @Override
    @Transactional
    public String getMasterNodeResult(final DataDomain tmpMasterNode, final OutputOptions oOptions) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("save node:" + tmpMasterNode.getNameForLogger());
        }
        
        //map masternode
        BaseNode masterNode = (BaseNode) tmpMasterNode;

        // initial recalc data
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("recalc children for:" + masterNode.getNameForLogger());
        }
        masterNode.recalcData(BaseNodeService.RecalcRecurseDirection.CHILDREN);

        // iterate the new children, look for them and delete them in db
        for (BaseNode newChildNode : masterNode.getChildNodes()) {
            BaseNode childDbNode = baseNodeDBService.findBaseNode(newChildNode.getSysUID());
            if (childDbNode != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("delete dbchildren for:" + masterNode.getNameForLogger() 
                                    + " child:" + childDbNode.getNameForLogger());
                }
                // delete the old dbNode with all children
                baseNodeDBService.removeChildNodesFromDB(childDbNode);
                baseNodeDBService.delete(childDbNode);
            }
        }
        
        // look for this masternode in DB
        BaseNode masterDbNode = baseNodeDBService.findBaseNode(masterNode.getSysUID());
        if (masterDbNode != null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("copy children to existing master:" + masterNode.getNameForLogger());
            }
            // read masternode with all children
            masterDbNode.initChildNodesFromDB(0);
            
            // iterate the new children, an add them to masterDBNode
            for (BaseNode newChildNode : masterNode.getChildNodes()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("add newchildren for existing master:" + masterNode.getNameForLogger() 
                                    + " child:" + newChildNode.getNameForLogger());
                }
                LOGGER.info("save new master for:" + masterNode.getNameForLogger());
                // add to masterNode
                newChildNode.setParentNode(masterDbNode);
                
                // save newChildNode
                baseNodeDBService.save(newChildNode);
                baseNodeDBService.saveChildNodesToDB(newChildNode,
                        NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN, false);
            }
            
            // recalc+save masterNode and parents
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("recalc+save existing master+parents for:" + masterNode.getNameForLogger());
            }
            baseNodeDBService.updateMeAndMyParents(masterDbNode);
            
        } else {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("save new master for:" + masterNode.getNameForLogger());
            }
            // create masterDBNode and persist children
            baseNodeDBService.save(masterNode);
            
            // save the children
            baseNodeDBService.saveChildNodesToDB(masterNode,
                    NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN, false);
        }

        // add it to masterDBNode
        return "saved node: " + masterNode.getName() + "\n";
    }

    @Override
    public StringBuffer getNodeResult(final DataDomain node,  final String praefix,
            final OutputOptions oOptions) {
        throw new IllegalStateException("This function should not be used, but you used it for " + node);
    }
}
