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
 *     the controller to load the nodes for url-params and register the yaio-functions
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration BusinessLogic
 */
yaioApp.controller('NodeShowCtrl', function($rootScope, $scope, $location, $http, $routeParams, setFormErrors, OutputOptionsEditor, authorization, yaioUtils) {
    'use strict';

    // include utils
    $scope.yaioUtils = yaioUtils;

    // register the editor
    $scope.outputOptionsEditor = OutputOptionsEditor;

    // check parameter - set default if empty
    var baseUrl = '/show/';
    var nodeId = $routeParams.nodeId;
    var nodeByAllId = $routeParams.nodeByAllId;
    var flgNodeByAllId = false;
    if (nodeByAllId != null && nodeByAllId != "" && nodeByAllId) {
        nodeId = nodeByAllId;
        baseUrl = '/showByAllIds/';
        flgNodeByAllId = true;
    }
    if (nodeId == null || nodeId == "" || ! nodeId) {
        nodeId = yaioUtils.getConfig().CONST_MasterId;
    }

    // create node
    $scope.node = {};
    $scope.config = {treeOpenLevel: 1};
    
    // save lastLocation for login
    $rootScope.lastLocation = baseUrl + nodeId;

    // save nodeId for NodeEditor
    $rootScope.nodeId = nodeId;

    // call authentificate 
    authorization.authentificate(function () {
        // check authentification
        if (! $rootScope.authenticated) {
            console.log("showControl: not authentification: " + $rootScope.authenticated);
            $location.path(yaioUtils.getConfig().appLoginUrl);
            $scope.error = false;
        } else {
            // configure data load
            var loadOptions = {
                flgNodeByAllId: flgNodeByAllId
            };

            // check for activeNodeId if set
            var activeNodeId = $routeParams.activeNodeId;
            if (activeNodeId) {
                loadOptions.loadActiveNodeIdHandler = function() {
                    console.log("start loading activenode:" + activeNodeId);
                    return yaioUtils.getService('YaioNodeData').yaioDoLoadNodeById(activeNodeId, {})
                        .then(function sucess(angularResponse) {
                                // handle success
                                $scope.loadActiveNodeIdSuccessHandler(activeNodeId, {}, angularResponse.data);
                            }, function error(angularResponse) {
                                // handle error
                                var data = angularResponse.data;
                                var header = angularResponse.header;
                                var config = angularResponse.config;
                                var message = "error loading activenode with url: " + activeNodeUrl;
                                yaioUtils.getService('YaioBase').logError(message, true);
                                message = "error data: " + data + " header:" + header + " config:" + config;
                                yaioUtils.getService('YaioBase').logError(message, false);
                        });
                };
            }

            // load data
            console.log("NodeShowCtrl - processing nodeId=" + nodeId + " activeNodeId=" + activeNodeId);
            return yaioUtils.getService('YaioNodeData').yaioDoLoadNodeById(nodeId, loadOptions)
                .then(function sucess(angularResponse) {
                        // handle success
                        $scope.loadCurrentNodeIdSuccessHandler(nodeId, loadOptions, angularResponse.data);
                    }, function error(angularResponse) {
                        // handle error
                        var data = angularResponse.data;
                        var header = angularResponse.header;
                        var config = angularResponse.config;
                        var message = "error loading node with url: " + curNodeUrl;
                        yaioUtils.getService('YaioBase').logError(message, true);
                        message = "error data: " + data + " header:" + header + " config:" + config;
                        yaioUtils.getService('YaioBase').logError(message, false);
                });
        }
    });
    
    $scope.loadActiveNodeIdSuccessHandler = function(nodeId, options, yaioNodeActionResponse) {
        // check response
        var state = yaioNodeActionResponse.state;
        if (state == "OK") {
            // all fine
            console.log("NodeShowCtrl - OK loading activenode:" + yaioNodeActionResponse.stateMsg);
            
            // create nodehierarchy
            var nodeIdHierarchy = new Array();
            var parentNode = yaioNodeActionResponse.node.parentNode;
            while (parentNode != null && parentNode != "" && parentNode != "undefined") {
                nodeIdHierarchy.push(parentNode.sysUID);
                parentNode = parentNode.parentNode;
            }
            nodeIdHierarchy.reverse();
            
            // add me 
            nodeIdHierarchy.push(yaioNodeActionResponse.node.sysUID);
            
            // open Hierarchy
            yaioUtils.getService('YaioExplorerAction').openNodeHierarchy("#tree", nodeIdHierarchy);
        } else {
            // error
            yaioUtils.getService('YaioBase').logError("error loading activenode:" + yaioNodeActionResponse.stateMsg 
                    + " details:" + yaioNodeActionResponse, false);
        }
    };

    $scope.loadCurrentNodeIdSuccessHandler = function(nodeId, options, yaioNodeActionResponse) {
        // check response
        var state = yaioNodeActionResponse.state;
        if (state == "OK") {
            // all fine
            console.log("NodeShowCtrl - OK loading nodes:" + yaioNodeActionResponse.stateMsg);
            $scope.node = yaioNodeActionResponse.node;
            
            // create nodehierarchy
            var nodeHierarchy = new Array();
            var parentNode = yaioNodeActionResponse.node.parentNode;
            while (parentNode != null && parentNode != "" && parentNode != "undefined") {
                nodeHierarchy.push(parentNode);
                parentNode = parentNode.parentNode;
            }
            nodeHierarchy.reverse();
            $scope.nodeHierarchy = nodeHierarchy;

            // load fencytree
            yaioUtils.getService('YaioExplorerTree').yaioCreateFancyTree("#tree", $scope.node.sysUID, options.loadActiveNodeIdHandler);
            
            // load me
            $scope.yaioUtils.renderNodeLine(yaioNodeActionResponse.node, "#masterTr");

            // recalc gantt
            yaioUtils.getService('YaioNodeGanttRender').yaioRecalcMasterGanttBlock($scope.node);
        } else {
            // error
            yaioUtils.getService('YaioBase').logError("error loading nodes:" + yaioNodeActionResponse.stateMsg 
                    + " details:" + yaioNodeActionResponse, true);
        }
    }

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
        yaioUtils.getService('YaioExplorerAction').yaioExportExplorerLinesAsOverview();
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
        yaioUtils.getService('YaioExplorerAction').yaioSnapshot($scope.node);
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
        yaioUtils.getService('YaioExplorerAction').yaioOpenSubNodesForTree("#tree", $scope.config.treeOpenLevel);
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
        yaioUtils.getService('YaioNodeGanttRender').yaioRecalcFancytreeGanttBlocks();
        yaioUtils.getService('YaioNodeGanttRender').yaioRecalcMasterGanttBlock($scope.node);
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
        $("#inputGanttRangeStart").val(yaioUtils.getService('YaioBase').formatGermanDate(start)).trigger('input').triggerHandler("change");
        $("#inputGanttRangeEnde").val(yaioUtils.getService('YaioBase').formatGermanDate(ende)).trigger('input').triggerHandler("change");
        return false;
    };
});
