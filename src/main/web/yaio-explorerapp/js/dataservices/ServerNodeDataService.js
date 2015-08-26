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
Yaio.ServerNodeDataService = function(appBase) {
    'use strict';

    // my own instance
    var me = Yaio.NodeDataService(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };
    
    
    me.loadNodeData = function(nodeId) {
        console.log("load data for node:" + nodeId);
        return { 
            url: me.appBase.config.restShowUrl + nodeId, 
            cache: false 
        };
    };
    
    /*****************************************
     *****************************************
     * Service-Funktions (webservice)
     *****************************************
     *****************************************/
    me._yaioCallUpdateNode = function(fancynode, json) {
        var url = me.appBase.config.restUpdateUrl + fancynode.key;
        return me._yaioCallPatchNode(fancynode, url, json);
    };
    
    me._yaioCallMoveNode = function(fancynode, newParentKey, newPos, json) {
        var url = me.appBase.config.restMoveUrl+ fancynode.key + "/" + newParentKey + "/" + newPos;
        return me._yaioCallPatchNode(fancynode, url, json);
    };

    me._yaioCallPatchNode = function(fancynode, url, json) {
        var msg = "_yaioCallPatchNode for fancynode:" + fancynode.key;
        console.log(msg + " CALL: " + "url: "+ url + " with:" + json);
        return me.$.ajax({
            headers : {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json'
            },
            url : url,
            type : 'PATCH',
            data : json,
            error : function(jqXHR, textStatus, errorThrown) {
                // log the error to the console
                me.appBase.get('YaioBase').logError("The following error occured: " + textStatus + " " + errorThrown, true);
                me.appBase.get('YaioBase').logError("cant save fancynode:" + fancynode.key + " error:" + textStatus);
            },
            complete : function() {
                console.log("update fancynode:" + fancynode.key + "' ran");
            }
        });
    };
    
    me._yaioCallLoadSymLinkData = function(basenode, fancynode) {
        var msg = "_yaioCallLoadSymLinkData for node:" + basenode.sysUID + " symlink:" + basenode.symLinkRef + " fancynode:" + fancynode.key;
        console.log(msg + " START");
        var url = me.appBase.config.restSymLinkUrl + basenode.symLinkRef;
        return me.$.ajax({
            headers : {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json'
            },
            url : url,
            type : 'GET',
            error : function(jqXHR, textStatus, errorThrown) {
                // log the error to the console
                me.appBase.get('YaioBase').logError("ERROR  " + msg + " The following error occured: " + textStatus + " " + errorThrown, false);
                me.appBase.get('YaioBase').logError("cant load " + msg + " error:" + textStatus, true);
            },
            complete : function() {
                console.log("completed load " + msg);
            }
        });
    }
    
    me._yaioCallRemoveNode = function(nodeId) {
        var msg = "_yaioCallRemoveNode node:" + nodeId;
        var url = me.appBase.config.restRemoveUrl + nodeId;
        console.log(msg + " START: with:" + url);
        return me.$.ajax({
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json'
            },
            url: url,
            type: 'DELETE',
            error: function(jqXHR, textStatus, errorThrown) {
                // log the error to the console
                me.appBase.get('YaioBase').logError("The following error occured: " + textStatus + " " + errorThrown, false);
                me.appBase.get('YaioBase').logError("cant remove node:" + nodeId + " error:" + textStatus, true);
            },
            complete: function() {
                console.log("remove node:" + nodeId + "' ran");
            }
        });
    };

    me._yaioCallSaveNode = function(nodeObj, options) {
        var formName, mode, className, sysUID; 
        var msg = "_yaioCallSaveNode node: " + options.mode + ' ' + nodeObj['sysUID'];
        console.log(msg + " START");
        // branch depending on mode
        var method, url, json, ajaxCall;
        if (options.mode === "edit") {
            // mode update 
            method = "PATCH";
            url = me.appBase.config.restUpdateUrl + options.className + "/" + options.sysUID;
            ajaxCall = function () {
                // hack because shortcut .patch not exists yet in angular-version
                var http = me.appBase.get('Angular.$http');
                return http({method: method, url: url, data: nodeObj});
            }
        } else if (options.mode === "create") {
            // mode create 
            method = "POST";
            url = me.appBase.config.restCreateUrl + options.className + "/" + options.sysUID;
            
            // unset sysUID
            nodeObj["sysUID"] = null;

            ajaxCall = function () {
                return me.appBase.get('Angular.$http').post(url, nodeObj);
            }
        } else {
            // unknown mode
            me.appBase.get('YaioBase').logError("unknown mode=" + options.mode + " form formName=" + options.formName, false);
            return null;
        }

        // define json for common fields
        var json = JSON.stringify(nodeObj);
        
        // do http
        console.log(msg + " CALL url:" + url + " data:" + json);
        return ajaxCall();
    };

    me._yaioCallLoadNodeById = function(nodeId, options) {
        var msg = "_yaioCallLoadNodeById node: " + nodeId + " options:" + options;
        console.log(msg + " START");
        var restBaseUrl = me.appBase.config.restShowUrl;
        if (options.flgNodeByAllId) {
            restBaseUrl = me.appBase.config.restSymLinkUrl;
        }
        var url = restBaseUrl + nodeId;
        var ajaxCall = function () {
            return me.appBase.get('Angular.$http').get(url);
        }
        
        // do http
        console.log(msg + " CALL url:" + url);
        return ajaxCall();
    };
    
    me._yaioCallFulltextSearch = function(searchOptions) {
        var msg = "_yaioCallFulltextSearch searchOptions: " + searchOptions;
        var uri = encodeURI(searchOptions.curPage)
            + '/' + encodeURI(searchOptions.pageSize)
            + '/' + encodeURI(searchOptions.searchSort)
            + '/' + encodeURI(searchOptions.baseSysUID)
            + '/';

        // no empty fulltext for webservice -> we use there another route 
        if (searchOptions.fulltext && searchOptions.fulltext.length > 0) {
            uri = uri + encodeURI(searchOptions.fulltext) + '/';
        }

        // load data
        var url = me.appBase.config.restSearchUrl + uri;
        var ajaxCall = function () {
            return me.appBase.get('Angular.$http').get(url);
        }
        
        // do http
        console.log(msg + " CALL url:" + url);
        return ajaxCall();
    }
    

    me._init();
    
    return me;
};
