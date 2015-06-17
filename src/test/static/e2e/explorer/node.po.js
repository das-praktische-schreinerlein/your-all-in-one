/**
 * defines elements of the NodePage of YAIO
 */
'use strict';
var YAIOFrontPage = require('../frontpage/frontpage.po.js');
var fs = require('fs');

var YAIONodePage = function() {
    var yaioFrontPage = new YAIOFrontPage();
    var me = this;

    // nodeIds
    me.sysplayId = 'SysPlay1';
    me.systestId = "SysTest1";
    me.jsLayoutTestId = "DT2015061620443946714";
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
     * focus on a node
     * @param   {String}  nodeId  id of the node to focus
     * @returns {Promise}         browser.wait for element.clicl()
     */
    me.focusOnNode = function (nodeId) {
        // load base for node
        var linkCmdCreateNodeForMe = $('#cmdCreateSymLink' + nodeId);
        protractor.utils.waitUntilElementClickable(linkCmdCreateNodeForMe, protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect(linkCmdCreateNodeForMe.getAttribute('id')).toEqual('cmdCreateSymLink' + nodeId);
        
        // find symlink on nodeline
        var focusMe = linkCmdCreateNodeForMe.element(by.xpath("../../td[1]/span/span[2]/a"));
        protractor.utils.waitUntilElementPresent(focusMe, protractor.utils.CONST_WAIT_ELEMENT);
        expect(focusMe.isDisplayed()).toEqual(true);
        // click symlink
        focusMe.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;
    
            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(linkCmdCreateNodeForMe, protractor.utils.CONST_WAIT_ELEMENT);
            expect(linkCmdCreateNodeForMe.getAttribute('id')).toEqual('cmdCreateSymLink' + nodeId);
            
            // expect new Url
            expect(browser.getLocationAbsUrl()).toContain('show/' + nodeId);

//        }).then(null, function (err) {
//            console.error("an error occured:", err);
//            expect(err).toBe(false);
        });
        

        return linkCmdCreateNodeForMe;
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
        expect(nodeId).toBeDefined();

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
     * navigate to Node by id, call delete and wait until new site present without nodeId
     * @param   {Integer} nodeId  nodeId of the node to delete
     * @param   {Integer} checkId nodeId of the node to check
     * @returns {Element}         element-filter on the cmdRemode
     */
    me.deleteNodeById = function (nodeId, checkId) {
        expect(nodeId).toBeDefined();

        var linkCmdRemoveNode = $('#cmdRemove' + nodeId);
        protractor.utils.waitUntilElementClickable(linkCmdRemoveNode, protractor.utils.CONST_WAIT_ELEMENT);
        expect(linkCmdRemoveNode.isDisplayed()).toEqual(true);
        
        linkCmdRemoveNode.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;
            
            // accept delete
            var alertDialog = browser.switchTo().alert();
            expect(alertDialog.accept()).toBe(null);
    
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

    /**
     * navigate to Node by id, open container with link, call checkHandler and close container
     * @param   {Integer}  nodeId             nodeId of the node to show
     * @param   {Function} checkHandler       handler which is called after opening the container (should return a promise)
     * @param   {String}   containerIdPraefix prefix of the container-element
     * @param   {String}   linkIdPraefix      prefix of the container-toggler
     * @returns {Element}                     element-filter on the containerDesc
     */
    me.showContainerForNode = function (nodeId, checkHandler, containerIdPraefix, linkIdPraefix) {
        // check id
        expect(nodeId).toBeDefined();
        
        browser.ignoreSynchronization = true;

        // define container
        var container = $(containerIdPraefix + nodeId);

        // open container
        var linkCmdShowContainer = $(linkIdPraefix + nodeId);
        protractor.utils.waitUntilElementClickable(linkCmdShowContainer, protractor.utils.CONST_WAIT_ELEMENT);
        expect(linkCmdShowContainer.isDisplayed()).toEqual(true);
        linkCmdShowContainer.click().then(function doneOpenContainer() {
            // wait till container is loaded
            protractor.utils.waitUntilElementVisible(container, protractor.utils.CONST_WAIT_ELEMENT);
            expect(container.isDisplayed()).toEqual(true);
            
            // define CloseHandler
            var closeContainer = function () {
                // close Desc
                linkCmdShowContainer.click().then(function () {
                    // wait till data is loaded and container hidden
                    protractor.utils.waitThatElementIsNotPresent(container, protractor.utils.CONST_WAIT_ELEMENT);
                    expect(container.isDisplayed()).toEqual(false);
                });
            }
            
            if (checkHandler != "undefined") {
                checkHandler(container).then( function doneCheckHandler() {
                    // run CloseHandler
                    closeContainer()
                });
            } else  {
                // run CloseHandler
                closeContainer()
            }
        });

        browser.ignoreSynchronization = false;
        
        return container;
    };

    /**
     * navigate to Node by id, call showDesc open/close
     * @param   {Integer}  nodeId        nodeId of the node to show
     * @param   {Function} checkHandler  handler which is called after opening of the desc (should return a promise)
     * @returns {Element}                element-filter on the containerDesc
     */
    me.showDescForNode = function (nodeId, checkHandler) {
        return me.showContainerForNode(nodeId, checkHandler, '#container_content_desc_', '#toggler_desc_');
    };

    /**
     * navigate to Node by id, call showMetaData open/close
     * @param   {Integer}  nodeId        nodeId of the node to show
     * @param   {Function} checkHandler  handler which is called after opening of the desc (should return a promise)
     * @returns {Element}                element-filter on the containerDesc
     */
    me.showSysForNode = function (nodeId, checkHandler) {
        return me.showContainerForNode(nodeId, checkHandler, '#detail_sys_', '#toggler_sys_');
    };
    

    /**
     * click export-link for node
     * @returns {JQuery}                 export-link as JQuery-Selector
     * @returns {Promise}                promise on the exportLink-click
     */
    me.clickButtonExportNodeToClipboard = function (linkExportCommand) {
        protractor.utils.waitUntilElementClickable(linkExportCommand, protractor.utils.CONST_WAIT_ELEMENT);
        expect(linkExportCommand.isDisplayed()).toEqual(true);
        return linkExportCommand.click();
    };

    /**
     * click text-export-link for node
     * @param   {Integer}  nodeId        nodeId of the node to show
     * @returns {Promise}                promise on the exportLink-click
     */
    me.clickButtonExportNodeToClipboardAsText = function (nodeId) {
        var linkExportCommand = $("div#commands_desc_" + nodeId + " a.button.command-desc-txtexport");
        return me.clickButtonExportNodeToClipboard(linkExportCommand);
    };
    
    /**
     * click jira-export-link for node
     * @param   {Integer}  nodeId        nodeId of the node to show
     * @returns {Promise}                promise on the exportLink-click
     */
    me.clickButtonExportNodeToClipboardAsJira = function (nodeId) {
        var linkExportCommand = $("div#commands_desc_" + nodeId + " a.button.command-desc-jiraexport");
        return me.clickButtonExportNodeToClipboard(linkExportCommand);
    };
    
    /**
     * returns a handler(descContainer) which will check the nodedesc, click all export-links and check the exports (text, jira)
     * @param   {Integer}  nodeId        nodeId of the node to check
     * @param   {String}   expectedDesc  expected nodeDesc in descContainer
     * @param   {String}   expectedText  expected nodeDesc in clipboard after click on TextExport
     * @param   {String}   expectedJira  expected nodeDesc in clipboard after click on JiraExport
     * @returns {Function}               function: handler(descContainer) return promise
     */
    me.createHandlerToCheckNodeExports = function (nodeId, expectedDesc, expectedText, expectedJira) {
        // define checkHandler
        var checkClipboardHandlerText = function (clipboard) {
            // check text content
            expect(clipboard.getText()).toContain(expectedText);
            return clipboard.getText();
        };
        var checkClipboardHandlerJira = function (clipboard) {
            // check jira content
            expect(clipboard.getText()).toContain(expectedJira);
            return clipboard.getText();
        };
        var contentHandler = function (descContainer) {
            // check desc content
            expect(descContainer.getInnerHtml()).toContain(expectedDesc);

            // export to text clipboard
            var contentActions = me.clickButtonExportNodeToClipboardAsText(nodeId)
            .then(function doneCallTextExport() {
                // check text-clipboard
                var clipboardElement = me.checkAndCloseClipboard(checkClipboardHandlerText);
                return clipboardElement.getText();
            })
            .then(function doneCloseClipboard() {
                // export to jira clipboard
                return me.clickButtonExportNodeToClipboardAsJira(nodeId);
            })
            .then(function doneCallJiraExport() {
                // check jira-clipboard
                var clipboardElement = me.checkAndCloseClipboard(checkClipboardHandlerJira);
                return clipboardElement.getText();
            });
            
            return contentActions;
        };
        
        return contentHandler;
    }

    /**
     * check the clipboard-content and close the clipboard
     * @param   {Function} checkHandler  handler which is called to check the clipboard (should return a promise) before the closebutton is cliked
     * @returns {Element}                element-filter on the clipboardContent
     */
    me.checkAndCloseClipboard = function(checkHandler) {
        browser.ignoreSynchronization = true;

        // define container
        var clipboardContent = $('#clipboard-content');
        protractor.utils.waitUntilElementVisible(clipboardContent, protractor.utils.CONST_WAIT_ELEMENT);
        expect(clipboardContent.isDisplayed()).toEqual(true);
        
        // define CloseHandler
        var linkCmdCloseClipboard = $('div.ui-dialog div.ui-dialog-buttonset button.ui-button');
        protractor.utils.waitUntilElementClickable(linkCmdCloseClipboard, protractor.utils.CONST_WAIT_ELEMENT);
        expect(linkCmdCloseClipboard.isDisplayed()).toEqual(true);
        var closeContainer = function () {
            // close Desc
            linkCmdCloseClipboard.click().then(function () {
                // wait till data is loaded and container hidden
                protractor.utils.waitThatElementIsNotPresent(clipboardContent, protractor.utils.CONST_WAIT_ELEMENT);
                expect(clipboardContent.isDisplayed()).toEqual(false);
            });
        }
        
        if (checkHandler != "undefined") {
            // run checkHandler
            checkHandler(clipboardContent).then( function doneCheckHandler() {
                // run CloseHandler
                closeContainer()
            });
        } else  {
            // run CloseHandler
            closeContainer()
        }

        browser.ignoreSynchronization = false;
        
        return clipboardContent;
    }


    /**
     * open Export-Menu, click export-link, check result and close export-menu
     * @returns {JQuery} linkExportCommand   export-link as JQuery-Selector
     * @param   {Function} checkHandler      handler which is called to check the result (should return a promise)
     * @returns {Promise}                    promise on the linkExportMenu-click
     */
    me.clickButtonExportAndCheck = function (linkExportCommand, checkHandler) {
        // look for element and click
        var linkExportMenu = $('[translate="common.command.Export"]');
        protractor.utils.waitUntilElementClickable(linkExportMenu, protractor.utils.CONST_WAIT_ELEMENT);
        expect(linkExportMenu.isDisplayed()).toEqual(true);

        // open menu
        return linkExportMenu.click()
        .then(function doneMenuCommand() {
            // click export-button for new window
            browser.ignoreSynchronization = true;
            protractor.utils.waitUntilElementClickable(linkExportCommand, protractor.utils.CONST_WAIT_ELEMENT);
            expect(linkExportCommand.isDisplayed()).toEqual(true);
            return linkExportCommand.click()
        })
        .then(function doneLinkCommand() {
            // call checkHandler
            return checkHandler();
        })
        .then(function doneCheckHandler() {
            // close export-menu
            return linkExportMenu.click();
        });
    };

    /**
     * open Export-Menu, click export-link, check result in new window and close export-menu
     * @returns {JQuery} linkExportCommand   export-link as JQuery-Selector
     * @param   {Function} checkHandler      handler which is called to check the result (should return a promise)
     * @returns {Promise}                    promise on the linkExportMenu-click
     */
    me.clickButtonExportAndCheckInNewWindow = function (linkExportCommand, checkHandler) {
        var newWindowCheckHandler = function () {
            var myHandles = [];
            // get windows
            return browser.getAllWindowHandles()
            .then(function doneGetWindowHandles(handles) {
                // switch to the popup
                myHandles = handles;
                return browser.switchTo().window(myHandles[1]);
            }).then(function doneSwitchWindow() {
                // call checkHandler
                return checkHandler();
            }).then(function doneSwitchWindow() {
                // close and go back to the main window
                browser.driver.close();
                return browser.switchTo().window(myHandles[0]);
            })
        }
        return me.clickButtonExportAndCheck(linkExportCommand, newWindowCheckHandler);
    };
    
    /**
     * export node as html-Documentation-DirectLink and check against expected text
     * @param   {String} expectedText  export must contain this text
     * @returns {Promise}              promise on the linkExportMenu-click
     */
    me.clickShortlinkExportAsHtmlDocumentation = function (expectedText) {
        // define export-button
        var linkExportCommand = $('[translate="common.command.ExportHtmlDocumentationDirect"]');
        
        // define checkhandler
        var checkHandler = function () {
            protractor.utils.waitUntilElementPresent($("#div_full"), protractor.utils.CONST_WAIT_NODEHIRARCHY);
            expect($("#div_full").getInnerHtml()).toContain(expectedText);
        };
        // call exporter
        var newWindow = me.clickButtonExportAndCheckInNewWindow(linkExportCommand, checkHandler);
        return newWindow;
    };

    /**
     * export node by button, download and check the filecontent against expected text
     * @returns {JQuery} linkExportCommand   export-link as JQuery-Selector
     * @param   {String} filename            static name of the exportfile
     * @param   {String} expectedText        export must contain this text
     * @returns {Promise}                    promise on the linkExportMenu-click
     */
    me.clickButtonExportAndCheckFileDownload = function (linkExportCommand, fileName, expectedText) {
        // delete file
        var filePath = browser.params.downloadPath + fileName;
        if (fs.existsSync(filePath)) {
            // Make sure the browser doesn't have to rename the download.
            fs.unlinkSync(filePath);
        }

        // define checkHandler
        var downloadCheckHandler = function () {
            return browser.driver.wait(function() {
                // Wait until the file has been downloaded.
                return fs.existsSync(filePath);
            }, 2000)
            .then(function() {
                // check that file contains text
                expect(fs.readFileSync(filePath, { encoding: 'utf8' })).toContain(expectedText);
            });
        };
        
        return me.clickButtonExportAndCheck(linkExportCommand, downloadCheckHandler);
    };

    /**
     * export node as ICal-DirectLink and check against expected text
     * @param   {String} expectedText  export must contain this text
     * @returns {Promise}              promise on the linkExportMenu-click
     */
    me.clickShortlinkExportAsICal = function (expectedText) {
        // define export-button
        var linkExportCommand = $('[translate="common.command.ExportICalDirect"]');
        return me.clickButtonExportAndCheckFileDownload(linkExportCommand, "converted.ics", expectedText);
    };

    /**
     * export node as Mindmap-DirectLink and check against expected text
     * @param   {String} expectedText  export must contain this text
     * @returns {Promise}              promise on the linkExportMenu-click
     */
    me.clickShortlinkExportAsMindmap = function (expectedText) {
        // define export-button
        var linkExportCommand = $('[translate="common.command.ExportMindmapDirect"]');
        return me.clickButtonExportAndCheckFileDownload(linkExportCommand, "converted.mm", expectedText);
    };
};
module.exports = YAIONodePage;
