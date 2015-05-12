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
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     controller for the yaio-gui
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
var yaioM = angular.module('yaioExplorerApp', ['ngAnimate', 'ngRoute', 'pascalprecht.translate']);

// define pattern
yaioM.CONST_PATTERN_CSSCLASS = /^[ A-Za-z0-9\._-]+$/;
yaioM.CONST_PATTERN_NUMBERS = /^\d+$/;
yaioM.CONST_PATTERN_TEXTCONST = /^[A-Za-z0-9_]$/;
yaioM.CONST_PATTERN_TITLE = /^[A-Za-z0-9_]$/;

yaioM.CONST_PATTERN_SEG_TASK = /__[A-Za-z]+?[0-9]+?__/;
/** Pattern to parse Aufwand-segments */
yaioM.CONST_PATTERN_SEG_HOURS = /^[0-9]?\\.?[0-9.]+$/;
/** Pattern to parse Stand-segments */
yaioM.CONST_PATTERN_SEG_STAND = /^[0-9]?\\.?[0-9.]+$/;
/** Pattern to parse Date-segments */
yaioM.CONST_PATTERN_SEG_DATUM = /^\\d\\d\\.\\d\\d.\\d\\d\\d\\d$/;
/** Pattern to parse common String-segments */
yaioM.CONST_PATTERN_SEG_STRING = /^[-0-9\\p{L}/+_\\*\\. ]$/;
/** Pattern to parse Flag-segments */
yaioM.CONST_PATTERN_SEG_FLAG = /^[-0-9\\p{L}+_]$/;
/** Pattern to parse Integer-segments */
yaioM.CONST_PATTERN_SEG_INT = /^[0-9]$/;
/** Pattern to parse UID-segments */
yaioM.CONST_PATTERN_SEG_UID = /^[0-9A-Za-z]$/;
/** Pattern to parse ID-segments */
yaioM.CONST_PATTERN_SEG_ID = /^[0-9]$/;
/** Pattern to parse Tag-segments */
yaioM.CONST_PATTERN_SEG_TAGS = /^[-0-9\\p{L}+_\\*\\.;]$/;
/** Pattern to parse ID-Praefix-segments */
yaioM.CONST_PATTERN_SEG_PRAEFIX = /^[A-Za-z]$/;
/** Pattern to parse Checksum-segments */
yaioM.CONST_PATTERN_SEG_CHECKSUM = /^[0-9A-Za-z]$/;
/** Pattern to parse Time-segments */
yaioM.CONST_PATTERN_SEG_TIME = /^\\d\\d\\:\\d\\d$/;

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     configures the routing for the app<br>
 *     add new routes to the $routeProvider-instance
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>updates $routeProvider
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Routing Configuration
 * @param $routeProvider - the $routeProvider-instance to add the new routes
 */
yaioM.config(function($routeProvider) {
    // configure routes
    $routeProvider
        .when('/show/:nodeId/activate/:activeNodeId', { 
            controller:  'NodeShowCtrl',
            templateUrl: 'templates/node.html' })
        .when('/showByAllIds/:nodeByAllId', { 
            controller:  'NodeShowCtrl',
            templateUrl: 'templates/node.html' })
        .when('/show/:nodeId', { 
            controller:  'NodeShowCtrl',
            templateUrl: 'templates/node.html' })
        .when('/search/:curPage?/:pageSize?/:searchSort?/:baseSysUID?/:fulltext?/', { 
            controller:  'NodeSearchCtrl',
            templateUrl: 'templates/list-nodes.html' })
        .when('/search/', { 
            controller:  'NodeSearchCtrl',
            templateUrl: 'templates/list-nodes.html' })
        .when('/login', {
            controller : 'AuthController',
            templateUrl: 'templates/login.html' })
        .when('/frontpage/:nodeId', { 
            controller:  'FrontPageCtrl',
            templateUrl: 'templates/frontpage.html' })
        .when('/', { 
            controller:  'FrontPageCtrl',
            templateUrl: 'templates/frontpage.html' })
        .otherwise({ redirectTo: '/'});
});

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     configures $httpProvider - adds default-headers for patch-requests
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>updates $httpProvider
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 * @param $httpProvider - the $httpProvider to change the default-headers
 */
yaioM.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.headers.patch = {
        'Content-Type': 'application/json;charset=utf-8'
    }
}]);


/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     configures $translateProvider - international app
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>updates $translateProvider
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 * @param $translateProvider - the $translateProvider to get text-resources
 */
yaioM.config(function ($translateProvider) {
    $translateProvider.translations();
    
    $translateProvider.useStaticFilesLoader({
        prefix: 'lang/lang-',
        suffix: '.json'
      });

    // default-language
    var langKey = 'de';
    
    // init
    $translateProvider.preferredLanguage(langKey);
    initLanguageSupport(langKey);
    $translateProvider.currentLanguageKey = langKey;
    

    // change icons
    $(".button-lang").removeClass("button-lang-active").addClass("button-lang-inactive");
    $("#button_lang_" + langKey).removeClass("button-lang-inactive").addClass("button-lang-active");
});

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     adds the new tag-directive "state" to output formated node-state
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new directive
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary
 */
yaioM.directive('state', function(){
    return {
        restrict: 'E', // own tag
        scope: {
            value: '='
        },
        template: '<span ng-show="value == \'RUNNING\'">laufend</span>' +
                  '<span ng-show="value != \'RUNNING\'">{{value}}</span>'
     };
})
    
/**
 * <h4>FeatureDomain:</h4>
 *     Utils
 * <h4>FeatureDescription:</h4>
 *     factory to create yaioUtils with util-functions
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new yaioUtils obj
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     Utils
 */
yaioM.factory('yaioUtils', function ($rootScope) {
    var ganttRangeStart = formatGermanDate((new Date()).getTime() - 90*24*60*60*1000); 
    var ganttRangeEnd = formatGermanDate((new Date()).getTime() + 90*24*60*60*1000);
    
    return {
        /**
         * <h4>FeatureDomain:</h4>
         *     Help
         * <h4>FeatureDescription:</h4>
         *     callbackhandler to open helpsite
         * <h4>FeatureResult:</h4>
         *   <ul>
         *     <li>open the helpsite
         *   </ul> 
         * <h4>FeatureKeywords:</h4>
         *     GUI Callback
         * @param url - the url of the helpsite
         */
        showHelpSite: function(url) {
            console.log("showHelpSite:" + " url:" + url);
            yaioShowHelpSite(url);
            return false;
        },
        
        toggleSysContainerForNode: function(node) {
            toggleNodeSysContainer(node.sysUID);
        },
        
        openNodeEditorForNode: function(node, mode) {
            yaioOpenNodeEditorForNode(node, mode);
        },
        
        renderNodeLine: function(node, trIdSelector) {
            // load me
            var data = {
                 node: {
                     data: {
                         basenode: node,
                     },
                     tr: trIdSelector,
                 } 
            };
            
            console.log("renderNodeLine nodeId=" + node.sysUID + " tr=" + $(trIdSelector).length);
            renderColumnsForNode(null, data, true);
        },
        
        ganttOptions: { 
            ganttRangeStart: ganttRangeStart, 
            ganttRangeEnd: ganttRangeEnd
        }
    };
});

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     the factory to check the authorization
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new authorisation-obj
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 */
yaioM.factory('authorization', function ($rootScope, $http) {
    return {
        authentificate: function(callback) {
            $http.get('/user/current').success(function(data) {
                console.log("authentificate: success " + data);
                if (data) {
                    $rootScope.authenticated = true;
                } else {
                    $rootScope.authenticated = false;
                }
                callback && callback();
            }).error(function(data) {
                console.log("authentificate: error " + data);
                $rootScope.authenticated = false;
                callback && callback();
            });
        }
    };
});

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     the controller to load login-page
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 */
yaioM.controller('AuthController', function($rootScope, $scope, $location, $http, $routeParams, setFormErrors, OutputOptionsEditor, authorization) {
    $scope.credentials = {};
    $scope.login = function() {
        $http.post('/login', $.param($scope.credentials), {
            headers : {
                "content-type" : "application/x-www-form-urlencoded"
            }
        }).success(function(data) {
            authorization.authentificate(function() {
                if ($rootScope.authenticated) {
                    if ($rootScope.lastLocation) {
                        $location.path($rootScope.lastLocation);
                    } else {
                        $location.path("/");
                    }
                    $scope.error = false;
                } else {
                    $location.path("/login");
                    $scope.error = true;
                }
            });
        }).error(function(data) {
            $location.path("/login");
            $scope.error = true;
            $rootScope.authenticated = false;
        })
    };
    
    $scope.logout = function() {
        $http.post('/logout', {}).success(function() {
            $rootScope.authenticated = false;
            $location.path("/");
        }).error(function(data) {
            $rootScope.authenticated = false;
        });
    }    
})

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     the controller to load the frontpage
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 */
yaioM.controller('FrontPageCtrl', function($rootScope, $scope, $location, $http, $routeParams, setFormErrors, OutputOptionsEditor, authorization, yaioUtils) {
    // include utils    
    $scope.yaioUtils = yaioUtils;
    
    // set vars
    var nodeId = $routeParams.nodeId;
    if (nodeId == null || nodeId == "" || ! nodeId) {
        nodeId = 'SysStart1';
    }
    console.log("FrontPageCtrl - processing nodeId=" + nodeId);
    
    // call authentificate 
    authorization.authentificate(function () {
        // check authentification
        if (! $rootScope.authenticated) {
            $location.path("/login");
            $scope.error = false;
        } else {
            // load data
            $scope.frontPageUrl = '/exports/htmlfrontpagefragment/' + nodeId;
        }
    });
})
    
/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     the controller to change language
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 */
yaioM.controller('LanguageCtrl', ['$translate', '$scope', function ($translate, $scope, yaioUtils) {
    // include utils    
    $scope.yaioUtils = yaioUtils;

    // define languageutils
    $scope.currentLanguageKey = $translate.currentLanguageKey;
    $scope.changeLanguage = function (langKey) {
        // change angularTranslate
        $translate.use(langKey);
        
        // change other languagetranslator
        window.lang.change(langKey);
        
        $scope.currentLanguageKey = langKey;
        
        // change icons
        $(".button-lang").removeClass("button-lang-active").addClass("button-lang-inactive");
        $("#button_lang_" + langKey).removeClass("button-lang-inactive").addClass("button-lang-active");
    };
}]);


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
yaioM.controller('NodeSearchCtrl', function($rootScope, $scope, $location, $http, $routeParams, setFormErrors, authorization, yaioUtils) {
    // include utils    
    $scope.yaioUtils = yaioUtils;

    // create search
    $scope.nodes = new Array();
    
    $scope.searchOptions = {
            curPage: 1,
            pageSize: 20,
            searchSort: 'lastChangeDown',
            baseSysUID: "MasterplanMasternode1",
            fulltext: "",
            total: 0
    };
    if ($routeParams.curPage)
        $scope.searchOptions.curPage = decodeURI($routeParams.curPage); 
    if ($routeParams.pageSize)
        $scope.searchOptions.pageSize = decodeURI($routeParams.pageSize); 
    if ($routeParams.searchSort)
        $scope.searchOptions.searchSort = decodeURI($routeParams.searchSort); 
    if ($routeParams.baseSysUID)
        $scope.searchOptions.baseSysUID = decodeURI($routeParams.baseSysUID); 
    if ($routeParams.fulltext)
        $scope.searchOptions.fulltext = decodeURI($routeParams.fulltext); 
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
    }
    
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
    }

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
            uri = uri + encodeURI($scope.searchOptions.fulltext)
            + '/';
        }

        // load data
        var searchNodeUrl = '/nodes/search'
                            + uri;
        $http.get(searchNodeUrl).then(function(nodeResponse) {
            // success handler
            
            // check response
            var state = nodeResponse.data.state;
            if (state == "OK") {
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
                logError("error loading nodes:" + nodeResponse.data.stateMsg 
                        + " details:" + nodeResponse, true)
            }
        }, function(response) {
            // error handler
            var data = response.data;
            var status = response.status;
            var header = response.header;
            var config = response.config;
            var message = "error loading nodes with url: " + searchNodeUrl;
            logError(message, true);
            message = "error data: " + data + " header:" + header + " config:" + config;
            logError(message, false);
        });
    }
    
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
                parentStr = "<b>" + htmlEscapeText(parentStr) + "</b>";
                
                // extract search words
                var searchExtract = "";
                if ($scope.searchOptions.fulltext 
                    && $scope.searchOptions.fulltext.length > 0
                    && node.nodeDesc != undefined) {
                    // split to searchwords
                    var searchWords = $scope.searchOptions.fulltext.split(" ");
                    var searchWord, searchResults, splitLength, splitText;

                    var descText = node.nodeDesc;
                    descText = descText.replace(/\<WLBR\>/g, "\n");
                    descText = descText.replace(/\<WLESC\>/g, "\\");
                    descText = descText.replace(/\<WLTAB\>/g, "\t");
                    descText = descText.toLowerCase();
                    
                    for (var idx in searchWords) {
                        searchWord = escapeRegExp(searchWords[idx]);

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
                                    + htmlEscapeText(splitText) + "...";
                            }
                            if (idx2 < searchResults.length) {
                                splitLength = (searchResults[idx2].length > 50 ? 50 : searchResults[idx2].length);
                                splitText = searchResults[idx2].substr(
                                        searchResults[idx2].length - splitLength, 
                                        searchResults[idx2].length);
                                console.log("found " + searchWord + " before use " + splitLength + " extracted:" + splitText);
                                searchExtract += "..." 
                                    + htmlEscapeText(splitText);
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
        for (var idx in $scope.nodes) {
            var node = $scope.nodes[idx];
            yaioRecalcGanttBlock(node);
        }
    }
    
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
    }
});


/***************************************
 ***************************************
 * OutputOptions for ExportLinks
 ***************************************
 ***************************************/

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     new functions to control the outputoptions
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new function
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration
 */
yaioM.factory('OutputOptionsEditor', function($http) {
    var node = {};
    var oOptions =  {};
    var url, target;

    // default-values
    oOptions.flgDoIntend = true;
    oOptions.flgShowBrackets = true;
    oOptions.intendFuncArea = 80;
    oOptions.flgIntendSum = false;

    oOptions.maxEbene = 9999;
    oOptions.maxUeEbene = 3;
    oOptions.intend = 2;
    oOptions.intendLi = 2;
    oOptions.intendSys = 160;
    oOptions.flgTrimDesc = true;
    oOptions.flgReEscapeDesc = true;

    oOptions.flgShowState = true;
    oOptions.flgShowType = true;
    oOptions.flgShowName = true;
    oOptions.flgShowResLoc = true;
    oOptions.flgShowSymLink = true;
    oOptions.flgShowDocLayout = true;
    oOptions.flgShowIst = true;
    oOptions.flgShowPlan = true;
    oOptions.flgShowChildrenSum = false;
    oOptions.flgShowMetaData = true;
    oOptions.flgShowSysData = true;
    oOptions.flgShowDesc = true;
    oOptions.flgShowDescWithUe = false;
    oOptions.flgShowDescInNextLine = false;

    oOptions.flgChildrenSum = false;
    oOptions.flgProcessDocLayout = false;
    oOptions.flgUsePublicBaseRef = false;
    oOptions.flgRecalc= false;
    oOptions.strClassFilter = "";
    oOptions.strTypeFilter = "";
    oOptions.strReadIfStatusInListOnly = "";
    
    // define the functions
    return {
        oOptions: oOptions,
        
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
        discard: function() {
            yaioCloseOutputOptionsEditor();
            console.log("discard done");
            return false;
        },
    
        /**
         * <h4>FeatureDomain:</h4>
         *     Download
         * <h4>FeatureDescription:</h4>
         *     callbackhandler to send and close the editor
         * <h4>FeatureResult:</h4>
         *   <ul>
         *     <li>send request and updates layout
         *   </ul> 
         * <h4>FeatureKeywords:</h4>
         *     GUI Callback
         */
        send: function() {
//            // branch depending on mode
//            var method;
//            method = "POST";
//
//            // define json for common fields
//            var json = JSON.stringify(oOptions);
//            
//            // create url
//            console.log("Send - url::" + url + " data:" + json);
//            
//            // do http
//            $http({
//                    method: method,
//                    url: url,
//                    data: json
//            }).then(function(response) {
//                // sucess handler
//                yaioCloseOutputOptionsEditor();
//                
//                downloadAsFile(null, response.data, "test.xxx", "dummy", "dummy");
//                
//                console.log("send done");
//            }, function(response) {
//                // error handler
//                var data = response.data;
//                var status = response.status;
//                var header = response.header;
//                var config = response.config;
//                var message = "error while do export with url: " + url;
//                logError(message, true);
//                message = "error data: " + data + " header:" + header + " config:" + config;
//                logError(message, false);
//            });

            yaioSendOutputOptionsEditor();
            yaioCloseOutputOptionsEditor();
            console.log("send done");
            return false;
        },
        
        /**
         * <h4>FeatureDomain:</h4>
         *     GUI Download
         * <h4>FeatureDescription:</h4>
         *     callbackhandler to open the outputOptionsEditor
         * <h4>FeatureResult:</h4>
         *   <ul>
         *     <li>open the outputOptionsEditor
         *   </ul> 
         * <h4>FeatureKeywords:</h4>
         *     GUI Callback
         * @param sysUID - the sysUID of the current node
         * @param url - the url to send
         * @param target - the target window-name
         */
        showOutputOptionsEditor: function(sysUID, newUrl, newTarget) {
            url = newUrl;
            target = newTarget;
            yaioOpenOutputOptionsEditor(sysUID, url, target);
            console.log("showOutputOptionsEditor done:" + " url:" + url);
            return false;
        }
    }
})

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     the controller to load the outputoptions register the yaio-functions
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new controller
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration BusinessLogic
 */
yaioM.controller('OutputOptionsCtrl', function($rootScope, $scope, $location, $http, $routeParams, setFormErrors, OutputOptionsEditor, yaioUtils) {
    // include utils    
    $scope.yaioUtils = yaioUtils;

    // register the editor
    $scope.outputOptionsEditor = OutputOptionsEditor;
    // create options
    $scope.oOptions = $scope.outputOptionsEditor.oOptions;
    
    console.log("OutputOptionsCtrl - started");
});

/***************************************
 ***************************************
 * errorhandling from https://nulogy.com/articles/designing-angularjs-directives#.VBp5gvnV-Po
 ***************************************
 ***************************************/

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     new function to set form-errors when using input-elements with attribute "witherrors" 
 *     and element "fielderrors" to show the corresponding errors 
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new function
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary Errorhandling
 */
yaioM.factory('setFormErrors', function() {
    // Registered withErrors controllers
    var withErrorCtrls = [];

    // The exposed service
    var setFormErrors = function(opts) {
        var fieldErrors = opts.fieldErrors;
        var ctrl = withErrorCtrls[opts.formName];

        Object.keys(fieldErrors).forEach(function(fieldName) {
            ctrl.setErrorsFor(fieldName, fieldErrors[fieldName]);
        });
    };

    // Registers withErrors controller by form name (for internal use)
    setFormErrors._register = function(formName, ctrl) {
        withErrorCtrls[formName] = ctrl;
    };

    return setFormErrors;
});


/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     adds the new attribute-directive "witherrors" to set/show errors from
 *     different sources (AngularJS, App, WebServices) for this element
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new directive
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary Errorhandling
 */
yaioM.directive('withErrors', ['setFormErrors', function(setFormErrors) {
    return {
        restrict: 'A',
        require: 'withErrors',
        controller: ['$scope', '$element', function($scope, $element) {
            var controls = {};

            this.addControl = function(fieldName, ctrl) {
                controls[fieldName] = ctrl;
            };

            this.setErrorsFor = function(fieldName, errors) {
                if (!(fieldName in controls)) return;
                return controls[fieldName].setErrors(errors);
            };

            this.clearErrorsFor = function(fieldName, errors) {
                if (!(fieldName in controls)) return;
                return controls[fieldName].clearErrors(errors);
            };
        }],
        link: function(scope, element, attrs, ctrl) {
            // Make this form controller accessible to setFormErrors service
            setFormErrors._register(attrs.name, ctrl);
        }
    }; 
}]);


/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     extend the element with the new attribute-directive "witherrors" to 
 *     set/show errors from different sources (AngularJS, App, WebServices) for this element
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new callback
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary Errorhandling
 */
function directiveFieldsWithErrors () {
    return {
        restrict: 'E',
        require: ['?ngModel', '?^withErrors'],
        scope: true,
        link: function(scope, element, attrs, ctrls) {
            var ngModelCtrl = ctrls[0];
            var withErrorsCtrl = ctrls[1];
            var fieldName = attrs.name;

            if (!ngModelCtrl || !withErrorsCtrl) return;

            // Watch for model changes and set errors if any
            scope.$watch(attrs.ngModel, function() {
                if (ngModelCtrl.$dirty && ngModelCtrl.$invalid) {
                    withErrorsCtrl.setErrorsFor(fieldName, errorMessagesFor(ngModelCtrl));
                } else if (ngModelCtrl.$valid) {
                    withErrorsCtrl.clearErrorsFor(fieldName);
                }
            });

            // Mapping Angular validation errors to a message
            var errorMessages = {
                    required: 'This field is required'
            };

            function errorMessagesFor(ngModelCtrl) {
                return Object.keys(ngModelCtrl.$error).
                map(function(key) {
                    if (ngModelCtrl.$error[key]) return errorMessages[key];
                    else return null;
                }).
                filter(function(msg) {
                    return msg !== null;
                });
            }
        }
    }  
}

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     extend the element with the new attribute-directive "witherrors" to 
 *     set/show errors from different sources (AngularJS, App, WebServices) for this element
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns updated directive
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary Errorhandling
 */
yaioM.directive('input', function() {
    return directiveFieldsWithErrors();
});
//yaioM.directive('select', function() {
//    return directiveFieldsWithErrors();
//});
//yaioM.directive('textarea', function() {
//    return directiveFieldsWithErrors();
//});

/**
 * <h4>FeatureDomain:</h4>
 *     Configuration
 * <h4>FeatureDescription:</h4>
 *     adds the new element-directive "fielderrors" to set/show errors from
 *     different sources (AngularJS, App, WebServices) for this element
 * <h4>FeatureResult:</h4>
 *   <ul>
 *     <li>returns new directive
 *   </ul> 
 * <h4>FeatureKeywords:</h4>
 *     GUI Configuration Taglibrary Errorhandling
 */
yaioM.directive('fielderrors', function() {
    return {
        restrict: 'E',
        replace: true,
        scope: true,
        require: ['fielderrors', '^withErrors'],
        template: 
            '<div class="fielderror" ng-repeat="error in errors">' +
            '<small class="error">{{ error }}</small>' +
            '</div>',
            controller: ['$scope', function($scope) {
                $scope.errors = [];
                this.setErrors = function(errors) {
                    $scope.errors = errors;
                };
                this.clearErrors = function() {
                    $scope.errors = [];
                };
            }],
            link: function(scope, element, attrs, ctrls) {
                var fieldErrorsCtrl = ctrls[0];
                var withErrorsCtrl = ctrls[1];
                withErrorsCtrl.addControl(attrs.for, fieldErrorsCtrl);
            }
    };
});
