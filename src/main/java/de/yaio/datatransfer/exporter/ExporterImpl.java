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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.BaseWorkflowData;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.formatter.BaseDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.DescDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.DocLayoutDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.Formatter;
import de.yaio.datatransfer.exporter.formatter.IstChildrenSumDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.IstDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.MetaDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.PlanChildrenSumDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.PlanDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.ResLocDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.SymLinkDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.SysDataFormatterImpl;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 * <h4>FeatureDescription:</h4>
 *     service-functions for export of Nodes
 * 
 * @package de.yaio.datatransfer.exporter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ExporterImpl implements Exporter {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(ExporterImpl.class);

    public TreeSet<Formatter> hshDataDomainFormatter = new TreeSet<Formatter>();
    public Map<String, Formatter> hshDataDomainFormatterByClassName = 
            new HashMap<String, Formatter>();

    public ExporterImpl() {
        // NodeTypes konfigurieren
        this.initDataDomainFormatter();
    }

    //////////////
    // service-functions for configuration
    //////////////
    @Override
    public void initDataDomainFormatter() {
        BaseDataFormatterImpl.configureDataDomainFormatter(this);
        SymLinkDataFormatterImpl.configureDataDomainFormatter(this);
        ResLocDataFormatterImpl.configureDataDomainFormatter(this);
        DocLayoutDataFormatterImpl.configureDataDomainFormatter(this);
        IstDataFormatterImpl.configureDataDomainFormatter(this);
        PlanDataFormatterImpl.configureDataDomainFormatter(this);
        IstChildrenSumDataFormatterImpl.configureDataDomainFormatter(this);
        PlanChildrenSumDataFormatterImpl.configureDataDomainFormatter(this);
        MetaDataFormatterImpl.configureDataDomainFormatter(this);
        SysDataFormatterImpl.configureDataDomainFormatter(this);
        DescDataFormatterImpl.configureDataDomainFormatter(this);
    }

    @Override
    public void addDataDomainFormatter(Formatter formatter) {
        if (formatter.getTargetOrder() < 0 ) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: Targetorder < 0 TargetOrder:" 
                        + formatter.getTargetOrder()
                        + " Formatter:" + formatter.getClass().getName());
                return;
            }
        }
        this.hshDataDomainFormatter.add(formatter);
        this.hshDataDomainFormatterByClassName.put(
                formatter.getClass().getName(), formatter);
    }

    public Formatter getDataDomainFormatterByClassName(String className) {
        return this.hshDataDomainFormatterByClassName.get(className);
    }
    
    //////////////
    // service-functions for formatter-output
    //////////////
    @Override
    public void formatNodeDataDomains(DataDomain node, 
            StringBuffer nodeOutput, OutputOptions options)  throws Exception {
        for (Formatter formatter : this.hshDataDomainFormatter) {
            this.formatNodeDataDomain(node, formatter, nodeOutput, options);
        }
    }

    @Override
    public void formatNodeDataDomain(DataDomain node, Formatter formatter, 
            StringBuffer nodeOutput, OutputOptions options) throws Exception {
        // nur formatieren, wenn zustaendig
        if (formatter.getTargetClass().isInstance(node)) {
            formatter.format(node, nodeOutput, options);
        } else {
            LOGGER.debug("formatNodeDataDomain SKIP: Node " + 
                    node.getClass().getName() + " is not of type + " 
                    + formatter.getTargetClass().getName() 
                    + " Node=" + node.getNameForLogger());
        }
    }

    ////////////////
    // service-functions for node-output
    ////////////////
    @Override
    public String getMasterNodeResult(DataDomain masterNode,
            OutputOptions oOptions) throws Exception {
        // Parameter pruefen
        if (masterNode == null) {
            throw new IllegalArgumentException("Masternode must not be null: '" + masterNode + "'");
        }

        return this.getNodeResult(masterNode, "", oOptions).toString();
    }

    @Override
    public void prepareNodeForExport(DataDomain node,
            OutputOptions oOptions) throws Exception {
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     formats recursively node and all childnodes (runs formatter)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue StringBuffer - formatted output of node-hierarchy and DataDomains
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param node - node for output recursively
     * @param praefix - string to use as prefix in front of nodeoutput
     * @param oOptions - options for output (formatter)
     * @return - formatted output of node-hierarchy and DataDomains
     * @throws Exception
     */
    public StringBuffer getNodeResult(DataDomain node, String praefix,
            OutputOptions oOptions) throws Exception {
        // Parameter pruefen
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null: '" + node + "'");
        }
        
        // prepare for Export
        this.prepareNodeForExport(node, oOptions);

        StringBuffer res = new StringBuffer();
        formatNodeDataDomains(node, res, oOptions);
        res.append("\n");
        for (BaseNode childNode : node.getChildNodes()) {
            res.append(getNodeResult(childNode, praefix, oOptions));
        }

        return res;
    }


    @Override
    public DataDomain filterNodes(DataDomain masterNode,
            OutputOptions oOptions) throws Exception {

        // Filter konfigurieren
        Map<String, Object> mpStates = null;
        if (oOptions.getStrReadIfStatusInListOnly() != null) {
            mpStates = new HashMap<String, Object>();
            String [] arrStatusFilter =
                    oOptions.getStrReadIfStatusInListOnly().split(",");
            for (int zaehler = 0; zaehler < arrStatusFilter.length; zaehler++) {
                mpStates.put(arrStatusFilter[zaehler], arrStatusFilter[zaehler]);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("NodeFilter-Status enablend: "
                            + arrStatusFilter[zaehler]);
                }
            }
        }
        // falls Filter belegt: filtern
        if (mpStates != null && mpStates.size() > 0) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("NodeFilter-Status start: " + masterNode.getNameForLogger());
            }
            masterNode = this.filterNodeByState(masterNode, oOptions, mpStates);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("NodeFilter-Status end: " + masterNode.getNameForLogger());
            }
        }

        return masterNode;
    }

    @Override
    public DataDomain filterNodeByState(DataDomain node,
            OutputOptions oOptions, Map<String, Object> mpStates) throws Exception {

        // wenn kein Workflow-Element loeschen
        if (! BaseWorkflowData.class.isInstance(node)) {
            return null;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("NodeFilter-Status start: "
                    + " for " + node.getNameForLogger());
        }

        // SubNodes filtern und testen
        Map<String, DataDomain> masterChilds = node.getChildNodesByNameMap();
        List<String> lstDelChilds = new ArrayList<String>();
        for (String nodeName : masterChilds.keySet()) {

            // SubNode einlesen
            DataDomain subNode = masterChilds.get(nodeName);

            // SubNode testen
            subNode = this.filterNodeByState(subNode, oOptions, mpStates);

            // falls nicht gefunden: SubNode loeschen
            if (subNode == null) {
                lstDelChilds.add(nodeName);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("NodeFilter-Status checkChild delete: " 
                            + nodeName + " for " + node.getNameForLogger());
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("NodeFilter-Status checkChild OK: " 
                            + nodeName + " for " + node.getNameForLogger());
                }
            }
        }
        for (String nodeName : lstDelChilds) {
            masterChilds.remove(nodeName);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("NodeFilter-Status removeChild: "
                        + nodeName + " for " + node.getNameForLogger());
            }
        }

        // pruefen
        boolean flgHasChilds = false;
        if (masterChilds.size() > 0) {
            // Anzahl pruefen
            flgHasChilds = true;
        } else if (masterChilds.size() != node.getChildNodesByNameMap().size()) {
            // Konsistenz-Check
            throw new IllegalStateException(
                    "Count of Childnodes differ for Node:" + node.getNameForLogger());
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("NodeFilter-Status hasChilds: " + flgHasChilds
                    + " for " + node.getNameForLogger());
        }

        // Daten neu berechnen
// TODO check it       node.recalcData(NodeService.CONST_RECURSE_DIRECTION_ONLYME);
        
        // Wenn Status nicht im Filter und keine Kindselemente vorhanden
        String state = ((BaseWorkflowData)node).getState();
        if (   (mpStates.get(state) == null)
                && ! flgHasChilds) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("NodeFilter-Status delete: no Children and state="
                        + state + " not in list for " + node.getNameForLogger());
            }
            return null;
        }
        
        // Ich bleibe: Juhu
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("NodeFilter-Status OK: has Children=" + flgHasChilds 
                    + " or state=" + state + " in list for " + node.getNameForLogger());
        }
        return node;
    }

}
