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
import de.yaio.app.core.nodeservice.BaseNodeService;
import de.yaio.app.core.nodeservice.EventNodeService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

/** 
 * bean with EventNode-data (calendar-events) and matching businesslogic
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Configurable
@Entity
public class EventNode extends TaskNode {

    @Transient
    private static EventNodeService nodeDataService = EventNodeService.getInstance();

    @XmlTransient
    @JsonIgnore
    @Override
    public BaseNodeService getBaseNodeService() {
        return nodeDataService;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
