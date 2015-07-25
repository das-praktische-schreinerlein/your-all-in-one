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

JsHelferlein.SpeechSynthHelperService = function(appBase, config) {
    // my own instance
    var me = JsHelferlein.ServiceBase(appBase, config, JsHelferlein.SpeechSynthConfig());
    
    /**
     * initialize the object
     */
    me._init = function() {
    }

    me.initSrcFromOpener = function() {
        // Text vom Opener holen
        if (opener && opener.targetElement) {
            document.getElementById(me.config.finalTextareaId).value = $(opener.targetElement).text();
            if (! document.getElementById(me.config.finalTextareaId).value) {
                document.getElementById(me.config.finalTextareaId).value = $(opener.targetElement).val();
            }
        }
    }
    
    me.initUi = function() {
        if (me.appBase.getDetector('SpeechSynthDetector').isSupported()) {
            me._initVoices();
            me._initControllerElements();
        } else {
            if (me.appBase.get("Logger") && me.appBase.get("Logger").isWarning) {
                me.appBase.get("Logger").logWarning("JsHelferlein.SpeechSynthHelper.initUi: speechsynth not suppoorted");
            }
            me._disableControllerElements();
        }
    };
    
    me.startSpeech = function() {
        me.stopSpeech();
        if (me.config.isSet('buttonPauseId')) {
            document.getElementById(me.config.buttonPauseId).removeAttribute('disabled');
        }
        if (me.config.isSet('buttonResumeId')) {
            document.getElementById(me.config.buttonResumeId).setAttribute('disabled', 'disabled');
        }
        me._splitOrSpeakText(document.getElementById(me.config.finalTextareaId).value, me.config.splitChars, 0, 150);
    }

    me.pauseSpeech = function() {
        window.speechSynthesis.pause();
        if (me.config.isSet('buttonPauseId')) {
            document.getElementById(me.config.buttonPauseId).setAttribute('disabled', 'disabled');
        }
        if (me.config.isSet('buttonResumeId')) {
            document.getElementById(me.config.buttonResumeId).removeAttribute('disabled');
        }
    }
    
    me.resumeSpeech = function() {
        window.speechSynthesis.resume();
        if (me.config.isSet('buttonPauseId')) {
            document.getElementById(me.config.buttonPauseId).removeAttribute('disabled');
        }
        if (me.config.isSet('buttonResumeId')) {
            document.getElementById(me.config.buttonResumeId).setAttribute('disabled', 'disabled');
        }
    }

    me.stopSpeech = function() {
        for (var i=1; i < 100; i++) {
            window.speechSynthesis.cancel(); // if it errors, this clears out the error.
        }
    }

    me._initVoices = function() {
        // inspired by http://www.sitepoint.com/talking-web-pages-and-the-speech-synthesis-api/
        // init voice config
        me.voices = document.getElementById(me.config.selectVoiceId);
        me.rate = document.getElementById(me.config.inputRateId);
        me.pitch = document.getElementById(me.config.inputPitchId);
        // Workaround for a Chrome issue (#340160 - https://code.google.com/p/chromium/issues/detail?id=340160)
        var watch = setInterval(function() {
            // Load all voices available
            var voicesAvailable = speechSynthesis.getVoices();
            if (voicesAvailable.length !== 0) {
                for(var i = 0; i < voicesAvailable.length; i++) {
                    var selected = "";
                    if (voicesAvailable[i].lang == me.config.defaultLang) {
                        selected = " selected ";
                    }
                    me.voices.innerHTML += '<option value="' + voicesAvailable[i].lang + '"' +
                                        'data-voice-uri="' + voicesAvailable[i].voiceURI + '"' + selected + '>' +
                                        voicesAvailable[i].name +
                                        (voicesAvailable[i].default ? ' (default)' : '') + '</option>';
                }
              clearInterval(watch);
            }
        }, 1);
    };
    
    me._initControllerElements = function() {
        if (me.config.isSet('buttonStartId')) {
            document.getElementById(me.config.buttonStartId).onclick = me.startSpeech;
        }
        if (me.config.isSet('buttonPauseId')) {
            document.getElementById(me.config.buttonPauseId).onclick = me.pauseSpeech;
            document.getElementById(me.config.buttonPauseId).setAttribute('disabled', 'disabled');
        }
        if (me.config.isSet('buttonResumeId')) {
            document.getElementById(me.config.buttonResumeId).onclick = me.resumeSpeech;
            document.getElementById(me.config.buttonResumeId).setAttribute('disabled', 'disabled');
        }
        if (me.config.isSet('buttonStopId')) {
            document.getElementById(me.config.buttonStopId).onclick = me.stopSpeech;
        }
    }
    
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
    }
    
    me._createSpeaker = function() {
        var msg = new SpeechSynthesisUtterance();
        
        var selectedVoice = me.voices.options[me.voices.selectedIndex];
        msg.voice = selectedVoice.getAttribute('data-voice-uri'); // Note: some voices don't support altering params
        msg.voiceURI = 'native';
        msg.lang = selectedVoice.value;
        msg.volume = 1; // 0 to 1
        msg.rate = me.rate.value; // 0.1 to 10
        msg.pitch = me.pitch.value; //0 to 2
        
        // create handler
        msg.onstart = function (event) {
            me.appBase.get("Logger").logDebug("started:" + msg.text);
        };
        msg.onend = function(event) {
            me.appBase.get("Logger").logDebug('Finished in ' + event.elapsedTime + ' seconds.');
        };
        msg.onerror = function(event) {
            me.appBase.get("Logger").logError('Errored ' + event);
        }
        msg.onpause = function (event) {
            me.appBase.get("Logger").logDebug('paused ' + event);
        }
        msg.onboundary = function (event) {
            me.appBase.get("Logger").logDebug('onboundary ' + event);
        }

        return msg;
    }

    me._speakText = function(text) {
        var speaker = me._createSpeaker();
        me.appBase.get("Logger").logDebug("say text: " + text);
        speaker.text = text;
        window.speechSynthesis.speak(speaker);
    }

    me._splitOrSpeakText = function(text, splitterStr, ebene, maxLength) {
        // inspired by http://stackoverflow.com/questions/21947730/chrome-speech-synthesis-with-longer-texts

        // set default maxLength if not set
        if (! (maxLength > 100)) {
            maxLength = 100;
        }
        
        // split sentences
        var splitter = splitterStr.substr(ebene,1);
        var sentences = [];
        
        // if last splitChar rechaed, split by splitwords
        if (ebene >= splitterStr.length) {
            // split at last splitword before maxLength
            var nextText = "";
            var wordIndex = 0;
            
            // interate splitwords till text < maxLength
            do {
                var splitWord = me.config.splitWords[wordIndex];
                var pos = text.lastIndexOf(splitWord);
                while (pos > 0  && text.length > maxLength) {
                    // iterate until splitword found before maxLength
                    nextText = text.substr(pos) + nextText;
                    text = text.substr(0, pos);
                    pos = text.lastIndexOf(splitWord);
                    me.appBase.get("Logger").logDebug("split by word:'" + splitWord + "' text:''" + text + "' nextText:'" + nextText + "'");
                }
                wordIndex++;
            } while (wordIndex < splitWords.length && text.length > maxLength)
            
            // fallback if text > maxLength
            if (text.length > maxLength) {
                // attempt to split a " " before maxLength
                var posSpace = text.indexOf(" ");
                if ((posSpace <= 0 || posSpace > maxLength)) {
                    // not " " before maxLength -> do it hard
                    var text1 = text.substr(0, maxLength);
                    var text2 = text.substr(maxLength);
                    me.appBase.get("Logger").logDebug("split texthard text1:'" + text1 + "' text2:''" + text2 + "'");
                    sentences = [text1, text2];
                } else {
                    // split at space   
                    var text1 = text.substr(0, posSpace);
                    var text2 = text.substr(posSpace);
                    me.appBase.get("Logger").logDebug("split space text1:'" + text1 + "' text2:''" + text2 + "'");
                    sentences = [text1, text2];
                }
            } else {
                // add text
                sentences.push(text);
            }
            
            // add nextText
            sentences.push(nextText);
        } else {
            // split text by splitter
            me.appBase.get("Logger").logDebug("split by:'" + splitter + "' ebene:" + ebene);
            sentences = text.split(splitter);
        }
        
        // iterate sentences
        for (var i=0;i< sentences.length;i++) {
            if (sentences[i].length > maxLength) {
                // sentence  > maxLength: split it with next splitChar
                me.appBase.get("Logger").logDebug("split new " +  i + " sentences[i]:" + sentences[i])
                me._splitOrSpeakText(sentences[i], splitterStr, ebene+1);
            } else {
                // sentence ok: say it
                me.appBase.get("Logger").logDebug("say i: " + i + " sentences[i]:" + sentences[i])
                me._speakText(sentences[i] + splitter);
            }
        }
    }

    // init all
    me._init();
    
    return me;
};