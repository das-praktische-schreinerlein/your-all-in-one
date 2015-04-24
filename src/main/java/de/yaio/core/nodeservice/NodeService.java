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
package de.yaio.core.nodeservice;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomainservice.DataDomainRecalc;

/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     interface for businesslogic of nodes
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
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
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     initialize all DataDomainRecalcer
     *     must be override and call Exporter.addDataDomaineRecalcer for every Recalcer
     * <h4>FeatureKeywords:</h4>
     *     Config
     */
    void configureDataDomainRecalcer();

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     add DataDomainRecalcer to the Service-Config
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param dataDomainRecalcer - instance of the dataDomainRecalcer
     */
    void addDataDomainRecalcer(DataDomainRecalc dataDomainRecalcer);
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     recalcs the DataDomain of the node
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberVariables
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BsuinessLogic
     * @param node - node to recalc
     * @param recurseDirection - Type of recursion (parent, me, children) NodeService.CONST_RECURSE_DIRECTION_*
     * @throws Exception - parser/format-Exceptions possible
     */
    void doRecalc(DataDomain node, int recurseDirection) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     recalcs the DataDomain of the node before the children are recalced
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberVariables
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BsuinessLogic
     * @param node - node to recalc
     * @param recurceDirection - Type of recursion (parent, me, children) NodeService.CONST_RECURSE_DIRECTION_*
     * @throws Exception - parser/format-Exceptions possible
     */
    void doRecalcBeforeChildren(DataDomain node, int recurceDirection) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     recalcs the DataDomain of the nodes children
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates children of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BsuinessLogic
     * @param node - node which children to recalc
     * @param recurceDirection - Type of recursion (parent, me, children) NodeService.CONST_RECURSE_DIRECTION_*
     * @throws Exception - parser/format-Exceptions possible
     */
    void doRecalcChildren(DataDomain node, int recurceDirection) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     recalcs the DataDomain of the node after the children are recalced
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberVariables
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BsuinessLogic
     * @param node - node to recalc
     * @param recurceDirection - Type of recursion (parent, me, children) NodeService.CONST_RECURSE_DIRECTION_*
     * @throws Exception - parser/format-Exceptions possible
     */
    void doRecalcAfterChildren(DataDomain node, int recurceDirection) throws Exception;
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     recalc the WFData of the node
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberfields of dataDomain PlanChildrenSum
     *     <li>updates memberfields of dataDomain IstChildrenSum
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param baseNode - node to get the state from
     * @param recursionDirection - direction for recursivly recalc CONST_RECURSE_DIRECTION_* 
     * @throws Exception - parser/format-Exceptions possible
     */
    void recalcData(DataDomain baseNode, int recursionDirection) throws Exception;

    ///////////////////////
    // Hirarchy
    ///////////////////////
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     sets the parentNode of the baseNode and updates recursivly 
     *     field Ebene of all childnodes 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberVariable baseNode.parentNode
     *     <li>updates memberVariable parentNode.childNodes
     *     <li>updates memberVariable baseNode.ebene for all childs
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param baseNode - node to set new parentNode
     * @param parentNode - new parentNode
     * @param flgRenewParent - flag force Renew of the parent, if nothing changed too 
     */
    void setParentNode(DataDomain baseNode, DataDomain parentNode, boolean flgRenewParent);

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns the max level of children for this node 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return maximum childLevel
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param baseNode - node
     * @return maximum childLevel
     */
    int getMaxChildEbene(DataDomain baseNode);

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns parenthierarchy
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return parenthierarchy
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param baseNode - node
     * @param pdelimiter - text to delimit the diffrent parentnames
     * @param directionForward - if set reverse the order
     * @return parenthierarchy
     */
    String getParentNameHirarchry(final DataDomain baseNode,
                                  final String pdelimiter, 
                                  final boolean directionForward);

    ///////////////////////
    // Workflow
    ///////////////////////
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     checks weather the state is a configurated workflow-state
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - workflow-state yes/no
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param state - state to check
     * @return workflow-state yes/no
     */
    boolean isWFStatus(String state);

    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     checks weather the state is a configurated workflow-state for DONE
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - workflow-DONE yes/no
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param state - state to check
     * @return workflow-DONE yes/no
     */
    boolean isWFStatusDone(String state);

    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     checks weather the state is a configurated workflow-state for OPEN
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - workflow-OPEN yes/no
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param state - state to check
     * @return workflow-OPEN yes/no
     */
    boolean isWFStatusOpen(String state);

    /**
     * <h4>FeatureDomain:</h4>
     *     Workflow
     * <h4>FeatureDescription:</h4>
     *     checks weather the state is a configurated workflow-state for CANCELED
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - workflow-CANCELED yes/no
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Worflow
     * @param state - state to check
     * @return workflow-CANCELED yes/no
     */
    boolean isWFStatusCanceled(String state);
    

    ///////////////////////
    // Name+Id-Services
    ///////////////////////
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns WorkingId to identify in process (parsing...)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return WorkingId to identify in process (parsing...)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param baseNode - node
     * @return WorkingId to identify in process (parsing...)
     */
    String getWorkingId(final DataDomain baseNode);

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns name for logging
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return name for logging
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param baseNode - node
     * @return name for logging
     */
    String getNameForLogger(final DataDomain baseNode);

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns the data to create a checksum from
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return datastring
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param baseNode - node
     * @return datastring
     * @throws Exception 
     */
    String getDataBlocks4CheckSum(final DataDomain baseNode) throws Exception;
}
