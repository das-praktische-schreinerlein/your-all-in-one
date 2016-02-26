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
 * servicefunctions for explorer-actions
 *
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.ExplorerAction = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * open node-hierarchy of treeId
     * @param {String} treeId           id of the fancytree
     * @param {Array} lstIdsHierarchy   node-hierarchy too open as list of node.id
     */
    me.openNodeHierarchyForTreeId = function(treeId, lstIdsHierarchy) {
        var svcLogger = me.appBase.Logger;

        // check for tree
        var tree = me.$(treeId).fancytree('getTree');
        if (! tree) {
            svcLogger.logError('openHierarchy: error tree:"' + treeId + '" not found.', false);
            return;
        }
    
        // check for rootNode
        var rootNode = tree.rootNode;
        if (! rootNode) {
            svcLogger.logError('openHierarchy: error for tree:"' + treeId
                        + '" rootNode not found.', false);
            return;
        }
        
        // check for lstIdsHierarchy
        if (! lstIdsHierarchy || lstIdsHierarchy.length <= 0) {
            svcLogger.logError('openHierarchy: error for tree:"' + treeId + '" lstIdsHierarchy is empty.', false);
            return;
        }
        
        // search for firstNode in rootTree, ignore if not found
        var firstNodeId, firstNode;
        var lstIdsHierarchySave = [].concat(lstIdsHierarchy);
        while (! firstNode && lstIdsHierarchy.length > 0) {
            firstNodeId = lstIdsHierarchy.shift();
            firstNode = rootNode.mapChildren[firstNodeId];
        }
        if (! firstNode) {
            svcLogger.logError('openHierarchy: error for tree:"' + treeId + '" firstNode of:"' + lstIdsHierarchySave + '"'
                        + ' not found on rootNode.', false);
            return;
        }
    
        // open Hierarchy
        var opts = {};
        opts.openHierarchy = lstIdsHierarchy;
        opts.activateLastNode = true;
        firstNode.setExpanded(true, opts);
    };

    /**
     * open node-hierarchy of treeId till level
     * @param {String} treeId     id of the fancytree
     * @param {int} level         open till level
     */
    me.openSubNodesForTreeId = function(treeId, level) {
        var svcLogger = me.appBase.Logger;

        var tree = me.$(treeId).fancytree('getTree');
        if (! tree) {
            svcLogger.logError('yaioOpenSubNodesForTree: error tree:"' + treeId + '" not found.', false);
            return;
        }
        
        // check for activeNodeId
        var treeNode = tree.rootNode;
        if (! treeNode) {
            svcLogger.logError('yaioOpenSubNodesForTree: error rootnode for tree:"' + treeId + '" not found.', false);
            return null;
        }
        
        var opts = {};
        opts.minExpandLevel = level;
        opts.recursively = true;
        console.log('yaioOpenSubNodesForTree setExpanded:' + ' level:' + level);
        treeNode.setExpanded(true, opts);
    };

    /**
     * move the node as new child of parent
     * @param {Object} node           node to copy
     * @param {String} newParentKey   key of the new parentNode to add a copy of the node as child
     * @param {int} newPos            position to move node to
     */
    me.doMoveNode = function(node, newParentKey, newPos) {
        console.log('move node:' + node.key + ' to:' + newParentKey + ' Pos:' + newPos);
        me.appBase.YaioNodeData.moveNode(node.key, newParentKey, newPos)
            .done(function(yaioNodeActionResponse, textStatus, jqXhr ) {
                me.patchNodeSuccessHandler(node.key, yaioNodeActionResponse, textStatus, jqXhr);
            });
    };

    /**
     * make a copy of the node as new child of parent
     * @param {Object} node           node to copy
     * @param {String} newParentKey   key of the new parentNode to add a copy of the node as child
     */
    me.doCopyNode = function(node, newParentKey) {
        console.log('copy node:' + node.key + ' to:' + newParentKey);
        me.appBase.YaioNodeData.copyNode(node.key, newParentKey)
            .done(function(yaioNodeActionResponse, textStatus, jqXhr ) {
                me.patchNodeSuccessHandler(node.key, yaioNodeActionResponse, textStatus, jqXhr);
            });
    };

    /**
     * success-handler if patchNode succeeded (resolves yaioNodeActionResponse.state)
     * @param {String} nodeId                       nodeId to patch
     * @param {Object} yaioNodeActionResponse       the serverresponse (java de.yaio.rest.controller.NodeActionReponse)
     * @param {String} textStatus                   http-state as text
     * @param {JQueryXHR} jqXhr                     jqXhr-Object
     */
    me.patchNodeSuccessHandler = function(nodeId, yaioNodeActionResponse, textStatus, jqXhr) {
        var svcLogger = me.appBase.get('Logger');
        var msg = '_patchNodeSuccessHandler for nodeId:' + nodeId;
        console.log(msg + ' OK done!' + yaioNodeActionResponse.state);
        if (yaioNodeActionResponse.state === 'OK') {
            console.log(msg + ' OK saved nodeId:' + nodeId + ' load:' + yaioNodeActionResponse.parentIdHierarchy);
            if (yaioNodeActionResponse.parentIdHierarchy && yaioNodeActionResponse.parentIdHierarchy.length > 0) {
                // reload tree
                var tree = me.$('#tree').fancytree('getTree');
                tree.reload().done(function(){
                    // handler when done
                    console.log(msg + ' RELOAD tree done:' + yaioNodeActionResponse.parentIdHierarchy);
                    console.log(msg + ' CALL openNodeHierarchy load hierarchy:' + yaioNodeActionResponse.parentIdHierarchy);
                    me.appBase.get('YaioExplorerAction').openNodeHierarchyForTreeId('#tree', yaioNodeActionResponse.parentIdHierarchy);
                });
            } else {
                svcLogger.logError('got no hierarchy for:' + nodeId
                    + ' hierarchy:' + yaioNodeActionResponse.parentIdHierarchy, true);
            }
        } else {
            var message = 'cant save nodeId:' + nodeId + ' error:' + yaioNodeActionResponse.stateMsg;
            // check for violations
            if (yaioNodeActionResponse.violations) {
                // iterate violations
                message = message +  ' violations: ';
                for (var idx in yaioNodeActionResponse.violations) {
                    if (!yaioNodeActionResponse.violations.hasOwnProperty(idx)) {
                        continue;
                    }
                    var violation = yaioNodeActionResponse.violations[idx];
                    svcLogger.logError('violations while save nodeId:' + nodeId
                        + ' field:' + violation.path + ' message:' + violation.message, false);
                    message = message +  violation.path + ' (' + violation.message + '),';
                }
            }
            svcLogger.logError(message, true);
        }
    };


    /**
     * open confirmbox and remove node if confirmed
     * @param {String} nodeId    id of the node to delete
     */
    me.doRemoveNodeByNodeId = function(nodeId) {
        var svcLogger = me.appBase.Logger;

        if (window.confirm('Wollen Sie die Node wirklich l&ouml;schen?')) {
            console.log('remove node:' + nodeId);
            // check for tree
            var treeId = '#tree';
            var tree = me.$(treeId).fancytree('getTree');
            if (! tree) {
                svcLogger.logError('yaioRemoveNode: error tree:"' + treeId + '" not found.', false);
                return;
            }
            
            // check for activeNodeId
            var treeNode = tree.getNodeByKey(nodeId);
            if (! treeNode) {
                svcLogger.logError('yaioRemoveNode: error for tree:"' + treeId + '"' +
                    ' activeNode ' + nodeId + ' not found.', false);
                return null;
            }
            me.appBase.YaioNodeData.deleteNode(nodeId)
                .done(function(yaioNodeActionResponse, textStatus, jqXhr ) {
                    me.deleteNodeSuccessHandler(nodeId, yaioNodeActionResponse, textStatus, jqXhr);
                });
        } else {
            // discard
            return false;
        }
    };

    /**
     * success-handler if deleteNode succeeded (resolves yaioNodeActionResponse.state)
     * @param {String} nodeId                       nodeId to patch
     * @param {Object} yaioNodeActionResponse       the serverresponse (java de.yaio.rest.controller.NodeActionReponse)
     * @param {String} textStatus                   http-state as text
     * @param {JQueryXHR} jqXhr                     jqXhr-Object
     */
    me.deleteNodeSuccessHandler = function(nodeId, yaioNodeActionResponse, textStatus, jqXhr) {
        var svcLogger = me.appBase.get('Logger');
        var msg = '_deleteNodeSuccessHandler for nodeId:' + nodeId;
        console.log(msg + ' OK done!' + yaioNodeActionResponse.state);
        if (yaioNodeActionResponse.state === 'OK') {
            console.log(msg + ' OK removed node:' + nodeId + ' load:' + yaioNodeActionResponse.parentIdHierarchy);
            if (yaioNodeActionResponse.parentIdHierarchy && yaioNodeActionResponse.parentIdHierarchy.length >= 0) {
                // reload tree
                var tree = me.$('#tree').fancytree('getTree');
                tree.reload().done(function(){
                    // handler when done
                    console.log(msg + ' RELOAD tree done:' + yaioNodeActionResponse.parentIdHierarchy);
                    console.log(msg + ' CALL openNodeHierarchy load hierarchy:' + yaioNodeActionResponse.parentIdHierarchy);
                    me.appBase.get('YaioExplorerAction').openNodeHierarchyForTreeId('#tree', yaioNodeActionResponse.parentIdHierarchy);
                });
            } else {
                svcLogger.logError('got no hierarchy for:' + nodeId
                    + ' hierarchy:' + yaioNodeActionResponse.parentIdHierarchy, true);
            }
        } else {
            svcLogger.logError('cant remove node:' + nodeId + ' error:' + yaioNodeActionResponse.stateMsg, false);
            // check for violations
            if (yaioNodeActionResponse.violations) {
                // iterate violations
                for (var idx in yaioNodeActionResponse.violations) {
                    if (!yaioNodeActionResponse.violations.hasOwnProperty(idx)) {
                        continue;
                    }
                    var violation = yaioNodeActionResponse.violations[idx];
                    svcLogger.logError('violations while remove node:' + nodeId
                        + ' field:' + violation.path + ' message:' + violation.message, false);
                    window.alert('cant remove node because: ' + violation.path + ' (' + violation.message + ')');
                }
            }
        }
    };



    /** 
     * opens jira window with jira-converted node-content
     * @param {String} nodeId                 id of the node
     */
    me.openJiraExportWindowByNodeId = function(nodeId) {
        var svcLogger = me.appBase.Logger;
        var svcDataUtils = me.appBase.DataUtils;

        // check vars
        if (! nodeId) {
            // tree not found
            svcLogger.logError('error openJiraWindow: nodeId required', false);
            return null;
        }
        // load node
        var tree = me.$('#tree').fancytree('getTree');
        if (!tree) {
            // tree not found
            svcLogger.logError('error openJiraWindow: cant load tree for node:' + nodeId, false);
            return null;
        }
        var treeNode = tree.getNodeByKey(nodeId);
        if (! treeNode) {
            svcLogger.logError('error openJiraWindow: cant load node:' + nodeId, false);
            return null;
        }
        
        // extract nodedata
        var basenode = treeNode.data.basenode;
        var descText = basenode.nodeDesc;
        if (! descText) {
            descText = '';
        }
        descText = descText.replace(/<WLBR>/g, '\n');
        descText = descText.replace(/<WLESC>/g, '\\');
        descText = descText.replace(/<WLTAB>/g, '\t');
        
        // convert and secure
        var nodeDesc = me.appBase.YaioMarkdownConverter.convertMarkdownToJira(descText);
        nodeDesc = svcDataUtils.htmlEscapeText(nodeDesc);
        
        // set clipboard-content
        me.$( '#clipboard-content' ).html(nodeDesc);
        
        // show message
        me.$( '#clipboard-box' ).dialog({
            modal: true,
            width: '700px',
            buttons: {
              Ok: function() {
                me.$( this ).dialog( 'close' );
              }
            }
        });    
    };
    
    
    /** 
     * opens clipboard window with checklist/ganttmarkdown-converted node-content
     */
    me.openClipBoardWithCurrentViewAsOverview = function() {
        var svcDataUtils = me.appBase.DataUtils;
        var svcYaioExplorerConverter = me.appBase.YaioExplorerConverter;

        // convert and secure
        var checkListSrc = svcYaioExplorerConverter.convertExplorerLinesAsCheckList();
        checkListSrc = svcDataUtils.htmlEscapeText(checkListSrc);
        var ganttSrc = svcYaioExplorerConverter.convertExplorerLinesAsGanttMarkdown();
        ganttSrc = svcDataUtils.htmlEscapeText(ganttSrc);
        
        // set clipboard-content
        me.$( '#clipboard-content' ).html(checkListSrc + '\n\n' +  ganttSrc);
        
        // show message
        me.$( '#clipboard-box' ).dialog({
            modal: true,
            width: '700px',
            buttons: {
              Ok: function() {
                me.$( this ).dialog( 'close' );
              }
            }
        });    
    };
    
    /** 
     * open the nodeeditor with a new infornode with snaphot of current gui: checklist and gantt-markdown
     * @param {Object} parentNode     parentNode to get the content from
     */
    me.openNewInfoNodeWithCurrentViewAsSnapshotForParent = function(parentNode) {
        var svcDataUtils = me.appBase.DataUtils;
        var svcYaioExplorerConverter = me.appBase.YaioExplorerConverter;

        // convert and secure
        var checkListSrc = svcYaioExplorerConverter.convertExplorerLinesAsCheckList();
        checkListSrc = svcDataUtils.htmlEscapeText(checkListSrc);
        var ganttSrc = svcYaioExplorerConverter.convertExplorerLinesAsGanttMarkdown();
        ganttSrc = svcDataUtils.htmlEscapeText(ganttSrc);
    
        // open editor
        me.appBase.YaioEditor.yaioOpenNodeEditorForNode(parentNode, 'createsnapshot', {nodeDesc: checkListSrc + '\n\n' +  ganttSrc});
    };
    
    
    /** 
     * opens txt-window with txt node-content
     * @param {String} content                txt content
     */
    me.openTxtExportWindowForContent = function(content) {
        // secure
        content = me.appBase.DataUtils.htmlEscapeText(content);
    
        // set clipboard-content
        me.$( '#clipboard-content' ).html(content);
        
        // show message
        me.$( '#clipboard-box' ).dialog({
            modal: true,
            width: '700px',
            buttons: {
              Ok: function() {
                me.$( this ).dialog( 'close' );
              }
            }
        });    
    };

    /** 
     * open the dmsdownloadwindow for the node  
     * @param {String} nodeId                 id of the node
     */
    me.openDMSDownloadWindowForNodeId = function(nodeId) {
        var svcLogger = me.appBase.Logger;

        // check vars
        if (! nodeId) {
            // tree not found
            svcLogger.logError('error openDMSDownloadExportWindow: nodeId required', false);
            return null;
        }
        // load node
        var tree = me.$('#tree').fancytree('getTree');
        if (!tree) {
            // tree not found
            svcLogger.logError('error openDMSDownloadExportWindow: cant load tree for node:' + nodeId, false);
            return null;
        }
        var treeNode = tree.getNodeByKey(nodeId);
        if (! treeNode) {
            svcLogger.logError('error openDMSDownloadExportWindow: cant load node:' + nodeId, false);
            return null;
        }
        
        // extract nodedata
        var basenode = treeNode.data.basenode;
        var embedUrl = me.appBase.YaioAccessManager.getAvailiableNodeAction('dmsEmbed', basenode.sysUID, false) + basenode.sysUID;
        var downloadUrl = me.appBase.YaioAccessManager.getAvailiableNodeAction('dmsDownload', basenode.sysUID, false) + basenode.sysUID;

        // set clipboard-content
        me.$( '#download-iframe' ).attr('src', embedUrl);
        
        // show message
        me.$( '#download-box' ).dialog({
            modal: true,
            width: '700px',
            buttons: {
              Ok: function() {
                me.$( this ).dialog( 'close' );
              },
              'Download': function() {
                var helpFenster = window.open(downloadUrl, 'download', 'width=200,height=200,scrollbars=yes,resizable=yes');
                helpFenster.focus();
              }
            }
        });    
    };
    
    /** 
     * open the dmsdownloadwindow for the extracted metadata of the node document 
     * @param {String} nodeId                 id of the node
     */
    me.openDMSIndexDownloadWindowForNodeId = function(nodeId) {
        var svcLogger = me.appBase.Logger;

        // check vars
        if (! nodeId) {
            // tree not found
            svcLogger.logError('error openDMSIndexDownloadWindow: nodeId required', false);
            return null;
        }
        // load node
        var tree = me.$('#tree').fancytree('getTree');
        if (!tree) {
            // tree not found
            svcLogger.logError('error openDMSIndexDownloadWindow: cant load tree for node:' + nodeId, false);
            return null;
        }
        var treeNode = tree.getNodeByKey(nodeId);
        if (! treeNode) {
            svcLogger.logError('error openDMSIndexDownloadWindow: cant load node:' + nodeId, false);
            return null;
        }
        
        // extract nodedata
        var basenode = treeNode.data.basenode;
        var embedUrl = me.appBase.YaioAccessManager.getAvailiableNodeAction('dmsIndexEmbed', basenode.sysUID, false) + basenode.sysUID;
        var downloadUrl = me.appBase.YaioAccessManager.getAvailiableNodeAction('dmsIndexDownload', basenode.sysUID, false) + basenode.sysUID;

        $.getJSON( embedUrl, function(data) {
            // set clipboard-content
            var parent = me.$( '#downloadindex-content' );
            parent.html('');
            for (var key in data.versions) {
                if (data.versions.hasOwnProperty(key)) {
                    me.createDMSIndexDiv(key, data.versions[key], parent);
                }
            }
        });
        
        
        // show message
        me.$( '#downloadindex-box' ).dialog({
            modal: true,
            width: '700px',
            buttons: {
              Ok: function() {
                me.$( this ).dialog( 'close' );
              },
              'Download': function() {
                var helpFenster = window.open(downloadUrl, 'download', 'width=200,height=200,scrollbars=yes,resizable=yes');
                helpFenster.focus();
              }
            }
        });    
    };

    /**
     * create the dmsdownloadwindow for the extracted metadata of the node document
     * @param {String} key                  id of the
     * @param {Object} data                 data to show
     * @param {String} parent               JQuery-Selector to append the download-window
     */
    me.createDMSIndexDiv = function (key, data, parent) {
        var content = '' + data.content;
        var name = '' + data.parserName;
        content = me.appBase.DataUtils.htmlEscapeText(content);
        content = content.replace(/\n/g, '<br />');
        $(parent).append('<div class="downloadindex-container"><div class="downloadindex-name">' + name + '</div><br><pre>' + content + '<pre></div>');
    };

    /** 
     * Toggle the '#detail_desc_' for the specified id with a slide - updates DOM
     * @param {String} id                     sysUID of the node
     */
    me.toggleNodeDescContainerForNodeId = function(id) {
        var svcYaioFormatter = me.appBase.YaioFormatter;
        me.$('#detail_desc_' + id).slideToggle(1000,function() {
            // show/hide toggler
            if (me.$('#detail_desc_' + id).css('display') === 'block') {
                // desc is now shown
                me.$('#toggler_desc_' + id).addClass('toggler_show').removeClass('toggler_hidden');
   
                // check if syntaxhighlighting to do
                var descBlock = me.$('#container_content_desc_' + id);
                if (me.$(descBlock).hasClass('syntaxhighlighting-open')) {
                    me.$(descBlock).removeClass('syntaxhighlighting-open');
                    console.log('toggleNodeDescContainer highlight for descBlock: ' + me.$(descBlock).attr('id'));
                    svcYaioFormatter.runAllRendererOnBlock(descBlock);
                }
            } else {
                // desc is now hidden
                me.$('#toggler_desc_' + id).addClass('toggler_hidden').removeClass('toggler_show');
            }
        });
    };

    /**
     * Toggle all desc-divs for nodes with '#detail_desc_' depending on state of #toggler_desc_all with a slide.
     */
    me.toggleAllNodeDescContainer = function() {
        if (me.$('#toggler_desc_all').hasClass('toggler_hidden')) {
            // show all desc
            me.$('div.field_nodeDesc').slideDown(1000);
            me.$('div.fieldtype_descToggler > a').addClass('toggler_show').removeClass('toggler_hidden');
   
            // check if syntaxhighlighting to do
            me.$('div.syntaxhighlighting-open').each(function (i, descBlock) {
                console.log('toggleAllNodeDescContainer highlight for descBlock: ' + me.$(descBlock).attr('id'));
                me.appBase.YaioFormatter.runAllRendererOnBlock(descBlock);
            });
        } else {
            // hide all desc
            me.$('div.field_nodeDesc').slideUp(1000);
            me.$('div.fieldtype_descToggler > a').addClass('toggler_hidden').removeClass('toggler_show');
        }
    };
    
    /** 
     * Toggle the '#detail_sys_' for the specified id with a slide.
     * @param {String} id                     sysUID of the node
    */
    me.toggleNodeSysContainerForNodeId = function(id) {
        me.$('#detail_sys_' + id).slideToggle(1000,function() {
            // show/hide toggler
            if (me.$('#detail_sys_' + id).css('display') === 'block') {
                // desc is now shown
                me.$('#toggler_sys_' + id).addClass('toggler_show').removeClass('toggler_hidden');
            } else {
                // desc is now hidden
                me.$('#toggler_sys_' + id).addClass('toggler_hidden').removeClass('toggler_show');
            }
        });
    };

    /**
     * Toggle all sys-divs for nodes with '#detail_sys_' depending on state of #toggler_sys_all with a slide.
     */
    me.toggleAllNodeSysContainer = function() {
        if (me.$('#toggler_sys_all').hasClass('toggler_hidden')) {
            // show all sys
            me.$('div.field_nodeSys').slideDown(1000);
            me.$('div.fieldtype_sysToggler > a').addClass('toggler_show').removeClass('toggler_hidden');
        }  else {
            // hide all sys
            me.$('div.field_nodeSys').slideUp(1000);
            me.$('div.fieldtype_sysToggler > a').addClass('toggler_hidden').removeClass('toggler_show');
        }
    };
  
    me._init();
    
    return me;
};
