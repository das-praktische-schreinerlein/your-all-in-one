/**
 * test the language specific elements of YAIO
 * 
 */

'use strict';
var YAIOAuthPage = require('../auth/auth.po.js');
var YAIONodePage = require('../explorer/node.po.js');
var YAIOWysiwygPage = require('../wysiwyg/wysiwyg.po.js');

describe('yaio wysiwyg', function() {
    // define vars
    var yaioAuthPage, yaioNodePage, yaioWysiwygPage;

    /**
     * prepare tests
     */
    beforeEach(function() {
        // initpages (reset elements)
        yaioAuthPage = new YAIOAuthPage();
        yaioNodePage = new YAIONodePage();
        yaioWysiwygPage = new YAIOWysiwygPage();
        
        // do Login
        yaioAuthPage.doLogin();
        protractor.utils.waitUntilElementPresent($(yaioAuthPage.loginResult), protractor.utils.CONST_WAIT_NODEHIRARCHY);
        browser.ignoreSynchronization = true;
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
    it('should focus on node, open wysiwyg-editor and add markdown, close editor and open preview', function doCheckButtons() {
        // Given
        var markdownText = '# Ue1\n\n## Ue2\n';
        var expected = '<p class="jsh-md-p">Diese Aufgabe bitte nicht löschen, da hier die JavaScript-E2E-Tests ausgeführt werden.</p>\n<h1 id="undefined_1_ue1">Ue1</h1>\n<h2 id="undefined_2_ue2">Ue2</h2>\n';
        
        // open page
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/show/' + yaioNodePage.jsFuncTestId);
        protractor.utils.waitUntilElementClickable($('#cmdEditJsFuncTest1'), protractor.utils.CONST_WAIT_NODEHIRARCHY);
        return $('#cmdEditJsFuncTest1').click()
            .then(function openWysiwygEditor() {
                // open wysiwyg-editor
                browser.sleep(1000);
                return $(yaioNodePage.openWysiwyg4inputNodeDescTaskNode).click();
            })
            .then(function sendMarkdown() {
                // sendMarkdown
                return yaioWysiwygPage.checkWysiwygContent(markdownText, expected);
            })
            .then(function extendMarkdown() {
                // extend markdown
                markdownText = '\n### Ue3\n';
                expected = '<p class="jsh-md-p">Diese Aufgabe bitte nicht löschen, da hier die JavaScript-E2E-Tests ausgeführt werden.</p>\n<h1 id="undefined_3_ue1">Ue1</h1>\n<h2 id="undefined_4_ue2">Ue2</h2>\n<h3 id="undefined_5_ue3">Ue3</h3>';
                return yaioWysiwygPage.checkWysiwygContent(markdownText, expected);
            })
            .then(function closeMarkdown() {
                // close markdown-editor
                return $$('div.ui-dialog-buttonset button.ui-button.ui-button-text-only span.ui-button-text').first().click();
            })
            .then(function closeMarkdown() {
                // open Preview
                return $('#showPreview4inputNodeDescTaskNode').click();
            })
            .then(function checkPreview() {
                // check Preview
                expected = '<p class="jsh-md-p">Diese Aufgabe bitte nicht löschen, da hier die JavaScript-E2E-Tests ausgeführt werden.</p>\n<h1 id="undefined_6_ue1">Ue1</h1>\n<h2 id="undefined_7_ue2">Ue2</h2>\n<h3 id="undefined_8_ue3">Ue3</h3>';
                var editorPreview = $('#ymf-preview-content');
                expect(editorPreview.getInnerHtml()).toContain(expected.trim());
            });
    });

    it('should open wysiwyg-editor, submit markdown, add more markdown in a second step', function doCheckButtons() {
        // Given
        var markdownText = '# Ue1\n\n## Ue2\n';
        var expected = '<h1 class="jsh-md-h1" id="undefined_1_ue1">Ue1</h1>\n<h2 class="jsh-md-h2" id="undefined_2_ue2">Ue2</h2>\n';
        
        // check markdown
        browser.get(browser.params.yaioConfig.yaioBaseUrl + '/yaio-explorerapp/ymf-editorapp.html');
        protractor.utils.waitUntilElementVisible($(yaioWysiwygPage.editorInput), protractor.utils.CONST_WAIT_NODEHIRARCHY);
        return yaioWysiwygPage.checkWysiwygContent(markdownText, expected)
            .then(function extendMarkdown() {
                // extend markdown
                markdownText = '\n### Ue3\n';
                expected = '<h1 class="jsh-md-h1" id="undefined_5_ue1">Ue1</h1>\n<h2 class="jsh-md-h2" id="undefined_6_ue2">Ue2</h2>\n<h3 class="jsh-md-h3" id="undefined_7_ue3">Ue3</h3>';
                return yaioWysiwygPage.checkWysiwygContent(markdownText, expected);
            });
        
    });
});

