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
package de.yaio.core.datadomainservice;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.BaseWorkflowData;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.StatData;
import de.yaio.core.dbservice.DBFilter;
import de.yaio.core.node.InfoNode;
import de.yaio.core.node.UrlResNode;
import de.yaio.core.nodeservice.NodeService;

/** 
 * businesslogic for dataDomain: StatData
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class StatDataServiceImpl extends DataDomainRecalcImpl implements StatDataService {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(StatDataServiceImpl.class);

    private static StatDataServiceImpl instance = new StatDataServiceImpl();
    
    /** 
     * return the main instance of this service
     * @FeatureDomain                Persistence
     * @FeatureResult                return the main instance of this service
     * @FeatureKeywords              Persistence
     * @return                       the main instance of this service
     */
    public static StatDataServiceImpl getInstance() {
        return instance;
    }

    /** 
     * add me as DataDomainRecalcer to the Service-Config
     * @FeatureDomain                DataExport Presentation
     * @FeatureKeywords              Config
     * @param nodeService            instance of the nodeService which will call me as recalcer
     */
    public static void configureDataDomainRecalcer(final NodeService nodeService) {
        DataDomainRecalc baseDataDomainRecalc = StatDataServiceImpl.getInstance();
        nodeService.addDataDomainRecalcer(baseDataDomainRecalc);
    }
    
    @Override
    public void doRecalcBeforeChildren(final DataDomain node, final int recurceDirection) throws Exception {
        // NOP
    }

    @Override
    public void doRecalcAfterChildren(final DataDomain node, final int recurceDirection) throws Exception {
        if (node == null) {
            return;
        }

        // Check if node is compatibel
        if (!StatData.class.isInstance(node)) {
            throw new IllegalArgumentException();
        }
        
        // Roll
        this.updateChildrenCount((StatData) node);
    }
    
    @Override
    public void doRecalcWhenTriggered(final DataDomain node, final int recurceDirection) throws Exception {
        // NOP
    }

    @Override
    public List<DBFilter> getDBTriggerFilter() throws Exception {
        // NOP
        return new ArrayList<DBFilter>();
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
        int urlResCount = 0;
        int infoCount = 0;
        
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
        if (UrlResNode.class.isInstance(node)) {
            // check UrlRes of the node
            urlResCount++;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("UrlResNode " + node.getNameForLogger() 
                                + " urlResCount++");
            }
        } else if (InfoNode.class.isInstance(node)) {
            // check Info of the node
            infoCount++;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("InfoNode " + node.getNameForLogger() 
                                + " infoCount++");
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
                    if (statChildNode.getStatInfoCount() != null) {
                        infoCount += statChildNode.getStatInfoCount();
                    }
                    if (statChildNode.getStatUrlResCount() != null) {
                        urlResCount += statChildNode.getStatUrlResCount();
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
        node.setStatInfoCount(infoCount);
        node.setStatUrlResCount(urlResCount);
    }
}
