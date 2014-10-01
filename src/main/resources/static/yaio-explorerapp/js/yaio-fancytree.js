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
 *     controller for the yaio-app and treeview
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
 * @param doneHandler - 
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
 * @param doneHandler - 
 */
function yaioCreateFancyTree(treeId, masterNodeId, doneHandler){
    treeInstances[treeId] = {};
    treeInstances[treeId].state = "loading";
    $(treeId).fancytree({
        
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
  
  
        // parse the nodedata
        postProcess: function(event, data) {
            postProcessNodeData(event, data);
        },

        // render the extra nodedata in grid
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
        }

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
//                node.remove();
                yaioRemoveNodeById(node.key, 'create');
                break;
            case "addChild":
//                node.editCreateNode("child", "New node");
                yaioOpenNodeEditor(node.key, 'create');
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
            {title: "Edit <kbd>[F2]</kbd>", cmd: "rename", uiIcon: "ui-icon-pencil" },
            {title: "Delete <kbd>[Del]</kbd>", cmd: "remove", uiIcon: "ui-icon-trash" },
            {title: "----"},
//            {title: "New sibling <kbd>[Ctrl+N]</kbd>", cmd: "addSibling", uiIcon: "ui-icon-plus" },
            {title: "New child <kbd>[Ctrl+Shift+N]</kbd>", cmd: "addChild", uiIcon: "ui-icon-arrowreturn-1-e" },
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

    if (basenode.className == "UrlResNode") {
        datanode.title = basenode.resLocName;
    }
    
    return datanode;
} 

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

function renderDataBlock(basenode, fancynode) {
    // extract nodedata
    var nodestate = basenode.state;
    var statestyle = "node-state-" + nodestate;   

    var msg = "datablock for node:" + basenode.sysUID;
    console.log("renderDataBlock START: " + msg);

    // current datablock
    var nodeDataBlock = "";
    var $table = $("<div class='container_data_table' />");
    var $row = $("<div class='container_data_row'/>");
    $table.append($row);
    
    // default fields
    $row.append($("<div />").html(htmlEscapeText(basenode.metaNodePraefix + basenode.metaNodeNummer))
            .addClass("container_field")
            .addClass("fieldtype_metanummer")
            .addClass("field_metanummer")
            );
        
    $row.append($("<div lang='tech' />").html(basenode.className)
           .addClass("container_field")
           .addClass("fieldtype_type")
           .addClass("field_type")
           .addClass(statestyle));
//    $row.append($("<div lang='tech' />").html(basenode.state)
//            .addClass("container_field")
//            .addClass("fieldtype_state")
//            .addClass("field_state")
//            .addClass(statestyle));
    if (basenode.className == "TaskNode" || basenode.className == "EventNode") {
        // TaskNode
        $row.append(
                $("<div />").html("&nbsp;" + formatNumbers(basenode.istChildrenSumStand, 2, "%"))
                        .addClass("container_field")
                        .addClass("fieldtype_stand")
                        .addClass("field_istChildrenSumStand")
                        .addClass(statestyle)
                        ); 
        $row.append(
                $("<div />").html("&nbsp;" + formatNumbers(basenode.istChildrenSumAufwand, 2, "h"))
                        .addClass("container_field")
                        .addClass("fieldtype_aufwand")
                        .addClass("field_istChildrenSumAufwand")
                        .addClass(statestyle)
                        );
        $row.append(
                $("<div />").html("&nbsp;" + formatGermanDate(basenode.istChildrenSumStart)
                        + "-" + formatGermanDate(basenode.istChildrenSumEnde))
                         .addClass("container_field")
                         .addClass("fieldtype_fromto")
                         .addClass("field_istChildrenSum")
                         .addClass(statestyle)
                         );
        $row.append(
                $("<div />").html("&nbsp;" + formatNumbers(basenode.planChildrenSumAufwand, 2, "h"))
                         .addClass("container_field")
                         .addClass("fieldtype_aufwand")
                         .addClass("field_planChildrenSumAufwand")
                         .addClass(statestyle)
                         );
        $row.append(
                $("<div />").html("&nbsp;" + formatGermanDate(basenode.planChildrenSumStart)
                         + "-" + formatGermanDate(basenode.planChildrenSumEnde))
                         .addClass("container_field")
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
                              .addClass("fieldtype_url")
                              .addClass("field_resLocRef")
    //                          .addClass(statestyle)
                              ); 
        }
    
        // both
        if (   basenode.docLayoutTagCommand || basenode.docLayoutShortName
            || basenode.docLayoutAddStyleClass || basenode.docLayoutFlgCloseDiv) {
                $row.append(
                        $("<div lang='tech' />").html("Layout ")
                                .addClass("container_field")
                                .addClass("fieldtype_ueDocLayout")
                                .addClass("field_ueDocLayout")
//                                    .addClass(statestyle)
                                ); 
            
            // check which docLayout is set    
            if (basenode.docLayoutTagCommand) {
                $row.append(
                        $("<div lang='tech' />").html("Tag: " 
                                    + htmlEscapeText(basenode.docLayoutTagCommand))
                                .addClass("container_field")
                                .addClass("fieldtype_docLayoutTagCommand")
                                .addClass("field_docLayoutTagCommand")
//                                    .addClass(statestyle)
                                ); 
            }
            if (basenode.docLayoutAddStyleClass) {
                $row.append(
                        $("<div lang='tech' />").html("Style: " 
                                    + htmlEscapeText(basenode.docLayoutAddStyleClass))
                                .addClass("container_field")
                                .addClass("fieldtype_docLayoutAddStyleClass")
                                .addClass("field_docLayoutAddStyleClass")
//                                    .addClass(statestyle)
                                ); 
            }
            if (basenode.docLayoutShortName) {
                $row.append(
                        $("<div lang='tech' />").html("Kurzname: " 
                                    + htmlEscapeText(basenode.docLayoutShortName))
                                .addClass("container_field")
                                .addClass("fieldtype_docLayoutShortName")
                                .addClass("field_docLayoutShortName")
//                                    .addClass(statestyle)
                                ); 
            }
            if (basenode.docLayoutFlgCloseDiv) {
                $row.append(
                        $("<div lang='tech' />").html("Block schlie&szligen!")
                                .addClass("container_field")
                                .addClass("fieldtype_docLayoutFlgCloseDiv")
                                .addClass("field_docLayoutFlgCloseDiv")
//                                    .addClass(statestyle)
                                ); 
            }
        }
    } else if (basenode.className == "SymLinkNode") {
        yaioLoadSymLinkData(basenode, fancynode);
    } 

    console.log("renderDataBlock DONE: " + msg);

    return $table;
}

function renderColumnsForNode(event, data) {
    // extract nodedata
    var node = data.node;
    var basenode = node.data.basenode;
    var nodestate = basenode.state;
    var statestyle = "node-state-" + nodestate;
    var nodeTypeName = basenode.className;
    
    
    var colName = 0;
    var colData = 1;
    var colActions = 2;
    
    // get tdlist
    var $tdList = $(node.tr).find(">td");

    // (index #0 is rendered by fancytree by adding the checkbox)
//    $tdList.eq(1).text(node.getIndexHier()).addClass("alignRight").addClass(statestyle);

    // add stateclasss to tr
    $(node.tr)
       //.addClass(statestyle)
       .addClass("container_nodeline");
    
    // add fields
    $tdList.eq(colActions).html(
            "<a href='#/show/" + basenode.sysUID + "'"
                    + " class='yaio-icon-center'"
                    + " data-tooltip='Zeige nur diesen Teilbaum mit allen Kindselementen'></a>"
            + "<a onclick=\"javascript: yaioOpenNodeEditor('" + basenode.sysUID + "', 'edit'); return false;\""
                    + " class='yaio-icon-edit'"
                    + " data-tooltip='Bearbeite die Daten'></a>"
            + "<a onclick=\"javascript: yaioOpenNodeEditor('" + basenode.sysUID + "', 'create'); return false;\""
                    + " class='yaio-icon-create'"
                    + " data-tooltip='Erzeuge ein neues KindsElement'></a>"
            + "<a onclick=\"javascript: yaioOpenNodeEditor('" + basenode.sysUID + "', 'createsymlink'); return false;\""
                    + " class='yaio-icon-createsymlink'"
                    + " data-tooltip='Erzeuge einen SymLink der auf dieses Element verweist'></a>"
            + "<a onclick=\"javascript: yaioRemoveNodeById('" + basenode.sysUID + "'); return false;\""
                    + " class='yaio-icon-remove'"
                    + " data-tooltip='L&ouml;sche dieses Element'></a>"
            ).addClass("container_field")
             .addClass("fieldtype_actions")
             //.addClass(statestyle)
             ;

    // manipulate name-column
    $tdList.eq(colName).addClass("container_field")
                 .addClass("fieldtype_name")
                 .addClass("field_name")
                 //.addClass(statestyle)
                 ;
    // insert state before name-span
    var $nameEle = $tdList.eq(colName).find("span.fancytree-title");
    var $div = $("<div style='disply: block-inline' />")
        .append($("<span class='" + statestyle + " fancytree-title-state' lang='de' />")
                    .html(basenode.state + " ")
                    )
        .append("&nbsp;")
        .append($("<span class=''>" + htmlEscapeText(basenode.name) + "</span>"))
        ;
    $nameEle.html($div)
    //$tdList.eq(colName).find("span.fancytree-expander").addClass(statestyle);
    
    // render datablock
    var $nodeDataBlock = renderDataBlock(basenode, node);

    // add nodeDesc if set
    if (basenode.nodeDesc != "" && basenode.nodeDesc != null) {
        // columncount
        //var columnCount = $(">td", $nodedataBlock).length;
        
        // add toggler column
        $($nodeDataBlock).find("div.container_data_row").append(
                $("<div />").html("<a href='#'" +
                        " onclick=\"toggleNodeDescContainer('" + basenode.sysUID + "'); return false;\"" +
                            " id='toggler_desc_" + basenode.sysUID + "'" +
                            " data-tooltip='Zeige die detaillierte Beschreibung an'></a>")
                        .addClass("container_field")
                        .addClass("fieldtype_descToggler")
                        .addClass("toggler_show")
                        //.addClass(statestyle)
                        );
        
        // add desc row
        $nodeDataBlock.append(
                $("<div class='togglecontainer' id='detail_desc_" + basenode.sysUID + "'>"
                        + "<pre>" + htmlEscapeText(basenode.nodeDesc).replace(/\<WLBR\>/g, "\n") + "</pre>"
                        + "</div>").addClass("field_nodeDesc"));
    }
    
    // add nodeData
    $tdList.eq(colData).html($nodeDataBlock).addClass("block_nodedata");
    
    // toogle
    toggleNodeDescContainer(basenode.sysUID);
};

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

/*****************************************
 *****************************************
 * Service-Funktions (editor)
 *****************************************
 *****************************************/


/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     reset editor (hide all form, empty all formfields)
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: hide editor
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor
 */
function yaioResetNodeEditor() {
    // reset editor
    console.log("yaioResetNodeEditor: show tree, hide editor");
    
    // show full tree
    $("#containerYaioTree").css("width", "100%");
    
    // hide editor-container
    $("#containerYaioEditor").css("width", "100%");
    $("#containerYaioEditor").css("display", "none");
    
    // hide editor-box
    $("#containerBoxYaioEditor").css("width", "100%");
    $("#containerBoxYaioEditor").css("display", "none");
    
    // hide forms
    yaioHideAllNodeEditorForms();
    yaioResetNodeEditorFormFields();
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     hide all editor-forms
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: hide all editor-forms 
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor
 */
function yaioHideAllNodeEditorForms() {
    // reset editor
    console.log("yaioHideAllNodeEditorForms: hide forms");
    // hide forms
    $("#containerFormYaioEditorCreate").css("display", "none");
    $("#containerFormYaioEditorTaskNode").css("display", "none");
    $("#containerFormYaioEditorEventNode").css("display", "none");
    $("#containerFormYaioEditorInfoNode").css("display", "none");
    $("#containerFormYaioEditorUrlResNode").css("display", "none");
    $("#containerFormYaioEditorSymLinkNode").css("display", "none");
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     reset all formfields
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: empty all formfields 
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor
 */
function yaioResetNodeEditorFormFields() {
    // reset data
    // configure value mapping
    var basenode = {};
    for (var formName in configNodeTypeFields) {
        var fields = new Array();
        fields = fields.concat(configNodeTypeFields.Common.fields);
        fields = fields.concat(configNodeTypeFields[formName].fields);
        for (var idx in fields) {
            var field = fields[idx];
            yaioSetFormField(field, formName, basenode);
        }
    }
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     updates the formfield with the nodedata
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: updates formfield 
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor
 * @param field - fieldconfig from configNodeTypeFields
 * @param fieldSuffix - sufix of the fieldName to identify the form (nodeclass of basenode)
 * @param basenoe - the node to map the fieldvalue
 */
function yaioSetFormField(field, fieldSuffix, basenode) {
    var fieldName = field.fieldName;
    var fieldNameId = "#input" + fieldName.charAt(0).toUpperCase() + fieldName.slice(1) + fieldSuffix;
    var value = basenode[fieldName];
    
    // convert value
    if (field.datatype == "integer" && (! value || value == "undefined" || value == null)) {
        // specical int
        value = 0
    } else if (field.datatype == "date")  {
        // date
        value = formatGermanDate(value);
    } else if (field.datatype == "datetime")  {
        // date
        value = formatGermanDateTime(value);
    } else if (! value || value == "undefined" || value == null) {
        // alle other
        value = "";
    } 
    
    // set depending on the fieldtype
    if (field.type == "hidden") {
        $(fieldNameId).val(value).trigger('input').triggerHandler("change");
    } else if (field.type == "select") {
        $(fieldNameId).val(value).trigger('select').triggerHandler("change");
    } else if (field.type == "checkbox") {
        if (value) {
            $(fieldNameId).prop("checked", true);
        } else {
            $(fieldNameId).prop("checked", false);
        }
        $(fieldNameId).trigger('input').triggerHandler("change");
    } else if (field.type == "textarea") {
        $(fieldNameId).val(value).trigger('select').triggerHandler("change");
    } else {
        // input
        $(fieldNameId).val(value).trigger('input');
    }
    console.log("yaioSetFormField map nodefield:" + fieldName 
            + " set:" + fieldNameId + "=" + value);
    
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     open the nodeeditor for the node (toggle it fromleft), transfer the data from node to the formfields  
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: reset forms+field, hide forms, open the spcific form for the nodeclass, updates fields
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor
 * @param nodeId - id of the node
 * @param mode - edit, create, createsymlink
 */
function yaioOpenNodeEditor(nodeId, mode) {
    // reset editor
    console.log("yaioOpenNodeEditor: reset editor");
    yaioResetNodeEditor();
    
    // check vars
    if (! nodeId) {
        // tree not found
        logError("error yaioOpenNodeEditor: nodeId required", false);
        return null;
    }
    // load node
    var tree = $("#tree").fancytree("getTree");
    if (!tree) {
        // tree not found
        logError("error yaioOpenNodeEditor: cant load tree for node:" + nodeId, false);
        return null;
    }
    var treeNode = tree.getNodeByKey(nodeId);
    if (! treeNode) {
        logError("error yaioOpenNodeEditor: cant load node:" + nodeId, false);
        return null;
    }
    
    // extract nodedata
    var basenode = treeNode.data.basenode;
        
    // check mode    
    var fields = new Array();
    var formSuffix, fieldSuffix;
    var origBasenode = basenode;
    if (mode == "edit") {
        // mode edit
        
        // configure value mapping
        fields = fields.concat(configNodeTypeFields.Common.fields);
        if (basenode.className == "TaskNode") {
            fields = fields.concat(configNodeTypeFields.TaskNode.fields);
        } else if (basenode.className == "EventNode") {
            fields = fields.concat(configNodeTypeFields.EventNode.fields);
        } else if (basenode.className == "InfoNode") {
            fields = fields.concat(configNodeTypeFields.InfoNode.fields);
        }  else if (basenode.className == "UrlResNode") {
            fields = fields.concat(configNodeTypeFields.UrlResNode.fields);
        }  else if (basenode.className == "SymLinkNode") {
            fields = fields.concat(configNodeTypeFields.SymLinkNode.fields);
        }
        
        // set formSuffix
        formSuffix = basenode.className;
        fieldSuffix = basenode.className;
        basenode.mode = "edit";
        console.log("yaioOpenNodeEditor mode=edit for node:" + nodeId);
    } else if (mode == "create") {
        // mode create
        formSuffix = "Create";
        fieldSuffix = "Create";
        fields = fields.concat(configNodeTypeFields.Create.fields);
        
        // new basenode
        basenode = {
                mode: "create",
                sysUID: origBasenode.sysUID
        };
        console.log("yaioOpenNodeEditor mode=create for node:" + nodeId);
    } else if (mode == "createsymlink") {
        // mode create
        formSuffix = "SymLinkNode";
        fieldSuffix = "SymLinkNode";
        fields = fields.concat(configNodeTypeFields.CreateSymlink.fields);

        // new basenode
        basenode = {
                mode: "create",
                sysUID: origBasenode.sysUID,
                name: "Symlink auf: '" + origBasenode.name + "'",
                type: "SYMLINK",
                state: "SYMLINK",
                className: "SymLinkNode",
                symLinkRef: origBasenode.metaNodePraefix + "" + origBasenode.metaNodeNummer
        };
        console.log("yaioOpenNodeEditor mode=createsymlink for node:" + nodeId);
    } else {
        logError("error yaioOpenNodeEditor: unknown mode" + mode 
                + " for nodeId:" + nodeId, false);
        return null;
    }
        
    // iterate fields
    for (var idx in fields) {
        var field = fields[idx];
        yaioSetFormField(field, fieldSuffix, basenode);
    }
    
    // show editor
    var width = $("#box_data").width();
    console.log("yaioOpenNodeEditor show editor: " + formSuffix 
            + " for node:" + nodeId);

    // set width
    $("#containerYaioEditor").css("width", "600px");
    $("#containerBoxYaioEditor").css("width", "600px");
    $("#containerYaioTree").css("width", (width - $("#containerYaioEditor").width() - 30) + "px");
    
    // display editor and form for the formSuffix
    $("#containerBoxYaioEditor").css("display", "block");
    $("#containerFormYaioEditor" + formSuffix).css("display", "block");
    //$("#containerYaioEditor").css("display", "block");
    toggleElement("#containerYaioEditor");
    
    // add datepicker to all dateinput
    $.datepicker.setDefaults($.datepicker.regional['de']);
    $.timepicker.regional['de'] = {
            timeOnlyTitle: 'Uhrzeit auswählen',
            timeText: 'Zeit',
            hourText: 'Stunde',
            minuteText: 'Minute',
            secondText: 'Sekunde',
            currentText: 'Jetzt',
            closeText: 'Auswählen',
            ampm: false
          };
    $.timepicker.setDefaults($.timepicker.regional['de']);    
    $('input.inputtype_date').datepicker();
    $('input.inputtype_datetime').datetimepicker();
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     close the nodeditor, toggle it to the left
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: close the editor
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Tree Editor
 */
function yaioCloseNodeEditor() {
    console.log("close editor");
    toggleElement("#containerYaioEditor");
    yaioResetNodeEditor();
} 




/**
 * <h4>FeatureDomain:</h4>
 *     GUI Download
 * <h4>FeatureDescription:</h4>
 *     open the outputOptionsEditor
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>open the outputOptionsEditor
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI
 * @param sysUID - the sysUID of the current node
 * @param url - the url to send
 * @param target - the target window-name
 */
function yaioOpenOutputOptionsEditor(sysUID, url, target) {
    var formId = "#nodeFormOutputOptions";
    console.log("OutputOptionsEditor:" + " url:" + url);
    $("#containerFormYaioEditorOutputOptions").css("display", "node");
    toggleElement("#containerFormYaioEditorOutputOptions");
    $(formId).attr("target", target);
    $(formId).attr("action", url);
    $(formId).trigger('form').triggerHandler("change");
    $(formId).trigger('input');
    
    return false;
}

function yaioSendOutputOptionsEditor() {
    var formId = "#nodeFormOutputOptions";
    $(formId).submit();
    
    return false;
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     close the outüutoptionseditor, toggle it to the left
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: close the editor
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Tree Editor
 */
function yaioCloseOutputOptionsEditor() {
    console.log("close OutputOptionseditor");
    toggleElement("#containerFormYaioEditorOutputOptions");
    return false;
} 




/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     add speechRecognition to name+nodeDesc-Label if availiable<br>
 *     set the flg webkitSpeechRecognitionAdded on the element, so that there is no doubling
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: add speechrecognition to elements
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor SpeechRecognition
 */
function addSpeechRecognitionToElements() {
    // add speechrecognitionif availiable
    if (('webkitSpeechRecognition' in window)) {
        // add speechrecognition to nodeDesc+name
        $("label[for='nodeDesc'], label[for='name']").append(function (idx) {
            var link = "";
            var label = this;
            
            // check if already set
            if ($(label).attr("webkitSpeechRecognitionAdded")) {
                console.error("addSpeechRecognitionToElements: SKIP because already added: " + $(label).attr("for"));
                return link;
            }

            // get corresponding form
            var forName = $(label).attr("for");
            var form = $(label).closest("form");
            
            // get for-element byName from form
            var forElement = form.find("[name="+ forName + "]").first();
            if (forElement.length > 0) {
                // define link to label
                link = "<a href=\"\" class=\"\"" +
                    " onClick=\"openSpeechRecognitionWindow(" +
                        "document.getElementById('" + forElement.attr('id') + "')); return false;" +
                    "\" data-tooltip='Spracherkennung nutzen'>" +
                    "<img alt='Spracherkennung nutzen' style='width:25px'" +
                        " src='https://www.google.com/intl/en/chrome/assets/common/images/content/mic.gif'></a>";
                
                // set flag
                $(label).attr("webkitSpeechRecognitionAdded", "true")
                console.log("addSpeechRecognitionToElements: add : " + forName + " for " + forElement.attr('id'));
            }
            return link;
        });
    }
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     open speechrecognition for element
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: open speechrecognition for element
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor SpeechRecognition
 * @param target - target-element to update (HTML-Element)
 */
function openSpeechRecognitionWindow(target) {
    if (target == null) target = self;
    target.focus();
    var speechrecognitionWindow = window.open('speechrecognition.html', "speechrecognition", "width=690,height=350,resizable=yes,dependent=yes,scrollbars=yes");
    speechrecognitionWindow.focus();
    if (speechrecognitionWindow.opener == null) { speechrecognitionWindow.opener = self; }
    speechrecognitionWindow.opener.targetElement = target;
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     a hack to call updatetrigger for the element because for speechregognition the popup
 *     cant call the trigger for another window (security)<br>
 *     the function binds to the current document-window
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: calls updatetrigger for element
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor SpeechRecognition
 * @param element - element (HTML-Element) to fire the trigger
 */
self.callUpdateTriggerForElement = function (element) {
    if (element != null) {
        $(element).trigger('input').triggerHandler("change");
        $(element).trigger('select').triggerHandler("change");
        $(element).trigger('input');
        $(element).focus();
    }
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     init the multilanguage support for all tags wirth attribute <XX lang="de">
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: init multilanguage-support
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor Multilanguagesupport
 */
function initLanguageSupport() {
    // Create language switcher instance and set default language to tech
    window.lang = new Lang('tech');

    //Define the de language pack as a dynamic pack to be loaded on demand
    //if the user asks to change to that language. We pass the two-letter language
    //code and the path to the language pack js file
    window.lang.dynamic('de', 'lang/lang-tech-to-de.json');
    window.lang.loadPack('de');

    // change to de
    window.lang.change('de');
}


/*****************************************
 *****************************************
 * Service-Funktions (busnesslogic)
 *****************************************
 *****************************************/

/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     recalcs the istStand depending on the state/type
 *     if ERLEDIGT || VERWORFEN || EVENT_ERLEDIGT || EVENT_VERWORFEN: update istStand=100
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>ReturnValue Integer - the recalced stand
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     BusinessLogic
 * @param basenode - the node to recalc
 * @return istStand in %
 */
function calcIstStandFromState(basenode) {
    var istStand = basenode.istStand;
    if (   basenode.type == "EVENT_ERLEDIGT"
        || basenode.type == "EVENT_VERWORFEN"
        || basenode.type == "ERLEDIGT"
        || basenode.type == "VERWORFEN") {
        istStand = 100;
    }
    console.log("calcIstStandFromState for node:" + basenode.sysUID + " state=" + basenode.type + " new istStand=" + istStand);
    
    return istStand;
}

/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     recalcs the type/state depending on the istStand
 *     <ul>
 *       <li>if className=TaskNode && 0: update type=OFFEN
 *       <li>if className=TaskNode && >0&&<100 && ! WARNING: update type=RUNNING
 *       <li>if className=TaskNode && 100 && != VERWORFEN: update type=ERLEDIGT
 *       <li>if className=EventNode && 0: update type=EVENT_PLANED
 *       <li>if className=EventNode && >0&&<100 && ! EVENT_WARNING: update type=EVENT_RUNNING
 *       <li>if className=EventNode && 100 && != EVENT_VERWORFEN: update type=EVENT_ERLEDIGT
 *     </ul>
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>ReturnValue String - the recalced type/state
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     BusinessLogic
 * @param basenode - the node to recalc
 * @return the recalced type
 */
function calcTypeFromIstStand(basenode) {
    var type = basenode.type;

    if (basenode.className == "TaskNode") {
        // TaskNode
        if (basenode.istStand == "0") {
            // 0: OFFEN
            type = "OFFEN"; 
        } else if (basenode.istStand == 100 && basenode.type != "VERWORFEN") {
            // 100: ERLEDIGT if not VERWORFEN already
            type = "ERLEDIGT"; 
        } else if (basenode.istStand < 100 && basenode.istStand > 0) {
            // 0<istStand<100: RUNNING if not WARNING already
            if (basenode.type != "WARNING") {
                type = "RUNNING"; 
            }
        }
    } else if (basenode.className == "EventNode") {
        // EventNode
        if (basenode.istStand == "0") {
            // 0: EVENT_PLANED
            type = "EVENT_PLANED"; 
        } else if (basenode.istStand == 100 && basenode.type != "EVENT_VERWORFEN") {
            // 100: EVENT_ERLEDIGT if not EVENT_VERWORFEN already
            type = "EVENT_ERLEDIGT"; 
        } else if (basenode.istStand < 100 && basenode.istStand > 0) {
            // 0<istStand<100: EVENT_RUNNING if not EVENT_WARNING already
            if (basenode.type != "EVENT_WARNING") {
                type = "EVENT_RUNNING"; 
            }
        }
    }
    console.log("calcTypeFromIstStand for node:" + basenode.sysUID + " istStand=" + basenode.istStand + " newstate=" + type);
    
    return type;
}

/*****************************************
 *****************************************
 * Service-Funktions (layout)
 *****************************************
 *****************************************/


function showModalErrorMessage(message) {
    // set messagetext
    $( "#error-message-text" ).html(message);
    
    // show message
    $( "#error-message" ).dialog({
        modal: true,
        buttons: {
          Ok: function() {
            $( this ).dialog( "close" );
          }
        }
    });    
}

function showModalConfirmDialog(message, yesHandler, noHandler) {
    // set messagetext
    $( "#dialog-confirm-text" ).html(message);
    
    // show message
    
    $( "#dialog-confirm" ).dialog({
        resizable: false,
        height:140,
        modal: true,
        buttons: {
          "Ja": function() {
            $( this ).dialog( "close" );
            if (yesHandler) {
                yesHandler();
            }
          },
          "Abbrechen": function() {
            $( this ).dialog( "close" );
            if (noHandler) {
                noHandler();
            }
          }
        }
    });
}

function yaioShowHelpSite(url) {
    // set messagetext
    console.log("yaioShowHelpSite:" + url);
    $("#help-iframe").attr('src',url);
    
    // show message
    $( "#help-box" ).dialog({
        modal: true,
        width: "800px",
        buttons: {
          "Schliessen": function() {
            $( this ).dialog( "close" );
          },
          "Eigenes Fenster": function() {
              var helpFenster = window.open(url, "help", "width=750,height=500,scrollbars=yes,resizable=yes");
              helpFenster.focus();
              $( this ).dialog( "close" );
            }
        }
    });    
}


function toggleNodeDescContainer(id) {
    $("#detail_desc_" + id).slideToggle(1000,function() {
        if ($("#detail_desc_" + id).css("display") == "block") {
            $("#toggler_desc_" + id).addClass('toggler_show').removeClass('toggler_hidden');
        } else {
            $("#toggler_desc_" + id).addClass('toggler_hidden').removeClass('toggler_show');
        }
    });
}

function toggleElement(id) {
    // get effect type from
    var selectedEffect = "drop";

    // most effect types need no options passed by default
    var options = {};
    // some effects have required parameters
    if ( selectedEffect === "scale" ) {
      options = { percent: 0 };
    } else if ( selectedEffect === "size" ) {
      options = { to: { width: 200, height: 60 } };
    }

    // run the effect
    $( id ).toggle( selectedEffect, options, 500 );
};


/*****************************************
 *****************************************
 * Service-Funktions (logging)
 *****************************************
 *****************************************/


function logError(message, flgShowDialog) {
    console.error(message);
    if (flgShowDialog) {
        showModalErrorMessage(htmlEscapeText(message));
    }
}

/*****************************************
 *****************************************
 * Service-Funktions (data)
 *****************************************
 *****************************************/

function htmlEscapeText(text) {
    if (text && text != "undefined" && text != "" && text != null) {
        text = text.replace("&", "&amp;");
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        text = text.replace("\"", "&quot;");
        text = text.replace("'", "&#x27;");
        text = text.replace("/", "&#x2F;");
    }
    return text;
}

function formatGermanDateTime(millis) {
    if (millis == null) {
       return "";
    }
    var date = new Date(millis);
    return padNumber(date.getDate(), 2)
        + "." + padNumber(date.getMonth() + 1, 2)
        + "." + date.getFullYear()
        + " " + padNumber(date.getHours(), 2)
        + ":" + padNumber(date.getMinutes(), 2);
}
function formatGermanDate(millis) {
    if (millis == null) {
       return "";
    }
    var date = new Date(millis);
    return padNumber(date.getDate(), 2)
        + "." + padNumber(date.getMonth() + 1, 2)
        + "." + date.getFullYear();
}
function padNumber(number, count) {
    var r = String(number);
    while ( r.length < count) {
    r = '0' + r;
    }
    return r;
} 
function formatNumbers(number, nachkomma, suffix) {
   if (number == null) {
       return "";
   }
   
   return (number.toFixed(nachkomma)) + suffix;
}

function downloadAsFile($link, data, fileName, mime, encoding) {
    if (mime == "undefind") {
        mime = "application/text";
    }
    if (encoding == "undefind") {
        mime = "uft-8";
    }
    // data URI
    var dataURI = 'data:' + mime + ';charset=' + encoding + ','
            + encodeURIComponent(data);

    // set link
    var flgSafeMode = 1;
    if (   (navigator.userAgent.indexOf("Trident") >= 0) 
        || (navigator.userAgent.indexOf("MSIE") >= 0)
        || flgSafeMode) {
       // IE or SafeMode
       var popup = window.open("");
       if (! popup) {
           // warn message
           logError("Leider kann der Download nicht angezeigt werden, da Ihr Popup-Blocker aktiv ist. Beachten Sie die Hinweise im Kopf des Browsers. ", true);
       } else {
           // set data to document
           $(popup.document.body).html(data);
       }
       return false;
   } else {
        // all expect IE
        $link.attr({
            'download' : fileName,
            'href' : dataURI,
            'target' : '_blank'
        });
   }
}
