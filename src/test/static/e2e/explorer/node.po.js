/**
 * defines elements of the NodePage of YAIO
 */
'use strict';

var YAIONodePage = function() {
    var sysplayId = 'SysPlay1';
    var systestId = "SysTest1";
    
    // explorer-link
    this.linkFrontpage = element(by.css('[translate="common.command.OpenFrontpage"]'));
    
    // mastercontainer
    this.containerMasterdata = element(by.id('masterdata'));
    this.expanderSysPlay1 = element(by.id('expander' + sysplayId));
    this.linkCreateChildSysTest1 = element(by.id('cmdCreate' + systestId));
    
    // create form
    this.inputCreateNodeType = element(by.id('inputCreateNodeType'));

    // taskform
    this.inputNameTaskNode = element(by.id('inputNameTaskNode'));
    this.inputTypeTaskNode = element(by.id('inputTypeTaskNode'));
    this.inputPlanAufwandTaskNode = element(by.id('inputPlanAufwandTaskNode'));
    this.inputPlanStartTaskNode = element(by.id('inputPlanStartTaskNode'));
    this.inputPlanEndeTaskNode = element(by.id('inputPlanEndeTaskNode'));
    this.filterDescTaskForm_Off = element(by.id('filterDescTaskForm_Off'));
    this.inputNodeDescTaskNode = element(by.id('inputNodeDescTaskNode'));
    this.buttonSaveTask = element(by.css('#nodeFormTaskNode fieldset button[translate="common.buttonSave"]'));
    
    // utils
    this.idUiDatePicker = "#ui-datepicker-div";
    this.uiDatePickerDay1 = element.all(by.css(this.idUiDatePicker + " table.ui-datepicker-calendar tbody tr td a")).get(1);
    this.uiDatePickerDay25 = element.all(by.css(this.idUiDatePicker + " table.ui-datepicker-calendar tbody tr td a")).get(25);
};
module.exports = YAIONodePage;
