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
 * servicefunctions basics
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

/*****************************************
 *****************************************
 * Service-Funktions (layout)
 *****************************************
 *****************************************/
Yaio.PromiseHelper = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    // Angular-PromiseHelper
    var PromiseHelper = (function () {
        function wrapPromise(promise) {
            return {
                then: promise.then,
                success: function (fn) {
                    promise.then(fn);
                    return wrapPromise(promise);
                },
                error: function (fn) {
                    promise.then(null, fn);
                    return wrapPromise(promise);
                }
            };
        }
     
        function PromiseHelper() {
            var _this = this;
            var $q = appBase.get('Angular.$q');
            _this._deferred = $q.defer();
            _this.$rootScope = appBase.get('Angular.$rootScope');
        }
      
        PromiseHelper.prototype.resolve = function (data) {
            this._deferred.resolve(data);
            //this.$rootScope.$apply();
        };
      
        PromiseHelper.prototype.reject = function (data) {
            this._deferred.reject(data);
            //this.$rootScope.$apply();
        };
      
        PromiseHelper.prototype.getHttpPromiseMock = function () {
            var promise = this._deferred.promise;
            return wrapPromise(promise);
        };
      
        return PromiseHelper;
    })();
    

    /**
     * initialize the object
     */
    me._init = function() {
    };

    me.createAngularPromiseHelper = function() {
        return new PromiseHelper();
    };
    
    me._init();
    
    return me;
};
