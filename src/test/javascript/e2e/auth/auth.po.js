/**
 * defines elements of the AuthPage of YAIO
 */
'use strict';

var YAIOAuthPage = function() {
    // login-form
    var me = this;
    me.eleUsername = element(by.model('credentials.username'));
    me.elePassword = element(by.model('credentials.password'));
    me.submit = '[translate="loginform.button.login"]';
    
    // results
    me.errorMsg = '.alert-danger';
    me.loginResult = '#front-content-left';
    
    /**
     * open login-page
     * @returns {Browser}  - browser element
     */
    me.openAuthPage = function () {
        browser.manage().window().setSize(
                browser.params.yaioConfig.browserSize.width, 
                browser.params.yaioConfig.browserSize.height);
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/login');
        return browser;
    };

    /**
     * submit valid login-page
     * @returns {Promise}
     */
    me.submitValidAuthPage = function () {
        // fill loginform with invalid credentials
        me.eleUsername.sendKeys('admin');
        me.elePassword.sendKeys('secret');
        
        // send login
        return $(me.submit).click();
    };

    /**
     * do valid login
     * @returns {Promise}
     */
    me.doLogin = function () {
        me.openAuthPage();
        return me.submitValidAuthPage();
    };

    /**
     * submit logout
     * @returns {Promise}
     */
    me.doLogout = function () {
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/logout');
        return browser;
    };
    
    /**
     * check login and open pageurl if session is ok or login passes
     * @param {Striong} url    url to open after login-check
     * @returns {Promise}  - promise
     */
    me.loginAndOpenPage = function(url) {
        return me.checkLogin()
            .then(function loadUrl() {
                return browser.get(url);
            });
    };

    /**
     * check login and do login if no session
     * @returns {Promise}  - promise
     */
    me.checkLogin = function() {
        var defer = protractor.promise.defer();
        
        // define checkResponseHandler
        var checkResponseHandler = function(source){
            if (! source || ! (source.search('authenticated') > 0)) {
                // on error do Login
                console.error("session not ok: do login");
                me.doLogin()
                    .then(function loginPassed() {
                        protractor.utils.waitUntilElementPresent($(me.loginResult), protractor.utils.CONST_WAIT_ELEMENT);
                        defer.fulfill(true);
                    }, function loginFailed () {
                        console.error("login failed");
                        defer.reject(false);
                    })
            } else {
                // open Frontpage
                browser.get(browser.params.yaioConfig.yaioBaseAppUrl + "/frontpage")
                    .then(function () {
                        defer.fulfill(true);
                    });
            }
        }
           
        // check the current Userstate 
        browser.ignoreSynchronization = true;
        browser.get(browser.params.yaioConfig.yaioBaseUrl + '/user/current')
            .then(function sucess() {
                browser.getPageSource()
                    .then(function setSource(source) {
                        browser.ignoreSynchronization = false;
                        checkResponseHandler(source);
                    }, function sourceError() {
                        console.error("error while getting source");
                        browser.ignoreSynchronization = false;
                        checkResponseHandler(source);
                    })
            }); 
        
        // Return a promise so the caller can wait on it for the request to complete
        return defer.promise;
    };
};
module.exports = YAIOAuthPage;
