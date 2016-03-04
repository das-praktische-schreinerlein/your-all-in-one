/**
 * defines elements of the SearchPage of YAIO
 */
'use strict';
var YAIOFrontPage = require('../frontpage/frontpage.po.js');
var fs = require('fs');

var YAIOSearchPage = function() {
    var yaioFrontPage = new YAIOFrontPage();
    var me = this;

    // explorer-link
    me.linkFrontpage = '[translate="common.command.OpenFrontpage"]';

    // search form
    me.buttonDoSearch = '[translate="common.command.DoSearch"]';
    me.inputFullText = '#inputSearchOptionsFulltext';
    me.selectSort = '#inputSearchOptionsSearchSortt';
    me.inputSearchOptionsStrNotNodePraefix = '#inputSearchOptionsStrNotNodePraefix';
    
    // pagination
    me.paginationLinkStyles = 'ul.pagination > li';
    me.linkPaginationLastPage = 'ul.pagination > li:last-of-type > span';

    // searchwords
    me.fieldSearchDataStyle = '.field_nodeSearchData > b';
    
    /**
     * open search from frontpage and wait until present
     * @returns {Promise}
     */
    me.openSearchFromFrontPage = function () {
        // expect frontPage
        protractor.utils.waitUntilElementPresent($(yaioFrontPage.fontContentLeft), protractor.utils.CONST_WAIT_ELEMENT);
        
        // open search-link
        $(yaioFrontPage.linkSearch).click();

        // expect SearchButton
        protractor.utils.waitUntilElementClickable($(me.buttonDoSearch), protractor.utils.CONST_WAIT_ELEMENT);
        expect($(me.buttonDoSearch).isDisplayed()).toEqual(true);

        // reload with static url because we want no hidden searchresult (defaults of SearchOptionsStrNotNodePraefix)
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/search/1/20/lastChangeDown/MasterplanMasternode1////blablub/');
        
        // expect SearchButton
        protractor.utils.waitUntilElementClickable($(me.buttonDoSearch), protractor.utils.CONST_WAIT_ELEMENT);
        expect($(me.buttonDoSearch).isDisplayed()).toEqual(true);
        return protractor.utils.waitUntilElementClickable($(me.buttonDoSearch), protractor.utils.CONST_WAIT_ELEMENT);
    };
    
    
    /**
     * get all resultlinks for linkStyle, click on the first and check that browserlocation changes
     * @param  linkStyle        css to filter the resultlinks
     * @returns {Promise}
     */
    me.clickAndCheckSearchResLink = function(linkStyle) {
        var currentNodeUrl, currentNode;
        return protractor.promise.filter($$(linkStyle), function(node) { 
                return node.isDisplayed(); 
            })
            .then( function setNode(nodes) {
                // set node and extract url
                currentNode = nodes[0];
                return currentNode.getAttribute('href');
            })
            .then( function setUrl(url) {
                // set url
                currentNodeUrl = url;
            })
            .then( function opennode() {
                // click on node
                currentNode.click();
            })
            .then( function checkPage() {
                // check loaded page
                expect(browser.getLocationAbsUrl()).toContain(currentNodeUrl);
            });
    };
};
module.exports = YAIOSearchPage;
