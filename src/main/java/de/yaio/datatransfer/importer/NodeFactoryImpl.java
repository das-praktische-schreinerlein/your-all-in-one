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

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.BaseData;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomainservice.MetaDataService;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.importer.parser.BaseDataParserImpl;
import de.yaio.datatransfer.importer.parser.DescDataParserImpl;
import de.yaio.datatransfer.importer.parser.DocLayoutDataParserImpl;
import de.yaio.datatransfer.importer.parser.IstDataParserImpl;
import de.yaio.datatransfer.importer.parser.MetaDataParserImpl;
import de.yaio.datatransfer.importer.parser.Parser;
import de.yaio.datatransfer.importer.parser.PlanDataParserImpl;
import de.yaio.datatransfer.importer.parser.ResLocDataParserImpl;
import de.yaio.datatransfer.importer.parser.SymLinkDataParserImpl;
import de.yaio.datatransfer.importer.parser.SysDataParserImpl;

/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *    factory-functions for creation of Nodes
 * 
 * @package de.yaio.datatransfer.importer
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class NodeFactoryImpl implements NodeFactory {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(NodeFactoryImpl.class);

    // Map mit den Identifier-String un der entprechenden Node-Klase
    protected Map<String, Class<?>> hshNodeTypeIdentifier = new HashMap<String, Class<?>>();
    protected TreeSet<Parser> hshDataDomainParser = new TreeSet<Parser>();

    // Parameter des Standard-Node-Konstruktors
    private static final Class<?>[] CONST_NODE_CONSTRUCTOR = {};

    protected static int curId = 1;
    protected ImportOptions options = null;

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     create Factory to create nodes
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the importer
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     *  @param options - the importoptions for the parser...
     */
    public NodeFactoryImpl(ImportOptions options) {
        this.options = options;

        // NodeTypes konfigurieren
        this.initDataDomainParser();
        this.initNodeTypeIdentifier();
    }

    //////////////
    // Config-Funktionen
    //////////////
    @Override
    public void initNodeTypeIdentifier() {
        NodeConfigurator.configureNodeTypeIdentifier(this);
        EventNodeConfigurator.configureNodeTypeIdentifier(this);
        InfoNodeConfigurator.configureNodeTypeIdentifier(this);
        SymLinkNodeConfigurator.configureNodeTypeIdentifier(this);
        TaskNodeConfigurator.configureNodeTypeIdentifier(this);
        UrlResNodeConfigurator.configureNodeTypeIdentifier(this);
    }

    @Override
    public void addNodeTypeIdentifier(Map<String, Object> stateMap, Class<?> classType) {
        for (String stateDef : stateMap.keySet()) {
            this.putNodeTypeIdentifier(stateDef, classType);
        }
    }

    protected void putNodeTypeIdentifier(String type, Class<?> classType) {
        this.hshNodeTypeIdentifier.put(type, classType);
    }

    public Map<String, Class<?>> getHshNodeTypeIdentifier() {
        return hshNodeTypeIdentifier;
    }

    @Override
    public void initDataDomainParser() {
        BaseDataParserImpl.configureDataDomainParser(this);
        SymLinkDataParserImpl.configureDataDomainParser(this);
        ResLocDataParserImpl.configureDataDomainParser(this);
        DocLayoutDataParserImpl.configureDataDomainParser(this);
        IstDataParserImpl.configureDataDomainParser(this);
        PlanDataParserImpl.configureDataDomainParser(this);
        MetaDataParserImpl.configureDataDomainParser(this);
        SysDataParserImpl.configureDataDomainParser(this);
        DescDataParserImpl.configureDataDomainParser(this);
    }

    @Override
    public void addDataDomainParser(Parser parser) {
        if (parser.getTargetOrder() < 0 ) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: Targetorder < 0 TargetOrder:" 
                        + parser.getTargetOrder()
                        + " Parser:" + parser.getClass().getName());
                return;
            }
        }
        
        this.hshDataDomainParser.add(parser);
    }

    @Override
    public Collection<Parser> getDataDomainParser() {
        return this.hshDataDomainParser;
    }


    public TreeSet<Parser> getHshDataDomainParser() {
        return hshDataDomainParser;
    }

    ////////////////
    // Parser-Funktionen
    ////////////////
    @Override
    public int parseNodeDataDomains(DataDomain node, ImportOptions options)  throws Exception {
        int found = 0;
        for (Parser parser : getDataDomainParser()) {
            found += this.parseNodeDataDomain(node, parser, options);
        }
        return found;
    }

    @Override
    public int parseNodeDataDomain(DataDomain node, Parser parser, ImportOptions options) throws Exception {
        // nur parsen, wenn zustaendig
        if (parser.getTargetClass().isInstance(node)) {
            return parser.parseFromName(node, options);
        } else {
            LOGGER.debug("parseNodeDataDomain SKIP: Node is not of type + " 
                    + parser.getTargetClass().getName() 
                    + " Node=" + node.getNameForLogger());
        }
        return 0;
    }

    ////////////////
    // Generierungs-Funktionen
    ////////////////
    @Override
    public DataDomain createNodeObjFromText(Class<?> classType, int id, String strFullSrc, String srcName,
            DataDomain curParentNode) throws Exception {

        // Node anlegen
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("create Node: " 
                    + classType.getName() 
                    + " id=" + id 
                    + " strFullSrc=" + strFullSrc);

        // Node anhand des Konstruktors anlegen
        Constructor<?> constr = classType.getDeclaredConstructor(
                CONST_NODE_CONSTRUCTOR
                );
        BaseData node = (BaseData) constr.newInstance();

        // Nodetype extrahieren
        Map<String, Class<?>> hshCurNodeTypeIdentifier = this.hshNodeTypeIdentifier;
        String nodeTypeIdentifier = 
                getNodeTypeIdentifierFromText(hshCurNodeTypeIdentifier, srcName);
        if (nodeTypeIdentifier != null) {
            if (srcName.startsWith(nodeTypeIdentifier  + " ")) {
                srcName = srcName.substring(nodeTypeIdentifier.length(), srcName.length());
                srcName = srcName.trim();
                if (srcName.startsWith("- ")) {
                    srcName = srcName.substring("- ".length(), srcName.length());
                }
            }
        }

        // Daten belegen
        node.setFullSrc(strFullSrc);
        node.setSrcName(strFullSrc);
        node.setName(srcName);
        node.setImportTmpId(new Long(id));
        node.setEbene(0);
        node.setType(nodeTypeIdentifier);
        node.setParentNode(curParentNode);

        
        // Node initialisieren (Parser)
        parseNodeDataDomains(node, options);

        return node;
    }

    @Override
    public Class<?> getNodeTypeFromText(String strFullSrc, String srcName) throws Exception {
        // Node-Klasse festlegen
        Class<?> classType = BaseNode.class;
        Map<String, Class<?>> hshCurNodeTypeIdentifier = this.hshNodeTypeIdentifier;
        String nodeTypeIdentifier = 
                getNodeTypeIdentifierFromText(hshCurNodeTypeIdentifier, srcName);
        if (nodeTypeIdentifier != null && 
                hshCurNodeTypeIdentifier.get(nodeTypeIdentifier) != null) {
            classType = hshCurNodeTypeIdentifier.get(nodeTypeIdentifier);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("set Nodetyp:" + classType + " for Name:" + srcName);
        }

        return classType;
    }

    protected String getNodeTypeIdentifierFromText(
            Map<String, Class<?>>hshCurNodeTypeIdentifier, String srcName) throws Exception {
        // TODO - change implementation: extract first word an check if key exists in hash
        for (Iterator<String> iter = hshCurNodeTypeIdentifier.keySet().iterator();
                iter.hasNext();) {
            // die Identifier durchlaufen und den ersten Treffer benutzen
            String typeIdentifier = iter.next().toString();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Check typeIdentifier:" + typeIdentifier + " for Name:" + srcName);
            }
            if (srcName.startsWith(typeIdentifier + " ")) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("set NodetypeIdentifier:" + typeIdentifier + " for Name:" + srcName);
                }
                return typeIdentifier;
            }
        }
        return null;
    }

    @Override
    public void setMetaDataService(MetaDataService metaDataService) {
        BaseNode.setMetaDataService(metaDataService);
    }

    @Override
    public MetaDataService getMetaDataService() {
        return BaseNode.getConfiguredMetaDataService();
    }
}
