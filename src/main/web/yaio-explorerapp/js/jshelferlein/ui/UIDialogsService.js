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
JsHelferlein.UIDialogsService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);
    
    /**
     * initialize the object
     */
    me._init = function() {
    };


    me.showModalErrorMessage = function(message) {
        // set messagetext
        me.$( "#error-message-text" ).html(message);
        
        // show message
        me.$( "#error-message" ).dialog({
            modal: true,
            buttons: {
              Ok: function() {
                me.$( this ).dialog( "close" );
              }
            }
        });    
    };
   
    me.showModalConfirmDialog = function(message, yesHandler, noHandler) {
        // set messagetext
        me.$( "#dialog-confirm-text" ).html(message);
        
        // show message
        
        me.$( "#dialog-confirm" ).dialog({
            resizable: false,
            height:140,
            modal: true,
            buttons: {
              "Ja": function() {
                me.$( this ).dialog( "close" );
                if (yesHandler) {
                    yesHandler();
                }
              },
              "Abbrechen": function() {
                me.$( this ).dialog( "close" );
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
        toastr[type](me.appBase.get('DataUtils').htmlEscapeText(message), title);
    };

    me._init();

    return me;
};