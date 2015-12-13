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
import de.yaio.datatransfer.exporter.formatter.MetaDataFormatterImpl;
import de.yaio.datatransfer.importer.parser.MetaDataParserImpl;

/** 
 * test of the datadomain-logic: MetaData<br>
 * test: parser, formatter
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class MetaDataTest extends DataDomainTest {
    
    /** 
     * testobject for datadomain-logic: MetaData
     * 
     * @FeatureDomain                Test
     * @package                      de.yaio.core.datadomain
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class MetaDataTestObj extends InfoNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getMetaNodePraefix()).append("|")
                     .append(this.getMetaNodeNummer()).append("|")
                     .append(this.getMetaNodeTypeTags()).append("|")
                     .append(this.getMetaNodeSubTypeTags()).append("|");
            return resBuffer.toString();
            
        }
    }

    @Override
    public void setupParser() throws Exception {
        parser = new MetaDataParserImpl();
    }

    @Override
    public void setupFormatter() throws Exception {
        formatter = new MetaDataFormatterImpl();
    }

    @Override
    public TestObj setupNewTestObj() throws Exception {
        return new MetaDataTestObj();
    }
    
    /** 
     * setup the a TestObj for the test
     * @FeatureDomain                Tests
     * @FeatureResult                returnvalue TestObj - the dataobj for the test
     * @FeatureKeywords              Test Config Initialisation
     * @return                       a new dataobj for the test
     * @throws Exception             possible Exception     */
    protected MetaDataTestObj getNewMetaDataTestObj() throws Exception  {
        return (MetaDataTestObj) setupNewTestObj();
    }

    @Override
    public void testParser() throws Exception {
        MetaDataTestObj mytestObj = null;
        String expected = null;

        // normal
        mytestObj = getNewMetaDataTestObj();
        mytestObj.setName("Name [NodeMeta: Praefix,123,Tags,SubTags]");
        expected = "Name|Praefix|123|Tags|SubTags|";
        testParser(mytestObj, expected, importOptions);

        // normal without data
        mytestObj = getNewMetaDataTestObj();
        mytestObj.setName("Name [NodeMeta: Praefix,123,,]");
        expected = "Name|Praefix|123|||";
        testParser(mytestObj, expected, importOptions);
    }

    @Override
    public void testFormatter() throws Exception {
        MetaDataTestObj mytestObj = null;
        String expected = null;
        
        // tag only
        mytestObj = getNewMetaDataTestObj();
        mytestObj.setName("Name XX  ");
        mytestObj.setMetaNodePraefix("Praefix");
        mytestObj.setMetaNodeNummer("Id");
        expected = "                                                                                                                                                                [NodeMeta: Praefix,Id,,]";
        testFormatter(mytestObj, expected, outputOptions);  

        // full
        mytestObj.setMetaNodeTypeTags("Tags");
        mytestObj.setMetaNodeSubTypeTags("SubTags");
        expected = "                                                                                                                                                                [NodeMeta: Praefix,Id,Tags,SubTags]";
        testFormatter(mytestObj, expected, outputOptions);

        // intend Sys
        outputOptions.setIntendSys(10);
        expected = "          [NodeMeta: Praefix,Id,Tags,SubTags]";
        testFormatter(mytestObj, expected, outputOptions);

        // dont show
        outputOptions.setAllFlgShow(false);
        expected = "";
        testFormatter(mytestObj, expected, outputOptions);
    }
}
