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
import de.yaio.app.core.datadomain.ResContentData;
import de.yaio.app.core.datadomain.ResIndexData;
import de.yaio.app.core.datadomain.ResLocData;
import de.yaio.app.core.nodeservice.BaseNodeService;
import de.yaio.app.core.nodeservice.UrlResNodeService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/** 
 * bean with UrlResNode-data (files, urls, links) and matching businesslogic
 *
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Configurable
@Entity
public class UrlResNode extends InfoNode implements ResLocData, ResContentData, ResIndexData {
    @Transient
    private static UrlResNodeService nodeDataService = UrlResNodeService.getInstance();

    /**
     */
    @Size(max = 2000)
    private String resLocRef;

    /**
     */
    @Size(max = 255)
    private String resLocName;

    /**
     */
    @Size(max = 255)
    private String resLocTags;

    /**
     */
    @Size(max = 60)
    private String resContentMime;

    /**
     */
    @Min(0L)
    private Long resContentSize;

    /**
     */
    @Size(max = 255)
    private String resContentDMSId;

    /**
     */
    @Size(max = 20)
    private String resContentDMSType;

    /**
     */
    private UploadWorkflowState resContentDMSState;

    /**
     */
    @Size(max = 255)
    private String resIndexDMSId;

    /**
     */
    @Size(max = 20)
    private String resIndexDMSType;

    /**
     */
    private IndexWorkflowState resIndexDMSState;

    @XmlTransient
    @JsonIgnore
    @Override
    public BaseNodeService getBaseNodeService() {
        return nodeDataService;
    }

    @Override
    public void resetResLocData() {
        this.setResLocRef(null);
        this.setResLocName(null);
        this.setResLocTags(null);
    }

    @Override
    public void resetResContentData() {
        this.setResContentDMSId(null);
        this.setResContentDMSType(null);
        this.setResContentDMSState(UploadWorkflowState.NOUPLOAD);
        this.setResContentSize(null);
        this.setResContentMime(null);
    }

    @Override
    public void resetResIndexData() {
        this.setResIndexDMSId(null);
        this.setResIndexDMSType(null);
        this.setResIndexDMSState(IndexWorkflowState.NOINDEX);
    }

    @Override
    public String getResLocRef() {
        return this.resLocRef;
    }

    @Override
    public void setResLocRef(String resLocRef) {
        this.resLocRef = resLocRef;
    }

    @Override
    public String getResLocName() {
        return this.resLocName;
    }

    @Override
    public void setResLocName(String resLocName) {
        this.resLocName = resLocName;
    }

    @Override
    public String getResLocTags() {
        return this.resLocTags;
    }

    @Override
    public void setResLocTags(String resLocTags) {
        this.resLocTags = resLocTags;
    }

    @Override
    public String getResContentMime() {
        return this.resContentMime;
    }

    @Override
    public void setResContentMime(String resContentMime) {
        this.resContentMime = resContentMime;
    }

    @Override
    public Long getResContentSize() {
        return this.resContentSize;
    }

    @Override
    public void setResContentSize(Long resContentSize) {
        this.resContentSize = resContentSize;
    }

    @Override
    public String getResContentDMSId() {
        return this.resContentDMSId;
    }

    @Override
    public void setResContentDMSId(String resContentDMSId) {
        this.resContentDMSId = resContentDMSId;
    }

    @Override
    public String getResContentDMSType() {
        return this.resContentDMSType;
    }

    @Override
    public void setResContentDMSType(String resContentDMSType) {
        this.resContentDMSType = resContentDMSType;
    }

    @Override
    public UploadWorkflowState getResContentDMSState() {
        return this.resContentDMSState;
    }

    @Override
    public void setResContentDMSState(UploadWorkflowState resContentDMSState) {
        this.resContentDMSState = resContentDMSState;
    }

    @Override
    public String getResIndexDMSId() {
        return this.resIndexDMSId;
    }

    @Override
    public void setResIndexDMSId(String resIndexDMSId) {
        this.resIndexDMSId = resIndexDMSId;
    }

    @Override
    public String getResIndexDMSType() {
        return this.resIndexDMSType;
    }

    @Override
    public void setResIndexDMSType(String resIndexDMSType) {
        this.resIndexDMSType = resIndexDMSType;
    }

    @Override
    public IndexWorkflowState getResIndexDMSState() {
        return this.resIndexDMSState;
    }

    @Override
    public void setResIndexDMSState(IndexWorkflowState resIndexDMSState) {
        this.resIndexDMSState = resIndexDMSState;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
