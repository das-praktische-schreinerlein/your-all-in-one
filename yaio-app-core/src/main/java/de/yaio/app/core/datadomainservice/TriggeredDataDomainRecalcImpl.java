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
package de.yaio.app.core.datadomainservice;

import de.yaio.app.core.dbservice.BaseNodeRepository;
import de.yaio.app.core.node.BaseNode;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/** 
 * baseservice for businesslogic of datadomains
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public abstract class TriggeredDataDomainRecalcImpl implements TriggeredDataDomainRecalc {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(TriggeredDataDomainRecalcImpl.class);

    @Override
    public void doSearchAndTrigger() {
        int maxPerRun = 500;

        // repeat until no more entries available
        boolean flgAllDone = false;
        while (!flgAllDone) {
            // create query and read
            TypedQuery<BaseNode> query = this.createQuery();
            query.setFirstResult(0);
            query.setMaxResults(maxPerRun);
            List<BaseNode> results = query.getResultList();
            if (results.size() > 0) {
                LOGGER.info("got results:" + results.size() + " for query: do iterate nodes");
            }

            // iterate all node
            for (BaseNode node : results) {
                this.doRecalcWhenTriggered(node);
            }
            
            // stop if no more entries available
            if (results.size() < maxPerRun) {
                flgAllDone = true;
            }
        }
    }

    protected String createSort() {
        return "order by ebene desc";
    }

    protected TypedQuery<BaseNode> createQuery() {
        return getBaseNodeDBService().createTypedQuery(BaseNode.class, this.getDBTriggerFilter(), this.createSort());
    }

    protected abstract BaseNodeRepository getBaseNodeDBService();
}
