'use strict';

angular.module('yaioExplorerApp', ['ngAnimate', 'ngRoute'])
    // configure Module
    .config(function($routeProvider) {
        // configure routes
        $routeProvider
            .when('/', { 
                controller:  'NodeShowCtrl',
                templateUrl: 'templates/node.html' })
            .when('/show/:nodeId', { 
                controller:  'NodeShowCtrl',
                templateUrl: 'templates/node.html' })
            .when('/show/:nodeId/activate/:activeNodeId', { 
                controller:  'NodeShowCtrl',
                templateUrl: 'templates/node.html' })
            .otherwise({ redirectTo: '/'});
    })
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.defaults.headers.patch = {
            'Content-Type': 'application/json;charset=utf-8'
        }
    }])
    
    // add own directives
    .directive('state', function(){
        return {
            restrict: 'E', // own tag
            scope: {
                value: '='
            },
            template: '<span ng-show="value == \'RUNNING\'">laufend</span>' +
                      '<span ng-show="value != \'RUNNING\'">{{value}}</span>'
         };
    })
    
    // add own NodesController
    .controller('NodeShowCtrl', function($scope, $location, $http, $routeParams) {
        // check parameter - set default if empty
        var nodeId = $routeParams.nodeId;
        if (nodeId == null || nodeId == "" || ! nodeId) {
            nodeId = "MasterplanMasternode1";
        }
        // create node
        $scope.node = {};
        $scope.nodeForEdit = {};
        
        // check activeNodeId
        var activeNodeIdHandler;
        var activeNodeId = $routeParams.activeNodeId;
        if (activeNodeId) {
            // function to get NodeHierarchy fro activeNodeId
            activeNodeIdHandler = function() {
                console.log("start loading activenodes:" + activeNodeId)
                $http.get('/nodes/show/' + activeNodeId).then(function(nodeResponse) {
                    // check response
                    var state = nodeResponse.data.state;
                    if (state == "OK") {
                        // all fine
                        console.log("NodeShowCtrl - OK loading activenodes:" + nodeResponse.data.stateMsg)
                        
                        // create nodehierarchy
                        var nodeIdHierarchy = new Array();
                        var parentNode = nodeResponse.data.node.parentNode;
                        while (parentNode != null && parentNode != "" && parentNode != "undefined") {
                            nodeIdHierarchy.push(parentNode.sysUID);
                            parentNode = parentNode.parentNode;
                        }
                        nodeIdHierarchy.reverse();
                        
                        // open Hierarchy
                        openNodeHierarchy("#tree", nodeIdHierarchy);
                    } else {
                        // error
                        console.error("error loading activenodes:" + nodeResponse.data.stateMsg 
                                + " details:" + nodeResponse)
                    }
                    
                });
            }
        }
    
        // load data
        $http.get('/nodes/show/' + nodeId).then(function(nodeResponse) {
            // check response
            var state = nodeResponse.data.state;
            if (state == "OK") {
                // all fine
                console.log("NodeShowCtrl - OK loading nodes:" + nodeResponse.data.stateMsg)
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
                createOrReloadYAIOFancyTree("#tree", $scope.node.sysUID, activeNodeIdHandler);
            } else {
                // error
                console.error("error loading nodes:" + nodeResponse.data.stateMsg 
                        + " details:" + nodeResponse)
            }
            
        });

        // add save
        $scope.save = function() {
            // define json for common fields
            var json = JSON.stringify({name: $scope.nodeForEdit.name});
            
            // do extra for the different classNames
            if ($scope.nodeForEdit.className == "TaskNode") {
                
            } else if ($scope.nodeForEdit.className == "EventNode") {
                
            } else if ($scope.nodeForEdit.className == "InfoNode") {
                
            } else if ($scope.nodeForEdit.className == "UrlResNode") {
                
            }
            
            // create url
            var url = updateUrl + $scope.nodeForEdit.className + "/" + $scope.nodeForEdit.sysUID;
            console.log("NodeSave - url::" + url + " data:" + json);
            
            // do http
            $http({
                    method: 'PATCH',
                    url: url,
                    data: json
            }).then(function(nodeResponse) {
                // check response
                var state = nodeResponse.data.state;
                if (state == "OK") {
                    // all fine
                    console.log("NodeSave - OK loading nodes:" + nodeResponse.data.stateMsg)
                    
                    // reload
                    var newUrl = '/show/' + nodeId + '/activate/' + $scope.nodeForEdit.sysUID;
                    console.log("reaload:" + newUrl);
                    
                    // TODO: no cache!!!
                    $location.path(newUrl);
                } else {
                    // error
                    console.error("error loading nodes:" + nodeResponse.data.stateMsg 
                            + " details:" + nodeResponse)
                }
                
            });
        };
        
        
        // $location.path('/'); to redirect to url
    })
;
