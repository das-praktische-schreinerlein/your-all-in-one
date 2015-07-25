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

JsHelferlein.CookieHelperService = function(appBase) {
    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    }

    /**
     * Cookie erzeugen
     * @param Name
     * @param Wert
     * @param Gueltigkeit in Tagen
     */
    me.writeCookie = function(name,value,days, path) {
        // wie lange gueltig??
        if (days) {
            var date = new Date();
            date.setTime(date.getTime()+(days*24*60*60*1000));
            var expires = "; expires="+date.toGMTString();
        } else {
            expires = "";
        }
        // Coockie setzen
        document.cookie = name+"="+value+expires+"; path=/";
    };

    /**
     * Cookie einlesen
     * @param: Name des Coockies
     * @return Wert des Coockies
     */
    me.readCookie = function(name) {
        // Vollen Namen mit = suchen
        var nameFull = name + "=";
        var cookie = document.cookie;
        var ca = cookie.split(';');
        if (ca.length == 0) {
            ca = [cookie];
        }
        for(var i=0;i < ca.length;i++) {
            var c = ca[i];

            // Leerzechen entfernen
            while (c.charAt(0)==' ') {
                c = c.substring(1,c.length);
            }

            // Value extrahieren
            if (c.indexOf(nameFull) == 0) {
                return c.substring(nameFull.length,c.length);
            }
        }
        return null;
    };

    // init all
    me._init();

    return me;
};