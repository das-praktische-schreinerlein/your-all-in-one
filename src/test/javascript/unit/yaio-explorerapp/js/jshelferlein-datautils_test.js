/** 
 * software for projectmanagement and documentation
 * 
 * @FeatureDomain                Collaboration 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

'use strict';

/** 
 * tests for jshelferlein-datautils.js
 *  
 * @FeatureDomain                Test
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

(function () {
    var testObj = yaioAppBase.get('DataUtils');
    
    describe('Modul jshelferlein-datautils Service-Funktions (htmlEscapeText)', function doSuiteHtmlEscapeText() {
        it( "check htmlEscapeText", function doTestHtmlEscapeText() {
            var res = testObj.htmlEscapeText("<>/&'\"");
            expect(res).toBe("&lt;&gt;&#x2F;&amp;&#x27;&quot;");
        });
    });
        
    describe('Modul jshelferlein-datautils Service-Funktions (formatGermanDate)', function doSuiteFormatGermanDate() {
        it( "check formatGermanDate 1ms", function doTestFormatGermanDate1ms() {
            var res = testObj.formatGermanDate(1);
            expect(res).toBe("01.01.1970");
        });
        
        it( "check formatGermanDate 2147483647000ms", function doTestFormatGermanDate2147483647000ms() {
            var res = testObj.formatGermanDate(2147483647000);
            expect(res).toBe("19.01.2038");
        });
    });
    
    describe('Modul jshelferlein-datautils Service-Funktions (padNumber)', function doSuitePadNumber() {
        
        it( "check padNumber null", function doTestPadNumberNull() {
            var res = testObj.padNumber("", 2);
            expect(res).toBe("00");
        });
        
        it( "check padNumber 1", function  doTestPadNumber1() {
            var res = testObj.padNumber(1, 2);
            expect(res).toBe("01");
        });
        
        it( "check padNumber 10", function  doTestPadNumber10() {
            var res = testObj.padNumber(10, 2);
            expect(res).toBe("10");
        });
        
        it( "check padNumber 100", function  doTestPadNumber100() {
            var res = testObj.padNumber(100, 2);
            expect(res).toBe("100");
        });
        
    });
    
    describe('Modul jshelferlein-datautils Service-Funktions (formatNumbers)', function doSuiteFormatNumbers() {
        it( "check formatNumbers 100.245 with 2", function doTestFormatNumbersFull() {
            var res = testObj.formatNumbers(100.245, 2, "");
            expect(res).toBe("100.25");
        });
        
        it( "check formatNumbers 100.245 with 0", function doTestFormatNumbers0() {
            var res = testObj.formatNumbers(100.245, 0, "");
            expect(res).toBe("100");
        });
        
        it( "check formatNumbers 100.245 with 4", function doTestFormatNumbersMore() {
            var res = testObj.formatNumbers(100.245, 4, "")
            expect(res).toBe("100.2450");
        });
        
        it( "check formatNumbers 100.245 with 4 + h", function doTestFormatNumbersSuffix() {
            var res = testObj.formatNumbers(100.245, 4, "h");
            expect(res).toBe("100.2450h");
        });
        
        it( "check formatNumbers null", function doTestFormatNumbersNull() {
            var res = testObj.formatNumbers(null, 4, "h");
            expect(res).toBe("");
        });
    });
})();
