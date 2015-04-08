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
 *     servicefunctions for markdowneditor
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

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
    
    // do mermaid when preview visible
    formatMermaidGlobal();

    // do syntax-highlight
    formatDescBlock($("#preview-content"));
}

function showMarkdownHelp() {
    // show message
    var url = "/examples/markdownhelp/markdownhelp.html" + "?" + createXFrameAllowFrom();
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
        console.log("openWysiwhgForTextareaId: updatetextAreaId: " + textAreaId);

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
    showWyswhgPreviewForTextareaId(textAreaId);

    // show message
    $( "#wysiwhg-box" ).dialog({
        modal: true,
        width: "1200px",
        buttons: {
          Ok: function() {
            $( this ).dialog( "close" );
            console.log("openWysiwhgForTextareaId: clearMyInterval : " + intervalHandler + " for " + myParentId);
            clearInterval(intervalHandler);
          },
          "Hilfe": function () {
              showMarkdownHelp();
          },
          "Vorschau": function () {
              showPreviewForTextareaId(textAreaId);
          },
          "Vorlesen": function () {
              openSpeechSynthWindow(document.getElementById('wysiwhg-preview'));
          },
          "Load": function () {
              // define handler
              var handleImportJSONFileSelectHandler = function handleImportJSONFileSelect(evt) {
                  var files = evt.target.files; // FileList object

                  // Loop through the FileList.
                  for (var i = 0, numFiles = files.length; i < numFiles; i++) {
                      var file = files[i];
                      var reader = new FileReader();

                      // config reader
                      reader.onload = function(res) {
                          console.log("read fileName:" + file.name);
                          var data = res.target.result;
                          
                          // set new content (textarea+editor)
                          editor.setValue(data);
                          $("#" + myParentId).data("aceEditor.flgChanged", "true");
                      };
                      
                      // read the file
                      reader.readAsText(file);
                      
                      i = files.length +1000;
                  }
              }
              // initFileUploader
              var fileDialog = document.getElementById('importJSONFile');
              fileDialog.addEventListener('change', handleImportJSONFileSelectHandler, false);
              $( "#jsonuploader-box" ).dialog({
                  modal: true,
                  width: "200px",
                  buttons: {
                    "Schließen": function() {
                      $( this ).dialog( "close" );
                    }
                  }
              });
           }
        }
    });
    // add export-link -> buggy to mix jquery and styles
    $(".ui-dialog-buttonset").append($("<a href='' id='wysiwyg-exportlink'" +
        + " sdf='ojfvbhwjh'"
        + " onclick=\"downloadAsFile($('#wysiwyg-exportlink'), $('#" + textAreaId + "').val(), 'data.md', 'text/markdown', 'utf-8');\">"
        + "<span class='ui-button-text'>Export</span></a>"));
    $('#wysiwyg-exportlink').addClass("ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only");

}

function showWyswhgPreviewForTextareaId(textAreaId) {
    // prepare descText
    var descText = $("#" + textAreaId).val();
    var descHtmlMarked = formatMarkdown(descText, true);

    // set preview-content
    $( "#wysiwhg-preview" ).html(descHtmlMarked);

    // do mermaid when preview visible
    formatMermaidGlobal();
    
    // do syntax-highlight
    formatDescBlock($("#wysiwhg-preview"));
} 



