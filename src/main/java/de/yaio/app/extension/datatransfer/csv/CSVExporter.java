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
package de.yaio.app.extension.datatransfer.csv;

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.datatransfer.exporter.formatter.BaseDataFormatterImpl;
import de.yaio.app.datatransfer.exporter.formatter.DescDataFormatterImpl;
import de.yaio.app.datatransfer.exporter.formatter.FormatterImpl;
import de.yaio.app.extension.datatransfer.wiki.WikiExporter;
import de.yaio.app.core.node.BaseNode;
import org.apache.log4j.Logger;

import java.util.Date;

/** 
 * export nodes as CSV
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.ical
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class CSVExporter extends WikiExporter {
    
    protected static FormatterImpl baseFormatter = new BaseDataFormatterImpl();
    
    private static final Logger LOGGER = Logger.getLogger(CSVExporter.class);

    /** 
     * export nodes as CSV
     */
    public CSVExporter() {
        super();
    }

    @Override
    public void initDataDomainFormatter() {
        DescDataFormatterImpl.configureDataDomainFormatter(this);
    }

    @Override
    public String getMasterNodeResult(final DataDomain masterNode, final OutputOptions oOptions)
            throws Exception {
        StringBuffer res = new StringBuffer();
        
        // show Header
        res.append(this.getLineStart()).append("UId")
            .append(this.getFieldDelimiter()).append("Id")
            .append(this.getFieldDelimiter()).append("Parents")
            .append(this.getFieldDelimiter()).append("Name")
            .append(this.getFieldDelimiter()).append("State")
            .append(this.getFieldDelimiter()).append("PlanStart")
            .append(this.getFieldDelimiter()).append("PlanEnde")
            .append(this.getFieldDelimiter()).append("PlanAufwand")
            .append(this.getFieldDelimiter()).append("IstStand")
            .append(this.getFieldDelimiter()).append("IstAufwand")
            .append(this.getFieldDelimiter()).append("IstStart")
            .append(this.getFieldDelimiter()).append("IstEnde")
            .append(this.getFieldDelimiter()).append("PlanStartCalcSum")
            .append(this.getFieldDelimiter()).append("PlanEndeCalcSum")
            .append(this.getFieldDelimiter()).append("PlanAufwandCalcSum")
            .append(this.getFieldDelimiter()).append("IstStandCalcSum")
            .append(this.getFieldDelimiter()).append("IstAufwandCalcSum")
            .append(this.getFieldDelimiter()).append("IstStartCalcSum")
            .append(this.getFieldDelimiter()).append("IstEndeCalcSum")
            .append(this.getFieldDelimiter()).append("Ebene")
            .append(this.getFieldDelimiter()).append("Desc")
            .append(this.getLineEnd());
        
        // show details
        res.append(super.getMasterNodeResult(masterNode, oOptions));
        
        return res.toString();
    }

    @Override
    public StringBuffer getNodeResult(final DataDomain node,  final String praefix,
            final OutputOptions oOptions) throws Exception {
        StringBuffer res = new StringBuffer();

        // Template-Nodes ignorieren
//        if (TemplateNode.class.isInstance(curNode)) {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("SKIP TemplateNode node:" + curNode.getNameForLogger());
//            }
//            return res;
//        }
        
        BaseNode curNode = (BaseNode) node;

        // Anfang
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node: start processing" + curNode.getNameForLogger());
        }

        // max. Ebene pruefen
        if (curNode.getEbene() > oOptions.getMaxEbene()) {
            return res;
        }

        // iterate all children
        StringBuffer childRes = new StringBuffer();
        boolean flgChildMatched = false;
        if (curNode.getEbene() < oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Do Childs: Ebene " + curNode.getEbene() 
                        + " >= MaxEbene " + oOptions.getMaxEbene() 
                        + " Count:" + curNode.getChildNodesByNameMap().size() 
                        + " for " + curNode.getNameForLogger());
            }
            for (String nodeName : curNode.getChildNodesByNameMap().keySet()) {
                DataDomain childNode = curNode.getChildNodesByNameMap().get(nodeName);
                childRes.append(this.getNodeResult(childNode, "", oOptions));
            }
        }
        // check if children matches (childRes filled)
        if (childRes.length() > 0) {
            flgChildMatched = true;
        }
        // check if I'am matching
        boolean flgMatchesFilter = this.isNodeMatchingFilter(curNode, oOptions);
        if (!(flgMatchesFilter || flgChildMatched)) {
            // sorry me and my children didnt match
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sorry me and my children didnt match"
                                + " - node:" + curNode.getWorkingId() 
                                + " flgMatchesFilter=" + flgMatchesFilter
                                + " flgChildMatched=" + flgChildMatched);
            }
            return res;
        }

        // ungültige Zeichen entfernen
        String name = curNode.getName();
        name = name.replaceAll("\"", "'");
        name = name.replaceAll("[&\\\"]", " ");
        name = name.replace("\\", " ");
        name = name.replaceAll("\n", "<br>");
        name = name.replaceAll("\t", " ");

        String parent = curNode.getParentNameHirarchry(" -> ", true);
        parent = parent.replaceAll("\"", "'");
        parent = parent.replaceAll("[&\\\"]", " ");
        parent = parent.replace("\\", " ");
        parent = parent.replaceAll("\n", "<br>");
        parent = parent.replaceAll("\t", " ");

        // Desc
        StringBuffer descFull = new StringBuffer();
        this.formatNodeDataDomains(curNode, descFull, genOutputOptionsForDescArea(oOptions));
        String desc = descFull.toString();
        if (desc == null) {
            desc = "";
        }
        desc = desc.replaceAll("\"", "'");
        desc = desc.replace("\\", "/");

        // aktuelle Node ausgeben
        res.append(this.getLineStart()).append(curNode.getSysUID())
            .append(this.getFieldDelimiter()).append(curNode.getWorkingId())
            .append(this.getFieldDelimiter()).append(parent)
            .append(this.getFieldDelimiter()).append(name)
            .append(this.getFieldDelimiter()).append(curNode.getState())
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeDate(curNode, curNode.getPlanStart()))
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeDate(curNode, curNode.getPlanEnde()))
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeNumber(curNode, curNode.getPlanAufwand(), 0, 2))
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeNumber(curNode, curNode.getIstStand(), 0, 2))
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeNumber(curNode, curNode.getIstAufwand(), 0, 2))
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeDate(curNode , curNode.getIstStart()))
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeDate(curNode , curNode.getIstEnde()))
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeDate(curNode , curNode.getPlanChildrenSumStart()))
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeDate(curNode , curNode.getPlanChildrenSumEnde()))
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeNumber(curNode, curNode.getPlanChildrenSumAufwand(), 0, 2))
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeNumber(curNode, curNode.getIstChildrenSumStand(), 0, 2))
            .append(this.getFieldDelimiter()).append(this.formatNodeNumber(curNode , 
                curNode.getIstChildrenSumAufwand(), 0, 2))
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeDate(curNode, curNode.getIstChildrenSumStart()))
            .append(this.getFieldDelimiter()).append(
                    this.formatNodeDate(curNode, curNode.getIstChildrenSumEnde()))
            .append(this.getFieldDelimiter()).append(curNode.getEbene())
            .append(this.getFieldDelimiter()).append(desc)
            .append(this.getLineEnd());

        // append generated children
        res.append(childRes);

        return res;
    }
    
    
    //////////
    // Service-functions
    //////////

    protected String getFieldDelimiter() {
        return "\t";
    }
    protected String getLineStart() {
        return "";
    }
    protected String getLineEnd() {
        return "\n";
    }
    
    protected String formatNodeDate(final BaseNode curNode, final Date src) {
        return baseFormatter.formatDate(src);
    }
    protected String formatNodeNumber(final BaseNode curNode, final Double src, 
                                      final int minStellen, final int maxStellen) {
        return baseFormatter.formatNumber(src, minStellen, maxStellen).replace(".", ",");
    }

}
