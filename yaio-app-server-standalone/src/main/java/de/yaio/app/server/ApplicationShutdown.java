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
package de.yaio.app.server;

import de.yaio.app.config.YaioConfiguration;
import de.yaio.app.core.datadomainservice.NodeNumberService;
import de.yaio.app.core.node.BaseNode;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * do cli on applicationStartup
 */
@Component
public class ApplicationShutdown implements ApplicationListener<ContextClosedEvent> {

    protected static NodeNumberService nodeNumberService;
    protected static String strPathIdDB;

    private static final Logger LOGGER = Logger.getLogger(ApplicationShutdown.class);

    /*
     * This method is called during Spring's shutdown.
     *
     * @param event Event raised when an ApplicationContext gets shutdown.
     */
    @Override
    public void onApplicationEvent(final ContextClosedEvent event) {
        shutdownMetaDataService();
    }

    /**
     * shutdownMetaDataService metadataservice on shutdown
     */
    protected void shutdownMetaDataService() {
        try {
            // Ids speichern
            LOGGER.info("shutdownMetaDataService start");
            nodeNumberService = BaseNode.getConfiguredMetaDataService().getNodeNumberService();
            strPathIdDB = YaioConfiguration.getInstance().getCliOption("pathiddb", null).getStringValue();
            if (strPathIdDB != null && nodeNumberService != null) {
                // save to file
                LOGGER.info("shutdownMetaDataService export nextNodeNumbers to " + strPathIdDB +
                        " with:" + nodeNumberService.getNextNodeNumberMap());
                nodeNumberService.exportNextNodeNumbersToFile(strPathIdDB);
            }

            LOGGER.info("shutdownMetaDataService done");
        } catch (Exception ex) {
            LOGGER.error("ERROR while shutdownMetaDataService on shutdown", ex);
        }
    }
}
