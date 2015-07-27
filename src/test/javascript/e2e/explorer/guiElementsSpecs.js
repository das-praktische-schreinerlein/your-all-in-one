/**
 * test the gui elements on the ExplorerPage of YAIO
 * 
 */

'use strict';
var YAIOAuthPage = require('../auth/auth.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');
var YAIONodePage = require('../explorer/node.po.js');

describe('yaio explorer guielements', function() {
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
    it('should open/collapse tree when click on "Open till Level"', function doOpenCollapseTree() {
        // Given
        var countNodes = 0;

        // count visible nodes
        return yaioNodePage.getVisibleNodes().then( function (nodes) {
                // set nodecount
                countNodes = nodes.length;
            })
            .then( function doPpenTillLevel() {
                // open hierarchy
                return $(yaioNodePage.buttonOpenTilllevel).click();
            })
            .then( function () {
                // count nodes
                return yaioNodePage.getVisibleNodes().then( function (nodes) {
                    // set nodecount
                    expect(nodes.length).toBeGreaterThan(countNodes);
                    countNodes = nodes.length;
                });
            })
            .then( function () {
                // increment (arrow_right)
                $(yaioNodePage.inputOpenTilllevel).click();
                return $(yaioNodePage.inputOpenTilllevel).sendKeys('\uE014');
            })
            .then( function () {
                // count nodes
                return yaioNodePage.getVisibleNodes().then( function (nodes) {
                    // check nodecount
                    expect(nodes.length).toBeGreaterThan(countNodes);
                    countNodes = nodes.length;
                });
            })
            .then( function () {
                // dec level (arrow_left)
                $(yaioNodePage.inputOpenTilllevel).click();
                return $(yaioNodePage.inputOpenTilllevel).sendKeys('\uE012\uE012\uE012\uE012\uE012\uE012\uE012');
            })
            .then( function () {
                // count nodes
                return yaioNodePage.getVisibleNodes().then( function (nodes) {
                    expect(nodes.length).toBeLessThan(countNodes);
                });
            });
    });
});

