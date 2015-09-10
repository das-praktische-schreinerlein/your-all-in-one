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
Yaio.FormatterService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };


    me._localHtmlId = 1;
    
    // states
    me.checkListConfigs = {
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
    me.formatMarkdown = function(descText, flgHighlightNow, headerPrefix) {
        // prepare descText
        descText = me.prepareTextForMarkdown(descText);
        
        var renderer = new marked.Renderer();
        // my own code-handler
        renderer.code = function (code, language) {
            code = me.appBase.get('YaioBase').htmlEscapeTextLazy(code);
            if (code.match(/^sequenceDiagram/) || code.match(/^graph/) || code.match(/^gantt/)) {
                return '<div id="inlineMermaid' + (me._localHtmlId++) + '" class="mermaid">'+ me.prepareTextForMermaid(code ) + '</div>';
            } else if (language !== undefined 
                       && (language.match(/^yaiomindmap/) || language.match(/^yaiofreemind/))) {
                return '<div id="inlineMindmap' + (me._localHtmlId++) + '"  class="yaiomindmap">'+ code + '</div>';
            } else {
                return '<pre><code id="inlineCode' + (me._localHtmlId++) + '" class="lang-' + language + '">' + code + '</code></pre>';
            }
        };
        
        // my own heading-handler to be sure that the heading id is unique
        renderer.heading = function(text, level, raw) {
          return '<h' + level 
            + ' id="' + this.options.headerPrefix + '_' + (me._localHtmlId++) + '_' + raw.toLowerCase().replace(/[^\w]+/g, '-')
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
    };
    
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
    me.prepareTextForMarkdown = function(descText) {
        // prepare descText
        var noCode = "";
        var newDescText = '';
        var newDescTextRest = descText;
        var codeStart = newDescTextRest.indexOf("```");
        while (codeStart >= 0) {
            // splice start before ```and add to newDescText
            noCode = newDescTextRest.slice(0, codeStart + 3);
            
            // replace <> but prevent <br> in noCode
            noCode = me.appBase.get('YaioBase').htmlEscapeTextLazy(noCode);
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
        noCode = me.appBase.get('YaioBase').htmlEscapeTextLazy(noCode);
        noCode = noCode.replace(/&lt;br&gt;/g, "<br>");
        
        // add rest to newDescText
        newDescText += noCode;
        
        return newDescText;
    };
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     format the block-content as mindmap. 
     *     <ul>
     *     <li>creates a FlashObject /dist/vendors.vendorversion/freemind-flash/visorFreemind.swf
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
    me.formatYaioMindmap = function(block) {
        var content = me.$(block).html();
        var blockId = me.$(block).attr('id');
        var url = "/converters/mindmap?source=" + encodeURIComponent(content);
        console.log("formatYaioMindmap " + blockId + " url:" + url);
        
        var fo = new FlashObject("/dist/vendors.vendorversion/freemind-flash/visorFreemind.swf", "visorFreeMind", "100%", "100%", 6, "#9999ff");
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
    };
    
    
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
    me.formatMermaidGlobal = function() {
        mermaid.parseError = function(err,hash){
            me.appBase.get('YaioBase').showToastMessage("error", "Oops! Ein Fehlerchen :-(", "Syntaxfehler bei Parsen des Diagrammcodes:" + err);
    //        me.appBase.get('YaioBase').showModalErrorMessage(":" + err);
        };
        try {
            mermaid.init();
        } catch (ex) {
            console.error("formatMermaidGlobal error:" + ex);
        }
    };
    
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
    me.prepareTextForMermaid = function(descText) {
        // prepare descText
        var newDescText = descText;
        newDescText = newDescText.replace(/\n\.\n/g, "\n\n");
        return newDescText;
    };
        
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
    me.convertMarkdownToJira = function(descText) {
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
    };
    
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
    me.formatDescBlock = function(descBlock) {
        var flgDoMermaid = false;
        var descBlockId = me.$(descBlock).attr('id');
    
        console.log("formatDescBlock highlight for descBlock: " + descBlockId);
        // remove trigger-flag
        me.$(descBlock).removeClass('syntaxhighlighting-open');
        
        // higlight code-blocks
        me.$("#" + descBlockId + " code").each(function(i, block) {
            var blockId = me.$(block).attr('id');
            if (me.$(block).hasClass("lang-mermaid") || me.$(block).hasClass("mermaid")) {
                // mermaid: no highlight
                console.log("formatDescBlock mermaid descBlock: " + descBlockId + " block: " + blockId);
                me.addServicesToDiagrammBlock(block, 'mermaid',
                        "<a href='' id='linkdownload" + blockId + "'  target='_blank'"
                        +   " onclick=\"javascript: yaioAppBase.get('YaioFile').downloadAsFile(yaioAppBase.$('#linkdownload" + blockId + "'), yaioAppBase.$('#" + blockId + "').html(), 'diagram.svg', 'image/svg+xml', 'utf-8'); return true;\">"
                        + "Download</a>");
                flgDoMermaid = true;
            } else {
                // do highlight
                console.log("formatDescBlock highlight descBlock: " + descBlockId + " block: " + blockId);
                hljs.highlightBlock(block);
            }
        });
    
        // mermaid/mindmap div-blocks
        me.$("#" + descBlockId + " div").each(function(i, block) {
            var blockId = me.$(block).attr('id');
            if (   (me.$(block).hasClass("lang-mermaid") || me.$(block).hasClass("mermaid")) 
                && ! me.$(block).attr("data-processed")) {
                // mermaid: no highlight
                console.log("formatDescBlock mermaid descBlock: " + descBlockId + " block: " + blockId);
                me.addServicesToDiagrammBlock(block, 'mermaid',
                        "<a href='' id='linkdownload" + blockId + "'  target='_blank'"
                        +   " onclick=\"javascript: yaioAppBase.get('YaioFile').downloadAsFile(yaioAppBase.$('#linkdownload" + blockId + "'), yaioAppBase.$('#" + blockId + "').html(), 'diagram.svg', 'image/svg+xml', 'utf-8'); return true;\">"
                        + "Download</a>");
                flgDoMermaid = true;
            } else if (me.$(block).hasClass("lang-yaiomindmap") || me.$(block).hasClass("yaiomindmap")) {
                // mindmap: no highlight
                console.log("formatDescBlock yaiomindmap for descBlock: " + descBlockId + " block: " + blockId);
                var content = me.$(block).html();
                var url = "/converters/mindmap?source=" + encodeURIComponent(content);
                me.addServicesToDiagrammBlock(block, 'yaiomindmap', "<a href='" + url + "' id='download" + blockId + "' target='_blank'>Download</a>");
                me.formatYaioMindmap(block);
            }
        });
    
        // highlight checklist
        me.highlightCheckList(descBlock);
        
        return flgDoMermaid;
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     executes checklist-formatter (add span with checklist-Styles) on the block [use me.checkListConfigs]
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates DOM
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Convert
     * @param descBlock - id-filter to identify the block to format
     */
    me.highlightCheckList = function(descBlock) {
        var descBlockId = me.$(descBlock).attr('id');
        console.log("highlightCheckList highlight for descBlock: " + descBlockId);
    
        // tests
        for (var idx in me.checkListConfigs) {
            var matchers = me.checkListConfigs[idx].matchers;
            me.highlightCheckListForMatchers(descBlock, matchers, idx, '');
        }
    };
    
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
    me.highlightCheckListForMatchers = function(descBlock, matchers, styleClass, style) {
        var descBlockId = me.$(descBlock).attr('id');
        console.log("highlightCheckListForMatchers matchers '" + matchers + "' for descBlock: " + descBlockId);
        for (var idx in matchers) {
            me.highlightCheckListForMatcher(descBlock, "[" + matchers[idx] + "]", styleClass, style);
        }
    };
    
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
    me.highlightCheckListForMatcher = function(descBlock, matcherStr, styleClass, style) {
        var descBlockId = me.$(descBlock).attr('id');
        console.log("highlightCheckListForMatcher matcherStr '" + matcherStr + "' for descBlock: " + descBlockId);
        me.$("#" + descBlockId + " li:contains('" + matcherStr + "'),h1:contains('" + matcherStr + "'),h2:contains('" + matcherStr + "')").each(function(index, value) {
            var regEx = RegExp(me.appBase.get('YaioBase').escapeRegExp(matcherStr), 'gi');
            findAndReplaceDOMText(me.$(value).get(0), {
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
    };
    
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
    me.convertExplorerLinesAsCheckList = function() {
        // get title
        var title = me.$("#masterTr td.fieldtype_name").text();
        var now = me.appBase.get('YaioBase').formatGermanDateTime((new Date()).getTime());
    
        var checkList = "# Checklist: " + title + " (Stand: " + now + ")\n\n";
        
        // iterate all nodelines
        me.$("table.fancytree-ext-table tr").each(function(i, line) {
            // extract data
            var titleSpan = me.$(line).find("span.fancytree-title2");
            var stateSpan = me.$(line).find("span.fancytree-title-state");
            var numberSpan = me.$(line).find("div.field_metanummer");
            var levelSpan = me.$(line).find("span.fancytree-node");
            var istStandDiv = me.$(line).find("div.fieldtype_stand.field_istChildrenSumStand");
            var istAufwandDiv = me.$(line).find("div.fieldtype_aufwand.field_istChildrenSumAufwand");
            var planAufwandDiv = me.$(line).find("div.fieldtype_aufwand.field_planChildrenSumAufwand");
            
            // extract content
            var level = 0;
            var title = null;
            var state = null;
            var number = null;
            var istStand = "0%";
            var istAufwand = "0h";
            var planAufwand = null;
            if (me.$(levelSpan).size() > 0) {
                // extract level from intend
                var intend = me.$(levelSpan).css("margin-left").replace("px", "");
                level = parseInt(intend, 10) / 20;
            }
            if (me.$(stateSpan).size() > 0) {
                // extract state from style
                var idx = me.extractCheckListStatefromStateSpan(me.$(stateSpan));
                if (idx) {
                    state = idx;
                    state = state.replace("checklist-state-", "");
                    state = state.replace("checklist-test-", "");
                }
            }
            if (me.$(titleSpan).size() > 0) {
                title = me.$(titleSpan).text();
            }
            if (me.$(numberSpan).size() > 0) {
                number = me.$(numberSpan).text();
            }
            
            if (me.$(istAufwandDiv).size() > 0) {
                istAufwand = me.$(istAufwandDiv).text();
            }
            if (me.$(planAufwandDiv).size() > 0) {
                planAufwand = me.$(planAufwandDiv).text();
            }
            if (me.$(istStandDiv).size() > 0) {
                istStand = me.$(istStandDiv).text();
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
    };
    
    me.extractCheckListStatefromStateSpan = function(block) {
        // iterate all configs
        for (var idx in me.checkListConfigs) {
            var matchers = me.checkListConfigs[idx].matchers;
            
            // iterate all matchers
            for (var idx2 in matchers) {
                // check for matcher in style
                if (block.hasClass("node-state-" + matchers[idx2])) {
                    return idx;
                }
            }
        }
        return null;
    };
    
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
    me.convertExplorerLinesAsGanttMarkdown = function() {
        // get title
        var title = me.$("#masterTr td.fieldtype_name").text();
        var now = me.appBase.get('YaioBase').formatGermanDateTime((new Date()).getTime());
    
        var ganttMarkdown = "# Gantt: " + title + " (Stand: " + now + ")\n\n"
            + "```mermaid\n"
            + "gantt\n"
            + "    title " + title + " (Stand: " + now + ")\n"
            + "    dateFormat  DD.MM.YYYY\n"
            + "\n";
        var ganttMarkdownPlan = "";
        var ganttMarkdownIst  = "";
        
        // iterate all nodelines
        me.$("table.fancytree-ext-table tr").each(function(i, line) {
            // extract data
            var titleSpan = me.$(line).find("span.fancytree-title2");
            var numberSpan = me.$(line).find("div.field_metanummer");
            var startEndPlanDiv = me.$(line).find("div.fieldtype_fromto.field_planChildrenSum");
            var startEndIstDiv = me.$(line).find("div.fieldtype_fromto.field_istChildrenSum");
            
            // extract content
            var title = null;
            var number = null;
            if (me.$(titleSpan).size() > 0) {
                title = me.$(titleSpan).text();
            }
            if (me.$(numberSpan).size() > 0) {
                number = me.$(numberSpan).text();
            }
            ganttMarkdownPlan += me.generateGanttMarkdownLineFromBlock(title, number, startEndPlanDiv);
            ganttMarkdownIst += me.generateGanttMarkdownLineFromBlock(title, number, startEndIstDiv);
        });
    
        // concat
        ganttMarkdownPlan = ganttMarkdownPlan.length > 0 ? "    section Plan\n" + ganttMarkdownPlan : "";
        ganttMarkdownIst = ganttMarkdownIst.length > 0 ? "    section Ist\n" + ganttMarkdownIst : "";
        ganttMarkdown += ganttMarkdownPlan + ganttMarkdownIst  + "```\n";
        
        return ganttMarkdown;
    };
    
    
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
    me.generateGanttMarkdownLineFromBlock = function(title, number, selector) {
        if (me.$(selector).size() > 0) {
            // extract dates
            var dates = me.$(selector).html().replace(/\&nbsp\;/g, ' ').split("-");
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
    };
    
    me.addServicesToDiagrammBlock = function(block, type, downloadLink) {
        var content = me.$(block).html();
        var blockId = me.$(block).attr('id');
    
        // add source
        me.$(block).before("<div class='" + type + "-source' id='fallback" + blockId + "'>"
                + "<pre>" + content + "</pre></div>");
        // add service-links
        me.$("#fallback" + blockId).before(
                "<div class='services" + type + "' id='services" + blockId + "'><div>"
                + downloadLink
                + "<a href='#' style='display: none;' id='toggleorig" + blockId + "' onclick=\"yaioAppBase.get('YaioBase').toggleWithLinks('#toggleorig" + blockId + "', '#togglesource" + blockId + "', '#" + blockId + "', '#fallback" + blockId + "'); return false;\" target='_blank'>Diagramm</a>"
                + "<a href='#' id='togglesource" + blockId + "' onclick=\"yaioAppBase.get('YaioBase').toggleWithLinks('#toggleorig" + blockId + "', '#togglesource" + blockId + "', '#" + blockId + "', '#fallback" + blockId + "'); return false;\" target='_blank'>Source</a>"
                + "</div></div>");
    };
    
    
    me.addTOCForBlock = function(tocElement, srcElement, settings) {
        // add TOC
        settings = settings || { toc: {}};
        settings.toc = settings.toc || { };
        settings.toc.dest = me.$(tocElement);
        me.$.fn.toc(me.$(srcElement), settings);
    };
    
    me._init();
    
    return me;
};
