/**
 * defines elements of the gantt-diagrams of YAIO
 */
'use strict';

var YAIOGanttPage = function() {
    var me = this;
    
    me.ganttId = 'DT2015061620443947215';
    
    me.linkTabTogglerGantt = "#tabTogglerGantt";
    me.linkTabTogglerData = "#tabTogglerData";
    
    me.inputGanttRangeStart = '#inputGanttRangeStart';
    me.inputGanttRangeEnde = '#inputGanttRangeEnde';
    
    me.buttonRecalcGanttIst = '[translate="fancytree.table.headline.recalcGanttIst"]';
    me.buttonRecalcGanttPlan = '[translate="fancytree.table.headline.recalcGanttPlan"]';
    
    me.masterGantt = "#masterTr > td.block_nodegantt";
    me.masterData = "#masterTr > td.block_nodedata";
    
    
    /**
     * checks the ganttline (plan and ist) for nodeId
     * if plan/istData is set {aufwand: "10h", style: "width: 92px; margin-left: 0px;"} these values are checked
     * @param nodeId    id of the node to check the ganttline for
     * @param planData  expected values for plan {aufwand: "10h", style: "width: 92px; margin-left: 0px;"}
     * @param istData   expected values for ist {aufwand: "10h", style: "width: 92px; margin-left: 0px;"}
     */
    me.checkGanttLine = function(nodeId, planData, istData) {
        me.checkGanttBar(nodeId, 'plan', planData);
        me.checkGanttBar(nodeId, 'ist', istData);
    }

    /**
     * checks the ganttbar (plan or ist) for nodeId
     * if data is set {aufwand: "10h", style: "width: 92px; margin-left: 0px;"} these values are checked
     * @param nodeId    id of the node to check the ganttline for
     * @param modus     bar to check (plan or ist)
     * @param data      expected values for bar {aufwand: "10h", style: "width: 92px; margin-left: 0px;"}
     */
    me.checkGanttBar = function(nodeId, modus, data) {
        // check aufwand
        if (data.aufwand) {
            expect($('div#gantt_' + modus + 'ChildrenSum_aufwand_' + nodeId + ' span.gantt_aufwand_value').getText()).toBe(data.aufwand);
        } else {
            expect($('div#gantt_' + modus + 'ChildrenSum_aufwand_' + nodeId + ' span.gantt_aufwand_value').getText()).toBe("");
        }
        
        // check style (margin, width)
        if (data.style) {
            $('div#gantt_' + modus + 'ChildrenSum_bar_' + nodeId).getAttribute('style')
                .then(function getStyle(style) {
                    // normalize with
                    style = style.replace(/\.\d+px/g, 'px').trim();
                    var expected = data.style.replace(/\.\d+px/g, 'px').trim();
                    expect(style).toBe(expected);
                });
        }
    }

};
module.exports = YAIOGanttPage;
