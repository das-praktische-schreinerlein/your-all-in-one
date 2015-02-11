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

import de.yaio.core.node.TaskNode;

/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for entity: TaskNode
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class TaskNodeService extends BaseNodeService {

    @Override
    public boolean isWFStatus(final String state) {
        if (TaskNode.CONST_NODETYPE_IDENTIFIER_OPEN.equalsIgnoreCase(state)) {
            return true;
        } else if (TaskNode.CONST_NODETYPE_IDENTIFIER_RUNNNING.equalsIgnoreCase(state)) {
            return true;
        } else if (TaskNode.CONST_NODETYPE_IDENTIFIER_SHORT.equalsIgnoreCase(state)) {
            return true;
        } else if (TaskNode.CONST_NODETYPE_IDENTIFIER_LATE.equalsIgnoreCase(state)) {
            return true;
        } else if (TaskNode.CONST_NODETYPE_IDENTIFIER_DONE.equalsIgnoreCase(state)) {
            return true;
        }

        
        return false;
    }

    @Override
    public boolean isWFStatusDone(final String state) {
        if (TaskNode.CONST_NODETYPE_IDENTIFIER_DONE.equalsIgnoreCase(state)) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isWFStatusCanceled(final String state) {
        if (TaskNode.CONST_NODETYPE_IDENTIFIER_CANCELED.equalsIgnoreCase(state)) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isWFStatusOpen(final String state) {
        if (TaskNode.CONST_NODETYPE_IDENTIFIER_OPEN.equalsIgnoreCase(state)) {
            return true;
        } else if (TaskNode.CONST_NODETYPE_IDENTIFIER_RUNNNING.equalsIgnoreCase(state)) {
            return true;
        } else if (TaskNode.CONST_NODETYPE_IDENTIFIER_SHORT.equalsIgnoreCase(state)) {
            return true;
        } else if (TaskNode.CONST_NODETYPE_IDENTIFIER_LATE.equalsIgnoreCase(state)) {
            return true;
        }

        return false;
    }
}
