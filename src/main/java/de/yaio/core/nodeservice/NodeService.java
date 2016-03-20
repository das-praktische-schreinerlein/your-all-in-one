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

import javax.xml.bind.annotation.XmlTransient;

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

    @XmlTransient
    enum RecalcRecurseDirection {
        /** constants for recursion of recalc-functions: recurse all children */
        CHILDREN,
        /** constants for recursion of recalc-functions: recurse only me */
        ONLYME,
        /** constants for recursion of recalc-functions: recurse parents */
        PARENT
    }

    /** constants for recursion of DB-functions: recurse all children */
    int CONST_DB_RECURSIONLEVEL_ALL_CHILDREN = -1;

    ///////////////////////
    // Recalc
    ///////////////////////
    /** 
     * initialize all DataDomainRecalcer
     * must be override and call Exporter.addDataDomaineRecalcer for every Recalcer
     */
    void configureDataDomainRecalcer();

    /** 
     * add DataDomainRecalcer to the Service-Config
     * @param dataDomainRecalcer     instance of the dataDomainRecalcer
     */
    void addDataDomainRecalcer(DataDomainRecalc dataDomainRecalcer);
    
    
    /** 
     * recalcs the DataDomain of the node
     * @param node                   node to recalc
     * @param recurseDirection       Type of recursion (parent, me, children) NodeService.RecalcRecurseDirection.*
     * @throws Exception             parser/format-Exceptions possible
     */
    void doRecalc(DataDomain node, NodeService.RecalcRecurseDirection recurseDirection) throws Exception;

    /** 
     * recalcs the DataDomain of the node before the children are recalced
     * @param node                   node to recalc
     * @param recurseDirection       Type of recursion (parent, me, children) NodeService.RecalcRecurseDirection.*
     * @throws Exception             parser/format-Exceptions possible
     */
    void doRecalcBeforeChildren(DataDomain node, NodeService.RecalcRecurseDirection recurseDirection) throws Exception;

    /** 
     * recalcs the DataDomain of the nodes children
     * @param node                   node which children to recalc
     * @param recurseDirection       Type of recursion (parent, me, children) NodeService.RecalcRecurseDirection.*
     * @throws Exception             parser/format-Exceptions possible
     */
    void doRecalcChildren(DataDomain node, NodeService.RecalcRecurseDirection recurseDirection) throws Exception;

    /** 
     * recalcs the DataDomain of the node after the children are recalced
     * @param node                   node to recalc
     * @param recurseDirection       Type of recursion (parent, me, children) NodeService.RecalcRecurseDirection.*
     * @throws Exception             parser/format-Exceptions possible
     */
    void doRecalcAfterChildren(DataDomain node, NodeService.RecalcRecurseDirection recurseDirection) throws Exception;
    
    
    /** 
     * recalc the WFData of the node
     * @param baseNode               node to get the state from
     * @param recursionDirection     direction for recursivly recalc CONST_RECURSE_DIRECTION_* 
     * @throws Exception             parser/format-Exceptions possible
     */
    void recalcData(DataDomain baseNode, NodeService.RecalcRecurseDirection recursionDirection) throws Exception;


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
     * @param baseNode               node to set new parentNode
     * @param parentNode             new parentNode
     * @param flgRenewParent         flag force Renew of the parent, if nothing changed too 
     */
    void setParentNode(DataDomain baseNode, DataDomain parentNode, boolean flgRenewParent);

    /** 
     * get a List of the Ids of parent-hierarchy 
     * @param baseNode               node
     * @return                       list of the parent-sysUIDs, start with my own parent (not baseNode)
     */
    List<String> getParentIdHierarchy(final DataDomain baseNode);

    /** 
     * get a List of the parent-hierarchy
     * @param baseNode               node
     * @return                       list of the parents, start with my own parent (not baseNode)
     */
    List<BaseNode> getParentHierarchy(final DataDomain baseNode);
    
    
    /** 
     * check if parentSysUID exists in parent-hierarchy
     * @param baseNode               node
     * @param parentSysUID           sysUID to search
     * @return                       list of the parents, start with my own parent (not baseNode)
     */
     boolean hasParent(final DataDomain baseNode, final String parentSysUID);
}
