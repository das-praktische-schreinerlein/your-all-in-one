/**
 * test the ExplorerPage of YAIO with a full lifecycle of a node
 * 
 */

'use strict';
var YAIOLoginPage = require('../login/login.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');
var YAIONodePage = require('../explorer/node.po.js');

describe('yaio explorer guielements', function() {
    // define vars
    var yaioLoginPage, yaioFrontPage, yaioNodePage;

    /**
     * prepare tests
     */
    beforeEach(function() {
        // initpages (reset elements)
        yaioLoginPage = new YAIOLoginPage();
        yaioFrontPage = new YAIOFrontPage();
        yaioNodePage = new YAIONodePage();
        
        // do Login
        yaioLoginPage.doLogin()
        .then(function doneOpenExplorer() {
            // open explorer
            return yaioNodePage.openExplorerFromFrontPage();
        });
        protractor.utils.waitUntilElementClickable(yaioNodePage.containerMasterdata, protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect(yaioNodePage.containerMasterdata.isPresent()).toEqual(true);
    });

    /**
     * cleanup after tests
     */
    afterEach(function() {
        browser.ignoreSynchronization = false;
        
        // do logout
        yaioLoginPage.doLogout();
    });
    
    /**
     * define tests
     */
    it('should show Desc of Testnode', function doShowDescOfTestNode() {
        // Given
        var expectedText = "Diese Aufgabe bitte nicht löschen, da hier die JavaScript-E2E-Tests ausgeführt werden.";
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
            })
            
            return deferred.promise;
        });
    });

    it('should show Sys of Testnode', function doShowSysOfTestNode() {
        // Given
        var expectedText = "Stand:";
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
            })
            
            return deferred.promise;
        });
    });
});

