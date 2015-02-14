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
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import de.yaio.BaseTest.TestObj;

/**
 * <h4>FeatureDomain:</h4>
 *     Tests
 * <h4>FeatureDescription:</h4>
 *     services for tests<br>
 * 
 * @package de.yaio
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category tests
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class TestServices {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(TestServices.class);

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     compares the resulting dataobj.toString with myExpecteResult
     * <h4>FeatureKeywords:</h4>
     *     Test Service
     * @param myTestObj - the dataobj to test
     * @param myExpectedResult - the expected result
     * @throws Exception - io-Exceptions possible
     */
    public void checkToStringResult(final TestObj myTestObj, 
                    final String myExpectedResult) throws Exception {
        // Master ausgeben
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("got result of:" + myTestObj.toString());
        }

        // Test
        assertEquals(myExpectedResult, myTestObj.toString());
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     compares the resulting string line by line with myExpecteResult
     * <h4>FeatureKeywords:</h4>
     *     Test Service
     * @param result - the string to test
     * @param myExpectedResult - the expected result
     * @throws Exception - io-Exceptions possible
     */
    public void checkStringLineByLine(final String result, final String myExpectedResult) 
                    throws Exception {
        // Master ausgeben
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("got result of:" + result);
        }
        
        // split Strings
        String[] resLines = result.split("\n");
        String[] expectedResLines = myExpectedResult.split("\n");
        String resLine, expectedResLine;
        
        // compare line by line
        for (int lineNum = 0; lineNum < resLines.length && lineNum <= expectedResLines.length; lineNum++) {
            resLine = resLines[lineNum].replace("\r", "");
            expectedResLine = expectedResLines[lineNum].replace("\r", "");
            assertEquals("Line " + lineNum + ": " + expectedResLine, 
                         "Line " + lineNum + ": " + resLine);
        }

        // Test size
        assertEquals("LineCount: " + expectedResLines.length, 
                     "LineCount: " + resLines.length);
//        assertEquals("Size: " + myExpectedResult.length(), 
//                     "Size: " + result.length());
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Tests
     * <h4>FeatureDescription:</h4>
     *     read the fixture-file from package-resource 
     * <h4>FeatureKeywords:</h4>
     *     FileUtil
     * @param base - base to get the packagepath for srcFile
     * @param srcFile - file to load from package
     * @return - resulting filecontent
     * @throws Exception - io-Exceptions possible
     */
    public StringBuffer readFixture(final Class<?> base, final String srcFile) throws Exception {
        // open resource
        InputStream is = base.getResourceAsStream(srcFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        
        // read lines
        String zeile = "";
        StringBuffer sb = new StringBuffer();
        while ((zeile = br.readLine()) != null) {
          sb.append(zeile).append("\n");
        }
        
        return sb;
    }
}
