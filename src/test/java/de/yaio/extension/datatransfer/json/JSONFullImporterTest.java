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
package de.yaio.extension.datatransfer.json;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.Importer;
import de.yaio.extension.datatransfer.wiki.WikiImportOptions;
import de.yaio.extension.datatransfer.wiki.WikiImporterTest;

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
public class JSONFullImporterTest extends WikiImporterTest {

    @Override
    public Importer setupNewImporter() throws Exception {
        return new JSONFullImporter(setupNewImportOptions());
    }
    
    @Override
    public ImportOptions setupNewImportOptions() throws Exception {
        return new WikiImportOptions();
    }
    
    @Test
    @Override
    public void testImport() throws Exception {
        testImportFromFixture("FixtureJSONFullImportSource.json", "FixtureJSONFullImportResult.txt"); 
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
        JSONFullImporter importerObj = (JSONFullImporter) setupNewImporter();
        
        JSONResponse jsonResponse = importerObj.parseJSONResponse(source);
        DataDomain node = jsonResponse.getNode();
        String res = node.getBaseNodeService().visualizeNodeHierarchy("", node);
        //System.out.println("Erg:" + res);
        
        // check
        this.testService.checkStringLineByLine(res, expectedResult);
    }
}
