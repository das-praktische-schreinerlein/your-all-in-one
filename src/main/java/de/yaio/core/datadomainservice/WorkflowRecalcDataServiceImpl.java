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
package de.yaio.core.datadomainservice;

import de.yaio.core.datadomain.BaseWorkflowData;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.WorkflowState;
import de.yaio.core.dbservice.DBFilter;
import de.yaio.core.node.BaseNode;
import de.yaio.core.nodeservice.NodeService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * recalc workflowData
 */
@Service
public class WorkflowRecalcDataServiceImpl extends TriggeredDataDomainRecalcImpl implements WorkflowRecalcDataService {

    protected BaseWorkflowDataServiceImpl baseWorkflowDataService = BaseWorkflowDataServiceImpl.getInstance();

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(WorkflowRecalcDataServiceImpl.class);

    public Class<?> getRecalcTargetClass() {
        return BaseWorkflowData.class;
    }

    @Override
    @Transactional
    public void doRecalcWhenTriggered(final DataDomain datanode) throws Exception {
        if (datanode == null) {
            return;
        }

        // Check if node is compatible
        if (!BaseWorkflowData.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        BaseNode node = (BaseNode)datanode;

        // Roll
        try {
            // recalc
            baseWorkflowDataService.doRecalcBeforeChildren(node, NodeService.RecalcRecurseDirection.ONLYME);
            baseWorkflowDataService.doRecalcAfterChildren(node, NodeService.RecalcRecurseDirection.ONLYME);
        } catch (IOException ex) {
            LOGGER.error("error while recalcing node:" + node.getNameForLogger(), ex);
        }

        // save node
        node.getBaseNodeDBService().updateBaseNode(node);
    }

    @Override
    protected String createSort() throws Exception {
        return "order by ebene desc";
    }

    @Override
    public List<DBFilter> getDBTriggerFilter() throws IOException {
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();

        // filter open and running only
        String sql = "((planStart < :now and (istStand = 0 or istStand is null)) " +
                " or (planEnde < :now and (istStand < 100 or istStand is null)))" +
                " and (workflowState in (:workflowState1, :workflowState2))";
        List<DBFilter.Parameter> parameters = new ArrayList<DBFilter.Parameter>();
        parameters.add(new DBFilter.Parameter("now", new Date()));
        parameters.add(new DBFilter.Parameter("workflowState1", WorkflowState.OPEN));
        parameters.add(new DBFilter.Parameter("workflowState2", WorkflowState.RUNNING));
        dbFilters.add(new DBFilter(sql, parameters));

        return dbFilters;
    }
}
