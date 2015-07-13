/**
 * test the gantt-elements of YAIO
 * 
 */

'use strict';
var YAIOLoginPage = require('../login/login.po.js');
var YAIONodePage = require('../explorer/node.po.js');
var YAIOGanttPage = require('../gantt/gantt.po.js');

describe('yaio gantt', function() {
    // define vars
    var yaioLoginPage, yaioNodePage, yaioGanttPage;

    /**
     * prepare tests
     */
    beforeEach(function() {
        // initpages (reset elements)
        yaioLoginPage = new YAIOLoginPage();
        yaioNodePage = new YAIONodePage();
        yaioGanttPage = new YAIOGanttPage();
        
        // do Login
        yaioLoginPage.checkLogin()
            .then(function doneOpenExplorer() {
                // open explorer
                return yaioNodePage.openExplorerFromFrontPage();
            });
        protractor.utils.waitUntilElementClickable($(yaioNodePage.containerMasterdata), protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect($(yaioNodePage.containerMasterdata).isPresent()).toEqual(true);
        browser.get(browser.params.yaioConfig.yaioBaseAppUrl + '/show/' + yaioGanttPage.ganttId);
        protractor.utils.waitUntilElementClickable($(yaioNodePage.containerMasterdata), protractor.utils.CONST_WAIT_NODEHIRARCHY);
    });

    /**
     * cleanup after tests
     */
    afterEach(function() {
        browser.ignoreSynchronization = false;
    });
    
    /**
     * define tests
     */
    it('should switch timeframe default/plan/ist and check that gantt-bars change', function doSwitchTimeFrame() {
        var ganttId2 = 'DT2015061620443947716';
        
        // switch to gantt
        return $(yaioGanttPage.linkTabTogglerGantt).click()
            .then(function showGantt()  {
                // show gantt
                protractor.utils.waitUntilElementVisible($(yaioGanttPage.linkTabTogglerData), protractor.utils.CONST_WAIT_ELEMENT);
                expect($(yaioGanttPage.inputGanttRangeStart).getAttribute('value')).toBe(protractor.utils.formatGermanDate((new Date()).getTime() - 90*24*60*60*1000));
                expect($(yaioGanttPage.inputGanttRangeEnde).getAttribute('value')).toBe(protractor.utils.formatGermanDate((new Date()).getTime() + 90*24*60*60*1000));

                // switch to plan
                return $(yaioGanttPage.buttonRecalcGanttPlan).click();
            })
            .then(function showData()  {
                // show plan
                expect($(yaioGanttPage.inputGanttRangeStart).getAttribute('value')).toBe("01.03.2015");
                expect($(yaioGanttPage.inputGanttRangeEnde).getAttribute('value')).toBe("10.10.2025");
                
                // check gantt-lines
                yaioGanttPage.checkGanttLine(yaioGanttPage.ganttId, {aufwand: "25h"}, {aufwand: "12h"});
                yaioGanttPage.checkGanttLine(ganttId2, 
                        {aufwand: "10h", style: "width: 1px; margin-left: 0px;"}, 
                        {aufwand: "8h", style: "width: 0px; margin-left: 0px;"});
                
                // switch to ist
                return $(yaioGanttPage.buttonRecalcGanttIst).click();
            })
            .then(function showData()  {
                // show ist
                expect($(yaioGanttPage.inputGanttRangeStart).getAttribute('value')).toBe("04.03.2015");
                expect($(yaioGanttPage.inputGanttRangeEnde).getAttribute('value')).toBe("04.04.2015");

                // check gantt-lines
                yaioGanttPage.checkGanttLine(yaioGanttPage.ganttId, {aufwand: "12h"}, {aufwand: "12h"});
                yaioGanttPage.checkGanttLine(ganttId2, 
                        {aufwand: "7h", style: "width: 92px; margin-left: 0px;"}, 
                        {aufwand: "8h", style: "width: 19px; margin-left: 0px;"});
            });
    });

    it('should switch to gantt-view and back', function doSwitchGantt() {
        // Given
        expect($(yaioGanttPage.linkTabTogglerGantt).isDisplayed()).toBe(true);
        expect($(yaioGanttPage.linkTabTogglerData).isDisplayed()).toBe(false);
        expect($(yaioGanttPage.masterGantt).isDisplayed()).toBe(false);
        expect($(yaioGanttPage.masterData).isDisplayed()).toBe(true);
        
        // switch to gantt
        return $(yaioGanttPage.linkTabTogglerGantt).click()
            .then(function showGantt()  {
                // show gantt
                protractor.utils.waitUntilElementVisible($(yaioGanttPage.linkTabTogglerData), protractor.utils.CONST_WAIT_ELEMENT);
                expect($(yaioGanttPage.linkTabTogglerGantt).isDisplayed()).toBe(false);
                expect($(yaioGanttPage.linkTabTogglerData).isDisplayed()).toBe(true);
                expect($(yaioGanttPage.masterGantt).isDisplayed()).toBe(true);
                expect($(yaioGanttPage.masterData).isDisplayed()).toBe(false);

                // switch back to data
                return $(yaioGanttPage.linkTabTogglerData).click();
            })
            .then(function showData()  {
                // show data
                protractor.utils.waitUntilElementVisible($(yaioGanttPage.linkTabTogglerGantt), protractor.utils.CONST_WAIT_ELEMENT);
                expect($(yaioGanttPage.linkTabTogglerGantt).isDisplayed()).toBe(true);
                expect($(yaioGanttPage.linkTabTogglerData).isDisplayed()).toBe(false);
                expect($(yaioGanttPage.masterGantt).isDisplayed()).toBe(false);
                expect($(yaioGanttPage.masterData).isDisplayed()).toBe(true);
            });
    });
});

