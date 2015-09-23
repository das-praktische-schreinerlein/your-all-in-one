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
 *     servicefunctions for datastore-services
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.StaticNodeDataStoreService = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase, config, defaultConfig);
    
    me.nodeList = [];
    me.curUId = 1;

    /**
     * initialize the object
     */
    me._init = function() {
    };
    
    me.resetNodeList = function() {
        me.nodeList = [];
    }

    me.loadStaticNodeData = function (node) {
        if (! node) {
            return;
        }
        if (! node.sysUID) {
            return;
        }

        me.nodeList[node.sysUID] = node;
        me.nodeList.push(node.sysUID);
        
        for (var i = 0; i < node.childNodes.length; i++) {
            var childNode = node.childNodes[i];
            childNode.parentId = node.sysUID;
            me.loadStaticNodeData(childNode);
            node.childNodes[i] = childNode.sysUID;
        }
    }
    
    me.getNodeDataById = function(nodeId, flgCopy) {
        if (flgCopy) {
            var node = me.getNodeDataById(nodeId, false);
            return JSON.parse(JSON.stringify(node));
        }
        var node = me.nodeList[nodeId];
        if (node) {
            node.statChildNodeCount = node.childNodes.length;
        }

        return node;
    }

    me.moveNode = function(fancynode, newParentKey, newPos, json) {
        var msg = "moveNode for fancynode:" + fancynode.key + " newParentKey:" + newParentKey + " newPos:" + newPos;
        var node = me.getNodeDataById(fancynode.key, false);
        var parent = me.getNodeDataById(newParentKey, false);
        var oldParent = me.getNodeDataById(node.parentId, false);
        
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
            var curNode = me.getNodeDataById(parent.childNodes[idx], false);
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

        return node;
    };

    me.removeNodeById = function(nodeId) {
        var svcYaioBase = me.appBase.get('YaioBase');
        var msg = "removeNode node:" + nodeId;
        
        var node = me.getNodeDataById(nodeId, false);
        if (node) {
            // delete all children
            for (var i = 0; i < node.childNodes.length; i++) {
                var childNodeId = node.childNodes[i];
                me.removeNodeById(childNodeId);
            }
            
            // delete from parent
            var parent = me.getNodeDataById(node.parentId, false);
            if (parent) {
                parent.childNodes.splice(parent.childNodes.indexOf(nodeId), 1);
            }
        }

        // delete from list
        me.nodeList[nodeId] = null;
        me.nodeList.splice(me.nodeList.indexOf(nodeId), 1);
    }

    me.saveNode = function(nodeObj, options) {
        var svcYaioBase = me.appBase.get('YaioBase');

        var node = JSON.parse(JSON.stringify(nodeObj));
        var msg = "_saveNode node: " + options.mode + ' ' + nodeObj['sysUID'];
        var now = new Date();
        console.log(msg + " START:", nodeObj);

        if (options.mode === "edit") {
            // mode update
            
            // merge orig and new node
            var orig = me.getNodeDataById(node['sysUID'], false);
            for (var prop in node){
                orig[prop] = node[prop];
            }
            node = orig;
        } else if (options.mode === "create") {
            // mode create 
            
            // read parent
            var parent = me.getNodeDataById(options.sysUID, false);
            
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
            var parentChilds = me.getNodeDataById(parent.sysUID, false);
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
        
        console.log(msg + " response:", node);

        return node;
    };

    me.fulltextSearch = function(searchOptions) {
        var msg = "fulltextSearch searchOptions: " + searchOptions;
//            + '/' + encodeURI(searchOptions.searchSort)
        // search ids
        var nodeId, node, flgFound, content;
        var searchResultIds = [];
        var suchworte = searchOptions.fulltext.toLowerCase().split(" ");
        var classes = searchOptions.strClassFilter.split(",");
        var states = searchOptions.strWorkflowStateFilter.split(",");
        for (var idx = 0; idx < me.nodeList.length; idx++) {
            nodeId = me.nodeList[idx];
            node = me.getNodeDataById(nodeId, true);
            content = node.nodeDesc + " " + node.name + " " + node.state;

            flgFound = false;
            // Fulltext
            if (suchworte.length > 0 && !me.appBase.get("YaioExportedData").VolltextTreffer(content.toLowerCase(), suchworte)) {
                // words not found
                continue;
            }
            // Classfilter
            if (states.length > 0 && !me.appBase.get("YaioExportedData").VolltextTreffer(node.workflowState, states)) {
                // words not found
                continue;
            }
            // Workflowstate-Filter
            if (states.length > 0 && !me.appBase.get("YaioExportedData").VolltextTreffer(node.className, classes)) {
                // words not found
                continue;
            }
            
            // no filter or all machtes
            searchResultIds.push(nodeId);
            flgFound = true;
        }
        
        // sort TODO
        
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
            searchResult.push(me.getNodeDataById(nodeId, true));
        }
        
        var searchResponse = { 
            state: "OK", 
            stateMsg: "search done",
            nodes: searchResult,
            curPage: searchOptions.curPage,
            pageSize: searchOptions.pageSize,
            count: searchResultIds.length
        };
        console.log(msg + " response:", searchResponse);

        return searchResponse;
    };

    
    me._init();
    
    return me;
};
