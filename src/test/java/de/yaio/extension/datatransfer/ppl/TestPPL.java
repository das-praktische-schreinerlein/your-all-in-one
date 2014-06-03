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
package de.yaio.extension.datatransfer.ppl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;

public class TestPPL {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(TestPPL.class);

    public static String DEFAULT_ENTRY_DELIMITER = PPLService.DEFAULT_ENTRY_DELIMITER;
    public static String LINE_DELIMITER = PPLService.LINE_DELIMITER;
    
    //@Test
    public void testPPLParserAndExporter() {
        String src =
                "OFFEN - Projekt1" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Unterprojekt1" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Punkt1" + LINE_DELIMITER
                + "OFFEN - Projekt1" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Unterprojekt1" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Punkt2" + LINE_DELIMITER
                + "OFFEN - Projekt1" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Unterprojekt2" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Punkt1" + LINE_DELIMITER
                + "OFFEN - Projekt1" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Unterprojekt2" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Punkt2" + LINE_DELIMITER
                + "RUNNING - Projekt2" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Unterprojekt1" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Punkt1" + LINE_DELIMITER
                + "RUNNING - Projekt2" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Unterprojekt1" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Punkt2" + LINE_DELIMITER
                + "RUNNING - Projekt2" + DEFAULT_ENTRY_DELIMITER + "RUNNING - Unterprojekt2" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Punkt1" + LINE_DELIMITER
                + "RUNNING - Projekt2" + DEFAULT_ENTRY_DELIMITER + "RUNNING - Unterprojekt2" + DEFAULT_ENTRY_DELIMITER + "RUNNING - Punkt2" + LINE_DELIMITER
                + "LATE - Projekt3" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Unterprojekt1" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Punkt1" + LINE_DELIMITER
                + "LATE - Projekt3" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Unterprojekt1" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Punkt2" + LINE_DELIMITER
                + "LATE - Projekt3" + DEFAULT_ENTRY_DELIMITER + "RUNNING - Unterprojekt2" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Punkt1" + LINE_DELIMITER
                + "LATE - Projekt3" + DEFAULT_ENTRY_DELIMITER + "RUNNING - Unterprojekt2" + DEFAULT_ENTRY_DELIMITER + "RUNNING - Punkt2" + LINE_DELIMITER
                + "LATE - Projekt3" + DEFAULT_ENTRY_DELIMITER + "LATE - Unterprojekt3" + DEFAULT_ENTRY_DELIMITER + "OFFEN - Punkt1" + LINE_DELIMITER
                + "LATE - Projekt3" + DEFAULT_ENTRY_DELIMITER + "LATE - Unterprojekt3" + DEFAULT_ENTRY_DELIMITER + "RUNNING - Punkt2" + LINE_DELIMITER
                + "LATE - Projekt3" + DEFAULT_ENTRY_DELIMITER + "LATE - Unterprojekt3" + DEFAULT_ENTRY_DELIMITER + "LATE - Punkt3" + LINE_DELIMITER
                + "LATE - Projekt3" + DEFAULT_ENTRY_DELIMITER + "LATE - Unterprojekt3" + DEFAULT_ENTRY_DELIMITER + "DONE - Punkt4" + LINE_DELIMITER
                + "LATE - Projekt3" + DEFAULT_ENTRY_DELIMITER + "DONE - Unterprojekt3" + DEFAULT_ENTRY_DELIMITER + "DONE - Punkt1" + LINE_DELIMITER
                + "DONE - Projekt4" + DEFAULT_ENTRY_DELIMITER + "DONE - Unterprojekt1" + DEFAULT_ENTRY_DELIMITER + "DONE - Punkt1" + LINE_DELIMITER
                + "DONE - Projekt4" + DEFAULT_ENTRY_DELIMITER + "DONE - Unterprojekt1" + DEFAULT_ENTRY_DELIMITER + "DONE - Punkt2" + LINE_DELIMITER
                + "DONE - Projekt4" + DEFAULT_ENTRY_DELIMITER + "DONE - Unterprojekt2" + DEFAULT_ENTRY_DELIMITER + "DONE - Punkt1" + LINE_DELIMITER
                + "DONE - Projekt4" + DEFAULT_ENTRY_DELIMITER + "DONE - Unterprojekt2" + DEFAULT_ENTRY_DELIMITER + "DONE - Punkt2 Ist: 10% 2h 29.01.2011-30.04.2012 __t2123__ Plan: 3h 30.01.2011-30.03.2012" + LINE_DELIMITER
                ;
        String expected = "Master\nMaster	OFFEN - Projekt1\nMaster	OFFEN - Projekt1	OFFEN - Unterprojekt2\nMaster	OFFEN - Projekt1	OFFEN - Unterprojekt2	OFFEN - Punkt2\nMaster	OFFEN - Projekt1	OFFEN - Unterprojekt1\nMaster	OFFEN - Projekt1	OFFEN - Unterprojekt1	OFFEN - Punkt2\nMaster	OFFEN - Projekt1	OFFEN - Unterprojekt1\nMaster	OFFEN - Projekt1	OFFEN - Unterprojekt1	OFFEN - Punkt1\nMaster	OFFEN - Projekt1	OFFEN - Unterprojekt2\nMaster	OFFEN - Projekt1	OFFEN - Unterprojekt2	OFFEN - Punkt1\nMaster	RUNNING - Projekt2\nMaster	RUNNING - Projekt2	RUNNING - Unterprojekt2\nMaster	RUNNING - Projekt2	RUNNING - Unterprojekt2	OFFEN - Punkt1\nMaster	RUNNING - Projekt2	OFFEN - Unterprojekt1\nMaster	RUNNING - Projekt2	OFFEN - Unterprojekt1	OFFEN - Punkt1\nMaster	RUNNING - Projekt2	RUNNING - Unterprojekt2\nMaster	RUNNING - Projekt2	RUNNING - Unterprojekt2	RUNNING - Punkt2\nMaster	RUNNING - Projekt2	OFFEN - Unterprojekt1\nMaster	RUNNING - Projekt2	OFFEN - Unterprojekt1	OFFEN - Punkt2\nMaster	DONE - Projekt4\nMaster	DONE - Projekt4	DONE - Unterprojekt1\nMaster	DONE - Projekt4	DONE - Unterprojekt1	DONE - Punkt1\nMaster	DONE - Projekt4	DONE - Unterprojekt1\nMaster	DONE - Projekt4	DONE - Unterprojekt1	DONE - Punkt2\nMaster	DONE - Projekt4	DONE - Unterprojekt2\nMaster	DONE - Projekt4	DONE - Unterprojekt2	DONE - Punkt2 Ist: 10% 2h 29.01.2011-30.04.2012 __t2123__                                           [Plan:      3h 30.01.2011-30.03.2012]\nMaster	DONE - Projekt4	DONE - Unterprojekt2\nMaster	DONE - Projekt4	DONE - Unterprojekt2	DONE - Punkt1\nMaster	LATE - Projekt3\nMaster	LATE - Projekt3	LATE - Unterprojekt3\nMaster	LATE - Projekt3	LATE - Unterprojekt3	RUNNING - Punkt2\nMaster	LATE - Projekt3	LATE - Unterprojekt3\nMaster	LATE - Projekt3	LATE - Unterprojekt3	OFFEN - Punkt1\nMaster	LATE - Projekt3	RUNNING - Unterprojekt2\nMaster	LATE - Projekt3	RUNNING - Unterprojekt2	RUNNING - Punkt2\nMaster	LATE - Projekt3	LATE - Unterprojekt3\nMaster	LATE - Projekt3	LATE - Unterprojekt3	LATE - Punkt3\nMaster	LATE - Projekt3	OFFEN - Unterprojekt1\nMaster	LATE - Projekt3	OFFEN - Unterprojekt1	OFFEN - Punkt1\nMaster	LATE - Projekt3	LATE - Unterprojekt3\nMaster	LATE - Projekt3	LATE - Unterprojekt3	DONE - Punkt4\nMaster	LATE - Projekt3	RUNNING - Unterprojekt2\nMaster	LATE - Projekt3	RUNNING - Unterprojekt2	OFFEN - Punkt1\nMaster	LATE - Projekt3	OFFEN - Unterprojekt1\nMaster	LATE - Projekt3	OFFEN - Unterprojekt1	OFFEN - Punkt2\nMaster	LATE - Projekt3	DONE - Unterprojekt3\nMaster	LATE - Projekt3	DONE - Unterprojekt3	DONE - Punkt1\n";
        String delimiter = DEFAULT_ENTRY_DELIMITER;

        try {
            PPLImporter impFactory = new PPLImporter(null);

            // Mastererzuegen
            String masterName = "Master";
            DataDomain masterNode  = impFactory.createNodeObjFromText(1, masterName, masterName, null);

            // Daten parsen
            impFactory.extractNodesFromLines(masterNode, src, delimiter);

            // Master ausgeben
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Masternode.toString:" + masterNode);

            // formatiert ausgeben
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("ExportFactory Start:");
            OutputOptions oOptions = new OutputOptionsImpl();
            PPLExporter expFactory = new PPLExporter();
            String res = expFactory.getMasterNodeResult(masterNode, oOptions);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(res);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("ExportFactory End");

            // Test
            assertEquals(expected, res.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail("Failed with Exception:" + e);
        }


    }

}
