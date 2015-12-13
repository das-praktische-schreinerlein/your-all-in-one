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

import de.yaio.core.node.UrlResNode;
import de.yaio.core.nodeservice.UrlResNodeService;

/** 
 * configurator for import of Nodetype: UrlResNode
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class UrlResNodeConfigurator {

    protected static UrlResNodeConfigurator me = new UrlResNodeConfigurator();

    /** 
     * ueber diese Funktionen werden die Schlagworte und die dann zu 
     * über NodeFactory.getNodeTypeFromText zu instantiierenden Node-Klassen 
     * bekannt gemacht
     * @FeatureDomain                DataImport
     * @FeatureKeywords              Config
     * @param nodeFactory            instance of the nodeFactory which will use the config 
     */
    public static void configureNodeTypeIdentifier(final NodeFactory nodeFactory) {
        nodeFactory.addNodeTypeIdentifier(UrlResNodeService.getInstance().getConfigState(), UrlResNode.class);
    }

    /** 
     * configure variants of NodeTypeIdentifier (used by parser to normalize 
     * the sourcedata)<br>
     * @FeatureDomain                DataImport
     * @FeatureKeywords              Config
     * @param importer               instance of the importer which will use the config 
     */
    public static void configureNodeTypes(final Importer importer) {
        importer.addNodeTypeIdentifierVariantMapping(UrlResNodeService.getInstance().getConfigState());
    }

    /** 
     * configure special Workflow-NodeTypeIdentifier (used by Importer.isWFStatus)
     * @FeatureDomain                DataImport
     * @FeatureKeywords              Config
     * @param importer               instance of the importer which will use the config 
     */
    public static void configureWorkflowNodeTypeMapping(final Importer importer) {
        // NOP 
    }
}
