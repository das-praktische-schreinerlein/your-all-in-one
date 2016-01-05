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
 * servicefunctions for markdown-rendering
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.MarkdownRendererService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };


    me._localHtmlId = 1;

    /**
     * calls the global mermaid-formatter
     * @FeatureDomain                GUI
     * @FeatureResult                formats all divs with class=mermaid
     * @FeatureKeywords              Layout
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
        // Marked
        me._configureMarked(flgHighlightNow, headerPrefix);
        var descHtmlMarked = marked(descText);

        return descHtmlMarked;
    };

    /**
     * prepare the text to format as markdown
     * prefix empty lines inline code-segs (```) so that they will interpreted as codeline by markdown-parser
     * @FeatureDomain                GUI
     * @FeatureResult                returnValue String - prepared text
     * @FeatureKeywords              Layout
     * @param descText               the string to prepare
     * @return                       {String}  prepared text to format as markdown
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
     * format the block-content as mindmap. 
     * <ul>
     * <li>creates a FlashObject /dist/vendors.vendorversion/freemind-flash/visorFreemind.swf
     * <li>Calls /converters/mindmap with the html-content of the block
     * <li>insert the returning flash-object into block-element 
     * 
     * @FeatureDomain                GUI
     * @FeatureResult                returnValue shows Freemind-Flashviewer with the mindmap-content of block
     * @FeatureKeywords              Layout
     * @param block                  jquery-html-element with the content to convert to mindmap 
     */
    me.formatMindmap = function(block) {
        var content = me.$(block).html();
        var blockId = me.$(block).attr('id');
        var url = "/converters/mindmap?source=" + encodeURIComponent(content);
        console.log("formatMindmap " + blockId + " url:" + url);
        
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
     * format the block-content as plantuml. 
     * <ul>
     * <li>creates a Img-Tag with src "http://www.plantuml.com/plantuml/img/
     * 
     * @FeatureDomain                GUI
     * @FeatureResult                returnValue shows Img with the plantuml-content of block
     * @FeatureKeywords              Layout
     * @param block                  jquery-html-element with the content to convert to plantuml
     */
    me.formatPlantuml = function(block) {
        var blockId = me.$(block).attr('id');
        var content = me.$(block).html();
        var url = me.generatePlantuml(content);
        console.log("formatPlantuml " + blockId + " url:" + url);
        me.$(block).html('<img class="yaioplantuml" src="'+ url + '" id="' + blockId + 'Img">');
    };

    me.generatePlantuml = function(content) {
        function encode64(data) {
            var r = "";
            for (var i=0; i<data.length; i+=3) {
                if (i+2==data.length) {
                    r +=append3bytes(data.charCodeAt(i), data.charCodeAt(i+1), 0);
                } else if (i+1==data.length) {
                    r += append3bytes(data.charCodeAt(i), 0, 0);
                } else {
                    r += append3bytes(data.charCodeAt(i), data.charCodeAt(i+1),
                        data.charCodeAt(i+2));
                }
            }
            return r;
        }

        function append3bytes(b1, b2, b3) {
            var c1 = b1 >> 2;
            var c2 = ((b1 & 0x3) << 4) | (b2 >> 4);
            var c3 = ((b2 & 0xF) << 2) | (b3 >> 6);
            var c4 = b3 & 0x3F;
            var r = "";
            r += encode6bit(c1 & 0x3F);
            r += encode6bit(c2 & 0x3F);
            r += encode6bit(c3 & 0x3F);
            r += encode6bit(c4 & 0x3F);
            return r;
        }

        function encode6bit(b) {
            if (b < 10) {
                return String.fromCharCode(48 + b);
            }
            b -= 10;
            if (b < 26) {
                return String.fromCharCode(65 + b);
            }
            b -= 26;
            if (b < 26) {
                return String.fromCharCode(97 + b);
            }
            b -= 26;
            if (b == 0) {
                return '-';
            }
            if (b == 1) {
                return '_';
            }
            return '?';
        }

        var txt = content;
        txt = txt.replace(/&gt;/g,'>');
        txt = txt.replace(/&lt;/g,'<');
        txt = txt.replace(/\n\.\n/g,'\n');
        txt = txt.replace(/\n\n/g,'\n');
        var s = unescape(encodeURIComponent(txt));
        var url = me.appBase.config.plantUmlBaseUrl + "plantuml/svg/" + encode64(deflate(s, 9));

        return url;
    }

    /**
     * prepare the text to format as mermaid
     * delete .
     * @FeatureDomain                GUI
     * @FeatureResult                returnValue String - prepared text
     * @FeatureKeywords              Layout
     * @param descText               the string to prepare
     * @return                       {String}  prepared text to format with mermaid
     */
    me._prepareTextForMermaid = function(descText) {
        // prepare descText
        var newDescText = descText;
        newDescText = newDescText.replace(/\n\.\n/g, "\n\n");
        return newDescText;
    };
        
    me._configureMarked = function (flgHighlightNow, headerPrefix) {
        var renderer = me._createMarkdownRenderer();
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
    };

    me._createMarkdownRenderer = function () {
        var renderer = new marked.Renderer();

        // my own code-handler
        renderer.code = me._renderMarkdownCode;

        // my own heading-handler to be sure that the heading id is unique
        renderer.heading = me._renderMarkdownHeading;

        // my own link-renderer: for yaio
        renderer.link = me._renderMarkdownLink;

        // my own img-renderer: for yaio
        renderer.image = me._renderMarkdownImage;

        return renderer;
    };

    /**
     * parse yaio-links like yaio:, yaiodmsdownload:, yaiodmsidxdownload:, yaiodmsembed:, yaiodmsidxembed: from href
     * and replace if exists with dms-urls...
     * @FeatureDomain                GUI
     * @FeatureResult                returnValue String - mapped url
     * @FeatureKeywords              Layout
     * @param href                   the url to parse
     * @param dmsOnly                parse dms only: not yaio:
     * @return  {String}             mapped url
     */
    me._parseLinks = function(href, dmsOnly) {
        var sysUID;
        if (!dmsOnly && href && href.indexOf('yaio:') === 0) {
            sysUID = href.substr(5);
            href = "/yaio-explorerapp/yaio-explorerapp.html#/showByAllIds/" + sysUID;
        } else if (href && href.indexOf('yaiodmsdownload:') === 0) {
            sysUID = href.substr('yaiodmsdownload:'.length);
            href = me.appBase.get('YaioAccessManager').getAvailiableNodeAction('dmsDownload', sysUID, false) + sysUID;
        } else if (href && href.indexOf('yaiodmsidxdownload:') === 0) {
            sysUID = href.substr('yaiodmsidxdownload:'.length);
            href = me.appBase.get('YaioAccessManager').getAvailiableNodeAction('dmsIndexDownload', sysUID, false) + sysUID;
        } else if (href && href.indexOf('yaiodmsembed:') === 0) {
            sysUID = href.substr('yaiodmsembed:'.length);
            href = me.appBase.get('YaioAccessManager').getAvailiableNodeAction('dmsEmbed', sysUID, false) + sysUID;
        } else if (href && href.indexOf('yaiodmsidxembed:') === 0) {
            sysUID = href.substr('yaiodmsidxembed:'.length);
            href = me.appBase.get('YaioAccessManager').getAvailiableNodeAction('dmsIndexEmbed', sysUID, false) + sysUID;
        }
        return href;
    };

    me._renderMarkdownCode = function (code, language) {
        code = me.appBase.get('YaioBase').htmlEscapeTextLazy(code);
        if (code.match(/^sequenceDiagram/) || code.match(/^graph/) || code.match(/^gantt/)) {
            return '<div id="inlineMermaid' + (me._localHtmlId++) + '" class="mermaid">'+ me._prepareTextForMermaid(code ) + '</div>';
        } else if (language !== undefined  && (language.match(/^yaiomindmap/) || language.match(/^yaiofreemind/))) {
            return '<div id="inlineMindmap' + (me._localHtmlId++) + '"  class="yaiomindmap">'+ code + '</div>';
        } else if (language !== undefined  && (language.match(/^yaioplantuml/))) {
            return '<div id="inlinePlantUML' + (me._localHtmlId++) + '"  class="yaioplantuml">' + code + '</div>';
        } else {
            return '<pre><code id="inlineCode' + (me._localHtmlId++) + '" class="lang-' + language + '">' + code + '</code></pre>';
        }
    };

    me._renderMarkdownHeading = function(text, level, raw) {
        return '<h' + level
            + ' id="' + this.options.headerPrefix + '_' + (me._localHtmlId++) + '_' + raw.toLowerCase().replace(/[^\w]+/g, '-')
            + '">'
            + text
            + '</h' + level + '>\n';
    };

    me._renderMarkdownLink = function(href, title, text) {
        if (this.options.sanitize) {
            var prot;
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
            href = me._parseLinks(href, false);
        }
        var out = '<a href="' + href + '"';
        if (title) {
            out += ' title="' + title + '"';
        }
        out += '>' + text + '</a>';
        return out;
    };

    me._renderMarkdownImage = function(href, title, text) {
        if (this.options.sanitize) {
            var prot;
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
            href = me._parseLinks(href, true);
        }
        var out = '<img src="' + href + '" alt="' + text + '"';
        if (title) {
            out += ' title="' + title + '"';
        }
        out += this.options.xhtml ? '/>' : '>';
        return out;
    };


    me._init();
    
    return me;
};
