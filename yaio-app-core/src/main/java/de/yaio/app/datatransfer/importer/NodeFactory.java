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

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.datadomainservice.MetaDataService;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.importer.parser.Parser;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/** 
 *    interface with factory-functions for creation of Nodes
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface NodeFactory {

    //////////////
    // Config-Funktionen
    //////////////
    
    /** 
     * ueber diese Funktionen werden die Schlagworte und die dann zu 
     * über NodeFactory.getNodeTypeFromText zu instantiierenden Node-Klassen 
     * bekannt gemacht<br>
     * muss ueberladen und mit den Configuratoren gefuellt werden, die dann
     * NodeFactory.addNodeTypeIdentifier aufrufen
     */
    void initNodeTypeIdentifier();

    /** 
     * ueber diese Funktionen werden die Schlagworte und die dann zu 
     * über NodeFactory.getNodeTypeFromText zu instantiierenden Node-Klassen 
     * bekannt gemacht
     * @param constMapNodetypeIdentifier Liste der Schlagworte (Typ, Status usw.)
     * @param classType              Klasse (Node) die bei Auffinden des Schlagwortes instanziiert wird
     */
    void addNodeTypeIdentifier(Map<String, String> constMapNodetypeIdentifier,
            Class<?> classType);

    /** 
     * liefert die Map der bekannten NodeTypeIdentifier zurueck
     * @return                       Map der konfigurierten NodeTypeIdentifier
     */
    Map<String, Class<?>> getHshNodeTypeIdentifier();

    /** 
     * ueber diese Funktionen werden die Parser für das spätere Extrahieren
     * der NodeDaten aus dem Namen (NodeFactory.parseNodeDataDomains) 
     * bekannt gemacht<br>
     * muss ueberladen und mit den Configuratoren gefuellt werden,
     * die NodeFactory.addDataDomainParser aufrufen
     */
    void initDataDomainParser();

    /** 
     * hängt den Parser für das spätere Extrahieren der NodeDaten aus dem 
     * Namen (NodeFactory.parseNodeDataDomains) in die Parserliste
     * @param parser                 Instanz des Parsers
     */
    void addDataDomainParser(Parser parser);

    /** 
     * liefert die Liste der bekannten Parser zurueck
     * @return                       Liste der konfigurierten parser
     */
    Collection<Parser> getDataDomainParser();

    /** 
     * liefert die Map der bekannten Parser zurueck
     * @return                       Map der konfigurierten Parser
     */
    Set<Parser> getHshDataDomainParser();
    
    /** 
     * sets the MetaDataService to use for generating NodeNumbers
     * @param metaDataService        to use for generating NodeNumbers
     */
    void setMetaDataService(MetaDataService metaDataService);

    /** 
     * returns the MetaDataService to use for generating NodeNumbers
     *   <ul>
     * <li>returnValue MetaDataService - current MetaDataService
     *   </ul> 
     * @return                       MetaDataService to use for generating NodeNumbers
     */
    MetaDataService getMetaDataService();
    

    ////////////////
    // Parser-Funktionen
    ////////////////
    
    /** 
     * fuehrt alle ueber NodeFactory#initDataDomainParser() konfigurierten 
     * Parser aus und extrahiert mit diesen die Daten aus dem NodeNamen und 
     * belegt die Felder
     * @param node                   zu parsenden Node
     * @param options                Parser-Optionen
     * @return                       Anzahl der gefundenen Fragmente
     * @throws ParserException        parser/format-Exceptions possible
     */
    int parseNodeDataDomains(DataDomain node, ImportOptions options) throws ParserException;

    /** 
     * fuehrt den Parser aus, der die Daten aus dem NodeNamen extrahiert 
     * und die Felder belegt
     * @param node                   zu parsenden Node
     * @param parser                 Instanz des auszufuehrenden Parsers
     * @param options                Parser-Optionen
     * @return                       Anzahl der gefundenen Fragmente
     * @throws ParserException        parser/format-Exceptions possible
     */
    int parseNodeDataDomain(DataDomain node, Parser parser, ImportOptions options) throws ParserException;

    /**
     * sets timezone to all Formatters
     * @param timeZone     new timeZone (like "Europe/Berlin")
     */
    void setTimeZone(final TimeZone timeZone);

    ////////////////
    // Generierungs-Funktionen
    ////////////////
    
    /** 
     * erzeugt anhand von classType eine neue Node und initialisiet deren 
     * Basisdaten<br>
     * @param classType              Klasse von Node abgeleitet
     * @param id                     id der Node
     * @param strFullSrc             Src der Node
     * @param srcName                Name der Node (wird geparst)
     * @param curParentNode          ParentNode
     * @return                       erzeugte Node-Instanz
     * @throws ParserException        parser/format-Exceptions possible
     */
    DataDomain createNodeObjFromText(Class<?> classType, int id,
            String strFullSrc, String srcName, DataDomain curParentNode) throws ParserException;

    /** 
     * parst anhand der in NodeFactory.initNodeTypeIdentifier konfigurierten +
     * NodeTypeIdetifiern den passenden Klassennamen der Node die dann über
     * NodeFactory.createNodeObjFromText erzeugt werdne kann
     * @param strFullSrc             FullSrc der Node
     * @param srcName                NodeName
     * @return                       Klasse die anhand des Namens zugeordnet wurde (anhand der Daten aus NodeFactory.initNodeTypeIdentifier)
     * @throws ParserException        parser/format-Exceptions possible
     */
    Class<?> getNodeTypeFromText(String strFullSrc, String srcName) throws ParserException;
}
