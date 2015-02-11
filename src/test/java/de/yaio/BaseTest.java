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
package de.yaio;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

/**
 * <h4>FeatureDomain:</h4>
 *     Tests
 * <h4>FeatureDescription:</h4>
 *     baseinterface for tests<br>
 * 
 * @package de.yaio
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category tests
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public abstract class BaseTest {
    
    protected TestServices testService = new TestServices();
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Test
     * <h4>FeatureDescription:</h4>
     *     testobject
     * 
     * @package de.yaio
     * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category tests
     * @copyright Copyright (c) 2014, Michael Schreiner
     * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public interface TestObj  {
        String toString();
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
     * @return the dataobj for the test
     * @throws Exception - io-Exceptions possible
     */
    public abstract TestObj setupNewTestObj() throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     setup the test
     * <h4>FeatureKeywords:</h4>
     *     Test Config Initialisation
     * @throws Exception - io-Exceptions possible
     */
    @Before
    public void setUp() throws Exception {
    };

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     teardown the test
     * <h4>FeatureKeywords:</h4>
     *     Test Config
     * @throws Exception - io-Exceptions possible
     */
    @After
    public void tearDown() throws Exception {
    };
}