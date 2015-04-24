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

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.SymLinkNode;


/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for entity: SymLinkNode
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class SymLinkNodeService extends BaseNodeService {

    /** nodetype-identifier for parser/formatter on SymLinknode */
    public static final String CONST_NODETYPE_IDENTIFIER_SYMLINK = "SYMLINK";

    // Status-Konstanten
    public static final Map<String, Object> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, Object>();

    static {
        // Defaults
        CONST_MAP_NODETYPE_IDENTIFIER.put("SYMLINK", CONST_NODETYPE_IDENTIFIER_SYMLINK);
        // Abarten
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_SYMLINK, CONST_NODETYPE_IDENTIFIER_SYMLINK);
    }
    
    private static SymLinkNodeService instance = new SymLinkNodeService();
    
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
}
