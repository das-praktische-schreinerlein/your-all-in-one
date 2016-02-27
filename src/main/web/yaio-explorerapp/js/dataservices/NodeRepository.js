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
Yaio.NodeRepository = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase, config, defaultConfig);

    /**
     * initialize the object
     */
    me._init = function() {
        me.AccessManager = null;
    };

    /**
     * return the accessmanager for this service
     * @returns {Yaio.AccessManager}    instance of Yaio.AccessManager
     */
    me.getAccessManager = function() {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().getAccessManager();
    };
    
    /**
     * load the data of the node (own, parent, children)
     * use promise as described on https://github.com/mar10/fancytree/wiki/TutorialLoadData#user-content-use-a-deferred-promise
     * @param {String} nodeId     id of the node to load data for
     * @returns {JQueryPromise<T>|JQueryPromise<*>|Object}    Object or JQueryPromise for FancyTree
     */
    me.loadNodeData = function(nodeId) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().loadNodeData(nodeId);
    };

    /**
     * connect the dataservice
     * @returns {JQueryPromise<T>|JQueryPromise<*>}    promise if connect succeed or failed
     */
    me.connectService = function() {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().connectService();
    };

    /**
     * update my config (this instance of NodeDataConfig)
     * @param {Object} yaioCommonApiConfig  Common Api Config from yaio-server
     */
    me.configureDataService = function(yaioCommonApiConfig) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().configureDataService(yaioCommonApiConfig);
    };

    /**
     * update appBase-config (plantUmlBaseUrl, masterSysUId, excludeNodePraefix) with my config (this instance of NodeDataConfig)
     */
    me.reconfigureBaseApp = function() {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().reconfigureBaseApp();
    };


    /**
     * read all available templates from configured parents accessmanager.systemTemplateId, accessmanager.ownTemplateId
     * @return {Promise}     with obj { systemTemplates: [], ownTemplates: [] }
     */
    me.getAvailableTemplates = function() {
        var msg = 'getAvailableTemplates';
        console.log(msg + ' START');

        var promiseHelper = me.appBase.get('YaioPromiseHelper').createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        };
        var angularResponse = {
            data: {systemTemplates: [], ownTemplates: []}
        };

        var systemTemplateId = me.getAccessManager().getAvailiableNodeAction('systemTemplateId'); 
        var ownTemplateId = me.getAccessManager().getAvailiableNodeAction('ownTemplateId');
        if (systemTemplateId) {
            me.getNodeById(systemTemplateId, {})
            .then(function sucess(systemAngularResponse) {
                // handle success: systemTemplates
                angularResponse.data = {systemTemplates: systemAngularResponse.data.childNodes, ownTemplates: []};
                if (! ownTemplateId) {
                    // return only mine
                    console.log(msg + ' response:', angularResponse);
                    promiseHelper.resolve(angularResponse);
                }
                
                // load own templates
                me.getNodeById(ownTemplateId, {})
                    .then(function sucess(ownAngularResponse) {
                        // handle success: ownTemplates
                        angularResponse.data = {systemTemplates: systemAngularResponse.data.childNodes, 
                            ownTemplates: ownAngularResponse.data.childNodes};
                        console.log(msg + ' response:', angularResponse);
                        promiseHelper.resolve(angularResponse);
                    }, function error() {
                        console.log(msg + 'error response:', angularResponse);
                        promiseHelper.resolve(angularResponse);
                    });
            }, function error() {
                console.log(msg + 'error response:', angularResponse);
                promiseHelper.resolve(angularResponse);
            });
        } else  {
            // no templates configured
            promiseHelper.resolve(angularResponse);
        }

        return ajaxCall();
    };

    /**
     * get symlinked nodedata for basenode
     * @param {Object} basenode                           node-data to get symlink-data
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if read succeed or failed with parameters { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me.getNodeForSymLink = function(basenode) {
        var msg = 'getNodeForSymLink for node:' + basenode.sysUID + ' symlink:' + basenode.symLinkRef;
        console.log(msg + ' START');
        return me._getNodeForSymLink(basenode);
    };

    /**
     * update node with values in json
     * @param {String} nodeId                             nodeId to update
     * @param {String} json                               json with the update-values
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if update succeed or failed  { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me.updateNode = function(nodeId, json) {
        var msg = 'updateNode for nodeId:' + nodeId;
        console.log(msg + ' START');
        return me._updateNode(nodeId, json);
    };

    /**
     * move node to newParentKey at position newPos
     * @param {String} nodeId                             nodeId to move
     * @param {String} newParentKey                       nodeId of the new parent
     * @param {int} newPos                                sort-position in parents childList
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if update succeed or failed { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me.moveNode = function(nodeId, newParentKey, newPos) {
        var msg = 'moveNode for nodeId:' + nodeId + ' newParentKey:' + newParentKey + ' newPos:' + newPos;
        console.log(msg + ' START');
        return me._moveNode(nodeId, newParentKey, newPos);
    };

    /**
     * copy node to newParentKey
     * @param {String} nodeId                             nodeId to copy
     * @param {String} newParentKey                       nodeId of the new parent
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if update succeed or failed { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me.copyNode = function(nodeId, newParentKey) {
        var msg = 'copyNode for nodeId:' + nodeId + ' newParentKey:' + newParentKey;
        console.log(msg + ' START');
        return me._copyNode(nodeId, newParentKey);
    };

    /**
     * delete node
     * @param {String} nodeId                             nodeId to delete
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if delete succeed or failed { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me.deleteNode = function(nodeId) {
        var msg = 'deleteNode for nodeId:' + nodeId;
        console.log(msg + ' START');
        return me._deleteNode(nodeId);
    };

    /**
     * save (create/update) node
     * @param {Object} nodeObj                            node with values to save
     * @param {Object} options                            options
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if save succeed or failed
     */
    me.saveNode = function(nodeObj, options) {
        var msg = 'saveNode for node:' + nodeObj.sysUID;
        console.log(msg + ' START');
        return me._saveNode(nodeObj, options);
    };

    /**
     * read node
     * @param {String} nodeId                             nodeId to read
     * @param {Object} options                            options
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if read succeed or failed
     */
    me.getNodeById = function(nodeId, options) {
        var msg = 'getNodeById for node:' + nodeId;
        console.log(msg + ' START');
        return me._getNodeById(nodeId, options);
    };

    /**
     * search node
     * @param {Object} searchOptions                      filters and sorts...
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if search succeed or failed
     */
    me.searchNode = function(searchOptions) {
        var msg = 'searchNode for searchOptions:' + searchOptions;
        console.log(msg + ' START');
        return me._searchNode(searchOptions);
    };

    /**
     * login to service
     * @param {Object} credentials                        credentials to login with
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if login succeed or failed
     */
    me.loginToService = function(credentials) {
        var msg = 'loginToService for credentials:' + credentials;
        console.log(msg + ' START');
        return me._loginToService(credentials);
    };

    /**
     * logout from service
     * @param {Object} session                            session to logout
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if logout succeed or failed
     */
    me.logoutFromService = function(session) {
        var msg = 'logoutFromService for session' + session;
        console.log(msg + ' START');
        return me._logoutFromService(session);
    };

    /**
     * check current session of service
     * @param {Object} session                            session to check
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if check succeed or failed returns { data: 'OK' }
     */
    me.checkSession = function(session) {
        var msg = 'checkSession for session:' + session;
        console.log(msg + ' START');
        return me._checkSession(session);
    };

    /**
     * implementation of: get symlinked nodedata for basenode
     * @param {Object} basenode                           node-data to get symlink-data
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if read succeed or failed with parameters { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me._getNodeForSymLink = function(basenode) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().getNodeForSymLink(basenode);
    };

    /**
     * implementation of: update node with values in json
     * @param {String} nodeId                             nodeId to update
     * @param {String} json                               json with the update-values
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if update succeed or failed  { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me._updateNode = function(nodeId, json) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().updateNode(nodeId, json);
    };

    /**
     * implementation of: move node to newParentKey at position newPos
     * @param {String} nodeId                             nodeId to move
     * @param {String} newParentKey                       nodeId of the new parent
     * @param {int} newPos                                sort-position in parents childList
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if update succeed or failed { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me._moveNode = function(nodeId, newParentKey, newPos) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().moveNode(nodeId, newParentKey, newPos);
    };

    /**
     * implementation of: copy node to newParentKey
     * @param {String} nodeId                             nodeId to copy
     * @param {String} newParentKey                       nodeId of the new parent
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if update succeed or failed { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me._copyNode = function(nodeId, newParentKey) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().copyNode(nodeId, newParentKey);
    };

    /**
     * implementation of: patch node
     * @param {String} nodeId                             nodeId to copy
     * @param {String} url                                url to call
     * @param {String} json                               json to submit
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if update succeed or failed { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me._patchNode = function(nodeId, url, json) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().patchNode(nodeId, url, json);
    };

    /**
     * implementation of delete node
     * @param {String} nodeId                             nodeId to delete
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if delete succeed or failed { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me._deleteNode = function(nodeId) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().deleteNode(nodeId);
    };

    /**
     * implementation of: save (create/update) node
     * @param {Object} nodeObj                            node with values to save
     * @param {Object} options                            options
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if save succeed or failed
     */
    me._saveNode = function(nodeObj, options) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().saveNode(nodeObj, options);
    };

    /**
     * implementation of: read node
     * @param {String} nodeId                             nodeId to read
     * @param {Object} options                            options
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if read succeed or failed
     */
    me._getNodeById = function(nodeId, options) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().getNodeById(nodeId, options);
    };

    /**
     * implementation of: search node
     * @param {Object} searchOptions                      filters and sorts...
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if search succeed or failed
     */
    me._searchNode = function(searchOptions) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().searchNode(searchOptions);
    };

    /**
     * implementation of: login to service
     * @param {Object} credentials                        credentials to login with
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if login succeed or failed
     */
    me._loginToService = function(credentials) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().loginToService(credentials);
    };

    /**
     * implementation of: logout from service
     * @param {Object} session                            session to logout
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if logout succeed or failed
     */
    me._logoutFromService = function(session) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().logoutFromService(session);
    };

    /**
     * implementation of: check current session of service
     * @param {Object} session                            session to check
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if check succeed or failed returns { data: 'OK' }
     */
    me._checkSession = function(session) {
        return me.appBase.YaioDataSourceManager.getCurrentConnection().checkSession(session);
    };

    me._init();
    
    return me;
};
