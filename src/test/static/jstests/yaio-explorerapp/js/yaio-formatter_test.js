/**
 * <h4>FeatureDomain:</h4>
 *     Collaboration
 *
 * <h4>FeatureDescription:</h4>
 *     software for projectmanagement and documentation
 * 
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// 'use strict'; fails in IE because of findandreplacedomtext

/**
 * <h4>FeatureDomain:</h4>
 *     Test
 * <h4>FeatureDescription:</h4>
 *     tests for yaio-formatter.js checks formatter
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

(function () {
    describe('Modul yaio-formatter Service-Funktions (Formatter formatMarkdown)', function () {
        var source, expected;

        beforeEach(function (done) {
            // load async
            setTimeout(function () {
                // load fixture
                loadFixtures("yaio-formatter_markdown.html");
                
                // call done
                done();
            }, 1000);
            
            // set localHtmlId
            localHtmlId = 1;
        });
    
        afterEach(function () {
        });

        it( "formatMarkdown should result html", function() {
            // Given
            var src = $("#markdown_src").val();
            var expected = $("#markdown_res").html().trim();
            
            // When
            var res = formatMarkdown(src, false).trim();
            // should be regenerated to get browser-specific attr-order
            var reGeneratedRes = $("<div>" + res + "</div>").html(); 
            
            // Then
            expect(reGeneratedRes).toBe(expected);
        });
    });

    describe('Modul yaio-formatter Service-Funktions (Formatter highlightCheckList)', function () {
        var source, expected;

        beforeEach(function (done) {
            // load async
            setTimeout(function () {
                // load fixture
                loadFixtures("yaio-formatter_checklist.html");
                
                // call done
                done();
            }, 1000);
            
            // set localHtmlId
            localHtmlId = 1;
        });
    
        afterEach(function () {
        });

        it( "highlightCheckList should result checklist", function() {
            // Given
            var src = $("#markdown_src").val();
            var checkSrc = $("#checklist_src").html(formatMarkdown(src, false));
            var expected = $("#checklist_res").html().trim();
            
            // When
            highlightCheckList("#checklist_src");
            var res = $("#checklist_src").html().trim();
            
            // Then
            expect(res).toBe(expected);
        });
    });
})();
