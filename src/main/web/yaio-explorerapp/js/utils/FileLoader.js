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
 * FileLoader-servicefunctions
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

/*****************************************
 *****************************************
 * Service-Funktions (file)
 *****************************************
 *****************************************/
Yaio.FileLoader = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    me.loadFile = function (fileName) {
        var msg = "loadFile fileName:" + fileName;

        console.log("START " + msg);
        var req = me.$.ajax({
            url : fileName,
            type : 'GET',
            complete : function() {
                console.log("COMPLETE " + msg);
            }
        });
        
        return req;
    };

    me.loadContentIntoContainerFile = function(containerFileName, content) {
        var msg = "loadContentIntoContainerFile containerFileName:" + containerFileName;

        // create promise
        var dfd = new $.Deferred();
        var res = dfd.promise();
        
        console.log("START " + msg);
        me.loadFile(containerFileName)
            .then(function doneContainerFile(containerData) {
                // do replacements
                containerData = containerData.replace(/<!-- REPLACECONTENT_START -->[\s\S]*<!-- REPLACECONTENT_END -->/gi, 
                        "<!-- REPLACECONTENT_START -->" + content + "<!-- REPLACECONTENT_END -->");
                // set baseref
                var baseRef = me.appBase.config.resBaseUrl + "../";
                containerData = containerData.replace(/=\".\/dist\//g, 
                        "=\"" + baseRef + "dist/");
                containerData = containerData.replace("yaioAppBase.config.resBaseUrl = \"\";", 
                        "yaioAppBase.config.resBaseUrl = \"" + baseRef + "\";");

                console.log("DONE " + msg + " doneContainerFile + replace -> return");
                dfd.resolve(containerData);
            }, function error(errorThrown) {
                console.error("ERROR " + msg + " error -> return", errorThrown);
                dfd.reject(errorThrown);
            });
        
        return res;
    };
        
    me.loadStaticFileIntoContainerFile = function(containerFileName, contentFileName) {
        var msg = "loadStaticFileIntoContainerFile containerFileName:" + containerFileName + " contentFileName:" + contentFileName;
        // create promise
        var dfd = new $.Deferred();
        var res = dfd.promise();
        
        console.log("START " + msg);
        me.loadFile(contentFileName)
            .then(function doneContentFile(contentData) {
                console.log(msg + " doneContentFile DONE call loadContentIntoContainerFile");
                me.loadContentIntoContainerFile(containerFileName, contentData) 
                    .then(function doneContainerFile(containerData) {
                        console.log("DONE " + msg + " doneContainerFile -> return");
                        dfd.resolve(containerData);
                    }, function error(errorThrown) {
                        console.error("ERROR " + msg + " error -> return", errorThrown);
                        dfd.reject(errorThrown);
                    });
            }, function error(errorThrown) {
                console.error("ERROR " + msg + " error -> return", errorThrown);
                dfd.reject(errorThrown);
            });
        
        return res;
    };
    
    me.loadDocumentationContainerContent = function(contentFileName) {
        var msg = "loadDocumentationContainerContent contentFileName:" + contentFileName;
        var containerFileName = '../exporttemplates/documentation-export.html';
        // create promise
        var dfd = new $.Deferred();
        var res = dfd.promise();

        console.log("START " + msg);
        me.loadStaticFileIntoContainerFile(containerFileName, contentFileName)
            .then(function doneContentFile(contentData) {
                console.log("DONE " + msg + " loading -> return");
                dfd.resolve(contentData);
            }, function error(errorThrown) {
                console.error("ERROR " + msg + " error -> return", errorThrown);
                dfd.reject(errorThrown);
            });

        return res;
    };
    
    me.downloadDocumentationContainerContent = function($link, contentFileName, mime, encoding, target) {
        var target = $link.attr('target');
        me.loadDocumentationContainerContent(contentFileName)
            .then(function doneContentFile(contentData) {
                me.downloadAsFile($link, contentData, fileName, mime, encoding, target);
            });
    };
    
    me.downloadAsFile = function($link, data, fileName, mime, encoding, target) {
        if (mime == "undefind") {
            mime = "application/text";
        }
        if (encoding == "undefind") {
            mime = "uft-8";
        }
        if (target == "undefind") {
            target = "_blank";
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
               me.appBase.get("Logger").logError("Leider kann der Download nicht angezeigt werden, da Ihr Popup-Blocker aktiv ist. Beachten Sie die Hinweise im Kopf des Browsers. ", true);
           } else {
               // set data to document
               me.$(popup.document.body).html("<pre>" + me.htmlEscapeTextLazy(data) + "</pre>");
           }
           return false;
       } else {
            // all expect IE
            $link.attr({
                'download' : fileName,
                'href' : dataURI,
                'target' : target
            });
       }
    };
    
    me._init();
    
    return me;
};