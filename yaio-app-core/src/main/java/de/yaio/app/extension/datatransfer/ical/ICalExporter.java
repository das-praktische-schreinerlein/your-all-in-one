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
package de.yaio.app.extension.datatransfer.ical;

import biweekly.ICalDataType;
import biweekly.ICalendar;
import biweekly.component.VAlarm;
import biweekly.component.VEvent;
import biweekly.component.VTodo;
import biweekly.parameter.Related;
import biweekly.property.*;
import biweekly.util.Duration;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.node.EventNode;
import de.yaio.app.core.node.InfoNode;
import de.yaio.app.core.node.TaskNode;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.datatransfer.exporter.formatter.DescDataFormatterImpl;
import de.yaio.app.datatransfer.exporter.formatter.Formatter;
import de.yaio.app.datatransfer.exporter.formatter.FormatterImpl;
import de.yaio.app.extension.datatransfer.wiki.WikiExporter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/** 
 * export of Nodes as ICal
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.ical
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ICalExporter extends WikiExporter {

    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(ICalExporter.class);

    protected final DateFormat DF = new SimpleDateFormat("yyyyMMdd");
    protected final DateFormat TF = new SimpleDateFormat("HHmmss");

    /** 
     * service functions to export nodes as ICal
     */
    public ICalExporter() {
        super();
    }

    @Override
    public void initDataDomainFormatter() {
        DescDataFormatterImpl.configureDataDomainFormatter(this);
    }

    @Override
    public StringBuffer getNodeResult(final DataDomain curNode, final String praefix,
                                      final OutputOptions oOptions) {
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
     * formats recursively node in ICal-format
     * @param paramCurNode           node for output recursively
     * @param oOptions               options for output (formatter)
     * @return                       formatted output of node-hierarchy and DataDomains
     */
    public String genICalForNode(final BaseNode paramCurNode, final OutputOptions oOptions) {
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
     * formats single TaskNode in ICal-format
     * @param paramCurNode           node for output
     * @param oOptions               options for output (formatter)
     * @return                       formatted output
     */
    public String genICalForTaskNode(final TaskNode paramCurNode, final OutputOptions oOptions) {
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
        name = name.replaceAll("<WLESC>", "\\\\");
        name = name.replaceAll("<WLTAB>", "\t");

        // Felder einlesen
        StringBuffer descFull = new StringBuffer();
        OutputOptions descOOptions = genOutputOptionsForDescArea(oOptions);
        descOOptions.setFlgReEscapeDesc(true);
        this.formatNodeDataDomains(curNode, descFull, descOOptions);
        String statusName = "IN-PROCESS";
        Status status = Status.inProgress();

        // Result erzeugen
        res += "BEGIN:VTODO\n";
        res += "CREATED:" + DF.format(curNode.getSysCreateDate()) + "T" 
                        + TF.format(curNode.getSysCreateDate()) + "Z" + "\n";
        res += "LAST-MODIFIED:" + DF.format(curNode.getSysChangeDate()) + "T" 
                        + TF.format(curNode.getSysChangeDate()) + "Z" + "\n";
        res += "DTSTAMP:" + DF.format(curNode.getSysChangeDate()) + "T" 
                        + TF.format(curNode.getSysChangeDate()) + "Z" + "\n";
        res += "UID:" + curNode.getSysUID() + "\n";

        VTodo event = new VTodo();
        event.setCreated(new Created(curNode.getSysCreateDate()));
        event.setLastModified(new LastModified(curNode.getSysCreateDate()));
        event.setDateTimeStamp(new DateTimeStamp(curNode.getSysCreateDate()));
        event.setUid(new Uid(curNode.getSysUID()));
        
        String id = "";
        id += curNode.getMetaNodePraefix() != null ? curNode.getMetaNodePraefix() : "";
        id += curNode.getMetaNodeNummer() != null ? curNode.getMetaNodeNummer() : "";
        res += "SUMMARY:" + id  + ": " + name + "\n";
        res += "CATEGORIES:Planung\n";

        Summary summary = event.setSummary(id  + ": " + name);
        summary.setLanguage("de-de");
        event.addCategories("Planung");

        Date dateStart = curNode.getCurrentStart();
        Date dateEnde = curNode.getPlanEnde();
        if (dateStart != null) {
            res += "DTSTART;TZID=Europe/Berlin:" + DF.format(dateStart) + "T080000\n";
            event.setDateStart(new DateStart(dateStart, true));
        }
        if (dateEnde != null) {
            res += "DUE;TZID=Europe/Berlin:" + DF.format(dateEnde) + "T180000\n";
            event.setDateDue(new DateDue(dateEnde, true));
        }
        res += "LOCATION:Berlin\n";
        event.setLocation(new Location("Berlin"));
        
        // Status anzeigen
        if (curNode.getIstStand() != null) {
            if (curNode.getIstStand().intValue() > 99 && dateEnde != null) {
                res += "COMPLETED:" + DF.format(dateEnde) + "T180000Z\n";
                statusName = "COMPLETED";
                status = Status.completed();
            } else {
                statusName = "IN-PROCESS";
                status = Status.inProgress();
            }
            res += "PERCENT-COMPLETE:" + curNode.getIstStand().intValue() + "\n";
            event.setExperimentalProperty("PERCENT-COMPLETE", ICalDataType.INTEGER, curNode.getIstStand().toString());
        }
        res += "STATUS:" + statusName + "\n";
        event.setStatus(status);

        if (descFull != null && descFull.length() > 0 && oOptions.isFlgShowDesc()) {
            // Html-Escapen
            event.setDescription(new Description(descFull.toString()));
            String tmpDesc = descFull.toString().replaceAll("\n", "\\\\n");
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
//        Trigger trigger = new Trigger(new Duration.Builder().days(-1));
//        VAlarm alarm = VAlarm.display(trigger, name);
//        event.addAlarm(alarm);
        res += "END:VTODO\n";
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getWorkingId() + " return datalength:" + res.length());
        }

        return res;
    }

    /** 
     * formats single EventNode in ICal-format
     * @param paramCurNode           node for output
     * @param oOptions               options for output (formatter)
     * @return                       formatted output
     */
    public String genICalForEventNode(final EventNode paramCurNode, final OutputOptions oOptions) {
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
        name = name.replaceAll("<WLESC>", "\\\\");
        name = name.replaceAll("<WLTAB>", "\t");

        // Felder einlesen
        StringBuffer descFull = new StringBuffer();
        OutputOptions descOOptions = genOutputOptionsForDescArea(oOptions);
        descOOptions.setFlgReEscapeDesc(true);
        this.formatNodeDataDomains(curNode, descFull, descOOptions);
        String statusName = "CONFIRMED";
        Status status = Status.confirmed();

        // Result erzeugen
        res += "BEGIN:VEVENT\n";
        res += "CREATED:" + DF.format(curNode.getSysCreateDate()) + "T" 
                        + TF.format(curNode.getSysCreateDate()) + "Z" + "\n";
        res += "LAST-MODIFIED:" + DF.format(curNode.getSysChangeDate()) + "T" 
                        + TF.format(curNode.getSysChangeDate()) + "Z" + "\n";
        res += "DTSTAMP:" + DF.format(curNode.getSysChangeDate()) + "T" 
                        + TF.format(curNode.getSysChangeDate()) + "Z" + "\n";
        res += "UID:" + curNode.getSysUID() + "\n";

        VEvent event = new VEvent();
        event.setCreated(new Created(curNode.getSysCreateDate()));
        event.setLastModified(new LastModified(curNode.getSysCreateDate()));
        event.setDateTimeStamp(new DateTimeStamp(curNode.getSysCreateDate()));
        event.setUid(new Uid(curNode.getSysUID()));

        String id = "";
        id += curNode.getMetaNodePraefix() != null ? curNode.getMetaNodePraefix() : "";
        id += curNode.getMetaNodeNummer() != null ? curNode.getMetaNodeNummer() : "";
        res += "SUMMARY:" + id  + ": " + name + "\n";
        res += "CATEGORIES:Planung\n";

        Summary summary = event.setSummary(id  + ": " + name);
        summary.setLanguage("de-de");
        event.addCategories("Planung");

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

            event.setDateStart(new DateStart(dateStart, true));
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

            event.setDateEnd(new DateEnd(dateStart, true));
        }
        res += "LOCATION:Berlin\n";
        event.setLocation(new Location("Berlin"));
        
        // Status anzeigen
        if (curNode.getIstStand() != null) {
            if (curNode.getIstStand().intValue() > 99 && dateEnde != null) {
                res += "X-MOZ-LASTACK:" + DF.format(dateEnde) + "T180000Z\n";
                event.setExperimentalProperty("X-MOZ-LASTACK", ICalDataType.DATE_TIME, DF.format(dateEnde) + "T180000Z");
                res += "COMPLETED:" + DF.format(dateEnde) + "T180000Z\n";
                status = Status.inProgress();
            } else {
                statusName = "CONFIRMED";
                status = Status.confirmed();
            }
            res += "PERCENT-COMPLETE:" + curNode.getIstStand().intValue() + "\n";
            event.setExperimentalProperty("PERCENT-COMPLETE", ICalDataType.INTEGER, curNode.getIstStand().toString());
        } else {
            res += "STATUS:" + statusName + "\n";
        }
        event.setStatus(status);

        if (!StringUtils.isEmpty(descFull) && oOptions.isFlgShowDesc()) {
            // Html-Escapen
            event.setDescription(new Description(descFull.toString()));
            String tmpDesc = descFull.toString();
            tmpDesc = tmpDesc.replaceAll("\n", "\\\\n");
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

        Trigger trigger = new Trigger(new Duration.Builder().prior(true).days(-1).build(), Related.START);
        VAlarm alarm = VAlarm.display(trigger, name);
        event.addAlarm(alarm);

        res += "END:VEVENT\n";
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + curNode.getWorkingId() + " return datalength:" + res.length());
        }

        return res;
    }


    @Override
    public String getMasterNodeResult(final DataDomain masterNode, 
            final OutputOptions oOptions) throws ConverterException {
        String icalRes = getCalHeader(masterNode, oOptions);

        icalRes += super.getMasterNodeResult(masterNode, oOptions);
        
        // Footer anhaengen
        icalRes += getCalFooter(masterNode, oOptions);
        
        // Hack wegen UFT8-Sonderzeichen
        // escape non latin
        StringBuilder sb;
        try {
            sb = FormatterImpl.escapeNonLatin(icalRes, new StringBuilder());
        } catch (IOException ex) {
            throw new ConverterException("error while escapeNonLatin for icalExport", icalRes, ex);
        }
        icalRes = sb.toString();
        icalRes = icalRes.replaceAll("\n", "\r\n");
        
        return icalRes;
    }
    
    /** 
     * get header for a ICal-calendar
     * @param masterNode             node for output
     * @param oOptions               options for output (formatter)
     * @return                       header for ICal-calendar
     */
    public String getCalHeader(final DataDomain masterNode, 
            final OutputOptions oOptions) {
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

        return icalRes;
    }

    /** 
     * get footer for a ICal-calendar
     * @param masterNode             node for output
     * @param oOptions               options for output (formatter)
     * @return                       footer for ICal-calendar
     */
    public String getCalFooter(final DataDomain masterNode, 
                                  final OutputOptions oOptions) {
        String icalRes = "END:VCALENDAR\n";

        return icalRes;
    }
    
    public ICalendar createICalendar() {
        ICalendar ical = new ICalendar();
        return ical;
    }
}
