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
package de.yaio.app.webapp.controller;

import java.nio.charset.Charset;

import de.yaio.app.webapp.controller.ExportController;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.yaio.app.BaseTest;
import de.yaio.app.config.Configurator;
import de.yaio.app.core.datadomainservice.NodeNumberService;
import de.yaio.app.core.node.BaseNode;

/** 
 * test: RESTFull webservices
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.rest.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public abstract class ImportControllerTest  extends BaseTest {
    /** masternodeId **/
    public static final String CONST_MASTERNODE_ID = "MasterplanMasternode1";
    public static final String CONST_TESTPARENTNODE_ID = "JavaUnitTest1";
                    
    /** contentype of my app **/
    public static final MediaType APPLICATION_JSON_UTF8 = 
                    new MediaType(MediaType.APPLICATION_JSON.getType(), 
                                    MediaType.APPLICATION_JSON.getSubtype(), 
                                    Charset.forName("utf8"));

    /** Logger **/
    private static final Logger LOGGER =
            Logger.getLogger(ImportControllerTest.class);
    
    protected BaseNode baseNode;
    protected MockMvc mockMvc;
    
    @Override
    public void setUp() throws Exception {
        // initApplicationContext
        String[] args = new String[2];
        args[0] = "--config";
        args[1] = "./config/application-test.properties";
        if (Configurator.getInstance().getCmdLineArgs() == null) {
            Configurator.getInstance().getAvailiableCmdLineOptions();
            Configurator.getInstance().setCmdLineArgs(args);
            Configurator.getInstance().getCommandLine();
            Configurator.getInstance().getSpringApplicationContext();
            
            // gets NodeNumberService
            NodeNumberService nodeNumberService = 
                            BaseNode.getConfiguredMetaDataService().getNodeNumberService();
            nodeNumberService.initNextNodeNumber("WEBTEST", 1);
        }
        
        ExportController nodeController = new ExportController();
        mockMvc = MockMvcBuilders.standaloneSetup(nodeController).build();
    };

    @Override
    public void tearDown() throws Exception {
    };
}
