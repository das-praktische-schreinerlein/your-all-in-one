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
 *     the controller to load the sourceselector
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 */
yaioApp.controller('SourceSelectorCtrl', function($rootScope, $scope, $location, yaioUtils) {
    'use strict';

    // include utils
    $scope.yaioUtils = yaioUtils;
    
    $scope.switchDataSource = function(datasourceKey) {
        yaioUtils.getAppBase().configureService("YaioNodeData", function() { return yaioAppBase.get(datasourceKey);});
        yaioUtils.getAppBase().configureService("YaioAccessManager", function() { return yaioAppBase.get("YaioNodeData").getAccessManager(); });
        
        // load data and open frontpage if succeed
        yaioUtils.getAppBase().get("YaioNodeData").loadStaticJson().then(function success() {
            console.log("success loadStaticJson");
            $location.path(yaioUtils.getConfig().appFrontpageUrl);
            yaioAppBase.get("Angular.$location").path(yaioAppBase.config.appFrontpageUrl);
            console.log("success loadStaticJson done:" + yaioUtils.getConfig().appFrontpageUrl);
        });
    }
});
