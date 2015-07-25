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
        $row.append($("<div />").html(yaioAppBase.get('YaioBaseService').htmlEscapeText(basenode.metaNodePraefix + basenode.metaNodeNummer))
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
                    $("<div />").html("&nbsp;" + yaioAppBase.get('YaioBaseService').formatNumbers(basenode.istChildrenSumStand, 0, "%"))
                            .addClass("container_field")
                            .addClass("fieldtype_additionaldata")
                            .addClass("fieldtype_stand")
                            .addClass("field_istChildrenSumStand")
                            .addClass(statestyle)
                            ); 
            $row.append(
                    $("<div />").html("&nbsp;" + yaioAppBase.get('YaioBaseService').formatNumbers(basenode.istChildrenSumAufwand, 1, "h"))
                            .addClass("container_field")
                            .addClass("fieldtype_additionaldata")
                            .addClass("fieldtype_aufwand")
                            .addClass("field_istChildrenSumAufwand")
                            .addClass(statestyle)
                            );
            $row.append(
                    $("<div />").html("&nbsp;" + yaioAppBase.get('YaioBaseService').formatGermanDate(basenode.istChildrenSumStart)
                            + "-" + yaioAppBase.get('YaioBaseService').formatGermanDate(basenode.istChildrenSumEnde))
                             .addClass("container_field")
                             .addClass("fieldtype_additionaldata")
                             .addClass("fieldtype_fromto")
                             .addClass("field_istChildrenSum")
                             .addClass(statestyle)
                             );
            $row.append(
                    $("<div />").html("&nbsp;" + yaioAppBase.get('YaioBaseService').formatNumbers(basenode.planChildrenSumAufwand, 1, "h"))
                             .addClass("container_field")
                             .addClass("fieldtype_additionaldata")
                             .addClass("fieldtype_aufwand")
                             .addClass("field_planChildrenSumAufwand")
                             .addClass(statestyle)
                             );
            $row.append(
                    $("<div />").html("&nbsp;" + yaioAppBase.get('YaioBaseService').formatGermanDate(basenode.planChildrenSumStart)
                             + "-" + yaioAppBase.get('YaioBaseService').formatGermanDate(basenode.planChildrenSumEnde))
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
                        $("<div />").html("<a href='" + yaioAppBase.get('YaioBaseService').htmlEscapeText(basenode.resLocRef) + "' target='_blank'>" 
                                         + yaioAppBase.get('YaioBaseService').htmlEscapeText(basenode.resLocRef) + "</a>")
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
                                        + yaioAppBase.get('YaioBaseService').htmlEscapeText(basenode.docLayoutTagCommand))
                                    .addClass("container_field")
                                    .addClass("fieldtype_additionaldata")
                                    .addClass("fieldtype_docLayoutTagCommand")
                                    .addClass("field_docLayoutTagCommand")
                                    ); 
                }
                if (basenode.docLayoutAddStyleClass) {
                    $row.append(
                            $("<div lang='tech' />").html("Style: " 
                                        + yaioAppBase.get('YaioBaseService').htmlEscapeText(basenode.docLayoutAddStyleClass))
                                    .addClass("container_field")
                                    .addClass("fieldtype_additionaldata")
                                    .addClass("fieldtype_docLayoutAddStyleClass")
                                    .addClass("field_docLayoutAddStyleClass")
                                    ); 
                }
                if (basenode.docLayoutShortName) {
                    $row.append(
                            $("<div lang='tech' />").html("Kurzname: " 
                                        + yaioAppBase.get('YaioBaseService').htmlEscapeText(basenode.docLayoutShortName))
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
            yaioAppBase.get('YaioNodeDataService').yaioLoadSymLinkData(basenode, fancynode);
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
        yaioAppBase.get('UIToggler').toggleTableBlock("#tabTogglerData");
        yaioAppBase.get('UIToggler').toggleTableBlock("td.block_nodegantt, th.block_nodegantt");
        setTimeout(function(){
            yaioAppBase.get('UIToggler').toggleTableBlock("#tabTogglerGantt");
            yaioAppBase.get('UIToggler').toggleTableBlock("td.block_nodedata, th.block_nodedata");
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
