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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/** 
 * Testsuite for the Excel-logic
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.extension.datatransfer.excel
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

@RunWith(Suite.class)
@SuiteClasses({ExcelExporterTest.class, ExcelImporterTest.class})
public class DoAllExcelTests {
}
