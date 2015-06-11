/**
 * defines elements of the NodePage of YAIO
 */
'use strict';

var YAIONodePage = function() {
    var sysplayId = 'SysPlay1';
    var systestId = "SysTest1";
    
    // explorer-link
    this.linkFrontpage = $('[translate="common.command.OpenFrontpage"]');
    
    // mastercontainer
    this.containerMasterdata = $('#masterdata');
    this.expanderSysPlay1 = $('#expander' + sysplayId);
    this.linkCreateChildSysTest1 = $('#cmdCreate' + systestId);
    
    // create form
    this.inputCreateNodeType = $('#inputCreateNodeType');

    // taskform
    this.inputNameTaskNode = $('#inputNameTaskNode');
    this.inputTypeTaskNode = $('#inputTypeTaskNode');
    this.inputPlanAufwandTaskNode = $('#inputPlanAufwandTaskNode');
    this.inputPlanStartTaskNode = $('#inputPlanStartTaskNode');
    this.inputPlanEndeTaskNode = $('#inputPlanEndeTaskNode');
    this.filterDescTaskForm_Off = $('#filterDescTaskForm_Off');
    this.inputNodeDescTaskNode = $('#inputNodeDescTaskNode');
    this.buttonSaveTask = $('#nodeFormTaskNode fieldset button[translate="common.buttonSave"]');
    
    // utils
    this.idUiDatePicker = "#ui-datepicker-div";
    this.uiDatePickerDay1 = $$(this.idUiDatePicker + " table.ui-datepicker-calendar tbody tr td a").get(1);
    this.uiDatePickerDay25 = $$(this.idUiDatePicker + " table.ui-datepicker-calendar tbody tr td a").get(25);
};
module.exports = YAIONodePage;
