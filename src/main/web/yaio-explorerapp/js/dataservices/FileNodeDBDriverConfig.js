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

Yaio.FileNodeDBDriverConfig = function(urlBase, name, desc) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ConfigBase();
    
    me.urlBase                      = urlBase || window.location.host;
    me.name                         = name || ('Dateiupload');
    me.desc                         = desc || ('Daten werden aus einer Yaio-JSON-Datei geladen.');
    me.plantUmlBaseUrl              = 'http://www.plantuml.com/';
    me.excludeNodePraefix           = 'Sys* *Templ MyStart MyHelp JsUnitTest JsFuncTest JUnitTest';
    me.masterSysUId                 = 'MasterplanMasternode1';

    return me;
};