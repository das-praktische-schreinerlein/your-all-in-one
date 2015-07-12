/**
 * test the language specific elements of YAIO
 * 
 */

'use strict';
var YAIOLoginPage = require('../login/login.po.js');
var YAIONodePage = require('../explorer/node.po.js');
var YAIOWysiwygPage = require('../wysiwyg/wysiwyg.po.js');

describe('yaio wysiwyg', function() {
    // define vars
    var yaioLoginPage, yaioNodePage, yaioWysiwygPage;

    /**
     * prepare tests
     */
    beforeEach(function() {
        // initpages (reset elements)
        yaioLoginPage = new YAIOLoginPage();
        yaioNodePage = new YAIONodePage();
        yaioWysiwygPage = new YAIOWysiwygPage();
        
        // do Login
        yaioLoginPage.doLogin()
            .then(function doneOpenExplorer() {
                // open explorer
                return yaioNodePage.openExplorerFromFrontPage();
            });
        protractor.utils.waitUntilElementClickable($(yaioNodePage.containerMasterdata), protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect($(yaioNodePage.containerMasterdata).isPresent()).toEqual(true);
        browser.ignoreSynchronization = true;
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
    it('should focus on node, open wysiwyg-editor and add markdown', function doCheckButtons() {
        // Given
        var markdownText = '# Ue1\n\n## Ue2\n';
        var expected = '<p>Diese Aufgabe bitte nicht löschen, da hier die JavaScript-E2E-Tests ausgeführt werden.</p>\n<h1 id="undefined_5_ue1">Ue1</h1>\n<h2 id="undefined_6_ue2">Ue2</h2>\n';
        
        // open page
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/show/' + yaioNodePage.jsFuncTestId);
        
        return $('#cmdEditJsFuncTest1').click()
            .then(function openWysiwygEditor() {
                // open wysiwyg-editor
                browser.sleep(1000);
                return $('#openWysiwyg4inputNodeDescTaskNode').click();
            })
            .then(function sendMarkdown() {
                // sendMarkdown
                return yaioWysiwygPage.checkWysiwygContent(markdownText, expected)
            })
            .then(function extendMarkdown() {
                // extend markdown
                markdownText = '\n### Ue3\n';
                expected = '<p>Diese Aufgabe bitte nicht löschen, da hier die JavaScript-E2E-Tests ausgeführt werden.</p>\n<h1 id="undefined_7_ue1">Ue1</h1>\n<h2 id="undefined_8_ue2">Ue2</h2>\n<h3 id="undefined_9_ue3">Ue3</h3>';
                return yaioWysiwygPage.checkWysiwygContent(markdownText, expected);
            });
        
    });

    it('should open wysiwyg-editor, submit markdown, add more markdown in a second step', function doCheckButtons() {
        // Given
        var markdownText = '# Ue1\n\n## Ue2\n';
        var expected = '<h1 id="undefined_1_ue1">Ue1</h1>\n<h2 id="undefined_2_ue2">Ue2</h2>\n';
        
        // check markdown
        browser.get(browser.params.yaioConfig.yaioBaseUrl + '/yaio-explorerapp/wysiwyg.html');
        return yaioWysiwygPage.checkWysiwygContent(markdownText, expected)
            .then(function extendMarkdown() {
                // extend markdown
                markdownText = '\n### Ue3\n';
                expected = '<h1 id="undefined_3_ue1">Ue1</h1>\n<h2 id="undefined_4_ue2">Ue2</h2>\n<h3 id="undefined_5_ue3">Ue3</h3>';
                return yaioWysiwygPage.checkWysiwygContent(markdownText, expected);
            });
        
    });
});

