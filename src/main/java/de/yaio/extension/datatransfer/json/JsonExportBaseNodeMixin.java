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
package de.yaio.extension.datatransfer.json;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.yaio.core.node.BaseNode;

/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 *     Persistence
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     bean with overrides for BaseNode-data for JsonExport
 * 
 * @package de.yaio.extension.datatransfer.json
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public abstract class JsonExportBaseNodeMixin {
    JsonExportBaseNodeMixin(@JsonProperty final Set<BaseNode> childNodes, 
                            @JsonProperty final BaseNode parentNode) { }
    JsonExportBaseNodeMixin() { }
    
    /**
     * export childNodes
     * @return childNodes
     */
    @JsonProperty
    public abstract Set<BaseNode> getChildNodes();

    /**
     * ignore parentNode
     * @return parentNode
     */
    @JsonIgnore
    public abstract BaseNode getParentNode();
}
