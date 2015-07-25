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
 *     configures $translateProvider - international app
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>updates $translateProvider
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 * @param $translateProvider - the $translateProvider to get text-resources
 */
yaioApp.config(function ($translateProvider) {
    $translateProvider.translations();
    
    $translateProvider.useStaticFilesLoader({
        prefix: 'lang/lang-',
        suffix: '.json'
      });

    // default-language
    var langKey = 'de';
    
    // init
    $translateProvider.preferredLanguage(langKey);
    yaioAppBase.get('YaioLayout').initLanguageSupport(langKey);
    $translateProvider.currentLanguageKey = langKey;
    

    // change icons
    $(".button-lang").removeClass("button-lang-active").addClass("button-lang-inactive");
    $("#button_lang_" + langKey).removeClass("button-lang-inactive").addClass("button-lang-active");
});
