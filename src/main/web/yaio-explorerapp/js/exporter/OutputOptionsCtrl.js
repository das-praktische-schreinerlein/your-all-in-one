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
 * the controller to load the outputoptions register the yaio-functions
 * @FeatureDomain                Configuration
 * @FeatureResult                returns new controller
 * @FeatureKeywords              GUI Configuration BusinessLogic
 */
yaioApp.controller('OutputOptionsCtrl', function($rootScope, $scope, $location, $routeParams,
                                                 setFormErrors, OutputOptionsEditor, yaioUtils) {
    'use strict';

    /**
     * init the controller
     * @private
     */
    $scope._init = function () {
        // include utils
        $scope.yaioUtils = yaioUtils;

        // register the editor
        $scope.outputOptionsEditor = OutputOptionsEditor;
        // create options
        $scope.oOptions = $scope.outputOptionsEditor.oOptions;

        console.log('OutputOptionsCtrl - started');
    };

    // init
    $scope._init();
});

