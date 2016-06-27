(function () {
    'use strict';

    module.exports = {
        options: {
            livereload: true
        },
        dist: {
            // run when any projectfiles or tests changed
            files: ['<%= srcBase %>yaio-explorerapp/**/*.*'],
            tasks: ['dist']
        }
    };
})();