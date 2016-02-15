/**
 * defines import-elements of the NodePage of YAIO
 */
'use strict';

var path = require('path');

var YAIOImporterPage = function() {
    var me = this;
    
    me.linkImportMenu = '[translate="common.command.Import"]';
    me.linkImport = '[translate="common.command.ImportWiki"]';
    me.inputImportFile = '#inputfileImort';
    me.buttonSend = 'form#nodeFormImport button:first-of-type';
    
    /**
     * click import-link, fill upload-form with filename, upload and check result
     * @param {Element} linkImportCommand   import-link
     * @param {String}  fileToUpload       filename to import (relative to current dir)
     * @param {Function} checkHandler      handler which is called to check the result (should return a promise)
     * @returns {Promise}                    promise
     */
    me.clickButtonImportAndCheck = function (linkImportCommand, fileToUpload, checkHandler) {
        var linkImportMenu = $(me.linkImportMenu);
        protractor.utils.waitUntilElementClickable(linkImportMenu, protractor.utils.CONST_WAIT_ELEMENT);
        expect(linkImportMenu.isDisplayed()).toEqual(true);

        // click import-button for new window
        return linkImportMenu.click()
            .then(function doneMenuCommand() {
                // look for element and click
                protractor.utils.waitUntilElementClickable(linkImportCommand, protractor.utils.CONST_WAIT_ELEMENT);
                expect(linkImportCommand.isDisplayed()).toEqual(true);
                // open upload
                return linkImportCommand.click();
            })
            .then(function doneLinkCommand() {
                // fill upload-form and submit
                var absolutePath = path.resolve(__dirname, fileToUpload);
                $(me.inputImportFile).sendKeys(absolutePath);
                return $(me.buttonSend).click();
            })
            .then(function checkData() {
                // call checkHandler
                if (checkHandler) {
                    return checkHandler();
                }
            });
    };

    /**
     * click import-link, fill upload-form with filename, upload and check result for importwindow and mainwindow
     * @param {Element} linkImportCommand   import-link
     * @param {String}  fileToUpload       filename to import (relative to current dir)
     * @param {Function} checkHandlerImportWindow    handler which is called to check ImportWindow (should return a promise)
     * @param {Function} checkHandlerMainWindow      handler which is called to check MainWindow (should return a promise)
     * @returns {JQueryPromise}                    promise
     */
    me.importFileAndCheckForNewTask = function (linkImportCommand, fileToUpload, checkHandlerImportWindow, checkHandlerMainWindow) {
        var newWindowCheckHandler = function () {
            var myHandles = [];
            // get windows
            return browser.getAllWindowHandles()
                .then(function doneGetWindowHandles(handles) {
                    // switch to the popup
                    myHandles = handles;
                    return browser.switchTo().window(myHandles[1]);
                }).then(function doneSwitchWindow() {
                    // call checkHandler for importwindow
                    return checkHandlerImportWindow();
                }).then(function doneSwitchWindow() {
                    // close and go back to the main window
                    browser.driver.close();
                    return browser.switchTo().window(myHandles[0]);
                }).then(function doneSwitchWindow() {
                    // call checkHandler for mainwindow
                    browser.ignoreSynchronization = true;
                    return browser.refresh();
                }).then(function doneReferesh() {
                    browser.ignoreSynchronization = false;
                    return checkHandlerMainWindow();
                });
        };
        return me.clickButtonImportAndCheck(linkImportCommand, fileToUpload, newWindowCheckHandler);
    };
};
module.exports = YAIOImporterPage;
