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

import javax.validation.ConstraintViolation;

import de.yaio.core.node.BaseNode;

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
    int CONST_ORDER = 1;
    
    // because of Spring roo with Node
    Set<BaseNode> getChildNodes();
    BaseNode getParentNode();
    void setParentNode(BaseNode parentNode);
    void setParentNode(DataDomain parentNode);

    // hirarchy-functions
    void setParentNodeOnly(DataDomain parentNode);
    Map<String, DataDomain> getChildNodesByNameMap();
    String getIdForChildByNameMap();
    void addChildNode(DataDomain childNode);
    boolean hasChildNode(DataDomain childNode);
    Integer getSortPos();
    void setSortPos(Integer sortPos);

    // basefields
    Integer getEbene();
    void setEbene(Integer ebene);
    String getName();
    void setName(String name);

    // servies
    de.yaio.core.nodeservice.BaseNodeService getBaseNodeService();
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Validation
     * <h4>FeatureDescription:</h4>
     *     validates the node against the declared validation rules
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>ReturnValue Set<ConstraintViolation<BaseNode>> - set of violations
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Validation
     * @return - set of violations
     */
     Set<ConstraintViolation<BaseNode>> validateMe();

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
     * @throws Exception - possible Exception
     */
    void recalcData(int recursionDirection) throws Exception;

    String getNameForLogger();

    /**
     * @return the {@link BaseNode#flgForceUpdate}
     */
    boolean isFlgForceUpdate();
    /**
     * @param flgForceUpdate the {@link BaseNode#flgForceUpdate} to set
     */
    void setFlgForceUpdate(boolean flgForceUpdate);
}
