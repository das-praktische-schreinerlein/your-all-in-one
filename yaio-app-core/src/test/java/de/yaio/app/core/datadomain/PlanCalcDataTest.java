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
import de.yaio.app.datatransfer.exporter.formatter.PlanCalcDataFormatterImpl;
import de.yaio.app.datatransfer.importer.parser.PlanCalcDataParserImpl;

import java.text.ParseException;

/** 
 * test of the datadomain-logic: PlanCalcData<br>
 * test: parser, formatter
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class PlanCalcDataTest extends DataDomainTest {
    
    /** 
     * testobject for datadomain-logic: PlanCalcData
     * 
     * @package                      de.yaio.core.datadomain
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class PlanCalcDataTestObj extends TaskNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(formatDateForCheck(this.getPlanCalcStart())).append("|")
                     .append(formatDateForCheck(this.getPlanCalcEnde())).append("|");
            return resBuffer.toString();
            
        }
    }

    @Override
    public void setupParser() {
        parser = new PlanCalcDataParserImpl();
    }

    @Override
    public void setupFormatter() {
        formatter = new PlanCalcDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() {
        return new PlanCalcDataTestObj();
    }
    
    /** 
     * setup the a TestObj for the test
     * @return                       a new dataobj for the test
     **/
    protected PlanCalcDataTestObj getNewPlanCalcDataTestObj() {
        return (PlanCalcDataTestObj) setupNewTestObj();
    }

    @Override
    public void testParser() throws ParserException {
        PlanCalcDataTestObj mytestObj = null;
        String expected = null;

        // normal with date
        mytestObj = getNewPlanCalcDataTestObj();
        mytestObj.setName("Name [PlanCalc: 12.03.2014-12.04.2014]");
        expected = "Name|12.03.2014 00:00|12.04.2014 00:00|";
        testParser(mytestObj, expected, importOptions);

        // normal with datetime
        mytestObj = getNewPlanCalcDataTestObj();
        mytestObj.setName("Name [PlanCalc: 12.03.2014 12:00-12.04.2014]");
        expected = "Name|12.03.2014 12:00|12.04.2014 00:00|";
        testParser(mytestObj, expected, importOptions);

        // normal without startdate
        mytestObj = getNewPlanCalcDataTestObj();
        mytestObj.setName("Name [PlanCalc: -12.04.2014]");
        expected = "Name|null|12.04.2014 00:00|";
        testParser(mytestObj, expected, importOptions);

        // normal without enddate
        mytestObj = getNewPlanCalcDataTestObj();
        mytestObj.setName("Name [PlanCalc: 12.03.2014 12:00-]");
        expected = "Name|12.03.2014 12:00|null|";
        testParser(mytestObj, expected, importOptions);

        // without date
        mytestObj = getNewPlanCalcDataTestObj();
        mytestObj.setName("Name [PlanCalc:    ]");
        expected = "Name [PlanCalc:    ]|null|null|";
        testParser(mytestObj, expected, importOptions);
    }

    @Override
    public void testFormatter() throws ParseException {
        PlanCalcDataTestObj mytestObj = null;
        String expected = null;
        
        outputOptions.setFlgShowPlanCalc(true);
        
        // without date
        mytestObj = getNewPlanCalcDataTestObj();
        mytestObj.setName("Name XX  ");
        expected = "";
        testFormatter(mytestObj, expected, outputOptions);

        // without enddate
        mytestObj.setPlanCalcStart(DF.parse("12.03.2014"));
        expected = "                                                                                [PlanCalc: 12.03.2014 00:00-          ]";
        testFormatter(mytestObj, expected, outputOptions);

        // without startdate
        mytestObj.setPlanCalcStart(null);
        mytestObj.setPlanCalcEnde(DF.parse("12.04.2014"));
        expected = "                                                                                [PlanCalc:           -12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);
        
        // with date
        mytestObj.setPlanCalcStart(DF.parse("12.03.2014"));
        mytestObj.setPlanCalcEnde(DF.parse("12.04.2014"));
        expected = "                                                                                [PlanCalc: 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Func
        outputOptions.setIntendFuncArea(10);
        expected = "          [PlanCalc: 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // not intend Dates usw.
        outputOptions.setFlgDoIntend(true);
        expected = "          [PlanCalc: 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Sum usw.
        outputOptions.setFlgIntendSum(true);
        outputOptions.setFlgShowChildrenSum(true);
        expected = "          [PlanCalc: 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // dont show
        outputOptions.setAllFlgShow(false);
        expected = "";
        testFormatter(mytestObj, expected, outputOptions);
    }
}
