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
 * servicefunctions for parsing checklists
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
JsHelferlein.ChecklistParserService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.AbstractParserService(appBase);

    // states
    me.checkListConfigs = {
        "checklist-state-OPEN": {
            styleClassPraefix: "checklist-state-",
            matchers: ["OPEN", "OFFEN", "o", "O", "0", "TODO"]
        },
        "checklist-state-RUNNING": {
            styleClassPraefix: "checklist-state-",
            matchers: ["RUNNING"]
        },
        "checklist-state-LATE": {
            styleClassPraefix: "checklist-state-",
            matchers: ["LATE"]
        },
        "checklist-state-BLOCKED": {
            styleClassPraefix: "checklist-state-",
            matchers: ["BLOCKED", "WAITING", "WAIT"]
        },
        "checklist-state-WARNING": {
            styleClassPraefix: "checklist-state-",
            matchers: ["WARNING"]
        },
        "checklist-state-DONE": {
            styleClassPraefix: "checklist-state-",
            matchers: ["DONE", "OK", "x", "X", "ERLEDIGT"]
        },
        "checklist-test-TESTOPEN": {
            styleClassPraefix: "checklist-test-",
            matchers: ["TESTOPEN"]
        },
        "checklist-test-PASSED": {
            styleClassPraefix: "checklist-test-",
            matchers: ["PASSED"]
        },
        "checklist-test-FAILED": {
            styleClassPraefix: "checklist-test-",
            matchers: ["FAILED", "ERROR"]
        }
    };

    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * executes checklist-formatter (add span with checklist-Styles) on the block [use me.checkListConfigs]
     * @FeatureDomain                GUI
     * @FeatureResult                updates DOM
     * @FeatureKeywords              Convert
     * @param blockId                filter to identify the block to format
     */
    me.parseBlock = function(blockId) {
        me.$(blockId).each(function(i, block) {
            console.log("ChecklistParserService.renderBlock " + blockId);
            me.highlightCheckList(block);
        });
    };

    /**
     * executes checklist-formatter (add span with checklist-Styles) on the block [use me.checkListConfigs]
     * @FeatureDomain                GUI
     * @FeatureResult                updates DOM
     * @FeatureKeywords              Convert
     * @param descBlock              id-filter to identify the block to format
     */
    me.highlightCheckList = function(descBlock) {
        var descBlockId = me.$(descBlock).attr('id');
        console.log("highlightCheckList highlight for descBlock: " + descBlockId);

        // tests
        for (var idx in me.checkListConfigs) {
            var matchers = me.checkListConfigs[idx].matchers;
            me._highlightCheckListForMatchers(descBlock, matchers, idx, '');
        }
    };

    /**
     * executes checklist-formatter (add span with checklistFormat) with style and styleclass for all matchers "[XXX]" on descBlock
     * @FeatureDomain                GUI
     * @FeatureResult                updates DOM
     * @FeatureKeywords              Convert
     * @param descBlock              id-filter to identify the block to format
     * @param matchers               list of matcher which will call as stringfilter of "[" + matcher + "]" to identify checklist-entry
     * @param styleClass             styleClass to add to span for matcher found
     * @param style                  style to add to new span for matcher found
     */
    me._highlightCheckListForMatchers = function(descBlock, matchers, styleClass, style) {
        var descBlockId = me.$(descBlock).attr('id');
        console.log("highlightCheckListForMatchers matchers '" + matchers + "' for descBlock: " + descBlockId);
        for (var idx in matchers) {
            me._highlightCheckListForMatcher(descBlock, "[" + matchers[idx] + "]", styleClass, style);
        }
    };

    /**
     * executes checklist-formatter (add span with checklistFormat) with style and styleclass for all matchers "[XXX]" on descBlock
     * @FeatureDomain                GUI
     * @FeatureResult                updates DOM
     * @FeatureKeywords              Convert
     * @param descBlock              id-filter to identify the block to format
     * @param matcherStr             matcher will call as stringfilter to identify checklist-entry
     * @param styleClass             styleClass to add to span for matcher found
     * @param style                  style to add to new span for matcher found
     */
    me._highlightCheckListForMatcher = function(descBlock, matcherStr, styleClass, style) {
        var descBlockId = me.$(descBlock).attr('id');
        console.log("highlightCheckListForMatcher matcherStr '" + matcherStr + "' for descBlock: " + descBlockId);
        me.$("#" + descBlockId + " li:contains('" + matcherStr + "'),h1:contains('" + matcherStr + "'),h2:contains('" + matcherStr + "')").each(function(index, value) {
            var regEx = RegExp(me.appBase.get('DataUtils').escapeRegExp(matcherStr), 'gi');
            findAndReplaceDOMText(me.$(value).get(0), {
                find: regEx,
                replace: function(portion) {
                    var el = document.createElement('span');
                    if (style) {
                        el.style = style;
                    }
                    if (styleClass) {
                        el.className = styleClass;
                    }
                    el.innerHTML = portion.text;
                    return el;
                }
            });
        });
    };

    me._init();
    
    return me;
};
