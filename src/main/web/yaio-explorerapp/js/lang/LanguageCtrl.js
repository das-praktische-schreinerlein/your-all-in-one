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
 *     the controller to change language
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 */
yaioApp.controller('LanguageCtrl', ['$translate', '$scope', function ($translate, $scope, yaioUtils) {
    'use strict';

    // include utils
    $scope.yaioUtils = yaioUtils;

    // define languageutils
    $scope.currentLanguageKey = $translate.currentLanguageKey;
    $scope.changeLanguage = function (langKey) {
        // change angularTranslate
        $translate.use(langKey);
        
        // change other languagetranslator
        window.lang.change(langKey);
        
        $scope.currentLanguageKey = langKey;
        
        // change icons
        $(".button-lang").removeClass("button-lang-active").addClass("button-lang-inactive");
        $("#button_lang_" + langKey).removeClass("button-lang-inactive").addClass("button-lang-active");
    };
}]);
