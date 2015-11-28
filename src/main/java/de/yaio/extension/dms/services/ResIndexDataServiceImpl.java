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
package de.yaio.extension.dms.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.ResContentData;
import de.yaio.core.datadomain.ResContentData.UploadWorkflowState;
import de.yaio.core.datadomain.ResIndexData;
import de.yaio.core.datadomain.ResIndexData.IndexWorkflowState;
import de.yaio.core.datadomain.ResLocData;
import de.yaio.core.datadomainservice.TriggeredDataDomainRecalcImpl;
import de.yaio.core.dbservice.DBFilter;
import de.yaio.core.node.UrlResNode;
import de.yaio.core.nodeservice.UrlResNodeService;
import de.yaio.extension.dms.utils.DMSClient;
import de.yaio.extension.dms.utils.YaioMetaExtractClient;
import de.yaio.utils.DataUtils;

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
public class ResIndexDataServiceImpl extends TriggeredDataDomainRecalcImpl implements ResIndexDataService {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(ResIndexDataServiceImpl.class);

    @Autowired
    protected YaioMetaExtractClient metaextractProvider;
    @Autowired
    protected DMSClient dmsProvider;

    public Class<?> getRecalcTargetClass() {
        return ResContentData.class;
    }

    @Override
    @Transactional
    public void doRecalcWhenTriggered(final DataDomain datanode) throws Exception {
        if (datanode == null) {
            return;
        }

        // Check if node is compatibel
        if (!UrlResNode.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        
        // Check if data is valid
        UrlResNode node = (UrlResNode) datanode;
        if (!UploadWorkflowState.UPLOAD_DONE.equals(node.getResContentDMSState())) {
            LOGGER.info("no UPLOAD_DONE for node:" + datanode.getNameForLogger());
            return;
        }
        if (!IndexWorkflowState.INDEX_OPEN.equals(node.getResIndexDMSState())) {
            LOGGER.info("no INDEX_OPEN for node:" + datanode.getNameForLogger());
            return;
        }

        // Roll
        try {
            // upload
            this.indexResLoc((ResLocData) datanode);
        } catch (IOException ex) {
            // error: reset id and set to failed
            LOGGER.error("error while indexing node to dms:" + datanode.getNameForLogger(), ex);
            node.setResIndexDMSState(IndexWorkflowState.INDEX_FAILED);
        }

        // save node
        node.getBaseNodeDBService().updateBaseNode(node);
    }

    @Override
    public List<DBFilter> getDBTriggerFilter() throws IOException {
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();

        // filter upload open only
        String sql = "(resContentDMSState = :eqresContentDMSState) and (resIndexDMSState = :eqresIndexDMSState)";
        List<DBFilter.Parameter> parameters = new ArrayList<DBFilter.Parameter>();
        parameters.add(new DBFilter.Parameter("eqresContentDMSState", UploadWorkflowState.UPLOAD_DONE));
        parameters.add(new DBFilter.Parameter("eqresIndexDMSState", IndexWorkflowState.INDEX_OPEN));
        dbFilters.add(new DBFilter(sql, parameters));

        return dbFilters;
    }

    @Override
    public void indexResLoc(final ResLocData datanode) throws IOException {
        if (!ResIndexData.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        // Check if node is compatibel
        if (!UrlResNode.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        UrlResNode node = (UrlResNode) datanode;
        
        // extract metadata
        byte[] response;
        if (UrlResNodeService.CONST_NODETYPE_IDENTIFIER_URLRES.equals(node.getType())) {
            // get metadata from url
            response = metaextractProvider.getMetaExtractFromUrl(datanode.getResLocRef());
        } else {
            // get metadata from file
            File contentFile = dmsProvider.getContentFileFromDMS(node.getResContentDMSId(), 0);
            response = metaextractProvider.getMetaExtractFromFile(contentFile.getCanonicalPath());
        }

        // push metadata to dms
        InputStream input = new ByteArrayInputStream(response);
        this.uploadResIndexToDMS((ResIndexData) datanode, "metadata.json", input);
    }

    @Override
    public void uploadResIndexToDMS(final ResIndexData datanode, final String fileName, final InputStream input) throws IOException {
        if (!UrlResNode.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        
        UrlResNode node = (UrlResNode) datanode;
        
        String normfileName = DataUtils.normalizeFileName(fileName);
        String resIndexDMSId = node.getResIndexDMSId();
        if (StringUtils.isEmpty(resIndexDMSId)) {
            // add new file
            resIndexDMSId = node.getSysUID() + "-" + "index";
            dmsProvider.addContentToDMS(resIndexDMSId, normfileName, input);
            node.setResIndexDMSId(resIndexDMSId);
        } else {
            // update file
            dmsProvider.updateContentInDMS(resIndexDMSId, normfileName, input);
        }

        // everything fine
        node.setResIndexDMSType("yaio-dms-service");
        node.setResIndexDMSState(IndexWorkflowState.INDEX_DONE);
    }
}
