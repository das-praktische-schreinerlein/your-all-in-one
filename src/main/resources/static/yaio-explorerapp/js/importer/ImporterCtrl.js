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
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     the controller for Imports
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration BusinessLogic
 */
yaioM.controller('ImporterCtrl', function($rootScope, $scope, $location, $http, $routeParams, setFormErrors, authorization, yaioUtils) {
    // include utils    
    $scope.yaioUtils = yaioUtils;

    /**
     * <h4>FeatureDomain:</h4>
     *     Download
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to send and close the importeditor
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>send request and updates layout
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.sendImport = function() {
        yaioSendImportEditor();
        yaioCloseImportEditor();
        console.log("send done");
        return false;
    },
    
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
    $scope.discardImport = function() {
        yaioCloseImportEditor();
        console.log("discard done");
        return false;
    },

    /**
     * <h4>FeatureDomain:</h4>
     *     GUI Download
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to open the importEditor
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>open the importEditor
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     * @param sysUID - the sysUID of the current node
     * @param url - the url to send
     * @param target - the target window-name
     */
    $scope.showImportEditor = function(sysUID, newUrl, newTarget) {
        var url = newUrl;
        var target = newTarget;
        yaioOpenImportEditor(sysUID, url, target);
        console.log("showImportEditor done:" + " url:" + url);
        return false;
    }
});
