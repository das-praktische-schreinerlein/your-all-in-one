/**
 * test the LoginPage of YAIO
 * 
 */
'use strict';

var YAIOLoginPage = require('./login.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');

describe('yaio loginpage', function() {
    var yaioLoginPage = new YAIOLoginPage();
    var yaioFrontPage = new YAIOFrontPage();
    
    /**
     * prepare tests
     */
    beforeEach(function() {
        yaioLoginPage.openLoginPage();
    });

    /**
     * cleanup after tests
     */
    afterEach(function() {
        yaioLoginPage.doLogout();
    });
    
    /**
     * define tests
     */
    it('should show login', function doShowLogin() {
        expect(yaioLoginPage.username.getAttribute('id')).toEqual("username");
    });
    
    it('should reject login with invalid credentials', function doRejectInvalidLogin() {
        // fill loginform with invalid credentials
        yaioLoginPage.username.sendKeys('admin');
        yaioLoginPage.password.sendKeys('blabla');
        
        // send login
        yaioLoginPage.submit.click();
    
        // expect login-error
        expect(yaioLoginPage.errorMsg.getText()).toEqual("There was a problem logging in. Please try again.");
    });
    
    it('should show explorer after login', function doValidLogin() {
        // fill loginform with valid credentials
        yaioLoginPage.submitValidLoginPage();
    
        // expect frontContent
        protractor.utils.waitUntilElementPresent(yaioFrontPage.fontContentLeft, protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect(yaioFrontPage.fontContentLeft.getAttribute('id')).toEqual("front-content-left");
    });
});

