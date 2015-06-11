/**
 * defines elements of the FrontPage of YAIO
 */
'use strict';

var YAIOFrontpagePage = function() {
    // explorer-link
    this.linkExplorer = $('[translate="common.command.OpenTreeView"]');

    // content
    this.fontContentLeft = $('#front-content-left');
};
module.exports = YAIOFrontpagePage;
