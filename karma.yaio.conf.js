// Karma configuration
// Generated on Sat Apr 25 2015 10:40:30 GMT+0200 (Mitteleuropäische Sommerzeit)

(function () {
    'use strict';

    var packageConfig = require('./package.json');

    var distSrcPath = 'src/main/generated-resources/static/dist/';
    var testSrcPath = 'src/test/javascript/';

    module.exports = function(config) {
        config.set({

            // base path that will be used to resolve all patterns (eg. files, exclude)
            basePath: '',

            // frameworks to use
            // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
            frameworks: ['jasmine'], // jasmine, qunit


            // list of files / patterns to load in the browser
            files: [
                // vendors
                distSrcPath + 'vendors-full-' + packageConfig.vendorversion + '.js',
                // needs own script for loading plugins
                distSrcPath + 'vendors-' + packageConfig.vendorversion + '/fancytree/jquery.fancytree.js',
                distSrcPath + 'vendors-' + packageConfig.vendorversion + '/fancytree/jquery.fancytree.dnd.js',
                distSrcPath + 'vendors-' + packageConfig.vendorversion + '/fancytree/jquery.fancytree.edit.js',
                distSrcPath + 'vendors-' + packageConfig.vendorversion + '/fancytree/jquery.fancytree.gridnav.js',
                distSrcPath + 'vendors-' + packageConfig.vendorversion + '/fancytree/jquery.fancytree.table.js',
                distSrcPath + 'vendors-' + packageConfig.vendorversion + '/ace/ace.js',
                distSrcPath + 'vendors-' + packageConfig.vendorversion + '/ace/ext-spellcheck.js',
                distSrcPath + 'vendors-' + packageConfig.vendorversion + '/freemind-flash/flashobject.js',
                // yaio: files
                distSrcPath + 'yaio-support-dist-' + packageConfig.supportversion + '.js',
                distSrcPath + 'yaio-app-dist-' + packageConfig.appversion + '.js',
                testSrcPath + 'unit/resources/js/jasmine/jasmine-jquery.js',
                testSrcPath + 'unit/jasmine-config.js',

                // unit-tests
                testSrcPath + 'unit/yaio-explorerapp/**/*_test.js',

                // fixtures
                {
                    pattern: testSrcPath + 'unit/fixtures/**/*.html',
                    watched: true,
                    served: true,
                    included: false
                }
            ],


            // list of files to exclude
            exclude: [
            ],


            // preprocess matching files before serving them to the browser
            // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
            preprocessors: {
            },


            // test results reporter to use
            // possible values: 'dots', 'progress'
            // available reporters: https://npmjs.org/browse/keyword/karma-reporter
            reporters: ['story', 'progress'],


            // web server port
            port: 9876,


            // enable / disable colors in the output (reporters and logs)
            colors: true,


            // level of logging
            // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
            logLevel: config.LOG_WARN,


            // enable / disable watching file and executing tests whenever any file changes
            autoWatch: true,


            // start these browsers
            // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
            browsers: ['PhantomJS'], // 'PhantomJS', 'Chrome', 'Firefox', 'IE'


            // Continuous Integration mode
            // if true, Karma captures browsers, runs the tests and exits
            singleRun: false
        });
    };
})();
