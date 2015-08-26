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
 *     the controller to load login-page
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 */
yaioApp.controller('AuthController', function($rootScope, $scope, $location, $http, $routeParams, setFormErrors, OutputOptionsEditor, authorization, yaioUtils) {
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
                                $location.path(yaioUtils.getConfig().appRootUrl);
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
});

