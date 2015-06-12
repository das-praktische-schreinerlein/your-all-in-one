'use strict';

var Utils = {
    /**
     * wait until element present in DOM
     * @param   {ElementFinder} elem     expected element
     * @param   {Integer}       timeout  timeout in milliseconds
     * @returns {Promise}       browser.wait for elem
     */
    waitUntilElementPresent: function (elem, timeout) {
        var EC = protractor.ExpectedConditions;
        var isPresent = EC.presenceOf(elem);
        return browser.wait(isPresent, timeout);
    },

    waitTime: function (time) {
        browser.sleep(time);
    },
};

module.exports = Utils;
