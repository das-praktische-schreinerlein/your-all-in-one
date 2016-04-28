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
package de.yaio.extension.datatransfer.excel;

import de.yaio.commons.data.DataUtils;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.TaskNode;
import de.yaio.core.nodeservice.BaseNodeService;
import de.yaio.core.nodeservice.TaskNodeService;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.ImportOptionsImpl;
import de.yaio.datatransfer.importer.ImporterImpl;
import de.yaio.extension.datatransfer.ppl.PPLExporter;
import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.utils.ExcelService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/** 
 * import of Nodes from Excel
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.extension.datatransfer.excel
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ExcelImporter extends ImporterImpl {

    protected static Map<String, String> hshNodes = new HashMap<String, String>();

    public PPLImporter importer = new PPLImporter(new ImportOptionsImpl());
    public PPLExporter exporter = new PPLExporter();

    protected ExcelOutputService exlSv = null;

    public ExcelImporter(final ImportOptions options) {
        super(options);
    }

    public List<String> fromExcel(final String inFileName) throws Exception {

        // Parameter pruefen
        if (inFileName == null) {
            throw new IllegalArgumentException("Infile must not be null: '" + inFileName + "'");
        }

        // WorkBook erzeugen
        File inFile = new File(inFileName);
        InputStream is = new FileInputStream(inFile);
        HSSFWorkbook wb = new HSSFWorkbook(is);

        List<String> lines = this.parsePlanungSheet(wb);

        return lines;
    }

    public List<String> parsePlanungSheet(final HSSFWorkbook wb)
                    throws Exception {

        // ExcelService anlegen
        this.exlSv = new ExcelOutputService(wb);

        // Parameter pruefen
        if (wb == null) {
            throw new IllegalArgumentException("Workbook must not be null: '"
                    + wb + "'");
        }

        List<String> lines = new ArrayList<String>();

        // Betreffendes Sheet anhand des Indexes ermitteln (funkt. auch noch
        // wenn mal das Tabellenblatt umbenannt wird)
        HSSFSheet sheet = null;
        sheet = wb.getSheet(ExcelNodeService.CONST_SHEETNNAME_PLANUNG);
        if (sheet == null) {
            sheet = wb.createSheet(ExcelNodeService.CONST_SHEETNNAME_PLANUNG);
        }

        // alle Formeln berechnen
        @SuppressWarnings("deprecation")
        HSSFFormulaEvaluator formulaEval = new HSSFFormulaEvaluator(sheet, wb);
        int startRownNum = ExcelNodeService.CONST_PLANUNG_ROUW_UE;
        // Ue;
        startRownNum++;
        for (int rowNum = startRownNum; rowNum <= sheet.getLastRowNum(); rowNum++) {
            String line = this.parsePlanungLine(sheet, formulaEval, rowNum);
            if (line != null) {
                lines.add(line);
            }
        }

        return lines;
    }

    public String parsePlanungLine(final HSSFSheet sheet, final HSSFFormulaEvaluator formulaEval,
            final int startRownNum) throws Exception {
        Date MINDATE = DataUtils.getDF().parse("01.01.1970");

        // auf Bereich pruefen
        if (startRownNum > sheet.getLastRowNum()) {
            return null;
        }

        // Zeile einlesen
        String Id =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_ID).
            getRichStringCellValue().getString();
        String projekt =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PROJEKT)
            .getRichStringCellValue().getString();
        String modul =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_MODUL)
            .getRichStringCellValue().getString();
        String paket =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PAKET)
            .getRichStringCellValue().getString();
        String unterpaket =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_UNTERPAKET)
            .getRichStringCellValue().getString();
        String schritt =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT)
            .getRichStringCellValue().getString();
        String schritt2 =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT2)
            .getRichStringCellValue().getString();
        String schritt3 =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT3)
            .getRichStringCellValue().getString();
        String schritt4 =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT4)
            .getRichStringCellValue().getString();
        String schritt5 =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_SCHRITT5)
            .getRichStringCellValue().getString();
        String idesc =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_DESC)
            .getRichStringCellValue().getString();

        // Namen extrahieren
       List<Object> ebenen = new ArrayList<Object>();
        if (projekt == null || projekt.length() == 0) {
            // kein Projekt: nichts zu tun
            return null;
        } else {
            // alles parsen
            ebenen.add(projekt);
            if (modul != null && modul.length() > 0) {
                ebenen.add(modul);
                if (paket != null && paket.length() > 0) {
                    ebenen.add(paket);
                    if (unterpaket != null && unterpaket.length() > 0) {
                        ebenen.add(unterpaket);
                        if (schritt != null && schritt.length() > 0) {
                            ebenen.add(schritt);
                            if (schritt2 != null && schritt2.length() > 0) {
                                ebenen.add(schritt2);
                                if (schritt3 != null && schritt3.length() > 0) {
                                    ebenen.add(schritt3);
                                    if (schritt4 != null && schritt4.length() > 0) {
                                        ebenen.add(schritt4);
                                        if (schritt5 != null && schritt5.length() > 0) {
                                            ebenen.add(schritt5);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Plan
        String ptask =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_TASK)
            .getRichStringCellValue().getString();

        HSSFCell cell = null;
        cell = ExcelService.getCellEvaluated(sheet, formulaEval, startRownNum, 
                        ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND);
//        cell = ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_AUFWAND);
        Double paufwand = ExcelService.getCellNummeric(cell);

        cell = ExcelService.getCellEvaluated(sheet, formulaEval, startRownNum, 
                        ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START);
//        cell = ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_START);
        Date pstart = ExcelService.getCellDate(cell);
        if (pstart == null || pstart.before(MINDATE)) {
            pstart = null;
        }

        cell = ExcelService.getCellEvaluated(sheet, formulaEval, startRownNum, 
                        ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_ENDE);
//        cell = ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_PLAN_DATE_ENDE);
        Date pende = ExcelService.getCellDate(cell);
        if (pende == null || pende.before(MINDATE)) {
            pende = null;
        }

        // Ist
        String itask =
            ExcelService.getCell(sheet, startRownNum, ExcelNodeService.CONST_PLANUNG_COL_IST_TASK)
            .getRichStringCellValue().getString();

        cell = ExcelService.getCellEvaluated(sheet, formulaEval, startRownNum, 
                        ExcelNodeService.CONST_PLANUNG_COL_IST_STAND);
        double istand =  ExcelService.getCellNummeric(cell) * 100;

        cell = ExcelService.getCellEvaluated(sheet, formulaEval, startRownNum, 
                        ExcelNodeService.CONST_PLANUNG_COL_IST_AUFWAND);
        double iaufwand = ExcelService.getCellNummeric(cell);

        cell = ExcelService.getCellEvaluated(sheet, formulaEval, startRownNum, 
                        ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_START);
        Date istart = ExcelService.getCellDate(cell);
        if (istart == null || istart.before(MINDATE)) {
            istart = null;
        }

        cell = ExcelService.getCellEvaluated(sheet, formulaEval, startRownNum, 
                        ExcelNodeService.CONST_PLANUNG_COL_IST_DATE_ENDE);
        Date iende = ExcelService.getCellDate(cell);
        if (iende == null || iende.before(MINDATE)) {
            iende = null;
        }

        // Real
        cell = ExcelService.getCellEvaluated(sheet, formulaEval, startRownNum, 
                        ExcelNodeService.CONST_PLANUNG_COL_REAL_AUFWAND);
        double raufwand = ExcelService.getCellNummeric(cell);

        cell = ExcelService.getCellEvaluated(sheet, formulaEval, startRownNum, 
                        ExcelNodeService.CONST_PLANUNG_COL_REAL_OFFEN);
        double roffen = ExcelService.getCellNummeric(cell);

        cell = ExcelService.getCellEvaluated(sheet, formulaEval, startRownNum, 
                        ExcelNodeService.CONST_PLANUNG_COL_REAL_DIFF);
        double rdiff = ExcelService.getCellNummeric(cell);

        // Status aus den Planzahlen extrahieren
        String status = BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN;
        if (paufwand != null && (paufwand > 0)) {
            status = TaskNodeService.CONST_NODETYPE_IDENTIFIER_OPEN;
            if ((iaufwand > 0) || (istand > 0)) {
                status = TaskNodeService.CONST_NODETYPE_IDENTIFIER_RUNNNING;
                if (roffen == 0) {
                    status = TaskNodeService.CONST_NODETYPE_IDENTIFIER_DONE;
                } else if (rdiff > 0) {
                    status = TaskNodeService.CONST_NODETYPE_IDENTIFIER_SHORT;
                }
            }
        } else {
            // falls keine Planzahlen aus IstStand
            if (istand == 100) {
                status = TaskNodeService.CONST_NODETYPE_IDENTIFIER_DONE;
            } else if (istand > 0) {
                status = TaskNodeService.CONST_NODETYPE_IDENTIFIER_RUNNNING;
            }
        }

        // letztes Element der Hirarchy extrahieren: den Namen
        String curName = (String) ebenen.remove(ebenen.size() - 1);
        String fullName = status + " - " + curName;

        // create Hirarchy
        String hirarchyStr = "";
        String nodeSrc = "";
        String nodeRes = "";
        for (Iterator<Object> iter = ebenen.iterator(); iter.hasNext();) {
            nodeRes = iter.next().toString();
            nodeSrc = nodeRes;
            if (hshNodes.containsKey(nodeSrc)) {
                nodeRes = hshNodes.get(nodeSrc);
            }
            hirarchyStr += nodeRes + "\t";
        }
        
        // Projektnode erzeugen
        TaskNode node = (TaskNode) this.getNodeFactory().createNodeObjFromText(
                TaskNode.class, ExcelImporter.curId++, 
                hirarchyStr + fullName, fullName, null);
        node.setPlanAufwand(paufwand);
        node.setPlanStart(pstart);
        node.setPlanEnde(pende);
        node.setPlanTask(ptask);
        node.setIstAufwand(iaufwand);
        node.setIstStand(istand);
        node.setIstStart(istart);
        node.setIstEnde(iende);
        node.setIstTask(itask);
        if (idesc != null) {
            idesc = idesc.replaceAll("\n", "<WLBR>");
            idesc = idesc.replaceAll("\t", "<WLTAB>");
        }
        node.setNodeDesc(idesc);
        
        // validate node
        Set<ConstraintViolation<BaseNode>> violations = node.validateMe();
        if (violations.size() > 0) {
            ConstraintViolationException ex = new ConstraintViolationException(
                            "error while validating newNode" + node.getNameForLogger(), 
                            new HashSet<ConstraintViolation<?>>(violations));
            throw ex;
        }

        // Ausgabetext
        StringBuffer tmpBuffer = new StringBuffer();
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setFlgReEscapeDesc(false);
        this.exporter.formatNodeDataDomains(node, tmpBuffer, oOptions);
        
        // save nodeResult to map and return PPL-string
        nodeRes = tmpBuffer.toString();
        nodeSrc = curName;
        hshNodes.put(nodeSrc, nodeRes);

        return hirarchyStr + nodeRes;
    }
}
