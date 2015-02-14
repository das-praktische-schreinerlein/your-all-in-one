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

import java.util.HashMap;
import java.util.Map;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.importer.parser.Parser;

/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *    service-functions for import of Nodes
 * 
 * @package de.yaio.datatransfer.importer
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ImporterImpl implements Importer {

    // Map mit den Identifier-String un der entprechenden Node-Klase
    protected Map<String, Class<?>> hshNodeTypeIdentifier = new HashMap<String, Class<?>>();
    protected Map<String, Object> hshNodeTypeIdentifierVariantMapping = new HashMap<String, Object>();
    protected Map<String, Object> hshWorkflowNodeTypeMapping = new HashMap<String, Object>();
    protected Map<Integer, Parser> hshDataDomainParser = new HashMap<Integer, Parser>();

    protected static int curId = 1;

    protected ImportOptions options = null;
    protected NodeFactory nodeFactory = null;

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     create Importer to import nodes
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the importer
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     *  @param options - the importoptions for the parser...
     */
    public ImporterImpl(final ImportOptions options) {
        this.options = options;
        
        // NodeFactory anlegen
        this.initNodeFactory(options);

        // NodeTypes konfigurieren
        this.initNodeTypeIdentifierVariantMapping();
        this.initWorkflowNodeTypeMapping();
    }

    //////////////
    // Config-Funktionen
    //////////////
    @Override
    public void initNodeTypeIdentifierVariantMapping() {
        NodeConfigurator.configureNodeTypes(this);
        EventNodeConfigurator.configureNodeTypes(this);
        InfoNodeConfigurator.configureNodeTypes(this);
        SymLinkNodeConfigurator.configureNodeTypes(this);
        TaskNodeConfigurator.configureNodeTypes(this);
        UrlResNodeConfigurator.configureNodeTypes(this);
    }

    @Override
    public void addNodeTypeIdentifierVariantMapping(final Map<String, Object> stateMap) {
        for (String stateDef : stateMap.keySet()) {
            this.addNodeTypeIdentifierVariantMapping(stateDef, (String) stateMap.get(stateDef));
        }
    }

    protected void addNodeTypeIdentifierVariantMapping(final String type, final String masterType) {
        this.hshNodeTypeIdentifierVariantMapping.put(type, masterType);
    }


    public Map<String, Object> getHshNodeTypeIdentifierVariantMapping() {
        return hshNodeTypeIdentifierVariantMapping;
    }
    
    protected void initNodeFactory(final ImportOptions iOptions) {
        this.nodeFactory = new NodeFactoryImpl(iOptions);
    }
    
    public NodeFactory getNodeFactory() {
        return this.nodeFactory;
    }

    @Override
    public void initWorkflowNodeTypeMapping() {
        NodeConfigurator.configureWorkflowNodeTypeMapping(this);
        EventNodeConfigurator.configureWorkflowNodeTypeMapping(this);
        InfoNodeConfigurator.configureWorkflowNodeTypeMapping(this);
        SymLinkNodeConfigurator.configureWorkflowNodeTypeMapping(this);
        TaskNodeConfigurator.configureWorkflowNodeTypeMapping(this);
        UrlResNodeConfigurator.configureWorkflowNodeTypeMapping(this);
    }

    @Override
    public void addWorkflowNodeTypeMapping(final Map<String, Object> stateMap) {
        for (String stateDef : stateMap.keySet()) {
            this.addWorkflowNodeTypeMapping(stateDef, (String) stateMap.get(stateDef));
        }
    }

    protected void addWorkflowNodeTypeMapping(final String type, final String masterType) {
        this.hshWorkflowNodeTypeMapping.put(type, masterType);
    }

    public Map<String, Object> getHshWorkflowNodeTypeMapping() {
        return hshWorkflowNodeTypeMapping;
    }

    public boolean isWFStatus(final String state) {
        if (this.hshWorkflowNodeTypeMapping.get(state) != null) {
            return true;
        }

        return false;
    }

    @Override
    public DataDomain createNodeObjFromText(final int id, final String strFullSrc,
            final String srcName, final DataDomain curParentNode) throws Exception {
        NodeFactory nodeFactory = this.getNodeFactory();

        Class<?> classType = nodeFactory.getNodeTypeFromText(strFullSrc, srcName);
        DataDomain myNode = nodeFactory.createNodeObjFromText(
                classType, curId++, strFullSrc, srcName, curParentNode);

        return myNode;
    }
    
}
