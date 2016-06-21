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

import de.yaio.app.core.node.SymLinkNode;

/** 
 * test: RESTFull webservices for SymLinkNodes
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.rest.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class SymLinkNodeRestControllerTest extends BaseNodeRestControllerTest {
    
    /** 
     * testobject for SymLinkNode
     * 
     * @package                      de.yaio.rest.controller
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class SymLinkNodeRestControllerTestObj extends SymLinkNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getNameForLogger()).append("|");
            return resBuffer.toString();
        }
        
        @Override
        public String getClassName() {
            return "SymLinkNode";
            
        }
    }

    /* (non-Javadoc)
     * @see de.yaio.app.BaseTest#setupNewTestObj()
     */
    @Override
    public TestObj setupNewTestObj() throws Exception {
     // create, show, update, delete task
        String name1 = "Test-SymLinkNode";
        
        SymLinkNodeRestControllerTestObj node = new SymLinkNodeRestControllerTestObj();
        node.setName(name1);
        
        return node;
    }
}
