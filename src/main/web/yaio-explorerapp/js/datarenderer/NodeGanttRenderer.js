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
 * servicefunctions for gantt-rendering
 *  
 * @FeatureDomain                WebGUI
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.NodeGanttRenderer = function(appBase) {
    'use strict';

    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    };

    /**
     * Shows the GanttBlock - Updates DOM
     * Toggles DataBlock, GanttBlock and the links #tabTogglerData, #tabTogglerGantt.
     * Hide all: td.block_nodedata, th.block_nodedata + #tabTogglerGantt
     * Show all: td.block_nodegantt, th.block_nodegantt + #tabTogglerData
     */
    me.showGanttView = function() {
        var svcUIToggler = me.appBase.get('UIToggler');

        svcUIToggler.toggleTableBlock('#tabTogglerGantt');
        svcUIToggler.toggleTableBlock('td.block_nodedata, th.block_nodedata');
        setTimeout(function(){
            svcUIToggler.toggleTableBlock('#tabTogglerData');
            svcUIToggler.toggleTableBlock('td.block_nodegantt, th.block_nodegantt');
        }, 400);
        // set it to none: force
        setTimeout(function(){
            me.$('#tabTogglerGantt').css('display', 'none');
            me.$('td.block_nodedata, th.block_nodedata').css('display', 'none');
        }, 400);
    };

    /**
     * activate one of the gantt-blocks for the element - Updates DOM
     * When flgMeOnly ist set: activate #gantt_ist_container_ + #gantt_plan_container
     * to display only the gantt with the data of this node
     * When flgMeOnly ist nmot set: activate #gantt_istChildrenSum_container_ + #gantt_planChildrenSum_container
     * to display only the gantt with the data of this node and children
     * @param {FancytreeNode} node             the FancytreeNode
     * @param {Boolean} flgMeOnly              true - display only the gantt for the node / false - node+children
     */
    me.showGanttBlockForNode = function(node, flgMeOnly) {
        if (flgMeOnly) {
            console.debug('yaioActivateGanttBlock: activate gantt - only own data for ' + node.key);
            // I'm expanded: show only my own data
            me.$('#gantt_istChildrenSum_container_' + node.key).css('display', 'none');
            me.$('#gantt_planChildrenSum_container_' + node.key).css('display', 'none');
            me.$('#gantt_ist_container_' + node.key).css('display', 'block');
            me.$('#gantt_plan_container_' + node.key).css('display', 'block');
        } else {
            // I'm collapsed: show me and my childsum
            console.debug('yaioActivateGanttBlock: activate gantt - sum data of me+children for ' + node.key);
            me.$('#gantt_ist_container_' + node.key).css('display', 'none');
            me.$('#gantt_plan_container_' + node.key).css('display', 'none');
            me.$('#gantt_istChildrenSum_container_' + node.key).css('display', 'block');
            me.$('#gantt_planChildrenSum_container_' + node.key).css('display', 'block');
        }

        // recalc gantt tree
        me.recalcMasterGanttBlockForTree();
    };

    /**
     * recalc gantt-block for the basenode - Updates DOM
     * calls fillGanttBlock for (plan, ist, planChildrenSum, istChildrenSum)
     * @param {Object} basenode               the basenode to recalc (java de.yaio.core.node.BaseNode)
     */
    me.recalcGanttBlockForNode = function(basenode) {
        me._fillGanttBlock(basenode, 'plan', 'Plan', null);
        me._fillGanttBlock(basenode, 'planChildrenSum', 'PlanSum', null);
        me._fillGanttBlock(basenode, 'ist', 'Ist', null);
        me._fillGanttBlock(basenode, 'istChildrenSum', 'IstSum', null);
    };

    /**
     * recalc all gantt-blocks of the fancytree-nodes (iterates over getRooNode.visit() - Updates DOM
     * calls yaioRecalcGanttBlock for every node and afterwards me.recalcMasterGanttBlockForTree()
     */
    me.recalcGanttBlocksForTree = function() {
        if (me.$('#tree').length > 0) {
            // tree exists
            me.$('#tree').fancytree('getRootNode').visit(function(node){
                me.recalcGanttBlockForNode(node.data.basenode);
            });
        }
        me.recalcMasterGanttBlockForTree();
    };

    /**
     * recalc mastergantt-block for the basenode on top of the page - Updates DOM
     * calls yaioRecalcGanttBlock and yaioRecalcMasterGanttBlockFromTree
     * @param {Object} basenode               the basenode to recalc (java de.yaio.core.node.BaseNode)
     */
    me.recalcMasterGanttBlock = function(basenode) {
        // default: set with own
        me.recalcGanttBlockForNode(basenode);

        // calc from tree
        me.recalcMasterGanttBlockForTree();
    };


    /* jshint maxstatements: 100 */
    /**
     * Updates GUI: #gantt_' + type + '_container_' + basenode.sysUID
     * Calcs+renders the gantt-block of specified type: (ist, plan, planChildrenSum, 
     * istChildrenSum) for basenode. Updates this elements:
     * <ul>
     *   <li> #gantt_' + type + '_container_' + basenode.sysUID
     *   <li> #gantt_' + type + '_aufwand_' + basenode.sysUID
     *   <li> #gantt_' + type + '_bar_' + basenode.sysUID
     * </ul>
     * @param {Object} basenode               the nodedata to render (java de.yaio.core.node.BaseNode)
     * @param {String} type                   the type of data to calc (ist, plan, planChildrenSum, istChildrenSum)
     * @param {String} label                  the label to show if aufwand >0
     * @param {JQuery} $divLine               optional ganttContainer to use - if not set #gantt_' + type + '_container_' + basenode.sysUID will be used
     */
    me._fillGanttBlock = function (basenode, type, label, $divLine) {
        var msg = 'ganttblock for node:' + basenode.sysUID;
    
        // get divs
        if (! $divLine) {
            $divLine = me.$('#gantt_' + type + '_container_' + basenode.sysUID);
        }
        var $divLabel = me.$($divLine).find('#gantt_' + type + '_aufwand_' + basenode.sysUID);
        var $div = me.$($divLine).find('#gantt_' + type + '_bar_' + basenode.sysUID);
        
        // reset
        $divLabel.html('');
        $div.html('&nbsp;');
        $div.css('width', 0);
        $div.css('margin-left', 0);
        $div.attr('data-rangeaufwand', 0);
    
        // set range
        var dateRangeStartStr = me.$('#inputGanttRangeStart').val();
        var dateRangeEndStr = me.$('#inputGanttRangeEnde').val();
        if (me.appBase.DataUtils.isUndefinedStringValue(dateRangeStartStr) || me.appBase.DataUtils.isUndefinedStringValue(dateRangeEndStr)) {
            console.error('fillGanttBlock range is not set correctly: '
                    + dateRangeStartStr + '-' + dateRangeEndStr + ' ' + msg);
            return;
        }
        
        // calc dates...
        var lstDate=dateRangeStartStr.split('.');
        var dateRangeStart = new Date(lstDate[1]+'/'+lstDate[0]+'/'+lstDate[2]);
        lstDate=dateRangeEndStr.split('.');
        var dateRangeEnd = new Date(lstDate[1]+'/'+lstDate[0]+'/'+lstDate[2]);
        if (me.appBase.DataUtils.isUndefinedStringValue(dateRangeStart) || me.appBase.DataUtils.isUndefinedStringValue(dateRangeEndStr)) {
            console.error('fillGanttBlock range is not set correctly: '
                    + dateRangeStartStr + '-' + dateRangeEndStr + ' ' + msg);
            return;
        }
        var dateRangeStartMillis = dateRangeStart.getTime();
        var dateRangeEndMillis = dateRangeEnd.getTime();
        if (isNaN(dateRangeStartMillis) || isNaN(dateRangeEndMillis)) {
            console.error('fillGanttBlock range is not set correctly: '
                    + dateRangeStartStr + '-' + dateRangeEndStr + ' ' + msg);
            return;
        }
    
        var rangeWidth = 450;
        var rangeDays = (dateRangeEndMillis-dateRangeStartMillis);
        var rangeFactor = rangeWidth / rangeDays;
    
        // check if dates are set
        var startMillis = basenode[type + 'Start'];
        var endMillis = basenode[type + 'Ende'];
        var aufwand = basenode[type + 'Aufwand'];
        if (!me.appBase.DataUtils.isUndefinedStringValue(startMillis) && !me.appBase.DataUtils.isUndefinedStringValue(endMillis)) {
    
            var startPos = 0;
            var endPos = 0;
            var rangeAufwand = 0;
            var flgMatchesRange = false;
            
            // add 8h to the end
            endMillis = endMillis + 1000 * 60 * 60 * 8;
            
            // check if range matches
            if (startMillis > dateRangeEndMillis) {
                // sorry you start later
                console.log('fillGanttBlock SKIP sorry you start later: '
                        + startMillis + '>' + dateRangeEndMillis + ' ' + msg);
            } else if (endMillis < dateRangeStartMillis) {
                // sorry you end before
                console.log('fillGanttBlock SKIP sorry you start later: '
                        + endMillis + '<' + dateRangeStartMillis + ' ' + msg);
            } else {
                // we match
                flgMatchesRange = true;
                if (startMillis > dateRangeStartMillis) {
                    //
                    startPos = (startMillis - dateRangeStartMillis) * rangeFactor;
                } else {
                    // we start on it
                    startPos = 0;
                }
                
                if (endMillis < dateRangeEndMillis) {
                    //
                    endPos = (endMillis - dateRangeStartMillis) * rangeFactor;
                } else {
                    // we start on it
                    endPos = rangeWidth;
                }
                
                // calc aufwand in range
                rangeAufwand = (endPos - startPos) * aufwand / ((endMillis - startMillis) * rangeFactor);
            }
            
            if (flgMatchesRange) {
                $div.html('&nbsp;');
                $div.css('width', endPos-startPos);
                $div.css('margin-left', startPos);
                
                // show aufwand
                if (rangeAufwand > 0) {
                    $divLabel.html('<span class="gantt_aufwand_label">' + label + ':' + '</span>'
                                   + '<span class="gantt_aufwand_value"' + ' data-rangeaufwand="' + rangeAufwand + '">'
                                     + me.appBase.get('DataUtils').formatNumbers(rangeAufwand, 0, 'h') + '</span>');
                    $div.attr('data-rangeaufwand', rangeAufwand);
                }
                
                console.log('fillGanttBlock MATCHES width: '
                        + startPos + '-' + endPos + ' aufwand:' + rangeAufwand + ' ' + msg);
            } else {
                console.log('fillGanttBlock SKIP dates not matched: ' + msg);
            }
        } else {
            console.log('fillGanttBlock SKIP no planDates: ' + msg);
        }
    };
    /* jshint maxstatements: 50 */

    /** 
     * Updates GUI: #gantt_' + type + '_container_' + basenode.sysUID
     * Create the gantt-block of specified type: (ist, plan, planChildrenSum,
     * istChildrenSum) for basenode. Creates this elements:
     * <ul>
     *   <li> #gantt_' + type + '_container_' + basenode.sysUID
     *   <li> #gantt_' + type + '_aufwand_' + basenode.sysUID
     *   <li> #gantt_' + type + '_bar_' + basenode.sysUID
     * </ul>
     * @param {Object} basenode               the nodedata to render (java de.yaio.core.node.BaseNode)
     * @param {String} type                   the type of data to calc (ist, plan, planChildrenSum, istChildrenSum)
     * @param {String} addStyle               optional css-class to add to t-element
     * @param {String} label                  the label to show if aufwand >0
     * @returns {JQuery}                      JQuery-Html-Object - the rendered ganttblock
     */
    me._createGanttBlock = function(basenode, type, addStyle, label) {
        // create line
        var $divLine = me.$('<div id="gantt_' + type + '_container_' + basenode.sysUID + '"' +
                ' class ="gantt_container gantt_' + type + '_container"' +
                ' lang="tech" data-tooltip="tooltip.hint.Gantt"/>');
        
        // create aufwand
        var $divLabel = me.$('<div id="gantt_' + type + '_aufwand_' + basenode.sysUID + '"' +
                ' class ="gantt_aufwand ganttblock_' + type + '_aufwand" />');
        $divLabel.addClass(addStyle);
        $divLine.append($divLabel);
        
        // create gantt
        var $div = me.$('<div id="gantt_' +type + '_bar_' + basenode.sysUID + '"' +
                ' class ="gantt_bar gantt_' +type + '_bar" />');
        $div.addClass(addStyle);
        $divLine.append($div);
        
        // fill gantt
        me._fillGanttBlock(basenode, type, label, $divLine);
        
        return $divLine;
    };
    
    /** 
     * renders the full GanttBlock (ist, plan, istChildrenSum, planChildrenSum) 
     * for basenode and returns a JQuery-Html-Obj.
     * @param {Object} basenode               the nodedata to render (java de.yaio.core.node.BaseNode)
     * @param {FancytreeNode} fancynode       the corresponding fancynode
     * @returns {JQuery}                      JQuery-Html-Object - the rendered ganttblock
     */
    me.renderGanttBlock = function(basenode, fancynode) {
        // extract nodedata
        var nodestate = basenode.state;
        var statestyle = 'node-state-' + nodestate;
    
        var msg = 'ganttblock for node:' + basenode.sysUID;
        console.log('renderGanttBlock START: ' + msg);
    
        // current ganttblock
        var $table = me.$('<div class="container_gantt_table" />');
        var $row = me.$('<div class="container_gantt_row" />');
        $table.append($row);
        
        if (basenode.className === 'TaskNode' || basenode.className === 'EventNode') {
            // TaskNode
            var $div;
            
            // create plan
            $div = me._createGanttBlock(basenode, 'plan', statestyle, 'Plan');
            $row.append($div);
            $div = me._createGanttBlock(basenode, 'planChildrenSum', statestyle, 'PlanSum');
            $row.append($div);
            
            // create ist and add statestyle
            $div = me._createGanttBlock(basenode, 'ist', statestyle, 'Ist');
            $row.append($div);
            $div = me._createGanttBlock(basenode, 'istChildrenSum', statestyle, 'IstSum');
            $row.append($div);
        } else {
            console.log('renderGanttBlock SKIP no task or event: ' + msg);
        }
    
        console.log('renderGanttBlock DONE: ' + msg);
    
        return $table;
    };
    
    
    /**
     * recalc mastergantt-block from the tree-data - Updates DOM
     * extract nodeid of the masternode from '#masterTr.data-value'
     * calls yaioRecalcMasterGanttBlockLine for plan+ist
     */
    me.recalcMasterGanttBlockForTree = function() {
        // calc from children
        var masterNodeId = me.$('#masterTr').attr('data-value');
        if (!me.appBase.DataUtils.isUndefinedStringValue(masterNodeId)) {
            console.log('yaioRecalcMasterGanttBlockFromTree calc for masterNodeId:', masterNodeId);
            me._recalcMasterGanttBlockLine(masterNodeId, 'plan');
            me._recalcMasterGanttBlockLine(masterNodeId, 'ist');
        } else {
            console.log('yaioRecalcMasterGanttBlockFromTree skip: no masterNodeId');
        }
    };
    
    /** 
     * Recalcs mastergantt-line for praefix (plan, ist) with the tree-data - Updates DOM
     * It extracts the data-rangeaufwand from gantt_${praefix}ChildrenSum_bar_$masterNodeId
     * It iterates over all visible div.gantt_$praefix_bar, div.gantt_${praefix}ChildrenSum_bar
     * and adds their data-rangeaufwand
     * At the end the sumRangeAufwand will be placed on #gantt_${praefix}ChildrenSum_aufwand_{masterNodeId}
     * @param {String} masterNodeId           id of the masterNode on top of the page
     * @param {String} praefix                datablock to racalc (plan, ist)
     */
    me._recalcMasterGanttBlockLine = function(masterNodeId, praefix) {
        // calc rangeAufwand
        var sumRangeAufwand = 0;
    
        // init with aufwand of the masternode
        var masterBarId = '#gantt_' + praefix + '_bar_' + masterNodeId;
        var $masterBar = me.$(masterBarId);
        if ($masterBar.length > 0) {
            sumRangeAufwand = parseFloat($masterBar.attr('data-rangeaufwand'));
            console.log('yaioRecalcMasterGanttBlock type=' + praefix + ' found masterrangeaufwand :' + sumRangeAufwand + ' for ' + masterBarId);
        } else {
            console.log('yaioRecalcMasterGanttBlock type=' + praefix + ' no masterrangeaufwand :' + sumRangeAufwand + ' for ' + masterBarId);
        }
        
        // check for tree
        var treeId = '#tree';
        var tree = me.$(treeId).fancytree('getTree');
        if (me.appBase.DataUtils.isEmptyStringValue(me.$(treeId).length) || me.appBase.DataUtils.isEmptyStringValue(tree)) {
            me.appBase.get('Logger').logError('yaioRecalcMasterGanttBlock: error tree:"' + treeId + '" not found.', false);
            return;
        }
        
        // filter ganttblocks
        var filter = 'div.gantt_' + praefix + '_bar, div.gantt_' + praefix + 'ChildrenSum_bar';
        var $ganttBars = me.$(filter).filter(function () { 
            return me.$(this).parent().css('display') === 'block';
        });
        console.log('yaioRecalcMasterGanttBlock type=' + praefix + ' found:' + $ganttBars.length + ' for filter:' + filter);
        if ($ganttBars.length > 0) {
            me.$($ganttBars).each( function () {
                // check if node is visible
                var nodeId = this.id;
                nodeId = nodeId.replace(/gantt_(.*)bar_/, '');
                var treeNode = tree.getNodeByKey(nodeId);
                if (treeNode && treeNode.isVisible()) {
                    // node is visible: calc
                    var rangeAufwand = me.$(this).attr('data-rangeaufwand');
                    if (this.id.indexOf(masterNodeId) <= 0) {
                        console.log('yaioRecalcMasterGanttBlock type=' + praefix + ' found rangeaufwand :' + rangeAufwand + ' for ' + this.id);
                        sumRangeAufwand += parseFloat(rangeAufwand);
                        
                    } else {
                        console.log('yaioRecalcMasterGanttBlock type=' + praefix + ' ignore rangeaufwand from master:' + rangeAufwand + ' for ' + this.id);
                    }
                } else if (! treeNode) {
                    // not found
                    console.log('yaioRecalcMasterGanttBlock type=' + praefix + ' skip node not found nodeId:' + nodeId + ' for ' + this.id);
                } else {
                    // not visble
                    console.log('yaioRecalcMasterGanttBlock type=' + praefix + ' skip node not visble nodeId:' + nodeId + ' for ' + this.id);
                }
            });
        }
        console.log('yaioRecalcMasterGanttBlock type=' + praefix + ' calced rangeaufwand :' + sumRangeAufwand + ' for ' + masterNodeId);
    
        // update masterBlock
        var type = praefix + 'ChildrenSum';
        var $divLine = me.$('#gantt_' + type + '_container_' + masterNodeId);
        var $divLabel = me.$($divLine).find('#gantt_' + type + '_aufwand_' + masterNodeId);
        $divLabel.html('');
        if (sumRangeAufwand > 0)  {
            console.log('yaioRecalcMasterGanttBlock type=' + praefix + ' set gantt_aufwand_label with calced rangeaufwand :' + sumRangeAufwand + ' for ' + masterNodeId);
            $divLabel.html('<span class="gantt_aufwand_label">'
                    + praefix + 'Sum:' + '</span>'
                    + '<span class="gantt_aufwand_value"' + '>'
                    + me.appBase.get('DataUtils').formatNumbers(sumRangeAufwand, 0, 'h') + '</span>');
        } else {
            console.log('yaioRecalcMasterGanttBlock type=' + praefix + ' hide gantt_aufwand_label because no calced rangeaufwand :' + sumRangeAufwand + ' for ' + masterNodeId);
        }
    };
    

    me._init();
    
    return me;
};
