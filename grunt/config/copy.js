(function () {
    'use strict';

    module.exports = {
        patchOldComments: {
            options: {
                process: function (content, srcpath) {
                    return patchComments(content, srcpath);
                }
            },
            files: [
                // JS
                {expand: true, cwd: 'src/main', src: ['web/**/*.js', 'web/**/*.css', 'java/**/*.java'], dest: 'tmp/corrected/', flatten: false},
                {expand: true, cwd: 'src/test', src: ['javascript/**/*.js', 'java/**/*.java'], dest: 'tmp/corrected/', flatten: false}
            ]
        },
        // copy bower-text resources (js/css/html-files) to dest and patch them
        bower2vendors: {
            options: {
                process: function (content, srcpath) {
                    if (-1 < srcpath.search('slimbox2.')) {
                        return patchFileSlimbox2(content, srcpath);
                    } else if (-1 < srcpath.search('highlightjs')) {
                        return patchFileHighlightJsLang(content, srcpath);
                    } else if (-1 < srcpath.search('jquery-lang')) {
                        return patchFileJQueryLang(content, srcpath);
                    } else if (-1 < srcpath.search('jquery-ui')) {
                        return patchFileJQueryUi(content, srcpath);
                    }
                    return content;
                }
            },
            files: [
                // JS
                {expand: true, cwd: '<%= bowerSrcBase %>ace-builds/src-min-noconflict', src: ['**'], dest: '<%= vendorDestBase %>js/ace/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>angular', src: ['angular.js'], dest: '<%= vendorDestBase %>js/angularjs/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>angular-paging', src: ['paging.js'], dest: '<%= vendorDestBase %>js/angularjs/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>angular-animate', src: ['angular-animate.js'], dest: '<%= vendorDestBase %>js/angularjs/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>angular-route', src: ['angular-route.js'], dest: '<%= vendorDestBase %>js/angularjs/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>angular-translate', src: ['*.js'], dest: '<%= vendorDestBase %>js/angularjs/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>angular-translate-loader-static-files', src: ['angular-translate-loader-static-files.js'], dest: '<%= vendorDestBase %>js/angularjs/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>angular-update-meta/dist', src: ['update-meta.js'], dest: '<%= vendorDestBase %>js/angularjs/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>fancytree/dist/', src: ['skin-win8/*.js', 'skin-win8/*.css', 'jquery.fancytree.js'], dest: '<%= vendorDestBase %>js/fancytree/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>fancytree/dist/src', src: ['jquery.fancytree.dnd.js',
                    'jquery.fancytree.edit.js',
                    'jquery.fancytree.gridnav.js',
                    'jquery.fancytree.table.js'], dest: '<%= vendorDestBase %>js/fancytree/', flatten: true},
                {expand: true, cwd: '<%= bowerSrcBase %>find-and-replace-dom-text/src', src: ['findAndReplaceDOMText.js'], dest: '<%= vendorDestBase %>js/findandreplacedomtext/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>highlightjs/', src: ['**/highlight.pack.js'], dest: '<%= vendorDestBase %>js/highlightjs/', flatten: true},
                {expand: true, cwd: '<%= bowerSrcBase %>jquery/dist', src: ['jquery.min.js'], dest: '<%= vendorDestBase %>js/jquery/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>jquery-lang-js/js', src: ['jquery-lang.js'], dest: '<%= vendorDestBase %>js/jquery/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>jquery-ui', src: ['**/jquery-ui.min.js', '**/jquery-ui-i18n.min.js'], dest: '<%= vendorDestBase %>js/jqueryui/', flatten: true},
                {expand: true, cwd: '<%= bowerSrcBase %>jqueryui-timepicker-addon', src: ['dist/jquery-ui-sliderAccess.js', 'dist/jquery-ui-timepicker-addon.js'], dest: '<%= vendorDestBase %>js/jqueryui/', flatten: true},
                {expand: true, cwd: '<%= bowerSrcBase %>js-deflate', src: ['rawdeflate.js'], dest: '<%= vendorDestBase %>js/js-deflate/', flatten: true},
                {expand: true, cwd: '<%= bowerSrcBase %>marked', src: ['lib/marked.js'], dest: '<%= vendorDestBase %>js/marked/', flatten: true},
                // mermaid 0.5
                {expand: true, cwd: '<%= bowerSrcBase %>mermaid', src: ['dist/mermaid.js'], dest: '<%= vendorDestBase %>js/mermaid/', flatten: true, filter: 'isFile',
                    rename: function(dest, src) {
                        return dest + src.replace('mermaid.js','mermaid.full.js');
                    }
                },
                // mermaid 0.4
                {expand: true, cwd: '<%= bowerSrcBase %>mermaid', src: ['dist/mermaid.full.js'], dest: '<%= vendorDestBase %>js/mermaid/', flatten: true, filter: 'isFile',
                    rename: function(dest, src) {
                        return dest + src.replace('-legacy.full.js','.full.js');
                    }
                },
                {expand: true, cwd: '<%= bowerSrcBase %>select2', src: ['dist/js/select2.full.min.js'], dest: '<%= vendorDestBase %>js/select2/', flatten: true},
                {expand: true, cwd: '<%= bowerSrcBase %>slimbox2', src: ['js/slimbox2.js'], dest: '<%= vendorDestBase %>js/slimbox2/', flatten: true},
                {expand: true, cwd: '<%= bowerSrcBase %>strapdown', src: ['src/js/strapdown-toc.js'], dest: '<%= vendorDestBase %>js/strapdown/', flatten: true},
                {expand: true, cwd: '<%= bowerSrcBase %>toastr', src: ['build/toastr.min.js'], dest: '<%= vendorDestBase %>js/toastr/', flatten: true},
                {expand: true, cwd: '<%= bowerSrcBase %>ui-contextmenu', src: ['jquery.ui-contextmenu.min.js'], dest: '<%= vendorDestBase %>js/jqueryui/', flatten: true},
                // CSS
                {expand: true, cwd: '<%= bowerSrcBase %>highlightjs/', src: ['**/default.css'], dest: '<%= vendorDestBase %>css/highlightjs/', flatten: true},
                {expand: true, cwd: '<%= bowerSrcBase %>jquery-ui/themes/smoothness', src: ['jquery-ui.css'], dest: '<%= vendorDestBase %>css/jqueryui/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>jqueryui-timepicker-addon', src: ['dist/jquery-ui-timepicker-addon.css'], dest: '<%= vendorDestBase %>css/jqueryui/', flatten: true},
                {expand: true, cwd: '<%= bowerSrcBase %>mermaid', src: ['dist/mermaid.css'], dest: '<%= vendorDestBase %>css/mermaid/', flatten: true, filter: 'isFile'},
                {expand: true, cwd: '<%= bowerSrcBase %>select2', src: ['dist/css/select2.min.css'], dest: '<%= vendorDestBase %>css/select2/', flatten: true},
                {expand: true, cwd: '<%= bowerSrcBase %>slimbox2', src: ['**/slimbox2.css'], dest: '<%= vendorDestBase %>css/slimbox2/', flatten: true, filter: 'isFile'},
                {expand: true, cwd: '<%= bowerSrcBase %>toastr', src: ['toastr.css'], dest: '<%= vendorDestBase %>css/toastr/', flatten: true, filter: 'isFile'},

                // ymf
                {expand: true, cwd: '<%= bowerSrcBase %>ymf/build/dist/', src: ['*.css'], dest: '<%= vendorDestBase %>css/ymf/', flatten: true, filter: 'isFile'},
                {expand: true, cwd: '<%= bowerSrcBase %>ymf/build/dist/', src: ['*.js'], dest: '<%= vendorDestBase %>js/ymf/', flatten: true, filter: 'isFile'},
                {expand: true, cwd: '<%= bowerSrcBase %>ymf/build/ymf-editorapp/', src: ['*.html'], dest: '<%= vendorDestBase %>html/ymf/', flatten: true, filter: 'isFile'}
            ]
        },
        // copy bower-binary resources (png...-files) to dest
        bowerbin2vendors: {
            files: [
                {expand: true, cwd: '<%= bowerSrcBase %>fancytree/dist/', src: ['skin-win8/*.png', 'skin-win8/*.gif'], dest: '<%= vendorDestBase %>js/fancytree/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>fancytree/dist/', src: ['skin-lion/*.png', 'skin-lion/*.gif'], dest: '<%= vendorDestBase %>js/fancytree/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>jquery-ui/themes/smoothness', src: ['images/*.*'], dest: '<%= vendorDestBase %>css/jqueryui/', flatten: false},
                {expand: true, cwd: '<%= bowerSrcBase %>ymf/build/dist/', src: ['*.png'], dest: '<%= vendorDestBase %>css/ymf/', flatten: false}
            ]
        },
        // copy vendor-files which must exists in specific pathes to dist
        vendors2dist: {
            files: [
                // vendors
                {expand: true, cwd: '<%= vendorDestBase %>js/', src: ['ace/**'], dest: '<%= destBase %>dist/vendors-<%= pkg.vendorversion %>/', flatten: false},
                {expand: true, cwd: '<%= vendorDestBase %>js/fancytree', src: ['jquery.fancytree.js',
                    'jquery.fancytree.dnd.js',
                    'jquery.fancytree.edit.js',
                    'jquery.fancytree.gridnav.js',
                    'jquery.fancytree.table.js',
                    'skin-lion/*.*',
                    'skin-win8/*.*'], dest: '<%= destBase %>dist/vendors-<%= pkg.vendorversion %>/fancytree/', flatten: false},
                {expand: true, cwd: '<%= vendorDestBase %>css', src: ['jqueryui/images/*.*'], dest: '<%= destBase %>dist/vendors-<%= pkg.vendorversion %>/', flatten: false},
                {expand: true, cwd: '<%= vendorDestBase %>js/mermaid', src: ['*.js'], dest: '<%= destBase %>dist/vendors-<%= pkg.vendorversion %>/mermaid/', flatten: false},
                // yaio-intern vendors
                {expand: true, cwd: '<%= vendorSrcBase %>', src: ['freemind-flash/**'], dest: '<%= destBase %>dist/vendors-<%= pkg.vendorversion %>/', flatten: false},
                {expand: true, cwd: '<%= vendorSrcBase %>js/', src: ['yaio/**'], dest: '<%= destBase %>dist/vendors-<%= pkg.vendorversion %>/', flatten: false},
                {expand: true, cwd: '<%= vendorSrcBase %>css/', src: ['yaio/**'], dest: '<%= destBase %>dist/vendors-<%= pkg.vendorversion %>/', flatten: false},

                //
                {expand: true, cwd: '<%= vendorDestBase %>html/ymf/', src: ['*.html'], dest: '<%= destBase %>yaio-explorerapp/', flatten: false},
                {expand: true, cwd: '<%= vendorDestBase %>css/ymf/', src: ['*.png'], dest: '<%= destBase %>dist/', flatten: false},
                {expand: true, cwd: '<%= vendorDestBase %>css/ymf/', src: ['*-icons-*.css'], dest: '<%= destBase %>dist/', flatten: false}
            ]
        },
        // copy archiv to dist
        archiv2dist: {
            files: [
                {expand: true, cwd: '<%= archivSrcBase %>', src: ['**'], dest: '<%= destBase %>dist/', flatten: false}
            ]
        },
        // copy files which must excists in specifc version (because exports include them) from dist to archiv
        dist2archiv: {
            files: [
                {expand: true, cwd: '<%= destBase %>dist/',
                    src: [
                        'vendors-<%= pkg.vendorversion %>/**',
                        'vendors-full-<%= pkg.vendorversion %>.js',
                        'vendors-full-<%= pkg.vendorversion %>.css',
                        '<%= pkg.name %>-reset-<%= pkg.resetversion %>.css',
                        '<%= pkg.name %>-exports-dist-<%= pkg.exportsversion %>.js',
                        '<%= pkg.name %>-exports-dist-<%= pkg.exportsversion %>.css',
                        '<%= pkg.name %>-exports-print-<%= pkg.exportsversion %>.css',
                        '<%= pkg.name %>-exports-print-dataonly-<%= pkg.exportsversion %>.css'
                    ], dest: '<%= archivSrcBase %>', flatten: false},
                {expand: true, cwd: '<%= archivSrcBase %>', src: ['**'], dest: '<%= destBase %>dist/', flatten: false}
            ]
        },
        // copy the yaio.sources to dist
        yaiores2dist: {
            files: [
                {expand: true, cwd: '<%= srcBase %>pages/', src: ['*.html'], dest: '<%= destBase %>', flatten: false},
                {expand: true, cwd: '<%= srcBase %>', src: ['yaio-explorerapp/**/*.html', 'yaio-explorerapp/**/*.json', 'yaio-explorerapp/static/**'], dest: '<%= destBase %>', flatten: false},
                {expand: true, cwd: '<%= tplSrcBase %>', src: ['exporttemplates/*.html'], dest: '<%= destBase %>', flatten: false}
            ]
        }
    };

    /**
     * patches
     **/
    function patchComments(content, srcpath) {
        var newContent = content;
        console.log('patchComments:' + srcpath);

        // convert my own html-tags in comments to @-tags
        newContent = patchSingleBlock('FeatureDomain', newContent, true);
        newContent = patchSingleBlock('FeatureConditions', newContent, true);
        newContent = patchSingleBlock('FeatureKeywords', newContent, true);
        newContent = patchSingleBlock('FeatureDescription', newContent, false);
        newContent = patchSingleBlock('FeatureResult', newContent, true);
        newContent = patchLiBlock('FeatureResult', newContent);

        newContent = switchBlocks('FeatureDomain', 'FeatureDescription', newContent);
        newContent = reintendBlock('FeatureDescription', newContent, 1);
        newContent = deleteBlock('FeatureDescription', newContent);

        newContent = intendBlock('FeatureDomain', newContent, 30);
        newContent = intendBlock('FeatureConditions', newContent, 30);
        newContent = intendBlock('FeatureKeywords', newContent, 30);
        newContent = intendBlock('FeatureResult', newContent, 30);

        newContent = intendBlock('param', newContent, 7, 30);
        newContent = intendBlock('return', newContent, 30);
        newContent = intendBlock('returns', newContent, 30);
        newContent = intendBlock('throws', newContent, 8, 30);

        newContent = intendBlock('package', newContent, 30);
        newContent = intendBlock('author', newContent, 30);
        newContent = intendBlock('category', newContent, 30);
        newContent = intendBlock('copyright', newContent, 30);
        newContent = intendBlock('license', newContent, 30);

//    console.log('patchComments2 result:' + newContent);
        return newContent;
    }
    function patchSingleBlock(block, content, flgOneline) {
        var newContent = content;
        var regExStr = '(.*?)' +
            '([\\r\\n]+[ ]+\\* *)<h4>' + block + ':<\\/h4>([\\s\\S\\r\\n]*?)([\\n\\r] +\\* +<h4>|[\\n\\r] +\\* +@|[\\n\\r] +\\*\\/)' +
            '';
        var myRegexp = new RegExp(regExStr, 'gim');
        var match;

        // iterate all matching comment-html-tags
        while (match = myRegexp.exec(newContent)) {
            // extract data
            var blockContent = match[3];
            var restBefore = RegExp.leftContext;
            var restAfter = RegExp.rightContext;

            // fit text to one line
            if (flgOneline && blockContent) {
                blockContent = blockContent.replace(/ +/, ' ').replace(/[\r\n] +\* +/g, ' ').replace(/\n/g, ' ').replace(/\r/g, ' ').replace(/^ +\* +/g, ' ').replace(/ +\* *$/g, ' ');
            }
            // convert to @-tag
            newContent = restBefore + match[1] +
                match[2] + '@' + block + ' ' + blockContent + match[4] + restAfter;
        }
        return newContent;
    }

    function switchBlocks(block1, block2, content) {
        var newContent = content;
        var regExStr = '(.*?)' +
            '([\\r\\n]+[ ]+\\* *@' + block1 + '[\\s\\S\\r\\n]*?)([\\r\\n]+[ ]+\\* *@' + block2 + '[\\s\\S\\r\\n]*?)([\\n\\r] +\\* +@|[\\n\\r] +\\*\\/)' +
            '';
        var myRegexp = new RegExp(regExStr, 'gim');
        var match;
        while (match = myRegexp.exec(newContent)) {
            var blockContent = match[3];
            var restBefore = RegExp.leftContext;
            var restAfter = RegExp.rightContext;
            newContent = restBefore + match[1] +
                match[3] + match[2] + match[4] + restAfter;
        }
        return newContent;
    }

    function deleteBlock(block, content) {
        var newContent = content;
        var regExStr = '(.*?)' +
            '([\\r\\n]+[ ]+\\* *@' + block + '[\\s\\n]*?)' +
            '';
        var myRegexp = new RegExp(regExStr, 'gim');
        var match;
        while (match = myRegexp.exec(newContent)) {
            var blockContent = match[3];
            var restBefore = RegExp.leftContext;
            var restAfter = RegExp.rightContext;
            newContent = restBefore + match[1] + restAfter;
        }
        return newContent
    }

    function intendBlock(block, content, intend, intend2) {
        var newContent = content;
        var regExStr = '(.*?)' +
            '([\\r\\n]+[ ]+\\* *)(@' + block + ') +(.*)' +
            '';
        var myRegexp = new RegExp(regExStr, 'gim');
        var match;
        while (match = myRegexp.exec(newContent)) {
            var restBefore = RegExp.leftContext + match[1];
            var restAfter = RegExp.rightContext;
            var blockContent = match[4];
            blockContent = blockContent.replace(/^[- ]+/g, '');

            newContent = restBefore + match[2];
            var paramLine = match[3];
            for (var i = paramLine.length; i < intend; i++) {
                paramLine += ' ';
            }
            if (intend2) {
                var match2 = new RegExp(' *(\\S*)[- ]*(.*)').exec(blockContent);
                if (match2) {
                    var firstWord = match2[1];
                    blockContent = match2[2];
                    paramLine += firstWord;
                    for (var i = paramLine.length; i < intend2 - 1; i++) {
                        paramLine += ' ';
                    }
                    paramLine += ' ';
                }
            }
            newContent += paramLine + blockContent + restAfter;
        }
        return newContent;
    }

    function reintendBlock(block, content) {
        var newContent = content;
        var regExStr = '(.*?)' +
            '([\\r\\n]+[ ]+\\* *@' + block + ')([\\s\\S\\r\\n]*?)([\\n\\r] +\\* +@|[\\n\\r] +\\*\\/)' +
            '';
        var myRegexp = new RegExp(regExStr, 'gim');
        var match;
        while (match = myRegexp.exec(newContent)) {
            var blockContent = match[3];
            var restBefore = RegExp.leftContext;
            var restAfter = RegExp.rightContext;

            // intend
            blockContent = blockContent.replace(/ \*     /g, ' * ');

            newContent = restBefore + match[1] +
                match[2] + blockContent + match[4] + restAfter;
        }
        return newContent;
    }

    function patchLiBlock(block, content) {
        var newContent = content;
        var regExStr = '(.*?)' +
            '[\\r\\n]+([ ]+\\* *)@' + block + ' *<ul>(.*?)<\\/ul> *[\\r\\n]'
        '';
        var myRegexp = new RegExp(regExStr, 'gim');
        var match;

        // iterate all matching comment-li-tags
        while (match = myRegexp.exec(newContent)) {
            newContent = RegExp.leftContext + match[1] + '\n';

            // extract data
            var restAfter = RegExp.rightContext;
            restAfter.replace(/^[\s]+$/gm, '');
            var praefix = match[2];
            praefix = praefix.replace(/^[\s]+$/gm, '');

            // split list by li and convert
            var srcLi = match[3];
            var listLis = srcLi.split('<li>');
            for (var i = 0; i < listLis.length; i++) {
                var li = listLis[i];
                li = li.replace(/^ +/g, '').replace(/ +$/g, '').replace(/^[\s]+$/g, '');
                if (li.length > 0) {
                    newContent += praefix + '@' + block + ' ' + li + '\n';
                }
            }

            newContent += restAfter;
        }
        return newContent;
    }

    function checkWebResOnly(srcpath) {
        if (srcpath.search(/\.js|\.css|\.html/) < 0) {
            return false;
        }
        return true;
    }

    function patchFileSlimbox2(content, srcpath) {
        if (! checkWebResOnly(srcpath)) {
            return content;
        }
        var newContent = content;
        console.log('patchFileSlimbox2:' + srcpath);
        newContent = newContent.replace(/\/\*\!/g,
            '    /*!');
        newContent = newContent.replace(/\t/g,
            '    ');
        newContent = newContent.replace(/ img\:not\(\[class\]\)/g,
            ' img.jsh-md-img');

        newContent = newContent.replace('middle = win.scrollTop() + (win.height() / 2);',
            'middle = win.scrollTop() + (window.innerHeight / 2);');
        return newContent;
    }

    function patchFileJQueryUi(content, srcpath) {
        if (!checkWebResOnly(srcpath)) {
            return content;
        }
        var newContent = content;
        console.log('patchFileJQueryUi:' + srcpath);
        newContent = newContent.replace(/url\(images\//g,
            'url(vendors-vendorversion/jqueryui/images/');
        return newContent;
    }

    function patchFileJQueryLang(content, srcpath) {
        if (!checkWebResOnly(srcpath)) {
            return content;
        }
        var newContent = content;
        console.log('patchFileJQueryLang:' + srcpath);
        newContent = newContent.replace(/'placeholder'\n\t*];/g,
            '\'placeholder\',\n\t\t\'data-tooltip\'\n\t];');
        return newContent;
    }

    function patchFileHighlightJsLang(content, srcpath) {
        if (!checkWebResOnly(srcpath)) {
            return content;
        }
        var newContent = content;
        console.log('patchFileHighlightJsLang:' + srcpath);
        newContent = newContent.replace(/\.hljs-annotation,\n.diff .hljs-header,/g,
            '.hljs-annotation,\n.hljs-template_comment,\n.diff .hljs-header,');
        return newContent;
    }
})();