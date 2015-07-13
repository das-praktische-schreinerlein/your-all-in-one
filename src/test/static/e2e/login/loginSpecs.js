/**
 * test the LoginPage of YAIO
 * 
 */
'use strict';

var YAIOLoginPage = require('./login.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');

describe('yaio loginpage', function() {
    // define vars
    var yaioLoginPage, yaioFrontPage;

    /**
     * prepare tests
     */
    beforeEach(function() {
        // initpages (reset elements)
        yaioLoginPage = new YAIOLoginPage();
        yaioFrontPage = new YAIOFrontPage();
        
        yaioLoginPage.doLogout();
        yaioLoginPage.openLoginPage();
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
        expect(yaioLoginPage.eleUsername.getAttribute('id')).toEqual("username");
    });
    
    it('should reject login with invalid credentials', function doRejectInvalidLogin() {
        // fill loginform with invalid credentials
        yaioLoginPage.eleUsername.sendKeys('admin');
        yaioLoginPage.elePassword.sendKeys('blabla');
        
        // send login
        $(yaioLoginPage.submit).click();
    
        // expect login-error
        expect($(yaioLoginPage.errorMsg).getText()).toEqual("There was a problem logging in. Please try again.");
    });
    
    it('should show explorer after login', function doValidLogin() {
        // fill loginform with valid credentials
        yaioLoginPage.submitValidLoginPage();
    
        // expect frontContent
        protractor.utils.waitUntilElementPresent($(yaioFrontPage.fontContentLeft), protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect($(yaioFrontPage.fontContentLeft).getAttribute('id')).toEqual("front-content-left");
    });
});

