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
package de.yaio.core.datadomain;

import javax.xml.bind.annotation.XmlTransient;


/** 
 * interface for DataDomain: ResIndex (Mime, DMSRef -> for Urls, Files...) of the Node
 * 
 * @FeatureDomain                DataDefinition
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface ResContentData extends DataDomain {
    int CONST_ORDER = 26;

    @XmlTransient
    public static enum UploadWorkflowState {
        // the order is important for the calculation of workflow!!!!
        NOUPLOAD, 
        UPLOAD_OPEN, 
        UPLOAD_RUNNING, 
        UPLOAD_FAILED, 
        UPLOAD_DONE
    }
    
    public String getResContentMime();
    public void setResContentMime(String resContentMime);
    public Long getResContentSize();
    public void setResContentSize(Long resContentSize);
    
    public String getResContentDMSId();
    public void setResContentDMSId(String resContentDMSId);
    public String getResContentDMSType();
    public void setResContentDMSType(String resContentDMSType);
    public UploadWorkflowState getResContentDMSState();
    public void setResContentDMSState(UploadWorkflowState resContentDMSState);

    void resetResContentData();
}
