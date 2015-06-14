'use strict';

var Utils = {
    
    CONST_WAIT_ELEMENT: 2000,
    CONST_WAIT_NODEHIRARCHY: 10000,
    
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

    /**
     * wait that element is not present in DOM
     * @param   {ElementFinder} elem     expected element
     * @param   {Integer}       timeout  timeout in milliseconds
     * @returns {Promise}       browser.wait for elem
     */
    waitThatElementIsNotPresent: function (elem, timeout) {
        var EC = protractor.ExpectedConditions;
        var isNotPresent = EC.invisibilityOf(elem);
        return browser.wait(isNotPresent, timeout);
    },

    waitTime: function (time) {
        browser.sleep(time);
    },
};

module.exports = Utils;
