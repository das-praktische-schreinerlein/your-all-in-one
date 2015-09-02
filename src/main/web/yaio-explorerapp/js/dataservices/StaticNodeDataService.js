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
Yaio.StaticNodeDataService = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = Yaio.NodeDataService(appBase, config, defaultConfig);
    
    me.nodeList = {};
    me.parentIdHirarchies = {};
    me.flgDataLoaded = false;

    /**
     * initialize the object
     */
    me._init = function() {
    };
    
    me.loadNodeData = function(nodeId) {
        // use promise as described on https://github.com/mar10/fancytree/wiki/TutorialLoadData#user-content-use-a-deferred-promise
        console.log("load data for node:" + nodeId);
        var nodeActionResponse = me._getNodeActionResponseById(nodeId);
        var fancyTreeResponse = { response: nodeActionResponse};
        me.appBase.get('YaioExplorerTree').postProcessNodeData({}, fancyTreeResponse);
        
        var dfd = new $.Deferred();
        var res = dfd.promise();
        dfd.resolve(fancyTreeResponse.result);

        return res;
    };
    
    me.loadStaticJson = function() {
        // load data
        me.nodeList = {};
        me.parentIdHirarchies = {};
        me.flgDataLoaded = false;
        me._loadStaticJson(JSON.stringify(window.yaioStaticJSON));
        
        // return promise
        var dfd = new $.Deferred();
        var res = dfd.promise();
        dfd.resolve("OK");
        return res;
    }
    
    me.exportNodeActionResponseJSONById = function(nodeId) {
        return JSON.stringify(me._exportNodeActionResponseById(nodeId))
    }
    
    /*****************************************
     *****************************************
     * Service-Funktions (webservice)
     *****************************************
     *****************************************/
    me._createAccessManager = function() {
        return Yaio.StaticAccessManagerService(me.appBase, me.config);
    };
    
    me._loadStaticJson = function(json) {
        var yaioNodeActionReponse = JSON.parse(json);
        var masterNode = yaioNodeActionReponse.node;
        var masterParentIdHirarchy = [];
        
        // create Masternode if not exists 
        if (masterNode.sysUID != me.appBase.config.CONST_MasterId) {
            masterNode = {
                "className": "TaskNode",
                "name": "Masterplan",
                "ebene": 0,
                "nodeDesc": "Masternode of the Masterplan",
                "sysUID": me.appBase.config.CONST_MasterId,
                "sysChangeCount": 9,
                "sysCreateDate": 1411717226471,
                "sysChangeDate": 1429887836887,
                "metaNodePraefix": "Masterplan",
                "metaNodeNummer": "1",
                "state": "RUNNING",
                "type": "RUNNING",
                "workflowState": "RUNNING",
                "sortPos": 0,
                "childNodes": [yaioNodeActionReponse.node]
            };
        }
        me._convertStaticNodeData(masterNode, masterParentIdHirarchy);
        me.flgDataLoaded = true;
    }

    me._convertStaticNodeData = function (node, parentIdHirarchy) {
        if (! node) {
            return;
        }
        if (! node.sysUID) {
            return;
        }
        if (! parentIdHirarchy) {
            parentIdHirarchy = [];
        }

        // TODO: create a copy of the node
        me.nodeList[node.sysUID] = node;
        me.parentIdHirarchies[node.sysUID] = parentIdHirarchy;
        
        var newParentIdHirarchy = parentIdHirarchy.slice();
        newParentIdHirarchy.push(node.sysUID);
        for (var i = 0; i < node.childNodes.length; i++) {
            var childNode = node.childNodes[i];
            me._convertStaticNodeData(childNode, newParentIdHirarchy);
            node.childNodes[i] = childNode.sysUID;
        }
    }
    
    me._getNodeDataById = function(nodeId, flgCopy) {
        if (flgCopy) {
            return JSON.parse(JSON.stringify(me._getNodeDataById(nodeId, false)));
        }
        return me.nodeList[nodeId];
    }
    me._getParentIdHierarchyById = function(nodeId) {
        return me.parentIdHirarchies[nodeId];
    }
    me._getChildNodesById = function(nodeId, flgCopy) {
        // check for node
        var node = me._getNodeDataById(nodeId, false);
        if (! node) {
            return [];
        }
        
        // read nodes for childNodeIds
        var childNodes = [];
        for (var i = 0; i < node.childNodes.length; i++) {
            var childNodeId = node.childNodes[i];
            childNodes.push(me._getNodeDataById(node.childNodes[i], flgCopy));
        }
        
        return childNodes;
    }
    
    me._getNodeActionResponseById = function(nodeId) {
        // extract data
        var nodeData = me._getNodeDataById(nodeId);
        var parentIdHierarchy = me._getParentIdHierarchyById(nodeId);
        var childNodes = me._getChildNodesById(nodeId);
        
        // create response
        var nodeActionResponse = {
            state: "OK",
            stateMsg: "node '" +  nodeId + "' found",
            node: nodeData,
            parentIdHierarchy: parentIdHierarchy,
            childNodes: childNodes
        };
        if (! nodeData || ! parentIdHierarchy) {
            nodeActionResponse.state = "ERROR",
            nodeActionResponse.stateMsg = "node '" +  nodeId + "' not found";
        }

        return nodeActionResponse;
    }

    me._exportNodeActionResponseById = function(nodeId) {
        // extract data
        var nodeData = me._exportNodeJSONById(nodeId);
        var parentIdHierarchy = me._getParentIdHierarchyById(nodeId, true);
        
        // create response
        var nodeActionResponse = {
            state: "OK",
            stateMsg: "node '" +  nodeId + "' found",
            node: nodeData,
            parentIdHierarchy: parentIdHierarchy
        };
        if (! nodeData || ! parentIdHierarchy) {
            nodeActionResponse.state = "ERROR",
            nodeActionResponse.stateMsg = "node '" +  nodeId + "' not found";
        }

        return nodeActionResponse;
    }

    me._exportNodeJSONById = function(nodeId) {
        // extract data
        var node = me._getNodeDataById(nodeId, true);
        var childNodes = [];
        for (var i = 0; i < node.childNodes.length; i++) {
            var childNodeId = node.childNodes[i];
            childNodes.push(me._exportNodeJSONById(node.childNodes[i]));
        }
        node.childNodes = childNodes;

        return node;
    }

    me._yaioCallLoadNodeById = function(nodeId, options) {
        var msg = "_yaioCallLoadNodeById node: " + nodeId + " options:" + options;
        console.log(msg + " START");
        
        // mock the ajax-request
        var promiseHelper = me.appBase.get("YaioPromiseHelper").createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        }
        var angularResponse = {
            data: me._getNodeActionResponseById(nodeId)
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
