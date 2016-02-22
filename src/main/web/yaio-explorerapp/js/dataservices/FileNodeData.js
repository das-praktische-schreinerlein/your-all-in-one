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
 * servicefunctions for data-services
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.FileNodeData = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = Yaio.StaticNodeData(appBase, config, defaultConfig);
    
    /**
     * initialize the object
     */
    me._init = function() {
    };
    
    me.connectService = function() {
        // return promise
        var dfd = new $.Deferred();
        var res = dfd.promise();
        
        // define handler
        var handleLoadJSONFileSelectHandler = function handleLoadJSONFileSelect(evt) {
            var files = evt.target.files; // FileList object
            var reader = new FileReader();

            if (files.length === 1) {
                var file = files[0];

                // config reader
                reader.onload = function (res) {
                    console.log('read fileName:' + file.name);

                    // update serviceconfig
                    me.updateServiceConfig();
                    me.updateAppConfig();

                    // set content as json
                    window.yaioFileJSON = res.target.result;

                    // load content
                    me._loadStaticJson(window.yaioFileJSON);

                    // set new name
                    me.config.name = 'Dateiupload: ' + file.name;

                    dfd.resolve('OK');
                };

                // read the file
                reader.readAsText(file);
            }
        };
        
        // initFileUploader
        var fileDialog = document.getElementById('yaioLoadJSONFile');
        fileDialog.addEventListener('change', handleLoadJSONFileSelectHandler, false);
        me.$( '#yaioloadjsonuploader-box' ).dialog({
            modal: true,
            width: '200px',
            buttons: {
              'Schließen': function() {
                me.$( this ).dialog( 'close' );
              }
            }
        });
        
        return res;
    };

    /*****************************************
     *****************************************
     * Service-Funktions (webservice)
     *****************************************
     *****************************************/
    me._createAccessManager = function() {
        return Yaio.FileAccessManager(me.appBase, me.config);
    };
    
    me._init();
    
    return me;
};
