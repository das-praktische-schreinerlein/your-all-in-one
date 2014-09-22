'use strict';

var yaioM = angular.module('yaioExplorerApp', ['ngAnimate', 'ngRoute']);

// configure Module
yaioM.config(function($routeProvider) {
    // configure routes
    $routeProvider
        .when('/show/:nodeId/activate/:activeNodeId', { 
            controller:  'NodeShowCtrl',
            templateUrl: 'templates/node.html' })
        .when('/show/:nodeId', { 
            controller:  'NodeShowCtrl',
            templateUrl: 'templates/node.html' })
        .when('/', { 
            controller:  'NodeShowCtrl',
            templateUrl: 'templates/node.html' })
        .otherwise({ redirectTo: '/'});
});
yaioM.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.headers.patch = {
        'Content-Type': 'application/json;charset=utf-8'
    }
}]);
    
// add own directives
yaioM.directive('state', function(){
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
yaioM.controller('NodeShowCtrl', function($scope, $location, $http, $routeParams, setFormErrors) {

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

    console.log("NodeShowCtrl - processing nodeId=" + nodeId + " activeNodeId=" + activeNodeId);

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
            createYAIOFancyTree("#tree", $scope.node.sysUID, activeNodeIdHandler);
        } else {
            // error
            console.error("error loading nodes:" + nodeResponse.data.stateMsg 
                    + " details:" + nodeResponse)
        }
        
    });

    // add discard
    $scope.discard = function(formName) {
        closeEditorForNode();
    }

    // add save
    $scope.save = function(formName) {
        // define json for common fields
        var nodeObj = {name: $scope.nodeForEdit.name};

        // do extra for the different classNames
        // configure value mapping
        var fields = new Array();
        fields = fields.concat(configNodeTypeFields.Common.fields);
        if ($scope.nodeForEdit.className == "TaskNode") {
            fields = fields.concat(configNodeTypeFields.TaskNode.fields);
        } else if ($scope.nodeForEdit.className == "EventNode") {
            fields = fields.concat(configNodeTypeFields.EventNode.fields);
        } else if ($scope.nodeForEdit.className == "InfoNode") {
            fields = fields.concat(configNodeTypeFields.InfoNode.fields);
        } else if ($scope.nodeForEdit.className == "UrlResNode") {
            fields = fields.concat(configNodeTypeFields.UrlResNode.fields);
        } else if ($scope.nodeForEdit.className == "SymLinkNode") {
            fields = fields.concat(configNodeTypeFields.SymLinkNode.fields);
        }
        
        // iterate fields an map to nodeObj
        for (var idx in fields) {
            var field = fields[idx];
            var fieldName = field.fieldName;
            var value = $scope.nodeForEdit[fieldName];
            
            // convert values
            if (field.datatype == "date" && value) {
                var lstDate=value.split(".");
                var newDate=new Date(lstDate[1]+"/"+lstDate[0]+"/"+lstDate[2]);
                value = newDate.getTime();
            } if (field.datatype == "datetime" && value) {
                var lstDateTime=value.split(" ");
                var lstDate = lstDateTime[0].split(".");
                var lstTime = lstDateTime[1];
                var newDate=new Date(lstDate[1]+"/"+lstDate[0]+"/"+lstDate[2] + " " + lstTime[1]);
                value = newDate.getTime();
            }
            
            nodeObj[fieldName] = value;
            console.log("map nodefield:" + fieldName + "=" + value);
        }

        // define json for common fields
        var json = JSON.stringify(nodeObj);
        
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
                console.log("NodeSave - OK updated node:" + nodeResponse.data.stateMsg)
                
                // reload
                var newUrl = '/show/' + nodeId + '/activate/' + $scope.nodeForEdit.sysUID;
                console.log("reload:" + newUrl);
                
                // no cache!!!
                $location.path(newUrl + "?" + (new Date()).getTime());
            } else {
                // error
                console.error("error updating node:" + nodeResponse.data.stateMsg 
                        + " details:" + nodeResponse);
                
                // map violations
                var violations = nodeResponse.data.violations;
                var fieldErrors = {};
                if (violations && violations.length > 0) {
                    for (var idx in violations) {
                        var violation = violations[idx];
                        console.log("map violation " + violation + " = " + violation.path + ":" + violation.message);
                        fieldErrors[violation.path] = [violation.message];
                    }
                }
                
                // Failed
                setFormErrors({
                    formName: formName,
                    fieldErrors: fieldErrors
                });
            }
        });
    };
});


// errorhandling from https://nulogy.com/articles/designing-angularjs-directives#.VBp5gvnV-Po
yaioM.factory('setFormErrors', function() {
    // Registered withErrors controllers
    var withErrorCtrls = [];

    // The exposed service
    var setFormErrors = function(opts) {
        var fieldErrors = opts.fieldErrors;
        var ctrl = withErrorCtrls[opts.formName];

        Object.keys(fieldErrors).forEach(function(fieldName) {
            ctrl.setErrorsFor(fieldName, fieldErrors[fieldName]);
        });
    };

    // Registers withErrors controller by form name (for internal use)
    setFormErrors._register = function(formName, ctrl) {
        withErrorCtrls[formName] = ctrl;
    };

    return setFormErrors;
});

yaioM.directive('withErrors', ['setFormErrors', function(setFormErrors) {
    return {
        restrict: 'A',
        require: 'withErrors',
        controller: ['$scope', '$element', function($scope, $element) {
            var controls = {};

            this.addControl = function(fieldName, ctrl) {
                controls[fieldName] = ctrl;
            };

            this.setErrorsFor = function(fieldName, errors) {
                if (!(fieldName in controls)) return;
                return controls[fieldName].setErrors(errors);
            };

            this.clearErrorsFor = function(fieldName, errors) {
                if (!(fieldName in controls)) return;
                return controls[fieldName].clearErrors(errors);
            };
        }],
        link: function(scope, element, attrs, ctrl) {
            // Make this form controller accessible to setFormErrors service
            setFormErrors._register(attrs.name, ctrl);
        }
    }; 
}]);

yaioM.directive('input', function() {
    return {
        restrict: 'E',
        require: ['?ngModel', '?^withErrors'],
        scope: true,
        link: function(scope, element, attrs, ctrls) {
            var ngModelCtrl = ctrls[0];
            var withErrorsCtrl = ctrls[1];
            var fieldName = attrs.name;

            if (!ngModelCtrl || !withErrorsCtrl) return;

            // Watch for model changes and set errors if any
            scope.$watch(attrs.ngModel, function() {
                if (ngModelCtrl.$dirty && ngModelCtrl.$invalid) {
                    withErrorsCtrl.setErrorsFor(fieldName, errorMessagesFor(ngModelCtrl));
                } else if (ngModelCtrl.$valid) {
                    withErrorsCtrl.clearErrorsFor(fieldName);
                }
            });

            // Mapping Angular validation errors to a message
            var errorMessages = {
                    required: 'This field is required'
            };

            function errorMessagesFor(ngModelCtrl) {
                return Object.keys(ngModelCtrl.$error).
                map(function(key) {
                    if (ngModelCtrl.$error[key]) return errorMessages[key];
                    else return null;
                }).
                filter(function(msg) {
                    return msg !== null;
                });
            }
        }
    }  
});

yaioM.directive('fielderrors', function() {
    return {
        restrict: 'E',
        replace: true,
        scope: true,
        require: ['fielderrors', '^withErrors'],
        template: 
            '<div ng-repeat="error in errors">' +
            '<small class="error">{{ error }}</small>' +
            '</div>',
            controller: ['$scope', function($scope) {
                $scope.errors = [];
                this.setErrors = function(errors) {
                    $scope.errors = errors;
                };
                this.clearErrors = function() {
                    $scope.errors = [];
                };
            }],
            link: function(scope, element, attrs, ctrls) {
                var fieldErrorsCtrl = ctrls[0];
                var withErrorsCtrl = ctrls[1];
                withErrorsCtrl.addControl(attrs.for, fieldErrorsCtrl);
            }
    };
});
  