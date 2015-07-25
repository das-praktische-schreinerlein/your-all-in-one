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
'use strict';

window.Yaio = {};
window.YaioAppBase = function() {
    // my own instance
    var me = JsHelferlein.AppBase(YaioAppBaseConfig());

    me._init = function () {
        me._configureDefaultServices();
        me._configureDefaultDetectors();
    }
    
    me._configureDefaultServices = function() {
        me.configureService("YaioBaseService", function() { return Yaio.BaseService(me); });
        me.configureService("YaioLayoutService", function() { return Yaio.LayoutService(me); });
        me.configureService("YaioEditorService", function() { return Yaio.EditorService(me); });
        me.configureService("YaioFormatterService", function() { return Yaio.FormatterService(me); });
        me.configureService("YaioMarkdownEditorService", function() { return Yaio.MarkdownEditorService(me); });
    };

    // init all
    me._init();

    return me;
};