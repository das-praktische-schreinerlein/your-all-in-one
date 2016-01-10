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
 * servicefunctions for converting explorer-content to markdown/jira...
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.ExplorerConverterService = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };


    /**
     * extract data from explorerlines (table.fancytree-ext-table tr) and format
     * them as linked markdown-checklists ([state] - [title](yaio:number)
     * @FeatureDomain                GUI
     * @FeatureResult                return String - checklist in yaio-markdown-format
     * @FeatureKeywords              Convert
     * @return                       {String}  checklist in yaio-markdown-format
     */
    me.convertExplorerLinesAsCheckList = function() {
        // get title
        var idx;
        var title = me.$("#masterTr td.fieldtype_name").text();
        var now = me.appBase.get('DataUtils').formatGermanDateTime((new Date()).getTime());

        var checkList = "# Checklist: " + title + " (Stand: " + now + ")\n\n";

        // iterate all nodelines
        me.$("table.fancytree-ext-table tr").each(function(i, line) {
            // extract data
            var titleSpan = me.$(line).find("span.fancytree-title2");
            var stateSpan = me.$(line).find("span.fancytree-title-state");
            var numberSpan = me.$(line).find("div.field_metanummer");
            var levelSpan = me.$(line).find("span.fancytree-node");
            var istStandDiv = me.$(line).find("div.fieldtype_stand.field_istChildrenSumStand");
            var istAufwandDiv = me.$(line).find("div.fieldtype_aufwand.field_istChildrenSumAufwand");
            var planAufwandDiv = me.$(line).find("div.fieldtype_aufwand.field_planChildrenSumAufwand");

            // extract content
            var level = 0;
            var title = null;
            var state = null;
            var number = null;
            var istStand = "0%";
            var istAufwand = "0h";
            var planAufwand = null;
            if (me.$(levelSpan).size() > 0) {
                // extract level from intend
                var intend = me.$(levelSpan).css("margin-left").replace("px", "");
                level = parseInt(intend, 10) / 20;
            }
            if (me.$(stateSpan).size() > 0) {
                // extract state from style
                idx = me._extractCheckListStatefromStateSpan(me.$(stateSpan));
                if (idx) {
                    state = idx;
                    state = state.replace("checklist-state-", "");
                    state = state.replace("checklist-test-", "");
                }
            }
            if (me.$(titleSpan).size() > 0) {
                title = me.$(titleSpan).text();
            }
            if (me.$(numberSpan).size() > 0) {
                number = me.$(numberSpan).text();
            }

            if (me.$(istAufwandDiv).size() > 0) {
                istAufwand = me.$(istAufwandDiv).text();
            }
            if (me.$(planAufwandDiv).size() > 0) {
                planAufwand = me.$(planAufwandDiv).text();
            }
            if (me.$(istStandDiv).size() > 0) {
                istStand = me.$(istStandDiv).text();
            }

            var stand = istStand.trim() + " (" + istAufwand.trim();
            if (planAufwand) {
                stand  += "/" + planAufwand.trim();
            }
            stand += ")";

            // if all set: generate checklist
            console.log("state:" + state + " title:" + title + " number:" + number + " level:" + level + " stand:" + stand);
            if (title && state && number) {
                for (idx = 0; idx < level; idx ++) {
                    checkList += "    ";
                }
                checkList += "- [" + state + "] - [" + title + "](yaio:" + number + ") " + stand + "\n";
            }
        });

        return checkList;
    };

    me._extractCheckListStatefromStateSpan = function(block) {
        // iterate all configs
        var checkListConfigs = me.appBase.get('ChecklistParser').checkListConfigs;
        for (var idx in checkListConfigs) {
            var matchers = checkListConfigs[idx].matchers;

            // iterate all matchers
            for (var idx2 in matchers) {
                // check for matcher in style
                if (block.hasClass("node-state-" + matchers[idx2])) {
                    return idx;
                }
            }
        }
        return null;
    };

    /**
     * extract data from explorerlines (table.fancytree-ext-table tr) and format
     * them as mermaid-gantt-markdown
     * @FeatureDomain                GUI
     * @FeatureResult                return String - mermaid-gantt-markdown
     * @FeatureKeywords              Convert
     * @return                       {String}    mermaid-gantt-markdown
     */
    me.convertExplorerLinesAsGanttMarkdown = function() {
        // get title
        var title = me.$("#masterTr td.fieldtype_name").text();
        var now = me.appBase.get('DataUtils').formatGermanDateTime((new Date()).getTime());

        var ganttMarkdown = "# Gantt: " + title + " (Stand: " + now + ")\n\n"
            + "```mermaid\n"
            + "gantt\n"
            + "    title " + title + " (Stand: " + now + ")\n"
            + "    dateFormat  DD.MM.YYYY\n"
            + "\n";
        var ganttMarkdownPlan = "";
        var ganttMarkdownIst  = "";

        // iterate all nodelines
        me.$("table.fancytree-ext-table tr").each(function(i, line) {
            // extract data
            var titleSpan = me.$(line).find("span.fancytree-title2");
            var numberSpan = me.$(line).find("div.field_metanummer");
            var startEndPlanDiv = me.$(line).find("div.fieldtype_fromto.field_planChildrenSum");
            var startEndIstDiv = me.$(line).find("div.fieldtype_fromto.field_istChildrenSum");

            // extract content
            var title = null;
            var number = null;
            if (me.$(titleSpan).size() > 0) {
                title = me.$(titleSpan).text();
            }
            if (me.$(numberSpan).size() > 0) {
                number = me.$(numberSpan).text();
            }
            ganttMarkdownPlan += me._generateGanttMarkdownLineFromBlock(title, number, startEndPlanDiv);
            ganttMarkdownIst += me._generateGanttMarkdownLineFromBlock(title, number, startEndIstDiv);
        });

        // concat
        ganttMarkdownPlan = ganttMarkdownPlan.length > 0 ? "    section Plan\n" + ganttMarkdownPlan : "";
        ganttMarkdownIst = ganttMarkdownIst.length > 0 ? "    section Ist\n" + ganttMarkdownIst : "";
        ganttMarkdown += ganttMarkdownPlan + ganttMarkdownIst  + "```\n";

        return ganttMarkdown;
    };


    /**
     * generate a mermaid-gantt-markdown-line for selector (if start, end-date can be extracted)
     * @FeatureDomain                GUI
     * @FeatureResult                return String - mermaid-gantt-markdown
     * @FeatureKeywords              Convert
     * @param title                  title of the line
     * @param number                 referenc
     * @param selector               seletor to filter the element with jquery
     * @return                       {String}      mermaid-gantt-markdown-line
     */
    me._generateGanttMarkdownLineFromBlock = function(title, number, selector) {
        if (me.$(selector).size() > 0) {
            // extract dates
            var dates = me.$(selector).html().replace(/&nbsp;/g, ' ').split("-");
            if (dates.length != 2) {
                return "";
            }
            var start = dates[0];
            var end = dates[1];

            // if all set: generate gantt
            console.log("extractGanttMarkdownLineFromBlock: title:" + title + " number:" + number + " start:" + start + " end:" + end);
            if (title && number && start && end) {
                return "    " + title + ": " + number + ", " + start + ", " + end + "\n";
            }
        }
        return "";
    };


    me._init();
    
    return me;
};
