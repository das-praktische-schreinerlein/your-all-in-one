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
 * servicefunctions for parsing plantuml-sources
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
JsHelferlein.PlantumlParserService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.AbstractParserService(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * render the block-content as plantuml. 
     * creates a Img-Tag with src "http://www.plantuml.com/plantuml/img/
     * 
     * @FeatureDomain                GUI
     * @FeatureResult                returnValue shows Img with the plantuml-content of block
     * @FeatureKeywords              Layout
     * @param blockId                jquery-selector with the content to convert
     */
    me.renderBlock = function(blockId) {
        me.$(blockId).each(function(i, block) {
            var blockId = me.$(block).attr('id');
            var content = me.$(block).html();
            var url = me.generatePlantuml(content);
            console.log("PlantumlParserService.renderBlock " + blockId + " url:" + url);
            me.$(block).html('<img class="yaioplantuml" src="'+ url + '" id="' + blockId + 'Img">');
        });
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
    };


    me._init();
    
    return me;
};
