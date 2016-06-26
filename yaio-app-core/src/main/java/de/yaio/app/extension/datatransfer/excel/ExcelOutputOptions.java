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
package de.yaio.app.extension.datatransfer.excel;

import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.datatransfer.exporter.OutputOptionsImpl;


/** 
 *    options for export of Nodes as Excel
 * 
 * @FeatureDomain                export
 * @package                      de.yaio.extension.datatransfer.excel
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ExcelOutputOptions extends OutputOptionsImpl {

    public boolean flgMergeExcelPlanungGantSheets = true;

    public ExcelOutputOptions() {
        super();
    }

    public ExcelOutputOptions(final OutputOptions oOptions) {
        super(oOptions);
    }

    public boolean isFlgMergeExcelPlanungGantSheets() {
        return flgMergeExcelPlanungGantSheets;
    }

    public void setFlgMergeExcelPlanungGantSheets(
            final boolean flgMergeExcelPlanungGantSheets) {
        this.flgMergeExcelPlanungGantSheets = flgMergeExcelPlanungGantSheets;
    }
}
