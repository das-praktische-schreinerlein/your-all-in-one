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
Yaio.AbstractNodeDBDriver = function(appBase, config, defaultConfig) {
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
        if (! me.AccessManager) {
            me.AccessManager = me._createAccessManager();
        }
        return me.AccessManager;
    };

    /**
     * load the data of the node (own, parent, children)
     * use promise as described on https://github.com/mar10/fancytree/wiki/TutorialLoadData#user-content-use-a-deferred-promise
     * @abstract
     * @param {String} nodeId     id of the node to load data for
     * @returns {JQueryPromise<T>|JQueryPromise<*>|Object}    Object or JQueryPromise for FancyTree
     */
    me.loadNodeData = function(nodeId) {
        me.logNotImplemented();
    };

    /**
     * connect the dataservice
     * @abstract
     * @returns {JQueryPromise<T>|JQueryPromise<*>}    promise if connect succeed or failed
     */
    me.connectService = function() {
        me.logNotImplemented();
    };

    /**
     * update my config (this instance of NodeDataConfig)
     * @param {Object} yaioCommonApiConfig  Common Api Config from yaio-server
     */
    me.configureDataService = function(yaioCommonApiConfig) {
        me.logNotImplemented();
    };

    /**
     * update appBase-config (plantUmlBaseUrl, masterSysUId, excludeNodePraefix) with my config (this instance of NodeDataConfig)
     */
    me.reconfigureBaseApp = function() {
        var msg = 'reconfigureBaseApp';
        console.log(msg + ' with:', me.config);
        if (me.config.plantUmlBaseUrl) {
            me.appBase.config.plantUmlBaseUrl = me.config.plantUmlBaseUrl;
        }
        if (me.config.masterSysUId) {
            me.appBase.config.masterSysUId = me.config.masterSysUId;
        }
        if (me.config.excludeNodePraefix) {
            me.appBase.config.excludeNodePraefix = me.config.excludeNodePraefix;
        }
        console.log(msg + ' to:', me.appBase.config);
    };


    /**
     * implementation of: get symlinked nodedata for basenode
     * @abstract
     * @param {Object} basenode                           node-data to get symlink-data
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if read succeed or failed with parameters { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me.getNodeForSymLink = function(basenode) {
        me.logNotImplemented();
    };

    /**
     * implementation of: update node with values in json
     * @abstract
     * @param {String} nodeId                             nodeId to update
     * @param {String} json                               json with the update-values
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if update succeed or failed  { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me.updateNode = function(nodeId, json) {
        me.logNotImplemented();
    };

    /**
     * implementation of: move node to newParentKey at position newPos
     * @abstract
     * @param {String} nodeId                             nodeId to move
     * @param {String} newParentKey                       nodeId of the new parent
     * @param {int} newPos                                sort-position in parents childList
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if update succeed or failed { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me.moveNode = function(nodeId, newParentKey, newPos) {
        me.logNotImplemented();
    };

    /**
     * implementation of: copy node to newParentKey
     * @abstract
     * @param {String} nodeId                             nodeId to copy
     * @param {String} newParentKey                       nodeId of the new parent
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if update succeed or failed { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me.copyNode = function(nodeId, newParentKey) {
        me.logNotImplemented();
    };

    /**
     * implementation of: patch node
     * @abstract
     * @param {String} nodeId                             nodeId to copy
     * @param {String} url                                url to call
     * @param {String} json                               json to submit
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if update succeed or failed { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me.patchNode = function(nodeId, url, json) {
        me.logNotImplemented();
    };

    /**
     * implementation of delete node
     * @abstract
     * @param {String} nodeId                             nodeId to delete
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if delete succeed or failed { yaioNodeActionResponse, textStatus, jqXhr}
     */
    me.deleteNode = function(nodeId) {
        me.logNotImplemented();
    };

    /**
     * implementation of: save (create/update) node
     * @abstract
     * @param {Object} nodeObj                            node with values to save
     * @param {Object} options                            options
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if save succeed or failed
     */
    me.saveNode = function(nodeObj, options) {
        me.logNotImplemented();
    };

    /**
     * implementation of: read node
     * @abstract
     * @param {String} nodeId                             nodeId to read
     * @param {Object} options                            options
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if read succeed or failed
     */
    me.getNodeById = function(nodeId, options) {
        me.logNotImplemented();
    };

    /**
     * implementation of: search node
     * @abstract
     * @param {Object} searchOptions                      filters and sorts...
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if search succeed or failed
     */
    me.searchNode = function(searchOptions) {
        me.logNotImplemented();
    };

    /**
     * implementation of: login to service
     * @abstract
     * @param {Object} credentials                        credentials to login with
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if login succeed or failed
     */
    me.loginToService = function(credentials) {
        me.logNotImplemented();
    };

    /**
     * mplementation of: logout from service
     * @abstract
     * @param {Object} session                            session to logout
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if logout succeed or failed
     */
    me.logoutFromService = function(session) {
        me.logNotImplemented();
    };

    /**
     * implementation of: check current session of service
     * @abstract
     * @param {Object} session                            session to check
     * @returns {JQueryPromise<T>|JQueryDeferred<T>|any}  promise if check succeed or failed returns { data: 'OK' }
     */
    me.checkSession = function(session) {
        me.logNotImplemented();
    };

    /**
     * implementation of: create an accessmanager for this service
     * @abstract
     * @returns {Yaio.AccessManager}    instance of Yaio.AccessManager
     */
    me._createAccessManager = function() {
        me.logNotImplemented();
    };

    me._init();
    
    return me;
};
