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
package de.yaio.core.datadomain;

import java.util.Map;
import java.util.Set;

import de.yaio.core.node.BaseNode;
import de.yaio.core.nodeservice.NodeService;

/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 * <h4>FeatureDescription:</h4>
 *     interface for DataDomains of the Node
 * 
 * @package de.yaio.core.datadomain
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface DataDomain {
    public static final int CONST_ORDER = 1;
    
    // because of Spring roo with Node
    public Set<BaseNode> getChildNodes();
    public BaseNode getParentNode();
    public void setParentNode(BaseNode parentNode);
    public void setParentNode(DataDomain parentNode);

    // hirarchy-functions
    public void setParentNodeOnly(DataDomain parentNode);
    public Map<String, DataDomain> getChildNodesByNameMap();
    public String getIdForChildByNameMap();
    public void addChildNode(DataDomain childNode);
    public boolean hasChildNode(DataDomain childNode);

    // basefields
    public Integer getEbene();
    public void setEbene(Integer ebene);
    public String getName();
    public void setName(String name);

    // servies
    public NodeService getNodeService();

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     recalc the WFData of the node
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>upadtes memberfields of dataDomain PlanChildrenSum
     *     <li>upadtes memberfields of dataDomain IstChildrenSum
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param recursionDirection - direction for recursivly recalc CONST_RECURSE_DIRECTION_* 
     * @throws Exception
     */
    public void recalcData(int recursionDirection) throws Exception;

    public String getNameForLogger();
}