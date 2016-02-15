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
 * configures $translateProvider - international app
 * @FeatureDomain                Configuration
 * @FeatureResult                updates $translateProvider
 * @FeatureKeywords              GUI Configuration
 * @param $translateProvider     the $translateProvider to get text-resources
 */
yaioApp.config(function ($translateProvider) {
    'use strict';

    $translateProvider.translations();
    
    $translateProvider.useStaticFilesLoader({
        prefix: yaioAppBase.config.resBaseUrl + 'lang/lang-',
        suffix: '.json'
      });

    // default-language
    var langKey = 'de';
    
    // init
    $translateProvider.preferredLanguage(langKey);
    yaioAppBase.get('YaioLayout').initLanguageSupport(langKey);
    $translateProvider.currentLanguageKey = langKey;
    

    // change icons
    $('.button-lang').removeClass('button-lang-active').addClass('button-lang-inactive');
    $('#button_lang_' + langKey).removeClass('button-lang-inactive').addClass('button-lang-active');
});
