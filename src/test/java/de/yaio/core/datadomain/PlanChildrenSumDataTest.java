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
package de.yaio.core.datadomain;

import de.yaio.core.node.TaskNode;
import de.yaio.datatransfer.exporter.formatter.PlanChildrenSumDataFormatterImpl;
import de.yaio.datatransfer.importer.parser.Parser;

/**
 * <h4>FeatureDomain:</h4>
 *     Tests
 * <h4>FeatureDescription:</h4>
 *     test of the datadomain-logic: PlanChildrenSumData<br>
 *     test: parser, formatter
 * 
 * @package de.yaio.core.datadomain
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category tests
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class PlanChildrenSumDataTest extends DataDomainTest {
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Test
     * <h4>FeatureDescription:</h4>
     *     testobject for datadomain-logic: PlanChildrenSumData
     * 
     * @package de.yaio.core.datadomain
     * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category tests
     * @copyright Copyright (c) 2014, Michael Schreiner
     * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class PlanChildrenSumDataTestObj extends TaskNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getPlanChildrenSumAufwand()).append("|")
                     .append(this.getPlanChildrenSumStart()).append("|")
                     .append(this.getPlanChildrenSumEnde()).append("|")
                     ;
            return resBuffer.toString();
            
        }
    }

    @Override
    public void setupParser() throws Exception {
    }

    @Override
    public void setupFormatter() throws Exception {
        outputOptions.setFlgShowChildrenSum(true);
        formatter = new PlanChildrenSumDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return new PlanChildrenSumDataTestObj();
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     setup the a TestObj for the test
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnvalue TestObj - the dataobj for the test
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Test Config Initialisation
     * @return - a new dataobj for the test
     * @throws Exception
     */
    protected PlanChildrenSumDataTestObj getNewPlanChildrenSumDataTestObj() throws Exception  {
        return (PlanChildrenSumDataTestObj) setupNewTestObj();
    }

    @Override
    public void testParser() throws Exception {
    }

    @Override
    public void testFormatter() throws Exception {
        PlanChildrenSumDataTestObj mytestObj = null;
        String expected = null;
        
        // without date
        mytestObj = getNewPlanChildrenSumDataTestObj();
        mytestObj.setName("Name XX  ");
        mytestObj.setPlanChildrenSumAufwand(2.0);
        expected = "                                                                                [PlanSum:      2h]";
        testFormatter(mytestObj, expected, outputOptions);

        // without enddate
        mytestObj.setPlanChildrenSumStart(Parser.DF.parse("12.03.2014"));
        expected = "                                                                                [PlanSum:      2h 12.03.2014 00:00-          ]";
        testFormatter(mytestObj, expected, outputOptions);

        // without startdate
        mytestObj.setPlanChildrenSumStart(null);
        mytestObj.setPlanChildrenSumEnde(Parser.DF.parse("12.04.2014"));
        expected = "                                                                                [PlanSum:      2h           -12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);
        
        // with date
        mytestObj.setPlanChildrenSumStart(Parser.DF.parse("12.03.2014"));
        mytestObj.setPlanChildrenSumEnde(Parser.DF.parse("12.04.2014"));
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
