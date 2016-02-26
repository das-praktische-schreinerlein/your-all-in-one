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
Yaio.AccessManager = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase, config, defaultConfig);

    /**
     * initialize the object
     */
    me._init = function() {
        me.masterNodeId = undefined;
        me.availiableExportForms = {};
        me.availiableExportLinks = {};
        me.availiableStaticExportLinks = {};
        me.availiableImportForms = {};
        me.availiableNodeActions = {};
        me.nodeActions = {};
    };


    /**
     * return all available export-form-keys for the node "mindmap, html...)
     * @param {String} nodeId      optional id of the node to check to get the available export-forms (form-keys)
     * @param {Boolean} flgMaster  true if it is the master-node
     * @returns {Array}            available export-form-keys for the node
     */
    me.getAvailiableExportFormKeys = function(nodeId, flgMaster) {
        return Object.keys(me.availiableExportForms);
    };

    /**
     * return data of the export-form (url)
     * @param {String} key         key of the export-form
     * @param {String} nodeId      optional id of the node to check to get the available export-form (form-keys)
     * @param {Boolean} flgMaster  true if it is the master-node
     * @returns {String}           url
     */
    me.getAvailiableExportForm = function(key, nodeId, flgMaster) {
        return me.availiableExportForms[key];
    };

    /**
     * set data for the export-form (url)
     * @param {String} key         key of the export-form
     * @param {String} url         url for the export-form
     */
    me.setAvailiableExportForm = function(key, url) {
        me.availiableExportForms[key] = url;
        return me.availiableExportForms[key];
    };

    /**
     * return all available export-link-keys for the node "mindmap, html...)
     * @param {String} nodeId      optional id of the node to check to get the available export-links (link-keys)
     * @param {Boolean} flgMaster  true if it is the master-node
     * @returns {Array}            available export-link-keys for the node
     */
    me.getAvailiableExportLinkKeys = function(nodeId, flgMaster) {
        return Object.keys(me.availiableExportLinks);
    };

    /**
     * return data of the export-link (url)
     * @param {String} key         key of the export-link
     * @param {String} nodeId      optional id of the node to check to get the available export-link (form-keys)
     * @param {Boolean} flgMaster  true if it is the master-node
     * @returns {String}           url
     */
    me.getAvailiableExportLink = function(key, nodeId, flgMaster) {
        return me.availiableExportLinks[key];
    };

    /**
     * set data for the export-link (url)
     * @param {String} key         key of the export-link
     * @param {String} url         url for the export-link
     */
    me.setAvailiableExportLink = function(key, url) {
        me.availiableExportLinks[key] = url;
        return me.availiableExportLinks[key];
    };

    /**
     * return all available static-export-link-keys for the node "mindmap, html...)
     * @param {String} nodeId      optional id of the node to check to get the available static-export-links (link-keys)
     * @param {Boolean} flgMaster  true if it is the master-node
     * @returns {Array}            available static-export-link-keys for the node
     */
    me.getAvailiableStaticExportLinkKeys = function(nodeId, flgMaster) {
        return Object.keys(me.availiableStaticExportLinks);
    };

    /**
     * return data of the static-export-link (url)
     * @param {String} key         key of the static-export-link
     * @param {String} nodeId      optional id of the node to check to get the available static-export-link (form-keys)
     * @param {Boolean} flgMaster  true if it is the master-node
     * @returns {String}           url
     */
    me.getAvailiableStaticExportLink = function(key, nodeId, flgMaster) {
        return me.availiableStaticExportLinks[key];
    };

    /**
     * set data for the static-export-link (url)
     * @param {String} key         key of the static-export-link
     * @param {String} url         url for the static-export-link
     */
    me.setAvailiableStaticExportLink = function(key, url) {
        me.availiableStaticExportLinks[key] = url;
        return me.availiableStaticExportLinks[key];
    };

    /**
     * return all available import-form-keys for the node "json, wiki...)
     * @param {String} nodeId      optional id of the node to check to get the available import-forms (form-keys)
     * @param {Boolean} flgMaster  true if it is the master-node
     * @returns {Array}            available import-form-keys for the node
     */
    me.getAvailiableImportFormKeys = function(nodeId, flgMaster) {
        return Object.keys(me.availiableImportForms);
    };

    /**
     * return data of the import-form (url)
     * @param {String} key         key of the import-form
     * @param {String} nodeId      optional id of the node to check to get the available import-form (form-keys)
     * @param {Boolean} flgMaster  true if it is the master-node
     * @returns {String}           url
     */
    me.getAvailiableImportForm = function(key, nodeId, flgMaster) {
        return me.availiableImportForms[key];
    };

    /**
     * set data for the import-form (url)
     * @param {String} key         key of the import-form
     * @param {String} url         url for the import-form
     */
    me.setAvailiableImportForm = function(key, url) {
        me.availiableImportForms[key] = url;
        return me.availiableImportForms[key];
    };

    /**
     * return all available node-action-keys for the node "create, edit...)
     * @param {String} nodeId      optional id of the node to check to get the available node-action (action-keys)
     * @param {Boolean} flgMaster  true if it is the master-node
     * @returns {Array}            available import-form-keys for the node
     */
    me.getAvailiableNodeActionKeys = function(nodeId, flgMaster) {
        return Object.keys(me.availiableNodeActions);
    };

    /**
     * return data of the node-action (url|flag)
     * @param {String} key            key of the node-action
     * @param {String} nodeId         optional id of the node to check to get the available node-action (action-keys)
     * @param {Boolean} flgMaster     true if it is the master-node
     * @returns {String|Boolean} url  url/flag for the node-action
     */
    me.getAvailiableNodeAction = function(key, nodeId, flgMaster) {
        if (key === 'delete' && flgMaster) {
            return false;
        }
        if (key === 'frontpagebaseurl') {
            return me.availiableNodeActions[key] + nodeId;
        }
        return me.availiableNodeActions[key];
    };

    /**
     * set data for the node-action (url|flag)
     * @param {String} key            key of the node-action
     * @param {String|Boolean} url    url/flag for the node-action
     */
    me.setAvailiableNodeAction = function(key, url) {
        me.availiableNodeActions[key] = url;
        return me.availiableNodeActions[key];
    };

    me._init();
    
    return me;
};
