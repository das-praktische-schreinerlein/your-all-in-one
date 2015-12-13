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
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.SymLinkNode;


/** 
 * businesslogic for entity: SymLinkNode
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class SymLinkNodeService extends BaseNodeService {

    /** nodetype-identifier for parser/formatter on SymLinknode */
    public static final String CONST_NODETYPE_IDENTIFIER_SYMLINK = "SYMLINK";

    // Status-Konstanten
    private static final Map<String, String> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, String>();

    static {
        // Defaults
        CONST_MAP_NODETYPE_IDENTIFIER.put("SYMLINK", CONST_NODETYPE_IDENTIFIER_SYMLINK);
        // Abarten
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_SYMLINK, CONST_NODETYPE_IDENTIFIER_SYMLINK);
    }
    
    private static SymLinkNodeService instance = new SymLinkNodeService();
    
    /** 
     * return the main instance of this service
     * @FeatureDomain                Persistence
     * @FeatureResult                return the main instance of this service
     * @FeatureKeywords              Persistence
     * @return                       the main instance of this service
     */
    public static SymLinkNodeService getInstance() {
        return instance;
    }

    @Override
    public String getDataBlocks4CheckSum(final DataDomain baseNode) throws Exception {
        SymLinkNode node = (SymLinkNode) baseNode;

        // Content erzeugen
        StringBuffer data = new StringBuffer();
        data.append(super.getDataBlocks4CheckSum(node))
            .append(" symLinkRef=").append(node.getSymLinkRef())
            .append(" symLinkName=").append(node.getSymLinkName())
            .append(" symLinkTags=").append(node.getSymLinkTags());
        return data.toString();
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
