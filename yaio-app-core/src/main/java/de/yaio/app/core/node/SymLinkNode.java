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
import de.yaio.app.core.datadomain.SymLinkData;
import de.yaio.app.core.nodeservice.BaseNodeService;
import de.yaio.app.core.nodeservice.SymLinkNodeService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.Entity;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/** 
 * bean with SymLinkNode-data (SymLink to another node corresponding 
 * fileystems-link) and matching businesslogic
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Configurable
@Entity
public class SymLinkNode extends BaseNode implements SymLinkData {

    protected static SymLinkNodeService nodeDataService = SymLinkNodeService.getInstance();

    /**
     */
    @Size(max = 800)
    private String symLinkRef;

    /**
     */
    @Size(max = 255)
    private String symLinkName;

    /**
     */
    @Size(max = 255)
    private String symLinkTags;

    @XmlTransient
    @JsonIgnore
    public BaseNodeService getBaseNodeService() {
        return nodeDataService;
    }

    public String getSymLinkRef() {
        return this.symLinkRef;
    }

    public void setSymLinkRef(String symLinkRef) {
        this.symLinkRef = symLinkRef;
    }

    public String getSymLinkName() {
        return this.symLinkName;
    }

    public void setSymLinkName(String symLinkName) {
        this.symLinkName = symLinkName;
    }

    public String getSymLinkTags() {
        return this.symLinkTags;
    }

    public void setSymLinkTags(String symLinkTags) {
        this.symLinkTags = symLinkTags;
    }

    @Override
    public void resetSymLinkData() {
        this.setSymLinkRef(null);
        this.setSymLinkName(null);
        this.setSymLinkTags(null);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
