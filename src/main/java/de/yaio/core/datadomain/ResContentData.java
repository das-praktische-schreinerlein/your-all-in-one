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
    enum UploadWorkflowState {
        // the order is important for the calculation of workflow!!!!
        NOUPLOAD, 
        UPLOAD_OPEN, 
        UPLOAD_RUNNING, 
        UPLOAD_FAILED, 
        UPLOAD_DONE
    }
    
    String getResContentMime();
    void setResContentMime(String resContentMime);
    Long getResContentSize();
    void setResContentSize(Long resContentSize);
    
    String getResContentDMSId();
    void setResContentDMSId(String resContentDMSId);
    String getResContentDMSType();
    void setResContentDMSType(String resContentDMSType);
    UploadWorkflowState getResContentDMSState();
    void setResContentDMSState(UploadWorkflowState resContentDMSState);

    void resetResContentData();
}
