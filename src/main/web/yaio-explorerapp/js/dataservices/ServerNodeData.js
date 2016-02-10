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
Yaio.ServerNodeData = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = Yaio.NodeData(appBase, config, defaultConfig);

    /**
     * initialize the object
     */
    me._init = function() {
    };
    
    me.loadNodeData = function(nodeId) {
        console.log("load data for node:" + nodeId);
        return { 
            url: me.config.restShowUrl + nodeId, 
            cache: false 
        };
    };
    
    me.connectService = function() {
        var res = me._loadConfig();
        return res;
    };
    
    me.updateServiceConfig = function(yaioCommonApiConfig) {
        if (yaioCommonApiConfig) {
            var msg = "updateServiceConfig for yaio";
            console.log(msg + " with:", yaioCommonApiConfig);
            
            // base
            me.config.name               = yaioCommonApiConfig.yaioInstanceName;
            // dont overwrite configured url, maybe its tunneled: me.config.urlBase            = yaioCommonApiConfig.yaioInstanceUrl;
            me.config.desc               = yaioCommonApiConfig.yaioInstanceDesc;
            me.config.updateConfig();
            
            // options
            me.config.masterSysUId       = yaioCommonApiConfig.yaioMastersysuid;
            me.config.excludeNodePraefix = yaioCommonApiConfig.yaioExportcontrollerExcludenodepraefix;
            me.config.excludeNodePraefix = (me.config.excludeNodePraefix != null ? me.config.excludeNodePraefix.replace(/%/g, "*") : "nothing");

            // services
            me.config.plantUmlBaseUrl    = yaioCommonApiConfig.plantUmlBaseUrl;

            me.config.dmsAvailable       = yaioCommonApiConfig.dmsAvailable;
            me.config.webshotAvailable   = yaioCommonApiConfig.webshotAvailable;
            me.config.metaextractAvailable = yaioCommonApiConfig.metaextractAvailable;

            console.log(msg + " to:", me.config);
        }
    };
    
    /*****************************************
     *****************************************
     * Service-Funktions (webservice)
     *****************************************
     *****************************************/
    me._createAccessManager = function() {
        return Yaio.ServerAccessManager(me.appBase, me.config);
    };
    
    me._loadConfig = function() {
        // return promise
        var dfd = new $.Deferred();
        var res = dfd.promise();

        // load config from server
        var resConfig = me._yaioCallLoadConfig();
        var loadDone = false;
        resConfig.done(function(yaioCommonApiConfig, textStatus, jqXhr ) {
            var msg = "_loadConfig for yaio";
            console.log(msg + " done");
            // update config
            me.updateServiceConfig(yaioCommonApiConfig);
            me.updateAppConfig();
            
            // resolve promise
            dfd.resolve("OK");
        });

        return res;
    };
    
    me._yaioCallLoadConfig = function() {
        var svcLogger = me.appBase.get('Logger');

        var url = me.config.configUrl;
        var msg = "_yaioCallLoadConfig for yaio:" + url;
        console.log(msg + " START");
        return me.$.ajax({
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json'
            },
            xhrFields : {
                // for CORS
                withCredentials : true
            },
            url : url,
            type : 'GET',
            error : function(jqXHR, textStatus, errorThrown) {
                // log the error to the console
                svcLogger.logError("ERROR  " + msg + " The following error occured: " + textStatus + " " + errorThrown, false);
                svcLogger.logError("cant load " + msg + " error:" + textStatus, true);
            },
            complete : function() {
                console.log("completed load " + msg);
            }
        });
    };
    
    me._yaioCallUpdateNode = function(fancynode, json) {
        var url = me.config.restUpdateUrl + fancynode.key;
        return me._yaioCallPatchNode(fancynode, url, json);
    };
    
    me._yaioCallMoveNode = function(fancynode, newParentKey, newPos, json) {
        var url = me.config.restMoveUrl+ fancynode.key + "/" + newParentKey + "/" + newPos;
        return me._yaioCallPatchNode(fancynode, url, json);
    };

    me._yaioCallCopyNode = function(fancynode, newParentKey, json) {
        var url = me.config.restCopyUrl+ fancynode.key + "/" + newParentKey;
        return me._yaioCallPatchNode(fancynode, url, json);
    };

    me._yaioCallPatchNode = function(fancynode, url, json) {
        var svcLogger = me.appBase.get('Logger');

        var msg = "_yaioCallPatchNode for fancynode:" + fancynode.key;
        console.log(msg + " CALL: " + "url: "+ url + " with:" + json);
        return me.$.ajax({
            headers : {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json'
            },
            xhrFields : {
                // for CORS
                withCredentials : true
            },
            url : url,
            type : 'PATCH',
            data : json,
            error : function(jqXHR, textStatus, errorThrown) {
                // log the error to the console
                svcLogger.logError("The following error occured: " + textStatus + " " + errorThrown, true);
                svcLogger.logError("cant save fancynode:" + fancynode.key + " error:" + textStatus);
            },
            complete : function() {
                console.log("update fancynode:" + fancynode.key + "' ran");
            }
        });
    };
    
    me._yaioCallLoadSymLinkData = function(basenode, fancynode) {
        var svcLogger = me.appBase.get('Logger');

        var msg = "_yaioCallLoadSymLinkData for node:" + basenode.sysUID + " symlink:" + basenode.symLinkRef + " fancynode:" + fancynode.key;
        console.log(msg + " START");
        var url = me.config.restSymLinkUrl + basenode.symLinkRef;
        return me.$.ajax({
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json'
            },
            xhrFields : {
                // for CORS
                withCredentials : true
            },
            url : url,
            type : 'GET',
            error : function(jqXHR, textStatus, errorThrown) {
                // log the error to the console
                svcLogger.logError("ERROR  " + msg + " The following error occured: " + textStatus + " " + errorThrown, false);
                svcLogger.logError("cant load " + msg + " error:" + textStatus, true);
            },
            complete : function() {
                console.log("completed load " + msg);
            }
        });
    };
    
    me._yaioCallRemoveNode = function(nodeId) {
        var svcLogger = me.appBase.get('Logger');

        var msg = "_yaioCallRemoveNode node:" + nodeId;
        var url = me.config.restRemoveUrl + nodeId;
        console.log(msg + " START: with:" + url);
        return me.$.ajax({
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json'
            },
            xhrFields : {
                // for CORS
                withCredentials : true
            },
            url: url,
            type: 'DELETE',
            error: function(jqXHR, textStatus, errorThrown) {
                // log the error to the console
                svcLogger.logError("The following error occured: " + textStatus + " " + errorThrown, false);
                svcLogger.logError("cant remove node:" + nodeId + " error:" + textStatus, true);
            },
            complete: function() {
                console.log("remove node:" + nodeId + "' ran");
            }
        });
    };

    me._yaioCallSaveNode = function(nodeObj, options) {
        var svcLogger = me.appBase.get('Logger');

        var msg = "_yaioCallSaveNode node: " + options.mode + ' ' + nodeObj.sysUID;
        console.log(msg + " START");
        // branch depending on mode
        var method, url, json, ajaxCall, formData;
        
        if (options.mode === "create") {
            // unset sysUID
            nodeObj.sysUID = null;
        }

        // special case UrlResNode because of multipart-uploads
        var flgMultiPart = false;
        if (options.className == "UrlResNode") {
            // UrlResNod set formdata for multipart-uploads
            flgMultiPart = true;
            
            // create formadata
            formData = new FormData();
            formData.append('node', new Blob([JSON.stringify(nodeObj)], {
                                                type: "application/json"
                                            })
            );
            
            // add uploadfile only if set
            formData.append("uploadFile", options.uploadFile ? options.uploadFile : "");
        } else {
            // default: json-request: add nodeObj
            formData = nodeObj;
        }
        
        if (options.mode === "edit") {
            // mode update 
            method = "PATCH";
            url = me.config.restUpdateUrl + options.className + "/" + options.sysUID;
            ajaxCall = function () {
                // hack because shortcut .patch not exists yet in angular-version
                var http = me.appBase.get('Angular.$http');
                var httpOptions = {
                        method: method, 
                        url: url, 
                        data: formData, 
                        withCredentials: true 
                };
                if (flgMultiPart) {
                    // spring accepts no fileuploads for PATCH
                    httpOptions.method = "POST",
                    httpOptions.headers = {'Content-Type': undefined};
                    httpOptions.transformRequest = angular.identity;
                }
                return http(httpOptions);
            };
        } else if (options.mode === "create") {
            // mode create 
            method = "POST";
            url = me.config.restCreateUrl + options.className + "/" + options.sysUID;
            
            ajaxCall = function () {
                var http = me.appBase.get('Angular.$http');
                var httpOptions = {
                        method: method, 
                        url: url, 
                        data: formData, 
                        withCredentials: true 
                };
                if (flgMultiPart) {
                    httpOptions.headers = {'Content-Type': undefined};
                    httpOptions.transformRequest = angular.identity;
                }
                return http(httpOptions);
            };
        } else {
            // unknown mode
            svcLogger.logError("unknown mode=" + options.mode + " form formName=" + options.formName, false);
            return null;
        }

        // define json for common fields
        json = JSON.stringify(nodeObj);
        
        // do http
        console.log(msg + " CALL url:" + url + " data:" + json);
        return ajaxCall();
    };

    me._yaioCallLoadNodeById = function(nodeId, options) {
        var msg = "_yaioCallLoadNodeById node: " + nodeId + " options:" + options;
        console.log(msg + " START");
        var restBaseUrl = me.config.restShowUrl;
        if (options.flgNodeByAllId) {
            restBaseUrl = me.config.restSymLinkUrl;
        }
        var url = restBaseUrl + nodeId;
        var ajaxCall = function () {
            return me.appBase.get('Angular.$http').get(url);
        };
        
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
        
        // copy availiable serverSearchOptions
        var serverSearchOptions = {};
        var searchFields = ["strTypeFilter", "strReadIfStatusInListOnly", "maxEbene", "strClassFilter", "strWorkflowStateFilter", "strNotNodePraefix", "flgConcreteToDosOnly"];
        var searchField;
        for (var idx = 0; idx < searchFields.length; idx++) {
            searchField = searchFields[idx];
            if (searchOptions.hasOwnProperty(searchField)) {
                serverSearchOptions[searchField] = searchOptions[searchField];
            }
        }
        if (serverSearchOptions.hasOwnProperty("strNotNodePraefix")) {
            // replace * with sql %
            serverSearchOptions.strNotNodePraefix = serverSearchOptions.strNotNodePraefix.replace(/\*/g, "%");
        }

        // load data
        var url = me.config.restSearchUrl + uri;
        var ajaxCall = function () {
            return me.appBase.get('Angular.$http').post(url, serverSearchOptions, {
                    withCredentials: true,
                    headers : {
                        "content-type" : "application/json;charset=utf-8"
                    }
                });
        };
        
        // do http
        console.log(msg + " CALL url:" + url);
        return ajaxCall();
    };

    me._yaioCallLogin = function(credentials) {
        var msg = "_yaioCallLogin for credentials:" + credentials;
        console.log(msg + " START");

        // load data
        var url = me.config.restLoginUrl;
        var ajaxCall = function () {
            return me.appBase.get('Angular.$http').post(url, $.param(credentials),
                {
                    headers : {
                        "content-type" : "application/x-www-form-urlencoded"
                    }
                });
        };
        
        // do http
        console.log(msg + " CALL url:" + url);
        return ajaxCall();
    };
    
    me._yaioCallLogout = function(session) {
        var msg = "_yaioCallLogout for session" + session;
        console.log(msg + " START");

        // load data
        var url = me.config.restLogoutUrl;
        var ajaxCall = function () {
            return me.appBase.get('Angular.$http').post(url, $.param({}), {withCredentials: true});
        };
        
        // do http
        console.log(msg + " CALL url:" + url);
        return ajaxCall();
    };
    
    me._yaioCallCheckUser = function(session) {
        var msg = "_yaioCallCheckUser for session:" + session;
        console.log(msg + " START");

        // load data
        var url = me.config.restCheckUserUrl + "?" + (new Date()).getTime();
        var ajaxCall = function () {
            return me.appBase.get('Angular.$http').get(url);
        };
        
        // do http
        console.log(msg + " CALL url:" + url);
        return ajaxCall();
    };
    
    me._init();
    
    return me;
};
