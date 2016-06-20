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
package de.yaio.app.extension.dms.services;

import de.yaio.app.core.datadomain.ResContentData;
import de.yaio.app.core.datadomain.ResIndexData;
import de.yaio.app.core.node.UrlResNode;
import de.yaio.app.extension.dms.utils.DMSClient;
import de.yaio.services.dms.storage.StorageResourceVersion;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/** 
 * businesslogic for dataDomain: ResContentData (upload url/file to dms)
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.extension.dms
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Service
public class ResDocumentService {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(ResDocumentService.class);

    @Autowired
    protected DMSClient dmsProvider;

    /**
     * get content for version from dms
     * @param datanode               node
     * @param version                version of the request content 
     * @return                       Inputstream
     * @throws IOException           Exceptions possible
     */
    public InputStream downloadResContentFromDMS(final ResContentData datanode, final Integer version)
                    throws IOException {
        if (!UrlResNode.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        
        UrlResNode node = (UrlResNode) datanode;
        
        String resContentDMSId = node.getResContentDMSId();
        if (!StringUtils.isEmpty(resContentDMSId)) {
            return dmsProvider.getContentFromDMS(resContentDMSId, version);
        }
        
        return null;
    }

    /**
     * get metadata for version from dms
     * @param datanode               node
     * @param version                version of the request content 
     * @return                       StorageResourceVersion
     * @throws IOException           Exceptions possible
     */
    public StorageResourceVersion getMetaDataForResContentFromDMS(final ResContentData datanode, final Integer version) 
                    throws IOException {
        if (!UrlResNode.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        
        UrlResNode node = (UrlResNode) datanode;
        
        String resContentDMSId = node.getResContentDMSId();
        if (!StringUtils.isEmpty(resContentDMSId)) {
            return dmsProvider.getMetaDataForContentFromDMS(resContentDMSId, version);
        }
        
        return null;
    }

    /**
     * get index for version from dms
     * @param datanode               node
     * @param version                version of the request index 
     * @return                       Inputstream
     * @throws IOException           Exceptions possible
     */
    public InputStream downloadResIndexFromDMS(final ResIndexData datanode, final Integer version)
                    throws IOException {
        if (!UrlResNode.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        
        UrlResNode node = (UrlResNode) datanode;
        
        String resIndexDMSId = node.getResIndexDMSId();
        if (!StringUtils.isEmpty(resIndexDMSId)) {
            return dmsProvider.getContentFromDMS(resIndexDMSId, version);
        }
        
        return null;
    }

    /**
     * get metadata for version from dms
     * @param datanode               node
     * @param version                version of the request index 
     * @return                       StorageResourceVersion
     * @throws IOException           Exceptions possible
     */
    public StorageResourceVersion getMetaDataForResIndexFromDMS(final ResIndexData datanode, final Integer version) 
                    throws IOException {
        if (!UrlResNode.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        
        UrlResNode node = (UrlResNode) datanode;
        
        String resIndexDMSId = node.getResIndexDMSId();
        if (!StringUtils.isEmpty(resIndexDMSId)) {
            return dmsProvider.getMetaDataForContentFromDMS(resIndexDMSId, version);
        }
        
        return null;
    }
}
