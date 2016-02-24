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
 * the controller to change language
 * @FeatureDomain                Configuration
 * @FeatureResult                returns new controller
 * @FeatureKeywords              GUI Configuration
 */
yaioApp.controller('LanguageCtrl', ['$translate', '$scope', function ($translate, $scope, yaioUtils) {
    'use strict';

    /**
     * init the controller
     * @private
     */
    $scope._init = function () {
        // include utils
        $scope.yaioUtils = yaioUtils;

        // define languageutils
        $scope.currentLanguageKey = $translate.currentLanguageKey;
    };

    /**
     * switch current language to key (swictehs language-buttons)
     * @param {String} langKey    new langkey
     */
    $scope.changeLanguage = function (langKey) {
        // change angularTranslate
        $translate.use(langKey);
        
        // change other languagetranslator
        window.lang.change(langKey);
        
        $scope.currentLanguageKey = langKey;
        
        // change icons
        $('.button-lang').removeClass('button-lang-active').addClass('button-lang-inactive');
        $('#button_lang_' + langKey).removeClass('button-lang-inactive').addClass('button-lang-active');
    };

    // init
    $scope._init();
}]);
