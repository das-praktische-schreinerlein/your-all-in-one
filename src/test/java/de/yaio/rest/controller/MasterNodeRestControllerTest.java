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
package de.yaio.rest.controller;

import org.junit.Test;

/** 
 * test: RESTFull webservices for the MasterNode
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.rest.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class MasterNodeRestControllerTest extends BaseNodeRestControllerTest {
    /** 
     * test the masternode
     * @FeatureDomain                TestService-function
     * @FeatureKeywords              Test
     * @throws Exception             io-Exceptions possible
     */
    @Test
    public void doTestShowMasterNode() throws Exception {
        // test show masternode
        testShowMasternode();
    }

    @Override
    public void doTestNodeLifeCycle() throws Exception {
    }

    /* (non-Javadoc)
     * @see de.yaio.BaseTest#setupNewTestObj()
     */
    @Override
    public TestObj setupNewTestObj() throws Exception {
        return null;
    }
}
