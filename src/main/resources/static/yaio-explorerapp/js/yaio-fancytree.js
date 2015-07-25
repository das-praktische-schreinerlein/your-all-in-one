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

'use strict';

/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     controller for the treeview
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */


var CLIPBOARD = null;

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
Yaio.ExplorerTreeService = function(appBase) {
    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Initialisation
     * <h4>FeatureDescription:</h4>
     *     create an fancytree on the html-element treeId and inits it with the data
     *     of masterNodeId<br>
     *     if the tree already exists it will only reload the tree<br>
     *     after init of the tree the doneHandler will be executed  
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates html-element treeId with a fancytree
     *     <li>calls doneHandler
     *     <li>updates global var treeInstances[treeId]
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree
     * @param treeId - id of the html-element containing the tree
     * @param masterNodeId - the node.sysUID to load
     * @param doneHandler - callback-function when tree is created
     */
    me.yaioCreateOrReloadFancyTree = function(treeId, masterNodeId, doneHandler){
        // check if already loaded
        var state = null;
        if (treeInstances[treeId]) {
            state = treeInstances[treeId].state;
        }
        console.log("yaioCreateOrReloadFancyTree for id: " + treeId + " state=" + state + " caller: " + treeInstances[treeId]);
        if (state) {
            console.log("yaioCreateOrReloadFancyTree: flgYAIOFancyTreeLoaded is set: prepare reload=" 
                    + yaioAppBase.config.showUrl + masterNodeId);
            yaioAppBase.get('YaioExplorerTreeService').yaioDoOnFancyTreeState(treeId, "rendering_done", 1000, 5, function () {
                // do reload if rendering done
                console.log("yaioCreateOrReloadFancyTree: do reload=" 
                        + yaioAppBase.config.showUrl + masterNodeId);
                var tree = $(treeId).fancytree("getTree");
                tree.reload(yaioAppBase.config.showUrl + masterNodeId).done(function(){
                    console.log("yaioCreateOrReloadFancyTree reload tree done:" + masterNodeId);
    
                    // check if doneHandler
                    if (doneHandler) {
                        console.log("yaioCreateOrReloadFancyTree call doneHandler");
                        doneHandler();
                    }
                });
            }, "yaioCreateOrReloadFancyTree.reloadHandler");
        } else {
            console.log("yaioCreateOrReloadFancyTree: flgYAIOFancyTreeLoaded not set:"
                    + " create=" + yaioAppBase.config.showUrl + masterNodeId);
            yaioAppBase.get('YaioExplorerTreeService').yaioCreateFancyTree(treeId, masterNodeId, doneHandler);
        }
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Initialisation
     * <h4>FeatureDescription:</h4>
     *     creates an fancytree on the html-element treeId and inits it with the data
     *     of masterNodeId<br>
     *     after init of the tree the doneHandler will be executed  
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates html-element treeId with a fancytree
     *     <li>calls doneHandler
     *     <li>updates global var treeInstances[treeId]
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree
     * @param treeId - id of the html-element containing the tree
     * @param masterNodeId - the node.sysUID to load
     * @param doneHandler - callback-function when tree is created
     */
    me.yaioCreateFancyTree = function(treeId, masterNodeId, doneHandler) {
        treeInstances[treeId] = {};
        treeInstances[treeId].state = "loading";
        $(treeId).fancytree({
            
            // errorHandler
            loadError: function (e,data) { 
                yaioAppBase.get('YaioExplorerTreeService').yaioFancyTreeLoadError(e, data); 
            },
            
            // save masterNodeId
            masterNodeId: masterNodeId,
            
            // set layoutoptions
            maxTitleWidth: 400,
            
            checkbox: true,
            titlesTabbable: true,     // Add all node titles to TAB chain
      
            source: { 
                url: yaioAppBase.config.showUrl + masterNodeId, 
                cache: false 
            },
          
            // set state of the tree for callback, when tree is created
            loadChildren: function(event, data) {
                treeInstances[treeId].state = "loading_done";
            },    
    
            // lazy load the children
            lazyLoad: function(event, data) {
                var node = data.node;
                console.debug("yaioCreateFancyTree load data for " + node.key 
                        + " from " + yaioAppBase.config.showUrl + node.key);
                data.result = {
                    url: yaioAppBase.config.showUrl + node.key,
                    cache: false
                };
            },
      
            // callback if expanded-state of node changed, to show the matching gantt (only node or + childsum)
            onExpandCallBack: function (node, flag) {
                // activate/deactivate gantt for node
                if (flag) {
                    console.debug("onExpandCallBack: activate gantt - only own data for " + node.key);
                    yaioAppBase.get('YaioNodeGanttRenderService').yaioActivateGanttBlock(node, true);
                } else {
                    // I'm collapsed: show me and my childsum
                    console.debug("onExpandCallBack: activate gantt - sum data of me+children for " + node.key);
                    yaioAppBase.get('YaioNodeGanttRenderService').yaioActivateGanttBlock(node, false);
                }
            },
            
            // parse the nodedata
            postProcess: function(event, data) {
                yaioAppBase.get('YaioExplorerTreeService').postProcessNodeData(event, data);
            },
    
            // render the extra nodedata in grid, sets state for callbaclfunction when tree is created
            renderColumns: function(event, data) {
                yaioAppBase.get('YaioExplorerTreeService').renderColumnsForNode(event, data);
                treeInstances[treeId].state = "rendering_done";
            },
    
            // extensions: ["edit", "table", "gridnav"],
            extensions: ["edit", "dnd", "table", "gridnav"],
    
            dnd: {
                preventVoidMoves: true,
                preventRecursiveMoves: true,
                autoExpandMS: 400,
                dragStart: function(node, data) {
                    return true;
                },
                dragEnter: function(node, data) {
                    // return ["before", "after"];
                    return true;
                },
                dragDrop: function(node, data) {
                    if (window.confirm("Wollen Sie die Node wirklich verschieben?")) {
                        data.otherNode.moveTo(node, data.hitMode);
    
                        // check parent of the node
                        var newPos = data.otherNode.data.basenode.sortPos;
                        var newParent = node.getParent();
                        var newParentKey = node.getParent().key;
                        if (newParent.isRootNode() || newParentKey == "undefined" || ! newParent) {
                            newParentKey = node.tree.options.masterNodeId;
                        }
                        switch( data.hitMode){
                        case "before":
                            newPos = node.data.basenode.sortPos - 2;
                            break;
                        case "after":
                            newPos = node.data.basenode.sortPos + 2;
                            break;
                        default:
                            // add it to the current node
                            newParentKey = node.key;
                            newPos = 9999;
                        }
                        yaioAppBase.get('YaioExplorerActionService').yaioMoveNode(data.otherNode, newParentKey, newPos);
                        return true;
                    } else {
                        // discard
                        return false;
                    }
                }
            },
            edit: {
                triggerStart: ["f2", "dblclick", "shift+click", "mac+enter"],
                beforeEdit: function(event, data){
                    // open yaio-editor
                    yaioAppBase.get('YaioEditorService').yaioOpenNodeEditor(data.node.key, 'edit');
                    
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
    
                    if (window.confirm("Wollen Sie den Titel wirklich ändern?")) {
                        // Save data.input.val() or return false to keep editor open
                        yaioAppBase.get('YaioExplorerActionService').yaioSaveNode(data);
                        // We return true, so ext-edit will set the current user input
                        // as title
                        return true;
                    } else {
                        // discard
                        return false;
                    }
                },
                close: function(event, data){
                    // unused because we use the yaioeditor
    
                    // Editor was removed
                    if( data.save ) {
                        // Since we started an async request, mark the node as preliminary
                        $(data.node.span).addClass("pending");
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
                    tt = $.ui.fancytree.getEventTargetType(event.originalEvent);
                if (tt == undefined) {
                    return true;
                }
            },
    **/
        }).on("nodeCommand", function(event, data){
            // Custom event handler that is triggered by keydown-handler and
            // context menu:
            var refNode,
                tree = $(this).fancytree("getTree"),
                node = tree.getActiveNode();
        
            switch( data.cmd ) {
                case "rename":
                    node.editStart();
                    break;
                case "indent":
                    if (window.confirm("Wollen Sie die Node wirklich verschieben?")) {
                        // move fancynode
                        refNode = node.getPrevSibling();
                        node.moveTo(refNode, "child");
                        refNode.setExpanded();
                        node.setActive();
                        
                        // map rootnode to masterNodeId 
                        var newParentKey = refNode.key;
                        if (refNode.isRootNode()) {
                            newParentKey = tree.options.masterNodeId;
                        }
                        
                        // move yaioNode
                        yaioAppBase.get('YaioExplorerActionService').yaioMoveNode(node, newParentKey, 9999);
                        return true;
                    } else {
                        // discard
                        return false;
                    }
                    
                    break;
                case "outdent":
                    if (window.confirm("Wollen Sie die Node wirklich verschieben?")) {
                        // move fancynode
                        var newParent = node.getParent().getParent();
                        node.moveTo(node.getParent(), "after");
                        node.setActive();
                        
                        // map rootnode to masterNodeId 
                        var newParentKey = newParent.key;
                        if (newParent.isRootNode() || newParentKey == "undefined" || ! newParent) {
                            newParentKey = tree.options.masterNodeId;
                        }
                        // move yaioNode
                        yaioAppBase.get('YaioExplorerActionService').yaioMoveNode(node, newParentKey, 9999);
                        return true;
                    } else {
                        // discard
                        return false;
                    }
                    break;
                case "moveUp":
                    // check parent
                    var newParent = node.getParent();
                    var newParentKey = node.getParent().key;
                    if (newParent.isRootNode() || newParentKey == "undefined" || ! newParent) {
                        newParentKey = tree.options.masterNodeId;
                    }
                    // calc new position
                    var newPos = -2;
                    if (node.getPrevSibling() != null) {
                        newPos = node.getPrevSibling().data.basenode.sortPos - 2;
                    }
    
                    node.moveTo(node.getPrevSibling(), "before");
                    node.setActive();
                    
                    yaioAppBase.get('YaioExplorerActionService').yaioMoveNode(node, newParentKey, newPos);
                    break;
                case "moveDown":
                    // check parent
                    var newParent = node.getParent();
                    var newParentKey = node.getParent().key;
                    if (newParent.isRootNode() || newParentKey == "undefined" || ! newParent) {
                        newParentKey = tree.options.masterNodeId;
                    }
                    // calc new position
                    var newPos = 9999;
                    if (node.getNextSibling() != null) {
                        newPos = node.getNextSibling().data.basenode.sortPos + 2;
                    }
    
                    node.moveTo(node.getNextSibling(), "after");
                    node.setActive();
    
                    yaioAppBase.get('YaioExplorerActionService').yaioMoveNode(node, newParentKey, newPos);
                    break;
                case "remove":
                    yaioAppBase.get('YaioExplorerActionService').yaioRemoveNodeById(node.key);
                    break;
                case "addChild":
                    yaioAppBase.get('YaioEditorService').yaioOpenNodeEditor(node.key, 'create');
                    break;
                case "asTxt":
                    yaioAppBase.get('YaioExplorerActionService').openTxtExportWindow($('#container_content_desc_' + node.key).text());
                    break;
                case "asJira":
                    yaioAppBase.get('YaioExplorerActionService').openJiraExportWindow(node.key);
                    break;
                case "focus":
                    window.location = '#/show/' + node.key;
                    break;
                case "focusNewWindow":
                    window.open('#/show/' + node.key, '_blank');
                    break;
                default:
                    window.alert("Unhandled command: " + data.cmd);
                    return;
            }
      
        }).on("keydown", function(e){
            var c = String.fromCharCode(e.which),
                cmd = null;
        
            if( c === "N" && e.ctrlKey && e.shiftKey) {
                cmd = "addChild";
            } else if( e.which === $.ui.keyCode.DELETE ) {
                cmd = "remove";
            } else if( e.which === $.ui.keyCode.F2 ) {
                cmd = "rename";
            } else if( e.which === $.ui.keyCode.UP && e.ctrlKey ) {
                cmd = "moveUp";
            } else if( e.which === $.ui.keyCode.DOWN && e.ctrlKey ) {
                cmd = "moveDown";
            } else if( e.which === $.ui.keyCode.RIGHT && e.ctrlKey ) {
                cmd = "indent";
            } else if( e.which === $.ui.keyCode.LEFT && e.ctrlKey ) {
                cmd = "outdent";
            }
            if( cmd ){
                $(this).trigger("nodeCommand", {cmd: cmd});
                return false;
            }
        });
        
        // check if donehandler
        if (doneHandler) {
            yaioAppBase.get('YaioExplorerTreeService').yaioDoOnFancyTreeState(treeId, "rendering_done", 1000, 5, doneHandler, 
                    "yaioCreateFancyTree.doneHandler");
        }
    
        /*
         * Context menu (https://github.com/mar10/jquery-ui-contextmenu)
         */
        $(treeId).contextmenu({
            delegate: "span.fancytree-node",
            menu: [
                {title: "Bearbeiten <kbd>[F2]</kbd>", cmd: "rename", uiIcon: "ui-icon-pencil" },
                {title: "Löschen <kbd>[Del]</kbd>", cmd: "remove", uiIcon: "ui-icon-trash" },
                {title: "----"},
    //            {title: "New sibling <kbd>[Ctrl+N]</kbd>", cmd: "addSibling", uiIcon: "ui-icon-plus" },
                {title: "Kind zeugen", cmd: "addChild", uiIcon: "ui-icon-plus" },
                {title: "----"},
                {title: "Focus", cmd: "focus", uiIcon: "ui-icon-arrowreturn-1-e" },
                {title: "In neuem Fenster", cmd: "focusNewWindow", uiIcon: "ui-icon-arrowreturn-1-e" },
                {title: "Export Jira", cmd: "asJira", uiIcon: "ui-icon-clipboard" },
                {title: "Export Txt", cmd: "asTxt", uiIcon: "ui-icon-clipboard" },
                {title: "----"}
    //            {title: "Cut <kbd>Ctrl+X</kbd>", cmd: "cut", uiIcon: "ui-icon-scissors"},
    //            {title: "Copy <kbd>Ctrl-C</kbd>", cmd: "copy", uiIcon: "ui-icon-copy"},
    //            {title: "Paste as child<kbd>Ctrl+V</kbd>", cmd: "paste", uiIcon: "ui-icon-clipboard", disabled: true }
              ],
            beforeOpen: function(event, ui) {
                var node = $.ui.fancytree.getNode(ui.target);
                $("#tree").contextmenu("enableEntry", "paste", !!CLIPBOARD);
                node.setActive();
            },
            select: function(event, ui) {
                var that = this;
                // delay the event, so the menu can close and the click event does
                // not interfere with the edit control
                setTimeout(function(){
                    $(that).trigger("nodeCommand", {cmd: ui.cmd});
                }, 100);
            }
        });
    }
    
    /*****************************************
     *****************************************
     * Service-Funktions (fancytree-callbacks)
     *****************************************
     *****************************************/
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Initialisation
     * <h4>FeatureDescription:</h4>
     *     Checks if the tree is in wished state and runs doneHandler.
     *     If tree is not in state, it waits waitTime trys it till maxTries is reached.
     *     If maxTries reached, doneHandler is done regardless of the state.
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>calls doneHandler if tree is in state
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree
     * @param treeId - id of the html-element containing the tree
     * @param state - the state the tree must reached to run doneHandler
     * @param waitTime - millis to wait for next try if tree is not in state 
     * @param maxTries - maximum of tries till donehandlder will run if tree is not in state 
     * @param doneHandler - callback-function to run if tree is in state
     * @param name        - name of the callback-function fpr logging
     */
    me.yaioDoOnFancyTreeState = function(treeId, state, waitTime, maxTries, doneHandler, name) {
        // check if donehandler
        if (doneHandler) {
            // only postprocess after rendering
            if (treeInstances[treeId].state != state && maxTries > 0) {
                // wait if maxTries>0 or state is set to rendering_done
                console.log("yaioDoOnFancyTreeState doneHandler:" + name + ") try=" + maxTries 
                        + " wait=" + waitTime + "ms for " + treeId + "=" + state);
                setTimeout(function() { 
                    yaioAppBase.get('YaioExplorerTreeService').yaioDoOnFancyTreeState(treeId, state, waitTime, maxTries-1, doneHandler);
                }, waitTime);
            } else {
                // maxTries=0 or state is set to rendering_done
                console.log("yaioDoOnFancyTreeState call doneHandler:" + name + " try=" + maxTries 
                        + " for " + treeId + "=" + state);
                doneHandler();
            } 
        }
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Initialisation
     * <h4>FeatureDescription:</h4>
     *     create an fancytree-datanode from an yaio.basenode  
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>datanode - a datanode for FancyTree
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree
     * @param basenode - a basenode from yaio
     * @returns a datanode for FancyTree
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
        if (basenode.statChildNodeCount == "undefined" || basenode.statChildNodeCount <= 0) {
            datanode.lazy = false;
            datanode.children = [];
        }
    
        if (basenode.className == "UrlResNode") {
            datanode.title = basenode.resLocName;
        }
        
        return datanode;
    } 
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Initialisation
     * <h4>FeatureDescription:</h4>
     *     Callbackhandler for FancyTree to convert the presponse from server to fancytree-data. 
     *     Fancytree runs it if nodedata is read from server.<br>
     *     checks for data.response.state=OK, create FancydataNode from data.response.childNodes
     *     and adds them to data.result.
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates data.result with the childlist of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree
     * @param event - fancytree-event
     * @param data - the serverresponse (java de.yaio.rest.controller.NodeActionReponse)
     */
    me.postProcessNodeData = function(event, data) {
        var list = new Array();
        
        // check response
        var state = data.response.state;
        if (state == "OK") {
            // all fine
            console.log("OK loading nodes:" + data.response.stateMsg);
            
            var baseNode = data.response.node;
            if (data.response.childNodes) {
                 // iterate childnodes
                for (var zaehler =0; zaehler < data.response.childNodes.length; zaehler++) {
                    var childBaseNode = data.response.childNodes[zaehler];
                    var datanode = yaioAppBase.get('YaioExplorerTreeService').createFancyDataFromNodeData(childBaseNode);
                    console.debug("add childnode for " + baseNode.sysUID 
                            + " = " + childBaseNode.sysUID + " " + childBaseNode.name);
                    list.push(datanode);
                }
            }
        } else {
            // error
            yaioAppBase.get('YaioBaseService').logError("error loading nodes:" + data.response.stateMsg, true)
        }
        
        data.result = list;
    }
    
    
    me.yaioFancyTreeLoadError = function(e, data) {
        var error = data.error;
        if (error.status && error.statusText) {
            data.message = "Ajax error: " + data.message;
            data.details = "Ajax error: " + error.statusText + ", status code = " + error.status;
            
            // check if http-form result
            if (error.status == 401) {
                // reload loginseite
                $( "#error-message-text" ).html("Sie wurden vom System abgemeldet.");
                
                // show message
                $( "#error-message" ).dialog({
                    modal: true,
                    buttons: {
                      "Neu anmelden": function() {
                        $( this ).dialog( "close" );
                        window.location.assign(yaioAppBase.config.loginUrl);
                      }
                    }
                });    
            }
        } else {
            data.message = "Custom error: " + data.message;
            data.details = "An error occured during loading: " + error;
        }
        yaioAppBase.get('YaioBaseService').showToastMessage("error", "Oops! Ein Fehlerchen beim Laden :-(", 
                "Es ist ein Fehler beim Nachladen aufgetreten:" + data.message 
                + " Details:" + data.details);
      }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Layout
     *     Rendering
     * <h4>FeatureDescription:</h4>
     *     Callback for fancyftree. Renders the full block for corresponding basenode.
     *     Manipulates the default-fancytree-tablerow (replace+add+hide elements).<br>
     *     <ul>
     *       <li>data.node.tr: Html-Obj of the table-line
     *       <li>data.node.data.basenode: the basenode (java de.yaio.core.node.BaseNode)
     *     </ul>
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>Updates DOM
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree Rendering
     * @param event - fancytree-event
     * @param data - the fancytreenode-data (basenode = data.node.data.basenode, tr = data.node.tr)
     * @param preventActionsColum - dont replace Action-column
     */
    me.renderColumnsForNode = function(event, data, preventActionsColum) {
        // extract nodedata
        var node = data.node;
        var basenode = node.data.basenode;
        var nodestate = basenode.state;
        var statestyle = "node-state-" + nodestate;
    
        var colName = 0;
        var colData = 1;
        var colGantt = 2;
        var colActions = 3;
        
        // get tdlist
        var $tdList = $(node.tr).find(">td");
    
        // add stateclasss to tr
        $(node.tr).addClass("container_nodeline");
        
        // add fields
        if (! preventActionsColum) {
            $tdList.eq(colActions).html(
                    "<div class='fieldtype_sysToggler toggler_show'>"
                        + "<a onclick=\"javascript: yaioAppBase.get('YaioLayoutService').toggleNodeSysContainer('" + basenode.sysUID + "'); return false;\""
                                + " id='toggler_sys_" + basenode.sysUID + "'"
                                + " class='' "
                                + " data-tooltip='tooltip.command.ToggleSys' lang='tech'></a>"
                        + "</div>"
                    + "<a onclick=\"javascript: yaioAppBase.get('YaioEditorService').yaioOpenNodeEditor('" + basenode.sysUID + "', 'edit'); return false;\""
                            + " id='cmdEdit" + basenode.sysUID + "'"
                            + " class='yaio-icon-edit'"
                            + " lang='tech' data-tooltip='tooltip.command.NodeEdit'></a>"
                    + "<a onclick=\"javascript: yaioAppBase.get('YaioEditorService').yaioOpenNodeEditor('" + basenode.sysUID + "', 'create'); return false;\""
                            + " id='cmdCreate" + basenode.sysUID + "'"
                            + " class='yaio-icon-create'"
                            + " lang='tech' data-tooltip='tooltip.command.NodeCreateChild'></a>"
                    + "<a onclick=\"javascript: yaioAppBase.get('YaioEditorService').yaioOpenNodeEditor('" + basenode.sysUID + "', 'createsymlink'); return false;\""
                            + " id='cmdCreateSymLink" + basenode.sysUID + "'"
                            + " class='yaio-icon-createsymlink'"
                            + " lang='tech' data-tooltip='tooltip.command.NodeCreateSymLink'></a>"
                    + "<a onclick=\"javascript: yaioAppBase.get('YaioExplorerActionService').yaioRemoveNodeById('" + basenode.sysUID + "'); return false;\""
                            + " id='cmdRemove" + basenode.sysUID + "'"
                            + " class='yaio-icon-remove'"
                            + " lang='tech' data-tooltip='tooltip.command.NodeDelete'></a>"
                    ).addClass("container_field")
                     .addClass("fieldtype_actions")
                     //.addClass(statestyle)
                     ;
        }
    
        // replace checkbox by center-command
        var $expanderEle = $tdList.eq(colName).find("span.fancytree-expander");
        $expanderEle.attr('data-tooltip', 'tooltip.command.NodeShow');
        $expanderEle.attr('lang', 'tech');
        $expanderEle.attr('id', 'expander' + basenode.sysUID);
    
        // replace checkbox by center-command
        var $checkEle = $tdList.eq(colName).find("span.fancytree-checkbox");
        $checkEle.html("<a href='#/show/" + basenode.sysUID + "'"
            + " class='yaio-icon-center'"
            + " lang='tech' data-tooltip='tooltip.command.NodeFocus'></a>");
        $checkEle.removeClass("fancytree-checkbox").addClass("command-center");
    
        // manipulate name-column
        $tdList.eq(colName).addClass("container_field")
                     .addClass("fieldtype_name")
                     .addClass("field_name")
                     //.addClass(statestyle)
                     ;
        
        // define name
        var name = basenode.name;
        if (name == null || name.length <= 0) {
           if (basenode.className == "UrlResNode") {
               name = basenode.resLocName;
           } else if (basenode.className == "SymLinkNode") {
               name = basenode.symLinkName;
           }
        }
    
        // insert state before name-span
        var $nameEle = $tdList.eq(colName).find("span.fancytree-title");
        var $div = $("<div style='disply: block-inline' />")
            .append($("<span class='" + statestyle + " fancytree-title-state' lang='de' id='titleState" + basenode.sysUID + "'/>")
                        .html(basenode.state + " ")
                        )
            .append("&nbsp;")
            .append($("<span class='fancytree-title2' id='title" + basenode.sysUID + "'>" 
                    + yaioAppBase.get('YaioBaseService').htmlEscapeText(name) + "</span>"))
            ;
        $nameEle.html($div);
        //$tdList.eq(colName).find("span.fancytree-expander").addClass(statestyle);
        
        // render datablock
        var $nodeDataBlock = yaioAppBase.get('YaioNodeDataRenderService').renderDataBlock(basenode, node);
        
        // add SysData
        // create sys row
        var $row = $("<div class='togglecontainer field_nodeSys' id='detail_sys_" + basenode.sysUID + "' />");
        $nodeDataBlock.append($row);
        $row.append(
                $("<div lang='tech' />").html("Stand: " + yaioAppBase.get('YaioBaseService').formatGermanDateTime(basenode.sysChangeDate))
                        .addClass("container_field")
                        .addClass("fieldtype_basedata")
                        .addClass("fieldtype_sysChangeDate")
                        .addClass("field_sysChangeDate")
                        );
        $row.append(
                $("<div lang='tech' />").html(" (V " + basenode.sysChangeCount + ")")
                        .addClass("container_field")
                        .addClass("fieldtype_basedata")
                        .addClass("fieldtype_sysChangeCount")
                        .addClass("field_sysChangeCount")
                        );
        $row.append(
                $("<div lang='tech' />").html("angelegt: " + yaioAppBase.get('YaioBaseService').formatGermanDateTime(basenode.sysCreateDate))
                        .addClass("container_field")
                        .addClass("fieldtype_basedata")
                        .addClass("fieldtype_sysCreateDate")
                        .addClass("field_sysCreateDate")
                        ); 
        $row.append(
                $("<div lang='tech' />").html("Kinder: " + basenode.statChildNodeCount 
                        + " Workflows: " + basenode.statWorkflowCount
                        + " ToDos: " + basenode.statWorkflowTodoCount)
                        .addClass("container_field")
                        .addClass("fieldtype_basedata")
                        .addClass("fieldtype_statistik")
                        .addClass("field_statistik")
                        ); 
        
        // add nodeDesc if set
        if (basenode.nodeDesc != "" && basenode.nodeDesc != null) {
            // columncount
            //var columnCount = $(">td", $nodedataBlock).length;
            
            // add  column
            $($nodeDataBlock).find("div.container_data_row").append(
                    $("<div />").html("<a href='#'" +
                            " onclick=\"yaioAppBase.get('YaioLayoutService').toggleNodeDescContainer('" + basenode.sysUID + "'); return false;\"" +
                                " id='toggler_desc_" + basenode.sysUID + "'" +
                                " data-tooltip='tooltip.command.ToggleDesc' lang='tech'></a>")
                            .addClass("container_field")
                            .addClass("fieldtype_descToggler")
                            .addClass("toggler_show")
                            //.addClass(statestyle)
                            );
            
            // create desc row
            var $divDesc = $("<div class='togglecontainer' id='detail_desc_" + basenode.sysUID + "' />");
            $divDesc.addClass("field_nodeDesc");
    
            // add commands
            var commands = "<div class='container-commands-desc' id='commands_desc_" + basenode.sysUID + "'"
                + " data-tooltip='tooltip.command.TogglePreWrap' lang='tech' >" 
                + "<input type='checkbox' id='cmd_toggle_content_desc_" + basenode.sysUID + "' onclick=\"yaioAppBase.get('YaioLayoutService').togglePreWrap('#content_desc_" + basenode.sysUID + "');yaioAppBase.get('YaioLayoutService').togglePreWrap('#container_content_desc_" + basenode.sysUID + "'); return true;\">"
                + "<span lang='tech'>im Originallayout anzeigen</span>"
        //        + "<input type='checkbox' id='cmd_toggle_content_desc_markdown_" + basenode.sysUID + "' onclick=\"toggleDescMarkdown('#container_content_desc_" + basenode.sysUID + "'); return true;\">"
        //        + "<span lang='tech'>Markdown</span>"
                ;
            commands += "<a class=\"button command-desc-jiraexport\" onClick=\"yaioAppBase.get('YaioExplorerActionService').openJiraExportWindow('"+ basenode.sysUID + "'); return false;" 
                +   "\" lang='tech' data-tooltip='tooltip.command.OpenJiraExportWindow'>common.command.OpenJiraExportWindow</a>";
            commands += "<a class=\"button command-desc-txtexport\" onClick=\"yaioAppBase.get('YaioExplorerActionService').openTxtExportWindow(" 
                +   "$('#container_content_desc_" + basenode.sysUID + "').text()); return false;" 
                +   "\" lang='tech' data-tooltip='tooltip.command.OpenTxtExportWindow'>common.command.OpenTxtExportWindow</a>";
            if ('speechSynthesis' in window) {
                // Synthesis support. Make your web apps talk!
                commands += "<a class=\"button\" onClick=\"yaioAppBase.get('YaioLayoutService').openSpeechSynthWindow(" 
                    +   "document.getElementById('container_content_desc_" + basenode.sysUID + "')); return false;" 
                    +   "\" lang='tech' data-tooltip='tooltip.command.OpenSpeechSynth'>common.command.OpenSpeechSynth</a>";
     
               }        
            commands += "</div>";
            $divDesc.append(commands);
            $divDesc.append("<div class='container-toc-desc' id='toc_desc_" + basenode.sysUID + "'><h1>Inhalt</h1></div>");
            
            // append content
            var descText = basenode.nodeDesc;
            descText = descText.replace(/\<WLBR\>/g, "\n");
            descText = descText.replace(/\<WLESC\>/g, "\\");
            descText = descText.replace(/\<WLTAB\>/g, "\t");
    
            // prepare descText
            var descHtml = yaioAppBase.get('YaioFormatterService').formatMarkdown(descText, false, basenode.sysUID);
            $divDesc.append("<div id='container_content_desc_" + basenode.sysUID + "' class='container-content-desc syntaxhighlighting-open'>" + descHtml + "</div>");
    
            // append to datablock
            $nodeDataBlock.append($divDesc);
    
            // disable draggable for td.block_nodedata
            $( "#tree" ).draggable({ cancel: "td.block_nodedata" });
            
            // enable selction
            $("td.block_nodedata").enableSelection();
        }
        
        // add nodeData
        $tdList.eq(colData).html($nodeDataBlock).addClass("block_nodedata");
        
        // add TOC
        var settings = {
                toc: {}
        };
        settings.toc.dest = $("#toc_desc_" + basenode.sysUID);
        settings.toc.minDeep = 2;
        $.fn.toc($("#container_content_desc_" + basenode.sysUID), settings);
    
        // add gantt
        var $nodeGanttBlock = yaioAppBase.get('YaioNodeGanttRenderService').renderGanttBlock(basenode, node);
        $tdList.eq(colGantt).html($nodeGanttBlock).addClass("block_nodegantt");
    
        // set visibility of data/gantt-block
        if ($("#tabTogglerGantt").css("display") != "none") {
            $tdList.eq(colData).css("display", "table-cell");
            $tdList.eq(colGantt).css("display", "none");
        } else {
            $tdList.eq(colGantt).css("display", "table-cell");
            $tdList.eq(colData).css("display", "none");
        }
        
        // toogle sys
        yaioAppBase.get('YaioLayoutService').toggleNodeSysContainer(basenode.sysUID);
        
        // toogle desc
        yaioAppBase.get('YaioLayoutService').toggleNodeDescContainer(basenode.sysUID);
    
        // calc nodeData
        yaioAppBase.get('YaioNodeGanttRenderService').yaioRecalcMasterGanttBlockFromTree();
    }

    me._init();
    
    return me;
};
