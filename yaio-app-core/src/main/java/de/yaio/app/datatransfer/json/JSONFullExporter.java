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
package de.yaio.app.datatransfer.json;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.node.*;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.core.nodeservice.NodeService;
import de.yaio.app.datatransfer.exporter.ExporterImpl;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/** 
 * export nodes in JSON-Format
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.wiki
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JSONFullExporter extends ExporterImpl {
    // Logger
    private static final Logger LOGGER = Logger.getLogger(JSONFullExporter.class);

    /**
     * class for dynamic property-filtering
     */
    @JsonFilter("filter properties by name")
    protected class PropertyFilterMixIn { }
    
    // Jackson
    protected ObjectMapper mapper;
    protected ObjectWriter writer;
    
    /** 
     * service functions to export nodes as Wiki
     */
    public JSONFullExporter() {
        super();
        configureJSONExporter();
    }
    
    ////////////////
    // common export-functions
    ////////////////
    @Override
    public String getMasterNodeResult(final DataDomain pMasterNode,
            final OutputOptions oOptions) throws Exception {
        DataDomain masterNode = pMasterNode;
        // Parameter pruefen
        if (masterNode == null) {
            throw new IllegalArgumentException("Masternode must not be null: '" 
        + masterNode + "'");
        }

        // Mastennode falls leer löschen
        Map<String, DataDomain> masterChilds = masterNode.getChildNodesByNameMap();
        if (masterChilds.size() == 1) {
            masterNode = (DataDomain) masterChilds.values().toArray()[0];
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("set ParentNode empty for new Masternode " 
                        + masterNode.getNameForLogger());
            }
            masterNode.setParentNode(null);
        }

        // recalcData
        if (oOptions.isFlgRecalc()) {
            // if set do it for all
            masterNode.recalcData(NodeService.RecalcRecurseDirection.CHILDREN);
        }
        

        return this.getNodeResult(masterNode, "", oOptions).toString();
    }


    ////////////////
    // service-functions to generate JSON from node
    ////////////////
    @Override
    public StringBuffer getNodeResult(final DataDomain curNode,  final String praefix,
            final OutputOptions oOptions) throws Exception {
        StringBuffer res = new StringBuffer();
        BaseNode node = (BaseNode) curNode;
        
        // extract parents
        List<String> parentIdHierarchy = node.getBaseNodeService().getParentIdHierarchy(node);
        
        // reverse
        Collections.reverse(parentIdHierarchy);
        
        // add me
        parentIdHierarchy.add(node.getSysUID());

        // remove master
        if (parentIdHierarchy.size() > 0) {
            parentIdHierarchy.remove(0);
        }
        
        // create response
        JSONResponse response = new JSONResponse(
                        "OK", 
                        "node '" + node.getSysUID() + "' found", 
                        node, 
                        parentIdHierarchy);
        res.append(writer.writeValueAsString(response));

        return res;
    }

    protected void configureJSONExporter() {
        mapper = new ObjectMapper();
        
        // configure
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        
        // set Mixins
        mapper.addMixInAnnotations(BaseNode.class, JsonExportBaseNodeMixin.class);
        mapper.addMixInAnnotations(TaskNode.class, JsonExportBaseNodeMixin.class);
        mapper.addMixInAnnotations(EventNode.class, JsonExportBaseNodeMixin.class);
        mapper.addMixInAnnotations(InfoNode.class, JsonExportBaseNodeMixin.class);
        mapper.addMixInAnnotations(SymLinkNode.class, JsonExportBaseNodeMixin.class);
        mapper.addMixInAnnotations(UrlResNode.class, JsonExportBaseNodeMixin.class);
        
        // add additional filters
        mapper.addMixInAnnotations(Object.class, PropertyFilterMixIn.class);
        String[] ignorableFieldNames = {"importTmpId", "fullSrc", "srcName"};
        FilterProvider filters = new SimpleFilterProvider().addFilter("filter properties by name",
                        SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
        writer = mapper.writer(filters);
    }
}
