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
package de.yaio.datatransfer.importer;

import java.util.Map;

import de.yaio.core.datadomain.DataDomain;

/** 
 *    interface with service-functions for import of Nodes
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface Importer {

    //////////////
    // service-functions for configuration
    //////////////

    /** 
     * configure variants of NodeTypeIdentifier (used by parser to normalize 
     * the sourcedata)<br>
     * must be override and call Exporter.addNodeTypeIdentifierVariantMapping 
     * for every nodeType 
     * @FeatureDomain                DataImport
     * @FeatureKeywords              Config
     */
    void initNodeTypeIdentifierVariantMapping();

    /** 
     * configure variants of NodeTypeIdentifier (used by parser to normalize 
     * the sourcedata)<br>
     * @FeatureDomain                DataImport
     * @FeatureKeywords              Config
     * @param constMapNodetypeIdentifier Map der Schlagwortvarianten (Variante = NodeTypeIdentifier)
     */
    void addNodeTypeIdentifierVariantMapping(Map<String, String> constMapNodetypeIdentifier);

    /** 
     * get variants for NodeTypeIdentifier
     * @FeatureDomain                DataImport
     * @FeatureResult                returnValue Map - Map of variants for NodeTypeIdentifier (variant = NodeTypeIdentifier)
     * @FeatureKeywords              Config
     * @return                       variants for NodeTypeIdentifier
     */
    Map<String, String> getHshNodeTypeIdentifierVariantMapping();

    /** 
     * configure special Workflow-NodeTypeIdentifier (used by Importer.isWFStatus)<br>
     * must be override and call Exporter.addWorkflowNodeTypeMapping 
     * for every nodeType 
     * @FeatureDomain                DataImport
     * @FeatureKeywords              Config
     */
    void initWorkflowNodeTypeMapping();

    /** 
     * configure special Workflow-NodeTypeIdentifier (used by Importer.isWFStatus)
     * @FeatureDomain                DataImport
     * @FeatureKeywords              Config
     * @param stateMap               Map der Schlagwortvarianten (Variante = NodeTypeIdentifier)
     */
    void addWorkflowNodeTypeMapping(Map<String, String> stateMap);

    /** 
     * retuns special Workflow-NodeTypeIdentifier
     * @FeatureDomain                DataImport
     * @FeatureResult                returnValue Map - Map of Workflow-NodeTypeIdentifier  (NodeTypeIdentifier)
     * @FeatureKeywords              Config
     * @return                       Map of Workflow-NodeTypeIdentifier
     */
    Map<String, String> getHshWorkflowNodeTypeMapping();
    
    
    /** 
     * returns current NodeFactory for creation of Nodes
     * @FeatureDomain                DataImport
     * @FeatureResult                returnValue NodeFactory - current NodeFactory for creation of Nodes
     * @FeatureKeywords              Config
     * @return                       current NodeFactory for creation of Nodes
     */
    NodeFactory getNodeFactory();

    ////////////////
    // Generierungs-Funktionen
    ////////////////
    /** 
     * creates from strFullSrc with NodeFactory new Nodes and initializes them
     * @FeatureDomain                DataImport
     * @FeatureResult                returnValue Node - new node Node
     * @FeatureResult                updates memberVariable node.xx = runs NodeFactrory.parseNodeDataDomains  to initialize node
     * @FeatureKeywords              Parser
     * @param id                     id of the new Node
     * @param strFullSrc             source of the node to create
     * @param srcName                Name of the node (for parsing)
     * @param curParentNode          ParentNode
     * @return                       new Node-instance
     * @throws Exception             parser/format-Exceptions possible
     */
    DataDomain createNodeObjFromText(int id, String strFullSrc, String srcName, 
            DataDomain curParentNode)
                    throws Exception;

    ////////////////
    // Service-Funktionen
    ////////////////
    /** 
     * checks Map of Workflow-NodeTypeIdentifier and
     * returns weather the state is a Workflow-state
     * @FeatureDomain                Service
     * @FeatureResult                returnValue boolean - true/false Workflow-state from getHshWorkflowNodeTypeMapping
     * @FeatureKeywords              Service Workflow Parser
     * @param state                  the state to check
     * @return                       true/false Workflow-state from getHshWorkflowNodeTypeMapping
     */
    boolean isWFStatus(String state);
}
