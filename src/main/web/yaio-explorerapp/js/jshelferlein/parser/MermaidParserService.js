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
 * servicefunctions for parsing mermaid-sources
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
JsHelferlein.MermaidParserService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.AbstractParserService(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * render the block-content as mermaid. 
     * @FeatureDomain                GUI
     * @FeatureResult                renders block-content to mermaid
     * @FeatureKeywords              Layout
     * @param blockId                jquery-selector with the content to convert
     */
    me.renderBlock = function(blockId) {
    };

    me.formatMermaidGlobal = function() {
        mermaid.parseError = function(err,hash){
            me.appBase.get('UIDialogs').showToastMessage("error", "Oops! Ein Fehlerchen :-(", "Syntaxfehler bei Parsen des Diagrammcodes:" + err);
        };
        try {
            mermaid.init();
        } catch (ex) {
            console.error("formatMermaidGlobal error:" + ex);
        }
    };

    /**
     * prepare the text to format as mermaid
     * delete .
     * @FeatureDomain                GUI
     * @FeatureResult                returnValue String - prepared text
     * @FeatureKeywords              Layout
     * @param descText               the string to prepare
     * @return                       {String}  prepared text to format with mermaid
     */
    me.prepareTextForMermaid = function(descText) {
        // prepare descText
        var newDescText = descText;
        newDescText = newDescText.replace(/\n\.\n/g, "\n\n");
        return newDescText;
    };
        
    
    me._init();
    
    return me;
};
