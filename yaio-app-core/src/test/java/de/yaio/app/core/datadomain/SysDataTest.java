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

import de.yaio.app.core.node.InfoNode;
import de.yaio.app.datatransfer.exporter.formatter.SysDataFormatterImpl;
import de.yaio.app.datatransfer.importer.parser.SysDataParserImpl;

/** 
 * test of the datadomain-logic: SysData<br>
 * test: parser, formatter
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class SysDataTest extends DataDomainTest {
    
    /** 
     * testobject for datadomain-logic: SysData
     * 
     * @package                      de.yaio.core.datadomain
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class SysDataTestObj extends InfoNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getSysUID()).append("|")
                     .append(this.getSysCreateDate()).append("|")
                     .append(this.getSysCurChecksum()).append("|")
                     .append(this.getSysChangeDate()).append("|")
                     .append(this.getSysChangeCount()).append("|");
            return resBuffer.toString();
            
        }
    }

    @Override
    public void setupParser() throws Exception {
        parser = new SysDataParserImpl();
    }

    @Override
    public void setupFormatter() throws Exception {
        formatter = new SysDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return new SysDataTestObj();
    }
    
    
    /** 
     * setup the a TestObj for the test
     * @return                       a new dataobj for the test
     * @throws Exception             possible Exception     */
    protected SysDataTestObj getNewSysDataTestObj() throws Exception  {
        return (SysDataTestObj) setupNewTestObj();
    }

    @Override
    public void testParser() throws Exception {
        SysDataTestObj mytestObj = null;
        String expected = null;

        // normal
        mytestObj = getNewSysDataTestObj();
        mytestObj.setName("Name [NodeSys: DT201404081141327673,08.04.2014 11:41,BD87EF8392D672FE28B4D861A43A9F5F,10.04.2014 10:04,9]");
        expected = "Name|DT201404081141327673|Tue Apr 08 11:41:00 CEST 2014|BD87EF8392D672FE28B4D861A43A9F5F|Thu Apr 10 10:04:00 CEST 2014|9|";
        testParser(mytestObj, expected, importOptions);
    }

    @Override
    public void testFormatter() throws Exception {
        SysDataTestObj mytestObj = null;
        String expected = null;
        
        // full
        mytestObj = getNewSysDataTestObj();
        mytestObj.setName("Name XX  ");
        mytestObj.setSysUID("DT201404081141327673");
        mytestObj.setSysCreateDate(DTF.parse("08.04.2014 11:41"));
        mytestObj.setSysCurChecksum("BD87EF8392D672FE28B4D861A43A9F5F");
        mytestObj.setSysChangeDate(DTF.parse("10.04.2014 10:04"));
        mytestObj.setSysChangeCount(9);
        expected = "                                                                                                                                                                [NodeSys: DT201404081141327673,08.04.2014 11:41,BD87EF8392D672FE28B4D861A43A9F5F,10.04.2014 10:04,9]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Sys
        outputOptions.setIntendSys(10);
        expected = "          [NodeSys: DT201404081141327673,08.04.2014 11:41,BD87EF8392D672FE28B4D861A43A9F5F,10.04.2014 10:04,9]";
        testFormatter(mytestObj, expected, outputOptions);

        // dont show
        outputOptions.setAllFlgShow(false);
        expected = "";
        testFormatter(mytestObj, expected, outputOptions);
    }
}
