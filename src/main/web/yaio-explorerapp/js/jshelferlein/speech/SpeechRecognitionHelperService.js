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

JsHelferlein.SpeechRecognitionHelperService = function(appBase, config) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase, config, JsHelferlein.SpeechRecognitionConfig());
    
    /**
     * initialize the object
     */
    me._init = function() {
    };
    
    var final_transcript = '';
    var recognizing = false;
    var ignore_onend;
    var start_timestamp;
    var two_line = /\n\n/g;
    var one_line = /\n/g;
    var first_char = /\S/;

    me.initSrcFromOpener = function() {
        // Text vom Opener holen
        if (opener && opener.targetElement) {
            document.getElementById(me.config.finalTextareaId).value = me.$(opener.targetElement).text();
            if (! document.getElementById(me.config.finalTextareaId).value) {
                document.getElementById(me.config.finalTextareaId).value = me.$(opener.targetElement).val();
            }
        }
    };
    
    me.addResult2Opener = function(forceClose) {
        // Text vorbereiten
        var str = document.getElementById(me.config.finalTextareaId).value;
        str = str.replace(/<\/?.*?\/?>/g, String.fromCharCode(13));

        // text an Opener uebergeben
        opener.targetElement.value = str;
        opener.callUpdateTriggerForElement(opener.targetElement);

        // Fenster schließen
        if (forceClose) {
            window.close();
        }
    };
    
    me.initUi = function() {
        if (me.appBase.getDetector('SpeechRecognitionDetector').isSupported()) {
            me._initRecognition();
            me._initControllerElements();
        } else {
            var svcLogger = me.appBase.get("Logger");
            if (svcLogger && svcLogger.isWarning) {
                svcLogger.logWarning("JsHelferlein.SpeechRecognitionHelper.initUi: speechsynth not suppoorted");
            }
            me._disableControllerElements();
        }
    };
    
    me.startRecognition = function() {
        if (recognizing) {
            me.recognition.stop();
            return;
        }
        final_transcript = document.getElementById(me.config.finalTextareaId).value;
        me.recognition.lang = "de-de";
        me.recognition.start();
        ignore_onend = false;
        document.getElementById(me.config.interimSpanId).innerHTML = '';
        document.getElementById(me.config.statusImgId).src = me.config.statusImgSrcStop;

        me._showInfo('info-allow-sr');
        start_timestamp = event.timeStamp;
        if (me.config.isSet('buttonPauseId')) {
            document.getElementById(me.config.buttonPauseId).removeAttribute('disabled');
        }
        if (me.config.isSet('buttonResumeId')) {
            document.getElementById(me.config.buttonResumeId).setAttribute('disabled', 'disabled');
        }
    };

    me.pauseRecognition = function() {
        window.speechSynthesis.pause();
        if (me.config.isSet('buttonPauseId')) {
            document.getElementById(me.config.buttonPauseId).setAttribute('disabled', 'disabled');
        }
        if (me.config.isSet('buttonResumeId')) {
            document.getElementById(me.config.buttonResumeId).removeAttribute('disabled');
        }
    };
    
    me.resumeRecognition = function() {
        window.speechSynthesis.resume();
        if (me.config.isSet('buttonPauseId')) {
            document.getElementById(me.config.buttonPauseId).removeAttribute('disabled');
        }
        if (me.config.isSet('buttonResumeId')) {
            document.getElementById(me.config.buttonResumeId).setAttribute('disabled', 'disabled');
        }
    };

    me.stopRecognition = function() {
        me.addResult2Opener(true);
    };

    me._initRecognition = function() {
        // Erkennung aktivieren
        me.recognition = new webkitSpeechRecognition();

        // Diktat aktivieren: fuehrt nach Pause fort
        me.recognition.continuous = true;

        // interim results aendern sich nachtraeglich
        me.recognition.interimResults = true;
        
        // add language
        me.recognition.lang = "de-DE";

        // Handler

        // beim Start
        me.recognition.onstart = function() {
            recognizing = true;
            me._showInfo('info-speak-now-sr');
            document.getElementById(me.config.statusImgId).src = me.config.statusImgSrcRunning;
        };

        // Am Ende
        me.recognition.onerror = function(event) {
            if (event.error == 'no-speech') {
                document.getElementById(me.config.statusImgId).src = me.config.statusImgSrcStart;
                me._showInfo('info-no-speech-sr');
                ignore_onend = true;
            }
            if (event.error == 'audio-capture') {
                document.getElementById(me.config.statusImgId).src = me.config.statusImgSrcStart;
                me._showInfo('info-no-microphone-sr');
                ignore_onend = true;
            }
            if (event.error == 'not-allowed') {
                if (event.timeStamp - start_timestamp < 100) {
                    me._showInfo('info-blocked-sr');
                } else {
                    me._showInfo('info-denied-sr');
                }
                ignore_onend = true;
            }
        };

        me.recognition.onend = function() {
            recognizing = false;
            if (ignore_onend) {
                return;
            }
            document.getElementById(me.config.statusImgId).src = me.config.statusImgSrcStart;
            if (!final_transcript) {
                me._showInfo('info-start-sr');
                return;
            }
            me._showInfo('');
            if (window.getSelection) {
                window.getSelection().removeAllRanges();
                var range = document.createRange();
                range.selectNode(document.getElementById(me.config.finalTextareaId));
                window.getSelection().addRange(range);
            }
        };

        me.recognition.onresult = function(event) {
            var interim_transcript = '';
            final_transcript = document.getElementById(me.config.finalTextareaId).value;
            if (typeof (event.results) == 'undefined') {
                me.recognition.onend = null;
                me.recognition.stop();
                me.upgrade();
                return;
            }

            for ( var i = event.resultIndex; i < event.results.length; ++i) {
                if (event.results[i].isFinal) {
                    final_transcript += " " + event.results[i][0].transcript;
                } else {
                    interim_transcript += " " + event.results[i][0].transcript;
                }
            }
            final_transcript = me._capitalize(final_transcript);
            document.getElementById(me.config.finalTextareaId).value = me._linebreak(final_transcript);
            document.getElementById(me.config.interimSpanId).innerHTML = me._linebreak(interim_transcript);
        };
    };

    me._initControllerElements = function() {
        if (me.config.isSet('buttonStartId')) {
            document.getElementById(me.config.buttonStartId).onclick = me.startRecognition;
        }
        if (me.config.isSet('buttonPauseId')) {
            document.getElementById(me.config.buttonPauseId).onclick = me.pauseRecognition;
            document.getElementById(me.config.buttonPauseId).setAttribute('disabled', 'disabled');
        }
        if (me.config.isSet('buttonResumeId')) {
            document.getElementById(me.config.buttonResumeId).onclick = me.resumeRecognition;
            document.getElementById(me.config.buttonResumeId).setAttribute('disabled', 'disabled');
        }
        if (me.config.isSet('buttonStopId')) {
            document.getElementById(me.config.buttonStopId).onclick = me.stopRecognition;
        }
    };
    
    me._disableControllerElements = function() {
        if (me.config.isSet('buttonStartId')) {
            document.getElementById(me.config.buttonStartId).setAttribute('disabled', 'disabled');
        }
        if (me.config.isSet('buttonPauseId')) {
            document.getElementById(me.config.buttonPauseId).setAttribute('disabled', 'disabled');
        }
        if (me.config.isSet('buttonResumeId')) {
            document.getElementById(me.config.buttonResumeId).setAttribute('disabled', 'disabled');
        }
        if (me.config.isSet('buttonStopId')) {
            document.getElementById(me.config.buttonStopId).setAttribute('disabled', 'disabled');
        }
    };

    me._showInfo = function(s) {
        if (s) {
            for ( var child = info.firstChild; child; child = child.nextSibling) {
                if (child.style) {
                    child.style.display = child.id == s ? 'inline' : 'none';
                }
            }
            info.style.visibility = 'visible';
        } else {
            info.style.visibility = 'hidden';
        }
    };

    me._linebreak = function(s) {
        return s.replace(two_line, '<p></p>').replace(one_line, '<br>');
    };
    me._capitalize = function(s) {
        return s.replace(first_char, function(m) {
            return m.toUpperCase();
        });
    };
    
    // init all
    me._init();
    
    return me;
};