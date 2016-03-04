/**
 * test the AuthPage of YAIO
 * 
 */
'use strict';

var YAIOAuthPage = require('../auth/auth.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');

describe('yaio AuthPage', function() {
    // define vars
    var yaioAuthPage, yaioFrontPage;

    /**
     * prepare tests
     */
    beforeEach(function() {
        // initpages (reset elements)
        yaioAuthPage = new YAIOAuthPage();
        yaioFrontPage = new YAIOFrontPage();
        
        yaioAuthPage.doLogout();
        yaioAuthPage.openAuthPage();
    });

    /**
     * cleanup after tests
     */
    afterEach(function() {
    });
    
    /**
     * define tests
     */
    it('should show login', function doShowLogin() {
        expect(yaioAuthPage.eleUsername.getAttribute('id')).toEqual('username');
    });
    
    it('should reject login with invalid credentials', function doRejectInvalidLogin() {
        // fill loginform with invalid credentials
        yaioAuthPage.eleUsername.sendKeys('admin');
        yaioAuthPage.elePassword.sendKeys('blabla');
        
        // send login
        $(yaioAuthPage.submit).click();
    
        // expect login-error
        expect($(yaioAuthPage.errorMsg).getText()).toEqual('There was a problem logging in. Please try again.');
    });
    
    it('should show explorer after login', function doValidLogin() {
        // fill loginform with valid credentials
        yaioAuthPage.submitValidAuthPage();
    
        // expect frontContent
        protractor.utils.waitUntilElementPresent($(yaioFrontPage.fontContentLeft), protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect($(yaioFrontPage.fontContentLeft).getAttribute('id')).toEqual('front-content-left');
    });
});

