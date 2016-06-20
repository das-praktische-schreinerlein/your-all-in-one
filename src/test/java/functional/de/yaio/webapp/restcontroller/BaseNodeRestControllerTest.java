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
package de.yaio.webapp.restcontroller;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.yaio.BaseTest;
import de.yaio.app.Configurator;
import de.yaio.core.datadomainservice.NodeNumberService;
import de.yaio.core.node.BaseNode;
import org.apache.log4j.Logger;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
public abstract class BaseNodeRestControllerTest  extends BaseTest {
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
            Logger.getLogger(BaseNodeRestControllerTest.class);
    
    protected BaseNode baseNode;
    protected MockMvc mockMvc;

    @Override
    public void setUp() throws Exception {
        // initApplicationContext
        String[] args = new String[2];
        args[0] = "--config";
        args[1] = "./target/test-classes/config/application-test.properties";
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
        
        NodeRestController nodeController = new NodeRestController();
        mockMvc = MockMvcBuilders.standaloneSetup(nodeController).build();
    };

    @Override
    public void tearDown() throws Exception {
    };
    
    
    /***********************
     ***********************
     * common tests
     ***********************    
     ***********************/

    /** 
     * test the node-life-cycle (create, show, update, delete) of a TaskNode
     * @throws Exception             io-Exceptions possible
     */
    @Test
    public void doTestNodeLifeCycle() throws Exception {
        // create TestObj
        BaseNode node = (BaseNode) setupNewTestObj();
        node.setMetaNodePraefix("WEBTEST");
        
        // test the common lifecycle
        testNodeLifeCycle(node);
    }
    
    
    /***********************
     ***********************
     * common test-services
     ***********************    
     ***********************/

    
    /** 
     * service-function to test the node-life-cycle (create, show, update, delete)
     * @param node                   the node to create, show, update, delete
     * @throws Exception             io-Exceptions possible
     */
    public void testNodeLifeCycle(final BaseNode node) throws Exception {
        // create node
        String newSysUID = testCreateNode(CONST_TESTPARENTNODE_ID, node);
        node.setSysUID(newSysUID);
        
        // show node
        testShowNode(newSysUID, node.getName());
        
        // update node
        node.setName(node.getName() + "-updated");
        testUpdateNode(node);
        
        // show updated node
        testShowNode(newSysUID, node.getName());
        
        // delete node
        testDeleteNode(newSysUID);
    }
    

    /** 
     * service-function to test the response for getting a node
     * @param id                     id of the requested node
     * @param name                   name of the requested node
     * @throws Exception             io-Exceptions possible
     */
    public void testShowNode(final String id, final String name) throws Exception {
        // request
        MockHttpServletRequestBuilder req = 
                        MockMvcRequestBuilders.get("/nodes/show/" + id);
        ResultActions res = testBaseRequest(req);
        
        // check data
        res.andExpect(jsonPath("$.node.name", is(name)))
           .andExpect(jsonPath("$.node.sysUID", is(id)));
    }
        
     /** 
     * do test the response for getting the masternode
     * @throws Exception             io-Exceptions possible
     */
    public void testShowMasternode() throws Exception {
        // request
        testShowNode(CONST_MASTERNODE_ID, "Masterplan");
    }

    /** 
     * service-function to test the response for creating a node as child of the parentId
     * @param parentId               parentId of the new task
     * @param node                   the node to create
     * @return                       the sysUID of the new node
     * @throws Exception             io-Exceptions possible
     */
    public String testCreateNode(final String parentId, final BaseNode node) throws Exception {
        // request
        MockHttpServletRequestBuilder req = 
                        MockMvcRequestBuilders.post("/nodes/create/" 
                        + node.getClassName() 
                        + "/" + parentId);
        
        // set params
        req.content(convertObjectToJsonBytes(node));
        
        // set contenttype;
        req.contentType(APPLICATION_JSON_UTF8);
        ResultActions res = testBaseRequest(req);
        
        // check data
        res.andExpect(jsonPath("$.node.name", is(node.getName())))
           .andExpect(jsonPath("$.node.sysUID", IsNull.notNullValue()));
        
        // create JSON from String
        String response = res.andReturn().getResponse().getContentAsString();
        String sysUID = JsonPath.read(response, "$.node.sysUID");
        
        return sysUID;
    }

    /** 
     * service-function to test the response for updating a node
     * @param node                   the node to update on db
     * @return                       the ResultActions for more checks
     * @throws Exception             io-Exceptions possible
     */
    public ResultActions testUpdateNode(final BaseNode node) throws Exception {
        // request
        MockHttpServletRequestBuilder req = 
                        MockMvcRequestBuilders.patch("/nodes/update/" 
                        + node.getClassName() 
                        + "/" + node.getSysUID());
        
        // set params
        req.content(convertObjectToJsonBytes(node));
        
        // set contenttype;
        req.contentType(APPLICATION_JSON_UTF8);
        ResultActions res = testBaseRequest(req);
        
        // check data
        res.andExpect(jsonPath("$.node.name", is(node.getName())))
           .andExpect(jsonPath("$.node.sysUID", is(node.getSysUID())));
           
        return res;
    }

    /** 
     * service-function to test the response for deleting a node
     * @param sysuID                 sysuID of the node to delete
     * @return                       the ResultActions for more checks
     * @throws Exception             io-Exceptions possible
     */
    public ResultActions testDeleteNode(final String sysuID) throws Exception {
        // request
        MockHttpServletRequestBuilder req = 
                        MockMvcRequestBuilders.delete("/nodes/delete/" + sysuID);
        
        // set contenttype;
        req.contentType(APPLICATION_JSON_UTF8);
        ResultActions res = testBaseRequest(req);
        
        return res;
    }

    /***********************
     ***********************
     * base test-services
     ***********************    
     ***********************/

    /** 
     * service-function to test the standardchecks for the request
     * @param rb                     the reseust to test
     * @return                       the response for more checks
     * @throws Exception             io-Exception possible
     */
    public ResultActions testBaseRequest(final MockHttpServletRequestBuilder rb) throws Exception {
        // ask
        ResultActions res = mockMvc.perform(rb);

        // extract response
        MockHttpServletResponse response = res.andReturn().getResponse();
        if (response.getStatus() != 200) {
            LOGGER.error("result response-err:" + response.getStatus() 
                            + " content:" + response.getContentAsString()
                            + " errmsg:" + response.getErrorMessage()
                            );
        }

        // check status
        res.andExpect(status().isOk());

        // check contentType
        res.andExpect(content().contentType(APPLICATION_JSON_UTF8));

        // check state
        String content = response.getContentAsString();
        String state = JsonPath.read(content, "$.state");
        String stateMsg = JsonPath.read(content, "$.stateMsg");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("info state:" + state + " msg:" + stateMsg);
        }
        if (!"OK1".equalsIgnoreCase(state)) {
            LOGGER.error("error state:" + state + " msg:" + stateMsg);
        }
        res.andExpect(jsonPath("$.state", is("OK")));
        
        return res;
    }

    /** 
     * convert the obj to json to put it as request-parameter to the webservice
     * @param object                 the object to convert
     * @return                       json for the request
     * @throws IOException           io-Exceptions possible
     */
    public static byte[] convertObjectToJsonBytes(final Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
