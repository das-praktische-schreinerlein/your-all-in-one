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

import de.yaio.app.config.ContextHelper;
import de.yaio.app.config.YaioConfiguration;
import de.yaio.app.core.datadomainservice.NodeNumberService;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.recalcer.CachedDataRecalcer;
import de.yaio.app.core.recalcer.NodeRecalcer;
import de.yaio.app.core.recalcer.StatDataRecalcer;
import de.yaio.app.core.recalcer.SysDataRecalcer;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * do cli on applicationStartup
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ApplicationContext appContext;

    protected static NodeNumberService nodeNumberService;
    protected static String strPathIdDB;

    private static final Logger LOGGER = Logger.getLogger(ApplicationStartup.class);

    @Value(value = "${yaio.startup.recalcSysData}")
    private Boolean onStartupRecalcSysData;

    @Value(value = "${yaio.startup.recalcStatData}")
    private Boolean onStartupRecalcStatData;

    @Value(value = "${yaio.startup.recalcCachedData}")
    private Boolean onStartupRecalcCachedData;

    @Value(value = "${yaio.startup.recalcMasterSysUID}")
    private String onStartupRecalcMasterSysUID;

    /*
     * This method is called during Spring's startup.
     *
     * @param event Event raised when an ApplicationContext gets initialized or
     * refreshed.
     */
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        ContextHelper.getInstance().autowireDefaultServices(appContext);
        initMetaDataService();

        if (!StringUtils.isEmpty(onStartupRecalcMasterSysUID)) {
            recalcAllData();
            return;
        }
        if (onStartupRecalcSysData != null && onStartupRecalcSysData) {
            recalcSysData();
        }
        if (onStartupRecalcStatData != null && onStartupRecalcStatData) {
            recalcStatData();
        }
        if (onStartupRecalcCachedData != null && onStartupRecalcCachedData) {
            recalcCachedData();
        }
    }

    /**
     * init metadataservice on startup
     */
    protected void initMetaDataService() {
        try {
            // gets NodeNumberService
            LOGGER.info("initMetaDataService start");
            nodeNumberService =
                    BaseNode.getConfiguredMetaDataService().getNodeNumberService();

            // Id-Datei einlesen
            strPathIdDB = YaioConfiguration.getInstance().getCliOption("pathiddb", null).getStringValue();
            if (strPathIdDB != null) {
                if (nodeNumberService.isInitialised()) {
                    LOGGER.info("initMetaDataService from:" + strPathIdDB + " overwrite:" + nodeNumberService.getNextNodeNumberMap());
                } else {
                    LOGGER.info("initMetaDataService from:" + strPathIdDB + " initially");
                }


                nodeNumberService.initNextNodeNumbersFromFile(strPathIdDB, false);
                LOGGER.info("initMetaDataService from:" + strPathIdDB + " after:" + nodeNumberService.getNextNodeNumberMap());
            }
            LOGGER.info("initMetaDataService done");
        } catch (Exception ex) {
            LOGGER.error("ERROR while initMetaDataService on startup", ex);
        }
    }

    /**
     * recalc the sysData on startup
     */
    protected void recalcSysData() {
        try {
            SysDataRecalcer recalcer = new SysDataRecalcer();
            ContextHelper.getInstance().autowireService(appContext, recalcer);
            String res = recalcer.recalcSysData();
            LOGGER.info("recalcing SysData done:" + res);
        } catch (Exception ex) {
            LOGGER.error("ERROR while recalcSysData on startup", ex);
        }
    }

    /**
     * recalc the statData on startup
     */
    protected void recalcStatData() {
        try {
            StatDataRecalcer recalcer = new StatDataRecalcer();
            ContextHelper.getInstance().autowireService(appContext, recalcer);
            String res = recalcer.recalcStatData();
            LOGGER.info("recalcing StatData done:" + res);
        } catch (Exception ex) {
            LOGGER.error("ERROR while recalcStatData on startup", ex);
        }
    }

    /**
     * recalc the sysData on startup
     */
    protected void recalcCachedData() {
        try {
            CachedDataRecalcer recalcer = new CachedDataRecalcer();
            ContextHelper.getInstance().autowireService(appContext, recalcer);
            String res = recalcer.recalcCachedData();
            LOGGER.info("recalcing CachedData done:" + res);
        } catch (Exception ex) {
            LOGGER.error("ERROR while recalcCachedData on startup", ex);
        }
    }


    /**
     * recalc the statData on startup
     */
    protected void recalcAllData() {
        try {
            NodeRecalcer recalcer = new NodeRecalcer();
            ContextHelper.getInstance().autowireService(appContext, recalcer);
            String res = recalcer.findAndRecalcMasternode(this.onStartupRecalcMasterSysUID);
            LOGGER.info("recalcing StatData done:" + res);
        } catch (Exception ex) {
            LOGGER.error("ERROR while recalcStatData on startup", ex);
        }
    }
}
