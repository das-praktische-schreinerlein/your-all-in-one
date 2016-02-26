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
 * servicefunctions for data-rendering
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.NodeDataRenderer = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * Callback for fancyftree. Renders the full block for corresponding basenode.
     * Manipulates the default-fancytree-tablerow (replace+add+hide elements).
     * <ul>
     *   <li>data.node.tr: Html-Obj of the table-line
     *   <li>data.node.data.basenode: the basenode (java de.yaio.core.node.BaseNode)
     * </ul>
     * @param {FancyTreeEvent} event           fancytree-event
     * @param {FancyTreeData} data             the fancytreenode-data (basenode = data.node.data.basenode, tr = data.node.tr)
     * @param {Boolean} preventActionsColum    dont replace Action-column
     * @param {Boolean} flgRenderMinimum       render only the minimal subset of data
     */
    me.renderColumnsForNode = function(event, data, preventActionsColum, flgRenderMinimum) {
        var svcDataUtils = me.appBase.get('DataUtils');
        var svcYaioExplorerAction = me.appBase.get('YaioExplorerAction');
        var svcYaioNodeGanttRenderer = me.appBase.get('YaioNodeGanttRenderer');

        // extract nodedata
        var node = data.node;
        var basenode = node.data.basenode;
        var nodestate = basenode.state;
        var statestyle = 'node-state-' + nodestate;
    
        var colName = 0, colData = 1, colGantt = 2, colActions = 3;
        
        // get tdlist
        var $tdList = me.$(node.tr).find('>td');
    
        // add stateclasss to tr
        me.$(node.tr).addClass('container_nodeline');
        
        // replace checkbox by center-command
        var $expanderEle = $tdList.eq(colName).find('span.fancytree-expander');
        $expanderEle.attr('data-tooltip', 'tooltip.command.NodeShow');
        $expanderEle.attr('lang', 'tech');
        $expanderEle.attr('id', 'expander' + basenode.sysUID);
    
        // replace checkbox by center-command
        var $checkEle = $tdList.eq(colName).find('span.fancytree-checkbox');
        $checkEle.html('<a href="#/show/' + basenode.sysUID + '"' +
            ' class="yaio-icon-center"' +
            ' lang="tech" data-tooltip="tooltip.command.NodeFocus"></a>');
        $checkEle.removeClass('fancytree-checkbox').addClass('command-center');
    
        // manipulate name-column
        $tdList.eq(colName).addClass('container_field')
                     .addClass('fieldtype_name')
                     .addClass('field_name');
        
        // define name
        var name = basenode.name;
        if (me.appBase.DataUtils.isUndefinedStringValue(name)) {
           if (basenode.className === 'UrlResNode') {
               name = basenode.resLocName;
           } else if (basenode.className === 'SymLinkNode') {
               name = basenode.symLinkName;
           }
        }
    
        // insert state before name-span
        var $nameEle = $tdList.eq(colName).find('span.fancytree-title');
        var $div = me.$('<div style="display: inline-block" />')
            .append(me.$('<span class="' + statestyle + ' fancytree-title-state" lang="de" id="titleState' + basenode.sysUID + '"/>')
                .html(basenode.state + ' '))
            .append('&nbsp;')
            .append(me.$('<span class="fancytree-title2" id="title' + basenode.sysUID + '">' +
                svcDataUtils.htmlEscapeText(name) + '</span>'));
        $nameEle.html($div);

        // add nodeData
        var $nodeDataBlock = me.renderDataBlock(basenode, preventActionsColum, flgRenderMinimum);
        $tdList.eq(colData).html($nodeDataBlock).addClass('block_nodedata');

        // add action-links
        if (!preventActionsColum && !flgRenderMinimum) {
            // generate actions
            var actionHtml = me.renderNodeActionLinks(basenode);

            // add actions
            $tdList.eq(colActions).html(actionHtml)
                .addClass('container_field')
                .addClass('fieldtype_actions');
        }

        // add gantt
        if (!flgRenderMinimum) {
            var $nodeGanttBlock = svcYaioNodeGanttRenderer.renderGanttBlock(basenode, node);
            $tdList.eq(colGantt).html($nodeGanttBlock).addClass('block_nodegantt');
        
            // set visibility of data/gantt-block
            if (me.$('#tabTogglerGantt').css('display') !== 'none') {
                $tdList.eq(colData).css('display', 'table-cell');
                $tdList.eq(colGantt).css('display', 'none');
            } else {
                $tdList.eq(colGantt).css('display', 'table-cell');
                $tdList.eq(colData).css('display', 'none');
            }
        }

        // add TOC
        if (!flgRenderMinimum) {
            var settings = {
                toc: {}
            };
            settings.toc.dest = me.$('#toc_desc_' + basenode.sysUID);
            settings.toc.minDeep = 2;
            me.$.fn.toc(me.$('#container_content_desc_' + basenode.sysUID), settings);
        }

        // toogle sys
        svcYaioExplorerAction.toggleNodeSysContainerForNodeId(basenode.sysUID);
        
        // toogle desc
        svcYaioExplorerAction.toggleNodeDescContainerForNodeId(basenode.sysUID);
    
        // calc nodeData
        if (!flgRenderMinimum) {
            svcYaioNodeGanttRenderer.recalcMasterGanttBlockForTree();
        }
    };


    /**
     * render the whole data-block for the node
     * @param {Object} basenode    yaio-node to render
     * @param {Boolean} preventActionsColum    dont replace Action-column
     * @param {Boolean} flgRenderMinimum       render only the minimal subset of data
     * @returns {JQuery}           html
     */
    me.renderDataBlock = function(basenode, preventActionsColum, flgRenderMinimum) {
        var yaioAppBaseVarName = me.appBase.config.appBaseVarName,
            clickHandler;

        // render datablock
        var $nodeDataBlock = me.renderBaseDataBlock(basenode, preventActionsColum);

        // add SysData
        var $sysDataBlock = me.renderSysDataBlock(basenode);
        $nodeDataBlock.append($sysDataBlock);

        // add nodeDesc if set
        if (!me.appBase.DataUtils.isEmptyStringValue(basenode.nodeDesc) && !flgRenderMinimum) {
            // add  column
            clickHandler = yaioAppBaseVarName + '.YaioExplorerAction.toggleNodeDescContainerForNodeId(\'' + basenode.sysUID + '\'); return false;';
            me.$($nodeDataBlock).find('div.container_data_row').append(
                me.$('<div />').html('<a href="#"' +
                        ' onclick="' + clickHandler + '"' +
                        ' id="toggler_desc_' + basenode.sysUID + '"' +
                        ' data-tooltip="tooltip.command.ToggleDesc" lang="tech"></a>')
                    .addClass('container_field')
                    .addClass('fieldtype_descToggler')
                    .addClass('toggler_show')
            );

            var $descDataBlock = me.renderDescBlock(basenode);
            $nodeDataBlock.append($descDataBlock);

            // disable draggable for td.block_nodedata
            me.$( '#tree' ).draggable({ cancel: 'td.block_nodedata' });

            // enable selction
            me.$('td.block_nodedata').enableSelection();
        }

        return $nodeDataBlock;
    };

    /**
     * render the sysdata-block for the node
     * @param {Object} basenode    yaio-node to render
     * @returns {JQuery}           html
     */
    me.renderSysDataBlock = function(basenode) {
        var svcDataUtils = me.appBase.get('DataUtils');
        var $block = me.$('<div class="togglecontainer field_nodeSys" id="detail_sys_' + basenode.sysUID + '" />');
        $block.append(
            me.$('<div lang="tech" />').html('Stand: ' + svcDataUtils.formatGermanDateTime(basenode.sysChangeDate))
                .addClass('container_field')
                .addClass('fieldtype_basedata')
                .addClass('fieldtype_sysChangeDate')
                .addClass('field_sysChangeDate')
        );
        $block.append(
            me.$('<div lang="tech" />').html(' (V ' + basenode.sysChangeCount + ')')
                .addClass('container_field')
                .addClass('fieldtype_basedata')
                .addClass('fieldtype_sysChangeCount')
                .addClass('field_sysChangeCount')
        );
        $block.append(
            me.$('<div lang="tech" />').html('angelegt: ' + svcDataUtils.formatGermanDateTime(basenode.sysCreateDate))
                .addClass('container_field')
                .addClass('fieldtype_basedata')
                .addClass('fieldtype_sysCreateDate')
                .addClass('field_sysCreateDate')
        );
        $block.append(
            me.$('<div lang="tech" />').html('Kinder: ' + basenode.statChildNodeCount +
                    ' Workflows: ' + basenode.statWorkflowCount +
                    ' ToDos: ' + basenode.statWorkflowTodoCount +
                    ' Urls: ' + basenode.statUrlResCount +
                    ' Info: ' + basenode.statInfoCount)
                .addClass('container_field')
                .addClass('fieldtype_basedata')
                .addClass('fieldtype_statistik')
                .addClass('field_statistik')
        );

        return $block;
    };


    /**
     * render the desc-block for the node
     * @param {Object} basenode    yaio-node to render
     * @returns {JQuery}           html
     */
    me.renderDescBlock = function (basenode) {
        var yaioAppBaseVarName = me.appBase.config.appBaseVarName,
            clickHandler;

        // create desc row
        var $divDesc = me.$('<div class="togglecontainer" id="detail_desc_' + basenode.sysUID + '" />');
        $divDesc.addClass('field_nodeDesc');

        // add commands
        var commands = '<div class="container-commands-desc" id="commands_desc_' + basenode.sysUID + '"' +
            ' data-tooltip="tooltip.command.TogglePreWrap" lang="tech" >';

        clickHandler = yaioAppBaseVarName + '.UIToggler.togglePreWrap(\'#content_desc_' + basenode.sysUID + '\');' +
            yaioAppBaseVarName + '.get(\'UIToggler\').togglePreWrap(\'#container_content_desc_' + basenode.sysUID + '\'); return true;';
        commands += '<input type="checkbox" id="cmd_toggle_content_desc_' + basenode.sysUID +
            '" onclick="' + clickHandler + '">' +
            '<span lang="tech">im Originallayout anzeigen</span>';

        clickHandler = yaioAppBaseVarName + '.YaioExplorerAction.openJiraExportWindowByNodeId(\'' + basenode.sysUID + '\'); return false;';
        commands += '<a class="button command-desc-jiraexport" onClick="' + clickHandler + '" lang="tech"' +
            ' data-tooltip="tooltip.command.OpenJiraExportWindow">common.command.OpenJiraExportWindow</a>';

        clickHandler = yaioAppBaseVarName + '.YaioExplorerAction.openTxtExportWindowForContent(' + yaioAppBaseVarName + '.$(\'#container_content_desc_' + basenode.sysUID + '\').text()); return false;';
        commands += '<a class="button command-desc-txtexport" onClick="' + clickHandler + '" lang="tech"' +
            ' data-tooltip="tooltip.command.OpenTxtExportWindow">common.command.OpenTxtExportWindow</a>';
        if ('speechSynthesis' in window) {
            // Synthesis support. Make your web apps talk!
            clickHandler = yaioAppBaseVarName + '.SpeechSynthController.open(\'container_content_desc_' + basenode.sysUID + '\'); return false;';
            commands += '<a class="button" onClick="' + clickHandler + '" lang="tech"' +
                ' data-tooltip="tooltip.command.OpenSpeechSynth">common.command.OpenSpeechSynth</a>';

        }
        commands += '</div>';
        $divDesc.append(commands);
        $divDesc.append('<div class="container-toc-desc" id="toc_desc_' + basenode.sysUID + '"><h1>Inhalt</h1></div>');

        // append content
        var descText = basenode.nodeDesc;
        descText = descText.replace(/<WLBR>/g, '\n');
        descText = descText.replace(/<WLESC>/g, '\\');
        descText = descText.replace(/<WLTAB>/g, '\t');

        // prepare descText
        var descHtml = me.appBase.get('YaioFormatter').renderMarkdown(descText, false, basenode.sysUID);
        $divDesc.append('<div id="container_content_desc_' + basenode.sysUID + '"' +
            ' class="container-content-desc syntaxhighlighting-open">' + descHtml + '</div>');

        return $divDesc;
    };

    /**
     * render the available actions-links for the node
     * @param {Object} basenode    yaio-node to render
     * @returns {String}           html
     */
    me.renderNodeActionLinks = function(basenode) {
        var actionHtml = '',
            svcYaioAccessManager = me.appBase.get('YaioAccessManager'),
            yaioAppBaseVarName = me.appBase.config.appBaseVarName,
            clickHandler;

        if (svcYaioAccessManager.getAvailiableNodeAction('showsysdata', basenode.sysUID, false)) {
            clickHandler = yaioAppBaseVarName + '.YaioExplorerAction.toggleNodeSysContainerForNodeId(\'' + basenode.sysUID + '\'); return false;';
            actionHtml += '<div class="fieldtype_sysToggler">' +
                '<a onclick="javascript: ' + clickHandler + '"' +
                ' id="toggler_sys_' + basenode.sysUID + '"' +
                ' class="" ' +
                ' data-tooltip="tooltip.command.ToggleSys" lang="tech"></a>' +
                '</div>';
        }
        if (svcYaioAccessManager.getAvailiableNodeAction('edit', basenode.sysUID, false)) {
            clickHandler = yaioAppBaseVarName + '.YaioEditor.yaioOpenNodeEditor(\'' + basenode.sysUID + '\', \'edit\'); return false;';
            actionHtml += '<a onclick="javascript: ' + clickHandler + '"' +
                ' id="cmdEdit' + basenode.sysUID + '"' +
                ' class="yaio-icon-edit"' +
                ' lang="tech" data-tooltip="tooltip.command.NodeEdit"></a>';
        }
        if (svcYaioAccessManager.getAvailiableNodeAction('create', basenode.sysUID, false)) {
            clickHandler = yaioAppBaseVarName + '.YaioEditor.yaioOpenNodeEditor(\'' + basenode.sysUID + '\', \'create\'); return false;';
            actionHtml += '<a onclick="javascript: ' + clickHandler + '"' +
                ' id="cmdCreate' + basenode.sysUID + '"' +
                ' class="yaio-icon-create"' +
                ' lang="tech" data-tooltip="tooltip.command.NodeCreateChild"></a>';
        }
        if (svcYaioAccessManager.getAvailiableNodeAction('createsymlink', basenode.sysUID, false)) {
            clickHandler = yaioAppBaseVarName + '.YaioEditor.yaioOpenNodeEditor(\'' + basenode.sysUID + '\', \'createsymlink\'); return false;';
            actionHtml += '<a onclick="javascript: ' + clickHandler + '"' +
                ' id="cmdCreateSymLink' + basenode.sysUID + '"' +
                ' class="yaio-icon-createsymlink"' +
                ' lang="tech" data-tooltip="tooltip.command.NodeCreateSymLink"></a>';
        }
        if (svcYaioAccessManager.getAvailiableNodeAction('remove', basenode.sysUID, false)) {
            clickHandler = yaioAppBaseVarName + '.YaioExplorerAction.doRemoveNodeByNodeId(\'' + basenode.sysUID + '\'); return false;';
            actionHtml += '<a onclick="javascript: ' + clickHandler + '"' +
                ' id="cmdRemove' + basenode.sysUID + '"' +
                ' class="yaio-icon-remove"' +
                ' lang="tech" data-tooltip="tooltip.command.NodeDelete"></a>';
        }

        return actionHtml;
    };

    /* jshint maxstatements: 100 */
    /**
     * Renders the BaseDataBlock for basenode and returns a JQuery-Html-Obj.
     * @param {Object} basenode               the nodedata to render (java de.yaio.core.node.BaseNode)
     * @param {Boolean} preventActionsColum   dont replace Action-column
     * @returns {JQuery}                      JQuery-Html-Object - the rendered datablock
     */
    me.renderBaseDataBlock = function(basenode, preventActionsColum) {
        var svcDataUtils = me.appBase.get('DataUtils');
        var yaioAppBaseVarName = me.appBase.config.appBaseVarName;
        
        // extract nodedata
        var nodestate = basenode.state;
        var statestyle = 'node-state-' + nodestate;   
    
        var msg = 'datablock for node:' + basenode.sysUID;
        console.log('renderBaseDataBlock START: ' + msg);
    
        // current datablock
        var $table = me.$('<div class="container_data_table"/>');
        var $row = me.$('<div class="container_data_row"/>');
        $table.append($row);

        // default fields
        $row.append(me.$('<div />').html(svcDataUtils.htmlEscapeText(basenode.metaNodePraefix + basenode.metaNodeNummer))
            .addClass('container_field')
            .addClass('fieldtype_basedata')
            .addClass('fieldtype_metanummer')
            .addClass('field_metanummer')
        );

        $row.append(me.$('<div lang="tech" />').html(basenode.className)
            .addClass('container_field')
            .addClass('fieldtype_basedata')
            .addClass('fieldtype_type')
            .addClass('field_type')
            .addClass(statestyle));
        if (basenode.className === 'TaskNode' || basenode.className === 'EventNode') {
            // TaskNode
            $row.append(
                me.$('<div />').html('&nbsp;' + svcDataUtils.formatNumbers(basenode.istChildrenSumStand, 0, '%'))
                    .addClass('container_field')
                    .addClass('fieldtype_additionaldata')
                    .addClass('fieldtype_stand')
                    .addClass('field_istChildrenSumStand')
                    .addClass(statestyle)
            );
            $row.append(
                me.$('<div />').html('&nbsp;' + svcDataUtils.formatNumbers(basenode.istChildrenSumAufwand, 1, 'h'))
                    .addClass('container_field')
                    .addClass('fieldtype_additionaldata')
                    .addClass('fieldtype_aufwand')
                    .addClass('field_istChildrenSumAufwand')
                    .addClass(statestyle)
            );
            $row.append(
                me.$('<div />').html('&nbsp;' + svcDataUtils.formatGermanDate(basenode.istChildrenSumStart)
                        + '-' + svcDataUtils.formatGermanDate(basenode.istChildrenSumEnde))
                    .addClass('container_field')
                    .addClass('fieldtype_additionaldata')
                    .addClass('fieldtype_fromto')
                    .addClass('field_istChildrenSum')
                    .addClass(statestyle)
            );
            $row.append(
                me.$('<div />').html('&nbsp;' + svcDataUtils.formatNumbers(basenode.planChildrenSumAufwand, 1, 'h'))
                    .addClass('container_field')
                    .addClass('fieldtype_additionaldata')
                    .addClass('fieldtype_aufwand')
                    .addClass('field_planChildrenSumAufwand')
                    .addClass(statestyle)
            );
            $row.append(
                me.$('<div />').html('&nbsp;' + svcDataUtils.formatGermanDate(basenode.planChildrenSumStart)
                        + '-' + svcDataUtils.formatGermanDate(basenode.planChildrenSumEnde))
                    .addClass('container_field')
                    .addClass('fieldtype_additionaldata')
                    .addClass('fieldtype_fromto')
                    .addClass('field_planChildrenSum')
                    .addClass(statestyle)
            );
        } else if (basenode.className === 'InfoNode' || basenode.className === 'UrlResNode') {
            // render Info + UrlRes
            
            // Url only
            if (basenode.className === 'UrlResNode') {
                // url
                
                // upload content
                var resContentDMSState = basenode.resContentDMSState;
                var stateMapping = {'UPLOAD_DONE': 'Download', 'UPLOAD_OPEN': 'Webshoting', 'UPLOAD_FAILED': 'Webshot failed'};
                if (stateMapping[resContentDMSState] 
                    && me.appBase.get('YaioAccessManager').getAvailiableNodeAction('dmsDownload', basenode.sysUID, false)) {
                    // url
                    var stateBlock = svcDataUtils.htmlEscapeText(stateMapping[resContentDMSState]);
                    if (resContentDMSState === 'UPLOAD_DONE' && !preventActionsColum) {
                        stateBlock = '<a href="' + '" onClick="' + yaioAppBaseVarName + '.get(\'YaioExplorerAction\').openDMSDownloadWindowForNodeId(\''+ basenode.sysUID + '\'); return false;"'
                                     +   ' lang="tech" data-tooltip="tooltip.command.OpenDMSDownloadWindow_' + resContentDMSState + '">' + stateBlock + '</a>';
                    } else if (stateMapping[resContentDMSState]) {
                        stateBlock = '<span lang="tech" data-tooltip="tooltip.command.OpenDMSDownloadWindow_' + resContentDMSState + '">' + stateBlock + '</span>';
                    }
                    $row.append(
                        me.$('<div />').html(stateBlock)
                            .addClass('container_field')
                            .addClass('fieldtype_additionaldata')
                            .addClass('fieldtype_uploadsstate')
                            .addClass('field_resContentDMSState')
                            .addClass('field_resContentDMSState_' + resContentDMSState)
                    );
                }
                // extracted metadata
                var resIndexDMSState = basenode.resIndexDMSState;
                var indexStateMapping = {'INDEX_DONE': 'Metadata', 'INDEX_OPEN': 'Indexing', 'INDEX_FAILED': 'Indexing Failed'};
                if (indexStateMapping[resIndexDMSState] 
                    && me.appBase.get('YaioAccessManager').getAvailiableNodeAction('dmsDownload', basenode.sysUID, false)) {
                    // url
                    var stateBlock = svcDataUtils.htmlEscapeText(indexStateMapping[resIndexDMSState]);
                    if (resIndexDMSState === 'INDEX_DONE' && !preventActionsColum) {
                        stateBlock = '<a href="' + '" onClick="' + yaioAppBaseVarName + '.get(\'YaioExplorerAction\').openDMSIndexDownloadWindowForNodeId(\''+ basenode.sysUID + '\'); return false;"'
                                     +   ' lang="tech" data-tooltip="tooltip.command.OpenDMSIndexDownloadWindow_' + resIndexDMSState + '">' + stateBlock + '</a>';
                    } else if (indexStateMapping[resIndexDMSState]) {
                        stateBlock = '<span lang="tech" data-tooltip="tooltip.command.OpenDMSIndexDownloadWindow_' + resIndexDMSState + '">' + stateBlock + '</span>';
                    }
                    $row.append(
                        me.$('<div />').html(stateBlock)
                            .addClass('container_field')
                            .addClass('fieldtype_additionaldata')
                            .addClass('fieldtype_uploadsstate')
                            .addClass('field_resIndexDMSState')
                            .addClass('field_resIndexDMSState_' + resIndexDMSState)
                    );
                }
                
                // url-data
                var resLocData = svcDataUtils.htmlEscapeText(basenode.resLocRef);
                if (basenode.type === 'URLRES') {
                    resLocData = '<a href="' + resLocData + '" target="_blank">' + resLocData + '</a>';
                } else {
                    resLocData = '<span>' + resLocData + '</span>';
                }
                $row.append(
                    me.$('<div />').html(resLocData)
                        .addClass('container_field')
                        .addClass('fieldtype_additionaldata')
                        .addClass('fieldtype_url')
                        .addClass('field_resLocRef')
                );
            }
        
            // both
            if (   basenode.docLayoutTagCommand || basenode.docLayoutShortName
                || basenode.docLayoutAddStyleClass || basenode.docLayoutFlgCloseDiv) {
                // render both
                $row.append(
                    me.$('<div lang="tech" />').html('Layout ')
                        .addClass('container_field')
                        .addClass('fieldtype_additionaldata')
                        .addClass('fieldtype_ueDocLayout')
                        .addClass('field_ueDocLayout')
                );

                // check which docLayout is set    
                if (basenode.docLayoutTagCommand) {
                    $row.append(
                        me.$('<div lang="tech" />').html('Tag: '
                                + svcDataUtils.htmlEscapeText(basenode.docLayoutTagCommand))
                            .addClass('container_field')
                            .addClass('fieldtype_additionaldata')
                            .addClass('fieldtype_docLayoutTagCommand')
                            .addClass('field_docLayoutTagCommand')
                    );
                }
                if (basenode.docLayoutAddStyleClass) {
                    $row.append(
                        me.$('<div lang="tech" />').html('Style: '
                                + svcDataUtils.htmlEscapeText(basenode.docLayoutAddStyleClass))
                            .addClass('container_field')
                            .addClass('fieldtype_additionaldata')
                            .addClass('fieldtype_docLayoutAddStyleClass')
                            .addClass('field_docLayoutAddStyleClass')
                    );
                }
                if (basenode.docLayoutShortName) {
                    $row.append(
                        me.$('<div lang="tech" />').html('Kurzname: '
                                + svcDataUtils.htmlEscapeText(basenode.docLayoutShortName))
                            .addClass('container_field')
                            .addClass('fieldtype_additionaldata')
                            .addClass('fieldtype_docLayoutShortName')
                            .addClass('field_docLayoutShortName')
                    );
                }
                if (basenode.docLayoutFlgCloseDiv) {
                    $row.append(
                        me.$('<div lang="tech" />').html('Block schlie&szligen!')
                            .addClass('container_field')
                            .addClass('fieldtype_additionaldata')
                            .addClass('fieldtype_docLayoutFlgCloseDiv')
                            .addClass('field_docLayoutFlgCloseDiv')
                    );
                }
            }
        } else if (basenode.className === 'SymLinkNode') {
            // render SymLinkNode
            me.appBase.get('YaioNodeData').getNodeForSymLink(basenode)
                .done(function(yaioNodeActionResponse, textStatus, jqXhr ) {
                    console.log('call successHandler ' + msg + ' state:' + textStatus);
                    me.getNodeForSymLinkSuccessHandler(basenode, yaioNodeActionResponse, textStatus, jqXhr);
                });
        } 
    
        console.log('renderDataBlock DONE: ' + msg);
    
        return $table;
    };
    /* jshint maxstatements: 50 */

    /** 
     * Shows the DataBlock
     * Toggles DataBlock, GanttBlock and the links #tabTogglerData, #tabTogglerGantt.
     * Show all: td.block_nodedata, th.block_nodedata + #tabTogglerGantt
     * Hide all: td.block_nodegantt, th.block_nodegantt + #tabTogglerData
     */
    me.showDataView = function() {
        var svcUIToggler = me.appBase.get('UIToggler');

        svcUIToggler.toggleTableBlock('#tabTogglerData');
        svcUIToggler.toggleTableBlock('td.block_nodegantt, th.block_nodegantt');
        setTimeout(function(){
            svcUIToggler.toggleTableBlock('#tabTogglerGantt');
            svcUIToggler.toggleTableBlock('td.block_nodedata, th.block_nodedata');
        }, 400);
        // set it to none: force
        setTimeout(function(){
            me.$('#tabTogglerData').css('display', 'none');
            me.$('td.block_nodegantt, th.block_nodegantt').css('display', 'none');
        }, 400);
    };

    /**
     * succes-handler if getNodeForSymLink succeeded (resolves yaioNodeActionResponse.state)
     * @param {Object} basenode                     node to get symlinked-data for
     * @param {Object} yaioNodeActionResponse       the serverresponse (java de.yaio.rest.controller.NodeActionReponse)
     * @param {String} textStatus                   http-state as text
     * @param {JQueryXHR} jqXhr                     jqXhr-Object
     */
    me.getNodeForSymLinkSuccessHandler = function(basenode, yaioNodeActionResponse, textStatus, jqXhr) {
        var svcLogger = me.appBase.get('Logger');
        var msg = '_getNodeForSymLinkSuccessHandler for symLinkBaseId:' + basenode.sysUID;
        console.log(msg + ' OK done!' + yaioNodeActionResponse.state);
        if (yaioNodeActionResponse.state === 'OK') {
            if (yaioNodeActionResponse.node) {
                var $nodeDataBlock = me.appBase.get('YaioNodeDataRenderer').renderBaseDataBlock(yaioNodeActionResponse.node);

                // load referring node
                var tree = me.$('#tree').fancytree('getTree');
                if (!tree) {
                    // tree not found
                    svcLogger.logError('error getNodeForSymLink: cant load tree - ' + msg, false);
                    return null;
                }
                var rootNode = tree.rootNode;
                if (! rootNode) {
                    console.error(msg + ' openHierarchy: error for tree'
                        + ' rootNode not found: ' + msg);
                    return;
                }
                var treeNode = tree.getNodeByKey(basenode.sysUID);
                if (! treeNode) {
                    svcLogger.logError('error getNodeForSymLink: cant load node - ' + msg, false);
                    return null;
                }

                // append Link in current hierarchy to referenced node
                var newUrl = '#/show/' + tree.options.masterNodeId
                    + '/activate/' + yaioNodeActionResponse.node.sysUID + '/';

                // check if node-hierarchy exists (same tree)
                var firstNodeId, firstNode;
                var lstIdsHierarchy = [].concat(yaioNodeActionResponse.parentIdHierarchy);
                while (! firstNode && lstIdsHierarchy.length > 0) {
                    firstNodeId = lstIdsHierarchy.shift();
                    firstNode = rootNode.mapChildren[firstNodeId];
                }
                if (! firstNode) {
                    // load page for referenced node with full hierarchy
                    //firstNodeId = yaioNodeActionResponse.parentIdHierarchy.shift();
                    // we set it constant
                    firstNodeId = me.appBase.config.masterSysUId;

                    newUrl = '#/show/' + firstNodeId
                        + '/activate/' + yaioNodeActionResponse.node.sysUID + '/';
                }

                me.$(treeNode.tr).find('div.container_data_row').append(
                    '<a href="' + newUrl + '"'
                    + ' data-tooltip="Springe zum verkn&uuml;pften Element"'
                    + ' class="button">OPEN</a>');

                // add datablock of referenced node
                me.$(treeNode.tr).find('div.container_data_table').append($nodeDataBlock.html());

                console.log(msg + ' DONE');
            } else {
                svcLogger.logError('ERROR got no ' + msg, true);
            }
        } else {
            svcLogger.logError('ERROR cant load  ' + msg + ' error:' + yaioNodeActionResponse.stateMsg, true);
        }
    };

    me._init();
    
    return me;
};
