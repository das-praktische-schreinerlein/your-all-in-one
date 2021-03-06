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
package de.yaio.app.core.datadomainservice;

import de.yaio.app.core.node.TaskNode;
import de.yaio.app.core.nodeservice.NodeService;
import de.yaio.app.core.nodeservice.NodeServiceImpl;

import java.text.ParseException;
import java.util.Date;

/** 
 * Testsuite for the datadomainservice-logic: SysData<br>
 * tests: CreateDate, LastChanges, Checksum.
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomainservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class SysDataServiceTest extends DataDomainServiceTest {
    
    /** 
     * testobject for datadomain-logic: SysData
     * 
     * @package                      de.yaio.core.datadomainservice
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class SysDataTestObj extends TaskNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
            .append(this.getSysUID()).append("|")
            .append(formatDateForCheck(this.getSysCreateDate())).append("|")
            .append(this.getSysCurChecksum()).append("|")
            .append(formatDateForCheck(this.getSysChangeDate())).append("|")
            .append(this.getSysChangeCount()).append("|");
            return resBuffer.toString();
        }
    }

    @Override
    public TestObj setupNewTestObj() {
        return new SysDataTestObj();
    }
    
    /** 
     * setup the a TestObj for the test
     * @return                       a new dataobj for the test
     **/
    protected SysDataTestObj getNewSysDataTestObj() {
        return (SysDataTestObj) setupNewTestObj();
    }

    @Override
    public void setupDataDomainService() {
        NodeServiceImpl nodeService = (NodeServiceImpl) TaskNode.getConfiguredNodeService();
        dataDomainService = nodeService.hshDataDomainRecalcerByClass.get(SysDataServiceImpl.class);
    }

    @Override
    public void testServiceDoRecalc() {
        try {
            // init Vars
            SysDataTestObj myDataDomainObj = null;
            String expectedAfterDoBeforeChildren = null;
            String expectedAfterDoAfterChildren = null;
            NodeService.RecalcRecurseDirection recurseDirection = NodeService.RecalcRecurseDirection.CHILDREN;

            // check sys
            myDataDomainObj = getNewSysDataTestObj();
            expectedAfterDoBeforeChildren = "Name XX  |DT201404081141327673|08.04.2014 11:41|2586BA8C5D9B1C3D1DB52F90086EB844|10.04.2014 10:04|9|";
            expectedAfterDoAfterChildren = "Name XX  |DT201404081141327673|08.04.2014 11:41|2586BA8C5D9B1C3D1DB52F90086EB844|10.04.2014 10:04|9|";
            myDataDomainObj.setName("Name XX  ");
            myDataDomainObj.setSysUID("DT201404081141327673");
            myDataDomainObj.setSysCreateDate(DTF.parse("08.04.2014 11:41"));
            myDataDomainObj.setSysCurChecksum("2586BA8C5D9B1C3D1DB52F90086EB844");
            myDataDomainObj.setSysChangeDate(DTF.parse("10.04.2014 10:04"));
            myDataDomainObj.setSysChangeCount(9);
            this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren,
                    expectedAfterDoAfterChildren, recurseDirection);

            // new record (passes only on fast computers ;-)
            myDataDomainObj = getNewSysDataTestObj();
            expectedAfterDoBeforeChildren = "Name XX2|DT201404081141327673|" + formatDateForCheck(new Date()) + "|E7FA4150920D398AC180C7EEA1E14676|" + formatDateForCheck(new Date()) + "|1|";
            expectedAfterDoAfterChildren = expectedAfterDoBeforeChildren;
            myDataDomainObj.setName("Name XX2");
            myDataDomainObj.setSysUID("DT201404081141327673"); // cant be computed because of Milliseconds
            this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren,
                    expectedAfterDoAfterChildren, recurseDirection);

            // with changes (passes only on fast computers ;-)
            myDataDomainObj = getNewSysDataTestObj();
            expectedAfterDoBeforeChildren = "Name XX2|DT201404081141327673|08.04.2014 11:41|459FB28B91E59689E400563665B6B513|10.04.2014 10:04|9|";
            expectedAfterDoAfterChildren = "Name XX2|DT201404081141327673|08.04.2014 11:41|E7FA4150920D398AC180C7EEA1E14676|" + formatDateForCheck(new Date()) + "|10|";
            myDataDomainObj.setName("Name XX2");
            myDataDomainObj.setSysUID("DT201404081141327673");
            myDataDomainObj.setSysCreateDate(DTF.parse("08.04.2014 11:41"));
            myDataDomainObj.setSysCurChecksum("459FB28B91E59689E400563665B6B513");
            myDataDomainObj.setSysChangeDate(DTF.parse("10.04.2014 10:04"));
            myDataDomainObj.setSysChangeCount(9);
            this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren,
                    expectedAfterDoAfterChildren, recurseDirection);
        } catch (ParseException ex) {
            throw new IllegalStateException("parsing static dates failed", ex);
        }
    }
}
