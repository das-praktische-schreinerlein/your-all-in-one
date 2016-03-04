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

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import de.yaio.app.Configurator;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.Exporter;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.extension.datatransfer.wiki.JobNodes2Wiki;

/** 
 * job for import of Nodes in PPL-Format and output as Excel
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.excel
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobNodes2Excel extends JobNodes2Wiki {
    
    
    /** 
     * job to import nodes and output as Excel
     * @param args                   the command line arguments
     */
    public JobNodes2Excel(final String[] args) {
        super(args);
    }
    
    @Override
    protected Options addAvailiableCmdLineOptions() throws Exception {
        Options availiableCmdLineOptions = super.addAvailiableCmdLineOptions();
        
        // Excel-Option
        Option mergeOption = new Option("", "mergeexcelplanunggantsheets", false, 
                        "mergeexcelplanunggantsheets");
        mergeOption.setRequired(false);
        availiableCmdLineOptions.addOption(mergeOption);
        
        return availiableCmdLineOptions;
    }
    
    @Override
    public void publishResult(final Exporter exporter, final DataDomain masterNode, 
            final OutputOptions oOptions) throws Exception {
        String outFile = "test.xls";
        if (Configurator.getInstance().getCommandLine().getArgs().length > 1) {
            // aus Datei
            outFile = Configurator.getInstance().getCommandLine().getArgs()[1];
        }

        ExcelOutputOptions excelOptions = new ExcelOutputOptions(oOptions);
        excelOptions.flgMergeExcelPlanungGantSheets = 
                        Configurator.getInstance().getCommandLine().hasOption("mergeexcelplanunggantsheets");

        // Masternode ausgeben
        ((ExcelExporter) exporter).toExcel((BaseNode) masterNode, outFile, excelOptions);
    }
    
    @Override
    public void createExporter() {
        exporter = new ExcelExporter();
    }

    /** 
     * Main-method to start the application
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        JobNodes2Excel me = new JobNodes2Excel(args);
        me.startJobProcessing();
    }
}
