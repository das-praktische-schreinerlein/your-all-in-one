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
 * servicefunctions for markdowneditor
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

Yaio.MarkdownEditorService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    me.createNodeDescEditorForNode = function(parentId, textAreaId) {
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
        editor.setValue(me.$("#" + textAreaId).val());
        
        // set eventhandler to update corresponding textarea
        editor.getSession().on('change', function(e) {
            // update textarea for angular
            me.$("#" + textAreaId).val(editor.getValue()).trigger('select').triggerHandler("change");
        });
        
        // set editor as data-attr on parent
        me.$("#" + parentId).data("aceEditor", editor);
        
        return editor;
    };
    
    me.showPreviewForTextareaId = function(textAreaId) {
        var descText = me.$("#" + textAreaId).val();
    
        // prepare descText
        var descHtmlMarked = me.appBase.get('YaioFormatter').formatMarkdown(descText, true);
        me.showPreview(descHtmlMarked);
    };
    
        
    me.showPreview = function(content) {
        var svcYaioFormatter = me.appBase.get('YaioFormatter');

        // set preview-content
        me.$( "#preview-content" ).html(content);
        
        // show message
        me.$( "#preview-box" ).dialog({
            modal: true,
            width: "1050px",
            buttons: {
              Ok: function() {
                me.$( this ).dialog( "close" );
              },
              "Vorlesen": {
                  id: "Vorlesen",
                  text: "Vorlesen",
                  class: "jsh-show-inline-block-if-speechsynth",
                  click: function () {
                      me.appBase.get('YaioLayout').openSpeechSynthWindow(document.getElementById('preview-content'));
                  }
              },
            }
        });    
        
        // do mermaid when preview visible
        svcYaioFormatter.formatMermaidGlobal();
    
        // do syntax-highlight
        svcYaioFormatter.formatDescBlock(me.$("#preview-content"));
    };
    
    me.showMarkdownHelp = function() {
        // show message
        var url = "/examples/markdownhelp/markdownhelp.html";
        console.log("showMarkdownHelp:" + url);
        me.$("#markdownhelp-iframe").attr('src',url);
        me.$("#markdownhelp-box" ).dialog({
            modal: true,
            width: "1200px",
            buttons: {
                "Schliessen": function() {
                  me.$( this ).dialog( "close" );
                },
                "Eigenes Fenster": function() {
                    var helpFenster = window.open(url, "markdownhelp", "width=1200,height=500,scrollbars=yes,resizable=yes");
                    helpFenster.focus();
                    me.$( this ).dialog( "close" );
                } 
            }
        });    
    };
    
    
    me.openWysiwygForTextareaId = function(textAreaId) {
        // get existing parentEditor
        var parentEditorId = "editor" + textAreaId.charAt(0).toUpperCase() + textAreaId.substring(1);
        var parentEditor = me.$("#" + parentEditorId).data("aceEditor");
        console.log("found parentEditor on:" + parentEditorId);
    
        // create  Editor
        var myParentId = "wysiwyg-editor";
        var editor = me.createNodeDescEditorForNode(myParentId, textAreaId);
    
        // reset intervallHandler for this parent
        var intervalHandler = me.$("#" + myParentId).data("aceEditor.intervalHandler");
        if (intervalHandler != "undefined") {
            console.log("openWysiwygForTextareaId: clear old Interval : " + intervalHandler + " for " + myParentId);
            clearInterval(intervalHandler);
        }
        // create new intervalHandler: check every 5 second if there is a change und update all
        me.$("#" + myParentId).data("aceEditor.flgChanged", "false");
        intervalHandler = setInterval(function(){ 
            // check if something changed
            if (me.$("#" + myParentId).data("aceEditor.flgChanged") != "true") {
                // nothing changed
                return;
            }
            
            console.log("openWysiwygForTextareaId: updateData : " + " for " + myParentId);
    
            // reset flag
            me.$("#" + myParentId).data("aceEditor.flgChanged", "false");
    
            // update textarea for angular
            var value = editor.getValue();
            me.$("#" + textAreaId).val(value).trigger('select').triggerHandler("change");
            console.log("openWysiwygForTextareaId: updatetextAreaId: " + textAreaId);
    
            // update preview
            me.showWyswhgPreviewForTextareaId(textAreaId);
            
            // update parent
            if (parentEditor) {
                parentEditor.setValue(value);
            }
        }, 5000);
        console.log("openWysiwygForTextareaId: setIntervall : " + intervalHandler + " for " + myParentId);
        me.$("#" + myParentId).data("aceEditor.intervalHandler", intervalHandler);
        
        // set update-event
        editor.getSession().on('change', function(e) {
            me.$("#" + myParentId).data("aceEditor.flgChanged", "true");
        });
        
        // init preview
        me.showWyswhgPreviewForTextareaId(textAreaId);
    
        // show message
        me.$( "#wysiwyg-box" ).dialog({
            modal: true,
            width: "1200px",
            buttons: {
              Ok: function() {
                me.$( this ).dialog( "close" );
                console.log("openWysiwygForTextareaId: clearMyInterval : " + intervalHandler + " for " + myParentId);
                clearInterval(intervalHandler);
              },
              "Hilfe": function () {
                  me.showMarkdownHelp();
              },
              "Vorschau": function () {
                  me.showPreviewForTextareaId(textAreaId);
              },
              "Vorlesen": {
                  id: "Vorlesen",
                  text: "Vorlesen",
                  class: "jsh-show-inline-block-if-speechsynth",
                  click: function () {
                      me.appBase.get('YaioLayout').openSpeechSynthWindow(document.getElementById('wysiwyg-preview'));
                  }
              },
              "Load": function () {
                  // define handler
                  var handleImportMarkdownFileSelectHandler = function handleImportMarkdownFileSelect(evt) {
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
                              me.$("#" + myParentId).data("aceEditor.flgChanged", "true");
                          };
                          
                          // read the file
                          reader.readAsText(file);
                          
                          i = files.length +1000;
                      }
                  };
                  
                  // initFileUploader
                  var fileDialog = document.getElementById('wysiwygImportMarkdownFile');
                  fileDialog.addEventListener('change', handleImportMarkdownFileSelectHandler, false);
                  me.$( "#wysiwygmarkdownuploader-box" ).dialog({
                      modal: true,
                      width: "200px",
                      buttons: {
                        "Schließen": function() {
                          me.$( this ).dialog( "close" );
                        }
                      }
                  });
               }
            }
        });
        // add export-link -> buggy to mix jquery and styles
        me.$(".ui-dialog-buttonset").append(me.$("<a href='' id='wysiwyg-exportlink'" +
            + " sdf='ojfvbhwjh'"
            + " onclick=\"yaioAppBase.get('YaioFile').downloadAsFile(yaioAppBase.$('#wysiwyg-exportlink'), yaioAppBase.$('#" + textAreaId + "').val(), 'data.md', 'text/markdown', 'utf-8');\">"
            + "<span class='ui-button-text'>Export</span></a>"));
        me.$('#wysiwyg-exportlink').addClass("ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only");
    
    };
    
    me.showWyswhgPreviewForTextareaId = function(textAreaId) {
        var svcYaioFormatter = me.appBase.get('YaioFormatter');
        
        // prepare descText
        var descText = me.$("#" + textAreaId).val();
        var descHtmlMarked = svcYaioFormatter.formatMarkdown(descText, true);
    
        // set preview-content
        me.$( "#wysiwyg-preview" ).html(descHtmlMarked);
    
        // do mermaid when preview visible
        svcYaioFormatter.formatMermaidGlobal();
        
        // do syntax-highlight
        svcYaioFormatter.formatDescBlock(me.$("#wysiwyg-preview"));
    };

    me._init();
    
    return me;
};
