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
 *     the controller for dashboard-elements
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration BusinessLogic
 */
yaioApp.controller('DashBoardNodeSearchCtrl', function($rootScope, $scope, yaioUtils) {
    'use strict';

    // include utils
    $scope.yaioUtils = yaioUtils;

    // create search
    $scope.nodes = [];

    $scope.searchOptions = {
        curPage: 1,
        pageSize: 5,
        searchSort: 'lastChangeDown',
        baseSysUID: "MasterplanMasternode1",
        fulltext: "",
        total: 0,
        strNotNodePraefix: "Sys* *Templ MyStart MyHelp JsUnitTest JsFuncTest JUnitTest",
        strWorkflowStateFilter: "",
        strClassFilter: ""
    };

    $scope.createSearchUri = function() {
        var newUrl = '/search'
            + '/' + encodeURI(1)
            + '/' + encodeURI(20)
            + '/' + encodeURI($scope.searchOptions.searchSort)
            + '/' + encodeURI($scope.searchOptions.baseSysUID)
            + '/' + encodeURI($scope.searchOptions.fulltext)
            + '/' + encodeURI($scope.searchOptions.strClassFilter)
            + '/' + encodeURI($scope.searchOptions.strWorkflowStateFilter)
            + '/' + encodeURI($scope.searchOptions.strNotNodePraefix)
            + '/';
        return newUrl;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     send ajax-request for fulltextsearch to server and add reszult to scope<br>
     *     sends broadcast NodeListReady when result is ready
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li> Updates scope.nodes
     *     <li> sends broadcast NodeListReady when result is ready
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback Fulltextsearch
     */
    $scope.doFulltextSearch = function() {
        // search data
        var searchOptions = $scope.searchOptions;
        return yaioUtils.getService('YaioNodeData').yaioDoFulltextSearch(searchOptions)
            .then(function(angularResponse) {
                // success handler
                $scope.doFulltextSearchSuccessHandler(searchOptions, angularResponse.data)
            }, function(angularResponse) {
                // error handler
                var data = angularResponse.data;
                var header = angularResponse.header;
                var config = angularResponse.config;
                var message = "error loading nodes with searchOptions: " + searchOptions;
                yaioUtils.getService('YaioBase').logError(message, true);
                message = "error data: " + data + " header:" + header + " config:" + config;
                yaioUtils.getService('YaioBase').logError(message, false);
            });
    };
    
    $scope.doFulltextSearchSuccessHandler = function(searchOptions, yaioNodeSearchResponse) {
        // check response
        var state = yaioNodeSearchResponse.state;
        if (state === "OK") {
            // all fine
            console.log("NodeSearchCtrl - OK loading nodes:" + yaioNodeSearchResponse.stateMsg + " searchOptions=" + searchOptions);
            
            // add nodes to scope
            $scope.nodes = yaioNodeSearchResponse.nodes;
            
            // set count
            $scope.searchOptions.total = yaioNodeSearchResponse.count;
        } else {
            // error
            yaioUtils.getService('YaioBase').logError("error loading nodes:" + yaioNodeSearchResponse.stateMsg + " details:" + yaioNodeSearchResponse, true);
        }
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to rendernodeLine for node
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>renders nodeline
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.renderNodeLine = function(node) {
        // we need a timeout to put the tr into DOM
        setTimeout(function() {
                var domId = $scope.searchOptions.praefix + node.sysUID;
                $scope.yaioUtils.renderNodeLine(node, "#tr" + domId, true);
                console.log("renderNodeLine: done to:" + "#tr" + domId + $("#detail_sys_" + domId).length);

                // render hierarchy
                var parentNode = node.parentNode;
                var parentStr = node.name;
                while (parentNode != null && parentNode != "" && parentNode != "undefined") {
                    parentStr = parentNode.name + " --> " + parentStr;
                    parentNode = parentNode.parentNode;
                }
                parentStr = "<b>" + yaioUtils.getService('YaioBase').htmlEscapeText(parentStr) + "</b>";
                
                // add searchdata
                console.log("renderNodeLine: add searchdata to:" + "#tr" + domId);
                var $html = $("<div id='details_parent_" + domId + "'"
                                + " class='field_nodeParent'>"
                                + parentStr
                                + "</div>");
                $("#tr" + domId + " #detail_sys_" + node.sysUID).after($html);
                console.log("renderNodeLine: added searchdata to:" + "#detail_sys_" + domId + $("#detail_sys_" + domId).length);
            }, 10);
    };
});
