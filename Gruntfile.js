/**
 * <h4>FeatureDomain:</h4>
 *     Collaboration
 *
 * <h4>FeatureDescription:</h4>
 *     software for projectmanagement and documentation
 * 
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     taskconfiguration
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
var path = require('path');

/**
 * baseconfig
 **/
var srcBase = 'src/main/web/';
var tplSrcBase = 'src/main/web/';
var destBase = 'src/main/generated-resources/static/';
var bowerSrcBase = 'bower/_src/';
var vendorSrcBase = 'vendors/src/';
var vendorDestBase = 'vendors/';
var archivSrcBase = 'vendors/archiv/';
var testSrcBase = 'src/test/javascript/';

/**
 * patches
 **/
function checkWebResOnly(srcpath) {
    if (srcpath.search(/\.js|\.css|\.html/) < 0) {
        return false;
    }
    return true;
}
function patchFileSlimbox2(content, srcpath) {
    if (! checkWebResOnly(srcpath)) {
        return content;
    }
    var newContent = content;
    console.log("patchFileSlimbox2:" + srcpath);
    newContent = newContent.replace(/\/\*\!/g,
                                    "    /*!");
    newContent = newContent.replace(/\t/g,
                                    "    ");
    newContent = newContent.replace("middle = win.scrollTop() + (win.height() / 2);",
                                    "middle = win.scrollTop() + (window.innerHeight / 2);");
    return newContent;
}
function patchFileFancytree(content, srcpath) {
    if (! checkWebResOnly(srcpath)) {
        return content;
    }
    var newContent = content;
    console.log("patchFileFancytree:" + srcpath);
    newContent = newContent.replace(/@version .*?\n/g, 
                                    "@version @VERSION\n");
    newContent = newContent.replace(/@date .*?\n/g,
                                    "@date @DATE\n");
    
    return newContent;
}
function patchFileJQuery(content, srcpath) {
    if (! checkWebResOnly(srcpath)) {
        return content;
    }
    var newContent = content;
    console.log("patchFileJQuery:" + srcpath);
    newContent = newContent.replace(/\/\/# sourceMappingURL=jquery.min.map/g,
                                    "");
    return newContent;
}
function patchFileJQueryUi(content, srcpath) {
    if (! checkWebResOnly(srcpath)) {
        return content;
    }
    var newContent = content;
    console.log("patchFileJQueryUi:" + srcpath);
    newContent = newContent.replace(/url\(images\//g,
                                    "url(vendors-vendorversion/jqueryui/images/");
    return newContent;
}
function patchFileJQueryLang(content, srcpath) {
    if (! checkWebResOnly(srcpath)) {
        return content;
    }
    var newContent = content;
    console.log("patchFileJQueryLang:" + srcpath);
    newContent = newContent.replace(/'placeholder'\n\t*];/g,
                                    "'placeholder',\n\t\t'data-tooltip'\n\t];");
    return newContent;
}
function patchFileHighlightJsLang(content, srcpath) {
    if (! checkWebResOnly(srcpath)) {
        return content;
    }
    var newContent = content;
    console.log("patchFileHighlightJsLang:" + srcpath);
    newContent = newContent.replace(/\.hljs-annotation,\n.diff .hljs-header,/g,
                                     ".hljs-annotation,\n.hljs-template_comment,\n.diff .hljs-header,");
    return newContent;
}



/**
 * configure tasks
 **/
module.exports = function( grunt ){
   // configure tasks
    grunt.initConfig({
        // read package
        pkg: grunt.file.readJSON('package.json'),
        destBase: destBase,

        // define files
        vendorJsFiles: [
              vendorDestBase + 'js/jquery/jquery.min.js',
              vendorDestBase + 'js/jqueryui/jquery-ui.min.js',
              vendorDestBase + 'js/jqueryui/jquery.ui-contextmenu.min.js',
              vendorDestBase + 'js/jqueryui/jquery-ui-i18n.min.js',
              vendorDestBase + 'js/jqueryui/jquery-ui-sliderAccess.js',
              vendorDestBase + 'js/jqueryui/jquery-ui-timepicker-addon.js',
              vendorDestBase + 'js/jquery/jquery-lang.js',
// loaded standalone because of plugins
//              vendorDestBase + 'js/fancytree/jquery.fancytree.js',
//              vendorDestBase + 'js/fancytree/jquery.fancytree.dnd.js',
//              vendorDestBase + 'js/fancytree/jquery.fancytree.edit.js',
//              vendorDestBase + 'js/fancytree/jquery.fancytree.gridnav.js',
//              vendorDestBase + 'js/fancytree/jquery.fancytree.table.js',
              vendorDestBase + 'js/angularjs/angular.js',
              vendorDestBase + 'js/angularjs/angular-animate.js',
              vendorDestBase + 'js/angularjs/angular-route.js',
              vendorDestBase + 'js/angularjs/angular-translate.js',
              vendorDestBase + 'js/angularjs/angular-translate-loader-static-files.js',
              vendorDestBase + 'js/angularjs/update-meta.js',
              vendorDestBase + 'js/marked/marked.js',
// loaded standalone because of plugins
//              vendorDestBase + 'js/ace/ace.js',
//              vendorDestBase + 'js/ace/ext-spellcheck.js',
              vendorDestBase + 'js/strapdown/strapdown-toc.js',
              vendorDestBase + 'js/highlightjs/highlight.pack.js',
              vendorDestBase + 'js/toastr/toastr.min.js',
// loaded standalone because of problems
//              vendorDestBase + 'js/mermaid/mermaid.full.js',
              vendorDestBase + 'js/findandreplacedomtext/findAndReplaceDOMText.js',
// loaded standalone because of plugins
              vendorSrcBase + 'freemind-flash/flashobject.js',
              vendorSrcBase + 'js/yaio/JMATAllIn.js',
        ],
        vendorCssFiles: [
              vendorDestBase + 'css/jqueryui/jquery-ui.css',
              vendorDestBase + 'css/jqueryui/jquery-ui-timepicker-addon.css',
// loaded standalone because of plugins
//              vendorDestBase + 'js/fancytree/skin-win8/ui.fancytree.css',
              vendorDestBase + 'css/highlightjs/default.css',
              vendorDestBase + 'css/toastr/toastr.css',
              vendorDestBase + 'css/mermaid/mermaid.css',
              vendorSrcBase + 'css/yaio/style.css',
              vendorSrcBase + 'css/yaio/main.css'
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
            srcBase + 'yaio-explorerapp/js/sourceselector/SourceSelectorCtrl.js',
            // !!!! paging is vendor but patched :-(
            vendorDestBase + 'js/angularjs/paging.js'
        ],
        projectSupportJsFiles: [
            srcBase + 'yaio-explorerapp/js/jmat.js',
            srcBase + 'yaio-explorerapp/js/jshelferlein/JsHelferlein.js',
            srcBase + 'yaio-explorerapp/js/jshelferlein/**/*.js',
            srcBase + 'yaio-explorerapp/js/YaioAppBaseConfig.js',
            srcBase + 'yaio-explorerapp/js/YaioAppBase.js',
            srcBase + 'yaio-explorerapp/js/utils/PromiseHelperService.js',
            srcBase + 'yaio-explorerapp/js/utils/BaseService.js',
            srcBase + 'yaio-explorerapp/js/editor/EditorService.js',
            srcBase + 'yaio-explorerapp/js/layout/LayoutService.js',
            srcBase + 'yaio-explorerapp/js/wysiwyg/FormatterService.js',
            srcBase + 'yaio-explorerapp/js/wysiwyg/MarkdownEditorService.js',
            srcBase + 'yaio-explorerapp/js/utils/ExportedDataService.js'
        ],
        projectExportsJsFiles: [
            srcBase + 'yaio-explorerapp/js/utils/ExportedDataService.js',
        ],
        projectCssFiles: [
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
              srcBase + 'yaio-explorerapp/js/sourceselector/sourceselector.css',
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
            bower: ["bower/_dest", "vendors/js", "vendors/css"],
            dist:  [destBase]
        },
        copy: {
            // copy bower-text resources (js/css/html-files) to dest and patch them
            bower2vendors: {
                options: {
                    process: function (content, srcpath) {
                        if (srcpath.search('slimbox2.') > 0) {
                            return patchFileSlimbox2(content, srcpath);
                        } else if (srcpath.search('fancytree') > 0) {
                            return patchFileFancytree(content, srcpath);
                        } else if (srcpath.search('highlightjs') > 0) {
                            return patchFileHighlightJsLang(content, srcpath);
                        } else if (srcpath.search('jquery-lang') > 0) {
                            return patchFileJQueryLang(content, srcpath);
                        } else if (srcpath.search('jquery-ui') > 0) {
                            return patchFileJQueryUi(content, srcpath);
                        } else if (srcpath.search('jquery') > 0) {
                            return patchFileJQuery(content, srcpath);
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
                    {expand: true, cwd: bowerSrcBase + 'ace-builds/src-min-noconflict', src: ['**'], dest: vendorDestBase + 'js/ace/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'angular', src: ['angular.js'], dest: vendorDestBase + 'js/angularjs/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'angular-paging', src: ['paging.js'], dest: vendorDestBase + 'js/angularjs/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'angular-animate', src: ['angular-animate.js'], dest: vendorDestBase + 'js/angularjs/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'angular-route', src: ['angular-route.js'], dest: vendorDestBase + 'js/angularjs/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'angular-translate', src: ['*.js'], dest: vendorDestBase + 'js/angularjs/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'angular-translate-loader-static-files', src: ['angular-translate-loader-static-files.js'], dest: vendorDestBase + 'js/angularjs/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'angular-update-meta/dist', src: ['update-meta.js'], dest: vendorDestBase + 'js/angularjs/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'fancytree/dist/', src: ['skin-win8/*.js', 'skin-win8/*.css', 'jquery.fancytree.js'], dest: vendorDestBase + 'js/fancytree/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'fancytree/dist/src', src: ['jquery.fancytree.dnd.js',
                                                                                   'jquery.fancytree.edit.js',
                                                                                   'jquery.fancytree.gridnav.js',
                                                                                   'jquery.fancytree.table.js'], dest: vendorDestBase + 'js/fancytree/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'find-and-replace-dom-text/src', src: ['findAndReplaceDOMText.js'], dest: vendorDestBase + 'js/findandreplacedomtext/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'highlightjs/', src: ['**/highlight.pack.js'], dest: vendorDestBase + 'js/highlightjs/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'jquery/dist', src: ['jquery.min.js'], dest: vendorDestBase + 'js/jquery/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'jquery-lang-js/js', src: ['jquery-lang.js'], dest: vendorDestBase + 'js/jquery/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'jquery-ui', src: ['**/jquery-ui.min.js', '**/jquery-ui-i18n.min.js'], dest: vendorDestBase + 'js/jqueryui/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'jqueryui-timepicker-addon', src: ['dist/jquery-ui-sliderAccess.js', 'dist/jquery-ui-timepicker-addon.js'], dest: vendorDestBase + 'js/jqueryui/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'marked', src: ['lib/marked.js'], dest: vendorDestBase + 'js/marked/', flatten: true},
                    // mermaid 0.5
                    {expand: true, cwd: bowerSrcBase + 'mermaid', src: ['dist/mermaid.js'], dest: vendorDestBase + 'js/mermaid/', flatten: true, filter: 'isFile'
                        ,rename: function(dest, src) {
                            return dest + src.replace('mermaid.js','mermaid.full.js');
                          }
                    },
                    // mermaid 0.4
                    {expand: true, cwd: bowerSrcBase + 'mermaid', src: ['dist/mermaid.full.js'], dest: vendorDestBase + 'js/mermaid/', flatten: true, filter: 'isFile'
                        ,rename: function(dest, src) {
                            return dest + src.replace('-legacy.full.js','.full.js');
                          }
                    },
                    {expand: true, cwd: bowerSrcBase + 'slimbox2', src: ['js/*.js'], dest: vendorDestBase + 'js/slimbox2/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'strapdown', src: ['src/js/strapdown-toc.js'], dest: vendorDestBase + 'js/strapdown/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'toastr', src: ['build/toastr.min.js'], dest: vendorDestBase + 'js/toastr/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'ui-contextmenu', src: ['jquery.ui-contextmenu.min.js'], dest: vendorDestBase + 'js/jqueryui/', flatten: true},
                    // CSS
                    {expand: true, cwd: bowerSrcBase + 'highlightjs/', src: ['**/default.css'], dest: vendorDestBase + 'css/highlightjs/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'jquery-ui/themes/smoothness', src: ['jquery-ui.css'], dest: vendorDestBase + 'css/jqueryui/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'jqueryui-timepicker-addon', src: ['dist/jquery-ui-timepicker-addon.css'], dest: vendorDestBase + 'css/jqueryui/', flatten: true},
                    {expand: true, cwd: bowerSrcBase + 'mermaid', src: ['dist/mermaid.css'], dest: vendorDestBase + 'css/mermaid/', flatten: true, filter: 'isFile'},
                    {expand: true, cwd: bowerSrcBase + 'slimbox2', src: ['**/slimbox2.css'], dest: vendorDestBase + 'css/slimbox2/', flatten: true, filter: 'isFile'},
                    {expand: true, cwd: bowerSrcBase + 'toastr', src: ['toastr.css'], dest: vendorDestBase + 'css/toastr/', flatten: true, filter: 'isFile'}
                ],
            },
            // copy bower-binary resources (png...-files) to dest
            bowerbin2vendors: {
                files: [
                    {expand: true, cwd: bowerSrcBase + 'fancytree/dist/', src: ['skin-win8/*.png', 'skin-win8/*.gif'], dest: vendorDestBase + 'js/fancytree/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'fancytree/dist/', src: ['skin-lion/*.png', 'skin-lion/*.gif'], dest: vendorDestBase + 'js/fancytree/', flatten: false},
                    {expand: true, cwd: bowerSrcBase + 'jquery-ui/themes/smoothness', src: ['images/*.*'], dest: vendorDestBase + 'css/jqueryui/', flatten: false},
                ],
            },
            // copy vendor-files which must exists in specific pathes to dist
            vendors2dist: {
                files: [
                    // vendors
                    {expand: true, cwd: vendorDestBase + 'js/', src: ['ace/**'], dest: destBase + 'dist/vendors-<%= pkg.vendorversion %>/', flatten: false},
                    {expand: true, cwd: vendorDestBase + 'js/fancytree', src: ['jquery.fancytree.js',
                                                                              'jquery.fancytree.dnd.js',
                                                                              'jquery.fancytree.edit.js',
                                                                              'jquery.fancytree.gridnav.js',
                                                                              'jquery.fancytree.table.js',
                                                                              'skin-lion/*.*',
                                                                              'skin-win8/*.*'], dest: destBase + 'dist/vendors-<%= pkg.vendorversion %>/fancytree/', flatten: false},
                    {expand: true, cwd: vendorDestBase + 'css', src: ['jqueryui/images/*.*'], dest: destBase + 'dist/vendors-<%= pkg.vendorversion %>/', flatten: false},
                    {expand: true, cwd: vendorDestBase + 'js/mermaid', src: ['*.js'], dest: destBase + 'dist/vendors-<%= pkg.vendorversion %>/mermaid/', flatten: false},
                    {expand: true, cwd: vendorDestBase + 'js/', src: ['slimbox2/**'], dest: destBase + 'dist/vendors-<%= pkg.vendorversion %>/', flatten: false},
                    {expand: true, cwd: vendorDestBase + 'css/', src: ['slimbox2/**'], dest: destBase + 'dist/vendors-<%= pkg.vendorversion %>/', flatten: false},
                    // yaio-intern vendors
                    {expand: true, cwd: vendorSrcBase + '', src: ['freemind-flash/**'], dest: destBase + 'dist/vendors-<%= pkg.vendorversion %>/', flatten: false},
                    {expand: true, cwd: vendorSrcBase + 'js/', src: ['yaio/**'], dest: destBase + 'dist/vendors-<%= pkg.vendorversion %>/', flatten: false},
                    {expand: true, cwd: vendorSrcBase + 'css/', src: ['yaio/**'], dest: destBase + 'dist/vendors-<%= pkg.vendorversion %>/', flatten: false}
                ]
            },
            // copy archiv to dist
            archiv2dist: {
                files: [
                    {expand: true, cwd: archivSrcBase, src: ['**'], dest: destBase + 'dist/', flatten: false}
                ]
            },
            // copy files which must excists in specifc version (because exports include them) from dist to archiv
            dist2archiv: {
                files: [
                        {expand: true, cwd: destBase + 'dist/', 
                            src: [
                                'vendors-<%= pkg.vendorversion %>/**',
                                'vendors-full-<%= pkg.vendorversion %>.js',
                                'vendors-full-<%= pkg.vendorversion %>.css',
                                '<%= pkg.name %>-reset-<%= pkg.resetversion %>.css',
                                '<%= pkg.name %>-exports-full-<%= pkg.exportsversion %>.js',
                                '<%= pkg.name %>-exports-full-<%= pkg.exportsversion %>.css',
                                '<%= pkg.name %>-exports-print-<%= pkg.exportsversion %>.css',
                                '<%= pkg.name %>-exports-print-dataonly-<%= pkg.exportsversion %>.css'
                                ], dest: archivSrcBase, flatten: false},
                    {expand: true, cwd: archivSrcBase, src: ['**'], dest: destBase + 'dist/', flatten: false}
                ]
            },
            // copy the yaio.sources to dist
            yaiores2dist: {
                files: [
                    {expand: true, cwd: srcBase + 'pages/', src: ['*.html'], dest: destBase, flatten: false},
                    {expand: true, cwd: srcBase, src: ['yaio-explorerapp/**/*.html', 'yaio-explorerapp/**/*.json', 'yaio-explorerapp/static/**'], dest: destBase, flatten: false},
                    {expand: true, cwd: tplSrcBase, src: ['exporttemplates/*.html'], dest: destBase, flatten: false}
                ]
            }
        },

        replace: {
            // replace all version-placeholders in dist
            versionOnDist: {
                options: {
                    patterns: [
                        {
                            match: /[-.]appversion\.(css|js)/g,
                            replacement: "-<%= pkg.appversion %>.$1"
                        },
                        {
                            match: /[-.]exportsversion\.(css|js)/g,
                            replacement: "-<%= pkg.exportsversion %>.$1"
                        },
                        {
                            match: /[-.]supportversion\.(css|js)/g,
                            replacement: "-<%= pkg.supportversion %>.$1"
                        },
                        {
                            match: /[-.]resetversion\.(css|js)/g,
                            replacement: "-<%= pkg.resetversion %>.$1"
                        },
                        {
                            match: /vendors[-.]vendorversion\//g,
                            replacement: "vendors-<%= pkg.vendorversion %>/"
                        },
                        {
                            match: /[-.]vendorversion\.(css|js)/g,
                            replacement: "-<%= pkg.vendorversion %>.$1"
                        },
                    ]
                },
                files: [
                    {expand: true, cwd: destBase, src: ['**/*.html', '**/*.css', '**/*.js', 'yaio-explorerapp/static/**'], dest: destBase, flatten: false}
                ]
            },
            versionOnRes: {
                // replace all version-placeholders in static resourcefolder
                options: {
                    patterns: [
                        {
                            match: /dist\/yaio-exports-(.*)[-.](exportsversion|\d+\.\d+\.\d+)?\.(css|js)/g,
                            replacement: "dist\/yaio-exports-$1-<%= pkg.exportsversion %>.$3"
                        },
                        {
                            match: /dist\/yaio-reset[-.](resetversion|\d+\.\d+\.\d+)?\.(css|js)/g,
                            replacement: "dist\/yaio-reset-<%= pkg.resetversion %>.$2"
                        },
                        {
                            match: /dist\/yaio-support-(.*)[-.](supportversion|\d+\.\d+\.\d+)?\.(css|js)/g,
                            replacement: "dist\/yaio-support-$1-<%= pkg.supportversion %>.$3"
                        },
                        {
                            match: /dist\/vendors-(.*)[-.](vendorversion|\d+\.\d+\.\d+)?\.(css|js)/g,
                            replacement: "dist\/vendors-$1-<%= pkg.vendorversion %>.$3"
                        },
                        {
                            match: /dist\/vendors[-.](vendorversion|\d+\.\d+\.\d+)?\//g,
                            replacement: "dist\/vendors-<%= pkg.vendorversion %>/"
                        }
                    ]
                },
                files: [
                    {expand: true, 
                        src: ['resources/projektplan-export-header.html', 
                              'resources/examples/markdownhelp/markdownhelp.html'], 
                        dest: '', flatten: false}
                ]
            }
        },

        // concat files to create app-includes
        concat: {
            options: {
                stripBanners: true,
                banner: '/*! <%= pkg.name %> - v<%= pkg.version %> - <%= grunt.template.today("yyyy-mm-dd") %> */\n\n',
            },            
            vendors: {
                options: {
                    stripBanners: true,
                    banner: '/*! <%= pkg.name %>-v<%= pkg.version %> vendors-<%= pkg.vendorversion %> */\n\n',
                },            
                files: {
                    '<%= destBase %>dist/vendors-full-<%= pkg.vendorversion %>.js': ['<%= vendorJsFiles %>'],
                    '<%= destBase %>dist/vendors-full-<%= pkg.vendorversion %>.css': ['<%= vendorCssFiles %>'],
                },
            },
            reset: {
                options: {
                    stripBanners: true,
                    banner: '/*! <%= pkg.name %>-v<%= pkg.version %> reset-<%= pkg.resetversion %> */\n\n',
                },            
                files: {
                    '<%= destBase %>dist/<%= pkg.name %>-reset-<%= pkg.resetversion %>.css': ['<%= projectResetCssFiles %>'],
                },
            },
            app: {
                options: {
                    stripBanners: true,
                    banner: '/*! <%= pkg.name %>-v<%= pkg.version %> app-<%= pkg.appversion %> */\n\n',
                },            
                files: {
                    '<%= destBase %>dist/<%= pkg.name %>-app-full-<%= pkg.appversion %>.js': ['<%= projectJsFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-app-full-<%= pkg.appversion %>.css': ['<%= projectCssFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-app-print-<%= pkg.appversion %>.css': ['<%= projectPrintCssFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-app-print-dataonly-<%= pkg.appversion %>.css': ['<%= projectPrintDataOnlyCssFiles %>'],
                },
            },
            support: {
                options: {
                    stripBanners: true,
                    banner: '/*! <%= pkg.name %>-v<%= pkg.version %> support-<%= pkg.supportversion %> */\n\n',
                },            
                files: {
                    '<%= destBase %>dist/<%= pkg.name %>-support-full-<%= pkg.supportversion %>.js': ['<%= projectSupportJsFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-support-full-<%= pkg.supportversion %>.css': ['<%= projectSupportCssFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-support-print-<%= pkg.supportversion %>.css': ['<%= projectPrintCssFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-support-print-dataonly-<%= pkg.supportversion %>.css': ['<%= projectPrintDataOnlyCssFiles %>'],
                },
            },
            exports: {
                options: {
                    stripBanners: true,
                    banner: '/*! <%= pkg.name %>-v<%= pkg.version %> exports-<%= pkg.exportsversion %> */\n\n',
                },            
                files: {
                    '<%= destBase %>dist/<%= pkg.name %>-exports-full-<%= pkg.exportsversion %>.js': ['<%= projectSupportJsFiles %>', '<%= projectExportsJsFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-exports-full-<%= pkg.exportsversion %>.css': ['<%= projectSupportCssFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-exports-print-<%= pkg.exportsversion %>.css': ['<%= projectPrintCssFiles %>'],
                    '<%= destBase %>dist/<%= pkg.name %>-exports-print-dataonly-<%= pkg.exportsversion %>.css': ['<%= projectPrintDataOnlyCssFiles %>']
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
        // unit-tests with karma
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
        // e2e-tests with protractor
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
        // watcher to support dev-process (start specific actions if sourcefiles change)
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
                files: [srcBase + 'yaio-explorerapp/**/*.*', testSrcBase + 'e2e/**/*', testSrcBase + 'unit/**/*'],
                tasks: ['dist', 'karma:unit', 'protractor:e2e']
            }
        }
    });

    // register tasks
    grunt.registerTask('default',   ['jshint']);
    grunt.registerTask('vendors',   ['clean:bower', 'bower', 'copy:bower2vendors', 'copy:bowerbin2vendors']);
    grunt.registerTask('dist',      ['vendors', 'clean:dist', 'copy:archiv2dist', 'concat', 'copy:vendors2dist', 'copy:yaiores2dist', 'replace:versionOnDist', 'replace:versionOnRes', 'copy:dist2archiv']);
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
    grunt.loadNpmTasks('grunt-replace');
};