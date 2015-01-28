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
 *     servicefunctions for layout
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */


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
    // add speechrecognition if availiable
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
                    "\" lang='tech' data-tooltip='tooltip.command.OpenSpeechRecognition'>" +
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
 *     add speechSynth to nodeDesc-Label if availiable<br>
 *     set the flg speechSynthAdded on the element, so that there is no doubling
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: add speechSynth to elements
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor SpeechSynth
 */
function addSpeechSynthToElements() {
    // add speechSynth if availiable
    if (('speechSynthesis' in window)) {
        // add speechrecognition to nodeDesc+name
        $("label[for='nodeDesc']").append(function (idx) {
            var link = "";
            var label = this;
            
            // check if already set
            if ($(label).attr("speechSynthAdded")) {
                console.error("addSpeechSynthToElements: SKIP because already added: " + $(label).attr("for"));
                return link;
            }

            // get corresponding form
            var forName = $(label).attr("for");
            var form = $(label).closest("form");
            
            // get for-element byName from form
            var forElement = form.find("[name="+ forName + "]").first();
            if (forElement.length > 0) {
                // define link to label
                link = "<a href=\"\" class=\"button\"" +
                       " onClick=\"openSpeechSynthWindow(" +
                        "document.getElementById('" + forElement.attr('id') + "')); return false;" +
                       "\" lang='tech' data-tooltip='tooltip.command.OpenSpeechSynth' class='button'>common.command.OpenSpeechSynth</a>";
                
                // set flag
                $(label).attr("speechSynthAdded", "true")
                console.log("addSpeechSynthToElements: add : " + forName + " for " + forElement.attr('id'));
            }
            return link;
        });
    }
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     open speechsynth for element
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: open speechsynth for element
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor SpeechSynth
 * @param target - target-element to update (HTML-Element)
 */
function openSpeechSynthWindow(target) {
    if (target == null) target = self;
    target.focus();
    var speechsynthWindow = window.open('speechsynth.html', "speechsynth", "width=690,height=350,resizable=yes,dependent=yes,scrollbars=yes");
    speechsynthWindow.focus();
    if (speechsynthWindow.opener == null) { speechsynthWindow.opener = self; }
    speechsynthWindow.opener.targetElement = target;
}


/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     add datepicker to all input-elements with styleclass inputtype_date and inputtype_datetime
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: add datepicker
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor DatePicker
 */
function addDatePickerToElements() {
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
 *     add styleselectbox to all input-elements with styleclass inputtype_docLayoutAddStyleClass
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: add styleselectbox after input
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor
 */
function addDocLayoutStyleSelectorToElements() {
    // iterate over docLayoutSDtyleClass-elements
    $("input.inputtype_docLayoutAddStyleClass").each(function () {
        // add select only if id id set
        var ele = this;
        var id = $(ele).attr("id");
        if (id) {
            // add select
            var $select = $("<select id='" + id + "_select' lang='tech' />");
            
            // append values
            $select.append($("<option value=''>Standardstyle</option>"));
            $select.append($("<option value='row-label-value'>row-label-value</option>"));
            $select.append($("<option value='row-label-value'>row-label-value</option>"));
            $select.append($("<option value='row-boldlabel-value'>row-boldlabel-value</option>"));
            $select.append($("<option value='row-value-only-full'>row-value-only-full</option>"));
            $select.append($("<option value='row-label-only-full'>row-label-only-full</option>"));
            
            // add changehandler
            $select.change(function() {
                // set new value
                var style = $(this).val();
                $(ele).val(style);
                
                // call updatetrigger
                window.callUpdateTriggerForElement(ele);
            });
            
            // insert select after input
            $(ele).after($select);
        }
        
    });
}

/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     layout-servicefunctions
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

function togglePreWrap(element) {
    var classNoWrap = "pre-nowrap";
    var classWrap = "pre-wrap";
    var codeChilden = $(element).find("code");
    
    // remove/add class if element no has class
    if ($(element).hasClass(classNoWrap)) {
        $(element).removeClass(classNoWrap).addClass(classWrap);
        console.log("togglePreWrap for id:" + element + " set " + classWrap);
        // wrap code-blocks too
        $(codeChilden).removeClass(classNoWrap).addClass(classWrap);
        $(codeChilden).parent().removeClass(classNoWrap).addClass(classWrap);
    } else {
        $(element).removeClass(classWrap).addClass(classNoWrap);
        console.log("togglePreWrap for id:" + element + " set " + classNoWrap);
        // wrap code-blocks too
        $(codeChilden).removeClass(classWrap).addClass(classNoWrap);
        $(codeChilden).parent().removeClass(classNoWrap).addClass(classWrap);
   }
 }

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

 /**
  * <h4>FeatureDomain:</h4>
  *     Layout Toggler
  * <h4>FeatureDescription:</h4>
  *     Toggle the specified ojects with a fade. 
  * <h4>FeatureResult:</h4>
  *   <ul>
  *     <li>Updates DOM
  *   </ul> 
  * <h4>FeatureKeywords:</h4>
  *     GUI Tree Rendering
  * @param id - JQuery-Filter (html.id, style, objectlist...) 
  */
 function toggleTableBlock(id) {
     // get effect type from
     var selectedEffect = "fade";

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

 function togglePrintLayout() {
     if ($("#checkboxPrintAll").prop('checked')) {
         // print all
         $("#link_css_dataonly").attr("disabled", "disabled");
         $("#link_css_dataonly").prop("disabled", true);
     } else  {
         // print data only
         $("#link_css_dataonly").removeAttr("disabled");
         $("#link_css_dataonly").prop("disabled", false);
     }
 }
 
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
 function toggleNodeDescContainer(id) {
     $("#detail_desc_" + id).slideToggle(1000,function() {
         // show/hide toggler
         if ($("#detail_desc_" + id).css("display") == "block") {
             // desc is now shown
             $("#toggler_desc_" + id).addClass('toggler_show').removeClass('toggler_hidden');

             // check if syntaxhighlighting to do
             if ($("#container_content_desc_" + id).hasClass('syntaxhighlighting-open')) {
                 console.log("toggleNodeDescContainer highlight for: #container_content_desc_" + id);
                 
                 // remove trigger-flag
                 $("#container_content_desc_" + id).removeClass('syntaxhighlighting-open');
                 
                 // higlight code-blocks
                 $("#container_content_desc_" + id + " code").each(function(i, block) {
                     console.log("toggleNodeDescContainer highlight for #container_content_desc_" + id + " block: " + block.id);
                     hljs.highlightBlock(block);
                 });
             }
         } else {
             // desc is now hidden
             $("#toggler_desc_" + id).addClass('toggler_hidden').removeClass('toggler_show');
         }
     });
 }

 function toggleAllNodeDescContainer() {
     if ($("#toggler_desc_all").hasClass('toggler_hidden')) {
         // show all desc
         $("div.field_nodeDesc").slideDown(1000);
         $("div.fieldtype_descToggler > a").addClass('toggler_show').removeClass('toggler_hidden');

         // check if syntaxhighlighting to do
         $("div.syntaxhighlighting-open").each(function (i, descBlock) {
             console.log("toggleAllNodeDescContainer highlight for descBlock: " + descBlock.id);
             // remove trigger-flag
             $(descBlock).removeClass('syntaxhighlighting-open');
             
             // higlight code-blocks
             $("#" + descBlock.id + " code").each(function(i, block) {
                 console.log("toggleAllNodeDescContainer highlight descBlock: " + descBlock.id + " block: " + block.id);
                 hljs.highlightBlock(block);
             });
         });
     } else {
         // hide all desc
         $("div.field_nodeDesc").slideUp(1000);
         $("div.fieldtype_descToggler > a").addClass('toggler_hidden').removeClass('toggler_show');
     }
 }

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
 function toggleNodeSysContainer(id) {
     $("#detail_sys_" + id).slideToggle(1000,function() {
         // show/hide toggler
         if ($("#detail_sys_" + id).css("display") == "block") {
             // desc is now shown
             $("#toggler_sys_" + id).addClass('toggler_show').removeClass('toggler_hidden');
         } else {
             // desc is now hidden
             $("#toggler_sys_" + id).addClass('toggler_hidden').removeClass('toggler_show');
         }
     });
 }

 function toggleAllNodeSysContainer() {
     if ($("#toggler_sys_all").hasClass('toggler_hidden')) {
         // show all sys
         $("div.field_nodeSys").slideDown(1000);
         $("div.fieldtype_sysToggler > a").addClass('toggler_show').removeClass('toggler_hidden');
     } else {
         // hide all sys
         $("div.field_nodeSys").slideUp(1000);
         $("div.fieldtype_sysToggler > a").addClass('toggler_hidden').removeClass('toggler_show');
     }
 }

 /**
  * <h4>FeatureDomain:</h4>
  *     Layout Toggler
  * <h4>FeatureDescription:</h4>
  *     Toggle the specified ojects with a drop. 
  * <h4>FeatureResult:</h4>
  *   <ul>
  *     <li>Updates DOM
  *   </ul> 
  * <h4>FeatureKeywords:</h4>
  *     GUI Tree Rendering
  * @param id - JQuery-Filter (html.id, style, objectlist...) 
  */
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
 
 function hideFormRowTogglerIfSet(togglerId, className, state) {
     if (jMATService.getLayoutService().isInputRowsSet(className)) {
         // show all
         jMATService.getPageLayoutService().toggleFormrows(togglerId, className, true);
         
         // hide toggler
         $("#" + togglerId + "_On").css('display', 'none');
         $("#" + togglerId + "_Off").css('display', 'none');
     } else {
         // show or hide ??
         $("#" + togglerId + "_On").css('display', 'none');
         $("#" + togglerId + "_Off").css('display', 'block');
         jMATService.getPageLayoutService().toggleFormrows(togglerId, className, state);
     }
 }
 
 function createTogglerIfNotExists(parentId, toggleId, className) {
     var $ele = $("#" + toggleId + "_On");
     if ($ele.length <= 0) {
         // create toggler
         console.log("createTogglerIfNotExists link not exists: create new toggler parent=" + parentId 
                 + " toggleEleId=" + toggleId
                 + " className=" + className);
         jMATService.getPageLayoutService().appendFormrowToggler(parentId, toggleId, className, "&nbsp;");
     } else {
         console.log("createTogglerIfNotExists link exists: skip new toggler parent=" + parentId 
                 + " toggleEleId=" + toggleId
                 + " className=" + className);
     }
 }
 
 
 function createNodeDescEditorForNode(parentId, textAreaId) {
     var editor = ace.edit(parentId);
     
     // configure
     editor.setTheme("ace/theme/textmate");

     editor.getSession().setTabSize(4);
     editor.getSession().setUseSoftTabs(true);     
     editor.getSession().setMode("ace/mode/markdown");
     editor.setHighlightActiveLine(true);
     editor.setShowPrintMargin(true); 

     // options from http://ace.c9.io/build/kitchen-sink.html
     // editor.setShowFoldWidgets(value !== "manual");
     // editor.setOption("wrap", value);
     // editor.setOption("selectionStyle", checked ? "line" : "text");
     editor.setShowInvisibles(true);
     editor.setDisplayIndentGuides(true);
     editor.setPrintMarginColumn(80);
     editor.setShowPrintMargin(true);
     editor.setHighlightSelectedWord(true);
     // editor.setOption("hScrollBarAlwaysVisible", checked);
     // editor.setOption("vScrollBarAlwaysVisible", checked);
     editor.setAnimatedScroll(true);
     // editor.setBehavioursEnabled(checked);
     // editor.setFadeFoldWidgets(true);
     // editor.setOption("spellcheck", true);
     
     // set value
     editor.setValue($("#" + textAreaId).val());
     
     // set eventhandler to update corresponding textarea
     editor.getSession().on('change', function(e) {
         // update textarea for angular
         $("#" + textAreaId).val(editor.getValue()).trigger('select').triggerHandler("change");
     });
     
     // set editor as data-attr on parent
     $("#" + parentId).data("aceEditor", editor);
     
     return editor;
 }
 
 function addPreviewToElements() {
     // add preview to nodeDesc
     $("label[for='nodeDesc']").append(function (idx) {
         var link = "";
         var label = this;
         
         // check if already set
         if ($(label).attr("previewAdded")) {
             console.error("addPreviewElements: SKIP because already added: " + $(label).attr("for"));
             return link;
         }

         // get corresponding form
         var forName = $(label).attr("for");
         var form = $(label).closest("form");
         
         // get for-element byName from form
         var forElement = form.find("[name="+ forName + "]").first();
         if (forElement.length > 0) {
             // define link to label
             link = "<a href=\"#\" " +
                    " onClick=\"showPreviewForTextareaId('" +
                       forElement.attr('id') + "'); return false;" +
                    "\" lang='tech' data-tooltip='tooltip.command.OpenPreview' class='button'>common.command.OpenPreview</a>";
             link += "<a href=\"#\" " +
                     " onClick=\"showMarkdownHelp(); return false;" +
                     "\" lang='tech' data-tooltip='tooltip.command.OpenMarkdownHelp' class='button'>common.command.OpenMarkdownHelp</a>";
             
             // set flag
             $(label).attr("previewAdded", "true")
             console.log("addPreviewToElements: add : " + forName + " for " + forElement.attr('id'));
         }
         return link;
     });
 }
 
 function showPreviewForTextareaId(textAreaId) {
     var descText = $("#" + textAreaId).val();

     // prepare descText
     var descHtmlMarked = formatMarkdown(descText, true);
     showPreview(descHtmlMarked);
 }

     
 function showPreview(content) {
     // set preview-content
     $( "#preview-content" ).html(content);
     
     // show message
     $( "#preview-box" ).dialog({
         modal: true,
         width: "1050px",
         buttons: {
           Ok: function() {
             $( this ).dialog( "close" );
           },
           "Vorlesen": function () {
               openSpeechSynthWindow(document.getElementById('preview-content'));
           }
         }
     });    
 }
 
 function showMarkdownHelp() {
     // show message
     var url = "/examples/markdownhelp/markdownhelp.html";
     console.log("showMarkdownHelp:" + url);
     $("#markdownhelp-iframe").attr('src',url);
     $("#markdownhelp-box" ).dialog({
         modal: true,
         width: "1200px",
         buttons: {
             "Schliessen": function() {
               $( this ).dialog( "close" );
             },
             "Eigenes Fenster": function() {
                 var helpFenster = window.open(url, "markdownhelp", "width=1200,height=500,scrollbars=yes,resizable=yes");
                 helpFenster.focus();
                 $( this ).dialog( "close" );
             } 
         }
     });    
 }
 
 
 function addWysiwhgToElements() {
     // add preview to nodeDesc
     $("label[for='nodeDesc']").append(function (idx) {
         var link = "";
         var label = this;
         
         // check if already set
         if ($(label).attr("wysiwhgAdded")) {
             console.error("addWysiwhgElements: SKIP because already added: " + $(label).attr("for"));
             return link;
         }

         // get corresponding form
         var forName = $(label).attr("for");
         var form = $(label).closest("form");
         
         // get for-element byName from form
         var forElement = form.find("[name="+ forName + "]").first();
         if (forElement.length > 0) {
             // define link to label
             link = "<a href=\"#\" " +
                 " onClick=\"openWysiwhgForTextareaId('" +
                     forElement.attr('id') + "'); return false;" +
                 "\" lang='tech' data-tooltip='tooltip.command.OpenWysiwygEditor' class='button'>common.command.OpenWysiwygEditor</a>";
             
             // set flag
             $(label).attr("wysiwhgAdded", "true")
             console.log("addWysiwhgToElements: add : " + forName + " for " + forElement.attr('id'));
         }
         return link;
     });
 }
 
 function openWysiwhgForTextareaId(textAreaId) {
     // get existing parentEditor
     var parentEditorId = "editor" + textAreaId.charAt(0).toUpperCase() + textAreaId.substring(1);
     var parentEditor = $("#" + parentEditorId).data("aceEditor");
     console.log("found parentEditor on:" + parentEditorId);

     // create  Editor
     var myParentId = "wysiwhg-editor";
     var editor = createNodeDescEditorForNode(myParentId, textAreaId)

     // reset intervallHandler for this parent
     var intervalHandler = $("#" + myParentId).data("aceEditor.intervalHandler");
     if (intervalHandler != "undefined") {
         console.log("openWysiwhgForTextareaId: clear old Interval : " + intervalHandler + " for " + myParentId);
         clearInterval(intervalHandler)
     }
     // create new intervalHandler: check every 5 second if there is a change und update all
     $("#" + myParentId).data("aceEditor.flgChanged", "false");
     intervalHandler = setInterval(function(){ 
         // check if something changed
         if ($("#" + myParentId).data("aceEditor.flgChanged") != "true") {
             // nothing changed
             return;
         }
         
         console.log("openWysiwhgForTextareaId: updateData : " + " for " + myParentId);

         // reset flag
         $("#" + myParentId).data("aceEditor.flgChanged", "false");

         // update textarea for angular
         var value = editor.getValue();
         $("#" + textAreaId).val(value).trigger('select').triggerHandler("change");

         // update preview
         showWyswhgPreviewForTextareaId(textAreaId);
         
         // update parent
         if (parentEditor) {
             parentEditor.setValue(value);
         }
     }, 5000);
     console.log("openWysiwhgForTextareaId: setIntervall : " + intervalHandler + " for " + myParentId);
     $("#" + myParentId).data("aceEditor.intervalHandler", intervalHandler);
     
     // set update-event
     editor.getSession().on('change', function(e) {
         $("#" + myParentId).data("aceEditor.flgChanged", "true");
     });
     
     // init preview
     showWyswhgPreviewForTextareaId(textAreaId)

     // show message
     $( "#wysiwhg-box" ).dialog({
         modal: true,
         width: "1200px",
         buttons: {
           Ok: function() {
             $( this ).dialog( "close" );
             console.log("openWysiwhgForTextareaId: clearMyInterval : " + intervallHandler + " for " + myParentId);
             clearInterval(intervallHandler)
           },
           "Vorlesen": function () {
               openSpeechSynthWindow(document.getElementById('wysiwhg-preview'));
           }
         }
     });    
 }

 function showWyswhgPreviewForTextareaId(textAreaId) {
     // prepare descText
     var descText = $("#" + textAreaId).val();
     var descHtmlMarked = formatMarkdown(descText, true);

     // set preview-content
     $( "#wysiwhg-preview" ).html(descHtmlMarked);
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
     descText = descText.replace(/\<WLBR\>/g, "\n");
     descText = descText.replace(/\<WLESC\>/g, "\\");
     descText = descText.replace(/\<WLTAB\>/g, "\t");
     var nodeDesc = convertMarkdownToJira(descText);
     
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
 