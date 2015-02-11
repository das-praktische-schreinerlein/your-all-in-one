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
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.yaio.core.datadomain.ResLocData;

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
    
    // Status-Konstanten
    /** nodetype-identifier for parser/formatter on UrlResnode URL */
    public static final String CONST_NODETYPE_IDENTIFIER_URLRES = "URLRES";
    /** nodetype-identifier for parser/formatter on UrlResnode FILE */
    public static final String CONST_NODETYPE_IDENTIFIER_FILERES = "FILERES";
    /** nodetype-identifier for parser/formatter on UrlResnode IMAGEFILE */
    public static final String CONST_NODETYPE_IDENTIFIER_IMAGERES = "IMAGERES";
    /** nodetype-identifier for parser/formatter on UrlResnode EMAIL */
    public static final String CONST_NODETYPE_IDENTIFIER_EMAILRES = "EMAILRES";
    
    // Status-Konstanten
    public static final Map<String, Object> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, Object>();
    static {
        // Defaults
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_URLRES, CONST_NODETYPE_IDENTIFIER_URLRES);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_FILERES, CONST_NODETYPE_IDENTIFIER_FILERES);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_IMAGERES, CONST_NODETYPE_IDENTIFIER_IMAGERES);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EMAILRES, CONST_NODETYPE_IDENTIFIER_EMAILRES);
        // Abarten
        CONST_MAP_NODETYPE_IDENTIFIER.put("URLRES", CONST_NODETYPE_IDENTIFIER_URLRES);
        CONST_MAP_NODETYPE_IDENTIFIER.put("FILERES", CONST_NODETYPE_IDENTIFIER_FILERES);
        CONST_MAP_NODETYPE_IDENTIFIER.put("IMAGERES", CONST_NODETYPE_IDENTIFIER_IMAGERES);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EMAILRES", CONST_NODETYPE_IDENTIFIER_EMAILRES);
    }
    

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

    @Override
    @XmlTransient
    @JsonIgnore
    public Map<String, Object> getConfigState() {
        return CONST_MAP_NODETYPE_IDENTIFIER;
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
