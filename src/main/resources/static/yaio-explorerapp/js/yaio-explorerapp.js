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
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     controller for the yaio-gui
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
var yaioM = angular.module('yaioExplorerApp', ['ngAnimate', 'ngRoute']);

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     configures the routing for the app<br>
 *     add new routes to the $routeProvider-instance
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>updates $routeProvider
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Routing Configuration
 * @param $routeProvider - the $routeProvider-instance to add the new routes
 */
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

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     configures $httpProvider - adds default-headers for patch-requests
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>updates $httpProvider
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 * @param $httpProvider - the $httpProvider to change the default-headers
 */
yaioM.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.headers.patch = {
        'Content-Type': 'application/json;charset=utf-8'
    }
}]);
    
/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     adds the new tag-directive "state" to output formated node-state
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new directive
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary
 */
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
yaioM.controller('NodeShowCtrl', function($scope, $location, $http, $routeParams, setFormErrors) {

    // check parameter - set default if empty
    var nodeId = $routeParams.nodeId;
    if (nodeId == null || nodeId == "" || ! nodeId) {
        nodeId = "MasterplanMasternode1";
    }
    // create node
    $scope.node = {};
    $scope.nodeForEdit = {};
    $scope.config = {treeOpenLevel: 1};
    
    // check activeNodeId
    var activeNodeIdHandler;
    var activeNodeId = $routeParams.activeNodeId;

    console.log("NodeShowCtrl - processing nodeId=" + nodeId + " activeNodeId=" + activeNodeId);

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
                    
                    // add me 
                    nodeIdHierarchy.push(nodeResponse.data.node.sysUID);
                    
                    // open Hierarchy
                    openNodeHierarchy("#tree", nodeIdHierarchy);
                } else {
                    // error
                    logError("error loading activenodes:" + nodeResponse.data.stateMsg 
                            + " details:" + nodeResponse, false)
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
            logError("error loading nodes:" + nodeResponse.data.stateMsg 
                    + " details:" + nodeResponse, true)
        }
        
    });
    
    
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
    $scope.openSubNodes = function(flgOpen) {
        console.log("openSubNodes:" + " level:" + $scope.config.treeOpenLevel);
        yaioOpenSubNodesForTree("#tree", $scope.config.treeOpenLevel);
        return false;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Help
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to open helpsite
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>open the helpsite
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     * @param url - the url of the helpsite
     */
    $scope.showHelpSite = function(url) {
        console.log("showHelpSite:" + " url:" + url);
        yaioShowHelpSite(url);
        return false;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Editor
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to discard and close the editor
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates layout
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.discard = function(formName) {
        closeYAIONodeEditor();
        return false;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Callback
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to validate the type of a newNode and open the corresponding form
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates layout
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.selectNewNodeType = function(formName) {
        // hide all forms
        resetYAIONodeEditorForms();
        
        // display createform and select nodeform
        $("#containerFormYaioEditorCreate").css("display", "block");
        var className = $scope.nodeForEdit["className"];
        $("#containerFormYaioEditor" + className).css("display", "block");
        console.log("selectNewNodeType open form #containerFormYaioEditor" + className);

        // special fields
        if (className == "SymLinkNode") {
            $scope.nodeForEdit["type"] = "SYMLINK";
        }

        // set mode
        $scope.nodeForEdit.mode = "create";
        return false;
    }
    

    /**
     * <h4>FeatureDomain:</h4>
     *     Callback
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to map the nodedata, create json,call webservice and 
     *     relocate to the new nodeId
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>save node and updates layout
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
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
            
            if (field.intern) {
                // ignore intern
                continue;
            }
            
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
        
        
        // branch depending on mode
        var method, url;
        var mode =  $scope.nodeForEdit["mode"];
        if (mode == "edit") {
            // mode update 
            method = "PATCH";
            url = updateUrl + $scope.nodeForEdit.className + "/" + $scope.nodeForEdit.sysUID;
        } else if (mode == "create") {
            // mode create 
            method = "POST";
            url = createUrl + $scope.nodeForEdit.className + "/" + $scope.nodeForEdit.sysUID;
            
            // unset sysUID
            nodeObj["sysUID"] = null;
        } else {
            // unknown mode
            logError("unknown mode=" + mode + " form formName=" + formName, false);
            return null;
        }

        // define json for common fields
        var json = JSON.stringify(nodeObj);
        
        // create url
        console.log("NodeSave - url::" + url + " data:" + json);
        
        // do http
        $http({
                method: method,
                url: url,
                data: json
        }).then(function(nodeResponse) {
            // check response
            var state = nodeResponse.data.state;
            if (state == "OK") {
                // all fine
                console.log("NodeSave - OK saved node:" + nodeResponse.data.stateMsg)
                
                // reload
                var newUrl = '/show/' + nodeId 
                    + '/activate/' + nodeResponse.data.node.sysUID; //$scope.nodeForEdit.sysUID
                console.log("reload:" + newUrl);
                
                // no cache!!!
                $location.path(newUrl + "?" + (new Date()).getTime());
            } else {
                // error
                var message = "error saving node:" + nodeResponse.data.stateMsg 
                        + " details:" + nodeResponse;
                
                // map violations
                var violations = nodeResponse.data.violations;
                var fieldErrors = {};
                if (violations && violations.length > 0) {
                    message = message + " violations: ";
                    for (var idx in violations) {
                        var violation = violations[idx];
                        console.log("map violation " + violation + " = " + violation.path + ":" + violation.message);
                        fieldErrors[violation.path] = [violation.message];
                        message = message + violation.path + ":" + violation.message + ", ";
                    }
                }
                logError(message, true);
                
                // Failed
                setFormErrors({
                    formName: formName,
                    fieldErrors: fieldErrors
                });
            }
        });
    };
});

/***************************************
 ***************************************
 * errorhandling from https://nulogy.com/articles/designing-angularjs-directives#.VBp5gvnV-Po
 ***************************************
 ***************************************/

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     new function to set form-errors when using input-elements with attribute "witherrors" 
 *     and element "fielderrors" to show the corresponding errors 
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new function
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary Errorhandling
 */
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


/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     adds the new attribute-directive "witherrors" to set/show errors from
 *     different sources (AngularJS, App, WebServices) for this element
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new directive
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary Errorhandling
 */
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


/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     extend the element with the new attribute-directive "witherrors" to 
 *     set/show errors from different sources (AngularJS, App, WebServices) for this element
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new callback
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary Errorhandling
 */
function directiveFieldsWithErrors () {
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
}

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     extend the element with the new attribute-directive "witherrors" to 
 *     set/show errors from different sources (AngularJS, App, WebServices) for this element
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns updated directive
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary Errorhandling
 */
yaioM.directive('input', function() {
    return directiveFieldsWithErrors();
});
//yaioM.directive('select', function() {
//    return directiveFieldsWithErrors();
//});
//yaioM.directive('textarea', function() {
//    return directiveFieldsWithErrors();
//});

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     adds the new element-directive "fielderrors" to set/show errors from
 *     different sources (AngularJS, App, WebServices) for this element
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new directive
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary Errorhandling
 */
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


  