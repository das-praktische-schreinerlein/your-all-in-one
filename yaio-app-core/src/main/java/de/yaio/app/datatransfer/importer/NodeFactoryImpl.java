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
package de.yaio.app.datatransfer.importer;

import de.yaio.app.core.datadomain.BaseData;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.datadomainservice.MetaDataService;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.importer.parser.*;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.*;

/** 
 *    factory-functions for creation of Nodes
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class NodeFactoryImpl implements NodeFactory {

    protected static int curId = 1;

    // Parameter des Standard-Node-Konstruktors
    private static final Class<?>[] CONST_NODE_CONSTRUCTOR = {};

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(NodeFactoryImpl.class);

    // Map mit den Identifier-String un der entprechenden Node-Klase
    protected Map<String, Class<?>> hshNodeTypeIdentifier = new HashMap<String, Class<?>>();
    protected Set<Parser> hshDataDomainParser = new TreeSet<Parser>();

    protected ImportOptions options = null;

    /** 
     * create Factory to create nodes
     *  @param options                the importoptions for the parser...
     */
    public NodeFactoryImpl(final ImportOptions options) {
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
    public void addNodeTypeIdentifier(final Map<String, String> stateMap, final Class<?> classType) {
        for (String stateDef : stateMap.keySet()) {
            this.putNodeTypeIdentifier(stateDef, classType);
        }
    }

    protected void putNodeTypeIdentifier(final String type, final Class<?> classType) {
        this.hshNodeTypeIdentifier.put(type, classType);
    }

    @Override
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
        PlanCalcDataParserImpl.configureDataDomainParser(this);
        MetaDataParserImpl.configureDataDomainParser(this);
        SysDataParserImpl.configureDataDomainParser(this);
        DescDataParserImpl.configureDataDomainParser(this);
    }

    @Override
    public void addDataDomainParser(final Parser parser) {
        if (parser.getTargetOrder() < 0) {
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

    @Override
    public Set<Parser> getHshDataDomainParser() {
        return hshDataDomainParser;
    }

    ////////////////
    // Parser-Funktionen
    ////////////////
    @Override
    public int parseNodeDataDomains(final DataDomain node, final ImportOptions options) throws ParserException {
        int found = 0;
        for (Parser parser : getDataDomainParser()) {
            found += this.parseNodeDataDomain(node, parser, options);
        }
        return found;
    }

    @Override
    public int parseNodeDataDomain(final DataDomain node, final Parser parser, 
                                   final ImportOptions options) throws ParserException {
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

    @Override
    public void setTimeZone(final TimeZone timeZone) {
        for (Parser parser : this.hshDataDomainParser) {
            parser.setTimeZone(timeZone);
        }
    }

    ////////////////
    // Generierungs-Funktionen
    ////////////////
    @Override
    public DataDomain createNodeObjFromText(final Class<?> classType, final int id, 
                                            final String strFullSrc, final String pSrcName,
                                            final DataDomain curParentNode)throws ParserException {
        String srcName = pSrcName;

        // Node anlegen
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("create Node: " 
                    + classType.getName() 
                    + " id=" + id 
                    + " strFullSrc=" + strFullSrc);
        }

        // Node anhand des Konstruktors anlegen
        BaseData node = null;
        try {
            Constructor<?> constr = classType.getDeclaredConstructor(CONST_NODE_CONSTRUCTOR);
            node = (BaseData) constr.newInstance();
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("constructor for class not exists", ex);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalArgumentException("cant instantiate object of class", ex);
        }

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
    public Class<?> getNodeTypeFromText(final String strFullSrc, final String srcName) {
        // Node-Klasse festlegen
        Class<?> classType = BaseNode.class;
        Map<String, Class<?>> hshCurNodeTypeIdentifier = this.hshNodeTypeIdentifier;
        String nodeTypeIdentifier = 
                getNodeTypeIdentifierFromText(hshCurNodeTypeIdentifier, srcName);
        if (nodeTypeIdentifier != null && hshCurNodeTypeIdentifier.get(nodeTypeIdentifier) != null) {
            classType = hshCurNodeTypeIdentifier.get(nodeTypeIdentifier);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("set Nodetyp:" + classType + " for Name:" + srcName);
        }

        return classType;
    }

    protected String getNodeTypeIdentifierFromText(
            final Map<String, Class<?>>hshCurNodeTypeIdentifier, final String srcName) {
        // TODO - change implementation: extract first word an check if key exists in hash
        for (Iterator<String> iter = hshCurNodeTypeIdentifier.keySet().iterator();
                iter.hasNext();) {
            // die Identifier durchlaufen und den ersten Treffer benutzen
            String typeIdentifier = iter.next();
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
    public void setMetaDataService(final MetaDataService metaDataService) {
        BaseNode.setMetaDataService(metaDataService);
    }

    @Override
    public MetaDataService getMetaDataService() {
        return BaseNode.getConfiguredMetaDataService();
    }
}
