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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.yaio.core.datadomain.ResLocData;
import de.yaio.core.nodeservice.BaseNodeService;
import de.yaio.core.nodeservice.UrlResNodeService;

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
public class UrlResNode extends InfoNode implements ResLocData {
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

    @XmlTransient
    @JsonIgnore
    @Override
    public BaseNodeService getBaseNodeService() {
        return nodeDataService;
    }
}
