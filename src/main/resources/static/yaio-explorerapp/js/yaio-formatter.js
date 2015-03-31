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
/* require 'yaio-basesservice' */
/* require 'yaio-layout' */
/* require '/js/highlightjs' */
/* require '/js/jquery' */
/* require '/js/marked' */
/* require '/freemind-flash' */

/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     servicefunctions for formatting (markdown, diagramms, mindmaps..)
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */


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
        if (code.match(/^sequenceDiagram/)||code.match(/^graph/)) {
            return '<div id="inlineMermaid' + (localHtmlId++) + '" class="mermaid">'+ prepareTextForMermaid(code ) + '</div>';
        } else if (language !== undefined && language.match(/^yaiofreemind/)) {
            return '<div id="inlineFreemind' + (localHtmlId++) + '"  class="yaiofreemind">'+ code + '</div>';
        } else {
            return '<pre><code id="inlineCode' + (localHtmlId++) + '" class="lang-' + language + '">' + code + '</code></pre>';
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
 *     format the block-content as freemind. 
 *     <ul>
 *     <li>creates a FlashObject /freemind-flash/visorFreemind.swf
 *     <li>Calls /converters/mindmap with the html-content of the block
 *     <li>insert the returning flash-object into block-element 
 *     
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returnValue shows Freemind-Flashviewer with the mindmap-content of block
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Layout
 * @param block - jquery-html-element with the content to convert to mindmap 
 */
function formatYaioFreemind(block) {
    var content = $(block).html();
    var url = "/converters/mindmap?source=" + encodeURIComponent(content);
    console.log("formatYaioFreemind " + $(block).attr('id') + " url:" + url);
    
    var fo = new FlashObject("/freemind-flash/visorFreemind.swf", "visorFreeMind", "100%", "100%", 6, "#9999ff");
    fo.addParam("quality", "high");
    fo.addParam("bgcolor", "#a0a0f0");
    fo.addVariable("openUrl", "_blank");
    fo.addVariable("startCollapsedToLevel","10");
    fo.addVariable("maxNodeWidth","200");
    //
    fo.addVariable("mainNodeShape","elipse");
    fo.addVariable("justMap","false");
    
    fo.addVariable("initLoadFile",url);
    fo.addVariable("defaultToolTipWordWrap",200);
    fo.addVariable("offsetX","left");
    fo.addVariable("offsetY","top");
    fo.addVariable("buttonsPos","top");
    fo.addVariable("min_alpha_buttons",20);
    fo.addVariable("max_alpha_buttons",100);
    fo.addVariable("scaleTooltips","false");
    fo.write($(block).attr('id'));
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
        showToastMessage("error", "Oops! Ein Fehlerchen :-(", "Sytanxfehler bei Parsen des Diagrammcodes:" + err);
//        showModalErrorMessage(":" + err);
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

function formatDescBlock(descBlock) {
    var flgDoMermaid = false;

    console.log("formatDescBlock highlight for descBlock: " + $(descBlock).attr('id'));
    // remove trigger-flag
    $(descBlock).removeClass('syntaxhighlighting-open');
    
    // higlight code-blocks
    $("#" + $(descBlock).attr('id') + " code").each(function(i, block) {
        if ($(block).hasClass("lang-mermaid") || $(block).hasClass("mermaid")) {
            // mermaid: no highlight
            console.log("formatDescBlock preparemermaid descBlock: " + $(descBlock).attr('id') + " block: " + $(block).attr('id'));
            flgDoMermaid = true;
        } else {
            // do highlight
            console.log("formatDescBlock highlight descBlock: " + $(descBlock).attr('id') + " block: " + $(block).attr('id'));
            hljs.highlightBlock(block);
        }
    });

    // mermaid/freemind div-blocks
    $("#" + $(descBlock).attr('id') + " div").each(function(i, block) {
        if (   ($(block).hasClass("lang-mermaid") || $(block).hasClass("mermaid")) 
            && ! $(block).attr("data-processed")) {
            // mermaid: no highlight
            console.log("formatDescBlock mermaid descBlock: " + $(descBlock).attr('id') + " block: " + $(block).attr('id'));
            flgDoMermaid = true;
        } else if ($(block).hasClass("lang-yaiofreemind") || $(block).hasClass("yaiofreemind")) {
            // freemind: no highlight
            console.log("formatDescBlock yaiofreemind for descBlock: " + $(descBlock).attr('id') + " block: " + $(block).attr('id'));
            formatYaioFreemind(block);
        }
    });
    
    return flgDoMermaid;
}

