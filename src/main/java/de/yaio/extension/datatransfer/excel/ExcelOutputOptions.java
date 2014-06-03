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
package de.yaio.extension.datatransfer.excel;

import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;


/**
 * <h4>FeatureDomain:</h4>
*     export
 * <h4>FeatureDescription:</h4>
 *    options for export of Nodes in Excel-Format
 * 
 * @package de.yaio.extension.datatransfer.excel
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ExcelOutputOptions extends OutputOptionsImpl {

    public boolean flgMergeExcelPlanungGantSheets = true;

    public ExcelOutputOptions(OutputOptions oOptions) {
        super(oOptions);
    }

    public boolean isFlgMergeExcelPlanungGantSheets() {
        return flgMergeExcelPlanungGantSheets;
    }

    public void setFlgMergeExcelPlanungGantSheets(
            boolean flgMergeExcelPlanungGantSheets) {
        this.flgMergeExcelPlanungGantSheets = flgMergeExcelPlanungGantSheets;
    }
}
