(function () {
    'use strict';

    // configure
    var baseUrl = 'http://yaio-playground.local';
    var chromeBin = 'C:/ProgrammePortable/PortableApps/PortableApps/GoogleChromePortable/App/Chrome-bin/chrome.exe';
    var firefoxBin = 'C:/ProgrammePortable/PortableApps/PortableApps/FirefoxPortable/App/Firefox/firefox.exe';
    var downloadPath = 'd:/tmp/';

    // basics
    var baseDir = './src/test/javascript/e2e/';
    var testDir = baseDir;

    // imports
    var ScreenShotReporter = require('protractor-screenshot-reporter');
    var Utils = require(baseDir + './utils/utils.js');
    var FirefoxProfile = require('firefox-profile');

    // set some userPrefs if needed
    // Note: make sure you call encoded() after setting some userPrefs
    var fp = new FirefoxProfile();
    // activate and open firebug by default for all sites
    //fp.setPreference('extensions.firebug.allPagesActivation', 'on');
    // activate the console panel
    //fp.setPreference('extensions.firebug.console.enableSites', true);
    // show the console panel
    //fp.setPreference('extensions.firebug.defaultPanelName', 'console');
    fp.setPreference('browser.download.folderList', 2);
    fp.setPreference('browser.download.manager.showWhenStarting', false);
    fp.setPreference('browser.download.dir', downloadPath);
    fp.setPreference('browser.helperApps.neverAsk.saveToDisk', 'text/html');
    // done with prefs?
    fp.updatePreferences();


    exports.config = {
        // The address of a running selenium server.
        seleniumAddress: 'http://localhost:4444/wd/hub',
        seleniumServerJar: 'node_modules/selenium/lib/runner/selenium-server-standalone-2.20.0.jar',
        chromeDriver: 'node_modules/chromedriver/bin/chromedriver',

        // Capabilities to be passed to the webdriver instance.
        multiCapabilities: [
//        {
//            'browserName': 'chrome',
//            'chromeOptions': {
//                'binary': chromeBin,
//                args: [],
//                extensions: [],
//                prefs: {
//                    'download': {
//                        'prompt_for_download': false,
//                        'default_directory': downloadPath,
//                    },
//                },
//            },
//        },
//        {
//                'browserName': 'firefox',
//                'firefox_binary': firefoxBin,
//                'firefox_profile': fp
//        },
//        {
//            'browserName': 'iexplorer'
//        },
            {
                // phantomjs buggy :-(
                'browserName': 'phantomjs',
                'phantomjs.binary.path': require('phantomjs').path,
                // Command line args to pass to ghostdriver, phantomjs's browser driver. See https://github.com/detro/ghostdriver#faq
                'phantomjs.ghostdriver.cli.args': ['--local-storage-path d:/tmp/', '--loglevel=DEBUG']
            }
        ],

        getPageTimeout: 100000,
        allScriptsTimeout: 100000,

        // Spec patterns are relative to the configuration file location passed
        suites: {
            auth: [testDir + '/auth/**/*Specs.js'],
            explorer: [testDir + '/explorer/**/*Specs.js'],
            guielements: [testDir + '/explorer/**/guiElementsSpecs.js'],
            nodeelements: [testDir + '/explorer/**/nodeElementsSpecs.js'],
            exporter: [testDir + '/exporter/**/*Specs.js'],
            gantt: [testDir + '/gantt/**/*Specs.js'],
            importer: [testDir + '/importer/**/*Specs.js'],
            lang: [testDir + '/lang/**/*Specs.js'],
            nodelifecycle: [testDir + '/nodelifecycle/**/*Specs.js'],
            search: [testDir + '/search/**/*Specs.js'],
            wysiwyg: [testDir + '/wysiwyg/**/*Specs.js'],

            full: [testDir + '/**/*Specs.js']
        },

        // Options to be passed to Jasmine-node.
        jasmineNodeOpts: {
            showColors: true, // Use colors in the command line report.
            defaultTimeoutInterval: 360000,
            isVerbose: true,
            realtimeFailure: true,
            includeStackTrace: true,
            print: function () {
            }
        },

        // add Screenshots on error
        onPrepare: function () {
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
                    success: 'OK     ', //'✓ ',
                    failure: 'FAILED ', //'✗ ',
                    pending: '-      '  //'- '
                }
            }));

            // add ScreenshotReporter
// sometimes buggy :-(
            jasmine.getEnv().addReporter(
                new ScreenShotReporter({
                        baseDirectory: 'target/protractor-reports/'
                    }
                )
            );

            // add logging
            browser.manage().logs().get('browser').then(function (browserLog) {
                console.log('log: ' + require('util').inspect(browserLog));
            });

            // add browsername
            browser.getCapabilities().then(function (cap) {
                browser.browserName = cap.caps_.browserName;
            });

            // add utils
            protractor.utils = Utils;
            by.addLocator('text', protractor.utils.findByText);
        },

        // specific YAIO-config
        params: {
            downloadPath: downloadPath,
            baseDir: baseDir,
            yaioConfig: {
                yaioBaseUrl: baseUrl,
                yaioBaseAppUrl: baseUrl + '/yaio-explorerapp/yaio-explorerapp.html#',
                browserSize: {
                    width: 1380,
                    height: 800
                }
            }
        }
    };
})();