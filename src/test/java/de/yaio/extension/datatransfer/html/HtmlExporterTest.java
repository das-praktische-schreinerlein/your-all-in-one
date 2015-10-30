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
package de.yaio.extension.datatransfer.html;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.extension.datatransfer.wiki.WikiExporterTest;

/** 
 * test of the Html-exporter-logic<br>
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.extension.datatransfer.html
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public class HtmlExporterTest extends WikiExporterTest {

    @Override
    public Exporter setupNewExporter() throws Exception {
        return new HtmlExporter();
    }
    
    @Override
    public OutputOptions setupNewOutputOptions() throws Exception {
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setFlgRecalc(true);
        oOptions.setFlgShowChildrenSum(true);
        return oOptions;
    }
    
    @Override
    public void testExport() throws Exception {
        testExportFromFixture("FixtureHtmlExportSource.ppl", "FixtureHtmlExportResult.html");
        
        // DocLayout-test
        oOptions.setFlgProcessDocLayout(true);
        testExportFromFixture("FixtureHtmlExportSource.ppl", "FixtureHtmlDocLayoutExportResult.html");
    }
}
