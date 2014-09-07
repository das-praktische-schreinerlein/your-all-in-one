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

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.extension.datatransfer.wiki.JobNodes2Wiki;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     job for import of Nodes in PPL-Format and output in Excel-format
 * 
 * @package de.yaio.extension.datatransfer.excel
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobNodes2Excel extends JobNodes2Wiki {
    
    
    public JobNodes2Excel(String[] args) {
        super(args);
    }
    
    @Override
    protected Options genAvailiableCmdLineOptions() throws Throwable {
        Options availiableCmdLineOptions = super.genAvailiableCmdLineOptions();
        
        // Excel-Option
        Option mergeOption = new Option("", "mergeexcelplanunggantsheets", false, 
                        "mergeexcelplanunggantsheets");
        mergeOption.setRequired(false);
        availiableCmdLineOptions.addOption(mergeOption);
        
        return availiableCmdLineOptions;
    }
    
    @Override
    public void publishResult(Exporter exporter, DataDomain masterNode, 
            OutputOptions oOptions) throws Exception {
        String outFile = "test.xls";
        if (this.cmdLine.getArgs().length > 1) {
            // aus Datei
            outFile = this.cmdLine.getArgs()[1];
        }

        ExcelOutputOptions excelOptions = new ExcelOutputOptions(oOptions);
        excelOptions.flgMergeExcelPlanungGantSheets = 
                this.cmdLine.hasOption("mergeexcelplanunggantsheets");

        // Masternode ausgeben
        ((ExcelExporter)exporter).toExcel((BaseNode)masterNode, outFile, excelOptions);
    }
    @Override
    public void createExporter() {
        exporter = new ExcelExporter();
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) {
        JobNodes2Excel me = new JobNodes2Excel(args);
        me.startJobProcessing();
    }
}
