Yaio.NodeDataRenderService = function(appBase) {
    // my own instance
    var me = JsHelferlein.ServiceBase(appBase);

    /**
     * initialize the object
     */
    me._init = function() {
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Layout
     *     Rendering
     * <h4>FeatureDescription:</h4>
     *     Callback for fancyftree. Renders the full block for corresponding basenode.
     *     Manipulates the default-fancytree-tablerow (replace+add+hide elements).<br>
     *     <ul>
     *       <li>data.node.tr: Html-Obj of the table-line
     *       <li>data.node.data.basenode: the basenode (java de.yaio.core.node.BaseNode)
     *     </ul>
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>Updates DOM
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree Rendering
     * @param event - fancytree-event
     * @param data - the fancytreenode-data (basenode = data.node.data.basenode, tr = data.node.tr)
     * @param preventActionsColum - dont replace Action-column
     */
    me.renderColumnsForNode = function(event, data, preventActionsColum) {
        // extract nodedata
        var node = data.node;
        var basenode = node.data.basenode;
        var nodestate = basenode.state;
        var statestyle = "node-state-" + nodestate;
    
        var colName = 0;
        var colData = 1;
        var colGantt = 2;
        var colActions = 3;
        
        // get tdlist
        var $tdList = $(node.tr).find(">td");
    
        // add stateclasss to tr
        $(node.tr).addClass("container_nodeline");
        
        // add fields
        if (! preventActionsColum) {
            $tdList.eq(colActions).html(
                    "<div class='fieldtype_sysToggler toggler_show'>"
                        + "<a onclick=\"javascript: yaioAppBase.get('YaioExplorerAction').toggleNodeSysContainer('" + basenode.sysUID + "'); return false;\""
                                + " id='toggler_sys_" + basenode.sysUID + "'"
                                + " class='' "
                                + " data-tooltip='tooltip.command.ToggleSys' lang='tech'></a>"
                        + "</div>"
                    + "<a onclick=\"javascript: yaioAppBase.get('YaioEditor').yaioOpenNodeEditor('" + basenode.sysUID + "', 'edit'); return false;\""
                            + " id='cmdEdit" + basenode.sysUID + "'"
                            + " class='yaio-icon-edit'"
                            + " lang='tech' data-tooltip='tooltip.command.NodeEdit'></a>"
                    + "<a onclick=\"javascript: yaioAppBase.get('YaioEditor').yaioOpenNodeEditor('" + basenode.sysUID + "', 'create'); return false;\""
                            + " id='cmdCreate" + basenode.sysUID + "'"
                            + " class='yaio-icon-create'"
                            + " lang='tech' data-tooltip='tooltip.command.NodeCreateChild'></a>"
                    + "<a onclick=\"javascript: yaioAppBase.get('YaioEditor').yaioOpenNodeEditor('" + basenode.sysUID + "', 'createsymlink'); return false;\""
                            + " id='cmdCreateSymLink" + basenode.sysUID + "'"
                            + " class='yaio-icon-createsymlink'"
                            + " lang='tech' data-tooltip='tooltip.command.NodeCreateSymLink'></a>"
                    + "<a onclick=\"javascript: yaioAppBase.get('YaioExplorerAction').yaioRemoveNodeById('" + basenode.sysUID + "'); return false;\""
                            + " id='cmdRemove" + basenode.sysUID + "'"
                            + " class='yaio-icon-remove'"
                            + " lang='tech' data-tooltip='tooltip.command.NodeDelete'></a>"
                    ).addClass("container_field")
                     .addClass("fieldtype_actions")
                     //.addClass(statestyle)
                     ;
        }
    
        // replace checkbox by center-command
        var $expanderEle = $tdList.eq(colName).find("span.fancytree-expander");
        $expanderEle.attr('data-tooltip', 'tooltip.command.NodeShow');
        $expanderEle.attr('lang', 'tech');
        $expanderEle.attr('id', 'expander' + basenode.sysUID);
    
        // replace checkbox by center-command
        var $checkEle = $tdList.eq(colName).find("span.fancytree-checkbox");
        $checkEle.html("<a href='#/show/" + basenode.sysUID + "'"
            + " class='yaio-icon-center'"
            + " lang='tech' data-tooltip='tooltip.command.NodeFocus'></a>");
        $checkEle.removeClass("fancytree-checkbox").addClass("command-center");
    
        // manipulate name-column
        $tdList.eq(colName).addClass("container_field")
                     .addClass("fieldtype_name")
                     .addClass("field_name")
                     //.addClass(statestyle)
                     ;
        
        // define name
        var name = basenode.name;
        if (name == null || name.length <= 0) {
           if (basenode.className == "UrlResNode") {
               name = basenode.resLocName;
           } else if (basenode.className == "SymLinkNode") {
               name = basenode.symLinkName;
           }
        }
    
        // insert state before name-span
        var $nameEle = $tdList.eq(colName).find("span.fancytree-title");
        var $div = $("<div style='disply: block-inline' />")
            .append($("<span class='" + statestyle + " fancytree-title-state' lang='de' id='titleState" + basenode.sysUID + "'/>")
                        .html(basenode.state + " ")
                        )
            .append("&nbsp;")
            .append($("<span class='fancytree-title2' id='title" + basenode.sysUID + "'>" 
                    + me.appBase.get('YaioBase').htmlEscapeText(name) + "</span>"))
            ;
        $nameEle.html($div);
        //$tdList.eq(colName).find("span.fancytree-expander").addClass(statestyle);
        
        // render datablock
        var $nodeDataBlock = me.appBase.get('YaioNodeDataRender').renderDataBlock(basenode, node);
        
        // add SysData
        // create sys row
        var $row = $("<div class='togglecontainer field_nodeSys' id='detail_sys_" + basenode.sysUID + "' />");
        $nodeDataBlock.append($row);
        $row.append(
                $("<div lang='tech' />").html("Stand: " + me.appBase.get('YaioBase').formatGermanDateTime(basenode.sysChangeDate))
                        .addClass("container_field")
                        .addClass("fieldtype_basedata")
                        .addClass("fieldtype_sysChangeDate")
                        .addClass("field_sysChangeDate")
                        );
        $row.append(
                $("<div lang='tech' />").html(" (V " + basenode.sysChangeCount + ")")
                        .addClass("container_field")
                        .addClass("fieldtype_basedata")
                        .addClass("fieldtype_sysChangeCount")
                        .addClass("field_sysChangeCount")
                        );
        $row.append(
                $("<div lang='tech' />").html("angelegt: " + me.appBase.get('YaioBase').formatGermanDateTime(basenode.sysCreateDate))
                        .addClass("container_field")
                        .addClass("fieldtype_basedata")
                        .addClass("fieldtype_sysCreateDate")
                        .addClass("field_sysCreateDate")
                        ); 
        $row.append(
                $("<div lang='tech' />").html("Kinder: " + basenode.statChildNodeCount 
                        + " Workflows: " + basenode.statWorkflowCount
                        + " ToDos: " + basenode.statWorkflowTodoCount)
                        .addClass("container_field")
                        .addClass("fieldtype_basedata")
                        .addClass("fieldtype_statistik")
                        .addClass("field_statistik")
                        ); 
        
        // add nodeDesc if set
        if (basenode.nodeDesc != "" && basenode.nodeDesc != null) {
            // columncount
            //var columnCount = $(">td", $nodedataBlock).length;
            
            // add  column
            $($nodeDataBlock).find("div.container_data_row").append(
                    $("<div />").html("<a href='#'" +
                            " onclick=\"yaioAppBase.get('YaioExplorerAction').toggleNodeDescContainer('" + basenode.sysUID + "'); return false;\"" +
                                " id='toggler_desc_" + basenode.sysUID + "'" +
                                " data-tooltip='tooltip.command.ToggleDesc' lang='tech'></a>")
                            .addClass("container_field")
                            .addClass("fieldtype_descToggler")
                            .addClass("toggler_show")
                            //.addClass(statestyle)
                            );
            
            // create desc row
            var $divDesc = $("<div class='togglecontainer' id='detail_desc_" + basenode.sysUID + "' />");
            $divDesc.addClass("field_nodeDesc");
    
            // add commands
            var commands = "<div class='container-commands-desc' id='commands_desc_" + basenode.sysUID + "'"
                + " data-tooltip='tooltip.command.TogglePreWrap' lang='tech' >" 
                + "<input type='checkbox' id='cmd_toggle_content_desc_" + basenode.sysUID + "' onclick=\"yaioAppBase.get('UIToggler').togglePreWrap('#content_desc_" + basenode.sysUID + "');yaioAppBase.get('UIToggler').togglePreWrap('#container_content_desc_" + basenode.sysUID + "'); return true;\">"
                + "<span lang='tech'>im Originallayout anzeigen</span>"
        //        + "<input type='checkbox' id='cmd_toggle_content_desc_markdown_" + basenode.sysUID + "' onclick=\"toggleDescMarkdown('#container_content_desc_" + basenode.sysUID + "'); return true;\">"
        //        + "<span lang='tech'>Markdown</span>"
                ;
            commands += "<a class=\"button command-desc-jiraexport\" onClick=\"yaioAppBase.get('YaioExplorerAction').openJiraExportWindow('"+ basenode.sysUID + "'); return false;" 
                +   "\" lang='tech' data-tooltip='tooltip.command.OpenJiraExportWindow'>common.command.OpenJiraExportWindow</a>";
            commands += "<a class=\"button command-desc-txtexport\" onClick=\"yaioAppBase.get('YaioExplorerAction').openTxtExportWindow(" 
                +   "$('#container_content_desc_" + basenode.sysUID + "').text()); return false;" 
                +   "\" lang='tech' data-tooltip='tooltip.command.OpenTxtExportWindow'>common.command.OpenTxtExportWindow</a>";
            if ('speechSynthesis' in window) {
                // Synthesis support. Make your web apps talk!
                commands += "<a class=\"button\" onClick=\"yaioAppBase.get('YaioLayout').openSpeechSynthWindow(" 
                    +   "document.getElementById('container_content_desc_" + basenode.sysUID + "')); return false;" 
                    +   "\" lang='tech' data-tooltip='tooltip.command.OpenSpeechSynth'>common.command.OpenSpeechSynth</a>";
     
               }        
            commands += "</div>";
            $divDesc.append(commands);
            $divDesc.append("<div class='container-toc-desc' id='toc_desc_" + basenode.sysUID + "'><h1>Inhalt</h1></div>");
            
            // append content
            var descText = basenode.nodeDesc;
            descText = descText.replace(/\<WLBR\>/g, "\n");
            descText = descText.replace(/\<WLESC\>/g, "\\");
            descText = descText.replace(/\<WLTAB\>/g, "\t");
    
            // prepare descText
            var descHtml = me.appBase.get('YaioFormatter').formatMarkdown(descText, false, basenode.sysUID);
            $divDesc.append("<div id='container_content_desc_" + basenode.sysUID + "' class='container-content-desc syntaxhighlighting-open'>" + descHtml + "</div>");
    
            // append to datablock
            $nodeDataBlock.append($divDesc);
    
            // disable draggable for td.block_nodedata
            $( "#tree" ).draggable({ cancel: "td.block_nodedata" });
            
            // enable selction
            $("td.block_nodedata").enableSelection();
        }
        
        // add nodeData
        $tdList.eq(colData).html($nodeDataBlock).addClass("block_nodedata");
        
        // add TOC
        var settings = {
                toc: {}
        };
        settings.toc.dest = $("#toc_desc_" + basenode.sysUID);
        settings.toc.minDeep = 2;
        $.fn.toc($("#container_content_desc_" + basenode.sysUID), settings);
    
        // add gantt
        var $nodeGanttBlock = me.appBase.get('YaioNodeGanttRender').renderGanttBlock(basenode, node);
        $tdList.eq(colGantt).html($nodeGanttBlock).addClass("block_nodegantt");
    
        // set visibility of data/gantt-block
        if ($("#tabTogglerGantt").css("display") != "none") {
            $tdList.eq(colData).css("display", "table-cell");
            $tdList.eq(colGantt).css("display", "none");
        } else {
            $tdList.eq(colGantt).css("display", "table-cell");
            $tdList.eq(colData).css("display", "none");
        }
        
        // toogle sys
        me.appBase.get('YaioExplorerAction').toggleNodeSysContainer(basenode.sysUID);
        
        // toogle desc
        me.appBase.get('YaioExplorerAction').toggleNodeDescContainer(basenode.sysUID);
    
        // calc nodeData
        me.appBase.get('YaioNodeGanttRender').yaioRecalcMasterGanttBlockFromTree();
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Layout
     *     Rendering
     * <h4>FeatureDescription:</h4>
     *     Renders the DataBlock for basenode and returns a JQuery-Html-Obj.
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>ReturnValue JQuery-Html-Object - the rendered datablock
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree Rendering
     * @param basenode - the nodedata to render (java de.yaio.core.node.BaseNode)
     * @param fancynode - the corresponding fancynode
     * @returns JQuery-Html-Object - the rendered datablock
     */
    me.renderDataBlock = function(basenode, fancynode) {
        // extract nodedata
        var nodestate = basenode.state;
        var statestyle = "node-state-" + nodestate;   
    
        var msg = "datablock for node:" + basenode.sysUID;
        console.log("renderDataBlock START: " + msg);
    
        // current datablock
        var $table = $("<div class='container_data_table'/>");
        var $row = $("<div class='container_data_row'/>");
        $table.append($row);
        
        // default fields
        $row.append($("<div />").html(me.appBase.get('YaioBase').htmlEscapeText(basenode.metaNodePraefix + basenode.metaNodeNummer))
                .addClass("container_field")
                .addClass("fieldtype_basedata")
                .addClass("fieldtype_metanummer")
                .addClass("field_metanummer")
                );
            
        $row.append($("<div lang='tech' />").html(basenode.className)
               .addClass("container_field")
               .addClass("fieldtype_basedata")
               .addClass("fieldtype_type")
               .addClass("field_type")
               .addClass(statestyle));
        if (basenode.className == "TaskNode" || basenode.className == "EventNode") {
            // TaskNode
            $row.append(
                    $("<div />").html("&nbsp;" + me.appBase.get('YaioBase').formatNumbers(basenode.istChildrenSumStand, 0, "%"))
                            .addClass("container_field")
                            .addClass("fieldtype_additionaldata")
                            .addClass("fieldtype_stand")
                            .addClass("field_istChildrenSumStand")
                            .addClass(statestyle)
                            ); 
            $row.append(
                    $("<div />").html("&nbsp;" + me.appBase.get('YaioBase').formatNumbers(basenode.istChildrenSumAufwand, 1, "h"))
                            .addClass("container_field")
                            .addClass("fieldtype_additionaldata")
                            .addClass("fieldtype_aufwand")
                            .addClass("field_istChildrenSumAufwand")
                            .addClass(statestyle)
                            );
            $row.append(
                    $("<div />").html("&nbsp;" + me.appBase.get('YaioBase').formatGermanDate(basenode.istChildrenSumStart)
                            + "-" + me.appBase.get('YaioBase').formatGermanDate(basenode.istChildrenSumEnde))
                             .addClass("container_field")
                             .addClass("fieldtype_additionaldata")
                             .addClass("fieldtype_fromto")
                             .addClass("field_istChildrenSum")
                             .addClass(statestyle)
                             );
            $row.append(
                    $("<div />").html("&nbsp;" + me.appBase.get('YaioBase').formatNumbers(basenode.planChildrenSumAufwand, 1, "h"))
                             .addClass("container_field")
                             .addClass("fieldtype_additionaldata")
                             .addClass("fieldtype_aufwand")
                             .addClass("field_planChildrenSumAufwand")
                             .addClass(statestyle)
                             );
            $row.append(
                    $("<div />").html("&nbsp;" + me.appBase.get('YaioBase').formatGermanDate(basenode.planChildrenSumStart)
                             + "-" + me.appBase.get('YaioBase').formatGermanDate(basenode.planChildrenSumEnde))
                             .addClass("container_field")
                             .addClass("fieldtype_additionaldata")
                             .addClass("fieldtype_fromto")
                             .addClass("field_planChildrenSum")
                             .addClass(statestyle)
                             );
        } else if (basenode.className == "InfoNode" || basenode.className == "UrlResNode") {
            // Info + urlRes
            
            // Url only
            if (basenode.className == "UrlResNode") {
                // url
                $row.append(
                        $("<div />").html("<a href='" + me.appBase.get('YaioBase').htmlEscapeText(basenode.resLocRef) + "' target='_blank'>" 
                                         + me.appBase.get('YaioBase').htmlEscapeText(basenode.resLocRef) + "</a>")
                                  .addClass("container_field")
                                  .addClass("fieldtype_additionaldata")
                                  .addClass("fieldtype_url")
                                  .addClass("field_resLocRef")
                                  ); 
            }
        
            // both
            if (   basenode.docLayoutTagCommand || basenode.docLayoutShortName
                || basenode.docLayoutAddStyleClass || basenode.docLayoutFlgCloseDiv) {
                    $row.append(
                            $("<div lang='tech' />").html("Layout ")
                                    .addClass("container_field")
                                    .addClass("fieldtype_additionaldata")
                                    .addClass("fieldtype_ueDocLayout")
                                    .addClass("field_ueDocLayout")
                                    ); 
                
                // check which docLayout is set    
                if (basenode.docLayoutTagCommand) {
                    $row.append(
                            $("<div lang='tech' />").html("Tag: " 
                                        + me.appBase.get('YaioBase').htmlEscapeText(basenode.docLayoutTagCommand))
                                    .addClass("container_field")
                                    .addClass("fieldtype_additionaldata")
                                    .addClass("fieldtype_docLayoutTagCommand")
                                    .addClass("field_docLayoutTagCommand")
                                    ); 
                }
                if (basenode.docLayoutAddStyleClass) {
                    $row.append(
                            $("<div lang='tech' />").html("Style: " 
                                        + me.appBase.get('YaioBase').htmlEscapeText(basenode.docLayoutAddStyleClass))
                                    .addClass("container_field")
                                    .addClass("fieldtype_additionaldata")
                                    .addClass("fieldtype_docLayoutAddStyleClass")
                                    .addClass("field_docLayoutAddStyleClass")
                                    ); 
                }
                if (basenode.docLayoutShortName) {
                    $row.append(
                            $("<div lang='tech' />").html("Kurzname: " 
                                        + me.appBase.get('YaioBase').htmlEscapeText(basenode.docLayoutShortName))
                                    .addClass("container_field")
                                    .addClass("fieldtype_additionaldata")
                                    .addClass("fieldtype_docLayoutShortName")
                                    .addClass("field_docLayoutShortName")
                                    ); 
                }
                if (basenode.docLayoutFlgCloseDiv) {
                    $row.append(
                            $("<div lang='tech' />").html("Block schlie&szligen!")
                                    .addClass("container_field")
                                    .addClass("fieldtype_additionaldata")
                                    .addClass("fieldtype_docLayoutFlgCloseDiv")
                                    .addClass("field_docLayoutFlgCloseDiv")
                                    ); 
                }
            }
        } else if (basenode.className == "SymLinkNode") {
            me.appBase.get('YaioNodeData').yaioLoadSymLinkData(basenode, fancynode);
        } 
    
        console.log("renderDataBlock DONE: " + msg);
    
        return $table;
    }
    

    /**
     * <h4>FeatureDomain:</h4>
     *     Layout Toggler
     * <h4>FeatureDescription:</h4>
     *     Shows the DataBlock<br> 
     *     Toggles DataBlock, GanttBlock and the links #tabTogglerData, #tabTogglerGantt.<br>
     *     Show all: td.block_nodedata, th.block_nodedata + #tabTogglerGantt<br>
     *     Hide all: td.block_nodegantt, th.block_nodegantt + #tabTogglerData<br>
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>Updates DOM
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     GUI Tree Rendering
     */
    me.yaioShowDataBlock = function() {
        me.appBase.get('UIToggler').toggleTableBlock("#tabTogglerData");
        me.appBase.get('UIToggler').toggleTableBlock("td.block_nodegantt, th.block_nodegantt");
        setTimeout(function(){
            me.appBase.get('UIToggler').toggleTableBlock("#tabTogglerGantt");
            me.appBase.get('UIToggler').toggleTableBlock("td.block_nodedata, th.block_nodedata");
        }, 400);
        // set it to none: force
        setTimeout(function(){
            $("#tabTogglerData").css("display", "none");
            $("td.block_nodegantt, th.block_nodegantt").css("display", "none");
        }, 400);
    }

    me._init();
    
    return me;
};
