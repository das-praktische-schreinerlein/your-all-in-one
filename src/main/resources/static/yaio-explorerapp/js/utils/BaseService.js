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
 * Service-Funktions (layout)
 *****************************************
 *****************************************/
Yaio.BaseService = function(appBase) {
    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    }

    me.toggleWithLinks = function(link1, link2, id1, id2) {
         if ($(id1).css("display") != "none") {
             $(id1).css("display", "none");
             $(link1).css("display", "inline");
             $(id2).css("display", "block");
             $(link2).css("display", "none");
         } else {
             $(id2).css("display", "none");
             $(link2).css("display", "inline");
             $(id1).css("display", "block");
             $(link1).css("display", "none");
         }
         return false;     
     }
    
     me.showModalErrorMessage = function(message) {
         // set messagetext
         $( "#error-message-text" ).html(message);
         
         // show message
         $( "#error-message" ).dialog({
             modal: true,
             buttons: {
               Ok: function() {
                 $( this ).dialog( "close" );
               }
             }
         });    
     }
    
     me.showModalConfirmDialog = function(message, yesHandler, noHandler) {
         // set messagetext
         $( "#dialog-confirm-text" ).html(message);
         
         // show message
         
         $( "#dialog-confirm" ).dialog({
             resizable: false,
             height:140,
             modal: true,
             buttons: {
               "Ja": function() {
                 $( this ).dialog( "close" );
                 if (yesHandler) {
                     yesHandler();
                 }
               },
               "Abbrechen": function() {
                 $( this ).dialog( "close" );
                 if (noHandler) {
                     noHandler();
                 }
               }
             }
         });
     }
    
    
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
     }
    
    
    
    me.logError = function(message, flgShowDialog) {
        console.error(message);
        if (flgShowDialog) {
            me.showToastMessage("error", "Oops! Ein Fehlerchen :-(", me.htmlEscapeText(message));
    //        me.showModalErrorMessage(me.htmlEscapeText(message));
        }
    }
    
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
    }
    
    me.htmlEscapeTextLazy = function(text) {
        if ((text != "undefined") && (text != "") && (text != null)) {
            text = text.replace(/</g, "&lt;");
            text = text.replace(/>/g, "&gt;");
        }
        return text;
    }
    
    me.formatGermanDateTime = function(millis) {
        if (millis == null) {
           return "";
        }
        var date = new Date(millis);
        return me.padNumber(date.getDate(), 2)
            + "." + me.padNumber(date.getMonth() + 1, 2)
            + "." + date.getFullYear()
            + " " + me.padNumber(date.getHours(), 2)
            + ":" + me.padNumber(date.getMinutes(), 2);
    }
    me.formatGermanDate = function(millis) {
        if (millis == null) {
           return "";
        }
        var date = new Date(millis);
        return me.padNumber(date.getDate(), 2)
            + "." + me.padNumber(date.getMonth() + 1, 2)
            + "." + date.getFullYear();
    }
    me.padNumber = function(number, count) {
        var r = String(number);
        while ( r.length < count) {
        r = '0' + r;
        }
        return r;
    } 
    me.formatNumbers = function(number, nachkomma, suffix) {
       if (number == null) {
           return "";
       }
       
       return (number.toFixed(nachkomma)) + suffix;
    }
    
    me.downloadAsFile = function($link, data, fileName, mime, encoding) {
        if (mime == "undefind") {
            mime = "application/text";
        }
        if (encoding == "undefind") {
            mime = "uft-8";
        }
        // data URI
        var dataURI = 'data:' + mime + ';charset=' + encoding + ','
                + encodeURIComponent(data);
    
        // set link
        var flgSafeMode = 0;
        if (   (navigator.userAgent.indexOf("Trident") >= 0) 
            || (navigator.userAgent.indexOf("MSIE") >= 0)
            || flgSafeMode) {
           // IE or SafeMode
           var popup = window.open("");
           if (! popup) {
               // warn message
               me.logError("Leider kann der Download nicht angezeigt werden, da Ihr Popup-Blocker aktiv ist. Beachten Sie die Hinweise im Kopf des Browsers. ", true);
           } else {
               // set data to document
               $(popup.document.body).html("<pre>" + me.htmlEscapeTextLazy(data) + "</pre>");
           }
           return false;
       } else {
            // all expect IE
            $link.attr({
                'download' : fileName,
                'href' : dataURI,
                'target' : '_blank'
            });
       }
    }
    
    me.createXFrameAllowFrom = function() {
        return "x-frames-allow-from=" + window.location.hostname;
    }
    
    me.escapeRegExp = function(str) {
        return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
    };
    
    
    me._init();
    
    return me;
};