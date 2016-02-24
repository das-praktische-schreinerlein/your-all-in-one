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
yaioApp.controller('AuthController', function($rootScope, $scope, $location, $routeParams,
                                              setFormErrors, OutputOptionsEditor, authorization, yaioUtils) {
    'use strict';

    /**
     * init the controller
     * @private
     */
    $scope._init = function () {
        // include utils
        $scope.yaioUtils = yaioUtils;

        $scope.credentials = {};

        if ($routeParams.logout) {
            $scope.logout();
        }
    };

    /**
     * do login
     * @returns {*|JQueryPromise<any>|JQueryPromise<U>|JQueryPromise<void>}
     */
    $scope.login = function() {
        return yaioUtils.getService('YaioNodeData').yaioDoLogin($scope.credentials)
            .then(function success() {
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
                }, function error() {
                    // handle error
                    $location.path(yaioUtils.getConfig().appLoginUrl);
                    $scope.error = true;
                    $rootScope.authenticated = false;
            });
    };

    /**
     * do logout
     * @returns {*|JQueryPromise<any>|JQueryPromise<U>|JQueryPromise<void>}
     */
    $scope.logout = function() {
        return yaioUtils.getService('YaioNodeData').yaioDoLogout()
            .then(function success() {
                // handle success
                $rootScope.authenticated = false;
                $location.path(yaioUtils.getConfig().appRootUrl);
            }, function error() {
                // handle error
                $location.path(yaioUtils.getConfig().appRootUrl);
                $rootScope.authenticated = false;
            });
    };
    
    
    // init
    $scope._init();
});

