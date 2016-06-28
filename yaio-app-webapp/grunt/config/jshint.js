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
        }
    };
})();
