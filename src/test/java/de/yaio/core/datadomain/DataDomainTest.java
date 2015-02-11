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
package de.yaio.core.datadomain;
import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import de.yaio.BaseTest;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.datatransfer.exporter.formatter.Formatter;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.ImportOptionsImpl;
import de.yaio.datatransfer.importer.parser.Parser;

/**
 * <h4>FeatureDomain:</h4>
 *     Tests
 * <h4>FeatureDescription:</h4>
 *     interface for test of the datadomain-logic<br>
 *     test: parser, formatter
 * 
 * @package de.yaio.core.datadomain
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category tests
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public abstract class DataDomainTest extends BaseTest {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(DataDomainTest.class);

    // define Parser+Formatter
    Parser parser = null;
    ImportOptions importOptions = new ImportOptionsImpl();
    Formatter formatter = null;
    OutputOptions outputOptions = new OutputOptionsImpl();

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     setup the parser-obj to test
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates membervar parser
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Test Config Initialisation
     * @throws Exception - in case of Problems when setup
     */
    @Before
    public abstract void setupParser() throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     setup the formatter-obj to test
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates membervar formatter
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Test Config Initialisation
     * @throws Exception - in case of Problems when setup
     */
    @Before
    public abstract void setupFormatter() throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     do the parser-tests
     * <h4>FeatureKeywords:</h4>
     *     Test
     * @throws Exception - in case of Problems while test
     */
    @Test
    public abstract void testParser() throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     does a parser-test on the parser-obj with the dataobj<br>
     *     calls parser.parseFromName() and compares the resulting dataobj.toString 
     *     with myExpectedParserResult
     * <h4>FeatureKeywords:</h4>
     *     Test
     * @param myDataDomainObj - the dataobj to test
     * @param myExpectedParserResult - the expected result
     * @param myImportOptions - Importoptions for the parser
     * @throws Exception - io-Exceptions possible
     */
    public void testParser(final DataDomain myDataDomainObj, final String myExpectedParserResult, 
            final ImportOptions myImportOptions) throws Exception {
        // Master ausgeben
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("do parsing of:" + myDataDomainObj.getName());
        }

        // run
        parser.parseFromName(myDataDomainObj, myImportOptions);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("got parserresult of:" + myDataDomainObj.toString());
        }

        // Test
        testService.checkToStringResult((TestObj) myDataDomainObj, myExpectedParserResult);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     do the formatter-tests
     * <h4>FeatureKeywords:</h4>
     *     Test
     * @throws Exception - io-Exceptions possible
     */
    @Test
    public abstract void testFormatter() throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     does a formatter-test on the formatter-obj with the dataobj
     *     calls formatter.format() and compares the resulting String 
     *     with myExpectedFormatterResult
     * <h4>FeatureKeywords:</h4>
     *     Test
     * @param myDataDomainObj - the dataobj to test
     * @param myExpectedFormatterResult - the expected result
     * @param myOutputOptions - outputoptions for the formatter
     * @throws Exception - io-Exceptions possible
     */
    public void testFormatter(final DataDomain myDataDomainObj, final String myExpectedFormatterResult, 
            final OutputOptions myOutputOptions) throws Exception {
        // init
        StringBuffer resBuffer = new StringBuffer();
        
        // Master ausgeben
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("do format of:" + myDataDomainObj.toString());
        }

        // run
        formatter.format(myDataDomainObj, resBuffer, myOutputOptions);

        // get result
        String res = resBuffer.toString();
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("got format of:" + res);
        }

        // Test
        assertEquals(myExpectedFormatterResult, res);
    }
}