/**
 * defines elements of the gantt-diagrams of YAIO
 */
'use strict';

var YAIOGanttPage = function() {
    var me = this;
    
    me.linkTabTogglerGantt = "#tabTogglerGantt";
    me.linkTabTogglerData = "#tabTogglerData";
    
    me.inputGanttRangeStart = '#inputGanttRangeStart';
    me.inputGanttRangeEnde = '#inputGanttRangeEnde';
    
    me.buttonRecalcGanttIst = '[translate="fancytree.table.headline.recalcGanttIst"]';
    me.buttonRecalcGanttPlan = '[translate="fancytree.table.headline.recalcGanttPlan"]';
    
    me.masterGantt = "#masterTr > td.block_nodegantt";
    me.masterData = "#masterTr > td.block_nodedata";

};
module.exports = YAIOGanttPage;
