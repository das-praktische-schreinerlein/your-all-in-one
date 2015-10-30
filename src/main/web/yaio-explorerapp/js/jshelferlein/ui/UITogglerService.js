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

JsHelferlein.UIToggler = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);
    
    /**
     * initialize the object
     */
    me._init = function() {
    };

    // init all
    me._init();

    /** 
     * Toggle the specified ojects with a fade. 
     * @FeatureDomain                Layout Toggler
     * @FeatureResult                Updates DOM
     * @FeatureKeywords              GUI Tree Rendering
     * @param id                     JQuery-Filter (html.id, style, objectlist...) 
     */
    me.toggleTableBlock = function(id) {
        // get effect type from
        var selectedEffect = "fade";
   
        // most effect types need no options passed by default
        var options = {};
        // some effects have required parameters
        if ( selectedEffect === "scale" ) {
          options = { percent: 0 };
        } else if ( selectedEffect === "size" ) {
          options = { to: { width: 200, height: 60 } };
        }
   
        // run the effect
        me.$( id ).toggle( selectedEffect, options, 500 );
    };
    
    /** 
     * layout-servicefunctions
     *  
     * @FeatureDomain                WebGUI
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category                     collaboration
     * @copyright                    Copyright (c) 2014, Michael Schreiner
     * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    
    me.togglePreWrap = function(element) {
        var classNoWrap = "pre-nowrap";
        var classWrap = "pre-wrap";
        var flgClassNoWrap = "flg-pre-nowrap";
        var flgClassWrap = "flg-pre-wrap";
        var codeChilden = me.$(element).find("code");
        
        // remove/add class if element no has class
        if (me.$(element).hasClass(flgClassNoWrap)) {
            me.$(element).removeClass(flgClassNoWrap).addClass(flgClassWrap);
            console.log("togglePreWrap for id:" + element + " set " + classWrap);
            // wrap code-blocks too
            me.$(codeChilden).removeClass(classNoWrap).addClass(classWrap);
            me.$(codeChilden).parent().removeClass(classNoWrap).addClass(classWrap);
        } else {
            me.$(element).removeClass(flgClassWrap).addClass(flgClassNoWrap);
            console.log("togglePreWrap for id:" + element + " set " + classNoWrap);
            // wrap code-blocks too
            me.$(codeChilden).removeClass(classWrap).addClass(classNoWrap);
            me.$(codeChilden).parent().removeClass(classNoWrap).addClass(classWrap);
       }
    };
    
    /** 
     * Toggle the specified ojects with a drop. 
     * @FeatureDomain                Layout Toggler
     * @FeatureResult                Updates DOM
     * @FeatureKeywords              GUI Tree Rendering
     * @param id                     JQuery-Filter (html.id, style, objectlist...) 
     */
    me.toggleElement = function(id) {
        // get effect type from
        var selectedEffect = "drop";
   
        // most effect types need no options passed by default
        var options = {};
        // some effects have required parameters
        if ( selectedEffect === "scale" ) {
          options = { percent: 0 };
        } else if ( selectedEffect === "size" ) {
          options = { to: { width: 200, height: 60 } };
        }
   
        // run the effect
        me.$( id ).toggle( selectedEffect, options, 500 );
    };
   
    me._init();

    return me;
};