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
package de.yaio.app.datatransfer.importer;

import de.yaio.app.BaseTest;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.node.TaskNode;
import de.yaio.app.datatransfer.common.ParserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

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
public class NodeFactoryTest extends BaseTest {

    protected NodeFactory nodeFactoryObj = null;

    /** 
     * testobject for importer-logic
     * 
     * @package                      de.yaio.datatransfer.importer
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public static class NodeFactoryTestObj implements TestObj {
        
        BaseNode dataObj = null;
        
        public NodeFactoryTestObj(final DataDomain dataDomain) {
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
    public TestObj setupNewTestObj() {
        return null;
    }
    
    /** 
     * configure the nodefactory for the tests
     **/
    @Before
    public void setupNodeFactory() {
        nodeFactoryObj = new NodeFactoryImpl(null);
        TestNode.configureNodeTypeIdentifier(nodeFactoryObj);
    }

    /** 
     * do tests for creation of nodes from text
     **/
    @Test
    public void testCreateNode() throws ParserException {
        String expected = null;
        String src = null;
        NodeFactoryTestObj testObj = null;
        Class<?> parsedClass = null;
        
        // test OFFEN to TaskNode
        src = "OFFEN - Offen1";
        expected = "Offen1|OFFEN|de.yaio.app.core.node.TaskNode|";
        testObj = new NodeFactoryTestObj(nodeFactoryObj.createNodeObjFromText(TaskNode.class, 1, src, src, null));
        testService.checkToStringResult(testObj, expected);
        
        // test TEST to TestNode
        src = "TEST - test1";
        expected = "test1|TEST|de.yaio.app.datatransfer.importer.TestNode|";
        parsedClass = nodeFactoryObj.getNodeTypeFromText(src, src);
        testObj = new NodeFactoryTestObj(nodeFactoryObj.createNodeObjFromText(parsedClass, 1, src, src, null));
        testService.checkToStringResult(testObj, expected);

        // test TEST1 to TestNode
        src = "TEST1 - test2";
        expected = "test2|TEST1|de.yaio.app.datatransfer.importer.TestNode|";
        parsedClass = nodeFactoryObj.getNodeTypeFromText(src, src);
        testObj = new NodeFactoryTestObj(nodeFactoryObj.createNodeObjFromText(parsedClass, 1, src, src, null));
        testService.checkToStringResult(testObj, expected);

        // test TEST3 to BaseNode
        src = "TEST3 - test3";
        expected = "TEST3 - test3|null|de.yaio.app.core.node.BaseNode|";
        parsedClass = nodeFactoryObj.getNodeTypeFromText(src, src);
        testObj = new NodeFactoryTestObj(nodeFactoryObj.createNodeObjFromText(parsedClass, 1, src, src, null));
        testService.checkToStringResult(testObj, expected);
    }
}
