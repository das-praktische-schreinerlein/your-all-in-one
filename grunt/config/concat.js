'use strict';
module.exports = {
    options: {
        stripBanners: true,
        banner: '/*! <%= pkg.name %> - v<%= pkg.version %> - <%= grunt.template.today("yyyy-mm-dd") %> */\n\n'
    },
    vendors: {
        options: {
            stripBanners: true,
            banner: '/*! <%= pkg.name %>-v<%= pkg.version %> vendors-<%= pkg.vendorversion %> */\n\n'
        },
        files: {
            '<%= destBase %>dist/vendors-full-<%= pkg.vendorversion %>.js': ['<%= vendorJsFiles %>'],
            '<%= destBase %>dist/vendors-full-<%= pkg.vendorversion %>.css': ['<%= vendorCssFiles %>']
        }
    },
    reset: {
        options: {
            stripBanners: true,
            banner: '/*! <%= pkg.name %>-v<%= pkg.version %> reset-<%= pkg.resetversion %> */\n\n'
        },
        files: {
            '<%= destBase %>dist/<%= pkg.name %>-reset-<%= pkg.resetversion %>.css': ['<%= projectResetCssFiles %>']
        }
    },
    app: {
        options: {
            stripBanners: true,
            banner: '/*! <%= pkg.name %>-v<%= pkg.version %> app-<%= pkg.appversion %> */\n\n'
        },
        files: {
            '<%= destBase %>dist/<%= pkg.name %>-app-full-<%= pkg.appversion %>.js': ['<%= projectJsFiles %>'],
            '<%= destBase %>dist/<%= pkg.name %>-app-full-<%= pkg.appversion %>.css': ['<%= projectCssFiles %>'],
            '<%= destBase %>dist/<%= pkg.name %>-app-print-<%= pkg.appversion %>.css': ['<%= projectPrintCssFiles %>'],
            '<%= destBase %>dist/<%= pkg.name %>-app-print-dataonly-<%= pkg.appversion %>.css': ['<%= projectPrintDataOnlyCssFiles %>']
        }
    },
    support: {
        options: {
            stripBanners: true,
            banner: '/*! <%= pkg.name %>-v<%= pkg.version %> support-<%= pkg.supportversion %> */\n\n'
        },
        files: {
            '<%= destBase %>dist/<%= pkg.name %>-support-full-<%= pkg.supportversion %>.js': ['<%= projectSupportJsFiles %>'],
            '<%= destBase %>dist/<%= pkg.name %>-support-full-<%= pkg.supportversion %>.css': ['<%= projectSupportCssFiles %>'],
            '<%= destBase %>dist/<%= pkg.name %>-support-print-<%= pkg.supportversion %>.css': ['<%= projectPrintCssFiles %>'],
            '<%= destBase %>dist/<%= pkg.name %>-support-print-dataonly-<%= pkg.supportversion %>.css': ['<%= projectPrintDataOnlyCssFiles %>']
        }
    },
    exports: {
        options: {
            stripBanners: true,
            banner: '/*! <%= pkg.name %>-v<%= pkg.version %> exports-<%= pkg.exportsversion %> */\n\n'
        },
        files: {
            '<%= destBase %>dist/<%= pkg.name %>-exports-full-<%= pkg.exportsversion %>.js': ['<%= projectSupportJsFiles %>', '<%= projectExportsJsFiles %>'],
            '<%= destBase %>dist/<%= pkg.name %>-exports-full-<%= pkg.exportsversion %>.css': ['<%= projectSupportCssFiles %>'],
            '<%= destBase %>dist/<%= pkg.name %>-exports-print-<%= pkg.exportsversion %>.css': ['<%= projectPrintCssFiles %>'],
            '<%= destBase %>dist/<%= pkg.name %>-exports-print-dataonly-<%= pkg.exportsversion %>.css': ['<%= projectPrintDataOnlyCssFiles %>']
        }
    }
};

