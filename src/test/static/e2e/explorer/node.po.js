/**
 * defines elements of the NodePage of YAIO
 */
'use strict';
var YAIOFrontPage = require('../frontpage/frontpage.po.js');

var YAIONodePage = function() {
    var yaioFrontPage = new YAIOFrontPage();
    var me = this;

    // nodeIds
    me.sysplayId = 'SysPlay1';
    me.systestId = "SysTest1";
    me.jsFuncTestId = "JsFuncTest1";
    me.jsFuncTestHierarchy = ['SysPlay1', 'SysTest1', 'JsFuncTest1'];
    
    // explorer-link
    me.linkFrontpage = $('[translate="common.command.OpenFrontpage"]');
    
    // mastercontainer
    me.containerMasterdata = $('#masterdata');
    me.expanderSysPlay1 = $('#expander' + me.sysplayId);
    me.linkCreateChildJsFuncTest1 = $('#cmdCreate' + me.jsFuncTestId);
    
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
    
    // symlinkform
    me.inputNameSymLinkNode = $('#inputNameSymLinkNode');
    me.filterDescSymLinkForm_Off = $('#filterDescSymLinkForm_Off');
    me.inputNodeDescSymLinkNode = $('#inputNodeDescSymLinkNode');
    me.buttonSaveSymLink = $('#nodeFormSymLinkNode fieldset button[translate="common.buttonSave"]');

    // utils
    me.idUiDatePicker = "#ui-datepicker-div";
    me.uiDatePickerDay1 = $$(me.idUiDatePicker + " table.ui-datepicker-calendar tbody tr td a").get(1);
    me.uiDatePickerDay25 = $$(me.idUiDatePicker + " table.ui-datepicker-calendar tbody tr td a").get(25);
    
    /**
     * open masterpage
     * @returns {Browser}  - broser element
     */
    me.openMasternode = function () {
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/show/MasterplanMasternode1');
        return browser;
    }

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
        
        // expect SysPlay1
        protractor.utils.waitUntilElementClickable(me.expanderSysPlay1, protractor.utils.CONST_WAIT_ELEMENT);
        expect(me.expanderSysPlay1.getAttribute('id')).toEqual('expanderSysPlay1');
        return protractor.utils.waitUntilElementClickable(me.expanderSysPlay1, protractor.utils.CONST_WAIT_ELEMENT);

    };

    /**
     * open node-hierarchy and wait until present
     * @param {Array} nodeIdHirarchy       array of nodeids to expand recursively
     * @returns {Promise}                  promise on the cmdCreate if fullfilled
     */
    me.navigateToNode = function (nodeIdHirarchy) {
        var resultArr = [];
        
        // call navigateToNode
        function _recursive(idx) {
            if (idx >= nodeIdHirarchy.length) return resultArr.pop();
            return me.expandNode(nodeIdHirarchy[idx], nodeIdHirarchy.length == idx).then(function(res) {
                resultArr.push(res);
                return _recursive(idx + 1);
            });
        }

        return _recursive(0);
    };
    
    /**
     * expand with click on #expander{id} node and wait until present, if flag last is set then only check if #cmdCreate{id} is clickable
     * @param {Integer} id      id of the node to expand
     * @param {Boolean} last    flag if the node is the last of the hierarchy
     * @returns {Promise}       browser.wait for element "#cmdCreate..."
     */
    me.expandNode = function (id, last) {
        // console.log("expandNode: check for " + '#expander' + id);
        var expander = $('#expander' + id);
        var createChild = $('#cmdCreate' + id);
        var deferred = protractor.promise.defer();
        var promise = deferred.promise;
        if (last) {
            // last element: check only for link
            // console.log("expandNode: check for " + '#expander' + id);
            protractor.utils.waitUntilElementClickable(createChild, protractor.utils.CONST_WAIT_NODEHIRARCHY).then (function () {
                deferred.fulfill(expander);
//            }, function (err) {
//                deferred.reject(new Error("Element not found #expander" + id + " :" + err));
            });
        } else {
            // expand and check
            // console.log("expandNode: wait for clickable for " + '#expander' + id);
            protractor.utils.waitUntilElementClickable(expander, protractor.utils.CONST_WAIT_NODEHIRARCHY).then( function expanderClickable() {
                // clickable 
                // console.log("expandNode: click " + '#expander' + id);
                expander.click().then(function okCallBack() {
                    // click passed
                    // console.log("expandNode: click passed " + '#expander' + id);
                    deferred.fulfill(expander);
//                }, function errCallBack(err) {
//                    // click failed
//                    // console.log("expandNode: click failed" + '#expander' + id);
//                    deferred.reject(new Error("Error while clicking #expander" + id + " : " + err));
                });
//            }, function expandernotClickable(err) {
//                // Error not clickable; 
//                // console.log("expandNode: not clickable " + '#expander' + id);
//                deferred.reject(new Error("Element not clickable #expander" + id + " :" + err));
            });
            
        }

        return promise;
    };

    /**
     * create TaskNode and wait until present
     * @param   {String}  parentId  id of the parentNode
     * @returns {Element}           element-filter on the newTaskname
     */
    me.openNodeEditorAndCreateTaskNode = function (parentId) {
        var linkCmdCreateNode = $('#cmdCreate' + parentId);
        protractor.utils.waitUntilElementClickable(linkCmdCreateNode, protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect(linkCmdCreateNode.getAttribute('id')).toEqual('cmdCreate' + parentId);

        // create child for parentId
        linkCmdCreateNode.click();
        protractor.utils.waitUntilElementPresent(me.inputCreateNodeType, protractor.utils.CONST_WAIT_ELEMENT);
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
    
            // wait till parent- data is loaded
            protractor.utils.waitUntilElementPresent(linkCmdCreateNode, protractor.utils.CONST_WAIT_NODEHIRARCHY);
            expect(linkCmdCreateNode.getAttribute('id')).toEqual('cmdCreate' + parentId);
            
            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(eleNewTaskName, protractor.utils.CONST_WAIT_ELEMENT);
            expect(eleNewTaskName.getText()).toEqual(taskName);
            browser.ignoreSynchronization = false;
        });
        
        return eleNewTaskName;
    };

    /**
     * navigate to TaskNode by id, edit and wait until new site present
     * @param   {Integer} nodeId  nodeId of the node to edit
     * @returns {Element}         element-filter on the editedTaskName
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
                protractor.utils.waitUntilElementPresent(eleNewTaskName, protractor.utils.CONST_WAIT_NODEHIRARCHY);
                expect(eleNewTaskName.getText()).toEqual(taskName);
                browser.ignoreSynchronization = false;
            });
        });

        return eleNewTaskName;
    };


    /**
     * create SymLinkNode and wait until present
     * @param   {String}  parentId  id of the parentNode
     * @returns {Element}           element-filter on the newSymLinkname
     */
    me.openNodeEditorAndCreateSymLinkNode = function (parentId) {
        var linkCmdCreateNode = $('#cmdCreateSymLink' + parentId);
        protractor.utils.waitUntilElementClickable(linkCmdCreateNode, protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect(linkCmdCreateNode.getAttribute('id')).toEqual('cmdCreateSymLink' + parentId);

        // create child for parentId
        linkCmdCreateNode.click();
        protractor.utils.waitUntilElementPresent(me.inputCreateNodeType, protractor.utils.CONST_WAIT_ELEMENT);
        expect(me.inputCreateNodeType.getAttribute('id')).toEqual('inputCreateNodeType');

        // select Aufgabe
        expect(me.inputNameSymLinkNode.getAttribute('id')).toEqual('inputNameSymLinkNode');
        
        // set symlinkdata and submit form
        var nodeName = 'testsymlink' + new Date().getTime();
        me.inputNameSymLinkNode.clear().then(function () {
            me.inputNameSymLinkNode.sendKeys(nodeName);
        });
        
        // toggle desc
        me.filterDescSymLinkForm_Off.click();
        protractor.utils.waitUntilElementPresent(me.inputNodeDescSymLinkNode, protractor.utils.CONST_WAIT_ELEMENT);
        expect(me.inputNodeDescSymLinkNode.getAttribute('id')).toEqual('inputNodeDescSymLinkNode');
        // me.inputNodeDescSymLinkNode.sendKeys("fehlerhafte Tetsdaten");
        
        // define SearchElement
        var eleNewSymLinkName = element(by.cssContainingText('span.fancytree-title2', nodeName));

        // submit form
        protractor.utils.waitUntilElementPresent(me.buttonSaveSymLink, protractor.utils.CONST_WAIT_ELEMENT);
        expect(me.buttonSaveSymLink.isDisplayed()).toEqual(true);
        me.buttonSaveSymLink.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;
    
            // wait till parent- data is loaded
            protractor.utils.waitUntilElementPresent(linkCmdCreateNode, protractor.utils.CONST_WAIT_NODEHIRARCHY);
            expect(linkCmdCreateNode.getAttribute('id')).toEqual('cmdCreateSymLink' + parentId);
            
            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(eleNewSymLinkName, protractor.utils.CONST_WAIT_ELEMENT);
            expect(eleNewSymLinkName.getText()).toEqual(nodeName);
            browser.ignoreSynchronization = false;
        });
        
        return eleNewSymLinkName;
    };
    
    /**
     * follow SymLinkNode and wait until parent present
     * @param   {Integer} nodeId    nodeId of the symlinknode to follow
     * @param   {String}  parentId  id of the parentNode
     * @returns {Element}           element-filter on the symLink
     */
    me.followSymLinkNodeById = function (nodeId, parentId) {
        // load base for node
        var linkCmdCreateNodeForMe = $('#cmdCreateSymLink' + nodeId);
        protractor.utils.waitUntilElementClickable(linkCmdCreateNodeForMe, protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect(linkCmdCreateNodeForMe.getAttribute('id')).toEqual('cmdCreateSymLink' + nodeId);
        
        // load finder for parent
        var linkCmdCreateNodeForParent = $('#cmdCreateSymLink' + parentId);
        
        // find symlink on nodeline
        var symLink = linkCmdCreateNodeForMe.element(by.xpath("../../td[2]/div/div[1]/a"));
        protractor.utils.waitUntilElementPresent(symLink, protractor.utils.CONST_WAIT_ELEMENT);
        expect(symLink.isDisplayed()).toEqual(true);
        // click symlink
        symLink.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;
    
            // wait till parent- data is loaded
            protractor.utils.waitUntilElementPresent(linkCmdCreateNodeForParent, protractor.utils.CONST_WAIT_NODEHIRARCHY);
            expect(linkCmdCreateNodeForParent.getAttribute('id')).toEqual('cmdCreateSymLink' + parentId);
            
            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(linkCmdCreateNodeForMe, protractor.utils.CONST_WAIT_ELEMENT);
            expect(linkCmdCreateNodeForMe.getAttribute('id')).toEqual('cmdCreateSymLink' + nodeId);
            
            // expect new Url
            expect(browser.getLocationAbsUrl()).toContain('activate/' + parentId);

            browser.ignoreSynchronization = false;
//        }).then(null, function (err) {
//            console.error("an error occured:", err);
//            expect(err).toBe(false);
        });
        

        return linkCmdCreateNodeForMe;
    };

    /**
     * extract NnodeId from TaskNameElement
     * @param   {Element} eleTaskName       tasknameElement
     * @returns {Promise}                   promise to get nodeId 
     */
    me.extractNodeIdFromNodeNameElement = function (eleTaskName) {
        protractor.utils.waitUntilElementPresent(eleTaskName, protractor.utils.CONST_WAIT_ELEMENT);
        return eleTaskName.getAttribute('id').then(function (titleId) {
            return titleId.replace(/title/, '');
        });
    };
    
    /**
     * navigate to TaskNode by id, call delete and wait until new site present without nodeId
     * @param   {Integer} nodeId  nodeId of the node to delete
     * @param   {Integer} checkId nodeId of the node to check
     * @returns {Element}         element-filter on the cmdRemode
     */
    me.deleteNodeById = function (nodeId, checkId) {
        expect(nodeId).toMatch(/DT.*/);

        var linkCmdRemoveNode = $('#cmdRemove' + nodeId);
        protractor.utils.waitUntilElementClickable(linkCmdRemoveNode, protractor.utils.CONST_WAIT_ELEMENT);
        expect(linkCmdRemoveNode.isDisplayed()).toEqual(true);
        
        linkCmdRemoveNode.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;
            
            var alertDialog = browser.switchTo().alert();
            alertDialog.accept();
    
            // wait till data is loaded
            var checkElement = $('#cmdCreate' + checkId);
            protractor.utils.waitUntilElementClickable(checkElement, protractor.utils.CONST_WAIT_NODEHIRARCHY);
            expect(me.linkCreateChildJsFuncTest1.getAttribute('id')).toEqual('cmdCreate' + checkId);
            
            // wait till data is loaded
            protractor.utils.waitThatElementIsNotPresent(linkCmdRemoveNode, protractor.utils.CONST_WAIT_ELEMENT);
            expect(linkCmdRemoveNode.isPresent()).toEqual(false);
            browser.ignoreSynchronization = false;
        });
        
        return linkCmdRemoveNode;
    };
};
module.exports = YAIONodePage;
