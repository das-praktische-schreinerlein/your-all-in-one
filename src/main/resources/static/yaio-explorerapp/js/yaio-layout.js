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
         if ($("#detail_desc_" + id).css("display") == "block") {
             $("#toggler_desc_" + id).addClass('toggler_show').removeClass('toggler_hidden');
         } else {
             $("#toggler_desc_" + id).addClass('toggler_hidden').removeClass('toggler_show');
         }
     });
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
                 "\" data-tooltip='Vorschau' class='icon-preview'></a>";
             
             // set flag
             $(label).attr("previewAdded", "true")
             console.log("addPreviewToElements: add : " + forName + " for " + forElement.attr('id'));
         }
         return link;
     });
 }
 
 function showPreviewForTextareaId(textAreaId) {
     var descText = $("#" + textAreaId).val();
     marked.setOptions({
         renderer: new marked.Renderer(),
         gfm: true,
         tables: true,
         breaks: false,
         pedantic: false,
         sanitize: true,
         smartLists: true,
         smartypants: false
       });  
     var descHtmlMarked = marked(descText);
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
           }
         }
     });    
 }

