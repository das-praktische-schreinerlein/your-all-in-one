/**
 * defines export-elements of the NodePage of YAIO
 */
'use strict';
var fs = require('fs');

var YAIOExporterPage = function() {
    var me = this;
    
    me.baseUrl = browser.params.yaioConfig.yaioBaseAppUrl + '/show/' + 'MasterplanMasternode1';

    me.linkSnapshot = '[translate="common.command.Snapshot"]';
    me.linkExportOverview = '[translate="common.command.ExportAsOverview"]';

    /**
     * click export-link for node
     * @param   {Element} linkExportCommand   export-link
     * @returns {Promise}
     */
    me.clickButtonExportNodeToClipboard = function (linkExportCommand) {
        protractor.utils.waitUntilElementClickable(linkExportCommand, protractor.utils.CONST_WAIT_ELEMENT);
        expect(linkExportCommand.isDisplayed()).toEqual(true);
        return linkExportCommand.click();
    };

    /**
     * click text-export-link for node
     * @param   {Integer}  nodeId        nodeId of the node to show
     * @returns {Promise}
     */
    me.clickButtonExportNodeToClipboardAsText = function (nodeId) {
        var linkExportCommand = $("div#commands_desc_" + nodeId + " a.button.command-desc-txtexport");
        return me.clickButtonExportNodeToClipboard(linkExportCommand);
    };
    
    /**
     * click jira-export-link for node
     * @param   {Integer}  nodeId        nodeId of the node to show
     * @returns {Promise}
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
            return linkCmdCloseClipboard.click().then(function () {
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
     * @param   {Element} linkExportCommand   export-link
     * @param   {Function} checkHandler      handler which is called to check the result (should return a promise)
     * @returns {Promise}
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
                if (checkHandler) {
                    return checkHandler();
                }
            })
            .then(function doneCheckHandler() {
                // close export-menu
                return linkExportMenu.click();
            });
    };

    /**
     * open Export-Menu, click export-link, check result in new window and close export-menu
     * @param   {Element} linkExportCommand   export-link
     * @param   {Function} checkHandler      handler which is called to check the result (should return a promise)
     * @returns {Promise}
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
     * @returns {Promise}
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
     * @param   {Element} linkExportCommand   export-link
     * @param   {String} filename            static name of the exportfile
     * @param   {String} expectedText        export must contain this text
     * @returns {Promise}
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
        
        if (browser.browserName === "phantomjs" || browser.browserName === "iexplorer" || browser.browserName === "firefox") {
            downloadCheckHandler = null;
        }
        
        return me.clickButtonExportAndCheck(linkExportCommand, downloadCheckHandler);
    };

    /**
     * export node as ICal-DirectLink and check against expected text
     * @param   {String} expectedText  export must contain this text
     * @returns {Promise}
     */
    me.clickShortlinkExportAsICal = function (expectedText) {
        // define export-button
        var linkExportCommand = $('[translate="common.command.ExportICalDirect"]');
        return me.clickButtonExportAndCheckFileDownload(linkExportCommand, "converted.ics", expectedText);
    };

    /**
     * export node as Mindmap-DirectLink and check against expected text
     * @param   {String} expectedText  export must contain this text
     * @returns {Promise}
     */
    me.clickShortlinkExportAsMindmap = function (expectedText) {
        // define export-button
        var linkExportCommand = $('[translate="common.command.ExportMindmapDirect"]');
        return me.clickButtonExportAndCheckFileDownload(linkExportCommand, "converted.mm", expectedText);
    };
};
module.exports = YAIOExporterPage;
