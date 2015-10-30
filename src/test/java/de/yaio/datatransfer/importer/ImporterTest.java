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
package de.yaio.datatransfer.importer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import de.yaio.BaseTest;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;

/** 
 * test of the importer-logic<br>
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.datatransfer.importer
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public class ImporterTest extends BaseTest {

    protected Importer importerObj = null;

    /** 
     * testobject for importer-logic
     * 
     * @FeatureDomain                Test
     * @package                      de.yaio.datatransfer.importer
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class ImporterTestObj implements TestObj {
        
        BaseNode dataObj = null;
        
        public ImporterTestObj(final DataDomain dataDomain) {
            this.dataObj = (BaseNode) dataDomain;            
        }
        
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.dataObj.getName()).append("|")
                     .append(this.dataObj.getState()).append("|")
                     .append(this.dataObj.getClass().getName()).append("|");
            return resBuffer.toString();
            
        }
    }

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return null;
    }
    
    public static void configureNodeTypes(final Importer importer) {
        importer.addNodeTypeIdentifierVariantMapping(TestNode.CONST_MAP_NODETYPE_IDENTIFIER);
    }
    
    /** 
     * configure the importer for the tests
     * @FeatureDomain                Tests
     * @FeatureResult                updates membervars importerObj - Importer
     * @FeatureKeywords              Test Configuration
     * @throws Exception             possible Exception     */
    @Before
    public void setupImporter() throws Exception {
        importerObj = new ImporterImpl(null);
        NodeFactory nodeFactoryObj = importerObj.getNodeFactory();
        TestNode.configureNodeTypeIdentifier(nodeFactoryObj);
        TestNode.configureNodeTypes(importerObj);
        TestNode.configureWorkflowNodeTypeMapping(importerObj);
    }
    
    
    /** 
     * do tests for creation of nodes from text
     * @FeatureDomain                Tests
     * @FeatureKeywords              Test
     * @throws Exception             possible Exception     */
    @Test
    public void testCreateNode() throws Exception {
        String expected = null;
        String src = null;
        ImporterTestObj testObj = null;
        
        // test OFFEN to TaskNode
        src = "OFFEN - Offen1";
        expected = "Offen1|OFFEN|de.yaio.core.node.TaskNode|";
        testObj = new ImporterTestObj(importerObj.createNodeObjFromText(1, src, src, null));
        testService.checkToStringResult(testObj, expected);
        
        // test OFFEN to TaskNode
        src = "INFO - info1";
        expected = "info1|INFO|de.yaio.core.node.InfoNode|";
        testObj = new ImporterTestObj(importerObj.createNodeObjFromText(1, src, src, null));
        testService.checkToStringResult(testObj, expected);

        // test OFFEN to TaskNode
        src = "FILERES - fileres1";
        expected = "fileres1|FILERES|de.yaio.core.node.UrlResNode|";
        testObj = new ImporterTestObj(importerObj.createNodeObjFromText(1, src, src, null));
        testService.checkToStringResult(testObj, expected);

        // test TEST to TestNode
        src = "TEST - test1";
        expected = "test1|TEST|de.yaio.datatransfer.importer.TestNode|";
        testObj = new ImporterTestObj(importerObj.createNodeObjFromText(1, src, src, null));
        testService.checkToStringResult(testObj, expected);

        // test TEST1 to TestNode
        src = "TEST1 - test2";
        expected = "test2|TEST1|de.yaio.datatransfer.importer.TestNode|";
        testObj = new ImporterTestObj(importerObj.createNodeObjFromText(1, src, src, null));
        testService.checkToStringResult(testObj, expected);

        // test TEST3 to BaseNode
        src = "TEST3 - test3";
        expected = "TEST3 - test3|null|de.yaio.core.node.BaseNode|";
        testObj = new ImporterTestObj(importerObj.createNodeObjFromText(1, src, src, null));
        testService.checkToStringResult(testObj, expected);
    }
}
