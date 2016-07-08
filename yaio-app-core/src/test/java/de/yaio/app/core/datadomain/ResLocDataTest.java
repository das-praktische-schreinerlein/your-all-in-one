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
package de.yaio.app.core.datadomain;

import de.yaio.app.core.node.UrlResNode;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.exporter.formatter.ResLocDataFormatterImpl;
import de.yaio.app.datatransfer.importer.parser.ResLocDataParserImpl;

import java.text.ParseException;

/** 
 * test of the datadomain-logic: ResLocData<br>
 * test: parser, formatter
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class ResLocDataTest extends DataDomainTest {
    
    /** 
     * testobject for datadomain-logic: ResLocData
     * 
     * @package                      de.yaio.core.datadomain
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class ResLocDataTestObj extends UrlResNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getResLocRef()).append("|")
                     .append(this.getResLocName()).append("|")
                     .append(this.getResLocTags()).append("|");
            return resBuffer.toString();
            
        }
    }

    @Override
    public void setupParser() {
        parser = new ResLocDataParserImpl();
    }

    @Override
    public void setupFormatter() {
        formatter = new ResLocDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() {
        return new ResLocDataTestObj();
    }
    
    /** 
     * setup the a TestObj for the test
     * @return                       a new dataobj for the test
     **/
    protected ResLocDataTestObj getNewResLocDataTestObj() {
        return (ResLocDataTestObj) setupNewTestObj();
    }

    @Override
    public void testParser() throws ParserException {
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
    public void testFormatter() throws ParseException {
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
