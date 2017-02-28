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
package de.yaio.app.extension.datatransfer.wiki;

import de.yaio.app.BaseTest;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.exporter.Exporter;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.app.datatransfer.importer.ImportOptions;
import de.yaio.app.datatransfer.importer.ImportOptionsImpl;
import de.yaio.app.extension.datatransfer.ppl.PPLImporter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import java.io.IOException;

/** 
 * test of the wiki-exporter-logic<br>
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.extension.datatransfer.wiki
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public class WikiExporterTest extends BaseTest {

    protected ImportOptions importoptions = null;
    protected PPLImporter importerObj = null;
    protected Exporter exporter = null;
    protected OutputOptions oOptions = null;   

    @Override
    public TestObj setupNewTestObj() {
        return null;
    }
    
    /** 
     * configure the exporter for the tests
     * @return                       Exporter
     */
    public Exporter setupNewExporter() {
        return new WikiExporter();
    }
    
    /** 
     * configure the exporter for the tests
     * @return                       OutputOptions
     */
    public OutputOptions setupNewOutputOptions() {
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setFlgRecalc(true);
        return oOptions;
    }
    
    
    @Override
    public void setUp() {
        // setup environment
        importoptions = new ImportOptionsImpl();
        importerObj = new PPLImporter(importoptions);
        exporter = setupNewExporter();
        oOptions = setupNewOutputOptions();
    }
    
    /** 
     * do tests for Export
     * @throws ConverterException         Exceptions possible
     * @throws ParserException            Exceptions possible
     * @throws IOException                Exceptions possible
     */
    @Test
    public void testExport() throws ConverterException, ParserException, IOException {
        testExportFromFixture("FixtureWikiExportSource.ppl", "FixtureWikiExportResult.wiki"); 
//        testExportFromFixture("FixtureWikiExportSourceISO.ppl", "FixtureWikiExportResultISO.wiki"); 
    }

    /** 
     * parse the source, format it and compare the result with expectedResult 
     * @param srcFile                file with the source
     * @param expectedResultFile     file with the expected result
     * @throws ConverterException         Exceptions possible
     * @throws ParserException            Exceptions possible
     * @throws IOException                Exceptions possible
     */
    public void testExportFromFixture(final String srcFile, final String expectedResultFile) throws ConverterException,
            ParserException, IOException {
        testExport(this.testService.readFixture(this.getClass(), srcFile).toString(), 
                   this.testService.readFixture(this.getClass(), expectedResultFile).toString()); 
    }

    /** 
     * parse the ppl-source, format it and compare the result with expectedResult 
     * @param source                 the ppl-lines to parse and to convert
     * @param expectedResult         the expected lines from exporter
     * @throws ConverterException         Exceptions possible
     * @throws ParserException            Exceptions possible
     * @throws IOException                Exceptions possible
     */
    public synchronized void testExport(final String source, final String expectedResult) throws ConverterException,
            ParserException, IOException {
        // format source
        DataDomain masterNode  = importerObj.createNodeObjFromText(1, "Test", "Test", null);
        String delimiter = "\t";
        importerObj.extractNodesFromLines(masterNode, source, delimiter);
        
        String res = exporter.getMasterNodeResult(masterNode, oOptions);
        
        //System.out.println("Erg:\n" + res);
        
        // check
        this.testService.checkStringLineByLine(res, expectedResult);
    }
}
