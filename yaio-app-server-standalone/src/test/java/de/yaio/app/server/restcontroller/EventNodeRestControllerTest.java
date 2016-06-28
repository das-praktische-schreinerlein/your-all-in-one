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
package de.yaio.app.server.restcontroller;

import de.yaio.app.core.node.EventNode;

/** 
 * test: RESTFull webservices for EventNodes
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.rest.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class EventNodeRestControllerTest extends BaseNodeRestControllerTest {
    
    /** 
     * testobject for EventNode
     * 
     * @package                      de.yaio.rest.controller
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class EventNodeRestControllerTestObj extends EventNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getNameForLogger()).append("|");
            return resBuffer.toString();
        }
        
        @Override
        public String getClassName() {
            return "EventNode";
            
        }
    }

    /* (non-Javadoc)
     * @see de.yaio.app.BaseTest#setupNewTestObj()
     */
    @Override
    public TestObj setupNewTestObj() throws Exception {
     // create, show, update, delete task
        String name1 = "Test-EventNode";
        
        EventNodeRestControllerTestObj node = new EventNodeRestControllerTestObj();
        node.setName(name1);
        
        return node;
    }
}
