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

Yaio.StaticNodeDBDriverConfig = function(urlBase, name, desc) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ConfigBase();
    
    me.urlBase                      = '';
    me.name                         = name || 'Statische InApp-Daten';
    me.desc                         = desc || 'Die statisch in der App hinterlegten Daten werden geladen.';
    me.plantUmlBaseUrl              = 'http://www.plantuml.com/';
    me.excludeNodePraefix           = 'Sys* *Templ MyStart MyHelp JsUnitTest JsFuncTest JUnitTest';
    me.masterSysUId                 = 'MasterplanMasternode1';

    return me;
};