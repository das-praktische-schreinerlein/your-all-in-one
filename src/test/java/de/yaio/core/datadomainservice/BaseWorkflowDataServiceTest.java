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
package de.yaio.core.datadomainservice;

import de.yaio.core.node.TaskNode;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.core.nodeservice.NodeServiceImpl;
import de.yaio.datatransfer.importer.parser.Parser;

/**
 * <h4>FeatureDomain:</h4>
 *     Tests
 * <h4>FeatureDescription:</h4>
 *     Testsuite for the datadomainservice-logic: BaseWorkflowData<br>
 *     tests: rdecalc state and planCalcSum...
 * 
 * @package de.yaio.core.datadomainservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category tests
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class BaseWorkflowDataServiceTest extends DataDomainServiceTest {
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Test
     * <h4>FeatureDescription:</h4>
     *     testobject for datadomain-logic: BaseWorkflowData
     * 
     * @package de.yaio.core.datadomainservice
     * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category tests
     * @copyright Copyright (c) 2014, Michael Schreiner
     * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class BaseWorkflowDataTestObj extends TaskNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getState()).append("|")
                     .append(this.getIstStand()).append("|")
                     .append(this.getIstAufwand()).append("|")
                     .append(this.getIstStart()).append("|")
                     .append(this.getIstEnde()).append("|")
                     .append(this.getIstChildrenSumStand()).append("|")
                     .append(this.getIstChildrenSumAufwand()).append("|")
                     .append(this.getIstChildrenSumStart()).append("|")
                     .append(this.getIstChildrenSumEnde()).append("|")
                     .append(this.getPlanAufwand()).append("|")
                     .append(this.getPlanStart()).append("|")
                     .append(this.getPlanEnde()).append("|")
                     .append(this.getPlanChildrenSumAufwand()).append("|")
                     .append(this.getPlanChildrenSumStart()).append("|")
                     .append(this.getPlanChildrenSumEnde()).append("|")
                     ;
            return resBuffer.toString();
            
        }
    }

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return new BaseWorkflowDataTestObj();
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
    protected BaseWorkflowDataTestObj getNewBaseWorkflowDataTestObj() throws Exception  {
        return (BaseWorkflowDataTestObj)setupNewTestObj();
    }


    @Override
    public void setupDataDomainService() throws Exception {
        NodeServiceImpl nodeService = (NodeServiceImpl)TaskNode.getConfiguredNodeService();
        dataDomainService = nodeService.hshDataDomainRecalcerByClass.get(BaseWorkflowDataServiceImpl.class);

//        dataDomainService = new BaseWorkflowDataServiceImpl(); 
//        // clear DomainRecalcerConfig to prevent obscure effects
//        nodeService.hshDataDomainRecalcer.clear();
//        nodeService.addDataDomainRecalcer(dataDomainService);

    }

    @Override
    public void testServiceDoRecalc() throws Exception {
        // init Vars
        BaseWorkflowDataTestObj myDataDomainObj = null; 
        BaseWorkflowDataTestObj myDataDomainObj2 = null;
        BaseWorkflowDataTestObj myDataDomainObj3 = null;
        String expectedAfterDoBeforeChildren = null;
        String expectedAfterDoAfterChildren = null;
        int recurseDirection = NodeService.CONST_RECURSE_DIRECTION_CHILDREN;
        
        // empty BaseWorkflow calculate UNKNOWN without %
        myDataDomainObj = getNewBaseWorkflowDataTestObj(); 
        expectedAfterDoBeforeChildren = "null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|";
        expectedAfterDoAfterChildren = "null|UNKNOWN|null|null|null|null|null|null|null|null|null|null|null|null|null|null|";
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);

        // BaseWorkflow calculate ERLEDIGT if 100%
        myDataDomainObj = getNewBaseWorkflowDataTestObj(); 
        myDataDomainObj.setIstStand(100.0);
        expectedAfterDoBeforeChildren = "null|null|100.0|null|null|null|null|null|null|null|null|null|null|null|null|null|";
        expectedAfterDoAfterChildren = "null|ERLEDIGT|100.0|null|null|null|100.0|null|null|null|null|null|null|null|null|null|";
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);

        // BaseWorkflow calculate OFFEN if 0% but plan
        myDataDomainObj = getNewBaseWorkflowDataTestObj(); 
        myDataDomainObj.setPlanAufwand(50.0);
        expectedAfterDoBeforeChildren = "null|null|null|null|null|null|null|null|null|null|50.0|null|null|null|null|null|";
        expectedAfterDoAfterChildren = "null|OFFEN|null|null|null|null|0.0|null|null|null|50.0|null|null|50.0|null|null|";
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);

        // BaseWorkflow calculate OFFEN if 50%
        myDataDomainObj = getNewBaseWorkflowDataTestObj(); 
        myDataDomainObj.setIstStand(50.0);
        expectedAfterDoBeforeChildren = "null|null|50.0|null|null|null|null|null|null|null|null|null|null|null|null|null|";
        expectedAfterDoAfterChildren = "null|RUNNING|50.0|null|null|null|50.0|null|null|null|null|null|null|null|null|null|";
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);

        // check if children data are calculated
        myDataDomainObj = getNewBaseWorkflowDataTestObj();
        myDataDomainObj.setEbene(1);
        myDataDomainObj.setPlanAufwand(50.0);
        myDataDomainObj.setState("OFFEN");
        myDataDomainObj2 = getNewBaseWorkflowDataTestObj();
        myDataDomainObj2.setSrcName("test2");
        myDataDomainObj2.setEbene(2);
        myDataDomainObj2.setIstStand(45.0);
        myDataDomainObj2.setIstAufwand(10.0);
        myDataDomainObj2.setPlanAufwand(30.0);
        myDataDomainObj2.setParentNode(myDataDomainObj);
        myDataDomainObj3 = getNewBaseWorkflowDataTestObj();
        myDataDomainObj3.setSrcName("test3");
        myDataDomainObj3.setEbene(2);
        myDataDomainObj3.setPlanAufwand(20.0);
        myDataDomainObj3.setPlanStart(Parser.DF.parse("22.10.2013"));
        myDataDomainObj3.setParentNode(myDataDomainObj);
        expectedAfterDoBeforeChildren = "null|OFFEN|null|null|null|null|null|null|null|null|50.0|null|null|null|null|null|";
        expectedAfterDoAfterChildren = "null|LATE|null|null|null|null|13.5|10.0|null|null|50.0|null|null|100.0|Tue Oct 22 00:00:00 CEST 2013|null|";
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);

        // check if children data are calculated (no aufwand at all but children are done)
        myDataDomainObj = getNewBaseWorkflowDataTestObj();
        myDataDomainObj.setEbene(1);
        myDataDomainObj.setState("OFFEN");
        myDataDomainObj2 = getNewBaseWorkflowDataTestObj();
        myDataDomainObj2.setSrcName("test2");
        myDataDomainObj2.setEbene(2);
        myDataDomainObj2.setIstStand(100.0);
        myDataDomainObj2.setParentNode(myDataDomainObj);
        expectedAfterDoBeforeChildren = "null|OFFEN|null|null|null|null|null|null|null|null|null|null|null|null|null|null|";
        expectedAfterDoAfterChildren = "null|ERLEDIGT|null|null|null|null|100.0|null|null|null|null|null|null|null|null|null|";
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);

        // check if children data are calculated (no aufwand at all but me is note done and children are done)
        myDataDomainObj = getNewBaseWorkflowDataTestObj();
        myDataDomainObj.setEbene(1);
        myDataDomainObj.setState("OFFEN");
        myDataDomainObj.setIstStand(50.0);
        myDataDomainObj2 = getNewBaseWorkflowDataTestObj();
        myDataDomainObj2.setSrcName("test2");
        myDataDomainObj2.setEbene(2);
        myDataDomainObj2.setIstStand(100.0);
        myDataDomainObj2.setParentNode(myDataDomainObj);
        expectedAfterDoBeforeChildren = "null|OFFEN|50.0|null|null|null|null|null|null|null|null|null|null|null|null|null|";
        expectedAfterDoAfterChildren = "null|RUNNING|50.0|null|null|null|75.0|null|null|null|null|null|null|null|null|null|";
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);

        // check if children data are calculated (no aufwand at all but me is note done and children are done)
        myDataDomainObj = getNewBaseWorkflowDataTestObj();
        myDataDomainObj.setEbene(1);
        myDataDomainObj.setState("OFFEN");
        myDataDomainObj.setPlanAufwand(20.0);
        myDataDomainObj.setIstStand(50.0);
        myDataDomainObj2 = getNewBaseWorkflowDataTestObj();
        myDataDomainObj2.setSrcName("test2");
        myDataDomainObj2.setEbene(2);
        myDataDomainObj2.setIstStand(100.0);
        myDataDomainObj2.setParentNode(myDataDomainObj);
        expectedAfterDoBeforeChildren = "null|OFFEN|50.0|null|null|null|null|null|null|null|20.0|null|null|null|null|null|";
        expectedAfterDoAfterChildren = "null|RUNNING|50.0|null|null|null|50.0|null|null|null|20.0|null|null|20.0|null|null|";
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);
    }
}
