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

/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     servicefunctions for data-services
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.FileNodeDataService = function(appBase) {
    'use strict';

    // my own instance
    var me = Yaio.StaticNodeDataService(appBase);
    
    me.nodeList = {};

    /**
     * initialize the object
     */
    me._init = function() {
    };
    
    /*****************************************
     *****************************************
     * Service-Funktions (webservice)
     *****************************************
     *****************************************/
    me._createAccessManager = function() {
        return me.appBase.get("Yaio.FileAccessManagerService");
    };
    
    me._getNodeDataById = function(nodeId) {
        return window.yaioFileJSON.node;
    }
    me._getParentIdHierarchyById = function(nodeId) {
        return window.yaioFileJSON.parentIdHierarchy;
    }
    
    me._init();
    
    return me;
};
