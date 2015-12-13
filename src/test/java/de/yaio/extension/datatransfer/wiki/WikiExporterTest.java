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
package de.yaio.extension.datatransfer.wiki;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import de.yaio.BaseTest;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.ImportOptionsImpl;
import de.yaio.extension.datatransfer.ppl.PPLImporter;

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
    public TestObj setupNewTestObj() throws Exception {
        return null;
    }
    
    /** 
     * configure the exporter for the tests
     * @FeatureDomain                Tests
     * @FeatureResult                returnValue exporter - Exporter
     * @FeatureKeywords              Test Configuration
     * @return                       Exporter
     * @throws Exception             io-Exceptions possible
     */
    public Exporter setupNewExporter() throws Exception {
        return new WikiExporter();
    }
    
    /** 
     * configure the exporter for the tests
     * @FeatureDomain                Tests
     * @FeatureResult                returnValue oOptions - OutputOptions
     * @FeatureKeywords              Test Configuration
     * @return                       OutputOptions
     * @throws Exception             io-Exceptions possible
     */
    public OutputOptions setupNewOutputOptions() throws Exception {
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setFlgRecalc(true);
        return oOptions;
    }
    
    
    @Override
    public void setUp() throws Exception {
        // setup environment
        importoptions = new ImportOptionsImpl();
        importerObj = new PPLImporter(importoptions);
        exporter = setupNewExporter();
        oOptions = setupNewOutputOptions();   
    }
    
    /** 
     * do tests for Export
     * @FeatureDomain                Tests
     * @FeatureKeywords              Test
     * @throws Exception             io-Exceptions possible
     */
    @Test
    public void testExport() throws Exception {
        testExportFromFixture("FixtureWikiExportSource.ppl", "FixtureWikiExportResult.wiki"); 
//        testExportFromFixture("FixtureWikiExportSourceISO.ppl", "FixtureWikiExportResultISO.wiki"); 
    }

    /** 
     * parse the source, format it and compare the result with expectedResult 
     * @FeatureDomain                Tests
     * @FeatureKeywords              Test
     * @param srcFile                file with the source
     * @param expectedResultFile     file with the expected result
     * @throws Exception             io-Exceptions possible
     */
    public void testExportFromFixture(final String srcFile, final String expectedResultFile) throws Exception {
        testExport(this.testService.readFixture(this.getClass(), srcFile).toString(), 
                   this.testService.readFixture(this.getClass(), expectedResultFile).toString()); 
    }

    /** 
     * parse the ppl-source, format it and compare the result with expectedResult 
     * @FeatureDomain                Tests
     * @FeatureKeywords              Test
     * @param source                 the ppl-lines to parse and to convert
     * @param expectedResult         the expected lines from exporter
     * @throws Exception             io-Exceptions possible
     */
    public synchronized void testExport(final String source, final String expectedResult) throws Exception {
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
