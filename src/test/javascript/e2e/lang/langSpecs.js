/**
 * test the language specific elements of YAIO
 * 
 */

'use strict';
var YAIOAuthPage = require('../auth/auth.po.js');
var YAIONodePage = require('../explorer/node.po.js');
var YAIOLangPage = require('../lang/lang.po.js');

describe('yaio language-packs', function() {
    // define vars
    var yaioAuthPage, yaioNodePage, yaioLangPage;

    /**
     * prepare tests
     */
    beforeEach(function() {
        // initpages (reset elements)
        yaioAuthPage = new YAIOAuthPage();
        yaioNodePage = new YAIONodePage();
        yaioLangPage = new YAIOLangPage();
        
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
    it('should check the homepage-button text and tooltip, switch to En-Version and back', function doCheckButton() {
        return yaioLangPage.switchLanguageAndCheckTexts($(yaioLangPage.linkFrontpage), $(yaioLangPage.linkSwitchLangDe), "Startseite", "Zurück zur Startseite.")
            .then(function checkEn() {
                return yaioLangPage.switchLanguageAndCheckTexts($(yaioLangPage.linkFrontpage), $(yaioLangPage.linkSwitchLangEn), "Home", "Back to the homepage.");
            })
            .then(function checkDe2() {
                return yaioLangPage.switchLanguageAndCheckTexts($(yaioLangPage.linkFrontpage), $(yaioLangPage.linkSwitchLangDe), "Startseite", "Zurück zur Startseite.");
            });
    });

    it('should check the masterplan-node text, switch to En-Version and back', function doCheckNode() {
        return yaioLangPage.switchLanguageAndCheckTexts($(yaioLangPage.labelMasterplan), $(yaioLangPage.linkSwitchLangDe), "Überschritten - Masterplan")
            .then(function checkEn() {
                return yaioLangPage.switchLanguageAndCheckTexts($(yaioLangPage.labelMasterplan), $(yaioLangPage.linkSwitchLangEn), "short - Masterplan");
            })
            .then(function checkDe2() {
                return yaioLangPage.switchLanguageAndCheckTexts($(yaioLangPage.labelMasterplan), $(yaioLangPage.linkSwitchLangDe), "Überschritten - Masterplan");
            });
    });
});

