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
package de.yaio.core.datadomainservice;

import de.yaio.core.node.TaskNode;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.core.nodeservice.NodeServiceImpl;

/** 
 * Testsuite for the datadomainservice-logic: StatData<br>
 * tests: rdecalc state and planCalcSum...
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomainservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class StatDataServiceTest extends DataDomainServiceTest {
    
    /** 
     * testobject for datadomain-logic: StatData
     * 
     * @package                      de.yaio.core.datadomainservice
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class StatDataTestObj extends TaskNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getStatChildNodeCount()).append("|")
                     .append(this.getStatWorkflowCount()).append("|")
                     .append(this.getStatWorkflowTodoCount());
            return resBuffer.toString();
            
        }
    }

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return new StatDataTestObj();
    }
    
    /** 
     * setup the a TestObj for the test
     * @return                       a new dataobj for the test
     * @throws Exception             possible Exception     */
    protected StatDataTestObj getNewStatDataTestObj() throws Exception  {
        return (StatDataTestObj) setupNewTestObj();
    }


    @Override
    public void setupDataDomainService() throws Exception {
        NodeServiceImpl nodeService = (NodeServiceImpl) TaskNode.getConfiguredNodeService();
        dataDomainService = nodeService.hshDataDomainRecalcerByClass.get(StatDataServiceImpl.class);

//        dataDomainService = new StatDataServiceImpl(); 
//        // clear DomainRecalcerConfig to prevent obscure effects
//        nodeService.hshDataDomainRecalcer.clear();
//        nodeService.addDataDomainRecalcer(dataDomainService);

    }

    @Override
    public void testServiceDoRecalc() throws Exception {
        // init Vars
        StatDataTestObj myDataDomainObj = null; 
        StatDataTestObj myDataDomainObj2 = null;
        StatDataTestObj myDataDomainObj3 = null;
        String expectedAfterDoBeforeChildren = null;
        String expectedAfterDoAfterChildren = null;
        int recurseDirection = NodeService.CONST_RECURSE_DIRECTION_CHILDREN;
        
        // empty BaseWorkflow calculate UNKNOWN without %
        myDataDomainObj = getNewStatDataTestObj(); 
        myDataDomainObj.setState("OFFEN");
        expectedAfterDoBeforeChildren = "null|null|null|null";
        expectedAfterDoAfterChildren = "null|0|1|1";
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);

        // BaseWorkflow calculate ERLEDIGT if 100%
        myDataDomainObj = getNewStatDataTestObj(); 
        myDataDomainObj.setState("ERLEDIGT");
        expectedAfterDoBeforeChildren = "null|null|null|null";
        expectedAfterDoAfterChildren = "null|0|1|0";
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);

        // check if children data are calculated
        myDataDomainObj = getNewStatDataTestObj();
        myDataDomainObj.setEbene(1);
        myDataDomainObj.setState("OFFEN");
        myDataDomainObj.setPlanAufwand(50.0);
        myDataDomainObj.setIstStand(0.0);
        myDataDomainObj.setSrcName("test");
        myDataDomainObj2 = getNewStatDataTestObj();
        myDataDomainObj2.setSrcName("test2");
        myDataDomainObj2.setState("RUNNING");
        myDataDomainObj2.setPlanAufwand(50.0);
        myDataDomainObj2.setIstStand(50.0);
        myDataDomainObj2.setEbene(2);
        myDataDomainObj2.setParentNode(myDataDomainObj);
        myDataDomainObj3 = getNewStatDataTestObj();
        myDataDomainObj3.setSrcName("test3");
        myDataDomainObj3.setState("ERLEDIGT");
        myDataDomainObj3.setPlanAufwand(50.0);
        myDataDomainObj3.setIstStand(100.0);
        myDataDomainObj3.setEbene(2);
        myDataDomainObj3.setParentNode(myDataDomainObj2);
        expectedAfterDoBeforeChildren = "null|null|null|null";
        expectedAfterDoAfterChildren = "null|2|3|2";
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);

    }
}
