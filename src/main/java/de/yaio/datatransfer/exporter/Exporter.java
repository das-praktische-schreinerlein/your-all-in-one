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
package de.yaio.datatransfer.exporter;

import java.util.Map;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.exporter.formatter.Formatter;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 * <h4>FeatureDescription:</h4>
 *     interface with service-functions for export of Nodes
 * 
 * @package de.yaio.datatransfer.exporter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface Exporter {

    //////////////
    // service-functions for configuration
    //////////////

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     initialize all formatters for formatting DataDomains with 
     *     (Exporter.formatNodeDataDomains)<br>
     *     must be override and call Exporter.addDataDomainFormatter for every formatter 
     * <h4>FeatureKeywords:</h4>
     *     Config
     */
    void initDataDomainFormatter();

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     add formatter for formatting DataDomains with (Exporter.formatNodeDataDomains) 
     *     to the Exporter-Config
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param formatter - instance of the formatter
     */
    void addDataDomainFormatter(Formatter formatter);

    ////////////////
    // service-functions for node-output
    ////////////////
    
    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     runs all with Exporter#initDataDomainFormatter() configured formatter 
     *     for the DataDomains of node<br>
     *     appends the output to the StringBuffer nodeOutput
     * <h4>FeatureConditions:</h4>
     *     formatter must be configured for the DataDomain of the node (Formatter.getTargetClass)<br>
     *     oOptions must be set for the formatter 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates nodeOutput - appends output of the formatter 
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param node - node to be formatted
     * @param nodeOutput -StringBuffer to append output of the formatter
     * @param options - options for output (formatter) formatter
     * @throws Exception - parser/format-Exceptions possible
     */
    void formatNodeDataDomains(DataDomain node, StringBuffer nodeOutput, 
            OutputOptions options) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     runs the formatter for the DataDomain (node)<br>
     *     appends the output to the StringBuffer nodeOutput
     * <h4>FeatureConditions:</h4>
     *     formatter must be configured for the DataDomain of the node (Formatter.getTargetClass)<br>
     *     oOptions must be set for the formatter 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates nodeOutput - appends output of the formatter 
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param node - node to be formatted
     * @param formatter - formatter to be run
     * @param nodeOutput -StringBuffer to append output of the formatter
     * @param options - options for output (formatter) formatter
     * @throws Exception - parser/format-Exceptions possible
     */
    void formatNodeDataDomain(DataDomain node, Formatter formatter, 
            StringBuffer nodeOutput, OutputOptions options) throws Exception;


    ////////////////
    // Generierungs-Funktionen
    ////////////////
    
    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     prepare node 4 export
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param node - node to prepare
     * @param oOptions - options for output (formatter)
     * @throws Exception - parser/format/io-Exceptions possible
     */
    void prepareNodeForExport(DataDomain node, OutputOptions oOptions)
            throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     formats recursively masterNode and all childnodes (runs formatter)
     * <h4>FeatureConditions:</h4>
     *     oOptions must be set for the formatter 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue StringBuffer - formatted output of the Node-hierarchy
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param masterNode - node for output recursively
     * @param oOptions - options for output (formatter)
     * @return - formatted output of node-hierarchy and DataDomains
     * @throws Exception - parser/format/io-Exceptions possible
     */
    String getMasterNodeResult(DataDomain masterNode, OutputOptions oOptions)
            throws Exception;

    
    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     filters nodes only with type/state in OutputOptions.getStrReadIfStatusInListOnly
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Node - MasterNode with filtered childnodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Praesentation Filter
     * @param masterNode - Masternode with childnodes to filter
     * @param oOptions - options with filter
     * @return - new MasterNode with filtered childnodes
     * @throws Exception - parser/format-Exceptions possible
     */
    DataDomain filterNodes(DataDomain masterNode, OutputOptions oOptions)
            throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     filters nodes only with type/state in mpStates
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Node - MasterNode with filtered childnodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Praesentation Filter
     * @param node - Masternode with childnodes to filter
     * @param oOptions - options with filter
     * @param mpStates - types/status to filter (TypIdentifier)
     * @return - new MasterNode with filtered childnodes
     * @throws Exception - parser/format-Exceptions possible
     */
    DataDomain filterNodeByState(DataDomain node, OutputOptions oOptions,
            Map<String, Object> mpStates) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     check if it the node passes the filter in oOptions
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue boolean - true/false = /matches/didnt match the filter
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Praesentation Filter
     * @param node - node to check if it passes the filter
     * @param oOptions - options with filter
     * @return - true/false = /matches/didnt match the filter
     * @throws Exception - parser/format-Exceptions possible
     */
    boolean isNodeMatchingFilter(DataDomain node, OutputOptions oOptions)
                    throws Exception;

}