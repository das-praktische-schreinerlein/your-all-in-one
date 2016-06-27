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
    var testSrcBase = 'bower/_dest/js/yaio-explorerapp/src/test/javascript/e2e/';

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
                testSrcBase: testSrcBase,

                // define files
                projectE2EJsTestFiles: [
                    testSrcBase + 'e2e/yaio-explorerapp/**/*_test.js'
                ]
            }
        });

        // register tasks
        grunt.registerTask('default', ['e2e-prepare']);

        grunt.registerTask('e2e-prepare', ['clean:bower', 'bower']);
        grunt.registerTask('e2e-test', ['e2e-prepare', 'protractor:e2e']);
        grunt.registerTask('e2e-continuous', ['e2e-prepare', 'protractor:continuous', 'watch:protractor']);

        // load grunt tasks
        grunt.loadNpmTasks('grunt-bower-task');
        grunt.loadNpmTasks('grunt-contrib-clean');
        grunt.loadNpmTasks('grunt-contrib-jshint');
        grunt.loadNpmTasks('grunt-contrib-watch');
        grunt.loadNpmTasks('grunt-protractor-runner');
    };

})();
