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
yaioApp.factory('setFormErrors', function() {
    'use strict';

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
yaioApp.directive('withErrors', ['setFormErrors', function(setFormErrors) {
    'use strict';

    return {
        restrict: 'A',
        require: 'withErrors',
        controller: ['$scope', '$element', function($scope, $element) {
            var controls = {};

            this.addControl = function(fieldName, ctrl) {
                controls[fieldName] = ctrl;
            };

            this.setErrorsFor = function(fieldName, errors) {
                if (!(fieldName in controls)) { return; }
                return controls[fieldName].setErrors(errors);
            };

            this.clearErrorsFor = function(fieldName, errors) {
                if (!(fieldName in controls)) { return; }
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
    'use strict';

    return {
        restrict: 'E',
        require: ['?ngModel', '?^withErrors'],
        scope: true,
        link: function(scope, element, attrs, ctrls) {
            var ngModelCtrl = ctrls[0];
            var withErrorsCtrl = ctrls[1];
            var fieldName = attrs.name;

            if (!ngModelCtrl || !withErrorsCtrl) { return; }

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
                    if (ngModelCtrl.$error[key]) { return errorMessages[key]; }
                    else { return null; }
                }).
                filter(function(msg) {
                    return msg !== null;
                });
            }
        }
    };
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
yaioApp.directive('input', function() {
    'use strict';

    return directiveFieldsWithErrors();
});
//yaioApp.directive('select', function() {
//    return directiveFieldsWithErrors();
//});
//yaioApp.directive('textarea', function() {
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
yaioApp.directive('fielderrors', function() {
    'use strict';

    return {
        restrict: 'E',
        replace: true,
        scope: true,
        require: ['fielderrors', '^withErrors'],
        template: 
            '<div class="fielderror" ng-repeat="error in errors">' +
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
