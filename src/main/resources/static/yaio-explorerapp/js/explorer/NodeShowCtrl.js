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
 *     the controller to load the nodes for url-params and register the yaio-functions
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration BusinessLogic
 */
yaioM.controller('NodeShowCtrl', function($rootScope, $scope, $location, $http, $routeParams, setFormErrors, OutputOptionsEditor, authorization, yaioUtils) {
    // include utils    
    $scope.yaioUtils = yaioUtils;

    // register the editor
    $scope.outputOptionsEditor = OutputOptionsEditor;

    // check parameter - set default if empty
    var baseUrl = '/show/';
    var restBaseUrl = '/nodes/show/';
    var nodeId = $routeParams.nodeId;
    var nodeByAllId = $routeParams.nodeByAllId;
    if (nodeByAllId != null && nodeByAllId != "" && nodeByAllId) {
        nodeId = nodeByAllId;
        baseUrl = '/showByAllIds/';
        restBaseUrl = '/nodes/showsymlink/';
    }
    if (nodeId == null || nodeId == "" || ! nodeId) {
        nodeId = "MasterplanMasternode1";
    }

    // create node
    $scope.node = {};
    $scope.config = {treeOpenLevel: 1};
    
    // check activeNodeId
    var activeNodeIdHandler;
    var activeNodeId = $routeParams.activeNodeId;

    console.log("NodeShowCtrl - processing nodeId=" + nodeId + " activeNodeId=" + activeNodeId);

    var curNodeUrl = restBaseUrl + nodeId;

    // save lastLocation for login
    $rootScope.lastLocation = baseUrl + nodeId;

    // save nodeId for NodeEditor
    $rootScope.nodeId = nodeId;
    
    // call authentificate 
    authorization.authentificate(function () {
        // check authentification
        if (! $rootScope.authenticated) {
            console.log("showControl: not authentification: " + $rootScope.authenticated);
            $location.path("/login");
            $scope.error = false;
        } else {
            // do Search
            if (activeNodeId) {
                /**
                 * <h4>FeatureDomain:</h4>
                 *     GUI
                 * <h4>FeatureDescription:</h4>
                 *     callbackhandler to load activeNodeId and open the nodehierarchy
                 * <h4>FeatureResult:</h4>
                 *   <ul>
                 *     <li>updates nodetree
                 *   </ul> 
                 * <h4>FeatureKeywords:</h4>
                 *     GUI Callback
                 */
                activeNodeIdHandler = function() {
                    var activeNodeUrl = '/nodes/show/' + activeNodeId;
                    console.log("start loading activenode:" + activeNodeId);
                    $http.get(activeNodeUrl).then(function(nodeResponse) {
                        // success handler
                        
                        // check response
                        var state = nodeResponse.data.state;
                        if (state == "OK") {
                            // all fine
                            console.log("NodeShowCtrl - OK loading activenode:" + nodeResponse.data.stateMsg);
                            
                            // create nodehierarchy
                            var nodeIdHierarchy = new Array();
                            var parentNode = nodeResponse.data.node.parentNode;
                            while (parentNode != null && parentNode != "" && parentNode != "undefined") {
                                nodeIdHierarchy.push(parentNode.sysUID);
                                parentNode = parentNode.parentNode;
                            }
                            nodeIdHierarchy.reverse();
                            
                            // add me 
                            nodeIdHierarchy.push(nodeResponse.data.node.sysUID);
                            
                            // open Hierarchy
                            yaioAppBase.get('YaioExplorerTreeService').openNodeHierarchy("#tree", nodeIdHierarchy);
                        } else {
                            // error
                            yaioAppBase.get('YaioBaseService').logError("error loading activenode:" + nodeResponse.data.stateMsg 
                                    + " details:" + nodeResponse, false)
                        }
                    }, function(response) {
                        // error handler
                        var data = response.data;
                        var header = response.header;
                        var config = response.config;
                        var message = "error loading activenode with url: " + activeNodeUrl;
                        yaioAppBase.get('YaioBaseService').logError(message, true);
                        message = "error data: " + data + " header:" + header + " config:" + config;
                        yaioAppBase.get('YaioBaseService').logError(message, false);
                    });
                }
            }

            // load data
            $http.get(curNodeUrl).then(function(nodeResponse) {
                // success handler
                
                // check response
                var state = nodeResponse.data.state;
                if (state == "OK") {
                    // all fine
                    console.log("NodeShowCtrl - OK loading nodes:" + nodeResponse.data.stateMsg);
                    $scope.node = nodeResponse.data.node;
                    
                    // create nodehierarchy
                    var nodeHierarchy = new Array();
                    var parentNode = nodeResponse.data.node.parentNode;
                    while (parentNode != null && parentNode != "" && parentNode != "undefined") {
                        nodeHierarchy.push(parentNode);
                        parentNode = parentNode.parentNode;
                    }
                    nodeHierarchy.reverse();
                    $scope.nodeHierarchy = nodeHierarchy;

                    // load fencytree
                    yaioAppBase.get('YaioExplorerTreeService').yaioCreateFancyTree("#tree", $scope.node.sysUID, activeNodeIdHandler);
                    
                    // load me
                    $scope.yaioUtils.renderNodeLine(nodeResponse.data.node, "#masterTr");

                    // recalc gantt
                    yaioAppBase.get('YaioExplorerTreeService').yaioRecalcMasterGanttBlock($scope.node);
                } else {
                    // error
                    yaioAppBase.get('YaioBaseService').logError("error loading nodes:" + nodeResponse.data.stateMsg 
                            + " details:" + nodeResponse, true)
                }
            }, function(response) {
                // error handler
                var data = response.data;
                var header = response.header;
                var config = response.config;
                var message = "error loading node with url: " + curNodeUrl;
                yaioAppBase.get('YaioBaseService').logError(message, true);
                message = "error data: " + data + " header:" + header + " config:" + config;
                yaioAppBase.get('YaioBaseService').logError(message, false);
            });
        }
    });
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Editor
     * <h4>FeatureDescription:</h4>
     *     export GUI As Overview
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>exportAsOverview
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.exportAsOverview = function() {
        console.log("exportAsOverview");
        yaioAppBase.get('YaioExplorerTreeService').yaioExportExplorerLinesAsOverview();
        return false;
    };

    /**
     * <h4>FeatureDomain:</h4>
     *     Editor
     * <h4>FeatureDescription:</h4>
     *     create snapshot of GUI
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>snapshot
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.snapshot = function() {
        console.log("snapshot");
        yaioAppBase.get('YaioExplorerTreeService').yaioSnapshot($scope.node);
        return false;
    };

    /**
     * <h4>FeatureDomain:</h4>
     *     Editor
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to open all subnodes<level in the treeview
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>open the subNodes till treeOpenLevel
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.openSubNodes = function() {
        console.log("openSubNodes:" + " level:" + $scope.config.treeOpenLevel);
        yaioAppBase.get('YaioExplorerTreeService').yaioOpenSubNodesForTree("#tree", $scope.config.treeOpenLevel);
        return false;
    };

    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to recalc ganttblocks for nodes
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>recalc ganttblocks
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.recalcGanttBlocks = function() {
        yaioAppBase.get('YaioExplorerTreeService').yaioRecalcFancytreeGanttBlocks();
        yaioAppBase.get('YaioExplorerTreeService').yaioRecalcMasterGanttBlock($scope.node);
        return false;
    };

    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to recalc Gantt with Master-Ist/Plan-DateRange
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>recalc ganttblocks
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     * @param  flgShowIst   boolean if is set show ist, if not show plan
     */
    $scope.recalcGanttForIstOrPlan = function(flgShowIst) {
        var start = flgShowIst ? $scope.node.istChildrenSumStart : $scope.node.planChildrenSumStart;
        var ende = flgShowIst ? $scope.node.istChildrenSumEnde : $scope.node.planChildrenSumEnde;
        $("#inputGanttRangeStart").val(yaioAppBase.get('YaioBaseService').formatGermanDate(start)).trigger('input').triggerHandler("change");
        $("#inputGanttRangeEnde").val(yaioAppBase.get('YaioBaseService').formatGermanDate(ende)).trigger('input').triggerHandler("change");
        return false;
    }
});
