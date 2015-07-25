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
 *     servicefunctions for the editors
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */


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
 * @param basenode - the node to map the fieldvalue
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
        value = yaioAppBase.get('YaioBaseService').formatGermanDate(value);
    } else if (field.datatype == "datetime")  {
        // date
        value = yaioAppBase.get('YaioBaseService').formatGermanDateTime(value);
    } else if (! value || value == "undefined" || value == null) {
        // alle other
        value = "";
    } 
    
    // reescape data for form
    if (fieldName == "nodeDesc") {
        value = value.replace(/\<WLBR\>/g, "\n");
        value = value.replace(/\<WLESC\>/g, "\\");
        value = value.replace(/\<WLTAB\>/g, "\t");
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
        yaioAppBase.get('YaioBaseService').logError("error yaioOpenNodeEditor: nodeId required", false);
        return null;
    }
    // load node
    var tree = $("#tree").fancytree("getTree");
    if (!tree) {
        // tree not found
        yaioAppBase.get('YaioBaseService').logError("error yaioOpenNodeEditor: cant load tree for node:" + nodeId, false);
        return null;
    }
    var treeNode = tree.getNodeByKey(nodeId);
    if (! treeNode) {
        yaioAppBase.get('YaioBaseService').logError("error yaioOpenNodeEditor: cant load node:" + nodeId, false);
        return null;
    }
    
    // extract nodedata
    var basenode = treeNode.data.basenode;
    
    // open editor
    yaioOpenNodeEditorForNode(basenode, mode);
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
 * @param basenode - the node
 * @param mode - edit, create, createsymlink
 * @param newNode - optional node to copy data from (for mode createsnapshot...)
 */
function yaioOpenNodeEditorForNode(basenode, mode, newNode) {
    // reset editor
    console.log("yaioOpenNodeEditor: reset editor");
    yaioResetNodeEditor();
    
    // check vars
    if (! basenode) {
        // tree not found
        yaioAppBase.get('YaioBaseService').logError("error yaioOpenNodeEditor: basenode required", false);
        return null;
    }
    var nodeId = basenode['sysUID'];

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
    } else if (mode == "createsnapshot") {
        // mode create
        formSuffix = "InfoNode";
        fieldSuffix = "InfoNode";
        fields = fields.concat(configNodeTypeFields.CreateSnapshot.fields);

        // new basenode
        basenode = {
                mode: "create",
                sysUID: origBasenode.sysUID,
                name: "Snapshot für: '" + origBasenode.name + "' vom " + yaioAppBase.get('YaioBaseService').formatGermanDateTime((new Date()).getTime()),
                type: "INFO",
                state: "INFO",
                className: "InfoNode",
                nodeDesc: newNode.nodeDesc
        };
        console.error("yaioOpenNodeEditor mode=createsnapshot for node:" + nodeId);
    } else {
        yaioAppBase.get('YaioBaseService').logError("error yaioOpenNodeEditor: unknown mode=" + mode 
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
    $("#containerYaioEditor").css("width", "900px");
    $("#containerBoxYaioEditor").css("width", "900px");
    $("#containerYaioTree").css("width", (width - $("#containerYaioEditor").width() - 30) + "px");
    
    // display editor and form for the formSuffix
    $("#containerBoxYaioEditor").css("display", "block");
    $("#containerFormYaioEditor" + formSuffix).css("display", "block");
    //$("#containerYaioEditor").css("display", "block");
    toggleElement("#containerYaioEditor");

    // create Elements if not exists
    createTogglerIfNotExists("legendIstTaskForm", "filterIstTaskForm", "filter_IstTaskNode");
    createTogglerIfNotExists("legendDescTaskForm", "filterDescTaskForm", "filter_DescTaskNode");
    createTogglerIfNotExists("legendIstEventForm", "filterIstEventForm", "filter_IstEventNode");
    createTogglerIfNotExists("legendDescEventForm", "filterDescEventForm", "filter_DescEventNode");
    createTogglerIfNotExists("legendLayoutInfoForm", "filterLayoutInfoForm", "filter_LayoutInfoNode");
    createTogglerIfNotExists("legendDescInfoForm", "filterDescInfoForm", "filter_DescInfoNode");
    createTogglerIfNotExists("legendLayoutUrlResForm", "filterLayoutUrlResForm", "filter_LayoutUrlResNode");
    createTogglerIfNotExists("legendDescUrlResForm", "filterDescUrlResForm", "filter_DescUrlResNode");
    createTogglerIfNotExists("legendDescSymLinkForm", "filterDescSymLinkForm", "filter_DescSymLinkNode");
    
    // hide empty, optional elements
    hideFormRowTogglerIfSet("filterIstTaskForm", "filter_IstTaskNode", false);
    hideFormRowTogglerIfSet("filterDescTaskForm", "filter_DescTaskNode", false);
    hideFormRowTogglerIfSet("filterIstEventForm", "filter_IstEventNode", false);
    hideFormRowTogglerIfSet("filterDescEventForm", "filter_DescEventNode", false);
    hideFormRowTogglerIfSet("filterLayoutInfoForm", "filter_LayoutInfoNode", false);
    hideFormRowTogglerIfSet("filterDescInfoForm", "filter_DescInfoNode", false);
    hideFormRowTogglerIfSet("filterLayoutUrlResForm", "filter_LayoutUrlResNode", false);
    hideFormRowTogglerIfSet("filterDescUrlResForm", "filter_DescUrlResNode", false);
    hideFormRowTogglerIfSet("filterDescSymLinkForm", "filter_DescSymLinkNode", false);

    // create nodeDesc-editor
    createNodeDescEditorForNode("editorInputNodeDescTaskNode", "inputNodeDescTaskNode");
    createNodeDescEditorForNode("editorInputNodeDescEventNode", "inputNodeDescEventNode");
    createNodeDescEditorForNode("editorInputNodeDescInfoNode", "inputNodeDescInfoNode");
    createNodeDescEditorForNode("editorInputNodeDescUrlResNode", "inputNodeDescUrlResNode");
    createNodeDescEditorForNode("editorInputNodeDescSymLinkNode", "inputNodeDescSymLinkNode");
    
    // update appsize
    setupAppSize();
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
};


/*****************************************
 *****************************************
 * Service-Funktions (businesslogic)
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
