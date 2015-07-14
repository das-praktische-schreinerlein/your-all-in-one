/**
 * defines import-elements of the NodePage of YAIO
 */
'use strict';

var path = require('path');

var YAIOImporterPage = function() {
    var me = this;
    
    me.linkImport = '[translate="common.command.Import"]';
    me.inputImportFile = '#inputfileImort';
    me.buttonSend = 'form#nodeFormImport button:first-of-type';
    
    /**
     * click import-link, fill upload-form with filename, upload and check result
     * @returns {Element} linkImportCommand   import-link
     * @returns {String}  fileToUpload       filename to import (relative to current dir)
     * @param   {Function} checkHandler      handler which is called to check the result (should return a promise)
     * @returns {Promise}                    promise
     */
    me.clickButtonImportAndCheck = function (linkImportCommand, fileToUpload, checkHandler) {
        // look for element and click
        protractor.utils.waitUntilElementClickable(linkImportCommand, protractor.utils.CONST_WAIT_ELEMENT);
        expect(linkImportCommand.isDisplayed()).toEqual(true);

        // open upload
        return linkImportCommand.click()
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
     * @returns {Element} linkImportCommand   import-link
     * @returns {String}  fileToUpload       filename to import (relative to current dir)
     * @param   {Function} checkHandlerImportWindow    handler which is called to check ImportWindow (should return a promise)
     * @param   {Function} checkHandlerMainWindow      handler which is called to check MainWindow (should return a promise)
     * @returns {Promise}                    promise
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
                    browser.refresh();
                    return checkHandlerMainWindow();
                })
        }
        return me.clickButtonImportAndCheck(linkImportCommand, fileToUpload, newWindowCheckHandler);
    };
};
module.exports = YAIOImporterPage;
