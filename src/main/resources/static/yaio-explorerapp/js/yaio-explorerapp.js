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
            .otherwise({ redirectTo: '/'});
    })
    
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
    .controller('NodeShowCtrl', function($scope, $http, $routeParams) {
        var nodeId = $routeParams.nodeId;
    
        // check parameter - set default if empty
        if (nodeId == null || nodeId == "" || ! nodeId) {
            nodeId = "MasterplanMasternode1";
        }
    
        // load data
        $http.get('/nodes/show/' + nodeId).then(function(nodeResponse) {
            // create node
            $scope.node = {};
            
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
                createOrReloadYAIOFancyTree("#tree", $scope.node.sysUID);
            } else {
                // error
                console.error("error loading nodes:" + nodeResponse.data.stateMsg + " details:" + nodeResponse)
            }

        });
    })
;
