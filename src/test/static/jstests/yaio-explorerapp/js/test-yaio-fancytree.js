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
 *     tests for yaio-fancytree.js
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

/**
 * checks businesslogic
 */
QUnit.module("Modul yaio-fancytree Service-Funktions (businesslogic)", {
    setup: function () {
        this.basenode = {
                istStand: 0    
        }
    },
    teardown: function () {
    }
});
QUnit.test( "check calcIstStandFromState", function( assert ) {
    var defaultIstStand = 0;

    this.basenode.istStand = defaultIstStand;
    this.basenode.type = "RUNNING";
    assert.equal( calcIstStandFromState(this.basenode), defaultIstStand, "calcIstStandFromState 'RUNNING " + defaultIstStand + "'");

    this.basenode.istStand = defaultIstStand;
    this.basenode.type = "OFFEN";
    assert.equal( calcIstStandFromState(this.basenode), defaultIstStand, "calcIstStandFromState 'OFFEN " + defaultIstStand + "'");

    this.basenode.istStand = defaultIstStand;
    this.basenode.type = "ERLEDIGT";
    assert.equal( calcIstStandFromState(this.basenode), 100, "calcIstStandFromState 'ERLEDIGT 100'");

    this.basenode.istStand = defaultIstStand;
    this.basenode.type = "EVENT_ERLEDIGT";
    assert.equal( calcIstStandFromState(this.basenode), 100, "calcIstStandFromState 'EVENT_ERLEDIGT 100'");

    this.basenode.istStand = defaultIstStand;
    this.basenode.type = "VERWORFEN";
    assert.equal( calcIstStandFromState(this.basenode), 100, "calcIstStandFromState 'VERWORFEN 100'");
    
    this.basenode.istStand = defaultIstStand;
    this.basenode.type = "EVENT_VERWORFEN";
    assert.equal( calcIstStandFromState(this.basenode), 100, "calcIstStandFromState 'EVENT_VERWORFEN 100'");
});

QUnit.test( "check calcTypeFromIstStand", function( assert ) {
    this.basenode.istStand = 0;
    this.basenode.className = "TaskNode";
    this.basenode.type = "";
    assert.equal( calcTypeFromIstStand(this.basenode), "OFFEN", "calcTypeFromIstStand 'TaskNode OFFEN 0'");

    this.basenode.istStand = 0;
    this.basenode.className = "EventNode";
    this.basenode.type = "";
    assert.equal( calcTypeFromIstStand(this.basenode), "EVENT_PLANED", "calcTypeFromIstStand 'EventNode EVENT_PLANED 0'");

    
    this.basenode.istStand = 10;
    this.basenode.className = "TaskNode";
    this.basenode.type = "";
    assert.equal( calcTypeFromIstStand(this.basenode), "RUNNING", "calcTypeFromIstStand 'TaskNode RUNNING 10'");

    this.basenode.istStand = 10;
    this.basenode.className = "EventNode";
    this.basenode.type = "";
    assert.equal( calcTypeFromIstStand(this.basenode), "EVENT_RUNNING", "calcTypeFromIstStand 'EventNode EVENT_RUNNING 10'");

    
    this.basenode.istStand = 10;
    this.basenode.className = "TaskNode";
    this.basenode.type = "WARNING";
    assert.equal( calcTypeFromIstStand(this.basenode), "WARNING", "calcTypeFromIstStand 'TaskNode WARNING 10'");

    this.basenode.istStand = 10;
    this.basenode.className = "EventNode";
    this.basenode.type = "EVENT_WARNING";
    assert.equal( calcTypeFromIstStand(this.basenode), "EVENT_WARNING", "calcTypeFromIstStand 'EventNode EVENT_SHORT 10'");

    
    this.basenode.istStand = 100;
    this.basenode.className = "TaskNode";
    this.basenode.type = "";
    assert.equal( calcTypeFromIstStand(this.basenode), "ERLEDIGT", "calcTypeFromIstStand 'TaskNode ERLEDIGT 100'");

    this.basenode.istStand = 100;
    this.basenode.className = "EventNode";
    this.basenode.type = "";
    assert.equal( calcTypeFromIstStand(this.basenode), "EVENT_ERLEDIGT", "calcTypeFromIstStand 'EventNode EVENT_ERLEDIGT 100'");

    
    this.basenode.istStand = 100;
    this.basenode.className = "TaskNode";
    this.basenode.type = "VERWORFEN";
    assert.equal( calcTypeFromIstStand(this.basenode), "VERWORFEN", "calcTypeFromIstStand 'TaskNode VERWORFEN 100'");

    this.basenode.istStand = 100;
    this.basenode.className = "EventNode";
    this.basenode.type = "EVENT_VERWORFEN";
    assert.equal( calcTypeFromIstStand(this.basenode), "EVENT_VERWORFEN", "calcTypeFromIstStand 'EventNode EVENT_VERWORFEN 100'");


    this.basenode.istStand = 100;
    this.basenode.className = "EventNode";
    this.basenode.type = "VERWORFEN";
    assert.notEqual( calcTypeFromIstStand(this.basenode), "VERWORFEN", "calcTypeFromIstStand 'not EventNode VERWORFEN 100'");
});


/**
 * checks layout-helper
 */
QUnit.module("Modul yaio-fancytree Service-Funktions (layout)", {
    setup: function () {
        var $div = $("<div id='testDiv' style='width: 200px;'/>").append("blabla");
        $("body").append($div);
    },
    teardown: function () {
    }
});
QUnit.asyncTest( "asynchronous test: check toggleTableBlock", function( assert ) {
    // wait till the 3 results are back
    expect(3);
    
    // check default
    assert.equal( $("#testDiv").css("display"), "block", "#testDiv.display==block" );

    // toggle Block
    toggleTableBlock("#testDiv");
    
    // check 1s later
    setTimeout(function() {
        assert.equal( $("#testDiv").css("display"), "none", "#testDiv.display==none" );

        // toggle back
        toggleTableBlock("#testDiv");

        // check 1s later
        setTimeout(function() {
            assert.equal( $("#testDiv").css("display"), "block", "#testDiv.display==block" );

            // back to qunit
            setTimeout(function() {
                QUnit.start();
            }, 1000);
        }, 1000);

    }, 1000);

});






/**
 * checks data-helper
 */
QUnit.module("Modul yaio-fancytree Service-Funktions (data)", {
    setup: function () {
    },
    teardown: function () {
    }
});
QUnit.test( "check htmlEscapeText", function( assert ) {
    assert.equal( htmlEscapeText("<>/&'\""), "&lt;&gt;&#x2F;&amp;&#x27;&quot;", "htmlEscapeText '&lt;&gt;&#x2F;&amp;&#x27;&quot;'");
});
QUnit.test( "check formatGermanDate", function( assert ) {
    assert.equal( formatGermanDate(1), "01.01.1970", "formatGermanDate '01.01.1970'");
    assert.equal( formatGermanDate(2147483647000), "19.01.2038", "formatGermanDate '19.01.2038'");
});
QUnit.test( "check padNumber", function( assert ) {
    assert.equal( padNumber("", 2), "00", "padNumber '00'");
    assert.equal( padNumber(1, 2), "01", "padNumber '01'");
    assert.equal( padNumber(10, 2), "10", "padNumber '10'");
    assert.equal( padNumber(100, 2), "100", "padNumber '100'");
});
QUnit.test( "check formatNumbers", function( assert ) {
    assert.equal( formatNumbers(100.245, 2, ""), "100.25", "formatNumbers '100.25'");
    assert.equal( formatNumbers(100.245, 0, ""), "100", "formatNumbers '100'");
    assert.equal( formatNumbers(100.245, 4, ""), "100.2450", "formatNumbers '100.2450'");
    assert.equal( formatNumbers(100.245, 4, "h"), "100.2450h", "formatNumbers '100.2450h'");
    assert.equal( formatNumbers(null, 4, "h"), "", "formatNumbers ''");
});
