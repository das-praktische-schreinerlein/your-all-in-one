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
package de.yaio.jobs;

import de.yaio.core.datadomainservice.SysDataService;
import de.yaio.core.datadomainservice.SysDataServiceImpl;
import de.yaio.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.core.node.BaseNode;
import de.yaio.core.nodeservice.NodeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

/** 
 * recalc sysData
 */
public class SysDataRecalcer {

    protected SysDataService sysDataService = SysDataServiceImpl.getInstance();

    // Logger
    private static final Logger LOGGER = Logger.getLogger(SysDataRecalcer.class);


    /**
     * service functions to recalc nodedata
     */
    public SysDataRecalcer() {
        super();
    }

    /** 
     * read all nodes and recalc sysdata
     * @return                       result-message
     * @throws Exception             possible io/db/recalc-Exceptions
     */
    @Transactional
    public String recalcSysData() throws Exception {

        long count = BaseNode.countBaseNodes();
        // look for this masternode in DB
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read nodes for recalc sysData count:" + count);
        }
        int maxPerRun = 500;
        int maxRun = new Long(count / maxPerRun).intValue();
        TypedQuery<BaseNode> query = BaseNode.entityManager().createQuery("SELECT o FROM BaseNode o", BaseNode.class);

        for (int run = 0; run <= maxRun; run++) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("update nodes for recalc sysData run:" + (run * maxPerRun));
            }
            query.setFirstResult(run * maxPerRun);
            query.setMaxResults(maxPerRun);
            List<BaseNode> nodes = query.getResultList();
            for (BaseNode node: nodes) {
                String curCheckSum = node.getSysCurChecksum();
                sysDataService.initSysData(node);
                String newCheckSum = node.getSysCurChecksum();
                if (LOGGER.isDebugEnabled() && !StringUtils.isEmpty(newCheckSum) && !newCheckSum.equals(curCheckSum)) {
                    LOGGER.debug("updated sysdata node:" + node.getNameForLogger());
                }

                node.merge();
                // recalc+save masterNode and parents
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("save node:" + node.getNameForLogger());
                }
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("done update nodes for recalc sysData:" + count);
        }

        // add it to masterDBNode
        return "update sysData for nodes done";
    }
}
