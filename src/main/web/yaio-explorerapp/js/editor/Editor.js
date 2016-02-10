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
 * servicefunctions for the editors
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */


/*****************************************
 *****************************************
 * Service-Funktions (editor)
 *****************************************
 *****************************************/
Yaio.Editor = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
        self.callUpdateTriggerForElement = me.callUpdateTriggerForElement;
    };



    /** 
     * reset editor (hide all form, empty all formfields)
     * @FeatureDomain                GUI
     * @FeatureResult                GUI-result: hide editor
     * @FeatureKeywords              GUI Editor
     */
    me.yaioResetNodeEditor = function() {
        // reset editor
        console.log("yaioResetNodeEditor: show tree, hide editor");
        
        // show full tree
        me.$("#containerYaioTree").css("width", "100%");
        
        // hide editor-container
        me.$("#containerYaioEditor").css("width", "100%");
        me.$("#containerYaioEditor").css("display", "none");
        
        // hide editor-box
        me.$("#containerBoxYaioEditor").css("width", "100%");
        me.$("#containerBoxYaioEditor").css("display", "none");
        
        // hide forms
        me.yaioHideAllNodeEditorForms();
        me.yaioResetNodeEditorFormFields();
    };
    
    /** 
     * hide all editor-forms
     * @FeatureDomain                GUI
     * @FeatureResult                GUI-result: hide all editor-forms
     * @FeatureKeywords              GUI Editor
     */
    me.yaioHideAllNodeEditorForms = function() {
        // reset editor
        console.log("yaioHideAllNodeEditorForms: hide forms");
        // hide forms
        me.$("#containerFormYaioEditorCreate").css("display", "none");
        me.$("#containerFormYaioEditorTaskNode").css("display", "none");
        me.$("#containerFormYaioEditorEventNode").css("display", "none");
        me.$("#containerFormYaioEditorInfoNode").css("display", "none");
        me.$("#containerFormYaioEditorUrlResNode").css("display", "none");
        me.$("#containerFormYaioEditorSymLinkNode").css("display", "none");
    };
    
    /** 
     * reset all formfields
     * @FeatureDomain                GUI
     * @FeatureResult                GUI-result: empty all formfields
     * @FeatureKeywords              GUI Editor
     */
    me.yaioResetNodeEditorFormFields = function() {
        // reset data
        // configure value mapping
        var basenode = {};
        for (var formName in me.appBase.config.configNodeTypeFields) {
            var fields = new Array();
            fields = fields.concat(me.appBase.config.configNodeTypeFields.Common.fields);
            fields = fields.concat(me.appBase.config.configNodeTypeFields[formName].fields);
            for (var idx in fields) {
                var field = fields[idx];
                me.yaioSetFormField(field, formName, basenode);
            }
        }
    };
    
    /** 
     * updates the formfield with the nodedata
     * @FeatureDomain                GUI
     * @FeatureResult                GUI-result: updates formfield
     * @FeatureKeywords              GUI Editor
     * @param field                  fieldconfig from me.appBase.config.configNodeTypeFields
     * @param fieldSuffix            sufix of the fieldName to identify the form (nodeclass of basenode)
     * @param basenode               the node to map the fieldvalue
     */
    me.yaioSetFormField = function(field, fieldSuffix, basenode) {
        var svcDataUtils = me.appBase.get('DataUtils');
        var fieldName = field.fieldName;
        var fieldNameId = "#input" + fieldName.charAt(0).toUpperCase() + fieldName.slice(1) + fieldSuffix;
        var value = basenode[fieldName];
        
        // convert value
        if (field.datatype === "integer" && (! value || value == "undefined" || value === null)) {
            // specical int
            value = 0;
        } else if (field.datatype === "date")  {
            // date
            value = svcDataUtils.formatGermanDate(value);
        } else if (field.datatype === "datetime")  {
            // date
            value = svcDataUtils.formatGermanDateTime(value);
        } else if (! value || value == "undefined" || value == null) {
            // alle other
            value = "";
        } 
        
        // reescape data for form
        if (fieldName === "nodeDesc") {
            value = value.replace(/<WLBR>/g, "\n");
            value = value.replace(/<WLESC>/g, "\\");
            value = value.replace(/<WLTAB>/g, "\t");
        }
        
        // set depending on the fieldtype
        if (field.type === "hidden") {
            me.$(fieldNameId).val(value).trigger('input').triggerHandler("change");
        } else if (field.type === "select") {
            me.$(fieldNameId).val(value).trigger('select').triggerHandler("change");
        } else if (field.type === "checkbox") {
            if (value) {
                me.$(fieldNameId).prop("checked", true);
            } else {
                me.$(fieldNameId).prop("checked", false);
            }
            me.$(fieldNameId).trigger('input').triggerHandler("change");
        } else if (field.type === "textarea") {
            me.$(fieldNameId).val(value).trigger('select').triggerHandler("change");
        } else {
            // input
            me.$(fieldNameId).val(value).trigger('input');
        }
        console.log("yaioSetFormField map nodefield:" + fieldName 
                + " set:" + fieldNameId + "=" + value);
        
    };
    
    /** 
     * open the nodeeditor for the node (toggle it fromleft), transfer the data from node to the formfields  
     * @FeatureDomain                GUI
     * @FeatureResult                GUI-result: reset forms+field, hide forms, open the spcific form for the nodeclass, updates fields
     * @FeatureKeywords              GUI Editor
     * @param nodeId                 id of the node
     * @param mode                   edit, create, createsymlink
     * @param newNode                optional basedata for the new node
     */
    me.yaioOpenNodeEditor = function(nodeId, mode, newNode) {
        var svcLogger = me.appBase.get('Logger');

        // reset editor
        console.log("yaioOpenNodeEditor: reset editor");
        me.yaioResetNodeEditor();
        
        // check vars
        if (! nodeId) {
            // tree not found
            svcLogger.logError("error yaioOpenNodeEditor: nodeId required", false);
            return null;
        }
        // load node
        var tree = me.$("#tree").fancytree("getTree");
        if (!tree) {
            // tree not found
            svcLogger.logError("error yaioOpenNodeEditor: cant load tree for node:" + nodeId, false);
            return null;
        }
        var treeNode = tree.getNodeByKey(nodeId);
        if (! treeNode) {
            svcLogger.logError("error yaioOpenNodeEditor: cant load node:" + nodeId, false);
            return null;
        }
        
        // extract nodedata
        var basenode = treeNode.data.basenode;
        
        // open editor
        me.yaioOpenNodeEditorForNode(basenode, mode, newNode);
    };
        
    /** 
     * open the nodeeditor for the node (toggle it fromleft), transfer the data from node to the formfields  
     * @FeatureDomain                GUI
     * @FeatureResult                GUI-result: reset forms+field, hide forms, open the spcific form for the nodeclass, updates fields
     * @FeatureKeywords              GUI Editor
     * @param basenode               the node
     * @param mode                   edit, create, createsymlink
     * @param newNode                optional node to copy data from (for mode createsnapshot...)
     */
    me.yaioOpenNodeEditorForNode = function(basenode, mode, newNode) {
        var svcLogger = me.appBase.get('Logger');
        var svcDataUtils = me.appBase.get('DataUtils');
        var svcYaioLayout = me.appBase.get('YaioLayout');
        var svcYaioMarkdownEditor = me.appBase.get('YaioMarkdownEditor');

        // reset editor
        console.log("yaioOpenNodeEditor: reset editor");
        me.yaioResetNodeEditor();
        
        // check vars
        if (! basenode) {
            // tree not found
            svcLogger.logError("error yaioOpenNodeEditor: basenode required", false);
            return null;
        }
        var nodeId = basenode.sysUID;
    
        // check mode    
        var fields = new Array();
        var formSuffix, fieldSuffix;
        var origBasenode = basenode;
        if (mode === "edit") {
            // mode edit
            
            // configure value mapping
            fields = fields.concat(me.appBase.config.configNodeTypeFields.Common.fields);
            if (basenode.className === "TaskNode") {
                fields = fields.concat(me.appBase.config.configNodeTypeFields.TaskNode.fields);
            } else if (basenode.className === "EventNode") {
                fields = fields.concat(me.appBase.config.configNodeTypeFields.EventNode.fields);
            } else if (basenode.className === "InfoNode") {
                fields = fields.concat(me.appBase.config.configNodeTypeFields.InfoNode.fields);
            }  else if (basenode.className === "UrlResNode") {
                fields = fields.concat(me.appBase.config.configNodeTypeFields.UrlResNode.fields);
            }  else if (basenode.className === "SymLinkNode") {
                fields = fields.concat(me.appBase.config.configNodeTypeFields.SymLinkNode.fields);
            }
            
            // set formSuffix
            formSuffix = basenode.className;
            fieldSuffix = basenode.className;
            basenode.mode = "edit";
            console.log("yaioOpenNodeEditor mode=edit for node:" + nodeId);
        } else if (mode === "create") {
            // mode create
            formSuffix = "Create";
            fieldSuffix = "Create";
            fields = fields.concat(me.appBase.config.configNodeTypeFields.Create.fields);
            
            // new basenode
            basenode = {
                    mode: "create",
                    sysUID: origBasenode.sysUID
            };
            console.log("yaioOpenNodeEditor mode=create for node:" + nodeId);
        } else if (mode === "createsymlink") {
            // mode create
            formSuffix = "SymLinkNode";
            fieldSuffix = "SymLinkNode";
            fields = fields.concat(me.appBase.config.configNodeTypeFields.CreateSymlink.fields);
    
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
        } else if (mode === "createuploadurlresnode") {
            // mode create
            formSuffix = "UrlResNode";
            fieldSuffix = "UrlResNode";
            fields = fields.concat(me.appBase.config.configNodeTypeFields.CreateUploadFileUrlResNode.fields);

            // new basenode
            basenode = {
                mode: "create",
                sysUID: origBasenode.sysUID,
                name: newNode.name,
                className: "UrlResNode",
                type: "FILERES",
                state: "FILERES",
                resLocRef: newNode.resLocRef,
                resLocName: newNode.resLocName,
                resLocTags: newNode.resLocTags,
                uploadFile: newNode.uploadFile
            };
            console.log("yaioOpenNodeEditor mode=createupload for node:" + nodeId);
        } else if (mode === "createsnapshot") {
            // mode create
            formSuffix = "InfoNode";
            fieldSuffix = "InfoNode";
            fields = fields.concat(me.appBase.config.configNodeTypeFields.CreateSnapshot.fields);
    
            // new basenode
            basenode = {
                    mode: "create",
                    sysUID: origBasenode.sysUID,
                    name: "Snapshot für: '" + origBasenode.name + "' vom " + svcDataUtils.formatGermanDateTime((new Date()).getTime()),
                    type: "INFO",
                    state: "INFO",
                    className: "InfoNode",
                    nodeDesc: newNode.nodeDesc
            };
            console.error("yaioOpenNodeEditor mode=createsnapshot for node:" + nodeId);
        } else {
            svcLogger.logError("error yaioOpenNodeEditor: unknown mode=" + mode 
                    + " for nodeId:" + nodeId, false);
            return null;
        }
        
        // iterate fields
        for (var idx in fields) {
            var field = fields[idx];
            me.yaioSetFormField(field, fieldSuffix, basenode);
        }
        
        // show editor
        var width = me.$("#box_data").width();
        console.log("yaioOpenNodeEditor show editor: " + formSuffix 
                + " for node:" + nodeId);
    
        // set width
        me.$("#containerYaioEditor").css("width", "900px");
        me.$("#containerBoxYaioEditor").css("width", "900px");
        me.$("#containerYaioTree").css("width", (width - me.$("#containerYaioEditor").width() - 30) + "px");
        
        // display editor and form for the formSuffix
        me.$("#containerBoxYaioEditor").css("display", "block");
        me.$("#containerFormYaioEditor" + formSuffix).css("display", "block");
        //me.$("#containerYaioEditor").css("display", "block");
        me.appBase.get('UIToggler').toggleElement("#containerYaioEditor");
    
        // create Elements if not exists
        svcYaioLayout.createTogglerIfNotExists("legendIstTaskForm", "filterIstTaskForm", "filter_IstTaskNode");
        svcYaioLayout.createTogglerIfNotExists("legendDescTaskForm", "filterDescTaskForm", "filter_DescTaskNode");
        svcYaioLayout.createTogglerIfNotExists("legendIstEventForm", "filterIstEventForm", "filter_IstEventNode");
        svcYaioLayout.createTogglerIfNotExists("legendDescEventForm", "filterDescEventForm", "filter_DescEventNode");
        svcYaioLayout.createTogglerIfNotExists("legendLayoutInfoForm", "filterLayoutInfoForm", "filter_LayoutInfoNode");
        svcYaioLayout.createTogglerIfNotExists("legendDescInfoForm", "filterDescInfoForm", "filter_DescInfoNode");
        svcYaioLayout.createTogglerIfNotExists("legendLayoutUrlResForm", "filterLayoutUrlResForm", "filter_LayoutUrlResNode");
        svcYaioLayout.createTogglerIfNotExists("legendDescUrlResForm", "filterDescUrlResForm", "filter_DescUrlResNode");
        svcYaioLayout.createTogglerIfNotExists("legendDescSymLinkForm", "filterDescSymLinkForm", "filter_DescSymLinkNode");
        
        // hide empty, optional elements
        svcYaioLayout.hideFormRowTogglerIfSet("filterIstTaskForm", "filter_IstTaskNode", false);
        svcYaioLayout.hideFormRowTogglerIfSet("filterDescTaskForm", "filter_DescTaskNode", false);
        svcYaioLayout.hideFormRowTogglerIfSet("filterIstEventForm", "filter_IstEventNode", false);
        svcYaioLayout.hideFormRowTogglerIfSet("filterDescEventForm", "filter_DescEventNode", false);
        svcYaioLayout.hideFormRowTogglerIfSet("filterLayoutInfoForm", "filter_LayoutInfoNode", false);
        svcYaioLayout.hideFormRowTogglerIfSet("filterDescInfoForm", "filter_DescInfoNode", false);
        svcYaioLayout.hideFormRowTogglerIfSet("filterLayoutUrlResForm", "filter_LayoutUrlResNode", false);
        svcYaioLayout.hideFormRowTogglerIfSet("filterDescUrlResForm", "filter_DescUrlResNode", false);
        svcYaioLayout.hideFormRowTogglerIfSet("filterDescSymLinkForm", "filter_DescSymLinkNode", false);
    
        // create nodeDesc-editor
        svcYaioMarkdownEditor.createMarkdownEditorForTextarea("editorInputNodeDescTaskNode", "inputNodeDescTaskNode");
        svcYaioMarkdownEditor.createMarkdownEditorForTextarea("editorInputNodeDescEventNode", "inputNodeDescEventNode");
        svcYaioMarkdownEditor.createMarkdownEditorForTextarea("editorInputNodeDescInfoNode", "inputNodeDescInfoNode");
        svcYaioMarkdownEditor.createMarkdownEditorForTextarea("editorInputNodeDescUrlResNode", "inputNodeDescUrlResNode");
        svcYaioMarkdownEditor.createMarkdownEditorForTextarea("editorInputNodeDescSymLinkNode", "inputNodeDescSymLinkNode");
        
        // update appsize
        svcYaioLayout.setupAppSize();

        // set uploadfile
        if (mode === "createuploadurlresnode") {
            me.setUploadFileUrlResNode(basenode);
        }
    };
    
    /** 
     * close the nodeditor, toggle it to the left
     * @FeatureDomain                GUI
     * @FeatureResult                GUI-result: close the editor
     * @FeatureKeywords              GUI Tree Editor
     */
    me.yaioCloseNodeEditor = function() {
        console.log("close editor");
        me.appBase.get('UIToggler').toggleElement("#containerYaioEditor");
        me.yaioResetNodeEditor();
    };
    
    /** 
     * a hack to call updatetrigger for the element because for speechregognition the popup
     * cant call the trigger for another window (security)<br>
     * the function binds to the current document-window
     * @FeatureDomain                GUI
     * @FeatureResult                GUI-result: calls updatetrigger for element
     * @FeatureKeywords              GUI Editor SpeechRecognition
     * @param element                element (HTML-Element) to fire the trigger
     */
    me.callUpdateTriggerForElement = function(element) {
        if (element != null) {
            me.$(element).trigger('input').triggerHandler("change");
            me.$(element).trigger('select').triggerHandler("change");
            me.$(element).trigger('input');
            me.$(element).focus();

            // update ace-editor
            var parentEditor = me.$(element).data('aceEditor');
            if (parentEditor) {
                parentEditor.setValue(me.$(element).val());
            }
        }
    };

    /** 
     * handler for drag&drop-dragover
     * @FeatureDomain                GUI
     * @FeatureResult                GUI-result: show copy-hint
     * @FeatureKeywords              GUI Editor
     * @param evt                    Drag&Drop-event
     */
    me.handleUploadFileUrlResNodeDragOver = function(evt) {
        // Explicitly show this is a copy.
        evt.stopPropagation();
        evt.preventDefault();
        evt.dataTransfer.dropEffect = 'copy';
    };

    /** 
     * handler for drag&drop-drag: open the UrlResNode-Editor with the filedata to create and upload the file
     * File-Drag&Drop&Read inspired by http://www.html5rocks.com/de/tutorials/file/dndfiles/
     * @FeatureDomain                GUI
     * @FeatureResult                GUI-result: show uploadform
     * @FeatureKeywords              GUI Editor
     * @param evt                    Drag&Drop-event
     */
    me.handleUploadFileUrlResNodeSelect = function(evt) {
        evt.stopPropagation();
        evt.preventDefault();

        // get files
        var files = evt.dataTransfer.files;
        var file = files[0];
        var parentSysUID = evt.target.getAttribute('data-parentsysuid');

        // check data
        if (!parentSysUID || !file) {
            me.appBase.get('Logger').logError("error: parentSysUID and file required", false);
        }
        var baseNode = {
            sysUID: parentSysUID,
            className: 'UrlResNode',
            type: 'FILERES',
            name: file.name,
            resLocRef: file.name,
            resLocName: file.name,
            uploadFile: file
        };

        // open Editor
        me.yaioOpenNodeEditorForNode(baseNode, 'createuploadurlresnode', baseNode);
    };

    /** 
     * set uploadFile in angular
     * @FeatureDomain                GUI
     * @FeatureResult                GUI-result: init uploadFile
     * @FeatureKeywords              GUI Editor
     * @param basenode               the node-data sith attr: uploadfile
     */
    me.setUploadFileUrlResNode = function (basenode) {
        // set uploadFile in scope
        var uploadFile = basenode.uploadFile;
        var element = document.getElementById("inputTypeUrlResNode");
        angular.element(element).scope().setUploadFileUrlResNode(uploadFile, true);
    };


    /*****************************************
     *****************************************
     * Service-Funktions (businesslogic)
     *****************************************
     *****************************************/
    
    /** 
     * recalcs the istStand depending on the state/type
     * if ERLEDIGT || VERWORFEN || EVENT_ERLEDIGT || EVENT_VERWORFEN: update istStand=100
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                ReturnValue Integer - the recalced stand
     * @FeatureKeywords              BusinessLogic
     * @param basenode               the node to recalc
     * @return                       istStand in %
     */
    me.calcIstStandFromState = function(basenode) {
        var istStand = basenode.istStand;
        if (   basenode.type == "EVENT_ERLEDIGT"
            || basenode.type == "EVENT_VERWORFEN"
            || basenode.type == "ERLEDIGT"
            || basenode.type == "VERWORFEN") {
            istStand = 100;
        }
        console.log("calcIstStandFromState for node:" + basenode.sysUID + " state=" + basenode.type + " new istStand=" + istStand);
        
        return istStand;
    };
    
    /** 
     * recalcs the type/state depending on the istStand
     * <ul>
     *   <li>if className=TaskNode && 0: update type=OFFEN
     *   <li>if className=TaskNode && >0&&<100 && ! WARNING: update type=RUNNING
     *   <li>if className=TaskNode && 100 && != VERWORFEN: update type=ERLEDIGT
     *   <li>if className=EventNode && 0: update type=EVENT_PLANED
     *   <li>if className=EventNode && >0&&<100 && ! EVENT_WARNING: update type=EVENT_RUNNING
     *   <li>if className=EventNode && 100 && != EVENT_VERWORFEN: update type=EVENT_ERLEDIGT
     * </ul>
     * @FeatureDomain                BusinessLogic
     * @FeatureResult                ReturnValue String - the recalced type/state
     * @FeatureKeywords              BusinessLogic
     * @param basenode               the node to recalc
     * @return string                the recalced type
     */
    me.calcTypeFromIstStand = function(basenode) {
        var type = basenode.type;
    
        if (basenode.className == "TaskNode") {
            // TaskNode
            if (basenode.istStand == "0") {
                // 0: OFFEN
                type = "OFFEN"; 
            } else if (basenode.istStand == 100 && basenode.type !== "VERWORFEN") {
                // 100: ERLEDIGT if not VERWORFEN already
                type = "ERLEDIGT"; 
            } else if (basenode.istStand < 100 && basenode.istStand > 0) {
                // 0<istStand<100: RUNNING if not WARNING already
                if (basenode.type !== "WARNING") {
                    type = "RUNNING"; 
                }
            }
        } else if (basenode.className == "EventNode") {
            // EventNode
            if (basenode.istStand == "0") {
                // 0: EVENT_PLANED
                type = "EVENT_PLANED"; 
            } else if (basenode.istStand == 100 && basenode.type !== "EVENT_VERWORFEN") {
                // 100: EVENT_ERLEDIGT if not EVENT_VERWORFEN already
                type = "EVENT_ERLEDIGT"; 
            } else if (basenode.istStand < 100 && basenode.istStand > 0) {
                // 0<istStand<100: EVENT_RUNNING if not EVENT_WARNING already
                if (basenode.type !== "EVENT_WARNING") {
                    type = "EVENT_RUNNING"; 
                }
            }
        }
        console.log("calcTypeFromIstStand for node:" + basenode.sysUID + " istStand=" + basenode.istStand + " newstate=" + type);
        
        return type;
    };
    
    me._init();
    
    return me;
};