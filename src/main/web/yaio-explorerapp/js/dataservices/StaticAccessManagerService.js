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
Yaio.StaticAccessManagerService = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = Yaio.AccessManagerService(appBase, config, defaultConfig);

    /**
     * initialize the object
     */
    me._init = function() {
        // urls
        // we have problems with sysUID and nodeRef me.setAvailiableNodeAction('createsymlink', true);
        me.setAvailiableNodeAction('edit', true);
        me.setAvailiableNodeAction('create', true);
        me.setAvailiableNodeAction('move', true);
        me.setAvailiableNodeAction('remove', true);
        
        me.setAvailiableNodeAction('frontpagebaseurl', me.appBase.config.resBaseUrl + 'static/');
        me.setAvailiableNodeAction('syshelp', me.appBase.config.resBaseUrl + 'static/SysHelp1.html');
        me.setAvailiableNodeAction('sysinfo', me.appBase.config.resBaseUrl + 'static/SysInfo1.html');
        
        me.setAvailiableStaticExportLink('ExportStaticJsonDirect', me.appBase.config.appRootUrl + 'staticexporter/json/');
        
        me.setAvailiableNodeAction('showsysdata', true);
        me.setAvailiableNodeAction('print', true);
    };
    
    me.getAvailiableNodeAction = function(key, nodeId, flgMaster) {
        if (key === 'delete' && flgMaster) {
            return false;
        }
        if (key === 'frontpagebaseurl') {
            return me.availiableNodeActions[key] + nodeId + ".html";
        }
        return me.availiableNodeActions[key];
    };

    me._init();
    
    return me;
};
