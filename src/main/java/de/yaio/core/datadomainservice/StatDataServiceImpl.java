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
package de.yaio.core.datadomainservice;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.BaseWorkflowData;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.StatData;
import de.yaio.core.nodeservice.NodeService;

/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for dataDomain: StatData
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class StatDataServiceImpl extends DataDomainRecalcImpl implements StatDataService {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(StatDataServiceImpl.class);

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     add me as DataDomainRecalcer to the Service-Config
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param nodeService - instance of the nodeService which will call me as recalcer
     */
    public static void configureDataDomainRecalcer(final NodeService nodeService) {
        DataDomainRecalc baseDataDomainRecalc  = new StatDataServiceImpl();
        nodeService.addDataDomainRecalcer(baseDataDomainRecalc);
    }
    
    @Override
    public void doRecalcBeforeChildren(final DataDomain node, final int recurceDirection) throws Exception {
        // NOP
    }

    @Override
    public void doRecalcAfterChildren(final DataDomain node, final int recurceDirection) throws Exception {
        // Check if node is compatibel
        if (node != null) {
            if (!StatData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        }
        
        // Roll
        this.updateChildrenCount((StatData) node);
    }
    
    @Override
    public Class<?> getRecalcTargetClass() {
        return StatData.class;
    }

    @Override
    public int getRecalcTargetOrder() {
        return StatDataService.CONST_RECALC_ORDER;
    }
    
    
    @Override
    public void updateChildrenCount(final StatData node) throws Exception {
        int childCount = node.getChildNodesByNameMap().size();
        int wfCount = 0;
        int wfToDoCount = 0;
        
        // check Workflow-states of the node
        if (BaseWorkflowData.class.isInstance(node)) {
            BaseWorkflowData wfNode = (BaseWorkflowData) node;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("BaseWorkflow " + node.getNameForLogger() 
                                + " state:" + wfNode.getState());
            }
            if (wfNode.isWFStatus(wfNode.getState())) {
                wfCount++;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("BaseWorkflow " + node.getNameForLogger() 
                                    + " wfCount++ state:" + wfNode.getState());
                }
            }
            if (wfNode.isWFStatusOpen(wfNode.getState())) {
                wfToDoCount++;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("BaseWorkflow " + node.getNameForLogger() 
                                    + " wfTodoCount++ state:" + wfNode.getState());
                }
            }
        }

        
        // iterate children
        for (String nodeName : node.getChildNodesByNameMap().keySet()) {
            DataDomain childNode = node.getChildNodesByNameMap().get(nodeName);
            
            // add childcount
            if (StatData.class.isInstance(childNode)) {
                StatData statChildNode = (StatData) childNode;
                if (statChildNode != null) {
                    if (statChildNode.getStatChildNodeCount() != null) {
                        childCount += statChildNode.getStatChildNodeCount();
                    }
                    if (statChildNode.getStatWorkflowCount() != null) {
                        wfCount += statChildNode.getStatWorkflowCount();
                    }
                    if (statChildNode.getStatWorkflowTodoCount() != null) {
                        wfToDoCount += statChildNode.getStatWorkflowTodoCount();
                    }
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("child " + childNode.getNameForLogger() 
                                        + " wfCount:" + statChildNode.getStatWorkflowCount());
                        LOGGER.debug("child " + childNode.getNameForLogger() 
                                        + " wfTodoCount:" + statChildNode.getStatWorkflowTodoCount());
                    }
                }
            }
        }
            
        // set counts
        node.setStatChildNodeCount(childCount);
        node.setStatWorkflowCount(wfCount);
        node.setStatWorkflowTodoCount(wfToDoCount);
    }
}
