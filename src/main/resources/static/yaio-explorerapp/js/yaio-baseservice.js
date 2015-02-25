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
 * @param langKey - key of the preferred-language
 */
function initLanguageSupport(langKey) {
    // Create language switcher instance and set default language to tech
    window.lang = new Lang('tech');

    //Define the de language pack as a dynamic pack to be loaded on demand
    //if the user asks to change to that language. We pass the two-letter language
    //code and the path to the language pack js file
    window.lang.dynamic('de', 'lang/lang-tech-to-de.json');
    window.lang.dynamic('en', 'lang/lang-tech-to-en.json');
    window.lang.loadPack('de');
    window.lang.loadPack('en');

    // change to de
    window.lang.change(langKey);
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

    // Frontpage
    ele = $("#front-content-intro");
    if (ele.length > 0) {
        var maxHeight = height-$(ele).offset().top;
        
        // sub todonextbox
        if ($('#box_todonext').length > 0 ) {
            if ($('#box_todonext').height > 0) {
                maxHeight = maxHeight - $('#box_todonext').height;
            } else {
                // sometime height is not set: then default
                maxHeight = maxHeight - 100;
            }
        }
        $(ele).css("max-height", maxHeight);
        console.log("setup size front-content-intro width:" + window.innerWidth 
                + " height:" + window.innerHeight 
                + " scrollTop:" + $(window).scrollTop()
                + " offset.top" + $(ele).offset().top
                + " max-height:" + $(ele).css("max-height")
                );
    }
}

function yaioShowHelpSite(url) {
    // set messagetext
    url += "?" + createXFrameAllowFrom();
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

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     format the descText as Markdown
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returnValue String - formatted markdown
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Layout
 * @param descText - the string to format
 * @param flgHighlightNow - if isd set do syntax-highlighting while markdown-processing, if not set do it later
 * @return - formatted markdown
 */
function formatMarkdown(descText, flgHighlightNow) {
    // prepare descText
    descText = prepareTextForMarkdown(descText);
    
    var renderer = new marked.Renderer();
    renderer.code = function (code, language) {
        code = htmlEscapeTextLazy(code);
        if(code.match(/^sequenceDiagram/)||code.match(/^graph/)){
            return '<div class="mermaid">'+ prepareTextForMermaid(code ) + '</div>';
        else{
            return '<pre><code class="lang-' + language + '">' + code + '</code></pre>';
        }
    };    
    
    // Marked
    marked.setOptions({
      renderer: renderer,
      gfm: true,
      tables: true,
      breaks: false,
      pedantic: false,
      sanitize: true,
      smartLists: true,
      smartypants: false
    });
    if (flgHighlightNow) {
        marked.setOptions({
            highlight: function (code) {
                return hljs.highlightAuto(code).value;
            }
        });
    }
    var descHtmlMarked = marked(descText);
    
    return descHtmlMarked;
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     prepare the text to format as markdown
 *     prefix empty lines inline code-segs (```) so that they will interpreted as codeline by markdown-parser
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returnValue String - prepared text
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Layout
 * @param descText - the string to prepare
 * @return - prpeared text to format as markdown
 */
function prepareTextForMarkdown(descText) {
    // prepare descText
    var noCode = "";
    var newDescText = '';
    var newDescTextRest = descText;
    var codeStart = newDescTextRest.indexOf("```");
    while (codeStart >= 0) {
        // splice start before ```and add to newDescText
        noCode = newDescTextRest.slice(0, codeStart + 3);
        
        // replace <> but prevent <br> in noCode
        noCode = htmlEscapeTextLazy(noCode);
        noCode = noCode.replace(/&lt;br&gt;/g, "<br>");
        newDescText += noCode;
        
        // extract code
        newDescTextRest = newDescTextRest.slice(codeStart + 3);
        var codeEnd = newDescTextRest.indexOf("```");
        if (codeEnd >= 0) {
            // splice all before ending ```
            var code = newDescTextRest.slice(0, codeEnd);
            newDescTextRest = newDescTextRest.slice(codeEnd);
            
            // replace empty lines in code
            code = code.replace(/\r\n/g, "\n");
            code = code.replace(/\n\r/g, "\n");
            code = code.replace(/\n[ \t]*\n/g, "\n.\n");
            code = code.replace(/\n\n/g, "\n.\n");
            
            // add code to newDescText
            newDescText += code;
            
            // extract ending ``` and add it to newDescText
            newDescText += newDescTextRest.slice(0, 3);
            newDescTextRest = newDescTextRest.slice(3);
        }
        codeStart = newDescTextRest.indexOf("```");
    }

    // replace <> but prevent <br> in noCode
    noCode = newDescTextRest;
    noCode = htmlEscapeTextLazy(noCode);
    noCode = noCode.replace(/&lt;br&gt;/g, "<br>");
    
    // add rest to newDescText
    newDescText += noCode;
    
    return newDescText;
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     calls the global mermaid-formatter
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>formats all divs with class=mermaid
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Layout
 */
function formatMermaidGlobal() {
    mermaid.parseError = function(err,hash){
        showModalErrorMessage("Mermaid-processing failed:" + err);
    };
    try {
        mermaid.init();
    } catch (ex) {
        console.error("formatMermaidGlobal error:" + ex);
    }
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     prepare the text to format as mermaid
 *     delete .
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returnValue String - prepared text
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Layout
 * @param descText - the string to prepare
 * @return - prpeared text to format with mermaid
 */
function prepareTextForMermaid(descText) {
    // prepare descText
    var newDescText = descText;
    newDescText = newDescText.replace(/\n\.\n/g, "\n\n");
    return newDescText;
}
    
    /**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     convert the markdown-text to jira-format
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returnValue String - jira-converted text
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Convert
 * @param descText - the string to prepare
 * @return - markdown-text in jira-format
 */
function convertMarkdownToJira(descText) {
    // prepare descText
    var newDescText = '';
    
    var separatedBlocks = descText.split("```");
    for (var i=0;i < separatedBlocks.length; i++) {
        var tmpText = separatedBlocks[i];
        if ((i % 2) == 0) {
            // text-block: do convert
            
            // add dummy \n
            tmpText = '\n' + tmpText;
            
            // lists
            tmpText = tmpText.replace(/\n  - /g, "\n-- ");
            tmpText = tmpText.replace(/\n    - /g, "\n--- ");
            tmpText = tmpText.replace(/\n      - /g, "\n---- ");

            // headings
            tmpText = tmpText.replace(/\n##### /g, "\nh5. ");
            tmpText = tmpText.replace(/\n#### /g, "\nh4. ");
            tmpText = tmpText.replace(/\n### /g, "\nh3. ");
            tmpText = tmpText.replace(/\n## /g, "\nh2. ");
            tmpText = tmpText.replace(/\n# /g, "\nh1. ");
            
            // delete dummy \n
            tmpText = tmpText.substr(1);;
            
            newDescText += tmpText;
        } else {
            // code-block
            newDescText += '{code}' + tmpText + '{code}';
        }
    }
    
    return newDescText;
}

function createXFrameAllowFrom() {
    return "x-frames-allow-from=" + window.location.hostname;
}
