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
        
        yaioNodePage.openExplorerFromFrontPage().then(function () {
            yaioNodePage.navigateToSysTest1AndOpenCreateNode().then(function (){
                yaioNodePage.createTaskNode();
            });
        })

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

