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
        me.setAvailiableNodeAction('search', true);
        me.setAvailiableNodeAction('dashboard', "#/dashboard");
        
        me.setAvailiableNodeAction('frontpagebaseurl', me.appBase.config.resBaseUrl + 'static/');
        me.setAvailiableNodeAction('syshelp', me.appBase.config.exportStaticDocumentationUrl + 'SysHelp1');
        me.setAvailiableNodeAction('sysinfo', me.appBase.config.exportStaticDocumentationUrl + 'SysInfo1');
        
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
