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

/** 
 * servicefunctions for data-services
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.AccessManagerService = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase, config, defaultConfig);

    /**
     * initialize the object
     */
    me._init = function() {
        me.masterNodeId = undefined;
        me.availiableExportForms = {};
        me.availiableExportLinks = {};
        me.availiableStaticExportLinks = {};
        me.availiableImportForms = {};
        me.availiableNodeActions = {};
        me.nodeActions = {};
    };
    
    
    me.getAvailiableExportFormKeys = function(nodeId, flgMaster) {
        return Object.keys(me.availiableExportForms);
    };
    
    me.getAvailiableExportForm = function(key, nodeId, flgMaster) {
        return me.availiableExportForms[key];
    };
    
    me.setAvailiableExportForm = function(key, url) {
        return me.availiableExportForms[key] = url;
    };
    
    me.getAvailiableExportLinkKeys = function(nodeId, flgMaster) {
        return Object.keys(me.availiableExportLinks);
    };
    
    me.getAvailiableExportLink = function(key, nodeId, flgMaster) {
        return me.availiableExportLinks[key];
    };
    
    me.setAvailiableExportLink = function(key, url) {
        return me.availiableExportLinks[key] = url;
    };

    me.getAvailiableStaticExportLinkKeys = function(nodeId, flgMaster) {
        return Object.keys(me.availiableStaticExportLinks);
    };
    
    me.getAvailiableStaticExportLink = function(key, nodeId, flgMaster) {
        return me.availiableStaticExportLinks[key];
    };
    
    me.setAvailiableStaticExportLink = function(key, url) {
        return me.availiableStaticExportLinks[key] = url;
    };

    me.getAvailiableImportFormKeys = function(nodeId, flgMaster) {
        return Object.keys(me.availiableImportForms);
    };
    
    me.getAvailiableImportForm = function(key, nodeId, flgMaster) {
        return me.availiableImportForms[key];
    };
    
    me.setAvailiableImportForm = function(key, url) {
        return me.availiableImportForms[key] = url;
    };

    me.getAvailiableNodeActionKeys = function(nodeId, flgMaster) {
        return Object.keys(me.availiableNodeActions);
    };
    
    me.getAvailiableNodeAction = function(key, nodeId, flgMaster) {
        if (key === 'delete' && flgMaster) {
            return false;
        }
        if (key === 'frontpagebaseurl') {
            return me.availiableNodeActions[key] + nodeId;
        }
        return me.availiableNodeActions[key];
    };
    
    me.setAvailiableNodeAction = function(key, url) {
        return me.availiableNodeActions[key] = url;
    };

    me._init();
    
    return me;
};
