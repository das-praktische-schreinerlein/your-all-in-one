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

JsHelferlein.SpeechRecognitionConfig = function() {
    'use strict';

    // my own instance
    var me = JsHelferlein.ConfigBase();
    
    me.defaultLang         = "de-DE";
    
    me.finalTextareaId     = 'final-textarea-sr';
    me.interimSpanId       = 'interim-span-sr';
    me.statusImgId         = 'status-img-sr';
    
    me.statusImgSrcStart   = 'https://www.google.com/intl/en/chrome/assets/common/images/content/mic.gif';
    me.statusImgSrcRunning = 'https://www.google.com/intl/en/chrome/assets/common/images/content/mic-animate.gif';
    me.statusImgSrcStop    = 'https://www.google.com/intl/en/chrome/assets/common/images/content/mic-slash.gif';

    me.buttonStartId       = 'button-start-sr';
    me.buttonPauseId       = undefined;
    me.buttonResumeId      = undefined;
    me.buttonStopId        = 'button-stop-sr';

    return me;
};