'use strict';
module.exports = {
    options: {
        // Location of your protractor config file
        configFile: "protractor.yaio.conf.js",

        // Do you want the output to use fun colors?
        noColor: false,

        // Set to true if you would like to use the Protractor command line debugging tool
        // debug: true,

        // Additional arguments that are passed to the webdriver command
        args: { suite: "full"}
    },
    e2e: {
        options: {
            // Stops Grunt process if a test fails
            keepAlive: false
        }
    },
    continuous: {
        options: {
            keepAlive: true
        }
    }
};

