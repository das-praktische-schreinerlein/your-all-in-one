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
         * open helpsite
         * @param {String} url                    the url of the helpsite
         */
        showHelpSite: function(url) {
            console.log('showHelpSite:' + ' url:' + url);
            appBase.get('YaioLayout').yaioShowHelpSite(url);
            return false;
        },

        /**
         * toggle the sys-container for the node
         * @param {Object} node    node-data
         */
        toggleSysContainerForNode: function(node) {
            appBase.get('YaioExplorerAction').toggleNodeSysContainer(node.sysUID);
        },

        /**
         * toggle editor for the node
         * @param {Object} node     node-data
         * @param {String} mode     edit, create, createsymlink
         * @param {Object} newNode  optional node to copy data from (for mode createsnapshot...)
         */
        openNodeEditorForNode: function(node, mode, newNode) {
            appBase.get('YaioEditor').yaioOpenNodeEditorForNode(node, mode, newNode);
        },

        /**
         * download the content as file (create response and open in new window)
         * @param {String} domId     id of the link to add the action
         * @param {string} content   data to download
         * @param {string} fileName  filename for save-dialog of the browser
         * @param {string} mime      mimetype of the file
         * @param {string} encoding  encoding to set
         */
        downloadAsFile: function(domId, content, fileName, mime, encoding) {
            return appBase.getService('FileUtils').downloadAsFile(appBase.$(domId), content, fileName, mime, encoding);
        },

        /**
         * Renders the full block for corresponding basenode.
         * @param {Object} node           node-data to render
         * @param {String} trIdSelector   tr-selector to append the rendered data
         * @param {Boolean} flgMinimum    render only the minimal subset of data
         */
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

        /**
         * return the current appBase
         * @returns {JsHelferlein.AppBase}   the current appBase
         */
        getAppBase: function() {
            return appBase;
        },

        /**
         * return the current instance of the service
         * @param {String} service               servicename
         * @returns {JsHelferlein.ServiceBase}   current instance of the service
         */
        getService: function(service) {
            return appBase.get(service);
        },

        /**
         * return the current appBase-config
         * @returns {JsHelferlein.ConfigBase}   current appBase-config
         */
        getConfig: function() {
            return appBase.config;
        }
        
    };
}]);
