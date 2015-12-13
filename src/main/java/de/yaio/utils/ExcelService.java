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
package de.yaio.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellValue;


public class ExcelService {

    public static final String CONST_PARAM_DELIM = ",";

    public static final String CONST_CS_DATEFORM = "ddd dd.mm.yyyy";
    public static final String CONST_CS_GANT_DATEFORM = "dd.mm";
    public static final String CONST_CS_PERCENT = "0%";
    public static final String CONST_CS_AUFWAND = "0.0";

    public static final int CONST_COL_WIDTH_INT = 8 * 256;
    public static final int CONST_COL_WIDTH_DATE = 15 * 256;
    public static final int CONST_COL_WIDTH_DATE_SHORT = 6 * 256;


    public ExcelService(final HSSFWorkbook workbook) {

    }

    /**
     * Schreibt den Excel-Bericht <tt>wb</tt> in die Datei <tt>file</tt>.
     *
     * @param file                   
     * Datei, in welche der Bericht geschrieben werden soll.
     *
     * @param wb                     
     * Excel-Bericht als HSSFWorkbook-Objekt.
     *
     * @throws IOException           
     */
    public static void writeWorkbookToFile(final File file, final HSSFWorkbook wb)
            throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        try {
            wb.write(out);
        } finally {
            out.close();
        }
    }

    /**
     * Hilfsmethode, welche eine Excel-Zelle eines bestimmen Tabellenblattes
     * zur�ckgibt. Wenn die Zelle noch nicht exisitiert bzw. def. ist, wird
     * sie neu erzeugt.
     *
     * @param sheet                  
     * HSSFSheet-Objekt (Tabellenblatt)
     *
     * @param rownum                 
     * Zeilennummer
     *
     * @param cellnum                
     * Spaltennummer
     *
     * @return
     * HSSFCell-Objekt, welches eine Excel-Zelle repr�sentiert
     */
    @SuppressWarnings("deprecation")
    public static HSSFCell getCell(
            final HSSFSheet sheet, final int rownum, final int cellnum) {
        HSSFRow row = sheet.getRow(rownum);
        if (row == null) {
            row = sheet.createRow(rownum);
        }
        HSSFCell cell = row.getCell((short) cellnum);
        if (cell == null) {
            cell = row.createCell((short) cellnum);
        }
        return cell;
    }

    @SuppressWarnings("deprecation")
    public static HSSFCell getCellEvaluated(
            final HSSFSheet sheet, final HSSFFormulaEvaluator formulaEval, final int rownum, final int cellnum) {
        HSSFCell cell = getCell(sheet, rownum, cellnum);
        if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
            // berechnen
            formulaEval.setCurrentRow(sheet.getRow(rownum));
            // System.err.println("Parse Zeile:" + rownum + " Col" + cellnum + " Form:" + cell.getCellFormula());


            CellValue cellValue = formulaEval.evaluate(cell);

            // neu belegen
            if (cellValue.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                double value = cellValue.getNumberValue();
                setCellNumeric(sheet, rownum, cellnum, value);
            } else if (cellValue.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                String value = cellValue.getStringValue().toString();
                // System.err.println("Value:'" + value + "'" + " lenght:" + value.length());
                if (value != null && !"".equals(value) && value.length() > 0) {
                    setCellString(sheet, rownum, cellnum, value);
                }
            }
        }
        return cell;
    }


    public static HSSFCell setCellNumeric(
            final HSSFSheet sheet, final int rownum, final int cellnum, final Double value) {

        final HSSFCell cell = getCell(sheet, rownum, cellnum);

        if (value == null) {
            setCellEmpty(sheet, rownum, cellnum);
        } else {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(value);
        }

        return cell;
    }

    public static HSSFCell setCellNumeric(
            final HSSFSheet sheet, final int rownum, final int cellnum, final Double value, final HSSFCellStyle style) {

        final HSSFCell cell = setCellNumeric(sheet, rownum, cellnum, value);
        cell.setCellStyle(style);

        return cell;
    }

    public static Double getCellNummeric(final HSSFCell cell) {

        if (cell.getCellType() != HSSFCell.CELL_TYPE_NUMERIC) {
            return new Double(0);
        }

        return cell.getNumericCellValue();
    }


    public static HSSFCell setCellEmpty(
            final HSSFSheet sheet, final int rownum, final int cellnum) {

        final HSSFCell cell = getCell(sheet, rownum, cellnum);
        cell.setCellType(HSSFCell.CELL_TYPE_BLANK);

        return cell;
    }

    public static HSSFCell setCellEmpty(
            final HSSFSheet sheet, final int rownum, final int cellnum    , final HSSFCellStyle style) {

        final HSSFCell cell = setCellEmpty(sheet, rownum, cellnum);
        cell.setCellStyle(style);

        return cell;
    }


    public static HSSFCell setCellDate(
            final HSSFSheet sheet, final int rownum, final int cellnum, final Date value) {

        final HSSFCell cell = getCell(sheet, rownum, cellnum);

        if (value == null) {
            setCellEmpty(sheet, rownum, cellnum);
        } else {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(value);
        }

        return cell;
    }

    public static HSSFCell setCellDate(
            final HSSFSheet sheet, final int rownum, final int cellnum, final Date value, final HSSFCellStyle style) {

        final HSSFCell cell = setCellDate(sheet, rownum, cellnum, value);
        cell.setCellStyle(style);

        return cell;
    }


    public static Date getCellDate(final HSSFCell cell) {

        if (cell.getCellType() != HSSFCell.CELL_TYPE_NUMERIC) {
            return null;
        }

        return cell.getDateCellValue();
    }

    /**
     * Hilfsmethode zum Setzen eines String-Wertes.
     *
     * @param sheet                  
     * HSSFSheet-Objekt (Tabellenblatt)
     *
     * @param rownum                 
     * Zeilennummer
     *
     * @param cellnum                
     * Spaltennummer
     *
     * @param value                  
     * String-Wert, der gesetzt werden soll
     * @return                       the new cell
     */
    public static HSSFCell setCellString(
            final HSSFSheet sheet, final int rownum, final int cellnum, final String value) {

        final HSSFCell cell = getCell(sheet, rownum, cellnum);

        // Typ setzen, falls Formel �berschrieben wird
        if (value == null) {
            setCellEmpty(sheet, rownum, cellnum);
        } else {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(new HSSFRichTextString(value));
        }
        return cell;
    }

    public static HSSFCell setCellString(
            final HSSFSheet sheet, final int rownum, final int cellnum, final String value, final HSSFCellStyle style) {

        final HSSFCell cell = setCellString(sheet, rownum, cellnum, value);
        cell.setCellStyle(style);

        return cell;
    }

    public static HSSFCell setCellFormula(
            final HSSFSheet sheet, final int rownum, final int cellnum, final String value) {

        final HSSFCell cell = getCell(sheet, rownum, cellnum);
        // Typ setzen, falls Formel �berschrieben wird
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        cell.setCellFormula(value);

        return cell;
    }

    public static HSSFCell setCellFormula(
            final HSSFSheet sheet, final int rownum, final int cellnum, final String value, final HSSFCellStyle style) {

        final HSSFCell cell = setCellFormula(sheet, rownum, cellnum, value);
        cell.setCellStyle(style);

        return cell;
    }


    public static String getColName(final int pCellnum) {
        String res = "";
        int cellnum = pCellnum;
        if (cellnum >= 26 + 26) {
            //System.err.println(" value: " + cellnum + " statt " 
            //                   + (char)(65 + cellnum) + " -> A" + (char)(65 + cellnum-26));
            cellnum = cellnum - 26 + 26;
            res = "B";
        } else if (cellnum >= 26) {
            //System.err.println(" value: " + cellnum + " statt " 
            //                   + (char)(65 + cellnum) + " -> A" + (char)(65 + cellnum-26));
            cellnum = cellnum - 26;
            res = "A";
        }

        char c = (char) (65 + cellnum);
        return res + c;
    }

    public static String getRowNum(final Integer cellnum) {
        return new Integer(cellnum.intValue() + 1).toString();
    }

    public static String genFormula(final String funcName, final List<?> rowNums, final Integer col) {
        String formula = funcName + "(";
        for (Iterator<?> iter = rowNums.iterator(); iter.hasNext();) {
            Integer rowNum = (Integer) iter.next();
            formula +=
                    ExcelService.getColName(col) + ExcelService.getRowNum(rowNum)
                    + CONST_PARAM_DELIM;
        }
        formula = formula.substring(0, formula.length() - 1);
        formula += ")";
        return formula;
    }

    public static String genIfNotEmpty(final String pSheetName, final Integer row, final Integer col) {
        // Sheetbname vorbereiten
        String sheetName = pSheetName;
        if (sheetName != null && sheetName.length() > 0) {
            sheetName += "!";
        } else {
            sheetName = "";
        }
        String element = sheetName + ExcelService.getColName(col) + ExcelService.getRowNum(row);
        String formula =
                "IF(" + element + ">0" + CONST_PARAM_DELIM + element + CONST_PARAM_DELIM + "\"\")";
        return formula;
    }

    public static String genCase4Values(final String pSheetName, final Integer baseRow, 
                                        final Integer baseCol, final Map<?, ?> values) {
        // Sheetbname vorbereiten
        String sheetName = pSheetName;
        if (sheetName != null && sheetName.length() > 0) {
            sheetName += "!";
        } else {
            sheetName = "";
        }
        if (values == null) {
            return "";
        }
        String element = sheetName + ExcelService.getColName(baseCol) + ExcelService.getRowNum(baseRow);
        String formula = "\"\"";
        for (Iterator<?> iter = values.keySet().iterator(); iter.hasNext();) {
            String key = iter.next().toString();
            String value = (String) values.get(key);
            formula = "IF(" + element + "=" + key + CONST_PARAM_DELIM + value + CONST_PARAM_DELIM + formula + ")";
        }

        return formula;
    }

    public static void evaluateAllFormula(final HSSFSheet sheet, final HSSFFormulaEvaluator formulaEval) {
        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
            for (int colNum = 0; colNum <= sheet.getRow(rowNum).getLastCellNum(); colNum++) {
                getCellEvaluated(sheet, formulaEval, rowNum, colNum);
            }
        }
    }

    /**
     * Kopiert die Eigenschaften des �bergebenen Cell-Styles und gibt
     * es als neues HSSFCellStyle-Objekt zur�ck.
     *
     * @param style                  
     * zu kopierendens HSSFCellStyle-Objekt
     *
     * @param wb                     
     * das betreffende Workbook
     *
     * @return
     * neues HSSFCellStyle-Objekt
     */
    public HSSFCellStyle copyStyle(final HSSFCellStyle style, final HSSFWorkbook wb) {
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setDataFormat(style.getDataFormat());
        style2.setFont(wb.getFontAt(style.getFontIndex()));
        style2.setVerticalAlignment(style.getVerticalAlignment());
        style2.setAlignment(style.getAlignment());
        style2.setWrapText(style.getWrapText());
        style2.setBorderLeft(style.getBorderLeft());
        style2.setBorderRight(style.getBorderRight());
        style2.setBorderBottom(style.getBorderBottom());
        style2.setBorderTop(style.getBorderTop());
        style2.setFillForegroundColor(style.getFillForegroundColor());
        style2.setFillPattern(style.getFillPattern());
        return style2;
    }

    public HSSFCellStyle convertCss2Modul(final HSSFCellStyle style, final HSSFWorkbook wb) {
        HSSFCellStyle style2 = copyStyle(style, wb);
        style2.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        style2.setLocked(true);
        style2.setFillPattern(HSSFCellStyle.FINE_DOTS);
        return style2;
    }
}
