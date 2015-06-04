exports.config = {
    // The address of a running selenium server.
    seleniumAddress: 'http://localhost:4444/wd/hub',

    // Capabilities to be passed to the webdriver instance.
    capabilities: {
        'browserName': 'chrome',
        'chromeOptions': {
            binary: 'C://ProgrammePortable//PortableApps//PortableApps//GoogleChromePortable//App//Chrome-bin//chrome.exe',
            args: [],
            extensions: [],
        }
    },

    // Spec patterns are relative to the configuration file location passed
    suites: {
        login: 'src/test/static/e2e/login/**/*Specs.js',
        //search: ['tests/e2e/contact_search/**/*Spec.js']
      },
    //specs: ['src/test/static/e2e/**/*_test.js'],

    // Options to be passed to Jasmine-node.
    jasmineNodeOpts: {
        showColors: true, // Use colors in the command line report.
    }
};