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

'use strict';

/**
 * <h4>FeatureDomain:</h4>
 *     Test
 * <h4>FeatureDescription:</h4>
 *     tests for yaio-baseservice.js
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

(function () {
    describe('Modul yaio-baseservice Service-Funktions (htmlEscapeText)', function () {
        it( "check htmlEscapeText", function( ) {
            var res = htmlEscapeText("<>/&'\"");
            expect(res).toBe("&lt;&gt;&#x2F;&amp;&#x27;&quot;");
        });
    });
        
    describe('Modul yaio-baseservice Service-Funktions (formatGermanDate)', function () {
        it( "check formatGermanDate 1ms", function( ) {
            var res = formatGermanDate(1);
            expect(res).toBe("01.01.1970");
        });
        
        it( "check formatGermanDate 2147483647000ms", function( ) {
            var res = formatGermanDate(2147483647000);
            expect(res).toBe("19.01.2038");
        });
    });
    
    describe('Modul yaio-baseservice Service-Funktions (padNumber)', function () {
        
        it( "check padNumber null", function( ) {
            var res = padNumber("", 2);
            expect(res).toBe("00");
        });
        
        it( "check padNumber 1", function( ) {
            var res = padNumber(1, 2);
            expect(res).toBe("01");
        });
        
        it( "check padNumber 10", function( ) {
            var res = padNumber(10, 2);
            expect(res).toBe("10");
        });
        
        it( "check padNumber 100", function( ) {
            var res = padNumber(100, 2);
            expect(res).toBe("100");
        });
        
    });
    
    describe('Modul yaio-baseservice Service-Funktions (formatNumbers)', function () {
        it( "check formatNumbers 100.245 with 2", function( ) {
            var res = formatNumbers(100.245, 2, "");
            expect(res).toBe("100.25");
        });
        
        it( "check formatNumbers 100.245 with 0", function( ) {
            var res = formatNumbers(100.245, 0, "");
            expect(res).toBe("100");
        });
        
        it( "check formatNumbers 100.245 with 4", function( ) {
            var res = formatNumbers(100.245, 4, "")
            expect(res).toBe("100.2450");
        });
        
        it( "check formatNumbers 100.245 with 4 + h", function( ) {
            var res = formatNumbers(100.245, 4, "h");
            expect(res).toBe("100.2450h");
        });
        
        it( "check formatNumbers null", function( ) {
            var res = formatNumbers(null, 4, "h");
            expect(res).toBe("");
        });
    });
})();
