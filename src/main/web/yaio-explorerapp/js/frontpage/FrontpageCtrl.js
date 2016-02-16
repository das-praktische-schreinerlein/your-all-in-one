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
 * the controller to load the frontpage
 * @FeatureDomain                Configuration
 * @FeatureResult                returns new controller
 * @FeatureKeywords              GUI Configuration
 */
yaioApp.controller('FrontPageCtrl', function($rootScope, $scope, $location, $routeParams,
                                             setFormErrors, OutputOptionsEditor, authorization, yaioUtils) {
    'use strict';

    // include utils
    $scope.yaioUtils = yaioUtils;
    
    // set vars
    var nodeId = $routeParams.nodeId;
    if (yaioUtils.getService('DataUtils').isUndefinedStringValue(nodeId)) {
        nodeId = 'SysStart1';
    }
    console.log('FrontPageCtrl - processing nodeId=' + nodeId);
    
    $scope.frontpageNodeId = nodeId;
    
    // call authentificate 
    authorization.authentificate(function () {
        // check authentification
        if (! $rootScope.authenticated) {
            $location.path(yaioUtils.getConfig().appLoginUrl);
            $scope.error = false;
        } else {
            // load data
            $scope.frontPageUrl = yaioUtils.getService('YaioAccessManager').getAvailiableNodeAction('frontpagebaseurl', nodeId, false);
        }
    });
});
