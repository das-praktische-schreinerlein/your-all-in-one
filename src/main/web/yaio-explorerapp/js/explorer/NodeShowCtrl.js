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
 * the controller to load the nodes for url-params and register the yaio-functions
 * @FeatureDomain                Configuration
 * @FeatureResult                returns new controller
 * @FeatureKeywords              GUI Configuration BusinessLogic
 */
yaioApp.controller('NodeShowCtrl', function($rootScope, $scope, $location, $routeParams, setFormErrors, OutputOptionsEditor, authorization, yaioUtils) {
    'use strict';

    // include utils
    $scope.yaioUtils = yaioUtils;

    // register the editor
    $scope.outputOptionsEditor = OutputOptionsEditor;
    
    // register filterOptions
    $scope.filterOptions = {};
    if ($routeParams.workflowState && $routeParams.workflowState != "?") {
        $scope.filterOptions.strWorkflowStateFilter = $routeParams.workflowState;
    }
    if ($routeParams.statCount && $routeParams.statCount != "?") {
        $scope.filterOptions.strStatCountFilter = $routeParams.statCount;
    }

    // check parameter - set default if empty
    var baseUrl = '/show/';
    var nodeId = $routeParams.nodeId;
    var nodeByAllId = $routeParams.nodeByAllId;
    var activeNodeId = $routeParams.activeNodeId;
    var flgNodeByAllId = false;
    if (nodeByAllId != null && nodeByAllId != "" && nodeByAllId) {
        nodeId = nodeByAllId;
        baseUrl = '/showByAllIds/';
        flgNodeByAllId = true;
    }
    if (nodeId == null || nodeId == "" || ! nodeId) {
        nodeId = yaioUtils.getConfig().masterSysUId;
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
            
            // configure filter
            $scope.setExplorerFilter();

            // check for activeNodeId if set
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
            
            // load only when templates loaded, because we need some time for rendering angular :-(
            var tries = 20;
            var templateIsLoadedTimer;
            var templateIsLoadedHandler = function() {
                tries--
                var loaded = $("#masterTr").length;
                if (loaded || tries <= 0) {
                    clearInterval(templateIsLoadedTimer);
                    
                    // load fencytree
                    yaioUtils.getService('YaioExplorerTree').yaioCreateFancyTree("#tree", $scope.node.sysUID, options.loadActiveNodeIdHandler);
                    
                    // load me
                    $scope.yaioUtils.renderNodeLine(yaioNodeActionResponse.node, "#masterTr", false);

                    // recalc gantt
                    yaioUtils.getService('YaioNodeGanttRender').yaioRecalcMasterGanttBlock($scope.node);
                }
            };
            templateIsLoadedTimer = setInterval(templateIsLoadedHandler, 100);
        } else {
            // error
            yaioUtils.getService('YaioBase').logError("error loading nodes:" + yaioNodeActionResponse.stateMsg 
                    + " details:" + yaioNodeActionResponse, true);
        }
    }

    /** 
     * export GUI As Overview
     * @FeatureDomain                Editor
     * @FeatureResult                exportAsOverview
     * @FeatureKeywords              GUI Callback
     */
    $scope.exportAsOverview = function() {
        console.log("exportAsOverview");
        yaioUtils.getService('YaioExplorerAction').yaioExportExplorerLinesAsOverview();
        return false;
    };

    /** 
     * create snapshot of GUI
     * @FeatureDomain                Editor
     * @FeatureResult                snapshot
     * @FeatureKeywords              GUI Callback
     */
    $scope.snapshot = function() {
        console.log("snapshot");
        yaioUtils.getService('YaioExplorerAction').yaioSnapshot($scope.node);
        return false;
    };

    /** 
     * callbackhandler to open all subnodes<level in the treeview
     * @FeatureDomain                Editor
     * @FeatureResult                open the subNodes till treeOpenLevel
     * @FeatureKeywords              GUI Callback
     */
    $scope.openSubNodes = function() {
        console.log("openSubNodes:" + " level:" + $scope.config.treeOpenLevel);
        yaioUtils.getService('YaioExplorerAction').yaioOpenSubNodesForTree("#tree", $scope.config.treeOpenLevel);
        return false;
    };

    /** 
     * callbackhandler to recalc ganttblocks for nodes
     * @FeatureDomain                GUI
     * @FeatureResult                recalc ganttblocks
     * @FeatureKeywords              GUI Callback
     */
    $scope.recalcGanttBlocks = function() {
        yaioUtils.getService('YaioNodeGanttRender').yaioRecalcFancytreeGanttBlocks();
        yaioUtils.getService('YaioNodeGanttRender').yaioRecalcMasterGanttBlock($scope.node);
        return false;
    };

    /** 
     * callbackhandler to recalc Gantt with Master-Ist/Plan-DateRange
     * @FeatureDomain                GUI
     * @FeatureResult                recalc ganttblocks
     * @FeatureKeywords              GUI Callback
     * @param flgShowIst             boolean if is set show ist, if not show plan
     */
    $scope.recalcGanttForIstOrPlan = function(flgShowIst) {
        var start = flgShowIst ? $scope.node.istChildrenSumStart : $scope.node.planChildrenSumStart;
        var ende = flgShowIst ? $scope.node.istChildrenSumEnde : $scope.node.planChildrenSumEnde;
        $("#inputGanttRangeStart").val(yaioUtils.getService('YaioBase').formatGermanDate(start)).trigger('input').triggerHandler("change");
        $("#inputGanttRangeEnde").val(yaioUtils.getService('YaioBase').formatGermanDate(ende)).trigger('input').triggerHandler("change");
        return false;
    };
    
    $scope.changeExplorerFilter = function() {
        var msg = "changeExplorerFilter node: " + nodeId;
        var newUrl = '/show/' + nodeId 
            + '/' + ($scope.filterOptions.strWorkflowStateFilter ? $scope.filterOptions.strWorkflowStateFilter : "?")
            + '/' + ($scope.filterOptions.strStatCountFilter ? $scope.filterOptions.strStatCountFilter : "?") + "/";
        if (activeNodeId) {
            newUrl = newUrl + 'activate/' + activeNodeId + '/';
        }
        
        // no cache!!!
        newUrl = newUrl +  "?" + (new Date()).getTime();
        console.log(msg + " RELOAD:" + newUrl);
        $location.path(newUrl);
    };
    
    $scope.setExplorerFilter = function() {
        // set new filter
        var nodeFilter = yaioUtils.getService('YaioExplorerTree').nodeFilter || {};
        nodeFilter.workflowStates = null;
        if ($scope.filterOptions.strWorkflowStateFilter) {
            var arrWorkflowStateFilter = $scope.filterOptions.strWorkflowStateFilter.split(",");
            if (arrWorkflowStateFilter.length > 0) {
                nodeFilter.workflowStates = {};
                for (var i=0; i < arrWorkflowStateFilter.length; i++) {
                    nodeFilter.workflowStates[arrWorkflowStateFilter[i]] = arrWorkflowStateFilter[i];
                }
            }
        }
        nodeFilter.statCount = null;
        if ($scope.filterOptions.strStatCountFilter) {
            nodeFilter.statCount = $scope.filterOptions.strStatCountFilter;
        }
        console.log("setExplorerFilter: set filter:", nodeFilter);
        yaioUtils.getService('YaioExplorerTree').setNodeFilter(nodeFilter);
    }
    
    
    $scope.initDragDropFileUploader = function(divId) {
        // Setup the Uploadfile-Listener
        var dropZone = document.getElementById(divId);
        dropZone.addEventListener('dragover', function (event) { console.log("dragover:", event); yaioUtils.getService('YaioEditor').handleUploadFileUrlResNodeDragOver(event); }, false);
        dropZone.addEventListener('drop', function (event) { console.log("drop:", event); yaioUtils.getService('YaioEditor').handleUploadFileUrlResNodeSelect(event); }, false);
    }

});
