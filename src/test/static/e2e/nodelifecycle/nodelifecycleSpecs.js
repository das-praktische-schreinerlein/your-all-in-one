/**
 * test the LoginPage of YAIO
 * 
 */

'use strict';
var YAIOLoginPage = require('../login/login.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');
var YAIONodePage = require('../explorer/node.po.js');

describe('yaio explorer', function() {

    var yaioLoginPage = new YAIOLoginPage();
    var yaioFrontPage = new YAIOFrontPage();
    var yaioNodePage = new YAIONodePage();
    
    beforeEach(function() {
        yaioLoginPage.doLogin();
    });
    afterEach(function() {
        //yaioLoginPage.doLogout();
    });
    
    it('should create/edit/delete a new node', function() {
        // expect frontPage
        protractor.utils.waitUntilElementPresent(yaioFrontPage.fontContentLeft, 2000);
        expect(yaioFrontPage.fontContentLeft.getAttribute('id')).toEqual("front-content-left");
        
        // open explorer-link
        yaioFrontPage.linkExplorer.click();
        
        // expect SysPlay1
        protractor.utils.waitUntilElementPresent(yaioNodePage.expanderSysPlay1, 2000);
        expect(yaioNodePage.expanderSysPlay1.getAttribute('id')).toEqual('expanderSysPlay1');
        
        // expand SysPlay1
        yaioNodePage.expanderSysPlay1.click();
        protractor.utils.waitUntilElementPresent(yaioNodePage.linkCreateChildSysTest1, 2000);
        expect(yaioNodePage.linkCreateChildSysTest1.getAttribute('id')).toEqual('cmdCreateSysTest1');
        
        // create child for SysTest1
        yaioNodePage.linkCreateChildSysTest1.click();
        protractor.utils.waitUntilElementPresent(yaioNodePage.inputCreateNodeType, 2000);
        expect(yaioNodePage.inputCreateNodeType.getAttribute('id')).toEqual('inputCreateNodeType');

        // select Aufgabe
        yaioNodePage.inputCreateNodeType.sendKeys('Aufgabe\n');
        expect(yaioNodePage.inputNameTaskNode.getAttribute('id')).toEqual('inputNameTaskNode');
        
        // set taskdata and submit form
        var taskName = 'testask' + new Date().getTime();
        yaioNodePage.inputNameTaskNode.sendKeys(taskName);
        yaioNodePage.inputTypeTaskNode.sendKeys("+-- Offen\n");
        yaioNodePage.inputPlanAufwandTaskNode.sendKeys("1");
        yaioNodePage.inputPlanStartTaskNode.click();
        protractor.utils.waitUntilElementPresent(yaioNodePage.uiDatePickerDay1, 2000);
        yaioNodePage.uiDatePickerDay1.click();
        yaioNodePage.inputPlanEndeTaskNode.click();
        protractor.utils.waitUntilElementPresent(yaioNodePage.uiDatePickerDay25, 2000);
        yaioNodePage.uiDatePickerDay25.click();
        
        // toggle desc
//        yaioNodePage.filterDescTaskForm_Off.click().then(function() {
//          protractor.utils.waitUntilElementPresent(yaioNodePage.inputNodeDescTaskNode, 2000);
//          expect(yaioNodePage.inputNodeDescTaskNode.getAttribute('id')).toEqual('inputNodeDescTaskNode');
//          yaioNodePage.inputNodeDescTaskNode.sendKeys("fehlerhafte Tetsdaten");
//        });

        // submit form
        protractor.utils.waitUntilElementPresent(yaioNodePage.buttonSaveTask, 20000);
        expect(yaioNodePage.buttonSaveTask.isDisplayed()).toEqual(true);
        yaioNodePage.buttonSaveTask.click().then(function () {
            // wait for result
            browser.ignoreSynchronization = true;

            // wait till data is loaded
            protractor.utils.waitUntilElementPresent(yaioNodePage.linkCreateChildSysTest1, 10000);
            expect(yaioNodePage.linkCreateChildSysTest1.getAttribute('id')).toEqual('cmdCreateSysTest1');
            
            // wait till data is loaded
            var eleNewTaskName = element(by.cssContainingText('span.fancytree-title2', taskName));
            expect(eleNewTaskName.getText()).toEqual(taskName);
            browser.ignoreSynchronization = false;
        });

        /**
-->
<tr>
    <td>select</td>
    <td>id=inputTypeTaskNode</td>
    <td>label=+-- Offen</td>
</tr>
<tr>
    <td>type</td>
    <td>id=inputPlanAufwandTaskNode</td>
    <td>1</td>
</tr>
<tr>
    <td>click</td>
    <td>id=inputPlanStartTaskNode</td>
    <td></td>
</tr>
<tr>
    <td>click</td>
    <td>link=1</td>
    <td></td>
</tr>
<tr>
    <td>click</td>
    <td>id=inputPlanEndeTaskNode</td>
    <td></td>
</tr>
<tr>
    <td>click</td>
    <td>link=25</td>
    <td></td>
</tr>
<tr>
    <td>type</td>
    <td>id=inputNodeDescTaskNode</td>
    <td>fehlerhafte Tetsdaten</td>
</tr>
<tr>
    <td>click</td>
    <td>//form[@id='nodeFormTaskNode']/fieldset[4]/button</td>
    <td></td>
</tr>
<!-- 
  #######################################################
    wait for testTask 
  #######################################################
-->
<tr>
    <td>waitForElementPresent</td>
    <td>//span[text() = '${taskname}']</td>
    <td></td>
</tr>
<tr>
    <td>storeAttribute</td>
    <td>//span[text() = '${taskname}']@id</td>
    <td>tasknameid</td>
</tr>
<tr>
    <td>store</td>
    <td>javascript{ storedVars['tasknameid'].replace(&quot;title&quot;, &quot;&quot;); }</td>
    <td>nodeIdCreated</td>
</tr>
<tr>
    <td>echo</td>
    <td>id=${nodeIdCreated} of new task with taskname: ${taskname}</td>
    <td></td>
</tr>
<!-- 
  #######################################################
    wait and open editEditor for edit Task
  #######################################################
-->
<tr>
    <td>store</td>
    <td>${nodeIdCreated}</td>
    <td>nodeIdforEdit</td>
</tr>
<tr>
    <td>echo</td>
    <td>open editform for ${nodeIdforEdit}</td>
    <td></td>
</tr>
<tr>
    <td>waitForElementPresent</td>
    <td>dom=document.getElementById(&quot;cmdEdit${nodeIdforEdit}&quot;)</td>
    <td></td>
</tr>
<tr>
    <td>click</td>
    <td>dom=document.getElementById(&quot;cmdEdit${nodeIdforEdit}&quot;)</td>
    <td></td>
</tr>
<!-- 
  #######################################################
    edit Task with new name, date, xdesc 
  #######################################################
-->
<tr>
    <td>echo</td>
    <td>edit task with taskname: ${nodeIdforEdit}</td>
    <td></td>
</tr>
<tr>
    <td>type</td>
    <td>id=inputIstAufwandTaskNode</td>
    <td>100</td>
</tr>
<tr>
    <td>click</td>
    <td>id=inputIstStartTaskNode</td>
    <td></td>
</tr>
<tr>
    <td>click</td>
    <td>link=1</td>
    <td></td>
</tr>
<tr>
    <td>click</td>
    <td>id=inputIstEndeTaskNode</td>
    <td></td>
</tr>
<tr>
    <td>click</td>
    <td>link=25</td>
    <td></td>
</tr>
<tr>
    <td>type</td>
    <td>id=inputNodeDescTaskNode</td>
    <td>korrigierte Testdaten</td>
</tr>
<tr>
    <td>click</td>
    <td>//form[@id='nodeFormTaskNode']/fieldset[4]/button</td>
    <td></td>
</tr>
<!-- 
  #######################################################
    show SysPlay1 
  #######################################################
-->
<tr>
    <td>store</td>
    <td>SysTest1</td>
    <td>nodeIdforShow</td>
</tr>
<tr>
    <td>echo</td>
    <td>show for ${nodeIdforShow}</td>
    <td></td>
</tr>
<tr>
    <td>waitForElementPresent</td>
    <td>css=a[href*='#/show/${nodeIdforShow}']</td>
    <td></td>
</tr>
<tr>
    <td>click</td>
    <td>css=a[href*='#/show/${nodeIdforShow}']</td>
    <td></td>
</tr>
<tr>
    <td>waitForElementPresent</td>
    <td>//tr[@data-value = '${nodeIdforShow}']</td>
    <td></td>
</tr>
<!-- 
  #######################################################
    wait for testTask and remove 
  #######################################################
-->
<tr>
    <td>store</td>
    <td>${nodeIdCreated}</td>
    <td>nodeIdForDelete</td>
</tr>
<tr>
    <td>waitForElementPresent</td>
    <td>dom=document.getElementById(&quot;cmdCreate${nodeIdForDelete}&quot;)</td>
    <td></td>
</tr>
<tr>
    <td>click</td>
    <td>dom=document.getElementById(&quot;cmdRemove${nodeIdForDelete}&quot;)</td>
    <td></td>
</tr>
<tr>
    <td>storeConfirmation</td>
    <td>Wollen Sie die Node wirklich löschen?</td>
    <td></td>
</tr>
<!-- 
  #######################################################
    show SysPlay1
  #######################################################
-->
<tr>
    <td>store</td>
    <td>SysPlay1</td>
    <td>nodeIdforShow</td>
</tr>
<tr>
    <td>echo</td>
    <td>show for ${nodeIdforShow}</td>
    <td></td>
</tr>
<tr>
    <td>waitForElementPresent</td>
    <td>css=a[href*='#/show/${nodeIdforShow}']</td>
    <td></td>
</tr>
<tr>
    <td>click</td>
    <td>css=a[href*='#/show/${nodeIdforShow}']</td>
    <td></td>
</tr>
<tr>
    <td>waitForElementPresent</td>
    <td>//tr[@data-value = '${nodeIdforShow}']</td>
    <td></td>
</tr>
<!-- 
  #######################################################
    show MasterplanMasternode1 
  #######################################################
-->
<tr>
    <td>store</td>
    <td>MasterplanMasternode1</td>
    <td>nodeIdforShow</td>
</tr>
<tr>
    <td>echo</td>
    <td>show for ${nodeIdforShow}</td>
    <td></td>
</tr>
<tr>
    <td>waitForElementPresent</td>
    <td>css=a[href*='#/show/${nodeIdforShow}']</td>
    <td></td>
</tr>
<tr>
    <td>click</td>
    <td>css=a[href*='#/show/${nodeIdforShow}']</td>
    <td></td>
</tr>
<tr>
    <td>waitForElementPresent</td>
    <td>//tr[@data-value = '${nodeIdforShow}']</td>
    <td></td>
</tr>
 */
    });
});

