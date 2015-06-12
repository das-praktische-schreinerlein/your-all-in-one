/**
 * defines elements of the NodePage of YAIO
 */
'use strict';
var YAIOFrontPage = require('../frontpage/frontpage.po.js');

var YAIONodePage = function() {
    var yaioFrontPage = new YAIOFrontPage();
    var me = this;

    var sysplayId = 'SysPlay1';
    var systestId = "SysTest1";
    
    // explorer-link
    me.linkFrontpage = $('[translate="common.command.OpenFrontpage"]');
    
    // mastercontainer
    me.containerMasterdata = $('#masterdata');
    me.expanderSysPlay1 = $('#expander' + sysplayId);
    me.linkCreateChildSysTest1 = $('#cmdCreate' + systestId);
    
    // create form
    me.inputCreateNodeType = $('#inputCreateNodeType');

    // taskform
    me.inputNameTaskNode = $('#inputNameTaskNode');
    me.inputTypeTaskNode = $('#inputTypeTaskNode');
    me.inputPlanAufwandTaskNode = $('#inputPlanAufwandTaskNode');
    me.inputPlanStartTaskNode = $('#inputPlanStartTaskNode');
    me.inputPlanEndeTaskNode = $('#inputPlanEndeTaskNode');
    me.filterDescTaskForm_Off = $('#filterDescTaskForm_Off');
    me.inputNodeDescTaskNode = $('#inputNodeDescTaskNode');
    me.buttonSaveTask = $('#nodeFormTaskNode fieldset button[translate="common.buttonSave"]');
    
    // utils
    me.idUiDatePicker = "#ui-datepicker-div";
    me.uiDatePickerDay1 = $$(me.idUiDatePicker + " table.ui-datepicker-calendar tbody tr td a").get(1);
    me.uiDatePickerDay25 = $$(me.idUiDatePicker + " table.ui-datepicker-calendar tbody tr td a").get(25);
    
    /**
     * open explorer from frontpage and wait until present
     * @returns {Promise}       browser.wait for element "#front-content-left"
     */
    me.openExplorerFromFrontPage = function () {
        // expect frontPage
        protractor.utils.waitUntilElementPresent(yaioFrontPage.fontContentLeft, 2000);
        expect(yaioFrontPage.fontContentLeft.getAttribute('id')).toEqual("front-content-left");
        
        // open explorer-link
        yaioFrontPage.linkExplorer.click();
        
        return protractor.utils.waitUntilElementPresent(me.expanderSysPlay1, 2000);
    };

    /**
     * open node SysTests1, open createNodeDialog and wait until present
     * @returns {Promise}       browser.wait for element "#inputCreateNodeType"
     */
    me.navigateToSysTest1AndOpenCreateNode = function () {
        // expect SysPlay1
        expect(me.expanderSysPlay1.getAttribute('id')).toEqual('expanderSysPlay1');
        
        // expand SysPlay1
        me.expanderSysPlay1.click();
        protractor.utils.waitUntilElementPresent(me.linkCreateChildSysTest1, 2000);
        expect(me.linkCreateChildSysTest1.getAttribute('id')).toEqual('cmdCreateSysTest1');
        
        // create child for SysTest1
        me.linkCreateChildSysTest1.click();
        return protractor.utils.waitUntilElementPresent(me.inputCreateNodeType, 2000);
    };

    /**
     * create TaskNode and wait until present
     * @returns {Promise}       promise on the taskname
     */
    me.createTaskNode = function () {
        expect(me.inputCreateNodeType.getAttribute('id')).toEqual('inputCreateNodeType');

        // select Aufgabe
        me.inputCreateNodeType.sendKeys('Aufgabe\n');
        expect(me.inputNameTaskNode.getAttribute('id')).toEqual('inputNameTaskNode');
        
        // set taskdata and submit form
        var taskName = 'testask' + new Date().getTime();
        me.inputNameTaskNode.sendKeys(taskName);
        me.inputTypeTaskNode.sendKeys("+-- Offen\n");
        me.inputPlanAufwandTaskNode.sendKeys("1");
        me.inputPlanStartTaskNode.click();
        protractor.utils.waitUntilElementPresent(me.uiDatePickerDay1, 2000);
        me.uiDatePickerDay1.click();
        me.inputPlanEndeTaskNode.click();
        protractor.utils.waitUntilElementPresent(me.uiDatePickerDay25, 2000);
        me.uiDatePickerDay25.click();
        
        // toggle desc
        me.filterDescTaskForm_Off.click();
        protractor.utils.waitUntilElementPresent(me.inputNodeDescTaskNode, 2000);
        expect(me.inputNodeDescTaskNode.getAttribute('id')).toEqual('inputNodeDescTaskNode');
        // me.inputNodeDescTaskNode.sendKeys("fehlerhafte Tetsdaten");
        
        // define SearchElement
        var eleNewTaskName = element(by.cssContainingText('span.fancytree-title2', taskName));

        // submit form
        protractor.utils.waitUntilElementPresent(me.buttonSaveTask, 2000);
        expect(me.buttonSaveTask.isDisplayed()).toEqual(true);
        me.buttonSaveTask.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;
    
            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(me.linkCreateChildSysTest1, 10000);
            expect(me.linkCreateChildSysTest1.getAttribute('id')).toEqual('cmdCreateSysTest1');
            
            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(eleNewTaskName, 2000);
            expect(eleNewTaskName.getText()).toEqual(taskName);
            browser.ignoreSynchronization = false;
        });
        
        return eleNewTaskName.getText();
    };
    
};
module.exports = YAIONodePage;
