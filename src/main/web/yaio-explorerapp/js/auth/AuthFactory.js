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
 *     the factory to check the authorization
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new authorisation-obj
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 */
yaioApp.factory('authorization', function ($rootScope, $http, yaioUtils) {
    'use strict';

    return {
        authentificate: function(callback) {
//            return yaioUtils.getService('YaioNodeData').yaioDoCheckUser()
//                .then(function success(data) {
//                        // handle success
//                        console.log("authentificate: success " + data);
//                        if (data) {
//                            $rootScope.authenticated = true;
//                        } else {
//                            $rootScope.authenticated = false;
//                        }
//                        callback && callback();
//                    }, function error(data) {
//                        // handle error
//                        console.log("authentificate: error " + data);
//                        $rootScope.authenticated = false;
//                        callback && callback();
//                });
            yaioUtils.getService('YaioNodeData').yaioDoCheckUser().success(function(data) {
                console.log("authentificate: success " + data);
                if (data) {
                    $rootScope.authenticated = true;
                } else {
                    $rootScope.authenticated = false;
                }
                callback && callback();
            }).error(function(data) {
                console.log("authentificate: error " + data);
                $rootScope.authenticated = false;
                callback && callback();
            });
        }
    };
});
