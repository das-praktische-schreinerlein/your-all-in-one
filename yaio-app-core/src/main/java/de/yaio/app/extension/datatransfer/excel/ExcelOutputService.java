/*
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
package de.yaio.app.extension.datatransfer.excel;


import de.yaio.app.utils.ExcelService;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;


public class ExcelOutputService extends ExcelService {

    public static final short CONST_CS_BGCOLOR_PLANUNG =
        new HSSFColor.LIGHT_TURQUOISE().getIndex();
    private static final short CONST_CS_BGCOLOR_IST =
        new HSSFColor.LIGHT_YELLOW().getIndex();
    public static final short CONST_CS_BGCOLOR_REALT =
        new HSSFColor.LIGHT_ORANGE().getIndex();

    public HSSFFont fsFieldPlanungBase_Entry;
    public HSSFFont fsFieldPlanungBase_Modul;

    // Sheet Planung
    public HSSFCellStyle csFieldPlanungBase;
    public HSSFCellStyle csFieldPlanungBaseField;
    public HSSFCellStyle csFieldPlanungBaseFieldStruktur;
    public HSSFCellStyle csFieldPlanungBaseFieldPlan;
    public HSSFCellStyle csFieldPlanungBaseFieldIst;
    public HSSFCellStyle csFieldPlanungBaseFieldReal;
    public HSSFCellStyle csFieldPlanungBaseUe;

    public HSSFCellStyle csFieldPlanungStruktur_Entry;
    public HSSFCellStyle csFieldPlanungStruktur_Modul;

    public HSSFCellStyle csFieldPlanungPlanStartDate_Entry;
    public HSSFCellStyle csFieldPlanungPlanStartDate_Modul;
    public HSSFCellStyle csFieldPlanungPlanMinEndDate_Entry;
    public HSSFCellStyle csFieldPlanungPlanMinEndDate_Modul;
    public HSSFCellStyle csFieldPlanungPlanEndDate_Entry;
    public HSSFCellStyle csFieldPlanungPlanEndDate_Modul;
    public HSSFCellStyle csFieldPlanungPlanAufwand_Entry;
    public HSSFCellStyle csFieldPlanungPlanAufwand_Modul;
    public HSSFCellStyle csFieldPlanungPlanPrognose_Entry;
    public HSSFCellStyle csFieldPlanungPlanPrognose_Modul;

    public HSSFCellStyle csFieldPlanungIstStartDate_Entry;
    public HSSFCellStyle csFieldPlanungIstStartDate_Modul;
    public HSSFCellStyle csFieldPlanungIstEndDate_Entry;
    public HSSFCellStyle csFieldPlanungIstEndDate_Modul;
    public HSSFCellStyle csFieldPlanungIstAufwand_Entry;
    public HSSFCellStyle csFieldPlanungIstAufwand_Modul;
    public HSSFCellStyle csFieldPlanungIstStand_Entry;
    public HSSFCellStyle csFieldPlanungIstStand_Modul;
    public HSSFCellStyle csFieldPlanungIstStandFaktor_Entry;
    public HSSFCellStyle csFieldPlanungIstStandFaktor_Modul;

    public HSSFCellStyle csFieldPlanungRealAufwand_Entry;
    public HSSFCellStyle csFieldPlanungRealAufwand_Modul;
    public HSSFCellStyle csFieldPlanungRealOffen_Entry;
    public HSSFCellStyle csFieldPlanungRealOffen_Modul;
    public HSSFCellStyle csFieldPlanungRealDiff_Entry;
    public HSSFCellStyle csFieldPlanungRealDiff_Modul;
    public HSSFCellStyle csFieldPlanungRealStartDate_Entry;
    public HSSFCellStyle csFieldPlanungRealStartDate_Modul;
    public HSSFCellStyle csFieldPlanungRealEndDate_Entry;
    public HSSFCellStyle csFieldPlanungRealEndDate_Modul;

    // Sheet Gant
    public HSSFCellStyle csFieldGantBase;
    public HSSFCellStyle csFieldGantBaseField;
    public HSSFCellStyle csFieldGantBaseFieldStruktur;
    public HSSFCellStyle csFieldGantBaseFieldPlan;
    public HSSFCellStyle csFieldGantBaseFieldGant;
    public HSSFCellStyle csFieldGantBaseUe;

    public HSSFCellStyle csFieldGantStruktur_Entry;
    public HSSFCellStyle csFieldGantStruktur_Modul;

    public HSSFCellStyle csFieldGantPlanStartDate_Entry;
    public HSSFCellStyle csFieldGantPlanStartDate_Modul;
    public HSSFCellStyle csFieldGantPlanEndDate_Entry;
    public HSSFCellStyle csFieldGantPlanEndDate_Modul;
    public HSSFCellStyle csFieldGantPlanPrognose_Entry;
    public HSSFCellStyle csFieldGantPlanPrognose_Modul;
    public HSSFCellStyle csFieldGantPlanAufwand_Entry;
    public HSSFCellStyle csFieldGantPlanAufwand_Modul;

    public HSSFCellStyle csFieldGantStartDate_Entry;
    public HSSFCellStyle csFieldGantStartDate_Modul;
    public HSSFCellStyle csFieldGantNormDate_Entry;
    public HSSFCellStyle csFieldGantNormDate_Modul;
    public HSSFCellStyle csFieldGantEndDate_Entry;
    public HSSFCellStyle csFieldGantEndDate_Modul;
    public HSSFCellStyle csFieldGantNormDate_Ue;


    public ExcelOutputService (HSSFWorkbook workbook) {
        // Parent initialisieren
        super(workbook);
    
        //###########
        // Sheet Planung
        //###########

        //
        // Basis-Sytles
        //
        fsFieldPlanungBase_Entry = workbook.createFont();
        fsFieldPlanungBase_Entry.setFontHeightInPoints((short) 10);

        fsFieldPlanungBase_Modul = workbook.createFont();
        fsFieldPlanungBase_Modul.setFontHeightInPoints((short) 11);
        fsFieldPlanungBase_Modul.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);



        csFieldPlanungBase = workbook.createCellStyle();
        csFieldPlanungBase.setFont(fsFieldPlanungBase_Entry);

        csFieldPlanungBaseField = copyStyle(csFieldPlanungBase, workbook);

        csFieldPlanungBaseFieldStruktur = copyStyle(csFieldPlanungBaseField, workbook);

        csFieldPlanungBaseFieldPlan = copyStyle(csFieldPlanungBaseField, workbook);
        csFieldPlanungBaseFieldPlan.setFillForegroundColor(CONST_CS_BGCOLOR_PLANUNG);
        csFieldPlanungBaseFieldPlan.setFillPattern((short) 1);

        csFieldPlanungBaseFieldIst = copyStyle(csFieldPlanungBaseField, workbook);
        csFieldPlanungBaseFieldIst.setFillForegroundColor(CONST_CS_BGCOLOR_IST);
        csFieldPlanungBaseFieldIst.setFillPattern((short) 1);

        csFieldPlanungBaseFieldReal = copyStyle(csFieldPlanungBaseField, workbook);
        csFieldPlanungBaseFieldReal.setFillForegroundColor(CONST_CS_BGCOLOR_REALT);
        csFieldPlanungBaseFieldReal.setFillPattern(HSSFCellStyle.FINE_DOTS);

        csFieldPlanungBaseUe = copyStyle(csFieldPlanungBase, workbook);
        csFieldPlanungBaseUe.setFont(fsFieldPlanungBase_Modul);
        csFieldPlanungBaseUe.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        csFieldPlanungBaseUe.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);

        //
        // Struktur
        //

        // Basis
        csFieldPlanungStruktur_Entry = copyStyle(csFieldPlanungBaseFieldStruktur, workbook);

        // Ue
        csFieldPlanungStruktur_Modul = copyStyle(csFieldPlanungStruktur_Entry, workbook);
        csFieldPlanungStruktur_Modul.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        csFieldPlanungStruktur_Modul.setFont(fsFieldPlanungBase_Modul);

        //
        // Plan
        //

        //Basis
        csFieldPlanungPlanStartDate_Entry = copyStyle(csFieldPlanungBaseFieldPlan, workbook);
        csFieldPlanungPlanStartDate_Entry.setDataFormat(
                workbook.createDataFormat().getFormat(CONST_CS_DATEFORM));

        csFieldPlanungPlanMinEndDate_Entry = copyStyle(csFieldPlanungPlanStartDate_Entry, workbook);
        csFieldPlanungPlanEndDate_Entry = copyStyle(csFieldPlanungPlanStartDate_Entry, workbook);

        csFieldPlanungPlanAufwand_Entry = copyStyle(csFieldPlanungBaseFieldPlan, workbook);

        csFieldPlanungPlanPrognose_Entry = copyStyle(csFieldPlanungBaseFieldPlan, workbook);
        csFieldPlanungPlanPrognose_Entry.setDataFormat(
                HSSFDataFormat.getBuiltinFormat(CONST_CS_PERCENT));


        // Border setzen
        csFieldPlanungPlanAufwand_Entry.setBorderLeft(HSSFCellStyle.BORDER_DOUBLE);
        csFieldPlanungPlanEndDate_Entry.setBorderRight(HSSFCellStyle.BORDER_DOUBLE);

        // Ue setzen
        csFieldPlanungPlanStartDate_Modul = convertCss2Modul(csFieldPlanungPlanStartDate_Entry, workbook);
        csFieldPlanungPlanAufwand_Modul = convertCss2Modul(csFieldPlanungPlanAufwand_Entry, workbook);
        csFieldPlanungPlanPrognose_Modul = convertCss2Modul(csFieldPlanungPlanPrognose_Entry, workbook);
        csFieldPlanungPlanMinEndDate_Modul = convertCss2Modul(csFieldPlanungPlanMinEndDate_Entry, workbook);
        csFieldPlanungPlanEndDate_Modul = convertCss2Modul(csFieldPlanungPlanEndDate_Entry, workbook);

        //
        // Ist
        //

        // Basis
        csFieldPlanungIstStartDate_Entry = copyStyle(csFieldPlanungBaseFieldIst, workbook);
        csFieldPlanungIstStartDate_Entry.setDataFormat(
                workbook.createDataFormat().getFormat(CONST_CS_DATEFORM));

        csFieldPlanungIstEndDate_Entry = copyStyle(csFieldPlanungIstStartDate_Entry, workbook);

        csFieldPlanungIstAufwand_Entry = copyStyle(csFieldPlanungBaseFieldIst, workbook);

        csFieldPlanungIstStand_Entry = copyStyle(csFieldPlanungBaseFieldIst, workbook);
        csFieldPlanungIstStand_Entry.setDataFormat(
                HSSFDataFormat.getBuiltinFormat(CONST_CS_PERCENT));

        csFieldPlanungIstStandFaktor_Entry = copyStyle(csFieldPlanungBaseFieldIst, workbook);

        // Border setzen
        csFieldPlanungIstStand_Entry.setBorderLeft(HSSFCellStyle.BORDER_DOUBLE);
        csFieldPlanungIstEndDate_Entry.setBorderRight(HSSFCellStyle.BORDER_DOUBLE);

        // Ue setzen
        csFieldPlanungIstStartDate_Modul = convertCss2Modul(csFieldPlanungIstStartDate_Entry, workbook);
        csFieldPlanungIstEndDate_Modul = convertCss2Modul(csFieldPlanungIstEndDate_Entry, workbook);
        csFieldPlanungIstAufwand_Modul = convertCss2Modul(csFieldPlanungIstAufwand_Entry, workbook);
        csFieldPlanungIstStand_Modul = convertCss2Modul(csFieldPlanungIstStand_Entry, workbook);
        csFieldPlanungIstStandFaktor_Modul = convertCss2Modul(csFieldPlanungIstStandFaktor_Entry, workbook);

        //
        // Real
        //

        // Basis
        csFieldPlanungRealAufwand_Entry = copyStyle(csFieldPlanungBaseFieldReal, workbook);
        csFieldPlanungRealOffen_Entry = copyStyle(csFieldPlanungRealAufwand_Entry, workbook);
        csFieldPlanungRealDiff_Entry = copyStyle(csFieldPlanungRealAufwand_Entry, workbook);
        csFieldPlanungRealStartDate_Entry = copyStyle(csFieldPlanungBaseFieldReal, workbook);
        csFieldPlanungRealStartDate_Entry.setDataFormat(
                workbook.createDataFormat().getFormat(CONST_CS_DATEFORM));

        csFieldPlanungRealEndDate_Entry = copyStyle(csFieldPlanungRealStartDate_Entry, workbook);

        // Border setzen
        csFieldPlanungRealAufwand_Entry.setBorderLeft(HSSFCellStyle.BORDER_DOUBLE);
        csFieldPlanungRealEndDate_Entry.setBorderRight(HSSFCellStyle.BORDER_DOUBLE);

        // Ue setzen
        csFieldPlanungRealAufwand_Modul = convertCss2Modul(csFieldPlanungRealAufwand_Entry, workbook);
        csFieldPlanungRealOffen_Modul = convertCss2Modul(csFieldPlanungRealOffen_Entry, workbook);
        csFieldPlanungRealDiff_Modul = convertCss2Modul(csFieldPlanungRealDiff_Entry, workbook);
        csFieldPlanungRealStartDate_Modul = convertCss2Modul(csFieldPlanungRealStartDate_Entry, workbook);
        csFieldPlanungRealEndDate_Modul = convertCss2Modul(csFieldPlanungRealEndDate_Entry, workbook);


        //###########
        // Sheet Gant
        //###########
        csFieldGantBase = copyStyle(csFieldPlanungBase, workbook);
        csFieldGantBaseField = copyStyle(csFieldPlanungBaseField, workbook);
        csFieldGantBaseFieldPlan = copyStyle(csFieldPlanungBaseFieldPlan, workbook);
        csFieldGantBaseFieldStruktur = copyStyle(csFieldPlanungBaseFieldStruktur, workbook);

        // Struktur
        csFieldGantStruktur_Entry = copyStyle(csFieldPlanungStruktur_Entry, workbook);
        csFieldGantStruktur_Modul = copyStyle(csFieldPlanungStruktur_Modul, workbook);

        csFieldGantBaseUe = copyStyle(csFieldPlanungBaseUe, workbook);
        csFieldGantNormDate_Ue = copyStyle(csFieldGantBaseUe, workbook);
        csFieldGantNormDate_Ue.setDataFormat(
                workbook.createDataFormat().getFormat(CONST_CS_GANT_DATEFORM));

        // Plan
        csFieldGantPlanPrognose_Entry = copyStyle(csFieldPlanungPlanPrognose_Entry, workbook);
        csFieldGantPlanPrognose_Modul = copyStyle(csFieldPlanungPlanPrognose_Modul, workbook);
        csFieldGantPlanAufwand_Entry = copyStyle(csFieldPlanungPlanAufwand_Entry, workbook);
        csFieldGantPlanAufwand_Modul = copyStyle(csFieldPlanungPlanAufwand_Modul, workbook);
        csFieldGantPlanStartDate_Entry = copyStyle(csFieldPlanungPlanStartDate_Entry, workbook);
        csFieldGantPlanStartDate_Modul = copyStyle(csFieldPlanungPlanStartDate_Modul, workbook);
        csFieldGantPlanEndDate_Entry = copyStyle(csFieldPlanungPlanEndDate_Entry, workbook);
        csFieldGantPlanEndDate_Modul = copyStyle(csFieldPlanungPlanEndDate_Modul, workbook);

        // Gant
        csFieldGantBaseFieldGant = copyStyle(csFieldGantBaseField, workbook);

        csFieldGantStartDate_Entry = copyStyle(csFieldGantBaseFieldGant, workbook);
        csFieldGantStartDate_Entry.setDataFormat(
                workbook.createDataFormat().getFormat(CONST_CS_AUFWAND));
        csFieldGantStartDate_Modul = copyStyle(csFieldGantBaseFieldGant, workbook);
        csFieldGantStartDate_Modul.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);

        csFieldGantNormDate_Entry = copyStyle(csFieldGantStartDate_Entry, workbook);
        csFieldGantNormDate_Entry.setBorderRight(HSSFCellStyle.BORDER_HAIR);
        csFieldGantNormDate_Entry.setBorderLeft(HSSFCellStyle.BORDER_HAIR);
        csFieldGantNormDate_Modul = copyStyle(csFieldGantStartDate_Modul, workbook);
        csFieldGantNormDate_Modul.setBorderRight(HSSFCellStyle.BORDER_HAIR);
        csFieldGantNormDate_Modul.setBorderLeft(HSSFCellStyle.BORDER_HAIR);

        csFieldGantEndDate_Entry = copyStyle(csFieldGantStartDate_Entry, workbook);
        csFieldGantEndDate_Modul = copyStyle(csFieldGantStartDate_Modul, workbook);

        // Border
        csFieldGantStartDate_Entry.setBorderLeft(HSSFCellStyle.BORDER_DOUBLE);
        csFieldGantStartDate_Modul.setBorderLeft(HSSFCellStyle.BORDER_DOUBLE);
        csFieldGantEndDate_Entry.setBorderRight(HSSFCellStyle.BORDER_DOUBLE);
        csFieldGantEndDate_Modul.setBorderRight(HSSFCellStyle.BORDER_DOUBLE);
    }

    // TODO Doku
    @Override
    public HSSFCellStyle convertCss2Modul(HSSFCellStyle style, HSSFWorkbook wb) {
        HSSFCellStyle style2 = copyStyle(style, wb);
        style2.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        style2.setLocked(true);
        style2.setFillPattern(HSSFCellStyle.FINE_DOTS);
        style2.setFont(fsFieldPlanungBase_Modul);
        return style2;
    }
}
