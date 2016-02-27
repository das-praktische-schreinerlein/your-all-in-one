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
 * datasource manager
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.DataSourceManager = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase, config, defaultConfig);

    /**
     * initialize the object
     */
    me._init = function() {
        me.connections = {};
        me.currentConnection = undefined;
    };

    /**
     * return all available connection-keys
     * @returns {Array}            available connection-keys
     */
    me.getConnectionKeys = function() {
        return Object.keys(me.connections);
    };

    /**
     * return the connection
     * @param {String} key                     key of the connection
     * @returns {Yaio.AbstractNodeDBDriver}    connection
     */
    me.getConnection = function(key) {
        return me.connections[key];
    };

    /**
     * set data for the connection
     * @param {String} key                 key of the connection
     * @param {Yaio.AbstractNodeDBDriver}  connection
     */
    me.addConnection = function(key, connection) {
        me.connections[key] = connection;
    };

    /**
     * connect the service
     * @param {String} key                             key of the connection
     * @returns {JQueryPromise<T>|JQueryPromise<*>}    promise if connect succeed or failed
     */
    me.connectService = function(key) {
        me.currentConnection = undefined;
        if (typeof me.connections[key] === 'function') {
            me.currentConnection = me.connections[key]();
        } else {
            me.currentConnection = me.connections[key];
        }
        me.appBase.configureService('YaioAccessManager', me.currentConnection.getAccessManager());
        return me.currentConnection.connectService();
    };

    /**
     * get data for the current connection
     * @returns {Yaio.AbstractNodeDBDriver}    connection
     */
    me.getCurrentConnection = function() {
        return me.currentConnection;
    };

    me._init();
    
    return me;
};
