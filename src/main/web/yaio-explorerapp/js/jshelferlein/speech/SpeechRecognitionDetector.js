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

JsHelferlein.SpeechRecognitionDetector = function(appBase, config) {
    'use strict';

    // my own instance
    var me = JsHelferlein.DetectorBase(appBase, config, JsHelferlein.SpeechRecognitionDetectorConfig());
    
    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * liefert zurueck ob der Browser WebkitSpeechRecognition unterstuetzt
     */
    me.isSpeechRecognitionSupported = function() {
        try {
            if ('webkitSpeechRecognition' in window) {
                return true;
            }
        } catch (ex) {
            if (me.appBase.get("Logger") && me.appBase.get("Logger").isError) {
                me.appBase.get("Logger").logError("JsHelferlein.SpeechSynthHelper.isSpeechRecognitionSupported Exception: " + ex);
            }
        }
        return false;
    };
    
    me.isSupported = function() {
        return me.isSpeechRecognitionSupported();
    };
    
    // init all
    me._init();
    
    return me;
};