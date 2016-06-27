(function () {
    'use strict';

    module.exports = {
        options: {
            livereload: true
        },
        protractor: {
            // run when any projectfiles or tests changed
            files: ['<%= testSrcBase %>e2e/**/*'],
            tasks: ['protractor:continuous']
        }
    };
})();