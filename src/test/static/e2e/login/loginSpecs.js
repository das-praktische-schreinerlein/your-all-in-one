/**
 * test the LoginPage of YAIO
 * 
 */
var YAIOLoginPage = require('./login.po.js');
describe('yaio loginpage', function() {
    var yaioLoginPage = new YAIOLoginPage();
    
    beforeEach(function() {
        browser.get(protractor.yaioBaseAppUrl + '/login');
    });
    afterEach(function() {
        browser.get(protractor.yaioBaseAppUrl + '/logout');
    });
    
    it('should show login', function() {
        expect(yaioLoginPage.username.getAttribute('id')).toEqual("username");
    });
    
    it('should reject login with invalid credentials', function() {
        // fill loginform with invalid credentials
        yaioLoginPage.username.sendKeys('admin');
        yaioLoginPage.password.sendKeys('false');
        
        // send login
        yaioLoginPage.submit.click();
    
        // expect login-error
        expect(yaioLoginPage.errorMsg.getText()).toEqual("There was a problem logging in. Please try again.");
    });
    
    it('should show explorer after login', function() {
        // fill loginform with valid credentials
        yaioLoginPage.username.sendKeys('admin');
        yaioLoginPage.password.sendKeys('secret');
        
        // send login
        yaioLoginPage.submit.click();
    
        // expect login-error
        var head = element(by.id('ue_explorer'));
        expect(head.getAttribute('id')).toEqual("ue_explorer");
    });
});

