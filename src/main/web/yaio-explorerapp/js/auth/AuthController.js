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
 * the controller to load login-page
 * @FeatureDomain                Configuration
 * @FeatureResult                returns new controller
 * @FeatureKeywords              GUI Configuration
 */
yaioApp.controller('AuthController', function($rootScope, $scope, $location, $routeParams, setFormErrors, OutputOptionsEditor, authorization, yaioUtils) {
    'use strict';

    // include utils
    $scope.yaioUtils = yaioUtils;

    $scope.credentials = {};
    
    $scope.login = function() {
        return yaioUtils.getService('YaioNodeData').yaioDoLogin($scope.credentials)
            .then(function success(data) {
                    // handle success
                    authorization.authentificate(function() {
                        if ($rootScope.authenticated) {
                            if ($rootScope.lastLocation) {
                                $location.path($rootScope.lastLocation);
                            } else {
                                $location.path(yaioUtils.getConfig().appFrontpageUrl);
                            }
                            $scope.error = false;
                        } else {
                            $location.path(yaioUtils.getConfig().appLoginUrl);
                            $scope.error = true;
                        }
                    });
                }, function error(data) {
                    // handle error
                    $location.path(yaioUtils.getConfig().appLoginUrl);
                    $scope.error = true;
                    $rootScope.authenticated = false;
            });
    };
    
    $scope.logout = function() {
        return yaioUtils.getService('YaioNodeData').yaioDoLogout()
            .then(function success(data) {
                // handle success
                $rootScope.authenticated = false;
                $location.path(yaioUtils.getConfig().appRootUrl);
            }, function error(data) {
                // handle error
                $location.path(yaioUtils.getConfig().appRootUrl);
                $rootScope.authenticated = false;
            });
    };
    
    
    if ($routeParams.logout) {
        $scope.logout();
    };
});

