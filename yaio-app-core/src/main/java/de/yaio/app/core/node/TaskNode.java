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
package de.yaio.app.core.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.yaio.app.core.datadomain.ExtendedWorkflowData;
import de.yaio.app.core.datadomainservice.BaseWorkflowDataServiceImpl;
import de.yaio.app.core.nodeservice.BaseNodeService;
import de.yaio.app.core.nodeservice.NodeService;
import de.yaio.app.core.nodeservice.TaskNodeService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

/** 
 * bean for (projects, tasks, todos) with TaskNode-data like (Plan, is) 
 * and matching businesslogic
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Configurable
@Entity
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

    @Override
    public void resetIstData() {
        this.setIstAufwand(null);
        this.setIstEnde(null);
        this.setIstStand(null);
        this.setIstStart(null);
    }

    @Override
    public void resetPlanData() {
        this.setPlanAufwand(null);
        this.setPlanEnde(null);
        this.setPlanStart(null);
    }

    @Override
    public void resetExtendedWorkflowData() {
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

