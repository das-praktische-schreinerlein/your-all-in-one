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
    var jsFuncTestId = "JsFuncTest1";
    
    // explorer-link
    me.linkFrontpage = $('[translate="common.command.OpenFrontpage"]');
    
    // mastercontainer
    me.containerMasterdata = $('#masterdata');
    me.expanderSysPlay1 = $('#expander' + sysplayId);
    me.linkCreateChildSysTest1 = $('#cmdCreate' + systestId);
    me.expanderSysTest1 = $('#expander' + systestId);
    me.linkCreateChildJsFuncTest1 = $('#cmdCreate' + jsFuncTestId);
    
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
        protractor.utils.waitUntilElementPresent(yaioFrontPage.fontContentLeft, protractor.utils.CONST_WAIT_ELEMENT);
        expect(yaioFrontPage.fontContentLeft.getAttribute('id')).toEqual("front-content-left");
        
        // open explorer-link
        yaioFrontPage.linkExplorer.click();
        
        return protractor.utils.waitUntilElementClickable(me.expanderSysPlay1, protractor.utils.CONST_WAIT_ELEMENT);
    };

    /**
     * open node SysTests1, open createNodeDialog and wait until present
     * @returns {Promise}       browser.wait for element "#inputCreateNodeType"
     */
    me.navigateToJsFuncTest1ndOpenCreateNode = function () {
        // expect SysPlay1
        expect(me.expanderSysPlay1.getAttribute('id')).toEqual('expanderSysPlay1');
        
        // expand SysPlay1
        me.expanderSysPlay1.click();
        protractor.utils.waitUntilElementClickable(me.linkCreateChildSysTest1, protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect(me.linkCreateChildSysTest1.getAttribute('id')).toEqual('cmdCreateSysTest1');
        
        // expand SysTest1
        me.expanderSysTest1.click();
        protractor.utils.waitUntilElementClickable(me.linkCreateChildJsFuncTest1, protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect(me.linkCreateChildJsFuncTest1.getAttribute('id')).toEqual('cmdCreateJsFuncTest1');

        // create child for JsFuncTest1
        me.linkCreateChildJsFuncTest1.click();
        return protractor.utils.waitUntilElementPresent(me.inputCreateNodeType, protractor.utils.CONST_WAIT_ELEMENT);
    };

    /**
     * create TaskNode and wait until present
     * @returns {Element}       promise on the taskname
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
        protractor.utils.waitUntilElementPresent(me.uiDatePickerDay1, protractor.utils.CONST_WAIT_ELEMENT);
        me.uiDatePickerDay1.click();
        me.inputPlanEndeTaskNode.click();
        protractor.utils.waitUntilElementPresent(me.uiDatePickerDay25, protractor.utils.CONST_WAIT_ELEMENT);
        me.uiDatePickerDay25.click();
        
        // toggle desc
        me.filterDescTaskForm_Off.click();
        protractor.utils.waitUntilElementPresent(me.inputNodeDescTaskNode, protractor.utils.CONST_WAIT_ELEMENT);
        expect(me.inputNodeDescTaskNode.getAttribute('id')).toEqual('inputNodeDescTaskNode');
        // me.inputNodeDescTaskNode.sendKeys("fehlerhafte Tetsdaten");
        
        // define SearchElement
        var eleNewTaskName = element(by.cssContainingText('span.fancytree-title2', taskName));

        // submit form
        protractor.utils.waitUntilElementPresent(me.buttonSaveTask, protractor.utils.CONST_WAIT_ELEMENT);
        expect(me.buttonSaveTask.isDisplayed()).toEqual(true);
        me.buttonSaveTask.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;
    
            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(me.linkCreateChildSysTest1, protractor.utils.CONST_WAIT_NODEHIRARCHY);
            expect(me.linkCreateChildJsFuncTest1.getAttribute('id')).toEqual('cmdCreateJsFuncTest1');
            
            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(eleNewTaskName, protractor.utils.CONST_WAIT_ELEMENT);
            expect(eleNewTaskName.getText()).toEqual(taskName);
            browser.ignoreSynchronization = false;
        });
        
        return eleNewTaskName;
    };

    /**
     * navigate to TaskNode by id, edit and wait until new site present
     * @param   {Integer} nodeId  noid of the node to delete
     * @returns {Promise}         promise 
     */
    me.editTaskNodeById = function (nodeId) {
        expect(nodeId).toMatch(/DT.*/);

        var linkCmdEditNode = $('#cmdEdit' + nodeId);
        protractor.utils.waitUntilElementClickable(linkCmdEditNode, protractor.utils.CONST_WAIT_ELEMENT);
        expect(linkCmdEditNode.isDisplayed()).toEqual(true);

        // define SearchElement
        var taskName = 'correct testask' + new Date().getTime();
        var eleNewTaskName = element(by.cssContainingText('span.fancytree-title2', taskName));

        linkCmdEditNode.click().then(function () {
            // set new taskdata and submit form
            protractor.utils.waitUntilElementPresent(me.inputNameTaskNode, protractor.utils.CONST_WAIT_ELEMENT);
            me.inputNameTaskNode.clear().then(function () {
                me.inputNameTaskNode.sendKeys(taskName);
            });
            me.inputPlanAufwandTaskNode.clear().then(function () {
                me.inputPlanAufwandTaskNode.sendKeys("10");
            });
            me.inputPlanStartTaskNode.click();
            protractor.utils.waitUntilElementPresent(me.uiDatePickerDay1, protractor.utils.CONST_WAIT_ELEMENT);
            me.uiDatePickerDay1.click();
            me.inputPlanEndeTaskNode.click();
            protractor.utils.waitUntilElementPresent(me.uiDatePickerDay25, protractor.utils.CONST_WAIT_ELEMENT);
            me.uiDatePickerDay25.click();
            
            // toggle desc
            me.filterDescTaskForm_Off.click();
            protractor.utils.waitUntilElementPresent(me.inputNodeDescTaskNode, protractor.utils.CONST_WAIT_ELEMENT);
            expect(me.inputNodeDescTaskNode.getAttribute('id')).toEqual('inputNodeDescTaskNode');
            // me.inputNodeDescTaskNode.sendKeys("fehlerhafte Tetsdaten");

            // submit form
            protractor.utils.waitUntilElementPresent(me.buttonSaveTask, protractor.utils.CONST_WAIT_ELEMENT);
            expect(me.buttonSaveTask.isDisplayed()).toEqual(true);
            me.buttonSaveTask.click().then(function () {
                // wait for result
                browser.ignoreSynchronization = true;
        
                // wait till data is loaded
                protractor.utils.waitUntilElementClickable(me.linkCreateChildSysTest1, protractor.utils.CONST_WAIT_NODEHIRARCHY);
                expect(me.linkCreateChildJsFuncTest1.getAttribute('id')).toEqual('cmdCreateJsFuncTest1');
                
                // wait till data is loaded
                protractor.utils.waitUntilElementPresent(eleNewTaskName, protractor.utils.CONST_WAIT_ELEMENT);
                expect(eleNewTaskName.getText()).toEqual(taskName);
                browser.ignoreSynchronization = false;
            });
        });

        return eleNewTaskName;
    };

    /**
     * extract NnodeId from TaskNameElement
     * @param   {Element} eleTaskName       tasknameElement
     * @returns {Promise}                   promise to get nodeId 
     */
    me.extractNodeIdFromTaskNameElement = function (eleTaskName) {
        protractor.utils.waitUntilElementPresent(eleTaskName, protractor.utils.CONST_WAIT_ELEMENT);
        return eleTaskName.getAttribute('id').then(function (titleId) {
            return titleId.replace(/title/, '');
        });
    };
    
    /**
     * navigate to TaskNode by id, call delete and wait until new site present without nodeId
     * @param   {Integer} nodeId  noid of the node to delete
     * @returns {Promise}         promise 
     */
    me.deleteNodeById = function (nodeId) {
        expect(nodeId).toMatch(/DT.*/);

        var linkCmdRemoveNode = $('#cmdRemove' + nodeId);
        protractor.utils.waitUntilElementClickable(linkCmdRemoveNode, protractor.utils.CONST_WAIT_ELEMENT);
        expect(linkCmdRemoveNode.isDisplayed()).toEqual(true);
        
        linkCmdRemoveNode.click().then(function () {
            var alertDialog = browser.switchTo().alert();
            alertDialog.accept();
    
            // wait for result
            browser.ignoreSynchronization = true;
            
            // wait till data is loaded
            protractor.utils.waitUntilElementClickable(me.linkCreateChildJsFuncTest1, protractor.utils.CONST_WAIT_NODEHIRARCHY);
            expect(me.linkCreateChildJsFuncTest1.getAttribute('id')).toEqual('cmdCreateJsFuncTest1');
            
            // wait till data is loaded
            protractor.utils.waitThatElementIsNotPresent(linkCmdRemoveNode, protractor.utils.CONST_WAIT_ELEMENT);
            expect(linkCmdRemoveNode.isPresent()).toEqual(false);
            browser.ignoreSynchronization = false;
        });
        
        return linkCmdRemoveNode;
    };
};
module.exports = YAIONodePage;
