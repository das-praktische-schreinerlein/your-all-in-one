/**
 * <h4>FeatureDomain:</h4>
 *     Collaboration
 *
 * <h4>FeatureDescription:</h4>
 *     software for projectmanagement and documentation
 *
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.core.node;
import java.util.Map;

import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.yaio.core.datadomain.ResLocData;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.core.nodeservice.UrlResNodeService;

/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 *     Persistence
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     bean with UrlResNode-data (files, urls, links) and matching businesslogic
 *
 * @package de.yaio.core.node
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class UrlResNode extends InfoNode implements ResLocData {
    @Transient
    protected static NodeService nodeDataService = new UrlResNodeService();

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
    public NodeService getNodeService() {
        return nodeDataService;
    }
    public static void setNodeDataService(final NodeService newNodeDataService) {
        nodeDataService = newNodeDataService;
    }
    
    @Override
    @XmlTransient
    @JsonIgnore
    public Map<String, Object> getConfigState() {
        return UrlResNodeService.CONST_MAP_NODETYPE_IDENTIFIER;
    }

    @Override
    @XmlTransient
    @JsonIgnore
    public String getDataBlocks4CheckSum() throws Exception {
        // Content erzeugen
        StringBuffer data = new StringBuffer();
        
        data.append(super.getDataBlocks4CheckSum())
            .append(" resLocRef=").append(getResLocRef())
            .append(" resLocName=").append(getResLocName())
            .append(" resLocTags=").append(getResLocTags());
        return data.toString();
    }
}
