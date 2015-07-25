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
 *     the controller to load login-page
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 */
yaioApp.controller('AuthController', function($rootScope, $scope, $location, $http, $routeParams, setFormErrors, OutputOptionsEditor, authorization) {
    $scope.credentials = {};
    
    $scope.login = function() {
        $http.post('/login', $.param($scope.credentials), {
            headers : {
                "content-type" : "application/x-www-form-urlencoded"
            }
        }).success(function(data) {
            authorization.authentificate(function() {
                if ($rootScope.authenticated) {
                    if ($rootScope.lastLocation) {
                        $location.path($rootScope.lastLocation);
                    } else {
                        $location.path("/");
                    }
                    $scope.error = false;
                } else {
                    $location.path("/login");
                    $scope.error = true;
                }
            });
        }).error(function(data) {
            $location.path("/login");
            $scope.error = true;
            $rootScope.authenticated = false;
        })
    };
    
    $scope.logout = function() {
        $http.post('/logout', {}).success(function() {
            $rootScope.authenticated = false;
            $location.path("/");
        }).error(function(data) {
            $location.path("/");
            $rootScope.authenticated = false;
        });
    };
});

