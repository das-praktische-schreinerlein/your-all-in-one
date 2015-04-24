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
package de.yaio.core.nodeservice;

import java.util.HashMap;
import java.util.Map;


/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for entity: InfoNode
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class InfoNodeService extends BaseNodeService {

    // Daten
    /** nodetype-identifier for parser/formatter on Infonode INFO */
    public static final String CONST_NODETYPE_IDENTIFIER_INFO = "INFO";
    /** nodetype-identifier for parser/formatter on Infonode Idea */
    public static final String CONST_NODETYPE_IDENTIFIER_IDEE = "IDEE";
    /** nodetype-identifier for parser/formatter on Infonode Documentation */
    public static final String CONST_NODETYPE_IDENTIFIER_DOKU = "DOKU";
    /** nodetype-identifier for parser/formatter on Infonode Howto */
    public static final String CONST_NODETYPE_IDENTIFIER_HOWTO = "HOWTO";

    // Status-Konstanten
    public static final Map<String, Object> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, Object>();

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

    private static InfoNodeService instance = new InfoNodeService();
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     return the main instance of this service
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return the main instance of this service
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @return the main instance of this service
     */
    public static InfoNodeService getInstance() {
        return instance;
    }
}
