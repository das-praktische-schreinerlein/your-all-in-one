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

/**
 * <h4>FeatureDomain:</h4>
 *     Utils
 * <h4>FeatureDescription:</h4>
 *     factory to create yaioUtils with util-functions
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new yaioUtils obj
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Utils
 */
yaioM.factory('yaioUtils', function () {
    var ganttRangeStart = yaioAppBase.get('YaioBaseService').formatGermanDate((new Date()).getTime() - 90*24*60*60*1000); 
    var ganttRangeEnd = yaioAppBase.get('YaioBaseService').formatGermanDate((new Date()).getTime() + 90*24*60*60*1000);
    
    return {
        /**
         * <h4>FeatureDomain:</h4>
         *     Help
         * <h4>FeatureDescription:</h4>
         *     callbackhandler to open helpsite
         * <h4>FeatureResult:</h4>
         *   <ul>
         *     <li>open the helpsite
         *   </ul> 
         * <h4>FeatureKeywords:</h4>
         *     GUI Callback
         * @param url - the url of the helpsite
         */
        showHelpSite: function(url) {
            console.log("showHelpSite:" + " url:" + url);
            yaioShowHelpSite(url);
            return false;
        },
        
        toggleSysContainerForNode: function(node) {
            toggleNodeSysContainer(node.sysUID);
        },
        
        openNodeEditorForNode: function(node, mode) {
            yaioAppBase.get('YaioEditorService').yaioOpenNodeEditorForNode(node, mode);
        },
        
        renderNodeLine: function(node, trIdSelector) {
            // load me
            var data = {
                 node: {
                     data: {
                         basenode: node
                     },
                     tr: trIdSelector
                 } 
            };
            
            console.log("renderNodeLine nodeId=" + node.sysUID + " tr=" + $(trIdSelector).length);
            renderColumnsForNode(null, data, true);
        },
        
        ganttOptions: { 
            ganttRangeStart: ganttRangeStart, 
            ganttRangeEnd: ganttRangeEnd
        }
    };
});
