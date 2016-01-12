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
 * servicefunctions for parsing mindmap-sources
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
JsHelferlein.MindmapParserService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.AbstractParserService(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * render the block-content as mindmap. 
     * @FeatureDomain                GUI
     * @FeatureResult                converts block-content to freemind-flasdh-obj
     * @FeatureKeywords              Layout
     * @param blockId                jquery-selector with the content to convert
     */
    me.renderBlock = function(blockId) {
        me.$(blockId).each(function(i, block) {
            var blockId = me.$(block).attr('id');
            console.log("MindmapParserService.renderBlock " + blockId);
            me.formatMindmap(block);
        });
    };

    /**
     * format the block-content as mindmap. 
     * <ul>
     * <li>creates a FlashObject /dist/vendors.vendorversion/freemind-flash/visorFreemind.swf
     * <li>Calls /converters/mindmap with the html-content of the block
     * <li>insert the returning flash-object into block-element 
     * </ul>
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
    
    me._init();
    
    return me;
};
