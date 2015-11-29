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
package de.yaio.extension.dms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.yaio.commons.http.HttpUtils;
import de.yaio.services.dms.storage.StorageResourceVersion;

/** 
 * businesslogic for dms
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Service
public class YaioDMSClient implements DMSClient {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(YaioDMSClient.class);

    @Value("${yaio.dms-client.yaio-dms-service.appId}")
    protected String dmsappId;
    
    @Value("${yaio.dms-client.yaio-dms-service.baseurl}")
    protected String dmsurl;

    @Value("${yaio.dms-client.yaio-dms-service.username}")
    protected String dmsusername;

    @Value("${yaio.dms-client.yaio-dms-service.password}")
    protected String dmspassword;

    // Jackson
    @Autowired
    protected ObjectMapper mapper;

    @Value("${yaio.dms-client.yaio-dms-service.buffersize}")
    private int BUFFER_SIZE;

    @Override
    public byte[] addContentToDMS(final String id, final String origFileName,
                                        final InputStream input) throws IOException {
        return saveResContentInDMS(true, id, origFileName, input);
    }

    @Override
    public byte[] updateContentInDMS(final String id, final String origFileName,
                                        final InputStream input) throws IOException {
        return saveResContentInDMS(false, id, origFileName, input);
    }


    @Override
    public InputStream getContentFromDMS(final String id, final Integer version) throws IOException {
        File tmpFile = this.getContentFileFromDMS(id, version, false);
        return new FileInputStream(tmpFile);
    }
    
    @Override
    public File getContentFileFromDMS(final String id, final Integer version, 
                                      final boolean useOriginalExtension) throws IOException {
        String ex = ".tmp";
        if (useOriginalExtension) {
            // extract extension from dms
            StorageResourceVersion storageResVersion = this.getMetaDataForContentFromDMS(id, version);
            ex = "." + FilenameUtils.getExtension(storageResVersion.getResName());
        }

        // tmp-File
        File tmpFile = File.createTempFile("download", ex);
        tmpFile.deleteOnExit();

        // call url
        String baseUrl = dmsurl + "/get/" + dmsappId + "/" + id + "/" + version;
        HttpResponse response = HttpUtils.callGetUrlPure(baseUrl, dmsusername, dmspassword, null);
        HttpEntity entity = response.getEntity();
        
        // check response
        int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOException("illegal reponse:" + response.getStatusLine() 
                            + " for urlcall:" + baseUrl 
                            + " response:" + EntityUtils.toString(entity));
        }

        // write bytes read from the input stream into the output stream
        IOUtils.write(EntityUtils.toByteArray(entity), new FileOutputStream(tmpFile));

        return tmpFile;
    }
    
    @Override
    public StorageResourceVersion getMetaDataForContentFromDMS(final String id, final Integer version) 
                    throws IOException {
        // call url
        String baseUrl = dmsurl + "/getmetaversion/" + dmsappId + "/" + id + "/" + version;
        HttpResponse response = HttpUtils.callGetUrlPure(baseUrl, dmsusername, dmspassword, null);
        HttpEntity entity = response.getEntity();
        
        // check response
        int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOException("illegal reponse:" + response.getStatusLine() 
                            + " for urlcall:" + baseUrl 
                            + " response:" + EntityUtils.toString(entity));
        }
        
        // configure
        String metaJson = EntityUtils.toString(entity);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        StorageResourceVersion resourceVersion = mapper.readValue(metaJson, StorageResourceVersion.class);

        return resourceVersion;
    }


    protected byte[] saveResContentInDMS(final boolean flgNew, final String id, final String origFileName,
                                         final InputStream input) throws IOException {
        // upload file
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", dmsappId);
        params.put("id", id);
        params.put("origFileName", origFileName);
        Map<String, String> binfileParams = new HashMap<String, String>();
        File tmpFile = File.createTempFile("upload", "tmp");
        binfileParams.put("file", tmpFile.getCanonicalPath());
        tmpFile.deleteOnExit();

        // write bytes read from the input stream into the output stream
        OutputStream outStream = new FileOutputStream(tmpFile);
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        while ((bytesRead = input.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        input.close();
        outStream.close();

        // call url
        String baseUrl = dmsurl;
        HttpResponse response;
        if (flgNew) {
            baseUrl += "/add";
            response = HttpUtils.callPostUrlPure(baseUrl, dmsusername, dmspassword, 
                            params, null, binfileParams);
        } else {
            baseUrl += "/update";
            response = HttpUtils.callPostUrlPure(baseUrl, dmsusername, dmspassword, 
                            params, null, binfileParams);
        }
        HttpEntity entity = response.getEntity();
        
        // check response
        int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOException("illegal reponse:" + response.getStatusLine() 
                            + " for urlcall:" + baseUrl 
                            + " with params:" + params
                            + " response:" + EntityUtils.toString(entity));
        }

        return EntityUtils.toByteArray(entity);
    }
}
