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
 * Testsuite for the datadomainservice-logic: MetaData<br>
 * tests: NextnodeNumber...
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomainservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class MetaDataServiceTest extends DataDomainServiceTest {
    
    /** 
     * testobject for datadomain-logic: MetaData
     * 
     * @FeatureDomain                Test
     * @package                      de.yaio.core.datadomainservice
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class MetaDataTestObj extends TaskNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getMetaNodePraefix()).append("|")
                     .append(this.getMetaNodeNummer()).append("|")
                     .append(this.getMetaNodeTypeTags()).append("|")
                     .append(this.getMetaNodeSubTypeTags()).append("|");
            return resBuffer.toString();
            
        }
    }

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return new MetaDataTestObj();
    }
    
    /** 
     * setup the a TestObj for the test
     * @FeatureDomain                Tests
     * @FeatureResult                returnvalue TestObj - the dataobj for the test
     * @FeatureKeywords              Test Config Initialisation
     * @return                       a new dataobj for the test
     * @throws Exception             possible Exception     */
    protected MetaDataTestObj getNewMetaDataTestObj() throws Exception  {
        return (MetaDataTestObj) setupNewTestObj();
    }


    @Override
    public void setupDataDomainService() throws Exception {
        NodeServiceImpl nodeService = (NodeServiceImpl) TaskNode.getConfiguredNodeService();
        dataDomainService = nodeService.hshDataDomainRecalcerByClass.get(MetaDataServiceImpl.class);
    }

    @Override
    public void testServiceDoRecalc() throws Exception {
        // init Vars
        MetaDataTestObj myDataDomainObj = null; 
        MetaDataTestObj myDataDomainObj2 = null;
        String expectedAfterDoBeforeChildren = null;
        String expectedAfterDoAfterChildren = null;
        int recurseDirection = NodeService.CONST_RECURSE_DIRECTION_CHILDREN;
        
        // empty Meta
        myDataDomainObj = getNewMetaDataTestObj(); 
        expectedAfterDoBeforeChildren = "null|null|null|null|null|";
        expectedAfterDoAfterChildren = "null|null|null|null|null|";
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);

        // init nodenumberervice and check with prefix
        ((MetaDataService) dataDomainService).getNodeNumberService().initNextNodeNumber("Test", 100);
        expectedAfterDoBeforeChildren = "null|Test|101|null|null|";
        expectedAfterDoAfterChildren = "null|Test|101|null|null|";
        myDataDomainObj.setMetaNodePraefix("Test");
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);

        // check if parent-prefix is set
        ((MetaDataService) dataDomainService).getNodeNumberService().initNextNodeNumber("Test2", null);
        myDataDomainObj = getNewMetaDataTestObj();
        myDataDomainObj.setEbene(2);
        myDataDomainObj2 = getNewMetaDataTestObj();
        myDataDomainObj2.setEbene(1);
        myDataDomainObj.setParentNode(myDataDomainObj2);
        myDataDomainObj2.setMetaNodePraefix("Test2");
        expectedAfterDoBeforeChildren = "null|Test2|1|null|null|";
        expectedAfterDoAfterChildren = "null|Test2|1|null|null|";
        myDataDomainObj.setMetaNodePraefix(null);
        this.testServiceDoRecalc(myDataDomainObj, expectedAfterDoBeforeChildren, 
                        expectedAfterDoAfterChildren, recurseDirection);
    }
}
