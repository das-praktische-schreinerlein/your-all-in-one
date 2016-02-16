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
                    'grunt/**/*.js',
                    'src/main/web/**/*.js'
                ]
            }
        },
        unittests: {
            options: {
                extract: 'always',
                jshintrc: 'unittests.jshintrc'
            },
            files: {
                src: [
                    'src/test/javascript/unit/**/*.js'
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
        },
        html: {
            options: {
                extract: 'always',
                undef: true,
                browser: true,
                strict: false,
                jshintrc: 'html.jshintrc'
            },
            files: {
                src: ['src/main/web/**/*.html']
            }
        }
    };
})();
