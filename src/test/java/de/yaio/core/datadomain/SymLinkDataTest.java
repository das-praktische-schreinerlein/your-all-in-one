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

import de.yaio.core.node.SymLinkNode;
import de.yaio.datatransfer.exporter.formatter.SymLinkDataFormatterImpl;
import de.yaio.datatransfer.importer.parser.SymLinkDataParserImpl;

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
     * @FeatureDomain                Test
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
    public void setupParser() throws Exception {
        parser = new SymLinkDataParserImpl();
    }

    @Override
    public void setupFormatter() throws Exception {
        formatter = new SymLinkDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return new SymLinkDataTestObj();
    }
    
    /** 
     * setup the a TestObj for the test
     * @FeatureDomain                Tests
     * @FeatureResult                returnvalue TestObj - the dataobj for the test
     * @FeatureKeywords              Test Config Initialisation
     * @return                       a new dataobj for the test
     * @throws Exception             possible Exception     */
    protected SymLinkDataTestObj getNewSymLinkDataTestObj() throws Exception  {
        return (SymLinkDataTestObj) setupNewTestObj();
    }

    @Override
    public void testParser() throws Exception {
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
    public void testFormatter() throws Exception {
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
