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
    me.systestId = 'SysTest1';
    me.jsLayoutTestId = 'DT2015061620443946714';
    me.jsFuncTestId = 'JsFuncTest1';
    me.jsFuncTestHierarchy = ['SysPlay1', 'SysTest1', 'JsFuncTest1'];
    
    // explorer-link
    me.linkFrontpage = '[translate="common.command.OpenFrontpage"]';
    me.baseExplorerUrl = browser.params.yaioConfig.yaioBaseAppUrl + '/show/' + 'MasterplanMasternode1';
    
    // mastercontainer
    me.containerMasterdata = '#masterdata';
    me.expanderSysPlay1 = '#expander' + me.sysplayId;
    me.linkCreateChildJsFuncTest1 = '#cmdCreate' + me.jsFuncTestId;
    
    // gui-elements
    me.buttonOpenTilllevel = 'a.command_switchlevel';
    me.inputOpenTilllevel = '#treeOpenLevel';
    me.spanNodeName = 'span.fancytree-title2';
    
    // create form
    me.inputCreateNodeType = '#inputCreateNodeType';

    // taskform
    me.inputNameTaskNode = '#inputNameTaskNode';
    me.inputTypeTaskNode = '#inputTypeTaskNode';
    me.inputPlanAufwandTaskNode = '#inputPlanAufwandTaskNode';
    me.inputPlanStartTaskNode = '#inputPlanStartTaskNode';
    me.inputPlanEndeTaskNode = '#inputPlanEndeTaskNode';
    me.openWysiwyg4inputNodeDescTaskNode = '#openWysiwyg4inputNodeDescTaskNode';
    me.buttonSaveTask = '#nodeFormTaskNode fieldset button[translate="common.buttonSave"]';
    
    // symlinkform
    me.inputNameSymLinkNode = '#inputNameSymLinkNode';
    me.buttonSaveSymLink = '#nodeFormSymLinkNode fieldset button[translate="common.buttonSave"]';

    // infoform
    me.inputNameInfoNode = '#inputNameInfoNode';
    me.inputTypeInfoNode = '#inputTypeInfoNode';
    me.editorInputNodeDescInfoNode = '#editorInputNodeDescInfoNode';

    // urlresform
    me.inputNameUrlResNode = '#inputNameUrlResNode';
    me.inputTypeUrlResNode = '#inputTypeUrlResNode';
    me.inputResLocRefUrlResNode = '#inputResLocRefUrlResNode';
    me.inputResLocNameUrlResNode = '#inputResLocNameUrlResNode';
    me.inputUploadFileUrlResNode = '#inputUploadFileUrlResNode';
    me.inputResContentDMSStateUrlResNode = '#inputResContentDMSStateUrlResNode';
    me.inputResIndexDMSStateUrlResNode = '#inputResIndexDMSStateUrlResNode';
    me.editorInputNodeDescUrlResNode = '#editorInputNodeDescUrlResNode';
    me.buttonSaveUrlRes = '#nodeFormUrlResNode fieldset button[translate="common.buttonSave"]';

    // utils
    me.idUiDatePicker = '#ui-datepicker-div';
    me.uiDatePickerDay1 = $$(me.idUiDatePicker + ' table.ui-datepicker-calendar tbody tr td a').get(1);
    me.uiDatePickerDay25 = $$(me.idUiDatePicker + ' table.ui-datepicker-calendar tbody tr td a').get(25);
    
    /**
     * open masterpage
     * @returns {Browser}  - browser element
     */
    me.openMasternode = function () {
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/show/MasterplanMasternode1');
        return browser;
    };

    /**
     * open explorer from frontpage and wait until present
     * @returns {Promise}
     */
    me.openExplorerFromFrontPage = function () {
        // expect frontPage
        protractor.utils.waitUntilElementPresent($(yaioFrontPage.fontContentLeft), protractor.utils.CONST_WAIT_ELEMENT);
        
        // open explorer-link
        $(yaioFrontPage.linkExplorer).click();
        
        // expect SysPlay1
        return protractor.utils.waitUntilElementClickable($(me.expanderSysPlay1), protractor.utils.CONST_WAIT_ELEMENT);

    };

    /**
     * open node-hierarchy and wait until present
     * @param {Array} nodeIdHirarchy       array of nodeids to expand recursively
     * @returns {Promise}
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
     * @returns {Promise}
     */
    me.expandNode = function (id, last) {
        // console.log('expandNode: check for ' + '#expander' + id);
        var expander = $('#expander' + id);
        var createChild = $('#cmdCreate' + id);
        var deferred = protractor.promise.defer();
        var promise = deferred.promise;
        if (last) {
            // last element: check only for link
            protractor.utils.waitUntilElementClickable(createChild, protractor.utils.CONST_WAIT_NODEHIRARCHY).then (function () {
                deferred.fulfill(expander);
            });
        } else {
            // expand and check
            protractor.utils.waitUntilElementClickable(expander, protractor.utils.CONST_WAIT_NODEHIRARCHY).then( function expanderClickable() {
                // clickable 
                expander.click().then(function okCallBack() {
                    // click passed
                    deferred.fulfill(expander);
                });
            });
            
        }

        return promise;
    };

    /**
     * focus on a node
     * @param   {String}  nodeId  id of the node to focus
     * @returns {Promise}
     */
    me.focusOnNode = function (nodeId) {
        // load base for node
        var linkCmdCreateNodeForMe = $('#cmdCreateSymLink' + nodeId);
        protractor.utils.waitUntilElementClickable(linkCmdCreateNodeForMe, protractor.utils.CONST_WAIT_NODEHIRARCHY);
        
        // find symlink on nodeline
        var focusMe = linkCmdCreateNodeForMe.element(by.xpath('../../td[1]/span/span[2]/a'));
        protractor.utils.waitUntilElementPresent(focusMe, protractor.utils.CONST_WAIT_ELEMENT);
        expect(focusMe.isDisplayed()).toEqual(true);
        // click symlink
        focusMe.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;
    
            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(linkCmdCreateNodeForMe, protractor.utils.CONST_WAIT_ELEMENT);
            
            // expect new Url
            expect(browser.getLocationAbsUrl()).toContain('show/' + nodeId);
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

        // create child for parentId
        linkCmdCreateNode.click();
        protractor.utils.waitUntilElementPresent($(me.inputCreateNodeType), protractor.utils.CONST_WAIT_ELEMENT);

        // select Aufgabe
        $(me.inputCreateNodeType).sendKeys('Aufgabe\n');
        
        // set taskdata and submit form
        var taskName = 'testask' + new Date().getTime();
        $(me.inputNameTaskNode).sendKeys(taskName);
        $(me.inputTypeTaskNode).sendKeys('+-- Offen\n');
        $(me.inputPlanAufwandTaskNode).sendKeys('1');
        $(me.inputPlanStartTaskNode).click();
        protractor.utils.waitUntilElementPresent(me.uiDatePickerDay1, protractor.utils.CONST_WAIT_ELEMENT);
        me.uiDatePickerDay1.click();
        $(me.inputPlanEndeTaskNode).click();
        protractor.utils.waitUntilElementPresent(me.uiDatePickerDay25, protractor.utils.CONST_WAIT_ELEMENT);
        me.uiDatePickerDay25.click();
        
        // toggle and set desc
        me.openAndEditDescForNodeType('Task', 'fehlerhafte Task-Desc-Testdaten', true);
        
        // define SearchElement
        var eleNewTaskName = element(by.cssContainingText(me.spanNodeName, taskName));

        // submit form
        var buttonSaveTask = $(me.buttonSaveTask);
        protractor.utils.waitUntilElementPresent(buttonSaveTask, protractor.utils.CONST_WAIT_ELEMENT);
        expect(buttonSaveTask.isDisplayed()).toEqual(true);
        buttonSaveTask.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;
    
            // wait till parent- data is loaded
            protractor.utils.waitUntilElementPresent(linkCmdCreateNode, protractor.utils.CONST_WAIT_NODEHIRARCHY);
            
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

        if (browser.browserName === 'phantomjs') {
            //phantomjs breaks while nodeedit, so we return the current taskname-element 
            return $('#title' + nodeId);
        }
        
        // define SearchElement
        var taskName = 'correct testask' + new Date().getTime();
        var eleNewTaskName = element(by.cssContainingText(me.spanNodeName, taskName));

        linkCmdEditNode.click().then(function () {
            // set new taskdata and submit form
            var inputNameTaskNode = $(me.inputNameTaskNode);
            protractor.utils.waitUntilElementPresent(inputNameTaskNode, protractor.utils.CONST_WAIT_ELEMENT);
            inputNameTaskNode.clear().then(function () {
                inputNameTaskNode.sendKeys(taskName);
            });
            var inputPlanAufwandTaskNode = $(me.inputPlanAufwandTaskNode);
            inputPlanAufwandTaskNode.clear().then(function () {
                inputPlanAufwandTaskNode.sendKeys('10');
            });
            $(me.inputPlanStartTaskNode).click();
            protractor.utils.waitUntilElementPresent(me.uiDatePickerDay1, protractor.utils.CONST_WAIT_ELEMENT);
            me.uiDatePickerDay1.click();
            $(me.inputPlanEndeTaskNode).click();
            protractor.utils.waitUntilElementPresent(me.uiDatePickerDay25, protractor.utils.CONST_WAIT_ELEMENT);
            me.uiDatePickerDay25.click();
            
            // reset desc
            me.openAndEditDescForNodeType('Task', ' - berichtigt');

            // submit form
            var buttonSaveTask = $(me.buttonSaveTask);
            protractor.utils.waitUntilElementPresent(buttonSaveTask, protractor.utils.CONST_WAIT_ELEMENT);
            expect(buttonSaveTask.isDisplayed()).toEqual(true);
            buttonSaveTask.click().then(function () {
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
        protractor.utils.waitUntilElementPresent($(me.inputCreateNodeType), protractor.utils.CONST_WAIT_ELEMENT);

        // set symlinkdata
        var nodeName = 'testsymlink' + new Date().getTime();
        var inputNameSymLinkNode = $(me.inputNameSymLinkNode);
        inputNameSymLinkNode.clear().then(function () {
            inputNameSymLinkNode.sendKeys(nodeName);
        });
        
        // set desc
        me.openAndEditDescForNodeType('SymLink', 'korrekte SymLink-Desc-Testdaten', true);
        
        // define SearchElement
        var eleNewSymLinkName = element(by.cssContainingText(me.spanNodeName, nodeName));

        // submit form
        var buttonSaveSymLink = $(me.buttonSaveSymLink);
        protractor.utils.waitUntilElementPresent(buttonSaveSymLink, protractor.utils.CONST_WAIT_ELEMENT);
        expect(buttonSaveSymLink.isDisplayed()).toEqual(true);
        buttonSaveSymLink.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;
    
            // wait till parent- data is loaded
            protractor.utils.waitUntilElementPresent(linkCmdCreateNode, protractor.utils.CONST_WAIT_NODEHIRARCHY);
            
            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(eleNewSymLinkName, protractor.utils.CONST_WAIT_ELEMENT);
            expect(eleNewSymLinkName.getText()).toEqual(nodeName);
            browser.ignoreSynchronization = false;
        });
        
        return eleNewSymLinkName;
    };

    /**
     * open desc-editor of forn and set new content (if flgToggle is set toggle before to open layer)
     * @param   {String} nodeType    type of the node Task, SymLink...
     * @param   {String} newText     new text to set
     * @param   {Boolean} flgToggle  do open the layer
     * @returns {Promise}
     */
    me.openAndEditDescForNodeType = function (nodeType, newText, flgToggle) {
        var filterDescForm_Off = $('#filterDesc' + nodeType + 'Form_Off');
        var editorInputNodeDesc = $('#editorInputNodeDesc' + nodeType + 'Node');
        var inputAceElm = $('#editorInputNodeDesc' + nodeType + 'Node > textarea.ace_text-input');

        // toggle and set desc
        if (flgToggle) {
            filterDescForm_Off.click();
        }
        protractor.utils.waitUntilElementPresent(editorInputNodeDesc, protractor.utils.CONST_WAIT_ELEMENT);
        browser.actions().doubleClick(editorInputNodeDesc).perform();
        return inputAceElm.sendKeys(newText);
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
        
        // load finder for parent
        var linkCmdCreateNodeForParent = $('#cmdCreateSymLink' + parentId);
        
        // find symlink on nodeline
        var symLink = linkCmdCreateNodeForMe.element(by.xpath('../../td[2]/div/div[1]/a'));
        protractor.utils.waitUntilElementPresent(symLink, protractor.utils.CONST_WAIT_ELEMENT);
        expect(symLink.isDisplayed()).toEqual(true);
        // click symlink
        symLink.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;
    
            // wait till parent- data is loaded
            protractor.utils.waitUntilElementPresent(linkCmdCreateNodeForParent, protractor.utils.CONST_WAIT_NODEHIRARCHY);
            
            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(linkCmdCreateNodeForMe, protractor.utils.CONST_WAIT_ELEMENT);
            
            // expect new Url
            expect(browser.getLocationAbsUrl()).toContain('activate/' + parentId);

            browser.ignoreSynchronization = false;
        });
        

        return linkCmdCreateNodeForMe;
    };

    /**
     * create UrlResNode and wait until present
     * @param   {String}  parentId  id of the parentNode
     * @returns {Element}           element-filter on the newUrlResname
     */
    me.openNodeEditorAndCreateUrlResNode = function (parentId) {
        var linkCmdCreateNode = $('#cmdCreate' + parentId);
        protractor.utils.waitUntilElementClickable(linkCmdCreateNode, protractor.utils.CONST_WAIT_NODEHIRARCHY);

        // create child for parentId
        linkCmdCreateNode.click();
        protractor.utils.waitUntilElementPresent($(me.inputCreateNodeType), protractor.utils.CONST_WAIT_ELEMENT);

        // select Aufgabe
        $(me.inputCreateNodeType).sendKeys('Ressource\n');

        // set UrlResdata
        var nodeName = 'testUrlRes' + new Date().getTime();
        var inputNameUrlResNode = $(me.inputNameUrlResNode);
        inputNameUrlResNode.clear().then(function () {
            inputNameUrlResNode.sendKeys(nodeName);
        });
        var nodeUrl = 'http://bla.blub.de';
        var inputResLocRefUrlResNode = $(me.inputResLocRefUrlResNode);
        inputResLocRefUrlResNode.clear().then(function () {
            inputResLocRefUrlResNode.sendKeys(nodeUrl);
        });

        // set desc
        me.openAndEditDescForNodeType('UrlRes', 'korrekte UrlRes-Desc-Testdaten', true);

        // define SearchElement
        var eleNewUrlResName = element(by.cssContainingText(me.spanNodeName, nodeName));

        // submit form
        var buttonSaveUrlRes = $(me.buttonSaveUrlRes);
        protractor.utils.waitUntilElementPresent(buttonSaveUrlRes, protractor.utils.CONST_WAIT_ELEMENT);
        expect(buttonSaveUrlRes.isDisplayed()).toEqual(true);
        buttonSaveUrlRes.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;

            // wait till parent- data is loaded
            protractor.utils.waitUntilElementPresent(linkCmdCreateNode, protractor.utils.CONST_WAIT_NODEHIRARCHY);

            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(eleNewUrlResName, protractor.utils.CONST_WAIT_ELEMENT);
            expect(eleNewUrlResName.getText()).toEqual(nodeName);
            browser.ignoreSynchronization = false;
        });

        return eleNewUrlResName;
    };

    /**
     * extract NnodeId from TaskNameElement
     * @param   {Element} eleTaskName       tasknameElement
     * @returns {Promise}                   promise with nodeId 
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
        
        // bypassing PhantomJS 1.9.7/GhostDriver window.confirm (or alert) bug.
        // as WebDriver's switchTo().alert() is not implemented yet.
        if (browser.browserName === 'phantomjs') {
            browser.executeScript('window.alert = function(msg) {}');
            browser.executeScript('window.confirm = function(msg) {console.error("confirm: " + msg); return true;}');
        }

        linkCmdRemoveNode.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;
            
            // switch to alert only if not phantomjs
            if (browser.browserName !== 'phantomjs') {
                // accept delete
                var alertDialog = browser.switchTo().alert();
                expect(alertDialog.accept()).toBe(null);
            }
    
            // wait till data is loaded
            var checkElement = $('#cmdCreate' + checkId);
            protractor.utils.waitUntilElementClickable(checkElement, protractor.utils.CONST_WAIT_NODEHIRARCHY);
            
            // wait till data is loaded
            protractor.utils.waitThatElementIsNotPresent(linkCmdRemoveNode, protractor.utils.CONST_WAIT_ELEMENT);
            expect(linkCmdRemoveNode.isPresent()).toEqual(false);
            
            browser.ignoreSynchronization = false;
        });
        
        return linkCmdRemoveNode;
    };

    /**
     * navigate to Node by id, open container with link, call checkHandler and close container
     * @param {Int} nodeId                 nodeId of the node to show
     * @param {Function} checkHandler      handler which is called after opening the container (should return a promise)
     * @param {String} containerIdPraefix  prefix of the container-element
     * @param {String} linkIdPraefix       prefix of the container-toggler
     * @returns {Element}                  element-filter on the containerDesc
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
            };
            
            if (checkHandler !== undefined) {
                checkHandler(container).then( function doneCheckHandler() {
                    // run CloseHandler
                    closeContainer();
                });
            } else  {
                // run CloseHandler
                closeContainer();
            }
        });

        browser.ignoreSynchronization = false;
        
        return container;
    };

    /**
     * navigate to Node by id, call showDesc open/close
     * @param {Int} nodeId              nodeId of the node to show
     * @param {Function} checkHandler   handler which is called after opening of the desc (should return a promise)
     * @returns {Element}               element-filter on the containerDesc
     */
    me.showDescForNode = function (nodeId, checkHandler) {
        return me.showContainerForNode(nodeId, checkHandler, '#container_content_desc_', '#toggler_desc_');
    };

    /**
     * navigate to Node by id, call showMetaData open/close
     * @param {int} nodeId             nodeId of the node to show
     * @param {Function} checkHandler  handler which is called after opening of the desc (should return a promise)
     * @returns {Element}              element-filter on the containerDesc
     */
    me.showSysForNode = function (nodeId, checkHandler) {
        return me.showContainerForNode(nodeId, checkHandler, '#detail_sys_', '#toggler_sys_');
    };
    
    
    /**
     * returns a list of all visible treenode-spans
     * @return {[]}  visible treenode-spans
     */
    me.getVisibleNodes = function() { 
        var nodes = $$((me.spanNodeName)); 
        return protractor.promise.filter(nodes, function(node) { 
                return node.isDisplayed(); 
            }); 
    };
};
module.exports = YAIONodePage;
