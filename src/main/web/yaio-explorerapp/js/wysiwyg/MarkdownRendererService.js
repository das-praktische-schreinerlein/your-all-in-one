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
            noCode = me.appBase.get('DataUtils').htmlEscapeTextLazy(noCode);
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
        noCode = me.appBase.get('DataUtils').htmlEscapeTextLazy(noCode);
        noCode = noCode.replace(/&lt;br&gt;/g, "<br>");

        // add rest to newDescText
        newDescText += noCode;

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

        renderer.appBaseVarName = me.appBase.config.appBaseVarName;

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
        code = me.appBase.get('DataUtils').htmlEscapeTextLazy(code);
        if (code.match(/^sequenceDiagram/) || code.match(/^graph/) || code.match(/^gantt/)) {
            return '<div id="inlineMermaid' + (me._localHtmlId++) + '" class="mermaid">'+ me.appBase.get('MermaidParser').prepareTextForMermaid(code ) + '</div>';
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
