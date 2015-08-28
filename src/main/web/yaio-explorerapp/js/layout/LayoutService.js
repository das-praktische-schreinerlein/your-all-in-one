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

Yaio.LayoutService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };


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
    me.addSpeechRecognitionToElements = function() {
        // add speechrecognition if availiable
        if (me.appBase.getDetector('SpeechRecognitionDetector').isSupported()) {
            // add speechrecognition to nodeDesc+name
            me.$("label[for='nodeDesc'], label[for='name']").append(function (idx) {
                var link = "";
                var label = this;
                
                // check if already set
                if (me.$(label).attr("webkitSpeechRecognitionAdded")) {
                    console.error("addSpeechRecognitionToElements: SKIP because already added: " + me.$(label).attr("for"));
                    return link;
                }
    
                // get corresponding form
                var forName = me.$(label).attr("for");
                var form = me.$(label).closest("form");
                
                // get for-element byName from form
                var forElement = form.find("[name="+ forName + "]").first();
                if (forElement.length > 0) {
                    // define link to label
                    link = "<a href=\"\" class=\"\"" +
                        " onClick=\"yaioAppBase.get('YaioLayout').openSpeechRecognitionWindow(" +
                            "document.getElementById('" + forElement.attr('id') + "')); return false;" +
                        "\" lang='tech' data-tooltip='tooltip.command.OpenSpeechRecognition'>" +
                        "<img alt='Spracherkennung nutzen' style='width:25px'" +
                            " src='" + me.appBase.getService('SpeechRecognitionHelper').config.statusImgSrcStart + "'></a>";
                    
                    // set flag
                    me.$(label).attr("webkitSpeechRecognitionAdded", "true");
                    console.log("addSpeechRecognitionToElements: add : " + forName + " for " + forElement.attr('id'));
                }
                return link;
            });
        }
    };
    
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
    me.openSpeechRecognitionWindow = function(target) {
        if (target == null) { target = self; }
        target.focus();
        var speechrecognitionWindow = window.open('speechrecognition.html', "speechrecognition", "width=690,height=350,resizable=yes,dependent=yes,scrollbars=yes");
        speechrecognitionWindow.focus();
        if (speechrecognitionWindow.opener == null) { speechrecognitionWindow.opener = self; }
        speechrecognitionWindow.opener.targetElement = target;
    };
    
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
    me.addSpeechSynthToElements = function() {
        // add speechSynth if availiable
        if (me.appBase.getDetector('SpeechSynthDetector').isSupported()) {
            // add speechrecognition to nodeDesc+name
            me.$("label[for='nodeDesc']").append(function (idx) {
                var link = "";
                var label = this;
                
                // check if already set
                if (me.$(label).attr("speechSynthAdded")) {
                    console.error("addSpeechSynthToElements: SKIP because already added: " + me.$(label).attr("for"));
                    return link;
                }
    
                // get corresponding form
                var forName = me.$(label).attr("for");
                var form = me.$(label).closest("form");
                
                // get for-element byName from form
                var forElement = form.find("[name="+ forName + "]").first();
                if (forElement.length > 0) {
                    // define link to label
                    link = "<a href=\"\" class=\"button\"" +
                           " onClick=\"yaioAppBase.get('YaioLayout').openSpeechSynthWindow(" +
                            "document.getElementById('" + forElement.attr('id') + "')); return false;" +
                           "\" lang='tech' data-tooltip='tooltip.command.OpenSpeechSynth' class='button'>common.command.OpenSpeechSynth</a>";
                    
                    // set flag
                    me.$(label).attr("speechSynthAdded", "true");
                    console.log("addSpeechSynthToElements: add : " + forName + " for " + forElement.attr('id'));
                }
                return link;
            });
        }
    };
    
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
    me.openSpeechSynthWindow = function(target) {
        if (target == null) { target = self; }
        target.focus();
        var speechsynthWindow = window.open('speechsynth.html', "speechsynth", "width=690,height=350,resizable=yes,dependent=yes,scrollbars=yes");
        speechsynthWindow.focus();
        if (speechsynthWindow.opener == null) { speechsynthWindow.opener = self; }
        speechsynthWindow.opener.targetElement = target;
    };
    
    
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
    me.addDatePickerToElements = function() {
        // add datepicker to all dateinput
        me.$.datepicker.setDefaults(me.$.datepicker.regional['de']);
        me.$.timepicker.regional['de'] = {
                timeOnlyTitle: 'Uhrzeit auswählen',
                timeText: 'Zeit',
                hourText: 'Stunde',
                minuteText: 'Minute',
                secondText: 'Sekunde',
                currentText: 'Jetzt',
                closeText: 'Auswählen',
                ampm: false
              };
        me.$.timepicker.setDefaults(me.$.timepicker.regional['de']);    
        me.$('input.inputtype_date').datepicker();
        me.$('input.inputtype_datetime').datetimepicker();
    };
    
    
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
    me.addDocLayoutStyleSelectorToElements = function() {
        // iterate over docLayoutSDtyleClass-elements
        me.$("input.inputtype_docLayoutAddStyleClass").each(function () {
            // add select only if id id set
            var ele = this;
            var id = me.$(ele).attr("id");
            if (id) {
                // add select
                var $select = me.$("<select id='" + id + "_select' lang='tech' />");
                
                // append values
                $select.append(me.$("<option value=''>Standardstyle</option>"));
                $select.append(me.$("<option value='row-label-value'>row-label-value</option>"));
                $select.append(me.$("<option value='row-label-value'>row-label-value</option>"));
                $select.append(me.$("<option value='row-boldlabel-value'>row-boldlabel-value</option>"));
                $select.append(me.$("<option value='row-value-only-full'>row-value-only-full</option>"));
                $select.append(me.$("<option value='row-label-only-full'>row-label-only-full</option>"));
                
                // add changehandler
                $select.change(function() {
                    // set new value
                    var style = me.$(this).val();
                    me.$(ele).val(style);
                    
                    // call updatetrigger
                    window.callUpdateTriggerForElement(ele);
                });
                
                // insert select after input
                me.$(ele).after($select);
            }
            
        });
    };
    
    me.addPreviewToElements = function() {
        // add preview to nodeDesc
        me.$("label[for='nodeDesc']").append(function (idx) {
            var link = "";
            var label = this;
            
            // check if already set
            if (me.$(label).attr("previewAdded")) {
                console.error("addPreviewElements: SKIP because already added: " + me.$(label).attr("for"));
                return link;
            }
   
            // get corresponding form
            var forName = me.$(label).attr("for");
            var form = me.$(label).closest("form");
            
            // get for-element byName from form
            var forElement = form.find("[name="+ forName + "]").first();
            if (forElement.length > 0) {
                // define link to label
                link = "<a href=\"#\" id='showPreview4" + forElement.attr('id') + "'" +
                       " onClick=\"yaioAppBase.get('YaioMarkdownEditor').showPreviewForTextareaId('" +
                          forElement.attr('id') + "'); return false;" +
                       "\" lang='tech' data-tooltip='tooltip.command.OpenPreview' class='button'>common.command.OpenPreview</a>";
                link += "<a href=\"#\" id='openMarkdownHelp4" + forElement.attr('id') + "'" +
                        " onClick=\"yaioAppBase.get('YaioMarkdownEditor').showMarkdownHelp(); return false;" +
                        "\" lang='tech' data-tooltip='tooltip.command.OpenMarkdownHelp' class='button'>common.command.OpenMarkdownHelp</a>";
                
                // set flag
                me.$(label).attr("previewAdded", "true");
                console.log("addPreviewToElements: add : " + forName + " for " + forElement.attr('id'));
            }
            return link;
        });
    };
    
     
     
    me.addWysiwhgToElements = function() {
        // add preview to nodeDesc
        me.$("label[for='nodeDesc']").append(function (idx) {
            var link = "";
            var label = this;
            
            // check if already set
            if (me.$(label).attr("wysiwhgAdded")) {
                console.error("addWysiwhgElements: SKIP because already added: " + me.$(label).attr("for"));
                return link;
            }
   
            // get corresponding form
            var forName = me.$(label).attr("for");
            var form = me.$(label).closest("form");
            
            // get for-element byName from form
            var forElement = form.find("[name="+ forName + "]").first();
            if (forElement.length > 0) {
                // define link to label
                link = "<a href=\"#\" id='openWysiwyg4" + forElement.attr('id') + "'" +
                    " onClick=\"yaioAppBase.get('YaioMarkdownEditor').openWysiwhgForTextareaId('" +
                        forElement.attr('id') + "'); return false;" +
                    "\" lang='tech' data-tooltip='tooltip.command.OpenWysiwygEditor' class='button'>common.command.OpenWysiwygEditor</a>";
                
                // set flag
                me.$(label).attr("wysiwhgAdded", "true");
                console.log("addWysiwhgToElements: add : " + forName + " for " + forElement.attr('id'));
            }
            return link;
        });
    };
    
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
    me.initLanguageSupport = function(langKey) {
        // Create language switcher instance and set default language to tech
        window.lang = new Lang('tech');
    
        //Define the de language pack as a dynamic pack to be loaded on demand
        //if the user asks to change to that language. We pass the two-letter language
        //code and the path to the language pack js file
        window.lang.dynamic('de', me.appBase.config.resBaseUrl + 'lang/lang-tech-to-de.json');
        window.lang.dynamic('en', me.appBase.config.resBaseUrl + 'lang/lang-tech-to-en.json');
        window.lang.loadPack('de');
        window.lang.loadPack('en');
    
        // change to de
        window.lang.change(langKey);
    };
    
    me.setupAppSize = function() {
        var height = window.innerHeight;
        var width = window.innerWidth;
        
        // YAIO-editor
        var ele = me.$("#containerBoxYaioEditor");
        if (ele.length > 0) {
            // we are relative to the tree
            var paddingToHead = me.$("#containerYaioTree").position().top;
            var left = me.$("#containerYaioTree").position().left + me.$("#containerYaioTree").width + 2;
    
            // set posTop as scrollTop burt never < paddingToHead
            var posTop = me.$(window).scrollTop();
            if (posTop < paddingToHead) {
                posTop = paddingToHead;
            }
            
            // calc maxHeight = windHeight - 20 (puffer)
            var maxHeight = height - 20;
            // sub topPos - Scollpos
            maxHeight = maxHeight - (posTop - me.$(window).scrollTop());
    
            // set values
            me.$(ele).css("position", "absolute");
            me.$(ele).css("max-height", maxHeight);
            me.$(ele).css("top", posTop);
            me.$(ele).css("left", left);
            
            console.log("setup size containerBoxYaioEditor width:" + window.innerWidth 
                    + " height:" + window.innerHeight 
                    + " scrollTop:" + me.$(window).scrollTop()
                    + " offset.top" + me.$(ele).offset().top
                    + " top:" + posTop
                    + " max-height:" + me.$(ele).css("max-height")
                    );
        }
        
        // Export-editor
        ele = me.$("#containerFormYaioEditorOutputOptions");
        if (ele.length > 0) {
            me.$(ele).css("max-height", height-me.$(ele).offset().top);
            console.log("setup size containerFormYaioEditorOutputOptions width:" + window.innerWidth 
                    + " height:" + window.innerHeight 
                    + " scrollTop:" + me.$(window).scrollTop()
                    + " offset.top" + me.$(ele).offset().top
                    + " max-height:" + me.$(ele).css("max-height")
                    );
        }
        // Import-editor
        ele = me.$("#containerFormYaioEditorImport");
        if (ele.length > 0) {
            me.$(ele).css("max-height", height-me.$(ele).offset().top);
            console.log("setup size containerFormYaioEditorImport width:" + window.innerWidth 
                    + " height:" + window.innerHeight 
                    + " scrollTop:" + me.$(window).scrollTop()
                    + " offset.top" + me.$(ele).offset().top
                    + " max-height:" + me.$(ele).css("max-height")
                    );
        }
    
        // Frontpage
        ele = me.$("#front-content-intro");
        if (0 && ele.length > 0) {
            var maxHeight = height-me.$(ele).offset().top;
            
            // sub todonextbox
            if (me.$('#box_todonext').length > 0 ) {
                if (me.$('#box_todonext').height > 0) {
                    maxHeight = maxHeight - me.$('#box_todonext').height;
                } else {
                    // sometime height is not set: then default
                    maxHeight = maxHeight - 100;
                }
            }
            me.$(ele).css("max-height", maxHeight);
            console.log("setup size front-content-intro width:" + window.innerWidth 
                    + " height:" + window.innerHeight 
                    + " scrollTop:" + me.$(window).scrollTop()
                    + " offset.top" + me.$(ele).offset().top
                    + " max-height:" + me.$(ele).css("max-height")
                    );
        }
    };
    
    me.yaioShowHelpSite = function(url) {
        // set messagetext
        url += "?" + me.appBase.get('YaioBase').createXFrameAllowFrom();
        console.log("yaioShowHelpSite:" + url);
        me.$("#help-iframe").attr('src',url);
        
        // show message
        me.$( "#help-box" ).dialog({
            modal: true,
            width: "800px",
            buttons: {
              "Schliessen": function() {
                me.$( this ).dialog( "close" );
              },
              "Eigenes Fenster": function() {
                  var helpFenster = window.open(url, "help", "width=750,height=500,scrollbars=yes,resizable=yes");
                  helpFenster.focus();
                  me.$( this ).dialog( "close" );
                }
            }
        });    
    };
    
    me.hideFormRowTogglerIfSet = function(togglerId, className, state) {
        if (jMATService.getLayoutService().isInputRowsSet(className)) {
            // show all
            jMATService.getPageLayoutService().toggleFormrows(togglerId, className, true);
            
            // hide toggler
            me.$("#" + togglerId + "_On").css('display', 'none');
            me.$("#" + togglerId + "_Off").css('display', 'none');
        } else {
            // show or hide ??
            me.$("#" + togglerId + "_On").css('display', 'none');
            me.$("#" + togglerId + "_Off").css('display', 'block');
            jMATService.getPageLayoutService().toggleFormrows(togglerId, className, state);
        }
    };
     
    me.createTogglerIfNotExists = function(parentId, toggleId, className) {
        var $ele = me.$("#" + toggleId + "_On");
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
    };
     
    me.togglePrintLayout = function() {
        if (me.$("#checkboxPrintAll").prop('checked')) {
            // print all
            me.$("#link_css_dataonly").attr("disabled", "disabled");
            me.$("#link_css_dataonly").prop("disabled", true);
        } else  {
            // print data only
            me.$("#link_css_dataonly").removeAttr("disabled");
            me.$("#link_css_dataonly").prop("disabled", false);
        }
    };

    me._init();
    
    return me;
};
 
