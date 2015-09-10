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
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     servicefunctions basics
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
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
    
     me.showModalErrorMessage = function(message) {
         // set messagetext
         me.$( "#error-message-text" ).html(message);
         
         // show message
         me.$( "#error-message" ).dialog({
             modal: true,
             buttons: {
               Ok: function() {
                 me.$( this ).dialog( "close" );
               }
             }
         });    
     };
    
     me.showModalConfirmDialog = function(message, yesHandler, noHandler) {
         // set messagetext
         me.$( "#dialog-confirm-text" ).html(message);
         
         // show message
         
         me.$( "#dialog-confirm" ).dialog({
             resizable: false,
             height:140,
             modal: true,
             buttons: {
               "Ja": function() {
                 me.$( this ).dialog( "close" );
                 if (yesHandler) {
                     yesHandler();
                 }
               },
               "Abbrechen": function() {
                 me.$( this ).dialog( "close" );
                 if (noHandler) {
                     noHandler();
                 }
               }
             }
         });
     };
    
    
    /*****************************************
     *****************************************
     * Service-Funktions (logging)
     *****************************************
     *****************************************/
     me.showToastMessage = function(type, title, message) {
         // show message
         toastr.options = {
                 "closeButton": true,
                 "debug": false,
                 "newestOnTop": true,
                 "progressBar": true,
                 "positionClass": "toast-top-right",
                 "preventDuplicates": false,
                 "showDuration": "300",
                 "hideDuration": "1000",
                 "timeOut": "10000",
                 "extendedTimeOut": "1000",
                 "showEasing": "swing",
                 "hideEasing": "linear",
                 "showMethod": "fadeIn",
                 "hideMethod": "fadeOut"
         };
         toastr[type](me.htmlEscapeText(message), title);
     };
    
    
    
    me.logError = function(message, flgShowDialog) {
        console.error(message);
        if (flgShowDialog) {
            me.showToastMessage("error", "Oops! Ein Fehlerchen :-(", me.htmlEscapeText(message));
    //        me.showModalErrorMessage(me.htmlEscapeText(message));
        }
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
    
    me.createXFrameAllowFrom = function() {
        return "x-frames-allow-from=" + window.location.hostname;
    };
    
    me.escapeRegExp = function(str) {
        return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
    };
    
    me.getURLParameter = function(name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null
    }
    
    me._init();
    
    return me;
};