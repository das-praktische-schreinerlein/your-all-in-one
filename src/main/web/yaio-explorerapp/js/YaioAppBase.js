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
        me.configureService('Ymf.LayoutService', function () {
            return Ymf.LayoutService(me);
        });
        me.configureService('Ymf.MarkdownConverterService', function () {
            return Ymf.MarkdownConverterService(me);
        });
        me.configureService('JsHelferlein.MarkdownRendererService', function () {
            return Yaio.MarkdownRendererService(me);
        });
        me.configureService('Ymf.EditorDialogsService', function () {
            return Ymf.EditorDialogsService(me);
        });
        me.configureService('Ymf.MarkdownEditorService', function () {
            return Ymf.MarkdownEditorService(me);
        });

        // instances
        me.configureService("Yaio.PromiseHelperService", function() { return Yaio.PromiseHelperService(me); });
        me.configureService("Yaio.BaseService", function() { return Yaio.BaseService(me); });
        me.configureService("Yaio.FileLoaderService", function() { return Yaio.FileLoaderService(me); });
        me.configureService("Yaio.LayoutService", function() { return Yaio.LayoutService(me); });
        me.configureService("Yaio.EditorService", function() { return Yaio.EditorService(me); });
        me.configureService("Yaio.MarkdownConverterService", function() { return Yaio.MarkdownConverterService(me); });
        me.configureService("Yaio.MarkdownRendererService", function() { return Yaio.MarkdownRendererService(me); });
        me.configureService("Yaio.ExplorerConverterService", function() { return Yaio.ExplorerConverterService(me); });
        me.configureService("Yaio.FormatterService", function() { return Yaio.FormatterService(me); });
        me.configureService("Yaio.MarkdownEditorService", function() { return Yaio.MarkdownEditorService(me); });
        me.configureService("Yaio.ServerNodeDataService_Local", function() { return Yaio.ServerNodeDataService(me, Yaio.ServerNodeDataServiceConfig()); });
        me.configureService("Yaio.StaticNodeDataStoreService", function() { return Yaio.StaticNodeDataStoreService(me); });
        me.configureService("Yaio.StaticNodeDataService", function() { return Yaio.StaticNodeDataService(me, Yaio.StaticNodeDataServiceConfig()); });
        me.configureService("Yaio.FileNodeDataService", function() { return Yaio.FileNodeDataService(me, Yaio.FileNodeDataServiceConfig()); });
        me.configureService("Yaio.NodeDataRenderService", function() { return Yaio.NodeDataRenderService(me); });
        me.configureService("Yaio.NodeGanttRenderService", function() { return Yaio.NodeGanttRenderService(me); });
        me.configureService("Yaio.ExplorerActionService", function() { return Yaio.ExplorerActionService(me); });
        me.configureService("Yaio.ExplorerTreeService", function() { return Yaio.ExplorerTreeService(me); });
        me.configureService("Yaio.ExportedDataService", function() { return Yaio.ExportedDataService(me); });

        // aliases
        me.configureService('YmfLayout', function () {
            return me.get('Ymf.LayoutService');
        });
        me.configureService('YmfMarkdownConverter', function () {
            return me.get('Ymf.MarkdownConverterService');
        });
        me.configureService('YmfMarkdownRenderer', function () {
            return me.get('JsHelferlein.MarkdownRendererService');
        });
        me.configureService('YmfFormatter', function () {
            return me.get('JsHelferlein.FormatterService');
        });
        me.configureService('YmfEditorDialogs', function () {
            return me.get('Ymf.EditorDialogsService');
        });
        me.configureService('YmfMarkdownEditor', function () {
            return me.get('Ymf.MarkdownEditorService');
        });
        me.configureService("YaioPromiseHelper", function() { return me.get("Yaio.PromiseHelperService"); });
        me.configureService("YaioBase", function() { return me.get("Yaio.BaseService"); });
        me.configureService("YaioFileLoader", function() { return me.get("Yaio.FileLoaderService"); });
        me.configureService("YaioLayout", function() { return me.get("Yaio.LayoutService"); });
        me.configureService("YaioEditor", function() { return me.get("Yaio.EditorService"); });
        me.configureService("YaioMarkdownConverter", function() { return me.get("YmfMarkdownConverter"); });
        me.configureService("YaioMarkdownRenderer", function() { return me.get("YmfMarkdownRenderer"); });
        me.configureService("YaioExplorerConverter", function() { return me.get("Yaio.ExplorerConverterService"); });
        me.configureService("YaioFormatter", function() { return me.get("YmfFormatter"); });
        me.configureService("YaioMarkdownEditor", function() { return me.get("YmfMarkdownEditor"); });

        // use NodeDataService with aliases 
        me.configureService("YaioStaticNodeDataStore", function() { return me.get("Yaio.StaticNodeDataStoreService"); });
        me.configureService("YaioStaticNodeData", function() { return me.get("Yaio.StaticNodeDataService"); });
        me.configureService("YaioFileNodeData", function() { return me.get("Yaio.FileNodeDataService"); });
        me.configureService("YaioServerNodeData_Local", function() { return me.get("Yaio.ServerNodeDataService_Local"); });
        me.configureService("YaioNodeData", function() { return me.get("YaioStaticNodeData"); });
        me.configureService("YaioAccessManager", function() { return me.get("YaioNodeData").getAccessManager(); });
        
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