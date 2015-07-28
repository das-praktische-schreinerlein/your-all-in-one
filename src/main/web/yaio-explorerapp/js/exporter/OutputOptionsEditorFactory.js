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
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     new functions to control the outputoptions
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new function
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
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
    oOptions.strClassFilter = "";
    oOptions.strTypeFilter = "";
    oOptions.strReadIfStatusInListOnly = "";
    
    // define the functions
    return {
        oOptions: oOptions,
        
        /**
         * <h4>FeatureDomain:</h4>
         *     Download
         * <h4>FeatureDescription:</h4>
         *     callbackhandler to discard and close the editor
         * <h4>FeatureResult:</h4>
         *   <ul>
         *     <li>updates layout
         *   </ul> 
         * <h4>FeatureKeywords:</h4>
         *     GUI Callback
         */
        discard: function() {
            yaioUtils.getService('UIToggler').toggleElement("#containerFormYaioEditorOutputOptions");
            console.log("discard done");
            return false;
        },
    
        /**
         * <h4>FeatureDomain:</h4>
         *     Download
         * <h4>FeatureDescription:</h4>
         *     callbackhandler to send and close the editor
         * <h4>FeatureResult:</h4>
         *   <ul>
         *     <li>send request and updates layout
         *   </ul> 
         * <h4>FeatureKeywords:</h4>
         *     GUI Callback
         */
        send: function() {
//            // branch depending on mode
//            var method;
//            method = "POST";
//
//            // define json for common fields
//            var json = JSON.stringify(oOptions);
//            
//            // create url
//            console.log("Send - url::" + url + " data:" + json);
//            
//            // do http
//            $http({
//                    method: method,
//                    url: url,
//                    data: json
//            }).then(function(response) {
//                // sucess handler
//                yaioUtils.getService('UIToggler').toggleElement("#containerFormYaioEditorOutputOptions");
//                
//                yaioUtils.getService('YaioBase').downloadAsFile(null, response.data, "test.xxx", "dummy", "dummy");
//                
//                console.log("send done");
//            }, function(response) {
//                // error handler
//                var data = response.data;
//                var status = response.status;
//                var header = response.header;
//                var config = response.config;
//                var message = "error while do export with url: " + url;
//                yaioUtils.getService('YaioBase').logError(message, true);
//                message = "error data: " + data + " header:" + header + " config:" + config;
//                yaioUtils.getService('YaioBase').logError(message, false);
//            });

            var formId = "#nodeFormOutputOptions";
            $(formId).submit();
            yaioUtils.getService('UIToggler').toggleElement("#containerFormYaioEditorOutputOptions");
            console.log("send done");
            return false;
        },
        
        /**
         * <h4>FeatureDomain:</h4>
         *     GUI Download
         * <h4>FeatureDescription:</h4>
         *     callbackhandler to open the outputOptionsEditor
         * <h4>FeatureResult:</h4>
         *   <ul>
         *     <li>open the outputOptionsEditor
         *   </ul> 
         * <h4>FeatureKeywords:</h4>
         *     GUI Callback
         * @param sysUID - the sysUID of the current node
         * @param newUrl - the url to send
         * @param newTarget - the target window-name
         */
        showOutputOptionsEditor: function(sysUID, newUrl, newTarget) {
            url = newUrl;
            target = newTarget;
            
            var formId = "#nodeFormOutputOptions";
            console.log("OutputOptionsEditor:" + " url:" + url);
            $("#containerFormYaioEditorOutputOptions").css("display", "none");
            yaioUtils.getService('UIToggler').toggleElement("#containerFormYaioEditorOutputOptions");
            $(formId).attr("target", target);
            $(formId).attr("action", url);
            $(formId).trigger('form').triggerHandler("change");
            $(formId).trigger('input');
            
            // update appsize
            yaioUtils.getService('YaioLayout').setupAppSize();

            console.log("showOutputOptionsEditor done:" + " url:" + url);
            return false;
        }
    };
});
