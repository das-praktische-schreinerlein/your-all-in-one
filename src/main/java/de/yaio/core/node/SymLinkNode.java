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
package de.yaio.core.node;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.yaio.core.datadomain.SymLinkData;
import de.yaio.core.nodeservice.BaseNodeService;
import de.yaio.core.nodeservice.SymLinkNodeService;

/** 
 * bean with SymLinkNode-data (SymLink to another node corresponding 
 * fileystems-link) and matching businesslogic
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
}
