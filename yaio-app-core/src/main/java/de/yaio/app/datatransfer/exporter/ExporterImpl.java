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
package de.yaio.app.datatransfer.exporter;

import de.yaio.app.core.datadomain.BaseWorkflowData;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.exporter.formatter.*;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.exporter.formatter.Formatter;
import org.apache.commons.collections4.MapUtils;
import org.apache.log4j.Logger;

import java.util.*;

/** 
 * service-functions for export of Nodes
 * 
 * @FeatureDomain                DatenExport
 * @package                      de.yaio.datatransfer.exporter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ExporterImpl implements Exporter {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(ExporterImpl.class);

    protected Set<de.yaio.app.datatransfer.exporter.formatter.Formatter> hshDataDomainFormatter = new TreeSet<de.yaio.app.datatransfer.exporter.formatter.Formatter>();
    protected Map<String, de.yaio.app.datatransfer.exporter.formatter.Formatter> hshDataDomainFormatterByClassName =
            new HashMap<String, de.yaio.app.datatransfer.exporter.formatter.Formatter>();

    /** 
     * create Exporter to export nodes
     */
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
        PlanCalcDataFormatterImpl.configureDataDomainFormatter(this);
        PlanChildrenSumDataFormatterImpl.configureDataDomainFormatter(this);
        MetaDataFormatterImpl.configureDataDomainFormatter(this);
        SysDataFormatterImpl.configureDataDomainFormatter(this);
        DescDataFormatterImpl.configureDataDomainFormatter(this);
    }

    @Override
    public void addDataDomainFormatter(final de.yaio.app.datatransfer.exporter.formatter.Formatter formatter) {
        if (formatter.getTargetOrder() < 0) {
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

    protected Formatter getDataDomainFormatterByClassName(final String className) {
        return this.hshDataDomainFormatterByClassName.get(className);
    }

    @Override
    public void setTimeZone(final TimeZone timeZone) {
        for (Formatter formatter : this.hshDataDomainFormatter) {
            formatter.setTimeZone(timeZone);
        }
    }
    
    //////////////
    // service-functions for formatter-output
    //////////////
    @Override
    public void formatNodeDataDomains(final DataDomain node,
            final StringBuffer nodeOutput, final OutputOptions options) {
        for (de.yaio.app.datatransfer.exporter.formatter.Formatter formatter : this.hshDataDomainFormatter) {
            this.formatNodeDataDomain(node, formatter, nodeOutput, options);
        }
    }

    @Override
    public void formatNodeDataDomain(final DataDomain node, final de.yaio.app.datatransfer.exporter.formatter.Formatter formatter,
            final StringBuffer nodeOutput, final OutputOptions options) {
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
    public String getMasterNodeResult(final DataDomain masterNode,
            final OutputOptions oOptions) throws ConverterException {
        // Parameter pruefen
        if (masterNode == null) {
            throw new IllegalArgumentException("Masternode must not be null: '" + masterNode + "'");
        }

        return this.getNodeResult(masterNode, "", oOptions).toString();
    }

    @Override
    public void prepareNodeForExport(final DataDomain node,
            final OutputOptions oOptions) {
    }

    /** 
     * formats recursively node and all childnodes (runs formatter)
     * @param node                   node for output recursively
     * @param praefix                string to use as prefix in front of nodeoutput
     * @param oOptions               options for output (formatter)
     * @return                       formatted output of node-hierarchy and DataDomains
     * @throws ConverterException    parser/format/io-Exceptions possible
     */
    public StringBuffer getNodeResult(final DataDomain node, final String praefix,
            final OutputOptions oOptions) throws ConverterException {
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
    public DataDomain filterNodes(final DataDomain pMasterNode,
            final OutputOptions oOptions) {
        DataDomain masterNode = pMasterNode;

        // Filter konfigurieren
        Map<String, Object> mpStates = null;
        if (oOptions.getStrReadIfStatusInListOnly() != null 
            && oOptions.getStrReadIfStatusInListOnly().length() > 0) {
            mpStates = new HashMap<String, Object>();
            String[] arrStatusFilter =
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
    public DataDomain filterNodeByState(final DataDomain node,
            final OutputOptions oOptions, final Map<String, Object> mpStates) {

        // wenn kein Workflow-Element loeschen
        if (!BaseWorkflowData.class.isInstance(node)) {
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
// TODO check it       node.recalcData(NodeService.RecalcRecurseDirection.ONLYME);
        
        // Wenn Status nicht im Filter und keine Kindselemente vorhanden
        String state = ((BaseWorkflowData) node).getState();
        if ((mpStates.get(state) == null)
                && !flgHasChilds) {
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

    @Override
    public boolean isNodeMatchingFilter(final DataDomain node,
            final OutputOptions oOptions) {
        // check config
        Map<String, String> mpStates = oOptions.getMapStateFilter();
        Map<String, String> mpClasses = oOptions.getMapClassFilter();
        Map<String, String> mpTypes = oOptions.getMapTypeFilter();
        if (MapUtils.isEmpty(mpStates)
            && MapUtils.isEmpty(mpClasses) 
            && MapUtils.isEmpty(mpTypes)) {
            // no filter return true
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("no NodeFilter defined oOptions=" + oOptions 
                                + " for " + node.getNameForLogger());
            }
            return true;
        }

        // set flag to failed
        boolean flgMatchesState = false;
        boolean flgMatchesType = false;
        boolean flgMatchesClass = false;

        // if statefilter set: do it
        if (MapUtils.isNotEmpty(mpStates)) {
            // check if workflow node
            if (!BaseWorkflowData.class.isInstance(node)) {
                // no workflow -> not state
                flgMatchesState = false;
            } else {
                // check state
                String state = ((BaseWorkflowData) node).getState();
                if (mpStates.get(state) == null) {
                    flgMatchesState = false;
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("NodeFilter-Status FAILED: state="
                                + state + " not in list for " + node.getNameForLogger());
                    }
                } else {
                    // i did it
                    flgMatchesState = true;
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("NodeFilter-Status OK state=" + state 
                                        + " for " + node.getNameForLogger());
                    }
                }
            }
        } else {
            // if no filter set: true
            flgMatchesState = true;
        }

        // if classfilter set: do it
        if (MapUtils.isNotEmpty(mpClasses)) {
            // check class
            String className = ((BaseNode) node).getClassName();
            if (mpClasses.get(className) == null) {
                flgMatchesClass = false;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("NodeFilter-Class FAILED: class="
                                    + className + " not in list for " + node.getNameForLogger());
                }
            } else {
                // i did it
                flgMatchesClass = true;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("NodeFilter-Class OK class=" + className 
                                    + " for " + node.getNameForLogger());
                }
            }
        } else {
            // if no filter set: true
            flgMatchesClass = true;
        }

        // if classfilter set: do it
        if (MapUtils.isNotEmpty(mpTypes)) {
            // check class
            String type = ((BaseNode) node).getType();
            if (mpTypes.get(type) == null) {
                flgMatchesType = false;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("NodeFilter-Type FAILED: type="
                                    + type + " not in list for " + node.getNameForLogger());
                }
            } else {
                // i did it
                flgMatchesType = true;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("NodeFilter-Type OK type=" + type 
                                    + " for " + node.getNameForLogger());
                }
            }
        } else {
            // if no filter set: true
            flgMatchesType = true;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("NodeFilter Result=" + (flgMatchesClass && flgMatchesState && flgMatchesType)  
                            + " for " + node.getNameForLogger());
        }

        return flgMatchesClass && flgMatchesState && flgMatchesType;
    }

    @Override
    public boolean hasOwnNodeReader() {
        return false;
    }
}
