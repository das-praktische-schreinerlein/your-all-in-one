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
package de.yaio.app.utils;

import java.util.HashMap;
import java.util.Map;


/** 
 * container for configs
 * 
 * @FeatureDomain                Configuration
 * @package                      de.yaio.utils
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class CommonConfigContainer {
    protected Map<Class<?>, TargetConfig> targetConfigs = new HashMap<Class<?>, TargetConfig>();
    protected TargetConfig  globalConfig = new TargetConfig();
    
    public class TargetConfig {
        Map<Class<? extends CommonConfig>, CommonConfig> commonConfigs = new HashMap<Class<? extends CommonConfig>, CommonConfig>();
        
        public CommonConfig getConfig(Class<? extends CommonConfig> config) {
            return commonConfigs.get(config);
        }

        public void putConfig(Class<? extends CommonConfig> configClass, CommonConfig config) {
            commonConfigs.put(configClass, config);
        }
    }
    
    public TargetConfig getConfigForTarget(Class<?> target) {
        return targetConfigs.get(target);
    }

    public void addConfigForTarget(Class<?> target, Class<? extends CommonConfig> configClass, CommonConfig config) {
        TargetConfig targetConfig = targetConfigs.get(target);
        
        if (targetConfig == null) {
            targetConfig = new TargetConfig();
            targetConfig.putConfig(configClass, config);
        }
        
        targetConfig.putConfig(configClass, config);
    }

    public void addConfigForTarget(Class<?> target, CommonConfig config) {
        addConfigForTarget(target, config.getConfig(), config);
    }

    public void addConfigForTarget(CommonConfig config) {
        addConfigForTarget(config.getTarget(), config);
    }

    public TargetConfig getGlobalConfig() {
        return globalConfig;
    }

    public void addGlobalConfig(Class<? extends CommonConfig> configClass, CommonConfig config) {
        globalConfig.putConfig(configClass, config);
    }

    public void addGlobalConfig(CommonConfig config) {
        globalConfig.putConfig(config.getConfig(), config);
    }
    
    public CommonConfig getBestInstanceOf(Class<?> target, Class<? extends CommonConfig> configClass) {
        TargetConfig targetConfig = targetConfigs.get(target);
        if (targetConfig != null) {
            if (targetConfig.getConfig(configClass) != null) {
                return targetConfig.getConfig(configClass);
            }
        }

        if (globalConfig.getConfig(configClass) != null) {
            return globalConfig.getConfig(configClass);
        }
        
        return null;
    }
}
