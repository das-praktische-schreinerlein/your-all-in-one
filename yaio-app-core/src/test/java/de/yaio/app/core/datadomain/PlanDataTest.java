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
package de.yaio.app.core.datadomain;

import de.yaio.app.core.node.TaskNode;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.exporter.formatter.PlanDataFormatterImpl;
import de.yaio.app.datatransfer.importer.parser.PlanDataParserImpl;

import java.text.ParseException;

/** 
 * test of the datadomain-logic: PlanData<br>
 * test: parser, formatter
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class PlanDataTest extends DataDomainTest {
    
    /** 
     * testobject for datadomain-logic: PlanData
     * 
     * @package                      de.yaio.core.datadomain
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class PlanDataTestObj extends TaskNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getPlanAufwand()).append("|")
                     .append(formatDateForCheck(this.getPlanStart())).append("|")
                     .append(formatDateForCheck(this.getPlanEnde())).append("|");
            return resBuffer.toString();
            
        }
    }

    @Override
    public void setupParser() {
        parser = new PlanDataParserImpl();
    }

    @Override
    public void setupFormatter() {
        formatter = new PlanDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() {
        return new PlanDataTestObj();
    }
    
    /** 
     * setup the a TestObj for the test
     * @return                       a new dataobj for the test
     **/
    protected PlanDataTestObj getNewPlanDataTestObj() {
        return (PlanDataTestObj) setupNewTestObj();
    }

    @Override
    public void testParser() throws ParserException {
        PlanDataTestObj mytestObj = null;
        String expected = null;


        // normal with date
        mytestObj = getNewPlanDataTestObj();
        mytestObj.setName("Name [Plan: 2h 12.03.2014-12.04.2014]");
        expected = "Name|2.0|12.03.2014 00:00|12.04.2014 00:00|";
        testParser(mytestObj, expected, importOptions);

        // normal with datetime
        mytestObj = getNewPlanDataTestObj();
        mytestObj.setName("Name [Plan: 2h 12.03.2014 12:00-12.04.2014]");
        expected = "Name|2.0|12.03.2014 12:00|12.04.2014 00:00|";
        testParser(mytestObj, expected, importOptions);

        // normal without startdate
        mytestObj = getNewPlanDataTestObj();
        mytestObj.setName("Name [Plan: 2h -12.04.2014]");
        expected = "Name|2.0|null|12.04.2014 00:00|";
        testParser(mytestObj, expected, importOptions);

        // normal without enddate
        mytestObj = getNewPlanDataTestObj();
        mytestObj.setName("Name [Plan: 2h 12.03.2014 12:00-]");
        expected = "Name|2.0|12.03.2014 12:00|null|";
        testParser(mytestObj, expected, importOptions);

        // without date
        mytestObj = getNewPlanDataTestObj();
        mytestObj.setName("Name [Plan:    2h]");
        expected = "Name|2.0|null|null|";
        testParser(mytestObj, expected, importOptions);
    }

    @Override
    public void testFormatter() throws ParseException {
        PlanDataTestObj mytestObj = null;
        String expected = null;
        
        // without date
        mytestObj = getNewPlanDataTestObj();
        mytestObj.setName("Name XX  ");
        mytestObj.setPlanAufwand(2.0);
        expected = "                                                                                [Plan:      2h]";
        testFormatter(mytestObj, expected, outputOptions);

        // without enddate
        mytestObj.setPlanStart(DF.parse("12.03.2014"));
        expected = "                                                                                [Plan:      2h 12.03.2014 00:00-          ]";
        testFormatter(mytestObj, expected, outputOptions);

        // without startdate
        mytestObj.setPlanStart(null);
        mytestObj.setPlanEnde(DF.parse("12.04.2014"));
        expected = "                                                                                [Plan:      2h           -12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);
        
        // with date
        mytestObj.setPlanStart(DF.parse("12.03.2014"));
        mytestObj.setPlanEnde(DF.parse("12.04.2014"));
        expected = "                                                                                [Plan:      2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Func
        outputOptions.setIntendFuncArea(10);
        expected = "          [Plan:      2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // not intend Dates usw.
        outputOptions.setFlgDoIntend(true);
        expected = "          [Plan:      2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Sum usw.
        outputOptions.setFlgIntendSum(true);
        outputOptions.setFlgShowChildrenSum(true);
        expected = "          [Plan:         2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // dont show
        outputOptions.setAllFlgShow(false);
        expected = "";
        testFormatter(mytestObj, expected, outputOptions);
    }
}
