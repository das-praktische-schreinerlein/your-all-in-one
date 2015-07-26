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
     * <h4>FeatureDomain:</h4>
     *     Layout Toggler
     * <h4>FeatureDescription:</h4>
     *     Toggle the specified ojects with a fade. 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>Updates DOM
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree Rendering
     * @param id - JQuery-Filter (html.id, style, objectlist...) 
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
        $( id ).toggle( selectedEffect, options, 500 );
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     WebGUI
     * <h4>FeatureDescription:</h4>
     *     layout-servicefunctions
     *      
     * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category collaboration
     * @copyright Copyright (c) 2014, Michael Schreiner
     * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     */
    
    me.togglePreWrap = function(element) {
        var classNoWrap = "pre-nowrap";
        var classWrap = "pre-wrap";
        var flgClassNoWrap = "flg-pre-nowrap";
        var flgClassWrap = "flg-pre-wrap";
        var codeChilden = $(element).find("code");
        
        // remove/add class if element no has class
        if ($(element).hasClass(flgClassNoWrap)) {
            $(element).removeClass(flgClassNoWrap).addClass(flgClassWrap);
            console.log("togglePreWrap for id:" + element + " set " + classWrap);
            // wrap code-blocks too
            $(codeChilden).removeClass(classNoWrap).addClass(classWrap);
            $(codeChilden).parent().removeClass(classNoWrap).addClass(classWrap);
        } else {
            $(element).removeClass(flgClassWrap).addClass(flgClassNoWrap);
            console.log("togglePreWrap for id:" + element + " set " + classNoWrap);
            // wrap code-blocks too
            $(codeChilden).removeClass(classWrap).addClass(classNoWrap);
            $(codeChilden).parent().removeClass(classNoWrap).addClass(classWrap);
       }
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Layout Toggler
     * <h4>FeatureDescription:</h4>
     *     Toggle the specified ojects with a drop. 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>Updates DOM
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree Rendering
     * @param id - JQuery-Filter (html.id, style, objectlist...) 
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
        $( id ).toggle( selectedEffect, options, 500 );
    };
   
    me._init();

    return me;
};