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
import de.yaio.app.core.datadomain.DocLayoutData;
import de.yaio.app.core.nodeservice.BaseNodeService;
import de.yaio.app.core.nodeservice.InfoNodeService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.annotation.Transient;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlTransient;

/** 
 * bean with InfoNode-data (ideas, documentation...) and belonging businesslogic
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Configurable
@Entity
public class InfoNode extends BaseNode implements DocLayoutData {
    
    @Transient
    private static InfoNodeService nodeDataService = InfoNodeService.getInstance();

    @XmlTransient
    @JsonIgnore
    @Override
    public BaseNodeService getBaseNodeService() {
        return nodeDataService;
    }

    @Override
    public void resetDocLayoutData() {
        this.setDocLayoutAddStyleClass(null);
        this.setDocLayoutFlgCloseDiv(null);
        this.setDocLayoutShortName(null);
        this.setDocLayoutTagCommand(null);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
