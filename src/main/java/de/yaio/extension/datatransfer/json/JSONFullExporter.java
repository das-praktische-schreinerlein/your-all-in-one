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
package de.yaio.extension.datatransfer.json;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.EventNode;
import de.yaio.core.node.InfoNode;
import de.yaio.core.node.SymLinkNode;
import de.yaio.core.node.TaskNode;
import de.yaio.core.node.UrlResNode;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.datatransfer.exporter.ExporterImpl;
import de.yaio.datatransfer.exporter.OutputOptions;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     export nodes in JSON-Format
 * 
 * @package de.yaio.extension.datatransfer.wiki
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JSONFullExporter extends ExporterImpl {
    // Logger
    private static final Logger LOGGER = Logger.getLogger(JSONFullExporter.class);

    /**
     * class for dynamic property-filtering
     */
    @JsonFilter("filter properties by name")
    protected class PropertyFilterMixIn { }
    
    /**
     * structure of the basic json-response
     */
    protected class JSONResponse {
        /** the resulting state of the request OK/ERROR **/
        protected String state;
        /** the corresponding message to the state **/
        protected String stateMsg;
        /** the resulting node for the request **/
        protected DataDomain node;
        /** the list of the parentSysUID **/
        protected List<String> parentIdHierarchy;

        public JSONResponse(final String state, final String stateMsg, 
                            final DataDomain node, final List<String> parentIdHierarchy) {
            super();
            this.state = state;
            this.stateMsg = stateMsg;
            this.node = node;
            this.parentIdHierarchy = parentIdHierarchy;
        }

        /**
         * @return the {@link JSONFullExporter.JSONResponse#state}
         */
        public final String getState() {
            return this.state;
        }

        /**
         * @param state the {@link JSONFullExporter.JSONResponse#state} to set
         */
        public final void setState(final String state) {
            this.state = state;
        }

        /**
         * @return the {@link JSONFullExporter.JSONResponse#stateMsg}
         */
        public final String getStateMsg() {
            return this.stateMsg;
        }

        /**
         * @param stateMsg the {@link JSONFullExporter.JSONResponse#stateMsg} to set
         */
        public final void setStateMsg(final String stateMsg) {
            this.stateMsg = stateMsg;
        }

        /**
         * @return the {@link JSONFullExporter.JSONResponse#node}
         */
        public final DataDomain getNode() {
            return this.node;
        }

        /**
         * @param node the {@link JSONFullExporter.JSONResponse#node} to set
         */
        public final void setNode(final DataDomain node) {
            this.node = node;
        }

        /**
         * @return the {@link JSONFullExporter.JSONResponse#parentIdHierarchy}
         */
        public final List<String> getParentIdHierarchy() {
            return this.parentIdHierarchy;
        }

        /**
         * @param parentIdHierarchy the {@link JSONFullExporter.JSONResponse#parentIdHierarchy} to set
         */
        public final void setParentIdHierarchy(final List<String> parentIdHierarchy) {
            this.parentIdHierarchy = parentIdHierarchy;
        }
    }

    // Jackson
    protected ObjectMapper mapper;
    protected ObjectWriter writer;
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     service functions to export nodes as Wiki
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the exporter
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
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
            masterNode.recalcData(NodeService.CONST_RECURSE_DIRECTION_CHILDREN);
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
