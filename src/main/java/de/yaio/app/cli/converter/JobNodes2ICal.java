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
package de.yaio.app.cli.converter;

import de.yaio.app.extension.datatransfer.ical.ICalDBExporter;
import de.yaio.app.extension.datatransfer.ical.ICalExporter;

/** 
 * job for import of Nodes in PPL-Format and output as ICal
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.ical
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobNodes2ICal extends JobNodes2Wiki {
    
    
    /** 
     * job to import nodes and output as ICal
     * @param args                   the command line arguments
     */
    public JobNodes2ICal(final String[] args) {
        super(args);
    }

    @Override
    public void createExporter() {
        String sourceType = "";
        try {
            sourceType = this.getCmdLineHelper().getCommandLine().getOptionValue("sourcetype", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("jpa".equalsIgnoreCase(sourceType)) {
            // from jpa: use dbExporter
            exporter = new ICalDBExporter();
        } else {
            // all other
            exporter = new ICalExporter();
        }
    }

    /** 
     * Main-method to start the application
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        JobNodes2ICal me = new JobNodes2ICal(args);
        me.startJobProcessing();
    }
}
