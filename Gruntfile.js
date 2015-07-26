var path = require('path');

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
              'src/main/resources/static/yaio-explorerapp/../freemind-flash/flashobject.js',
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
              'src/main/resources/static/yaio-explorerapp/../css/yaio/main.css'
        ],
        projectJsFiles: [
            // services
            'src/main/resources/static/yaio-explorerapp/js/datarenderer/*.js',
            'src/main/resources/static/yaio-explorerapp/js/dataservices/*.js',
            'src/main/resources/static/yaio-explorerapp/js/explorer/ExplorerActionService.js',
            'src/main/resources/static/yaio-explorerapp/js/explorer/ExplorerTreeService.js',
            // angular
            'src/main/resources/static/yaio-explorerapp/js/YaioApp.js',
            'src/main/resources/static/yaio-explorerapp/js/utils/Constants.js',
            'src/main/resources/static/yaio-explorerapp/js/utils/Directives.js',
            'src/main/resources/static/yaio-explorerapp/js/utils/FormErrorHandlingUtils.js',
            'src/main/resources/static/yaio-explorerapp/js/utils/YaioUtilsFactory.js',
            'src/main/resources/static/yaio-explorerapp/js/auth/AuthFactory.js',
            'src/main/resources/static/yaio-explorerapp/js/auth/AuthController.js',
            'src/main/resources/static/yaio-explorerapp/js/lang/LanguageConfig.js',
            'src/main/resources/static/yaio-explorerapp/js/lang/LanguageCtrl.js',
            'src/main/resources/static/yaio-explorerapp/js/frontpage/FrontpageCtrl.js',
            'src/main/resources/static/yaio-explorerapp/js/editor/NodeEditorCtrl.js',
            'src/main/resources/static/yaio-explorerapp/js/importer/ImporterCtrl.js',
            'src/main/resources/static/yaio-explorerapp/js/exporter/OutputOptionsCtrl.js',
            'src/main/resources/static/yaio-explorerapp/js/exporter/OutputOptionsEditorFactory.js',
            'src/main/resources/static/yaio-explorerapp/js/explorer/NodeShowCtrl.js',
            'src/main/resources/static/yaio-explorerapp/js/search/NodeSearchCtrl.js',
            'src/main/resources/static/yaio-explorerapp/../js/angularjs/paging.js'
        ],
        projectSupportJsFiles: [
            'src/main/resources/static/yaio-explorerapp/js/jmat.js',
            'src/main/resources/static/yaio-explorerapp/js/jshelferlein/JsHelferlein.js',
            'src/main/resources/static/yaio-explorerapp/js/jshelferlein/**/*.js',
            'src/main/resources/static/yaio-explorerapp/js/YaioAppBaseConfig.js',
            'src/main/resources/static/yaio-explorerapp/js/YaioAppBase.js',
            'src/main/resources/static/yaio-explorerapp/js/utils/BaseService.js',
            'src/main/resources/static/yaio-explorerapp/js/editor/EditorService.js',
            'src/main/resources/static/yaio-explorerapp/js/layout/LayoutService.js',
            'src/main/resources/static/yaio-explorerapp/js/wysiwyg/FormatterService.js',
            'src/main/resources/static/yaio-explorerapp/js/wysiwyg/MarkdownEditorService.js'
        ],
        projectExportsJsFiles: [
            'src/main/resources/static/yaio-explorerapp/js/utils/ExportedDataService.js',
        ],
        projectCssFiles: [
              'src/main/resources/static/yaio-explorerapp/css/style.css',
              'src/main/resources/static/yaio-explorerapp/css/formatter.css'
        ],
        projectSupportCssFiles: [
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
        
        bower: {
            install: {
              options: {
                    targetDir: './bower/_dest',
                    layout: //'byType', // 'byComponent'
                        function(type, component, source) {
                            // map type
                            var extractedType = source.replace(/.*\.(.*)?/, "$1");
                            var renamedType = "js";
                            if (extractedType == 'js') renamedType = 'js';
                            else if (extractedType == 'css') renamedType = 'css';

                            // map compontent
                            if (component.search('fancytree') >= 0) {
                                // copy all fancytree to js
                                renamedType = "js";
                            } else if (component.search('ace-builds') >= 0) {
                                // map ace
                                component = "ace";
                            } else if (component.search('jquery-ui') >= 0) {
                                // map jqueryui
                                component = "jqueryui";
                            } else if (component.search('jqueryui') >= 0) {
                                // map jqueryui
                                component = "jqueryui";
                            } else if (component.search('ui-contextmenu') >= 0) {
                                // map jqueryui
                                component = "jqueryui";
                            } else if (component.search('jquery-lang') >= 0) {
                                // map jquery
                                component = "jquery";
                            } else if (component.search('angular') >= 0) {
                                // map angularjs
                                component = "angularjs";
                            }
                            return path.join(renamedType, component);
                        },
                    install: true,
                    verbose: true,
                    cleanTargetDir: true,
                    cleanBowerDir: false,
                    bowerOptions: {}
                }
            }
        },
        
        copy: {
            main: {
                files: [
                    // includes files within path
                    {expand: true, cwd: 'bower/_src/angular', src: ['*.js'], dest: 'vendors/js/angularjs/', flatten: false},
                    {expand: true, cwd: 'bower/_src/angular-translate', src: ['*.js'], dest: 'vendors/js/angularjs/', flatten: false},
                    {expand: true, cwd: 'bower/_src/jquery/dist', src: ['*.js'], dest: 'vendors/js/jquery/', flatten: false},
                    {expand: true, cwd: 'bower/_src/jquery-lang-js/js', src: ['*.js'], dest: 'vendors/js/jquery/', flatten: false},
                    {expand: true, cwd: 'bower/_src/jquery-ui', src: ['**/jquery-ui.min.js', '**/jquery-ui-i18n.min.js'], dest: 'vendors/js/jqueryui/', flatten: true},
                    {expand: true, cwd: 'bower/_src/jquery-lang', src: ['js/*.js'], dest: 'vendors/js/jqueryui/', flatten: true},
                    {expand: true, cwd: 'bower/_src/jqueryui-timepicker-addon', src: ['dist/*.js'], dest: 'vendors/js/jqueryui/', flatten: true},
                    {expand: true, cwd: 'bower/_src/ui-contextmenu', src: ['**/*.js'], dest: 'vendors/js/jqueryui/', flatten: true},
                    {expand: true, cwd: 'bower/_src/fancytree/dist/', src: ['skin*', '*.js'], dest: 'vendors/js/fancytree/', flatten: false},
                    {expand: true, cwd: 'bower/_src/fancytree/dist/', src: ['src/*.js'], dest: 'vendors/js/fancytree/', flatten: true},
                    {expand: true, cwd: 'bower/_src/ace-builds/src-min', src: ['**'], dest: 'vendors/js/ace/', flatten: false},
                ],
            },
        },

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
                    'src/main/resources/static/dist/<%= pkg.name %>-support-full.css': ['<%= projectSupportCssFiles %>'],
                    'src/main/resources/static/dist/<%= pkg.name %>-support-<%= pkg.version %>-full.js': ['<%= projectSupportJsFiles %>'],
                    'src/main/resources/static/dist/<%= pkg.name %>-support-<%= pkg.version %>-full.css': ['<%= projectSupportCssFiles %>'],

                    'src/main/resources/static/dist/<%= pkg.name %>-exports-full.js': ['<%= projectSupportJsFiles %>', '<%= projectExportsJsFiles %>'],
                    'src/main/resources/static/dist/<%= pkg.name %>-exports-full.css': ['<%= projectSupportCssFiles %>'],
                    'src/main/resources/static/dist/<%= pkg.name %>-exports-<%= pkg.version %>-full.js': ['<%= projectSupportJsFiles %>', '<%= projectExportsJsFiles %>'],
                    'src/main/resources/static/dist/<%= pkg.name %>-exports-<%= pkg.version %>-full.css': ['<%= projectSupportCssFiles %>'],

//                    'src/test/static/testsupport-full.js': ['<%= vendorJsTestFiles %>'],
//                    'src/test/static/<%= pkg.name %>_tests-full.js': ['<%= projectUnitJsTestFiles %>', '<%= projectE2EJsTestFiles %>']
                },
            },
        },   

        // jshint: look at https://github.com/gruntjs/grunt-contrib-jshint
        jshint: {
            files: [
                'GruntFile.js',
                'src/main/resources/static/yaio-explorerapp/js/**/*.js'
            ],
            options: {
                jshintrc: true
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
        },
        // watch
        watch: {
            options: {
                livereload: true
            },
            dist: {
                // run when any projectfiles or tests changed
                files: ['src/main/resources/static/yaio-explorerapp/**/*.*', 'src/test/static/e2e/**/*', 'src/test/static/unit/**/*'],
                tasks: ['dist']
            },
            karma: {
                // run when projectJsFiles or tests changed
                files: ['src/main/resources/static/yaio-explorerapp/**/*.*', 'src/test/static/unit/**/*'],
                tasks: ['dist', 'karma:continuous:run']
            },
            protractor: {
                // run when any projectfiles or tests changed
                files: ['src/main/resources/static/yaio-explorerapp/**/*.*', 'src/test/static/e2e/**/*'],
                tasks: ['dist', 'protractor:continuous']
            },
            tests: {
                // run when any projectfiles or tests changed
                files: ['src/main/resources/static/yaio-explorerapp/**/*.*', 'src/test/static/e2e/**/*', 'src/test/static/unit/**/*'],
                tasks: ['dist', 'karma:unit', 'protractor:e2e']
            }
        },
        wiredep: {

            task: {

              // Point to the files that should be updated when
              // you run `grunt wiredep`
              src: [
                'src/main/resources/static/html/**/*.html',
                'src/main/resources/static/yaio-explorerapp/*.html',
//                'app/styles/main.scss',  // .scss & .sass support...
//                'app/config.yml'         // and .yml & .yaml support out of the box!
              ],

              options: {
                // See wiredep's configuration documentation for the options
                // you may pass:

                // https://github.com/taptapship/wiredep#configuration
              }
            }
          }
    });

    // register tasks
    grunt.registerTask('vendors',   ['bower', 'copy']);
    grunt.registerTask('default',   ['jshint']);
    grunt.registerTask('dist',      ['concat:full']);
    grunt.registerTask('unit-test', ['dist', 'karma:continuous:start', 'watch:karma']);
    grunt.registerTask('e2e-test',  ['dist', 'protractor:continuous', 'watch:protractor']);

    // load grunt tasks
    grunt.loadNpmTasks('grunt-bower-task');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-jsdoc');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-protractor-runner');
    grunt.loadNpmTasks('grunt-wiredep');
//  grunt.loadNpmTasks('grunt-contrib-clean');
//  grunt.loadNpmTasks('grunt-contrib-compass');
//  grunt.loadNpmTasks('grunt-contrib-copy');
//  grunt.loadNpmTasks('grunt-karma-sonar');
};