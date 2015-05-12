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
var CONST_MasterId = "MasterplanMasternode1";
var loginUrl = "/yaio-explorerapp/yaio-explorerapp.html#/login";
var baseUrl = "/nodes/";
var showUrl = baseUrl + "show/";
var symLinkUrl = baseUrl + "showsymlink/";
var updateUrl = baseUrl + "update/";
var createUrl = baseUrl + "create/";
var moveUrl = baseUrl + "move/";
var removeUrl = baseUrl + "delete/";

var treeInstances = new Array();
var configNodeTypeFields = {
    Create: {
        fields: [
            { fieldName: "className", type: "hidden"},
            { fieldName: "sysUID", type: "hidden"},
        ]
    },
    CreateSymlink: {
        fields: [
            { fieldName: "name", type: "input"},
            { fieldName: "type", type: "hidden"},
            { fieldName: "className", type: "hidden"},
            { fieldName: "sysUID", type: "hidden"},
            { fieldName: "symLinkRef", type: "input"},
            { fieldName: "symLinkName", type: "input"},
            { fieldName: "symLinkTags", type: "textarea"},
            { fieldName: "mode", type: "hidden", intern: true},
        ]
    },
    Common: {
        fields: [
            { fieldName: "className", type: "hidden"},
            { fieldName: "sysUID", type: "hidden"},
            { fieldName: "mode", type: "hidden", intern: true},
            { fieldName: "type", type: "select"},
            { fieldName: "state", type: "select"},
            { fieldName: "nodeDesc", type: "textarea"},
        ]
    },
    TaskNode: {
        fields: [
            { fieldName: "name", type: "input"},
            { fieldName: "istAufwand", type: "input"},
            { fieldName: "istStand", type: "input"},
            { fieldName: "istStart", type: "input", datatype: "date"},
            { fieldName: "istEnde", type: "input", datatype: "date"},
            { fieldName: "planAufwand", type: "input"},
            { fieldName: "planStart", type: "input", datatype: "date"},
            { fieldName: "planEnde", type: "input", datatype: "date"},
        ]
    },
    EventNode: {
        fields: [
            { fieldName: "name", type: "input"},
            { fieldName: "istAufwand", type: "input"},
            { fieldName: "istStand", type: "input"},
            { fieldName: "istStart", type: "input", datatype: "datetime"},
            { fieldName: "istEnde", type: "input", datatype: "datetime"},
            { fieldName: "planAufwand", type: "input"},
            { fieldName: "planStart", type: "input", datatype: "datetime"},
            { fieldName: "planEnde", type: "input", datatype: "datetime"},
        ]
    },
    InfoNode: {
        fields: [
            { fieldName: "name", type: "textarea"},
            { fieldName: "docLayoutTagCommand", type: "select"},
            { fieldName: "docLayoutAddStyleClass", type: "input"},
            { fieldName: "docLayoutShortName", type: "input"},
            { fieldName: "docLayoutFlgCloseDiv", type: "checkbox"},
        ]
    },
    UrlResNode: {
        fields: [
            { fieldName: "name", type: "input"},
            { fieldName: "resLocRef", type: "input"},
            { fieldName: "resLocName", type: "input"},
            { fieldName: "resLocTags", type: "textarea"},
            { fieldName: "docLayoutTagCommand", type: "select"},
            { fieldName: "docLayoutAddStyleClass", type: "input"},
            { fieldName: "docLayoutShortName", type: "input"},
            { fieldName: "docLayoutFlgCloseDiv", type: "checkbox"},
        ]
    },
    SymLinkNode: {
        fields: [
            { fieldName: "name", type: "input"},
            { fieldName: "type", type: "hidden"},
            { fieldName: "symLinkRef", type: "input"},
            { fieldName: "symLinkName", type: "input"},
            { fieldName: "symLinkTags", type: "textarea"},
        ]
    }
};

/*****************************************
 *****************************************
 * YAIO-Treefunctions
 *****************************************
 *****************************************/

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
function yaioCreateOrReloadFancyTree(treeId, masterNodeId, doneHandler){
    // check if already loaded
    var state = null;
    if (treeInstances[treeId]) {
        state = treeInstances[treeId].state;
    }
    console.log("yaioCreateOrReloadFancyTree for id: " + treeId + " state=" + state + " caller: " + treeInstances[treeId]);
    if (state) {
        console.log("yaioCreateOrReloadFancyTree: flgYAIOFancyTreeLoaded is set: prepare reload=" 
                + showUrl + masterNodeId);
        yaioDoOnFancyTreeState(treeId, "rendering_done", 1000, 5, function () {
            // do reload if rendering done
            console.log("yaioCreateOrReloadFancyTree: do reload=" 
                    + showUrl + masterNodeId);
            var tree = $(treeId).fancytree("getTree");
            tree.reload(showUrl + masterNodeId).done(function(){
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
                + " create=" + showUrl + masterNodeId);
        yaioCreateFancyTree(treeId, masterNodeId, doneHandler);
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
function yaioCreateFancyTree(treeId, masterNodeId, doneHandler) {
    treeInstances[treeId] = {};
    treeInstances[treeId].state = "loading";
    $(treeId).fancytree({
        
        // errorHandler
        loadError: function (e,data) { 
            yaioFancyTreeLoadError(e, data); 
        },
        
        // save masterNodeId
        masterNodeId: masterNodeId,
        
        // set layoutoptions
        maxTitleWidth: 400,
        
        checkbox: true,
        titlesTabbable: true,     // Add all node titles to TAB chain
  
        source: { 
            url: showUrl + masterNodeId, 
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
                    + " from " + showUrl + node.key);
            data.result = {
                url: showUrl + node.key,
                cache: false
            };
        },
  
        // callback if expanded-state of node changed, to show the matching gantt (only node or + childsum)
        onExpandCallBack: function (node, flag) {
            // activate/deactivate gantt for node
            if (flag) {
                console.debug("onExpandCallBack: activate gantt - only own data for " + node.key);
                yaioActivateGanttBlock(node, true);
            } else {
                // I'm collapsed: show me and my childsum
                console.debug("onExpandCallBack: activate gantt - sum data of me+children for " + node.key);
                yaioActivateGanttBlock(node, false);
            }
        },
        
        // parse the nodedata
        postProcess: function(event, data) {
            postProcessNodeData(event, data);
        },

        // render the extra nodedata in grid, sets state for callbaclfunction when tree is created
        renderColumns: function(event, data) {
            renderColumnsForNode(event, data);
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
                if (confirm("Wollen Sie die Node wirklich verschieben?")) {
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
                    yaioMoveNode(data.otherNode, newParentKey, newPos);
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
                yaioOpenNodeEditor(data.node.key, 'edit');
                
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

                if (confirm("Wollen Sie den Titel wirklich ändern?")) {
                    // Save data.input.val() or return false to keep editor open
                    yaioSaveNode(data);
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
            nodeColumnIdx: 0,
//            checkboxColumnIdx: 0
        },
        gridnav: {
            autofocusInput: false,
            handleCursorKeys: true
        },
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
        var refNode, moveMode,
            tree = $(this).fancytree("getTree"),
            node = tree.getActiveNode();
    
        switch( data.cmd ) {
            case "rename":
                node.editStart();
                break;
            case "indent":
                if (confirm("Wollen Sie die Node wirklich verschieben?")) {
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
                    yaioMoveNode(node, newParentKey, 9999);
                    return true;
                } else {
                    // discard
                    return false;
                }
                
                break;
            case "outdent":
                if (confirm("Wollen Sie die Node wirklich verschieben?")) {
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
                    yaioMoveNode(node, newParentKey, 9999);
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
                var newPos = -2
                if (node.getPrevSibling() != null) {
                    newPos = node.getPrevSibling().data.basenode.sortPos - 2;
                }

                node.moveTo(node.getPrevSibling(), "before");
                node.setActive();
                
                yaioMoveNode(node, newParentKey, newPos);
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

                yaioMoveNode(node, newParentKey, newPos);
                break;
            case "remove":
                yaioRemoveNodeById(node.key);
                break;
            case "addChild":
                yaioOpenNodeEditor(node.key, 'create');
                break;
            case "asTxt":
                openTxtExportWindow($('#container_content_desc_' + node.key).text());
                break;
            case "asJira":
                openJiraExportWindow(node.key);
                break;
            case "focus":
                window.location = '#/show/' + node.key;
                break;
            case "focusNewWindow":
                window.open('#/show/' + node.key, '_blank');
                break;
            default:
                alert("Unhandled command: " + data.cmd);
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
        yaioDoOnFancyTreeState(treeId, "rendering_done", 1000, 5, doneHandler, 
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
            {title: "----"},
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
 * @param doneHandler - name of the callback-function fpr logging
 */
function yaioDoOnFancyTreeState(treeId, state, waitTime, maxTries, doneHandler, name) {
    // check if donehandler
    if (doneHandler) {
        // only postprocess after rendering
        if (treeInstances[treeId].state != state && maxTries > 0) {
            // wait if maxTries>0 or state is set to rendering_done
            console.log("yaioDoOnFancyTreeState doneHandler:" + name + ") try=" + maxTries 
                    + " wait=" + waitTime + "ms for " + treeId + "=" + state);
            setTimeout(function() { 
                yaioDoOnFancyTreeState(treeId, state, waitTime, maxTries-1, doneHandler);
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
function createFancyDataFromNodeData(basenode) {
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
function postProcessNodeData(event, data) {
    var list = new Array();
    
    // check response
    var state = data.response.state;
    if (state == "OK") {
        // all fine
        console.log("OK loading nodes:" + data.response.stateMsg)
        
        var baseNode = data.response.node;
        if (data.response.childNodes) {
             // iterate childnodes
            for (var zaehler =0; zaehler < data.response.childNodes.length; zaehler++) {
                var childBaseNode = data.response.childNodes[zaehler];
                var datanode = createFancyDataFromNodeData(childBaseNode);
                console.debug("add childnode for " + baseNode.sysUID 
                        + " = " + childBaseNode.sysUID + " " + childBaseNode.name);
                list.push(datanode);
            }
        }
    } else {
        // error
        logError("error loading nodes:" + data.response.stateMsg, true)
    }
    
    data.result = list;
}


function yaioFancyTreeLoadError(e, data) {
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
                    window.location.assign(loginUrl);
                  }
                }
            });    
        }
    } else {
        data.message = "Custom error: " + data.message;
        data.details = "An error occured during loading: " + error;
    }
    showToastMessage("error", "Oops! Ein Fehlerchen beim Laden :-(", 
            "Es ist ein Fehler beim Nachladen aufgetreten:" + data.message 
            + " Details:" + data.details);
  }


/**
 * <h4>FeatureDomain:</h4>
 *     Layout
 *     Rendering
 * <h4>FeatureDescription:</h4>
 *     Renders the DataBlock for basenode and returns a JQuery-Html-Obj.
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>ReturnValue JQuery-Html-Object - the rendered datablock
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Tree Rendering
 * @param basenode - the nodedata to render (java de.yaio.core.node.BaseNode)
 * @param fancynode - the corresponding fancynode
 * @returns JQuery-Html-Object - the rendered datablock
 */
function renderDataBlock(basenode, fancynode) {
    // extract nodedata
    var nodestate = basenode.state;
    var statestyle = "node-state-" + nodestate;   

    var msg = "datablock for node:" + basenode.sysUID;
    console.log("renderDataBlock START: " + msg);

    // current datablock
    var nodeDataBlock = "";
    var $table = $("<div class='container_data_table'/>");
    var $row = $("<div class='container_data_row'/>");
    $table.append($row);
    
    // default fields
    $row.append($("<div />").html(htmlEscapeText(basenode.metaNodePraefix + basenode.metaNodeNummer))
            .addClass("container_field")
            .addClass("fieldtype_basedata")
            .addClass("fieldtype_metanummer")
            .addClass("field_metanummer")
            );
        
    $row.append($("<div lang='tech' />").html(basenode.className)
           .addClass("container_field")
           .addClass("fieldtype_basedata")
           .addClass("fieldtype_type")
           .addClass("field_type")
           .addClass(statestyle));
    if (basenode.className == "TaskNode" || basenode.className == "EventNode") {
        // TaskNode
        $row.append(
                $("<div />").html("&nbsp;" + formatNumbers(basenode.istChildrenSumStand, 0, "%"))
                        .addClass("container_field")
                        .addClass("fieldtype_additionaldata")
                        .addClass("fieldtype_stand")
                        .addClass("field_istChildrenSumStand")
                        .addClass(statestyle)
                        ); 
        $row.append(
                $("<div />").html("&nbsp;" + formatNumbers(basenode.istChildrenSumAufwand, 1, "h"))
                        .addClass("container_field")
                        .addClass("fieldtype_additionaldata")
                        .addClass("fieldtype_aufwand")
                        .addClass("field_istChildrenSumAufwand")
                        .addClass(statestyle)
                        );
        $row.append(
                $("<div />").html("&nbsp;" + formatGermanDate(basenode.istChildrenSumStart)
                        + "-" + formatGermanDate(basenode.istChildrenSumEnde))
                         .addClass("container_field")
                         .addClass("fieldtype_additionaldata")
                         .addClass("fieldtype_fromto")
                         .addClass("field_istChildrenSum")
                         .addClass(statestyle)
                         );
        $row.append(
                $("<div />").html("&nbsp;" + formatNumbers(basenode.planChildrenSumAufwand, 1, "h"))
                         .addClass("container_field")
                         .addClass("fieldtype_additionaldata")
                         .addClass("fieldtype_aufwand")
                         .addClass("field_planChildrenSumAufwand")
                         .addClass(statestyle)
                         );
        $row.append(
                $("<div />").html("&nbsp;" + formatGermanDate(basenode.planChildrenSumStart)
                         + "-" + formatGermanDate(basenode.planChildrenSumEnde))
                         .addClass("container_field")
                         .addClass("fieldtype_additionaldata")
                         .addClass("fieldtype_fromto")
                         .addClass("field_planChildrenSum")
                         .addClass(statestyle)
                         );
    } else if (basenode.className == "InfoNode" || basenode.className == "UrlResNode") {
        // Info + urlRes
        
        // Url only
        if (basenode.className == "UrlResNode") {
            // url
            $row.append(
                    $("<div />").html("<a href='" + htmlEscapeText(basenode.resLocRef) + "' target='_blank'>" 
                                     + htmlEscapeText(basenode.resLocRef) + "</a>")
                              .addClass("container_field")
                              .addClass("fieldtype_additionaldata")
                              .addClass("fieldtype_url")
                              .addClass("field_resLocRef")
                              ); 
        }
    
        // both
        if (   basenode.docLayoutTagCommand || basenode.docLayoutShortName
            || basenode.docLayoutAddStyleClass || basenode.docLayoutFlgCloseDiv) {
                $row.append(
                        $("<div lang='tech' />").html("Layout ")
                                .addClass("container_field")
                                .addClass("fieldtype_additionaldata")
                                .addClass("fieldtype_ueDocLayout")
                                .addClass("field_ueDocLayout")
                                ); 
            
            // check which docLayout is set    
            if (basenode.docLayoutTagCommand) {
                $row.append(
                        $("<div lang='tech' />").html("Tag: " 
                                    + htmlEscapeText(basenode.docLayoutTagCommand))
                                .addClass("container_field")
                                .addClass("fieldtype_additionaldata")
                                .addClass("fieldtype_docLayoutTagCommand")
                                .addClass("field_docLayoutTagCommand")
                                ); 
            }
            if (basenode.docLayoutAddStyleClass) {
                $row.append(
                        $("<div lang='tech' />").html("Style: " 
                                    + htmlEscapeText(basenode.docLayoutAddStyleClass))
                                .addClass("container_field")
                                .addClass("fieldtype_additionaldata")
                                .addClass("fieldtype_docLayoutAddStyleClass")
                                .addClass("field_docLayoutAddStyleClass")
                                ); 
            }
            if (basenode.docLayoutShortName) {
                $row.append(
                        $("<div lang='tech' />").html("Kurzname: " 
                                    + htmlEscapeText(basenode.docLayoutShortName))
                                .addClass("container_field")
                                .addClass("fieldtype_additionaldata")
                                .addClass("fieldtype_docLayoutShortName")
                                .addClass("field_docLayoutShortName")
                                ); 
            }
            if (basenode.docLayoutFlgCloseDiv) {
                $row.append(
                        $("<div lang='tech' />").html("Block schlie&szligen!")
                                .addClass("container_field")
                                .addClass("fieldtype_additionaldata")
                                .addClass("fieldtype_docLayoutFlgCloseDiv")
                                .addClass("field_docLayoutFlgCloseDiv")
                                ); 
            }
        }
    } else if (basenode.className == "SymLinkNode") {
        yaioLoadSymLinkData(basenode, fancynode);
    } 

    console.log("renderDataBlock DONE: " + msg);

    return $table;
}

/**
 * <h4>FeatureDomain:</h4>
 *     Layout
 *     Rendering
 * <h4>FeatureDescription:</h4>
 *     Calcs+renders the gantt-block of specified type: (ist, plan, planChildrenSum, 
 *     istChildrenSum) for basenode. Updates this elements:
 *     <ul>
 *       <li> #gantt_" + type + "_container_" + basenode.sysUID
 *       <li> #gantt_" + type + "_aufwand_" + basenode.sysUID
 *       <li> #gantt_" + type + "_bar_" + basenode.sysUID
 *     </ul>
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>Updates GUI: #gantt_" + type + "_container_" + basenode.sysUID
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Tree Rendering
 * @param basenode - the nodedata to render (java de.yaio.core.node.BaseNode)
 * @param type - the type of data to calc (ist, plan, planChildrenSum, istChildrenSum)
 * @param label - the label to show if aufwand >0
 * @param $divLine - optional ganttContainer to use - if not set #gantt_" + type + "_container_" + basenode.sysUID will be used
 */
function fillGanttBlock(basenode, type, label, $divLine) {
    var msg = "ganttblock for node:" + basenode.sysUID;

    // get divs
    if (! $divLine) {
        $divLine = $("#gantt_" + type + "_container_" + basenode.sysUID);
    }
    var $divLabel = $($divLine).find("#gantt_" + type + "_aufwand_" + basenode.sysUID);
    var $div = $($divLine).find("#gantt_" + type + "_bar_" + basenode.sysUID);
    
    // reset
    $divLabel.html("");
    $div.html("&nbsp;");
    $div.css("width", 0);
    $div.css("margin-left", 0);
    $div.attr("data-rangeaufwand", 0);

    // set range
    var dateRangeStartStr = $("#inputGanttRangeStart").val();
    var dateRangeEndStr = $("#inputGanttRangeEnde").val();
    if (dateRangeEndStr == null || dateRangeEndStr == null) {
        console.error("fillGanttBlock range is not set correctly: " 
                + dateRangeStartStr + "-" + dateRangeEndStr + " " + msg);
        return;
    }
    
    // calc dates...
    var lstDate=dateRangeStartStr.split(".");
    var dateRangeStart = new Date(lstDate[1]+"/"+lstDate[0]+"/"+lstDate[2]);
    lstDate=dateRangeEndStr.split(".");
    var dateRangeEnd = new Date(lstDate[1]+"/"+lstDate[0]+"/"+lstDate[2]);    
    if (dateRangeStart == "NaN" || dateRangeEndStr == "NaN") {
        console.error("fillGanttBlock range is not set correctly: " 
                + dateRangeStartStr + "-" + dateRangeEndStr + " " + msg);
        return;
    }
    var dateRangeStartMillis = dateRangeStart.getTime();
    var dateRangeEndMillis = dateRangeEnd.getTime();
    if (dateRangeStartMillis == "NaN" || dateRangeEndMillis == "NaN") {
        console.error("fillGanttBlock range is not set correctly: " 
                + dateRangeStartStr + "-" + dateRangeEndStr + " " + msg);
        return;
    }

    var rangeWidth = 450;
    var rangeDays = (dateRangeEndMillis-dateRangeStartMillis);
    var rangeFactor = rangeWidth / rangeDays;

    // check if dates are set
    var startMillis = basenode[type + "Start"];
    var endMillis = basenode[type + "Ende"];
    var aufwand = basenode[type + "Aufwand"];
    if (startMillis != null && endMillis != null) {

        var startPos = 0;
        var endPos = 0;
        var rangeAufwand = 0;
        var flgMatchesRange = false;
        
        // add 8h to the end
        endMillis = endMillis + 1000 * 60 * 60 * 8;
        
        // check if range matches
        if (startMillis > dateRangeEndMillis) {
            // sorry you start later
            console.log("fillGanttBlock SKIP sorry you start later: " 
                    + startMillis + ">" + dateRangeEndMillis + " " + msg);
        } else if (endMillis < dateRangeStartMillis) {
            // sorry you end before
            console.log("fillGanttBlock SKIP sorry you start later: " 
                    + endMillis + "<" + dateRangeStartMillis + " " + msg);
        } else {
            // we match
            flgMatchesRange = true;
            if (startMillis > dateRangeStartMillis) {
                //
                startPos = (startMillis - dateRangeStartMillis) * rangeFactor;
            } else {
                // we start on it
                startPos = 0;
            }
            
            if (endMillis < dateRangeEndMillis) {
                //
                endPos = (endMillis - dateRangeStartMillis) * rangeFactor;
            } else {
                // we start on it
                endPos = rangeWidth;
            }
            
            // calc aufwand in range
            rangeAufwand = (endPos - startPos) * aufwand / ((endMillis - startMillis) * rangeFactor);
        }
        
        if (flgMatchesRange) {
            $div.html("&nbsp;");
            $div.css("width", endPos-startPos);
            $div.css("margin-left", startPos);
            
            // show aufwand
            if (rangeAufwand > 0) {
                $divLabel.html("<span class='gantt_aufwand_label'>" 
                                 + label + ":" + "</span>"
                               + "<span class='gantt_aufwand_value'" +
                                     " data-rangeaufwand='" + rangeAufwand + "'>" 
                                 + formatNumbers(rangeAufwand, 0, "h") + "</span");
                $div.attr("data-rangeaufwand", rangeAufwand);
            }
            
            console.log("fillGanttBlock MATCHES width: " 
                    + startPos + "-" + endPos + " aufwand:" + rangeAufwand + " " + msg);
        } else {
            console.log("fillGanttBlock SKIP dates not matched: " + msg);
        }
    } else {
        console.log("fillGanttBlock SKIP no planDates: " + msg);
    }
}

/**
 * <h4>FeatureDomain:</h4>
 *     Layout
 *     Rendering
 * <h4>FeatureDescription:</h4>
 *     Create the gantt-block of specified type: (ist, plan, planChildrenSum, 
 *     istChildrenSum) for basenode. Creates this elements:
 *     <ul>
 *       <li> #gantt_" + type + "_container_" + basenode.sysUID
 *       <li> #gantt_" + type + "_aufwand_" + basenode.sysUID
 *       <li> #gantt_" + type + "_bar_" + basenode.sysUID
 *     </ul>
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>Updates GUI: #gantt_" + type + "_container_" + basenode.sysUID
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Tree Rendering
 * @param basenode - the nodedata to render (java de.yaio.core.node.BaseNode)
 * @param type - the type of data to calc (ist, plan, planChildrenSum, istChildrenSum)
 * @param addStyle - optional css-class to add to t-element
 * @param label - the label to show if aufwand >0
 * @returns JQuery-Html-Object - the rendered ganttblock
 */
function createGanttBlock(basenode, type, addStyle, label) {
    var msg = "ganttblock for node:" + basenode.sysUID;

    // create line
    var $divLine = $("<div id='gantt_" + type + "_container_" + basenode.sysUID + "'" +
            " class ='gantt_container gantt_" + type + "_container'" +
            " lang='tech' data-tooltip='tooltip.hint.Gantt'/>");
    
    // create aufwand
    var $divLabel = $("<div id='gantt_" + type + "_aufwand_" + basenode.sysUID + "'" +
    		" class ='gantt_aufwand ganttblock_" + type + "_aufwand' />");
    $divLabel.addClass(addStyle);
    $divLine.append($divLabel);
    
    // create gantt
    var $div = $("<div id='gantt_" +type + "_bar_" + basenode.sysUID + "'" +
    		" class ='gantt_bar gantt_" +type + "_bar' />");
    $div.addClass(addStyle);
    $divLine.append($div);
    
    // fill gantt
    fillGanttBlock(basenode, type, label, $divLine);
    
    return $divLine;
}

/**
 * <h4>FeatureDomain:</h4>
 *     Layout
 *     Rendering
 * <h4>FeatureDescription:</h4>
 *     renders the full GanttBlock (ist, plan, istChildrenSum, planChildrenSum) 
 *     for basenode and returns a JQuery-Html-Obj.
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>ReturnValue JQuery-Html-Object - the rendered ganttblock
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Tree Rendering
 * @param basenode - the nodedata to render (java de.yaio.core.node.BaseNode)
 * @param fancynode - the corresponding fancynode
 * @returns JQuery-Html-Object - the rendered ganttblock
 */
function renderGanttBlock(basenode, fancynode) {
    // extract nodedata
    var nodestate = basenode.state;
    var statestyle = "node-state-" + nodestate;   

    var msg = "ganttblock for node:" + basenode.sysUID;
    console.log("renderGanttBlock START: " + msg);

    // current ganttblock
    var nodeDataBlock = "";
    var $table = $("<div class='container_gantt_table' />");
    var $row = $("<div class='container_gantt_row'/>");
    $table.append($row);
    
    if (basenode.className == "TaskNode" || basenode.className == "EventNode") {
        // TaskNode
        var $div;
        
        // create plan
        $div = createGanttBlock(basenode, "plan", statestyle, "Plan");
        $row.append($div);
        $div = createGanttBlock(basenode, "planChildrenSum", statestyle, "PlanSum");
        $row.append($div);
        
        // create isst and add stytestyle
        $div = createGanttBlock(basenode, "ist", statestyle, "Ist");
        $row.append($div);
        $div = createGanttBlock(basenode, "istChildrenSum", statestyle, "IstSum");
        $row.append($div);
    } else {
        console.log("renderGanttBlock SKIP no task or event: " + msg);
    }

    console.log("renderGanttBlock DONE: " + msg);

    return $table;
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
function renderColumnsForNode(event, data, preventActionsColum) {
    // extract nodedata
    var node = data.node;
    var basenode = node.data.basenode;
    var nodestate = basenode.state;
    var statestyle = "node-state-" + nodestate;
    var nodeTypeName = basenode.className;
    
    
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
                    + "<a onclick=\"javascript: toggleNodeSysContainer('" + basenode.sysUID + "'); return false;\""
                            + " id='toggler_sys_" + basenode.sysUID + "'"
                            + " class='' "
                            + " data-tooltip='tooltip.command.ToggleSys' lang='tech'></a>"
                    + "</div>"
                + "<a onclick=\"javascript: yaioOpenNodeEditor('" + basenode.sysUID + "', 'edit'); return false;\""
                        + " id='cmdEdit" + basenode.sysUID + "'"
                        + " class='yaio-icon-edit'"
                        + " lang='tech' data-tooltip='tooltip.command.NodeEdit'></a>"
                + "<a onclick=\"javascript: yaioOpenNodeEditor('" + basenode.sysUID + "', 'create'); return false;\""
                        + " id='cmdCreate" + basenode.sysUID + "'"
                        + " class='yaio-icon-create'"
                        + " lang='tech' data-tooltip='tooltip.command.NodeCreateChild'></a>"
                + "<a onclick=\"javascript: yaioOpenNodeEditor('" + basenode.sysUID + "', 'createsymlink'); return false;\""
                        + " id='cmdCreateSymLink" + basenode.sysUID + "'"
                        + " class='yaio-icon-createsymlink'"
                        + " lang='tech' data-tooltip='tooltip.command.NodeCreateSymLink'></a>"
                + "<a onclick=\"javascript: yaioRemoveNodeById('" + basenode.sysUID + "'); return false;\""
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
                + htmlEscapeText(name) + "</span>"))
        ;
    $nameEle.html($div)
    //$tdList.eq(colName).find("span.fancytree-expander").addClass(statestyle);
    
    // render datablock
    var $nodeDataBlock = renderDataBlock(basenode, node);
    
    // add SysData
    // create sys row
    var $row = $("<div class='togglecontainer field_nodeSys' id='detail_sys_" + basenode.sysUID + "' />");
    $nodeDataBlock.append($row);
    $row.append(
            $("<div lang='tech' />").html("Stand: " + formatGermanDateTime(basenode.sysChangeDate))
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
            $("<div lang='tech' />").html("angelegt: " + formatGermanDateTime(basenode.sysCreateDate))
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
                        " onclick=\"toggleNodeDescContainer('" + basenode.sysUID + "'); return false;\"" +
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
            + "<input type='checkbox' id='cmd_toggle_content_desc_" + basenode.sysUID + "' onclick=\"togglePreWrap('#content_desc_" + basenode.sysUID + "');togglePreWrap('#container_content_desc_" + basenode.sysUID + "'); return true;\">"
            + "<span lang='tech'>im Originallayout anzeigen</span>"
    //        + "<input type='checkbox' id='cmd_toggle_content_desc_markdown_" + basenode.sysUID + "' onclick=\"toggleDescMarkdown('#container_content_desc_" + basenode.sysUID + "'); return true;\">"
    //        + "<span lang='tech'>Markdown</span>"
            ;
        commands += "<a class=\"button command-desc-jiraexport\" onClick=\"openJiraExportWindow('"+ basenode.sysUID + "'); return false;" 
            +   "\" lang='tech' data-tooltip='tooltip.command.OpenJiraExportWindow'>common.command.OpenJiraExportWindow</a>";
        commands += "<a class=\"button command-desc-txtexport\" onClick=\"openTxtExportWindow(" 
            +   "$('#container_content_desc_" + basenode.sysUID + "').text()); return false;" 
            +   "\" lang='tech' data-tooltip='tooltip.command.OpenTxtExportWindow'>common.command.OpenTxtExportWindow</a>";
        if ('speechSynthesis' in window) {
            // Synthesis support. Make your web apps talk!
            commands += "<a class=\"button\" onClick=\"openSpeechSynthWindow(" 
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

        // Default Normal
        var descHtmlPre = "<pre class='content-desc pre-wrap' id='content_desc_" + basenode.sysUID + "'>" 
                        + htmlEscapeText(descText) + "</pre>";

        // prepare descText
        var descHtml = formatMarkdown(descText, false, basenode.sysUID);
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
    var $nodeGanttBlock = renderGanttBlock(basenode, node);
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
    toggleNodeSysContainer(basenode.sysUID);
    
    // toogle desc
    toggleNodeDescContainer(basenode.sysUID);

    // calc nodeData
    yaioRecalcMasterGanttBlockFromTree();
};


/**
 * <h4>FeatureDomain:</h4>
 *     Layout Toggler
 * <h4>FeatureDescription:</h4>
 *     Shows the DataBlock<br> 
 *     Toggles DataBlock, GanttBlock and the links #tabTogglerData, #tabTogglerGantt.<br>
 *     Show all: td.block_nodedata, th.block_nodedata + #tabTogglerGantt<br>
 *     Hide all: td.block_nodegantt, th.block_nodegantt + #tabTogglerData<br>
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>Updates DOM
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Tree Rendering
 */
function yaioShowDataBlock() {
    toggleTableBlock("#tabTogglerData");
    toggleTableBlock("td.block_nodegantt, th.block_nodegantt");
    setTimeout(function(){
        toggleTableBlock("#tabTogglerGantt");
        toggleTableBlock("td.block_nodedata, th.block_nodedata");
    }, 400);
    // set it to none: force
    setTimeout(function(){
        $("#tabTogglerData").css("display", "none");
        $("td.block_nodegantt, th.block_nodegantt").css("display", "none");
    }, 400);
}

/**
 * <h4>FeatureDomain:</h4>
 *     Layout Toggler
 * <h4>FeatureDescription:</h4>
 *     Shows the GanttBlock<br> 
 *     Toggles DataBlock, GanttBlock and the links #tabTogglerData, #tabTogglerGantt.<br>
 *     Hide all: td.block_nodedata, th.block_nodedata + #tabTogglerGantt<br>
 *     Show all: td.block_nodegantt, th.block_nodegantt + #tabTogglerData<br>
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>Updates DOM
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Tree Rendering
 */
function yaioShowGanttBlock() {
    toggleTableBlock("#tabTogglerGantt");
    toggleTableBlock("td.block_nodedata, th.block_nodedata");
    setTimeout(function(){
        toggleTableBlock("#tabTogglerData");
        toggleTableBlock("td.block_nodegantt, th.block_nodegantt");
    }, 400);
    // set it to none: force
    setTimeout(function(){
        $("#tabTogglerGantt").css("display", "none");
        $("td.block_nodedata, th.block_nodedata").css("display", "none");
    }, 400);
}


/**
 * <h4>FeatureDomain:</h4>
 *     Gantt
 * <h4>FeatureDescription:</h4>
 *     activate one of the gantt-blocks for the element<br>
 *     When flgMeOnly ist set: activate #gantt_ist_container_ + #gantt_plan_container 
 *     to display only the gantt with the data of this node<br>
 *     When flgMeOnly ist nmot set: activate #gantt_istChildrenSum_container_ + #gantt_planChildrenSum_container 
 *     to display only the gantt with the data of this node and children<br>
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>Updates DOM
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Gantt
 * @param node - the FancytreeNode
 * @param flgMeOnly - true - display only the gantt for the node / false - node+children
 */
function yaioActivateGanttBlock(node, flgMeOnly) {
    if (flgMeOnly) {
        console.debug("yaioActivateGanttBlock: activate gantt - only own data for " + node.key);
        // I'm expanded: show only my own data
        $("#gantt_istChildrenSum_container_" + node.key).css("display", "none");
        $("#gantt_planChildrenSum_container_" + node.key).css("display", "none");
        $("#gantt_ist_container_" + node.key).css("display", "block");
        $("#gantt_plan_container_" + node.key).css("display", "block");
    } else {
        // I'm collapsed: show me and my childsum
        console.debug("yaioActivateGanttBlock: activate gantt - sum data of me+children for " + node.key);
        $("#gantt_ist_container_" + node.key).css("display", "none");
        $("#gantt_plan_container_" + node.key).css("display", "none");
        $("#gantt_istChildrenSum_container_" + node.key).css("display", "block");
        $("#gantt_planChildrenSum_container_" + node.key).css("display", "block");
    }

    // recalc gantt tree
    yaioRecalcMasterGanttBlockFromTree()
}

/**
 * <h4>FeatureDomain:</h4>
 *     Gantt
 * <h4>FeatureDescription:</h4>
 *     recalc all gantt-blocks of the fancytree-nodes (iterates over getRooNode.visit()<br>
 *     calls yaioRecalcGanttBlock for every node and afterwards yaioRecalcMasterGanttBlockFromTree()
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>Updates DOM
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Gantt
 */
function yaioRecalcFancytreeGanttBlocks() {
    if ($("#tree").length > 0) {
        // tree exists
        $("#tree").fancytree("getRootNode").visit(function(node){
            yaioRecalcGanttBlock(node.data.basenode);
        });
    }
    yaioRecalcMasterGanttBlockFromTree()
}

/**
 * <h4>FeatureDomain:</h4>
 *     Gantt
 * <h4>FeatureDescription:</h4>
 *     recalc mastergantt-block for the basenode on top of the page<br>
 *     calls yaioRecalcGanttBlock and yaioRecalcMasterGanttBlockFromTree
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>Updates DOM
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Gantt
 * @param basenode - the basenode to recalc (java de.yaio.core.node.BaseNode)
 */
function yaioRecalcMasterGanttBlock(basenode) {
    // default: set with own
    yaioRecalcGanttBlock(basenode);

    // calc from tree
    yaioRecalcMasterGanttBlockFromTree()
}

/**
 * <h4>FeatureDomain:</h4>
 *     Gantt
 * <h4>FeatureDescription:</h4>
 *     recalc mastergantt-block from the tree-data<br>
 *     extract nodeid of the masternode from "#masterTr.data-value"<br>
 *     calls yaioRecalcMasterGanttBlockLine for plan+ist
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>Updates DOM
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Gantt
 */
function yaioRecalcMasterGanttBlockFromTree() {
    // calc from children
    var masterNodeId = $("#masterTr").attr('data-value');
    yaioRecalcMasterGanttBlockLine(masterNodeId, "plan");
    yaioRecalcMasterGanttBlockLine(masterNodeId, "ist");
}

/**
 * <h4>FeatureDomain:</h4>
 *     Gantt
 * <h4>FeatureDescription:</h4>
 *     Recalcs mastergantt-line for praefix (plan, ist) with the tree-data<br>
 *     It extracts the data-rangeaufwand from gantt_${praefix}ChildrenSum_bar_$masterNodeId<br>
 *     It iterates over all visible div.gantt_$praefix_bar, div.gantt_${praefix}ChildrenSum_bar
 *     and adds their data-rangeaufwand<br>
 *     At the end the sumRangeAufwand will be placed on #gantt_${praefix}ChildrenSum_aufwand_{masterNodeId}
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>Updates DOM
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *    Gantt
 * @param masterNodeId - id of the masterNode on top of the page
 * @param praefix - datablock to racalc (plan, ist)
 */
function yaioRecalcMasterGanttBlockLine(masterNodeId, praefix) {
    // calc rangeAufwand
    var sumRangeAufwand = 0;

    // init with aufwand of the masternode
    var masterBarId = "#gantt_" + praefix + "_bar_" + masterNodeId;
    var $masterBar = $(masterBarId);
    if ($masterBar.length > 0) {
        sumRangeAufwand = parseFloat($masterBar.attr("data-rangeaufwand"));
        console.log("yaioRecalcMasterGanttBlock type=" + praefix + " found masterrangeaufwand :" + sumRangeAufwand + " for " + masterBarId);
    } else {
        console.log("yaioRecalcMasterGanttBlock type=" + praefix + " no masterrangeaufwand :" + sumRangeAufwand + " for " + masterBarId);
    }
    
    // check for tree
    var treeId = "#tree";
    var tree = $(treeId).fancytree("getTree");
    if ($(treeId).length <= 0 || !tree || tree == "undefined" ) {
        logError("yaioRecalcMasterGanttBlock: error tree:'" + treeId + "' not found.", false);
        return;
    }
    
    // filter ganttblocks
    var filter = "div.gantt_" + praefix + "_bar, div.gantt_" + praefix + "ChildrenSum_bar";
    var $ganttBars = $(filter).filter(function () { return $(this).parent().css('display') == 'block' })
    console.log("yaioRecalcMasterGanttBlock type=" + praefix + " found:" + $ganttBars.length + " for filter:" + filter);
    if ($ganttBars.length > 0) {
        $($ganttBars).each( function () {
            // check if node is visible
            var nodeId = this.id;
            nodeId = nodeId.replace(/gantt_(.*)bar_/, "");
            var treeNode = tree.getNodeByKey(nodeId);
            if (treeNode && treeNode.isVisible()) {
                // node is visible: calc
                var rangeAufwand = $(this).attr("data-rangeaufwand");
                if (this.id.indexOf(masterNodeId) <= 0) {
                    console.log("yaioRecalcMasterGanttBlock type=" + praefix + " found rangeaufwand :" + rangeAufwand + " for " + this.id);
                    sumRangeAufwand += parseFloat(rangeAufwand);
                    
                } else {
                    console.log("yaioRecalcMasterGanttBlock type=" + praefix + " ignore rangeaufwand from master:" + rangeAufwand + " for " + this.id);
                }
            } else if (! treeNode) {
                // not found
                console.log("yaioRecalcMasterGanttBlock type=" + praefix + " skip node not found nodeId:" + nodeId + " for " + this.id);
            } else {
                // not visble
                console.log("yaioRecalcMasterGanttBlock type=" + praefix + " skip node not visble nodeId:" + nodeId + " for " + this.id);
            }
        });
    }
    console.log("yaioRecalcMasterGanttBlock type=" + praefix + " calced rangeaufwand :" + sumRangeAufwand + " for " + masterNodeId);

    // update masterBlock
    var type = praefix + "ChildrenSum";
    var $divLine = $("#gantt_" + type + "_container_" + masterNodeId);
    var $divLabel = $($divLine).find("#gantt_" + type + "_aufwand_" + masterNodeId);
    var $div = $($divLine).find("#gantt_" + type + "_bar_" + masterNodeId);
    $divLabel.html("");
    if (sumRangeAufwand > 0)  {
        console.log("yaioRecalcMasterGanttBlock type=" + praefix + " set gantt_aufwand_label with calced rangeaufwand :" + sumRangeAufwand + " for " + masterNodeId);
        $divLabel.html("<span class='gantt_aufwand_label'>" 
                + praefix + "Sum:" + "</span>"
              + "<span class='gantt_aufwand_value'" +
                    ">" 
                + formatNumbers(sumRangeAufwand, 0, "h") + "</span");
    } else {
        console.log("yaioRecalcMasterGanttBlock type=" + praefix + " hide gantt_aufwand_label because no calced rangeaufwand :" + sumRangeAufwand + " for " + masterNodeId);
    }
}

/**
 * <h4>FeatureDomain:</h4>
 *     Gantt
 * <h4>FeatureDescription:</h4>
 *     recalc gantt-block for the basenode<br>
 *     calls fillGanttBlock for (plan, ist, planChildrenSum, istChildrenSum)
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>Updates DOM
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Gantt
 * @param basenode - the basenode to recalc (java de.yaio.core.node.BaseNode)
 */
function yaioRecalcGanttBlock(basenode) {
    fillGanttBlock(basenode, "plan", "Plan", null);
    fillGanttBlock(basenode, "planChildrenSum", "PlanSum", null);
    fillGanttBlock(basenode, "ist", "Ist", null);
    fillGanttBlock(basenode, "istChildrenSum", "IstSum", null);
}

function openNodeHierarchy(treeId, lstIdsHierarchy) {
    // check for tree
    var tree = $(treeId).fancytree("getTree");
    if (! tree) {
        logError("openHierarchy: error tree:'" + treeId + "' not found.", false);
        return;
    }

    // check for rootNode
    var rootNode = tree.rootNode;
    if (! rootNode) {
        logError("openHierarchy: error for tree:'" + treeId 
                    + "' rootNode not found.", false);
        return;
    }
    
    // check for lstIdsHierarchy
    if (! lstIdsHierarchy || lstIdsHierarchy.length <= 0) {
        logError("openHierarchy: error for tree:'" + treeId 
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
        logError("openHierarchy: error for tree:'" + treeId 
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

function openNodeHierarchyForNodeId(treeId, activeNodeId) {
    // check for tree
    var tree = $(treeId).fancytree("getTree");
    if (! tree) {
        logError("openNodeHierarchyForNodeId: error tree:'" + treeId + "' not found.", false);
        return;
    }
    
    // check for activeNodeId
    var treeNode = tree.getNodeByKey(activeNodeId);
    if (! treeNode) {
        logError("openNodeHierarchyForNodeId: error for tree:'" + treeId 
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
    openNodeHierarchy(treeId, lstIdsHierarchy);
}


function yaioOpenSubNodesForTree(treeId, level) {
    var tree = $(treeId).fancytree("getTree");
    if (! tree) {
        logError("yaioOpenSubNodesForTree: error tree:'" + treeId + "' not found.", false);
        return;
    }
    
    // check for activeNodeId
    var treeNode = tree.rootNode;
    if (! treeNode) {
        logError("yaioOpenSubNodesForTree: error rootnode for tree:'" + treeId 
                + " not found.", false);
        return null;
    }
    
    var opts = {};
    opts.minExpandLevel = level;
    opts.recursively = true;
    console.log("yaioOpenSubNodesForTree setExpanded:" + " level:" + level);
    treeNode.setExpanded(true, opts);
}

function yaioSaveNode(data) {
    var json = JSON.stringify({name: data.input.val()});
    var url = updateUrl + data.node.key;
    doUpdateNode(data.node, url, json);
}

function yaioMoveNode(node, newParentKey, newPos) {
    console.log("move node:" + node.key + " to:" + newParentKey + " Pos:" + newPos);
    var json = JSON.stringify({parentNode: newParentKey});
    var url = moveUrl + node.key + "/" + newParentKey + "/" + newPos;
    yaioDoUpdateNode(node, url, json);
}

function yaioRemoveNodeById(nodeId) {
    if (confirm("Wollen Sie die Node wirklich l&ouml;schen?")) {
        console.log("remove node:" + nodeId);
        // check for tree
        var treeId = "#tree";
        var tree = $(treeId).fancytree("getTree");
        if (! tree) {
            logError("yaioRemoveNode: error tree:'" + treeId + "' not found.", false);
            return;
        }
        
        // check for activeNodeId
        var treeNode = tree.getNodeByKey(nodeId);
        if (! treeNode) {
            logError("yaioRemoveNode: error for tree:'" + treeId 
                    + "' activeNode " + nodeId + " not found.", false);
            return null;
        }
        var url = removeUrl + nodeId;
        yaioDoRemoveNode(treeNode, url);
    } else {
        // discard
        return false;
    }
}

/*****************************************
 *****************************************
 * Service-Funktions (webservice)
 *****************************************
 *****************************************/
function yaioDoUpdateNode(node, url, json) {
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
                        openNodeHierarchy("#tree", response.parentIdHierarchy);
                    });
                } else {
                    logError("got no hierarchy for:" + node.key 
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
                        logError("violations while save node:" + node.key 
                                + " field:" + violation.path + " message:" + violation.message, false);
                        message = message +  violation.path + " (" + violation.message + "),";
                    }
                }
                logError(message, true)
            }
        },
        error : function(jqXHR, textStatus, errorThrown) {
            // log the error to the console
            logError("The following error occured: " + textStatus + " " + errorThrown, true);
            logError("cant save node:" + node.key + " error:" + textStatus)
        },
        complete : function() {
            console.log("update node:" + node.key + "' ran");
        }
    });
}

function yaioLoadSymLinkData(basenode, fancynode) {
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
                    var $nodeDataBlock = renderDataBlock(response.node, fancynode);
                    
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
                        logError("error yaioLoadSymLinkData: cant load node - " + msg, false);
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
                    logError("ERROR got no " + msg, true);
                }
            } else {
                logError("ERROR cant load  " + msg + " error:" + response.stateMsg, true);
            }
        },
        error : function(jqXHR, textStatus, errorThrown) {
            // log the error to the console
            logError("ERROR  " + msg + " The following error occured: " + textStatus + " " + errorThrown, false);
            logError("cant load " + msg + " error:" + textStatus, true)
        },
        complete : function() {
            console.log("completed load " + msg);
        }
    });
}


function yaioDoRemoveNode(node, url) {
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
                        openNodeHierarchy("#tree", response.parentIdHierarchy);
                    });
                } else {
                    logError("got no hierarchy for:" + node.key 
                            + " hierarchy:" + response.parentIdHierarchy, true);
                }
            } else {
                logError("cant remove node:" + node.key + " error:" + response.stateMsg, false);
                // check for violations
                if (response.violations) {
                    // iterate violations
                    for (var idx in response.violations) {
                        var violation = response.violations[idx];
                        logError("violations while remove node:" + node.key 
                                + " field:" + violation.path + " message:" + violation.message, false);
                        alert("cant remove node because: " + violation.path + " (" + violation.message + ")")
                    }
                }
            }
        },
        error : function(jqXHR, textStatus, errorThrown) {
            // log the error to the console
            logError("The following error occured: " + textStatus + " " + errorThrown, false);
            logError("cant remove node:" + node.key + " error:" + textStatus, true)
        },
        complete : function() {
            console.log("remove node:" + node.key + "' ran");
        }
    });
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
function openJiraExportWindow(nodeId) {
    // check vars
    if (! nodeId) {
        // tree not found
        logError("error openJiraWindow: nodeId required", false);
        return null;
    }
    // load node
    var tree = $("#tree").fancytree("getTree");
    if (!tree) {
        // tree not found
        logError("error openJiraWindow: cant load tree for node:" + nodeId, false);
        return null;
    }
    var treeNode = tree.getNodeByKey(nodeId);
    if (! treeNode) {
        logError("error openJiraWindow: cant load node:" + nodeId, false);
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
    var nodeDesc = convertMarkdownToJira(descText);
    nodeDesc = htmlEscapeText(nodeDesc);
    
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
 *     open the clipboardwindow for the explorercontent 
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: opens clipboard window with checklist-converted node-content
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Convert
 */
function yaioExportExplorerLinesAsCheckList() {
    // convert and secure
    var checkListSrc = convertExplorerLinesAsCheckList();
    checkListSrc = htmlEscapeText(checkListSrc);
    
    // set clipboard-content
    $( "#clipboard-content" ).html(checkListSrc);
    
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
 *     open the txtwindow for the node  
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: opens txt-window with txt node-content
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Convert
 * @param content - txt content
 */
function openTxtExportWindow(content) {
    // secure
    content = htmlEscapeText(content);

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
