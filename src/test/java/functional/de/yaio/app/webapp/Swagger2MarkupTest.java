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
package de.yaio.app.webapp;


//@ContextConfiguration(locations={"./config/applicationContext.xml"}, 
//    classes={Application.class, SwaggerConfig.class}, 
//    loader = SpringApplicationContextLoader.class )
//public class Swagger2MarkupTest {
//    /** Logger **/
//    private static final Logger LOGGER =
//            Logger.getLogger(Swagger2MarkupTest.class);
//    
//
//    @Autowired
//    private WebApplicationContext context;
//
//    protected MockMvc mockMvc;
//    
//    @Before
//    public void setUp() throws Exception {
//        // initApplicationContext
//        String[] args = new String[2];
//        args[0] = "--config";
//        args[1] = "./config/application.properties";
//        if (Configurator.getInstance().getCmdLineArgs() == null) {
//            Configurator.getInstance().getAvailiableCmdLineOptions();
//            Configurator.getInstance().setCmdLineArgs(args);
//            Configurator.getInstance().getCommandLine();
//            Configurator.getInstance().getSpringApplicationContext();
//        }
//
//        Swagger2Controller controller = new Swagger2Controller();
//        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
////        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
//
//    };
//
//    @Test
//    public void dummy() throws Exception {
//    }
//
//    @Test
//    public void convertSwaggerToAsciiDoc() throws Exception {
//        this.mockMvc.perform(get("/swaggerv2")
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(Swagger2MarkupResultHandler.outputDirectory("resources/docs/api/asci").build())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void convertSwaggerToMarkdown() throws Exception {
//        this.mockMvc.perform(get("/v2/api-docs")
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(Swagger2MarkupResultHandler.outputDirectory("resources/docs/api/markdown")
//                    .withMarkupLanguage(MarkupLanguage.MARKDOWN).build())
//                .andExpect(status().isOk());
//    }
//
//}
