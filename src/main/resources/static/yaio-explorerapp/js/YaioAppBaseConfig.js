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

window.YaioAppBaseConfig = function() {
    // my own instance
    var me = JsHelferlein.AppBaseConfig();
    
    me.additionalDetectorStyleNS = ["yaio-"];

    me.CONST_MasterId = "MasterplanMasternode1";
    me.loginUrl = "/yaio-explorerapp/yaio-explorerapp.html#/login";
    me.baseUrl = "/nodes/";
    me.showUrl = me.baseUrl + "show/";
    me.symLinkUrl = me.baseUrl + "showsymlink/";
    me.updateUrl = me.baseUrl + "update/";
    me.createUrl = me.baseUrl + "create/";
    me.moveUrl = me.baseUrl + "move/";
    me.removeUrl = me.baseUrl + "delete/";

    me.configNodeTypeFields = {
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