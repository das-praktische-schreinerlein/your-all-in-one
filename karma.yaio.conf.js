// Karma configuration
// Generated on Sat Apr 25 2015 10:40:30 GMT+0200 (Mitteleuropäische Sommerzeit)

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
        'src/main/resources/static/yaio-explorerapp/../dist/vendors-full.js',
        // needs own script for loading plugins
        'src/main/resources/static/yaio-explorerapp/../js/fancytree/jquery.fancytree.js',
        'src/main/resources/static/yaio-explorerapp/../js/fancytree/jquery.fancytree.dnd.js',
        'src/main/resources/static/yaio-explorerapp/../js/fancytree/jquery.fancytree.edit.js',
        'src/main/resources/static/yaio-explorerapp/../js/fancytree/jquery.fancytree.gridnav.js',
        'src/main/resources/static/yaio-explorerapp/../js/fancytree/jquery.fancytree.table.js',
        'src/main/resources/static/yaio-explorerapp/../js/ace/ace.js',
        'src/main/resources/static/yaio-explorerapp/../js/ace/ext-spellcheck.js',
        'src/main/resources/static/yaio-explorerapp/../freemind-flash/flashobject.js',
        // yaio: files
        'src/main/resources/static/yaio-explorerapp/../dist/yaio-support-full.js',
        'src/main/resources/static/yaio-explorerapp/../dist/yaio-app-full.js',
        'src/test/static/unit/resources/js/jasmine/jasmine-jquery.js',
        'src/test/static/unit/jasmine-config.js',

        // unit-tests
        'src/test/static/unit/yaio-explorerapp/**/*_test.js',

        // fixtures
        {
            pattern: 'src/test/static/unit/fixtures/**/*.html',
            watched: true,
            served: true,
            included: false
        },
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
