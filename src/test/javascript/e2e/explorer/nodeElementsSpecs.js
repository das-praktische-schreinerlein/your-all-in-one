/**
 * test the node elements on the ExplorerPage of YAIO
 * 
 */

'use strict';
var YAIOAuthPage = require('../auth/auth.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');
var YAIONodePage = require('../explorer/node.po.js');

describe('yaio explorer nodeelements', function() {
    // define vars
    var yaioAuthPage, yaioFrontPage, yaioNodePage;

    /**
     * prepare tests
     */
    beforeEach(function() {
        // initpages (reset elements)
        yaioAuthPage = new YAIOAuthPage();
        yaioFrontPage = new YAIOFrontPage();
        yaioNodePage = new YAIONodePage();
        
        // do Login
        yaioAuthPage.checkLogin()
            .then(function doneOpenExplorer() {
                // open explorer
                return yaioNodePage.openExplorerFromFrontPage();
            });
        protractor.utils.waitUntilElementClickable($(yaioNodePage.containerMasterdata), protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect($(yaioNodePage.containerMasterdata).isPresent()).toEqual(true);
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
    it('should show Desc of Testnode', function doShowDescOfTestNode() {
        // Given
        var expectedText = 'Diese Aufgabe bitte nicht löschen, da hier die JavaScript-E2E-Tests ausgeführt werden.';
        var checkContentHandler = function (container) {
            expect(container.getText()).toContain(expectedText);
            return container.getText();
        };

        // When and Then

        // navigate to Node
        yaioNodePage.navigateToNode(yaioNodePage.jsFuncTestHierarchy)
            .then(function doneNavigate(){
                // show desc of testnode
                var deferred = protractor.promise.defer();
                var container = yaioNodePage.showDescForNode(yaioNodePage.jsFuncTestId, checkContentHandler);
                container.getText().then(function() {
                    deferred.fulfill(container);
                });
                
                return deferred.promise;
            });
    });

    it('should show Sys of Testnode', function doShowSysOfTestNode() {
        // Given
        var expectedText = 'Stand:';
        var checkContentHandler = function (container) {
            expect(container.getText()).toContain(expectedText);
            return container.getText();
        };

        // When and Then

        // navigate to Node
        yaioNodePage.navigateToNode(yaioNodePage.jsFuncTestHierarchy)
            .then(function doneNavigate(){
                // show sys of testnode
                var deferred = protractor.promise.defer();
                
                // call service-function
                var container = yaioNodePage.showSysForNode(yaioNodePage.jsFuncTestId, checkContentHandler);
                container.getText().then(function() {
                    deferred.fulfill(container);
                });
                
                return deferred.promise;
            });
    });
});

