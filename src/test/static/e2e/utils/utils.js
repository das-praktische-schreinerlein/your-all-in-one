'use strict';

var Utils = {
    waitUntilElementPresent: function (elem, timeout) {
        var EC = protractor.ExpectedConditions;
        var isPresent = EC.presenceOf(elem);
        browser.wait(isPresent, timeout);
    },

    waitTime: function (time) {
        browser.sleep(time);
    },
};

module.exports = Utils;
