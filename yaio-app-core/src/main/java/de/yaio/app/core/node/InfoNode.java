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
import de.yaio.app.core.nodeservice.InfoNodeService;
import de.yaio.app.core.datadomain.DocLayoutData;
import de.yaio.app.core.nodeservice.BaseNodeService;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import javax.xml.bind.annotation.XmlTransient;

/** 
 * bean with InfoNode-data (ideas, documentation...) and belonging businesslogic
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
public class InfoNode extends BaseNode implements DocLayoutData {
    
    protected static InfoNodeService nodeDataService = InfoNodeService.getInstance();

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
}
