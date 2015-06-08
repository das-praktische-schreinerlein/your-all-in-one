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
    describe('Modul yaio-formatter Service-Funktions (Formatter formatMarkdown)', function doSuiteFormatMarkdown() {
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

        it( "should format Markdown as html (formatMarkdown)", function doTestFormatMarkdown() {
            // Given
            var src = $("#markdown_src").val();
            var expected = $("#markdown_expected").html().trim();
            
            // When
            var res = formatMarkdown(src, false).trim();
            // should be regenerated to get browser-specific attr-order
            var reGeneratedRes = $("<div>" + res + "</div>").html(); 
            
            // Then
            expect(reGeneratedRes).toBe(expected);
        });
    });

    describe('Modul yaio-formatter Service-Funktions (Formatter highlightCheckList)', function doSuiteHighlightCheckList() {
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

        it( "should convert html-listentries to formatted checklist (highlightCheckList)", function doTestHighlightCheckList() {
            // Given
            var src = $("#markdown_src").val();
            var checkSrc = $("#checklist_src").html(formatMarkdown(src, false));
            var expected = $("#checklist_expected").html().trim();
            
            // When
            highlightCheckList("#checklist_src");
            var res = $("#checklist_src").html().trim();
            
            // Then
            expect(res).toBe(expected);
        });
    });

    describe('Modul yaio-formatter Service-Funktions (Formatter checklists)', function doSuiteHighlightCheckListForMatcher() {
        var doCheckHighlightCheckList = function(src, expected, formatter) {
            // Given
            $('body').html("<div id='test'>" + src + "</div>");
            
            // When
            formatter();
            var res = $("#test").html();
            
            // Expected
            expect(res).toBe(expected);
        }

        it( "should convert html-listentries to formatted checklist (highlightCheckListForMatchers)", function doTestHighlightCheckListForMatchers() {
            doCheckHighlightCheckList(
                    "<ul><li>[TEST1] - xyz</li><li>[TEST3] - xyz</li></ul>", 
                    "<ul><li><span class=\"style1\">[TEST1]</span> - xyz</li><li>[TEST3] - xyz</li></ul>", 
                    function() {
                        highlightCheckListForMatchers($("#test"), ["TEST1", "TEST2"], "style1");
            });
        });

        it( "should convert html-listentries to formatted checklist (highlightCheckListForMatcher)", function doTestHighlightCheckListForMatcher() {
            doCheckHighlightCheckList(
                    "<ul><li>[TEST1] - xyz</li><li>[TEST3] - xyz</li></ul>", 
                    "<ul><li><span class=\"style1\">[TEST1]</span> - xyz</li><li>[TEST3] - xyz</li></ul>", 
                    function() {
                        highlightCheckListForMatcher($("#test"), "[TEST1]", "style1");
            });
        });
    });

    describe('Modul yaio-formatter Service-Funktions (extract checklists)', function doSuiteExtractCheckList() {
        beforeEach(function (done) {
            // load async
            setTimeout(function () {
                // load fixture
                loadFixtures("yaio-formatter_extractchecklist.html");
                
                // call done
                done();
            }, 1000);
            
            // set localHtmlId
            localHtmlId = 1;
        });

        it( "should extract checklist from fancytree-table (convertExplorerLinesAsCheckList)", function doTestConvertExplorerLinesAsCheckList() {
            // Given
            var expected = $("#extractchecklist_expected").html().trim();
            
            // When
            var res = convertExplorerLinesAsCheckList().trim();
            
            // Then
            expect(res).toBe(expected);
        });
    });

    describe('Modul yaio-formatter Service-Funktions (extract ganttMarkdown)', function doSuiteExtractGanttMarkdown() {
        beforeEach(function (done) {
            // load async
            setTimeout(function () {
                // load fixture
                loadFixtures("yaio-formatter_extractganttmarkdown.html");
                
                // call done
                done();
            }, 1000);
            
            // set localHtmlId
            localHtmlId = 1;
        });

        it( "should extract ganttMarkdown from fancytree-table (convertExplorerLinesAsGanttMarkdown)", function doTestConvertExplorerLinesAsCheckList() {
            // Given
            var expected = $("#extractganttmarkdown_expected").html().trim();
            
            // When
            var res = convertExplorerLinesAsGanttMarkdown().trim();
            
            // Then
            expect(res).toBe(expected);
        });
    });

    describe('Modul yaio-formatter Service-Funktions (convertMarkdownToJira)', function doSuiteConvertMarkdownToJira() {
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

        it( "should convert markdown to jira (convertMarkdownToJira)", function doTestConvertMarkdownToJira() {
            // Given
            var src = $("#markdown2jira_src").val().trim();
            var expected = $("#markdown2jira_expected").val().trim();
            
            // When
            var res = convertMarkdownToJira(src).trim();
            
            // Then
            expect(res).toBe(expected);
        });
    });

    describe('Modul yaio-formatter Service-Funktions (TextPrepare)', function doSuiteTextPrepare() {
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

        it( "should pepare markdown by replacing html-tags outside code and mask empty lines (prepareTextForMarkdown)", function doTestPrepareTextForMarkdown() {
            // Given
            var src = $("#markdownPrepare_src").val().trim();
            var expected = $("#markdownPrepare_expected").html().trim();
            
            // When
            var res = prepareTextForMarkdown(src).trim();
            var reGeneratedRes = $("<div>" + res.trim() + "</div>").html(); 
            var reGeneratedExpected = $("<div>" + expected.trim() + "</div>").html(); 
            
            // Then
            expect(reGeneratedRes).toBe(reGeneratedExpected);
        });
    });
    

    describe('Modul yaio-formatter Service-Funktions (TOC)', function doSuiteTOC() {
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

        it( "should render TOC and show it if minDeep reached (addTOCForBlock)", function doTestAddTOCForBlockFull() {
            // Given
            var expected = $("#addTOCForBlockFilled_expected").html().trim();
            var expectedDisplay = "block";
            
            // When
            addTOCForBlock("#addTOCForBlockFilled", "#addTOCForBlock_src", { toc: { hide: false, minDeep: 2}});
            var res = $("#addTOCForBlockFilled").html();
            var display = $("#addTOCForBlockFilled").css("display");
            
            // extract common html
            var reGeneratedRes = $("<div>" + res.trim() + "</div>").html(); 
            var reGeneratedExpected = $("<div>" + expected.trim() + "</div>").html(); 
            reGeneratedExpected = reGeneratedExpected.replace(/href="" /g, '');
            reGeneratedRes = reGeneratedRes.replace(/href="" /g, '');
            
            // Then
            expect(display).toBe(expectedDisplay);
            expect(reGeneratedRes).toBe(reGeneratedExpected);
        });

        it( "should not show TOC if minDeep not reached (addTOCForBlock)", function doTestAddTOCForBlockEmpty() {
            // Given
            var expectedDisplay = "none";
            
            // When
            addTOCForBlock("#addTOCForBlockEmpty", "#addTOCForBlock_src", { toc: { hide: false, minDeep: 6}});
            var display = $("#addTOCForBlockEmpty").css("display");
            
            // Then
            expect(display).toBe(expectedDisplay);
        });
    });

})();
