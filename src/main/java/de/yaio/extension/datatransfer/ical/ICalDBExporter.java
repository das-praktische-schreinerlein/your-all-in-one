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
package de.yaio.extension.datatransfer.ical;

import java.util.List;

import org.apache.commons.collections4.MapUtils;
import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.dbservice.BaseNodeDBService;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.EventNode;
import de.yaio.core.node.InfoNode;
import de.yaio.core.node.TaskNode;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.datatransfer.exporter.formatter.FormatterImpl;
import de.yaio.utils.Calculator;

/** 
 * export of Nodes as ICal from database
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.ical
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ICalDBExporter extends ICalExporter {

    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(ICalDBExporter.class);

    /** 
     * service functions to export nodes as ICal
     */
    public ICalDBExporter() {
        super();
    }

    @Override
    public String getMasterNodeResult(final DataDomain masterNode, 
            final OutputOptions oOptions) throws Exception {
        // Parameter pruefen
        if (masterNode == null) {
            throw new IllegalArgumentException("Masternode must not be null: '" + masterNode + "'");
        }

        // create iCal
        String icalRes = getCalHeader(masterNode, oOptions);
        icalRes += this.getNodeResult(masterNode, "", oOptions).toString();
        icalRes += getCalFooter(masterNode, oOptions);
        
        // Hack wegen UFT8-Sonderzeichen
        // escape non latin
        StringBuilder sb = FormatterImpl.escapeNonLatin(icalRes, new StringBuilder());
        icalRes = sb.toString();
        icalRes = icalRes.replaceAll("\n", "\r\n");
        
        return icalRes;
    }
    
    /** 
     * formats recursively node in ICal-format
     * @param parentNode             node for output recursively
     * @param poOptions              options for output (formatter)
     * @return                       formatted output of node-hierarchy and DataDomains
     * @throws Exception             parser/format-Exceptions possible
     */
    public String genICalForNode(final BaseNode parentNode, 
        final OutputOptions poOptions) throws Exception {
        OutputOptions oOptions = new OutputOptionsImpl(poOptions);
        String res = "";

        // max. Ebene pruefen
        if (parentNode.getEbene() > oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + parentNode.getWorkingId() 
                    + " ignore:" + parentNode.getEbene() + ">" + oOptions.getMaxEbene());
            }
            return res;
        }

        // Anfang
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("curnode:" + parentNode.getWorkingId() + " start processing");
        }
        
        // search for Nodes
        if (MapUtils.isEmpty(oOptions.getMapClassFilter())) {
            // if not set: set default-filters
            oOptions.setStrClassFilter("EventNode,TaskNode");
        }
        BaseNodeDBService dbService = parentNode.getBaseNodeDBService();
        List<BaseNode> matchingNodes = dbService.findExtendedSearchBaseNodeEntries(null, oOptions, null, 0, 99999);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + parentNode.getWorkingId() 
                + " iterate matching nodes for search:" + matchingNodes.size());
        }
        
        // iterate nodes found
        String parentSysUID = parentNode.getSysUID();
        for (BaseNode curNode : matchingNodes) {
            // check for parent
            if (!curNode.getSysUID().equals(parentSysUID) 
                && !curNode.getBaseNodeService().hasParent(curNode,  parentSysUID)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("curnode:" + curNode.getNameForLogger() 
                        + " ignore: no child of  " + parentSysUID);
                }
                continue;
            }
            
            // check for TaskNode 
            if (!TaskNode.class.isInstance(curNode)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("curNode:" + curNode.getNameForLogger() 
                        + " ignore: not TaskNode but " + curNode.getClass());
                }
                continue;
            }

            // skip if no wf-state
            TaskNode curTaskNode = (TaskNode) curNode;
            String curNodeStatus = curTaskNode.getState();
            if (!curTaskNode.isWFStatus(curNodeStatus)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("curNode:" + curNode.getNameForLogger() 
                        + " ignore: no WF-Status but " + curNodeStatus);
                }
                continue;
            }
            
            // skip if no aufwand and children with workflow
            if (curTaskNode.getStatWorkflowCount() > 1 
                && !Calculator.isAufwand(curTaskNode.getPlanAufwand())
                && !Calculator.isAufwand(curTaskNode.getIstAufwand())
                && !Calculator.isStand(curTaskNode.getIstStand())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("curNode:" + curNode.getNameForLogger() 
                        + " ignore: hasWorkflowChildren and no own aufwand/stand");
                }
                continue;
            }
            
            // generate nodeData
            if (InfoNode.class.isInstance(curNode)) {
                // SKIP
            } else if (EventNode.class.isInstance(curNode)) {
                res += this.genICalForEventNode((EventNode) curNode, oOptions);
            } else if (TaskNode.class.isInstance(curNode)) {
                res += this.genICalForTaskNode((TaskNode) curNode, oOptions);
            }
            
        }
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + parentNode.getWorkingId() + " return datalength:" + res.length());
        }

        return res;
    }
    
    @Override
    public boolean hasOwnNodeReader() {
        return true;
    }
}
