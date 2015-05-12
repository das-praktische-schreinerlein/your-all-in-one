/**
# * <h4>FeatureDomain:</h4>

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
package de.yaio.extension.datatransfer.ical;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.EventNode;
import de.yaio.core.node.InfoNode;
import de.yaio.core.node.TaskNode;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.formatter.DescDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.Formatter;
import de.yaio.datatransfer.exporter.formatter.FormatterImpl;
import de.yaio.extension.datatransfer.wiki.WikiExporter;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     export of Nodes as ICal
 * 
 * @package de.yaio.extension.datatransfer.ical
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ICalExporter extends WikiExporter {

    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(ICalExporter.class);

    protected final DateFormat DF = new SimpleDateFormat("yyyyMMdd");
    protected final DateFormat TF = new SimpleDateFormat("HHmmss");

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     service functions to export nodes as ICal
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the exporter
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     */
    public ICalExporter() {
        super();
    }

    @Override
    public void initDataDomainFormatter() {
        DescDataFormatterImpl.configureDataDomainFormatter(this);
    };

    @Override
    public StringBuffer getNodeResult(final DataDomain curNode,  final String praefix,
            final OutputOptions oOptions) throws Exception {
        StringBuffer res = new StringBuffer();

        // Template-Nodes ignorieren
//        if (TemplateNode.class.isInstance(curNode)) {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("SKIP TemplateNode node:" + curNode.getNameForLogger());
//            }
//            return res;
//        }

        // Anfang
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node: start processing" + curNode.getNameForLogger());
        }

        // Projekt-Ausgabe aller 
        res.append(this.genICalForNode((BaseNode) curNode, oOptions));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node return datalength:" + res.length() 
                    + " for "  + curNode.getNameForLogger());
        }

        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     formats recursively node in ICal-format
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - formatted output of node-hirarchy
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param paramCurNode - node for output recursively
     * @param oOptions - options for output (formatter)
     * @return - formatted output of node-hierarchy and DataDomains
     * @throws Exception - parser/format-Exceptions possible
     */
    public String genICalForNode(final BaseNode paramCurNode, 
        final OutputOptions oOptions) throws Exception {
        String res = "";

        // max. Ebene pruefen
        if (paramCurNode.getEbene() > oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + paramCurNode.getWorkingId() 
                    + " ignore:" + paramCurNode.getEbene() + ">" + oOptions.getMaxEbene());
            }
            return res;
        }

        // Anfang
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("curnode:" + paramCurNode.getWorkingId() + " start processing");
        }
        
        // alle Kindselemente durchlaufen
        String blockChildren = "";
        boolean flgHasWFChildren = false;
        if (paramCurNode.getEbene() < oOptions.getMaxEbene() 
                && paramCurNode.getChildNodes().size() > 0) {
            // Elternblock: Zweig
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + paramCurNode.getWorkingId() 
                    + " iterate subnodes:" + paramCurNode.getChildNodes().size());
            }
            for (String nodeName : paramCurNode.getChildNodesByNameMap().keySet()) {
                DataDomain subNode = paramCurNode.getChildNodesByNameMap().get(nodeName);
                if (!TaskNode.class.isInstance(subNode)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("subnode:" + subNode.getNameForLogger() 
                            + " ignore: not TaskNode but " + subNode.getClass());
                    }
                    continue;
                }
                // nur ausfuehren, wenn Kindselement WF-Status hat
                TaskNode subTaskNode = (TaskNode) subNode;
                String subNodeStatus = subTaskNode.getState();
                if (subTaskNode.isWFStatus(subNodeStatus)) {
                    blockChildren += this.getNodeResult(subTaskNode, "", oOptions);
                    flgHasWFChildren = true;
                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("subnode:" + subNode.getNameForLogger() 
                            + " ignore: no WF-Status but " + subNodeStatus);
                    }
                }
            }
        } else {
            // Blatt
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + paramCurNode.getWorkingId() + " no subnodes");
            }
        }
        
        // nur Projektnodes zulassen
        if (!TaskNode.class.isInstance(paramCurNode)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + paramCurNode.getWorkingId() 
                    + " ignore: not TaskNode but " + paramCurNode.getClass());
            }
            return blockChildren;
        }
        TaskNode curNode = (TaskNode) paramCurNode;

        // Status ueberpruefen
        String state = curNode.getState();
        if (!curNode.isWFStatus(state) || flgHasWFChildren) {
            // kein eigener WFNode oder Kindelemente haben WF-State -> SKIP
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP - No WFNode - node:" + curNode.getWorkingId() 
                                + " return datalength:" + res.length());
            }
            return blockChildren;
        }
        
        // check if I'am matching
        boolean flgMatchesFilter = this.isNodeMatchingFilter(curNode, oOptions);
        if (!flgMatchesFilter) {
            // sorry I dont match
            return blockChildren;
        }

        // Juhu we match
        
        // Node-Daten generieren
        if (InfoNode.class.isInstance(paramCurNode)) {
            // SKIP
        } else if (EventNode.class.isInstance(paramCurNode)) {
            res += this.genICalForEventNode((EventNode) paramCurNode, oOptions);
        } else if (TaskNode.class.isInstance(paramCurNode)) {
            res += this.genICalForTaskNode((TaskNode) paramCurNode, oOptions);
        }
        
        res += blockChildren;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getWorkingId() + " return datalength:" + res.length());
        }

        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     formats single TaskNode in ICal-format
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - formatted output of single node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param paramCurNode - node for output
     * @param oOptions - options for output (formatter)
     * @return - formatted output
     * @throws Exception - parser/format-Exceptions possible
     */
    public String genICalForTaskNode(final TaskNode paramCurNode, 
        final OutputOptions oOptions) throws Exception {
        String res = "";

        // max. Ebene pruefen
        if (paramCurNode.getEbene() > oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + paramCurNode.getWorkingId() 
                    + " ignore:" + paramCurNode.getEbene() + ">" + oOptions.getMaxEbene());
            }
            return res;
        }

        // Anfang
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("curnode:" + paramCurNode.getWorkingId() + " start processing");
        }
        
        // nur Projektnodes zulassen
        if (!TaskNode.class.isInstance(paramCurNode)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + paramCurNode.getWorkingId() 
                    + " ignore: not ProjektNode but " + paramCurNode.getClass());
            }
            return "";
        }
        TaskNode curNode = (TaskNode) paramCurNode;

        // Status ueberpruefen
        String state = curNode.getState();
        if (!curNode.isWFStatus(state)) {
            // kein eigener WFNode oder Kindelemente haben WF-State -> SKIP
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP - No WFNode - node:" + curNode.getWorkingId() 
                                + " return datalength:" + res.length());
            }
            return res;
        }
        
        // Namen erzeugen
        String name = curNode.getName();
        if (curNode.getParentNode() != null) {
            name = curNode.getParentNode().getName() 
                + " - " + name 
                + " (" + curNode.getParentNameHirarchry(" <- ", false) + ")";
        }
        //name = name.replaceAll("\"", "'");
        name = name.replaceAll("<WLESC>", "\\");
        name = name.replaceAll("<WLTAB>", "\t");

        // Felder einlesen
        StringBuffer descFull = new StringBuffer();
        this.formatNodeDataDomains(curNode, descFull, genOutputOptionsForDescArea(oOptions));
        String statusName = "IN-PROCESS";

        // Result erzeugen
        res += "BEGIN:VTODO\n";
        res += "CREATED:" + DF.format(curNode.getSysCreateDate()) + "T" 
                        + TF.format(curNode.getSysCreateDate()) + "Z" + "\n";
        res += "LAST-MODIFIED:" + DF.format(curNode.getSysChangeDate()) + "T" 
                        + TF.format(curNode.getSysChangeDate()) + "Z" + "\n";
        res += "DTSTAMP:" + DF.format(curNode.getSysChangeDate()) + "T" 
                        + TF.format(curNode.getSysChangeDate()) + "Z" + "\n";
        res += "UID:" + curNode.getSysUID() + "\n";
        
        String Id = "";
        Id += (curNode.getMetaNodePraefix() != null ? curNode.getMetaNodePraefix() : "");
        Id += (curNode.getMetaNodeNummer() != null ? curNode.getMetaNodeNummer() : "");
        res += "SUMMARY:" + Id  + ": " + name + "\n";
        res += "CATEGORIES:Planung\n";
        Date dateStart = curNode.getCurrentStart();
        Date dateEnde = curNode.getPlanEnde();
        if (dateStart != null) {
            res += "DTSTART;TZID=Europe/Berlin:" + DF.format(dateStart) + "T080000\n";
        }
        if (dateEnde != null) {
            res += "DUE;TZID=Europe/Berlin:" + DF.format(dateEnde) + "T180000\n";
        }
        res += "LOCATION:Berlin\n";
        
        // Status anzeigen
        if (curNode.getIstStand() != null) {
            if (curNode.getIstStand().intValue() > 99 && dateEnde != null) {
                res += "COMPLETED:" + DF.format(dateEnde) + "T180000Z\n";
                statusName = "COMPLETED";
            } else {
                statusName = "IN-PROCESS";
            }
            res += "PERCENT-COMPLETE:" + curNode.getIstStand().intValue() + "\n";
        }
        res += "STATUS:" + statusName + "\n";

        if (descFull != null && descFull.length() > 0 && oOptions.isFlgShowDesc()) {
            // Html-Escapen
            String tmpDesc = descFull.toString().replaceAll("\n", "\\n");
            res += "DESCRIPTION:" + tmpDesc + "\n";
        } else {
            res += "DESCRIPTION:\n";
        }
//      X-MOZ-GENERATION:1
//      BEGIN:VALARM
//      ACTION:DISPLAY
//      TRIGGER;VALUE=DURATION:-P1D
//      DESCRIPTION:Mozilla Standardbeschreibung
//      END:VALARM
        res += "END:VTODO\n";
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getWorkingId() + " return datalength:" + res.length());
        }

        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     formats single EventNode in ICal-format
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - formatted output of single node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param paramCurNode - node for output
     * @param oOptions - options for output (formatter)
     * @return - formatted output
     * @throws Exception - parser/format-Exceptions possible
     */
    public String genICalForEventNode(final EventNode paramCurNode, 
        final OutputOptions oOptions) throws Exception {
        String res = "";

        // max. Ebene pruefen
        if (paramCurNode.getEbene() > oOptions.getMaxEbene()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + paramCurNode.getWorkingId() 
                    + " ignore:" + paramCurNode.getEbene() + ">" + oOptions.getMaxEbene());
            }
            return res;
        }

        // Anfang
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("curnode:" + paramCurNode.getWorkingId() + " start processing");
        }
        
        // nur Projektnodes zulassen
        if (!EventNode.class.isInstance(paramCurNode)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + paramCurNode.getWorkingId() 
                    + " ignore: not EventNode but " + paramCurNode.getClass());
            }
            return "";
        }
        TaskNode curNode = (TaskNode) paramCurNode;

        // Status ueberpruefen
        String state = curNode.getState();
        if (!curNode.isWFStatus(state)) {
            // kein eigener WFNode oder Kindelemente haben WF-State -> SKIP
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP - No WFNode - node:" + curNode.getWorkingId() 
                                + " return datalength:" + res.length());
            }
            return res;
        }
        
        // Namen erzeugen
        String name = curNode.getName();
        if (curNode.getParentNode() != null) {
            name = curNode.getParentNode().getName() 
                + " - " + name 
                + " (" + curNode.getParentNameHirarchry(" <- ", false) + ")";
        }
        //name = name.replaceAll("\"", "'");
        name = name.replaceAll("<WLESC>", "\\");
        name = name.replaceAll("<WLTAB>", "\t");

        // Felder einlesen
        StringBuffer descFull = new StringBuffer();
        this.formatNodeDataDomains(curNode, descFull, genOutputOptionsForDescArea(oOptions));
        String statusName = "CONFIRMED";

        // Result erzeugen
        res += "BEGIN:VEVENT\n";
        res += "CREATED:" + DF.format(curNode.getSysCreateDate()) + "T" 
                        + TF.format(curNode.getSysCreateDate()) + "Z" + "\n";
        res += "LAST-MODIFIED:" + DF.format(curNode.getSysChangeDate()) + "T" 
                        + TF.format(curNode.getSysChangeDate()) + "Z" + "\n";
        res += "DTSTAMP:" + DF.format(curNode.getSysChangeDate()) + "T" 
                        + TF.format(curNode.getSysChangeDate()) + "Z" + "\n";
        res += "UID:" + curNode.getSysUID() + "\n";

        String Id = "";
        Id += (curNode.getMetaNodePraefix() != null ? curNode.getMetaNodePraefix() : "");
        Id += (curNode.getMetaNodeNummer() != null ? curNode.getMetaNodeNummer() : "");
        res += "SUMMARY:" + Id  + ": " + name + "\n";
        res += "CATEGORIES:Planung\n";
        Date dateStart = curNode.getCurrentStart();
        Date dateEnde = curNode.getCurrentEnde();
        Calendar calTime = new GregorianCalendar();
        if (dateStart != null) {
            res += "DTSTART;TZID=Europe/Berlin:" + DF.format(dateStart);
            
            // Zeitanteil setzen
            calTime.setTime(dateStart);
            if (calTime.get(Calendar.SECOND) == Formatter.CONST_FLAG_NODATE_SECONDS) {
                // wenn Sekunde gesetzt, dann keine Uhrzeit angegeben: Standard 08:00
                res += "T080000\n";
            } else {
                // Uhrzeit des Datums benutzen
                res += "T" + TF.format(dateStart) + "\n";
            }
        }
        if (dateEnde != null) {
//            res += "DUE;TZID=Europe/Berlin:" + DF.format(dateEnde) + "T180000\n";
            res += "DTEND;TZID=Europe/Berlin:" + DF.format(dateEnde);

            // Zeitanteil setzen
            calTime.setTime(dateEnde);
            if (calTime.get(Calendar.SECOND) == Formatter.CONST_FLAG_NODATE_SECONDS) {
                // wenn Sekunde gesetzt, dann keine Uhrzeit angegeben: Standard 17:00
                res += "T170000\n";
            } else {
                // Uhrzeit des Datums benutzen
                res += "T" + TF.format(dateEnde) + "\n";
            }
        }
        res += "LOCATION:Berlin\n";
        
        // Status anzeigen
        if (curNode.getIstStand() != null) {
            if (curNode.getIstStand().intValue() > 99 && dateEnde != null) {
                res += "X-MOZ-LASTACK:" + DF.format(dateEnde) + "T180000Z\n";
                res += "COMPLETED:" + DF.format(dateEnde) + "T180000Z\n";
            } else {
                statusName = "CONFIRMED";
            }
            res += "PERCENT-COMPLETE:" + curNode.getIstStand().intValue() + "\n";
        } else {
            res += "STATUS:" + statusName + "\n";
        }

        if (descFull != null && descFull.length() > 0 && oOptions.isFlgShowDesc()) {
            // Html-Escapen
            String tmpDesc = descFull.toString().replaceAll("\n", "\\n");
            res += "DESCRIPTION:" + tmpDesc + "\n";
        } else {
            res += "DESCRIPTION:\n";
        }
        
        // Alarm
        res += "BEGIN:VALARM\n";
        res += "ACTION:DISPLAY\n";
        res += "TRIGGER;VALUE=DURATION:-P1D\n";
        res += "DESCRIPTION:Mozilla Standardbeschreibung\n";
        res += "END:VALARM\n";

        res += "END:VEVENT\n";
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getWorkingId() + " return datalength:" + res.length());
        }

        return res;
    }


    @Override
    public String getMasterNodeResult(final DataDomain masterNode, 
            final OutputOptions oOptions) throws Exception {
        String icalRes = "BEGIN:VCALENDAR\n";
        icalRes += "PRODID:-//Mozilla.org/NONSGML Mozilla Calendar V1.1//EN\n";
        icalRes += "VERSION:2.0\n";
        icalRes += "X-WR-CALNAME:MS-Tasks\n";
        icalRes += "X-WR-TIMEZONE:Europe/Berlin\n";
        icalRes += "BEGIN:VTIMEZONE\n";
        icalRes += "TZID:Europe/Berlin\n";
        icalRes += "X-LIC-LOCATION:Europe/Berlin\n";
        icalRes += "BEGIN:DAYLIGHT\n";
        icalRes += "TZOFFSETFROM:+0100\n";
        icalRes += "TZOFFSETTO:+0200\n";
        icalRes += "TZNAME:CEST\n";
        icalRes += "DTSTART:19700329T020000\n";
        icalRes += "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=3\n";
        icalRes += "END:DAYLIGHT\n";
        icalRes += "BEGIN:STANDARD\n";
        icalRes += "TZOFFSETFROM:+0200\n";
        icalRes += "TZOFFSETTO:+0100\n";
        icalRes += "TZNAME:CET\n";
        icalRes += "DTSTART:19701025T030000\n";
        icalRes += "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10\n";
        icalRes += "END:STANDARD\n";
        icalRes += "END:VTIMEZONE\n";

        // Daten berechnen
        if (oOptions.isFlgRecalc()) {
            masterNode.recalcData(NodeService.CONST_RECURSE_DIRECTION_CHILDREN);
        }
        icalRes += super.getMasterNodeResult(masterNode, oOptions);
        
        // Footer anhaengen
        icalRes += "END:VCALENDAR\n";
        
        // Hack wegen UFT8-Sonderzeichen
        // escape non latin
        StringBuilder sb = FormatterImpl.escapeNonLatin(icalRes, new StringBuilder());
        icalRes = sb.toString();
        icalRes = icalRes.replaceAll("\n", "\r\n");
        
        
        return icalRes;
    }
}
