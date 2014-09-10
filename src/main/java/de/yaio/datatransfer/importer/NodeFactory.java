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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomainservice.MetaDataService;
import de.yaio.datatransfer.importer.parser.Parser;

/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *    interface with factory-functions for creation of Nodes
 * 
 * @package de.yaio.datatransfer.importer
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface NodeFactory {

    //////////////
    // Config-Funktionen
    //////////////
    
    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     ueber diese Funktionen werden die Schlagworte und die dann zu 
     *     über NodeFactory.getNodeTypeFromText zu instantiierenden Node-Klassen 
     *     bekannt gemacht<br>
     *     muss ueberladen und mit den Configuratoren gefuellt werden, die dann
     *     NodeFactory.addNodeTypeIdentifier aufrufen
     * <h4>FeatureKeywords:</h4>
     *     Config
     */
    public void initNodeTypeIdentifier();

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     ueber diese Funktionen werden die Schlagworte und die dann zu 
     *     über NodeFactory.getNodeTypeFromText zu instantiierenden Node-Klassen 
     *     bekannt gemacht
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param stateMap - Liste der Schlagworte (Typ, Status usw.)
     * @param classType - Klasse (Node) die bei Auffinden des Schlagwortes instanziiert wird
     */
    public void addNodeTypeIdentifier(Map<String, Object> stateMap,
            Class<?> classType);

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     liefert die Map der bekannten NodeTypeIdentifier zurueck
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Map - Map der NodeTypeIdentifier 
     *         (Schlagwort = zu instanziierende Node-Klasse)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return - Map der konfigurierten NodeTypeIdentifier
     */
    public Map<String, Class<?>> getHshNodeTypeIdentifier();

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     ueber diese Funktionen werden die Parser für das spätere Extrahieren
     *     der NodeDaten aus dem Namen (NodeFactory.parseNodeDataDomains) 
     *     bekannt gemacht<br>
     *     muss ueberladen und mit den Configuratoren gefuellt werden,
     *     die NodeFactory.addDataDomainParser aufrufen
     * <h4>FeatureKeywords:</h4>
     *     Config
     */
    public void initDataDomainParser();

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     hängt den Parser für das spätere Extrahieren der NodeDaten aus dem 
     *     Namen (NodeFactory.parseNodeDataDomains) in die Parserliste
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param parser - Instanz des Parsers
     */
    public void addDataDomainParser(Parser parser);

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     liefert die Liste der bekannten Parser zurueck
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Collection - Liste der Parser
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return - Liste der konfigurierten parser
     */
    public Collection<Parser> getDataDomainParser();

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     liefert die Map der bekannten Parser zurueck
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Map - Map der Parser 
     *         (Position = Parser-Instanz)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return - Map der konfigurierten Parser
     */
    public Set<Parser> getHshDataDomainParser();
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Service
     * <h4>FeatureDescription:</h4>
     *     sets the MetaDataService to use for generating NodeNumbers
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param metaDataService to use for generating NodeNumbers
     */
    public void setMetaDataService (MetaDataService metaDataService);

    /**
     * <h4>FeatureDomain:</h4>
     *     Service
     * <h4>FeatureDescription:</h4>
     *     returns the MetaDataService to use for generating NodeNumbers
     *   <ul>
     *     <li>returnValue MetaDataService - current MetaDataService
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @return MetaDataService to use for generating NodeNumbers
     */
    public MetaDataService getMetaDataService ();
    

    ////////////////
    // Parser-Funktionen
    ////////////////
    
    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     fuehrt alle ueber NodeFactory#initDataDomainParser() konfigurierten 
     *     Parser aus und extrahiert mit diesen die Daten aus dem NodeNamen und 
     *     belegt die Felder
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue int - Anzahl der gefundenen Fragmente
     *     <li>updates memberVariable node.name = Parser etrahiert die Feldinhalte 
     *         aus dem Namen und löscht die Infos dort
     *     <li>updates memberVariable node.XX = Parser etrahiert die Feldinhalte 
     *         aus dem Namen und belegt die Felder
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Parser
     * @param node - zu parsenden Node
     * @param options - Parser-Optionen
     * @return - Anzahl der gefundenen Fragmente
     * @throws Exception - parser/format-Exceptions possible
     */
    public int parseNodeDataDomains(DataDomain node, ImportOptions options) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     fuehrt den Parser aus, der die Daten aus dem NodeNamen extrahiert 
     *     und die Felder belegt
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue int - Anzahl der gefundenen Fragmente
     *     <li>updates memberVariable node.name = Parser etrahiert die Feldinhalte 
     *         aus dem Namen und löscht die Infos dort
     *     <li>updates memberVariable node.XX = Parser etrahiert die Feldinhalte 
     *         aus dem Namen und belegt die Felder
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Parser
     * @param node - zu parsenden Node
     * @param parser - Instanz des auszufuehrenden Parsers
     * @param options - Parser-Optionen
     * @return - Anzahl der gefundenen Fragmente
     * @throws Exception - parser/format-Exceptions possible
     */
    public int parseNodeDataDomain(DataDomain node, Parser parser, ImportOptions options) throws Exception;

    ////////////////
    // Generierungs-Funktionen
    ////////////////
    
    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     erzeugt anhand von classType eine neue Node und initialisiet deren 
     *     Basisdaten<br>
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Node - erzeugt Node
     *     <li>updates memberVariable node.xx = fuehrt NodeFactrory.parseNodeDataDomains 
     *         zur Initialisierung der Node aus
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Parser
     * @param classType - Klasse von Node abgeleitet
     * @param id - id der Node
     * @param strFullSrc - Src der Node
     * @param srcName - Name der Node (wird geparst)
     * @param curParentNode - ParentNode
     * @return - erzeugte Node-Instanz
     * @throws Exception - parser/format-Exceptions possible
     */
    public DataDomain createNodeObjFromText(Class<?> classType, int id,
            String strFullSrc, String srcName, DataDomain curParentNode)
                    throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     parst anhand der in NodeFactory.initNodeTypeIdentifier konfigurierten +
     *     NodeTypeIdetifiern den passenden Klassennamen der Node die dann über
     *     NodeFactory.createNodeObjFromText erzeugt werdne kann
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Class - Klasse die anhand des Namens zugeordnet wurde (anhand der Daten aus NodeFactory.initNodeTypeIdentifier)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Parser
     * @param strFullSrc - FullSrc der Node
     * @param srcName - NodeName
     * @return - Klasse die anhand des Namens zugeordnet wurde (anhand der Daten aus NodeFactory.initNodeTypeIdentifier)
     * @throws Exception - parser/format-Exceptions possible
     */
    public Class<?> getNodeTypeFromText(String strFullSrc, String srcName)
            throws Exception;
}