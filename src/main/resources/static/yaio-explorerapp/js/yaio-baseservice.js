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

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     init the multilanguage support for all tags wirth attribute <XX lang="de">
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>GUI-result: init multilanguage-support
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Editor Multilanguagesupport
 */
function initLanguageSupport() {
    // Create language switcher instance and set default language to tech
    window.lang = new Lang('tech');

    //Define the de language pack as a dynamic pack to be loaded on demand
    //if the user asks to change to that language. We pass the two-letter language
    //code and the path to the language pack js file
    window.lang.dynamic('de', 'lang/lang-tech-to-de.json');
    window.lang.loadPack('de');

    // change to de
    window.lang.change('de');
}

function setupAppSize() {
    var height = window.innerHeight;
    var width = window.innerWidth;
    
    // YAIO-editor
    var ele = $("#containerBoxYaioEditor");
    if (ele.length > 0) {
        // we are relative to the tree
        var paddingToHead = $("#containerYaioTree").position().top;
        var left = $("#containerYaioTree").position().left + $("#containerYaioTree").width + 2;

        // set posTop as scrollTop burt never < paddingToHead
        var posTop = $(window).scrollTop();
        if (posTop < paddingToHead) {
            posTop = paddingToHead;
        }
        
        // calc maxHeight = windHeight - 20 (puffer)
        var maxHeight = height - 20;
        // sub topPos - Scollpos
        maxHeight = maxHeight - (posTop - $(window).scrollTop());

        // set values
        $(ele).css("position", "absolute");
        $(ele).css("max-height", maxHeight);
        $(ele).css("top", posTop);
        $(ele).css("left", left);
        
        console.log("setup size containerBoxYaioEditor width:" + window.innerWidth 
                + " height:" + window.innerHeight 
                + " scrollTop:" + $(window).scrollTop()
                + " offset.top" + $(ele).offset().top
                + " top:" + posTop
                + " max-height:" + $(ele).css("max-height")
                );
    }
    
    // Export-editor
    ele = $("#containerFormYaioEditorOutputOptions");
    if (ele.length > 0) {
        $(ele).css("max-height", height-$(ele).offset().top);
        console.log("setup size containerFormYaioEditorOutputOptions width:" + window.innerWidth 
                + " height:" + window.innerHeight 
                + " scrollTop:" + $(window).scrollTop()
                + " offset.top" + $(ele).offset().top
                + " max-height:" + $(ele).css("max-height")
                );
    }
    // Import-editor
     ele = $("#containerFormYaioEditorImport");
    if (ele.length > 0) {
        $(ele).css("max-height", height-$(ele).offset().top);
        console.log("setup size containerFormYaioEditorImport width:" + window.innerWidth 
                + " height:" + window.innerHeight 
                + " scrollTop:" + $(window).scrollTop()
                + " offset.top" + $(ele).offset().top
                + " max-height:" + $(ele).css("max-height")
                );
    }

}

function yaioShowHelpSite(url) {
    // set messagetext
    console.log("yaioShowHelpSite:" + url);
    $("#help-iframe").attr('src',url);
    
    // show message
    $( "#help-box" ).dialog({
        modal: true,
        width: "800px",
        buttons: {
          "Schliessen": function() {
            $( this ).dialog( "close" );
          },
          "Eigenes Fenster": function() {
              var helpFenster = window.open(url, "help", "width=750,height=500,scrollbars=yes,resizable=yes");
              helpFenster.focus();
              $( this ).dialog( "close" );
            }
        }
    });    
}

/*****************************************
 *****************************************
 * Service-Funktions (logging)
 *****************************************
 *****************************************/


function logError(message, flgShowDialog) {
    console.error(message);
    if (flgShowDialog) {
        showModalErrorMessage(htmlEscapeText(message));
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
    var flgSafeMode = 1;
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
           $(popup.document.body).html(data);
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
