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
 * Extended Markdown-syntax
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
'use strict';

marked.Lexer.prototype.extenedBlockRules = {
    box_start: /^ *(<|&lt;)\!---(BOX\.INFO|BOX\.WARN|BOX\.ALERT|BOX|CONTAINER|STYLE?) *([#-_a-zA-Z,;0-9\.: ]*?) *---(>|&gt;)/,
    box_end:   /^ *(<|&lt;)\!---\/(BOX\.INFO|BOX\.WARN|BOX\.ALERT|BOX|CONTAINER|STYLE?) *([#-_a-zA-Z,;0-9\.: ]*?) *---(>|&gt;)/
};

marked.Lexer.prototype.tokenizeExtenedMarkdown = function(lexer, src) {
    var cap;
    // check block.msextend
    if (lexer.extenedBlockRules.box_start) {
        if (cap = lexer.extenedBlockRules.box_start.exec(src)) {
            src = src.substring(cap[0].length);
            lexer.tokens.push({
                type: 'box_start',
                boxtype: cap[2],
                attr: cap[3]
            });
            return { src: src, found: true };
        }
    }
    if (lexer.extenedBlockRules.box_end) {
        if (cap = lexer.extenedBlockRules.box_end.exec(src)) {
            src = src.substring(cap[0].length);
            lexer.tokens.push({
                type: 'box_end',
                boxtype: cap[2],
                attr: cap[3]
            });
            return { src: src, found: true };
        }
    }

    return { src: src, found: false };
};

marked.Parser.prototype.renderExtenedMarkdownToken = function(parser, token) {
    switch (token.type) {
        case 'box_start': {
            return parser.renderer._renderExtenedMarkdownBoxStart(parser.renderer, token.boxtype, token.attr);
        }
        case 'box_end': {
            return parser.renderer._renderExtenedMarkdownBoxEnd(parser.renderer, token.boxtype, token.attr);
        }
        case 'toggler': {
            return parser.renderer._renderExtenedMarkdownToggler(parser.renderer, token.togglertype, token.attr);
        }
        case 'splitter': {
            return parser.renderer._renderExtenedMarkdownSplitter(parser.renderer, token.togglertype, token.attr, token.pre, token.after);
        }
    }
    return '';
};

marked.InlineLexer.prototype.extenedInlineRules = {
    toggler: /([\s\S]*?)(<|&lt;)!---(TOGGLER) *([-#_a-zA-Z,;0-9\.]*?) *---(>|&gt;)([\s\S]*)/,
    splitter: /([\s\S]*?)(:\|:)([\s\S]*)/,
    box_start: /([\s\S]*?)(<|&lt;)\!---(BOX\.INFO|BOX\.WARN|BOX\.ALERT|BOX|CONTAINER|STYLE?) *([#-_a-zA-Z,;0-9\.: ]*?) *---(>|&gt;)([\s\S]*)/,
    box_end:   /([\s\S]*?)(<|&lt;)\!---\/(BOX\.INFO|BOX\.WARN|BOX\.ALERT|BOX|CONTAINER|STYLE?) *([#-_a-zA-Z,;0-9\.: ]*?) *---(>|&gt;)([\s\S]*)/
};

marked.InlineLexer.prototype.renderExtenedInlineSyntax = function(inlinelexer, src) {
    var out = '', cap;

    // check inline.msextend
    if (inlinelexer.extenedInlineRules.splitter) {
        if (cap = inlinelexer.extenedInlineRules.splitter.exec(src)) {
            out += inlinelexer.renderer._renderExtenedMarkdownSplitter(inlinelexer.renderer, cap[2], '', 
                    inlinelexer.output(cap[1]), inlinelexer.output(cap[3]));
            src = '';
            return { out: out, src: src, found: true };
        }
    }
    if (inlinelexer.extenedInlineRules.toggler) {
        if (cap = inlinelexer.extenedInlineRules.toggler.exec(src)) {
            out += inlinelexer.output(cap[1]);
            out += inlinelexer.renderer._renderExtenedMarkdownToggler(inlinelexer.renderer, cap[3], cap[4]);
            src = cap[6];
            return { out: out, src: src, found: true };
        }
    }
    if (inlinelexer.extenedInlineRules.box_start) {
        if (cap = inlinelexer.extenedInlineRules.box_start.exec(src)) {
            out += inlinelexer.output(cap[1]);
            out += inlinelexer.renderer._renderExtenedMarkdownBoxStart(inlinelexer.renderer, cap[3], cap[4]);
            src = cap[6];
            return { out: out, src: src, found: true };
        }
    }
    if (inlinelexer.extenedInlineRules.box_end) {
        if (cap = inlinelexer.extenedInlineRules.box_end.exec(src)) {
            out += inlinelexer.output(cap[1]);
            out += inlinelexer.renderer._renderExtenedMarkdownBoxEnd(inlinelexer.renderer, cap[3], cap[4]);
            src = cap[6];
            return { out: out, src: src, found: true };
        }
    }
    return { out: '', src: src, found: false };
};


marked.Renderer.prototype._renderExtenedMarkdownBoxhtmlStart = function(renderer, type, param) {
    return '<div class="md-' + type + 'box ' + renderer.genStyleClassesForTag(type + 'box') + '">' +
           '<div class="md-' + type + 'box-ue ' + renderer.genStyleClassesForTag(type + 'box-ue') + '">' + param + '</div>' +
           '<div class="md-' + type + 'box-container ' + renderer.genStyleClassesForTag(type + 'box-container') + '">';
};

marked.Renderer.prototype._renderExtenedMarkdownBoxStart = function(renderer, type, param) {
    var res = '';

    if (type.toLowerCase() === 'box') {
        res = '<div class="md-box ' + renderer.genStyleClassesForTag('box') + ' ' + param + '">';
    } else if (type.toLowerCase() === 'container') {
        res = '<div class="md-container md-container-' + param + '" id="md-container-' + param + '">';
    } else if (type.toLowerCase() === 'box.info') {
        res = renderer._renderExtenedMarkdownBoxhtmlStart(renderer, 'info', param);
    } else if (type.toLowerCase() === 'box.warn') {
        res = renderer._renderExtenedMarkdownBoxhtmlStart(renderer, 'warn', param);
    } else if (type.toLowerCase() === 'box.alert') {
        res = renderer._renderExtenedMarkdownBoxhtmlStart(renderer, 'alert', param);
    } else if (type.toLowerCase() === 'style' && param) {
        // do set style for next elements

        // split params elements:styles
        var params = param.split(':'),
        tags = [],
        styles = [];
        if (params.length > 0) {
            tags = params[0].split(' ');
            if (params.length > 1) {
                styles = params[1].split(' ');
            }
        }
        // set styles for all tags
        var allTagStyles = renderer.allTagStyles;
        tags.map(function (tag) {
            var tagStyles = allTagStyles[tag];
            if (!tagStyles) {
                tagStyles = {};
            }
            styles.map(function (style) {
                tagStyles[style] = style;
            });
            allTagStyles[tag] = tagStyles;
        });
        renderer.allTagStyles = allTagStyles;
    }
    return res;
};

marked.Renderer.prototype._renderExtenedMarkdownBoxEnd = function(renderer, type, param) {
    var res = '';

    if (type.toLowerCase() === 'box') {
        res = '</div>';
    } else if (type.toLowerCase() === 'box.info' ||
               type.toLowerCase() === 'box.alert' ||
               type.toLowerCase() === 'box.warn') {
        res = '</div></div>';
    } else if (type.toLowerCase() === 'container') {
        res = '</div>';
    } else if (type.toLowerCase() === 'style' && param) {
        // do reset style for next elements
        // split params elements:styles
        var params = param.split(':'),
        tags = [],
        styles = [];
        if (params.length > 0) {
            tags = params[0].split(' ');
            if (params.length > 1) {
                styles = params[1].split(' ');
            }
        }
        // reset styles for all tags
        var allTagStyles = renderer.allTagStyles;
        tags.map(function (tag) {
            styles.map(function (style) {
                if (allTagStyles[tag] && allTagStyles[tag][style]) {
                    allTagStyles[tag][style] = '';
                    delete allTagStyles[tag][style];
                }
            });
        });
    }
    return res;
};

marked.Renderer.prototype._renderExtenedMarkdownToggler = function(renderer, type, attr) {
    var res = '';
    var params = (attr || '').split(','),
        id, togglerType;
    if (params.length > 0) {
        id = params[0].replace(" ");
        if (params.length > 1) {
            togglerType = params[1];
        }
    }

    if (type.toLowerCase() === 'toggler') {
        res = '<div class="md-togglerparent md-togglerparent-' + id + '" id="md-togglerparent-' + id + '"></div>' +
              '<script>yaioAppBase.get(\'UIToggler\').appendToggler(".md-togglerparent-' + id + '", ".md-container-' + id + '", "' + togglerType + '");</script>';
    }
    return res;
};

marked.Renderer.prototype._renderExtenedMarkdownSplitter = function(renderer, type, attr, first, second) {
    var res = '<label class="md-splitter-first ' + renderer.genStyleClassesForTag('splitter_first') + '">' + first + '</label>' +
              '<span class="md-splitter-second ' + renderer.genStyleClassesForTag('splitter_second') + '">' + second + '</span>';
    return res;
};

