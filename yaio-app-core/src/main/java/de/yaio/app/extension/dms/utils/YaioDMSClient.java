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
package de.yaio.app.extension.dms.utils;

import de.yaio.commons.http.HttpUtils;
import de.yaio.services.dms.storage.StorageResource;
import de.yaio.services.dms.storage.StorageResourceVersion;
import de.yaio.services.dms.storage.StorageUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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

    // StorageUtils
    @Autowired
    protected StorageUtils storageUtils;

    @Value("${yaio.dms-client.yaio-dms-service.buffersize}")
    private int BUFFER_SIZE;

    @Override
    public StorageResource addContentToDMS(final String srcId, final String origFileName,
                                        final InputStream input) throws IOException {
        return saveResContentInDMS(true, srcId, origFileName, input);
    }

    @Override
    public StorageResource updateContentInDMS(final String dmsId, final String origFileName,
                                        final InputStream input) throws IOException {
        return saveResContentInDMS(false, dmsId, origFileName, input);
    }


    @Override
    public InputStream getContentFromDMS(final String dmsId, final Integer version) throws IOException {
        File tmpFile = this.getContentFileFromDMS(dmsId, version, false);
        return new FileInputStream(tmpFile);
    }
    
    @Override
    public File getContentFileFromDMS(final String dmsId, final Integer version, 
                                      final boolean useOriginalExtension) throws IOException {
        String ex = ".tmp";
        if (useOriginalExtension) {
            // extract extension from dms
            StorageResourceVersion storageResVersion = this.getMetaDataForContentFromDMS(dmsId, version);
            ex = "." + FilenameUtils.getExtension(storageResVersion.getResName());
        }

        // tmp-File
        File tmpFile = File.createTempFile("download", ex);
        tmpFile.deleteOnExit();

        // call url
        String baseUrl = dmsurl + "/get/" + dmsappId + "/" + dmsId + "/" + version;
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
    public StorageResourceVersion getMetaDataForContentFromDMS(final String dmsId, final Integer version) 
                    throws IOException {
        // call url
        String baseUrl = dmsurl + "/getmetaversion/" + dmsappId + "/" + dmsId + "/" + version;
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
        StorageResourceVersion resourceVersion = storageUtils.parseStorageResourceVersionFromJson(metaJson);

        return resourceVersion;
    }


    protected StorageResource saveResContentInDMS(final boolean flgNew, final String id, final String origFileName,
                                         final InputStream input) throws IOException {
        // upload file
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", dmsappId);
        
        // use different id-params
        if (flgNew) {
            params.put("srcId", id);
        } else {
            params.put("dmsId", id);
        }
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

        // configure
        String metaJson = EntityUtils.toString(entity);
        StorageResource resource = storageUtils.parseStorageResourceFromJson(metaJson);

        return resource;
    }
}
