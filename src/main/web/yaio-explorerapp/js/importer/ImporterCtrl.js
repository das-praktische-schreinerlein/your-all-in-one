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
 * the controller for Imports
 * @FeatureDomain                Configuration
 * @FeatureResult                returns new controller
 * @FeatureKeywords              GUI Configuration BusinessLogic
 */
yaioApp.controller('ImporterCtrl', function($rootScope, $scope, $location, $routeParams,
                                            setFormErrors, authorization, yaioUtils) {
    'use strict';

    // include utils
    $scope.yaioUtils = yaioUtils;

    /** 
     * callbackhandler to send and close the importeditor
     * @FeatureDomain                Download
     * @FeatureResult                send request and updates layout
     * @FeatureKeywords              GUI Callback
     */
    $scope.sendImport = function() {
        var formId = '#nodeFormImport';
        $(formId).submit();
        yaioUtils.getService('UIToggler').toggleElement('#containerFormYaioEditorImport');

        console.log('send done');
        return false;
    };
    
    /** 
     * callbackhandler to discard and close the editor
     * @FeatureDomain                Download
     * @FeatureResult                updates layout
     * @FeatureKeywords              GUI Callback
     */
    $scope.discardImport = function() {
        yaioUtils.getService('UIToggler').toggleElement('#containerFormYaioEditorImport');
        console.log('discard done');
        return false;
    };

    /** 
     * callbackhandler to open the importEditor
     * @FeatureDomain                GUI Download
     * @FeatureResult                open the importEditor
     * @FeatureKeywords              GUI Callback
     * @param sysUID                 the sysUID of the current node
     * @param newUrl                 the url to send
     * @param newTarget              the target window-name
     */
    $scope.showImportEditor = function(sysUID, newUrl, newTarget) {
        var url = newUrl;
        var target = newTarget;

        var formId = '#nodeFormImport';
        $(formId).attr('target', target);
        $(formId).attr('action', url);
        $(formId).trigger('form').triggerHandler('change');
        $(formId).trigger('input');
        console.log('ImportEditor:' + ' url:' + url);
        $('#containerFormYaioEditorImport').css('display', 'none');
        yaioUtils.getService('UIToggler').toggleElement('#containerFormYaioEditorImport');
        
        // update appsize
        yaioUtils.getService('YaioLayout').setupAppSize();

        console.log('showImportEditor done:' + ' url:' + url);
        return false;
    };
});
