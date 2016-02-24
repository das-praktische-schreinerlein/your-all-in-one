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
 * controller for the yaio-gui
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
var yaioApp = angular.module('yaioExplorerApp', ['ngAnimate', 'ngRoute', 'pascalprecht.translate']);

/** 
 * configures the routing for the app
 * add new routes to the $routeProvider-instance
 * @FeatureDomain                Configuration
 * @FeatureResult                updates $routeProvider
 * @FeatureKeywords              GUI Routing Configuration
 * @param $routeProvider         the $routeProvider-instance to add the new routes
 */
yaioApp.config(function($routeProvider) {
    'use strict';
    
    var resBaseUrl = yaioAppBase.config.resBaseUrl;

    // configure routes
    $routeProvider
        .when('/show/:nodeId/activate/:activeNodeId/:dummy?', { 
            controller:  'NodeShowCtrl',
            templateUrl: resBaseUrl + 'js/explorer/node.html' })
        .when('/show/:nodeId/:workflowState?/:statCount?/activate/:activeNodeId/:dummy?', { 
            controller:  'NodeShowCtrl',
            templateUrl: resBaseUrl + 'js/explorer/node.html' })
        .when('/show/:nodeId/:workflowState?/:statCount?/:dummy?', { 
            controller:  'NodeShowCtrl',
            templateUrl: resBaseUrl + 'js/explorer/node.html' })
        .when('/show/:nodeId', { 
            controller:  'NodeShowCtrl',
            templateUrl: resBaseUrl + 'js/explorer/node.html' })
        .when('/showByAllIds/:nodeByAllId', { 
            controller:  'NodeShowCtrl',
            templateUrl: resBaseUrl + 'js/explorer/node.html' })
        .when('/search/:curPage?/:pageSize?/:searchSort?/:baseSysUID?/:fulltext?/:strClassFilter?/:strWorkflowStateFilter?/:strNotNodePraefix?/', { 
            controller:  'NodeSearchCtrl',
            templateUrl: resBaseUrl + 'js/search/node-search.html' })
        .when('/search/', { 
            controller:  'NodeSearchCtrl',
            templateUrl: resBaseUrl + 'js/search/node-search.html' })
        .when('/login', {
            controller : 'AuthController',
            templateUrl: resBaseUrl + 'js/auth/login.html' })
        .when('/logout', {
            controller : 'AuthController',
            templateUrl: resBaseUrl + 'js/auth/login.html' })
        .when('/logout/:logout', {
            controller : 'AuthController',
            templateUrl: resBaseUrl + 'js/auth/login.html' })
        .when('/frontpage/:nodeId', { 
            controller:  'FrontPageCtrl',
            templateUrl: resBaseUrl + 'js/frontpage/frontpage.html' })
        .when('/frontpage', { 
            controller:  'FrontPageCtrl',
            templateUrl: resBaseUrl + 'js/frontpage/frontpage.html' })
        .when('/dashboard', { 
            controller:  'DashboardCtrl',
            templateUrl: resBaseUrl + 'js/dashboard/dashboard.html' })
        .when('/sourceselect', { 
            controller:  'SourceSelectorCtrl',
            templateUrl: resBaseUrl + 'js/sourceselector/sourceselector.html' })
        .when('/sourceselect/:newds', { 
            controller:  'SourceSelectorCtrl',
            templateUrl: resBaseUrl + 'js/sourceselector/sourceselector.html' })
        .when('/', { 
            controller:  'SourceSelectorCtrl',
            templateUrl: resBaseUrl + 'js/sourceselector/sourceselector.html' })
        .otherwise({ redirectTo: '/'});
});

/** 
 * configures $sceDelegateProvider - adds resourcewhitelist
 * @FeatureDomain                Configuration
 * @FeatureResult                updates $sceDelegateProvider
 * @FeatureKeywords              GUI Configuration
 * @param $sceDelegateProvider   the $sceDelegateProvider to change the resource-whitelist...
 */
yaioApp.config(function($sceDelegateProvider) {
    'use strict';

    var resBaseUrl = yaioAppBase.config.resBaseUrl;
    var whitelist = [
        // Allow same origin resource loads.
        'self',
        // Allow loading from our assets domain.  Notice the difference between * and **.
        resBaseUrl + '**'];
    
    // configure additional resBaseUrls for CORS
    if (yaioAppBase.config.addResBaseUrls && yaioAppBase.config.addResBaseUrls.length  > 0) {
        for (var idx = 0; idx < yaioAppBase.config.addResBaseUrls.length; idx++) {
            whitelist.push(yaioAppBase.config.addResBaseUrls[idx] + '/**');
        }
    }
    
    if (resBaseUrl && resBaseUrl !== '') {
        $sceDelegateProvider.resourceUrlWhitelist(whitelist);
    }
});
    
/** 
 * configures $httpProvider - adds default-headers for patch-requests
 * @FeatureDomain                Configuration
 * @FeatureResult                updates $httpProvider
 * @FeatureKeywords              GUI Configuration
 * @param $httpProvider          the $httpProvider to change the default-headers
 */
yaioApp.config(['$httpProvider', function($httpProvider) {
    'use strict';

    $httpProvider.defaults.withCredentials = true;
    $httpProvider.defaults.headers.patch = {
        'Content-Type': 'application/json;charset=utf-8'
    };
}]);
