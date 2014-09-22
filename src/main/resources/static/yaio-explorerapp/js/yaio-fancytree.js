var CLIPBOARD = null;

// configure
var baseUrl = "/nodes/";
var showUrl = baseUrl + "show/";
var symLinkUrl = baseUrl + "showsymlink/";
var updateUrl = baseUrl + "update/";
var moveUrl = baseUrl + "move/";

var treeInstances = {};
var configNodeTypeFields = {
    Common: {
        fields: [
            { fieldName: "className", type: "hidden"},
            { fieldName: "sysUID", type: "hidden"},
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
        ]
    },
    UrlResNode: {
        fields: [
            { fieldName: "name", type: "input"},
            { fieldName: "resLocRef", type: "input"},
            { fieldName: "resLocName", type: "input"},
            { fieldName: "resLocTags", type: "textarea"},
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



function createYAIOFancyTree(treeId, masterNodeId, doneHandler){
    $(treeId).flgYAIOFancyTreeLoaded = true;
    $(treeId).fancytree({
        
        // save masterNodeId
        masterNodeId: masterNodeId,
        
        // set layoutoptions
        maxTitleWidth: 420,
        
        checkbox: true,
        titlesTabbable: true,     // Add all node titles to TAB chain
  
        source: { 
            url: showUrl + masterNodeId, 
            cache: false 
        },
      
        // lazy load the children
        lazyLoad: function(event, data) {
            var node = data.node;
            console.debug("createYAIOFancyTree load data for " + node.key 
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
                    yaioMoveNode(data.otherNode, node.key);
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
                openEditorForNode(data.node.key);
                
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
            nodeColumnIdx: 1,
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
                    yaioMoveNode(node, newParentKey);
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
                    yaioMoveNode(node, newParentKey);
                    return true;
                } else {
                    // discard
                    return false;
                }
                break;
// TODO
//            case "moveUp":
//                node.moveTo(node.getPrevSibling(), "before");
//                node.setActive();
//                break;
//            case "moveDown":
//                node.moveTo(node.getNextSibling(), "after");
//                node.setActive();
//                break;
//            case "remove":
//                node.remove();
//                break;
//            case "addChild":
//                node.editCreateNode("child", "New node");
//                break;
//            case "addSibling":
//                node.editCreateNode("after", "New node");
//                break;
//            case "cut":
//                CLIPBOARD = {mode: data.cmd, data: node};
//                break;
//            case "copy":
//                CLIPBOARD = {
//                  mode: data.cmd,
//                  data: node.toDict(function(n){
//                    delete n.key;
//                  })
//                };
//                break;
//            case "clear":
//                CLIPBOARD = null;
//                break;
//            case "paste":
//                if( CLIPBOARD.mode === "cut" ) {
//                  // refNode = node.getPrevSibling();
//                  CLIPBOARD.data.moveTo(node, "child");
//                  CLIPBOARD.data.setActive();
//                } else if( CLIPBOARD.mode === "copy" ) {
//                  node.addChildren(CLIPBOARD.data).setActive();
//                }
//                break;
            default:
                alert("Unhandled command: " + data.cmd);
                return;
        }
  
    }).on("keydown", function(e){
        var c = String.fromCharCode(e.which),
            cmd = null;
    
        if( c === "N" && e.ctrlKey && e.shiftKey) {
            cmd = "addChild";
//        } else if( c === "C" && e.ctrlKey ) {
//            cmd = "copy";
        } else if( c === "V" && e.ctrlKey ) {
            cmd = "paste";
        } else if( c === "X" && e.ctrlKey ) {
            cmd = "cut";
        } else if( c === "N" && e.ctrlKey ) {
            cmd = "addSibling";
        } else if( e.which === $.ui.keyCode.DELETE ) {
            cmd = "remove";
        } else if( e.which === $.ui.keyCode.F2 ) {
            cmd = "rename";
//        } else if( e.which === $.ui.keyCode.UP && e.ctrlKey ) {
//            cmd = "moveUp";
//        } else if( e.which === $.ui.keyCode.DOWN && e.ctrlKey ) {
//            cmd = "moveDown";
//        } else if( e.which === $.ui.keyCode.RIGHT && e.ctrlKey ) {
            cmd = "indent";
        } else if( e.which === $.ui.keyCode.LEFT && e.ctrlKey ) {
            cmd = "outdent";
        }
        if( cmd ){
            $(this).trigger("nodeCommand", {cmd: cmd});
            return false;
        }
    })
    
    setTimeout(function() {
        console.log("load tree done:" + masterNodeId);

        // openLoadHirarchy for activeNodeId when done
        if (doneHandler) {
            console.log("createYAIOFancyTree call doneHandler:");
            doneHandler();
        }
    }, 1000);

    /*
     * Context menu (https://github.com/mar10/jquery-ui-contextmenu)
     */
    $(treeId).contextmenu({
        delegate: "span.fancytree-node",
        menu: [
            {title: "Edit <kbd>[F2]</kbd>", cmd: "rename", uiIcon: "ui-icon-pencil" },
            {title: "Delete <kbd>[Del]</kbd>", cmd: "remove", uiIcon: "ui-icon-trash" },
            {title: "----"},
            {title: "New sibling <kbd>[Ctrl+N]</kbd>", cmd: "addSibling", uiIcon: "ui-icon-plus" },
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
 *
 * Service-Funktions
 *
 *****************************************/
function formatGermanDateTime(millis) {
    if (millis == null) {
       return "";
    }
    var date = new Date(millis);
    return padNumber(date.getDate(), 2)
        + "." + padNumber(date.getMonth() + 1, 2)
        + "." + date.getFullYear()
        + " " + padNumber(date.getHours(), 2)
        + ":" + padNumber(date.getMinutes(), 2)
        + ":" + padNumber(date.getSeconds(), 2);
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

function openNodeHierarchy(treeId, lstIdsHierarchy) {
    // check for tree
    var tree = $(treeId).fancytree("getTree");
    if (! tree) {
        console.error("openHierarchy: error tree:'" + treeId + "' not found.");
        return;
    }

    // check for rootNode
    var rootNode = tree.rootNode;
    if (! rootNode) {
        console.error("openHierarchy: error for tree:'" + treeId 
                    + "' rootNode not found.");
        return;
    }
    
    // check for lstIdsHierarchy
    if (! lstIdsHierarchy || lstIdsHierarchy.length <= 0) {
        console.error("openHierarchy: error for tree:'" + treeId 
                    + "' lstIdsHierarchy is empty.");
        return;
    }
    
    // search for firstNode in rootTree, ignore if not found
    var firstNodeId, firstNode;
    var lstIdsHierarchySave = lstIdsHierarchy;
    while (! firstNode && lstIdsHierarchy.length > 0) {
        firstNodeId = lstIdsHierarchy.shift();
        firstNode = rootNode.mapChildren[firstNodeId];
    }
    if (! firstNode) {
        console.error("openHierarchy: error for tree:'" + treeId 
                    + "' firstNode of:'" + lstIdsHierarchySave 
                    + "' not found on rootNode.");
        return;
    }

    // open Hierarchy
    var opts = {};
    opts.openHierarchy = lstIdsHierarchy;
    firstNode.setExpanded(true, opts);
}

function openNodeHierarchyForNodeId(treeId, activeNodeId) {
    // check for tree
    var tree = $(treeId).fancytree("getTree");
    if (! tree) {
        console.error("openNodeHierarchyForNodeId: error tree:'" + treeId + "' not found.");
        return;
    }
    
    // check for activeNodeId
    var treeNode = tree.getNodeByKey(activeNodeId);
    if (! treeNode) {
        console.error("openNodeHierarchyForNodeId: error for tree:'" + treeId 
                + "' activeNode " + activeNodeId + " not found.");
        return null;
    }

    // Return the parent keys separated by options.keyPathSeparator, e.g. "id_1/id_17/id_32"
    var keyPath = treeNode.getKeyPath(false);
    // extract lstIdsHierarchy
    var lstIdsHierarchy = keyPath.split("/");
    console.log("openNodeHierarchyForNodeId: extracted lst:" 
            + lstIdsHierarchy + " from keyPath:" + keyPath);
    
    // open Hierarchy
    openNodeHierarchy(treeid, lstIdsHierarchy);
}

function yaioSaveNode(data) {
    var json = JSON.stringify({name: data.input.val()});
    var url = updateUrl + data.node.key;
    doUpdateNode(data.node, url, json);
}

function yaioMoveNode(node, newParentKey) {
    console.log("move node:" + node.key + " to:" + newParentKey);
    var json = JSON.stringify({parentNode: newParentKey});
    var url = moveUrl + node.key + "/" + newParentKey;
    yaioDoUpdateNode(node, url, json);
}


function yaioDoUpdateNode(node, url, json) {
    var msg = "update for node:" + node.key;
    console.log("yaioDoUpdateNode START: " + msg + " with:" + json);
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
                    console.error("got no hierarchy for:" + node.key 
                            + " hierarchy:" + response.parentIdHierarchy);
                }
            } else {
                console.error("cant save node:" + node.key + " error:" + response.stateMsg);
                // check for violations
                if (response.violations) {
                    // iterate violations
                    for (var idx in response.violations) {
                        var violation = response.violations[idx];
                        console.error("violations while save node:" + node.key 
                                + " field:" + violation.path + " message:" + violation.message);
                        alert("cant save node because: " + violation.path + " (" + violation.message + ")")
                    }
                }
            }
        },
        error : function(jqXHR, textStatus, errorThrown) {
            // log the error to the console
            console.error("The following error occured: " + textStatus, errorThrown);
            alert("cant save node:" + node.key + " error:" + textStatus, errorThrown)
        },
        complete : function() {
            console.log("update node:" + node.key + "' ran");
        }
    });
}

function yaioLoadSymLinkData(basenode, fancynode) {
    var msg = "symlink for node:" + basenode.sysUID + " symlink:" + basenode.symLinkRef + " fancynode:" + fancynode.key;
    url = symLinkUrl + basenode.symLinkRef;
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
                        console.error("error yaioLoadSymLinkData: cant load tree - " + msg);
                        return null;
                    }
                    var treeNode = tree.getNodeByKey(basenode.sysUID);
                    if (! treeNode) {
                        console.error("error yaioLoadSymLinkData: cant load node - " + msg);
                        return null;
                    }
                    
                    // append Link to referenced node
                    $(treeNode.tr).find("div.container_data_row").append(
                            "<a href='#/show/" + response.node.sysUID + "' class='button'>OPEN</a>");
                    
                    // add datablock of referenced node
                    $(treeNode.tr).find("div.container_data_table").append($nodeDataBlock.html());

                    console.log("renderSymLinkDataBLock done:" + msg);
                } else {
                    console.error("ERROR got no " + msg);
                }
            } else {
                console.error("ERROR cant load  " + msg + " error:" + response.stateMsg);
            }
        },
        error : function(jqXHR, textStatus, errorThrown) {
            // log the error to the console
            console.error("ERROR  " + msg + " The following error occured: " + textStatus, errorThrown);
            alert("cant load " + msg + " error:" + textStatus, errorThrown)
        },
        complete : function() {
            console.log("completed load " + msg);
        }
    });
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
    $row.append($("<div />").html(basenode.className)
           .addClass("container_field")
           .addClass("fieldtype_type")
           .addClass("field_type")
           .addClass(statestyle));
    $row.append($("<div />").html(basenode.state)
            .addClass("container_field")
            .addClass("fieldtype_state")
            .addClass("field_state")
            .addClass(statestyle));
    
    if (basenode.className == "TaskNode" || basenode.className == "EventNode") {
        // TaskNode
        $row.append(
                $("<div />").html(formatNumbers(basenode.istChildrenSumStand, 2, "%"))
                        .addClass("container_field")
                        .addClass("fieldtype_stand")
                        .addClass("field_istChildrenSumStand")
                        .addClass(statestyle)); 
        $row.append(
                $("<div />").html(formatNumbers(basenode.istChildrenSumAufwand, 2, "h"))
                        .addClass("container_field")
                        .addClass("fieldtype_aufwand")
                        .addClass("field_istChildrenSumAufwand")
                        .addClass(statestyle));
        $row.append(
                $("<div />").html(formatGermanDate(basenode.istChildrenSumStart)
                        + "-" + formatGermanDate(basenode.istChildrenSumEnde))
                         .addClass("container_field")
                         .addClass("fieldtype_fromto")
                         .addClass("field_istChildrenSum")
                         .addClass(statestyle));
        $row.append(
                $("<div />").html(formatNumbers(basenode.planChildrenSumAufwand, 2, "h"))
                         .addClass("container_field")
                         .addClass("fieldtype_aufwand")
                         .addClass("field_planChildrenSumAufwand")
                         .addClass(statestyle));
        $row.append(
                $("<div />").html(formatGermanDate(basenode.planChildrenSumStart)
                         + "-" + formatGermanDate(basenode.planChildrenSumEnde))
                         .addClass("container_field")
                         .addClass("fieldtype_fromto")
                         .addClass("field_planChildrenSum")
                         .addClass(statestyle));
    } else if (basenode.className == "UrlResNode") {
        // url
        $row.append(
                $("<div />").html("<a href='" + basenode.resLocRef + "' target='_blank'>" 
                                 + basenode.resLocRef + "</a>")
                          .addClass("container_field")
                          .addClass("fieldtype_url")
                          .addClass("field_resLocRef")
                          .addClass(statestyle)); 
    } else if (basenode.className == "InfoNode") {
    } else if (basenode.className == "SymLinkNode") {
        yaioLoadSymLinkData(basenode, fancynode);
    } 
    $nodeDataBlock = $table;
    
    console.log("renderDataBlock DONE: " + msg);

    return $nodeDataBlock;
}


function renderColumnsForNode(event, data) {
    // extract nodedata
    var node = data.node;
    var basenode = node.data.basenode;
    var nodestate = basenode.state;
    var statestyle = "node-state-" + nodestate;
    
    // get tdlist
    var $tdList = $(node.tr).find(">td");

    // (index #0 is rendered by fancytree by adding the checkbox)
//    $tdList.eq(1).text(node.getIndexHier()).addClass("alignRight").addClass(statestyle);

    // add stateclasss to tr
    $(node.tr).addClass(statestyle).addClass("container_nodeline");
    
    // add fields
    $tdList.eq(0).html(
            "<a href='#/show/" + basenode.sysUID + "' class='button'>OPEN</a> "
            + "<a onclick=\"javascript: openEditorForNode('" + basenode.sysUID + "'); return false;\" class='button'>EDIT</a>"
            ).addClass("container_field")
             .addClass("fieldtype_actions")
             .addClass(statestyle);
    // (index #2 is rendered by fancytree)
    $tdList.eq(1).addClass("container_field")
                 .addClass("fieldtype_name")
                 .addClass("field_name")
                 .addClass(statestyle);
    
    // render datablock
    var $nodedataBlock = renderDataBlock(basenode, node);

    // add nodeDesc if set
    if (basenode.nodeDesc != "" && basenode.nodeDesc != null) {
        // columncount
        var columnCount = $(">td", $row).length;
        
        $nodeDataBlock.append(
                $("<br clear=all/><div class='togglecontainer' id='detail_desc_" + basenode.sysUID + "'><pre>" 
                        + basenode.nodeDesc + "</pre></div>").addClass("field_nodeDesc"));
    }
    
    // add nodeData
    $tdList.eq(2).html($nodeDataBlock).addClass("block_nodedata");
};

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
        console.error("error loading nodes:" + data.response.stateMsg)
    }
    
    data.result = list;
}

function createOrReloadYAIOFancyTree(treeId, masterNodeId, doneHandler){
    // check if already loaded
    console.log("createOrReloadYAIOFancyTree caller: " + createOrReloadYAIOFancyTree.caller);
    if ($(treeId).flgYAIOFancyTreeLoaded) {
        console.log("createOrReloadYAIOFancyTree: flgYAIOFancyTreeLoaded is set: reload=" 
                + showUrl + masterNodeId);
        var tree = $(treeId).fancytree("getTree");
        tree.reload(showUrl + masterNodeId).done(function(){
            console.log("createOrReloadYAIOFancyTree reload tree done:" + masterNodeId);

            // check if doneHandler
            if (doneHandler) {
                console.log("createOrReloadYAIOFancyTree call doneHandler");
                doneHandler();
            }
        });
    } else {
        console.log("createOrReloadYAIOFancyTree: flgYAIOFancyTreeLoaded not set:"
                + " create=" + showUrl + masterNodeId);
        createYAIOFancyTree(treeId, masterNodeId, doneHandler);
    }
}



function openEditorForNode(nodeId, caller) {
    if (nodeId) {
        // reset editor
        console.log("reset editor");
        $("#containerYaioTree").css("width", "100%");
        $("#containerYaioEditor").css("width", "100%");
        $("#containerYaioEditor").css("display", "none");
        $("#containerBoxYaioEditor").css("width", "100%");
        $("#containerBoxYaioEditor").css("display", "none");
        $("#containerFormYaioEditorTaskNode").css("display", "none");
        $("#containerFormYaioEditorEventNode").css("display", "none");
        $("#containerFormYaioEditorInfoNode").css("display", "none");
        $("#containerFormYaioEditorUrlResNode").css("display", "none");
        $("#containerFormYaioEditorSymLinkNode").css("display", "none");

        // load node
        var tree = $("#tree").fancytree("getTree");
        if (!tree) {
            // tree not found
            console.error("error openEditorForNode: cant load tree");
            return null;
        }
        var treeNode = tree.getNodeByKey(nodeId);
        if (! treeNode) {
            console.error("error openEditorForNode: cant load node:" + nodeId);
            return null;
        }

        // extract data
        var node = treeNode.data.basenode;
        
        // configure value mapping
        var fields = new Array();
        fields = fields.concat(configNodeTypeFields.Common.fields);
        if (node.className == "TaskNode") {
            fields = fields.concat(configNodeTypeFields.TaskNode.fields);
        } else if (node.className == "EventNode") {
            fields = fields.concat(configNodeTypeFields.EventNode.fields);
        } else if (node.className == "InfoNode") {
            fields = fields.concat(configNodeTypeFields.InfoNode.fields);
        }  else if (node.className == "UrlResNode") {
            fields = fields.concat(configNodeTypeFields.UrlResNode.fields);
        }  else if (node.className == "SymLinkNode") {
            fields = fields.concat(configNodeTypeFields.SymLinkNode.fields);
        }
        
        // iterate fields
        for (var idx in fields) {
            var field = fields[idx];
            var fieldName = field.fieldName;
            var fieldNameId = "#input" + fieldName.charAt(0).toUpperCase() + fieldName.slice(1) + node.className;
            var value = node[fieldName];
            
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
            
            if (field.type == "hidden") {
                $(fieldNameId).val(value).trigger('input').triggerHandler("change");
            } else if (field.type == "select") {
                $(fieldNameId).val(value).trigger('select').triggerHandler("change");
            } else if (field.type == "textarea") {
                $(fieldNameId).val(value).trigger('select').triggerHandler("change");
            }else {
                // input
                $(fieldNameId).val(value).trigger('input');
            }
            console.log("map nodefield:" + fieldName + " set:" + fieldNameId + "=" + value);
        }
        
        // show editor
        var width = $("#box_data").width();
        console.log("show editor");

        // set width
        $("#containerYaioEditor").css("width", "600px");
        $("#containerBoxYaioEditor").css("width", "600px");
        $("#containerYaioTree").css("width", (width - $("#containerYaioEditor").width() - 30) + "px");
        
        // dislay editor and form for the node-classname
        $("#containerBoxYaioEditor").css("display", "block");
        $("#containerYaioEditor").css("display", "block");
        $("#containerFormYaioEditor" + node.className).css("display", "block");
    } 
}

function closeEditorForNode() {
    console.log("close editor");
    $("#containerYaioTree").css("width", "100%");
    $("#containerYaioEditor").css("display", "none");
    $("#containerYaioEditor").css("width", "100%");
    $("#containerBoxYaioEditor").css("display", "none");
    $("#containerBoxYaioEditor").css("width", "100%");
    $("#containerFormYaioEditorTaskNode").css("display", "none");
    $("#containerFormYaioEditorEventNode").css("display", "none");
    $("#containerFormYaioEditorInfoNode").css("display", "none");
    $("#containerFormYaioEditorUrlResNode").css("display", "none");
    $("#containerFormYaioEditorSymLinkNode").css("display", "none");
} 
