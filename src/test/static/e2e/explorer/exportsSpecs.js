/**
 * test the ExplorerPage of YAIO with export-functions
 * 
 */

'use strict';
var YAIOLoginPage = require('../login/login.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');
var YAIONodePage = require('../explorer/node.po.js');
var YAIOExportPage = require('../explorer/exports.po.js');

describe('yaio explorer exports', function() {

    var yaioLoginPage = new YAIOLoginPage();
    var yaioFrontPage = new YAIOFrontPage();
    var yaioNodePage = new YAIONodePage();
    var yaioExportPage = new YAIOExportPage();

    /**
     * prepare tests
     */
    beforeEach(function() {
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
    it('should focus on a node and export the full Nodetree', function doShowDescOfTestNode() {
        // Given
        var expectedHtmlDocumentation = "Ein Beispiel-Projekt (SysPlay118)";
        var expectedMindmap = '<node  id="SysPlay119" text="WARNING - Teilprojekt 1" ID="SysPlay119" TEXT="WARNING - Teilprojekt 1" background_color="#FF6347" BACKGROUND_COLOR="#FF6347" >';
        var expectedICal = 'Ein Beispiel-Projekt';
        
        // When and Then

        // navigate to Node
        yaioNodePage.navigateToNode(yaioNodePage.jsFuncTestHierarchy)
            .then(function doneNavigate(){
                // focus on node
                var deferred = protractor.promise.defer();
                // call service-function
                var link = yaioNodePage.focusOnNode(yaioNodePage.jsLayoutTestId);
                link.getText().then(function() {
                    deferred.fulfill(link);
                });
                
                return deferred.promise;
            })
            .then(function doneFocusOnNode() {
                // export as html documentation
                return yaioExportPage.clickShortlinkExportAsHtmlDocumentation(expectedHtmlDocumentation);
            })
            .then(function doneExportAsHtmlDocumentation() {
                // export as ICal 
                return yaioExportPage.clickShortlinkExportAsMindmap(expectedMindmap);
            })
            .then(function doneExportAsMindmap() {
                // export as ICal 
                return yaioExportPage.clickShortlinkExportAsICal(expectedICal);
            });
    });

    it('should export Desc of Testnode', function doShowDescOfTestNode() {
        // Given
        var deferred = protractor.promise.defer();
        var expectedText = "Diese Aufgabe bitte nicht löschen, da hier die JavaScript-E2E-Tests ausgeführt werden.";
        var expectedHtml = "<p>" + expectedText + "</p>";
        var contentHandler = yaioExportPage.createHandlerToCheckNodeExports(yaioNodePage.jsFuncTestId, expectedHtml, expectedText, expectedText);

        // When and Then

        // navigate to Node
        yaioNodePage.navigateToNode(yaioNodePage.jsFuncTestHierarchy)
        .then(function doneNavigate(){

            var container = yaioNodePage.showDescForNode(yaioNodePage.jsFuncTestId, contentHandler);
            container.getText().then(function() {
                deferred.fulfill(container);
            })
            
            return deferred.promise;
        });
    });
});

