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
    if (yaioAppBase.getDetector('SpeechRecognitionDetector').isSupported()) {
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
                        " src='" + yaioAppBase.getService('SpeechRecognitionHelper').config.statusImgSrcStart + "'></a>";
                
                // set flag
                $(label).attr("webkitSpeechRecognitionAdded", "true");
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
    if (target == null) { target = self; }
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
    if (yaioAppBase.getDetector('SpeechSynthDetector').isSupported()) {
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
                $(label).attr("speechSynthAdded", "true");
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
    var flgClassNoWrap = "flg-pre-nowrap";
    var flgClassWrap = "flg-pre-wrap";
    var codeChilden = $(element).find("code");
    
    // remove/add class if element no has class
    if ($(element).hasClass(flgClassNoWrap)) {
        $(element).removeClass(flgClassNoWrap).addClass(flgClassWrap);
        console.log("togglePreWrap for id:" + element + " set " + classWrap);
        // wrap code-blocks too
        $(codeChilden).removeClass(classNoWrap).addClass(classWrap);
        $(codeChilden).parent().removeClass(classNoWrap).addClass(classWrap);
    } else {
        $(element).removeClass(flgClassWrap).addClass(flgClassNoWrap);
        console.log("togglePreWrap for id:" + element + " set " + classNoWrap);
        // wrap code-blocks too
        $(codeChilden).removeClass(classWrap).addClass(classNoWrap);
        $(codeChilden).parent().removeClass(classNoWrap).addClass(classWrap);
   }
 }

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     init the multilanguage support for all tags with attribute <XX lang="de">
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: init multilanguage-support
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor Multilanguagesupport
 * @param langKey - key of the preferred-language
 */
function initLanguageSupport(langKey) {
    // Create language switcher instance and set default language to tech
    window.lang = new Lang('tech');

    //Define the de language pack as a dynamic pack to be loaded on demand
    //if the user asks to change to that language. We pass the two-letter language
    //code and the path to the language pack js file
    window.lang.dynamic('de', 'lang/lang-tech-to-de.json');
    window.lang.dynamic('en', 'lang/lang-tech-to-en.json');
    window.lang.loadPack('de');
    window.lang.loadPack('en');

    // change to de
    window.lang.change(langKey);
}

function setupAppSize() {
    var height = window.innerHeight;
    var width = window.innerWidth;
    
    // YAIO-editor
    var ele = $("#containerBoxYaioEditor");
    if (ele.length > 0) {
        // we are relative to the tree
        var paddingToHead = $("#containerYaioTree").position().top;
        var left = $("#containerYaioTree").position().left + $("#containerYaioTree").width + 2;

        // set posTop as scrollTop burt never < paddingToHead
        var posTop = $(window).scrollTop();
        if (posTop < paddingToHead) {
            posTop = paddingToHead;
        }
        
        // calc maxHeight = windHeight - 20 (puffer)
        var maxHeight = height - 20;
        // sub topPos - Scollpos
        maxHeight = maxHeight - (posTop - $(window).scrollTop());

        // set values
        $(ele).css("position", "absolute");
        $(ele).css("max-height", maxHeight);
        $(ele).css("top", posTop);
        $(ele).css("left", left);
        
        console.log("setup size containerBoxYaioEditor width:" + window.innerWidth 
                + " height:" + window.innerHeight 
                + " scrollTop:" + $(window).scrollTop()
                + " offset.top" + $(ele).offset().top
                + " top:" + posTop
                + " max-height:" + $(ele).css("max-height")
                );
    }
    
    // Export-editor
    ele = $("#containerFormYaioEditorOutputOptions");
    if (ele.length > 0) {
        $(ele).css("max-height", height-$(ele).offset().top);
        console.log("setup size containerFormYaioEditorOutputOptions width:" + window.innerWidth 
                + " height:" + window.innerHeight 
                + " scrollTop:" + $(window).scrollTop()
                + " offset.top" + $(ele).offset().top
                + " max-height:" + $(ele).css("max-height")
                );
    }
    // Import-editor
    ele = $("#containerFormYaioEditorImport");
    if (ele.length > 0) {
        $(ele).css("max-height", height-$(ele).offset().top);
        console.log("setup size containerFormYaioEditorImport width:" + window.innerWidth 
                + " height:" + window.innerHeight 
                + " scrollTop:" + $(window).scrollTop()
                + " offset.top" + $(ele).offset().top
                + " max-height:" + $(ele).css("max-height")
                );
    }

    // Frontpage
    ele = $("#front-content-intro");
    if (0 && ele.length > 0) {
        var maxHeight = height-$(ele).offset().top;
        
        // sub todonextbox
        if ($('#box_todonext').length > 0 ) {
            if ($('#box_todonext').height > 0) {
                maxHeight = maxHeight - $('#box_todonext').height;
            } else {
                // sometime height is not set: then default
                maxHeight = maxHeight - 100;
            }
        }
        $(ele).css("max-height", maxHeight);
        console.log("setup size front-content-intro width:" + window.innerWidth 
                + " height:" + window.innerHeight 
                + " scrollTop:" + $(window).scrollTop()
                + " offset.top" + $(ele).offset().top
                + " max-height:" + $(ele).css("max-height")
                );
    }
}

function yaioShowHelpSite(url) {
    // set messagetext
    url += "?" + createXFrameAllowFrom();
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
 }

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
             var descBlock = $("#container_content_desc_" + id);
             if ($(descBlock).hasClass('syntaxhighlighting-open')) {
                 var flgDoMermaid = false;
                 console.log("toggleNodeDescContainer highlight for descBlock: " + $(descBlock).attr('id'));
                 flgDoMermaid = formatDescBlock(descBlock) || flgDoMermaid;
                 console.log("toggleNodeDescContainer resulting flgDoMermaid: " + flgDoMermaid);
                 
                 // do Mermaid if found
                 if (flgDoMermaid) {
                     formatMermaidGlobal();
                 }
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
         var flgDoMermaid = false;
         $("div.syntaxhighlighting-open").each(function (i, descBlock) {
             console.log("toggleAllNodeDescContainer highlight for descBlock: " + $(descBlock).attr('id'));
             flgDoMermaid = formatDescBlock(descBlock) || flgDoMermaid;
         });
         console.log("toggleAllNodeDescContainer resulting flgDoMermaid: " + flgDoMermaid);
         
         // mermaid all
         if (flgDoMermaid) {
             formatMermaidGlobal();
         }
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
 }

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
             link = "<a href=\"#\" id='showPreview4" + forElement.attr('id') + "'" +
                    " onClick=\"showPreviewForTextareaId('" +
                       forElement.attr('id') + "'); return false;" +
                    "\" lang='tech' data-tooltip='tooltip.command.OpenPreview' class='button'>common.command.OpenPreview</a>";
             link += "<a href=\"#\" id='openMarkdownHelp4" + forElement.attr('id') + "'" +
                     " onClick=\"showMarkdownHelp(); return false;" +
                     "\" lang='tech' data-tooltip='tooltip.command.OpenMarkdownHelp' class='button'>common.command.OpenMarkdownHelp</a>";
             
             // set flag
             $(label).attr("previewAdded", "true");
             console.log("addPreviewToElements: add : " + forName + " for " + forElement.attr('id'));
         }
         return link;
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
             link = "<a href=\"#\" id='openWysiwyg4" + forElement.attr('id') + "'" +
                 " onClick=\"openWysiwhgForTextareaId('" +
                     forElement.attr('id') + "'); return false;" +
                 "\" lang='tech' data-tooltip='tooltip.command.OpenWysiwygEditor' class='button'>common.command.OpenWysiwygEditor</a>";
             
             // set flag
             $(label).attr("wysiwhgAdded", "true");
             console.log("addWysiwhgToElements: add : " + forName + " for " + forElement.attr('id'));
         }
         return link;
     });
 }
 
