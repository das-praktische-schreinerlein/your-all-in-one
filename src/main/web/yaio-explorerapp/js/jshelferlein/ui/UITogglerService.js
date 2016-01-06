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
    
    me.appendToggler = function(parentId, containerId, type) {
        var $ele = me.$(containerId + "_On");
        if ($ele.length <= 0) {
            // create toggler
            console.log("createTogglerIfNotExists link not exists: create new toggler parent=" + parentId 
                    + " containerId=" + containerId);
            var html = me._createTogglerElement(containerId, type);
            me.$(parentId).append(html);
        } else {
            console.log("createTogglerIfNotExists link exists: skip new toggler parent=" + parentId 
                    + " containerId=" + containerId);
        }
    };
    
    me._createTogglerElement = function(containerId, type) {
        var containerClass = containerId.replace(".", "");
        var html;
        if (type === 'text') {
            html = me._createTogglerLinks(containerId, containerId,
                "<span class='text-toggler text-toggleron'>[ mehr... ]</span>",
                "<span class='text-toggler text-toggleroff'>[ weniger... ]</span>", '', '');
        } else if (type === 'icon2') {
            html = me._createTogglerLinks(containerId, containerId,
                "",
                "", "toggler_show", "toggler_hide");
        } else if (type === 'icon' || 1) {
            html = me._createTogglerLinks(containerId, containerId,
                "<span class='icon-toggler icon-toggleron'>&nbsp;</span>",
                "<span class='icon-toggler icon-toggleroff'>&nbsp;</span>", "", "");
        }
        html = '<div class="blockToggler block4Toggler' + containerClass + '" togglerbaseid="' + containerId + '" toggleid="' + containerId + '">' + html + '</div>';
        return html;
    };
    
    /**
     * FormRow-Toggler erzeugen (blendet Formularfelder eines Typs ein/aus)
     * @param togglerBaseId
     * @param toggleClassName
     * @param htmlOn
     * @param htmlOff
     * @param addStyleOn
     * @param addStyleOff
     * @returns
     */
    me._createTogglerLinks = function(togglerBaseId, toggleClassName,
            htmlOn, htmlOff, addStyleOn, addStyleOff) {
        // parameter pruefen
        var yaioAppBaseVarName = me.appBase.config.appBaseVarName;
        if (! togglerBaseId) {
           return null;
        }

        // html erzeugen
        var togglerBaseClass = togglerBaseId.replace(".", "");
        var html = "<a href=\"#\" onclick=\"javascript: " + yaioAppBaseVarName + ".get('UIToggler').toggle('" + togglerBaseId + "', '" + toggleClassName + "', false); return false;\" class=\"toggler togglerOn " + togglerBaseClass + "_On " + addStyleOn + "\" id=\"" + togglerBaseClass + "_On\">";
        html += htmlOn + "</a>";
        html += "<a href=\"#\" onclick=\"javascript: " + yaioAppBaseVarName + ".get('UIToggler').toggle('" + togglerBaseId + "', '" + toggleClassName + "', true); return false;\" class=\"toggler togglerOff " + togglerBaseClass + "_Off " + addStyleOff + "\" style=\"display: none;\" id=\"" + togglerBaseClass + "_Off\">";
        html += htmlOff + "</a>";

        return html;
    };

    /** 
     * Toggle the specific toggleContainer managed by toggler
     * @FeatureDomain                Layout Toggler
     * @FeatureResult                Updates DOM
     * @FeatureKeywords              GUI Tree Rendering
     * @param toggleContainerId      JQuery-Filter (html.id, style, objectlist...) for the specific toggleContainer to toggle
     * @param togglerId              JQuery-Filter (html.id, style, objectlist...) for the specific toggle to toggle
     */
    me.toggle = function(toggleContainerId, togglerId) {
        if (me.$(togglerId).hasClass('toggler_hidden')) {
            // show
            me.$(toggleContainerId).slideDown(1000);
            me.$(togglerId).addClass('toggler_show').removeClass('toggler_hidden');
        } else {
            // hide
            me.$(toggleContainerId).slideUp(1000);
            me.$(togglerId).addClass('toggler_hidden').removeClass('toggler_show');
        }
    };

    /** 
     * Toggle all toggleContainer managed by the masterToggler
     * @FeatureDomain                Layout Toggler
     * @FeatureResult                Updates DOM
     * @FeatureKeywords              GUI Tree Rendering
     * @param masterTogglerId        JQuery-Filter (html.id, style, objectlist...) for the masterToggler 
     * @param toggleContainerId      JQuery-Filter (html.id, style, objectlist...) for the specific toggleContainer to toggle
     * @param togglerId              JQuery-Filter (html.id, style, objectlist...) for the specific toggle to toggle
     */
    me.toggleAllToggler = function(masterTogglerId, toggleContainerId, togglerId) {
        if (me.$(masterTogglerId).hasClass('toggler_hidden')) {
            // show all
            me.$(toggleContainerId).slideDown(1000);
            me.$(togglerId).addClass('toggler_show').removeClass('toggler_hidden');
            me.$(masterTogglerId).addClass('toggler_show').removeClass('toggler_hidden');
        } else {
            // hide all
            me.$(toggleContainerId).slideUp(1000);
            me.$(togglerId).addClass('toggler_hidden').removeClass('toggler_show');
            me.$(masterTogglerId).addClass('toggler_show').removeClass('toggler_hidden');
        }
    };

    me._init();

    return me;
};