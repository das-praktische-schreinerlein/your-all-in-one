/**
 * test the elements on the SearchPage of YAIO
 * 
 */

'use strict';
var YAIOAuthPage = require('../auth/auth.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');
var YAIONodePage = require('../explorer/node.po.js');
var YAIOSearchPage = require('../search/search.po.js');

describe('yaio search', function() {
    // define vars
    var yaioAuthPage, yaioFrontPage, yaioNodePage, yaioSearchPage;

    /**
     * prepare tests
     */
    beforeEach(function() {
        // initpages (reset elements)
        yaioAuthPage = new YAIOAuthPage();
        yaioFrontPage = new YAIOFrontPage();
        yaioNodePage = new YAIONodePage();
        yaioSearchPage = new YAIOSearchPage();
        
        // do Login
        yaioAuthPage.checkLogin()
            .then(function doneOpenExplorer() {
                // open explorer
                return yaioSearchPage.openSearchFromFrontPage();
            });
        protractor.utils.waitUntilElementClickable($(yaioSearchPage.buttonDoSearch), protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect($(yaioSearchPage.buttonDoSearch).isPresent()).toEqual(true);
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
    it('should open searchpage, change order, jump to last page and find first result of origin search as last element on the last page', function doCheckSearchPage() {
        var firstElementId;
        // set defined order
        $(yaioSearchPage.selectSort).sendKeys('nodeNummer absteigend');
        return $(yaioSearchPage.buttonDoSearch).click()
            .then( function getFirstElement() {
                // get first element
                return element.all(by.repeater('node in nodes | filter:search')).first().getAttribute('data-value')
            })
            .then( function getFirstElement(id) {
                // get element id
                firstElementId = id;
            })
            .then( function changeOrder2() {
                // do Search with alternate order
                $(yaioSearchPage.selectSort).sendKeys('NodeNummer aufsteigend');
                return $(yaioSearchPage.buttonDoSearch).click();
            })
            .then(function getNewFirstElement() {
                // extract id of new first Element
                expect(element.all(by.repeater('node in nodes | filter:search')).first().getAttribute('data-value')).not.toEqual(firstElementId);
            })
            .then( function clickLastPage() {
                // jump to last page
                return $(yaioSearchPage.linkPaginationLastPage).click();
            })
            .then(function getLastElement() {
                // last element should be original first element
                expect(element.all(by.repeater('node in nodes | filter:search')).last().getAttribute('data-value')).toEqual(firstElementId);
            });
    });

    it('should get initial searchpage with 20 Nodes and pagination > 7 but without searchwords', function doCheckInitialSearchPage() {
        // count visible nodes, pages, searchwords
        return yaioNodePage.getVisibleNodes()
            .then( function (nodes) {
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

    it('should open nodelink when click on open', function doOpenOnNode() {
        return yaioSearchPage.clickAndCheckSearchResLink('.yaio-icon-open');
    });

    it('should open nodelink when click on focus', function doFocusOnNode() {
        return yaioSearchPage.clickAndCheckSearchResLink('.yaio-icon-center');
    });

    it('should search for "der" with less pagination and show more than 20 searchwords', function doCheckSearchPage() {
        // count pages
        var firstCount = 0;
        $$(yaioSearchPage.paginationLinkStyles).count()
            .then(function setCount(count) {
                // set+check initial pagination count
                firstCount = count;
            })
            .then( function fillSearchForm() {
                // do Search
                $(yaioSearchPage.inputFullText).sendKeys('der');
                return $(yaioSearchPage.buttonDoSearch).click();
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

    it('should show Sys of first SearchResult', function doShowSysOfFirstSearchResult() {
        // Given
        var expectedText = "Stand:";
        var checkContentHandler = function (container) {
            expect(container.getText()).toContain(expectedText);
            return container.getText();
        };

        // When and Then

        // check first Node
        return yaioNodePage.getVisibleNodes()
            .then( function (nodes) {
                // extract nodeid from new task
                return yaioNodePage.extractNodeIdFromNodeNameElement(nodes[0]);
            })
            .then(function doneExtractNodeId(nodeId) {
                // show sys of testnode
                var deferred = protractor.promise.defer();
                
                // call service-function
                var container = yaioNodePage.showSysForNode(nodeId, checkContentHandler);
                container.getText().then(function() {
                    deferred.fulfill(container);
                })
                
                return deferred.promise;
            });
    });

    it('should show Desc of first SearchResult', function doShowDescOfFirstSearchResult() {
        // Given
        var expectedText = "der";
        var checkContentHandler = function (container) {
            expect(container.getText()).toContain(expectedText);
            return container.getText();
        };

        // When
        $(yaioSearchPage.inputFullText).sendKeys('der');
        $(yaioSearchPage.buttonDoSearch).click();

        // check first Node for "der" in desc
        return yaioNodePage.getVisibleNodes()
            .then( function (nodes) {
                // extract nodeid from new task
                return yaioNodePage.extractNodeIdFromNodeNameElement(nodes[0]);
            })
            .then(function doneExtractNodeId(nodeId) {
                // show desc of testnode
                var deferred = protractor.promise.defer();
                
                // call service-function
                var container = yaioNodePage.showDescForNode(nodeId, checkContentHandler);
                container.getText().then(function() {
                    deferred.fulfill(container);
                })
                
                return deferred.promise;
            });
    });
});

