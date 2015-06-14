/**
 * test the ExplorerPage of YAIO with a full lifecycle of a node
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
            yaioNodePage.navigateToJsFuncTest1ndOpenCreateNode().then(function () {
                var newTaskNameElement = yaioNodePage.createTaskNode();
                yaioNodePage.extractNodeIdFromTaskNameElement(newTaskNameElement).then(function (taskId) {
                    var editTaskNameElement = yaioNodePage.editTaskNodeById(taskId);
                    yaioNodePage.extractNodeIdFromTaskNameElement(editTaskNameElement).then(function (taskId) {
                        yaioNodePage.deleteNodeById(taskId);
                    });
                });
            });
        })
    });
});

