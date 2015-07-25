Yaio.ExplorerActionService = function(appBase) {
    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    }
    
    me.openNodeHierarchy = function(treeId, lstIdsHierarchy) {
        // check for tree
        var tree = $(treeId).fancytree("getTree");
        if (! tree) {
            yaioAppBase.get('YaioBaseService').logError("openHierarchy: error tree:'" + treeId + "' not found.", false);
            return;
        }
    
        // check for rootNode
        var rootNode = tree.rootNode;
        if (! rootNode) {
            yaioAppBase.get('YaioBaseService').logError("openHierarchy: error for tree:'" + treeId 
                        + "' rootNode not found.", false);
            return;
        }
        
        // check for lstIdsHierarchy
        if (! lstIdsHierarchy || lstIdsHierarchy.length <= 0) {
            yaioAppBase.get('YaioBaseService').logError("openHierarchy: error for tree:'" + treeId 
                        + "' lstIdsHierarchy is empty.", false);
            return;
        }
        
        // search for firstNode in rootTree, ignore if not found
        var firstNodeId, firstNode;
        var lstIdsHierarchySave = new Array().concat(lstIdsHierarchy); 
        while (! firstNode && lstIdsHierarchy.length > 0) {
            firstNodeId = lstIdsHierarchy.shift();
            firstNode = rootNode.mapChildren[firstNodeId];
        }
        if (! firstNode) {
            yaioAppBase.get('YaioBaseService').logError("openHierarchy: error for tree:'" + treeId 
                        + "' firstNode of:'" + lstIdsHierarchySave 
                        + "' not found on rootNode.", false);
            return;
        }
    
        // open Hierarchy
        var opts = {};
        opts.openHierarchy = lstIdsHierarchy;
        opts.activateLastNode = true;
        firstNode.setExpanded(true, opts);
    }
    
    me.openNodeHierarchyForNodeId = function(treeId, activeNodeId) {
        // check for tree
        var tree = $(treeId).fancytree("getTree");
        if (! tree) {
            yaioAppBase.get('YaioBaseService').logError("openNodeHierarchyForNodeId: error tree:'" + treeId + "' not found.", false);
            return;
        }
        
        // check for activeNodeId
        var treeNode = tree.getNodeByKey(activeNodeId);
        if (! treeNode) {
            yaioAppBase.get('YaioBaseService').logError("openNodeHierarchyForNodeId: error for tree:'" + treeId 
                    + "' activeNode " + activeNodeId + " not found.", false);
            return null;
        }
    
        // Return the parent keys separated by options.keyPathSeparator, e.g. "id_1/id_17/id_32"
        var keyPath = treeNode.getKeyPath(false);
        // extract lstIdsHierarchy
        var lstIdsHierarchy = keyPath.split("/");
        console.log("openNodeHierarchyForNodeId: extracted lst:" 
                + lstIdsHierarchy + " from keyPath:" + keyPath);
        
        // open Hierarchy
        yaioAppBase.get('YaioExplorerActionService').openNodeHierarchy(treeId, lstIdsHierarchy);
    }
    
    
    me.yaioOpenSubNodesForTree = function(treeId, level) {
        var tree = $(treeId).fancytree("getTree");
        if (! tree) {
            yaioAppBase.get('YaioBaseService').logError("yaioOpenSubNodesForTree: error tree:'" + treeId + "' not found.", false);
            return;
        }
        
        // check for activeNodeId
        var treeNode = tree.rootNode;
        if (! treeNode) {
            yaioAppBase.get('YaioBaseService').logError("yaioOpenSubNodesForTree: error rootnode for tree:'" + treeId 
                    + " not found.", false);
            return null;
        }
        
        var opts = {};
        opts.minExpandLevel = level;
        opts.recursively = true;
        console.log("yaioOpenSubNodesForTree setExpanded:" + " level:" + level);
        treeNode.setExpanded(true, opts);
    }
    
    me.yaioSaveNode = function(data) {
        var json = JSON.stringify({name: data.input.val()});
        var url = updateUrl + data.node.key;
        doUpdateNode(data.node, url, json);
    }
    
    me.yaioMoveNode = function(node, newParentKey, newPos) {
        console.log("move node:" + node.key + " to:" + newParentKey + " Pos:" + newPos);
        var json = JSON.stringify({parentNode: newParentKey});
        var url = moveUrl + node.key + "/" + newParentKey + "/" + newPos;
        yaioAppBase.get('YaioNodeDataService').yaioDoUpdateNode(node, url, json);
    }
    
    me.yaioRemoveNodeById = function(nodeId) {
        if (window.confirm("Wollen Sie die Node wirklich l&ouml;schen?")) {
            console.log("remove node:" + nodeId);
            // check for tree
            var treeId = "#tree";
            var tree = $(treeId).fancytree("getTree");
            if (! tree) {
                yaioAppBase.get('YaioBaseService').logError("yaioRemoveNode: error tree:'" + treeId + "' not found.", false);
                return;
            }
            
            // check for activeNodeId
            var treeNode = tree.getNodeByKey(nodeId);
            if (! treeNode) {
                yaioAppBase.get('YaioBaseService').logError("yaioRemoveNode: error for tree:'" + treeId 
                        + "' activeNode " + nodeId + " not found.", false);
                return null;
            }
            var url = removeUrl + nodeId;
            yaioAppBase.get('YaioNodeDataService').yaioDoRemoveNode(treeNode, url);
        } else {
            // discard
            return false;
        }
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     open the jirawindow for the node  
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: opens jira window with jira-converted node-content
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Convert
     * @param nodeId - id of the node
     */
    me.openJiraExportWindow = function(nodeId) {
        // check vars
        if (! nodeId) {
            // tree not found
            yaioAppBase.get('YaioBaseService').logError("error openJiraWindow: nodeId required", false);
            return null;
        }
        // load node
        var tree = $("#tree").fancytree("getTree");
        if (!tree) {
            // tree not found
            yaioAppBase.get('YaioBaseService').logError("error openJiraWindow: cant load tree for node:" + nodeId, false);
            return null;
        }
        var treeNode = tree.getNodeByKey(nodeId);
        if (! treeNode) {
            yaioAppBase.get('YaioBaseService').logError("error openJiraWindow: cant load node:" + nodeId, false);
            return null;
        }
        
        // extract nodedata
        var basenode = treeNode.data.basenode;
        var descText = basenode.nodeDesc;
        if (! descText) {
            descText = "";
        }
        descText = descText.replace(/\<WLBR\>/g, "\n");
        descText = descText.replace(/\<WLESC\>/g, "\\");
        descText = descText.replace(/\<WLTAB\>/g, "\t");
        
        // convert and secure
        var nodeDesc = yaioAppBase.get('YaioFormatterService').convertMarkdownToJira(descText);
        nodeDesc = yaioAppBase.get('YaioBaseService').htmlEscapeText(nodeDesc);
        
        // set clipboard-content
        $( "#clipboard-content" ).html(nodeDesc);
        
        // show message
        $( "#clipboard-box" ).dialog({
            modal: true,
            width: "700px",
            buttons: {
              Ok: function() {
                $( this ).dialog( "close" );
              }
            }
        });    
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     open the clipboardwindow with generated checklist and gantt-markdown
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: opens clipboard window with checklist/ganttmarkdown-converted node-content
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Convert
     */
    me.yaioExportExplorerLinesAsOverview = function() {
        // convert and secure
        var checkListSrc = yaioAppBase.get('YaioFormatterService').convertExplorerLinesAsCheckList();
        checkListSrc = yaioAppBase.get('YaioBaseService').htmlEscapeText(checkListSrc);
        var ganttSrc = yaioAppBase.get('YaioFormatterService').convertExplorerLinesAsGanttMarkdown();
        ganttSrc = yaioAppBase.get('YaioBaseService').htmlEscapeText(ganttSrc);
        
        // set clipboard-content
        $( "#clipboard-content" ).html(checkListSrc + "\n\n" +  ganttSrc);
        
        // show message
        $( "#clipboard-box" ).dialog({
            modal: true,
            width: "700px",
            buttons: {
              Ok: function() {
                $( this ).dialog( "close" );
              }
            }
        });    
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     open the nodeeditor with a new infornode with snaphot of current gui: checklist and gantt-markdown
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: open the nodeeditor for new infonode with checklist/ganttmarkdown-converted node-content
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Convert
     */
    me.yaioSnapshot = function(parentNode) {
        // convert and secure
        var checkListSrc = yaioAppBase.get('YaioFormatterService').convertExplorerLinesAsCheckList();
        checkListSrc = yaioAppBase.get('YaioBaseService').htmlEscapeText(checkListSrc);
        var ganttSrc = yaioAppBase.get('YaioFormatterService').convertExplorerLinesAsGanttMarkdown();
        ganttSrc = yaioAppBase.get('YaioBaseService').htmlEscapeText(ganttSrc);
    
        // open editor
        yaioAppBase.get('YaioEditorService').yaioOpenNodeEditorForNode(parentNode, 'createsnapshot', {nodeDesc: checkListSrc + "\n\n" +  ganttSrc});
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     open the txtwindow for the node  
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: opens txt-window with txt node-content
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Convert
     * @param content - txt content
     */
    me.openTxtExportWindow = function(content) {
        // secure
        content = yaioAppBase.get('YaioBaseService').htmlEscapeText(content);
    
        // set clipboard-content
        $( "#clipboard-content" ).html(content);
        
        // show message
        $( "#clipboard-box" ).dialog({
            modal: true,
            width: "700px",
            buttons: {
              Ok: function() {
                $( this ).dialog( "close" );
              }
            }
        });    
    }

    me._init();
    
    return me;
};
