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
package de.yaio.app.core.recalcer;

import de.yaio.app.core.datadomainservice.StatDataService;
import de.yaio.app.core.datadomainservice.StatDataServiceImpl;
import de.yaio.app.core.dbservice.BaseNodeRepository;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.utils.db.DBFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/** 
 * recalc statData
 */
public class StatDataRecalcer {

    @Autowired
    protected BaseNodeRepository baseNodeDBService;

    protected StatDataService statDataService = StatDataServiceImpl.getInstance();

    // Logger
    private static final Logger LOGGER = Logger.getLogger(StatDataRecalcer.class);


    /**
     * service functions to recalc nodedata
     */
    public StatDataRecalcer() {
        super();
    }

    /** 
     * read all nodes and recalc statdata
     * @return                       result-message
     */
    @Transactional
    public String recalcStatData() {

        long count = baseNodeDBService.countBaseNodes();
        // look for this masternode in DB
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read nodes for recalc statData count:" + count);
        }
        int maxPerRun = 500;
        int maxRun = new Long(count / maxPerRun).intValue();

        List<DBFilter> dbFilters = new ArrayList<>();
        TypedQuery<BaseNode> query = baseNodeDBService.createTypedQuery(BaseNode.class, dbFilters, "order by ebene asc");

        for (int run = 0; run <= maxRun; run++) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("update nodes for recalc statData run:" + (run * maxPerRun));
            }
            query.setFirstResult(run * maxPerRun);
            query.setMaxResults(maxPerRun);
            List<BaseNode> nodes = query.getResultList();
            for (BaseNode node: nodes) {
                node.initChildNodesFromDB(0);
                statDataService.updateChildrenCount(node);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("updated statdata node:" + node.getNameForLogger());
                }

                baseNodeDBService.update(node);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("save node:" + node.getNameForLogger());
                }
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("done update nodes for recalc statData:" + count);
        }

        return "update statData for nodes done";
    }
}
