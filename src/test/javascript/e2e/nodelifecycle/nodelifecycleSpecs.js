/**
 * test the ExplorerPage of YAIO with a full lifecycle of a node
 * 
 */

'use strict';
var YAIOAuthPage = require('../auth/auth.po.js');
var YAIOFrontPage = require('../frontpage/frontpage.po.js');
var YAIONodePage = require('../explorer/node.po.js');

describe('yaio nodelifecycle', function() {
    // define vars
    var yaioAuthPage, yaioFrontPage, yaioNodePage;

    /**
     * prepare tests
     */
    beforeEach(function() {
        // initpages (reset elements)
        yaioAuthPage = new YAIOAuthPage();
        yaioFrontPage = new YAIOFrontPage();
        yaioNodePage = new YAIONodePage();
        
        // do Login
        yaioAuthPage.checkLogin()
            .then(function doneOpenExplorer() {
                // open explorer
                return yaioNodePage.openExplorerFromFrontPage();
            });
        protractor.utils.waitUntilElementClickable($(yaioNodePage.containerMasterdata), protractor.utils.CONST_WAIT_NODEHIRARCHY);
        expect($(yaioNodePage.containerMasterdata).isPresent()).toEqual(true);
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
    it('should create/edit/delete a new TaskNode', function doCreateEditDeleteTask() {
        // When and Then

        // navigate to Node
        yaioNodePage.navigateToNode(yaioNodePage.jsFuncTestHierarchy)
            .then(function doneNavigate(){
                // create task
                var deferred = protractor.promise.defer();
                var newNodeElement = yaioNodePage.openNodeEditorAndCreateTaskNode(yaioNodePage.jsFuncTestId);
                newNodeElement.getText().then(function() {
                    deferred.fulfill(newNodeElement);
                });
                
                return deferred.promise;
            })
            .then(function doneCreateTask(newNodeElement) {
                // extract nodeid from new task
                return yaioNodePage.extractNodeIdFromNodeNameElement(newNodeElement);
            })
            .then(function doneExtractNodeId(nodeId) {
                // edit new task
                var deferred = protractor.promise.defer();
                var editTaskNameElement = yaioNodePage.editTaskNodeById(nodeId);
                editTaskNameElement.getText().then(function() {
                    deferred.fulfill(editTaskNameElement);
                });
                
                return deferred.promise;
            })
            .then(function doneEditTask(editTaskNameElement){
                // extract nodeid from edited task
                return yaioNodePage.extractNodeIdFromNodeNameElement(editTaskNameElement);
            })
            .then(function doneExtractNodeId2(nodeId){
                // delete this task
                var deferred = protractor.promise.defer();
                var deletedElement = yaioNodePage.deleteNodeById(nodeId, yaioNodePage.jsFuncTestId);
                deletedElement.isPresent().then(function() {
                    deferred.fulfill(deletedElement);
                });
                
                // check that element no more exists
                expect(deletedElement.isPresent()).toEqual(false);
                
                return deferred.promise;
            });
    });

    it('should create/follow/delete a SymLinkNode', function doCreateSymLink() {
        // When and Then

        // navigate to Node
        yaioNodePage.navigateToNode(yaioNodePage.jsFuncTestHierarchy)
            .then(function doneNavigate(){
                // create symlink
                var deferred = protractor.promise.defer();
                var newNodeElement = yaioNodePage.openNodeEditorAndCreateSymLinkNode(yaioNodePage.jsFuncTestId);
                newNodeElement.getText().then(function() {
                    deferred.fulfill(newNodeElement);
                });
                
                return deferred.promise;
            })
            .then(function doneCreateSymLink(newNodeElement) {
                // extract nodeid from new task
                return yaioNodePage.extractNodeIdFromNodeNameElement(newNodeElement);
            })
            .then(function doneExtractNodeId(nodeId) {
                // follow symlink
                var deferred = protractor.promise.defer();
                var symlinkTargetElement = yaioNodePage.followSymLinkNodeById(nodeId, yaioNodePage.jsFuncTestId);
                symlinkTargetElement.getText().then(function() {
                    deferred.fulfill(nodeId);
                });
                
                return deferred.promise;
            })
            .then(function doneFollowSymLink(nodeId){
                // delete this symlink
                var deferred = protractor.promise.defer();
                var deletedElement = yaioNodePage.deleteNodeById(nodeId, yaioNodePage.jsFuncTestId);
                deletedElement.isPresent().then(function() {
                    deferred.fulfill(deletedElement);
                });
                // check that element no more exists
                expect(deletedElement.isPresent()).toEqual(false);
                
                return deferred.promise;
            });
    });

    it('should create/delete a UrlResNode', function doCreateSymLink() {
        // When and Then

return;        
/**
// navigate to Node
        yaioNodePage.navigateToNode(yaioNodePage.jsFuncTestHierarchy)
            .then(function doneNavigate(){
                // create urlres
                var deferred = protractor.promise.defer();
                var newNodeElement = yaioNodePage.openNodeEditorAndCreateUrlResNode(yaioNodePage.jsFuncTestId);
                newNodeElement.getText().then(function() {
                    deferred.fulfill(newNodeElement);
                });

                return deferred.promise;
            })
            .then(function doneCreateUrlRes(newNodeElement) {
                // extract nodeid from new task
                return yaioNodePage.extractNodeIdFromNodeNameElement(newNodeElement);
            })
            .then(function doneExtractNodeId(nodeId) {
                // do UrlRes
                var deferred = protractor.promise.defer();
                deferred.fulfill(nodeId);

                return deferred.promise;
            })
            .then(function doneUrlRes(nodeId){
                // delete this UrlRes
                var deferred = protractor.promise.defer();
                var deletedElement = yaioNodePage.deleteNodeById(nodeId, yaioNodePage.jsFuncTestId);
                deletedElement.isPresent().then(function() {
                    deferred.fulfill(deletedElement);
                });
                // check that element no more exists
                expect(deletedElement.isPresent()).toEqual(false);

                return deferred.promise;
            });
**/            
    });
});

