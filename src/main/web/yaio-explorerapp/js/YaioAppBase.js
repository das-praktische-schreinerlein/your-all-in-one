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

window.Yaio = {};
window.YaioAppBase = function() {
    'use strict';

    // my own instance
    var me = JsHelferlein.AppBase(YaioAppBaseConfig());

    me._init = function () {
        me._configureDefaultServices();
    };
    
    me._configureDefaultServices = function() {
        // instances
        me.configureService("Yaio.BaseService", function() { return Yaio.BaseService(me); });
        me.configureService("Yaio.LayoutService", function() { return Yaio.LayoutService(me); });
        me.configureService("Yaio.EditorService", function() { return Yaio.EditorService(me); });
        me.configureService("Yaio.FormatterService", function() { return Yaio.FormatterService(me); });
        me.configureService("Yaio.MarkdownEditorService", function() { return Yaio.MarkdownEditorService(me); });
        me.configureService("Yaio.ServerNodeDataService_Local", function() { return Yaio.ServerNodeDataService(me); });
        me.configureService("Yaio.NodeDataRenderService", function() { return Yaio.NodeDataRenderService(me); });
        me.configureService("Yaio.NodeGanttRenderService", function() { return Yaio.NodeGanttRenderService(me); });
        me.configureService("Yaio.ExplorerActionService", function() { return Yaio.ExplorerActionService(me); });
        me.configureService("Yaio.ExplorerTreeService", function() { return Yaio.ExplorerTreeService(me); });
        me.configureService("Yaio.ExportedDataService", function() { return Yaio.ExportedDataService(me); });

        // aliases
        me.configureService("YaioBase", function() { return me.get("Yaio.BaseService"); });
        me.configureService("YaioLayout", function() { return me.get("Yaio.LayoutService"); });
        me.configureService("YaioEditor", function() { return me.get("Yaio.EditorService"); });
        me.configureService("YaioFormatter", function() { return me.get("Yaio.FormatterService"); });
        me.configureService("YaioMarkdownEditor", function() { return me.get("Yaio.MarkdownEditorService"); });

        // use NodeDataService with aliases 
        me.configureService("YaioServerNodeData_Local", function() { return me.get("Yaio.ServerNodeDataService_Local"); });
        me.configureService("YaioNodeData", function() { return me.get("YaioServerNodeData_Local"); });
        
        me.configureService("YaioNodeDataRender", function() { return me.get("Yaio.NodeDataRenderService"); });
        me.configureService("YaioNodeGanttRender", function() { return me.get("Yaio.NodeGanttRenderService"); });
        me.configureService("YaioExplorerAction", function() { return me.get("Yaio.ExplorerActionService"); });
        me.configureService("YaioExplorerTree", function() { return me.get("Yaio.ExplorerTreeService"); });
        me.configureService("YaioExportedData", function() { return me.get("Yaio.ExportedDataService"); });
    };

    // init all
    me._init();

    return me;
};