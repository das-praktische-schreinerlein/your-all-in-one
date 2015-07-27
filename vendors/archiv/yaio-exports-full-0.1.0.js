/*! yaio - v0.1.0 - 2015-07-27 */'use strict';


    /**
     * fuegt FormRowToggler an Elternelement an
     */
    JMATPageLayout.prototype.appendFormrowToggler = function(parentId, togglerBaseId, toggleClassName, label) {
        var html = jMATService.getPageLayoutService().createFormrowToggler(
                togglerBaseId, toggleClassName, 
                label + "<span class='icon-formrowtoggler icon-formrowtoggleron'>&nbsp;</span>", 
                label + "<span class='icon-formrowtoggler icon-formrowtoggleroff'>&nbsp;</span>", "", "");
        jMATService.getJMSServiceObj().appendHtml(html,parentId, "formrowToggler");
    };

    /**
     * show InputRow
     * @param eleInputRow
     * @returns {Boolean}
     */
    JMSLayout.prototype.showInputRow = function(eleInputRow) {
        if (! eleInputRow) {
           return false;
        }
        eleInputRow.style.display = "inline-block";
        return true;
    };

    /**
     * liefert Status eines Input-Elements
     * @param eleInput  INPUT-HTMLElement
     * return belegt trzue/false
     */
    JMSLayout.prototype.getStateInputElement = function(eleInput) {
        var state = false;

        if (eleInput.nodeName.toUpperCase() == "SELECT") {
            // Select-Box
            if (eleInput.value && (eleInput.value !== "search_all.php")) {
                state = true;
            } else {
                // Multiselect auswerten
                for (var i = 0; i < eleInput.length; i++) {
                    if (eleInput.options[i].selected && eleInput.options[i].value && (eleInput.options[i].value !== "search_all.php")) {
                        state = true;
                        i = eleInput.length + 1;
                    }
                }
            }
        } else if (eleInput.nodeName.toUpperCase() == "INPUT") {
           // Element als Radio/Checkbox suchen
           if (eleInput.type.toUpperCase() == "RADIO") {
              if (eleInput.checked) {
                  state = true;
              }
           } else if (eleInput.type.toUpperCase() == "CHECKBOX") {
              if (eleInput.checked) {
                  state = true;
              }
           } else if (eleInput && eleInput.value) {
              // normales Eingabefeld
              state = true;
           }
        } else if (eleInput.nodeName.toUpperCase() == "TEXTAREA") {
            // Element als textarea suchen
            if (eleInput && eleInput.value) {
               // normales Eingabefeld
               state = true;
            }
         }

        return state;
    };

    
    JMSLayout.prototype.isInputRowsSet = function(className) {
        // InputRows anhand des Classnames abfragen
        var lstInputRows = this.getInputRows(className);
        if (! lstInputRows || lstInputRows.length <= 0) {
           return null;
        }
        var state = false;

        // alle InputRows iterieren
        for (var i = 0; i < lstInputRows.length; ++i){
           // InputRow verarbeiten
           var eleInputRow = lstInputRows[i];
           state = state || this.getState4InputRow(eleInputRow);
           console.log("state=" + state + " for " + eleInputRow.id);
        }
        return state;
     };
    
window.JsHelferlein = {};

JsHelferlein.AppBase = function(config) {
    'use strict';

    // my own instance
    var me = {};
    
    me._init = function () {
        // check Config
        me.config = me.checkConfig(config, JsHelferlein.AppBaseConfig());

        me._configureDefaultServices();
        me._configureDefaultDetectors();
    };
    
    // vars
    me.serviceConfigs = [];
    me.services = [];
    me.detectors = [];
    me.props = [];
    me.config = {};

    me.configureService = function(serviceName, constrCallBack) {
        return me.serviceConfigs[serviceName] = constrCallBack;
    };

    me.getServiceConfig = function(serviceName) {
        return me.serviceConfigs[serviceName];
    };

    me.setService = function(serviceName, serviceInstance) {
        me[serviceName] = serviceInstance;
        return me.services[serviceName] = serviceInstance;
    };

    me.getService = function(serviceName) {
        if (! me.services.hasOwnProperty(serviceName)) {
            if (me.serviceConfigs.hasOwnProperty(serviceName)) {
                var constr = me.getServiceConfig(serviceName);
                me.setService(serviceName, constr(me));
            }
        }
        return me.services[serviceName];
    };
    
    me.set = function (serviceName) {
        return me.setService(serviceName);
    };

    me.get = function (serviceName) {
        return me.getService(serviceName);
    };
    

    me.setDetector = function(detectorName, detectorInstance) {
        return me.detectors[detectorName] = detectorInstance;
    };

    me.getDetector = function(detectorName) {
        return me.detectors[detectorName];
    };
    
    
    me.setProp = function(propName, value) {
        return me.props[propName] = value;
    };

    me.getProp = function(propName) {
        return me.props[propName];
    };
    
    me.checkConfig = function(config, defaultConfig) {
        if (! config) {
            console.log("no config: use default:", defaultConfig);
            return defaultConfig;
        }
        console.log("config set use:", config);
        for (var propName in defaultConfig) {
            if (! config.hasOwnProperty(propName)) {
                console.log("option not set: use default: " + propName, defaultConfig[propName]);
                config[propName] = defaultConfig[propName]; 
            }
        }
        
        return config;
    };
    
    
    me.publishDetectorStyles = function() {
        var styles = "";
        console.log("detectrors",me.detectors);
        for (var detectorName in me.detectors) {
            console.log("detectror" + detectorName);
            styles += me.getDetector(detectorName).generateStyles();
        }
        me.get('DOMHelper').insertStyleBeforeScript(styles);
        me.get('DOMHelper').appendStyleAtEnd(styles);
        if (me.get('Logger') && me.get('Logger').isDebug) {
            me.get('Logger').logDebug("JsHelferlein.AppBase.publishDetectorStyles " + styles);
        }
    };
    
    me._configureDefaultServices = function() {
        me.jQuery = $;
        me.configureService("jQuery", function() { return me.jQuery; });
        me.configureService("Logger", function() { return JsHelferlein.LoggerService(me); });
        me.configureService("DOMHelper", function() { return JsHelferlein.DOMHelperService(me); });
        me.configureService("UIToggler", function() { return JsHelferlein.UIToggler(me); });
        me.configureService("SpeechSynthHelper", function() { return JsHelferlein.SpeechSynthHelperService(me); });
        me.configureService("SpeechRecognitionHelper", function() { return JsHelferlein.SpeechRecognitionHelperService(me); });
    };

    me._configureDefaultDetectors = function() {
        me.setDetector("JavascriptDetector", JsHelferlein.JavascriptDetector(me));
        me.setDetector("SpeechSynthDetector", JsHelferlein.SpeechSynthDetector(me));
        me.setDetector("SpeechRecognitionDetector", JsHelferlein.SpeechRecognitionDetector(me));
    };

    // init all
    me._init();

    return me;
};
JsHelferlein.AppBaseConfig = function() {
    'use strict';

    // my own instance
    var me = JsHelferlein.ConfigBase();
    
    me.defaultDetectorStyleNS    = 'jsh-';
    me.additionalDetectorStyleNS = [];
    me.appBaseVarName            = 'jsHelferlein';
    
    return me;
};
JsHelferlein.ConfigBase = function() {
    'use strict';

    // my own instance
    var me = {};
    
    
    me.isSet = function(option) {
        if (! me.hasOwnProperty(option)) {
            return false;
        }
        if (! me[option]) {
            return false;
        }
        
        return true;
    };

    return me;
};
JsHelferlein.DetectorBase = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = {};
    
    me._init = function() {
        // check Config
        me.appBase = appBase;
        me.config = appBase.checkConfig(config, defaultConfig);
    };

    me.isSupported = function() {
        return false;
    };
    
    me.publishDetectorStyles = function() {
        var styles = me.generateStyles();
        me.appBase.get('DOMHelper').insertStyleBeforeScript(styles);
        me.appBase.get('DOMHelper').appendStyleAtEnd(styles);
        if (me.appBase.get('Logger') && me.appBase.get('Logger').isDebug) {
            me.appBase.get('Logger').logDebug("JsHelferlein.DetectorBase.publishDetectorStyles " + styles);
        }
    };

    me.generateStyles = function() {
        var flgSet = me.isSupported();
        // generate default NS
        var styles = me._generateStylesForNS(me.appBase.config.defaultDetectorStyleNS, me.config.styleBaseName, flgSet);
        
        // generate additional NS
        if (me.appBase.config.isSet('additionalDetectorStyleNS')) {
            var additionalNS = me.appBase.config.additionalDetectorStyleNS;
            for (var index = 0; index < additionalNS.length; ++index) {
                styles += me._generateStylesForNS(additionalNS[index], me.config.styleBaseName, flgSet);
            }
        }
        
        return styles;
    };
    
    me._generateStylesForNS = function(ns, name, flgSet) {
        var styles = "";
        
        styles += "/** " + ns + " " + name + "**/\n";
        if (flgSet) {
            styles += "." + ns + "is-" + name + " { display: block;}\n";
        } else {
            styles += "." + ns + "is-not-" + name + " { display: block;}\n";
        }
        styles += "." + ns + "show-if-" + name + " { display: " + (flgSet ? "block" : "none") + ";}\n";
        styles += "." + ns + "show-inline-if-" + name + " { display: " + (flgSet ? "inline" : "none") + ";}\n";
        styles += "." + ns + "show-inline-block-if-" + name + " { display: " + (flgSet ? "inline-block" : "none") + ";}\n";
        styles += "." + ns + "hide-if-" + name + " { " + (flgSet ? "display: none;" : "") + "}\n";
        
        return styles;
    };

    me._init();

    return me;
};
JsHelferlein.DetectorBaseConfig = function() {
    'use strict';

    // my own instance
    var me = JsHelferlein.ConfigBase();
    
    me.styleBaseName = "misconfigured-detector";

    return me;
};
JsHelferlein.ServiceBase = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = {};
    
    me._init = function() {
        // check Config
        me.appBase = appBase;
        me.config = appBase.checkConfig(config, defaultConfig);
    };

    me._init();
    
    return me;
};
JsHelferlein.CookieHelperService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * Cookie erzeugen
     * @param Name
     * @param Wert
     * @param Gueltigkeit in Tagen
     */
    me.writeCookie = function(name,value,days, path) {
        // wie lange gueltig??
        var expires;
        if (days) {
            var date = new Date();
            date.setTime(date.getTime()+(days*24*60*60*1000));
            expires = "; expires="+date.toGMTString();
        } else {
            expires = "";
        }
        // Coockie setzen
        document.cookie = name+"="+value+expires+"; path=/";
    };

    /**
     * Cookie einlesen
     * @param: Name des Coockies
     * @return Wert des Coockies
     */
    me.readCookie = function(name) {
        // Vollen Namen mit = suchen
        var nameFull = name + "=";
        var cookie = document.cookie;
        var ca = cookie.split(';');
        if (ca.length == 0) {
            ca = [cookie];
        }
        for(var i=0;i < ca.length;i++) {
            var c = ca[i];

            // Leerzechen entfernen
            while (c.charAt(0)==' ') {
                c = c.substring(1,c.length);
            }

            // Value extrahieren
            if (c.indexOf(nameFull) == 0) {
                return c.substring(nameFull.length,c.length);
            }
        }
        return null;
    };

    // init all
    me._init();

    return me;
};
JsHelferlein.DOMHelperService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);
    
    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * fuegt ein Style an
     * @param styles: CSS-Styles 
     * @param parentId: Id des Elternelements
     * @return {Boolean} falls angefuegt
     */
    me.appendStyle = function(styles, parentId) {
        // neues Stylelement erzeugen
        var newStyle = document.createElement("style");
        newStyle.setAttribute("type", "text/css");
        var flgDone = false;
        var parent = document.getElementById(parentId);
        if (parent) {
            parent.appendChild(newStyle);
            // erst belegen, wenn im DM-Baum (wegen IE)
            if (newStyle.styleSheet) {
                // IE
                newStyle.styleSheet.cssText = styles;
            } else {
                // the world
                var textStyles = document.createTextNode(styles);
                newStyle.appendChild(textStyles);
            }
            flgDone = true;
        }
        return flgDone;
    };

    /**
     * fuegt ein HTML-Element an
     * @param html: HTML
     * @param parentId: Id des Elternelements
     * @param className: falls belegt - CSS-Klasse des neuen Elements
     * @return {Boolean} falls angefuegt
     */
    me.appendHtml = function(html, parentId, className) {
        // neues Htmllement erzeugen
        var newDiv = document.createElement("div");
        var flgDone = false;
        if (parentId) {
            var parentElement = document.getElementById(parentId);
            if (parentElement) {
                parentElement.appendChild(newDiv);
                // erst belegen, wenn im DM-Baum (wegen IE)
                newDiv.innerHTML = html;
                if (className) {
                    newDiv.className = className;
                }
                flgDone = true;
            }
        }
        return flgDone;
    };

    /**
     * fuegt ein Style vor dem 1. JavaScript-Block ein
     * @param styles: CSS-Styles 
     * @return {Boolean} falls angefuegt
     */
    me.insertStyleBeforeScript = function(styles) {
        // neues Stylelement erzeugen
        var newStyle = document.createElement("style");
        newStyle.setAttribute("type", "text/css");
        var flgDone = false;
        var firstScriptTag = document.getElementsByTagName('script')[0];
        if (firstScriptTag) {
            firstScriptTag.parentNode.insertBefore(newStyle, firstScriptTag);
            if (newStyle.styleSheet) {   // IE
                newStyle.styleSheet.cssText = styles;
            } else {                // the world
                var textStyles = document.createTextNode(styles);
                newStyle.appendChild(textStyles);
            }
            flgDone = true;
        }
        return flgDone;
    };

    /**
     * fuegt ein Style am Ende der Seite an (body)
     * @param styles: CSS-Styles 
     * @return {Boolean} falls angefuegt
     */
    me.appendStyleAtEnd = function(styles) {
        // neues Stylelement erzeugen
        var newStyle = document.createElement("style");
        newStyle.setAttribute("type", "text/css");
        var flgDone = false;
        var bodyTag = document.getElementsByTagName('body')[0];
        if (bodyTag) {
            bodyTag.appendChild(newStyle);
            if (newStyle.styleSheet) {   // IE
                newStyle.styleSheet.cssText = styles;
            } else {                // the world
                var textStyles = document.createTextNode(styles);
                newStyle.appendChild(textStyles);
            }
            flgDone = true;
        }
        return flgDone;
    };
    
    /**
     * sucht alle Elemente mit den Styklenames und setzt das uebergeben Event bei OnClick
     * @param classNames - Array von Stylenamen
     * @param event - Eventfunktion
     * @param force - auch wenn schon belegt, ueberschreiben
     */
    me.addLinkOnClickEvent = function(classNames, event, force){
        try {
            // alle Klassen iterieren
            for (var i = 0; i < classNames.length; i++) {
                var className = classNames[i];
                // Links suche und iterieren
                var links = document.getElementsByClassName(className);
                for (var j = 0; j < links.length; j++) {
                    // Elemente iterieren
                    var link = links[j];
                    if ((! link.onclick) || force) {
                        // entweder nicht definiert, oder Force
                        if (me.appBase.get("Logger") && me.appBase.get("Logger").isDebug) {
                            me.appBase.get("Logger").logDebug(
                                "DOMHelper.addLinkOnClickEvent set a.onclick() for "
                                + className
                                + " Id:" + link.id
                                + " with event");
                        }
                        link.onclickold = link.onclick;
                        link.onclick = event;
                    } else {
                        // nicht definiert
                        if (me.appBase.get("Logger") && me.appBase.get("Logger").isDebug) {
                            me.appBase.get("Logger").logDebug(
                                "DOMHelper.addLinkOnClickEvent cant set a.onclick() for "
                                + className
                                + " Id:" + link.id
                                + " with event already defined");
                        }
                    }
                }
            }
        } catch (ex) {
            if (me.appBase.get("Logger") && me.appBase.get("Logger").isError) {
                me.appBase.get("Logger").logError("DOMHelper.addLinkOnClickEvent set a.onclick() Exception: " + ex);
            }
        }
    };

    // init all
    me._init();

    return me;
};
JsHelferlein.JavascriptDetector = function(appBase, config) {
    'use strict';

    // my own instance
    var me = JsHelferlein.DetectorBase(appBase, config, JsHelferlein.JavascriptDetectorConfig());
    
    /**
     * initialize the object
     */
    me._init = function() {
    };

    me.isSupported = function() {
        return true;
    };
    
    // init all
    me._init();
    
    return me;
};
JsHelferlein.JavascriptDetectorConfig = function() {
    'use strict';

    // my own instance
    var me = JsHelferlein.DetectorBaseConfig();
    
    me.styleBaseName = "javascript";

    return me;
};
JsHelferlein.LoggerConfig = function() {
    'use strict';

    // my own instance
    var me = JsHelferlein.ConfigBase();
    
    me.flgOwnConsole = false;
    me.webLoggerUrl  = undefined;
    
    return me;
};
JsHelferlein.LoggerService = function(appBase, config) {
    'use strict';

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
JsHelferlein.SpeechRecognitionDetectorConfig = function() {
    'use strict';

    // my own instance
    var me = JsHelferlein.DetectorBaseConfig();
    
    me.styleBaseName = "speechrecognition";

    return me;
};
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
            document.getElementById(me.config.finalTextareaId).value = $(opener.targetElement).text();
            if (! document.getElementById(me.config.finalTextareaId).value) {
                document.getElementById(me.config.finalTextareaId).value = $(opener.targetElement).val();
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
            if (me.appBase.get("Logger") && me.appBase.get("Logger").isWarning) {
                me.appBase.get("Logger").logWarning("JsHelferlein.SpeechRecognitionHelper.initUi: speechsynth not suppoorted");
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
JsHelferlein.SpeechSynthConfig = function() {
    'use strict';

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
            if (me.appBase.get("Logger") && me.appBase.get("Logger").isError) {
                me.appBase.get("Logger").logError("JsHelferlein.SpeechSynthHelper.isSpeechSynthSupported Exception: " + ex);
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
JsHelferlein.SpeechSynthDetectorConfig = function() {
    'use strict';

    // my own instance
    var me = JsHelferlein.DetectorBaseConfig();
    
    me.styleBaseName = "speechsynth";

    return me;
};
JsHelferlein.SpeechSynthHelperService = function(appBase, config) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase, config, JsHelferlein.SpeechSynthConfig());
    
    /**
     * initialize the object
     */
    me._init = function() {
    };

    me.initSrcFromOpener = function() {
        // Text vom Opener holen
        if (opener && opener.targetElement) {
            document.getElementById(me.config.finalTextareaId).value = $(opener.targetElement).text();
            if (! document.getElementById(me.config.finalTextareaId).value) {
                document.getElementById(me.config.finalTextareaId).value = $(opener.targetElement).val();
            }
        }
    };
    
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
    };

    me.pauseSpeech = function() {
        window.speechSynthesis.pause();
        if (me.config.isSet('buttonPauseId')) {
            document.getElementById(me.config.buttonPauseId).setAttribute('disabled', 'disabled');
        }
        if (me.config.isSet('buttonResumeId')) {
            document.getElementById(me.config.buttonResumeId).removeAttribute('disabled');
        }
    };
    
    me.resumeSpeech = function() {
        window.speechSynthesis.resume();
        if (me.config.isSet('buttonPauseId')) {
            document.getElementById(me.config.buttonPauseId).removeAttribute('disabled');
        }
        if (me.config.isSet('buttonResumeId')) {
            document.getElementById(me.config.buttonResumeId).setAttribute('disabled', 'disabled');
        }
    };

    me.stopSpeech = function() {
        for (var i=1; i < 100; i++) {
            window.speechSynthesis.cancel(); // if it errors, this clears out the error.
        }
    };

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
        };
        msg.onpause = function (event) {
            me.appBase.get("Logger").logDebug('paused ' + event);
        };
        msg.onboundary = function (event) {
            me.appBase.get("Logger").logDebug('onboundary ' + event);
        };

        return msg;
    };

    me._speakText = function(text) {
        var speaker = me._createSpeaker();
        me.appBase.get("Logger").logDebug("say text: " + text);
        speaker.text = text;
        window.speechSynthesis.speak(speaker);
    };

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
            } while (wordIndex < me.config.splitWords.length && text.length > maxLength);
            
            // fallback if text > maxLength
            if (text.length > maxLength) {
                // attempt to split a " " before maxLength
                var posSpace = text.indexOf(" ");
                var text1, text2;
                if ((posSpace <= 0 || posSpace > maxLength)) {
                    // not " " before maxLength -> do it hard
                    text1 = text.substr(0, maxLength);
                    text2 = text.substr(maxLength);
                    me.appBase.get("Logger").logDebug("split texthard text1:'" + text1 + "' text2:''" + text2 + "'");
                    sentences = [text1, text2];
                } else {
                    // split at space   
                    text1 = text.substr(0, posSpace);
                    text2 = text.substr(posSpace);
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
                me.appBase.get("Logger").logDebug("split new " +  i + " sentences[i]:" + sentences[i]);
                me._splitOrSpeakText(sentences[i], splitterStr, ebene+1);
            } else {
                // sentence ok: say it
                me.appBase.get("Logger").logDebug("say i: " + i + " sentences[i]:" + sentences[i]);
                me._speakText(sentences[i] + splitter);
            }
        }
    };

    // init all
    me._init();
    
    return me;
};
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
window.YaioAppBaseConfig = function() {
    'use strict';

    // my own instance
    var me = JsHelferlein.AppBaseConfig();
    
    me.appBaseVarName               = 'yaioAppBase';
    
    me.additionalDetectorStyleNS    = ["yaio-"];

    me.CONST_MasterId               = "MasterplanMasternode1";
    me.loginUrl                     = "/yaio-explorerapp/yaio-explorerapp.html#/login";
    me.baseUrl                      = "/nodes/";
    me.showUrl                      = me.baseUrl + "show/";
    me.symLinkUrl                   = me.baseUrl + "showsymlink/";
    me.updateUrl                    = me.baseUrl + "update/";
    me.createUrl                    = me.baseUrl + "create/";
    me.moveUrl                      = me.baseUrl + "move/";
    me.removeUrl                    = me.baseUrl + "delete/";

    me.configNodeTypeFields         = {
        Create: {
            fields: [
                { fieldName: "className", type: "hidden"},
                { fieldName: "sysUID", type: "hidden"}
            ]
        },
        CreateSymlink: {
            fields: [
                { fieldName: "name", type: "input"},
                { fieldName: "type", type: "hidden"},
                { fieldName: "className", type: "hidden"},
                { fieldName: "sysUID", type: "hidden"},
                { fieldName: "symLinkRef", type: "input"},
                { fieldName: "symLinkName", type: "input"},
                { fieldName: "symLinkTags", type: "textarea"},
                { fieldName: "mode", type: "hidden", intern: true}
            ]
        },
        CreateSnapshot: {
            fields: [
                { fieldName: "name", type: "input"},
                { fieldName: "type", type: "hidden"},
                { fieldName: "className", type: "hidden"},
                { fieldName: "sysUID", type: "hidden"},
                { fieldName: "nodeDesc", type: "textarea"},
                { fieldName: "mode", type: "hidden", intern: true}
            ]
        },
        Common: {
            fields: [
                { fieldName: "className", type: "hidden"},
                { fieldName: "sysUID", type: "hidden"},
                { fieldName: "mode", type: "hidden", intern: true},
                { fieldName: "type", type: "select"},
                { fieldName: "state", type: "select"},
                { fieldName: "nodeDesc", type: "textarea"}
            ]
        },
        TaskNode: {
            fields: [
                { fieldName: "name", type: "input"},
                { fieldName: "istAufwand", type: "input"},
                { fieldName: "istStand", type: "input"},
                { fieldName: "istStart", type: "input", datatype: "date"},
                { fieldName: "istEnde", type: "input", datatype: "date"},
                { fieldName: "planAufwand", type: "input"},
                { fieldName: "planStart", type: "input", datatype: "date"},
                { fieldName: "planEnde", type: "input", datatype: "date"}
            ]
        },
        EventNode: {
            fields: [
                { fieldName: "name", type: "input"},
                { fieldName: "istAufwand", type: "input"},
                { fieldName: "istStand", type: "input"},
                { fieldName: "istStart", type: "input", datatype: "datetime"},
                { fieldName: "istEnde", type: "input", datatype: "datetime"},
                { fieldName: "planAufwand", type: "input"},
                { fieldName: "planStart", type: "input", datatype: "datetime"},
                { fieldName: "planEnde", type: "input", datatype: "datetime"}
            ]
        },
        InfoNode: {
            fields: [
                { fieldName: "name", type: "textarea"},
                { fieldName: "docLayoutTagCommand", type: "select"},
                { fieldName: "docLayoutAddStyleClass", type: "input"},
                { fieldName: "docLayoutShortName", type: "input"},
                { fieldName: "docLayoutFlgCloseDiv", type: "checkbox"}
            ]
        },
        UrlResNode: {
            fields: [
                { fieldName: "name", type: "input"},
                { fieldName: "resLocRef", type: "input"},
                { fieldName: "resLocName", type: "input"},
                { fieldName: "resLocTags", type: "textarea"},
                { fieldName: "docLayoutTagCommand", type: "select"},
                { fieldName: "docLayoutAddStyleClass", type: "input"},
                { fieldName: "docLayoutShortName", type: "input"},
                { fieldName: "docLayoutFlgCloseDiv", type: "checkbox"}
            ]
        },
        SymLinkNode: {
            fields: [
                { fieldName: "name", type: "input"},
                { fieldName: "type", type: "hidden"},
                { fieldName: "symLinkRef", type: "input"},
                { fieldName: "symLinkName", type: "input"},
                { fieldName: "symLinkTags", type: "textarea"}
            ]
        }
    };

    return me;
};
window.Yaio = {};
window.YaioAppBase = function() {
    'use strict';

    // my own instance
    var me = JsHelferlein.AppBase(YaioAppBaseConfig());

    me._init = function () {
        me._configureDefaultServices();
    };
    
    me._configureDefaultServices = function() {
        me.configureService("YaioBase", function() { return Yaio.BaseService(me); });
        me.configureService("YaioLayout", function() { return Yaio.LayoutService(me); });
        me.configureService("YaioEditor", function() { return Yaio.EditorService(me); });
        me.configureService("YaioFormatter", function() { return Yaio.FormatterService(me); });
        me.configureService("YaioMarkdownEditor", function() { return Yaio.MarkdownEditorService(me); });
        me.configureService("YaioNodeData", function() { return Yaio.NodeDataService(me); });
        me.configureService("YaioNodeDataRender", function() { return Yaio.NodeDataRenderService(me); });
        me.configureService("YaioNodeGanttRender", function() { return Yaio.NodeGanttRenderService(me); });
        me.configureService("YaioExplorerAction", function() { return Yaio.ExplorerActionService(me); });
        me.configureService("YaioExplorerTree", function() { return Yaio.ExplorerTreeService(me); });
        me.configureService("YaioExportedData", function() { return Yaio.ExportedDataService(me); });
    };

    // init all
    me._init();

    return me;
};
/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     servicefunctions basics
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

/*****************************************
 *****************************************
 * Service-Funktions (layout)
 *****************************************
 *****************************************/
Yaio.BaseService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    me.toggleWithLinks = function(link1, link2, id1, id2) {
         if ($(id1).css("display") != "none") {
             $(id1).css("display", "none");
             $(link1).css("display", "inline");
             $(id2).css("display", "block");
             $(link2).css("display", "none");
         } else {
             $(id2).css("display", "none");
             $(link2).css("display", "inline");
             $(id1).css("display", "block");
             $(link1).css("display", "none");
         }
         return false;     
     };
    
     me.showModalErrorMessage = function(message) {
         // set messagetext
         $( "#error-message-text" ).html(message);
         
         // show message
         $( "#error-message" ).dialog({
             modal: true,
             buttons: {
               Ok: function() {
                 $( this ).dialog( "close" );
               }
             }
         });    
     };
    
     me.showModalConfirmDialog = function(message, yesHandler, noHandler) {
         // set messagetext
         $( "#dialog-confirm-text" ).html(message);
         
         // show message
         
         $( "#dialog-confirm" ).dialog({
             resizable: false,
             height:140,
             modal: true,
             buttons: {
               "Ja": function() {
                 $( this ).dialog( "close" );
                 if (yesHandler) {
                     yesHandler();
                 }
               },
               "Abbrechen": function() {
                 $( this ).dialog( "close" );
                 if (noHandler) {
                     noHandler();
                 }
               }
             }
         });
     };
    
    
    /*****************************************
     *****************************************
     * Service-Funktions (logging)
     *****************************************
     *****************************************/
     me.showToastMessage = function(type, title, message) {
         // show message
         toastr.options = {
                 "closeButton": true,
                 "debug": false,
                 "newestOnTop": true,
                 "progressBar": true,
                 "positionClass": "toast-top-right",
                 "preventDuplicates": false,
                 "showDuration": "300",
                 "hideDuration": "1000",
                 "timeOut": "10000",
                 "extendedTimeOut": "1000",
                 "showEasing": "swing",
                 "hideEasing": "linear",
                 "showMethod": "fadeIn",
                 "hideMethod": "fadeOut"
         };
         toastr[type](me.htmlEscapeText(message), title);
     };
    
    
    
    me.logError = function(message, flgShowDialog) {
        console.error(message);
        if (flgShowDialog) {
            me.showToastMessage("error", "Oops! Ein Fehlerchen :-(", me.htmlEscapeText(message));
    //        me.showModalErrorMessage(me.htmlEscapeText(message));
        }
    };
    
    /*****************************************
     *****************************************
     * Service-Funktions (data)
     *****************************************
     *****************************************/
    
    me.htmlEscapeText = function(text) {
        if ((text != "undefined") && (text != "") && (text != null)) {
            text = text.replace(/&/g, "&amp;");
            text = text.replace(/</g, "&lt;");
            text = text.replace(/>/g, "&gt;");
            text = text.replace(/"/g, "&quot;");
            text = text.replace(/'/g, "&#x27;");
            text = text.replace(/\//g, "&#x2F;");
        }
        return text;
    };
    
    me.htmlEscapeTextLazy = function(text) {
        if ((text != "undefined") && (text != "") && (text != null)) {
            text = text.replace(/</g, "&lt;");
            text = text.replace(/>/g, "&gt;");
        }
        return text;
    };
    
    me.formatGermanDateTime = function(millis) {
        if (millis == null) {
           return "";
        }
        var date = new Date(millis);
        return me.padNumber(date.getDate(), 2)
            + "." + me.padNumber(date.getMonth() + 1, 2)
            + "." + date.getFullYear()
            + " " + me.padNumber(date.getHours(), 2)
            + ":" + me.padNumber(date.getMinutes(), 2);
    };
    me.formatGermanDate = function(millis) {
        if (millis == null) {
           return "";
        }
        var date = new Date(millis);
        return me.padNumber(date.getDate(), 2)
            + "." + me.padNumber(date.getMonth() + 1, 2)
            + "." + date.getFullYear();
    };
    me.padNumber = function(number, count) {
        var r = String(number);
        while ( r.length < count) {
        r = '0' + r;
        }
        return r;
    };
    me.formatNumbers = function(number, nachkomma, suffix) {
       if (number == null) {
           return "";
       }
       
       return (number.toFixed(nachkomma)) + suffix;
    };
    
    me.downloadAsFile = function($link, data, fileName, mime, encoding) {
        if (mime == "undefind") {
            mime = "application/text";
        }
        if (encoding == "undefind") {
            mime = "uft-8";
        }
        // data URI
        var dataURI = 'data:' + mime + ';charset=' + encoding + ','
                + encodeURIComponent(data);
    
        // set link
        var flgSafeMode = 0;
        if (   (navigator.userAgent.indexOf("Trident") >= 0) 
            || (navigator.userAgent.indexOf("MSIE") >= 0)
            || flgSafeMode) {
           // IE or SafeMode
           var popup = window.open("");
           if (! popup) {
               // warn message
               me.logError("Leider kann der Download nicht angezeigt werden, da Ihr Popup-Blocker aktiv ist. Beachten Sie die Hinweise im Kopf des Browsers. ", true);
           } else {
               // set data to document
               $(popup.document.body).html("<pre>" + me.htmlEscapeTextLazy(data) + "</pre>");
           }
           return false;
       } else {
            // all expect IE
            $link.attr({
                'download' : fileName,
                'href' : dataURI,
                'target' : '_blank'
            });
       }
    };
    
    me.createXFrameAllowFrom = function() {
        return "x-frames-allow-from=" + window.location.hostname;
    };
    
    me.escapeRegExp = function(str) {
        return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
    };
    
    
    me._init();
    
    return me;
};
/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     servicefunctions for the editors
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */


/*****************************************
 *****************************************
 * Service-Funktions (editor)
 *****************************************
 *****************************************/
Yaio.EditorService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
        self.callUpdateTriggerForElement = me.callUpdateTriggerForElement;
    };



    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     reset editor (hide all form, empty all formfields)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: hide editor
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor
     */
    me.yaioResetNodeEditor = function() {
        // reset editor
        console.log("yaioResetNodeEditor: show tree, hide editor");
        
        // show full tree
        $("#containerYaioTree").css("width", "100%");
        
        // hide editor-container
        $("#containerYaioEditor").css("width", "100%");
        $("#containerYaioEditor").css("display", "none");
        
        // hide editor-box
        $("#containerBoxYaioEditor").css("width", "100%");
        $("#containerBoxYaioEditor").css("display", "none");
        
        // hide forms
        me.yaioHideAllNodeEditorForms();
        me.yaioResetNodeEditorFormFields();
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     hide all editor-forms
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: hide all editor-forms 
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor
     */
    me.yaioHideAllNodeEditorForms = function() {
        // reset editor
        console.log("yaioHideAllNodeEditorForms: hide forms");
        // hide forms
        $("#containerFormYaioEditorCreate").css("display", "none");
        $("#containerFormYaioEditorTaskNode").css("display", "none");
        $("#containerFormYaioEditorEventNode").css("display", "none");
        $("#containerFormYaioEditorInfoNode").css("display", "none");
        $("#containerFormYaioEditorUrlResNode").css("display", "none");
        $("#containerFormYaioEditorSymLinkNode").css("display", "none");
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     reset all formfields
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: empty all formfields 
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor
     */
    me.yaioResetNodeEditorFormFields = function() {
        // reset data
        // configure value mapping
        var basenode = {};
        for (var formName in me.appBase.config.configNodeTypeFields) {
            var fields = new Array();
            fields = fields.concat(me.appBase.config.configNodeTypeFields.Common.fields);
            fields = fields.concat(me.appBase.config.configNodeTypeFields[formName].fields);
            for (var idx in fields) {
                var field = fields[idx];
                me.yaioSetFormField(field, formName, basenode);
            }
        }
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     updates the formfield with the nodedata
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: updates formfield 
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor
     * @param field - fieldconfig from me.appBase.config.configNodeTypeFields
     * @param fieldSuffix - sufix of the fieldName to identify the form (nodeclass of basenode)
     * @param basenode - the node to map the fieldvalue
     */
    me.yaioSetFormField = function(field, fieldSuffix, basenode) {
        var fieldName = field.fieldName;
        var fieldNameId = "#input" + fieldName.charAt(0).toUpperCase() + fieldName.slice(1) + fieldSuffix;
        var value = basenode[fieldName];
        
        // convert value
        if (field.datatype === "integer" && (! value || value == "undefined" || value === null)) {
            // specical int
            value = 0;
        } else if (field.datatype === "date")  {
            // date
            value = me.appBase.get('YaioBase').formatGermanDate(value);
        } else if (field.datatype === "datetime")  {
            // date
            value = me.appBase.get('YaioBase').formatGermanDateTime(value);
        } else if (! value || value == "undefined" || value == null) {
            // alle other
            value = "";
        } 
        
        // reescape data for form
        if (fieldName === "nodeDesc") {
            value = value.replace(/<WLBR>/g, "\n");
            value = value.replace(/<WLESC>/g, "\\");
            value = value.replace(/<WLTAB>/g, "\t");
        }
        
        // set depending on the fieldtype
        if (field.type === "hidden") {
            $(fieldNameId).val(value).trigger('input').triggerHandler("change");
        } else if (field.type === "select") {
            $(fieldNameId).val(value).trigger('select').triggerHandler("change");
        } else if (field.type === "checkbox") {
            if (value) {
                $(fieldNameId).prop("checked", true);
            } else {
                $(fieldNameId).prop("checked", false);
            }
            $(fieldNameId).trigger('input').triggerHandler("change");
        } else if (field.type === "textarea") {
            $(fieldNameId).val(value).trigger('select').triggerHandler("change");
        } else {
            // input
            $(fieldNameId).val(value).trigger('input');
        }
        console.log("yaioSetFormField map nodefield:" + fieldName 
                + " set:" + fieldNameId + "=" + value);
        
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     open the nodeeditor for the node (toggle it fromleft), transfer the data from node to the formfields  
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: reset forms+field, hide forms, open the spcific form for the nodeclass, updates fields
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor
     * @param nodeId - id of the node
     * @param mode - edit, create, createsymlink
     */
    me.yaioOpenNodeEditor = function(nodeId, mode) {
        // reset editor
        console.log("yaioOpenNodeEditor: reset editor");
        me.yaioResetNodeEditor();
        
        // check vars
        if (! nodeId) {
            // tree not found
            me.appBase.get('YaioBase').logError("error yaioOpenNodeEditor: nodeId required", false);
            return null;
        }
        // load node
        var tree = $("#tree").fancytree("getTree");
        if (!tree) {
            // tree not found
            me.appBase.get('YaioBase').logError("error yaioOpenNodeEditor: cant load tree for node:" + nodeId, false);
            return null;
        }
        var treeNode = tree.getNodeByKey(nodeId);
        if (! treeNode) {
            me.appBase.get('YaioBase').logError("error yaioOpenNodeEditor: cant load node:" + nodeId, false);
            return null;
        }
        
        // extract nodedata
        var basenode = treeNode.data.basenode;
        
        // open editor
        me.yaioOpenNodeEditorForNode(basenode, mode);
    };
        
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     open the nodeeditor for the node (toggle it fromleft), transfer the data from node to the formfields  
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: reset forms+field, hide forms, open the spcific form for the nodeclass, updates fields
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor
     * @param basenode - the node
     * @param mode - edit, create, createsymlink
     * @param newNode - optional node to copy data from (for mode createsnapshot...)
     */
    me.yaioOpenNodeEditorForNode = function(basenode, mode, newNode) {
        // reset editor
        console.log("yaioOpenNodeEditor: reset editor");
        me.yaioResetNodeEditor();
        
        // check vars
        if (! basenode) {
            // tree not found
            me.appBase.get('YaioBase').logError("error yaioOpenNodeEditor: basenode required", false);
            return null;
        }
        var nodeId = basenode['sysUID'];
    
        // check mode    
        var fields = new Array();
        var formSuffix, fieldSuffix;
        var origBasenode = basenode;
        if (mode === "edit") {
            // mode edit
            
            // configure value mapping
            fields = fields.concat(me.appBase.config.configNodeTypeFields.Common.fields);
            if (basenode.className === "TaskNode") {
                fields = fields.concat(me.appBase.config.configNodeTypeFields.TaskNode.fields);
            } else if (basenode.className === "EventNode") {
                fields = fields.concat(me.appBase.config.configNodeTypeFields.EventNode.fields);
            } else if (basenode.className === "InfoNode") {
                fields = fields.concat(me.appBase.config.configNodeTypeFields.InfoNode.fields);
            }  else if (basenode.className === "UrlResNode") {
                fields = fields.concat(me.appBase.config.configNodeTypeFields.UrlResNode.fields);
            }  else if (basenode.className === "SymLinkNode") {
                fields = fields.concat(me.appBase.config.configNodeTypeFields.SymLinkNode.fields);
            }
            
            // set formSuffix
            formSuffix = basenode.className;
            fieldSuffix = basenode.className;
            basenode.mode = "edit";
            console.log("yaioOpenNodeEditor mode=edit for node:" + nodeId);
        } else if (mode === "create") {
            // mode create
            formSuffix = "Create";
            fieldSuffix = "Create";
            fields = fields.concat(me.appBase.config.configNodeTypeFields.Create.fields);
            
            // new basenode
            basenode = {
                    mode: "create",
                    sysUID: origBasenode.sysUID
            };
            console.log("yaioOpenNodeEditor mode=create for node:" + nodeId);
        } else if (mode === "createsymlink") {
            // mode create
            formSuffix = "SymLinkNode";
            fieldSuffix = "SymLinkNode";
            fields = fields.concat(me.appBase.config.configNodeTypeFields.CreateSymlink.fields);
    
            // new basenode
            basenode = {
                    mode: "create",
                    sysUID: origBasenode.sysUID,
                    name: "Symlink auf: '" + origBasenode.name + "'",
                    type: "SYMLINK",
                    state: "SYMLINK",
                    className: "SymLinkNode",
                    symLinkRef: origBasenode.metaNodePraefix + "" + origBasenode.metaNodeNummer
            };
            console.log("yaioOpenNodeEditor mode=createsymlink for node:" + nodeId);
        } else if (mode === "createsnapshot") {
            // mode create
            formSuffix = "InfoNode";
            fieldSuffix = "InfoNode";
            fields = fields.concat(me.appBase.config.configNodeTypeFields.CreateSnapshot.fields);
    
            // new basenode
            basenode = {
                    mode: "create",
                    sysUID: origBasenode.sysUID,
                    name: "Snapshot für: '" + origBasenode.name + "' vom " + me.appBase.get('YaioBase').formatGermanDateTime((new Date()).getTime()),
                    type: "INFO",
                    state: "INFO",
                    className: "InfoNode",
                    nodeDesc: newNode.nodeDesc
            };
            console.error("yaioOpenNodeEditor mode=createsnapshot for node:" + nodeId);
        } else {
            me.appBase.get('YaioBase').logError("error yaioOpenNodeEditor: unknown mode=" + mode 
                    + " for nodeId:" + nodeId, false);
            return null;
        }
        
        // iterate fields
        for (var idx in fields) {
            var field = fields[idx];
            me.yaioSetFormField(field, fieldSuffix, basenode);
        }
        
        // show editor
        var width = $("#box_data").width();
        console.log("yaioOpenNodeEditor show editor: " + formSuffix 
                + " for node:" + nodeId);
    
        // set width
        $("#containerYaioEditor").css("width", "900px");
        $("#containerBoxYaioEditor").css("width", "900px");
        $("#containerYaioTree").css("width", (width - $("#containerYaioEditor").width() - 30) + "px");
        
        // display editor and form for the formSuffix
        $("#containerBoxYaioEditor").css("display", "block");
        $("#containerFormYaioEditor" + formSuffix).css("display", "block");
        //$("#containerYaioEditor").css("display", "block");
        me.appBase.get('UIToggler').toggleElement("#containerYaioEditor");
    
        // create Elements if not exists
        me.appBase.get('YaioLayout').createTogglerIfNotExists("legendIstTaskForm", "filterIstTaskForm", "filter_IstTaskNode");
        me.appBase.get('YaioLayout').createTogglerIfNotExists("legendDescTaskForm", "filterDescTaskForm", "filter_DescTaskNode");
        me.appBase.get('YaioLayout').createTogglerIfNotExists("legendIstEventForm", "filterIstEventForm", "filter_IstEventNode");
        me.appBase.get('YaioLayout').createTogglerIfNotExists("legendDescEventForm", "filterDescEventForm", "filter_DescEventNode");
        me.appBase.get('YaioLayout').createTogglerIfNotExists("legendLayoutInfoForm", "filterLayoutInfoForm", "filter_LayoutInfoNode");
        me.appBase.get('YaioLayout').createTogglerIfNotExists("legendDescInfoForm", "filterDescInfoForm", "filter_DescInfoNode");
        me.appBase.get('YaioLayout').createTogglerIfNotExists("legendLayoutUrlResForm", "filterLayoutUrlResForm", "filter_LayoutUrlResNode");
        me.appBase.get('YaioLayout').createTogglerIfNotExists("legendDescUrlResForm", "filterDescUrlResForm", "filter_DescUrlResNode");
        me.appBase.get('YaioLayout').createTogglerIfNotExists("legendDescSymLinkForm", "filterDescSymLinkForm", "filter_DescSymLinkNode");
        
        // hide empty, optional elements
        me.appBase.get('YaioLayout').hideFormRowTogglerIfSet("filterIstTaskForm", "filter_IstTaskNode", false);
        me.appBase.get('YaioLayout').hideFormRowTogglerIfSet("filterDescTaskForm", "filter_DescTaskNode", false);
        me.appBase.get('YaioLayout').hideFormRowTogglerIfSet("filterIstEventForm", "filter_IstEventNode", false);
        me.appBase.get('YaioLayout').hideFormRowTogglerIfSet("filterDescEventForm", "filter_DescEventNode", false);
        me.appBase.get('YaioLayout').hideFormRowTogglerIfSet("filterLayoutInfoForm", "filter_LayoutInfoNode", false);
        me.appBase.get('YaioLayout').hideFormRowTogglerIfSet("filterDescInfoForm", "filter_DescInfoNode", false);
        me.appBase.get('YaioLayout').hideFormRowTogglerIfSet("filterLayoutUrlResForm", "filter_LayoutUrlResNode", false);
        me.appBase.get('YaioLayout').hideFormRowTogglerIfSet("filterDescUrlResForm", "filter_DescUrlResNode", false);
        me.appBase.get('YaioLayout').hideFormRowTogglerIfSet("filterDescSymLinkForm", "filter_DescSymLinkNode", false);
    
        // create nodeDesc-editor
        me.appBase.get('YaioMarkdownEditor').createNodeDescEditorForNode("editorInputNodeDescTaskNode", "inputNodeDescTaskNode");
        me.appBase.get('YaioMarkdownEditor').createNodeDescEditorForNode("editorInputNodeDescEventNode", "inputNodeDescEventNode");
        me.appBase.get('YaioMarkdownEditor').createNodeDescEditorForNode("editorInputNodeDescInfoNode", "inputNodeDescInfoNode");
        me.appBase.get('YaioMarkdownEditor').createNodeDescEditorForNode("editorInputNodeDescUrlResNode", "inputNodeDescUrlResNode");
        me.appBase.get('YaioMarkdownEditor').createNodeDescEditorForNode("editorInputNodeDescSymLinkNode", "inputNodeDescSymLinkNode");
        
        // update appsize
        me.appBase.get('YaioLayout').setupAppSize();
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     close the nodeditor, toggle it to the left
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: close the editor
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree Editor
     */
    me.yaioCloseNodeEditor = function() {
        console.log("close editor");
        me.appBase.get('UIToggler').toggleElement("#containerYaioEditor");
        me.yaioResetNodeEditor();
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     a hack to call updatetrigger for the element because for speechregognition the popup
     *     cant call the trigger for another window (security)<br>
     *     the function binds to the current document-window
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: calls updatetrigger for element
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor SpeechRecognition
     * @param element - element (HTML-Element) to fire the trigger
     */
    me.callUpdateTriggerForElement = function(element) {
        if (element != null) {
            $(element).trigger('input').triggerHandler("change");
            $(element).trigger('select').triggerHandler("change");
            $(element).trigger('input');
            $(element).focus();
        }
    };
    
    
    /*****************************************
     *****************************************
     * Service-Funktions (businesslogic)
     *****************************************
     *****************************************/
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     recalcs the istStand depending on the state/type
     *     if ERLEDIGT || VERWORFEN || EVENT_ERLEDIGT || EVENT_VERWORFEN: update istStand=100
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>ReturnValue Integer - the recalced stand
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param basenode - the node to recalc
     * @return istStand in %
     */
    me.calcIstStandFromState = function(basenode) {
        var istStand = basenode.istStand;
        if (   basenode.type == "EVENT_ERLEDIGT"
            || basenode.type == "EVENT_VERWORFEN"
            || basenode.type == "ERLEDIGT"
            || basenode.type == "VERWORFEN") {
            istStand = 100;
        }
        console.log("calcIstStandFromState for node:" + basenode.sysUID + " state=" + basenode.type + " new istStand=" + istStand);
        
        return istStand;
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     recalcs the type/state depending on the istStand
     *     <ul>
     *       <li>if className=TaskNode && 0: update type=OFFEN
     *       <li>if className=TaskNode && >0&&<100 && ! WARNING: update type=RUNNING
     *       <li>if className=TaskNode && 100 && != VERWORFEN: update type=ERLEDIGT
     *       <li>if className=EventNode && 0: update type=EVENT_PLANED
     *       <li>if className=EventNode && >0&&<100 && ! EVENT_WARNING: update type=EVENT_RUNNING
     *       <li>if className=EventNode && 100 && != EVENT_VERWORFEN: update type=EVENT_ERLEDIGT
     *     </ul>
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>ReturnValue String - the recalced type/state
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param basenode - the node to recalc
     * @return the recalced type
     */
    me.calcTypeFromIstStand = function(basenode) {
        var type = basenode.type;
    
        if (basenode.className == "TaskNode") {
            // TaskNode
            if (basenode.istStand == "0") {
                // 0: OFFEN
                type = "OFFEN"; 
            } else if (basenode.istStand == 100 && basenode.type !== "VERWORFEN") {
                // 100: ERLEDIGT if not VERWORFEN already
                type = "ERLEDIGT"; 
            } else if (basenode.istStand < 100 && basenode.istStand > 0) {
                // 0<istStand<100: RUNNING if not WARNING already
                if (basenode.type !== "WARNING") {
                    type = "RUNNING"; 
                }
            }
        } else if (basenode.className == "EventNode") {
            // EventNode
            if (basenode.istStand == "0") {
                // 0: EVENT_PLANED
                type = "EVENT_PLANED"; 
            } else if (basenode.istStand == 100 && basenode.type !== "EVENT_VERWORFEN") {
                // 100: EVENT_ERLEDIGT if not EVENT_VERWORFEN already
                type = "EVENT_ERLEDIGT"; 
            } else if (basenode.istStand < 100 && basenode.istStand > 0) {
                // 0<istStand<100: EVENT_RUNNING if not EVENT_WARNING already
                if (basenode.type !== "EVENT_WARNING") {
                    type = "EVENT_RUNNING"; 
                }
            }
        }
        console.log("calcTypeFromIstStand for node:" + basenode.sysUID + " istStand=" + basenode.istStand + " newstate=" + type);
        
        return type;
    };
    
    me._init();
    
    return me;
};
/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     servicefunctions for layout
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

Yaio.LayoutService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };


    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     add speechRecognition to name+nodeDesc-Label if availiable<br>
     *     set the flg webkitSpeechRecognitionAdded on the element, so that there is no doubling
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: add speechrecognition to elements
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor SpeechRecognition
     */
    me.addSpeechRecognitionToElements = function() {
        // add speechrecognition if availiable
        if (me.appBase.getDetector('SpeechRecognitionDetector').isSupported()) {
            // add speechrecognition to nodeDesc+name
            $("label[for='nodeDesc'], label[for='name']").append(function (idx) {
                var link = "";
                var label = this;
                
                // check if already set
                if ($(label).attr("webkitSpeechRecognitionAdded")) {
                    console.error("addSpeechRecognitionToElements: SKIP because already added: " + $(label).attr("for"));
                    return link;
                }
    
                // get corresponding form
                var forName = $(label).attr("for");
                var form = $(label).closest("form");
                
                // get for-element byName from form
                var forElement = form.find("[name="+ forName + "]").first();
                if (forElement.length > 0) {
                    // define link to label
                    link = "<a href=\"\" class=\"\"" +
                        " onClick=\"yaioAppBase.get('YaioLayout').openSpeechRecognitionWindow(" +
                            "document.getElementById('" + forElement.attr('id') + "')); return false;" +
                        "\" lang='tech' data-tooltip='tooltip.command.OpenSpeechRecognition'>" +
                        "<img alt='Spracherkennung nutzen' style='width:25px'" +
                            " src='" + me.appBase.getService('SpeechRecognitionHelper').config.statusImgSrcStart + "'></a>";
                    
                    // set flag
                    $(label).attr("webkitSpeechRecognitionAdded", "true");
                    console.log("addSpeechRecognitionToElements: add : " + forName + " for " + forElement.attr('id'));
                }
                return link;
            });
        }
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     open speechrecognition for element
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: open speechrecognition for element
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor SpeechRecognition
     * @param target - target-element to update (HTML-Element)
     */
    me.openSpeechRecognitionWindow = function(target) {
        if (target == null) { target = self; }
        target.focus();
        var speechrecognitionWindow = window.open('speechrecognition.html', "speechrecognition", "width=690,height=350,resizable=yes,dependent=yes,scrollbars=yes");
        speechrecognitionWindow.focus();
        if (speechrecognitionWindow.opener == null) { speechrecognitionWindow.opener = self; }
        speechrecognitionWindow.opener.targetElement = target;
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     add speechSynth to nodeDesc-Label if availiable<br>
     *     set the flg speechSynthAdded on the element, so that there is no doubling
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: add speechSynth to elements
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor SpeechSynth
     */
    me.addSpeechSynthToElements = function() {
        // add speechSynth if availiable
        if (me.appBase.getDetector('SpeechSynthDetector').isSupported()) {
            // add speechrecognition to nodeDesc+name
            $("label[for='nodeDesc']").append(function (idx) {
                var link = "";
                var label = this;
                
                // check if already set
                if ($(label).attr("speechSynthAdded")) {
                    console.error("addSpeechSynthToElements: SKIP because already added: " + $(label).attr("for"));
                    return link;
                }
    
                // get corresponding form
                var forName = $(label).attr("for");
                var form = $(label).closest("form");
                
                // get for-element byName from form
                var forElement = form.find("[name="+ forName + "]").first();
                if (forElement.length > 0) {
                    // define link to label
                    link = "<a href=\"\" class=\"button\"" +
                           " onClick=\"yaioAppBase.get('YaioLayout').openSpeechSynthWindow(" +
                            "document.getElementById('" + forElement.attr('id') + "')); return false;" +
                           "\" lang='tech' data-tooltip='tooltip.command.OpenSpeechSynth' class='button'>common.command.OpenSpeechSynth</a>";
                    
                    // set flag
                    $(label).attr("speechSynthAdded", "true");
                    console.log("addSpeechSynthToElements: add : " + forName + " for " + forElement.attr('id'));
                }
                return link;
            });
        }
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     open speechsynth for element
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: open speechsynth for element
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor SpeechSynth
     * @param target - target-element to update (HTML-Element)
     */
    me.openSpeechSynthWindow = function(target) {
        if (target == null) { target = self; }
        target.focus();
        var speechsynthWindow = window.open('speechsynth.html', "speechsynth", "width=690,height=350,resizable=yes,dependent=yes,scrollbars=yes");
        speechsynthWindow.focus();
        if (speechsynthWindow.opener == null) { speechsynthWindow.opener = self; }
        speechsynthWindow.opener.targetElement = target;
    };
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     add datepicker to all input-elements with styleclass inputtype_date and inputtype_datetime
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: add datepicker
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor DatePicker
     */
    me.addDatePickerToElements = function() {
        // add datepicker to all dateinput
        $.datepicker.setDefaults($.datepicker.regional['de']);
        $.timepicker.regional['de'] = {
                timeOnlyTitle: 'Uhrzeit auswählen',
                timeText: 'Zeit',
                hourText: 'Stunde',
                minuteText: 'Minute',
                secondText: 'Sekunde',
                currentText: 'Jetzt',
                closeText: 'Auswählen',
                ampm: false
              };
        $.timepicker.setDefaults($.timepicker.regional['de']);    
        $('input.inputtype_date').datepicker();
        $('input.inputtype_datetime').datetimepicker();
    };
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     add styleselectbox to all input-elements with styleclass inputtype_docLayoutAddStyleClass
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: add styleselectbox after input
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor
     */
    me.addDocLayoutStyleSelectorToElements = function() {
        // iterate over docLayoutSDtyleClass-elements
        $("input.inputtype_docLayoutAddStyleClass").each(function () {
            // add select only if id id set
            var ele = this;
            var id = $(ele).attr("id");
            if (id) {
                // add select
                var $select = $("<select id='" + id + "_select' lang='tech' />");
                
                // append values
                $select.append($("<option value=''>Standardstyle</option>"));
                $select.append($("<option value='row-label-value'>row-label-value</option>"));
                $select.append($("<option value='row-label-value'>row-label-value</option>"));
                $select.append($("<option value='row-boldlabel-value'>row-boldlabel-value</option>"));
                $select.append($("<option value='row-value-only-full'>row-value-only-full</option>"));
                $select.append($("<option value='row-label-only-full'>row-label-only-full</option>"));
                
                // add changehandler
                $select.change(function() {
                    // set new value
                    var style = $(this).val();
                    $(ele).val(style);
                    
                    // call updatetrigger
                    window.callUpdateTriggerForElement(ele);
                });
                
                // insert select after input
                $(ele).after($select);
            }
            
        });
    };
    
    me.addPreviewToElements = function() {
        // add preview to nodeDesc
        $("label[for='nodeDesc']").append(function (idx) {
            var link = "";
            var label = this;
            
            // check if already set
            if ($(label).attr("previewAdded")) {
                console.error("addPreviewElements: SKIP because already added: " + $(label).attr("for"));
                return link;
            }
   
            // get corresponding form
            var forName = $(label).attr("for");
            var form = $(label).closest("form");
            
            // get for-element byName from form
            var forElement = form.find("[name="+ forName + "]").first();
            if (forElement.length > 0) {
                // define link to label
                link = "<a href=\"#\" id='showPreview4" + forElement.attr('id') + "'" +
                       " onClick=\"yaioAppBase.get('YaioMarkdownEditor').showPreviewForTextareaId('" +
                          forElement.attr('id') + "'); return false;" +
                       "\" lang='tech' data-tooltip='tooltip.command.OpenPreview' class='button'>common.command.OpenPreview</a>";
                link += "<a href=\"#\" id='openMarkdownHelp4" + forElement.attr('id') + "'" +
                        " onClick=\"yaioAppBase.get('YaioMarkdownEditor').showMarkdownHelp(); return false;" +
                        "\" lang='tech' data-tooltip='tooltip.command.OpenMarkdownHelp' class='button'>common.command.OpenMarkdownHelp</a>";
                
                // set flag
                $(label).attr("previewAdded", "true");
                console.log("addPreviewToElements: add : " + forName + " for " + forElement.attr('id'));
            }
            return link;
        });
    };
    
     
     
    me.addWysiwhgToElements = function() {
        // add preview to nodeDesc
        $("label[for='nodeDesc']").append(function (idx) {
            var link = "";
            var label = this;
            
            // check if already set
            if ($(label).attr("wysiwhgAdded")) {
                console.error("addWysiwhgElements: SKIP because already added: " + $(label).attr("for"));
                return link;
            }
   
            // get corresponding form
            var forName = $(label).attr("for");
            var form = $(label).closest("form");
            
            // get for-element byName from form
            var forElement = form.find("[name="+ forName + "]").first();
            if (forElement.length > 0) {
                // define link to label
                link = "<a href=\"#\" id='openWysiwyg4" + forElement.attr('id') + "'" +
                    " onClick=\"yaioAppBase.get('YaioMarkdownEditor').openWysiwhgForTextareaId('" +
                        forElement.attr('id') + "'); return false;" +
                    "\" lang='tech' data-tooltip='tooltip.command.OpenWysiwygEditor' class='button'>common.command.OpenWysiwygEditor</a>";
                
                // set flag
                $(label).attr("wysiwhgAdded", "true");
                console.log("addWysiwhgToElements: add : " + forName + " for " + forElement.attr('id'));
            }
            return link;
        });
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     init the multilanguage support for all tags with attribute <XX lang="de">
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>GUI-result: init multilanguage-support
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Editor Multilanguagesupport
     * @param langKey - key of the preferred-language
     */
    me.initLanguageSupport = function(langKey) {
        // Create language switcher instance and set default language to tech
        window.lang = new Lang('tech');
    
        //Define the de language pack as a dynamic pack to be loaded on demand
        //if the user asks to change to that language. We pass the two-letter language
        //code and the path to the language pack js file
        window.lang.dynamic('de', 'lang/lang-tech-to-de.json');
        window.lang.dynamic('en', 'lang/lang-tech-to-en.json');
        window.lang.loadPack('de');
        window.lang.loadPack('en');
    
        // change to de
        window.lang.change(langKey);
    };
    
    me.setupAppSize = function() {
        var height = window.innerHeight;
        var width = window.innerWidth;
        
        // YAIO-editor
        var ele = $("#containerBoxYaioEditor");
        if (ele.length > 0) {
            // we are relative to the tree
            var paddingToHead = $("#containerYaioTree").position().top;
            var left = $("#containerYaioTree").position().left + $("#containerYaioTree").width + 2;
    
            // set posTop as scrollTop burt never < paddingToHead
            var posTop = $(window).scrollTop();
            if (posTop < paddingToHead) {
                posTop = paddingToHead;
            }
            
            // calc maxHeight = windHeight - 20 (puffer)
            var maxHeight = height - 20;
            // sub topPos - Scollpos
            maxHeight = maxHeight - (posTop - $(window).scrollTop());
    
            // set values
            $(ele).css("position", "absolute");
            $(ele).css("max-height", maxHeight);
            $(ele).css("top", posTop);
            $(ele).css("left", left);
            
            console.log("setup size containerBoxYaioEditor width:" + window.innerWidth 
                    + " height:" + window.innerHeight 
                    + " scrollTop:" + $(window).scrollTop()
                    + " offset.top" + $(ele).offset().top
                    + " top:" + posTop
                    + " max-height:" + $(ele).css("max-height")
                    );
        }
        
        // Export-editor
        ele = $("#containerFormYaioEditorOutputOptions");
        if (ele.length > 0) {
            $(ele).css("max-height", height-$(ele).offset().top);
            console.log("setup size containerFormYaioEditorOutputOptions width:" + window.innerWidth 
                    + " height:" + window.innerHeight 
                    + " scrollTop:" + $(window).scrollTop()
                    + " offset.top" + $(ele).offset().top
                    + " max-height:" + $(ele).css("max-height")
                    );
        }
        // Import-editor
        ele = $("#containerFormYaioEditorImport");
        if (ele.length > 0) {
            $(ele).css("max-height", height-$(ele).offset().top);
            console.log("setup size containerFormYaioEditorImport width:" + window.innerWidth 
                    + " height:" + window.innerHeight 
                    + " scrollTop:" + $(window).scrollTop()
                    + " offset.top" + $(ele).offset().top
                    + " max-height:" + $(ele).css("max-height")
                    );
        }
    
        // Frontpage
        ele = $("#front-content-intro");
        if (0 && ele.length > 0) {
            var maxHeight = height-$(ele).offset().top;
            
            // sub todonextbox
            if ($('#box_todonext').length > 0 ) {
                if ($('#box_todonext').height > 0) {
                    maxHeight = maxHeight - $('#box_todonext').height;
                } else {
                    // sometime height is not set: then default
                    maxHeight = maxHeight - 100;
                }
            }
            $(ele).css("max-height", maxHeight);
            console.log("setup size front-content-intro width:" + window.innerWidth 
                    + " height:" + window.innerHeight 
                    + " scrollTop:" + $(window).scrollTop()
                    + " offset.top" + $(ele).offset().top
                    + " max-height:" + $(ele).css("max-height")
                    );
        }
    };
    
    me.yaioShowHelpSite = function(url) {
        // set messagetext
        url += "?" + me.appBase.get('YaioBase').createXFrameAllowFrom();
        console.log("yaioShowHelpSite:" + url);
        $("#help-iframe").attr('src',url);
        
        // show message
        $( "#help-box" ).dialog({
            modal: true,
            width: "800px",
            buttons: {
              "Schliessen": function() {
                $( this ).dialog( "close" );
              },
              "Eigenes Fenster": function() {
                  var helpFenster = window.open(url, "help", "width=750,height=500,scrollbars=yes,resizable=yes");
                  helpFenster.focus();
                  $( this ).dialog( "close" );
                }
            }
        });    
    };
    
    me.hideFormRowTogglerIfSet = function(togglerId, className, state) {
        if (jMATService.getLayoutService().isInputRowsSet(className)) {
            // show all
            jMATService.getPageLayoutService().toggleFormrows(togglerId, className, true);
            
            // hide toggler
            $("#" + togglerId + "_On").css('display', 'none');
            $("#" + togglerId + "_Off").css('display', 'none');
        } else {
            // show or hide ??
            $("#" + togglerId + "_On").css('display', 'none');
            $("#" + togglerId + "_Off").css('display', 'block');
            jMATService.getPageLayoutService().toggleFormrows(togglerId, className, state);
        }
    };
     
    me.createTogglerIfNotExists = function(parentId, toggleId, className) {
        var $ele = $("#" + toggleId + "_On");
        if ($ele.length <= 0) {
            // create toggler
            console.log("createTogglerIfNotExists link not exists: create new toggler parent=" + parentId 
                    + " toggleEleId=" + toggleId
                    + " className=" + className);
            jMATService.getPageLayoutService().appendFormrowToggler(parentId, toggleId, className, "&nbsp;");
        } else {
            console.log("createTogglerIfNotExists link exists: skip new toggler parent=" + parentId 
                    + " toggleEleId=" + toggleId
                    + " className=" + className);
        }
    };
     
    me.togglePrintLayout = function() {
        if ($("#checkboxPrintAll").prop('checked')) {
            // print all
            $("#link_css_dataonly").attr("disabled", "disabled");
            $("#link_css_dataonly").prop("disabled", true);
        } else  {
            // print data only
            $("#link_css_dataonly").removeAttr("disabled");
            $("#link_css_dataonly").prop("disabled", false);
        }
    };

    me._init();
    
    return me;
};
 

/* require '/js/highlightjs' */
/* require '/js/jquery' */
/* require '/js/marked' */
/* require '/freemind-flash' */

/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     servicefunctions for formatting (markdown, diagramms, mindmaps..)
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.FormatterService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };


    me._localHtmlId = 1;
    
    // states
    me.checkListConfigs = {
        "checklist-state-OPEN": {
            styleClassPraefix: "checklist-state-",
            matchers: ["OPEN", "OFFEN", "o", "O", "0", "TODO"]
        },
        "checklist-state-RUNNING": {
            styleClassPraefix: "checklist-state-",
            matchers: ["RUNNING"]
        },
        "checklist-state-LATE": {
            styleClassPraefix: "checklist-state-",
            matchers: ["LATE"]
        },
        "checklist-state-BLOCKED": {
            styleClassPraefix: "checklist-state-",
            matchers: ["BLOCKED", "WAITING", "WAIT"]
        },
        "checklist-state-WARNING": {
            styleClassPraefix: "checklist-state-",
            matchers: ["WARNING"]
        },
        "checklist-state-DONE": {
            styleClassPraefix: "checklist-state-",
            matchers: ["DONE", "OK", "x", "X", "ERLEDIGT"]
        },
        "checklist-test-TESTOPEN": {
            styleClassPraefix: "checklist-test-",
            matchers: ["TESTOPEN"]
        },
        "checklist-test-PASSED": {
            styleClassPraefix: "checklist-test-",
            matchers: ["PASSED"]
        },
        "checklist-test-FAILED": {
            styleClassPraefix: "checklist-test-",
            matchers: ["FAILED", "ERROR"]
        }
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     format the descText as Markdown
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - formatted markdown
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param descText - the string to format
     * @param flgHighlightNow - if is set do syntax-highlighting while markdown-processing, if not set do it later
     * @param headerPrefix - headerPrefix for heading-ids
     * @return {String} - formatted markdown
     */
    me.formatMarkdown = function(descText, flgHighlightNow, headerPrefix) {
        // prepare descText
        descText = me.prepareTextForMarkdown(descText);
        
        var renderer = new marked.Renderer();
        // my own code-handler
        renderer.code = function (code, language) {
            code = me.appBase.get('YaioBase').htmlEscapeTextLazy(code);
            if (code.match(/^sequenceDiagram/) || code.match(/^graph/) || code.match(/^gantt/)) {
                return '<div id="inlineMermaid' + (me._localHtmlId++) + '" class="mermaid">'+ me.prepareTextForMermaid(code ) + '</div>';
            } else if (language !== undefined 
                       && (language.match(/^yaiomindmap/) || language.match(/^yaiofreemind/))) {
                return '<div id="inlineMindmap' + (me._localHtmlId++) + '"  class="yaiomindmap">'+ code + '</div>';
            } else {
                return '<pre><code id="inlineCode' + (me._localHtmlId++) + '" class="lang-' + language + '">' + code + '</code></pre>';
            }
        };
        
        // my own heading-handler to be sure that the heading id is unique
        renderer.heading = function(text, level, raw) {
          return '<h' + level 
            + ' id="' + this.options.headerPrefix + '_' + (me._localHtmlId++) + '_' + raw.toLowerCase().replace(/[^\w]+/g, '-')
            + '">'
            + text
            + '</h' + level + '>\n';
        };
        
        // my own link: for yaio
        renderer.link = function(href, title, text) {
            var prot; 
            if (this.options.sanitize) {
              try {
                prot = decodeURIComponent(unescape(href))
                  .replace(/[^\w:]/g, '')
                  .toLowerCase();
              } catch (e) {
                return '';
              }
              if (prot && prot.indexOf('javascript:') === 0) {
                return '';
              }
              if (prot && prot.indexOf('yaio:') === 0) {
                  href = href.substr(5);
                  href="/yaio-explorerapp/yaio-explorerapp.html#/showByAllIds/" + href;
              }
            }
            var out = '<a href="' + href + '"';
            if (title) {
              out += ' title="' + title + '"';
            }
            out += '>' + text + '</a>';
            return out;
          };
        
        // Marked
        marked.setOptions({
          renderer: renderer,
          gfm: true,
          tables: true,
          breaks: false,
          pedantic: false,
          sanitize: true,
          smartLists: true,
          smartypants: false,
          headerPrefix: headerPrefix
        });
        if (flgHighlightNow) {
            marked.setOptions({
                highlight: function (code) {
                    return hljs.highlightAuto(code).value;
                }
            });
        }
        var descHtmlMarked = marked(descText);
        
        return descHtmlMarked;
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     prepare the text to format as markdown
     *     prefix empty lines inline code-segs (```) so that they will interpreted as codeline by markdown-parser
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - prepared text
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param descText   the string to prepare
     * @return {String}  prepared text to format as markdown
     */
    me.prepareTextForMarkdown = function(descText) {
        // prepare descText
        var noCode = "";
        var newDescText = '';
        var newDescTextRest = descText;
        var codeStart = newDescTextRest.indexOf("```");
        while (codeStart >= 0) {
            // splice start before ```and add to newDescText
            noCode = newDescTextRest.slice(0, codeStart + 3);
            
            // replace <> but prevent <br> in noCode
            noCode = me.appBase.get('YaioBase').htmlEscapeTextLazy(noCode);
            noCode = noCode.replace(/&lt;br&gt;/g, "<br>");
            newDescText += noCode;
            
            // extract code
            newDescTextRest = newDescTextRest.slice(codeStart + 3);
            var codeEnd = newDescTextRest.indexOf("```");
            if (codeEnd >= 0) {
                // splice all before ending ```
                var code = newDescTextRest.slice(0, codeEnd);
                newDescTextRest = newDescTextRest.slice(codeEnd);
                
                // replace empty lines in code
                code = code.replace(/\r\n/g, "\n");
                code = code.replace(/\n\r/g, "\n");
                code = code.replace(/\n[ \t]*\n/g, "\n.\n");
                code = code.replace(/\n\n/g, "\n.\n");
                
                // add code to newDescText
                newDescText += code;
                
                // extract ending ``` and add it to newDescText
                newDescText += newDescTextRest.slice(0, 3);
                newDescTextRest = newDescTextRest.slice(3);
            }
            codeStart = newDescTextRest.indexOf("```");
        }
    
        // replace <> but prevent <br> in noCode
        noCode = newDescTextRest;
        noCode = me.appBase.get('YaioBase').htmlEscapeTextLazy(noCode);
        noCode = noCode.replace(/&lt;br&gt;/g, "<br>");
        
        // add rest to newDescText
        newDescText += noCode;
        
        return newDescText;
    };
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     format the block-content as mindmap. 
     *     <ul>
     *     <li>creates a FlashObject /dist/vendors.vendorversion/freemind-flash/visorFreemind.swf
     *     <li>Calls /converters/mindmap with the html-content of the block
     *     <li>insert the returning flash-object into block-element 
     *     
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue shows Freemind-Flashviewer with the mindmap-content of block
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param block - jquery-html-element with the content to convert to mindmap 
     */
    me.formatYaioMindmap = function(block) {
        var content = $(block).html();
        var blockId = $(block).attr('id');
        var url = "/converters/mindmap?source=" + encodeURIComponent(content);
        console.log("formatYaioMindmap " + blockId + " url:" + url);
        
        var fo = new FlashObject("/dist/vendors.vendorversion/freemind-flash/visorFreemind.swf", "visorFreeMind", "100%", "100%", 6, "#9999ff");
        fo.addParam("quality", "high");
        fo.addParam("bgcolor", "#a0a0f0");
        fo.addVariable("openUrl", "_blank");
        fo.addVariable("startCollapsedToLevel","10");
        fo.addVariable("maxNodeWidth","200");
        //
        fo.addVariable("mainNodeShape","elipse");
        fo.addVariable("justMap","false");
        
        fo.addVariable("initLoadFile",url);
        fo.addVariable("defaultToolTipWordWrap",200);
        fo.addVariable("offsetX","left");
        fo.addVariable("offsetY","top");
        fo.addVariable("buttonsPos","top");
        fo.addVariable("min_alpha_buttons",20);
        fo.addVariable("max_alpha_buttons",100);
        fo.addVariable("scaleTooltips","false");
        fo.write(blockId);
    };
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     calls the global mermaid-formatter
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>formats all divs with class=mermaid
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     */
    me.formatMermaidGlobal = function() {
        mermaid.parseError = function(err,hash){
            me.appBase.get('YaioBase').showToastMessage("error", "Oops! Ein Fehlerchen :-(", "Syntaxfehler bei Parsen des Diagrammcodes:" + err);
    //        me.appBase.get('YaioBase').showModalErrorMessage(":" + err);
        };
        try {
            mermaid.init();
        } catch (ex) {
            console.error("formatMermaidGlobal error:" + ex);
        }
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     prepare the text to format as mermaid
     *     delete .
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - prepared text
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Layout
     * @param descText   the string to prepare
     * @return {String}  prepared text to format with mermaid
     */
    me.prepareTextForMermaid = function(descText) {
        // prepare descText
        var newDescText = descText;
        newDescText = newDescText.replace(/\n\.\n/g, "\n\n");
        return newDescText;
    };
        
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     convert the markdown-text to jira-format
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - jira-converted text
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Convert
     * @param descText  the string to prepare
     * @return {String} markdown-text in jira-format
     */
    me.convertMarkdownToJira = function(descText) {
        // prepare descText
        var newDescText = '';
        
        var separatedBlocks = descText.split("```");
        for (var i=0;i < separatedBlocks.length; i++) {
            var tmpText = separatedBlocks[i];
            if ((i % 2) == 0) {
                // text-block: do convert
                
                // add dummy \n
                tmpText = '\n' + tmpText;
                
                // lists
                tmpText = tmpText.replace(/\n    - /g, "\n-- ");
                tmpText = tmpText.replace(/\n        - /g, "\n--- ");
                tmpText = tmpText.replace(/\n            - /g, "\n---- ");
    
                // headings
                tmpText = tmpText.replace(/\n##### /g, "\nh5. ");
                tmpText = tmpText.replace(/\n#### /g, "\nh4. ");
                tmpText = tmpText.replace(/\n### /g, "\nh3. ");
                tmpText = tmpText.replace(/\n## /g, "\nh2. ");
                tmpText = tmpText.replace(/\n# /g, "\nh1. ");
                
                // delete dummy \n
                tmpText = tmpText.substr(1);
                newDescText += tmpText;
            } else {
                // code-block
                newDescText += '{code}' + tmpText + '{code}';
            }
        }
        
        return newDescText;
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     executes mermaid, highlight and checklist-formatter on the block,
     *     return a flag if a mermaid-block is found an mermaid should be executed
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Boolean - flag if a Mermaid-Block is found and mermaid should be executed
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Convert
     * @param descBlock - id-filter to identify the block to format
     * @return {boolean} - flag if a Mermaid-Block is found and mermaid should be executed
     */
    me.formatDescBlock = function(descBlock) {
        var flgDoMermaid = false;
        var descBlockId = $(descBlock).attr('id');
    
        console.log("formatDescBlock highlight for descBlock: " + descBlockId);
        // remove trigger-flag
        $(descBlock).removeClass('syntaxhighlighting-open');
        
        // higlight code-blocks
        $("#" + descBlockId + " code").each(function(i, block) {
            var blockId = $(block).attr('id');
            if ($(block).hasClass("lang-mermaid") || $(block).hasClass("mermaid")) {
                // mermaid: no highlight
                console.log("formatDescBlock mermaid descBlock: " + descBlockId + " block: " + blockId);
                me.addServicesToDiagrammBlock(block, 'mermaid',
                        "<a href='' id='linkdownload" + blockId + "'  target='_blank'"
                        +   " onclick=\"javascript: yaioAppBase.get('YaioBase').downloadAsFile($('#linkdownload" + blockId + "'), $('#" + blockId + "').html(), 'diagram.svg', 'image/svg+xml', 'utf-8'); return true;\">"
                        + "Download</a>");
                flgDoMermaid = true;
            } else {
                // do highlight
                console.log("formatDescBlock highlight descBlock: " + descBlockId + " block: " + blockId);
                hljs.highlightBlock(block);
            }
        });
    
        // mermaid/mindmap div-blocks
        $("#" + descBlockId + " div").each(function(i, block) {
            var blockId = $(block).attr('id');
            if (   ($(block).hasClass("lang-mermaid") || $(block).hasClass("mermaid")) 
                && ! $(block).attr("data-processed")) {
                // mermaid: no highlight
                console.log("formatDescBlock mermaid descBlock: " + descBlockId + " block: " + blockId);
                me.addServicesToDiagrammBlock(block, 'mermaid',
                        "<a href='' id='linkdownload" + blockId + "'  target='_blank'"
                        +   " onclick=\"javascript: yaioAppBase.get('YaioBase').downloadAsFile($('#linkdownload" + blockId + "'), $('#" + blockId + "').html(), 'diagram.svg', 'image/svg+xml', 'utf-8'); return true;\">"
                        + "Download</a>");
                flgDoMermaid = true;
            } else if ($(block).hasClass("lang-yaiomindmap") || $(block).hasClass("yaiomindmap")) {
                // mindmap: no highlight
                console.log("formatDescBlock yaiomindmap for descBlock: " + descBlockId + " block: " + blockId);
                var content = $(block).html();
                var url = "/converters/mindmap?source=" + encodeURIComponent(content);
                me.addServicesToDiagrammBlock(block, 'yaiomindmap', "<a href='" + url + "' id='download" + blockId + "' target='_blank'>Download</a>");
                me.formatYaioMindmap(block);
            }
        });
    
        // highlight checklist
        me.highlightCheckList(descBlock);
        
        return flgDoMermaid;
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     executes checklist-formatter (add span with checklist-Styles) on the block [use me.checkListConfigs]
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates DOM
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Convert
     * @param descBlock - id-filter to identify the block to format
     */
    me.highlightCheckList = function(descBlock) {
        var descBlockId = $(descBlock).attr('id');
        console.log("highlightCheckList highlight for descBlock: " + descBlockId);
    
        // tests
        for (var idx in me.checkListConfigs) {
            var matchers = me.checkListConfigs[idx].matchers;
            me.highlightCheckListForMatchers(descBlock, matchers, idx, '');
        }
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     executes checklist-formatter (add span with checklistFormat) with style and styleclass for all matchers "[XXX]" on descBlock
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates DOM
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Convert
     * @param descBlock  - id-filter to identify the block to format
     * @param matchers   - list of matcher which will call as stringfilter of "[" + matcher + "]" to identify checklist-entry
     * @param styleClass - styleClass to add to span for matcher found 
     * @param style      - style to add to new span for matcher found
     */
    me.highlightCheckListForMatchers = function(descBlock, matchers, styleClass, style) {
        var descBlockId = $(descBlock).attr('id');
        console.log("highlightCheckListForMatchers matchers '" + matchers + "' for descBlock: " + descBlockId);
        for (var idx in matchers) {
            me.highlightCheckListForMatcher(descBlock, "[" + matchers[idx] + "]", styleClass, style);
        }
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     executes checklist-formatter (add span with checklistFormat) with style and styleclass for all matchers "[XXX]" on descBlock
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates DOM
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Convert
     * @param descBlock  - id-filter to identify the block to format
     * @param matcherStr - matcher will call as stringfilter to identify checklist-entry
     * @param styleClass - styleClass to add to span for matcher found 
     * @param style      - style to add to new span for matcher found
     */
    me.highlightCheckListForMatcher = function(descBlock, matcherStr, styleClass, style) {
        var descBlockId = $(descBlock).attr('id');
        console.log("highlightCheckListForMatcher matcherStr '" + matcherStr + "' for descBlock: " + descBlockId);
        $("#" + descBlockId + " li:contains('" + matcherStr + "'),h1:contains('" + matcherStr + "'),h2:contains('" + matcherStr + "')").each(function(index, value) {
            var regEx = RegExp(me.appBase.get('YaioBase').escapeRegExp(matcherStr), 'gi');
            findAndReplaceDOMText($(value).get(0), {
                find: regEx,
                replace: function(portion) {
                    var el = document.createElement('span');
                    if (style) {
                        el.style = style;
                    }
                    if (styleClass) {
                        el.className = styleClass;
                    }
                    el.innerHTML = portion.text;
                    return el;
                }
            });
        });
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     extract data from explorerlines (table.fancytree-ext-table tr) and format 
     *     them as linked markdown-checklists ([state] - [title](yaio:number)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return String - checklist in yaio-markdown-format
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Convert
     * @return {String}  checklist in yaio-markdown-format
     */
    me.convertExplorerLinesAsCheckList = function() {
        // get title
        var title = $("#masterTr td.fieldtype_name").text();
        var now = me.appBase.get('YaioBase').formatGermanDateTime((new Date()).getTime());
    
        var checkList = "# Checklist: " + title + " (Stand: " + now + ")\n\n";
        
        // iterate all nodelines
        $("table.fancytree-ext-table tr").each(function(i, line) {
            // extract data
            var titleSpan = $(line).find("span.fancytree-title2");
            var stateSpan = $(line).find("span.fancytree-title-state");
            var numberSpan = $(line).find("div.field_metanummer");
            var levelSpan = $(line).find("span.fancytree-node");
            var istStandDiv = $(line).find("div.fieldtype_stand.field_istChildrenSumStand");
            var istAufwandDiv = $(line).find("div.fieldtype_aufwand.field_istChildrenSumAufwand");
            var planAufwandDiv = $(line).find("div.fieldtype_aufwand.field_planChildrenSumAufwand");
            
            // extract content
            var level = 0;
            var title = null;
            var state = null;
            var number = null;
            var istStand = "0%";
            var istAufwand = "0h";
            var planAufwand = null;
            if ($(levelSpan).size() > 0) {
                // extract level from intend
                var intend = $(levelSpan).css("margin-left").replace("px", "");
                level = parseInt(intend, 10) / 20;
            }
            if ($(stateSpan).size() > 0) {
                // extract state from style
                var idx = me.extractCheckListStatefromStateSpan($(stateSpan));
                if (idx) {
                    state = idx;
                    state = state.replace("checklist-state-", "");
                    state = state.replace("checklist-test-", "");
                }
            }
            if ($(titleSpan).size() > 0) {
                title = $(titleSpan).text();
            }
            if ($(numberSpan).size() > 0) {
                number = $(numberSpan).text();
            }
            
            if ($(istAufwandDiv).size() > 0) {
                istAufwand = $(istAufwandDiv).text();
            }
            if ($(planAufwandDiv).size() > 0) {
                planAufwand = $(planAufwandDiv).text();
            }
            if ($(istStandDiv).size() > 0) {
                istStand = $(istStandDiv).text();
            }
    
            var stand = istStand.trim() + " (" + istAufwand.trim();
            if (planAufwand) {
                stand  += "/" + planAufwand.trim();
            }
            stand += ")";
            
            // if all set: generate checklist
            console.log("state:" + state + " title:" + title + " number:" + number + " level:" + level + " stand:" + stand);
            if (title && state && number) {
                for (var idx = 0; idx < level; idx ++) {
                    checkList += "    ";
                }
                checkList += "- [" + state + "] - [" + title + "](yaio:" + number + ") " + stand + "\n";
            }
        });
        
        return checkList;
    };
    
    me.extractCheckListStatefromStateSpan = function(block) {
        // iterate all configs
        for (var idx in me.checkListConfigs) {
            var matchers = me.checkListConfigs[idx].matchers;
            
            // iterate all matchers
            for (var idx2 in matchers) {
                // check for matcher in style
                if (block.hasClass("node-state-" + matchers[idx2])) {
                    return idx;
                }
            }
        }
        return null;
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     extract data from explorerlines (table.fancytree-ext-table tr) and format 
     *     them as mermaid-gantt-markdown
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return String - mermaid-gantt-markdown
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Convert
     * @return {String}    mermaid-gantt-markdown
     */
    me.convertExplorerLinesAsGanttMarkdown = function() {
        // get title
        var title = $("#masterTr td.fieldtype_name").text();
        var now = me.appBase.get('YaioBase').formatGermanDateTime((new Date()).getTime());
    
        var ganttMarkdown = "# Gantt: " + title + " (Stand: " + now + ")\n\n"
            + "```mermaid\n"
            + "gantt\n"
            + "    title " + title + " (Stand: " + now + ")\n"
            + "    dateFormat  DD.MM.YYYY\n"
            + "\n";
        var ganttMarkdownPlan = "";
        var ganttMarkdownIst  = "";
        
        // iterate all nodelines
        $("table.fancytree-ext-table tr").each(function(i, line) {
            // extract data
            var titleSpan = $(line).find("span.fancytree-title2");
            var numberSpan = $(line).find("div.field_metanummer");
            var startEndPlanDiv = $(line).find("div.fieldtype_fromto.field_planChildrenSum");
            var startEndIstDiv = $(line).find("div.fieldtype_fromto.field_istChildrenSum");
            
            // extract content
            var title = null;
            var number = null;
            if ($(titleSpan).size() > 0) {
                title = $(titleSpan).text();
            }
            if ($(numberSpan).size() > 0) {
                number = $(numberSpan).text();
            }
            ganttMarkdownPlan += me.generateGanttMarkdownLineFromBlock(title, number, startEndPlanDiv);
            ganttMarkdownIst += me.generateGanttMarkdownLineFromBlock(title, number, startEndIstDiv);
        });
    
        // concat
        ganttMarkdownPlan = ganttMarkdownPlan.length > 0 ? "    section Plan\n" + ganttMarkdownPlan : "";
        ganttMarkdownIst = ganttMarkdownIst.length > 0 ? "    section Ist\n" + ganttMarkdownIst : "";
        ganttMarkdown += ganttMarkdownPlan + ganttMarkdownIst  + "```\n";
        
        return ganttMarkdown;
    };
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     generate a mermaid-gantt-markdown-line for selector (if start, end-date can be extracted)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>return String - mermaid-gantt-markdown
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Convert
     * @param title          title of the line
     * @param number         referenc
     * @param selector       seletor to filter the element with jquery
     * @return {String}      mermaid-gantt-markdown-line
     */
    me.generateGanttMarkdownLineFromBlock = function(title, number, selector) {
        if ($(selector).size() > 0) {
            // extract dates
            var dates = $(selector).html().replace(/\&nbsp\;/g, ' ').split("-");
            if (dates.length != 2) {
                return "";
            }
            var start = dates[0];
            var end = dates[1];
    
            // if all set: generate gantt
            console.log("extractGanttMarkdownLineFromBlock: title:" + title + " number:" + number + " start:" + start + " end:" + end);
            if (title && number && start && end) {
                return "    " + title + ": " + number + ", " + start + ", " + end + "\n";
            }
        }
        return "";
    };
    
    me.addServicesToDiagrammBlock = function(block, type, downloadLink) {
        var content = $(block).html();
        var blockId = $(block).attr('id');
    
        // add source
        $(block).before("<div class='" + type + "-source' id='fallback" + blockId + "'>"
                + "<pre>" + content + "</pre></div>");
        // add service-links
        $("#fallback" + blockId).before(
                "<div class='services" + type + "' id='services" + blockId + "'><div>"
                + downloadLink
                + "<a href='#' style='display: none;' id='toggleorig" + blockId + "' onclick=\"yaioAppBase.get('YaioBase').toggleWithLinks('#toggleorig" + blockId + "', '#togglesource" + blockId + "', '#" + blockId + "', '#fallback" + blockId + "'); return false;\" target='_blank'>Diagramm</a>"
                + "<a href='#' id='togglesource" + blockId + "' onclick=\"yaioAppBase.get('YaioBase').toggleWithLinks('#toggleorig" + blockId + "', '#togglesource" + blockId + "', '#" + blockId + "', '#fallback" + blockId + "'); return false;\" target='_blank'>Source</a>"
                + "</div></div>");
    };
    
    
    me.addTOCForBlock = function(tocElement, srcElement, settings) {
        // add TOC
        settings = settings || { toc: {}};
        settings.toc = settings.toc || { };
        settings.toc.dest = $(tocElement);
        $.fn.toc($(srcElement), settings);
    };
    
    me._init();
    
    return me;
};

/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     servicefunctions for markdowneditor
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

Yaio.MarkdownEditorService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    me.createNodeDescEditorForNode = function(parentId, textAreaId) {
        var editor = ace.edit(parentId);
        
        // configure
        editor.setTheme("ace/theme/textmate");
    
        editor.getSession().setTabSize(4);
        editor.getSession().setUseSoftTabs(true);     
        editor.getSession().setMode("ace/mode/markdown");
        editor.setHighlightActiveLine(true);
        editor.setShowPrintMargin(true); 
    
        // options from http://ace.c9.io/build/kitchen-sink.html
        // editor.setShowFoldWidgets(value !== "manual");
        // editor.setOption("wrap", value);
        // editor.setOption("selectionStyle", checked ? "line" : "text");
        editor.setShowInvisibles(true);
        editor.setDisplayIndentGuides(true);
        editor.setPrintMarginColumn(80);
        editor.setShowPrintMargin(true);
        editor.setHighlightSelectedWord(true);
        // editor.setOption("hScrollBarAlwaysVisible", checked);
        // editor.setOption("vScrollBarAlwaysVisible", checked);
        editor.setAnimatedScroll(true);
        // editor.setBehavioursEnabled(checked);
        // editor.setFadeFoldWidgets(true);
        // editor.setOption("spellcheck", true);
        
        // set value
        editor.setValue($("#" + textAreaId).val());
        
        // set eventhandler to update corresponding textarea
        editor.getSession().on('change', function(e) {
            // update textarea for angular
            $("#" + textAreaId).val(editor.getValue()).trigger('select').triggerHandler("change");
        });
        
        // set editor as data-attr on parent
        $("#" + parentId).data("aceEditor", editor);
        
        return editor;
    };
    
    me.showPreviewForTextareaId = function(textAreaId) {
        var descText = $("#" + textAreaId).val();
    
        // prepare descText
        var descHtmlMarked = me.appBase.get('YaioFormatter').formatMarkdown(descText, true);
        me.showPreview(descHtmlMarked);
    };
    
        
    me.showPreview = function(content) {
        // set preview-content
        $( "#preview-content" ).html(content);
        
        // show message
        $( "#preview-box" ).dialog({
            modal: true,
            width: "1050px",
            buttons: {
              Ok: function() {
                $( this ).dialog( "close" );
              },
              "Vorlesen": function () {
                  me.appBase.get('YaioLayout').openSpeechSynthWindow(document.getElementById('preview-content'));
              }
            }
        });    
        
        // do mermaid when preview visible
        me.appBase.get('YaioFormatter').formatMermaidGlobal();
    
        // do syntax-highlight
        me.appBase.get('YaioFormatter').formatDescBlock($("#preview-content"));
    };
    
    me.showMarkdownHelp = function() {
        // show message
        var url = "/examples/markdownhelp/markdownhelp.html" + "?" + me.appBase.get('YaioBase').createXFrameAllowFrom();
        console.log("showMarkdownHelp:" + url);
        $("#markdownhelp-iframe").attr('src',url);
        $("#markdownhelp-box" ).dialog({
            modal: true,
            width: "1200px",
            buttons: {
                "Schliessen": function() {
                  $( this ).dialog( "close" );
                },
                "Eigenes Fenster": function() {
                    var helpFenster = window.open(url, "markdownhelp", "width=1200,height=500,scrollbars=yes,resizable=yes");
                    helpFenster.focus();
                    $( this ).dialog( "close" );
                } 
            }
        });    
    };
    
    
    me.openWysiwhgForTextareaId = function(textAreaId) {
        // get existing parentEditor
        var parentEditorId = "editor" + textAreaId.charAt(0).toUpperCase() + textAreaId.substring(1);
        var parentEditor = $("#" + parentEditorId).data("aceEditor");
        console.log("found parentEditor on:" + parentEditorId);
    
        // create  Editor
        var myParentId = "wysiwhg-editor";
        var editor = me.createNodeDescEditorForNode(myParentId, textAreaId);
    
        // reset intervallHandler for this parent
        var intervalHandler = $("#" + myParentId).data("aceEditor.intervalHandler");
        if (intervalHandler != "undefined") {
            console.log("openWysiwhgForTextareaId: clear old Interval : " + intervalHandler + " for " + myParentId);
            clearInterval(intervalHandler);
        }
        // create new intervalHandler: check every 5 second if there is a change und update all
        $("#" + myParentId).data("aceEditor.flgChanged", "false");
        intervalHandler = setInterval(function(){ 
            // check if something changed
            if ($("#" + myParentId).data("aceEditor.flgChanged") != "true") {
                // nothing changed
                return;
            }
            
            console.log("openWysiwhgForTextareaId: updateData : " + " for " + myParentId);
    
            // reset flag
            $("#" + myParentId).data("aceEditor.flgChanged", "false");
    
            // update textarea for angular
            var value = editor.getValue();
            $("#" + textAreaId).val(value).trigger('select').triggerHandler("change");
            console.log("openWysiwhgForTextareaId: updatetextAreaId: " + textAreaId);
    
            // update preview
            me.showWyswhgPreviewForTextareaId(textAreaId);
            
            // update parent
            if (parentEditor) {
                parentEditor.setValue(value);
            }
        }, 5000);
        console.log("openWysiwhgForTextareaId: setIntervall : " + intervalHandler + " for " + myParentId);
        $("#" + myParentId).data("aceEditor.intervalHandler", intervalHandler);
        
        // set update-event
        editor.getSession().on('change', function(e) {
            $("#" + myParentId).data("aceEditor.flgChanged", "true");
        });
        
        // init preview
        me.showWyswhgPreviewForTextareaId(textAreaId);
    
        // show message
        $( "#wysiwhg-box" ).dialog({
            modal: true,
            width: "1200px",
            buttons: {
              Ok: function() {
                $( this ).dialog( "close" );
                console.log("openWysiwhgForTextareaId: clearMyInterval : " + intervalHandler + " for " + myParentId);
                clearInterval(intervalHandler);
              },
              "Hilfe": function () {
                  me.showMarkdownHelp();
              },
              "Vorschau": function () {
                  me.showPreviewForTextareaId(textAreaId);
              },
              "Vorlesen": {
                  id: "Vorlesen",
                  text: "Vorlesen",
                  class: "jsh-show-inline-block-if-speechsynth",
                  click: function () {
                      me.appBase.get('YaioLayout').openSpeechSynthWindow(document.getElementById('wysiwhg-preview'));
                  }
              },
              "Load": function () {
                  // define handler
                  var handleImportJSONFileSelectHandler = function handleImportJSONFileSelect(evt) {
                      var files = evt.target.files; // FileList object
    
                      // Loop through the FileList.
                      for (var i = 0, numFiles = files.length; i < numFiles; i++) {
                          var file = files[i];
                          var reader = new FileReader();
    
                          // config reader
                          reader.onload = function(res) {
                              console.log("read fileName:" + file.name);
                              var data = res.target.result;
                              
                              // set new content (textarea+editor)
                              editor.setValue(data);
                              $("#" + myParentId).data("aceEditor.flgChanged", "true");
                          };
                          
                          // read the file
                          reader.readAsText(file);
                          
                          i = files.length +1000;
                      }
                  };
                  
                  // initFileUploader
                  var fileDialog = document.getElementById('importJSONFile');
                  fileDialog.addEventListener('change', handleImportJSONFileSelectHandler, false);
                  $( "#jsonuploader-box" ).dialog({
                      modal: true,
                      width: "200px",
                      buttons: {
                        "Schließen": function() {
                          $( this ).dialog( "close" );
                        }
                      }
                  });
               }
            }
        });
        // add export-link -> buggy to mix jquery and styles
        $(".ui-dialog-buttonset").append($("<a href='' id='wysiwyg-exportlink'" +
            + " sdf='ojfvbhwjh'"
            + " onclick=\"yaioAppBase.get('YaioBase').downloadAsFile($('#wysiwyg-exportlink'), $('#" + textAreaId + "').val(), 'data.md', 'text/markdown', 'utf-8');\">"
            + "<span class='ui-button-text'>Export</span></a>"));
        $('#wysiwyg-exportlink').addClass("ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only");
    
    };
    
    me.showWyswhgPreviewForTextareaId = function(textAreaId) {
        // prepare descText
        var descText = $("#" + textAreaId).val();
        var descHtmlMarked = me.appBase.get('YaioFormatter').formatMarkdown(descText, true);
    
        // set preview-content
        $( "#wysiwhg-preview" ).html(descHtmlMarked);
    
        // do mermaid when preview visible
        me.appBase.get('YaioFormatter').formatMermaidGlobal();
        
        // do syntax-highlight
        me.appBase.get('YaioFormatter').formatDescBlock($("#wysiwhg-preview"));
    };

    me._init();
    
    return me;
};

/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     servicefunctions for exported documentations
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

/*****************************************
 *****************************************
 * Servicefunctions for exported documentations
 *****************************************
 *****************************************/

Yaio.ExportedDataService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };
    /*
     * ###########################
     * # PrintLayout
     * ###########################
     */
    me.togglePrintLayout = function() {
        if ($("#checkboxPrintAll").prop('checked')) {
            // print all
            $("#link_css_dataonly").attr("disabled", "disabled");
            $("#link_css_dataonly").prop("disabled", true);
        } else  {
            // print data only
            $("#link_css_dataonly").removeAttr("disabled");
            $("#link_css_dataonly").prop("disabled", false);
        }
    };
    
    /*
     * ###########################
     * # SpeechSynth
     * ###########################
     */
    me.openSpeechSynth = function() {
         var target = document.getElementById('div_full');
         if (target == null) { target = self; }
         target.focus();
         var speechsynthWindow = window.open('/yaio-explorerapp/speechsynth.html', "speechsynth", "width=690,height=350,resizable=yes,dependent=yes,scrollbars=yes");
         speechsynthWindow.focus();
         if (speechsynthWindow.opener == null) { speechsynthWindow.opener = self; }
         speechsynthWindow.opener.targetElement = target;
     };
    
    /*
     * ###########################
     * # Kalkulation
     * ###########################
     */
    const CONST_FUNCNAME_SUM = "SUM";
    
    me.calcColumns = function(nodeTDId, functionName, praefix, suffix) {
       // Variablen belegen
       var errMsg = null;
       var elemNodeTD = null;
       var idxCol = null;
       var elemNodeTABLE = null;
        
       // Parameter checken und Eelemente einlesen
       
       // TD-Element einlesen
       if (! errMsg && ! functionName) { 
           errMsg = "Parameter functionName required"; 
       } else {
           if (functionName != CONST_FUNCNAME_SUM) {
              errMsg = "Parameter functionName must be [" + CONST_FUNCNAME_SUM + "]"; 
           }
       }
       if (! errMsg && ! nodeTDId) { 
           errMsg = "Parameter nodeTDId required"; 
       } else {
           elemNodeTD = $("#" + nodeTDId);
       }
       
       // TABLE-Element einlesen
       if (! errMsg && ! elemNodeTD) { 
           errMsg = "HTMLElement nodeTDId not found"; 
       } else {
           idxCol = elemNodeTD.attr('cellIndex');
           elemNodeTABLE = $(elemNodeTD).closest('table');
       }
    
       // IDX einlesen
       if (! errMsg && ! elemNodeTABLE) { 
           errMsg = "HTMLElement elemNodeTABLE not found"; 
       } else {
           idxCol = elemNodeTD[0].cellIndex;
       }
       if (! errMsg && ! idxCol) { 
           errMsg = "idxCol not found"; 
       }
       
       // abschließender Fehlercheck
       if (errMsg) {
          window.alert("Fehler aufgetreten: " + errMsg);
          return null;
       }
       
       // Funktionalitaet: alle x-Spalten iterieren und Zahlen extrahieren
       var filterTD = "td:nth-child(" + (idxCol + 1) + ")";
       var numbers = new Array();
       $(elemNodeTABLE).children("tbody").children("tr").children(filterTD).each(function (i) {
           var col = $(this);
    
           // mich selbst herauslassen 
           if (col.attr('id') == nodeTDId) {
              // Jquery continue;
              //alert("SKIP NODEID: Zeile:" + (i+1) + " ID:" + col.id + " Content:" + col.html() + " Number" + number);
              return;
           }
    
           // normalisieren
           var strNumber = col.html();
           strNumber = strNumber.replace(/./g, "");
           strNumber = strNumber.replace(/,/, ".");
           
           // auf Zahl testen
           var number = parseFloat(strNumber, "10");
           if (isNaN(number)) {
              // Jquery continue;
              //alert("SKIP NAN: Zeile:" + (i+1) + " ID:" + col.id + " Content:" + col.html() + " Number" + number);
              return;
           }
           
           // an Zahlen anhaengen
           //alert("ADD NUMBER: Zeile:" + (i+1) + " ID:" + col.id + " Content:" + col.html() + " Number" + number);
           numbers.push(number);
       });       
       
       // Funktionalitaet: gewünsche Aktion auf alle Zahlen ausführen
       var funcResult = 0;
       for (var index = 0; index < numbers.length; ++index) {
           // alert("funcResult +" +  numbers[index]);
           if (functionName == CONST_FUNCNAME_SUM) {
              funcResult += numbers[index]; 
           }
       }
       
       // Inhalt des Elements setzen
       //alert("funcResult:" + funcResult);
       var text = (praefix ? praefix : "") + funcResult + (suffix ? suffix : "");
       elemNodeTD.html(funcResult);
    };
    
    /*
     * ######################
     * # Volltextsuche
     * #######################
     */
    me.doAllNodeToggler = function(flgShow, minEbene) {
        try {
            // Bloecke Oeffnen/Schließen
            var toggleList = document.getElementsByClassName("blockToggler");
            if (toggleList.length > 0) {
                // Elemente vorhanden
                for (var j = 0; j < toggleList.length; j++) {
                    // Elemente iterieren
                    var element = toggleList[j];
                    var toggleId = element.getAttribute('toggleId');
                    var togglerBaseId = element.getAttribute('togglerBaseId');
    
                    // Pruefen ob Node-Toggler
                    if (toggleId && toggleId.search("childrencontainer") > 0) {
                        // Ebene einlesen
                        var toggleBlock = document.getElementById(toggleId);
                        var curEbene = 0;
                        if (toggleBlock) {
                            curEbene = toggleBlock.getAttribute('data-pjebene');
                        }
    
                        var effect = function(){
                            // Leertoggler
                            var togEf = new ToggleEffect(toggleId); 
                            togEf.slideAniLen = 1; 
                            togEf.doEffect();
                        };
                        if (flgShow) {
                            // Block zeigen
                            if (minEbene && curEbene && curEbene < minEbene) {
                                jMATService.getLayoutService().togglerBlockShow(
                                    togglerBaseId, toggleId, effect);
                            } else {
                                jMATService.getLayoutService().togglerBlockHide(
                                    togglerBaseId, toggleId, effect);
                            }
                        }
                        else {
                            // Block verbergen
                            if (minEbene && curEbene && curEbene > minEbene) {
                                jMATService.getLayoutService().togglerBlockHide(
                                    togglerBaseId, toggleId, effect);
                            } else {
                                jMATService.getLayoutService().togglerBlockShow(
                                    togglerBaseId, toggleId, effect);
                            }
                        }
                    }
                }
            }
        } catch (e) {
            // anscheinend  nicht definiert
            window.alert(e);
        }
    };
    
    me.doParentNodeToggler = function(myId, flgShow) {
        try {
            // Parent-Container einlesen (Child)
            var parents = $("#" + myId).parents();
            if (parents) {
                parents.map( 
                    function () {
                        // Toggler visible setzen, wenn gefunden
                        var parentId = $(this).attr("id");
                        if (parentId) {
                            var nodeIdMatcher = parentId.match(/node_(.*)_childrencontainer/);
                            if (nodeIdMatcher && nodeIdMatcher.length > 0) {
                                // Toggler aktivieren
                                var togglerId = parentId;
                                if (! $(this).is(":visible")) {
                                    jMATService.getLayoutService().togglerBlockShow(
                                        togglerId, togglerId, function () { 
                                            var togEf = new ToggleEffect(togglerId); 
                                            togEf.slideAniLen = 1; 
                                            togEf.doEffect();
                                        }
                                     );
                                }
    
                                // Element anzeigen
                                var nodeId = nodeIdMatcher[1];
                                var master = $("#node_" + nodeId + "_master");
                                if (master && ! $(master).is(":visible") ) {
                                    $(master).show();                                
                                }
                            }
                        }
                    }
                );
            }
        } catch (e) {
            // anscheinend  nicht definiert
            window.alert(e);
        }
    };
    
    
    /* Um einen Volltext-Treffer zu haben, müssen alle Worte im durchsuchten Text vorkommen. */
    me.VolltextTreffer = function(inhalt, suchworte) {
        // Wenn keine Suchzeichenkette als gefnden kennzeichnen
        if ( suchworte.length == 0 ) {
            return true;
        }

        // alle Suchworte iterieren
        for (var i = 0; i < suchworte.length; i++) {
            if ( inhalt.indexOf(suchworte[i]) == -1) {
                // Wort nicht gefunden
                return false;
            }
        }
     
        // alle Worte gefunden
        return true;
    };
    
    me.doSearch = function(suchworte) {
        // Suche auf alle Node-Elemente ausführen
        $(".node-block").each(
            function(index, value) {
                var flgFound = false;
                
                // Datenelemente konfigurieren
                var searchElements = new Array();
                searchElements.push($("#" + $(value).attr("id") + " > div:eq(1)").attr("id")); // Desc
                searchElements.push($("#" + $(value).attr("id") + " > div:first > div:first > div:first > div:eq(1)").attr("id")); // Name
                searchElements.push($("#" + $(value).attr("id") + " > div:first > div:eq(1)").attr("id")); // Status 
    
                // alle Datenelemente iterieren
                $.each(searchElements,
                    function(subIndex, subId) {
                        // Inhalt auslesen
                        var inhalt = $("#"+subId).text().toLowerCase();
                        
                        // Volltextsuche
                        if (me.VolltextTreffer(inhalt, suchworte)) {
                            // wenn geufndne: verlassen;
                            flgFound = true;
                            return;
                        }
                    }
                 );
                
                // Elemente je nach Status der SubElemente ein/ausblenden
                if (flgFound) {
                    if ( !$(value).is(":visible") ) {
                        // Element aktivieren
                        $(value).show();
                    }  
    
                    // Eltern oeffnen
                    me.doParentNodeToggler($(value).attr("id"), true);
                } else {
                    // Element deaktivieren
                    $(value).hide();
                }
            }
        );
    };
    
    me.startSearch = function() {
        // Suchworte auslesen
        var suchworte = $(".volltextsuchfeld").val().toLowerCase().split(" ");
    
        // alle Toggler schließen
        me.doAllNodeToggler(false, 0);
        
        // Suche ausfuehren
        me.doSearch(suchworte);
    };
    
    me.resetSearch = function() {
        // Suchworte leeren
        $(".volltextsuchfeld").val('');
        
        // suche ausfuehren
        me.startSearch();
    };
    
    me.initSearch = function() {
        // Volltextsuche
        $(".volltextsuchfeld").keyup(
            function(event) {
                // nur ausfuehren wenn enter
                if(event.keyCode != 13) {
                   return;
                }
                
                // Suche ausfuehren
                me.startSearch();
            }
        );
    };
    
    /*
     * ######################
     * # Symlink
     * #######################
     */
    me.openNode = function(nodeId) {
        var masterId = "node_" + nodeId + "_master";
        
        // Node aktivieren
        $("#" + masterId).show();
        
        // openParent-Nodes
        me.doParentNodeToggler(masterId, true);
        
        // animated ScrollTo after intervall, because of time used for ToggleEffects
        var delayedFocus = window.setInterval(
            function() {
                // nach 0,1 Sekunden ausfuehren und Intervall loeschen
                $('html, body').animate({
                    scrollTop: $("#" + masterId).offset().top
                    }, 
                    1000);
                window.clearInterval(delayedFocus);
            }, 
            100
        );
    };

    me._init();
    
    return me;
};
 
