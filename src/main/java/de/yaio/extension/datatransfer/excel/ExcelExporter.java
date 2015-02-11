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
package de.yaio.extension.datatransfer.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.CFRuleRecord.ComparisonOperator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFConditionalFormattingRule;
import org.apache.poi.hssf.usermodel.HSSFPatternFormatting;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSheetConditionalFormatting;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.TaskNode;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.formatter.DescDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.DocLayoutDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.IstDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.PlanDataFormatterImpl;
import de.yaio.datatransfer.importer.parser.ParserImpl;
import de.yaio.extension.datatransfer.wiki.WikiExporter;
import de.yaio.utils.ExcelService;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     export of Nodes as Excel
 * 
 * @package de.yaio.extension.datatransfer.excel
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ExcelExporter extends WikiExporter {
    
    public ExcelExporter() {
        super();
    }
    
    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(ExcelExporter.class);
    
    protected ExcelOutputService exlSv = null;
    public static String CONST_FORMATTER_DESC = DescDataFormatterImpl.class.getName();
    public static String CONST_FORMATTER_DOCLAYOUT = DocLayoutDataFormatterImpl.class.getName();
    public static String CONST_FORMATTER_IST = IstDataFormatterImpl.class.getName();
    public static String CONST_FORMATTER_PLAN = PlanDataFormatterImpl.class.getName();
    
    @Override
    public String getMasterNodeResult(final DataDomain masterNode,
            final OutputOptions oOptions) throws Exception {
        throw new IllegalAccessException("This function must not be used!!!");
    }

    @Override
    public StringBuffer getNodeResult(final DataDomain node,  final String praefix,
            final OutputOptions oOptions) throws Exception {
        throw new IllegalAccessException("This function must not be used!!!");
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     * <h4>FeatureDescription:</h4>
     *     generate helper-OutputOptions for generation of the excel
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue OutputOptions - OuputOptions for generation
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Cofiguration helper
     * @param baseOOptions - Default OutputOptions to override
     * @return OuputOptions for generatio
     */
    public ExcelOutputOptions genOutputOptionsForExcel(final OutputOptions baseOOptions) {
        ExcelOutputOptions options = new ExcelOutputOptions(baseOOptions);

        // activate desc
        options.setFlgReEscapeDesc(true);
        options.setFlgTrimDesc(false);
        options.setFlgShowDescInNextLine(false);
        options.setFlgShowBrackets(false);
        options.setIntendFuncArea(0);
        options.setIntendSys(0);

        return options;
    }
    

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     formats recursively nodes into workbook-sheet Planung
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates wb - insert lines
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param wb - Workbook to fill
     * @param masterNode - node for output recursively
     * @param oOptions - options for output (formatter)
     * @throws Exception - possible Exception     */
    public void fillPlanungSheet(final HSSFWorkbook wb, final BaseNode masterNode,
                final ExcelOutputOptions oOptions)
        throws Exception {

        // ExcelService anlegen
        this.exlSv = new ExcelOutputService(wb);

        // Parameter pruefen
        if (wb == null) {
            throw new IllegalArgumentException("Workbook must not be null: '"
                    + wb + "'");
        }
        if (masterNode == null) {
            throw new IllegalArgumentException("Masternode must not be null: '"
                    + masterNode + "'");
        }

        // Betreffendes Sheet anhand des Indexes ermitteln (funkt. auch noch
        // wenn mal das Tabellenblatt umbenannt wird)
        HSSFSheet sheet = null;
        sheet = wb.getSheet(ExcelNodeService.CONST_SHEETNNAME_PLANUNG);
        if (sheet == null) {
            sheet = wb.createSheet(ExcelNodeService.CONST_SHEETNNAME_PLANUNG);
        }

        // alle SubNotes des Mastrs durchlaufen
        if (masterNode.getEbene() != 1) {
// TODO
//            throw new IllegalArgumentException("Masternode ist not Master: "
//                    + masterNode.getEbene() + "'" + masterNode + "'");
        }

        int startRownNum = ExcelNodeService.CONST_PLANUNG_ROUW_UE;
        this.createPlanungLineUe(sheet, startRownNum, oOptions);
        if (oOptions.isFlgMergeExcelPlanungGantSheets()) {
            this.createGantLineDiagUe(sheet, startRownNum, oOptions, ExcelNodeService.CONST_GANT_VERSATZ);
        }
        startRownNum++;
        int lastIndex = this.createPlanungLines4Node(sheet, masterNode,
                startRownNum, -1, oOptions);

        // Blatt sch�tzen, damit Felder gesperrt
        //sheet.setProtect(true);

        // Projekt+ID Spalte ausblenden
        sheet.setColumnHidden(new Integer(ExcelNodeService.CONST_PLANUNG_COL_ID), true);
        sheet.setColumnHidden(new Integer(ExcelNodeService.CONST_PLANUNG_COL_FLG_DETAIL), true);
        sheet.setColumnHidden(new Integer(ExcelNodeService.CONST_PLANUNG_COL_PROJEKT), true);
        sheet.setColumnHidden(new Integer(ExcelNodeService.CONST_PLANUNG_COL_IST_STANDFAKTOR), true);

        // Gesamten Sheet formatieren
        for (int curCol = 1; curCol < ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND; curCol++) {
            // Spalte auf AutoSize
            sheet.autoSizeColumn(curCol);
        }
        // alle Struktur-Spalten vor MaxEbene verkleinern
        int maxEbene = masterNode.getMaxChildEbene();
        for (int curCol = ExcelNodeService.CONST_PLANUNG_COL_PROJEKT; curCol < ExcelNodeService.CONST_PLANUNG_COL_PROJEKT + maxEbene - 1; curCol++) {
            sheet.setColumnWidth(curCol, 150);
        }

        // alle Struktur-Spalten hinter MaxEbene ausblenden
        for (int curCol = ExcelNodeService.CONST_PLANUNG_COL_PROJEKT + maxEbene; curCol <= ExcelNodeService.CONST_PLANUNG_COL_DESC; curCol++) {
            sheet.setColumnWidth(curCol, 0);
            sheet.setColumnHidden(curCol, true);
        }

        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND, ExcelService.CONST_COL_WIDTH_INT);
        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START, ExcelService.CONST_COL_WIDTH_DATE);
        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_ENDE, ExcelService.CONST_COL_WIDTH_DATE);
        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_MIN_ENDE, ExcelService.CONST_COL_WIDTH_DATE);
        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_IST_STAND, ExcelService.CONST_COL_WIDTH_INT);
        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_IST_AUFWAND, ExcelService.CONST_COL_WIDTH_INT);
        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_START, ExcelService.CONST_COL_WIDTH_DATE);
        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_ENDE, ExcelService.CONST_COL_WIDTH_DATE);
        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_REAL_AUFWAND, ExcelService.CONST_COL_WIDTH_INT);
        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_REAL_OFFEN, ExcelService.CONST_COL_WIDTH_INT);
        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_REAL_DIFF, ExcelService.CONST_COL_WIDTH_INT);
        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_START, ExcelService.CONST_COL_WIDTH_DATE);
        sheet.setColumnWidth(ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_ENDE, ExcelService.CONST_COL_WIDTH_DATE);

        // Gant-Diagramm formatieren
        if (oOptions.isFlgMergeExcelPlanungGantSheets()) {
            this.formatGantSheetDiag(sheet, lastIndex, oOptions, ExcelNodeService.CONST_GANT_VERSATZ);
        }

        // Druckanpassung
        sheet.setFitToPage(true);
        HSSFPrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
        printSetup.setUsePage(true);
        printSetup.setFitWidth((short) 1);
        printSetup.setScale((short) 50);
    }

    // TODO Doku
    public int createPlanungLines4Node(final HSSFSheet sheet, final BaseNode node,
            final int startRownNum, final int vorgaengerRownNum,
            final ExcelOutputOptions oOptions)
    throws Exception {
        // Parameter pruefen
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet must not be null: '"
                    + sheet + "'");
        }
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null: '"
                    + node + "'");
        }
        if (startRownNum < 0) {
            throw new IllegalArgumentException("startRownNum must not be null: " + startRownNum
                    + " for '" + node + "'");
        }

        // alle KindElemente ab der Folgezeile anfuegen
        Map<String, DataDomain> childNodes = node.getChildNodesByNameMap();
        int curRowNum = startRownNum;
        List<Integer> lstChildRowNum = new ArrayList<Integer>();
        int curListVorgaengerRownNum = -1;
        int lastListVorgaengerRownNum = -1;
        // nur Kindselemente einfuegen, wenn Ebene
        if (node.getEbene() <= oOptions.getMaxEbene()) {
            for (String nodeName : childNodes.keySet()) {
                BaseNode childNode = (BaseNode) childNodes.get(nodeName);
                // naechste Zeile
                curRowNum++;
                lstChildRowNum.add(new Integer(curRowNum));

                // die Nr des Vorgaengers speichern
                lastListVorgaengerRownNum = curRowNum;

                // aktuelles Element einfuegen
                curRowNum = this.createPlanungLines4Node(sheet, childNode, curRowNum,
                        curListVorgaengerRownNum, oOptions);

                // die Postition des Vorgaengers herstellen
                curListVorgaengerRownNum = lastListVorgaengerRownNum;
            }
        }

        // meine eigene Zeile einfuegen
        this.createPlanungLine4Node(sheet, node, startRownNum,
                lstChildRowNum, vorgaengerRownNum, oOptions);
        if (oOptions.isFlgMergeExcelPlanungGantSheets()) {
            this.createGantLineDiag4Node(sheet, node, startRownNum,
                    lstChildRowNum, oOptions, ExcelNodeService.CONST_GANT_VERSATZ);
        }

        return curRowNum;
    }

    // TODO Doku
    public void createPlanungLineUe(final HSSFSheet sheet, final int startRownNum,
            final ExcelOutputOptions oOptions)
    throws Exception {
        // Parameter pruefen
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet must not be null: '"
                    + sheet + "'");
        }
        if (startRownNum < 0) {
            throw new IllegalArgumentException("Startindex must not be null: " + startRownNum
                    + " for Ue");
        }

        HSSFCell cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_ID, "Id", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PROJEKT, "Projekt", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_MODUL, "Modul", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PAKET, "Paket", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_UNTERPAKET, "Unterpaket", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT, "Schritt", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT2, "Schritt2", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT3, "Schritt3", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT4, "Schritt4", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT5, "Schritt5", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_DESC, "ProjektDesc", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_TASK, "P-Task", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_PROGNOSE, "Prognose", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND, "P-Aufwand", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START, "P-Start", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_ENDE, "P-Ende", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_MIN_ENDE, "P-Min-Ende", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_IST_TASK, "I-Task", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_IST_STAND, "I-Stand", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_IST_AUFWAND, "I-Aufwand", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_START, "I-Start", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_ENDE, "I-Ende", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_REAL_AUFWAND, "R-Plan-Aufwand", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_REAL_OFFEN, "R-Offen", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_REAL_DIFF, "R-Diff", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_START, "R-Start", this.exlSv.csFieldPlanungBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_ENDE, "R-Ende", this.exlSv.csFieldPlanungBaseUe);
    }

    // TODO Doku
    public void createPlanungLine4Node(final HSSFSheet sheet, final BaseNode node,
            final int startRownNum, final List<Integer> lstChildRowNum, final int vorgaengerRownNum,
            final ExcelOutputOptions oOptions)
    throws Exception {
        // Parameter pruefen
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet must not be null: '"
                    + sheet + "'");
        }
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null: '"
                    + node + "'");
        }
        if (startRownNum < 0) {
            throw new IllegalArgumentException("Startindex must not be null: " + startRownNum
                    + " for '" + node + "'");
        }

        // Id in 1. Spalte
        ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_ID, node.getWorkingId());
        
        // eventuelle Projektnode belegen
        TaskNode projektNode = null;
        if (TaskNode.class.isInstance(node)) {
            projektNode = (TaskNode) node;
        }
        
        HSSFCell cell = null;
        if (lstChildRowNum.size() > 0 && node.getEbene() <= oOptions.getMaxEbene()) {
            // Oberpunkt: Summe der Unterpunkte

            // Detail-Flag
            cell = ExcelService.setCellNumeric(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_FLG_DETAIL, new Double(0),
                    this.exlSv.csFieldPlanungStruktur_Modul);

            //
            // Plan
            //

            // Unterpunkte summieren
            String formula = ExcelService.genFormula("SUM", lstChildRowNum,
                    ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND);
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND, formula,
                    this.exlSv.csFieldPlanungPlanAufwand_Modul);

            // Minimum-StartDatum berechnen
            formula = ExcelService.genFormula("MIN", lstChildRowNum,
                    ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START);
            formula =
                "IF(" + formula + " > 0" + ExcelService.CONST_PARAM_DELIM +
                formula + ExcelService.CONST_PARAM_DELIM + "\"\")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START, formula,
                    this.exlSv.csFieldPlanungPlanStartDate_Modul);

            // Maximum-EndDatum berechnen
            formula = ExcelService.genFormula("MAX", lstChildRowNum,
                    ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_ENDE);
            formula =
                "IF(" + formula + " > 0" + ExcelService.CONST_PARAM_DELIM
                + formula + ExcelService.CONST_PARAM_DELIM + "\"\")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_ENDE, formula,
                    this.exlSv.csFieldPlanungPlanEndDate_Modul);

            // Maximum-MinEndDatum berechnen
            formula = ExcelService.genFormula("MAX", lstChildRowNum,
                    ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_MIN_ENDE);
            formula =
                "IF(" + formula + " > 0" + ExcelService.CONST_PARAM_DELIM
                + formula + ExcelService.CONST_PARAM_DELIM + "\"\")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_MIN_ENDE, formula,
                    this.exlSv.csFieldPlanungPlanMinEndDate_Modul);

            //
            // Ist
            //

            // Unterpunkte summieren
            formula = ExcelService.genFormula("SUM", lstChildRowNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_AUFWAND);
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_AUFWAND, formula,
                    this.exlSv.csFieldPlanungIstAufwand_Modul);

            // Minimum-StartDatum berechnen
            formula = ExcelService.genFormula("MIN", lstChildRowNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_START);
            formula =
                "IF(" + formula + " > 0" + ExcelService.CONST_PARAM_DELIM +
                formula  + ExcelService.CONST_PARAM_DELIM + "\"\")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_START, formula,
                    this.exlSv.csFieldPlanungIstStartDate_Modul);

            // Maximum-EndDatum berechnen
            formula = ExcelService.genFormula("MAX", lstChildRowNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_ENDE);
            formula =
                "IF(" + formula + " > 0" + ExcelService.CONST_PARAM_DELIM
                + formula + ExcelService.CONST_PARAM_DELIM + "\"\")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_ENDE, formula,
                    this.exlSv.csFieldPlanungIstEndDate_Modul);

            // Stand berechnen (Standfaktor = Stand / Aufwand)
            String aufwand =
                ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND)
                + ExcelService.getRowNum(startRownNum);
            formula = "IF("
                + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_STANDFAKTOR)
                + ExcelService.getRowNum(startRownNum)
                + "=\"\"" + ExcelService.CONST_PARAM_DELIM + "0"
                + ExcelService.CONST_PARAM_DELIM 
                + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_STANDFAKTOR)
                + ExcelService.getRowNum(startRownNum) + ")";
            formula = "IF(" + aufwand + " > 0" + ExcelService.CONST_PARAM_DELIM
                + formula
                + "/" + aufwand
                + ExcelService.CONST_PARAM_DELIM + "1)";
//                formula = "IF("
//                    + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_STANDFAKTOR)
//                    + ExcelService.getRowNum(startRownNum)
//                    + "=\"\"" + ExcelService.CONST_PARAM_DELIM + "1"
//                    + ExcelService.CONST_PARAM_DELIM + formula + ")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_STAND, formula,
                    this.exlSv.csFieldPlanungIstStand_Modul);

            // Standfaktor berechnen
            formula = ExcelService.genFormula("SUM", lstChildRowNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_STANDFAKTOR);
            formula =
                "IF(" + formula + " > 0" + ExcelService.CONST_PARAM_DELIM +
                formula + ExcelService.CONST_PARAM_DELIM + "\"\")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_STANDFAKTOR, formula,
                    this.exlSv.csFieldPlanungIstStandFaktor_Modul);

            //
            // Real
            //

            // Real-Plan:
            formula =
                ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_STAND)
                + ExcelService.getRowNum(startRownNum);
            String formula2 =
                ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_AUFWAND)
                + ExcelService.getRowNum(startRownNum);
            formula = "IF(" + formula + "> 0" + ExcelService.CONST_PARAM_DELIM
                + "1/" + formula + "*" + formula2
                + ExcelService.CONST_PARAM_DELIM + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND)
                + ExcelService.getRowNum(startRownNum) +  ")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_REAL_AUFWAND, formula,
                    this.exlSv.csFieldPlanungRealAufwand_Modul);

            // Real-Offen:
            formula =
                ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_REAL_AUFWAND)
                + ExcelService.getRowNum(startRownNum)
                + " - " + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_AUFWAND)
                + ExcelService.getRowNum(startRownNum);
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_REAL_OFFEN, formula,
                    this.exlSv.csFieldPlanungRealOffen_Modul);

            // Real-Diff:
            formula =
                ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_REAL_AUFWAND)
                + ExcelService.getRowNum(startRownNum)
                + " - " + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND)
                + ExcelService.getRowNum(startRownNum);
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_REAL_DIFF, formula,
                    this.exlSv.csFieldPlanungRealDiff_Modul);

            // Minimum-StartDatum berechnen
            formula = ExcelService.genFormula("MIN", lstChildRowNum,
                    ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_START);
            formula =
                "IF(" + formula + " > 0" + ExcelService.CONST_PARAM_DELIM +
                formula  + ExcelService.CONST_PARAM_DELIM + "\"\")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_START, formula,
                    this.exlSv.csFieldPlanungRealStartDate_Modul);

            // Maximum-EndDatum berechnen
            formula = ExcelService.genFormula("MAX", lstChildRowNum,
                    ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_ENDE);
            formula =
                "IF(" + formula + " > 0" + ExcelService.CONST_PARAM_DELIM
                + formula + ExcelService.CONST_PARAM_DELIM + "\"\")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_ENDE, formula,
                    this.exlSv.csFieldPlanungRealEndDate_Modul);
        } else if (projektNode != null) {
            // Unterpunkt: als Projektnode

            // Detail-Flag
            cell = ExcelService.setCellNumeric(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_FLG_DETAIL, new Double(1),
                    this.exlSv.csFieldPlanungStruktur_Modul);

            //
            // Plan
            //

            // Aufwand
            Double aufwand = null;
            if (projektNode.getPlanAufwand() != null) {
                aufwand = projektNode.getPlanAufwand().doubleValue();
            }
            cell = ExcelService.setCellNumeric(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND, aufwand,
                    this.exlSv.csFieldPlanungPlanAufwand_Entry);

            // StartDatum berechnen
            Date date = projektNode.getPlanStart();
            Date MINDATE = ParserImpl.DF.parse("01.01.1970");
            if ((date == null || date.before(MINDATE)) && vorgaengerRownNum >= 0) {
                // falls leer mit dem Ende des Vorgaengers belegen
                String formula = ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_ENDE) + ExcelService.getRowNum(vorgaengerRownNum);
                cell = ExcelService.setCellFormula(sheet, startRownNum,
                        ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START, formula,
                        this.exlSv.csFieldPlanungPlanStartDate_Entry);
            } else {
                // wenn belegt mit Wert belegen
                cell = ExcelService.setCellDate(sheet, startRownNum,
                        ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START, date,
                        this.exlSv.csFieldPlanungPlanStartDate_Entry);
            }

            // EndDatum berechnen
            date = projektNode.getPlanEnde();
            if (date == null) {
                // falls leer mit dem berechneten Endedatum belegen
                String formula = ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_MIN_ENDE) + ExcelService.getRowNum(startRownNum);
                cell = ExcelService.setCellFormula(sheet, startRownNum,
                        ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_ENDE, formula,
                        this.exlSv.csFieldPlanungPlanEndDate_Entry);
            } else {
                // wenn belegt mit Wert belegen
                cell = ExcelService.setCellDate(sheet, startRownNum,
                        ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_ENDE, date,
                        this.exlSv.csFieldPlanungPlanEndDate_Entry);
            }

            // Maximum-MinEndDatum berechnen
            // WENN(UND(K30>0;K30<>"";J30>0);   ARBEITSTAG(K30;J30/8)   + ((STUNDE(K30)+MINUTE(K30)/60)/3 + REST(J30;8))/8;"")
            String pStart = ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START) + ExcelService.getRowNum(startRownNum);
            String pAufwand = ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND) + ExcelService.getRowNum(startRownNum);
            int faktor = 24 / ExcelNodeService.CONST_WORKHOURS_PERDAY;
            String formula =
                "IF(AND("
                + pStart
                + "  >0" + ExcelService.CONST_PARAM_DELIM + pStart + "<>\"\""
                + ExcelService.CONST_PARAM_DELIM + pAufwand + "  >0)" + ExcelService.CONST_PARAM_DELIM

//                  + "  WORKDAY(" + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START) + ExcelService.getRowNum(startRownNum)
//                  + " ;" + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND) + ExcelService.getRowNum(startRownNum)
//                  + " /" + ExcelNodeService.CONST_WORKHOURS_PERDAY + ") ;\"\")"
                + " DATE(YEAR(" + pStart + ")" + ExcelService.CONST_PARAM_DELIM
                + "MONTH(" + pStart + ")" + ExcelService.CONST_PARAM_DELIM + "DAY(" + pStart + "))"
                + " + TRUNC(" + pAufwand + " /" + ExcelNodeService.CONST_WORKHOURS_PERDAY + ")"

                + " +(("
// HOUR in POI erst ab 3.8 (wg. Recalc)               + " +(((HOUR(" + pStart + ")+ MINUTE(" + pStart + ")/60)/" + faktor
                + " +  MOD(" + pAufwand  + ExcelService.CONST_PARAM_DELIM + ExcelNodeService.CONST_WORKHOURS_PERDAY + "))/" + ExcelNodeService.CONST_WORKHOURS_PERDAY
                + " )"

                + ExcelService.CONST_PARAM_DELIM + "\"\")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_MIN_ENDE, formula,
                    this.exlSv.csFieldPlanungPlanMinEndDate_Entry);

            //
            // Ist
            //

            // Aufwand
            aufwand = new Double(0);
            if (projektNode.getIstAufwand() != null) {
                aufwand = projektNode.getIstAufwand().doubleValue();
            }
            cell = ExcelService.setCellNumeric(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_AUFWAND, aufwand,
                    this.exlSv.csFieldPlanungIstAufwand_Entry);

            // StartDatum berechnen
            date = projektNode.getIstStart();
            cell = ExcelService.setCellDate(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_START, date,
                    this.exlSv.csFieldPlanungIstStartDate_Entry);

            // EndDatum berechnen
            date = projektNode.getIstEnde();
            cell = ExcelService.setCellDate(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_ENDE, date,
                    this.exlSv.csFieldPlanungIstEndDate_Entry);

            // Stand
            Double stand = new Double(0);
            if (projektNode.getIstStand() != null) {
                stand = projektNode.getIstStand().doubleValue() / 100;
            }
            cell = ExcelService.setCellNumeric(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_STAND, stand,
                    this.exlSv.csFieldPlanungIstStand_Entry);

            // Standfaktor berechnen (Standfaktor = Stand * Aufwand)
            formula =
                ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_STAND) +
                ExcelService.getRowNum(startRownNum)
                + "*" + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND)
                + ExcelService.getRowNum(startRownNum);;;
            formula =
                "IF(" + formula + " > 0" + ExcelService.CONST_PARAM_DELIM + formula
                + ExcelService.CONST_PARAM_DELIM + "\"\")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_IST_STANDFAKTOR, formula,
                    this.exlSv.csFieldPlanungIstStandFaktor_Entry);

            //
            // Real
            //

            // Real-Plan:
            formula =
                ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_STAND)
                + ExcelService.getRowNum(startRownNum);
            String formula2 =
                ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_AUFWAND)
                + ExcelService.getRowNum(startRownNum);
            formula = "IF(" + formula + "> 0" + ExcelService.CONST_PARAM_DELIM
                + "1/" + formula + "*" + formula2
                + ExcelService.CONST_PARAM_DELIM + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND)
                + ExcelService.getRowNum(startRownNum) +  ")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_REAL_AUFWAND, formula,
                    this.exlSv.csFieldPlanungRealAufwand_Entry);

            // Real-Offen:
            formula =
                ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_REAL_AUFWAND)
                + ExcelService.getRowNum(startRownNum)
                + " - " + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_AUFWAND)
                + ExcelService.getRowNum(startRownNum);
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_REAL_OFFEN, formula,
                    this.exlSv.csFieldPlanungRealOffen_Entry);

            // Real-Diff:
            formula =
                ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_REAL_AUFWAND)
                + ExcelService.getRowNum(startRownNum)
                + " - " + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND)
                + ExcelService.getRowNum(startRownNum);
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_REAL_DIFF, formula,
                    this.exlSv.csFieldPlanungRealDiff_Entry);

            // Real-Start:
            String element =
                ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_START)
                + ExcelService.getRowNum(startRownNum);
            String element2 =
                ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START)
                + ExcelService.getRowNum(startRownNum);
            formula =
                "IF(" + element + ">0" + ExcelService.CONST_PARAM_DELIM
                + element + ExcelService.CONST_PARAM_DELIM
                + "IF(" + element2 + ">0" + ExcelService.CONST_PARAM_DELIM
                + element2 + ExcelService.CONST_PARAM_DELIM + "\"\")" + ")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_START, formula,
                    this.exlSv.csFieldPlanungRealStartDate_Entry);

            // Real-Ende:
            // Maximum-MinEndDatum berechnen
            // WENN(UND(K30>0;K30<>"";J30>0);   ARBEITSTAG(K30;J30/8)   + ((STUNDE(K30)+MINUTE(K30)/60)/3 + REST(J30;8))/8;"")
            String pEnde = ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_ENDE) 
                            + ExcelService.getRowNum(startRownNum);
            String iEnde = "today()"; //ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_ENDE) + ExcelService.getRowNum(startRownNum);
            pAufwand = ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_REAL_OFFEN) 
                            + ExcelService.getRowNum(startRownNum);
            faktor = 24 / ExcelNodeService.CONST_WORKHOURS_PERDAY;
            formula =
                "IF(" + pStart + " > 0" + ExcelService.CONST_PARAM_DELIM
                + "IF(AND("
                + iEnde
                + "  >0" + ExcelService.CONST_PARAM_DELIM + iEnde + "<>\"\""
                + ExcelService.CONST_PARAM_DELIM + pAufwand + "  >0)" + ExcelService.CONST_PARAM_DELIM

//                  + "  WORKDAY(" + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START) + ExcelService.getRowNum(startRownNum)
//                  + " ;" + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND) + ExcelService.getRowNum(startRownNum)
//                  + " /" + ExcelNodeService.CONST_WORKHOURS_PERDAY + ") ;\"\")"
                + " DATE(YEAR(" + iEnde + ")" + ExcelService.CONST_PARAM_DELIM
                + "MONTH(" + iEnde + ")" + ExcelService.CONST_PARAM_DELIM + "DAY(" + iEnde + "))"
                + " + TRUNC(" + pAufwand + " /" + ExcelNodeService.CONST_WORKHOURS_PERDAY + ")"

                + " +(("
// HOUR in POI erst ab 3.8 (wg. Recalc)               + " +(((HOUR(" + iEnde + ")+ MINUTE(" + iEnde + ")/60)/" + faktor
                + " +  MOD(" + pAufwand  + ExcelService.CONST_PARAM_DELIM + ExcelNodeService.CONST_WORKHOURS_PERDAY + "))/" + ExcelNodeService.CONST_WORKHOURS_PERDAY
                + " )"
                + ExcelService.CONST_PARAM_DELIM + "\"\")"
                + ExcelService.CONST_PARAM_DELIM + pEnde + ")";
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_ENDE, formula,
                    this.exlSv.csFieldPlanungRealEndDate_Entry);
        }

        // Einblenden der Struktur-Spalten:
        // Von der aktuellen Position aus die Element einblenden
        BaseNode curNode = node;
        StringBuffer tmpBuffer = null;
        for (int curEbene = node.getEbene(); curEbene > 0; curEbene--) {
            // aktuelles Element in die Spalte=Ebene schreiben
            if (curEbene <= ExcelNodeService.CONST_PLANUNG_COL_SCHRITT5) {
                String name = curNode.getName();
                tmpBuffer = new StringBuffer();
                
                // calc DocLayout
                this.formatNodeDataDomain(curNode, 
                        this.getDataDomainFormatterByClassName(CONST_FORMATTER_DOCLAYOUT), 
                        tmpBuffer, oOptions);
                String docLayout = tmpBuffer.toString();
                if (docLayout != null && oOptions.isFlgShowDocLayout()) {
                    name += docLayout;
                }
                
                // set name
                cell = ExcelService.setCellString(sheet, startRownNum, curEbene, name);
            }
            curNode = curNode.getParentNode();
        }
        
        // Desc einblenden
        tmpBuffer = new StringBuffer();
        this.formatNodeDataDomain(node, 
                this.getDataDomainFormatterByClassName(CONST_FORMATTER_DESC), 
                tmpBuffer, oOptions);
        String descFull = tmpBuffer.toString();
        cell = ExcelService.setCellString(sheet, startRownNum, 
                ExcelNodeService.CONST_PLANUNG_COL_DESC, 
                descFull);
        

        // Style der Struktur setzen
        HSSFCellStyle cellStyle =
            getCellStyle4ModulOrEntry(lstChildRowNum,
                    this.exlSv.csFieldPlanungStruktur_Modul,
                    this.exlSv.csFieldPlanungStruktur_Entry);
        for (int curCol = 1; curCol <= ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND - 1; curCol++) {
            // aktuelles Element in die Spalte=Ebene schreiben
            cell = ExcelService.getCell(sheet, startRownNum, curCol);
            cell.setCellStyle(cellStyle);
        }

        // Taskdaten eintragen falls Projektnode
        if (projektNode != null) {
            cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_PLANUNG_COL_PLAN_TASK, projektNode.getPlanTask(),
                cellStyle);
            cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_PLANUNG_COL_IST_TASK, projektNode.getIstTask(),
                cellStyle);
        }

        // Prognose eintragen
        cellStyle =
                getCellStyle4ModulOrEntry(lstChildRowNum,
                    this.exlSv.csFieldPlanungPlanPrognose_Modul,
                    this.exlSv.csFieldPlanungPlanPrognose_Entry);
        Double prognose = new Double(1);
//            if (node.getPlanPrognose() != null) {
//                prognose = node.getPlanPrognose().doubleValue() / 100;
//            }
        cell = ExcelService.setCellNumeric(sheet, startRownNum,
                ExcelNodeService.CONST_PLANUNG_COL_PLAN_PROGNOSE, prognose,
                cellStyle);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     formats recursively nodes into workbook-sheet Gannt
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates wb - insert lines
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param wb - Workbook to fill
     * @param masterNode - node for output recursively
     * @param oOptions - options for output (formatter)
     * @throws Exception - possible Exception     */
    public void fillGantSheet(final HSSFWorkbook wb, final BaseNode masterNode,
            final ExcelOutputOptions oOptions)
    throws Exception {

        // ExcelService anlegen
        this.exlSv = new ExcelOutputService(wb);

        // Parameter pruefen
        if (wb == null) {
            throw new IllegalArgumentException("Workbook must not be null: '"
                    + wb + "'");
        }
        if (masterNode == null) {
            throw new IllegalArgumentException("Masternode must not be null: '"
                    + masterNode + "'");
        }

        // Betreffendes Sheet anhand des Indexes ermitteln (funkt. auch noch
        // wenn mal das Tabellenblatt umbenannt wird)
        HSSFSheet sheet = null;
        sheet = wb.getSheet(ExcelNodeService.CONST_SHEETNNAME_GANT);
        if (sheet == null) {
            sheet = wb.createSheet(ExcelNodeService.CONST_SHEETNNAME_GANT);
        }

        // alle SubNotes des Masters durchlaufen
        if (masterNode.getEbene() != 1) {
// TODO 
//            throw new IllegalArgumentException("Masternode ist not Master: "
//                    + masterNode.getEbene() + "'" + masterNode + "'");
        }

        int startRownNum = ExcelNodeService.CONST_GANT_ROUW_UE;
        this.createGantLineUe(sheet, startRownNum, oOptions);
        startRownNum++;
        int lastIndex = this.createGantLines4Node(sheet, masterNode,
                startRownNum, oOptions);

        // Blatt sch�tzen, damit Felder gesperrt
        //sheet.setProtect(true);

        // Projekt+ID Spalte ausblenden
        sheet.setColumnHidden(new Integer(ExcelNodeService.CONST_PLANUNG_COL_ID), true);
        sheet.setColumnHidden(new Integer(ExcelNodeService.CONST_PLANUNG_COL_PROJEKT), true);

        // Gesamten Sheet formatieren
        HSSFSheet sheetPlanung = wb.getSheet(ExcelNodeService.CONST_SHEETNNAME_PLANUNG);
        for (int curCol = 1; curCol < ExcelNodeService.CONST_GANT_COL_SCHRITT5; curCol++) {
            if (sheetPlanung != null) {
                // Breite aus Planungssheet nehmen
                sheet.setColumnWidth(curCol, sheetPlanung.getColumnWidth(curCol));
            } else {
                // Autosize
                sheet.autoSizeColumn(curCol);
            }
        }

        // Gant-Diagramm formatieren
        this.formatGantSheetDiag(sheet, lastIndex, oOptions, 0);

        // Druckanpassung
        sheet.setFitToPage(true);
        HSSFPrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
        printSetup.setUsePage(true);
        printSetup.setFitWidth((short) 1);
        printSetup.setScale((short) 50);
    }


    // TODO Doku
    public void formatGantSheetDiag(final HSSFSheet sheet, final int lastRownNum,
            final ExcelOutputOptions oOptions, final int versatz) throws Exception {
        // Parameter pruefen
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet must not be null: '"
                    + sheet + "'");
        }
        if (lastRownNum < 0) {
            throw new IllegalArgumentException("startRownNum must not be null: " + lastRownNum);
        }

        // Summenzeile
        for (int zaehler = 0; zaehler <= ExcelNodeService.CONST_GANT_PERIODS; zaehler++) {
            // WENN($F3<=H$2;WENN($G3>=H$2;"X";"");WENN($F3<I$2;WENN($G3>=H$2;"X";"");""))

            // Datumsbereiche
            String formula =
                "SUMIF("
                + ExcelNodeService.CONST_SHEETNNAME_PLANUNG + "!$" + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_FLG_DETAIL) + ExcelService.getRowNum(ExcelNodeService.CONST_GANT_ROUW_UE+1)
                + ":" + ExcelNodeService.CONST_SHEETNNAME_PLANUNG + "!$" + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_FLG_DETAIL) + ExcelService.getRowNum(lastRownNum)
                + ExcelService.CONST_PARAM_DELIM + "1" + ExcelService.CONST_PARAM_DELIM
                + "$" + ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+zaehler) + ExcelService.getRowNum(ExcelNodeService.CONST_GANT_ROUW_UE+1)
                + ":" + "$" + ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+zaehler) + ExcelService.getRowNum(lastRownNum)
                + ")";
            HSSFCell cell = ExcelService.setCellFormula(sheet, lastRownNum+1,
                    versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+zaehler, formula,
                    this.exlSv.csFieldGantNormDate_Entry);
        }

        // Bedingte Formartierung setzen
        HSSFSheetConditionalFormatting condFormSheet =
            sheet.getSheetConditionalFormatting();
        CellRangeAddress [] regions =
        {
                new CellRangeAddress(
                        ExcelNodeService.CONST_GANT_ROUW_UE+1,
                        lastRownNum,
                        versatz + ExcelNodeService.CONST_GANT_COL_GANT_START,
                        versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+ExcelNodeService.CONST_GANT_PERIODS)
        };

        // Rule+pattern wenn leer
        HSSFConditionalFormattingRule condFormRule = null;
        HSSFPatternFormatting patternFmt = null;

        condFormRule =
            condFormSheet.createConditionalFormattingRule(
                    ComparisonOperator.EQUAL,
                    "\"\"", // 1st formula
                    null // 2nd formula is not used for comparison operator GE
            );
        patternFmt =
            condFormRule.createPatternFormatting();

        // Rule+pattern wenn = X
        HSSFConditionalFormattingRule condFormRule2 =
            condFormSheet.createConditionalFormattingRule(
                    ComparisonOperator.EQUAL,
                    "\"X\"", // 1st formula
                    null // 2nd formula is not used for comparison operator GE
            );
        patternFmt =
            condFormRule2.createPatternFormatting();
        patternFmt.setFillBackgroundColor(HSSFColor.YELLOW.index);

        // Rule+pattern wenn > 0
        HSSFConditionalFormattingRule condFormRule3 =
            condFormSheet.createConditionalFormattingRule(
                    ComparisonOperator.GT,
                    "0", // 1st formula
                    null // 2nd formula is not used for comparison operator GE
            );
        patternFmt =
            condFormRule3.createPatternFormatting();
        patternFmt.setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);

        HSSFConditionalFormattingRule[] rules = {
                condFormRule,
                condFormRule2,
                condFormRule3
        };
        condFormSheet.addConditionalFormatting(regions, rules);

        // Gesamten Sheet formatieren
        sheet.setColumnWidth(versatz+ExcelNodeService.CONST_GANT_COL_PLAN_DATE_START, ExcelService.CONST_COL_WIDTH_DATE);
        sheet.setColumnWidth(versatz+ExcelNodeService.CONST_GANT_COL_PLAN_DATE_ENDE, ExcelService.CONST_COL_WIDTH_DATE);
        sheet.setColumnWidth(versatz+ExcelNodeService.CONST_GANT_COL_PLAN_PROGNOSE, ExcelService.CONST_COL_WIDTH_INT);
        sheet.setColumnWidth(versatz+ExcelNodeService.CONST_GANT_COL_PLAN_PROGNOSE_AUFWAND, ExcelService.CONST_COL_WIDTH_INT);
        for (int curCol = ExcelNodeService.CONST_GANT_COL_GANT_START; curCol <= ExcelNodeService.CONST_GANT_COL_GANT_START+ExcelNodeService.CONST_GANT_PERIODS;curCol++) {
            sheet.setColumnWidth(versatz+curCol, ExcelService.CONST_COL_WIDTH_DATE_SHORT);
        }
    }


    // TODO Doku
    public int createGantLines4Node(HSSFSheet sheet, BaseNode node,
            int startRownNum, ExcelOutputOptions oOptions)
    throws Exception {
        // Parameter pruefen
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet must not be null: '"
                    + sheet + "'");
        }
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null: '"
                    + node + "'");
        }
        if (startRownNum < 0) {
            throw new IllegalArgumentException("startRownNum must not be null: " + startRownNum
                    + " for '" + node + "'");
        }

        // alle KindElemente ab der Folgezeile anfuegen
        Map<String, DataDomain> childNodes = node.getChildNodesByNameMap();
        int curRowNum = startRownNum;
        List<Integer> lstChildRowNum = new ArrayList<Integer>();
        // nur Kindselemente einfuegen, wenn Ebene
        if (node.getEbene() <= oOptions.getMaxEbene()) {
            for (String nodeName : childNodes.keySet()) {
                BaseNode childNode = (BaseNode) childNodes.get(nodeName);
                // naechste Zeile
                curRowNum++;
                lstChildRowNum.add(new Integer(curRowNum));
                curRowNum = this.createGantLines4Node(sheet, childNode, curRowNum,
                        oOptions);
            }
        }

        // meine eigene Zeile einfuegen
        this.createGantLine4Node(sheet, node, startRownNum, lstChildRowNum, oOptions);

        return curRowNum;
    }


    // TODO Doku
    public void createGantLineUe(HSSFSheet sheet, int startRownNum,
            ExcelOutputOptions oOptions)
    throws Exception {
        // Parameter pruefen
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet must not be null: '"
                    + sheet + "'");
        }
        if (startRownNum < 0) {
            throw new IllegalArgumentException("Startindex must not be null: " + startRownNum
                    + " for Ue");
        }

        HSSFCell cell = ExcelService.setCellString(sheet, startRownNum, ExcelNodeService.CONST_GANT_COL_ID, "Id");
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_PROJEKT, "Projekt", this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_MODUL, "Modul",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_PAKET, "Paket",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_UNTERPAKET, "Unterpaket",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_SCHRITT, "Schritt",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_SCHRITT2, "Schritt2",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_SCHRITT3, "Schritt3",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_SCHRITT4, "Schritt4",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_SCHRITT5, "Schritt5",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_PLAN_PROGNOSE, "P-Prognose",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_PLAN_PROGNOSE_AUFWAND, "P-Aufwand",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_PLAN_DATE_START, "P-Start",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                ExcelNodeService.CONST_GANT_COL_PLAN_DATE_ENDE, "P-Ende",
                this.exlSv.csFieldGantBaseUe);

        // Gant-Diagramm-UE
        this.createGantLineDiagUe(sheet, startRownNum, oOptions, 0);
    }


    // TODO Doku
    public void createGantLineDiagUe(HSSFSheet sheet, int startRownNum,
            ExcelOutputOptions oOptions, int versatz)
    throws Exception {
        // Parameter pruefen
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet must not be null: '"
                    + sheet + "'");
        }
        if (startRownNum < 0) {
            throw new IllegalArgumentException("Startindex must not be null: " + startRownNum
                    + " for Ue");
        }

        HSSFCell cell = null;

        //Modus
        cell = ExcelService.setCellNumeric(sheet, startRownNum-1,
                versatz + ExcelNodeService.CONST_GANT_COL_PLAN_MODUS, new Double(1),
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum-1,
                versatz + ExcelNodeService.CONST_GANT_COL_PLAN_MODUS_NAME, "Modus (1=Plan, 2=Ist, 3=Offen, 4=Real)",
                this.exlSv.csFieldGantBaseUe);

        // Gant-Daten
        cell = ExcelService.setCellString(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_PLAN_PROGNOSE, "P-Prognose",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_PLAN_PROGNOSE_AUFWAND, "P-Aufwand",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_PLAN_DATE_START, "P-Start",
                this.exlSv.csFieldGantBaseUe);
        cell = ExcelService.setCellString(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_PLAN_DATE_ENDE, "P-Ende",
                this.exlSv.csFieldGantBaseUe);

        // Gant-Datumsgrenzen
        String formula =
            ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_PLAN_DATE_START)
            + ExcelService.getRowNum(startRownNum+1);
        cell = ExcelService.setCellFormula(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_GANT_START, formula,
                this.exlSv.csFieldGantNormDate_Ue);
        formula =
            ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_PLAN_DATE_ENDE)
            + ExcelService.getRowNum(startRownNum+1);
        cell = ExcelService.setCellFormula(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+ExcelNodeService.CONST_GANT_PERIODS, formula,
                this.exlSv.csFieldGantNormDate_Ue);

        for (int zaehler = 1; zaehler < ExcelNodeService.CONST_GANT_PERIODS; zaehler++) {
            // $H$2+($O$2-$H$2)/7*1
            formula =
                "$" + ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_GANT_START)
                + "$" + ExcelService.getRowNum(startRownNum)
                + "+($" + ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+ExcelNodeService.CONST_GANT_PERIODS)
                + "$" + ExcelService.getRowNum(startRownNum)
                + "-" + "$" + ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_GANT_START)
                + "$" + ExcelService.getRowNum(startRownNum)
                + ")/" + ExcelNodeService.CONST_GANT_PERIODS + "*" + zaehler;
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+zaehler, formula,
                    this.exlSv.csFieldGantNormDate_Ue);
        }

        // letzte Zeile
        int zaehler = ExcelNodeService.CONST_GANT_PERIODS+1;
        formula =
            "$" + ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_GANT_START)
            + "$" + ExcelService.getRowNum(startRownNum)
            + "+($" + ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+ExcelNodeService.CONST_GANT_PERIODS)
            + "$" + ExcelService.getRowNum(startRownNum)
            + "-" + "$" + ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_GANT_START)
            + "$" + ExcelService.getRowNum(startRownNum)
            + ")/" + ExcelNodeService.CONST_GANT_PERIODS + "*" + zaehler;
        cell = ExcelService.setCellFormula(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+zaehler, formula,
                this.exlSv.csFieldGantNormDate_Ue);
    }


    // TODO Doku
    public void createGantLine4Node(HSSFSheet sheet, BaseNode node,
            int startRownNum, List<Integer> lstChildRowNum,
            ExcelOutputOptions oOptions)
    throws Exception {
        // Parameter pruefen
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet must not be null: '"
                    + sheet + "'");
        }
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null: '"
                    + node + "'");
        }
        if (startRownNum < 0) {
            throw new IllegalArgumentException("Startindex must not be null: " + startRownNum
                    + " for '" + node + "'");
        }

        HSSFCell cell = null;
        String formula = null;

        // Struktur kopieren
        Map<Integer, Integer> mpStructCols = new HashMap<Integer, Integer>();
        mpStructCols.put(ExcelNodeService.CONST_GANT_COL_PROJEKT, ExcelNodeService.CONST_PLANUNG_COL_PROJEKT);
        mpStructCols.put(ExcelNodeService.CONST_GANT_COL_MODUL, ExcelNodeService.CONST_PLANUNG_COL_MODUL);
        mpStructCols.put(ExcelNodeService.CONST_GANT_COL_PAKET, ExcelNodeService.CONST_PLANUNG_COL_PAKET);
        mpStructCols.put(ExcelNodeService.CONST_GANT_COL_UNTERPAKET, ExcelNodeService.CONST_PLANUNG_COL_UNTERPAKET);
        mpStructCols.put(ExcelNodeService.CONST_GANT_COL_SCHRITT, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT);
        mpStructCols.put(ExcelNodeService.CONST_GANT_COL_SCHRITT2, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT2);
        mpStructCols.put(ExcelNodeService.CONST_GANT_COL_SCHRITT3, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT3);
        mpStructCols.put(ExcelNodeService.CONST_GANT_COL_SCHRITT4, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT4);
        mpStructCols.put(ExcelNodeService.CONST_GANT_COL_SCHRITT5, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT5);
        for (Iterator<Integer> iter = mpStructCols.keySet().iterator(); iter.hasNext();) {
            int colGant = (Integer) iter.next();
            int colPlanung = (Integer) mpStructCols.get(colGant);
            formula =
                ExcelService.genIfNotEmpty(ExcelNodeService.CONST_SHEETNNAME_PLANUNG,
                        startRownNum, colPlanung);
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    colGant, formula,
                    getCellStyle4ModulOrEntry(lstChildRowNum,
                            this.exlSv.csFieldGantStruktur_Modul,
                            this.exlSv.csFieldGantStruktur_Entry)
                    );
        }

        // den Rest der Gant-Line hinzufuegen
        this.createGantLineDiag4Node(sheet, node, startRownNum, lstChildRowNum, oOptions, 0);
    }


    // TODO Doku
    public void createGantLineDiag4Node(HSSFSheet sheet, BaseNode node,
            int startRownNum, List<Integer> lstChildRowNum,
            ExcelOutputOptions oOptions, int versatz)
    throws Exception {
        // Parameter pruefen
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet must not be null: '"
                    + sheet + "'");
        }
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null: '"
                    + node + "'");
        }
        if (startRownNum < 0) {
            throw new IllegalArgumentException("Startindex must not be null: " + startRownNum
                    + " for '" + node + "'");
        }

        HSSFCell cell = null;
        String formula = null;

        //
        // Prognose
        //
        formula =
            ExcelService.genIfNotEmpty(ExcelNodeService.CONST_SHEETNNAME_PLANUNG,
                    startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_PROGNOSE);
        cell = ExcelService.setCellFormula(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_PLAN_PROGNOSE, formula,
                getCellStyle4ModulOrEntry(lstChildRowNum,
                        this.exlSv.csFieldGantPlanPrognose_Modul,
                        this.exlSv.csFieldGantPlanPrognose_Entry)
                );

        //
        // Aufwand
        //
        HashMap<String, String> formValues = new HashMap<String, String>();
        String element2 =
            ExcelNodeService.CONST_SHEETNNAME_PLANUNG + "!"
            + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_PROGNOSE) + ExcelService.getRowNum(startRownNum);

        // Plandaten
        String element =
            ExcelNodeService.CONST_SHEETNNAME_PLANUNG + "!"
            + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND) + ExcelService.getRowNum(startRownNum);
        formValues.put("1",
                "IF(AND(" + element + ">0" + ExcelService.CONST_PARAM_DELIM
                + element + ">0)" + ExcelService.CONST_PARAM_DELIM
                + element + "*" + element2 + ExcelService.CONST_PARAM_DELIM + "\"\")");

        // Istdaten
        element =
            ExcelNodeService.CONST_SHEETNNAME_PLANUNG + "!"
            + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_AUFWAND) + ExcelService.getRowNum(startRownNum);
        formValues.put("2",
                "IF(AND(" + element + ">0" + ExcelService.CONST_PARAM_DELIM
                + element + ">0)" + ExcelService.CONST_PARAM_DELIM
                + element + "*" + element2 + ExcelService.CONST_PARAM_DELIM + "\"\")");

        // Real-Offen
        element =
            ExcelNodeService.CONST_SHEETNNAME_PLANUNG + "!"
            + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_REAL_OFFEN) + ExcelService.getRowNum(startRownNum);
        formValues.put("3",
                "IF(AND(" + element + ">0" + ExcelService.CONST_PARAM_DELIM
                + element + ">0)" + ExcelService.CONST_PARAM_DELIM
                + element + "*" + element2 + ExcelService.CONST_PARAM_DELIM + "\"\")");

        // Real-Gesamt
        element =
            "("  + ExcelNodeService.CONST_SHEETNNAME_PLANUNG + "!"
            + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_IST_AUFWAND) + ExcelService.getRowNum(startRownNum)
            + " + " + ExcelNodeService.CONST_SHEETNNAME_PLANUNG + "!"
            + ExcelService.getColName(ExcelNodeService.CONST_PLANUNG_COL_REAL_OFFEN) + ExcelService.getRowNum(startRownNum)
            +")";
        formValues.put("4",
                "IF(AND(" + element + ">0" + ExcelService.CONST_PARAM_DELIM
                + element + ">0)" + ExcelService.CONST_PARAM_DELIM
                + element + "*" + element2 + ExcelService.CONST_PARAM_DELIM + "\"\")");

        formula =
            ExcelService.genCase4Values(null, ExcelNodeService.CONST_GANT_ROW_PLAN_MODUS,
                    versatz + ExcelNodeService.CONST_GANT_COL_PLAN_MODUS, formValues);
        cell = ExcelService.setCellFormula(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_PLAN_PROGNOSE_AUFWAND, formula,
                getCellStyle4ModulOrEntry(lstChildRowNum,
                        this.exlSv.csFieldGantPlanAufwand_Modul,
                        this.exlSv.csFieldGantPlanAufwand_Entry)
                );


        //
        // Start
        //

        // Plandaten
        formValues.put("1", ExcelService.genIfNotEmpty(ExcelNodeService.CONST_SHEETNNAME_PLANUNG,
                startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START));

        // Istdaten
        formValues.put("2", ExcelService.genIfNotEmpty(ExcelNodeService.CONST_SHEETNNAME_PLANUNG,
                startRownNum, ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_START));

        // Real-Offen
        formValues.put("3", ExcelService.genIfNotEmpty(ExcelNodeService.CONST_SHEETNNAME_PLANUNG,
                startRownNum, ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_ENDE));

        // Real-Gesamt
        formValues.put("4", ExcelService.genIfNotEmpty(ExcelNodeService.CONST_SHEETNNAME_PLANUNG,
                startRownNum, ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_START));

        formula =
            ExcelService.genCase4Values(null, ExcelNodeService.CONST_GANT_ROW_PLAN_MODUS,
                    versatz + ExcelNodeService.CONST_GANT_COL_PLAN_MODUS, formValues);
        cell = ExcelService.setCellFormula(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_PLAN_DATE_START, formula,
                getCellStyle4ModulOrEntry(lstChildRowNum,
                        this.exlSv.csFieldGantPlanStartDate_Modul,
                        this.exlSv.csFieldGantPlanStartDate_Entry)
                );


        //
        // Ende
        //

        // Plandaten
        formValues.put("1", ExcelService.genIfNotEmpty(ExcelNodeService.CONST_SHEETNNAME_PLANUNG,
                startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_ENDE));

        // Istdaten
        formValues.put("2", ExcelService.genIfNotEmpty(ExcelNodeService.CONST_SHEETNNAME_PLANUNG,
                startRownNum, ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_ENDE));

        // Real-Offen
        formValues.put("3", ExcelService.genIfNotEmpty(ExcelNodeService.CONST_SHEETNNAME_PLANUNG,
                startRownNum, ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_ENDE));

        // Real-Gesamt
        formValues.put("4", ExcelService.genIfNotEmpty(ExcelNodeService.CONST_SHEETNNAME_PLANUNG,
                startRownNum, ExcelNodeService.CONST_PLANUNG_COL_REAL_DATE_ENDE));

        formula =
            ExcelService.genCase4Values(null, ExcelNodeService.CONST_GANT_ROW_PLAN_MODUS,
                    versatz + ExcelNodeService.CONST_GANT_COL_PLAN_MODUS, formValues);
        cell = ExcelService.setCellFormula(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_PLAN_DATE_ENDE, formula,
                getCellStyle4ModulOrEntry(lstChildRowNum,
                        this.exlSv.csFieldGantPlanEndDate_Modul,
                        this.exlSv.csFieldGantPlanEndDate_Entry)
                );

        for (int zaehler = 0; zaehler <= ExcelNodeService.CONST_GANT_PERIODS; zaehler++) {
            // WENN($F3<=H$2;WENN($G3>=H$2;"X";"");WENN($F3<I$2;WENN($G3>=H$2;"X";"");""))

            // Datumsbereiche
            String pStart = "$" + ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_PLAN_DATE_START) + ExcelService.getRowNum(startRownNum);
            String pEnde = "$" + ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_PLAN_DATE_ENDE) + ExcelService.getRowNum(startRownNum);
            String pAufwand = "$" + ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_PLAN_PROGNOSE_AUFWAND) + ExcelService.getRowNum(startRownNum);

            //1. Anzahl der h innerhalb des Zeitraums berechen (Nettoarbeitstage
            String pAufwandPerDay = pAufwand + "/" +"(" + pEnde + "-" + pStart + "+1)";

            String curStart = ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+zaehler) + "$" + ExcelService.getRowNum(ExcelNodeService.CONST_GANT_ROUW_UE);
            String nextStart = ExcelService.getColName(versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+zaehler+1) + "$" + ExcelService.getRowNum(ExcelNodeService.CONST_GANT_ROUW_UE);

            if (lstChildRowNum.size() > 0 && node.getEbene() <= oOptions.getMaxEbene()) {
                // Oberpunkt
                formula =
                    "IF(" + pStart + "<=" + curStart + ExcelService.CONST_PARAM_DELIM
                    + " IF(" + pEnde + " >=" + curStart  + ExcelService.CONST_PARAM_DELIM + "\"X\""
                    + ExcelService.CONST_PARAM_DELIM + "\"\")"+ ExcelService.CONST_PARAM_DELIM
                    + " IF(" + pStart + " <" + nextStart + ExcelService.CONST_PARAM_DELIM
                    + "  IF(" + pEnde+ "  >=" + curStart  + ExcelService.CONST_PARAM_DELIM + "\"X\""
                    + ExcelService.CONST_PARAM_DELIM + "\"\")"
                    + ExcelService.CONST_PARAM_DELIM + "\"\")"
                    + ")";
            } else {
                //Detailpunkt

                // PStart+PEnde innerhalb des aktuellen Intervalls PStart>=curStart&&PEnde<NextStart: PEnde-PStart+1
                // PStart innerhalb des aktuellen Intervall, PEnde danach PStart>=curStart & PStart<NextStart & PEnde>=NextStart NextStart-PStart
                // PStart vor dem aktuellen Intervall, PEnde danach PStart<=curStart & PEnde>=NextStart: NextStart-curStart+1
                // PStart vor dem aktuellen Intervall, PEnde innerhalb PStart<=curStart & PEnde<NextStart & PEnde >=curStart: PEnde-curStart+1
                formula =
                    "IF(AND(" + pStart + ">=" + curStart + ExcelService.CONST_PARAM_DELIM + pEnde+ "<" + nextStart + ")" + ExcelService.CONST_PARAM_DELIM
                    + " (" + pEnde + "-" + pStart + "+1)*" + pAufwandPerDay
                    + ExcelService.CONST_PARAM_DELIM + "IF(AND(" + pStart + ">=" + curStart  + ExcelService.CONST_PARAM_DELIM + pStart + "<" + nextStart +  ExcelService.CONST_PARAM_DELIM + pEnde+ ">=" + nextStart + ")" + ExcelService.CONST_PARAM_DELIM
                    + "  (" + nextStart + "-" + pStart + ")*" + pAufwandPerDay
                    + ExcelService.CONST_PARAM_DELIM + "  IF(AND(" + pStart + "<=" + curStart  + ExcelService.CONST_PARAM_DELIM + pEnde + ">=" + nextStart + ")" + ExcelService.CONST_PARAM_DELIM
                    + "   (" + nextStart + "-" + curStart + ")*" + pAufwandPerDay
                    + ExcelService.CONST_PARAM_DELIM + "   IF(AND(" + pStart + "<=" + curStart  + ExcelService.CONST_PARAM_DELIM + pEnde + "<" + nextStart + ExcelService.CONST_PARAM_DELIM + pEnde + ">=" + curStart+ ")" + ExcelService.CONST_PARAM_DELIM
                    + "    (" + pEnde + "-" + curStart + "+1)*" + pAufwandPerDay
                    + ExcelService.CONST_PARAM_DELIM + "    \"\")"
                    + "  )"
                    + " )"
                    + ")";
//                    if (zaehler == ExcelNodeService.CONST_GANT_PERIODS) {
//                        formula = "IF(" + pEnde + ">=" + curStart + ExcelService.CONST_PARAM_DELIM + "1*" + pAufwandPerDay + ExcelService.CONST_PARAM_DELIM + "\"\")";
//                    }
                formula =
                    "IF(AND(" + pAufwand + ">0" + ExcelService.CONST_PARAM_DELIM + pAufwand  + "<>\"\"" + ExcelService.CONST_PARAM_DELIM
                    + pStart + ">0" + ExcelService.CONST_PARAM_DELIM + pStart  + "<>\"\"" + ExcelService.CONST_PARAM_DELIM
                    + pEnde + ">0" + ExcelService.CONST_PARAM_DELIM + pEnde  + "<>\"\")" + ExcelService.CONST_PARAM_DELIM
                    + formula  + ExcelService.CONST_PARAM_DELIM + "\"\")";
            }
            if (LOGGER.isDebugEnabled()) 
                LOGGER.debug("Row:" + startRownNum + " Col:" + (versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+zaehler) + " = " + formula);
            cell = ExcelService.setCellFormula(sheet, startRownNum,
                    versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+zaehler, formula,
                    getCellStyle4ModulOrEntry(lstChildRowNum,
                            this.exlSv.csFieldGantNormDate_Modul,
                            this.exlSv.csFieldGantNormDate_Entry)
                    );
        }
        cell = ExcelService.getCell(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_GANT_START);
        cell.setCellStyle(
                getCellStyle4ModulOrEntry(lstChildRowNum,
                        this.exlSv.csFieldGantStartDate_Modul,
                        this.exlSv.csFieldGantStartDate_Entry)
        );
        cell = ExcelService.getCell(sheet, startRownNum,
                versatz + ExcelNodeService.CONST_GANT_COL_GANT_START+ExcelNodeService.CONST_GANT_PERIODS);
        cell.setCellStyle(
                getCellStyle4ModulOrEntry(lstChildRowNum,
                        this.exlSv.csFieldGantEndDate_Modul,
                        this.exlSv.csFieldGantEndDate_Entry)
        );
    }

    // TODO Doku
    public HSSFWorkbook toExcel(BaseNode masterNode,
            ExcelOutputOptions oOptions) throws Exception {

        // Parameter pruefen
        if (masterNode == null) {
            throw new IllegalArgumentException("Masternode must not be null: '" + masterNode + "'");
        }

        // WorkBook erzeugen
//            File projektMasterFile = new File(FILEPATH_PLANUNGSMASTER);
//            InputStream is = new FileInputStream(projektMasterFile);
//            HSSFWorkbook wb = new HSSFWorkbook(is);
        HSSFWorkbook wb = new HSSFWorkbook();
        
        oOptions = genOutputOptionsForExcel(oOptions);

        // Mastenrode falls leer l�schen
        Set<BaseNode> masterChilds = masterNode.getChildNodes();
        while (masterChilds.size() == 1) {
            masterNode = (BaseNode) masterChilds.toArray()[0];
            masterNode.setParentNode(null);
            masterChilds = masterNode.getChildNodes();
        }

        // PlanungsSheet anfuegen
        this.fillPlanungSheet(wb, masterNode, oOptions);

        // GantSheet nur anfuegen, wenn nicht gemerged
        if (! oOptions.isFlgMergeExcelPlanungGantSheets())
            this.fillGantSheet(wb, masterNode, oOptions);

        return wb;
    }

    // TODO Doku
    public void toExcel(BaseNode masterNode, String outFile,
            ExcelOutputOptions oOptions) throws Exception {

        // Parameter pruefen
        if (outFile == null) {
            throw new IllegalArgumentException("Outfile must not be null: '" + outFile + "'");
        }

        // WorkBook erzeugen
        HSSFWorkbook wb = this.toExcel(masterNode, oOptions);

        File file = new File(outFile);
        ExcelService.writeWorkbookToFile(file, wb);
    }

    // TODO Doku
    public HSSFCellStyle getCellStyle4ModulOrEntry(List<Integer> children, 
            HSSFCellStyle styleModul, HSSFCellStyle styleEntry) {
        if (children != null && children.size() > 0)
            return styleModul;
        else
            return styleEntry;
    }

}
