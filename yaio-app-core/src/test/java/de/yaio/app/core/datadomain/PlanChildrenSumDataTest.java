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
import de.yaio.app.datatransfer.exporter.formatter.PlanChildrenSumDataFormatterImpl;

import java.text.ParseException;

/** 
 * test of the datadomain-logic: PlanChildrenSumData<br>
 * test: parser, formatter
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class PlanChildrenSumDataTest extends DataDomainTest {
    
    /** 
     * testobject for datadomain-logic: PlanChildrenSumData
     * 
     * @package                      de.yaio.core.datadomain
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class PlanChildrenSumDataTestObj extends TaskNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getPlanChildrenSumAufwand()).append("|")
                     .append(this.getPlanChildrenSumStart()).append("|")
                     .append(this.getPlanChildrenSumEnde()).append("|");
            return resBuffer.toString();
            
        }
    }

    @Override
    public void setupParser() {
    }

    @Override
    public void setupFormatter() {
        outputOptions.setFlgShowChildrenSum(true);
        formatter = new PlanChildrenSumDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() {
        return new PlanChildrenSumDataTestObj();
    }
    
    /** 
     * setup the a TestObj for the test
     * @return                       a new dataobj for the test
     **/
    protected PlanChildrenSumDataTestObj getNewPlanChildrenSumDataTestObj() {
        return (PlanChildrenSumDataTestObj) setupNewTestObj();
    }

    @Override
    public void testParser() {
    }

    @Override
    public void testFormatter() throws ParseException {
        PlanChildrenSumDataTestObj mytestObj = null;
        String expected = null;
        
        // without date
        mytestObj = getNewPlanChildrenSumDataTestObj();
        mytestObj.setName("Name XX  ");
        mytestObj.setPlanChildrenSumAufwand(2.0);
        expected = "                                                                                [PlanSum:      2h]";
        testFormatter(mytestObj, expected, outputOptions);

        // without enddate
        mytestObj.setPlanChildrenSumStart(DF.parse("12.03.2014"));
        expected = "                                                                                [PlanSum:      2h 12.03.2014 00:00-          ]";
        testFormatter(mytestObj, expected, outputOptions);

        // without startdate
        mytestObj.setPlanChildrenSumStart(null);
        mytestObj.setPlanChildrenSumEnde(DF.parse("12.04.2014"));
        expected = "                                                                                [PlanSum:      2h           -12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);
        
        // with date
        mytestObj.setPlanChildrenSumStart(DF.parse("12.03.2014"));
        mytestObj.setPlanChildrenSumEnde(DF.parse("12.04.2014"));
        expected = "                                                                                [PlanSum:      2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Func
        outputOptions.setIntendFuncArea(10);
        expected = "          [PlanSum:      2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // not intend Dates usw.
        outputOptions.setFlgDoIntend(true);
        expected = "          [PlanSum:      2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Sum usw.
        outputOptions.setFlgIntendSum(true);
        outputOptions.setFlgShowChildrenSum(true);
        expected = "          [PlanSum:         2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // dont show
        outputOptions.setAllFlgShow(false);
        expected = "";
        testFormatter(mytestObj, expected, outputOptions);
    }
}
