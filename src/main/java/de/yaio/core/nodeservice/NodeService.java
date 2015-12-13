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
package de.yaio.core.nodeservice;

import java.util.List;
import java.util.Map;

import de.yaio.core.datadomain.BaseWorkflowData.WorkflowState;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomainservice.DataDomainRecalc;
import de.yaio.core.node.BaseNode;

/** 
 * interface for businesslogic of nodes
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface NodeService {
    
    /** constants for recursion of recalc-functions: recurse all children */
    int CONST_RECURSE_DIRECTION_CHILDREN = -1;
    /** constants for recursion of recalc-functions: recurse only me */
    int CONST_RECURSE_DIRECTION_ONLYME = 0;
    /** constants for recursion of recalc-functions: recurse parents */
    int CONST_RECURSE_DIRECTION_PARENT = 1;
    
    /** constants for recursion of DB-functions: recurse all children */
    int CONST_DB_RECURSIONLEVEL_ALL_CHILDREN = -1;

    ///////////////////////
    // Recalc
    ///////////////////////
    /** 
     * initialize all DataDomainRecalcer
     * must be override and call Exporter.addDataDomaineRecalcer for every Recalcer
     * @FeatureDomain                BusinessLogic
     * @FeatureKeywords              Config
     */
    void configureDataDomainRecalcer();

    /** 
     * add DataDomainRecalcer to the Service-Config
     * @FeatureDomain                DataExport Presentation
     * @FeatureKeywords              Config
     * @param dataDomainRecalcer     instance of the dataDomainRecalcer
     */
    void addDataDomainRecalcer(DataDomainRecalc dataDomainRecalcer);
    
    
    /** 
     * recalcs the DataDomain of the node
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                updates memberVariables
     * @FeatureKeywords              BsuinessLogic
     * @param node                   node to recalc
     * @param recurseDirection       Type of recursion (parent, me, children) NodeService.CONST_RECURSE_DIRECTION_*
     * @throws Exception             parser/format-Exceptions possible
     */
    void doRecalc(DataDomain node, int recurseDirection) throws Exception;

    /** 
     * recalcs the DataDomain of the node before the children are recalced
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                updates memberVariables
     * @FeatureKeywords              BsuinessLogic
     * @param node                   node to recalc
     * @param recurceDirection       Type of recursion (parent, me, children) NodeService.CONST_RECURSE_DIRECTION_*
     * @throws Exception             parser/format-Exceptions possible
     */
    void doRecalcBeforeChildren(DataDomain node, int recurceDirection) throws Exception;

    /** 
     * recalcs the DataDomain of the nodes children
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                updates children of the node
     * @FeatureKeywords              BsuinessLogic
     * @param node                   node which children to recalc
     * @param recurceDirection       Type of recursion (parent, me, children) NodeService.CONST_RECURSE_DIRECTION_*
     * @throws Exception             parser/format-Exceptions possible
     */
    void doRecalcChildren(DataDomain node, int recurceDirection) throws Exception;

    /** 
     * recalcs the DataDomain of the node after the children are recalced
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                updates memberVariables
     * @FeatureKeywords              BsuinessLogic
     * @param node                   node to recalc
     * @param recurceDirection       Type of recursion (parent, me, children) NodeService.CONST_RECURSE_DIRECTION_*
     * @throws Exception             parser/format-Exceptions possible
     */
    void doRecalcAfterChildren(DataDomain node, int recurceDirection) throws Exception;
    
    
    /** 
     * recalc the WFData of the node
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                updates memberfields of dataDomain PlanChildrenSum
     * @FeatureResult                updates memberfields of dataDomain IstChildrenSum
     * @FeatureKeywords              BusinessLogic
     * @param baseNode               node to get the state from
     * @param recursionDirection     direction for recursivly recalc CONST_RECURSE_DIRECTION_* 
     * @throws Exception             parser/format-Exceptions possible
     */
    void recalcData(DataDomain baseNode, int recursionDirection) throws Exception;


    ///////////////////////
    // Workflow
    ///////////////////////
    Map<String, String> getConfigState();
    Map<String, WorkflowState> getConfigWorkflowState();
    Map<WorkflowState, String> getConfigWorkflowStateState();

    ///////////////////////
    // Hirarchy
    ///////////////////////
    /** 
     * sets the parentNode of the baseNode and updates recursivly 
     * field Ebene of all childnodes 
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                updates memberVariable baseNode.parentNode
     * @FeatureResult                updates memberVariable parentNode.childNodes
     * @FeatureResult                updates memberVariable baseNode.ebene for all childs
     * @FeatureKeywords              BusinessLogic
     * @param baseNode               node to set new parentNode
     * @param parentNode             new parentNode
     * @param flgRenewParent         flag force Renew of the parent, if nothing changed too 
     */
    void setParentNode(DataDomain baseNode, DataDomain parentNode, boolean flgRenewParent);

    /** 
     * get a List of the Ids of parent-hierarchy 
     * @FeatureDomain                Persistence
     * @FeatureResult                returnValue List<String> - list of the parent-sysUIDs, start with my own parent (not me)
     * @FeatureKeywords              Persistence
     * @param baseNode               node
     * @return                       list of the parent-sysUIDs, start with my own parent (not baseNode)
     */
    List<String> getParentIdHierarchy(final DataDomain baseNode);

    /** 
     * get a List of the parent-hierarchy
     * @FeatureDomain                Persistence
     * @FeatureResult                returnValue List<BaseNode> - list of the parents, start with my own parent (not me)
     * @FeatureKeywords              Persistence
     * @param baseNode               node
     * @return                       list of the parents, start with my own parent (not baseNode)
     */
    List<BaseNode> getParentHierarchy(final DataDomain baseNode);
    
    
    /** 
     * check if parentSysUID exists in parent-hierarchy
     * @FeatureDomain                Persistence
     * @FeatureResult                returnValue boolean - true/false weather parentSysUID exists or not in hierarchy
     * @FeatureKeywords              Persistence
     * @param baseNode               node
     * @param parentSysUID           sysUID to search
     * @return                       list of the parents, start with my own parent (not baseNode)
     */
    public boolean hasParent(final DataDomain baseNode, final String parentSysUID);
}
