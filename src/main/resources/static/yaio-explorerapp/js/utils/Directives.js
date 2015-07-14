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
'use strict';

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     adds the new tag-directive "state" to output formated node-state
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new directive
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary
 */
yaioM.directive('state', function(){
    return {
        restrict: 'E', // own tag
        scope: {
            value: '='
        },
        template: '<span ng-show="value == \'RUNNING\'">laufend</span>' +
                  '<span ng-show="value != \'RUNNING\'">{{value}}</span>'
     };
})
