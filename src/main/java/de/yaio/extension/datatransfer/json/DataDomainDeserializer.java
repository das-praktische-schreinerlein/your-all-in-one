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
package de.yaio.extension.datatransfer.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.nodeservice.NodeService;

/** 
 * deserializer for DataDomain-nodes
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.wiki
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class DataDomainDeserializer extends JsonDeserializer<DataDomain> {

    @Override
    public DataDomain deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();

        JsonNode node = oc.readTree(jsonParser);
        
        // create obj from node
        DataDomain result = deserializeJSONNode(node);
        return result;
    }
    
    /** 
     * deserialize a JSONNode to a DataDomain: create BaseNode by className and craete Children fom childNodes
     * @FeatureDomain                JSONParser
     * @FeatureResult                returns JSONResponse
     * @FeatureKeywords              JSONParser
     * @param node                   the jsonNODE to deserialize
     * @return                       the created BaseNode with children
     * @throws IOException           possible
     */
    public DataDomain deserializeJSONNode(final JsonNode node) throws IOException {
        // create obj from className
        DataDomain result = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        
        // etxract data
        String className = node.get("className").textValue();
        JsonNode childNodes = node.get("childNodes");
        
        // create BaseNode
        try {
            result = (DataDomain) mapper.treeToValue(node, Class.forName(className));
        } catch (ClassNotFoundException e) {
            try {
                result = (DataDomain) mapper.treeToValue(node, Class.forName("de.yaio.core.node." + className));
                result.recalcData(NodeService.CONST_RECURSE_DIRECTION_ONLYME);
            } catch (ClassNotFoundException e2) {
                throw new IOException("cant instattiate className: " + e2.getMessage());
            } catch (Exception e2) {
                throw new IOException("cant recalc node: " + e2.getMessage());
            }
        }
        
        // create Childnodes
        for (JsonNode childNode : childNodes) {
            DataDomain newNode = (DataDomain) deserializeJSONNode(childNode);
            newNode.setParentNode(result);
        }
        
        return result;
    }
}
