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

JsHelferlein.LoggerService = function(appBase, config) {
    // my own instance
    var me = JsHelferlein.ServiceBase(appBase, config, JsHelferlein.LoggerConfig());
    
    /**
     * initialize the object
     */
    me._init = function() {
        // my props
        me.console = {};
        
        // bietet Browser console an ??
        if (window.console) {
            me.console = window.console;

            // IE has a console that has a 'log' function but no 'debug'. to make console.debug work in IE,
            // we just map the function. (extend for info etc if needed)
            if (!window.console.debug && typeof window.console.log !== 'undefined') {
                window.console.debug = window.console.log;
            }
        }

        // ... and create all functions we expect the console to have (took from firebug).
        var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml",
                     "group", "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];

        for (var i = 0; i < names.length; ++i){
            if(!me.console[names[i]]){
                me.console[names[i]] = function() {};
            }
        }

        // eigene Console oeffnen
        me.ownConsoleElement = null;
        if (me.config.flgOwnConsole) {
            me.ownConsoleElement = me._initOwnConsole();
        }

        // WebLogger-Url
        me.webLoggerUrl = me.config.webLoggerUrl;

        /*
         * Root-Logger initialisieren
         */
        me.isError = true;
        me.isErrorWebLogger = false;
        me.isWarning = true;
        me.isWarningWebLogger = false;
        me.isInfo = false;
        me.isInfoWebLogger = false;
        me.isDebug = false;
        me.isDebugWebLogger = false;
        me.LoggerRoot = false;
    };

    /**
     * Loggen von Fehlern
     * - falls ownConsoleElement aktiviert - Logging in eigene Console mit logOwnConsole
     * - falls webLoggerUrl und isErrorWebLoogger aktiviert - Logging auf WebLogger mit logWebLogger
     * @param text
     */
    me.logError = function(text) {
        me.console.error(" ERROR:" + text);
        if (me.ownConsoleElement) { me._logOwnConsole(text); }
        if (me.webLoggerUrl && me.isErrorWebLogger) { me._logWebLogger("ERROR", text); }
    };

    /**
     * Loggen von Warnungen
     * - falls ownConsoleElement aktiviert - Logging in eigene Console mit logOwnConsole
     * - falls webLoggerUrl und isWarningWebLoogger aktiviert - Logging auf WebLogger mit logWebLogger
     * @param ext
     */
    me.logWarning = function(text) {
        me.console.warn(" WARNING:" + text);
        if (me.ownConsoleElement) { me._logOwnConsole(text); }
        if (me.webLoggerUrl && me.isWarningWebLogger) { me._logWebLogger("WARNING", text); }
    };
    
    /**
     * Loggen von Infos
     * - falls ownConsoleElement aktiviert - Logging in eigene Console mit logOwnConsole
     * - falls webLoggerUrl und isInfoWebLoogger aktiviert - Logging auf WebLogger mit logWebLogger
     * @param ext
     */
    me.logInfo = function(text) {
        me.console.info(" INFO:" + text);
        if (me.ownConsoleElement) { me._logOwnConsole(text); }
        if (me.webLoggerUrl && me.isInfoWebLogger) { me._logWebLogger("INFO", text); }
    };
    
    /**
     * Loggen von Debugmeldungen
     * - falls ownConsoleElement aktiviert - Logging in eigene Console mit logOwnConsole
     * - falls webLoggerUrl und isDebugWebLoogger aktiviert - Logging auf WebLogger mit logWebLogger
     * @param ext
     */
    me.logDebug = function(text) {
        me.console.debug(" DEBUG:" + text);
        if (me.ownConsoleElement) { me._logOwnConsole(text); }
        if (me.webLoggerUrl && me.isDebugWebLogger) { me._logWebLogger("DEBUG", text); }
    };


    /**
     * Initalisieren der eigenen LogConsole
     * @returns HtmlElement
     */
    me._initOwnConsole = function () {
        var consoleWindowElement = null;
        try {
            // Consolenfenster oeffnen
            var consoleWindow = window.open('', 'LoggerOwnConsole', 'height=400,width=650,resizable=yes,scrollbars=yes');

            // falls existent: bestehende Textarea abfragen
            consoleWindowElement = consoleWindow.document.getElementById("LoggerOwnConsoleDiv");

            // falls nicht existent: neue Textarea anlegen
            if (! consoleWindowElement) {
                consoleWindow.document.write("<textarea id='LoggerOwnConsoleDiv' cols='80' rows='40'></textarea>");
                consoleWindowElement = consoleWindow.document.getElementById("LoggerOwnConsoleDiv");
            }
        } catch (e) {
            me.logError("JMSLOGGER.initOwnConsole cant open Console window: " + e);
        }
        return consoleWindowElement;
    };

    /**
     * falls ownConsoleElement aktiviert, dann Anhaengen der Lognmeldung an die Textarea
     * @param text
     */
    me._logOwnConsole = function(text) {
        if (me.ownConsoleElement) {
            try {
                me.ownConsoleElement.value = me.ownConsoleElement.value + "\n" + text;
            } catch (e) {
            }
        }
    };

    /**
     * falls webLoggerUrl aktiviert, dann Insert eines iframe mit der Id:weblogger der webLoggerUrl einbindet
     * @param text
     */
    me._logWebLogger = function(logLevel, text) {
        if (me.webLoggerUrl) {
            try {
                // Logurl erzegen
                var url = me.webLoggerUrl + "LOGLEVEL=" + logLevel + "&LOGMSG=" + text + "&LOGURL=" + window.location;

                // neues Logelement erzeugen
                var logElement = document.createElement('script');
                logElement.src = url;
                parent = document.getElementsByTagName('script')[0];
                parent.parentNode.insertBefore(logElement, parent);

            } catch (e) {
                //alert("Exception:" + e)
            }
        }
    };

    // init all
    me._init();

    return me;
};