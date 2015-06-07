/**
 * defines elements of the FrontPage of YAIO
 */
'use strict';

var YAIOFrontpagePage = function() {
    // explorer-link
    this.linkExplorer = element(by.css('[translate="common.command.OpenTreeView"]'));

    // content
    this.fontContentLeft = element(by.id('front-content-left'));
};
module.exports = YAIOFrontpagePage;
