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
package de.yaio.app.extension.autoformatter.services;

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.datadomain.DescData;
import de.yaio.app.core.datadomain.ResContentData;
import de.yaio.app.core.datadomainservice.TriggeredDataDomainRecalcImpl;
import de.yaio.app.core.dbservice.BaseNodeRepository;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.nodeservice.UrlResNodeService;
import de.yaio.app.extension.autoformatter.formatter.FileDescAutoFormatter;
import de.yaio.app.extension.autoformatter.formatter.EmailDescAutoFormatter;
import de.yaio.app.extension.autoformatter.formatter.UrlDescAutoFormatter;
import de.yaio.app.utils.db.DBFilter;
import de.yaio.commons.io.IOExceptionWithCause;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.yaio.app.extension.autoformatter.formatter.FileDescAutoFormatter.COMMAND_DO_AUTO_GENERATE_DESC;

/**
 * businesslogic for dataDomain: DescData (format desc )
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Service
public class DescAutoFormatterServiceImpl extends TriggeredDataDomainRecalcImpl implements DescAutoFormatterService {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(DescAutoFormatterServiceImpl.class);

    @Autowired
    protected BaseNodeRepository baseNodeDBService;
    @Autowired
    protected EmailDescAutoFormatter emailFormatter;
    @Autowired
    protected FileDescAutoFormatter fileFormatter;
    @Autowired
    protected UrlDescAutoFormatter urlFormatter;

    public Class<?> getRecalcTargetClass() {
        return ResContentData.class;
    }

    @Override
    @Transactional
    public void doRecalcWhenTriggered(final DataDomain datanode) {
        if (datanode == null) {
            return;
        }
        if (!BaseNode.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        BaseNode node = (BaseNode)datanode;

        // Roll
        try {
            // update
            this.autoFormatDesc(node);
        } catch (IOExceptionWithCause ex) {
            // error: reset id and set to failed
            LOGGER.info("IOExceptionWithCause while autoformat desc of node:" + datanode.getNameForLogger(), ex);
        } catch (IOException ex) {
            // error: reset id and set to failed
            LOGGER.error("IOException while autoformat desc of node:" + datanode.getNameForLogger(), ex);
        }

        // save node
        baseNodeDBService.update(node);
    }

    @Override
    public List<DBFilter> getDBTriggerFilter() {
        List<DBFilter> dbFilters = new ArrayList<>();

        // filter COMMAND_DO_AUTO_GENERATE_DESC
        String sql = "(upper(nodeDesc) like :nodeDescLike)";
        List<DBFilter.Parameter> parameters = new ArrayList<>();
        parameters.add(new DBFilter.Parameter("nodeDescLike", "<!---" + COMMAND_DO_AUTO_GENERATE_DESC.toUpperCase() + "%"));
        dbFilters.add(new DBFilter(sql, parameters));

        return dbFilters;
    }

    @Override
    public void autoFormatDesc(final DescData datanode) throws IOExceptionWithCause, IOException {
        if (!DescData.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        BaseNode node = (BaseNode)datanode;

        switch (node.getType()) {
            case UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILRES:
                emailFormatter.genMetadataForNode(node);
                return;
            case UrlResNodeService.CONST_NODETYPE_IDENTIFIER_FILERES:
                fileFormatter.genMetadataForNode(node);
                return;
            case UrlResNodeService.CONST_NODETYPE_IDENTIFIER_URLRES:
                urlFormatter.genMetadataForNode(node);
                return;
            default:
        }
    }

    @Override
    protected BaseNodeRepository getBaseNodeDBService() {
        return baseNodeDBService;
    }
}
