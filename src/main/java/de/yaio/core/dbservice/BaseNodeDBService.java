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
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import de.yaio.core.node.BaseNode;

/**
 * <h4>FeatureDomain:</h4>
 *     Persistence
 * <h4>FeatureDescription:</h4>
 *     dbservices for BaseNodes
 * 
 * @package de.yaio.core.node
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@RooService(domainTypes = { de.yaio.core.node.BaseNode.class })
public interface BaseNodeDBService {

    /**
     * <h4>FeatureDomain:</h4>
     *     Webservice
     * <h4>FeatureDescription:</h4>
     *     recalc and merge the node and its parents recursively
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>List - list of the recalced and saved parenthierarchy
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Webservice 
     * @param node - the node to recalc and merge
     * @return List - list of the recalced and saved parenthierarchy
     * @throws Exception - io/DB-Exceptions possible
     */
    public List<BaseNode> updateMeAndMyParents(BaseNode node) throws Exception;
}
