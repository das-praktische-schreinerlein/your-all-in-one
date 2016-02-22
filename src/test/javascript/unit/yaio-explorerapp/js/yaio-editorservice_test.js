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
 * tests for yaio-editorservice.js checks businesslogic
 *  
 * @FeatureDomain                Test
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

(function () {
    var testObj = yaioAppBase.get('YaioEditor');
    
    describe('Modul yaio-editorservice Service-Funktions (businesslogic calcIstStandFromState)', function () {
        var doCheckCalcIstStandFromState = function(basenode, state, stand) {
            // Given
            basenode.istStand = 0;
            basenode.type = state;
            
            // When
            var res = testObj.calcIstStandFromState(basenode);
            
            // Expected
            expect(res).toBe(stand);
        };
        
        beforeEach(function () {
            this.basenode = {
                    istStand: 0
            };
        });

        it( 'check calcIstStandFromState', function() {
            doCheckCalcIstStandFromState(this.basenode, 'OFFEN', 0);
        });
        
        it( 'check calcIstStandFromState', function() {
            doCheckCalcIstStandFromState(this.basenode, 'ERLEDIGT', 100);
        });
        
        it( 'check calcIstStandFromState', function() {
            doCheckCalcIstStandFromState(this.basenode, 'EVENT_ERLEDIGT', 100);
        });
        
        it( 'check calcIstStandFromState', function() {
            doCheckCalcIstStandFromState(this.basenode, 'VERWORFEN', 100);
        });
        
        it( 'check calcIstStandFromState', function() {
            doCheckCalcIstStandFromState(this.basenode, 'EVENT_VERWORFEN', 100);
        });
    });

    describe('Modul yaio-editorservice Service-Funktions (businesslogic calcTypeFromIstStand)', function () {
        var doCheckCalcTypeFromIstStand = function(basenode, istStand, className, type, resType) {
            // Given
            basenode.istStand = istStand;
            basenode.className = className;
            basenode.type = type;
            
            // When
            var res = testObj.calcTypeFromIstStand(basenode);
            
            // Expected
            expect(res).toBe(resType);
        };

        beforeEach(function () {
            this.basenode = {
                    istStand: 0
            };
        });


        it( 'check calcTypeFromIstStand Event 0', function( ) {
            doCheckCalcTypeFromIstStand(this.basenode, 0, 'EventNode', '', 'EVENT_PLANED');
        });
        
        it( 'check calcTypeFromIstStand Task 10', function() {
            doCheckCalcTypeFromIstStand(this.basenode, 10, 'TaskNode', '', 'RUNNING');
        });
        
        it( 'check calcTypeFromIstStand Event 10', function() {
            doCheckCalcTypeFromIstStand(this.basenode, 10, 'EventNode', '', 'EVENT_RUNNING');
        });
        
        it( 'check calcTypeFromIstStand Task 10 Warn', function() {
            doCheckCalcTypeFromIstStand(this.basenode, 10, 'TaskNode', 'WARNING', 'WARNING');
        });
        
        it( 'check calcTypeFromIstStand Event 10 Warn', function() {
            doCheckCalcTypeFromIstStand(this.basenode, 10, 'EventNode', 'EVENT_WARNING', 'EVENT_WARNING');
        });
        
        it( 'check calcIstStandFromState Task 100', function() {
            doCheckCalcTypeFromIstStand(this.basenode, 100, 'TaskNode', '', 'ERLEDIGT');
        });
        
        it( 'check calcIstStandFromState Event 100', function() {
            doCheckCalcTypeFromIstStand(this.basenode, 100, 'EventNode', '', 'EVENT_ERLEDIGT');
        });
        
        it( 'check calcIstStandFromState Task 100 Verworfen', function() {
            doCheckCalcTypeFromIstStand(this.basenode, 100, 'TaskNode', 'VERWORFEN', 'VERWORFEN');
        });
        
        it( 'check calcIstStandFromState Event 100 Verworfen', function() {
            doCheckCalcTypeFromIstStand(this.basenode, 100, 'EventNode', 'EVENT_VERWORFEN', 'EVENT_VERWORFEN');
        });
        
        it( 'check calcIstStandFromState Event 100 Verworfen/Erledigt', function() {
            doCheckCalcTypeFromIstStand(this.basenode, 100, 'EventNode', 'VERWORFEN', 'EVENT_ERLEDIGT');
        });
    });
})();
