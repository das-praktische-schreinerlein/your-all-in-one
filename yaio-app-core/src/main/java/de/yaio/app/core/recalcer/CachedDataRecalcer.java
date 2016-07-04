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

import de.yaio.app.core.datadomainservice.CachedDataService;
import de.yaio.app.core.datadomainservice.CachedDataServiceImpl;
import de.yaio.app.core.node.BaseNode;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

/** 
 * recalc cachedData
 */
public class CachedDataRecalcer {

    protected CachedDataService cachedDataService = CachedDataServiceImpl.getInstance();

    // Logger
    private static final Logger LOGGER = Logger.getLogger(CachedDataRecalcer.class);


    /**
     * service functions to recalc nodedata
     */
    public CachedDataRecalcer() {
        super();
    }

    /** 
     * read all nodes and recalc cachedData
     * @return                       result-message
     */
    @Transactional
    public String recalcCachedData() {

        long count = BaseNode.countBaseNodes();
        // look for this masternode in DB
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read nodes for recalc CachedData count:" + count);
        }
        int maxPerRun = 500;
        int maxRun = new Long(count / maxPerRun).intValue();
        TypedQuery<BaseNode> query = BaseNode.entityManager().createQuery("SELECT o FROM BaseNode o " +
                "where cachedParentHierarchy is null order by ebene asc", BaseNode.class);

        for (int run = 0; run <= maxRun; run++) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("update nodes for recalc CachedData run:" + (run * maxPerRun));
            }
            query.setFirstResult(run * maxPerRun);
            query.setMaxResults(maxPerRun);
            List<BaseNode> nodes = query.getResultList();
            for (BaseNode node: nodes) {
                cachedDataService.initParentHierarchy(node);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("updated CachedData node:" + node.getNameForLogger());
                }

                node.merge();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("save node:" + node.getNameForLogger());
                }
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("done update nodes for recalc CachedData:" + count);
        }

        return "update CachedData for nodes done";
    }
}
