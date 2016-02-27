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
Yaio.ServerNodeDBDriver = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = Yaio.AbstractNodeDBDriver(appBase, config, defaultConfig);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * load the data of the node (own, parent, children)
     * use promise as described on https://github.com/mar10/fancytree/wiki/TutorialLoadData#user-content-use-a-deferred-promise
     * @param {String} nodeId     id of the node to load data for
     * @returns {JQueryPromise<T>|JQueryPromise<*>|Object}    Object or JQueryPromise for FancyTree
     */
    me.loadNodeData = function(nodeId) {
        console.log('load data for node:' + nodeId);
        return { 
            url: me.config.restShowUrl + nodeId, 
            cache: false 
        };
    };

    /**
     * connect the dataservice
     * - load config from server
     * - updateServiceConfig
     * - updateAppConfig
     * @returns {JQueryPromise<T>|JQueryPromise<*>}    promise if connect succeed or failed
     */
    me.connectService = function() {
        var res = me._loadConfig();
        return res;
    };

    /**
     * update my config (this instance of ServerNodeDBDriverConfig)
     * @param {Object} yaioCommonApiConfig  Common Api Config from yaio-server
     */
    me.configureDataService = function(yaioCommonApiConfig) {
        if (yaioCommonApiConfig) {
            var msg = 'configureService for yaio';
            console.log(msg + ' with:', yaioCommonApiConfig);
            
            // base
            me.config.name               = yaioCommonApiConfig.yaioInstanceName;
            // dont overwrite configured url, maybe its tunneled: me.config.urlBase            = yaioCommonApiConfig.yaioInstanceUrl;
            me.config.desc               = yaioCommonApiConfig.yaioInstanceDesc;
            me.config.updateConfig();
            
            // options
            me.config.masterSysUId       = yaioCommonApiConfig.yaioMastersysuid;
            me.config.excludeNodePraefix = yaioCommonApiConfig.yaioExportcontrollerExcludenodepraefix;
            me.config.excludeNodePraefix = (!me.appBase.DataUtils.isUndefined(me.config.excludeNodePraefix) ?
                me.config.excludeNodePraefix.replace(/%/g, '*') : 'nothing');

            // services
            me.config.plantUmlBaseUrl    = yaioCommonApiConfig.plantUmlBaseUrl;

            me.config.dmsAvailable       = yaioCommonApiConfig.dmsAvailable;
            me.config.webshotAvailable   = yaioCommonApiConfig.webshotAvailable;
            me.config.metaextractAvailable = yaioCommonApiConfig.metaextractAvailable;

            console.log(msg + ' to:', me.config);
        }
    };
    
    /*****************************************
     *****************************************
     * Service-Funktions (webservice)
     *****************************************
     *****************************************/

    /**
     * @inheritdoc
     */
    me.updateNode = function(nodeId, json) {
        var url = me.config.restUpdateUrl + nodeId;
        return me.patchNode(nodeId, url, json);
    };

    /**
     * @inheritdoc
     */
    me.moveNode = function(nodeId, newParentKey, newPos) {
        var json = JSON.stringify({parentNode: newParentKey});
        var url = me.config.restMoveUrl + nodeId + '/' + newParentKey + '/' + newPos;
        return me.patchNode(nodeId, url, json);
    };

    /**
     * @inheritdoc
     */
    me.copyNode = function(nodeId, newParentKey) {
        var json = JSON.stringify({parentNode: newParentKey});
        var url = me.config.restCopyUrl + nodeId + '/' + newParentKey;
        return me.patchNode(nodeId, url, json);
    };

    /**
     * @inheritdoc
     */
    me.patchNode = function(nodeId, url, json) {
        var svcLogger = me.appBase.get('Logger');

        var msg = '_patchNode for nodeId:' + nodeId;
        console.log(msg + ' CALL: ' + 'url: '+ url + ' with:' + json);
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
                svcLogger.logError('The following error occured: ' + textStatus + ' ' + errorThrown, true);
                svcLogger.logError('cant save nodeId:' + nodeId + ' error:' + textStatus);
            },
            complete : function() {
                console.log('update nodeId:' + nodeId + ' ran');
            }
        });
    };

    /**
     * @inheritdoc
     */
    me.getNodeForSymLink = function(basenode) {
        var svcLogger = me.appBase.get('Logger');

        var msg = '_getNodeForSymLink for node:' + basenode.sysUID + ' symlink:' + basenode.symLinkRef;
        console.log(msg + ' START');
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
                svcLogger.logError('ERROR  ' + msg + ' The following error occured: ' + textStatus + ' ' + errorThrown, false);
                svcLogger.logError('cant load ' + msg + ' error:' + textStatus, true);
            },
            complete : function() {
                console.log('completed load ' + msg);
            }
        });
    };

    /**
     * @inheritdoc
     */
    me.deleteNode = function(nodeId) {
        var svcLogger = me.appBase.get('Logger');

        var msg = '_deleteNode node:' + nodeId;
        var url = me.config.restRemoveUrl + nodeId;
        console.log(msg + ' START: with:' + url);
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
                svcLogger.logError('The following error occured: ' + textStatus + ' ' + errorThrown, false);
                svcLogger.logError('cant remove node:' + nodeId + ' error:' + textStatus, true);
            },
            complete: function() {
                console.log('remove node:' + nodeId + ' ran');
            }
        });
    };

    /**
     * @inheritdoc
     */
    me.saveNode = function(nodeObj, options) {
        var svcLogger = me.appBase.get('Logger');

        var msg = '_saveNode node: ' + options.mode + ' ' + nodeObj.sysUID;
        console.log(msg + ' START');
        // branch depending on mode
        var method, url, json, ajaxCall, formData;
        
        if (options.mode === 'create') {
            // unset sysUID
            nodeObj.sysUID = null;
        }

        // special case UrlResNode because of multipart-uploads
        var flgMultiPart = false;
        if (options.className === 'UrlResNode') {
            // UrlResNod set formdata for multipart-uploads
            flgMultiPart = true;
            
            // create formadata
            formData = new FormData();
            formData.append('node', new Blob([JSON.stringify(nodeObj)], {
                                                type: 'application/json'
                                            })
            );
            
            // add uploadfile only if set
            formData.append('uploadFile', options.uploadFile ? options.uploadFile : '');
        } else {
            // default: json-request: add nodeObj
            formData = nodeObj;
        }
        
        if (options.mode === 'edit') {
            // mode update 
            method = 'PATCH';
            url = me.config.restUpdateUrl + options.className + '/' + options.sysUID;
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
                    httpOptions.method = 'POST';
                    httpOptions.headers = {'Content-Type': undefined};
                    httpOptions.transformRequest = angular.identity;
                }
                return http(httpOptions);
            };
        } else if (options.mode === 'create') {
            // mode create 
            method = 'POST';
            url = me.config.restCreateUrl + options.className + '/' + options.sysUID;
            
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
            svcLogger.logError('unknown mode=' + options.mode + ' form formName=' + options.formName, false);
            return null;
        }

        // define json for common fields
        json = JSON.stringify(nodeObj);
        
        // do http
        console.log(msg + ' CALL url:' + url + ' data:' + json);
        return ajaxCall();
    };

    /**
     * @inheritdoc
     */
    me.getNodeById = function(nodeId, options) {
        var msg = '_getNodeById node: ' + nodeId + ' options:' + options;
        console.log(msg + ' START');
        var restBaseUrl = me.config.restShowUrl;
        if (options.flgNodeByAllId) {
            restBaseUrl = me.config.restSymLinkUrl;
        }
        var url = restBaseUrl + nodeId;
        var ajaxCall = function () {
            return me.appBase.get('Angular.$http').get(url);
        };
        
        // do http
        console.log(msg + ' CALL url:' + url);
        return ajaxCall();
    };

    /**
     * @inheritdoc
     */
    me.searchNode = function(searchOptions) {
        var msg = '_searchNode searchOptions: ' + searchOptions;
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
        var searchFields = ['strTypeFilter', 'strReadIfStatusInListOnly', 'maxEbene', 'strClassFilter', 'strWorkflowStateFilter', 'strNotNodePraefix', 'flgConcreteToDosOnly'];
        var searchField;
        for (var idx = 0; idx < searchFields.length; idx++) {
            searchField = searchFields[idx];
            if (searchOptions.hasOwnProperty(searchField)) {
                serverSearchOptions[searchField] = searchOptions[searchField];
            }
        }
        if (serverSearchOptions.hasOwnProperty('strNotNodePraefix')) {
            // replace * with sql %
            serverSearchOptions.strNotNodePraefix = serverSearchOptions.strNotNodePraefix.replace(/\*/g, '%');
        }

        // load data
        var url = me.config.restSearchUrl + uri;
        var ajaxCall = function () {
            return me.appBase.get('Angular.$http').post(url, serverSearchOptions, {
                    withCredentials: true,
                    headers : {
                        'content-type' : 'application/json;charset=utf-8'
                    }
                });
        };
        
        // do http
        console.log(msg + ' CALL url:' + url);
        return ajaxCall();
    };

    /**
     * @inheritdoc
     */
    me.loginToService = function(credentials) {
        var msg = '_loginToService for credentials:' + credentials;
        console.log(msg + ' START');

        // load data
        var url = me.config.restLoginUrl;
        var ajaxCall = function () {
            return me.appBase.get('Angular.$http').post(url, $.param(credentials),
                {
                    headers : {
                        'content-type' : 'application/x-www-form-urlencoded'
                    }
                });
        };
        
        // do http
        console.log(msg + ' CALL url:' + url);
        return ajaxCall();
    };

    /**
     * @inheritdoc
     */
    me.logoutFromService = function(session) {
        var msg = '_logoutFromService for session' + session;
        console.log(msg + ' START');

        // load data
        var url = me.config.restLogoutUrl;
        var ajaxCall = function () {
            return me.appBase.get('Angular.$http').post(url, $.param({}), {withCredentials: true});
        };
        
        // do http
        console.log(msg + ' CALL url:' + url);
        return ajaxCall();
    };

    /**
     * @inheritdoc
     */
    me.checkSession = function(session) {
        var msg = '_checkSession for session:' + session;
        console.log(msg + ' START');

        // load data
        var url = me.config.restCheckUserUrl + '?' + (new Date()).getTime();
        var ajaxCall = function () {
            return me.appBase.get('Angular.$http').get(url);
        };
        
        // do http
        console.log(msg + ' CALL url:' + url);
        return ajaxCall();
    };

    /**
     * @inheritdoc
     */
    me._createAccessManager = function() {
        return Yaio.ServerAccessManager(me.appBase, me.config);
    };

    /**
     * TODO
     * @returns {JQueryPromise<T>|JQueryPromise<*>}
     * @private
     */
    me._loadConfig = function() {
        // return promise
        var dfd = new $.Deferred();
        var res = dfd.promise();

        // load config from server
        var resConfig = me._loadConfigFromServer();
        resConfig.done(function(yaioCommonApiConfig, textStatus, jqXhr ) {
            var msg = '_loadConfig for yaio';
            console.log(msg + ' done');
            // update config
            me.configureDataService(yaioCommonApiConfig);
            me.reconfigureBaseApp();

            // resolve promise
            dfd.resolve('OK');
        });

        return res;
    };

    /**
     * TODO
     * @returns {*|JQueryXHR|{xhrFields}}
     * @private
     */
    me._loadConfigFromServer = function() {
        var svcLogger = me.appBase.get('Logger');

        var url = me.config.configUrl;
        var msg = '_loadConfigFromServer for yaio:' + url;
        console.log(msg + ' START');
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
                svcLogger.logError('ERROR  ' + msg + ' The following error occured: ' + textStatus + ' ' + errorThrown, false);
                svcLogger.logError('cant load ' + msg + ' error:' + textStatus, true);
            },
            complete : function() {
                console.log('completed load ' + msg);
            }
        });
    };

    me._init();
    
    return me;
};
