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

import de.yaio.app.Configurator;
import de.yaio.extension.datatransfer.wiki.JobNodes2Wiki;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     job for import of Nodes in PPL-Format and output as ICal
 * 
 * @package de.yaio.extension.datatransfer.ical
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobNodes2ICal extends JobNodes2Wiki {
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     job to import nodes and output as ICal
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the application
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     * @param args the command line arguments
     */
    public JobNodes2ICal(final String[] args) {
        super(args);
    }

    @Override
    public void createExporter() {
        String sourceType = "";
        try {
            sourceType = Configurator.getInstance().getCommandLine().getOptionValue(
                            "sourcetype", "");
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
     * <h4>FeatureDomain:</h4>
     *     CLI
     * <h4>FeatureDescription:</h4>
     *     Main-method to start the application
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the application
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     CLI
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        JobNodes2ICal me = new JobNodes2ICal(args);
        me.startJobProcessing();
    }
}
