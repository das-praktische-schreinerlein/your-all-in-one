var path = require('path');

var srcBase = 'src/main/resources/static/';
var destBase = 'src/main/resources/static/';
var bowerSrcBase = 'bower/_src/';
var vendorDestBase = 'vendors/';
var testSrcBase = 'src/test/static/';

function patchFileSlimbox2(content, srcpath) {
    var newContent = content;
    console.log("patchFileSlimbox2:" + srcpath);
    newContent = newContent.replace(/\t/g,"    ");
    newContent = newContent.replace("middle = win.scrollTop() + (win.height() / 2);", "middle = win.scrollTop() + (window.innerHeight / 2);");
    return newContent;
}
function patchFileFancytree(content, srcpath) {
    var newContent = content;
    console.log("patchFileFancytree:" + srcpath);
    newContent = newContent.replace(/@version .*?\n/, "@version @VERSION\n");
    newContent = newContent.replace(/@date .*?\n/, "@date @DATE\n");
    return newContent;
}
function patchFileJQueryLang(content, srcpath) {
    var newContent = content;
    console.log("patchFileJQueryLang:" + srcpath);
    newContent = newContent.replace(/'placeholder'\n\t*];/, "'placeholder', 'data-tooltip'\n\t\t];");
    return newContent;
}



module.exports = function( grunt ){
   // configure tasks
    grunt.initConfig({
        // read package
        pkg: grunt.file.readJSON('package.json'),
        destBase: destBase,

        // define files
        vendorJsFiles: [
              srcBase + 'js/jquery/jquery.min.js',
              srcBase + 'js/jqueryui/jquery-ui.min.js',
              srcBase + 'js/jqueryui/jquery.ui-contextmenu.min.js',
              srcBase + 'js/jqueryui/jquery-ui-i18n.min.js',
              srcBase + 'js/jqueryui/jquery-ui-sliderAccess.js',
              srcBase + 'js/jqueryui/jquery-ui-timepicker-addon.js',
              srcBase + 'js/jquery/jquery-lang.js',
// loaded standalone because of plugins
//              srcBase + 'js/fancytree/jquery.fancytree.js',
//              srcBase + 'js/fancytree/jquery.fancytree.dnd.js',
//              srcBase + 'js/fancytree/jquery.fancytree.edit.js',
//              srcBase + 'js/fancytree/jquery.fancytree.gridnav.js',
//              srcBase + 'js/fancytree/jquery.fancytree.table.js',
              srcBase + 'js/angularjs/angular.js',
              srcBase + 'js/angularjs/angular-animate.js',
              srcBase + 'js/angularjs/angular-route.js',
              srcBase + 'js/angularjs/angular-translate.js',
              srcBase + 'js/angularjs/angular-translate-loader-static-files.js',
              srcBase + 'js/marked/marked.js',
// loaded standalone because of plugins
//              srcBase + 'js/ace/ace.js',
//              srcBase + 'js/ace/ext-spellcheck.js',
              srcBase + 'js/strapdown/strapdown-toc.js',
              srcBase + 'js/highlightjs/highlight.pack.js',
              srcBase + 'js/toastr/toastr.min.js',
              srcBase + 'js/mermaid/mermaid.full.js',
              srcBase + 'js/findandreplacedomtext/findAndReplaceDOMText.js',
// loaded standalone because of plugins
              srcBase + 'freemind-flash/flashobject.js',
              srcBase + 'js/yaio/JMATAllIn.js',
        ],
        vendorCssFiles: [
              srcBase + 'css/reset.css',
              srcBase + 'css/jqueryui//jquery-ui.css',
              srcBase + 'css/jqueryui//jquery-ui-timepicker-addon.css',
// loaded standalone because of plugins
//              srcBase + 'js/fancytree/skin-win8/ui.fancytree.css',
              srcBase + 'css/highlightjs/default.css',
              srcBase + 'css/toastr/toastr.css',
              srcBase + 'css/mermaid/mermaid.css',
              srcBase + 'css/yaio/style.css',
              srcBase + 'css/yaio/main.css'
        ],
        projectJsFiles: [
            // services
            srcBase + 'yaio-explorerapp/js/datarenderer/*.js',
            srcBase + 'yaio-explorerapp/js/dataservices/*.js',
            srcBase + 'yaio-explorerapp/js/explorer/ExplorerActionService.js',
            srcBase + 'yaio-explorerapp/js/explorer/ExplorerTreeService.js',
            // angular
            srcBase + 'yaio-explorerapp/js/YaioApp.js',
            srcBase + 'yaio-explorerapp/js/utils/Constants.js',
            srcBase + 'yaio-explorerapp/js/utils/Directives.js',
            srcBase + 'yaio-explorerapp/js/utils/FormErrorHandlingUtils.js',
            srcBase + 'yaio-explorerapp/js/utils/YaioUtilsFactory.js',
            srcBase + 'yaio-explorerapp/js/auth/AuthFactory.js',
            srcBase + 'yaio-explorerapp/js/auth/AuthController.js',
            srcBase + 'yaio-explorerapp/js/lang/LanguageConfig.js',
            srcBase + 'yaio-explorerapp/js/lang/LanguageCtrl.js',
            srcBase + 'yaio-explorerapp/js/frontpage/FrontpageCtrl.js',
            srcBase + 'yaio-explorerapp/js/editor/NodeEditorCtrl.js',
            srcBase + 'yaio-explorerapp/js/importer/ImporterCtrl.js',
            srcBase + 'yaio-explorerapp/js/exporter/OutputOptionsCtrl.js',
            srcBase + 'yaio-explorerapp/js/exporter/OutputOptionsEditorFactory.js',
            srcBase + 'yaio-explorerapp/js/explorer/NodeShowCtrl.js',
            srcBase + 'yaio-explorerapp/js/search/NodeSearchCtrl.js',
            srcBase + 'js/angularjs/paging.js'
        ],
        projectSupportJsFiles: [
            srcBase + 'yaio-explorerapp/js/jmat.js',
            srcBase + 'yaio-explorerapp/js/jshelferlein/JsHelferlein.js',
            srcBase + 'yaio-explorerapp/js/jshelferlein/**/*.js',
            srcBase + 'yaio-explorerapp/js/YaioAppBaseConfig.js',
            srcBase + 'yaio-explorerapp/js/YaioAppBase.js',
            srcBase + 'yaio-explorerapp/js/utils/BaseService.js',
            srcBase + 'yaio-explorerapp/js/editor/EditorService.js',
            srcBase + 'yaio-explorerapp/js/layout/LayoutService.js',
            srcBase + 'yaio-explorerapp/js/wysiwyg/FormatterService.js',
            srcBase + 'yaio-explorerapp/js/wysiwyg/MarkdownEditorService.js'
        ],
        projectExportsJsFiles: [
            srcBase + 'yaio-explorerapp/js/utils/ExportedDataService.js',
        ],
        projectCssFiles: [
              srcBase + 'yaio-explorerapp/css/style.css',
              srcBase + 'yaio-explorerapp/js/layout/base.css',
              srcBase + 'yaio-explorerapp/js/layout/support.css',
              srcBase + 'yaio-explorerapp/js/layout/toc.css',
              srcBase + 'yaio-explorerapp/js/layout/yaio.css',
              srcBase + 'yaio-explorerapp/js/explorer/explorer.css',
              srcBase + 'yaio-explorerapp/js/datarenderer/gantt.css',
              srcBase + 'yaio-explorerapp/js/datarenderer/data.css',
              srcBase + 'yaio-explorerapp/js/search/search.css',
              srcBase + 'yaio-explorerapp/js/editor/editor.css',
              srcBase + 'yaio-explorerapp/js/exporter/exporter.css',
              srcBase + 'yaio-explorerapp/js/lang/lang.css',
              srcBase + 'yaio-explorerapp/js/auth/auth.css',
              srcBase + 'yaio-explorerapp/js/frontpage/frontpage.css',
              srcBase + 'yaio-explorerapp/js/wysiwyg/formatter.css'
        ],
        projectSupportCssFiles: [
            srcBase + 'yaio-explorerapp/js/layout/base.css',
            srcBase + 'yaio-explorerapp/js/layout/support.css',
            srcBase + 'yaio-explorerapp/js/layout/toc.css',
            srcBase + 'yaio-explorerapp/js/wysiwyg/formatter.css'
        ],
        projectPrintCssFiles: [
            srcBase + 'yaio-explorerapp/js/layout/print.css',
        ],
        projectPrintDataOnlyCssFiles: [
            srcBase + 'yaio-explorerapp/js/layout/print-dataonly.css',
        ],
        projectResetCssFiles: [
            srcBase + 'yaio-explorerapp/js/layout/reset.css',
        ],
        vendorJsTestFiles: [
            testSrcBase + 'unit/resources/js/jasmine/jasmine-jquery.js',
            testSrcBase + 'unit/jasmine-config.js',
        ],
        projectUnitJsTestFiles: [
            testSrcBase + 'unit/yaio-explorerapp/**/*_test.js',
        ],
        projectE2EJsTestFiles: [
            testSrcBase + 'e2e/yaio-explorerapp/**/*_test.js'
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
        
        clean: {
            bower: ["bower/_dest", "vendors"],
            dist:  ["src/main/resources/static/dist"]
        },
        copy: {
            bower2vendors: {
                options: {
                    process: function (content, srcpath) {
                        if (srcpath.search('slimbox2.') > 0) {
                            return patchFileSlimbox2(content, srcpath);
                        } else if (srcpath.search('fancytree') > 0) {
                            return patchFileFancytree(content, srcpath);
                        } else if (srcpath.search('jquery-lang') > 0) {
                            return patchFileJQueryLang(content, srcpath);
                        }
                        return content;
                    }
                },
                files: [
                    //"ace-builds": "v1.1.8", // OK
                    //"angular": "1.2.23", // OK
                    //"angular-translate": "2.4.0", // OK
                    //"fancytree": "2.4.0", // ToDo Patch
                    //"find-and-replace-dom-text": "0.4.3",  // OK
                    //"highlightjs": "8.4.0",  // OK
                    //"jquery": "1.11.1", //OK
                    //"jquery-lang-js": "#be9519db371f0f3131db13b357b9e54f2629df31 == 2.4.0", // OK
                    //"jquery-ui": "1.10.4", // OK
                    //"jqueryui-timepicker-addon": "1.4.5", // OK
                    //"marked": "0.3.3", // TODO Patch
                    //"mermaid": "0.4.0", // TODO Patch
                    //"slimbox2": "cbeyls/slimbox#2.05", // Done Patch
                    //"strapdown": "ndossougbe/strapdown#0.4.1", // ToDo Patch
                    //"toastr": "CodeSeven/toastr#2.1.0", // OK
                    //"ui-contextmenu": "1.7.0" // OK

                    // JS
                    {expand: true, cwd: bowerSrcBase + 'ace-builds/src-min-noconflict', src: ['**'], dest: 'vendors/js/ace/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'angular', src: ['*.js'], dest: vendorDestBase + 'js/angularjs/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'angular-translate', src: ['*.js'], dest: vendorDestBase + 'js/angularjs/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'fancytree/dist/', src: ['skin*/*.*', '*.js'], dest: vendorDestBase + 'js/fancytree/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'fancytree/dist/', src: ['src/*.js'], dest: vendorDestBase + 'js/fancytree/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'find-and-replace-dom-text/src', src: ['findAndReplaceDOMText.js'], dest: vendorDestBase + 'js/findandreplacedomtext/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'highlightjs/', src: ['**/highlight.pack.js'], dest: vendorDestBase + 'js/highlightjs/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'jquery/dist', src: ['*.js'], dest: vendorDestBase + 'js/jquery/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'jquery-lang-js/js', src: ['*.js'], dest: vendorDestBase + 'js/jquery/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'jquery-ui', src: ['**/jquery-ui.min.js', '**/jquery-ui-i18n.min.js'], dest: vendorDestBase + 'js/jqueryui/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'jqueryui-timepicker-addon', src: ['dist/*.js'], dest: vendorDestBase + 'js/jqueryui/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'marked', src: ['lib/marked.js'], dest: vendorDestBase + 'js/marked/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'mermaid', src: ['dist/mermaid.full.js'], dest: vendorDestBase + 'js/mermaid/', flatten: true, filter: 'isFile'
                        ,rename: function(dest, src) {
                            return dest + src.replace('-legacy.full.js','.full.js');
                          }
                    },
                    {expand: true, cwd: bowerSrcBase + 'slimbox2', src: ['js/slimbox2.js'], dest: vendorDestBase + 'js/slimbox2/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'strapdown', src: ['src/js/strapdown-toc.js'], dest: vendorDestBase + 'js/strapdown/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'toastr', src: ['build/toastr.min.js'], dest: vendorDestBase + 'js/toastr/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'ui-contextmenu', src: ['**/*.js'], dest: vendorDestBase + 'js/jqueryui/', flatten: true},
                    // CSS
                    {expand: true, cwd: bowerSrcBase + 'highlightjs/', src: ['**/default.css'], dest: vendorDestBase + 'css/highlightjs/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'jquery-ui', src: ['**/jquery-ui.css'], dest: vendorDestBase + 'css/jqueryui/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'jqueryui-timepicker-addon', src: ['dist/jquery-ui-timepicker-addon.css'], dest: vendorDestBase + 'css/jqueryui/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'mermaid', src: ['dist/mermaid.css'], dest: vendorDestBase + 'css/mermaid/', flatten: true, filter: 'isFile'},
                    {expand: true, cwd: bowerSrcBase + 'slimbox2', src: ['**/slimbox2.css'], dest: vendorDestBase + 'css/slimbox2/', flatten: true, filter: 'isFile'},
                    {expand: true, cwd: bowerSrcBase + 'toastr', src: ['toastr.css'], dest: vendorDestBase + 'css/toastr/', flatten: true, filter: 'isFile'}
                ],
            },
            vendors2dist: {
                files: [
                    {expand: true, cwd: srcBase + 'js/', src: ['fancytree/**'], dest: destBase + 'dist/vendors.<%= pkg.vendorversion %>/', flatten: false},
                    {expand: true, cwd: srcBase + 'js/', src: ['ace/**'], dest: destBase + 'dist/vendors.<%= pkg.vendorversion %>/', flatten: false},
                    {expand: true, cwd: srcBase + '', src: ['freemind-flash/**'], dest: destBase + 'dist/vendors.<%= pkg.vendorversion %>/', flatten: false},
                    {expand: true, cwd: srcBase + 'js/', src: ['yaio/**'], dest: destBase + 'dist/vendors.<%= pkg.vendorversion %>/', flatten: false},
                    {expand: true, cwd: srcBase + 'js/', src: ['slimbox2/**'], dest: destBase + 'dist/vendors.<%= pkg.vendorversion %>/', flatten: false},
                    {expand: true, cwd: srcBase + 'css/', src: ['slimbox2/**'], dest: destBase + 'dist/vendors.<%= pkg.vendorversion %>/', flatten: false},
                    {expand: true, cwd: srcBase + 'css/', src: ['yaio/**'], dest: destBase + 'dist/vendors.<%= pkg.vendorversion %>/', flatten: false}
                ]
            }
        },

        // concat files
        concat: {
            options: {
                stripBanners: true,
                banner: '/*! <%= pkg.name %> - v<%= pkg.version %> - <%= grunt.template.today("yyyy-mm-dd") %> */',
            },            
            full: {
                files: {
                    '<%= destBase %>dist/vendors-full.<%= pkg.vendorversion %>.js': ['<%= vendorJsFiles %>'],
                    '<%= destBase %>dist/vendors-full.<%= pkg.vendorversion %>.css': ['<%= vendorCssFiles %>'],

                    '<%= destBase %>dist/<%= pkg.name %>-reset.<%= pkg.resetversion %>.css': ['<%= projectResetCssFiles %>'],

                    '<%= destBase %>dist/<%= pkg.name %>-app-full.<%= pkg.appversion %>.js': ['<%= projectJsFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-app-full.<%= pkg.appversion %>.css': ['<%= projectCssFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-app-print.<%= pkg.appversion %>.css': ['<%= projectPrintCssFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-app-print-dataonly.<%= pkg.appversion %>.css': ['<%= projectPrintDataOnlyCssFiles %>'],

                    '<%= destBase %>dist/<%= pkg.name %>-support-full.<%= pkg.supportversion %>.js': ['<%= projectSupportJsFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-support-full.<%= pkg.supportversion %>.css': ['<%= projectSupportCssFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-support-print.<%= pkg.supportversion %>.css': ['<%= projectPrintCssFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-support-print-dataonly.<%= pkg.supportversion %>.css': ['<%= projectPrintDataOnlyCssFiles %>'],

                    '<%= destBase %>dist/<%= pkg.name %>-exports-full.<%= pkg.exportsversion %>.js': ['<%= projectSupportJsFiles %>', '<%= projectExportsJsFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-exports-full.<%= pkg.exportsversion %>.css': ['<%= projectSupportCssFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-exports-print.<%= pkg.exportsversion %>.css': ['<%= projectPrintCssFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-exports-print-dataonly.<%= pkg.exportsversion %>.css': ['<%= projectPrintDataOnlyCssFiles %>']

//                    testSrcBase + 'testsupport-full.js': ['<%= vendorJsTestFiles %>'],
//                    testSrcBase + '<%= pkg.name %>_tests-full.js': ['<%= projectUnitJsTestFiles %>', '<%= projectE2EJsTestFiles %>']
                },
            },
        },   

        // jshint: look at https://github.com/gruntjs/grunt-contrib-jshint
        jshint: {
            files: [
                'GruntFile.js',
                srcBase + 'yaio-explorerapp/js/**/*.js'
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
                files: [srcBase + 'yaio-explorerapp/**/*.*', testSrcBase + 'e2e/**/*', testSrcBase + 'unit/**/*'],
                tasks: ['dist']
            },
            karma: {
                // run when projectJsFiles or tests changed
                files: [srcBase + 'yaio-explorerapp/**/*.*', testSrcBase + 'unit/**/*'],
                tasks: ['dist', 'karma:continuous:run']
            },
            protractor: {
                // run when any projectfiles or tests changed
                files: [srcBase + 'yaio-explorerapp/**/*.*', testSrcBase + 'e2e/**/*'],
                tasks: ['dist', 'protractor:continuous']
            },
            tests: {
                // run when any projectfiles or tests changed
                files: [srcBase + 'yaio-explorerapp/**/*.*', testSrcBase + 'e2e/**/*', 'src/test/static/unit/**/*'],
                tasks: ['dist', 'karma:unit', 'protractor:e2e']
            }
        },
        wiredep: {

            task: {

              // Point to the files that should be updated when
              // you run `grunt wiredep`
              src: [
                srcBase + 'html/**/*.html',
                srcBase + 'yaio-explorerapp/*.html',
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
    grunt.registerTask('default',   ['jshint']);
    grunt.registerTask('vendors',   ['clean:bower', 'bower', 'copy:bower2vendors']);
    grunt.registerTask('dist',      ['vendors', 'clean:dist', 'concat:full', 'copy:vendors2dist']);
    grunt.registerTask('unit-test', ['dist', 'karma:continuous:start', 'watch:karma']);
    grunt.registerTask('e2e-test',  ['dist', 'protractor:continuous', 'watch:protractor']);

    // load grunt tasks
    grunt.loadNpmTasks('grunt-bower-task');
    grunt.loadNpmTasks('grunt-contrib-clean');
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