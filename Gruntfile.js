module.exports = function( grunt ){
   // configure tasks
    grunt.initConfig({
        // read package
        pkg: grunt.file.readJSON('package.json'),

        // define files
        vendorJsFiles: [
              'src/main/resources/static/yaio-explorerapp/../js/jquery/jquery.min.js',
              'src/main/resources/static/yaio-explorerapp/../js/jqueryui/jquery-ui.min.js',
              'src/main/resources/static/yaio-explorerapp/../js/jqueryui/jquery.ui-contextmenu.min.js',
              'src/main/resources/static/yaio-explorerapp/../js/jqueryui/jquery-ui-i18n.min.js',
              'src/main/resources/static/yaio-explorerapp/../js/jqueryui/jquery-ui-sliderAccess.js',
              'src/main/resources/static/yaio-explorerapp/../js/jqueryui/jquery-ui-timepicker-addon.js',
              'src/main/resources/static/yaio-explorerapp/../js/jquery/jquery-lang.js',
// loaded standalone because of plugins
//              'src/main/resources/static/yaio-explorerapp/../js/fancytree/jquery.fancytree.js',
//              'src/main/resources/static/yaio-explorerapp/../js/fancytree/jquery.fancytree.dnd.js',
//              'src/main/resources/static/yaio-explorerapp/../js/fancytree/jquery.fancytree.edit.js',
//              'src/main/resources/static/yaio-explorerapp/../js/fancytree/jquery.fancytree.gridnav.js',
//              'src/main/resources/static/yaio-explorerapp/../js/fancytree/jquery.fancytree.table.js',
              'src/main/resources/static/yaio-explorerapp/../js/angularjs/angular.js',
              'src/main/resources/static/yaio-explorerapp/../js/angularjs/angular-animate.js',
              'src/main/resources/static/yaio-explorerapp/../js/angularjs/angular-route.js',
              'src/main/resources/static/yaio-explorerapp/../js/angularjs/angular-translate.js',
              'src/main/resources/static/yaio-explorerapp/../js/angularjs/angular-translate-loader-static-files.js',
              'src/main/resources/static/yaio-explorerapp/../js/marked/marked.js',
// loaded standalone because of plugins
//              'src/main/resources/static/yaio-explorerapp/../js/ace/ace.js',
//              'src/main/resources/static/yaio-explorerapp/../js/ace/ext-spellcheck.js',
              'src/main/resources/static/yaio-explorerapp/../js/strapdown/strapdown-toc.js',
              'src/main/resources/static/yaio-explorerapp/../js/highlightjs/highlight.pack.js',
              'src/main/resources/static/yaio-explorerapp/../js/toastr/toastr.min.js',
              'src/main/resources/static/yaio-explorerapp/../js/mermaid/mermaid.full.js',
              'src/main/resources/static/yaio-explorerapp/../js/findandreplacedomtext/findAndReplaceDOMText.js',
// loaded standalone because of plugins
//              'src/main/resources/static/yaio-explorerapp/../freemind-flash/flashobject.js',
              'src/main/resources/static/yaio-explorerapp/../js/yaio/JMATAllIn.js',
        ],
        vendorCssFiles: [
              'src/main/resources/static/yaio-explorerapp/../css/reset.css',
              'src/main/resources/static/yaio-explorerapp/../css/jqueryui//jquery-ui.css',
              'src/main/resources/static/yaio-explorerapp/../css/jqueryui//jquery-ui-timepicker-addon.css',
// loaded standalone because of plugins
//              'src/main/resources/static/yaio-explorerapp/../js/fancytree/skin-win8/ui.fancytree.css',
              'src/main/resources/static/yaio-explorerapp/../css/highlightjs/default.css',
              'src/main/resources/static/yaio-explorerapp/../css/toastr/toastr.css',
              'src/main/resources/static/yaio-explorerapp/../css/mermaid/mermaid.css',
              'src/main/resources/static/yaio-explorerapp/../css/yaio/style.css',
              'src/main/resources/static/yaio-explorerapp/../css/yaio/main.css',
              'src/main/resources/static/yaio-explorerapp/../css/yaio/print.css',
        ],
        projectJsFiles: [
               'src/main/resources/static/yaio-explorerapp/js/jmat.js',
               'src/main/resources/static/yaio-explorerapp/js/yaio-editorservice.js',
               'src/main/resources/static/yaio-explorerapp/js/yaio-fancytree.js',
               'src/main/resources/static/yaio-explorerapp/js/yaio-explorerapp.js',
               'src/main/resources/static/yaio-explorerapp/../js/angularjs/paging.js',
        ],
        projectSupportJsFiles: [
                 'src/main/resources/static/yaio-explorerapp/js/yaio-baseservice.js',
                 'src/main/resources/static/yaio-explorerapp/js/yaio-layout.js',
                 'src/main/resources/static/yaio-explorerapp/js/yaio-formatter.js',
                 'src/main/resources/static/yaio-explorerapp/js/yaio-markdowneditor.js',
        ],
        projectCssFiles: [
              'src/main/resources/static/yaio-explorerapp/css/style.css',
              'src/main/resources/static/yaio-explorerapp/css/formatter.css'
        ],
        vendorJsTestFiles: [
              'src/test/static/unit/resources/js/jasmine/jasmine-jquery.js',
              'src/test/static/unit/jasmine-config.js',
        ],
        projectUnitJsTestFiles: [
              'src/test/static/unit/yaio-explorerapp/**/*_test.js',
        ],
        projectE2EJsTestFiles: [
              'src/test/static/e2e/yaio-explorerapp/**/*_test.js'
        ],

        // concat files
        concat: {
            options: {
                stripBanners: true,
                banner: '/*! <%= pkg.name %> - v<%= pkg.version %> - <%= grunt.template.today("yyyy-mm-dd") %> */',
            },            
            full: {
                files: {
                    'src/main/resources/static/dist/vendors-full.js': ['<%= vendorJsFiles %>'],
                    'src/main/resources/static/dist/vendors-full.css': ['<%= vendorCssFiles %>'],

                    'src/main/resources/static/dist/<%= pkg.name %>-app-full.js': ['<%= projectJsFiles %>'],
                    'src/main/resources/static/dist/<%= pkg.name %>-app-full.css': ['<%= projectCssFiles %>'],
                    'src/main/resources/static/dist/<%= pkg.name %>-app-<%= pkg.version %>-full.js': ['<%= projectJsFiles %>'],
                    'src/main/resources/static/dist/<%= pkg.name %>-app-<%= pkg.version %>-full.css': ['<%= projectCssFiles %>'],

                    'src/main/resources/static/dist/<%= pkg.name %>-support-full.js': ['<%= projectSupportJsFiles %>'],
                    'src/main/resources/static/dist/<%= pkg.name %>-support-full.css': ['<%= projectCssFiles %>'],
                    'src/main/resources/static/dist/<%= pkg.name %>-support-<%= pkg.version %>-full.js': ['<%= projectSupportJsFiles %>'],
                    'src/main/resources/static/dist/<%= pkg.name %>-support-<%= pkg.version %>-full.css': ['<%= projectCssFiles %>'],

                    'src/test/static/testsupport-full.js': ['<%= vendorJsTestFiles %>'],
                    'src/test/static/<%= pkg.name %>_tests-full.js': ['<%= projectUnitJsTestFiles %>', '<%= projectE2EJsTestFiles %>']
                },
            },
        },   

        // jshint: look at https://github.com/gruntjs/grunt-contrib-jshint
        jshint: {
            files: [
                'GruntFile.js',
                '<%= projectJsFiles %>',
                '<%= projectJsTestFiles %>'
            ],
            options: {
                ignores: [
                    'src/main/resources/static/yaio-explorerapp/../js/**/*'
                ],
                curly: true,
                eqeqeq: true,
                eqnull: true,
                browser: true,
                globals: {
                    jQuery: true,
                    $: true
                },
                // ignore errors
                '-W014': true, // [W014] Bad line breaking before '+'
                '-W033': true // [W033] Missing semicolon
            }
        },
        // karma
        karma: {
            options: {
                configFile: 'karma.yaio.conf.js',
                noColor: false
            },
            // testmodus: unit only
            unit: {
                singleRun: true
            },
            // testmodus: continous
            continuous: {
                // keep karma running in the background
                background: true
            }
        },
        // protractor
        protractor: {
            options: {
                // Location of your protractor config file
                configFile: "protractor.yaio.conf.js",
           
                // Do you want the output to use fun colors?
                noColor: false,
           
                // Set to true if you would like to use the Protractor command line debugging tool
                // debug: true,
           
                // Additional arguments that are passed to the webdriver command
                args: { }
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
        },
        // watch
        watch: {
            options: {
                livereload: true
            },
            karma: {
                // run when projectJsFiles or tests changed
                files: ['<%= projectJsFiles %>', 'src/test/static/unit/yaio-explorerapp/**/*'],
                tasks: ['karma:continuous:run']
            },
            protractor: {
                // run when any projectfiles or tests changed
                files: ['src/main/resources/static/yaio-explorerapp/**/*.*', 'src/test/static/e2e/yaio-explorerapp/**/*'],
                tasks: ['protractor:continuous']
            }
        }
    });

    // register tasks
    grunt.registerTask('default',   ['jshint']);
    grunt.registerTask('dist',      ['concat:full']);
    grunt.registerTask('unit-test', ['karma:continuous:start', 'watch:karma']);
    grunt.registerTask('e2e-test',  ['protractor:continuous', 'watch:protractor']);

    // load grunt tasks
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-jsdoc');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-protractor-runner');
//  grunt.loadNpmTasks('grunt-contrib-clean');
//  grunt.loadNpmTasks('grunt-contrib-compass');
//  grunt.loadNpmTasks('grunt-contrib-copy');
//  grunt.loadNpmTasks('grunt-karma-sonar');
};