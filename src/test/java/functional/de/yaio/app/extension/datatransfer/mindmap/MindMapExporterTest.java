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
package de.yaio.app.extension.datatransfer.mindmap;
import de.yaio.app.extension.datatransfer.mindmap.MindMapExporter;
import de.yaio.app.extension.datatransfer.wiki.WikiExporterTest;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import de.yaio.app.datatransfer.exporter.Exporter;

/** 
 * test of the MindMap-exporter-logic<br>
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.extension.datatransfer.mindma
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public class MindMapExporterTest extends WikiExporterTest {

    @Override
    public Exporter setupNewExporter() throws Exception {
        return new MindMapExporter();
    }
    
    @Override
    public void testExport() throws Exception {
        testExportFromFixture("FixtureMindMapExportSource.ppl", "FixtureMindMapExportResult.mm");
    }
}
