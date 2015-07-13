/**
 * test the import-functions on ExplorerPage of YAIO
 * 
 */

'use strict';
var YAIOLoginPage = require('../login/login.po.js');
var YAIONodePage = require('../explorer/node.po.js');
var YAIOImportPage = require('../explorer/imports.po.js');

describe('yaio explorer imports', function() {

    var yaioLoginPage = new YAIOLoginPage();
    var yaioNodePage = new YAIONodePage();
    var yaioImportPage = new YAIOImportPage();

    /**
     * prepare tests
     */
    beforeEach(function() {
        // do Login
        yaioLoginPage.checkLogin();
        protractor.utils.waitUntilElementPresent($(yaioLoginPage.loginResult), protractor.utils.CONST_WAIT_NODEHIRARCHY);
    });

    /**
     * cleanup after tests
     */
    afterEach(function() {
        browser.ignoreSynchronization = false;
    });
    
    /**
     * define tests
     */
    it('should focus on a node, click on import, upload file, reload page, delete new task', function doSnapshotOfTestNode() {
        // load page
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/show/' + yaioNodePage.jsFuncTestId);
        protractor.utils.waitUntilElementClickable($(yaioImportPage.linkImport), protractor.utils.CONST_WAIT_NODEHIRARCHY);
        
        // upload-file and new task
        var taskName = 'uploadtest';
        var fileToUpload = 'import.upload.wiki';

        // define checkhandler
        var checkHandlerImportWindow = function () {
            // check popupwindow with the import-message
            var defer = protractor.promise.defer();
            browser.ignoreSynchronization = true;
            browser.getPageSource()
                .then(function setSource(source) {
                    if (source && (source.search(/data for node .* imported/g) > 0)) {
                        // import solved
                        browser.ignoreSynchronization = false;
                        defer.fulfill(true);
                    } else {
                        browser.ignoreSynchronization = false;
                        console.error("upload failed:" + source);
                        defer.reject(false);
                    }
                    expect(source).toContain('imported');
                });
            // Return a promise so the caller can wait on it for the request to complete
            return defer.promise;
        };
        var checkHandlerMainWindow = function () {
            // check mainwindow for new task
            protractor.utils.waitUntilElementClickable($(yaioImportPage.linkImport), protractor.utils.CONST_WAIT_NODEHIRARCHY);
            var eleNewTaskName = element(by.cssContainingText(yaioNodePage.spanNodeName, taskName));
            protractor.utils.waitUntilElementVisible(eleNewTaskName, protractor.utils.CONST_WAIT_NODEHIRARCHY);
            expect(eleNewTaskName.getText()).toBe(taskName)
            return eleNewTaskName;
        };

        // navigate to Node
        return yaioImportPage.importFileAndCheckForNewTask(
                $(yaioImportPage.linkImport), 
                fileToUpload,
                checkHandlerImportWindow,
                checkHandlerMainWindow)
            .then(function extractNodeid(eleNewTaskName) {
                // extract nodeid from new Element
                return yaioNodePage.extractNodeIdFromNodeNameElement(eleNewTaskName);
            })
            .then(function deleteUploadedNode(nodeId){
                // delete this node
                var deferred = protractor.promise.defer();
                var deletedElement = yaioNodePage.deleteNodeById(nodeId, yaioNodePage.jsFuncTestId);
                deletedElement.isPresent().then(function() {
                    deferred.fulfill(deletedElement);
                })

                // check that element no more exists
                expect(deletedElement.isPresent()).toEqual(false);
                
                return deferred.promise;
            });
    });
});

