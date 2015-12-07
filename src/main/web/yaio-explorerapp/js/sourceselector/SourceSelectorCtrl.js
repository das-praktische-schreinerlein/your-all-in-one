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
 * the controller to load the sourceselector
 * @FeatureDomain                Configuration
 * @FeatureResult                returns new controller
 * @FeatureKeywords              GUI Configuration
 */
yaioApp.controller('SourceSelectorCtrl', function($rootScope, $scope, $location, $routeParams, yaioUtils) {
    'use strict';

    // include utils
    $scope.yaioUtils = yaioUtils;
    
    $scope.switchDataSource = function(datasourceKey) {
        yaioUtils.getAppBase().configureService("YaioNodeData", function() { return yaioAppBase.get(datasourceKey);});
        yaioUtils.getAppBase().configureService("YaioAccessManager", function() { return yaioAppBase.get("YaioNodeData").getAccessManager(); });
        
        // load data and open frontpage if succeed
        yaioUtils.getAppBase().get("YaioNodeData").connectService().then(function success() {
            console.error("success connectService:" + yaioUtils.getConfig().appFrontpageUrl);
            $location.path(yaioUtils.getConfig().appFrontpageUrl + "?" + new Date());
            yaioAppBase.get("Angular.$location").path(yaioAppBase.config.appFrontpageUrl + "?" + new Date());
            console.error("success connectService done:" + yaioUtils.getConfig().appFrontpageUrl);
        });
    };
    
    var dataSources = yaioUtils.getConfig().datasources;
    var dataSourceName = $routeParams.newds;
    if (dataSources.indexOf(dataSourceName) >= 0) {
        $scope.switchDataSource(dataSourceName);
    }
});
