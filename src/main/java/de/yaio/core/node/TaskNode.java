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
package de.yaio.core.node;
import java.util.Date;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.yaio.core.datadomain.ExtendedWorkflowData;
import de.yaio.core.datadomainservice.BaseWorkflowDataServiceImpl;
import de.yaio.core.nodeservice.BaseNodeService;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.core.nodeservice.TaskNodeService;

/** 
 * bean for (projects, tasks, todos) with TaskNode-data like (Plan, is) 
 * and matching businesslogic
 * 
 * @FeatureDomain                DataDefinition Persistence BusinessLogic
 * @package                      de.yaio.core.node
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class TaskNode extends BaseNode implements ExtendedWorkflowData {
    @Transient
    protected static TaskNodeService nodeDataService = TaskNodeService.getInstance();

    @XmlTransient
    @JsonIgnore
    @Override
    public BaseNodeService getBaseNodeService() {
        return nodeDataService;
    }

    @XmlTransient
    @JsonIgnore
    public static NodeService getConfiguredNodeService() {
        return nodeDataService;
    }

    @Override
    @XmlTransient
    @JsonIgnore
    public Date getCurrentStart() {
        return BaseWorkflowDataServiceImpl.getInstance().calcCurrentStart(this);
    }

    @Override
    @XmlTransient
    @JsonIgnore
    public Date getCurrentEnde() {
        return BaseWorkflowDataServiceImpl.getInstance().calcCurrentEnde(this);
    }
}
