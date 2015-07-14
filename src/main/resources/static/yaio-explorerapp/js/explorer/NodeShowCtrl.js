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
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     the controller to load the nodes for url-params and register the yaio-functions
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration BusinessLogic
 */
yaioM.controller('NodeShowCtrl', function($rootScope, $scope, $location, $http, $routeParams, setFormErrors, OutputOptionsEditor, authorization, yaioUtils) {
    // include utils    
    $scope.yaioUtils = yaioUtils;

    // register pattern
    $scope.CONST_PATTERN_CSSCLASS  = yaioM.CONST_PATTERN_CSSCLASS ;
    $scope.CONST_PATTERN_NUMBERS  = yaioM.CONST_PATTERN_NUMBERS ;
    $scope.CONST_PATTERN_TEXTCONST  = yaioM.CONST_PATTERN_TEXTCONST ;
    $scope.CONST_PATTERN_TITLE  = yaioM.CONST_PATTERN_TITLE ;
    $scope.CONST_PATTERN_SEG_TASK  = yaioM.CONST_PATTERN_SEG_TASK ;
    $scope.CONST_PATTERN_SEG_HOURS  = yaioM.CONST_PATTERN_SEG_HOURS ;
    $scope.CONST_PATTERN_SEG_STAND  = yaioM.CONST_PATTERN_SEG_STAND ;
    $scope.CONST_PATTERN_SEG_DATUM  = yaioM.CONST_PATTERN_SEG_DATUM ;
    $scope.CONST_PATTERN_SEG_STRING  = yaioM.CONST_PATTERN_SEG_STRING ;
    $scope.CONST_PATTERN_SEG_FLAG  = yaioM.CONST_PATTERN_SEG_FLAG ;
    $scope.CONST_PATTERN_SEG_INT  = yaioM.CONST_PATTERN_SEG_INT ;
    $scope.CONST_PATTERN_SEG_UID  = yaioM.CONST_PATTERN_SEG_UID ;
    $scope.CONST_PATTERN_SEG_ID  = yaioM.CONST_PATTERN_SEG_ID ;
    $scope.CONST_PATTERN_SEG_TAGS  = yaioM.CONST_PATTERN_SEG_TAGS ;
    $scope.CONST_PATTERN_SEG_PRAEFIX  = yaioM.CONST_PATTERN_SEG_PRAEFIX ;
    $scope.CONST_PATTERN_SEG_CHECKSUM  = yaioM.CONST_PATTERN_SEG_CHECKSUM ;
    $scope.CONST_PATTERN_SEG_TIME  = yaioM.CONST_PATTERN_SEG_TIME ;
    
    // register the editor
    $scope.outputOptionsEditor = OutputOptionsEditor;

    // check parameter - set default if empty
    var baseUrl = '/show/';
    var restBaseUrl = '/nodes/show/';
    var nodeId = $routeParams.nodeId;
    var nodeByAllId = $routeParams.nodeByAllId;
    if (nodeByAllId != null && nodeByAllId != "" && nodeByAllId) {
        nodeId = nodeByAllId;
        baseUrl = '/showByAllIds/';
        restBaseUrl = '/nodes/showsymlink/';
    }
    if (nodeId == null || nodeId == "" || ! nodeId) {
        nodeId = "MasterplanMasternode1";
    }

    // create node
    $scope.node = {};
    $scope.nodeForEdit = {};
    $scope.config = {treeOpenLevel: 1};
    
    // check activeNodeId
    var activeNodeIdHandler;
    var activeNodeId = $routeParams.activeNodeId;

    console.log("NodeShowCtrl - processing nodeId=" + nodeId + " activeNodeId=" + activeNodeId);

    var curNodeUrl = restBaseUrl + nodeId;

    // save lastLocation for login
    $rootScope.lastLocation = baseUrl + nodeId;
    
    // call authentificate 
    authorization.authentificate(function () {
        // check authentification
        if (! $rootScope.authenticated) {
            console.log("showControl: not authentification: " + $rootScope.authenticated);
            $location.path("/login");
            $scope.error = false;
        } else {
            // do Search
            if (activeNodeId) {
                /**
                 * <h4>FeatureDomain:</h4>
                 *     GUI
                 * <h4>FeatureDescription:</h4>
                 *     callbackhandler to load activeNodeId and open the nodehierarchy
                 * <h4>FeatureResult:</h4>
                 *   <ul>
                 *     <li>updates nodetree
                 *   </ul> 
                 * <h4>FeatureKeywords:</h4>
                 *     GUI Callback
                 */
                activeNodeIdHandler = function() {
                    var activeNodeUrl = '/nodes/show/' + activeNodeId;
                    console.log("start loading activenode:" + activeNodeId);
                    $http.get(activeNodeUrl).then(function(nodeResponse) {
                        // success handler
                        
                        // check response
                        var state = nodeResponse.data.state;
                        if (state == "OK") {
                            // all fine
                            console.log("NodeShowCtrl - OK loading activenode:" + nodeResponse.data.stateMsg)
                            
                            // create nodehierarchy
                            var nodeIdHierarchy = new Array();
                            var parentNode = nodeResponse.data.node.parentNode;
                            while (parentNode != null && parentNode != "" && parentNode != "undefined") {
                                nodeIdHierarchy.push(parentNode.sysUID);
                                parentNode = parentNode.parentNode;
                            }
                            nodeIdHierarchy.reverse();
                            
                            // add me 
                            nodeIdHierarchy.push(nodeResponse.data.node.sysUID);
                            
                            // open Hierarchy
                            openNodeHierarchy("#tree", nodeIdHierarchy);
                        } else {
                            // error
                            logError("error loading activenode:" + nodeResponse.data.stateMsg 
                                    + " details:" + nodeResponse, false)
                        }
                    }, function(response) {
                        // error handler
                        var data = response.data;
                        var status = response.status;
                        var header = response.header;
                        var config = response.config;
                        var message = "error loading activenode with url: " + activeNodeUrl;
                        logError(message, true);
                        message = "error data: " + data + " header:" + header + " config:" + config;
                        logError(message, false);
                    });
                }
            }

            // load data
            $http.get(curNodeUrl).then(function(nodeResponse) {
                // success handler
                
                // check response
                var state = nodeResponse.data.state;
                if (state == "OK") {
                    // all fine
                    console.log("NodeShowCtrl - OK loading nodes:" + nodeResponse.data.stateMsg);
                    $scope.node = nodeResponse.data.node;
                    
                    // create nodehierarchy
                    var nodeHierarchy = new Array();
                    var parentNode = nodeResponse.data.node.parentNode;
                    while (parentNode != null && parentNode != "" && parentNode != "undefined") {
                        nodeHierarchy.push(parentNode);
                        parentNode = parentNode.parentNode;
                    }
                    nodeHierarchy.reverse();
                    $scope.nodeHierarchy = nodeHierarchy;

                    // load fencytree
                    yaioCreateFancyTree("#tree", $scope.node.sysUID, activeNodeIdHandler);
                    
                    // load me
                    $scope.yaioUtils.renderNodeLine(nodeResponse.data.node, "#masterTr")

                    // recalc gantt
                    yaioRecalcMasterGanttBlock($scope.node);
                } else {
                    // error
                    logError("error loading nodes:" + nodeResponse.data.stateMsg 
                            + " details:" + nodeResponse, true)
                }
            }, function(response) {
                // error handler
                var data = response.data;
                var status = response.status;
                var header = response.header;
                var config = response.config;
                var message = "error loading node with url: " + curNodeUrl;
                logError(message, true);
                message = "error data: " + data + " header:" + header + " config:" + config;
                logError(message, false);
            });
        }
    });
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Editor
     * <h4>FeatureDescription:</h4>
     *     export GUI As Overview
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>exportAsOverview
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.exportAsOverview = function() {
        console.log("exportAsOverview");
        yaioExportExplorerLinesAsOverview();
        return false;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Editor
     * <h4>FeatureDescription:</h4>
     *     create snapshot of GUI
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>snapshot
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.snapshot = function() {
        console.log("snapshot");
        yaioSnapshot($scope.node);
        return false;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Editor
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to open all subnodes<level in the treeview
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>open the subNodes till treeOpenLevel
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.openSubNodes = function(flgOpen) {
        console.log("openSubNodes:" + " level:" + $scope.config.treeOpenLevel);
        yaioOpenSubNodesForTree("#tree", $scope.config.treeOpenLevel);
        return false;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Editor
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to discard and close the editor
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates layout
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.discard = function(formName) {
        yaioCloseNodeEditor();
        return false;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Callback
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to validate the type of a newNode and open the corresponding form
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates layout
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.selectNewNodeType = function(formName) {
        // hide all forms
        yaioHideAllNodeEditorForms();
        
        // display createform and select nodeform
        $("#containerFormYaioEditorCreate").css("display", "block");
        var className = $scope.nodeForEdit["className"];
        $("#containerFormYaioEditor" + className).css("display", "block");
        console.log("selectNewNodeType open form #containerFormYaioEditor" + className);

        // special fields
        if (className == "SymLinkNode") {
            $scope.nodeForEdit["type"] = "SYMLINK";
        } else if (className == "InfoNode") {
            $scope.nodeForEdit["type"] = "INFO";
        } else if (className == "UrlResNode") {
            $scope.nodeForEdit["type"] = "URLRES";
        } else if (className == "TaskNode") {
            $scope.nodeForEdit["type"] = "OFFEN";
        } else if (className == "EventNode") {
            $scope.nodeForEdit["type"] = "EVENT_PLANED";
        }

        // set mode
        $scope.nodeForEdit.mode = "create";
        return false;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Callback
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to perform actions when type has changed<br>
     *     calls calcIstStandFromState() for the node
     *     if ERLEDIGT || VERWORFEN || EVENT_ERLEDIGT || EVENT_VERWORFEN: update istStand=100
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates istStand
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.doTypeChanged = function() {
        $scope.nodeForEdit.istStand = calcIstStandFromState($scope.nodeForEdit);
        return false;
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Callback
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to perform actions when istStand has changed<br>
     *     recalcs the type/state depending on the istStand
     *     <ul>
     *       <li>if className=TaskNode && 0: update type=OFFEN
     *       <li>if className=TaskNode && >0&&<100 && ! WARNING: update type=RUNNING
     *       <li>if className=TaskNode && 100 && != VERWORFEN: update type=ERLEDIGT
     *       <li>if className=EventNode && 0: update type=EVENT_PLANED
     *       <li>if className=EventNode && >0&&<100 && ! EVENT_WARNING: update type=EVENT_RUNNING
     *       <li>if className=EventNode && 100 && != EVENT_VERWORFEN: update type=EVENT_ERLEDIGT
     *     </ul>
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates type
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.doIstStandChanged = function() {
        $scope.nodeForEdit.type = calcTypeFromIstStand($scope.nodeForEdit);
        return false;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Callback
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to perform actions when type has changed<br>
     *     if EVENT_ERLEDIGT || VERWORFEN: update stand=100;
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates stand
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.doTaskNodeTypeChanged = function() {
        if (   $scope.nodeForEdit.type =="ERLEDIGT"
            || $scope.nodeForEdit.type =="VERWORFEN"
            ) {
            $scope.nodeForEdit.stand ="100";
        }
        return false;
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Callback
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to map the nodedata, create json,call webservice and 
     *     relocate to the new nodeId
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>save node and updates layout
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.save = function(formName) {
        // define json for common fields
        var nodeObj = {name: $scope.nodeForEdit.name};

        // do extra for the different classNames
        // configure value mapping
        var fields = new Array();
        fields = fields.concat(configNodeTypeFields.Common.fields);
        if ($scope.nodeForEdit.className == "TaskNode") {
            fields = fields.concat(configNodeTypeFields.TaskNode.fields);
            $scope.nodeForEdit.state = $scope.nodeForEdit.type;
        } else if ($scope.nodeForEdit.className == "EventNode") {
            fields = fields.concat(configNodeTypeFields.EventNode.fields);
        } else if ($scope.nodeForEdit.className == "InfoNode") {
            fields = fields.concat(configNodeTypeFields.InfoNode.fields);
        } else if ($scope.nodeForEdit.className == "UrlResNode") {
            fields = fields.concat(configNodeTypeFields.UrlResNode.fields);
        } else if ($scope.nodeForEdit.className == "SymLinkNode") {
            fields = fields.concat(configNodeTypeFields.SymLinkNode.fields);
        }
        
        // iterate fields an map to nodeObj
        for (var idx in fields) {
            var field = fields[idx];
            var fieldName = field.fieldName;
            var value = $scope.nodeForEdit[fieldName];
            
            if (field.intern) {
                // ignore intern
                continue;
            } if (field.type == "checkbox" && ! value) {
                value = "";
            }
            
            // convert values
            if (field.datatype == "date" && value) {
                console.log("map nodefield date pre:" + fieldName + "=" + value);
                var lstDate=value.split(".");
                var newDate=new Date(lstDate[1]+"/"+lstDate[0]+"/"+lstDate[2]);
                value = newDate.getTime();
                console.log("map nodefield date post:" + fieldName + "=" + newDate + "->" + value);
            } if (field.datatype == "datetime" && value) {
                console.log("map nodefield datetime pre:" + fieldName + "=" + value);
                var lstDateTime=value.split(" ");
                var lstDate = lstDateTime[0].split(".");
                var lstTime = lstDateTime[1];
                var newDate=new Date(lstDate[1]+"/"+lstDate[0]+"/"+lstDate[2] + " " + lstTime[1] + ":00");
                value = newDate.getTime();
                console.log("map nodefield datetime post:" + fieldName + "=" + newDate + "->" + value);
            }
            
            nodeObj[fieldName] = value;
            console.log("map nodefield:" + fieldName + "=" + value);
        }
        
        
        // branch depending on mode
        var method, url;
        var mode =  $scope.nodeForEdit["mode"];
        if (mode == "edit") {
            // mode update 
            method = "PATCH";
            url = updateUrl + $scope.nodeForEdit.className + "/" + $scope.nodeForEdit.sysUID;
        } else if (mode == "create") {
            // mode create 
            method = "POST";
            url = createUrl + $scope.nodeForEdit.className + "/" + $scope.nodeForEdit.sysUID;
            
            // unset sysUID
            nodeObj["sysUID"] = null;
        } else {
            // unknown mode
            logError("unknown mode=" + mode + " form formName=" + formName, false);
            return null;
        }

        // define json for common fields
        var json = JSON.stringify(nodeObj);
        
        // create url
        console.log("NodeSave - url::" + url + " data:" + json);
        
        // do http
        $http({
                method: method,
                url: url,
                data: json
        }).then(function(nodeResponse) {
            // sucess handler
            
            // check response
            var state = nodeResponse.data.state;
            if (state == "OK") {
                // all fine
                console.log("NodeSave - OK saved node:" + nodeResponse.data.stateMsg)
                
                // reload
                var newUrl = '/show/' + nodeId 
                    + '/activate/' + nodeResponse.data.node.sysUID; //$scope.nodeForEdit.sysUID
                console.log("reload:" + newUrl);
                
                // no cache!!!
                $location.path(newUrl + "?" + (new Date()).getTime());
            } else {
                // error
                var message = "error saving node:" + nodeResponse.data.stateMsg 
                        + " details:" + nodeResponse;
                var userMessage = "error saving node:" + nodeResponse.data.stateMsg;
                
                // map violations
                var violations = nodeResponse.data.violations;
                var fieldErrors = {};
                if (violations && violations.length > 0) {
                    message = message + " violations: ";
                    userMessage = "";
                    for (var idx in violations) {
                        // map violation errors
                        var violation = violations[idx];
                        
                        // TODO crud hack
                        if (violation.path == "state") {
                            violation.path = "type";
                        } else if (violation.path == "planValidRange") {
                            violation.path = "planStart";
                        } else if (violation.path == "planStartValid") {
                            violation.path = "planStart";
                        } else if (violation.path == "planEndeValid") {
                            violation.path = "planEnde";
                        } else if (violation.path == "istValidRange") {
                            violation.path = "istStart";
                        } else if (violation.path == "istStartValid") {
                            violation.path = "istStart";
                        } else if (violation.path == "istEndeValid") {
                            violation.path = "istEnde";
                        }
                        fieldErrors[violation.path] = [violation.message];
                        message = message + violation.path + ":" + violation.message + ", ";

                        // find formelement
                        var $formField = $('#' + formName).find('*[name="' + violation.path + '"]');
                        if (($formField.length > 0) && ($formField.is(':visible'))) {
                            // formfield is shown by showErrors
                            console.log("map violation " + violation + " = " + violation.path + ":" + violation.message + " to " + formName + " id=" + $($formField).attr('id'));
                        } else {
                            // another error: show userMessage
                            userMessage += "<br>" + violation.path + ":" + violation.message;
                        }
                    }
                }
                logError(message, false);
                if (userMessage != "") {
                    logError(userMessage, true);
                }
                
                // Failed
                setFormErrors({
                    formName: formName,
                    fieldErrors: fieldErrors
                });
            }
        }, function(response) {
            // error handler
            var data = response.data;
            var status = response.status;
            var header = response.header;
            var config = response.config;
            var message = "error saving node with url: " + url;
            logError(message, true);
            message = "error data: " + data + " header:" + header + " config:" + config;
            logError(message, false);
        });
    };


    /**
     * <h4>FeatureDomain:</h4>
     *     Download
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to send and close the importeditor
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>send request and updates layout
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.sendImport = function() {
        yaioSendImportEditor();
        yaioCloseImportEditor();
        console.log("send done");
        return false;
    },
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Download
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to discard and close the editor
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates layout
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.discardImport = function() {
        yaioCloseImportEditor();
        console.log("discard done");
        return false;
    },

    /**
     * <h4>FeatureDomain:</h4>
     *     GUI Download
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to open the importEditor
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>open the importEditor
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     * @param sysUID - the sysUID of the current node
     * @param url - the url to send
     * @param target - the target window-name
     */
    $scope.showImportEditor = function(sysUID, newUrl, newTarget) {
        var url = newUrl;
        var target = newTarget;
        yaioOpenImportEditor(sysUID, url, target);
        console.log("showImportEditor done:" + " url:" + url);
        return false;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to recalc ganttblocks for nodes
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>recalc ganttblocks
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.recalcGanttBlocks = function() {
        yaioRecalcFancytreeGanttBlocks();
        yaioRecalcMasterGanttBlock($scope.node);
        return false;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to recalc Gantt with Master-Ist/Plan-DateRange
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>recalc ganttblocks
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     * @param  flgShowIst   boolean if is set show ist, if not show plan
     */
    $scope.recalcGanttForIstOrPlan = function(flgShowIst) {
        var start = flgShowIst ? $scope.node.istChildrenSumStart : $scope.node.planChildrenSumStart;
        var ende = flgShowIst ? $scope.node.istChildrenSumEnde : $scope.node.planChildrenSumEnde;
        $("#inputGanttRangeStart").val(formatGermanDate(start)).trigger('input').triggerHandler("change");
        $("#inputGanttRangeEnde").val(formatGermanDate(ende)).trigger('input').triggerHandler("change");
        return false;
    }
});
