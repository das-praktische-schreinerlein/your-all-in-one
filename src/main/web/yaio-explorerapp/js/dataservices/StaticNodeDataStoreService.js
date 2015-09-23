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
    me.mapWorkflowStates = [];
    me.mapSorts = [];

    /**
     * initialize the object
     */
    me._init = function() {
        me.mapWorkflowStates['UNKNOWN'] = 'NOTPLANED';
        me.mapWorkflowStates['OPEN'] = 'OPEN';
        me.mapWorkflowStates['LATE'] = 'LATE';
        me.mapWorkflowStates['RUNNING'] = 'RUNNING';
        me.mapWorkflowStates['WARNING'] = 'WARNING';
        me.mapWorkflowStates['ERLEDIGT'] = 'DONE';
        me.mapWorkflowStates['VERWORFEN'] = 'CANCELED';
        me.mapWorkflowStates['EVENT_UNKNOWN'] = 'NOTPLANED';
        me.mapWorkflowStates['EVENT_PLANED'] = 'OPEN';
        me.mapWorkflowStates['EVENT_CONFIRMED'] = 'OPEN';
        me.mapWorkflowStates['EVENT_LATE'] = 'LATE';
        me.mapWorkflowStates['EVENT_RUNNING'] = 'RUNNING';
        me.mapWorkflowStates['EVENT_SHORT'] = 'WARNING';
        me.mapWorkflowStates['EVENT_ERLEDIGT'] = 'DONE';
        me.mapWorkflowStates['EVENT_VERWORFEN'] = 'CANCELED';

        me.mapSorts["default"] = "ebene asc";
        me.mapSorts["createdUp"] = "sysCreateDate asc";
        me.mapSorts["createdDown"] = "sysCreateDate desc";
        me.mapSorts["istEndeUp"] = "istChildrenSumEnde asc";
        me.mapSorts["istEndeDown"] = "istChildrenSumEnde desc";
        me.mapSorts["istStartUp"] = "istChildrenSumStart asc";
        me.mapSorts["istStartDown"] = "istChildrenSumStart desc";
        me.mapSorts["lastChangeUp"] = "sysChangeDate asc";
        me.mapSorts["lastChangeDown"] = "sysChangeDate desc";
        me.mapSorts["nameUp"] = "name asc";
        me.mapSorts["nameDown"] = "name desc";
        me.mapSorts["nodeNumberUp"] = "metaNodePraefix asc, metaNodeNummer asc";
        me.mapSorts["nodeNumberDown"] = "metaNodePraefix desc, metaNodeNummer desc";
        me.mapSorts["planEndeUp"] = "planEnde asc";
        me.mapSorts["planEndeDown"] = "planEnde desc";
        me.mapSorts["planStartUp"] = "planStart asc";
        me.mapSorts["planStartDown"] = "planStart desc";
        me.mapSorts["planChildrenSumEndeUp"] = "planChildrenSumEnde asc";
        me.mapSorts["planChildrenSumEndeDown"] = "planChildrenSumEnde desc";
        me.mapSorts["planChildrenSumStartUp"] = "planChildrenSumStart asc";
        me.mapSorts["planChildrenSumStartDown"] = "planChildrenSumStart desc";
        me.mapSorts["typeUp"] = "type asc";
        me.mapSorts["typeDown"] = "type desc";
        me.mapSorts["workflowStateUp"] = "workflowState asc";
        me.mapSorts["workflowStateDown"] = "workflowState desc";
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
        node['workflowState'] = me.mapWorkflowStates[node['state']];
        
        // save node
        console.log(msg + " save node:", node)
        me.nodeList[node['sysUID']] = node;
        
        console.log(msg + " response:", node);

        return node;
    };

    me.fulltextSearch = function(searchOptions) {
        var msg = "fulltextSearch searchOptions: " + searchOptions;

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
        
        // read all data and sort
        var searchConfig = [];
        searchConfig.push(me.mapSorts[searchOptions.searchSort]);
        searchConfig.push("ebene asc");
        searchConfig.push("parentNode asc");
        searchConfig.push("sortPos asc");
        var tmpSearchResult = [];
        for (var idx = 0; idx < searchResultIds.length; idx++) {
            nodeId = searchResultIds[idx];
            tmpSearchResult.push(me.getNodeDataById(nodeId, true));
        }
        me.orderBy(tmpSearchResult, searchConfig);
        
        // paginate and read current searchresults
        var start = (searchOptions.curPage - 1) * searchOptions.pageSize;
        var ende = start + searchOptions.pageSize;
        if (ende >= tmpSearchResult.length) {
            ende = tmpSearchResult.length;
        }
        var searchResult = tmpSearchResult.slice((searchOptions.curPage - 1) * searchOptions.pageSize, ende);
        
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

    me.dynamicSort = function (property) {
        var sortOrder = 1;
        if (property.search(" desc") > 0) {
            sortOrder = -1;
        }
        property = property.replace(/( desc)|( asc)$/, "").trim();
        return function (a,b) {
            var result = (a[property] < b[property]) ? -1 : (a[property] > b[property]) ? 1 : 0;
            result = result * sortOrder;
            // sort null and undefined to last
            if (result == -1 && (a[property] == null || a[property] === "undefined")) {
                result = 1;
            } else if (result == 1 && (b[property] == null || b[property] === "undefined")) {
                result = -1;
            }
//            console.log("dynamicSort: " + sortOrder + " " + property + " a:" + a[property] + " b:" + b[property] + " res:" +  result * sortOrder);
            return result;
        }
    };
    
    me.dynamicSortMultiple = function (props) {
        return function (obj1, obj2) {
            var i = 0, result = 0, numberOfProperties = props.length;
            /* try getting a different result from 0 (equal)
             * as long as we have extra properties to compare
             */
            while(result === 0 && i < numberOfProperties) {
                result = me.dynamicSort(props[i])(obj1, obj2);
                i++;
            }
            return result;
        }
    };
    
    me.orderBy = function(list, sortConfig) {
        list.sort(me.dynamicSortMultiple(sortConfig));
    };

    
    me._init();
    
    return me;
};
