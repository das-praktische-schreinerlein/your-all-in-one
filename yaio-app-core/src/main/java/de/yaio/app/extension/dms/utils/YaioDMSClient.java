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

import de.yaio.commons.io.IOExceptionWithCause;
import de.yaio.services.dms.api.model.StorageResource;
import de.yaio.services.dms.api.model.StorageResourceVersion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/** 
 * businesslogic for dms
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Service
public class YaioDMSClient implements DMSClient {
    
    @Value("${yaio.dms-client.yaio-dms-service.appId}")
    protected String dmsappId;
    
    @Value("${yaio.dms-client.yaio-dms-service.baseurl}")
    protected String dmsurl;

    @Value("${yaio.dms-client.yaio-dms-service.username}")
    protected String dmsusername;

    @Value("${yaio.dms-client.yaio-dms-service.password}")
    protected String dmspassword;

    @Value("${yaio.dms-client.yaio-dms-service.buffersize}")
    private int BUFFER_SIZE;

    protected de.yaio.services.dms.client.DMSClient client;

    public YaioDMSClient() {
        client = de.yaio.services.dms.client.DMSClient.createClient(
                dmsappId, dmsurl, dmsusername, dmspassword, BUFFER_SIZE);
    }

    @Override
    public StorageResource addContentToDMS(String id, String origFileName, InputStream input) 
            throws IOExceptionWithCause, IOException {
        return client.addContentToDMS(id, origFileName, input);
    }

    @Override
    public StorageResource updateContentInDMS(String id, String origFileName, InputStream input)
            throws IOExceptionWithCause, IOException {
        return client.updateContentInDMS(id, origFileName, input);
    }

    @Override
    public InputStream getContentFromDMS(String id, Integer version) throws IOExceptionWithCause, IOException {
        return client.getContentFromDMS(id, version);
    }

    @Override
    public File getContentFileFromDMS(String id, Integer version, boolean useOriginalExtension) 
            throws IOExceptionWithCause, IOException {
        return client.getContentFileFromDMS(id, version, useOriginalExtension);
    }

    @Override
    public StorageResourceVersion getMetaDataForContentFromDMS(String id, Integer version) 
            throws IOExceptionWithCause, IOException {
        return client.getMetaDataForContentFromDMS(id, version);
    }
}
