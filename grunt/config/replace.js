'use strict';
module.exports = {
    // replace all version-placeholders in dist
    versionOnDist: {
        options: {
            patterns: [
                {
                    match: /[-.]appversion\.(css|js)/g,
                    replacement: "-<%= pkg.appversion %>.$1"
                },
                {
                    match: /[-.]exportsversion\.(css|js)/g,
                    replacement: "-<%= pkg.exportsversion %>.$1"
                },
                {
                    match: /[-.]supportversion\.(css|js)/g,
                    replacement: "-<%= pkg.supportversion %>.$1"
                },
                {
                    match: /[-.]resetversion\.(css|js)/g,
                    replacement: "-<%= pkg.resetversion %>.$1"
                },
                {
                    match: /vendors[-.]vendorversion\//g,
                    replacement: "vendors-<%= pkg.vendorversion %>/"
                },
                {
                    match: /[-.]vendorversion\.(css|js)/g,
                    replacement: "-<%= pkg.vendorversion %>.$1"
                },
                // ymf-files
                {
                    match: /dist\/ymf-(.*?)-vendors\.(css|js)/g,
                    replacement: "dist\/vendors-full-<%= pkg.vendorversion %>.$2"
                },
                {
                    match: /dist\/ymf-(.*?)-vendors\//g,
                    replacement: "dist\/vendors-<%= pkg.vendorversion %>\/"
                },
                {
                    match: /dist\/ymf-reset\.(css|js)/g,
                    replacement: "dist\/yaio-reset-<%= pkg.resetversion %>.$1"
                },
                {
                    match: /dist\/ymf-app-(full|print)(.*?)\.(css|js)/g,
                    replacement: "dist\/yaio-app-$1$2-<%= pkg.appversion %>.$3"
                },
                {
                    match: /dist\/ymf-jsh-(full|print)(.*?)\.(css|js)/g,
                    replacement: "dist\/yaio-support-$1$2-<%= pkg.supportversion %>.$3"
                }
            ]
        },
        files: [
            {expand: true, cwd: '<%= destBase %>', src: ['**/*.html', '**/*.css', '**/*.js', 'yaio-explorerapp/static/**'], dest: '<%= destBase %>', flatten: false}
        ]
    },
    versionOnRes: {
        // replace all version-placeholders in static resourcefolder
        options: {
            patterns: [
                {
                    match: /dist\/yaio-exports-(.*)[-.](exportsversion|\d+\.\d+\.\d+)?\.(css|js)/g,
                    replacement: "dist\/yaio-exports-$1-<%= pkg.exportsversion %>.$3"
                },
                {
                    match: /dist\/yaio-reset[-.](resetversion|\d+\.\d+\.\d+)?\.(css|js)/g,
                    replacement: "dist\/yaio-reset-<%= pkg.resetversion %>.$2"
                },
                {
                    match: /dist\/yaio-support-(.*)[-.](supportversion|\d+\.\d+\.\d+)?\.(css|js)/g,
                    replacement: "dist\/yaio-support-$1-<%= pkg.supportversion %>.$3"
                },
                {
                    match: /dist\/vendors-(.*)[-.](vendorversion|\d+\.\d+\.\d+)?\.(css|js)/g,
                    replacement: "dist\/vendors-$1-<%= pkg.vendorversion %>.$3"
                },
                {
                    match: /dist\/vendors[-.](vendorversion|\d+\.\d+\.\d+)?\//g,
                    replacement: "dist\/vendors-<%= pkg.vendorversion %>/"
                }
            ]
        },
        files: [
            {expand: true,
                src: ['resources/projektplan-export-header.html'],
                dest: '', flatten: false}
        ]
    }
};

