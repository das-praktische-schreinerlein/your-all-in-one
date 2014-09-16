var CLIPBOARD = null;

var baseUrl = "/nodes/";
var showUrl = baseUrl + "show/";
var updateUrl = baseUrl + "update/";
var moveUrl = baseUrl + "move/";

function createYAIOFancyTree(treeId, masterNodeId){
    $(treeId).flgYAIOFancyTreeLoaded = true;
    $(treeId).fancytree({
        checkbox: true,
        titlesTabbable: true,     // Add all node titles to TAB chain
  
//// MS Start
  // initial source is MasterplanMasternode1
        source: { 
            url: showUrl + masterNodeId, 
            cache: false 
        },
      
        // lazy load the children
        lazyLoad: function(event, data) {
            var node = data.node;
            console.debug("load data for " + node.key + " from " + showUrl + node.key);
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
                    moveNode(node, data);
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
                // Return false to prevent edit mode
            },
            edit: function(event, data){
                // Editor was opened (available as data.input)
            },
            beforeClose: function(event, data){
                // Return false to prevent cancel/save (data.input is available)
            },
            save: function(event, data){
                if (confirm("Wollen Sie den Titel wirklich ändern?")) {
                    // Save data.input.val() or return false to keep editor open
                    saveNode(this, data);
                    // We return true, so ext-edit will set the current user input
                    // as title
                    return true;
                } else {
                    // discard
                    return false;
                }
            },
            close: function(event, data){
                // Editor was removed
                if( data.save ) {
                    // Since we started an async request, mark the node as preliminary
                    $(data.node.span).addClass("pending");
                }
            }
        },
        table: {
            indentation: 20,
            nodeColumnIdx: 2,
            checkboxColumnIdx: 0
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
            case "moveUp":
                node.moveTo(node.getPrevSibling(), "before");
                node.setActive();
                break;
            case "moveDown":
                node.moveTo(node.getNextSibling(), "after");
                node.setActive();
                break;
            case "indent":
                refNode = node.getPrevSibling();
                node.moveTo(refNode, "child");
                refNode.setExpanded();
                node.setActive();
                break;
            case "outdent":
                node.moveTo(node.getParent(), "after");
                node.setActive();
                break;
            case "rename":
                node.editStart();
                break;
            case "remove":
                node.remove();
                break;
            case "addChild":
                node.editCreateNode("child", "New node");
                // refNode = node.addChildren({
                //   title: "New node",
                //   isNew: true
                // });
                // node.setExpanded();
                // refNode.editStart();
                break;
            case "addSibling":
                node.editCreateNode("after", "New node");
                // refNode = node.getParent().addChildren({
                //   title: "New node",
                //   isNew: true
                // }, node.getNextSibling());
                // refNode.editStart();
                break;
            case "cut":
                CLIPBOARD = {mode: data.cmd, data: node};
                break;
            case "copy":
                CLIPBOARD = {
                  mode: data.cmd,
                  data: node.toDict(function(n){
                    delete n.key;
                  })
                };
                break;
            case "clear":
                CLIPBOARD = null;
                break;
            case "paste":
                if( CLIPBOARD.mode === "cut" ) {
                  // refNode = node.getPrevSibling();
                  CLIPBOARD.data.moveTo(node, "child");
                  CLIPBOARD.data.setActive();
                } else if( CLIPBOARD.mode === "copy" ) {
                  node.addChildren(CLIPBOARD.data).setActive();
                }
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
        } else if( c === "C" && e.ctrlKey ) {
            cmd = "copy";
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
            {title: "Cut <kbd>Ctrl+X</kbd>", cmd: "cut", uiIcon: "ui-icon-scissors"},
            {title: "Copy <kbd>Ctrl-C</kbd>", cmd: "copy", uiIcon: "ui-icon-copy"},
            {title: "Paste as child<kbd>Ctrl+V</kbd>", cmd: "paste", uiIcon: "ui-icon-clipboard", disabled: true }
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
        + "." + padNumber(date.getMonth(), 2)
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
        + "." + padNumber(date.getMonth(), 2)
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
    var firstNodeId = lstIdsHierarchy.shift();
    var firstNode = rootNode.mapChildren[firstNodeId];
    if (! firstNode) {
        console.error("openHierarchy: error for tree:'" + treeId 
                    + "' firstNode:'" + firstNodeId + "' not found.");
        return;
    }

    // open Hierarchy
    var opts = {};
    opts.openHierarchy = lstIdsHierarchy;
    firstNode.setExpanded(true, opts);
}


function saveNode(newData, data) {
    var json = JSON.stringify({name: data.input.val()});
    var url = updateUrl + data.node.key;
    doUpdateNode(data.node, url, json);
}

function moveNode(node, data) {
    var json = JSON.stringify({parentNode: node.key});
    var url = moveUrl + data.otherNode.key + "/" + node.key;
    doUpdateNode(data.otherNode, url, json);
}

function doUpdateNode(node, url, json) {
    console.log("update node:" + node.key + " with:" + json);
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

function renderColumnsForNode(event, data) {
    var node = data.node,
      $tdList = $(node.tr).find(">td");

    // (index #0 is rendered by fancytree by adding the checkbox)
    if( node.isFolder() ) {
      // make the title cell span the remaining columns, if it is a folder:
      $tdList.eq(2)
        .prop("colspan", 6)
        .nextAll().remove();
    }
    
    var basenodedata = node.data.basenode;

    var nodestate = basenodedata.state;
    var statestyle = "node-state-" + nodestate;
//    $tdList.eq(1).text(node.getIndexHier()).addClass("alignRight").addClass(statestyle);
    $tdList.eq(1).html("<a href='#/show/" + basenodedata.sysUID + "'>OPEN</a>").addClass("alignRight").addClass(statestyle);
    // (index #2 is rendered by fancytree)
    $tdList.eq(2).addClass("field_name").addClass(statestyle);

    // currente datablock
    var nodeDataBlock = "";
    $table = $("<table />");
    $row = $("<tr />");
    $table.append($row);
    
    // default fields
    $row.append($("<td />").html(basenodedata.className).addClass("field_type").addClass(statestyle));
    $row.append($("<td />").html(basenodedata.state).addClass("field_state").addClass(statestyle));
    
    if (basenodedata.className == "TaskNode") {
        // TaskNode
        $row.append(
                $("<td />").html(formatNumbers(basenodedata.istChildrenSumStand, 2, "%")).addClass("field_istChildrenSumStand").addClass(statestyle)); 
        $row.append(
                $("<td />").html(formatNumbers(basenodedata.istChildrenSumAufwand, 2, "h")).addClass("field_istChildrenSumAufwand").addClass(statestyle));
        $row.append(
                $("<td />").html(formatGermanDate(basenodedata.istChildrenSumStart)
                         + "-" + formatGermanDate(basenodedata.istChildrenSumEnde)).addClass("field_istChildrenSum").addClass(statestyle));
        $row.append(
                $("<td />").html(formatNumbers(basenodedata.planChildrenSumAufwand, 2, "h")).addClass("field_planChildrenSumAufwand").addClass(statestyle));
        $row.append(
                $("<td />").html(formatGermanDate(basenodedata.planChildrenSumStart)
                         + "-" + formatGermanDate(basenodedata.planChildrenSumEnde)).addClass("field_planChildrenSum").addClass(statestyle));
    } else if (basenodedata.className == "UrlResNode") {
        // url
        $row.append(
                $("<td />").html("<a href='" + basenodedata.resLocRef + "' target='_blank'>" + basenodedata.resLocRef).addClass("field_resLocRef").addClass(statestyle)); 
    } else if (basenodedata.className == "InfoNode") {
    } else if (basenodedata.className == "EventNode") {
        $row.append(
                $("<td />").html(formatNumbers(basenodedata.istChildrenSumStand, 2, "%")).addClass("field_istChildrenSumStand").addClass(statestyle)); 
        $row.append($("<td />").html(formatNumbers(basenodedata.istChildrenSumAufwand, 2, "h")).addClass("field_istChildrenSumAufwand").addClass(statestyle));
        $row.append(
                $("<td />").html(formatGermanDate(basenodedata.istChildrenSumStart)
                         + "-" + formatGermanDate(basenodedata.istChildrenSumEnde)).addClass("field_istChildrenSum").addClass(statestyle));
        $row.append(
                $("<td />").html(formatNumbers(basenodedata.planChildrenSumAufwand, 2, "h")).addClass("field_planChildrenSumAufwand").addClass(statestyle));
        $row.append(
                $("<td />").html(formatGermanDate(basenodedata.planChildrenSumStart)
                         + "-" + formatGermanDate(basenodedata.planChildrenSumEnde)).addClass("field_planChildrenSum").addClass(statestyle));
    }
    $nodeDataBlock = $table;
    
    // add nodeDesc if set
    $toggler = "";
    if (basenodedata.nodeDesc != "" && basenodedata.nodeDesc != null) {
        // columncount
        var columnCount = $(">td", $row).length;
        
        $nodeDataBlock.append(
                $("<br /><div class='togglecontainer' id='detail_desc_" + basenodedata.sysUID + "'><pre>" 
                        + basenodedata.nodeDesc + "</pre></div>").addClass("field_nodeDesc"));
        
        // append toogler
//        $toggler = $("<div id='toogler_desc_" + basenodedata.sysUID + "' />");
    }
    
    // add nodeData
    $tdList.eq(3).html($nodeDataBlock).addClass("block_nodedata");
    $tdList.eq(4).html($toggler).addClass("toggler");

    // append toggler
    if ($toggler != "") {
        jMATService.getPageLayoutService().appendBlockToggler(
                'toogler_desc_' + basenodedata.sysUID, 'detail_desc_' + basenodedata.sysUID);
        jMATService.getLayoutService().togglerBlockHide('detail_desc_' + basenodedata.sysUID, 'detail_desc_' + basenodedata.sysUID, {});
    }
};

function createFancyDataFromNodeData(node) {
    var datanode = {
       title: node.name,
       key: node.sysUID, 
       children: null,
       lazy: true,
       basenode: node
    };

    if (node.className == "UrlResNode") {
        datanode.title = node.resLocName;
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
                var node = data.response.childNodes[zaehler];
                var datanode = createFancyDataFromNodeData(node);
                console.debug("add childnode for " + baseNode.sysUID + " = " + node.sysUID + " " + node.name);
                list.push(datanode);
            }
        }
    } else {
        // error
        console.error("error loading nodes:" + data.response.stateMsg)
    }
    
    data.result = list;
}

function createOrReloadYAIOFancyTree(treeId, masterNodeId){
    // check if already loaded
    if ($(treeId).flgYAIOFancyTreeLoaded) {
        console.log("createOrReloadYAIOFancyTree: flgYAIOFancyTreeLoaded is set: reload=" + showUrl + masterNodeId);
        var tree = $(treeId).fancytree("getTree");
        tree.reload(showUrl + masterNodeId);
    } else {
        console.log("createOrReloadYAIOFancyTree: flgYAIOFancyTreeLoaded not set: create=" + showUrl + masterNodeId);
        createYAIOFancyTree(treeId, masterNodeId);
    }
}
