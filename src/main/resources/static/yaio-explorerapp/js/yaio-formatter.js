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

var localHtmlId = 1;

// states
var checkListConfigs = {
    "checklist-state-OPEN": {
        styleClassPraefix: "checklist-state-",
        matchers: ["OPEN", "OFFEN", "o", "O", "0", "TODO"]
    },
    "checklist-state-RUNNING": {
        styleClassPraefix: "checklist-state-",
        matchers: ["RUNNING"]
    },
    "checklist-state-LATE": {
        styleClassPraefix: "checklist-state-",
        matchers: ["LATE"]
    },
    "checklist-state-BLOCKED": {
        styleClassPraefix: "checklist-state-",
        matchers: ["BLOCKED", "WAITING", "WAIT"]
    },
    "checklist-state-WARNING": {
        styleClassPraefix: "checklist-state-",
        matchers: ["WARNING"]
    },
    "checklist-state-DONE": {
        styleClassPraefix: "checklist-state-",
        matchers: ["DONE", "OK", "x", "X", "ERLEDIGT"]
    },
    "checklist-test-TESTOPEN": {
        styleClassPraefix: "checklist-test-",
        matchers: ["TESTOPEN"]
    },
    "checklist-test-PASSED": {
        styleClassPraefix: "checklist-test-",
        matchers: ["PASSED"]
    },
    "checklist-test-FAILED": {
        styleClassPraefix: "checklist-test-",
        matchers: ["FAILED", "ERROR"]
    }
};

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
 * @param flgHighlightNow - if is set do syntax-highlighting while markdown-processing, if not set do it later
 * @param headerPrefix - headerPrefix for heading-ids
 * @return {String} - formatted markdown
 */
function formatMarkdown(descText, flgHighlightNow, headerPrefix) {
    // prepare descText
    descText = prepareTextForMarkdown(descText);
    
    var renderer = new marked.Renderer();
    // my own code-handler
    renderer.code = function (code, language) {
        code = yaioAppBase.get('YaioBaseService').htmlEscapeTextLazy(code);
        if (code.match(/^sequenceDiagram/) || code.match(/^graph/) || code.match(/^gantt/)) {
            return '<div id="inlineMermaid' + (localHtmlId++) + '" class="mermaid">'+ prepareTextForMermaid(code ) + '</div>';
        } else if (language !== undefined 
                   && (language.match(/^yaiomindmap/) || language.match(/^yaiofreemind/))) {
            return '<div id="inlineMindmap' + (localHtmlId++) + '"  class="yaiomindmap">'+ code + '</div>';
        } else {
            return '<pre><code id="inlineCode' + (localHtmlId++) + '" class="lang-' + language + '">' + code + '</code></pre>';
        }
    };
    
    // my own heading-handler to be sure that the heading id is unique
    renderer.heading = function(text, level, raw) {
      return '<h' + level 
        + ' id="' + this.options.headerPrefix + '_' + (localHtmlId++) + '_' + raw.toLowerCase().replace(/[^\w]+/g, '-')
        + '">'
        + text
        + '</h' + level + '>\n';
    };
    
    // my own link: for yaio
    renderer.link = function(href, title, text) {
        var prot; 
        if (this.options.sanitize) {
          try {
            prot = decodeURIComponent(unescape(href))
              .replace(/[^\w:]/g, '')
              .toLowerCase();
          } catch (e) {
            return '';
          }
          if (prot && prot.indexOf('javascript:') === 0) {
            return '';
          }
          if (prot && prot.indexOf('yaio:') === 0) {
              href = href.substr(5);
              href="/yaio-explorerapp/yaio-explorerapp.html#/showByAllIds/" + href;
          }
        }
        var out = '<a href="' + href + '"';
        if (title) {
          out += ' title="' + title + '"';
        }
        out += '>' + text + '</a>';
        return out;
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
      smartypants: false,
      headerPrefix: headerPrefix
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
 * @param descText   the string to prepare
 * @return {String}  prepared text to format as markdown
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
        noCode = yaioAppBase.get('YaioBaseService').htmlEscapeTextLazy(noCode);
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
    noCode = yaioAppBase.get('YaioBaseService').htmlEscapeTextLazy(noCode);
    noCode = noCode.replace(/&lt;br&gt;/g, "<br>");
    
    // add rest to newDescText
    newDescText += noCode;
    
    return newDescText;
}


/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     format the block-content as mindmap. 
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
function formatYaioMindmap(block) {
    var content = $(block).html();
    var blockId = $(block).attr('id');
    var url = "/converters/mindmap?source=" + encodeURIComponent(content);
    console.log("formatYaioMindmap " + blockId + " url:" + url);
    
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
    fo.write(blockId);
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
        yaioAppBase.get('YaioBaseService').showToastMessage("error", "Oops! Ein Fehlerchen :-(", "Syntaxfehler bei Parsen des Diagrammcodes:" + err);
//        yaioAppBase.get('YaioBaseService').showModalErrorMessage(":" + err);
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
 * @param descText   the string to prepare
 * @return {String}  prepared text to format with mermaid
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
 * @param descText  the string to prepare
 * @return {String} markdown-text in jira-format
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
            tmpText = tmpText.replace(/\n    - /g, "\n-- ");
            tmpText = tmpText.replace(/\n        - /g, "\n--- ");
            tmpText = tmpText.replace(/\n            - /g, "\n---- ");

            // headings
            tmpText = tmpText.replace(/\n##### /g, "\nh5. ");
            tmpText = tmpText.replace(/\n#### /g, "\nh4. ");
            tmpText = tmpText.replace(/\n### /g, "\nh3. ");
            tmpText = tmpText.replace(/\n## /g, "\nh2. ");
            tmpText = tmpText.replace(/\n# /g, "\nh1. ");
            
            // delete dummy \n
            tmpText = tmpText.substr(1);
            newDescText += tmpText;
        } else {
            // code-block
            newDescText += '{code}' + tmpText + '{code}';
        }
    }
    
    return newDescText;
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     executes mermaid, highlight and checklist-formatter on the block,
 *     return a flag if a mermaid-block is found an mermaid should be executed
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returnValue Boolean - flag if a Mermaid-Block is found and mermaid should be executed
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Convert
 * @param descBlock - id-filter to identify the block to format
 * @return {boolean} - flag if a Mermaid-Block is found and mermaid should be executed
 */
function formatDescBlock(descBlock) {
    var flgDoMermaid = false;
    var descBlockId = $(descBlock).attr('id');

    console.log("formatDescBlock highlight for descBlock: " + descBlockId);
    // remove trigger-flag
    $(descBlock).removeClass('syntaxhighlighting-open');
    
    // higlight code-blocks
    $("#" + descBlockId + " code").each(function(i, block) {
        var blockId = $(block).attr('id');
        if ($(block).hasClass("lang-mermaid") || $(block).hasClass("mermaid")) {
            // mermaid: no highlight
            console.log("formatDescBlock mermaid descBlock: " + descBlockId + " block: " + blockId);
            addServicesToDiagrammBlock(block, 'mermaid',
                    "<a href='' id='linkdownload" + blockId + "'  target='_blank'"
                    +   " onclick=\"javascript: yaioAppBase.get('YaioBaseService').downloadAsFile($('#linkdownload" + blockId + "'), $('#" + blockId + "').html(), 'diagram.svg', 'image/svg+xml', 'utf-8'); return true;\">"
                    + "Download</a>");
            flgDoMermaid = true;
        } else {
            // do highlight
            console.log("formatDescBlock highlight descBlock: " + descBlockId + " block: " + blockId);
            hljs.highlightBlock(block);
        }
    });

    // mermaid/mindmap div-blocks
    $("#" + descBlockId + " div").each(function(i, block) {
        var blockId = $(block).attr('id');
        if (   ($(block).hasClass("lang-mermaid") || $(block).hasClass("mermaid")) 
            && ! $(block).attr("data-processed")) {
            // mermaid: no highlight
            console.log("formatDescBlock mermaid descBlock: " + descBlockId + " block: " + blockId);
            addServicesToDiagrammBlock(block, 'mermaid',
                    "<a href='' id='linkdownload" + blockId + "'  target='_blank'"
                    +   " onclick=\"javascript: yaioAppBase.get('YaioBaseService').downloadAsFile($('#linkdownload" + blockId + "'), $('#" + blockId + "').html(), 'diagram.svg', 'image/svg+xml', 'utf-8'); return true;\">"
                    + "Download</a>");
            flgDoMermaid = true;
        } else if ($(block).hasClass("lang-yaiomindmap") || $(block).hasClass("yaiomindmap")) {
            // mindmap: no highlight
            console.log("formatDescBlock yaiomindmap for descBlock: " + descBlockId + " block: " + blockId);
            var content = $(block).html();
            var url = "/converters/mindmap?source=" + encodeURIComponent(content);
            addServicesToDiagrammBlock(block, 'yaiomindmap', "<a href='" + url + "' id='download" + blockId + "' target='_blank'>Download</a>");
            formatYaioMindmap(block);
        }
    });

    // highlight checklist
    highlightCheckList(descBlock);
    
    return flgDoMermaid;
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     executes checklist-formatter (add span with checklist-Styles) on the block [use checkListConfigs]
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>updates DOM
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Convert
 * @param descBlock - id-filter to identify the block to format
 */
function highlightCheckList(descBlock) {
    var descBlockId = $(descBlock).attr('id');
    console.log("highlightCheckList highlight for descBlock: " + descBlockId);

    // tests
    for (var idx in checkListConfigs) {
        var matchers = checkListConfigs[idx].matchers;
        highlightCheckListForMatchers(descBlock, matchers, idx, '');
    }
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     executes checklist-formatter (add span with checklistFormat) with style and styleclass for all matchers "[XXX]" on descBlock
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>updates DOM
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Convert
 * @param descBlock  - id-filter to identify the block to format
 * @param matchers   - list of matcher which will call as stringfilter of "[" + matcher + "]" to identify checklist-entry
 * @param styleClass - styleClass to add to span for matcher found 
 * @param style      - style to add to new span for matcher found
 */
function highlightCheckListForMatchers(descBlock, matchers, styleClass, style) {
    var descBlockId = $(descBlock).attr('id');
    console.log("highlightCheckListForMatchers matchers '" + matchers + "' for descBlock: " + descBlockId);
    for (var idx in matchers) {
        highlightCheckListForMatcher(descBlock, "[" + matchers[idx] + "]", styleClass, style);
    }
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     executes checklist-formatter (add span with checklistFormat) with style and styleclass for all matchers "[XXX]" on descBlock
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>updates DOM
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Convert
 * @param descBlock  - id-filter to identify the block to format
 * @param matcherStr - matcher will call as stringfilter to identify checklist-entry
 * @param styleClass - styleClass to add to span for matcher found 
 * @param style      - style to add to new span for matcher found
 */
function highlightCheckListForMatcher(descBlock, matcherStr, styleClass, style) {
    var descBlockId = $(descBlock).attr('id');
    console.log("highlightCheckListForMatcher matcherStr '" + matcherStr + "' for descBlock: " + descBlockId);
    $("#" + descBlockId + " li:contains('" + matcherStr + "'),h1:contains('" + matcherStr + "'),h2:contains('" + matcherStr + "')").each(function(index, value) {
        var regEx = RegExp(yaioAppBase.get('YaioBaseService').escapeRegExp(matcherStr), 'gi');
        findAndReplaceDOMText($(value).get(0), {
            find: regEx,
            replace: function(portion) {
                var el = document.createElement('span');
                if (style) {
                    el.style = style;
                }
                if (styleClass) {
                    el.className = styleClass;
                }
                el.innerHTML = portion.text;
                return el;
            }
        });
    });
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     extract data from explorerlines (table.fancytree-ext-table tr) and format 
 *     them as linked markdown-checklists ([state] - [title](yaio:number)
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>return String - checklist in yaio-markdown-format
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Convert
 * @return {String}  checklist in yaio-markdown-format
 */
function convertExplorerLinesAsCheckList() {
    // get title
    var title = $("#masterTr td.fieldtype_name").text();
    var now = yaioAppBase.get('YaioBaseService').formatGermanDateTime((new Date()).getTime());

    var checkList = "# Checklist: " + title + " (Stand: " + now + ")\n\n";
    
    // iterate all nodelines
    $("table.fancytree-ext-table tr").each(function(i, line) {
        // extract data
        var titleSpan = $(line).find("span.fancytree-title2");
        var stateSpan = $(line).find("span.fancytree-title-state");
        var numberSpan = $(line).find("div.field_metanummer");
        var levelSpan = $(line).find("span.fancytree-node");
        var istStandDiv = $(line).find("div.fieldtype_stand.field_istChildrenSumStand");
        var istAufwandDiv = $(line).find("div.fieldtype_aufwand.field_istChildrenSumAufwand");
        var planAufwandDiv = $(line).find("div.fieldtype_aufwand.field_planChildrenSumAufwand");
        
        // extract content
        var level = 0;
        var title = null;
        var state = null;
        var number = null;
        var istStand = "0%";
        var istAufwand = "0h";
        var planAufwand = null;
        if ($(levelSpan).size() > 0) {
            // extract level from intend
            var intend = $(levelSpan).css("margin-left").replace("px", "");
            level = parseInt(intend, 10) / 20;
        }
        if ($(stateSpan).size() > 0) {
            // extract state from style
            var idx = extractCheckListStatefromStateSpan($(stateSpan));
            if (idx) {
                state = idx;
                state = state.replace("checklist-state-", "");
                state = state.replace("checklist-test-", "");
            }
        }
        if ($(titleSpan).size() > 0) {
            title = $(titleSpan).text();
        }
        if ($(numberSpan).size() > 0) {
            number = $(numberSpan).text();
        }
        
        if ($(istAufwandDiv).size() > 0) {
            istAufwand = $(istAufwandDiv).text();
        }
        if ($(planAufwandDiv).size() > 0) {
            planAufwand = $(planAufwandDiv).text();
        }
        if ($(istStandDiv).size() > 0) {
            istStand = $(istStandDiv).text();
        }

        var stand = istStand.trim() + " (" + istAufwand.trim();
        if (planAufwand) {
            stand  += "/" + planAufwand.trim();
        }
        stand += ")";
        
        // if all set: generate checklist
        console.log("state:" + state + " title:" + title + " number:" + number + " level:" + level + " stand:" + stand);
        if (title && state && number) {
            for (var idx = 0; idx < level; idx ++) {
                checkList += "    ";
            }
            checkList += "- [" + state + "] - [" + title + "](yaio:" + number + ") " + stand + "\n";
        }
    });
    
    return checkList;
}

function extractCheckListStatefromStateSpan(block) {
    // iterate all configs
    for (var idx in checkListConfigs) {
        var matchers = checkListConfigs[idx].matchers;
        
        // iterate all matchers
        for (var idx2 in matchers) {
            // check for matcher in style
            if (block.hasClass("node-state-" + matchers[idx2])) {
                return idx;
            }
        }
    }
    return null;
}

/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     extract data from explorerlines (table.fancytree-ext-table tr) and format 
 *     them as mermaid-gantt-markdown
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>return String - mermaid-gantt-markdown
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Convert
 * @return {String}    mermaid-gantt-markdown
 */
function convertExplorerLinesAsGanttMarkdown() {
    // get title
    var title = $("#masterTr td.fieldtype_name").text();
    var now = yaioAppBase.get('YaioBaseService').formatGermanDateTime((new Date()).getTime());

    var ganttMarkdown = "# Gantt: " + title + " (Stand: " + now + ")\n\n"
        + "```mermaid\n"
        + "gantt\n"
        + "    title " + title + " (Stand: " + now + ")\n"
        + "    dateFormat  DD.MM.YYYY\n"
        + "\n";
    var ganttMarkdownPlan = "";
    var ganttMarkdownIst  = "";
    
    // iterate all nodelines
    $("table.fancytree-ext-table tr").each(function(i, line) {
        // extract data
        var titleSpan = $(line).find("span.fancytree-title2");
        var numberSpan = $(line).find("div.field_metanummer");
        var startEndPlanDiv = $(line).find("div.fieldtype_fromto.field_planChildrenSum");
        var startEndIstDiv = $(line).find("div.fieldtype_fromto.field_istChildrenSum");
        
        // extract content
        var title = null;
        var number = null;
        if ($(titleSpan).size() > 0) {
            title = $(titleSpan).text();
        }
        if ($(numberSpan).size() > 0) {
            number = $(numberSpan).text();
        }
        ganttMarkdownPlan += generateGanttMarkdownLineFromBlock(title, number, startEndPlanDiv);
        ganttMarkdownIst += generateGanttMarkdownLineFromBlock(title, number, startEndIstDiv);
    });

    // concat
    ganttMarkdownPlan = ganttMarkdownPlan.length > 0 ? "    section Plan\n" + ganttMarkdownPlan : "";
    ganttMarkdownIst = ganttMarkdownIst.length > 0 ? "    section Ist\n" + ganttMarkdownIst : "";
    ganttMarkdown += ganttMarkdownPlan + ganttMarkdownIst  + "```\n";
    
    return ganttMarkdown;
}


/**
 * <h4>FeatureDomain:</h4>
 *     GUI
 * <h4>FeatureDescription:</h4>
 *     generate a mermaid-gantt-markdown-line for selector (if start, end-date can be extracted)
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>return String - mermaid-gantt-markdown
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Convert
 * @param title          title of the line
 * @param number         referenc
 * @param selector       seletor to filter the element with jquery
 * @return {String}      mermaid-gantt-markdown-line
 */
function generateGanttMarkdownLineFromBlock(title, number, selector) {
    if ($(selector).size() > 0) {
        // extract dates
        var dates = $(selector).html().replace(/\&nbsp\;/g, ' ').split("-");
        if (dates.length != 2) {
            return "";
        }
        var start = dates[0];
        var end = dates[1];

        // if all set: generate gantt
        console.log("extractGanttMarkdownLineFromBlock: title:" + title + " number:" + number + " start:" + start + " end:" + end);
        if (title && number && start && end) {
            return "    " + title + ": " + number + ", " + start + ", " + end + "\n";
        }
    }
    return "";
}

function addServicesToDiagrammBlock(block, type, downloadLink) {
    var content = $(block).html();
    var blockId = $(block).attr('id');

    // add source
    $(block).before("<div class='" + type + "-source' id='fallback" + blockId + "'>"
            + "<pre>" + content + "</pre></div>");
    // add service-links
    $("#fallback" + blockId).before(
            "<div class='services" + type + "' id='services" + blockId + "'><div>"
            + downloadLink
            + "<a href='#' style='display: none;' id='toggleorig" + blockId + "' onclick=\"yaioAppBase.get('YaioBaseService').toggleWithLinks('#toggleorig" + blockId + "', '#togglesource" + blockId + "', '#" + blockId + "', '#fallback" + blockId + "'); return false;\" target='_blank'>Diagramm</a>"
            + "<a href='#' id='togglesource" + blockId + "' onclick=\"yaioAppBase.get('YaioBaseService').toggleWithLinks('#toggleorig" + blockId + "', '#togglesource" + blockId + "', '#" + blockId + "', '#fallback" + blockId + "'); return false;\" target='_blank'>Source</a>"
            + "</div></div>");
}


function addTOCForBlock(tocElement, srcElement, settings) {
    // add TOC
    settings = settings || { toc: {}};
    settings.toc = settings.toc || { };
    settings.toc.dest = $(tocElement);
    $.fn.toc($(srcElement), settings);
}
