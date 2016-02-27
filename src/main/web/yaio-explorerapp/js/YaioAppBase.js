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

    /* jshint maxstatements: 100 */
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
        me.configureService('Ymf.MarkdownEditorController', function () {
            var config = new JsHelferlein.ConfigBase();
            config.usePrintWidget = false;
            return Ymf.MarkdownEditorController(me, config);
        });

        // instances
        me.configureService('Yaio.PromiseHelper', function() { return Yaio.PromiseHelper(me); });
        me.configureService('Yaio.Base', function() { return Yaio.Base(me); });
        me.configureService('Yaio.FileLoader', function() { return Yaio.FileLoader(me); });
        me.configureService('Yaio.Layout', function() { return Yaio.Layout(me); });
        me.configureService('Yaio.NodeEditor', function() { return Yaio.NodeEditor(me); });
        me.configureService('Yaio.MarkdownConverter', function() { return Yaio.MarkdownConverter(me); });
        me.configureService('Yaio.MarkdownRenderer', function() { return Yaio.MarkdownRenderer(me); });
        me.configureService('Yaio.ExplorerConverter', function() { return Yaio.ExplorerConverter(me); });
        me.configureService('Yaio.Formatter', function() { return Yaio.Formatter(me); });
        me.configureService('Yaio.MarkdownEditorController', function() { return Yaio.MarkdownEditorController(me); });
        me.configureService('Yaio.DataSourceManager', function() { return Yaio.DataSourceManager(me); });
        me.configureService('Yaio.ServerNodeDBDriver_Local', function() { return Yaio.ServerNodeDBDriver(me, Yaio.ServerNodeDBDriverConfig()); });
        me.configureService('Yaio.StaticNodeDataStore', function() { return Yaio.StaticNodeDataStore(me); });
        me.configureService('Yaio.StaticNodeDBDriver', function() { return Yaio.StaticNodeDBDriver(me, Yaio.StaticNodeDBDriverConfig()); });
        me.configureService('Yaio.FileNodeDBDriver', function() { return Yaio.FileNodeDBDriver(me, Yaio.FileNodeDBDriverConfig()); });
        me.configureService('Yaio.NodeRepository', function() { return Yaio.NodeRepository(me); });
        me.configureService('Yaio.NodeDataRenderer', function() { return Yaio.NodeDataRenderer(me); });
        me.configureService('Yaio.NodeGanttRenderer', function() { return Yaio.NodeGanttRenderer(me); });
        me.configureService('Yaio.ExplorerCommands', function() { return Yaio.ExplorerCommands(me); });
        me.configureService('Yaio.ExplorerTree', function() { return Yaio.ExplorerTree(me); });
        me.configureService('Yaio.ExportedData', function() { return Yaio.ExportedData(me); });

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
        me.configureService('YmfMarkdownEditorController', function () {
            return me.get('Ymf.MarkdownEditorController');
        });
        me.configureService('YaioPromiseHelper', function() { return me.get('Yaio.PromiseHelper'); });
        me.configureService('YaioBase', function() { return me.get('Yaio.Base'); });
        me.configureService('YaioFileLoader', function() { return me.get('Yaio.FileLoader'); });
        me.configureService('YaioLayout', function() { return me.get('Yaio.Layout'); });
        me.configureService('YaioNodeEditor', function() { return me.get('Yaio.NodeEditor'); });
        me.configureService('YaioMarkdownConverter', function() { return me.get('YmfMarkdownConverter'); });
        me.configureService('YaioMarkdownRenderer', function() { return me.get('YmfMarkdownRenderer'); });
        me.configureService('YaioExplorerConverter', function() { return me.get('Yaio.ExplorerConverter'); });
        me.configureService('YaioFormatter', function() { return me.get('YmfRenderer'); });
        me.configureService('YaioMarkdownEditorController', function() { return me.get('YmfMarkdownEditorController'); });

        me.configureService('YaioStaticNodeDataStore', function() { return me.get('Yaio.StaticNodeDataStore'); });
        me.configureService('YaioStaticNodeDBDriver', function() { return me.get('Yaio.StaticNodeDBDriver'); });
        me.configureService('YaioFileNodeDBDriver', function() { return me.get('Yaio.FileNodeDBDriver'); });
        me.configureService('YaioServerNodeDBDriver_Local', function() { return me.get('Yaio.ServerNodeDBDriver_Local'); });
        me.configureService('YaioNodeRepository', function() { return me.get('Yaio.NodeRepository'); });
        me.configureService('YaioAccessManager', function() { return me.get('YaioNodeRepository').getAccessManager(); });
        me.configureService('YaioDataSourceManager', function() {
            var dsm = me.get('Yaio.DataSourceManager');
            dsm.addConnection('YaioFileNodeDBDriver', function () { return me.get('YaioFileNodeDBDriver'); });
            dsm.addConnection('YaioStaticNodeDBDriver', function () { return me.get('YaioStaticNodeDBDriver'); });
            dsm.addConnection('YaioServerNodeDBDriver_Local', function () { return me.get('YaioServerNodeDBDriver_Local'); });
            return dsm;
        });

        me.configureService('YaioNodeDataRenderer', function() { return me.get('Yaio.NodeDataRenderer'); });
        me.configureService('YaioNodeGanttRenderer', function() { return me.get('Yaio.NodeGanttRenderer'); });
        me.configureService('YaioExplorerCommands', function() { return me.get('Yaio.ExplorerCommands'); });
        me.configureService('YaioExplorerTree', function() { return me.get('Yaio.ExplorerTree'); });
        me.configureService('YaioExportedData', function() { return me.get('Yaio.ExportedData'); });
    };
    /* jshint maxstatements: 50 */

    // init all
    me._init();

    return me;
};