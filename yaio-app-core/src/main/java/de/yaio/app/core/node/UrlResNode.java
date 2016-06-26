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
import de.yaio.app.core.nodeservice.UrlResNodeService;
import de.yaio.app.core.datadomain.ResContentData;
import de.yaio.app.core.datadomain.ResIndexData;
import de.yaio.app.core.datadomain.ResLocData;
import de.yaio.app.core.nodeservice.BaseNodeService;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/** 
 * bean with UrlResNode-data (files, urls, links) and matching businesslogic
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
public class UrlResNode extends InfoNode implements ResLocData, ResContentData, ResIndexData {
    @Transient
    protected static UrlResNodeService nodeDataService = UrlResNodeService.getInstance();

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
}
