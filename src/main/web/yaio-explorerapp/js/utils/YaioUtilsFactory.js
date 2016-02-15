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
 * factory to create yaioUtils with util-functions
 * @FeatureDomain                Utils
 * @FeatureResult                returns new yaioUtils obj
 * @FeatureKeywords              Utils
 */
yaioApp.factory('yaioUtils', ['$location', '$http', '$rootScope', '$q', function ($location, $http, $rootScope, $q) {
    'use strict';

    var appBase = yaioAppBase;
    var ganttRangeStart = appBase.get('DataUtils').formatGermanDate((new Date()).getTime() - 90*24*60*60*1000); 
    var ganttRangeEnd = appBase.get('DataUtils').formatGermanDate((new Date()).getTime() + 90*24*60*60*1000);

    // set angular to appbase
    appBase.configureService('Angular.$location', function() { return $location; });
    appBase.configureService('Angular.$http', function() { return $http; });
    appBase.configureService('Angular.$rootScope', function() { return $rootScope; });
    appBase.configureService('Angular.$q', function() { return $q; });

    return {
        /** 
         * callbackhandler to open helpsite
         * @FeatureDomain                Help
         * @FeatureResult                open the helpsite
         * @FeatureKeywords              GUI Callback
         * @param url                    the url of the helpsite
         */
        showHelpSite: function(url) {
            console.log('showHelpSite:' + ' url:' + url);
            appBase.get('YaioLayout').yaioShowHelpSite(url);
            return false;
        },
        
        toggleSysContainerForNode: function(node) {
            appBase.get('YaioExplorerAction').toggleNodeSysContainer(node.sysUID);
        },
        
        openNodeEditorForNode: function(node, mode, newNode) {
            appBase.get('YaioEditor').yaioOpenNodeEditorForNode(node, mode, newNode);
        },
        
        downloadAsFile: function(domId, content, filename, mime, encoding) {
            return appBase.getService('FileUtils').downloadAsFile(appBase.$(domId), content, filename, mime, encoding);
        },
        
        renderNodeLine: function(node, trIdSelector, flgMinimum) {
            // load me
            var data = {
                 node: {
                     data: {
                         basenode: node
                     },
                     tr: trIdSelector
                 } 
            };
            
            console.log('renderNodeLine nodeId=' + node.sysUID + ' tr=' + $(trIdSelector).length);
            appBase.get('YaioNodeDataRender').renderColumnsForNode(null, data, true, flgMinimum);
        },
        
        ganttOptions: { 
            ganttRangeStart: ganttRangeStart, 
            ganttRangeEnd: ganttRangeEnd
        },
        
        getAppBase: function() {
            return appBase;
        },

        getService: function(service) {
            return appBase.get(service);
        },

        getConfig: function() {
            return appBase.config;
        }
        
    };
}]);
