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

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.importer.parser.Parser;

import java.util.HashMap;
import java.util.Map;

/** 
 *    service-functions for import of Nodes
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ImporterImpl implements Importer {

    protected static int curId = 1;

    // Map mit den Identifier-String un der entprechenden Node-Klase
    protected Map<String, Class<?>> hshNodeTypeIdentifier = new HashMap<String, Class<?>>();
    protected Map<String, String> hshNodeTypeIdentifierVariantMapping = new HashMap<String, String>();
    protected Map<String, String> hshWorkflowNodeTypeMapping = new HashMap<String, String>();
    protected Map<Integer, Parser> hshDataDomainParser = new HashMap<Integer, Parser>();

    protected ImportOptions options = null;
    protected NodeFactory nodeFactory = null;

    /** 
     * create Importer to import nodes
     *  @param options                the importoptions for the parser...
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
    public void addNodeTypeIdentifierVariantMapping(final Map<String, String> stateMap) {
        for (String stateDef : stateMap.keySet()) {
            this.addNodeTypeIdentifierVariantMapping(stateDef, (String) stateMap.get(stateDef));
        }
    }

    protected void addNodeTypeIdentifierVariantMapping(final String type, final String masterType) {
        this.hshNodeTypeIdentifierVariantMapping.put(type, masterType);
    }


    public Map<String, String> getHshNodeTypeIdentifierVariantMapping() {
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
    public void addWorkflowNodeTypeMapping(final Map<String, String> stateMap) {
        for (String stateDef : stateMap.keySet()) {
            this.addWorkflowNodeTypeMapping(stateDef, (String) stateMap.get(stateDef));
        }
    }

    protected void addWorkflowNodeTypeMapping(final String type, final String masterType) {
        this.hshWorkflowNodeTypeMapping.put(type, masterType);
    }

    public Map<String, String> getHshWorkflowNodeTypeMapping() {
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
        NodeFactory myNodeFactory = this.getNodeFactory();

        Class<?> classType = myNodeFactory.getNodeTypeFromText(strFullSrc, srcName);
        DataDomain myNode = myNodeFactory.createNodeObjFromText(
                classType, curId++, strFullSrc, srcName, curParentNode);

        return myNode;
    }
    
}
