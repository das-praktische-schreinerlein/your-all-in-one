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

import de.yaio.core.node.InfoNode;
import de.yaio.datatransfer.exporter.formatter.DocLayoutDataFormatterImpl;
import de.yaio.datatransfer.importer.parser.DocLayoutDataParserImpl;

/** 
 * test of the datadomain-logic: DocLayoutData<br>
 * test: parser, formatter
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class DocLayoutDataTest extends DataDomainTest {
    
    /** 
     * testobject for datadomain-logic: DocLayoutData
     * 
     * @FeatureDomain                Test
     * @package                      de.yaio.core.datadomain
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class DocLayoutDataTestObj extends InfoNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getDocLayoutTagCommand()).append("|")
                     .append(this.getDocLayoutAddStyleClass()).append("|")
                     .append(this.getDocLayoutShortName()).append("|")
                     .append(this.getDocLayoutFlgCloseDiv()).append("|");
            return resBuffer.toString();
            
        }
    }

    @Override
    public void setupParser() throws Exception {
        parser = new DocLayoutDataParserImpl();
    }

    @Override
    public void setupFormatter() throws Exception {
        formatter = new DocLayoutDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return new DocLayoutDataTestObj();
    }
    
    /** 
     * setup the a TestObj for the test
     * @FeatureDomain                Tests
     * @FeatureResult                returnvalue TestObj - the dataobj for the test
     * @FeatureKeywords              Test Config Initialisation
     * @return                       a new dataobj for the test
     * @throws Exception             possible Exception     */
    protected DocLayoutDataTestObj getNewDocLayoutDataTestObj() throws Exception  {
        return (DocLayoutDataTestObj) setupNewTestObj();
    }

    @Override
    public void testParser() throws Exception {
        DocLayoutDataTestObj mytestObj = null;
        String expected = null;

        // normal
        mytestObj = getNewDocLayoutDataTestObj();
        mytestObj.setName("Name [DocLayout: Tag,Style,ShortName,FlgClodeDiv]");
        expected = "Name|Tag|Style|ShortName|FlgClodeDiv|";
        testParser(mytestObj, expected, importOptions);

        // normal without data
        mytestObj = getNewDocLayoutDataTestObj();
        mytestObj.setName("Name [DocLayout: Tag,,,]");
        expected = "Name|Tag||||";
        testParser(mytestObj, expected, importOptions);
    }

    @Override
    public void testFormatter() throws Exception {
        DocLayoutDataTestObj mytestObj = null;
        String expected = null;
        
        // tag only
        mytestObj = getNewDocLayoutDataTestObj();
        mytestObj.setName("Name XX  ");
        mytestObj.setDocLayoutTagCommand("Tag");
        expected = "                                                                                [DocLayout: Tag,,,]";
        testFormatter(mytestObj, expected, outputOptions);

        // full
        mytestObj.setDocLayoutAddStyleClass("Style");
        mytestObj.setDocLayoutShortName("Shortname");
        mytestObj.setDocLayoutFlgCloseDiv("X");
        expected = "                                                                                [DocLayout: Tag,Style,Shortname,X]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Func
        outputOptions.setIntendFuncArea(10);
        expected = "          [DocLayout: Tag,Style,Shortname,X]";
        testFormatter(mytestObj, expected, outputOptions);

        // dont show
        outputOptions.setAllFlgShow(false);
        expected = "";
        testFormatter(mytestObj, expected, outputOptions);
    }
}
