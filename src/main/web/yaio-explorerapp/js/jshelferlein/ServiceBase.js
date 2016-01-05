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

JsHelferlein.ServiceBase = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = {};
    
    me.logNotImplemented = function() {
        var svcLogger = me.appBase.get("Logger");
        if (svcLogger) {
            svcLogger.logError("not implemented");
        }
        throw "function is not implemented";
    };
    
    me._init = function() {
        // check Config
        me.appBase = appBase;
        me.config = appBase.checkConfig(config, defaultConfig);
        me.jQuery = appBase.jQuery;
        me.$ = me.jQuery;
    };

    me._init();
    
    return me;
};