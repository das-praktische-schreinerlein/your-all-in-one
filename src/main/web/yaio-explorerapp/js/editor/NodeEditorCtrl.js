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

/** 
 * the controller to edit nodes
 * @FeatureDomain                Configuration
 * @FeatureResult                returns new controller
 * @FeatureKeywords              GUI Configuration BusinessLogic
 */
yaioApp.controller('NodeEditorCtrl', function($rootScope, $scope, $location, $routeParams,
                                              setFormErrors, authorization, yaioUtils) {
    'use strict';

    // include utils
    $scope.yaioUtils = yaioUtils;
    
    // register pattern
    $scope.CONST_PATTERN_CSSCLASS  = yaioApp.CONST_PATTERN_CSSCLASS ;
    $scope.CONST_PATTERN_NUMBERS  = yaioApp.CONST_PATTERN_NUMBERS ;
    $scope.CONST_PATTERN_TEXTCONST  = yaioApp.CONST_PATTERN_TEXTCONST ;
    $scope.CONST_PATTERN_TITLE  = yaioApp.CONST_PATTERN_TITLE ;
    $scope.CONST_PATTERN_SEG_TASK  = yaioApp.CONST_PATTERN_SEG_TASK ;
    $scope.CONST_PATTERN_SEG_HOURS  = yaioApp.CONST_PATTERN_SEG_HOURS ;
    $scope.CONST_PATTERN_SEG_STAND  = yaioApp.CONST_PATTERN_SEG_STAND ;
    $scope.CONST_PATTERN_SEG_DATUM  = yaioApp.CONST_PATTERN_SEG_DATUM ;
    $scope.CONST_PATTERN_SEG_STRING  = yaioApp.CONST_PATTERN_SEG_STRING ;
    $scope.CONST_PATTERN_SEG_FLAG  = yaioApp.CONST_PATTERN_SEG_FLAG ;
    $scope.CONST_PATTERN_SEG_INT  = yaioApp.CONST_PATTERN_SEG_INT ;
    $scope.CONST_PATTERN_SEG_UID  = yaioApp.CONST_PATTERN_SEG_UID ;
    $scope.CONST_PATTERN_SEG_ID  = yaioApp.CONST_PATTERN_SEG_ID ;
    $scope.CONST_PATTERN_SEG_TAGS  = yaioApp.CONST_PATTERN_SEG_TAGS ;
    $scope.CONST_PATTERN_SEG_PRAEFIX  = yaioApp.CONST_PATTERN_SEG_PRAEFIX ;
    $scope.CONST_PATTERN_SEG_CHECKSUM  = yaioApp.CONST_PATTERN_SEG_CHECKSUM ;
    $scope.CONST_PATTERN_SEG_TIME  = yaioApp.CONST_PATTERN_SEG_TIME ;
    
    // create node
    $scope.nodeForEdit = {};
    var nodeId = $rootScope.nodeId;
    $scope.uploadFile = undefined;
    
    $scope.availiableSystemTemplates = [];
    $scope.availiableOwnTemplates = [];
    
    /** 
     * callbackhandler to discard and close the editor
     * @FeatureDomain                Editor
     * @FeatureResult                updates layout
     * @FeatureKeywords              GUI Callback
     */
    $scope.discard = function() {
        yaioUtils.getService('YaioEditor').yaioCloseNodeEditor();
        return false;
    };
    
    /** 
     * callbackhandler to validate the type of a newNode and open the corresponding form
     * @FeatureDomain                Callback
     * @FeatureResult                updates layout
     * @FeatureKeywords              GUI Callback
     */
    $scope.selectNewNodeType = function() {
        // hide all forms
        yaioUtils.getService('YaioEditor').yaioHideAllNodeEditorForms();
        
        // display createform and select nodeform
        $('#containerFormYaioEditorCreate').css('display', 'block');
        var className = $scope.nodeForEdit['className'];
        $('#containerFormYaioEditor' + className).css('display', 'block');
        console.log('selectNewNodeType open form #containerFormYaioEditor' + className);

        // special fields
        if (className === 'SymLinkNode') {
            $scope.nodeForEdit['type'] = 'SYMLINK';
        } else if (className === 'InfoNode') {
            $scope.nodeForEdit['type'] = 'INFO';
        } else if (className === 'UrlResNode') {
            $scope.nodeForEdit['type'] = 'URLRES';
        } else if (className === 'TaskNode') {
            $scope.nodeForEdit['type'] = 'OFFEN';
        } else if (className === 'EventNode') {
            $scope.nodeForEdit['type'] = 'EVENT_PLANED';
        }

        // set mode
        $scope.nodeForEdit.mode = 'create';
        return false;
    };
    
    
    $scope.selectCreateFromTemplate = function(formName) {
        // create new node by template
        var newParentKey = $scope.nodeForEdit.sysUID;
        if ($scope.nodeForEdit.createFromTemplate && $scope.nodeForEdit.createFromTemplate != '') {
            var json = JSON.stringify({parentNode: newParentKey});
            yaioUtils.getService('YaioNodeData').yaioDoCopyNode({key: $scope.nodeForEdit.createFromTemplate, sysUID: $scope.nodeForEdit.createFromTemplate}, newParentKey, json);
            yaioUtils.getService('YaioEditor').yaioCloseNodeEditor();
            return false;
        }
    };
    
    $scope.loadAvailiableTemplates = function() {
        yaioUtils.getService('YaioNodeData').yaioDoLoadAvailiableTemplates()
            .then(function sucess(angularResponse) {
                    // handle success
                    $scope.availiableSystemTemplates = angularResponse.data.systemTemplates;
                    $scope.availiableOwnTemplates = angularResponse.data.ownTemplates;
                }, function error(angularResponse) {
                    // handle error
                    var data = angularResponse.data;
                    var header = angularResponse.header;
                    var config = angularResponse.config;
                    var message = 'error loading node-templates';
                    yaioUtils.getService('Logger').logError(message, true);
                    message = 'error data: ' + data + ' header:' + header + ' config:' + config;
                    yaioUtils.getService('Logger').logError(message, false);
            });
    };

    /** 
     * callbackhandler to perform actions when type has changed<br>
     * calls yaioUtils.getService('YaioEditor').calcIstStandFromState() for the node
     * if ERLEDIGT || VERWORFEN || EVENT_ERLEDIGT || EVENT_VERWORFEN: update istStand=100
     * @FeatureDomain                Callback
     * @FeatureResult                updates istStand
     * @FeatureKeywords              GUI Callback
     */
    $scope.doTypeChanged = function() {
        $scope.nodeForEdit.istStand = yaioUtils.getService('YaioEditor').calcIstStandFromState($scope.nodeForEdit);
        return false;
    };
    
    
    /** 
     * callbackhandler to perform actions when istStand has changed<br>
     * recalcs the type/state depending on the istStand
     * <ul>
     *   <li>if className=TaskNode && 0: update type=OFFEN
     *   <li>if className=TaskNode && >0&&<100 && ! WARNING: update type=RUNNING
     *   <li>if className=TaskNode && 100 && != VERWORFEN: update type=ERLEDIGT
     *   <li>if className=EventNode && 0: update type=EVENT_PLANED
     *   <li>if className=EventNode && >0&&<100 && ! EVENT_WARNING: update type=EVENT_RUNNING
     *   <li>if className=EventNode && 100 && != EVENT_VERWORFEN: update type=EVENT_ERLEDIGT
     * </ul>
     * @FeatureDomain                Callback
     * @FeatureResult                updates type
     * @FeatureKeywords              GUI Callback
     */
    $scope.doIstStandChanged = function() {
        $scope.nodeForEdit.type = yaioUtils.getService('YaioEditor').calcTypeFromIstStand($scope.nodeForEdit);
        return false;
    };
    
    /** 
     * callbackhandler to perform actions when type has changed<br>
     * if EVENT_ERLEDIGT || VERWORFEN: update stand=100;
     * @FeatureDomain                Callback
     * @FeatureResult                updates stand
     * @FeatureKeywords              GUI Callback
     */
    $scope.doTaskNodeTypeChanged = function() {
        if (   $scope.nodeForEdit.type === 'ERLEDIGT'
            || $scope.nodeForEdit.type === 'VERWORFEN'
            ) {
            $scope.nodeForEdit.stand ='100';
        }
        return false;
    };
    
    /** 
     * callbackhandler to perform actions when UploadFile has changed<br>
     * set variable in scope with filename and updates resLocRef
     * @FeatureDomain                Callback
     * @FeatureResult                updates resLocRef and uploadFile
     * @FeatureKeywords              GUI Callback
     */
    $scope.doUploadFileUrlResNodeChanged = function() {
        var element = document.getElementById('inputUploadFileUrlResNode');
        if (element && element.files) {
            $scope.setUploadFileUrlResNode(element.files[0]);
        }
    };

    /**
     * set the uploadFile
     * @FeatureDomain                Callback
     * @FeatureResult                updates resLocRef and uploadFile
     * @FeatureKeywords              GUI Callback
     */
    $scope.setUploadFileUrlResNode = function(uploadFile, forceDoIndex) {
        $scope.uploadFile = uploadFile;
        var fileName = '';
        if ($scope.uploadFile) {
            fileName = $scope.uploadFile.name;
        }
        $scope.nodeForEdit.resLocRef = fileName;
        $('#inputResLocRefUrlResNode').val($scope.nodeForEdit.resLocRef);

        if (forceDoIndex) {
            $scope.nodeForEdit.resIndexDMSState = true;
            $('#inputResIndexDMSStateUrlResNode').prop('checked', true).trigger('input').triggerHandler('change');
        }
    };


    /**
     * callbackhandler to map the nodedata, create json,call webservice and 
     * relocate to the new nodeId
     * @FeatureDomain                Callback
     * @FeatureResult                save node and updates layout
     * @FeatureKeywords              GUI Callback
     */
    $scope.save = function(formName) {
        // define json for common fields
        var nodeObj = {name: $scope.nodeForEdit.name};

        // do extra for the different classNames
        // configure value mapping
        var fields = new Array();
        fields = fields.concat(yaioUtils.getConfig().configNodeTypeFields.Common.fields);
        if ($scope.nodeForEdit.className === 'TaskNode') {
            fields = fields.concat(yaioUtils.getConfig().configNodeTypeFields.TaskNode.fields);
            $scope.nodeForEdit.state = $scope.nodeForEdit.type;
        } else if ($scope.nodeForEdit.className === 'EventNode') {
            fields = fields.concat(yaioUtils.getConfig().configNodeTypeFields.EventNode.fields);
        } else if ($scope.nodeForEdit.className === 'InfoNode') {
            fields = fields.concat(yaioUtils.getConfig().configNodeTypeFields.InfoNode.fields);
        } else if ($scope.nodeForEdit.className === 'UrlResNode') {
            fields = fields.concat(yaioUtils.getConfig().configNodeTypeFields.UrlResNode.fields);
        } else if ($scope.nodeForEdit.className === 'SymLinkNode') {
            fields = fields.concat(yaioUtils.getConfig().configNodeTypeFields.SymLinkNode.fields);
        }
        
        // iterate fields an map to nodeObj
        for (var idx in fields) {
            var field = fields[idx];
            var fieldName = field.fieldName;
            var value = $scope.nodeForEdit[fieldName];
            
            if (field.intern) {
                // ignore intern
                continue;
            } if (field.type === 'checkbox' && ! value) {
                value = '';
            }
            
            // convert values
            var lstDate, newDate;
            if (field.datatype === 'date' && value) {
                console.log('map nodefield date pre:' + fieldName + '=' + value);
                lstDate = value.split('.');
                newDate = new Date(lstDate[1]+'/'+lstDate[0]+'/'+lstDate[2]);
                value = newDate.getTime();
                console.log('map nodefield date post:' + fieldName + '=' + newDate + '->' + value);
            } if (field.datatype === 'datetime' && value) {
                console.log('map nodefield datetime pre:' + fieldName + '=' + value);
                var lstDateTime = value.split(' ');
                lstDate = lstDateTime[0].split('.');
                var strTime = lstDateTime[1];
                var newDateTimeStr = lstDate[1]+'/'+lstDate[0]+'/'+lstDate[2] + ' ' + strTime + ':00';
                console.log('map nodefield datetime run:' + fieldName + '=' + newDateTimeStr);
                newDate = new Date(newDateTimeStr);
                value = newDate.getTime();
                console.log('map nodefield datetime post:' + fieldName + '=' + newDate + '->' + value);
            }
            
            nodeObj[fieldName] = value;
            console.log('map nodefield:' + fieldName + '=' + value);
        }
        if ($scope.nodeForEdit.className === 'UrlResNode') {
            if ($scope.nodeForEdit['resContentDMSState']) {
                nodeObj['resContentDMSState'] = 'UPLOAD_OPEN';
            }
            if ($scope.nodeForEdit['resIndexDMSState']) {
                nodeObj['resIndexDMSState'] = 'INDEX_OPEN';
            }
        }
        
        // save node
        var yaioSaveNodeSuccessHandler = function(nodeObj, options, yaioNodeActionResponse) {
            // sucess handler
            var msg = 'yaioSaveNodeSuccessHandler node: ' + options.mode + ' ' + nodeObj['sysUID'];
            
            // check response
            var state = yaioNodeActionResponse.state;
            if (state === 'OK') {
                // all fine
                console.log(msg + ' OK saved node:' + yaioNodeActionResponse.stateMsg);
                
                // reload
                var newUrl = '/show/' + nodeId 
                    + '/activate/' + yaioNodeActionResponse.node.sysUID + '/'; //$scope.nodeForEdit.sysUID
                console.log(msg + ' RELOAD:' + newUrl);
                
                // no cache!!!
                $location.path(newUrl + '?' + (new Date()).getTime());
            } else {
                // error
                var message = 'error saving node:' + yaioNodeActionResponse.stateMsg
                        + ' details:' + nodeResponse;
                var userMessage = 'error saving node:' + yaioNodeActionResponse.stateMsg;
                
                // map violations
                var violations = yaioNodeActionResponse.violations;
                var fieldErrors = {};
                if (violations && violations.length > 0) {
                    message = message + ' violations: ';
                    userMessage = '';
                    for (var idx in violations) {
                        // map violation errors
                        var violation = violations[idx];
                        
                        // TODO crud hack
                        if (violation.path === 'state') {
                            violation.path = 'type';
                        } else if (violation.path === 'planValidRange') {
                            violation.path = 'planStart';
                        } else if (violation.path === 'planStartValid') {
                            violation.path = 'planStart';
                        } else if (violation.path === 'planEndeValid') {
                            violation.path = 'planEnde';
                        } else if (violation.path === 'istValidRange') {
                            violation.path = 'istStart';
                        } else if (violation.path === 'istStartValid') {
                            violation.path = 'istStart';
                        } else if (violation.path === 'istEndeValid') {
                            violation.path = 'istEnde';
                        }
                        fieldErrors[violation.path] = [violation.message];
                        message = message + violation.path + ':' + violation.message + ', ';

                        // find formelement
                        var $formField = $('#' + options.formName).find('*[name="' + violation.path + '"]');
                        if (($formField.length > 0) && ($formField.is(':visible'))) {
                            // formfield is shown by showErrors
                            console.log(msg + ' MAP violation ' + violation + ' = ' + violation.path + ':' + violation.message + ' to ' + options.formName + ' id=' + $($formField).attr('id'));
                        } else {
                            // another error: show userMessage
                            userMessage += '<br>' + violation.path + ':' + violation.message;
                        }
                    }
                }
                yaioUtils.getService('Logger').logError(message, false);
                if (userMessage != '') {
                    yaioUtils.getService('Logger').logError(userMessage, true);
                }
                
                // Failed
                setFormErrors({
                    formName: options.formName,
                    fieldErrors: fieldErrors
                });
                
                console.log(msg + ' DONE');
            }
        };
        
        // call save
        var options = {
                mode: $scope.nodeForEdit['mode'],
                formName: formName,
                className: $scope.nodeForEdit.className,
                sysUID: $scope.nodeForEdit.sysUID,
                uploadFile: $scope.uploadFile
            };
        return yaioUtils.getService('YaioNodeData').yaioDoSaveNode(nodeObj, options)
            .then(function success(angularReponse) {
                    // handle success
                    return yaioSaveNodeSuccessHandler(nodeObj, options, angularReponse.data);
                }, function error(angularReponse) {
                    // handle error
                    var data = angularReponse.data;
                    var header = angularReponse.header;
                    var config = angularReponse.config;
                    var message = 'error saving node ' + nodeObj.sysUID;
                    yaioUtils.getService('Logger').logError(message, true);
                    message = 'error data: ' + data + ' header:' + header + ' config:' + config;
                    yaioUtils.getService('Logger').logError(message, false);
            });
    };
    
    // init
    $scope.loadAvailiableTemplates();
});
