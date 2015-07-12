/**
 * test the export-functions on ExplorerPage of YAIO
 * 
 */

'use strict';
var YAIOLoginPage = require('../login/login.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');
var YAIONodePage = require('../explorer/node.po.js');
var YAIOExportPage = require('../explorer/exports.po.js');
var fs = require('fs');

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
        protractor.utils.waitUntilElementClickable($(yaioNodePage.containerMasterdata), protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect($(yaioNodePage.containerMasterdata).isPresent()).toEqual(true);
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
    it('should focus on a node, click on snapshot and open the info-editor with a sanpshot of children as checklist+gantt', function doSnapshotOfTestNode() {
        // Given
        var expectedMarkdownPartial = "#·Gantt:·Überschritten·-·Ein·Beispiel-Projekt·(Stand:XXX)";

        // When and Then

        // navigate to Node
        return yaioNodePage.navigateToNode(yaioNodePage.jsFuncTestHierarchy)
            .then(function doneNavigate(){
                // focus on node
                return yaioNodePage.focusOnNode(yaioNodePage.jsLayoutTestId).getText();
            })
            .then(function clickExport() {
                // click Export-Button
                return $('[translate="common.command.Snapshot"]').click();
            })
            .then(function checkForm() {
                // check form to create new infonode with snapshot
                
                // check nodetype
                expect($('#inputTypeInfoNode').isDisplayed()).toEqual(true);
                //expect($('#inputTypeInfoNode > option:selected').getText()).toEqual("Information");

                // check name-data
                expect($('#inputNameInfoNode').isDisplayed()).toEqual(true);
                return $('#inputNameInfoNode').getAttribute('value')
                    .then(function getData(content) {
                        // normalize and check name-data
                        content = content.replace(/vom .*?/g, "vom XXX");
                        expect(content).toContain("Snapshot für: 'Ein Beispiel-Projekt' vom XXX");
                    });
            })
            .then(function checkDescForm() {
                // check desc-data
                expect($('#editorInputNodeDescInfoNode').isDisplayed()).toEqual(true);
                return $('#editorInputNodeDescInfoNode').getText()
                    .then(function getData(content) {
                        // normalize and check parts of desc-data
                        content = content.replace(/\(Stand:.*?\)/g, "(Stand: XXX)");
                        expectedMarkdownPartial = expectedMarkdownPartial.replace(/\(Stand:.*?\)/g, "(Stand: XXX)");
                        expect(content).toContain(expectedMarkdownPartial);
                    });
            });
    });

    it('should focus on a node and export the children as checklist+gantt', function doExportOverrviewOfTestNode() {
        // Given
        var filePath = browser.params.baseDir + "explorer/exports.exportOverview.md";
        var expectedMarkdown;

        var checkClipboardHandlerText = function (clipboard) {
            // check text content
            return clipboard.getText()
                .then(function getData(content) {
                    // normalize and check data
                    content = content.replace(/\(Stand: .*?\)/g, "(Stand: XXX)");
                    expectedMarkdown = expectedMarkdown.replace(/\(Stand: .*?\)/g, "(Stand: XXX)");
                    expect(content).toContain(expectedMarkdown);
                });
        };
        
        // When and Then

        // navigate to Node
        return yaioNodePage.navigateToNode(yaioNodePage.jsFuncTestHierarchy)
            .then(function doneNavigate(){
                return yaioNodePage.focusOnNode(yaioNodePage.jsLayoutTestId).getText();
            })
            .then(function clickExport() {
                // click Export-Button
                return $('[translate="common.command.ExportAsOverview"]').click();
            })
            .then(function readFileContent() {
                // read fixture
                return fs.readFileSync(filePath, { encoding: 'utf8' });
            })
            .then (function setFileContent(content) {
                // set data
                expectedMarkdown = content;
            })
            .then(function checkClipboard() {
                // check text-clipboard
                var clipboardElement = yaioExportPage.checkAndCloseClipboard(checkClipboardHandlerText);
                return clipboardElement.getText();
            });
    });

    it('should focus on a node and export the full Nodetree', function doShowDescOfTestNode() {
        // Given
        var expectedHtmlDocumentation = "Ein Beispiel-Projekt (SysPlay118)";
        var expectedMindmap = '<node  id="SysPlay119" text="WARNING - Teilprojekt 1" ID="SysPlay119" TEXT="WARNING - Teilprojekt 1" background_color="#FF6347" BACKGROUND_COLOR="#FF6347" >';
        var expectedICal = 'Ein Beispiel-Projekt';
        
        // When and Then

        // navigate to Node
        return yaioNodePage.navigateToNode(yaioNodePage.jsFuncTestHierarchy)
            .then(function doneNavigate(){
                return yaioNodePage.focusOnNode(yaioNodePage.jsLayoutTestId).getText();
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
        return yaioNodePage.navigateToNode(yaioNodePage.jsFuncTestHierarchy)
            .then(function doneNavigate(){
                var container = yaioNodePage.showDescForNode(yaioNodePage.jsFuncTestId, contentHandler);
                container.getText().then(function() {
                    deferred.fulfill(container);
                })
                
                return deferred.promise;
            });
    });
});

