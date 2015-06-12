// configure
var baseDir = "./src/test/static/e2e/";
var testDir = baseDir;
var baseUrl = 'http://yaio-playground.local';
var chromeBin = 'C://ProgrammePortable//PortableApps//PortableApps//GoogleChromePortable//App//Chrome-bin//chrome.exe';

// imports
var ScreenShotReporter = require('protractor-screenshot-reporter');
var Utils = require(baseDir + './utils/utils.js');

exports.config = {
    // The address of a running selenium server.
    seleniumAddress: 'http://localhost:4444/wd/hub',

    // Capabilities to be passed to the webdriver instance.
    capabilities: {
        'browserName': 'chrome',
        'chromeOptions': {
            binary: chromeBin,
            args: [],
            extensions: [],
        }
    },
    
    getPageTimeout: 100000,
    allScriptsTimeout: 100000,

    // Spec patterns are relative to the configuration file location passed
    suites: {
        login:         [testDir + '/login/**/*Specs.js'],
        nodelifecycle: [testDir + '/nodelifecycle/**/*Specs.js'],
        full:          [testDir + '/**/*Specs.js'],
        //search:        ['tests/e2e/search/**/*Spec.js']
        //formats:       ['tests/e2e/formats/**/*Spec.js']
        //export:        ['tests/e2e/export/**/*Spec.js']
    },

    // Options to be passed to Jasmine-node.
    jasmineNodeOpts: {
        showColors: true, // Use colors in the command line report.
        defaultTimeoutInterval: 360000,
        print: function() {}
    },
    
    // add Screenshots on error
    onPrepare: function() {
        // add spec-reporter
        var SpecReporter = require('jasmine-spec-reporter');
        // add jasmine spec reporter
        jasmine.getEnv().addReporter(new SpecReporter({
            displayStacktrace: 'all',     // display stacktrace for each failed assertion, values: (all|specs|summary|none) 
            displayFailuresSummary: true, // display summary of all failures after execution 
            displaySuccessfulSpec: true,  // display each successful spec 
            displayFailedSpec: true,      // display each failed spec 
            displayPendingSpec: true,     // display each pending spec 
            displaySpecDuration: true,    // display each spec duration 
            displaySuiteNumber: true,     // display each suite number (hierarchical) 
            colors: {
              success: 'green',
              failure: 'red',
              pending: 'cyan'
            },
            prefixes: {
                success: '✓ ',
                failure: '✗ ',
                pending: '- '
            }
        }));

        // add ScreenshotReporter
        jasmine.getEnv().addReporter(
            new ScreenShotReporter({
                baseDirectory: "target/protractor-reports/"
                }
            )
        );
        
        // add logging
        browser.manage().logs().get('browser').then(function(browserLog) {
            console.log('log: ' + require('util').inspect(browserLog));
        });
        
        // add utils
        protractor.utils = Utils;
        by.addLocator('text', protractor.utils.findByText);
    },

    // specific YAIO-config
    params: {
        yaioConfig: {
            yaioBaseUrl    : baseUrl,
            yaioBaseAppUrl : baseUrl + '/yaio-explorerapp/yaio-explorerapp.html#',
            browserSize    : {
                width: 1280,
                height: 800
            }
        },
    },
};
