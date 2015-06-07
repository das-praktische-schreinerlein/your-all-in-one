/**
 * defines elements of the LoginPage of YAIO
 */
'use strict';

var YAIOLoginPage = function() {
    // login-form
    this.username = element(by.model('credentials.username'));
    this.password = element(by.model('credentials.password'));
    this.submit = element(by.css('[translate="loginform.button.login"]'));
    
    // results
    this.errorMsg = element(by.css('.alert-danger'));
    
    this.openLoginPage = function () {
        browser.manage().window().setSize(
                browser.params.yaioConfig.browserSize.width, 
                browser.params.yaioConfig.browserSize.height);
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/login');
    }

    this.submitValidLoginPage = function () {
        // fill loginform with invalid credentials
        this.username.sendKeys('admin');
        this.password.sendKeys('secret');
        
        // send login
        this.submit.click();
    };

    this.doLogin = function () {
        this.openLoginPage();
        this.submitValidLoginPage();
    };

    this.doLogout = function () {
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/logout');
    }
    
};
module.exports = YAIOLoginPage;
