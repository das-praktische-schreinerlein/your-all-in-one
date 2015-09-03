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
    
    me.nodeList = [];
    me.flgDataLoaded = false;
    me.curUId = 1;

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
        me.nodeList = [];
        me.flgDataLoaded = false;

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

        me.nodeList[node.sysUID] = node;
        me.nodeList.push(node.sysUID);
        
        var newParentIdHirarchy = parentIdHirarchy.slice();
        newParentIdHirarchy.push(node.sysUID);
        for (var i = 0; i < node.childNodes.length; i++) {
            var childNode = node.childNodes[i];
            childNode.parentId = node.sysUID;
            me._convertStaticNodeData(childNode, newParentIdHirarchy);
            node.childNodes[i] = childNode.sysUID;
        }
    }
    
    me._getNodeDataById = function(nodeId, flgCopy) {
        if (flgCopy) {
            return JSON.parse(JSON.stringify(me._getNodeDataById(nodeId, false)));
        }
        var node = me.nodeList[nodeId];
        if (node) {
            node.statChildNodeCount = node.childNodes.length;
        }

        return node;
    }
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

    me._yaioCallMoveNode = function(fancynode, newParentKey, newPos, json) {
        var msg = "_yaioCallMoveNode for fancynode:" + fancynode.key + " newParentKey:" + newParentKey + " newPos:" + newPos;
        var node = me._getNodeDataById(fancynode.key, false);
        var parent = me._getNodeDataById(newParentKey, false);
        var oldParent = me._getNodeDataById(node.parentId, false);
        
        if (node.parentId != newParentKey) {
            // delete node form old parent and add to new with sortPos after last
            oldParent.childNodes.splice(oldParent.childNodes.indexOf(node.sysUID), 1);

            // update parentIdHierarchy
            console.log(msg + " use newparent:" + newPos, parent)
            
            // set new parentId
            node.parentId = parent.sysUID;
        } else {
            // change sortPos
            
            // delete node from childList 
            parent.childNodes.splice(parent.childNodes.indexOf(node.sysUID), 1);
            console.log(msg + " use oldparent:", parent)
        }

        // calc index where to add
        node.sortPos = newPos;
        var addIdx = -1;
        for (var idx = 0; idx < parent.childNodes.length; idx++) {
            var curNode = me._getNodeDataById(parent.childNodes[idx], false);
            if (addIdx >= 0) {
                // pos already found: add 5 to every following node
                curNode.sortPos = curNode.sortPos + 5;
            } else if (curNode.sortPos > node.sortPos) {
                // set id to add the node
                addIdx = idx;
                // pos found: add 5 to every following node
                curNode.sortPos = curNode.sortPos + 5;
            }
        }
        if (addIdx < 0) {
            addIdx = parent.childNodes.length;
        }
        // add node at addIdx
        console.log(msg + " addNewNode at pos:" + addIdx)
        parent.childNodes.splice(addIdx, 0, node.sysUID);

        // create response for
        var nodeActionResponse = me._getNodeActionResponseById(node.sysUID);

        var dfd = new $.Deferred();
        var res = dfd.promise();
        dfd.resolve(nodeActionResponse);

        return res;
    };

    me._yaioCallRemoveNode = function(nodeId) {
        var node = me._getNodeDataById(nodeId, false);
        
        // remove
        me._removeNodeById(nodeId);
        
        // create response for parent
        var nodeActionResponse = me._getNodeActionResponseById(node.parentId);

        var dfd = new $.Deferred();
        var res = dfd.promise();
        dfd.resolve(nodeActionResponse);

        return res;
    };
    
    me._removeNodeById = function(nodeId) {
        var svcYaioBase = me.appBase.get('YaioBase');
        var msg = "_yaioCallRemoveNode node:" + nodeId;
        
        var node = me._getNodeDataById(nodeId, false);
        if (node) {
            // delete all children
            for (var i = 0; i < node.childNodes.length; i++) {
                var childNodeId = node.childNodes[i];
                me._removeNodeById(childNodeId);
            }
            
            // delete from parent
            var parent = me._getNodeDataById(node.parentId, false);
            if (parent) {
                parent.childNodes.splice(parent.childNodes.indexOf(nodeId), 1);
            }
        }

        // delete from list
        me.nodeList[nodeId] = null;
        me.nodeList.splice(me.nodeList.indexOf(nodeId), 1);
    }

    me._yaioCallLoadSymLinkData = function(basenode, fancynode) {
        var svcYaioBase = me.appBase.get('YaioBase');

        var msg = "_yaioCallLoadSymLinkData for node:" + basenode.sysUID + " symlink:" + basenode.symLinkRef + " fancynode:" + fancynode.key;
        console.log(msg + " START");
        var nodeActionResponse = me._getNodeActionResponseById(basenode.symLinkRef);

        var dfd = new $.Deferred();
        var res = dfd.promise();
        dfd.resolve(nodeActionResponse);

        return res;
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
    
    me._yaioCallSaveNode = function(nodeObj, options) {
        var svcYaioBase = me.appBase.get('YaioBase');

        var node = JSON.parse(JSON.stringify(nodeObj));
        var msg = "_yaioCallSaveNode node: " + options.mode + ' ' + nodeObj['sysUID'];
        var now = new Date();
        console.log(msg + " START:", nodeObj);

        if (options.mode === "edit") {
            // mode update
            
            // merge orig and new node
            var orig = me._getNodeDataById(node['sysUID'], false);
            for(var prop in node){
                orig[prop] = node[prop];
            }
            node = orig;
        } else if (options.mode === "create") {
            // mode create 
            
            // read parent
            var parent = me._getNodeDataById(options.sysUID, false);
            
            // set initial values
            node['sysUID'] = "newDT" + now.toLocaleFormat('%y%m%d%H%M%S') + now.getMilliseconds() + me.curUId;
            me.curUId++;
            
            node['sysCreateDate'] = now.getTime();
            node['childNodes'] = [];
            node['className'] = options.className
            node['metaNodePraefix'] = parent['metaNodePraefix'];
            node['ebene'] = parent['ebene'] + 1;
            node['sortPos'] = 0;
            node['parentId'] = parent.sysUID;
            var parentChilds = me._getNodeDataById(parent.sysUID, false);
            if (parentChilds.length > 0) {
                node['sortPos'] = parentChilds[parentChilds.length].sortPos;
            }
            
            // add to parent
            parent.childNodes.push(node['sysUID']);
            
            // add to nodeList
            me.nodeList.push(node['sysUID']);
        } else {
            // unknown mode
            svcYaioBase.logError("unknown mode=" + options.mode + " form formName=" + options.formName, false);
            return null;
        }

        // set common values
        node['sysChangeDate'] = now.getTime();
        node['sysChangeCount'] = (node['sysChangeCount'] > 0 ? node['sysChangeCount']+1 : 1);
        node['state'] = node['type'];
        node['workflowState'] = node['state'];
        
        // save node
        console.log(msg + " save node:", node)
        me.nodeList[node['sysUID']] = node;
        
        // mock the ajax-request
        var promiseHelper = me.appBase.get("YaioPromiseHelper").createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        }
        var angularResponse = {
            data: me._getNodeActionResponseById(node['sysUID'])
        };
        console.log(msg + " response:", angularResponse);
        promiseHelper.resolve(angularResponse);

        return ajaxCall();
    };

    me._yaioCallFulltextSearch = function(searchOptions) {
        var msg = "_yaioCallFulltextSearch searchOptions: " + searchOptions;
//            + '/' + encodeURI(searchOptions.searchSort)
//            uri = uri + encodeURI(searchOptions.fulltext) + '/';
        // search ids
        var nodeId, node, flgFound, content;
        var searchResultIds = [];
        var suchworte = searchOptions.fulltext.toLowerCase().split(" ");
        for (var idx = 0; idx < me.nodeList.length; idx++) {
            nodeId = me.nodeList[idx];
            node = me._getNodeDataById(nodeId, true);
            content = node.nodeDesc + " " + node.name + " " + node.state;

            // Volltextsuche
            flgFound = false;
            if (me.appBase.get("YaioExportedData").VolltextTreffer(content.toLowerCase(), suchworte)) {
                // words found
                searchResultIds.push(nodeId);
            }
        }
        
        // paginate and read current searchresults
        var start = (searchOptions.curPage - 1) * searchOptions.pageSize;
        var ende = start + searchOptions.pageSize;
        if (ende >= searchResultIds.length) {
            ende = searchResultIds.length;
        }
        var curSearchResultIds = searchResultIds.slice((searchOptions.curPage - 1) * searchOptions.pageSize, ende);
        var searchResult = [];
        for (var idx = 0; idx < curSearchResultIds.length; idx++) {
            nodeId = curSearchResultIds[idx];
            searchResult.push(me._getNodeDataById(nodeId, true));
        }
        
        // mock the ajax-request
        var promiseHelper = me.appBase.get("YaioPromiseHelper").createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        }
        var angularResponse = {
            data: { 
                state: "OK", 
                stateMsg: "search done",
                nodes: searchResult,
                curPage: searchOptions.curPage,
                pageSize: searchOptions.pageSize,
                count: searchResultIds.length
            }
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
