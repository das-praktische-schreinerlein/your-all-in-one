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

import de.yaio.core.node.UrlResNode;
import de.yaio.datatransfer.exporter.formatter.ResLocDataFormatterImpl;
import de.yaio.datatransfer.importer.parser.ResLocDataParserImpl;

/**
 * <h4>FeatureDomain:</h4>
 *     Tests
 * <h4>FeatureDescription:</h4>
 *     test of the datadomain-logic: ResLocData<br>
 *     test: parser, formatter
 * 
 * @package de.yaio.core.datadomain
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category tests
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class ResLocDataTest extends DataDomainTest {
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Test
     * <h4>FeatureDescription:</h4>
     *     testobject for datadomain-logic: ResLocData
     * 
     * @package de.yaio.core.datadomain
     * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category tests
     * @copyright Copyright (c) 2014, Michael Schreiner
     * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class ResLocDataTestObj extends UrlResNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getResLocRef()).append("|")
                     .append(this.getResLocName()).append("|")
                     .append(this.getResLocTags()).append("|")
                     ;
            return resBuffer.toString();
            
        }
    }

    @Override
    public void setupParser() throws Exception {
        parser = new ResLocDataParserImpl();
    }

    @Override
    public void setupFormatter() throws Exception {
        formatter = new ResLocDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return new ResLocDataTestObj();
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
     * @return - a new dataobj for the test
     * @throws Exception
     */
    protected ResLocDataTestObj getNewResLocDataTestObj() throws Exception  {
        return (ResLocDataTestObj) setupNewTestObj();
    }

    @Override
    public void testParser() throws Exception {
        ResLocDataTestObj mytestObj = null;
        String expected = null;

        // normal with Url
        mytestObj = getNewResLocDataTestObj();
        mytestObj.setName("Name [ResLoc: http://www.domain.tdl/vcac,Domainname,Tag1;Tag2;Tag3]");
        expected = "Name|http://www.domain.tdl/vcac|Domainname|Tag1;Tag2;Tag3|";
        testParser(mytestObj, expected, importOptions);

        // normal without Tags
        mytestObj = getNewResLocDataTestObj();
        mytestObj.setName("Name [ResLoc: http://www.domain.tdl/vcac,Domainname,]");
        expected = "Name|http://www.domain.tdl/vcac|Domainname||";
        testParser(mytestObj, expected, importOptions);

        // normal without Tags+Name
        mytestObj = getNewResLocDataTestObj();
        mytestObj.setName("Name [ResLoc: http://www.domain.tdl/vcac,,]");
        expected = "Name|http://www.domain.tdl/vcac|||";
        testParser(mytestObj, expected, importOptions);

        // normal with File
        mytestObj = getNewResLocDataTestObj();
        mytestObj.setName("Name [ResLoc: /vcac,,]");
        expected = "Name|/vcac|||";
        testParser(mytestObj, expected, importOptions);

        // normal with Url + Url as Name -> not allowed
        mytestObj = getNewResLocDataTestObj();
        mytestObj.setName("Name [ResLoc: http://www.domain.tdl/vcac,http://www.domain.tdl/vcac,Tag1;Tag2;Tag3]");
        expected = "Name [ResLoc: http://www.domain.tdl/vcac,http://www.domain.tdl/vcac,Tag1;Tag2;Tag3]|null|null|null|";
        testParser(mytestObj, expected, importOptions);
    }

    @Override
    public void testFormatter() throws Exception {
        ResLocDataTestObj mytestObj = null;
        String expected = null;
        
        // full
        mytestObj = getNewResLocDataTestObj();
        mytestObj.setName("Name XX  ");
        mytestObj.setResLocRef("http://www.domain.tdl/vcac");
        mytestObj.setResLocName("Domainname");
        mytestObj.setResLocTags("Tag1;Tag2;Tag3");
        expected = "[ResLoc: http://www.domain.tdl/vcac,Domainname,Tag1;Tag2;Tag3]";
        testFormatter(mytestObj, expected, outputOptions);

        // dont show
        outputOptions.setAllFlgShow(false);
        expected = "";
        testFormatter(mytestObj, expected, outputOptions);
    }
}
