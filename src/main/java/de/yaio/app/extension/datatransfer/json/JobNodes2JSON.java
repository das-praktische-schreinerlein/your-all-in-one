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

import de.yaio.app.extension.datatransfer.wiki.JobNodes2Wiki;

/** 
 * job for import of Nodes in PPL-Format and output as JSON
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.json
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobNodes2JSON extends JobNodes2Wiki {
    
    
    /** 
     * job to import nodes and output as JSON
     * @param args                   the command line arguments
     */
    public JobNodes2JSON(final String[] args) {
        super(args);
    }

    @Override
    public void createExporter() {
        exporter = new JSONExporter();
    }

    /** 
     * Main-method to start the application
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        JobNodes2JSON me = new JobNodes2JSON(args);
        me.startJobProcessing();
    }
}
