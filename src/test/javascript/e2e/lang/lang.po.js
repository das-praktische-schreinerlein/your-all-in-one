/**
 * defines elements of the language-packs of YAIO
 */
'use strict';
var fs = require('fs');

var YAIOLangPage = function() {
    var me = this;

    // language links
    me.linkSwitchLangDe = '#button_lang_de';
    me.linkSwitchLangEn = '#button_lang_en';
    
    // tooltip
    me.tooltip = '.ui-tooltip';

    // explorer-link
    me.linkFrontpage = '[translate="common.command.OpenFrontpage"]';
    me.linkMouseReset = '#ue_explorer';
    me.labelMasterplan = '#masterTr > td.container_field.fieldtype_name.field_name';
    
    /**
     * switch language-button and check content of element and tooltip if mouseover
     * @param   {String}  ele            element to check content and tooltip if avaliable
     * @param   {String}  langSwitchLink language-switch to click
     * @param   {String}  contentText    expected text inside ele
     * @param   {String}  contentText    optional expected tooltip when mouseover on ele
     * @returns {Promise}                promise 
     */
    me.switchLanguageAndCheckTexts = function (ele, langSwitchLink, contentText, tooltipText) {
        return langSwitchLink.click()
            .then(function resetMousePos() {
                // reset mousepos for tooltips
                return $(me.linkMouseReset).click();
            })
            .then( function openTooltip() {
                // open Tooltip
                browser.sleep(500); // sleep for language-switch + tooltip
                return browser.actions().mouseMove(ele).perform();
            })
            .then( function checkDe2() {
                // check DE
                browser.sleep(500); // sleep for language-switch + tooltip
                
                if (tooltipText) {
                    // search for tooltip
                    var tooltip = element(by.cssContainingText(me.tooltip, tooltipText));
                    protractor.utils.waitUntilElementPresent(tooltip, protractor.utils.CONST_WAIT_ELEMENT);
                    expect(tooltip.getText()).toEqual(tooltipText);
                    
                }

                // check ele
                expect(ele.getText()).toEqual(contentText);
                
                // reset mousepos for tooltips
                return browser.actions().mouseMove($(me.linkMouseReset)).perform();
            });
    };
};
module.exports = YAIOLangPage;
