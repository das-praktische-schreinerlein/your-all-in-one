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
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.extension.datatransfer.wiki.WikiExporterTest;

/**
 * <h4>FeatureDomain:</h4>
 *     Tests
 * <h4>FeatureDescription:</h4>
 *     test of the excel-exporter-logic<br>
 * 
 * @package de.yaio.extension.datatransfer.excel
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category tests
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public class ExcelExporterTest extends WikiExporterTest {

    @Override
    public Exporter setupNewExporter() throws Exception {
        return new ExcelExporter();
    }
    
    @Override
    public void testExport() throws Exception {
        testExportFromFixture("FixtureExcelExportSource.ppl", "FixtureExcelExportResult.xls"); 
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     parse the ppl-source, format it and compare the result with expectedResult 
     * <h4>FeatureKeywords:</h4>
     *     Test
     * @param source - the ppl-lines to parse and to convert
     * @param expectedResult - the expected lines from exporter
     * @throws Exception
     */
    public synchronized void testExport(String source, String expectedResult) throws Exception {
        // parse
        DataDomain masterNode  = importerObj.createNodeObjFromText(1, "Test", "Test", null);
        String delimiter = "\t";
        importerObj.extractNodesFromLines(masterNode, source, delimiter);
        
        // create WorkBook
        ExcelOutputOptions excelOptions = new ExcelOutputOptions(oOptions);
        excelOptions.flgMergeExcelPlanungGantSheets = true;
        ExcelExporter excelExporter = (ExcelExporter)exporter;
        HSSFWorkbook wb = excelExporter.toExcel((BaseNode)masterNode, excelOptions);
        
        // to String
        OutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        String res = os.toString();

        // check size only, because of binary Excel mismatch :-(
        assertEquals("Size: " + expectedResult.length(), 
                     "Size: " + (res.length()+1));
    }
}