(function () {
    'use strict';

    // jshint: look at https://github.com/gruntjs/grunt-contrib-jshint
    module.exports = {
        options: {
            jshintrc: true
            //reporter: 'checkstyle'
        },
        js: {
            files: {
                src:                 [
                    'GruntFile.js',
                    'grunt/**/*.js'
                ]
            }
        },
        e2e: {
            options: {
                extract: 'always',
                jshintrc: 'e2e.jshintrc'
            },
            files: {
                src: [
                    'src/test/javascript/e2e/**/*.js'
                ]
            }
        }
    };
})();
