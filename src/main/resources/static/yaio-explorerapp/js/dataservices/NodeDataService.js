Yaio.NodeDataService = function(appBase) {
    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    }
    
    /*****************************************
     *****************************************
     * Service-Funktions (webservice)
     *****************************************
     *****************************************/
    me.yaioDoUpdateNode = function(node, url, json) {
        var msg = "update for node:" + node.key;
        console.log("yaioDoUpdateNode START: " + msg + "url: "+ url + " with:" + json);
        $.ajax({
            headers : {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json'
            },
            url : url,
            type : 'PATCH',
            data : json,
            success : function(response, textStatus, jqXhr) {
                console.log("OK done!" + response.state);
                if (response.state == "OK") {
                    console.log("OK saved node:" + node.key + " load:" + response.parentIdHierarchy);
                    if (response.parentIdHierarchy && response.parentIdHierarchy.length > 0) {
                        // reload tree
                        var tree = $("#tree").fancytree("getTree");
                        tree.reload().done(function(){
                            // handler when done
                            console.log("reload tree done:" + response.parentIdHierarchy);
                            console.log("call openNodeHierarchy load hierarchy:" + response.parentIdHierarchy);
                            yaioAppBase.get('YaioExplorerActionService').openNodeHierarchy("#tree", response.parentIdHierarchy);
                        });
                    } else {
                        yaioAppBase.get('YaioBaseService').logError("got no hierarchy for:" + node.key 
                                + " hierarchy:" + response.parentIdHierarchy, true);
                    }
                } else {
                    var message = "cant save node:" + node.key + " error:" + response.stateMsg;
                    // check for violations
                    if (response.violations) {
                        // iterate violations
                        message = message +  " violations: ";
                        for (var idx in response.violations) {
                            var violation = response.violations[idx];
                            yaioAppBase.get('YaioBaseService').logError("violations while save node:" + node.key 
                                    + " field:" + violation.path + " message:" + violation.message, false);
                            message = message +  violation.path + " (" + violation.message + "),";
                        }
                    }
                    yaioAppBase.get('YaioBaseService').logError(message, true)
                }
            },
            error : function(jqXHR, textStatus, errorThrown) {
                // log the error to the console
                yaioAppBase.get('YaioBaseService').logError("The following error occured: " + textStatus + " " + errorThrown, true);
                yaioAppBase.get('YaioBaseService').logError("cant save node:" + node.key + " error:" + textStatus)
            },
            complete : function() {
                console.log("update node:" + node.key + "' ran");
            }
        });
    }
    
    me.yaioLoadSymLinkData = function(basenode, fancynode) {
        var msg = "symlink for node:" + basenode.sysUID + " symlink:" + basenode.symLinkRef + " fancynode:" + fancynode.key;
        var url = symLinkUrl + basenode.symLinkRef;
        console.log("load " + msg);
        $.ajax({
            headers : {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json'
            },
            url : url,
            type : 'GET',
            success : function(response, textStatus, jqXhr) {
                console.log("OK done!" + response.state);
                if (response.state == "OK") {
                    console.log("OK got " + msg);
                    if (response.node) {
                        var $nodeDataBlock = yaioAppBase.get('YaioNodeDataRenderService').renderDataBlock(response.node, fancynode);
                        
                        // load referring node
                        var tree = $("#tree").fancytree("getTree");
                        if (!tree) {
                            // tree not found
                            logErrorerror("error yaioLoadSymLinkData: cant load tree - " + msg, false);
                            return null;
                        }
                        var rootNode = tree.rootNode;
                        if (! rootNode) {
                            console.error("openHierarchy: error for tree" 
                                        + " rootNode not found: " + msg);
                            return;
                        }
                        var treeNode = tree.getNodeByKey(basenode.sysUID);
                        if (! treeNode) {
                            yaioAppBase.get('YaioBaseService').logError("error yaioLoadSymLinkData: cant load node - " + msg, false);
                            return null;
                        }
                        
                        // append Link in current hierarchy to referenced node
                        var newUrl = '#/show/' + tree.options.masterNodeId 
                            + '/activate/' + response.node.sysUID;
                        
                        // check if node-hiarchy exists (same tree)
                        var firstNodeId, firstNode;
                        var lstIdsHierarchy = new Array().concat(response.parentIdHierarchy);
                        while (! firstNode && lstIdsHierarchy.length > 0) {
                            firstNodeId = lstIdsHierarchy.shift();
                            firstNode = rootNode.mapChildren[firstNodeId];
                        }
                        if (! firstNode) {
                            // load page for referenced node with full hierarchy
                            //firstNodeId = response.parentIdHierarchy.shift();
                            // we set it constant
                            firstNodeId = CONST_MasterId;
                            
                            newUrl = '#/show/' + firstNodeId 
                                + '/activate/' + response.node.sysUID;
                        }
    
                        $(treeNode.tr).find("div.container_data_row").append(
                                "<a href='" + newUrl + "'" 
                                   + " data-tooltip='Springe zum verkn&uuml;pften Element'"
                                   + " class='button'>OPEN</a>");
                        
                        // add datablock of referenced node
                        $(treeNode.tr).find("div.container_data_table").append($nodeDataBlock.html());
    
                        console.log("renderSymLinkDataBLock done:" + msg);
                    } else {
                        yaioAppBase.get('YaioBaseService').logError("ERROR got no " + msg, true);
                    }
                } else {
                    yaioAppBase.get('YaioBaseService').logError("ERROR cant load  " + msg + " error:" + response.stateMsg, true);
                }
            },
            error : function(jqXHR, textStatus, errorThrown) {
                // log the error to the console
                yaioAppBase.get('YaioBaseService').logError("ERROR  " + msg + " The following error occured: " + textStatus + " " + errorThrown, false);
                yaioAppBase.get('YaioBaseService').logError("cant load " + msg + " error:" + textStatus, true)
            },
            complete : function() {
                console.log("completed load " + msg);
            }
        });
    }
    
    
    me.yaioDoRemoveNode = function(node, url) {
        var msg = "remove node:" + node.key;
        console.log("yaioDoRemoveNode START: " + msg + " with:" + url);
        $.ajax({
            headers : {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json'
            },
            url : url,
            type : 'DELETE',
            success : function(response, textStatus, jqXhr) {
                console.log("OK done!" + response.state);
                if (response.state == "OK") {
                    console.log("OK removed node:" + node.key + " load:" + response.parentIdHierarchy);
                    if (response.parentIdHierarchy && response.parentIdHierarchy.length >= 0) {
                        // reload tree
                        var tree = $("#tree").fancytree("getTree");
                        tree.reload().done(function(){
                            // handler when done
                            console.log("reload tree done:" + response.parentIdHierarchy);
                            console.log("call openNodeHierarchy load hierarchy:" + response.parentIdHierarchy);
                            yaioAppBase.get('YaioExplorerActionService').openNodeHierarchy("#tree", response.parentIdHierarchy);
                        });
                    } else {
                        yaioAppBase.get('YaioBaseService').logError("got no hierarchy for:" + node.key 
                                + " hierarchy:" + response.parentIdHierarchy, true);
                    }
                } else {
                    yaioAppBase.get('YaioBaseService').logError("cant remove node:" + node.key + " error:" + response.stateMsg, false);
                    // check for violations
                    if (response.violations) {
                        // iterate violations
                        for (var idx in response.violations) {
                            var violation = response.violations[idx];
                            yaioAppBase.get('YaioBaseService').logError("violations while remove node:" + node.key 
                                    + " field:" + violation.path + " message:" + violation.message, false);
                            window.alert("cant remove node because: " + violation.path + " (" + violation.message + ")")
                        }
                    }
                }
            },
            error : function(jqXHR, textStatus, errorThrown) {
                // log the error to the console
                yaioAppBase.get('YaioBaseService').logError("The following error occured: " + textStatus + " " + errorThrown, false);
                yaioAppBase.get('YaioBaseService').logError("cant remove node:" + node.key + " error:" + textStatus, true)
            },
            complete : function() {
                console.log("remove node:" + node.key + "' ran");
            }
        });
    }

    me._init();
    
    return me;
};
