// jshint: look at https://github.com/gruntjs/grunt-contrib-jshint
'use strict';
module.exports = {
    files: [
        'GruntFile.js',
        '<%= srcBase %>' + 'yaio-explorerapp/js/**/*.js'
    ],
    options: {
        jshintrc: true
    }
};

