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
 * new functions to control the outputoptions
 * @FeatureDomain                Configuration
 * @FeatureResult                returns new function
 * @FeatureKeywords              GUI Configuration
 */
yaioApp.factory('OutputOptionsEditor', function(yaioUtils) {
    'use strict';

    var oOptions =  {};
    var url, target;

    // default-values
    oOptions.flgDoIntend = true;
    oOptions.flgShowBrackets = true;
    oOptions.intendFuncArea = 80;
    oOptions.flgIntendSum = false;

    oOptions.maxEbene = 9999;
    oOptions.maxUeEbene = 3;
    oOptions.intend = 2;
    oOptions.intendLi = 2;
    oOptions.intendSys = 160;
    oOptions.flgTrimDesc = true;
    oOptions.flgReEscapeDesc = true;

    oOptions.flgShowState = true;
    oOptions.flgShowType = true;
    oOptions.flgShowName = true;
    oOptions.flgShowResLoc = true;
    oOptions.flgShowSymLink = true;
    oOptions.flgShowDocLayout = true;
    oOptions.flgShowIst = true;
    oOptions.flgShowPlan = true;
    oOptions.flgShowChildrenSum = false;
    oOptions.flgShowMetaData = true;
    oOptions.flgShowSysData = true;
    oOptions.flgShowDesc = true;
    oOptions.flgShowDescWithUe = false;
    oOptions.flgShowDescInNextLine = false;

    oOptions.flgChildrenSum = false;
    oOptions.flgProcessDocLayout = false;
    oOptions.flgUsePublicBaseRef = false;
    oOptions.flgRecalc= false;
    oOptions.strClassFilter = '';
    oOptions.strTypeFilter = '';
    oOptions.strReadIfStatusInListOnly = '';
    
    // define the functions
    return {
        oOptions: oOptions,
        
        /** 
         * discard and close the editor
         */
        discard: function() {
            yaioUtils.getService('UIToggler').toggleElement('#containerFormYaioEditorOutputOptions');
            console.log('discard done');
            return false;
        },
    
        /** 
         * submit the form and close the editor
         */
        send: function() {
            var formId = '#nodeFormOutputOptions';
            $(formId).submit();
            yaioUtils.getService('UIToggler').toggleElement('#containerFormYaioEditorOutputOptions');
            console.log('send done');
            return false;
        },
        
        /** 
         * open the outputOptionsEditor
         * @param {String} sysUID                 the sysUID of the current node
         * @param {String} newUrl                 the url submit the form to
         * @param {String} newTarget              the target window-name
         */
        showOutputOptionsEditor: function(sysUID, newUrl, newTarget) {
            url = newUrl;
            target = newTarget;
            
            var formId = '#nodeFormOutputOptions';
            console.log('OutputOptionsEditor:' + ' url:' + url);
            $('#containerFormYaioEditorOutputOptions').css('display', 'none');
            yaioUtils.getService('UIToggler').toggleElement('#containerFormYaioEditorOutputOptions');
            $(formId).attr('target', target);
            $(formId).attr('action', url);
            $(formId).trigger('form').triggerHandler('change');
            $(formId).trigger('input');
            
            // update appsize
            yaioUtils.getService('YaioLayout').setupAppSize();

            console.log('showOutputOptionsEditor done:' + ' url:' + url);
            return false;
        }
    };
});
