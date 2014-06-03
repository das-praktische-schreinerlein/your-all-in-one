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

    public static void configureNodeTypeIdentifier(NodeFactory nodeFactory) {
        nodeFactory.addNodeTypeIdentifier(BaseNode.CONST_MAP_NODETYPE_IDENTIFIER, BaseNode.class);
    }

    public static void configureNodeTypes(Importer importer) {
        importer.addNodeTypeIdentifierVariantMapping(BaseNode.CONST_MAP_NODETYPE_IDENTIFIER);
    }

    public static void configureWorkflowNodeTypeMapping(Importer importer) {
        // NOP 
    }
}
