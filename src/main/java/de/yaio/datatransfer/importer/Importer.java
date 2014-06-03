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
package de.yaio.datatransfer.importer;

import java.util.Map;

import de.yaio.core.datadomain.DataDomain;

/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *    interface with service-functions for import of Nodes
 * 
 * @package de.yaio.datatransfer.importer
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface Importer {

    //////////////
    // service-functions for configuration
    //////////////

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     configure variants of NodeTypeIdentifier (used by parser to normalize 
     *     the sourcedata)<br>
     *     must be override and call Exporter.addNodeTypeIdentifierVariantMapping 
     *     for every nodeType 
     * <h4>FeatureKeywords:</h4>
     *     Config
     */
    public void initNodeTypeIdentifierVariantMapping();

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     configure variants of NodeTypeIdentifier (used by parser to normalize 
     *     the sourcedata)<br>
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param stateMap - Map der Schlagwortvarianten (Variante = NodeTypeIdentifier)
     */
    public void addNodeTypeIdentifierVariantMapping(Map<String, Object> stateMap);

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     get variants for NodeTypeIdentifier
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Map - Map of variants for NodeTypeIdentifier
     *         (variant = NodeTypeIdentifier)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return - variants for NodeTypeIdentifier
     */
    public Map<String, Object> getHshNodeTypeIdentifierVariantMapping();

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     configure special Workflow-NodeTypeIdentifier (used by Importer.isWFStatus)<br>
     *     must be override and call Exporter.addWorkflowNodeTypeMapping 
     *     for every nodeType 
     * <h4>FeatureKeywords:</h4>
     *     Config
     */
    public void initWorkflowNodeTypeMapping();

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     configure special Workflow-NodeTypeIdentifier (used by Importer.isWFStatus)
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param stateMap - Map der Schlagwortvarianten (Variante = NodeTypeIdentifier)
     */
    public void addWorkflowNodeTypeMapping(Map<String, Object> stateMap);

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     retuns special Workflow-NodeTypeIdentifier
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Map - Map of Workflow-NodeTypeIdentifier 
     *         (NodeTypeIdentifier)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return - Map of Workflow-NodeTypeIdentifier
     */
    public Map<String, Object> getHshWorkflowNodeTypeMapping();
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     returns current NodeFactory for creation of Nodes
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue NodeFactory - current NodeFactory for creation of Nodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return - current NodeFactory for creation of Nodes
     */
    public NodeFactory getNodeFactory();

    ////////////////
    // Generierungs-Funktionen
    ////////////////
    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     creates from strFullSrc with NodeFactory new Nodes and initializes them
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Node - new node Node
     *     <li>updates memberVariable node.xx = runs NodeFactrory.parseNodeDataDomains 
     *         to initialize node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Parser
     * @param id - id of the new Node
     * @param strFullSrc - source of the node to create
     * @param srcName - Name of the node (for parsing)
     * @param curParentNode - ParentNode
     * @return - new Node-instance
     * @throws Exception
     */
    public DataDomain createNodeObjFromText(int id, String strFullSrc, String srcName, 
            DataDomain curParentNode)
                    throws Exception;

    ////////////////
    // Service-Funktionen
    ////////////////
    /**
     * <h4>FeatureDomain:</h4>
     *     Service
     * <h4>FeatureDescription:</h4>
     *     checks Map of Workflow-NodeTypeIdentifier and
     *     returns weather the state is a Workflow-state
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - true/false Workflow-state from getHshWorkflowNodeTypeMapping
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Service Workflow Parser
     * @return true/false Workflow-state from getHshWorkflowNodeTypeMapping
     */
    public boolean isWFStatus (String state);
}