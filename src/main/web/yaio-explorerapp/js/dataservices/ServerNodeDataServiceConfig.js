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

Yaio.ServerNodeDataServiceConfig = function(urlBase, name, desc) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ConfigBase();
    
    me.urlBase                      = urlBase || (window.location.protocol + "//" + window.location.hostname + (window.location.port ? ":" + window.location.port : ""));
    me.name                         = name || ("Server: " + window.location.host);
    me.desc                         = desc || ("Daten werden vom aktuellen Server " + window.location.host + " geladen.");
    me.configUrl                    = me.urlBase + "/apiconfig/commonApiConfig";

    /**
     * initialize the object
     */
    me._init = function() {
        me.updateConfig();
    }

    /**
     * initialize the object
     */
    me.updateConfig = function() {
        // REST login
        me.restLoginUrl                 = me.urlBase + "/login";
        me.restLogoutUrl                = me.urlBase + "/logout";
        me.restCheckUserUrl             = me.urlBase + "/user/current";
        
        // REST actions
        me.restBaseUrl                  = me.urlBase + "/nodes/";
        me.restShowUrl                  = me.restBaseUrl + "show/";
        me.restSymLinkUrl               = me.restBaseUrl + "showsymlink/";
        me.restUpdateUrl                = me.restBaseUrl + "update/";
        me.restCreateUrl                = me.restBaseUrl + "create/";
        me.restMoveUrl                  = me.restBaseUrl + "move/";
        me.restCopyUrl                  = me.restBaseUrl + "copy/";
        me.restRemoveUrl                = me.restBaseUrl + "delete/";
        me.restSearchUrl                = me.restBaseUrl + "search/";
        me.restExportsBaseUrl           = me.urlBase + "/exports/";
    }
    
    me._init();
    
    return me;
};