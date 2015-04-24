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

import de.yaio.core.node.BaseNode;
import de.yaio.core.nodeservice.BaseNodeService;

/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *     configurator for import of Nodetype: Node
 * 
 * @package de.yaio.datatransfer.importer
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class NodeConfigurator {

    protected static NodeConfigurator me = new NodeConfigurator();

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     ueber diese Funktionen werden die Schlagworte und die dann zu 
     *     über NodeFactory.getNodeTypeFromText zu instantiierenden Node-Klassen 
     *     bekannt gemacht
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param nodeFactory - instance of the nodeFactory which will use the config 
     */
    public static void configureNodeTypeIdentifier(final NodeFactory nodeFactory) {
        nodeFactory.addNodeTypeIdentifier(BaseNodeService.getInstance().getConfigState(), BaseNode.class);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     configure variants of NodeTypeIdentifier (used by parser to normalize 
     *     the sourcedata)<br>
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param importer - instance of the importer which will use the config 
     */
    public static void configureNodeTypes(final Importer importer) {
        importer.addNodeTypeIdentifierVariantMapping(BaseNodeService.getInstance().getConfigState());
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     configure special Workflow-NodeTypeIdentifier (used by Importer.isWFStatus)
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param importer - instance of the importer which will use the config 
     */
    public static void configureWorkflowNodeTypeMapping(final Importer importer) {
        // NOP 
    }
}
