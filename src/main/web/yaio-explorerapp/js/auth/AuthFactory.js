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
 * the factory to check the authorization
 * @FeatureDomain                Configuration
 * @FeatureResult                returns new authorisation-obj
 * @FeatureKeywords              GUI Configuration
 */
yaioApp.factory('authorization', function ($rootScope, yaioUtils) {
    'use strict';

    return {
        /**
         * check if user is authentificated: sets $rootScope.authenticated and calls callback
         * @param {function} callback    callback to call after check
         */
        authentificate: function(callback) {
            yaioUtils.getService('YaioNodeData').checkSession().success(function(data) {
                console.log('authentificate: success ' + data);
                if (data) {
                    $rootScope.authenticated = true;
                } else {
                    $rootScope.authenticated = false;
                }
                if (callback) {
                    callback();
                }
            }).error(function(data) {
                console.log('authentificate: error ' + data);
                $rootScope.authenticated = false;
                if (callback) {
                    callback();
                }
            });
        }
    };
});
