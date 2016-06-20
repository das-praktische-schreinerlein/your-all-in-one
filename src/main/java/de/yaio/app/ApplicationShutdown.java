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
package de.yaio.app;

import de.yaio.core.datadomainservice.NodeNumberService;
import de.yaio.core.node.BaseNode;
import de.yaio.jobs.CachedDataRecalcer;
import de.yaio.jobs.NodeRecalcer;
import de.yaio.jobs.StatDataRecalcer;
import de.yaio.jobs.SysDataRecalcer;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * do jobs on applicationStartup
 */
@Component
public class ApplicationShutdown implements ApplicationListener<ContextStoppedEvent> {

    protected static NodeNumberService nodeNumberService;
    protected static String strPathIdDB;

    private static final Logger LOGGER = Logger.getLogger(ApplicationShutdown.class);

    /*
     * This method is called during Spring's shutdown.
     *
     * @param event Event raised when an ApplicationContext gets shutdown.
     */
    @Override
    public void onApplicationEvent(final ContextStoppedEvent event) {
        shutdownMetaDataService();
    }

    /**
     * shutdownMetaDataService metadataservice on shutdown
     */
    protected void shutdownMetaDataService() {
        try {
            // Ids speichern
            LOGGER.info("cleanUpAfterJob start");
            nodeNumberService = BaseNode.getConfiguredMetaDataService().getNodeNumberService();
            strPathIdDB = Configurator.getInstance().getCommandLine().getOptionValue("pathiddb", null);
            if (strPathIdDB != null && nodeNumberService != null) {
                // save to file
                LOGGER.info("shutdownMetaDataService export nextNodeNumbers to " + strPathIdDB);
                nodeNumberService.exportNextNodeNumbersToFile(strPathIdDB);
            }

            LOGGER.info("shutdownMetaDataService done");
        } catch (Exception ex) {
            LOGGER.error("ERROR while shutdownMetaDataService on shutdown", ex);
        }
    }
}
