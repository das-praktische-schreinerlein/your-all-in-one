/**
 * test the export-functions on ExplorerPage of YAIO
 * 
 */

'use strict';
var YAIOAuthPage = require('../auth/auth.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');
var YAIONodePage = require('../explorer/node.po.js');
var YAIOExporterPage = require('../exporter/exporter.po.js');
var fs = require('fs');

describe('yaio exporter', function() {

    var yaioAuthPage = new YAIOAuthPage();
    var yaioFrontPage = new YAIOFrontPage();
    var yaioNodePage = new YAIONodePage();
    var yaioExporterPage = new YAIOExporterPage();

    /**
     * prepare tests
     */
    beforeEach(function() {
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
    it('should focus on a node, click on snapshot and open the info-editor with a snapshot of children as checklist+gantt', function doSnapshotOfTestNode() {
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
                protractor.utils.waitUntilElementClickable($(yaioExporterPage.linkSnapshot), protractor.utils.CONST_WAIT_NODEHIRARCHY);
                return $(yaioExporterPage.linkSnapshot).click();
            })
            .then(function checkForm() {
                // check form to create new infonode with snapshot
                protractor.utils.waitUntilElementVisible($(yaioNodePage.inputTypeInfoNode), protractor.utils.CONST_WAIT_NODEHIRARCHY);
                
                // check nodetype
                expect($(yaioNodePage.inputTypeInfoNode).isDisplayed()).toEqual(true);
                //expect($('#inputTypeInfoNode > option:selected').getText()).toEqual("Information");

                // check name-data
                expect($(yaioNodePage.inputNameInfoNode).isDisplayed()).toEqual(true);
                return $(yaioNodePage.inputNameInfoNode).getAttribute('value')
                    .then(function getData(content) {
                        // normalize and check name-data
                        content = content.replace(/vom .*?/g, "vom XXX");
                        expect(content).toContain("Snapshot für: 'Ein Beispiel-Projekt' vom XXX");
                    });
            })
            .then(function checkDescForm() {
                // check desc-data
                expect($(yaioNodePage.editorInputNodeDescInfoNode).isDisplayed()).toEqual(true);
                return $(yaioNodePage.editorInputNodeDescInfoNode).getText()
                    .then(function getData(content) {
                        // normalize and check parts of desc-data
                        content = content.replace(/\(Stand:.*?\)/g, "(Stand: XXX)");
                        expectedMarkdownPartial = expectedMarkdownPartial.replace(/\(Stand:.*?\)/g, "(Stand: XXX)");
                        expect(content).toContain(expectedMarkdownPartial);
                    });
            });
    });

    it('should open page and export the children as checklist+gantt', function doExportOverrviewOfTestNode() {
        // Given
        var filePath = browser.params.baseDir + "exporter/exporter.exportOverview.md";
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
        return browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/show/' + yaioNodePage.jsLayoutTestId)
            .then(function clickExport() {
                // click Export-Button
                protractor.utils.waitUntilElementClickable($(yaioExporterPage.linkExportOverview), protractor.utils.CONST_WAIT_NODEHIRARCHY);
                return $(yaioExporterPage.linkExportOverview).click();
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
                var clipboardElement = yaioExporterPage.checkAndCloseClipboard(checkClipboardHandlerText);
                return clipboardElement.getText();
            });
    });

    it('should open page and export the full Nodetree', function doShowDescOfTestNode() {
        // Given
        var expectedHtmlDocumentation = "Ein Beispiel-Projekt (SysPlay118)";
        var expectedMindmap = '<node  id="SysPlay119" text="WARNING - Teilprojekt 1" ID="SysPlay119" TEXT="WARNING - Teilprojekt 1" background_color="#FF6347" BACKGROUND_COLOR="#FF6347" >';
        var expectedICal = 'Ein Beispiel-Projekt';
        
        // When and Then

        // navigate to Node
        return browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/show/' + yaioNodePage.jsLayoutTestId)
            .then(function doneFocusOnNode() {
                // export as html documentation
                protractor.utils.waitUntilElementClickable($(yaioNodePage.containerMasterdata), protractor.utils.CONST_WAIT_NODEHIRARCHY);
                return yaioExporterPage.clickShortlinkExportAsHtmlDocumentation(expectedHtmlDocumentation);
            })
            .then(function doneExportAsHtmlDocumentation() {
                // export as ICal 
                return yaioExporterPage.clickShortlinkExportAsMindmap(expectedMindmap);
            })
            .then(function doneExportAsMindmap() {
                // export as ICal 
                return yaioExporterPage.clickShortlinkExportAsICal(expectedICal);
            });
    });

    it('should export Desc of Testnode', function doShowDescOfTestNode() {
        // Given
        var deferred = protractor.promise.defer();
        var expectedText = 'Diese Aufgabe bitte nicht löschen, da hier die JavaScript-E2E-Tests ausgeführt werden.';
        var expectedHtml = '<p class="jsh-md-p">' + expectedText + '</p>';
        var contentHandler = yaioExporterPage.createHandlerToCheckNodeExports(yaioNodePage.jsFuncTestId, expectedHtml, expectedText, expectedText);

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

