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
 * servicefunctions for parsing sourcecode-sources
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
JsHelferlein.SyntaxHighlighterParserService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.AbstractParserService(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * render the block-content as sourcecode with syntax-highlighting
     * @FeatureDomain                GUI
     * @FeatureResult                renders block-content to formatted sourcecode
     * @FeatureKeywords              Layout
     * @param blockId                jquery-selector with the content to convert
     */
    me.renderBlock = function(blockId) {
        me.$(blockId).each(function(i, block) {
            var blockId = me.$(block).attr('id');
            console.log("SyntaxHighlighterParserService.renderBlock " + blockId);
            hljs.highlightBlock(block);
        });
    };

    me._init();
    
    return me;
};
