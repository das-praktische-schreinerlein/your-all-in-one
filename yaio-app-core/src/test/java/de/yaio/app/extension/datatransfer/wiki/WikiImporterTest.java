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
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.importer.ImportOptions;
import de.yaio.app.datatransfer.importer.Importer;
import de.yaio.app.extension.datatransfer.wiki.WikiImporter.WikiStructLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

/** 
 * test of the wiki-importer-logic<br>
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
public class WikiImporterTest extends BaseTest {

    @Override
    public TestObj setupNewTestObj() {
        return null;
    }
    
    /** 
     * configure the importer for the tests
     * @return                       optionsObj
     * @throws Exception             io-Exceptions possible
     */
    public Importer setupNewImporter() {
        return new WikiImporter(setupNewImportOptions());
    }
    
    /** 
     * configure the importoptions for the tests
     * @return                       optionsObj
     * @throws Exception             io-Exceptions possible
     */
    public ImportOptions setupNewImportOptions() {
        return new WikiImportOptions();
    }
    
    /** 
     * do tests for import
     * @throws ConverterException         Exceptions possible
     * @throws ParserException            Exceptions possible
     * @throws IOException                Exceptions possible
     */
    @Test
    public void testImport() throws ConverterException, ParserException, IOException {
        testImportFromFixture("FixtureWikiImportSource.wiki", "FixtureWikiImportResult.ppl"); 
        //testImportFromFixture("FixtureWikiImportSourceISO.wiki", "FixtureWikiImportResultISO.ppl"); 
    }

    /** 
     * parse the source and compare the result with expectedResult 
     * @param srcFile                file with the source
     * @param expectedResultFile     file with the expected result
     * @throws ConverterException         Exceptions possible
     * @throws ParserException            Exceptions possible
     * @throws IOException                Exceptions possible
     */
    public void testImportFromFixture(final String srcFile, final String expectedResultFile)
            throws ConverterException, ParserException, IOException {
        testImport(this.testService.readFixture(this.getClass(), srcFile).toString(), 
                   this.testService.readFixture(this.getClass(), expectedResultFile).toString()); 
    }

    /** 
     * parse the source and compare the result with expectedResult 
     * @param source                 the lines to parse
     * @param expectedResult         the expected ppl-lines from parser
     * @throws ConverterException         Exceptions possible
     * @throws ParserException            Exceptions possible
     * @throws IOException                Exceptions possible
     */
    public void testImport(final String source, final String expectedResult)
            throws ConverterException, ParserException, IOException {
        // configure Importer
        WikiImporter importerObj = (WikiImporter) setupNewImporter();
        WikiImportOptions importoptions = (WikiImportOptions) setupNewImportOptions();
        
        // parse source
        List<WikiStructLine> lstWikiLines;
        lstWikiLines = importerObj.extractWikiStructLinesFromSrc(source, importoptions);
        
        // concat resultlines
        StringBuffer resBuf = new StringBuffer();
        for (WikiStructLine wk : lstWikiLines) {
            resBuf.append(wk.getHirarchy()).append("\n");
        }
        
        //System.out.println("Erg:" + resBuf);
        
        // check
        this.testService.checkStringLineByLine(resBuf.toString(), expectedResult);
    }
}
