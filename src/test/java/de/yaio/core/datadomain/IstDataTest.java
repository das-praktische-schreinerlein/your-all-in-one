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
import de.yaio.datatransfer.exporter.formatter.IstDataFormatterImpl;
import de.yaio.datatransfer.importer.parser.IstDataParserImpl;
import de.yaio.datatransfer.importer.parser.Parser;

/**
 * <h4>FeatureDomain:</h4>
 *     Tests
 * <h4>FeatureDescription:</h4>
 *     test of the datadomain-logic: IstData<br>
 *     test: parser, formatter
 * 
 * @package de.yaio.core.datadomain
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category tests
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class IstDataTest extends DataDomainTest {
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Test
     * <h4>FeatureDescription:</h4>
     *     testobject for datadomain-logic: IstData
     * 
     * @package de.yaio.core.datadomain
     * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category tests
     * @copyright Copyright (c) 2014, Michael Schreiner
     * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class IstDataTestObj extends TaskNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getIstStand()).append("|")
                     .append(this.getIstAufwand()).append("|")
                     .append(this.getIstStart()).append("|")
                     .append(this.getIstEnde()).append("|")
                     ;
            return resBuffer.toString();
            
        }
    }

    @Override
    public void setupParser() throws Exception {
        parser = new IstDataParserImpl();
    }

    @Override
    public void setupFormatter() throws Exception {
        formatter = new IstDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return new IstDataTestObj();
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
    protected IstDataTestObj getNewIstDataTestObj() throws Exception  {
        return (IstDataTestObj)setupNewTestObj();
    }

    @Override
    public void testParser() throws Exception {
        IstDataTestObj mytestObj = null;
        String expected = null;

        // normal with date
        mytestObj = getNewIstDataTestObj();
        mytestObj.setName("Name [Ist: 30% 2h 12.03.2014-12.04.2014]");
        expected = "Name|30.0|2.0|Wed Mar 12 00:00:59 CET 2014|Sat Apr 12 00:00:59 CEST 2014|";
        testParser(mytestObj, expected, importOptions);

        // normal with datetime
        mytestObj = getNewIstDataTestObj();
        mytestObj.setName("Name [Ist: 30% 2h 12.03.2014 12:00-12.04.2014]");
        expected = "Name|30.0|2.0|Wed Mar 12 12:00:00 CET 2014|Sat Apr 12 00:00:59 CEST 2014|";
        testParser(mytestObj, expected, importOptions);

        // normal without startdate
        mytestObj = getNewIstDataTestObj();
        mytestObj.setName("Name [Ist: 30% 2h -12.04.2014]");
        expected = "Name|30.0|2.0|null|Sat Apr 12 00:00:59 CEST 2014|";
        testParser(mytestObj, expected, importOptions);

        // normal without enddate
        mytestObj = getNewIstDataTestObj();
        mytestObj.setName("Name [Ist: 30% 2h 12.03.2014 12:00-]");
        expected = "Name|30.0|2.0|Wed Mar 12 12:00:00 CET 2014|null|";
        testParser(mytestObj, expected, importOptions);

        // without date
        mytestObj = getNewIstDataTestObj();
        mytestObj.setName("Name [Ist:    30%     2h]");
        expected = "Name|30.0|2.0|null|null|";
        testParser(mytestObj, expected, importOptions);
    }

    @Override
    public void testFormatter() throws Exception {
        IstDataTestObj mytestObj = null;
        String expected = null;
        
        // without date
        mytestObj = getNewIstDataTestObj();
        mytestObj.setName("Name XX  ");
        mytestObj.setIstAufwand(2.0);
        mytestObj.setIstStand(30.0);
        expected = "                                                                                [Ist:  30%  2h]";
        testFormatter(mytestObj, expected, outputOptions);

        // without enddate
        mytestObj.setIstStart(Parser.DF.parse("12.03.2014"));
        expected = "                                                                                [Ist:  30%  2h 12.03.2014 00:00-          ]";
        testFormatter(mytestObj, expected, outputOptions);

        // without startdate
        mytestObj.setIstStart(null);
        mytestObj.setIstEnde(Parser.DF.parse("12.04.2014"));
        expected = "                                                                                [Ist:  30%  2h           -12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);
        
        // with date
        mytestObj.setIstStart(Parser.DF.parse("12.03.2014"));
        mytestObj.setIstEnde(Parser.DF.parse("12.04.2014"));
        expected = "                                                                                [Ist:  30%  2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Func
        outputOptions.setIntendFuncArea(10);
        expected = "          [Ist:  30%  2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // not intend Dates usw.
        outputOptions.setFlgDoIntend(true);
        expected = "          [Ist:  30%  2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Sum usw.
        outputOptions.setFlgIntendSum(true);
        outputOptions.setFlgShowChildrenSum(true);
        expected = "          [Ist:     30%  2h 12.03.2014 00:00-12.04.2014 00:00]";
        testFormatter(mytestObj, expected, outputOptions);

        // dont show
        outputOptions.setAllFlgShow(false);
        expected = "";
        testFormatter(mytestObj, expected, outputOptions);
    }
}
