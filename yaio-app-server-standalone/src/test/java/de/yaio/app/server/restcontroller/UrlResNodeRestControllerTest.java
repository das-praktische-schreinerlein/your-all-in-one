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
package de.yaio.app.server.restcontroller;

import com.jayway.jsonpath.JsonPath;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.node.UrlResNode;
import org.hamcrest.core.IsNull;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/** 
 * test: RESTFull webservices for UrlResNodes
 * 
 * @FeatureDomain                Tests
 * @package                      de.yaio.rest.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     tests
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class UrlResNodeRestControllerTest extends BaseNodeRestControllerTest {
    
    /** 
     * testobject for UrlResNode
     * 
     * @package                      de.yaio.rest.controller
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     tests
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    public class UrlResNodeRestControllerTestObj extends UrlResNode implements TestObj {
        @Override
        public String toString() {
            StringBuffer resBuffer = new StringBuffer();
            resBuffer.append(this.getName()).append("|")
                     .append(this.getNameForLogger()).append("|");
            return resBuffer.toString();
        }
        
        @Override
        public String getClassName() {
            return "UrlResNode";
            
        }
    }

    /* (non-Javadoc)
     * @see de.yaio.app.BaseTest#setupNewTestObj()
     */
    @Override
    public TestObj setupNewTestObj() {
        // create, show, update, delete task
        String name1 = "Test-UrlResNode";
        
        UrlResNodeRestControllerTestObj node = new UrlResNodeRestControllerTestObj();
        node.setName(name1);
        
        return node;
    }
    
    @Override
    public String testCreateNode(final String parentId, final BaseNode node) throws Exception {
        // configure request
        MockMultipartFile file = new MockMultipartFile("uploadFile", "myFile.txt", 
                        "text/plain", "hello".getBytes());
        MockMultipartFile json = new MockMultipartFile("node", "", "application/json", convertObjectToJsonBytes(node));
        file = null;

        MockMultipartHttpServletRequestBuilder req = MockMvcRequestBuilders.fileUpload(
                        "/nodes/create/" + node.getClassName() + "/" + parentId);
        req.file(json);
        if (file != null) {
            req.file(file);
        }

        ResultActions res = testBaseRequest(req);

        // check data
        res.andExpect(jsonPath("$.node.name", is(node.getName())))
           .andExpect(jsonPath("$.node.sysUID", IsNull.notNullValue()));

        // create JSON from String
        String response = res.andReturn().getResponse().getContentAsString();
        String sysUID = JsonPath.read(response, "$.node.sysUID");

        return sysUID;
    }

    @Override
    public ResultActions testUpdateNode(final BaseNode node) throws Exception {
        // configure request
        MockMultipartFile file = new MockMultipartFile("uploadFile", "myFile2.txt", 
                        "text/plain", "hello2".getBytes());
        MockMultipartFile json = new MockMultipartFile("node", "", "application/json", convertObjectToJsonBytes(node));
        file = null;

        MockMultipartHttpServletRequestBuilder req = MockMvcRequestBuilders.fileUpload(
                        "/nodes/update/" + node.getClassName() + "/" + node.getSysUID());
        req.file(json);
        if (file != null) {
            req.file(file);
        }

        ResultActions res = testBaseRequest(req);

        // check data
        res.andExpect(jsonPath("$.node.name", is(node.getName())))
           .andExpect(jsonPath("$.node.sysUID", is(node.getSysUID())));

        return res;
    }
}
