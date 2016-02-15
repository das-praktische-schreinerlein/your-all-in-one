/**
 * software for projectmanagement and documentation
 *
 * @FeatureDomain                Collaboration
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

/**
 * taskconfiguration
 *
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
(function () {
    'use strict';
    var path = require('path');

    /**
     * baseconfig
     **/
    var srcBase = 'src/main/web/';
    var resSrcBase = 'src/main/resources/';
    var tplSrcBase = 'src/main/web/';
    var destBase = 'src/main/generated-resources/static/';
    var bowerSrcBase = 'bower/_src/';
    var vendorSrcBase = 'vendors/src/';
    var vendorDestBase = 'vendors/';
    var archivSrcBase = 'vendors/archiv/';
    var testSrcBase = 'src/test/javascript/';

    /**
     * configure tasks
     **/
    module.exports = function(grunt) {
        require('load-grunt-config')(grunt, {
            path: path,
            configPath: path.join(process.cwd(), 'grunt/config'),
            jitGrunt: {
                customTasksDir: 'grunt/tasks'
            },
            data: {
                pkg: grunt.file.readJSON('package.json'),
                srcBase: srcBase,
                resSrcBase: resSrcBase,
                tplSrcBase: tplSrcBase,
                destBase: destBase,
                bowerSrcBase: bowerSrcBase,
                vendorSrcBase: vendorSrcBase,
                vendorDestBase: vendorDestBase,
                archivSrcBase: archivSrcBase,
                testSrcBase: testSrcBase,

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
                    vendorDestBase + 'js/js-deflate/rawdeflate.js',
                    vendorDestBase + 'js/strapdown/strapdown-toc.js',
                    vendorDestBase + 'js/highlightjs/highlight.pack.js',
                    vendorDestBase + 'js/select2/select2.full.min.js',
                    vendorDestBase + 'js/slimbox2/slimbox2.js',
                    vendorDestBase + 'js/toastr/toastr.min.js',
// loaded standalone because of problems
//              vendorDestBase + 'js/mermaid/mermaid.full.js',
                    vendorDestBase + 'js/findandreplacedomtext/findAndReplaceDOMText.js',
// loaded standalone because of plugins
                    vendorSrcBase + 'freemind-flash/flashobject.js',
                    vendorSrcBase + 'js/yaio/JMATAllIn.js',
                    // !!!! ymf is vendor but my project
                    vendorDestBase + 'js/ymf/ymf-app-dist.js'
                ],
                vendorCssFiles: [
                    vendorDestBase + 'css/jqueryui/jquery-ui.css',
                    vendorDestBase + 'css/jqueryui/jquery-ui-timepicker-addon.css',
// loaded standalone because of plugins
//              vendorDestBase + 'js/fancytree/skin-win8/ui.fancytree.css',
                    vendorDestBase + 'css/highlightjs/default.css',
                    vendorDestBase + 'css/slimbox2/slimbox2.css',
                    vendorDestBase + 'css/select2/select2.min.css',
                    vendorDestBase + 'css/toastr/toastr.css',
                    vendorDestBase + 'css/mermaid/mermaid.css',
                    vendorSrcBase + 'css/yaio/style.css',
                    vendorSrcBase + 'css/yaio/main.css',
                    // !!!! ymf is vendor but my project
                    vendorDestBase + 'css/ymf/ymf-app-dist.css'
                ],
                projectJsFiles: [
                    srcBase + 'yaio-explorerapp/js/jmat.js',
                    srcBase + 'yaio-explorerapp/js/YaioAppBaseConfig.js',
                    srcBase + 'yaio-explorerapp/js/YaioAppBase.js',
                    srcBase + 'yaio-explorerapp/js/utils/PromiseHelper.js',
                    srcBase + 'yaio-explorerapp/js/utils/Base.js',
                    srcBase + 'yaio-explorerapp/js/utils/FileLoader.js',
                    srcBase + 'yaio-explorerapp/js/editor/Editor.js',
                    srcBase + 'yaio-explorerapp/js/layout/Layout.js',
                    srcBase + 'yaio-explorerapp/js/formatter/MarkdownRenderer.js',
                    srcBase + 'yaio-explorerapp/js/utils/ExportedData.js',
                    // services
                    srcBase + 'yaio-explorerapp/js/datarenderer/*.js',
                    srcBase + 'yaio-explorerapp/js/dataservices/*.js',
                    srcBase + 'yaio-explorerapp/js/explorer/ExplorerAction.js',
                    srcBase + 'yaio-explorerapp/js/explorer/ExplorerConverter.js',
                    srcBase + 'yaio-explorerapp/js/explorer/ExplorerTree.js',
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
                    srcBase + 'yaio-explorerapp/js/dashboard/*.js',
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
                    srcBase + 'yaio-explorerapp/js/YaioAppBaseConfig.js',
                    srcBase + 'yaio-explorerapp/js/YaioAppBase.js',
                    srcBase + 'yaio-explorerapp/js/utils/PromiseHelper.js',
                    srcBase + 'yaio-explorerapp/js/utils/Base.js',
                    srcBase + 'yaio-explorerapp/js/utils/FileLoader.js',
                    srcBase + 'yaio-explorerapp/js/editor/Editor.js',
                    srcBase + 'yaio-explorerapp/js/layout/Layout.js',
                    srcBase + 'yaio-explorerapp/js/formatter/MarkdownRenderer.js',
                    srcBase + 'yaio-explorerapp/js/utils/ExportedData.js'
                ],
                projectExportsJsFiles: [
                    srcBase + 'yaio-explorerapp/js/utils/ExportedData.js'
                ],
                projectCssFiles: [
                    srcBase + 'yaio-explorerapp/js/layout/base.css',
                    srcBase + 'yaio-explorerapp/js/layout/support.css',
                    srcBase + 'yaio-explorerapp/js/layout/toc.css',
                    srcBase + 'yaio-explorerapp/js/layout/yaio.css',
                    srcBase + 'yaio-explorerapp/js/explorer/explorer.css',
                    srcBase + 'yaio-explorerapp/js/datarenderer/gantt.css',
                    srcBase + 'yaio-explorerapp/js/datarenderer/data.css',
                    srcBase + 'yaio-explorerapp/js/formatter/formatter.css',
                    srcBase + 'yaio-explorerapp/js/search/search.css',
                    srcBase + 'yaio-explorerapp/js/editor/editor.css',
                    srcBase + 'yaio-explorerapp/js/exporter/exporter.css',
                    srcBase + 'yaio-explorerapp/js/sourceselector/sourceselector.css',
                    srcBase + 'yaio-explorerapp/js/lang/lang.css',
                    srcBase + 'yaio-explorerapp/js/auth/auth.css',
                    srcBase + 'yaio-explorerapp/js/frontpage/frontpage.css',
                    srcBase + 'yaio-explorerapp/js/dashboard/*.css'
                ],
                projectSupportCssFiles: [
                    srcBase + 'yaio-explorerapp/js/layout/base.css',
                    srcBase + 'yaio-explorerapp/js/formatter/formatter.css',
                    srcBase + 'yaio-explorerapp/js/layout/support.css'
                ],
                projectPrintCssFiles: [
                    // !!!! ymf is vendor but my project
                    vendorDestBase + 'css/ymf/ymf-app-print.css',
                    srcBase + 'yaio-explorerapp/js/layout/base-print.css'
                ],
                projectPrintDataOnlyCssFiles: [
                    // !!!! ymf is vendor but my project
                    vendorDestBase + 'css/ymf/ymf-app-print-dataonly.css',
                    srcBase + 'yaio-explorerapp/js/layout/base-print-dataonly.css'
                ],
                projectResetCssFiles: [
                    // !!!! ymf is vendor but my project
                    vendorDestBase + 'css/ymf/ymf-reset.css',
                    srcBase + 'yaio-explorerapp/js/layout/reset.css'
                ],
                vendorJsTestFiles: [
                    testSrcBase + 'unit/resources/js/jasmine/jasmine-jquery.js',
                    testSrcBase + 'unit/jasmine-config.js'
                ],
                projectUnitJsTestFiles: [
                    testSrcBase + 'unit/yaio-explorerapp/**/*_test.js'
                ],
                projectE2EJsTestFiles: [
                    testSrcBase + 'e2e/yaio-explorerapp/**/*_test.js'
                ]
            }
        });

        // register tasks
        grunt.registerTask('default', ['distfull']);

        grunt.registerTask('css-images', ['css_image']);
        grunt.registerTask('sprites', ['sprite']);
        grunt.registerTask('data-uri', ['dataUri']);
        grunt.registerTask('images', ['sprites', 'css-images', 'data-uri']);

        grunt.registerTask('vendorslocal', ['copy:bower2vendors', 'copy:bowerbin2vendors']);
        grunt.registerTask('vendorsfull', ['clean:bower', 'bower', 'vendorslocal']);
        grunt.registerTask('distyaio', ['images', 'concat', 'copy:yaiores2dist', 'replace:versionOnDist', 'replace:versionOnRes', 'copy:dist2archiv']);
        grunt.registerTask('distlocal', ['vendorslocal', 'copy:vendors2dist', 'distyaio']);
        grunt.registerTask('distfull', ['vendorsfull', 'clean:dist', 'copy:archiv2dist', 'images', 'concat', 'copy:vendors2dist', 'copy:yaiores2dist', 'replace:versionOnDist', 'replace:versionOnRes', 'copy:dist2archiv']);
        grunt.registerTask('dist', ['distfull']);
        grunt.registerTask('unit-test', ['dist', 'karma:continuous:start', 'watch:karma']);
        grunt.registerTask('e2e-test', ['dist', 'protractor:continuous', 'watch:protractor']);

        // load grunt tasks
        grunt.loadNpmTasks('grunt-bower-task');
        grunt.loadNpmTasks('grunt-css-image');
        grunt.loadNpmTasks('grunt-contrib-clean');
        grunt.loadNpmTasks('grunt-contrib-copy');
        grunt.loadNpmTasks('grunt-contrib-concat');
        grunt.loadNpmTasks('grunt-contrib-jshint');
        grunt.loadNpmTasks('grunt-contrib-watch');
        grunt.loadNpmTasks('grunt-data-uri');
        grunt.loadNpmTasks('grunt-jsdoc');
        grunt.loadNpmTasks('grunt-karma');
        grunt.loadNpmTasks('grunt-protractor-runner');
        grunt.loadNpmTasks('grunt-replace');
        grunt.loadNpmTasks('grunt-spritesmith');
    };

})();
