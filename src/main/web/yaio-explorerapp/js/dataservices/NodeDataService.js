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
Yaio.NodeDataService = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase, config, defaultConfig);

    /**
     * initialize the object
     */
    me._init = function() {
        me.AccessManager = null;
    };
    
    me.getAccessManager = function() {
        if (! me.AccessManager) {
            me.AccessManager = me._createAccessManager();
        }
        return me.AccessManager;
    };
    
    /*****************************************
     *****************************************
     * Service-Funktions (webservice)
     *****************************************
     *****************************************/
    me.loadNodeData = function(nodeId) {
        me.logNotImplemented();
    };
    
    me.loadStaticJson = function() {
        // return promise
        var dfd = new $.Deferred();
        var res = dfd.promise();
        dfd.resolve("OK");
        return res;
    }
    
    me.yaioDoLoadAvailiableTemplates = function() {
        var msg = "yaioDoLoadTemplates";
        console.log(msg + " START");

        var promiseHelper = me.appBase.get("YaioPromiseHelper").createAngularPromiseHelper();
        var ajaxCall = function () {
            return promiseHelper.getHttpPromiseMock();
        }
        var angularResponse = {
            data: {systemTemplates: [], ownTemplates: []}
        };

        var systemTemplateId = me.getAccessManager().getAvailiableNodeAction('systemTemplateId'); 
        var ownTemplateId = me.getAccessManager().getAvailiableNodeAction('ownTemplateId');
        if (systemTemplateId) {
            me.yaioDoLoadNodeById(systemTemplateId, {})
            .then(function sucess(systemAngularResponse) {
                // handle success: systemTemplates
                angularResponse.data = {systemTemplates: systemAngularResponse.data.childNodes, ownTemplates: []};
                if (! ownTemplateId) {
                    // return only mine
                    console.log(msg + " response:", angularResponse);
                    promiseHelper.resolve(angularResponse);
                }
                
                // load own templates
                me.yaioDoLoadNodeById(ownTemplateId, {})
                    .then(function sucess(ownAngularResponse) {
                        // handle success: ownTemplates
                        angularResponse.data = {systemTemplates: systemAngularResponse.data.childNodes, 
                            ownTemplates: ownAngularResponse.data.childNodes};
                        console.log(msg + " response:", angularResponse);
                        promiseHelper.resolve(angularResponse);
                    }, function error() {
                        console.log(msg + "error response:", angularResponse);
                        promiseHelper.resolve(angularResponse);
                    });
            }, function error() {
                console.log(msg + "error response:", angularResponse);
                promiseHelper.resolve(angularResponse);
            });
        } else  {
            // no templates configured
            promiseHelper.resolve(angularResponse);
        }

        return ajaxCall();
    };
    
    me.yaioLoadSymLinkData = function(basenode, fancynode) {
        var msg = "yaioLoadSymLinkData for node:" + basenode.sysUID + " symlink:" + basenode.symLinkRef + " fancynode:" + fancynode.key;
        console.log(msg + " START");
        return me._yaioCallLoadSymLinkData(basenode, fancynode)
            .done(function(yaioNodeActionResponse, textStatus, jqXhr ) {
                console.log("call successHandler " + msg + " state:" + textStatus);
                me._yaioLoadSymLinkDataSuccessHandler(basenode, fancynode, yaioNodeActionResponse, textStatus, jqXhr);
            });
    };
        
    me.yaioDoUpdateNode = function(fancynode, json) {
        var msg = "yaioDoUpdateNode for fancynode:" + fancynode.key;
        console.log(msg + " START");
        return me._yaioCallUpdateNode(fancynode, json)
            .done(function(yaioNodeActionResponse, textStatus, jqXhr ) {
                me._yaioPatchNodeSuccessHandler(fancynode, yaioNodeActionResponse, textStatus, jqXhr);
            });
    };
    
    me.yaioDoMoveNode = function(fancynode, newParentKey, newPos, json) {
        var msg = "yaioDoMoveNode for fancynode:" + fancynode.key + " newParentKey:" + newParentKey + " newPos:" + newPos;
        console.log(msg + " START");
        return me._yaioCallMoveNode(fancynode, newParentKey, newPos, json)
            .done(function(yaioNodeActionResponse, textStatus, jqXhr ) {
                me._yaioPatchNodeSuccessHandler(fancynode, yaioNodeActionResponse, textStatus, jqXhr);
            });
    };
    
    me.yaioDoCopyNode = function(fancynode, newParentKey, json) {
        var msg = "yaioDoCopyNode for fancynode:" + fancynode.key + " newParentKey:" + newParentKey;
        console.log(msg + " START");
        return me._yaioCallCopyNode(fancynode, newParentKey, json)
            .done(function(yaioNodeActionResponse, textStatus, jqXhr ) {
                me._yaioPatchNodeSuccessHandler(fancynode, yaioNodeActionResponse, textStatus, jqXhr);
            });
    };
    
    me.yaioDoRemoveNode = function(nodeId) {
        var msg = "yaioDoRemoveNode for nodeId:" + nodeId;
        console.log(msg + " START");
        return me._yaioCallRemoveNode(nodeId)
            .done(function(yaioNodeActionResponse, textStatus, jqXhr ) {
                me._yaioRemoveNodeSuccessHandler(nodeId, yaioNodeActionResponse, textStatus, jqXhr);
            });
    };
    
    me.yaioDoSaveNode = function(nodeObj, options) {
        var msg = "yaioDoSaveNode for node:" + nodeObj['sysUID'];
        console.log(msg + " START");
        return me._yaioCallSaveNode(nodeObj, options);
    };
    
    me.yaioDoLoadNodeById = function(nodeId, options) {
        var msg = "yaioDoLoadNodeById for node:" + nodeId;
        console.log(msg + " START");
        return me._yaioCallLoadNodeById(nodeId, options);
    };
    
    me.yaioDoFulltextSearch = function(searchOptions) {
        var msg = "yaioDoFulltextSearch for searchOptions:" + searchOptions;
        console.log(msg + " START");
        return me._yaioCallFulltextSearch(searchOptions);
    };
    
    me.yaioDoLogin = function(credentials) {
        var msg = "yaioDoLogin for credentials:" + credentials;
        console.log(msg + " START");
        return me._yaioCallLogin(credentials);
    };
    
    me.yaioDoLogout = function(session) {
        var msg = "yaioDoLogout for session" + session;
        console.log(msg + " START");
        return me._yaioCallLogout(session);
    };
    
    me.yaioDoCheckUser = function(session) {
        var msg = "yaioDoCheckUser for session:" + session;
        console.log(msg + " START");
        return me._yaioCallCheckUser(session);
    };
    
    me._createAccessManager = function() {
        me.logNotImplemented();
    };
    
    me._yaioCallLoadSymLinkData = function(basenode, fancynode) {
        me.logNotImplemented();
    };
    
    me._yaioLoadSymLinkDataSuccessHandler = function(basenode, fancynode, yaioNodeActionResponse, textStatus, jqXhr) {
        var svcYaioBase = me.appBase.get('YaioBase');
        var msg = "_yaioLoadSymLinkDataSuccessHandler for fancynode:" + fancynode.key;
        console.log(msg + " OK done!" + yaioNodeActionResponse.state);
        if (yaioNodeActionResponse.state == "OK") {
            if (yaioNodeActionResponse.node) {
                var $nodeDataBlock = me.appBase.get('YaioNodeDataRender').renderDataBlock(yaioNodeActionResponse.node, fancynode);
                
                // load referring node
                var tree = me.$("#tree").fancytree("getTree");
                if (!tree) {
                    // tree not found
                    svcYaioBase.logError("error yaioLoadSymLinkData: cant load tree - " + msg, false);
                    return null;
                }
                var rootNode = tree.rootNode;
                if (! rootNode) {
                    console.error(msg + " openHierarchy: error for tree" 
                                + " rootNode not found: " + msg);
                    return;
                }
                var treeNode = tree.getNodeByKey(basenode.sysUID);
                if (! treeNode) {
                    svcYaioBase.logError("error yaioLoadSymLinkData: cant load node - " + msg, false);
                    return null;
                }
                
                // append Link in current hierarchy to referenced node
                var newUrl = '#/show/' + tree.options.masterNodeId 
                    + '/activate/' + yaioNodeActionResponse.node.sysUID;
                
                // check if node-hierarchy exists (same tree)
                var firstNodeId, firstNode;
                var lstIdsHierarchy = new Array().concat(yaioNodeActionResponse.parentIdHierarchy);
                while (! firstNode && lstIdsHierarchy.length > 0) {
                    firstNodeId = lstIdsHierarchy.shift();
                    firstNode = rootNode.mapChildren[firstNodeId];
                }
                if (! firstNode) {
                    // load page for referenced node with full hierarchy
                    //firstNodeId = yaioNodeActionResponse.parentIdHierarchy.shift();
                    // we set it constant
                    firstNodeId = me.appBase.config.CONST_MasterId;
                    
                    newUrl = '#/show/' + firstNodeId 
                        + '/activate/' + yaioNodeActionResponse.node.sysUID;
                }

                me.$(treeNode.tr).find("div.container_data_row").append(
                        "<a href='" + newUrl + "'" 
                           + " data-tooltip='Springe zum verkn&uuml;pften Element'"
                           + " class='button'>OPEN</a>");
                
                // add datablock of referenced node
                me.$(treeNode.tr).find("div.container_data_table").append($nodeDataBlock.html());

                console.log(msg + " DONE");
            } else {
                svcYaioBase.logError("ERROR got no " + msg, true);
            }
        } else {
            svcYaioBase.logError("ERROR cant load  " + msg + " error:" + yaioNodeActionResponse.stateMsg, true);
        }
    };
    
    me._yaioCallUpdateNode = function(fancynode, json) {
        me.logNotImplemented();
    };
    
    me._yaioCallMoveNode = function(fancynode, newParentKey, newPos, json) {
        me.logNotImplemented();
    };
    
    me._yaioCallCopyNode = function(fancynode, newParentKey, json) {
        me.logNotImplemented();
    };
    
    me._yaioCallPatchNode = function(fancynode, url, json) {
        me.logNotImplemented();
    };
    
    me._yaioPatchNodeSuccessHandler = function(fancynode, yaioNodeActionResponse, textStatus, jqXhr) {
        var svcYaioBase = me.appBase.get('YaioBase');
        var msg = "_yaioPatchNodeSuccessHandler for fancynode:" + fancynode.key;
        console.log(msg + " OK done!" + yaioNodeActionResponse.state);
        if (yaioNodeActionResponse.state == "OK") {
            console.log(msg + " OK saved fancynode:" + fancynode.key + " load:" + yaioNodeActionResponse.parentIdHierarchy);
            if (yaioNodeActionResponse.parentIdHierarchy && yaioNodeActionResponse.parentIdHierarchy.length > 0) {
                // reload tree
                var tree = me.$("#tree").fancytree("getTree");
                tree.reload().done(function(){
                    // handler when done
                    console.log(msg + " RELOAD tree done:" + yaioNodeActionResponse.parentIdHierarchy);
                    console.log(msg + " CALL openNodeHierarchy load hierarchy:" + yaioNodeActionResponse.parentIdHierarchy);
                    me.appBase.get('YaioExplorerAction').openNodeHierarchy("#tree", yaioNodeActionResponse.parentIdHierarchy);
                });
            } else {
                svcYaioBase.logError("got no hierarchy for:" + fancynode.key 
                        + " hierarchy:" + yaioNodeActionResponse.parentIdHierarchy, true);
            }
        } else {
            var message = "cant save fancynode:" + fancynode.key + " error:" + yaioNodeActionResponse.stateMsg;
            // check for violations
            if (yaioNodeActionResponse.violations) {
                // iterate violations
                message = message +  " violations: ";
                for (var idx in yaioNodeActionResponse.violations) {
                    var violation = yaioNodeActionResponse.violations[idx];
                    svcYaioBase.logError("violations while save fancynode:" + fancynode.key 
                            + " field:" + violation.path + " message:" + violation.message, false);
                    message = message +  violation.path + " (" + violation.message + "),";
                }
            }
            svcYaioBase.logError(message, true);
        }
    };

    me._yaioCallRemoveNode = function(nodeId) {
        me.logNotImplemented();
    };

    me._yaioRemoveNodeSuccessHandler = function(nodeId, yaioNodeActionResponse, textStatus, jqXhr) {
        var svcYaioBase = me.appBase.get('YaioBase');
        var msg = "_yaioRemoveNodeSuccessHandler for nodeId:" + nodeId;
        console.log(msg + " OK done!" + yaioNodeActionResponse.state);
        if (yaioNodeActionResponse.state == "OK") {
            console.log(msg + " OK removed node:" + nodeId + " load:" + yaioNodeActionResponse.parentIdHierarchy);
            if (yaioNodeActionResponse.parentIdHierarchy && yaioNodeActionResponse.parentIdHierarchy.length >= 0) {
                // reload tree
                var tree = me.$("#tree").fancytree("getTree");
                tree.reload().done(function(){
                    // handler when done
                    console.log(msg + " RELOAD tree done:" + yaioNodeActionResponse.parentIdHierarchy);
                    console.log(msg + " CALL openNodeHierarchy load hierarchy:" + yaioNodeActionResponse.parentIdHierarchy);
                    me.appBase.get('YaioExplorerAction').openNodeHierarchy("#tree", yaioNodeActionResponse.parentIdHierarchy);
                });
            } else {
                svcYaioBase.logError("got no hierarchy for:" + nodeId
                        + " hierarchy:" + yaioNodeActionResponse.parentIdHierarchy, true);
            }
        } else {
            svcYaioBase.logError("cant remove node:" + nodeId + " error:" + yaioNodeActionResponse.stateMsg, false);
            // check for violations
            if (yaioNodeActionResponse.violations) {
                // iterate violations
                for (var idx in yaioNodeActionResponse.violations) {
                    var violation = yaioNodeActionResponse.violations[idx];
                    svcYaioBase.logError("violations while remove node:" + nodeId 
                            + " field:" + violation.path + " message:" + violation.message, false);
                    window.alert("cant remove node because: " + violation.path + " (" + violation.message + ")");
                }
            }
        }
    };
    
    me._yaioCallSaveNode = function(nodeObj, options) {
        me.logNotImplemented();
    };

    me._yaioCallLoadNodeById = function(nodeId, options) {
        me.logNotImplemented();
    };
    
    me._yaioCallFulltextSearch = function(searchOptions) {
        me.logNotImplemented();
    };

    me._yaioCallLogin = function(credentials) {
        me.logNotImplemented();
    };

    me._yaioCallLogout = function() {
        me.logNotImplemented();
    };

    me._yaioCallCheckUser = function() {
        me.logNotImplemented();
    };

    me._init();
    
    return me;
};
