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
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import de.yaio.datatransfer.importer.Importer;
import de.yaio.extension.datatransfer.wiki.WikiImporterTest;

/** 
 * test of the excel-importer-logic<br>
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.extension.datatransfer.excel
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public class ExcelImporterTest extends WikiImporterTest {

    @Override
    public Importer setupNewImporter() throws Exception {
        return new ExcelImporter(setupNewImportOptions());
    }
    
    @Override
    public void testImport() throws Exception {
        testImportExcelFile("FixtureExcelImportSource.xls", "FixtureExcelImportResult.ppl"); 
    }

    
    /** 
     * parse the source and compare the result with expectedResult 
     * @param srcFile                the excelfile to parse
     * @param expectedResultFile     the file with the expected ppl-lines from parser
     * @throws Exception             possible Exception     */
    public void testImportExcelFile(final String srcFile, final String expectedResultFile) throws Exception {
        // configure Importer
        ExcelImporter importerObj = (ExcelImporter) setupNewImporter();
        
        // parse excel-file
        InputStream is = this.getClass().getResourceAsStream(srcFile);
        HSSFWorkbook wb = new HSSFWorkbook(is);
        List<String> lstPPLLines = importerObj.parsePlanungSheet(wb);
        
        // concat resultlines
        StringBuffer resBuf = new StringBuffer();
        for (String line : lstPPLLines) {
            resBuf.append(line).append("\n");
        }
        
        // check
        String expectedResult = 
                        this.testService.readFixture(this.getClass(), expectedResultFile).toString();
        this.testService.checkStringLineByLine(resBuf.toString(), expectedResult);
    }
}
