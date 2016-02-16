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
 * controller for the treeview
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */


/*****************************************
 *****************************************
 * Configuration
 *****************************************
 *****************************************/
var treeInstances = new Array();

/*****************************************
 *****************************************
 * YAIO-Treefunctions
 *****************************************
 *****************************************/
Yaio.ExplorerTree = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me.clipboardNode = null;
    me.pasteMode = null;
    me.nodeFilter = {};
    me._init = function() {
    };

    me.sourceHandler = function(nodeId) {
        return me.appBase.get('YaioNodeData').loadNodeData(nodeId);
    };

    /** 
     * creates an fancytree on the html-element treeId and inits it with the data
     * of masterNodeId<br>
     * after init of the tree the doneHandler will be executed  
     * @FeatureDomain                Initialisation
     * @FeatureResult                updates html-element treeId with a fancytree
     * @FeatureResult                calls doneHandler
     * @FeatureResult                updates global var treeInstances[treeId]
     * @FeatureKeywords              GUI Tree
     * @param treeId                 id of the html-element containing the tree
     * @param masterNodeId           the node.sysUID to load
     * @param doneHandler            callback-function when tree is created
     */
    me.yaioCreateFancyTree = function(treeId, masterNodeId, doneHandler) {
        treeInstances[treeId] = {};
        treeInstances[treeId].state = 'loading';
        me.$(treeId).fancytree({
            
            // errorHandler
            loadError: function (e,data) { 
                me.appBase.get('YaioExplorerTree').yaioFancyTreeLoadError(e, data); 
            },
            
            // save masterNodeId
            masterNodeId: masterNodeId,
            
            // set layoutoptions
            maxTitleWidth: 400,
            
            checkbox: true,
            titlesTabbable: true,     // Add all node titles to TAB chain
      
            source: me.sourceHandler(masterNodeId),
            
            // defaultoptions for ajax-request
            ajax: {
                xhrFields : {
                    // for CORS
                    withCredentials : true
                }
            },
          
            // set state of the tree for callback, when tree is created
            loadChildren: function(event, data) {
                treeInstances[treeId].state = 'loading_done';
            },    
    
            // lazy load the children
            lazyLoad: function(event, data) {
                var node = data.node;
                console.debug('yaioCreateFancyTree load data for ' + node.key);
                data.result = me.sourceHandler(node.key);
            },
      
            // callback if expanded-state of node changed, to show the matching gantt (only node or + childsum)
            onExpandCallBack: function (node, flag) {
                // activate/deactivate gantt for node
                if (flag) {
                    console.debug('onExpandCallBack: activate gantt - only own data for ' + node.key);
                    me.appBase.get('YaioNodeGanttRender').yaioActivateGanttBlock(node, true);
                } else {
                    // I'm collapsed: show me and my childsum
                    console.debug('onExpandCallBack: activate gantt - sum data of me+children for ' + node.key);
                    me.appBase.get('YaioNodeGanttRender').yaioActivateGanttBlock(node, false);
                }
            },
            
            // parse the nodedata
            postProcess: function(event, data) {
                me.appBase.get('YaioExplorerTree').postProcessNodeData(event, data);
            },
    
            // render the extra nodedata in grid, sets state for callbaclfunction when tree is created
            renderColumns: function(event, data) {
                me.appBase.get('YaioNodeDataRender').renderColumnsForNode(event, data);
                treeInstances[treeId].state = 'rendering_done';
            },
    
            // extensions: ['edit', 'table', 'gridnav'],
            extensions: ['edit', 'dnd', 'table', 'gridnav'],
    
            dnd: {
                preventVoidMoves: true,
                preventRecursiveMoves: true,
                autoExpandMS: 400,
                dragStart: function(node, data) {
                    return true;
                },
                dragEnter: function(node, data) {
                    // return ['before', 'after'];
                    return true;
                },
                dragDrop: function(node, data) {
                    // check permission
                    if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('move', node.key, false)) {
                        return false;
                    }
                    if (window.confirm('Wollen Sie die Node wirklich verschieben?')) {
                        data.otherNode.moveTo(node, data.hitMode);
    
                        // check parent of the node
                        var newPos = data.otherNode.data.basenode.sortPos;
                        var newParent = node.getParent();
                        var newParentKey = node.getParent().key;
                        if (newParent.isRootNode() || me.appBase.DataUtils.isUndefinedStringValue(newParentKey) || !newParent) {
                            newParentKey = node.tree.options.masterNodeId;
                        }
                        switch( data.hitMode){
                        case 'before':
                            newPos = node.data.basenode.sortPos - 2;
                            break;
                        case 'after':
                            newPos = node.data.basenode.sortPos + 2;
                            break;
                        default:
                            // add it to the current node
                            newParentKey = node.key;
                            newPos = 9999;
                        }
                        me.appBase.get('YaioExplorerAction').yaioMoveNode(data.otherNode, newParentKey, newPos);
                        return true;
                    } else {
                        // discard
                        return false;
                    }
                }
            },
            edit: {
                triggerStart: ['f2', 'dblclick', 'shift+click', 'mac+enter'],
                beforeEdit: function(event, data){
                    // check permission
                    if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('edit', data.node.key, false)) {
                        return false;
                    }
                    // open yaio-editor
                    me.appBase.get('YaioEditor').yaioOpenNodeEditor(data.node.key, 'edit');
                    
                    // Return false to prevent edit mode
                    // dont use fancyeditor
                    return false;
                },
                edit: function(event, data){
                    // unused because we use the yaioeditor
                    
                    // Editor was opened (available as data.input)
                },
                beforeClose: function(event, data){
                    // unused because we use the yaioeditor
    
                    // Return false to prevent cancel/save (data.input is available)
                },
                save: function(event, data){
                    // unused because we use the yaioeditor
    
//                    if (window.confirm('Wollen Sie den Titel wirklich ändern?')) {
//                        // Save data.input.val() or return false to keep editor open
//                        me.appBase.get('YaioExplorerAction').yaioSaveNode(data);
//                        // We return true, so ext-edit will set the current user input
//                        // as title
//                        return true;
//                    } else {
//                        // discard
//                        return false;
//                    }
                },
                close: function(event, data){
                    // unused because we use the yaioeditor
    
                    // Editor was removed
                    if( data.save ) {
                        // Since we started an async request, mark the node as preliminary
                        me.$(data.node.span).addClass('pending');
                    }
                }
            },
            table: {
                indentation: 20,
                nodeColumnIdx: 0
    //            checkboxColumnIdx: 0
            },
            gridnav: {
                autofocusInput: false,
                handleCursorKeys: true
            }
            /**
            click: function(event, data) {
                var node = data.node,
                    tt = me.$.ui.fancytree.getEventTargetType(event.originalEvent);
                if (tt == undefined) {
                    return true;
                }
            },
    **/
        }).on('nodeCommand', function(event, data){
            var svcYaioExplorerAction = me.appBase.get('YaioExplorerAction');
            
            // Custom event handler that is triggered by keydown-handler and
            // context menu:
            var refNode,
                tree = me.$(this).fancytree('getTree'),
                node = tree.getActiveNode();
        
            switch( data.cmd ) {
                case 'edit':
                    // check permission
                    if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('edit', node.key, false)) {
                        return false;
                    }
                    // open yaio-editor
                    me.appBase.get('YaioEditor').yaioOpenNodeEditor(node.key, 'edit');
                    return true;
                case 'cut':
                    if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('move', node.key, false)) {
                        return false;
                    }
                    me.clipboardNode = node;
                    me.pasteMode = data.cmd;
                    break;
                case 'copy':
                    if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('copy', node.key, false)) {
                        return false;
                    }
                    me.clipboardNode = node;
                    me.pasteMode = data.cmd;
                    break;
                case 'paste':
                    if (!me.clipboardNode ) {
                        me.appBase.get('Logger').logError('Clipoard is empty.', true);
                        break;
                    }
                    var newParent = node;
                    var node = me.clipboardNode.toDict(true);
                    if (me.pasteMode === 'cut' ) {
                        if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('move', node.key, false)) {
                            return false;
                        }
                        // Cut mode: check for recursion and remove source
                        if(newParent.isDescendantOf(node) ) {
                            me.appBase.get('Logger').logError('Cannot move a node to it\'s sub node.', true);
                            return;
                        }
                        if (window.confirm('Wollen Sie die Node und Ihre Subnodes wirklich hierher verschieben?')) {
                            // map rootnode to masterNodeId 
                            var newParentKey = newParent.key;
                            if (newParent.isRootNode() || me.appBase.DataUtils.isUndefinedStringValue(newParentKey) || !newParent) {
                                newParentKey = tree.options.masterNodeId;
                            }
                            // move yaioNode
                            svcYaioExplorerAction.yaioMoveNode(node, newParentKey, 9999);
                            me.clipboardNode = me.pasteMode = null;
                            return true;
                        } else {
                            // discard
                            return false;
                        }
                    } else {
                        // Copy mode: prevent duplicate keys:
                        if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('copy', node.key, false)) {
                            return false;
                        }
                        if(newParent.isDescendantOf(node) ) {
                            me.appBase.get('Logger').logError('Cannot copy a node to it\'s sub node.', true);
                            return;
                        }
                        if (window.confirm('Wollen Sie die Node und Ihre Subnodes wirklich hierher kopieren?')) {
                            // map rootnode to masterNodeId 
                            var newParentKey = newParent.key;
                            if (newParent.isRootNode() || me.appBase.DataUtils.isUndefinedStringValue(newParentKey) || !newParent) {
                                newParentKey = tree.options.masterNodeId;
                            }
                            // copy yaioNode
                            svcYaioExplorerAction.yaioCopyNode(node, newParentKey);
                            me.clipboardNode = me.pasteMode = null;
                            return true;
                        } else {
                            // discard
                            return false;
                        }
                    }
                    break;
                case 'indent':
                    if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('move', node.key, false)) {
                        return false;
                    }
                    if (window.confirm('Wollen Sie die Node wirklich verschieben?')) {
                        // move fancynode
                        refNode = node.getPrevSibling();
                        
                        // map rootnode to masterNodeId 
                        var newParentKey = refNode.key;
                        if (refNode.isRootNode()) {
                            newParentKey = tree.options.masterNodeId;
                        }
                        
                        // move yaioNode
                        svcYaioExplorerAction.yaioMoveNode(node, newParentKey, 9999);
                        return true;
                    } else {
                        // discard
                        return false;
                    }
                    
                    break;
                case 'outdent':
                    if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('move', node.key, false)) {
                        return false;
                    }
                    if (window.confirm('Wollen Sie die Node wirklich verschieben?')) {
                        // move fancynode
                        var newParent = node.getParent().getParent();
                        
                        // map rootnode to masterNodeId 
                        var newParentKey = newParent.key;
                        if (newParent.isRootNode() || me.appBase.DataUtils.isUndefinedStringValue(newParentKey) || !newParent) {
                            newParentKey = tree.options.masterNodeId;
                        }
                        // move yaioNode
                        svcYaioExplorerAction.yaioMoveNode(node, newParentKey, 9999);
                        return true;
                    } else {
                        // discard
                        return false;
                    }
                    break;
                case 'moveUp':
                    if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('move', node.key, false)) {
                        return false;
                    }
                    // check parent
                    var newParent = node.getParent();
                    var newParentKey = node.getParent().key;
                    if (newParent.isRootNode() || me.appBase.DataUtils.isUndefinedStringValue(newParentKey) || !newParent) {
                        newParentKey = tree.options.masterNodeId;
                    }
                    // calc new position
                    var newPos = -2;
                    if (!me.appBase.DataUtils.isUndefined(node.getPrevSibling())) {
                        newPos = node.getPrevSibling().data.basenode.sortPos - 2;
                    }
    
                    svcYaioExplorerAction.yaioMoveNode(node, newParentKey, newPos);
                    break;
                case 'moveDown':
                    if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('move', node.key, false)) {
                        return false;
                    }
                    // check parent
                    var newParent = node.getParent();
                    var newParentKey = node.getParent().key;
                    if (newParent.isRootNode() || me.appBase.DataUtils.isUndefinedStringValue(newParentKey) || !newParent) {
                        newParentKey = tree.options.masterNodeId;
                    }
                    // calc new position
                    var newPos = 9999;
                    if (!me.appBase.DataUtils.isUndefined(node.getNextSibling())) {
                        newPos = node.getNextSibling().data.basenode.sortPos + 2;
                    }
    
                    svcYaioExplorerAction.yaioMoveNode(node, newParentKey, newPos);
                    break;
                case 'remove':
                    if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('remove', node.key, false)) {
                        return false;
                    }
                    svcYaioExplorerAction.yaioRemoveNodeById(node.key);
                    break;
                case 'addChild':
                    if (! me.appBase.get('YaioAccessManager').getAvailiableNodeAction('create', node.key, false)) {
                        return false;
                    }
                    me.appBase.get('YaioEditor').yaioOpenNodeEditor(node.key, 'create');
                    break;
                case 'asTxt':
                    svcYaioExplorerAction.openTxtExportWindow(me.$('#container_content_desc_' + node.key).text());
                    break;
                case 'asJira':
                    svcYaioExplorerAction.openJiraExportWindow(node.key);
                    break;
                case 'focus':
                    window.location = '#/show/' + node.key;
                    break;
                case 'focusNewWindow':
                    window.open('#/show/' + node.key, '_blank');
                    break;
                default:
                    window.alert('Unhandled command: ' + data.cmd);
                    return;
            }
      
        }).on('keydown', function(e){
            var c = String.fromCharCode(e.which),
                cmd = null;
        
            if( c === 'N' && e.ctrlKey && e.shiftKey) {
                cmd = 'addChild';
            } else if( e.which === me.$.ui.keyCode.DELETE ) {
                cmd = 'remove';
            } else if( e.which === me.$.ui.keyCode.F2 ) {
                cmd = 'rename';
            } else if( e.which === me.$.ui.keyCode.UP && e.ctrlKey ) {
                cmd = 'moveUp';
            } else if( e.which === me.$.ui.keyCode.DOWN && e.ctrlKey ) {
                cmd = 'moveDown';
            } else if( e.which === me.$.ui.keyCode.RIGHT && e.ctrlKey ) {
                cmd = 'indent';
            } else if( e.which === me.$.ui.keyCode.LEFT && e.ctrlKey ) {
                cmd = 'outdent';
            }
            if( cmd ){
                me.$(this).trigger('nodeCommand', {cmd: cmd});
                return false;
            }
        });
        
        // check if donehandler
        if (doneHandler) {
            me.appBase.get('YaioExplorerTree').yaioDoOnFancyTreeState(treeId, 
                    'rendering_done', 1000, 5, doneHandler, 'yaioCreateFancyTree.doneHandler');
        }
    
        /*
         * Context menu (https://github.com/mar10/jquery-ui-contextmenu)
         */
        me.$(treeId).contextmenu({
            delegate: 'span.fancytree-node',
            menu: [
                {title: 'Bearbeiten <kbd>[F2]</kbd>', cmd: 'edit', uiIcon: 'ui-icon-pencil', disabled: true },
                {title: 'Löschen <kbd>[Del]</kbd>', cmd: 'remove', uiIcon: 'ui-icon-trash', disabled: true },
                {title: '----'},
    //            {title: 'New sibling <kbd>[Ctrl+N]</kbd>', cmd: 'addSibling', uiIcon: 'ui-icon-plus' },
                {title: 'Kind zeugen', cmd: 'addChild', uiIcon: 'ui-icon-plus', disabled: true},
                {title: '----'},
                {title: 'Focus', cmd: 'focus', uiIcon: 'ui-icon-arrowreturn-1-e' },
                {title: 'In neuem Fenster', cmd: 'focusNewWindow', uiIcon: 'ui-icon-arrowreturn-1-e' },
                {title: 'Export Jira', cmd: 'asJira', uiIcon: 'ui-icon-clipboard' },
                {title: 'Export Txt', cmd: 'asTxt', uiIcon: 'ui-icon-clipboard' },
                {title: '----'},
                {title: 'Cut <kbd>Ctrl+X</kbd>', cmd: 'cut', uiIcon: 'ui-icon-scissors', disabled: true},
                {title: 'Copy <kbd>Ctrl-C</kbd>', cmd: 'copy', uiIcon: 'ui-icon-copy', disabled: true},
                {title: 'Paste as child<kbd>Ctrl+V</kbd>', cmd: 'paste', uiIcon: 'ui-icon-clipboard', disabled: true}
              ],
            beforeOpen: function(event, ui) {
                var node = me.$.ui.fancytree.getNode(ui.target);
                me.$('#tree').contextmenu('enableEntry', 'edit', !!me.appBase.get('YaioAccessManager').getAvailiableNodeAction('edit', node.key, false));
                me.$('#tree').contextmenu('enableEntry', 'remove', !!me.appBase.get('YaioAccessManager').getAvailiableNodeAction('remove', node.key, false));
                me.$('#tree').contextmenu('enableEntry', 'addChild', !!me.appBase.get('YaioAccessManager').getAvailiableNodeAction('create', node.key, false));
                me.$('#tree').contextmenu('enableEntry', 'cut', !!me.appBase.get('YaioAccessManager').getAvailiableNodeAction('move', node.key, false));
                me.$('#tree').contextmenu('enableEntry', 'copy', !!me.appBase.get('YaioAccessManager').getAvailiableNodeAction('copy', node.key, false));
                me.$('#tree').contextmenu('enableEntry', 'paste', !!me.clipboardNode);
                node.setActive();
            },
            select: function(event, ui) {
                var that = this;
                // delay the event, so the menu can close and the click event does
                // not interfere with the edit control
                setTimeout(function() { 
                        me.$(that).trigger('nodeCommand', {cmd: ui.cmd});
                    }, 100);
            }
        });
    };
    
    /*****************************************
     *****************************************
     * Service-Funktions (fancytree-callbacks)
     *****************************************
     *****************************************/
    
    /** 
     * Checks if the tree is in wished state and runs doneHandler.
     * If tree is not in state, it waits waitTime trys it till maxTries is reached.
     * If maxTries reached, doneHandler is done regardless of the state.
     * @FeatureDomain                Initialisation
     * @FeatureResult                calls doneHandler if tree is in state
     * @FeatureKeywords              GUI Tree
     * @param treeId                 id of the html-element containing the tree
     * @param state                  the state the tree must reached to run doneHandler
     * @param waitTime               millis to wait for next try if tree is not in state 
     * @param maxTries               maximum of tries till donehandlder will run if tree is not in state 
     * @param doneHandler            callback-function to run if tree is in state
     * @param name                   name of the callback-function fpr logging
     */
    me.yaioDoOnFancyTreeState = function(treeId, state, waitTime, maxTries, doneHandler, name) {
        // check if donehandler
        if (doneHandler) {
            // only postprocess after rendering
            if (treeInstances[treeId].state !== state && maxTries > 0) {
                // wait if maxTries>0 or state is set to rendering_done
                console.log('yaioDoOnFancyTreeState doneHandler:' + name + ') try=' + maxTries
                        + ' wait=' + waitTime + 'ms for ' + treeId + '=' + state);
                setTimeout(function() { 
                    me.appBase.get('YaioExplorerTree').yaioDoOnFancyTreeState(treeId, state, waitTime, maxTries-1, doneHandler);
                }, waitTime);
            } else {
                // maxTries=0 or state is set to rendering_done
                console.log('yaioDoOnFancyTreeState call doneHandler:' + name + ' try=' + maxTries
                        + ' for ' + treeId + '=' + state);
                doneHandler();
            } 
        }
    };
    
    /** 
     * create an fancytree-datanode from an yaio.basenode  
     * @FeatureDomain                Initialisation
     * @FeatureResult                datanode - a datanode for FancyTree
     * @FeatureKeywords              GUI Tree
     * @param basenode               a basenode from yaio
     * @returns                      a datanode for FancyTree
     */
    me.createFancyDataFromNodeData = function(basenode) {
        var datanode = {
           title: basenode.name,
           key: basenode.sysUID, 
           children: null,
           lazy: true,
           basenode: basenode
        };
        
        // deactivate lazyload for node if no children avaiable
        if (me.appBase.DataUtils.isUndefinedStringValue(basenode.statChildNodeCount) || basenode.statChildNodeCount <= 0) {
            datanode.lazy = false;
            datanode.children = [];
        }
    
        if (basenode.className === 'UrlResNode') {
            datanode.title = basenode.resLocName;
        }
        
        return datanode;
    };
    
    /** 
     * Callbackhandler for FancyTree to convert the presponse from server to fancytree-data. 
     * Fancytree runs it if nodedata is read from server.<br>
     * checks for data.response.state=OK, create FancydataNode from data.response.childNodes
     * and adds them to data.result.
     * @FeatureDomain                Initialisation
     * @FeatureResult                updates data.result with the childlist of the node
     * @FeatureKeywords              GUI Tree
     * @param event                  fancytree-event
     * @param data                   the serverresponse (java de.yaio.rest.controller.NodeActionReponse)
     */
    me.postProcessNodeData = function(event, data) {
        var list = new Array();
        
        // check response
        var state = data.response.state;
        if (state === 'OK') {
            // all fine
            console.log('OK loading nodes:' + data.response.stateMsg);
            
            var baseNode = data.response.node;
            if (data.response.childNodes) {
                 // iterate childnodes
                for (var zaehler = 0; zaehler < data.response.childNodes.length; zaehler++) {
                    var childBaseNode = data.response.childNodes[zaehler];
                    
                    if (! me.filterNodeData(childBaseNode)) {
                        continue;
                    }
                    
                    var datanode = me.appBase.get('YaioExplorerTree').createFancyDataFromNodeData(childBaseNode);
                    console.debug('add childnode for ' + baseNode.sysUID
                            + ' = ' + childBaseNode.sysUID + ' ' + childBaseNode.name);
                    list.push(datanode);
                }
            }
        } else {
            // error
            me.appBase.get('Logger').logError('error loading nodes:' + data.response.stateMsg, true);
        }
        
        data.result = list;
    };
    
    
    
    /** 
     * checks if the node passes the current nodefilter
     * @FeatureDomain                Initialisation
     * @FeatureResult                return boolean: check passes or not
     * @FeatureKeywords              GUI Tree
     * @param node                   nodedata from serverresponse (java de.yaio.rest.controller.NodeActionReponse)
     * @return {boolean}             check passes or not
     */
    me.filterNodeData = function(node) {
        if (! me.nodeFilter) {
            // no filter
            return true;
        }
        
        // check filter
        if (me.nodeFilter.classNames && !me.nodeFilter.classNames[node.className]) {
            console.log('filterNodeData: skip node by className:' + node.className);
            return false;
        }
        if (me.nodeFilter.workflowStates && !me.nodeFilter.workflowStates[node.workflowState]) {
            console.log('filterNodeData: skip node by workflowState:' + node.workflowState);
            return false;
        }
        if (me.nodeFilter.statCount && (node[me.nodeFilter.statCount] <= 0)) {
            console.log('filterNodeData: skip node by statCount:' + me.nodeFilter.statCount);
            return false;
        }
        
        return true;
    };

    me.setNodeFilter = function(nodeFilter) {
        me.nodeFilter = nodeFilter || {};
        if (! me.nodeFilter) {
            // no filter
            return;
        }
        
        // filter existing nodes
        ;
    };
    
    me.yaioFancyTreeLoadError = function(e, data) {
        var error = data.error;
        if (error.status && error.statusText) {
            data.message = 'Ajax error: ' + data.message;
            data.details = 'Ajax error: ' + error.statusText + ', status code = ' + error.status;
            
            // check if http-form result
            if (error.status === 401) {
                // reload loginseite
                me.$( '#error-message-text' ).html('Sie wurden vom System abgemeldet.');
                
                // show message
                me.$( '#error-message' ).dialog({
                    modal: true,
                    buttons: {
                      'Neu anmelden': function() {
                        me.$( this ).dialog( 'close' );
                        window.location.assign(me.appBase.config.loginUrl);
                      }
                    }
                });    
            }
        } else {
            data.message = 'Custom error: ' + data.message;
            data.details = 'An error occured during loading: ' + error;
        }
        me.appBase.get('UIDialogs').showToastMessage('error', 'Oops! Ein Fehlerchen beim Laden :-(',
                'Es ist ein Fehler beim Nachladen aufgetreten:' + data.message
                + ' Details:' + data.details);
    };
    
    
    me._init();
    
    return me;
};
