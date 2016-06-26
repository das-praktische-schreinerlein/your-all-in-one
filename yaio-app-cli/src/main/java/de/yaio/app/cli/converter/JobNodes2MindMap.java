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

import de.yaio.app.extension.datatransfer.mindmap.MindMapExporter;

/** 
 * job for import of Nodes in PPL-Format and output as MindMap
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.csv
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobNodes2MindMap extends JobNodes2Wiki {
    
    
    /** 
     * job to import nodes and output as Mindmap
     * @param args                   the command line arguments
     */
    public JobNodes2MindMap(final String[] args) {
        super(args);
    }

    @Override
    public void createExporter() {
        exporter = new MindMapExporter();
    }

    /** 
     * Main-method to start the application
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        JobNodes2MindMap me = new JobNodes2MindMap(args);
        me.startJobProcessing();
    }
}
