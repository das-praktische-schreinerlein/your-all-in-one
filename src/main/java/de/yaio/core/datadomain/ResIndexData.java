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
 * interface for DataDomain: ResContent (Mime, DMSRef -> for Urls, Files...) of the Node
 * 
 * @FeatureDomain                DataDefinition
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface ResIndexData extends DataDomain {
    int CONST_ORDER = 27;

    @XmlTransient
    enum IndexWorkflowState {
        // the order is important for the calculation of workflow!!!!
        NOINDEX, 
        INDEX_OPEN, 
        INDEX_RUNNING, 
        INDEX_FAILED,
        INDEX_DONE
    }

    String getResIndexDMSId();
    void setResIndexDMSId(String resContentIndexDMSId);
    String getResIndexDMSType();
    void setResIndexDMSType(String resContentIndexDMSType);
    IndexWorkflowState getResIndexDMSState();
    void setResIndexDMSState(IndexWorkflowState resContentIndexDMSState);

    void resetResIndexData();
}
