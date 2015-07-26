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
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     controller for the yaio-gui
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
var yaioApp = angular.module('yaioExplorerApp', ['ngAnimate', 'ngRoute', 'pascalprecht.translate']);

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
yaioApp.config(function($routeProvider) {
    'use strict';

    // configure routes
    $routeProvider
        .when('/show/:nodeId/activate/:activeNodeId', { 
            controller:  'NodeShowCtrl',
            templateUrl: 'js/explorer/node.html' })
        .when('/showByAllIds/:nodeByAllId', { 
            controller:  'NodeShowCtrl',
            templateUrl: 'js/explorer/node.html' })
        .when('/show/:nodeId', { 
            controller:  'NodeShowCtrl',
            templateUrl: 'js/explorer/node.html' })
        .when('/search/:curPage?/:pageSize?/:searchSort?/:baseSysUID?/:fulltext?/', { 
            controller:  'NodeSearchCtrl',
            templateUrl: 'js/search/node-search.html' })
        .when('/search/', { 
            controller:  'NodeSearchCtrl',
            templateUrl: 'js/search/node-search.html' })
        .when('/login', {
            controller : 'AuthController',
            templateUrl: 'js/auth/login.html' })
        .when('/logout', {
            controller : 'AuthController',
            templateUrl: 'js/auth/login.html' })
        .when('/frontpage/:nodeId', { 
            controller:  'FrontPageCtrl',
            templateUrl: 'js/frontpage/frontpage.html' })
        .when('/', { 
            controller:  'FrontPageCtrl',
            templateUrl: 'js/frontpage/frontpage.html' })
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
yaioApp.config(['$httpProvider', function($httpProvider) {
    'use strict';

    $httpProvider.defaults.headers.patch = {
        'Content-Type': 'application/json;charset=utf-8'
    };
}]);
