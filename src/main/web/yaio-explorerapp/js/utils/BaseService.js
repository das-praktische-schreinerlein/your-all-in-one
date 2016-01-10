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
 * servicefunctions basics
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

/*****************************************
 *****************************************
 * Service-Funktions (base)
 *****************************************
 *****************************************/
Yaio.BaseService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    me.toggleWithLinks = function(link1, link2, id1, id2) {
         if (me.$(id1).css("display") != "none") {
             me.$(id1).css("display", "none");
             me.$(link1).css("display", "inline");
             me.$(id2).css("display", "block");
             me.$(link2).css("display", "none");
         } else {
             me.$(id2).css("display", "none");
             me.$(link2).css("display", "inline");
             me.$(id1).css("display", "block");
             me.$(link1).css("display", "none");
         }
         return false;
     };
    
    me.createXFrameAllowFrom = function() {
        return "x-frames-allow-from=" + window.location.hostname;
    };

    me._init();

    return me;
};