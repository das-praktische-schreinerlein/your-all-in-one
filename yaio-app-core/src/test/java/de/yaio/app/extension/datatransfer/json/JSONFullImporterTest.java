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
package de.yaio.app.extension.datatransfer.json;

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.importer.ImportOptions;
import de.yaio.app.datatransfer.importer.ImportOptionsImpl;
import de.yaio.app.datatransfer.importer.Importer;
import de.yaio.app.datatransfer.json.JSONFullImporter;
import de.yaio.app.datatransfer.json.JSONResponse;
import de.yaio.app.extension.datatransfer.wiki.WikiImporterTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import java.io.IOException;

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
public class JSONFullImporterTest extends WikiImporterTest {

    @Override
    public Importer setupNewImporter() {
        return new JSONFullImporter(setupNewImportOptions());
    }
    
    @Override
    public ImportOptions setupNewImportOptions() {
        return new ImportOptionsImpl();
    }
    
    @Test
    @Override
    public void testImport() throws ConverterException, ParserException, IOException {
        testImportFromFixture("FixtureJSONFullImportSource.json", "FixtureJSONFullImportResult.txt"); 
    }

    /** 
     * parse the source and compare the result with expectedResult 
     * @param source                 the lines to parse
     * @param expectedResult         the expected ppl-lines from parser
     * @throws IOException           Exceptions possible
     * @throws ParserException       Exceptions possible
     * @throws ConverterException    Exceptions possible
     */
    public void testImport(final String source, final String expectedResult) throws ConverterException, ParserException, IOException {
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
