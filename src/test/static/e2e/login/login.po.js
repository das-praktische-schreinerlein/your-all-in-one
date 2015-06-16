/**
 * defines elements of the LoginPage of YAIO
 */
'use strict';

var YAIOLoginPage = function() {
    // login-form
    var me = this;
    me.username = element(by.model('credentials.username'));
    me.password = element(by.model('credentials.password'));
    me.submit = $('[translate="loginform.button.login"]');
    
    // results
    me.errorMsg = $('.alert-danger');
    
    /**
     * open login-page
     * @returns {Browser}  - broser element
     */
    me.openLoginPage = function () {
        browser.manage().window().setSize(
                browser.params.yaioConfig.browserSize.width, 
                browser.params.yaioConfig.browserSize.height);
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/login');
        return browser;
    }

    /**
     * submit valid login-page
     * @returns {Promise}  - promise on the submit-click
     */
    me.submitValidLoginPage = function () {
        // fill loginform with invalid credentials
        me.username.sendKeys('admin');
        me.password.sendKeys('secret');
        
        // send login
        return me.submit.click();
    };

    /**
     * do valid login
     * @returns {Promise}  - promise on the submit-click
     */
    me.doLogin = function () {
        me.openLoginPage();
        return me.submitValidLoginPage();
    };

    /**
     * submit logout
     * @returns {Promise}  - promise on the browser.get
     */
    me.doLogout = function () {
        return browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/logout');
    }
    
};
module.exports = YAIOLoginPage;
