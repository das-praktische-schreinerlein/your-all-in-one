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

    /**
     * load content into containerFile insert it between <!-- REPLACECONTENT_START --> and <!-- REPLACECONTENT_END -->
     * @param {String} containerFileName             name of the containerFile
     * @param {String} content                       content
     * @returns {JQueryPromise<T>|JQueryPromise<*>}  promise if load succeed or failed - resolves with content
     */
    me.loadContentIntoContainerFile = function(containerFileName, content) {
        var msg = 'loadContentIntoContainerFile containerFileName:' + containerFileName;

        // create promise
        var dfd = new $.Deferred();
        var res = dfd.promise();
        
        console.log('START ' + msg);
        me.appBase.FileUtils.loadFile(containerFileName)
            .then(function doneContainerFile(containerData) {
                // do replacements
                containerData = containerData.replace(/<!-- REPLACECONTENT_START -->[\s\S]*<!-- REPLACECONTENT_END -->/gi, 
                        '<!-- REPLACECONTENT_START -->' + content + '<!-- REPLACECONTENT_END -->');
                // set baseref
                var baseRef = me.appBase.config.resBaseUrl + '../';
                containerData = containerData.replace(/=\'.\/dist\//g,
                        '=\'' + baseRef + 'dist/');
                containerData = containerData.replace('yaioAppBase.config.resBaseUrl = \'\';',
                        'yaioAppBase.config.resBaseUrl = \'' + baseRef + '\';');

                console.log('DONE ' + msg + ' doneContainerFile + replace -> return');
                dfd.resolve(containerData);
            }, function error(errorThrown) {
                console.error('ERROR ' + msg + ' error -> return', errorThrown);
                dfd.reject(errorThrown);
            });
        
        return res;
    };

    /**
     * load content of contentFileName into containerFile
     * @param {String} containerFileName             name of the containerFile
     * @param {String} contentFileName               name of the contentFile to insert into containerFile
     * @returns {JQueryPromise<T>|JQueryPromise<*>}  promise if load succeed or failed - resolves with content
     */
    me.loadStaticFileIntoContainerFile = function(containerFileName, contentFileName) {
        var msg = 'loadStaticFileIntoContainerFile containerFileName:' + containerFileName + ' contentFileName:' + contentFileName;
        // create promise
        var dfd = new $.Deferred();
        var res = dfd.promise();
        
        console.log('START ' + msg);
        me.appBase.FileUtils.loadFile(contentFileName)
            .then(function doneContentFile(contentData) {
                console.log(msg + ' doneContentFile DONE call loadContentIntoContainerFile');
                me.loadContentIntoContainerFile(containerFileName, contentData) 
                    .then(function doneContainerFile(containerData) {
                        console.log('DONE ' + msg + ' doneContainerFile -> return');
                        dfd.resolve(containerData);
                    }, function error(errorThrown) {
                        console.error('ERROR ' + msg + ' error -> return', errorThrown);
                        dfd.reject(errorThrown);
                    });
            }, function error(errorThrown) {
                console.error('ERROR ' + msg + ' error -> return', errorThrown);
                dfd.reject(errorThrown);
            });
        
        return res;
    };

    /**
     * load content of contentFileName into containerFile: documentation-export.html
     * @param {String} contentFileName               name of the contentFile to insert into containerFile
     * @returns {JQueryPromise<T>|JQueryPromise<*>}  promise if load succeed or failed - resolves with content
     */
    me.loadDocumentationContainerContent = function(contentFileName) {
        var msg = 'loadDocumentationContainerContent contentFileName:' + contentFileName;
        var containerFileName = '../exporttemplates/documentation-export.html';
        // create promise
        var dfd = new $.Deferred();
        var res = dfd.promise();

        console.log('START ' + msg);
        me.loadStaticFileIntoContainerFile(containerFileName, contentFileName)
            .then(function doneContentFile(contentData) {
                console.log('DONE ' + msg + ' loading -> return');
                dfd.resolve(contentData);
            }, function error(errorThrown) {
                console.error('ERROR ' + msg + ' error -> return', errorThrown);
                dfd.reject(errorThrown);
            });

        return res;
    };
    
    me.downloadDocumentationContainerContent = function($link, contentFileName, mime, encoding) {
        var target = $link.attr('target');
        me.loadDocumentationContainerContent(contentFileName)
            .then(function doneContentFile(contentData) {
                me.appBase.FileUtils.downloadAsFile($link, contentData, contentFileName, mime, encoding, target);
            });
    };

    me._init();
    
    return me;
};