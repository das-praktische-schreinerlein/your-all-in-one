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

JsHelferlein.SpeechSynthDetector = function(appBase, config) {
    'use strict';

    // my own instance
    var me = JsHelferlein.DetectorBase(appBase, config, JsHelferlein.SpeechSynthDetectorConfig());
    
    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * liefert zurueck ob der Browser SpeechSynth unterstuetzt
     */
    me.isSpeechSynthSupported = function() {
        try {
            if ('speechSynthesis' in window) {
                return true;
            }
        } catch (ex) {
            var svcLogger = me.appBase.get("Logger");
            if (svcLogger && svcLogger.isError) {
                svcLogger.logError("JsHelferlein.SpeechSynthHelper.isSpeechSynthSupported Exception: " + ex);
            }
        }
        return false;
    };
    
    me.isSupported = function() {
        return me.isSpeechSynthSupported();
    };
    
    // init all
    me._init();
    
    return me;
};