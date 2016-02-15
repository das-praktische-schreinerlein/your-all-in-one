(function () {
    'use strict';

    module.exports = {
        iconsYaio: {
            src: '<%= resSrcBase %>images/icons/*.*',
            dest: '<%= destBase %>dist/yaio-icons-sprite.png',
            destCss: '<%= destBase %>dist/yaio-icons-sprite-<%= pkg.supportversion %>.css',
            cssTemplate: 'css.template.handlebars',
            cssVarMap: function (sprite) {
                sprite.name = 'yaio-' + sprite.name;
            }
        }
    };
})();

