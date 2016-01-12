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
        console.log("configureService: set new serviceconfig:" + serviceName);
        // delete all instances
        if (me.services[serviceName]) {
            console.log("configureService: delete instance for serviceconfig:" + serviceName);
            me.services[serviceName] = null;
            me.services.splice(me.services.indexOf(serviceName), 1);
        }
        if (me.services.hasOwnProperty(serviceName)) {
            console.log("configureService: delete instance for serviceconfig:" + serviceName);
            me.services.splice(me.services.indexOf(serviceName), 1);
        }
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
        if (! me.services[serviceName]) {
console.log("create new instance of:" + serviceName);
            if (me.serviceConfigs.hasOwnProperty(serviceName)) {
                var constr = me.getServiceConfig(serviceName);
                me.setService(serviceName, constr(me));
            }
        }
        return me.services[serviceName];
    };
    
    me.set = function (serviceName, serviceInstance) {
        return me.setService(serviceName, serviceInstance);
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
        me.$ = me.jQuery;
        me.configureService("jQuery", function() { return me.jQuery; });
        
        // configure instances
        me.configureService("JsHelferlein.LoggerService", function() { return JsHelferlein.LoggerService(me); });
        me.configureService("JsHelferlein.DOMHelperService", function() { return JsHelferlein.DOMHelperService(me); });
        me.configureService("JsHelferlein.DataUtilsService", function() { return JsHelferlein.DataUtilsService(me); });
        me.configureService("JsHelferlein.FileUtilsService", function() { return JsHelferlein.FileUtilsService(me); });
        me.configureService("JsHelferlein.UIDialogsService", function() { return JsHelferlein.UIDialogsService(me); });
        me.configureService("JsHelferlein.UITogglerService", function() { return JsHelferlein.UITogglerService(me); });
        me.configureService("JsHelferlein.SpeechSynthHelperService", function() { return JsHelferlein.SpeechSynthHelperService(me); });
        me.configureService("JsHelferlein.SpeechRecognitionHelperService", function() { return JsHelferlein.SpeechRecognitionHelperService(me); });
        me.configureService("JsHelferlein.ChecklistParserService", function() { return JsHelferlein.ChecklistParserService(me); });
        me.configureService("JsHelferlein.MermaidParserService", function() { return JsHelferlein.MermaidParserService(me); });
        me.configureService("JsHelferlein.MindmapParserService", function() { return JsHelferlein.MindmapParserService(me); });
        me.configureService("JsHelferlein.PlantumlParserService", function() { return JsHelferlein.PlantumlParserService(me); });
        me.configureService("JsHelferlein.SyntaxHighlighterParserService", function() { return JsHelferlein.SyntaxHighlighterParserService(me); });

        // configure aliases
        me.configureService("Logger", function() { return me.get("JsHelferlein.LoggerService"); });
        me.configureService("DOMHelper", function() { return me.get("JsHelferlein.DOMHelperService"); });
        me.configureService("UIDialogs", function() { return me.get("JsHelferlein.UIDialogsService"); });
        me.configureService("UIToggler", function() { return me.get("JsHelferlein.UITogglerService"); });
        me.configureService("DataUtils", function() { return me.get("JsHelferlein.DataUtilsService"); });
        me.configureService("FileUtils", function() { return me.get("JsHelferlein.FileUtilsService"); });
        me.configureService("SpeechSynthHelper", function() { return me.get("JsHelferlein.SpeechSynthHelperService"); });
        me.configureService("SpeechRecognitionHelper", function() { return me.get("JsHelferlein.SpeechRecognitionHelperService"); });
        me.configureService("ChecklistParser", function() { return me.get("JsHelferlein.ChecklistParserService"); });
        me.configureService("MermaidParser", function() { return me.get("JsHelferlein.MermaidParserService"); });
        me.configureService("MindmapParser", function() { return me.get("JsHelferlein.MindmapParserService"); });
        me.configureService("PlantumlParser", function() { return me.get("JsHelferlein.PlantumlParserService"); });
        me.configureService("SyntaxHighlighterParser", function() { return me.get("JsHelferlein.SyntaxHighlighterParserService"); });
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