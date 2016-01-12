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

/* require '/js/highlightjs' */
/* require '/js/jquery' */
/* require '/js/marked' */
/* require '/freemind-flash' */

/** 
 * servicefunctions for formatting (markdown, diagramms, mindmaps..)
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
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


    /**
     * executes mermaid, highlight and checklist-formatter on the block,
     * return a flag if a mermaid-block is found an mermaid should be executed
     * @FeatureDomain                GUI
     * @FeatureResult                returnValue Boolean - flag if a Mermaid-Block is found and mermaid should be executed
     * @FeatureKeywords              Convert
     * @param descBlock              id-filter to identify the block to format
     * @return                       {boolean} - flag if a Mermaid-Block is found and mermaid should be executed
     */
    me.formatDescBlock = function(descBlock) {
        var content, url;
        var yaioAppBaseVarName = me.appBase.config.appBaseVarName;
        var flgDoMermaid = false;
        var descBlockId = me.$(descBlock).attr('id');

        var svcSyntaxHighlighterParser = me.appBase.get('SyntaxHighlighterParser');
        var svcChecklistParser = me.appBase.get('ChecklistParser');
        var svcMermaidParser = me.appBase.get('MermaidParser');
        var svcMindmapParser = me.appBase.get('MindmapParser');
        var svcPlantumlParser = me.appBase.get('PlantumlParser');

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
                        +   " onclick=\"javascript: " + yaioAppBaseVarName + ".get('FileUtils').downloadAsFile(" 
                        +       yaioAppBaseVarName + ".$('#linkdownload" + blockId + "'), "
                        +       yaioAppBaseVarName + ".$('#" + blockId + "').html(), 'diagram.svg', 'image/svg+xml', 'utf-8'); return true;\">"
                        + "Download</a>");
                flgDoMermaid = true;
            } else {
                // do highlight
                console.log("formatDescBlock highlight descBlock: " + descBlockId + " block: " + blockId);
                svcSyntaxHighlighterParser.renderBlock(block);
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
                        +   " onclick=\"javascript: " + yaioAppBaseVarName + ".get('FileUtils').downloadAsFile(" 
                                + yaioAppBaseVarName + ".$('#linkdownload" + blockId + "'), " 
                                + yaioAppBaseVarName + ".$('#" + blockId + "').html(), 'diagram.svg', 'image/svg+xml', 'utf-8'); return true;\">"
                        + "Download</a>");
                flgDoMermaid = true;
            } else if (me.$(block).hasClass("lang-yaiomindmap") || me.$(block).hasClass("yaiomindmap")) {
                // mindmap: no highlight
                console.log("formatDescBlock yaiomindmap for descBlock: " + descBlockId + " block: " + blockId);
                content = me.$(block).html();
                url = "/converters/mindmap?source=" + encodeURIComponent(content);
                me.addServicesToDiagrammBlock(block, 'yaiomindmap', "<a href='" + url + "' id='download" + blockId + "' target='_blank'>Download</a>");
                svcMindmapParser.renderBlock(block);
            } else if (me.$(block).hasClass("lang-yaioplantuml") || me.$(block).hasClass("yaioplantuml")) {
                // mindmap: no highlight
                console.log("formatDescBlock yaioplantuml for descBlock: " + descBlockId + " block: " + blockId);
                content = me.$(block).html();
                url = svcPlantumlParser.generatePlantuml(content);
                me.addServicesToDiagrammBlock(block, 'yaioplantuml', "<a href='" + url + "' id='download" + blockId + "' target='_blank'>Download</a>");
                svcPlantumlParser.renderBlock(block);
            }
        });
    
        // highlight checklist
        svcChecklistParser.parseBlock(descBlock);
        
        return flgDoMermaid;
    };

    /**
     * calls the global mermaid-formatter
     * @FeatureDomain                GUI
     * @FeatureResult                formats all divs with class=mermaid
     * @FeatureKeywords              Layout
     */
    me.formatMermaidGlobal = function() {
        return me.appBase.get('MermaidParser').formatMermaidGlobal();
    };

    /**
     * format the descText as Markdown
     * @FeatureDomain                GUI
     * @FeatureResult                returnValue String - formatted markdown
     * @FeatureKeywords              Layout
     * @param descText               the string to format
     * @param flgHighlightNow        if is set do syntax-highlighting while markdown-processing, if not set do it later
     * @param headerPrefix           headerPrefix for heading-ids
     * @return                       {String} - formatted markdown
     */
    me.formatMarkdown = function(descText, flgHighlightNow, headerPrefix) {
        var svcYaioMarkdownRenderer = me.appBase.get('YaioMarkdownRenderer');

        // prepare descText
        descText = svcYaioMarkdownRenderer.prepareTextForMarkdown(descText);

        return svcYaioMarkdownRenderer.formatMarkdown(descText, flgHighlightNow, headerPrefix);
    };

    me.addServicesToDiagrammBlock = function(block, type, downloadLink) {
        var content = me.$(block).html();
        var blockId = me.$(block).attr('id');
        var yaioAppBaseVarName = me.appBase.config.appBaseVarName;

        // add source
        me.$(block).before("<div class='" + type + "-source' id='fallback" + blockId + "'>"
            + "<pre>" + content + "</pre></div>");
        // add service-links
        me.$("#fallback" + blockId).before(
            "<div class='services" + type + "' id='services" + blockId + "'><div>"
            + downloadLink
            + "<a href='#' style='display: none;' id='toggleorig" + blockId + "' "
            +     "onclick=\"" + yaioAppBaseVarName +".get('YaioBase').toggleWithLinks('#toggleorig" + blockId + "', '#togglesource" + blockId + "', '#" + blockId + "', '#fallback" + blockId + "'); return false;\" target='_blank'>Diagramm</a>"
            + "<a href='#' id='togglesource" + blockId + "' "
            +     "onclick=\"" + yaioAppBaseVarName +".get('YaioBase').toggleWithLinks('#toggleorig" + blockId + "', '#togglesource" + blockId + "', '#" + blockId + "', '#fallback" + blockId + "'); return false;\" target='_blank'>Source</a>"
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
