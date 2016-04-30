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

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.WorkflowState;
import de.yaio.core.node.UrlResNode;

import java.util.HashMap;
import java.util.Map;


/** 
 * businesslogic for entity:  UrlResNode
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class UrlResNodeService extends InfoNodeService {

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
    private static final Map<String, String> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, String>();
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
    
    
    private static UrlResNodeService instance = new UrlResNodeService();
    
    /** 
     * return the main instance of this service
     * @return                       the main instance of this service
     */
    public static UrlResNodeService getInstance() {
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

    @Override
    public String getDataBlocks4CheckSum(final DataDomain baseNode) throws Exception {
        UrlResNode node = (UrlResNode) baseNode;
        
        // Content erzeugen
        StringBuffer data = new StringBuffer();
        data.append(super.getDataBlocks4CheckSum(node))
            .append(" resLocRef=").append(node.getResLocRef())
            .append(" resLocName=").append(node.getResLocName())
            .append(" resLocTags=").append(node.getResLocTags());
        return data.toString();
    }
}
