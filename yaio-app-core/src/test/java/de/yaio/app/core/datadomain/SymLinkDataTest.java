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

import de.yaio.app.core.node.SymLinkNode;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.exporter.formatter.SymLinkDataFormatterImpl;
import de.yaio.app.datatransfer.importer.parser.SymLinkDataParserImpl;

import java.text.ParseException;

/** 
 * test of the datadomain-logic: SymLinkData<br>
 * test: parser, formatter
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class SymLinkDataTest extends DataDomainTest {
    
    /** 
     * testobject for datadomain-logic: SymLinkData
     * 
     * @package                      de.yaio.core.datadomain
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class SymLinkDataTestObj extends SymLinkNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getSymLinkRef()).append("|")
                     .append(this.getSymLinkName()).append("|")
                     .append(this.getSymLinkTags()).append("|");
            return resBuffer.toString();
            
        }
    }

    @Override
    public void setupParser() {
        parser = new SymLinkDataParserImpl();
    }

    @Override
    public void setupFormatter() {
        formatter = new SymLinkDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() {
        return new SymLinkDataTestObj();
    }
    
    /** 
     * setup the a TestObj for the test
     * @return                       a new dataobj for the test
     **/
    protected SymLinkDataTestObj getNewSymLinkDataTestObj() {
        return (SymLinkDataTestObj) setupNewTestObj();
    }

    @Override
    public void testParser() throws ParserException {
        SymLinkDataTestObj mytestObj = null;
        String expected = null;

        // normal
        mytestObj = getNewSymLinkDataTestObj();
        mytestObj.setName("Name [SymLink: Bui123,SymLinkname,Tag1;Tag2;Tag3]");
        expected = "Name|Bui123|SymLinkname|Tag1;Tag2;Tag3|";
        testParser(mytestObj, expected, importOptions);

        // normal without Tags
        mytestObj = getNewSymLinkDataTestObj();
        mytestObj.setName("Name [SymLink: Bui123,SymLinkname,]");
        expected = "Name|Bui123|SymLinkname||";
        testParser(mytestObj, expected, importOptions);

        // normal without Tags+Name
        mytestObj = getNewSymLinkDataTestObj();
        mytestObj.setName("Name [SymLink: Bui123,,]");
        expected = "Name|Bui123|||";
        testParser(mytestObj, expected, importOptions);
    }

    @Override
    public void testFormatter() throws ParseException {
        SymLinkDataTestObj mytestObj = null;
        String expected = null;
        
        // full
        mytestObj = getNewSymLinkDataTestObj();
        mytestObj.setName("Name XX  ");
        mytestObj.setSymLinkRef("Bui123");
        mytestObj.setSymLinkName("SymLinkname");
        mytestObj.setSymLinkTags("Tag1;Tag2;Tag3");
        expected = "[SymLink: Bui123,SymLinkname,Tag1;Tag2;Tag3]";
        testFormatter(mytestObj, expected, outputOptions);

        // dont show
        outputOptions.setAllFlgShow(false);
        expected = "";
        testFormatter(mytestObj, expected, outputOptions);
    }
}
