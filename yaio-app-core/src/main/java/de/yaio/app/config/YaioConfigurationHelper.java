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
package de.yaio.app.config;

import de.yaio.commons.io.IOExceptionWithCause;
import de.yaio.commons.cli.CmdLineHelper;
import de.yaio.commons.config.Configuration;
import de.yaio.commons.config.ConfigurationHelper;
import de.yaio.commons.config.ConfigurationOption;
import de.yaio.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class YaioConfigurationHelper extends ConfigurationHelper {

    private static final Logger LOGGER = Logger.getLogger(YaioConfiguration.class);

    // must be instantiated after LOGGER because it is used in constructor
    private static final YaioConfigurationHelper yaioinstance = new YaioConfigurationHelper();

    private YaioConfigurationHelper() {
        super();
    }
    
    /**
     * return the current static YaioCmdLineHelper-instance
     * @return                       the current YaioCmdLineHelper-instance
     * @throws Exception             parse/io-Exceptions possible
     */
    public static YaioConfigurationHelper getInstance() {
        return YaioConfigurationHelper.yaioinstance;
    }

    @Override
    public Configuration getConfigurationInstance() {
        return getYaioConfigurationInstance();
    }

    public YaioConfiguration getYaioConfigurationInstance() {
        return YaioConfiguration.getInstance();
    }

    @Override
    protected void initCalcedProperties(final CmdLineHelper cmdLineHelper) {
        // load PostProcessorReplacements
        super.initCalcedProperties(cmdLineHelper);
        String replacerConfigPath = ConfigurationOption.stringValueOf(
                this.getYaioConfigurationInstance().getCliOption(YaioConfiguration.CONST_PROPNAME_EXPORTCONTROLLER_REPLACER));
        if (replacerConfigPath != null) {
            Properties replacerConfig;
            try {
                replacerConfig = IOUtils.getInstance().readProperties(replacerConfigPath);
            } catch (IOExceptionWithCause ex) {
                throw new IllegalArgumentException("cant read propertyFile for replacerConfig", ex);
            }

            // load defined
            int count = replacerConfig.size() / 2;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("check PostProcessorReplacements_documentation found:"
                        + count + " in file:" + replacerConfigPath + " props:" + replacerConfig);
            }
            for (int zaehler = 0; zaehler <= count; zaehler++) {
                String keyName = YaioConfiguration.CONST_PROPNAME_EXPORTCONTROLLER_REPLACER_DOCUMENTATION_SRC + "." + (zaehler + 1);
                String pattern = replacerConfig.getProperty(keyName);
                String valueName = YaioConfiguration.CONST_PROPNAME_EXPORTCONTROLLER_REPLACER_DOCUMENTATION_TARGET + "." + (zaehler + 1);
                String target = replacerConfig.getProperty(valueName);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.info("check PostProcessorReplacements_documentation:"
                            + zaehler + " " + keyName + "=" + valueName
                            + " / " + pattern + "=" + target);
                }
                if (pattern != null) {
                    this.getYaioConfigurationInstance().getPostProcessorReplacements().put(
                            pattern,
                            target != null ? target : "");
                    LOGGER.info("set PostProcessorReplacements_documentation:"
                            + pattern + "=" + target);
                }
            }
        }

        // load defined
        Set optinNames = this.getYaioConfigurationInstance().getPropertieNames();
        int count = optinNames.size() / 3;
        for (int zaehler = 0; zaehler <= count; zaehler++) {
            String keyName = YaioConfiguration.CONST_PROPNAME_YAIOINSTANCES_NAME + "." + (zaehler + 1);
            String name = this.getYaioConfigurationInstance().getProperty(keyName, null).getStringValue();
            String descName = YaioConfiguration.CONST_PROPNAME_YAIOINSTANCES_DESC + "." + (zaehler + 1);
            String desc =this.getYaioConfigurationInstance().getProperty(descName, null).getStringValue();
            String urlName = YaioConfiguration.CONST_PROPNAME_YAIOINSTANCES_URL + "." + (zaehler + 1);
            String url = this.getYaioConfigurationInstance().getProperty(urlName, null).getStringValue();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("check YaioInstances:"
                        + zaehler + " " + keyName + "=" + name
                        + " / " + urlName + "=" + url
                        + " / " + descName + "=" + desc);
            }
            if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(url)) {
                Map<String, String> data = new HashMap<>();
                data.put(YaioConfiguration.CONST_PROPNAME_YAIOINSTANCES_NAME, name);
                data.put(YaioConfiguration.CONST_PROPNAME_YAIOINSTANCES_DESC, desc);
                data.put(YaioConfiguration.CONST_PROPNAME_YAIOINSTANCES_URL, url);
                this.getYaioConfigurationInstance().knownYaioInstances.put(name, data);
                LOGGER.info("set YaioInstances:" + name + "=" + url + " " + desc);
            }
        }
    }
}
