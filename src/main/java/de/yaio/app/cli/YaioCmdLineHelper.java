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
package de.yaio.app.cli;

import de.yaio.app.config.MinimalContextConfig;
import de.yaio.app.utils.CmdLineHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;



/** 
 * baseclass for configuration
 *
 * @FeatureDomain                Configuration
 * @package                      de.yaio.utils
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @copyright                    Copyright (c) 2013, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class YaioCmdLineHelper extends CmdLineHelper {

    /** property: file location exportcontroller.replacer.config **/
    public static final String CONST_PROPNAME_EXPORTCONTROLLER_REPLACER = 
                    "yaio.exportcontroller.replacerdef.location";
    /** property:  exportcontroller.replacer documentation pattern**/
    public static final String CONST_PROPNAME_EXPORTCONTROLLER_REPLACER_DOCUMENTATION_SRC = 
                    "replacer.documentation.pattern";
    /** property:  exportcontroller.replacer documentation target**/
    public static final String CONST_PROPNAME_EXPORTCONTROLLER_REPLACER_DOCUMENTATION_TARGET = 
                    "replacer.documentation.target";
    
    
    /** property:  yaioinstances.name **/
    public static final String CONST_PROPNAME_YAIOINSTANCES_NAME = 
                    "yaio.knowninstances.name";
    /** property:  yaioinstances.desc **/
    public static final String CONST_PROPNAME_YAIOINSTANCES_DESC = 
                    "yaio.knowninstances.desc";
    /** property:  yaioinstances.url **/
    public static final String CONST_PROPNAME_YAIOINSTANCES_URL = 
                    "yaio.knowninstances.url";
    
    /** property: masterid to export for static datasource */
    public static final String CONST_PROPNAME_YAIOEXPORT_STATIC_MASTERID = 
                    "yaio.staticdatasource.mastersysuid";
    

    private static final Logger LOGGER = Logger.getLogger(YaioCmdLineHelper.class);

    // must be instantiated after LOGGER because it is used in constructor
    protected static final YaioCmdLineHelper yaioinstance = new YaioCmdLineHelper();
    
    /** the configured yaioInstances to allow XFrameHeader and to construct in sourceselector of app */
    protected final Map<String, Map<String, String>> knownYaioInstances = new LinkedHashMap<String, Map<String, String>>();

    /** replacements to do after processing a node in documentation-context **/
    public static final Map<String, String> PostProcessorReplacements_documentation = 
                    new LinkedHashMap<String, String>();
    
    protected ApplicationContext applicationContext;
    protected YaioCmdLineHelper() {
        super();
    }
    
    /**
     * return the current static YaioCmdLineHelper-instance
     * @return                       the current YaioCmdLineHelper-instance
     * @throws Exception             parse/io-Exceptions possible
     */
    public static YaioCmdLineHelper getInstance() {
        return YaioCmdLineHelper.yaioinstance;
    }
    

    /* 
     ***********************
     ***********************
     * SpringApplicationContext
     ***********************
     ***********************
     */
    
    /** 
     * return the current SpringApplicationContext 
     * if is not set call ApplicationContext.initSpringApplicationContext()
     * @return                       the current SpringApplicationContext
     * @throws Exception             parse/io-Exceptions possible
     */
    public ApplicationContext getSpringApplicationContext() throws Exception {
        // check weather exists
        if (applicationContext == null) {
            this.initSpringApplicationContext();
        }
        
        return applicationContext;
    }

    public Properties initProperties()  throws Exception {
        Properties props = super.initProperties();

        // load PostProcessorReplacements
        String replacerConfigPath = props.getProperty(
                CONST_PROPNAME_EXPORTCONTROLLER_REPLACER);
        if (replacerConfigPath != null) {
            Properties replacerConfig = readProperties(replacerConfigPath);

            // load defined
            int count = replacerConfig.size() / 2;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("check PostProcessorReplacements_documentation found:"
                        + count + " in file:" + replacerConfigPath + " props:" + replacerConfig);
            }
            for (int zaehler = 0; zaehler <= count; zaehler++) {
                String keyName = CONST_PROPNAME_EXPORTCONTROLLER_REPLACER_DOCUMENTATION_SRC + "." + (zaehler + 1);
                String pattern = replacerConfig.getProperty(keyName);
                String valueName = CONST_PROPNAME_EXPORTCONTROLLER_REPLACER_DOCUMENTATION_TARGET + "." + (zaehler + 1);
                String target = replacerConfig.getProperty(valueName);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.info("check PostProcessorReplacements_documentation:"
                            + zaehler + " " + keyName + "=" + valueName
                            + " / " + pattern + "=" + target);
                }
                if (pattern != null) {
                    PostProcessorReplacements_documentation.put(
                            pattern,
                            target != null ? target : "");
                    LOGGER.info("set PostProcessorReplacements_documentation:"
                            + pattern + "=" + target);
                }
            }
        }

        // load defined
        int count = props.size() / 3;
        for (int zaehler = 0; zaehler <= count; zaehler++) {
            String keyName = CONST_PROPNAME_YAIOINSTANCES_NAME + "." + (zaehler + 1);
            String name = props.getProperty(keyName);
            String descName = CONST_PROPNAME_YAIOINSTANCES_DESC + "." + (zaehler + 1);
            String desc = props.getProperty(descName);
            String urlName = CONST_PROPNAME_YAIOINSTANCES_URL + "." + (zaehler + 1);
            String url = props.getProperty(urlName);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("check YaioInstances:"
                        + zaehler + " " + keyName + "=" + name
                        + " / " + urlName + "=" + url
                        + " / " + descName + "=" + desc);
            }
            if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(url)) {
                Map<String, String> data = new HashMap<String, String>();
                data.put(CONST_PROPNAME_YAIOINSTANCES_NAME, name);
                data.put(CONST_PROPNAME_YAIOINSTANCES_DESC, desc);
                data.put(CONST_PROPNAME_YAIOINSTANCES_URL, url);
                this.knownYaioInstances.put(name, data);
                LOGGER.info("set YaioInstances:" + name + "=" + url + " " + desc);
            }
        }

        return props;
    }

    protected void initSpringApplicationContext() throws Exception {
        // check
        if (applicationContext != null) {
            throw new IllegalStateException("initSpringApplicationContext: "
                            + "applicationContext already set");
        }

        Properties props = this.initProperties();

        // init applicationContext
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(MinimalContextConfig.class);
        applicationContext.refresh();
    }

    /**
     * @return                       the {@link YaioCmdLineHelper#knownYaioInstances}
     */
    public final Map<String, Map<String, String>> getKnownYaioInstances() {
        return this.knownYaioInstances;
    }
}
