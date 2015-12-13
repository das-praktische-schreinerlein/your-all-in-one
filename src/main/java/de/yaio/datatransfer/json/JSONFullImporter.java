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
package de.yaio.datatransfer.json;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.ImporterImpl;

/** 
 * import nodes in JSON-Format
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.wiki
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JSONFullImporter extends ImporterImpl {
    // Logger
    private static final Logger LOGGER = Logger.getLogger(JSONFullImporter.class);

    /**
     * class for dynamic property-filtering
     */
    @JsonFilter("filter properties by name")
    protected class PropertyFilterMixIn { }
    
    // Jackson
    protected ObjectMapper mapper;
    
    /** 
     * service functions to export nodes as Wiki
     * @FeatureDomain                Constructor
     * @FeatureResult                initialize the exporter
     * @FeatureKeywords              Constructor
     * @param options                importoptions
     */
    public JSONFullImporter(final ImportOptions options) {
        super(options);
        configureJSONImporter();
    }
    
    /** 
     * parse a JSONResponse-String to a JSONResponse-Obj
     * @FeatureDomain                JSONParser
     * @FeatureResult                returns JSONResponse
     * @FeatureKeywords              JSONParser
     * @param jsonSrc                json-source of a JSONResponse to parse
     * @return                       a JSONResponse-Obj
     * @throws JsonProcessingException possible
     * @throws IOException           possible
     */
    public JSONResponse parseJSONResponse(final String jsonSrc) throws JsonProcessingException, IOException {
        ObjectReader jsonReader = mapper.reader(JSONResponse.class);

        JSONResponse response = jsonReader.readValue(jsonSrc);
        return response;
    }
    
    ////////////////
    // service-functions to generate JSON from node
    ////////////////
    protected void configureJSONImporter() {
        mapper = new ObjectMapper();
        
        // configure
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        
        // add deserializer
        DataDomainDeserializer deserializer = new DataDomainDeserializer();
        SimpleModule module = new SimpleModule("DataDomainDeserializer", new Version(1, 0, 0, null));
        module.addDeserializer(DataDomain.class, deserializer);

        mapper.registerModule(module);
    }
}
