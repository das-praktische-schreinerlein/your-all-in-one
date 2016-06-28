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
import de.yaio.app.datatransfer.exporter.formatter.IstChildrenSumDataFormatterImpl;

/** 
 * test of the datadomain-logic: IstChildrenSumData<br>
 * test: parser, formatter
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class IstChildrenSumDataTest extends DataDomainTest {
    
    /** 
     * testobject for datadomain-logic: IstChildrenSumData
     * 
     * @package                      de.yaio.core.datadomain
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class IstChildrenSumDataTestObj extends TaskNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getIstChildrenSumStand()).append("|")
                     .append(this.getIstChildrenSumAufwand()).append("|")
                     .append(this.getIstChildrenSumStart()).append("|")
                     .append(this.getIstChildrenSumEnde()).append("|");
            return resBuffer.toString();
            
        }
    }

    @Override
    public void setupParser() throws Exception {
    }

    @Override
    public void setupFormatter() throws Exception {
        outputOptions.setFlgShowChildrenSum(true);
        formatter = new IstChildrenSumDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return new IstChildrenSumDataTestObj();
    }
    
    /** 
     * setup the a TestObj for the test
     * @return                       a new dataobj for the test
     * @throws Exception             possible Exception     */
    protected IstChildrenSumDataTestObj getNewIstChildrenSumDataTestObj() throws Exception  {
        return (IstChildrenSumDataTestObj) setupNewTestObj();
    }

    @Override
    public void testParser() throws Exception {
    }

    @Override
    public void testFormatter() throws Exception {
        IstChildrenSumDataTestObj mytestObj = null;
        String expected = null;
        
        // without date
        mytestObj = getNewIstChildrenSumDataTestObj();
        mytestObj.setName("Name XX  ");
        mytestObj.setIstChildrenSumAufwand(2.0);
        mytestObj.setIstChildrenSumStand(30.0);
        expected = "                                                                                [IstSum:  30%  2h]";
        testFormatter(mytestObj, expected, outputOptions);

        // without enddate
        mytestObj.setIstChildrenSumStart(DF.parse("12.03.2014"));
        expected = "                                                                                [IstSum:  30%  2h 12.03.2014 00:00-          ]";
        testFormatter(mytestObj, expected, outputOptions);

        // without startdate
        mytestObj.setIstChildrenSumStart(null);
        mytestObj.setIstChildrenSumEnde(DF.parse("12.04.2014"));
        expected = "                                                                                [IstSum:  30%  2h           -12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);
        
        // with date
        mytestObj.setIstChildrenSumStart(DF.parse("12.03.2014"));
        mytestObj.setIstChildrenSumEnde(DF.parse("12.04.2014"));
        expected = "                                                                                [IstSum:  30%  2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Func
        outputOptions.setIntendFuncArea(10);
        expected = "          [IstSum:  30%  2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // not intend Dates usw.
        outputOptions.setFlgDoIntend(false);
        expected = "          [IstSum: 30% 2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Sum usw.
        outputOptions.setFlgIntendSum(true);
        expected = "          [IstSum:    30% 2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // dont show
        outputOptions.setAllFlgShow(false);
        expected = "";
        testFormatter(mytestObj, expected, outputOptions);
    }
}
