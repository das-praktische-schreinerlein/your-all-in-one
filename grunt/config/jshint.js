(function () {
    'use strict';

    // jshint: look at https://github.com/gruntjs/grunt-contrib-jshint
    module.exports = {
        options: {
            jshintrc: true
        },
        js: {
            files: {
                src:                 [
                    'GruntFile.js',
                    'grunt/**/*.js',
                    'src/main/web/**/*.js',
                    'src/test/javascript/**/*.js'
                ]
            }
        },
        html: {
            options: {
                extract: 'always',
                undef: true,
                browser: true,
                strict: false,
                jshintrc: 'browser.jshintrc'
            },
            files: {
                src: ['src/**/*.html']
            }
        }
    };
})();
