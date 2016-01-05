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
Yaio.StaticNodeDataService = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = Yaio.NodeDataService(appBase, config, defaultConfig);
    
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
    
    me.connectService = function() {
        // update serviceconfig
        me.updateServiceConfig();
        me.updateAppConfig();
        
        // load data
        me._loadStaticJson(JSON.stringify(window.yaioStaticJSON));

        // return promise
        var dfd = new $.Deferred();
        var res = dfd.promise();
        dfd.resolve("OK");
        return res;
    };
    
    me.updateServiceConfig = function(yaioCommonApiConfig) {
    };

    me.exportNodeActionResponseJSONById = function(nodeId) {
        return JSON.stringify(me._exportNodeActionResponseById(nodeId));
    };
    
    /*****************************************
     *****************************************
     * Service-Funktions (webservice)
     *****************************************
     *****************************************/
    me._createAccessManager = function() {
        return Yaio.StaticAccessManagerService(me.appBase, me.config);
    };
    
    me._loadStaticJson = function(json) {
        me.appBase.get("YaioStaticNodeDataStore").resetNodeList();
        me.flgDataLoaded = false;

        var yaioNodeActionReponse = JSON.parse(json);
        var masterNode = yaioNodeActionReponse.node;
        
        // create Masternode if not exists 
        if (masterNode.sysUID != me.appBase.config.masterSysUId) {
            masterNode = {
                "className": "TaskNode",
                "name": "Masterplan",
                "ebene": 0,
                "nodeDesc": "Masternode of the Masterplan",
                "sysUID": me.appBase.config.masterSysUId,
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
        me.appBase.get("YaioStaticNodeDataStore").loadStaticNodeData(masterNode);
        me.flgDataLoaded = true;
    };

    me._getNodeDataById = function(nodeId, flgCopy) {
        return me.appBase.get("YaioStaticNodeDataStore").getNodeDataById(nodeId, flgCopy);
    };

    me._getParentIdHierarchyById = function(nodeId, flgCopy) {
        if (flgCopy) {
            return JSON.parse(JSON.stringify(me._getParentIdHierarchyById(nodeId, false)));
        }
        
        var parentIdHirarchy = [];
        var node = me._getNodeDataById(nodeId, false);
        if (node && node.parentId && node.parentId != "" && node.parentId != "undefined") {
            parentIdHirarchy = me._getParentIdHierarchyById(node.parentId, true);
            parentIdHirarchy.push(node.parentId);
        }
        return parentIdHirarchy;
    };

    me._getChildNodesById = function(nodeId, flgCopy) {
        // check for node
        var node = me._getNodeDataById(nodeId, false);
        if (! node) {
            return [];
        }
        
        // read nodes for childNodeIds
        var childNodes = [];
        for (var i = 0; i < node.childNodes.length; i++) {
            childNodes.push(me._getNodeDataById(node.childNodes[i], flgCopy));
        }
        
        return childNodes;
    };
    
    me._getNodeActionResponseById = function(nodeId) {
        // extract data
        var nodeData = me._getNodeDataById(nodeId, true);
        var parentIdHierarchy = me._getParentIdHierarchyById(nodeId, false);
        var childNodes = me._getChildNodesById(nodeId, false);
        
        // add parentHierarchy
        var curNode = nodeData;
        while (curNode.parentId) {
            curNode.parentNode = me._getNodeDataById(curNode.parentId, true);
            curNode = curNode.parentNode;
        }
        
        // create response
        var nodeActionResponse = {
            state: "OK",
            stateMsg: "node '" +  nodeId + "' found",
            node: nodeData,
            parentIdHierarchy: parentIdHierarchy,
            childNodes: childNodes
        };
        if (! nodeData || ! parentIdHierarchy) {
            nodeActionResponse.state = "ERROR";
            nodeActionResponse.stateMsg = "node '" +  nodeId + "' not found";
        }

        return nodeActionResponse;
    };

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
            nodeActionResponse.state = "ERROR";
            nodeActionResponse.stateMsg = "node '" +  nodeId + "' not found";
        }

        return nodeActionResponse;
    };

    me._exportNodeJSONById = function(nodeId) {
        // extract data
        var node = me._getNodeDataById(nodeId, true);
        var childNodes = [];
        for (var i = 0; i < node.childNodes.length; i++) {
            childNodes.push(me._exportNodeJSONById(node.childNodes[i]));
        }
        node.childNodes = childNodes;

        return node;
    };

    me._yaioCallMoveNode = function(fancynode, newParentKey, newPos, json) {
        var msg = "_yaioCallMoveNode for fancynode:" + fancynode.key + " newParentKey:" + newParentKey + " newPos:" + newPos;

        var node = me.appBase.get("YaioStaticNodeDataStore").moveNode(fancynode, newParentKey, newPos, json);

        // create response for
        var nodeActionResponse = me._getNodeActionResponseById(node.sysUID);

        var dfd = new $.Deferred();
        var res = dfd.promise();
        dfd.resolve(nodeActionResponse);

        return res;
    };

    me._yaioCallRemoveNode = function(nodeId) {
        var node = me._getNodeDataById(nodeId, true);
        me.appBase.get("YaioStaticNodeDataStore").removeNodeById(nodeId);
        
        // create response for parent
        var nodeActionResponse = me._getNodeActionResponseById(node.parentId);

        var dfd = new $.Deferred();
        var res = dfd.promise();
        dfd.resolve(nodeActionResponse);

        return res;
    };
    
    me._yaioCallLoadSymLinkData = function(basenode, fancynode) {
        var msg = "_yaioCallLoadSymLinkData for node:" + basenode.sysUID + " symlink:" + basenode.symLinkRef + " fancynode:" + fancynode.key;
        console.log(msg + " START");
        var nodeActionResponse = me._getNodeActionResponseById(basenode.symLinkRef);

        var dfd = new $.Deferred();
        var res = dfd.promise();
        dfd.resolve(nodeActionResponse);

        return res;
    };
    
    me._yaioCallLoadNodeById = function(nodeId, options) {
        var msg = "_yaioCallLoadNodeById node: " + nodeId + " options:" + options;
        console.log(msg + " START");
        
        // mock the ajax-request
        var promiseHelper = me.appBase.get("YaioPromiseHelper").createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        };
        var angularResponse = {
            data: me._getNodeActionResponseById(nodeId)
        };
        promiseHelper.resolve(angularResponse);
        
        // do http
        return ajaxCall();
    };
    
    me._yaioCallSaveNode = function(nodeObj, options) {
        var msg = "_yaioCallSaveNode node: " + options.mode + ' ' + nodeObj.sysUID;
        console.log(msg + " START:", nodeObj);

        var node = me.appBase.get("YaioStaticNodeDataStore").saveNode(nodeObj, options);

        // mock the ajax-request
        var promiseHelper = me.appBase.get("YaioPromiseHelper").createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        };
        var angularResponse = {
            data: me._getNodeActionResponseById(node.sysUID)
        };
        console.log(msg + " response:", angularResponse);
        promiseHelper.resolve(angularResponse);

        return ajaxCall();
    };

    me._yaioCallFulltextSearch = function(searchOptions) {
        var msg = "_yaioCallFulltextSearch searchOptions: " + searchOptions;
        
        var searchResponse = me.appBase.get("YaioStaticNodeDataStore").fulltextSearch(searchOptions);
        
        // mock the ajax-request
        var promiseHelper = me.appBase.get("YaioPromiseHelper").createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        };
        var angularResponse = {
            data: searchResponse
        };
        console.log(msg + " response:", angularResponse);
        promiseHelper.resolve(angularResponse);

        return ajaxCall();
    };

    me._yaioCallLogin = function(credentials) {
        var msg = "_yaioCallLogin for credentials:" + credentials;
        console.log(msg + " START");

        // load data
        var promiseHelper = me.appBase.get("YaioPromiseHelper").createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        };
        var angularResponse = {
            data: "OK"
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
        };
        var angularResponse = {
            data: "OK"
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
        };
        var angularResponse = {
            data: "OK"
        };
        promiseHelper.resolve(angularResponse);
        
        // do http
        return ajaxCall();
    };
    
    me._init();
    
    return me;
};
