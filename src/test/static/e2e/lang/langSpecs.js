/**
 * test the language specific elements of YAIO
 * 
 */

'use strict';
var YAIOLoginPage = require('../login/login.po.js');
var YAIONodePage = require('../explorer/node.po.js');
var YAIOLangPage = require('../lang/lang.po.js');

describe('yaio language-packs', function() {
    // define vars
    var yaioLoginPage, yaioNodePage, yaioLangPage;

    /**
     * prepare tests
     */
    beforeEach(function() {
        // initpages (reset elements)
        yaioLoginPage = new YAIOLoginPage();
        yaioNodePage = new YAIONodePage();
        yaioLangPage = new YAIOLangPage();
        
        // do Login
        yaioLoginPage.checkLogin()
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
        return yaioLangPage.switchLanguageAndCheckTexts($(yaioLangPage.linkFrontpage), $(yaioLangPage.linkSwitchLangDe), "Zur Startseite", "Zurück zur Startseite.")
            .then(function checkEn() {
                return yaioLangPage.switchLanguageAndCheckTexts($(yaioLangPage.linkFrontpage), $(yaioLangPage.linkSwitchLangEn), "Home", "Back to the homepage.");
            })
            .then(function checkDe2() {
                return yaioLangPage.switchLanguageAndCheckTexts($(yaioLangPage.linkFrontpage), $(yaioLangPage.linkSwitchLangDe), "Zur Startseite", "Zurück zur Startseite.");
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

