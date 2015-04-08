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
package de.yaio.extension.datatransfer.wiki;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import de.yaio.BaseTest;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.Importer;
import de.yaio.extension.datatransfer.wiki.WikiImporter.WikiStructLine;

/**
 * <h4>FeatureDomain:</h4>
 *     Tests
 * <h4>FeatureDescription:</h4>
 *     test of the wiki-importer-logic<br>
 * 
 * @package de.yaio.extension.datatransfer.wiki
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category tests
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public class WikiImporterTest extends BaseTest {

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return null;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     configure the importer for the tests
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returns importerObj - Importer
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Test Configuration
     * @return - optionsObj
     * @throws Exception - io-Exceptions possible
     */
    public Importer setupNewImporter() throws Exception {
        return new WikiImporter(setupNewImportOptions());
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     configure the importoptions for the tests
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returns importoptions - ImportOptions
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Test Configuration
     * @return - optionsObj
     * @throws Exception - io-Exceptions possible
     */
    public ImportOptions setupNewImportOptions() throws Exception {
        return new WikiImportOptions();
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     do tests for import
     * <h4>FeatureKeywords:</h4>
     *     Test
     * @throws Exception - io-Exceptions possible
     */
    @Test
    public void testImport() throws Exception {
        testImportFromFixture("FixtureWikiImportSource.wiki", "FixtureWikiImportResult.ppl"); 
        //testImportFromFixture("FixtureWikiImportSourceISO.wiki", "FixtureWikiImportResultISO.ppl"); 
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     parse the source and compare the result with expectedResult 
     * <h4>FeatureKeywords:</h4>
     *     Test
     * @param srcFile - file with the source
     * @param expectedResultFile - file with the expected result
     * @throws Exception - io-Exceptions possible
     */
    public void testImportFromFixture(final String srcFile, final String expectedResultFile) throws Exception {
        testImport(this.testService.readFixture(this.getClass(), srcFile).toString(), 
                   this.testService.readFixture(this.getClass(), expectedResultFile).toString()); 
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     parse the source and compare the result with expectedResult 
     * <h4>FeatureKeywords:</h4>
     *     Test
     * @param source - the lines to parse
     * @param expectedResult - the expected ppl-lines from parser
     * @throws Exception - io-Exceptions possible
     */
    public void testImport(final String source, final String expectedResult) throws Exception {
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
