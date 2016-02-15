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
 * servicefunctions for layout
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

Yaio.Layout = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
        me.jMATService = jMATService;
    };


    me.addPreviewToElements = function() {
        me.appBase.YmfMarkdownEditorServiceHelper.addPreviewToElements('label[for="nodeDesc"');
    };
    
    me.addWysiwygToElements = function() {
        me.appBase.YmfMarkdownEditorServiceHelper.addWysiwygToElements('label[for="nodeDesc"');
    };
    
    /** 
     * add speechRecognition to name+nodeDesc-Label if availiable<br>
     * set the flg webkitSpeechRecognitionAdded on the element, so that there is no doubling
     */
    me.addSpeechRecognitionToElements = function() {
        me.appBase.SpeechServiceHelper.addSpeechRecognitionToElements('label[for="nodeDesc"], label[for="name"]');
    };
    
    /** 
     * add speechSynth to nodeDesc-Label if availiable<br>
     * set the flg speechSynthAdded on the element, so that there is no doubling
     */
    me.addSpeechSynthToElements = function() {
        me.appBase.SpeechServiceHelper.addSpeechSynthToElements('label[for="nodeDesc"]');
    };
    
    /**
     * toggle printlayout depending on #checkboxPrintAll is checked or not
     */
    me.togglePrintLayout = function () {
        if (me.$('#checkboxPrintAll').prop('checked')) {
            // print all
            me.$('#link_css_dataonly').attr('disabled', 'disabled');
            me.$('#link_css_dataonly').prop('disabled', true);
        } else {
            // print data only
            me.$('#link_css_dataonly').removeAttr('disabled');
            me.$('#link_css_dataonly').prop('disabled', false);
        }
    };

    /**
     * add datepicker to all input-elements with styleclass inputtype_date and inputtype_datetime
     */
    me.addDatePickerToElements = function () {
        // add datepicker to all dateinput
        me.$.datepicker.setDefaults(me.$.datepicker.regional.de);
        me.$.timepicker.regional.de = {
            timeOnlyTitle: 'Uhrzeit auswählen',
            timeText: 'Zeit',
            hourText: 'Stunde',
            minuteText: 'Minute',
            secondText: 'Sekunde',
            currentText: 'Jetzt',
            closeText: 'Auswählen',
            ampm: false
        };
        me.$.timepicker.setDefaults(me.$.timepicker.regional.de);
        me.$('input.inputtype_date').datepicker();
        me.$('input.inputtype_datetime').datetimepicker();
    };

    /** 
     * add styleselectbox to all input-elements with styleclass inputtype_docLayoutAddStyleClass
     */
    me.addDocLayoutStyleSelectorToElements = function() {
        // iterate over docLayoutSDtyleClass-elements
        me.$('input.inputtype_docLayoutAddStyleClass').each(function () {
            // add select only if id id set
            var ele = this;
            var id = me.$(ele).attr('id');
            if (id) {
                // add select
                var $select = me.$('<select id="' + id + '_select" lang="tech" />');
                
                // append values
                $select.append(me.$('<option value="">Standardstyle</option>'));
                $select.append(me.$('<option value="row-label-value">row-label-value</option>'));
                $select.append(me.$('<option value="row-label-value">row-label-value</option>'));
                $select.append(me.$('<option value="row-boldlabel-value">row-boldlabel-value</option>'));
                $select.append(me.$('<option value="row-value-only-full">row-value-only-full</option>'));
                $select.append(me.$('<option value="row-label-only-full">row-label-only-full</option>'));
                
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
    
    /** 
     * init the multilanguage support for all tags with attribute <XX lang='de'>
     * @param {string} langKey                key of the preferred-language
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

    me.setupAppSize = function () {
        var height = window.innerHeight;

        // YAIO-editor
        var ele = me.$('#containerBoxYaioEditor');
        if (ele.length > 0) {
            // we are relative to the tree
            var paddingToHead = me.$('#containerYaioTree').position().top;
            var left = me.$('#containerYaioTree').position().left + me.$('#containerYaioTree').width + 2;

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
            me.$(ele).css('position', 'absolute');
            me.$(ele).css('max-height', maxHeight);
            me.$(ele).css('top', posTop);
            me.$(ele).css('left', left);

            console.log('setup size containerBoxYaioEditor width:' + window.innerWidth
                + ' height:' + window.innerHeight
                + ' scrollTop:' + me.$(window).scrollTop()
                + ' offset.top' + me.$(ele).offset().top
                + ' top:' + posTop
                + ' max-height:' + me.$(ele).css('max-height')
            );
        }

        // Export-editor
        ele = me.$('#containerFormYaioEditorOutputOptions');
        if (ele.length > 0) {
            me.$(ele).css('max-height', height - me.$(ele).offset().top);
            console.log('setup size containerFormYaioEditorOutputOptions width:' + window.innerWidth
                + ' height:' + window.innerHeight
                + ' scrollTop:' + me.$(window).scrollTop()
                + ' offset.top' + me.$(ele).offset().top
                + ' max-height:' + me.$(ele).css('max-height')
            );
        }
        // Import-editor
        ele = me.$('#containerFormYaioEditorImport');
        if (ele.length > 0) {
            me.$(ele).css('max-height', height - me.$(ele).offset().top);
            console.log('setup size containerFormYaioEditorImport width:' + window.innerWidth
                + ' height:' + window.innerHeight
                + ' scrollTop:' + me.$(window).scrollTop()
                + ' offset.top' + me.$(ele).offset().top
                + ' max-height:' + me.$(ele).css('max-height')
            );
        }

        // Frontpage
        ele = me.$('#front-content-intro');
        if (0 && ele.length > 0) {
            var maxHeight = height - me.$(ele).offset().top;

            // sub todonextbox
            if (me.$('#box_todonext').length > 0) {
                if (me.$('#box_todonext').height > 0) {
                    maxHeight = maxHeight - me.$('#box_todonext').height;
                } else {
                    // sometime height is not set: then default
                    maxHeight = maxHeight - 100;
                }
            }
            me.$(ele).css('max-height', maxHeight);
            console.log('setup size front-content-intro width:' + window.innerWidth
                + ' height:' + window.innerHeight
                + ' scrollTop:' + me.$(window).scrollTop()
                + ' offset.top' + me.$(ele).offset().top
                + ' max-height:' + me.$(ele).css('max-height')
            );
        }
    };

    me.yaioShowHelpSite = function (url) {
        // set messagetext
        console.log('yaioShowHelpSite:' + url);
        me.$('#help-iframe').attr('src', url);

        // show message
        me.$('#help-box').dialog({
            modal: true,
            width: '800px',
            buttons: {
                'Schliessen': function () {
                    me.$(this).dialog('close');
                },
                'Eigenes Fenster': function () {
                    var helpFenster = window.open(url, 'help', 'width=750,height=500,scrollbars=yes,resizable=yes');
                    helpFenster.focus();
                    me.$(this).dialog('close');
                }
            }
        });
    };

    me.hideFormRowTogglerIfSet = function(togglerId, className, state) {
        if (jMATService.getLayoutService().isInputRowsSet(className)) {
            // show all
            jMATService.getPageLayoutService().toggleFormrows(togglerId, className, true);
            
            // hide toggler
            me.$('#' + togglerId + '_On').css('display', 'none');
            me.$('#' + togglerId + '_Off').css('display', 'none');
        } else {
            // show or hide ??
            me.$('#' + togglerId + '_On').css('display', 'none');
            me.$('#' + togglerId + '_Off').css('display', 'block');
            jMATService.getPageLayoutService().toggleFormrows(togglerId, className, state);
        }
    };
     
    me.createTogglerIfNotExists = function(parentId, toggleId, className) {
        var $ele = me.$('#' + toggleId + '_On');
        if ($ele.length <= 0) {
            // create toggler
            console.log('createTogglerIfNotExists link not exists: create new toggler parent=' + parentId 
                    + ' toggleEleId=' + toggleId
                    + ' className=' + className);
            jMATService.getPageLayoutService().appendFormrowToggler(parentId, toggleId, className, '&nbsp;');
        } else {
            console.log('createTogglerIfNotExists link exists: skip new toggler parent=' + parentId 
                    + ' toggleEleId=' + toggleId
                    + ' className=' + className);
        }
    };
     

    me._init();
    
    return me;
};
 
