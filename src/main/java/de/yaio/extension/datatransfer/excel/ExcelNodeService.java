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




public class ExcelNodeService {


    public static final String CONST_SHEETNNAME_PLANUNG = "Planung";
    public static final int CONST_PLANUNG_ROUW_UE = 1;
    public static final int CONST_PLANUNG_COL_ID = 0;
    public static final int CONST_PLANUNG_COL_PROJEKT = 1;
    public static final int CONST_PLANUNG_COL_MODUL = 2;
    public static final int CONST_PLANUNG_COL_PAKET = 3;
    public static final int CONST_PLANUNG_COL_UNTERPAKET = 4;
    public static final int CONST_PLANUNG_COL_SCHRITT = 5;
    public static final int CONST_PLANUNG_COL_SCHRITT2 = 6;
    public static final int CONST_PLANUNG_COL_SCHRITT3 = 7;
    public static final int CONST_PLANUNG_COL_SCHRITT4 = 8;
    public static final int CONST_PLANUNG_COL_SCHRITT5 = 9;
    public static final int CONST_PLANUNG_COL_DESC = 10;
    public static final int CONST_PLANUNG_COL_PLAN_TASK =
        CONST_PLANUNG_COL_DESC + 1;
    public static final int CONST_PLANUNG_COL_IST_TASK =
        CONST_PLANUNG_COL_DESC + 2;
    public static final int CONST_PLANUNG_COL_FLG_DETAIL =
        CONST_PLANUNG_COL_DESC + 3;
    public static final int CONST_PLANUNG_COL_PLAN_PROGNOSE =
        CONST_PLANUNG_COL_DESC + 4;
    public static final int CONST_PLANUNG_COL_PLAN_AUFWAND =
        CONST_PLANUNG_COL_DESC + 5;
    public static final int CONST_PLANUNG_COL_PLAN_DATE_START =
        CONST_PLANUNG_COL_DESC + 6;
    public static final int CONST_PLANUNG_COL_PLAN_DATE_MIN_ENDE =
        CONST_PLANUNG_COL_DESC + 7;
    public static final int CONST_PLANUNG_COL_PLAN_DATE_ENDE =
        CONST_PLANUNG_COL_DESC + 8;
    public static final int CONST_PLANUNG_COL_IST_STAND =
        CONST_PLANUNG_COL_DESC + 9;
    public static final int CONST_PLANUNG_COL_IST_AUFWAND =
        CONST_PLANUNG_COL_DESC + 10;
    public static final int CONST_PLANUNG_COL_IST_DATE_START =
        CONST_PLANUNG_COL_DESC + 11;
    public static final int CONST_PLANUNG_COL_IST_DATE_ENDE =
        CONST_PLANUNG_COL_DESC + 12;
    public static final int CONST_PLANUNG_COL_IST_STANDFAKTOR =
        CONST_PLANUNG_COL_DESC + 13;
    public static final int CONST_PLANUNG_COL_REAL_AUFWAND =
        CONST_PLANUNG_COL_DESC + 14;
    public static final int CONST_PLANUNG_COL_REAL_OFFEN =
        CONST_PLANUNG_COL_DESC + 15;
    public static final int CONST_PLANUNG_COL_REAL_DIFF =
        CONST_PLANUNG_COL_DESC + 16;
    public static final int CONST_PLANUNG_COL_REAL_DATE_START =
        CONST_PLANUNG_COL_DESC + 17;
    public static final int CONST_PLANUNG_COL_REAL_DATE_ENDE =
        CONST_PLANUNG_COL_DESC + 18;

    public static final String CONST_SHEETNNAME_GANT = "Gant";
    public static final int CONST_GANT_ROUW_UE = CONST_PLANUNG_ROUW_UE;
    public static final int CONST_GANT_ROW_PLAN_MODUS = CONST_GANT_ROUW_UE - 1;
    public static final int CONST_GANT_COL_ID = 0;
    public static final int CONST_GANT_COL_PROJEKT = 1;
    public static final int CONST_GANT_COL_MODUL = 2;
    public static final int CONST_GANT_COL_PAKET = 3;
    public static final int CONST_GANT_COL_UNTERPAKET = 4;
    public static final int CONST_GANT_COL_SCHRITT = 5;
    public static final int CONST_GANT_COL_SCHRITT2 = 6;
    public static final int CONST_GANT_COL_SCHRITT3 = 7;
    public static final int CONST_GANT_COL_SCHRITT4 = 8;
    public static final int CONST_GANT_COL_SCHRITT5 = 9;
    public static final int CONST_GANT_COL_PLAN_PROGNOSE =
        CONST_GANT_COL_SCHRITT5 + 1;
    public static final int CONST_GANT_COL_PLAN_PROGNOSE_AUFWAND =
        CONST_GANT_COL_SCHRITT5 + 2;
    public static final int CONST_GANT_COL_PLAN_DATE_START =
        CONST_GANT_COL_SCHRITT5 + 3;
    public static final int CONST_GANT_COL_PLAN_DATE_ENDE =
        CONST_GANT_COL_SCHRITT5 + 4;
    public static final int CONST_GANT_COL_GANT_START =
        CONST_GANT_COL_SCHRITT5 + 5;
    public static final int CONST_GANT_COL_PLAN_MODUS = CONST_GANT_COL_PLAN_DATE_START;
    public static final int CONST_GANT_COL_PLAN_MODUS_NAME = CONST_GANT_COL_PLAN_MODUS + 1;


    public static final int CONST_GANT_VERSATZ =
        CONST_PLANUNG_COL_REAL_DATE_ENDE
        - CONST_GANT_COL_SCHRITT5;
    public static final int CONST_GANT_PERIODS = 11;
    public static final int CONST_WORKHOURS_PERDAY = 8;
}
