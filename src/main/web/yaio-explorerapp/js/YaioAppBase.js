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
        me.configureService('Ymf.MarkdownEditorServiceHelper', function () {
            return Ymf.MarkdownEditorServiceHelper(me);
        });
        me.configureService('Ymf.MarkdownConverter', function () {
            return Ymf.MarkdownConverter(me);
        });
        me.configureService('JsHelferlein.MarkdownRenderer', function () {
            return Yaio.MarkdownRenderer(me);
        });
        me.configureService('Ymf.MarkdownEditor', function () {
            var config = new JsHelferlein.ConfigBase();
            config.usePrintWidget = false;
            return Ymf.MarkdownEditor(me, config);
        });

        // instances
        me.configureService("Yaio.PromiseHelper", function() { return Yaio.PromiseHelper(me); });
        me.configureService("Yaio.Base", function() { return Yaio.Base(me); });
        me.configureService("Yaio.FileLoader", function() { return Yaio.FileLoader(me); });
        me.configureService("Yaio.Layout", function() { return Yaio.Layout(me); });
        me.configureService("Yaio.Editor", function() { return Yaio.Editor(me); });
        me.configureService("Yaio.MarkdownConverter", function() { return Yaio.MarkdownConverter(me); });
        me.configureService("Yaio.MarkdownRenderer", function() { return Yaio.MarkdownRenderer(me); });
        me.configureService("Yaio.ExplorerConverter", function() { return Yaio.ExplorerConverter(me); });
        me.configureService("Yaio.Formatter", function() { return Yaio.Formatter(me); });
        me.configureService("Yaio.MarkdownEditor", function() { return Yaio.MarkdownEditor(me); });
        me.configureService("Yaio.ServerNodeData_Local", function() { return Yaio.ServerNodeData(me, Yaio.ServerNodeDataConfig()); });
        me.configureService("Yaio.StaticNodeDataStore", function() { return Yaio.StaticNodeDataStore(me); });
        me.configureService("Yaio.StaticNodeData", function() { return Yaio.StaticNodeData(me, Yaio.StaticNodeDataConfig()); });
        me.configureService("Yaio.FileNodeData", function() { return Yaio.FileNodeData(me, Yaio.FileNodeDataConfig()); });
        me.configureService("Yaio.NodeDataRender", function() { return Yaio.NodeDataRender(me); });
        me.configureService("Yaio.NodeGanttRender", function() { return Yaio.NodeGanttRender(me); });
        me.configureService("Yaio.ExplorerAction", function() { return Yaio.ExplorerAction(me); });
        me.configureService("Yaio.ExplorerTree", function() { return Yaio.ExplorerTree(me); });
        me.configureService("Yaio.ExportedData", function() { return Yaio.ExportedData(me); });

        // aliases
        me.configureService('YmfMarkdownEditorServiceHelper', function () {
            return me.get('Ymf.MarkdownEditorServiceHelper');
        });
        me.configureService('YmfMarkdownConverter', function () {
            return me.get('Ymf.MarkdownConverter');
        });
        me.configureService('YmfMarkdownRenderer', function () {
            return me.get('JsHelferlein.MarkdownRenderer');
        });
        me.configureService('YmfRenderer', function () {
            return me.get('JsHelferlein.Renderer');
        });
        me.configureService('YmfMarkdownEditor', function () {
            return me.get('Ymf.MarkdownEditor');
        });
        me.configureService("YaioPromiseHelper", function() { return me.get("Yaio.PromiseHelper"); });
        me.configureService("YaioBase", function() { return me.get("Yaio.Base"); });
        me.configureService("YaioFileLoader", function() { return me.get("Yaio.FileLoader"); });
        me.configureService("YaioLayout", function() { return me.get("Yaio.Layout"); });
        me.configureService("YaioEditor", function() { return me.get("Yaio.Editor"); });
        me.configureService("YaioMarkdownConverter", function() { return me.get("YmfMarkdownConverter"); });
        me.configureService("YaioMarkdownRenderer", function() { return me.get("YmfMarkdownRenderer"); });
        me.configureService("YaioExplorerConverter", function() { return me.get("Yaio.ExplorerConverter"); });
        me.configureService("YaioFormatter", function() { return me.get("YmfRenderer"); });
        me.configureService("YaioMarkdownEditor", function() { return me.get("YmfMarkdownEditor"); });

        // use NodeData with aliases
        me.configureService("YaioStaticNodeDataStore", function() { return me.get("Yaio.StaticNodeDataStore"); });
        me.configureService("YaioStaticNodeData", function() { return me.get("Yaio.StaticNodeData"); });
        me.configureService("YaioFileNodeData", function() { return me.get("Yaio.FileNodeData"); });
        me.configureService("YaioServerNodeData_Local", function() { return me.get("Yaio.ServerNodeData_Local"); });
        me.configureService("YaioNodeData", function() { return me.get("YaioStaticNodeData"); });
        me.configureService("YaioAccessManager", function() { return me.get("YaioNodeData").getAccessManager(); });
        
        me.configureService("YaioNodeDataRender", function() { return me.get("Yaio.NodeDataRender"); });
        me.configureService("YaioNodeGanttRender", function() { return me.get("Yaio.NodeGanttRender"); });
        me.configureService("YaioExplorerAction", function() { return me.get("Yaio.ExplorerAction"); });
        me.configureService("YaioExplorerTree", function() { return me.get("Yaio.ExplorerTree"); });
        me.configureService("YaioExportedData", function() { return me.get("Yaio.ExportedData"); });
    };

    // init all
    me._init();

    return me;
};