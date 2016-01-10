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
JsHelferlein.DataUtilsService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    /*****************************************
     *****************************************
     * Service-Funktions (data)
     *****************************************
     *****************************************/
    
    me.htmlEscapeText = function(text) {
        if ((text != "undefined") && (text != "") && (text != null)) {
            text = text.replace(/&/g, "&amp;");
            text = text.replace(/</g, "&lt;");
            text = text.replace(/>/g, "&gt;");
            text = text.replace(/"/g, "&quot;");
            text = text.replace(/'/g, "&#x27;");
            text = text.replace(/\//g, "&#x2F;");
        }
        return text;
    };
    
    me.htmlEscapeTextLazy = function(text) {
        if ((text != "undefined") && (text != "") && (text != null)) {
            text = text.replace(/</g, "&lt;");
            text = text.replace(/>/g, "&gt;");
        }
        return text;
    };
    
    me.formatGermanDateTime = function(millis) {
        if (millis == null || millis == "undefined" || millis == "") {
           return "";
        }
        var date = new Date(millis);
        return me.padNumber(date.getDate(), 2)
            + "." + me.padNumber(date.getMonth() + 1, 2)
            + "." + date.getFullYear()
            + " " + me.padNumber(date.getHours(), 2)
            + ":" + me.padNumber(date.getMinutes(), 2);
    };
    me.formatGermanDate = function(millis) {
        if (millis == null || millis == "undefined" || millis == "") {
           return "";
        }
        var date = new Date(millis);
        return me.padNumber(date.getDate(), 2)
            + "." + me.padNumber(date.getMonth() + 1, 2)
            + "." + date.getFullYear();
    };
    me.padNumber = function(number, count) {
        var r = String(number);
        while ( r.length < count) {
        r = '0' + r;
        }
        return r;
    };
    me.formatNumbers = function(number, nachkomma, suffix) {
       if (number == null || number == "undefined") {
           return "";
       }
       
       return (number.toFixed(nachkomma)) + suffix;
    };
    
    me.escapeRegExp = function(str) {
        return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
    };
    
    me.getURLParameter = function(name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
    };
    
    me.getBaseRefFromUrl = function(url) {
        var withoutAncor = url.split('#')[0];
        var withoutParams = withoutAncor.split('?')[0];
        var withoutFile = withoutParams.split('/');
        withoutFile.splice(-1, 1);
        return withoutFile.join('/');
    };
    
    me._init();
    
    return me;
};