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
                    vendorDestBase + 'js/yaio-explorerapp/yaio-app-dist-0.1.0.js',
                    // !!!! paging is vendor but patched :-(
                    vendorDestBase + 'js/angularjs/paging.js'
                ],
                projectSupportJsFiles: [
                    vendorDestBase + 'js/yaio-explorerapp/yaio-support-dist-0.1.0.js'
                ],
                projectExportsJsFiles: [
                    vendorDestBase + 'js/yaio-explorerapp/yaio-export-dist-0.1.0.js'
                ],
                projectCssFiles: [
                    vendorDestBase + 'js/yaio-explorerapp/yaio-app-dist-0.1.0.css'
                ],
                projectSupportCssFiles: [
                    vendorDestBase + 'js/yaio-explorerapp/yaio-app-support-0.1.0.css'
                ],
                projectPrintCssFiles: [
                    // !!!! ymf is vendor but my project
                    vendorDestBase + 'js/yaio-explorerapp/yaio-app-print-0.1.0.css'
                ],
                projectPrintDataOnlyCssFiles: [
                    // !!!! ymf is vendor but my project
                    vendorDestBase + 'js/yaio-explorerapp/yaio-app-print-dataonly-0.1.0.css'
                ],
                projectResetCssFiles: [
                    // !!!! ymf is vendor but my project
                    vendorDestBase + 'js/yaio-explorerapp/yaio-reset-0.1.0.css'
                ]
            }
        });

        // register tasks
        grunt.registerTask('default', ['distfull']);

        grunt.registerTask('vendorslocal', ['copy:bower2vendors', 'copy:bowerbin2vendors']);
        grunt.registerTask('vendorsfull', ['clean:bower', 'bower', 'vendorslocal']);
        grunt.registerTask('distfull', ['vendorsfull', 'clean:dist', 'copy:archiv2dist', 'concat', 'copy:vendors2dist', 'replace:versionOnDist', 'replace:versionOnRes', 'copy:dist2archiv']);
        grunt.registerTask('dist', ['distfull']);

        // load grunt tasks
        grunt.loadNpmTasks('grunt-bower-task');
        grunt.loadNpmTasks('grunt-contrib-clean');
        grunt.loadNpmTasks('grunt-contrib-copy');
        grunt.loadNpmTasks('grunt-contrib-concat');
        grunt.loadNpmTasks('grunt-contrib-jshint');
        grunt.loadNpmTasks('grunt-contrib-watch');
        grunt.loadNpmTasks('grunt-data-uri');
        grunt.loadNpmTasks('grunt-jsdoc');
        grunt.loadNpmTasks('grunt-replace');
    };

})();
