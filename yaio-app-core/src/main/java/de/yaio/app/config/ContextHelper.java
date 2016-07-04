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

import de.yaio.app.utils.config.Configuration;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class ContextHelper {
    private static final Logger LOGGER = Logger.getLogger(ContextHelper.class);

    // must be instantiated after LOGGER because it is used in constructor
    protected static final ContextHelper yaioinstance = new ContextHelper();

    protected ApplicationContext applicationContext;
    protected List<Class>configClasses = new ArrayList<>();


    protected ContextHelper() {
        super();
    }

    public static ContextHelper getInstance() {
        return ContextHelper.yaioinstance;
    }

    public void addSpringConfig(Class...contextConfig) {
        // check weather exists
        if (applicationContext != null) {
            throw new IllegalStateException("context allreday initialized");
        }

        configClasses.addAll(Arrays.asList(contextConfig));
    }

    public List<Class> getSpringConfig() {
        return configClasses;
    }

    /**
     * return the current SpringApplicationContext
     * if is not set call ApplicationContext.initSpringApplicationContext()
     * @return                       the current SpringApplicationContext
     */
    public ApplicationContext getSpringApplicationContext() {
        // check weather exists
        if (applicationContext == null) {
            this.initSpringApplicationContext();
        }

        return applicationContext;
    }

    protected void initSpringApplicationContext() {
        // check
        if (applicationContext != null) {
            throw new IllegalStateException("initSpringApplicationContext: "
                            + "applicationContext already set");
        }

        Configuration config = YaioConfiguration.getInstance();
        config.publishProperties();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("init spring with props:" + System.getProperties());
        }

        // init applicationContext
        applicationContext = new AnnotationConfigApplicationContext();
        for (Class<?> configClass : configClasses) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("init spring with class:" + configClass);
            }
            ((AnnotationConfigApplicationContext)applicationContext).register(configClass);
        }
        ((AnnotationConfigApplicationContext)applicationContext).refresh();
    }
}
