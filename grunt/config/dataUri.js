(function () {
    'use strict';

    module.exports = {
        distIconsYaio: {
            // src file
            src: ['<%= destBase %>dist/yaio-icons-embed-<%= pkg.supportversion %>.css'],
            // output dir
            dest: '<%= destBase %>dist/',
            options: {
                // specified files are only encoding
                target: ['<%= resSrcBase %>images/icons/**/*.png', '<%= resSrcBase %>images/icons/**/*.gif', '<%= resSrcBase %>images/icons/**/*.jpg'],
                // adjust relative path?
                fixDirLevel: true,
                // img detecting base dir
                baseDir: '<%= resSrcBase %>images/icons/',

                // Do not inline any images larger
                // than this size. 2048 is a size
                // recommended by Google's mod_pagespeed.
                maxBytes : 20000
            }
        }
    };
})();