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

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     the controller to search nodes for url-params and register the yaio-functions
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration BusinessLogic
 */
yaioApp.controller('NodeSearchCtrl', function($rootScope, $scope, $location, $http, $routeParams, setFormErrors, authorization, yaioUtils) {
    'use strict';

    // include utils
    $scope.yaioUtils = yaioUtils;

    // create search
    $scope.nodes = [];

    $scope.searchOptions = {
        curPage: 1,
        pageSize: 20,
        searchSort: 'lastChangeDown',
        baseSysUID: "MasterplanMasternode1",
        fulltext: "",
        total: 0
    };
    if ($routeParams.curPage) {
        $scope.searchOptions.curPage = decodeURI($routeParams.curPage);
    }
    if ($routeParams.pageSize) {
        $scope.searchOptions.pageSize = decodeURI($routeParams.pageSize);
    }
    if ($routeParams.searchSort) {
        $scope.searchOptions.searchSort = decodeURI($routeParams.searchSort);
    }
    if ($routeParams.baseSysUID) {
        $scope.searchOptions.baseSysUID = decodeURI($routeParams.baseSysUID);
    }
    if ($routeParams.fulltext) {
        $scope.searchOptions.fulltext = decodeURI($routeParams.fulltext);
    }
    console.log("NodeSearchCtrl - processing");
    
    // pagination has to wait for event
    $scope.NodeListReady = false;

    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     callbackhandler for pagination to load new page
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>changes $location
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback Pagination
     * @param text - page text
     * @param page - new pagenumber
     */
    $scope.nextPageAct = function(text, page){
        console.log(text, page);
        var newUrl = '/search'
            + '/' + encodeURI(page)
            + '/' + encodeURI($scope.searchOptions.pageSize)
            + '/' + encodeURI($scope.searchOptions.searchSort)
            + '/' + encodeURI($scope.searchOptions.baseSysUID)
            + '/' + encodeURI($scope.searchOptions.fulltext)
            + '/';
        
        // save lastLocation for login
        $rootScope.lastLocation = newUrl;

        // no cache!!!
        console.log("load new Url:" + newUrl);
        $location.path(newUrl);
    };
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     callbackhandler for fulltextsearch if keyCode=13 (Enter) run doNewFulltextSearch
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>starts doNewFulltextSearch if keyCode=13
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback Fulltextsearch
     * @param event - key-pressed event
     */
    $scope.checkEnterFulltextSearch = function(event) {
        if (event.keyCode == 13) {
            $scope.doNewFulltextSearch();
        }
        
        return event;
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     callbackhandler for fulltextsearch set page=1 and start doFulltextSearch
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>starts doFulltextSearch with page=1
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback Fulltextsearch
     */
    $scope.doNewFulltextSearch = function() {
        $scope.searchOptions.curPage = 1;
        var newUrl = '/search'
            + '/' + encodeURI($scope.searchOptions.curPage)
            + '/' + encodeURI($scope.searchOptions.pageSize)
            + '/' + encodeURI($scope.searchOptions.searchSort)
            + '/' + encodeURI($scope.searchOptions.baseSysUID)
            + '/' + encodeURI($scope.searchOptions.fulltext)
            + '/';
        // save lastLocation for login
        $rootScope.lastLocation = newUrl;

        // no cache!!!
        console.log("load new Url:" + newUrl);
        $location.path(newUrl);
    };

    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     send ajax-request for fulltextsearch to server and add reszult to scope<br>
     *     sends broadcast NodeListReady when result is ready
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li> Updates scope.nodes
     *     <li> sends broadcast NodeListReady when result is ready
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback Fulltextsearch
     */
    $scope.doFulltextSearch = function() {
        var uri = '/' + encodeURI($scope.searchOptions.curPage)
                + '/' + encodeURI($scope.searchOptions.pageSize)
                + '/' + encodeURI($scope.searchOptions.searchSort)
                + '/' + encodeURI($scope.searchOptions.baseSysUID)
                + '/';
        // save lastLocation for login
        $rootScope.lastLocation = '/search' + uri + encodeURI($scope.searchOptions.fulltext) + '/';

        // no empty fulltext for webservice -> we use there another route 
        if ($scope.searchOptions.fulltext && $scope.searchOptions.fulltext.length > 0) {
            uri = uri + encodeURI($scope.searchOptions.fulltext) + '/';
        }

        // load data
        var searchNodeUrl = '/nodes/search' + uri;
        $http.get(searchNodeUrl).then(function(nodeResponse) {
            // success handler
            
            // check response
            var state = nodeResponse.data.state;
            if (state === "OK") {
                // all fine
                console.log("NodeSearchCtrl - OK loading nodes:" + nodeResponse.data.stateMsg + " searchNodeUrl=" + searchNodeUrl);
                
                // add nodes to scope
                $scope.nodes = nodeResponse.data.nodes;
                
                // set count
                $scope.searchOptions.total = nodeResponse.data.count;
                
                // set event for paginantion
                $scope.NodeListReady = true;
                $scope.$broadcast("NodeListReady");
            } else {
                // error
                yaioUtils.getService('YaioBase').logError("error loading nodes:" + nodeResponse.data.stateMsg + " details:" + nodeResponse, true);
            }
        }, function(response) {
            // error handler
            var data = response.data;
            var header = response.header;
            var config = response.config;
            var message = "error loading nodes with url: " + searchNodeUrl;
            yaioUtils.getService('YaioBase').logError(message, true);
            message = "error data: " + data + " header:" + header + " config:" + config;
            yaioUtils.getService('YaioBase').logError(message, false);
        });
    };
    
    /**
     * <h4>FeatureDomain:</h4>
     *     GUI
     * <h4>FeatureDescription:</h4>
     *     callbackhandler to rendernodeLine for node
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>renders nodeline
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Callback
     */
    $scope.renderNodeLine = function(node) {
        // we need a timeout to put the tr into DOM
        setTimeout(function(){
                $scope.yaioUtils.renderNodeLine(node, "#tr" + node.sysUID);
                console.log("renderNodeLine: done to:" + "#tr" + node.sysUID + $("#detail_sys_" + node.sysUID).length);

                // render hierarchy
                var parentNode = node.parentNode;
                var parentStr = node.name;
                while (parentNode != null && parentNode != "" && parentNode != "undefined") {
                    parentStr = parentNode.name + " --> " + parentStr;
                    parentNode = parentNode.parentNode;
                }
                parentStr = "<b>" + yaioUtils.getService('YaioBase').htmlEscapeText(parentStr) + "</b>";
                
                // extract search words
                var searchExtract = "";
                if ($scope.searchOptions.fulltext 
                    && $scope.searchOptions.fulltext.length > 0
                    && node.nodeDesc != undefined) {
                    // split to searchwords
                    var searchWords = $scope.searchOptions.fulltext.split(" ");
                    var searchWord, searchResults, splitLength, splitText;

                    var descText = node.nodeDesc;
                    descText = descText.replace(/<WLBR>/g, "\n");
                    descText = descText.replace(/<WLESC>/g, "\\");
                    descText = descText.replace(/<WLTAB>/g, "\t");
                    descText = descText.toLowerCase();
                    
                    for (var idx in searchWords) {
                        searchWord = yaioUtils.getService('YaioBase').escapeRegExp(searchWords[idx]);

                        // split by searchwords
                        searchResults = descText.toLowerCase().split(searchWord.toLowerCase());
                        
                        // add dummy-element if desc start/ends with searchWord 
                        if (descText.search(searchWord.toLowerCase()) == 0) {
                            searchResults.insert(" ");
                        }
                        if (descText.search(searchWord.toLowerCase()) == (descText.length - searchWord.length)) {
                            searchResults.push(" ");
                        }

                        // iterate and show 50 chars before and behind
                        for (var idx2 = 0; idx2 < searchResults.length; idx2++) {
//                            console.log("found " + searchWord + " after " + searchResults[idx2]);
                            if (idx2 > 0) {
                                splitLength = (searchResults[idx2].length > 50 ? 50 : searchResults[idx2].length);
                                splitText = searchResults[idx2].substr(0, splitLength);
                                console.log("found " + searchWord + " after use " + splitLength + " extracted:" + splitText);
                                searchExtract += "<b>"+ searchWord + "</b>" 
                                    + yaioUtils.getService('YaioBase').htmlEscapeText(splitText) + "...";
                            }
                            if (idx2 < searchResults.length) {
                                splitLength = (searchResults[idx2].length > 50 ? 50 : searchResults[idx2].length);
                                splitText = searchResults[idx2].substr(
                                        searchResults[idx2].length - splitLength, 
                                        searchResults[idx2].length);
                                console.log("found " + searchWord + " before use " + splitLength + " extracted:" + splitText);
                                searchExtract += "..." 
                                    + yaioUtils.getService('YaioBase').htmlEscapeText(splitText);
                            }
                        }
                    }
                }
                
                // add searchdata
                console.log("renderNodeLine: add searchdata to:" + "#tr" + node.sysUID);
                var $html = $("<div id='details_parent_" + node.sysUID + "'"
                                + " class='field_nodeParent'>"
                                + parentStr
                                + "</div>"
                              + "<div id='details_searchdata_" + node.sysUID + "'"
                                + " class='field_nodeSearchData'>"
                                + searchExtract
                                + "</div>");
                $("#detail_sys_" + node.sysUID).after($html);
                console.log("renderNodeLine: added searchdata to:" + "#detail_sys_" + node.sysUID + $("#detail_sys_" + node.sysUID).length);
            }, 10);
    };

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
        for (var idx in $scope.nodes) {
            var node = $scope.nodes[idx];
            yaioUtils.getService('YaioNodeGanttRender').yaioRecalcGanttBlock(node);
        }
    };
    
    // call authentificate 
    authorization.authentificate(function () {
        // check authentification
        if (! $rootScope.authenticated) {
            $location.path("/login");
            $scope.error = false;
        } else {
            // do Search
            $scope.doFulltextSearch();
        }
    });
});    
