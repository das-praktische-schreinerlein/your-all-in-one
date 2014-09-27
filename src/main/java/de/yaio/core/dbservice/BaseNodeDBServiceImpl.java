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
package de.yaio.core.dbservice;

import java.util.ArrayList;
import java.util.List;

import de.yaio.core.node.BaseNode;
import de.yaio.core.nodeservice.BaseNodeService;


/**
 * <h4>FeatureDomain:</h4>
 *     Persistence
 * <h4>FeatureDescription:</h4>
 *     implementation of dbservices for BaseNodes
 * 
 * @package de.yaio.core.node
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class BaseNodeDBServiceImpl implements BaseNodeDBService {
    
    
    protected static BaseNodeDBService instance = new BaseNodeDBServiceImpl();
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     return the instance of BaseNodeDBService
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>ReturnValue BaseNodeDBService - the instance of BaseNodeDBService
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @return - the instance of BaseNodeDBService
     */
    public static BaseNodeDBService getInstance() {
        return instance;
    }
    
    @Override
    public List<BaseNode> updateMeAndMyParents(BaseNode node) throws Exception {
        List<BaseNode> parentHierarchy = null;
        if (node != null) {
            // init Cildren from DB
            node.initChildNodesFromDB(0);
            
            // recalc me
            node.recalcData(BaseNodeService.CONST_RECURSE_DIRECTION_ONLYME);
            
            // save me
            node.merge();
            
            // update my parents
            if (node.getParentNode() != null) {
                parentHierarchy = updateMeAndMyParents(node.getParentNode());
            } else {
                // i am root -> create hierarchylist
                parentHierarchy = new ArrayList<BaseNode>();
            }
            
            // add me to hierarchylist
            parentHierarchy.add(node);
        }
        return parentHierarchy;
    }
}
