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
yaioApp.controller('NodeShowCtrl', function($rootScope, $scope, $location, $routeParams,
                                            setFormErrors, OutputOptionsEditor, authorization, yaioUtils) {
    'use strict';

    // check parameter - set default if empty
    var baseUrl = '/show/';
    var nodeId = $routeParams.nodeId;
    var nodeByAllId = $routeParams.nodeByAllId;
    var activeNodeId = $routeParams.activeNodeId;
    var flgNodeByAllId = false;
    if (!yaioUtils.getService('DataUtils').isUndefinedStringValue(nodeByAllId)) {
        nodeId = nodeByAllId;
        baseUrl = '/showByAllIds/';
        flgNodeByAllId = true;
    }
    if (yaioUtils.getService('DataUtils').isUndefinedStringValue(nodeId)) {
        nodeId = yaioUtils.getConfig().masterSysUId;
    }

    /**
     * init the controller
     * @private
     */
    $scope._init = function () {
        // include utils
        $scope.yaioUtils = yaioUtils;

        // register the editor
        $scope.outputOptionsEditor = OutputOptionsEditor;

        // register filterOptions
        $scope.filterOptions = {};
        if ($routeParams.workflowState && $routeParams.workflowState !== '?') {
            $scope.filterOptions.strWorkflowStateFilter = $routeParams.workflowState;
        }
        if ($routeParams.statCount && $routeParams.statCount !== '?') {
            $scope.filterOptions.strStatCountFilter = $routeParams.statCount;
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
            if (!$rootScope.authenticated) {
                console.log('showControl: not authentification: ' + $rootScope.authenticated);
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
                    loadOptions.loadActiveNodeIdHandler = function () {
                        console.log('start loading activenode:' + activeNodeId);
                        return yaioUtils.getService('YaioNodeData').getNodeById(activeNodeId, {})
                            .then(function sucess(angularResponse) {
                                // handle success
                                $scope.loadActiveNodeIdSuccessHandler(activeNodeId, {}, angularResponse.data);
                            }, function error(angularResponse) {
                                // handle error
                                var data = angularResponse.data;
                                var header = angularResponse.header;
                                var config = angularResponse.config;
                                var message = 'error loading activenode: ' + activeNodeId;
                                yaioUtils.getService('Logger').logError(message, true);
                                message = 'error data: ' + data + ' header:' + header + ' config:' + config;
                                yaioUtils.getService('Logger').logError(message, false);
                            });
                    };
                }

                // load data
                console.log('NodeShowCtrl - processing nodeId=' + nodeId + ' activeNodeId=' + activeNodeId);
                return yaioUtils.getService('YaioNodeData').getNodeById(nodeId, loadOptions)
                    .then(function sucess(angularResponse) {
                        // handle success
                        $scope.loadCurrentNodeIdSuccessHandler(nodeId, loadOptions, angularResponse.data);
                    }, function error(angularResponse) {
                        // handle error
                        var data = angularResponse.data;
                        var header = angularResponse.header;
                        var config = angularResponse.config;
                        var message = 'error loading node=' + nodeId + ' activeNodeId=' + activeNodeId;
                        yaioUtils.getService('Logger').logError(message, true);
                        message = 'error data: ' + data + ' header:' + header + ' config:' + config;
                        yaioUtils.getService('Logger').logError(message, false);
                    });
            }
        });
    };

    /**
     * callbackhandler if load of active nodeId succeeded
     * opens the nodehirarchy till that activeId in current fancytree
     * @param {String} nodeId                    active node id till the hirarchy-tre will opened
     * @param {Object} options                   loadOptions
     * @param {Object} yaioNodeActionResponse    server-response with nodedata + parenthirarchy
     */
    $scope.loadActiveNodeIdSuccessHandler = function(nodeId, options, yaioNodeActionResponse) {
        // check response
        var state = yaioNodeActionResponse.state;
        if (state === 'OK') {
            // all fine
            console.log('NodeShowCtrl - OK loading activenode:' + yaioNodeActionResponse.stateMsg);
            
            // create nodehierarchy
            var nodeIdHierarchy = [];
            var parentNode = yaioNodeActionResponse.node.parentNode;
            while (!yaioUtils.getService('DataUtils').isEmptyStringValue(parentNode)) {
                nodeIdHierarchy.push(parentNode.sysUID);
                parentNode = parentNode.parentNode;
            }
            nodeIdHierarchy.reverse();
            
            // add me 
            nodeIdHierarchy.push(yaioNodeActionResponse.node.sysUID);
            
            // open Hierarchy
            yaioUtils.getService('YaioExplorerAction').openNodeHierarchyForTreeId('#tree', nodeIdHierarchy);
        } else {
            // error
            yaioUtils.getService('Logger').logError('error loading activenode:' + yaioNodeActionResponse.stateMsg
                    + ' details:' + yaioNodeActionResponse, false);
        }
    };

    /**
     * callbackhandler if load of current nodeId succeeded
     * renders the fancytree for the children of current node
     * @param {String} nodeId                    active node id to open
     * @param {Object} options                   loadOptions
     * @param {Object} yaioNodeActionResponse    server-response with nodedata + children
     */
    $scope.loadCurrentNodeIdSuccessHandler = function(nodeId, options, yaioNodeActionResponse) {
        // check response
        var state = yaioNodeActionResponse.state;
        if (state === 'OK') {
            // all fine
            console.log('NodeShowCtrl - OK loading nodes:' + yaioNodeActionResponse.stateMsg);
            $scope.node = yaioNodeActionResponse.node;
            
            // create nodehierarchy
            var nodeHierarchy = [];
            var parentNode = yaioNodeActionResponse.node.parentNode;
            while (!yaioUtils.getService('DataUtils').isEmptyStringValue(parentNode)) {
                nodeHierarchy.push(parentNode);
                parentNode = parentNode.parentNode;
            }
            nodeHierarchy.reverse();
            $scope.nodeHierarchy = nodeHierarchy;
            
            // load only when templates loaded, because we need some time for rendering angular :-(
            var tries = 20;
            var templateIsLoadedTimer;
            var templateIsLoadedHandler = function() {
                tries--;
                var loaded = $('#masterTr').length;
                if (loaded || tries <= 0) {
                    clearInterval(templateIsLoadedTimer);
                    
                    // load fencytree
                    yaioUtils.getService('YaioExplorerTree').yaioCreateFancyTree('#tree', $scope.node.sysUID, options.loadActiveNodeIdHandler);
                    
                    // load me
                    $scope.yaioUtils.renderNodeLine(yaioNodeActionResponse.node, '#masterTr', false);

                    // recalc gantt
                    yaioUtils.getService('YaioNodeGanttRenderer').recalcMasterGanttBlock($scope.node);
                }
            };
            templateIsLoadedTimer = setInterval(templateIsLoadedHandler, 100);
        } else {
            // error
            yaioUtils.getService('Logger').logError('error loading nodes:' + yaioNodeActionResponse.stateMsg
                    + ' details:' + yaioNodeActionResponse, true);
        }
    };

    /** 
     * buttoncommand to export visible nodehirarchy as overview into clipboardwindow
     */
    $scope.exportAsOverview = function() {
        console.log('exportAsOverview');
        yaioUtils.getService('YaioExplorerAction').openClipBoardWithCurrentViewAsOverview();
        return false;
    };

    /**
     * buttoncommand to open nodeeditor with the visible nodehirarchy as snaphot
     */
    $scope.snapshot = function() {
        console.log('snapshot');
        yaioUtils.getService('YaioExplorerAction').openNewInfoNodeWithCurrentViewAsSnapshotForParent($scope.node);
        return false;
    };

    /** 
     * buttoncommand to open all subnodes < $scope.config.treeOpenLevel in the treeview
     */
    $scope.openSubNodes = function() {
        console.log('openSubNodes:' + ' level:' + $scope.config.treeOpenLevel);
        yaioUtils.getService('YaioExplorerAction').openSubNodesForTreeId('#tree', $scope.config.treeOpenLevel);
        return false;
    };

    /**
     * callbackhandler to recalc ganttblocks for current treeview
     */
    $scope.recalcGanttBlocks = function() {
        yaioUtils.getService('YaioNodeGanttRenderer').recalcGanttBlocksForTree();
        yaioUtils.getService('YaioNodeGanttRenderer').recalcMasterGanttBlock($scope.node);
        return false;
    };

    /**
     * buttoncommand to recalc ganttblocks for current treeview and date-filter
     * - from: $scope.node.istChildrenSumStart || $scope.node.planChildrenSumStart
     * - to: $scope.node.istChildrenSumEnde || $scope.node.planChildrenSumEnde
     * @param {Booolean} flgShowIst             if is set show IST, if not show PLAN
     */
    $scope.recalcGanttForIstOrPlan = function(flgShowIst) {
        var start = flgShowIst ? $scope.node.istChildrenSumStart : $scope.node.planChildrenSumStart;
        var ende = flgShowIst ? $scope.node.istChildrenSumEnde : $scope.node.planChildrenSumEnde;
        $('#inputGanttRangeStart').val(yaioUtils.getService('DataUtils').formatGermanDate(start)).trigger('input').triggerHandler('change');
        $('#inputGanttRangeEnde').val(yaioUtils.getService('DataUtils').formatGermanDate(ende)).trigger('input').triggerHandler('change');
        return false;
    };

    /**
     * callback to reload page after change of $scope.filterOptions.strWorkflowStateFilter or
     * $scope.filterOptions.strStatCountFilter
     */
    $scope.changeExplorerFilter = function() {
        var msg = 'changeExplorerFilter node: ' + nodeId;
        var newUrl = '/show/' + nodeId 
            + '/' + ($scope.filterOptions.strWorkflowStateFilter ? $scope.filterOptions.strWorkflowStateFilter : '?')
            + '/' + ($scope.filterOptions.strStatCountFilter ? $scope.filterOptions.strStatCountFilter : '?') + '/';
        if (activeNodeId) {
            newUrl = newUrl + 'activate/' + activeNodeId + '/';
        }
        
        // no cache!!!
        newUrl = newUrl +  '?' + (new Date()).getTime();
        console.log(msg + ' RELOAD:' + newUrl);
        $location.path(newUrl);
    };

    /**
     * callback to set explorerfilter for fancytree with $scope.filterOptions.strWorkflowStateFilter or
     * $scope.filterOptions.strStatCountFilter
     */
    $scope.setExplorerFilter = function() {
        // set new filter
        var nodeFilter = yaioUtils.getService('YaioExplorerTree').nodeFilter || {};
        nodeFilter.workflowStates = null;
        if ($scope.filterOptions.strWorkflowStateFilter) {
            var arrWorkflowStateFilter = $scope.filterOptions.strWorkflowStateFilter.split(',');
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
        console.log('setExplorerFilter: set filter:', nodeFilter);
        yaioUtils.getService('YaioExplorerTree').setNodeFilter(nodeFilter);
    };


    /**
     * initialize the drag&drop
     * @param {String} divId          id of the drag&drop-html-element
     */
    $scope.initDragDropFileUploader = function(divId) {
        // change to https://www.npmjs.com/package/angular-draganddrop
        // Setup the Uploadfile-Listener
        var dropZone = document.getElementById(divId);
        dropZone.addEventListener('dragover', function (event) {
            console.log('dragover:', event); yaioUtils.getService('YaioEditor').handleUploadFileUrlResNodeDragOver(event);
        }, false);
        dropZone.addEventListener('drop', function (event) {
            console.log('drop:', event); yaioUtils.getService('YaioEditor').handleUploadFileUrlResNodeSelect(event);
        }, false);
    };

    // init
    $scope._init();
});
