'use strict';
module.exports = {
    distYaio:{
        files:[{
            cwd: '<%= resSrcBase %>images/icons/jsh/',
            src: '**/*.{png,jpg,gif}',
            dest: '<%= destBase %>dist/yaio-icons-embed-<%= pkg.supportversion %>.css'
        }],
        options:{
            prefix: '',
            images_path: './'
        }
    }
};

