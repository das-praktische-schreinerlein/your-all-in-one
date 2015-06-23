/**
 * test the elements on the SearchPage of YAIO
 * 
 */

'use strict';
var YAIOLoginPage = require('../login/login.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');
var YAIONodePage = require('../explorer/node.po.js');
var YAIOSearchPage = require('../search/search.po.js');

describe('yaio explorer search', function() {
    // define vars
    var yaioLoginPage, yaioFrontPage, yaioNodePage, yaioSearchPage;

    /**
     * prepare tests
     */
    beforeEach(function() {
        // initpages (reset elements)
        yaioLoginPage = new YAIOLoginPage();
        yaioFrontPage = new YAIOFrontPage();
        yaioNodePage = new YAIONodePage();
        yaioSearchPage = new YAIOSearchPage();
        
        // do Login
        yaioLoginPage.doLogin()
            .then(function doneOpenExplorer() {
                // open explorer
                return yaioSearchPage.openSearchFromFrontPage();
            });
        protractor.utils.waitUntilElementClickable(yaioSearchPage.buttonDoSearch, protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect(yaioSearchPage.buttonDoSearch.isPresent()).toEqual(true);
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
    it('should get initial 20 Nodes with pagination but without searchwords', function doCheckInitialSearchPage() {
        // count visible nodes, pages, seatchwords
        return yaioNodePage.getVisibleNodes().then( function (nodes) {
                // check nodecount
                expect(nodes.length).toEqual(20);
            })
            .then ( function checkPagination() {
                // extract pagination
                return $$(yaioSearchPage.paginationLinkStyles).count()
            }) 
            .then(function setCount(count) {
                // check pagination count
                expect(count).toBeGreaterThan(7);
            })
            .then( function calcSearchWords() {
                // extract searchwords
                return $$(yaioSearchPage.fieldSearchDataStyle).count();
            })
            .then(function setCount(count) {
                // check for 0 searchword-divs
                expect(count).toEqual(0);
            });
    });

    it('should open nodelink when click on open', function doFocusOnNode() {
        return yaioSearchPage.clickAndCheckSearchResLink('.yaio-icon-open');
    });

    it('should open nodelink when click on focus', function doFocusOnNode() {
        return yaioSearchPage.clickAndCheckSearchResLink('.yaio-icon-center');
    });

    it('should search for "der" with less pagination and show more than 20 searchwords', function doCheckInitialSearchPage() {
        // count pages
        var firstCount = 0;
        $$(yaioSearchPage.paginationLinkStyles).count()
            .then(function setCount(count) {
                // set+check initial pagination count
                firstCount = count;
            })
            .then( function fillSearchForm() {
                // do Search
                yaioSearchPage.inputFullText.sendKeys('der');
                return yaioSearchPage.buttonDoSearch.click();
            })
            .then( function calcPagination() {
                // extract pagination
                return $$(yaioSearchPage.paginationLinkStyles).count();
            })
            .then(function setCount(count) {
                // check for lesser resultpages
                expect(count).toBeLessThan(firstCount);
            })
            .then( function calcSearchWords() {
                // extract searchwords
                return $$(yaioSearchPage.fieldSearchDataStyle).count();
            })
            .then(function setCount(count) {
                // check for 20 searchword-divs
                expect(count).toBeGreaterThan(19);
            });
    });

});

