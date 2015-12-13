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
package de.yaio.core.nodeservice;

import java.util.HashMap;
import java.util.Map;

import de.yaio.core.datadomain.BaseWorkflowData.WorkflowState;


/** 
 * businesslogic for entity: InfoNode
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
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
    private static final Map<String, String> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, String>();
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
     * return the main instance of this service
     * @FeatureDomain                Persistence
     * @FeatureResult                return the main instance of this service
     * @FeatureKeywords              Persistence
     * @return                       the main instance of this service
     */
    public static InfoNodeService getInstance() {
        return instance;
    }

    @Override
    public Map<String, String> getConfigState() {
        return CONST_MAP_NODETYPE_IDENTIFIER;
    }
    @Override
    public Map<String, WorkflowState> getConfigWorkflowState() {
        return super.getConfigWorkflowStateNoWorkflow();
    }
}
