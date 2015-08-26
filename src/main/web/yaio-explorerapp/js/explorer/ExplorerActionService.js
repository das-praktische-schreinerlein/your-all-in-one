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
 *     servicefunctions for explorer-actions
 *
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.ExplorerActionService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };
    
    me.openNodeHierarchy = function(treeId, lstIdsHierarchy) {
        // check for tree
        var tree = me.$(treeId).fancytree("getTree");
        if (! tree) {
            me.appBase.get('YaioBase').logError("openHierarchy: error tree:'" + treeId + "' not found.", false);
            return;
        }
    
        // check for rootNode
        var rootNode = tree.rootNode;
        if (! rootNode) {
            me.appBase.get('YaioBase').logError("openHierarchy: error for tree:'" + treeId 
                        + "' rootNode not found.", false);
            return;
        }
        
        // check for lstIdsHierarchy
        if (! lstIdsHierarchy || lstIdsHierarchy.length <= 0) {
            me.appBase.get('YaioBase').logError("openHierarchy: error for tree:'" + treeId 
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
            me.appBase.get('YaioBase').logError("openHierarchy: error for tree:'" + treeId 
                        + "' firstNode of:'" + lstIdsHierarchySave 
                        + "' not found on rootNode.", false);
            return;
        }
    
        // open Hierarchy
        var opts = {};
        opts.openHierarchy = lstIdsHierarchy;
        opts.activateLastNode = true;
        firstNode.setExpanded(true, opts);
    };
    
    me.openNodeHierarchyForNodeId = function(treeId, activeNodeId) {
        // check for tree
        var tree = me.$(treeId).fancytree("getTree");
        if (! tree) {
            me.appBase.get('YaioBase').logError("openNodeHierarchyForNodeId: error tree:'" + treeId + "' not found.", false);
            return;
        }
        
        // check for activeNodeId
        var treeNode = tree.getNodeByKey(activeNodeId);
        if (! treeNode) {
            me.appBase.get('YaioBase').logError("openNodeHierarchyForNodeId: error for tree:'" + treeId 
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
        me.openNodeHierarchy(treeId, lstIdsHierarchy);
    };
    
    
    me.yaioOpenSubNodesForTree = function(treeId, level) {
        var tree = me.$(treeId).fancytree("getTree");
        if (! tree) {
            me.appBase.get('YaioBase').logError("yaioOpenSubNodesForTree: error tree:'" + treeId + "' not found.", false);
            return;
        }
        
        // check for activeNodeId
        var treeNode = tree.rootNode;
        if (! treeNode) {
            me.appBase.get('YaioBase').logError("yaioOpenSubNodesForTree: error rootnode for tree:'" + treeId 
                    + " not found.", false);
            return null;
        }
        
        var opts = {};
        opts.minExpandLevel = level;
        opts.recursively = true;
        console.log("yaioOpenSubNodesForTree setExpanded:" + " level:" + level);
        treeNode.setExpanded(true, opts);
    };
    
    me.yaioSaveNode = function(data) {
        var json = JSON.stringify({name: data.input.val()});
        me.appBase.get('YaioNodeData').yaioDoUpdateNode(data.node, json);
    };
    
    me.yaioMoveNode = function(node, newParentKey, newPos) {
        console.log("move node:" + node.key + " to:" + newParentKey + " Pos:" + newPos);
        var json = JSON.stringify({parentNode: newParentKey});
        me.appBase.get('YaioNodeData').yaioDoMoveNode(node, newParentKey, newPos, json);
    };
    
    me.yaioRemoveNodeById = function(nodeId) {
        if (window.confirm("Wollen Sie die Node wirklich l&ouml;schen?")) {
            console.log("remove node:" + nodeId);
            // check for tree
            var treeId = "#tree";
            var tree = me.$(treeId).fancytree("getTree");
            if (! tree) {
                me.appBase.get('YaioBase').logError("yaioRemoveNode: error tree:'" + treeId + "' not found.", false);
                return;
            }
            
            // check for activeNodeId
            var treeNode = tree.getNodeByKey(nodeId);
            if (! treeNode) {
                me.appBase.get('YaioBase').logError("yaioRemoveNode: error for tree:'" + treeId 
                        + "' activeNode " + nodeId + " not found.", false);
                return null;
            }
            me.appBase.get('YaioNodeData').yaioDoRemoveNode(nodeId);
        } else {
            // discard
            return false;
        }
    };
    
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
            me.appBase.get('YaioBase').logError("error openJiraWindow: nodeId required", false);
            return null;
        }
        // load node
        var tree = me.$("#tree").fancytree("getTree");
        if (!tree) {
            // tree not found
            me.appBase.get('YaioBase').logError("error openJiraWindow: cant load tree for node:" + nodeId, false);
            return null;
        }
        var treeNode = tree.getNodeByKey(nodeId);
        if (! treeNode) {
            me.appBase.get('YaioBase').logError("error openJiraWindow: cant load node:" + nodeId, false);
            return null;
        }
        
        // extract nodedata
        var basenode = treeNode.data.basenode;
        var descText = basenode.nodeDesc;
        if (! descText) {
            descText = "";
        }
        descText = descText.replace(/<WLBR>/g, "\n");
        descText = descText.replace(/<WLESC>/g, "\\");
        descText = descText.replace(/<WLTAB>/g, "\t");
        
        // convert and secure
        var nodeDesc = me.appBase.get('YaioFormatter').convertMarkdownToJira(descText);
        nodeDesc = me.appBase.get('YaioBase').htmlEscapeText(nodeDesc);
        
        // set clipboard-content
        me.$( "#clipboard-content" ).html(nodeDesc);
        
        // show message
        me.$( "#clipboard-box" ).dialog({
            modal: true,
            width: "700px",
            buttons: {
              Ok: function() {
                me.$( this ).dialog( "close" );
              }
            }
        });    
    };
    
    
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
        var checkListSrc = me.appBase.get('YaioFormatter').convertExplorerLinesAsCheckList();
        checkListSrc = me.appBase.get('YaioBase').htmlEscapeText(checkListSrc);
        var ganttSrc = me.appBase.get('YaioFormatter').convertExplorerLinesAsGanttMarkdown();
        ganttSrc = me.appBase.get('YaioBase').htmlEscapeText(ganttSrc);
        
        // set clipboard-content
        me.$( "#clipboard-content" ).html(checkListSrc + "\n\n" +  ganttSrc);
        
        // show message
        me.$( "#clipboard-box" ).dialog({
            modal: true,
            width: "700px",
            buttons: {
              Ok: function() {
                me.$( this ).dialog( "close" );
              }
            }
        });    
    };
    
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
        var checkListSrc = me.appBase.get('YaioFormatter').convertExplorerLinesAsCheckList();
        checkListSrc = me.appBase.get('YaioBase').htmlEscapeText(checkListSrc);
        var ganttSrc = me.appBase.get('YaioFormatter').convertExplorerLinesAsGanttMarkdown();
        ganttSrc = me.appBase.get('YaioBase').htmlEscapeText(ganttSrc);
    
        // open editor
        me.appBase.get('YaioEditor').yaioOpenNodeEditorForNode(parentNode, 'createsnapshot', {nodeDesc: checkListSrc + "\n\n" +  ganttSrc});
    };
    
    
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
        content = me.appBase.get('YaioBase').htmlEscapeText(content);
    
        // set clipboard-content
        me.$( "#clipboard-content" ).html(content);
        
        // show message
        me.$( "#clipboard-box" ).dialog({
            modal: true,
            width: "700px",
            buttons: {
              Ok: function() {
                me.$( this ).dialog( "close" );
              }
            }
        });    
    };

    /**
     * <h4>FeatureDomain:</h4>
     *     Layout Toggler
     * <h4>FeatureDescription:</h4>
     *     Toggle the "#detail_desc_" for the specified id with a slide. 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>Updates DOM
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree Rendering
     * @param id - sysUID of the node 
     */
   me.toggleNodeDescContainer = function(id) {
        me.$("#detail_desc_" + id).slideToggle(1000,function() {
            // show/hide toggler
            if (me.$("#detail_desc_" + id).css("display") == "block") {
                // desc is now shown
                me.$("#toggler_desc_" + id).addClass('toggler_show').removeClass('toggler_hidden');
   
                // check if syntaxhighlighting to do
                var descBlock = me.$("#container_content_desc_" + id);
                if (me.$(descBlock).hasClass('syntaxhighlighting-open')) {
                    var flgDoMermaid = false;
                    console.log("toggleNodeDescContainer highlight for descBlock: " + me.$(descBlock).attr('id'));
                    flgDoMermaid = me.appBase.get('YaioFormatter').formatDescBlock(descBlock) || flgDoMermaid;
                    console.log("toggleNodeDescContainer resulting flgDoMermaid: " + flgDoMermaid);
                    
                    // do Mermaid if found
                    if (flgDoMermaid) {
                        me.appBase.get('YaioFormatter').formatMermaidGlobal();
                    }
                }
            } else {
                // desc is now hidden
                me.$("#toggler_desc_" + id).addClass('toggler_hidden').removeClass('toggler_show');
            }
        });
    };
   
    me.toggleAllNodeDescContainer = function() {
        if (me.$("#toggler_desc_all").hasClass('toggler_hidden')) {
            // show all desc
            me.$("div.field_nodeDesc").slideDown(1000);
            me.$("div.fieldtype_descToggler > a").addClass('toggler_show').removeClass('toggler_hidden');
   
            // check if syntaxhighlighting to do
            var flgDoMermaid = false;
            me.$("div.syntaxhighlighting-open").each(function (i, descBlock) {
                console.log("toggleAllNodeDescContainer highlight for descBlock: " + me.$(descBlock).attr('id'));
                flgDoMermaid = me.appBase.get('YaioFormatter').formatDescBlock(descBlock) || flgDoMermaid;
            });
            console.log("toggleAllNodeDescContainer resulting flgDoMermaid: " + flgDoMermaid);
           
            // mermaid all
            if (flgDoMermaid) {
               me.appBase.get('YaioFormatter').formatMermaidGlobal();
            }
        } else {
            // hide all desc
            me.$("div.field_nodeDesc").slideUp(1000);
            me.$("div.fieldtype_descToggler > a").addClass('toggler_hidden').removeClass('toggler_show');
        }
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Layout Toggler
     * <h4>FeatureDescription:</h4>
     *     Toggle the "#detail_sys_" for the specified id with a slide. 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>Updates DOM
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree Rendering
     * @param id - sysUID of the node 
    */
    me.toggleNodeSysContainer = function(id) {
        me.$("#detail_sys_" + id).slideToggle(1000,function() {
            // show/hide toggler
            if (me.$("#detail_sys_" + id).css("display") == "block") {
                // desc is now shown
                me.$("#toggler_sys_" + id).addClass('toggler_show').removeClass('toggler_hidden');
            } else {
                // desc is now hidden
                me.$("#toggler_sys_" + id).addClass('toggler_hidden').removeClass('toggler_show');
            }
        });
    };
  
    me.toggleAllNodeSysContainer = function() {
        if (me.$("#toggler_sys_all").hasClass('toggler_hidden')) {
            // show all sys
            me.$("div.field_nodeSys").slideDown(1000);
            me.$("div.fieldtype_sysToggler > a").addClass('toggler_show').removeClass('toggler_hidden');
        }  else {
            // hide all sys
            me.$("div.field_nodeSys").slideUp(1000);
            me.$("div.fieldtype_sysToggler > a").addClass('toggler_hidden').removeClass('toggler_show');
        }
    };
  
    me._init();
    
    return me;
};
