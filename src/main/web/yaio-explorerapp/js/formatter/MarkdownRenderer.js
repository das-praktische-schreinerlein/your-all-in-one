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
Yaio.MarkdownRenderer = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.MarkdownRenderer(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
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

    me._init();
    
    return me;
};
