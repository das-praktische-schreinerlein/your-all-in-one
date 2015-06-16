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
        // do Login
        yaioLoginPage.doLogin()
        .then(function doneOpenExplorer() {
            // open explorer
            yaioNodePage.openExplorerFromFrontPage();
        });
        protractor.utils.waitUntilElementClickable(yaioNodePage.containerMasterdata, protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect(yaioNodePage.containerMasterdata.isPresent()).toEqual(true);
    });
    afterEach(function() {
        yaioLoginPage.doLogout();
    });
    
    it('should create/edit/delete a new node', function doCreateEditDeleteTask() {
        // navigate to Node
        yaioNodePage.navigateToNode(yaioNodePage.jsFuncTestHierarchy)
        .then(function doneNavigate(){
            // create task
            var deferred = protractor.promise.defer();
            var newTaskNameElement = yaioNodePage.openNodeEditorAndCreateTaskNode(yaioNodePage.jsFuncTestId);
            newTaskNameElement.getText().then(function() {
                deferred.fulfill(newTaskNameElement);
            })
            
            return deferred.promise;
        })
        .then(function doneCreateTask(newTaskNameElement) {
            // extract nodeid from new task
            return yaioNodePage.extractNodeIdFromTaskNameElement(newTaskNameElement);
        })
        .then(function doneExtractNodeId(taskId) {
            // edit new task
            var deferred = protractor.promise.defer();
            var editTaskNameElement = yaioNodePage.editTaskNodeById(taskId);
            editTaskNameElement.getText().then(function() {
                deferred.fulfill(editTaskNameElement);
            })
            
            return deferred.promise;
        })
        .then(function doneEditTask(editTaskNameElement){
            // extract nodeid from edited task
            return yaioNodePage.extractNodeIdFromTaskNameElement(editTaskNameElement);
        })
        .then(function doneExtractNodeId2(taskId){
            // delete this task
            var deferred = protractor.promise.defer();
            var deletedElement = yaioNodePage.deleteNodeById(taskId, yaioNodePage.jsFuncTestId);
            deletedElement.isPresent().then(function() {
                deferred.fulfill(deletedElement);
            })
            // check that element no more exists
            expect(deletedElement.isPresent()).toEqual(false);
            
            return deferred.promise;
        })
        .then(null, function(err) {
            // on error
            console.error("an error occured:", err);
            expect(err).toBe(false);
        });
    });

});

