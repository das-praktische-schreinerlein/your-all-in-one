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
package de.yaio.app.server.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import de.yaio.app.cli.YaioCmdLineHelper;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.yaio.app.BaseTest;
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
public abstract class ExportControllerTest  extends BaseTest {
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
            Logger.getLogger(ExportControllerTest.class);
    
    protected BaseNode baseNode;
    protected MockMvc mockMvc;
    
    @Override
    public void setUp() throws Exception {
        // initApplicationContext
        String[] args = new String[2];
        args[0] = "--config";
        args[1] = "./config/application-test.properties";
        if (YaioCmdLineHelper.getInstance().getCmdLineArgs() == null) {
            YaioCmdLineHelper.getInstance().getAvailiableCmdLineOptions();
            YaioCmdLineHelper.getInstance().setCmdLineArgs(args);
            YaioCmdLineHelper.getInstance().getCommandLine();
            YaioCmdLineHelper.getInstance().getSpringApplicationContext();
            
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
    
    public ResultActions testBaseRequest(final MockHttpServletRequestBuilder rb, 
                                         final String contentType) throws Exception {
        // ask
        ResultActions res = mockMvc.perform(rb);

        // extract response
        MockHttpServletResponse response = res.andReturn().getResponse();
        if (response.getStatus() != 200) {
            LOGGER.error("result response-err:" + response.getStatus() 
                            + " errmsg:" + response.getErrorMessage()
                            + " content:" + response.getContentAsString()
                            );
        }

        // check status
        res.andExpect(status().isOk());

        // check contentType
        res.andExpect(content().contentType(contentType));

        // check state
        String content = response.getContentAsString();
        
        return res;
    }

    public void testExportAsHtml(final String id, final String name) throws Exception {
//        // request
//        MockHttpServletRequestBuilder req = 
//                        MockMvcRequestBuilders.get("/exports/show/" + id);
//        ResultActions res = testBaseRequest(MediaType);
//        
//        // check data
//        res.andExpect(jsonPath("$.node.name", is(name)))
//           .andExpect(jsonPath("$.node.sysUID", is(id)));
    }
}
