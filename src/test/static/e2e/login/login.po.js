/**
 * defines elements of the LoginPage of YAIO
 */
var globConf = require('../config.js');

var YAIOLoginPage = function() {
    // login-form
    this.username = element(by.model('credentials.username'));
    this.password = element(by.model('credentials.password'));
    this.submit = element(by.css('[translate="loginform.button.login"]'));
    
    // results
    this.errorMsg = element(by.css('.alert-danger'));
};
module.exports = YAIOLoginPage;
