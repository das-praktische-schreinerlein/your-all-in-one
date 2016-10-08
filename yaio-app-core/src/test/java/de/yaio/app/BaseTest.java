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
package de.yaio.app;

import de.yaio.commons.data.DataUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import java.util.Date;
import java.util.TimeZone;

/** 
 * baseinterface for tests<br>
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public abstract class BaseTest {

    static {
        // dirty hack: set timezone to pass tests with fixtures
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
    }
    
    protected TestServices testService = new TestServices();
    
    /** 
     * testobject
     * 
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     */
    public interface TestObj  {
        String toString();
    }

    /** 
     * setup the a TestObj for the test
     * @return                       the dataobj for the test
     */
    public abstract TestObj setupNewTestObj();

    /** 
     * setup the test
     */
    @Before
    public void setUp() {
    }

    /** 
     * teardown the test
     */
    @After
    public void tearDown() {
    }

    protected String formatDateForCheck(final Date date) {
        if (date == null) {
            return null;
        }
        return DataUtils.getDTF().format(date);
    }
}
