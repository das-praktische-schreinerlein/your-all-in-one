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
'use strict';

JsHelferlein.SpeechSynthConfig = function() {
    // my own instance
    var me = JsHelferlein.ConfigBase();
    
    me.splitChars       = "\n?!:;,.";
    me.splitWords       = [" und ", " oder ", " aber ", " dabei ", " bis ", " "];

    me.defaultLang      = "de-DE";
    me.selectVoiceId    = 'voice';
    me.inputRateId      = 'rate';
    me.inputPitchId     = 'pitch';
    
    me.finalTextareaId  = 'final-textarea-ss';

    me.buttonStartId    = 'button-start-ss';
    me.buttonPauseId    = 'button-pause-ss';
    me.buttonResumeId   = 'button-resume-ss';
    me.buttonStopId     = 'button-stop-ss';

    return me;
};