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

 function showModalErrorMessage(message) {
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

 function showModalConfirmDialog(message, yesHandler, noHandler) {
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
 function showToastMessage(type, title, message) {
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
     toastr[type](htmlEscapeText(message), title);
 }



function logError(message, flgShowDialog) {
    console.error(message);
    if (flgShowDialog) {
        showToastMessage("error", "Oops! Ein Fehlerchen :-(", htmlEscapeText(message));
//        showModalErrorMessage(htmlEscapeText(message));
    }
}

/*****************************************
 *****************************************
 * Service-Funktions (data)
 *****************************************
 *****************************************/

function htmlEscapeText(text) {
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

function htmlEscapeTextLazy(text) {
    if ((text != "undefined") && (text != "") && (text != null)) {
        text = text.replace(/</g, "&lt;");
        text = text.replace(/>/g, "&gt;");
    }
    return text;
}

function formatGermanDateTime(millis) {
    if (millis == null) {
       return "";
    }
    var date = new Date(millis);
    return padNumber(date.getDate(), 2)
        + "." + padNumber(date.getMonth() + 1, 2)
        + "." + date.getFullYear()
        + " " + padNumber(date.getHours(), 2)
        + ":" + padNumber(date.getMinutes(), 2);
}
function formatGermanDate(millis) {
    if (millis == null) {
       return "";
    }
    var date = new Date(millis);
    return padNumber(date.getDate(), 2)
        + "." + padNumber(date.getMonth() + 1, 2)
        + "." + date.getFullYear();
}
function padNumber(number, count) {
    var r = String(number);
    while ( r.length < count) {
    r = '0' + r;
    }
    return r;
} 
function formatNumbers(number, nachkomma, suffix) {
   if (number == null) {
       return "";
   }
   
   return (number.toFixed(nachkomma)) + suffix;
}

 function downloadAsFile($link, data, fileName, mime, encoding) {
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
           logError("Leider kann der Download nicht angezeigt werden, da Ihr Popup-Blocker aktiv ist. Beachten Sie die Hinweise im Kopf des Browsers. ", true);
       } else {
           // set data to document
           $(popup.document.body).html("<pre>" + htmlEscapeTextLazy(data) + "</pre>");
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

function createXFrameAllowFrom() {
    return "x-frames-allow-from=" + window.location.hostname;
}
