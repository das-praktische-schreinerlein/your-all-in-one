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
package de.yaio.core.datadomain;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import de.yaio.BaseTest;
import de.yaio.commons.data.DataUtils;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.datatransfer.exporter.formatter.Formatter;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.ImportOptionsImpl;
import de.yaio.datatransfer.importer.parser.Parser;

/** 
 * interface for test of the datadomain-logic<br>
 * test: parser, formatter
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public abstract class DataDomainTest extends BaseTest {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(DataDomainTest.class);

    // define Parser+Formatter
    protected Parser parser = null;
    protected ImportOptions importOptions = new ImportOptionsImpl();
    protected Formatter formatter = null;
    protected OutputOptions outputOptions = new OutputOptionsImpl();

    /** dateformat-instance for german date dd.MM.yyyy  */
    protected final DateFormat DF = DataUtils.getDF();
    /** dateformat-instance for german time HH:mm */
    protected final DateFormat TF = DataUtils.getTF();
    /** dateformat-instance for german datetime dd.MM.yyyy HH:mm */
    protected final DateFormat DTF = DataUtils.getDTF();
    /** dateformat-instance for UID yyyyMMddHHmmssSSS */
    protected final DateFormat UIDF = DataUtils.getUIDF();
    
    /** 
     * setup the parser-obj to test
     * @throws Exception             in case of Problems when setup
     */
    @Before
    public abstract void setupParser() throws Exception;

    /** 
     * setup the formatter-obj to test
     * @throws Exception             in case of Problems when setup
     */
    @Before
    public abstract void setupFormatter() throws Exception;

    /** 
     * do the parser-tests
     * @throws Exception             in case of Problems while test
     */
    @Test
    public abstract void testParser() throws Exception;

    /** 
     * does a parser-test on the parser-obj with the dataobj<br>
     * calls parser.parseFromName() and compares the resulting dataobj.toString 
     * with myExpectedParserResult
     * @param myDataDomainObj        the dataobj to test
     * @param myExpectedParserResult the expected result
     * @param myImportOptions        Importoptions for the parser
     * @throws Exception             io-Exceptions possible
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
     * do the formatter-tests
     * @throws Exception             io-Exceptions possible
     */
    @Test
    public abstract void testFormatter() throws Exception;

    /** 
     * does a formatter-test on the formatter-obj with the dataobj
     * calls formatter.format() and compares the resulting String 
     * with myExpectedFormatterResult
     * @param myDataDomainObj        the dataobj to test
     * @param myExpectedFormatterResult the expected result
     * @param myOutputOptions        outputoptions for the formatter
     * @throws Exception             io-Exceptions possible
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
