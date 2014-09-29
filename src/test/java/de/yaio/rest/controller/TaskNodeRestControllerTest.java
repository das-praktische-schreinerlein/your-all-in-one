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
package de.yaio.rest.controller;

import de.yaio.core.node.TaskNode;

/**
 * <h4>FeatureDomain:</h4>
 *     Tests
 * <h4>FeatureDescription:</h4>
 *     test: RESTFull webservices for TaskNodes
 * 
 * @package de.yaio.rest.controller
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category tests
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class TaskNodeRestControllerTest extends BaseNodeRestControllerTest {
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Test
     * <h4>FeatureDescription:</h4>
     *     testobject for TaskNode
     * 
     * @package de.yaio.rest.controller
     * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category tests
     * @copyright Copyright (c) 2014, Michael Schreiner
     * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class TaskNodeRestControllerTestObj extends TaskNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getNameForLogger()).append("|")
                     ;
            return resBuffer.toString();
        }
        
        @Override
        public String getClassName() {
            return "TaskNode";
            
        }
    }

    /* (non-Javadoc)
     * @see de.yaio.BaseTest#setupNewTestObj()
     */
    @Override
    public TestObj setupNewTestObj() throws Exception {
     // create, show, update, delete task
        String name1 = "Test-TaskNode";
        
        TaskNodeRestControllerTestObj node = new TaskNodeRestControllerTestObj();
        node.setName(name1);
        
        return node;
    }
}