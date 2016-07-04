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
package de.yaio.app.core.datadomainservice;
import java.text.DateFormat;

import de.yaio.app.core.datadomainservice.DataDomainRecalc;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import de.yaio.app.BaseTest;
import de.yaio.commons.data.DataUtils;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.nodeservice.NodeService;

/** 
 * interface for test of the datadomainservice-logic<br>
 * test: doRecalc...
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomainservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public abstract class DataDomainServiceTest extends BaseTest {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(DataDomainServiceTest.class);

    // define Service
    protected DataDomainRecalc dataDomainService = null;
    
    /** dateformat-instance for german date dd.MM.yyyy  */
    protected final DateFormat DF = DataUtils.getDF();
    /** dateformat-instance for german time HH:mm */
    protected final DateFormat TF = DataUtils.getTF();
    /** dateformat-instance for german datetime dd.MM.yyyy HH:mm */
    protected final DateFormat DTF = DataUtils.getDTF();
    /** dateformat-instance for UID yyyyMMddHHmmssSSS */
    protected final DateFormat UIDF = DataUtils.getUIDF();
    
    /** 
     * setup the datadomainservice-obj to test
     **/
    @Before
    public abstract void setupDataDomainService();
    
    @Test
    /** 
     * do the ServiceRecalc-tests
     **/
    public abstract void testServiceDoRecalc();

    /** 
     * does a datadomainservice-test on the datadomainservice-obj with the dataobj<br>
     * calls dataDomainService.doRecalcBeforeChildren and checks the result with checkServiceResult()<br>  
     * calls recalcData for every childNode<br>  
     * calls dataDomainService.doRecalcAfterChildren and checks the result with checkServiceResult()<br>  
     * @param testObj                the dataobj to test
     * @param expectedAfterDoBeforeChildren the expected result after call doRecalcBeforeChildren
     * @param expectedAfterDoAfterChildren the expected result after call doRecalcAfterChildren
     * @param recurseDirection       direction of recalc
     **/
    public void testServiceDoRecalc(final TestObj testObj, 
                    final String expectedAfterDoBeforeChildren,
                    final String expectedAfterDoAfterChildren,
                    final NodeService.RecalcRecurseDirection recurseDirection) {
        
        DataDomain myDataDomainObj = (DataDomain) testObj;
        
        // run doRecalcBeforeChildren
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("src before doRecalcBeforeChildren:" 
                         + myDataDomainObj.toString());
        }
        dataDomainService.doRecalcBeforeChildren((DataDomain) myDataDomainObj, recurseDirection);
        testService.checkToStringResult(testObj, expectedAfterDoBeforeChildren);
        
        // recalc children
        if (recurseDirection == NodeService.RecalcRecurseDirection.CHILDREN) {
            for (String name : myDataDomainObj.getChildNodesByNameMap().keySet()) {
                myDataDomainObj.getChildNodesByNameMap().get(name).recalcData(recurseDirection);
            }
        }

        // run doRecalcBeforeChildren
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("src before doRecalcAfterChildren:" 
                         + myDataDomainObj.toString());
        }
        dataDomainService.doRecalcAfterChildren(myDataDomainObj, recurseDirection);
        testService.checkToStringResult(testObj, expectedAfterDoAfterChildren);
    };
}
