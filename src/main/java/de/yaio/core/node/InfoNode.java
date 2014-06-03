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

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import de.yaio.core.datadomain.DocLayoutData;

/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 *     Persistence
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     bean with InfoNode-data (ideas, documentation...) and belonging businesslogic
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
public class InfoNode extends BaseNode implements DocLayoutData {
    
    // Daten
    public static final String CONST_NODETYPE_IDENTIFIER_INFO = "INFO";
    public static final String CONST_NODETYPE_IDENTIFIER_IDEE = "IDEE";
    public static final String CONST_NODETYPE_IDENTIFIER_DOKU = "DOKU";
    public static final String CONST_NODETYPE_IDENTIFIER_HOWTO = "HOWTO";
    
    // Status-Konstanten
    public static Map<String, Object> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, Object>();
    static {
        // Defaults
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_INFO, CONST_NODETYPE_IDENTIFIER_INFO);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_DOKU, CONST_NODETYPE_IDENTIFIER_DOKU);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_IDEE, CONST_NODETYPE_IDENTIFIER_IDEE);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_HOWTO, CONST_NODETYPE_IDENTIFIER_HOWTO);
        // Abarten
        CONST_MAP_NODETYPE_IDENTIFIER.put("INFO", CONST_NODETYPE_IDENTIFIER_INFO);
        CONST_MAP_NODETYPE_IDENTIFIER.put("DOKU", CONST_NODETYPE_IDENTIFIER_DOKU);
        CONST_MAP_NODETYPE_IDENTIFIER.put("IDEE", CONST_NODETYPE_IDENTIFIER_IDEE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("HOWTO", CONST_NODETYPE_IDENTIFIER_HOWTO);
    }

    @Override
    public Map<String, Object> getConfigState() {
        return CONST_MAP_NODETYPE_IDENTIFIER;
    }
}
