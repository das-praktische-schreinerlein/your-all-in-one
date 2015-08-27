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
Yaio.StaticNodeDataService = function(appBase) {
    'use strict';

    // my own instance
    var me = Yaio.NodeDataService(appBase);
    
    me.nodeList = {};

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
    me._createAccessManager = function() {
        return me.appBase.get("Yaio.StaticAccessManagerService");
    };
    
    me._yaioCallLoadNodeById = function(nodeId, options) {
        var msg = "_yaioCallLoadNodeById node: " + nodeId + " options:" + options;
        console.log(msg + " START");
        var promiseHelper = me.appBase.get("YaioPromiseHelper").createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        }
        var angularResponse = {
            data: window.yaioStaticJSON,
        };
        promiseHelper.resolve(angularResponse);
        
        // do http
        return ajaxCall();
    };
    
    me._yaioCallLogin = function(credentials) {
        var msg = "_yaioCallLogin for credentials:" + credentials;
        console.log(msg + " START");

        // load data
        var promiseHelper = me.appBase.get("YaioPromiseHelper").createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        }
        var angularResponse = {
            data: "OK",
        };
        promiseHelper.resolve(angularResponse);
        
        // do http
        return ajaxCall();
    };
    
    me._yaioCallLogout = function(session) {
        var msg = "_yaioCallLogout for session" + session;
        console.log(msg + " START");

        // load data
        var promiseHelper = me.appBase.get("YaioPromiseHelper").createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        }
        var angularResponse = {
            data: "OK",
        };
        promiseHelper.resolve(angularResponse);
        
        // do http
        return ajaxCall();
    };
    
    me._yaioCallCheckUser = function(session) {
        var msg = "_yaioCallCheckUser for session:" + session;
        console.log(msg + " START");

        // load data
        var promiseHelper = me.appBase.get("YaioPromiseHelper").createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        }
        var angularResponse = {
            data: "OK",
        };
        promiseHelper.resolve(angularResponse);
        
        // do http
        return ajaxCall();
    };
    
    me._init();
    
    return me;
};
