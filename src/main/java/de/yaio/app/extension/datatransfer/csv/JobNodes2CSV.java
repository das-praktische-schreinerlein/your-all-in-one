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
package de.yaio.app.extension.datatransfer.csv;

import de.yaio.app.extension.datatransfer.wiki.JobNodes2Wiki;

/** 
 * job for import of Nodes in PPL-Format and output in CSV-format
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.csv
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobNodes2CSV extends JobNodes2Wiki {
    
    
    /** 
     * create job-object to import nodes and output as CSV
     * @param args                   the command line arguments
     */
    public JobNodes2CSV(final String[] args) {
        super(args);
    }

    @Override
    public void createExporter() {
        exporter = new CSVExporter();
    }

    /** 
     * Main-method to start the application
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        JobNodes2CSV me = new JobNodes2CSV(args);
        me.startJobProcessing();
    }
}
