/**
 * defines elements of the LoginPage of YAIO
 */
'use strict';

var YAIOLoginPage = function() {
    // login-form
    var me = this;
    me.eleUsername = element(by.model('credentials.username'));
    me.elePassword = element(by.model('credentials.password'));
    me.submit = '[translate="loginform.button.login"]';
    
    // results
    me.errorMsg = '.alert-danger';
    
    /**
     * open login-page
     * @returns {Browser}  - browser element
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
        me.eleUsername.sendKeys('admin');
        me.elePassword.sendKeys('secret');
        
        // send login
        return $(me.submit).click();
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
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/logout');
        return browser;
    }
    
};
module.exports = YAIOLoginPage;
