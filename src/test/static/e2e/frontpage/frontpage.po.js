/**
 * defines elements of the FrontPage of YAIO
 */
'use strict';

var YAIOFrontpagePage = function() {
    // define elements
    this.linkExplorer = $('[translate="common.command.OpenTreeView"]');
    this.linkSearch = $('[translate="common.command.OpenSearchView"]');

    // content
    this.fontContentLeft = $('#front-content-left');
};
module.exports = YAIOFrontpagePage;
