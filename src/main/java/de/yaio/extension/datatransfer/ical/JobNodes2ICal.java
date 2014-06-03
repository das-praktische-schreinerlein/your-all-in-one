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
package de.yaio.extension.datatransfer.ical;

import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.extension.datatransfer.wiki.JobNodes2Wiki;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     job for import of Nodes in PPL-Format and output in ICal-format
 * 
 * @package de.yaio.extension.datatransfer.ical
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobNodes2ICal extends JobNodes2Wiki {
    
    
    public JobNodes2ICal(String[] args) {
        super(args);
    }

    @Override
    public void createExporter() {
        exporter = new ICalExporter();
    }

    @Override
    public void createImporter() {
        importer = new PPLImporter(null);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) {
        JobNodes2ICal me = new JobNodes2ICal(args);
        me.startJobProcessing();
    }
}
