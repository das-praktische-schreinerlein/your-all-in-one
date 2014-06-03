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

import de.yaio.core.node.EventNode;

/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for entity: EventNode
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class EventNodeService extends TaskNodeService {

    @Override
    public boolean isWFStatus (String state) {
        if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_PLANED.equalsIgnoreCase(state)) {
            return true;
        } else if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_CONFIRMED.equalsIgnoreCase(state)) {
            return true;
        } else if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING.equalsIgnoreCase(state)) {
            return true;
        } else if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_SHORT.equalsIgnoreCase(state)) {
            return true;
        } else if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_LATE.equalsIgnoreCase(state)) {
            return true;
        } else if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_DONE.equalsIgnoreCase(state)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isWFStatusDone (String state) {
        if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_DONE.equalsIgnoreCase(state)) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isWFStatusCanceled(String state) {
        if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED.equalsIgnoreCase(state)) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isWFStatusOpen (String state) {
        if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_PLANED.equalsIgnoreCase(state)) {
            return true;
        } else if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_CONFIRMED.equalsIgnoreCase(state)) {
            return true;
        } else if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING.equalsIgnoreCase(state)) {
            return true;
        } else if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_SHORT.equalsIgnoreCase(state)) {
            return true;
        } else if (EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_LATE.equalsIgnoreCase(state)) {
            return true;
        }

        return false;
    }
}
