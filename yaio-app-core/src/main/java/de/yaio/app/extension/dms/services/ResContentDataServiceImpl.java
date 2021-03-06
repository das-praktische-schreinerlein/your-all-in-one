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

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.datadomain.ResContentData;
import de.yaio.app.core.datadomain.ResContentData.UploadWorkflowState;
import de.yaio.app.core.datadomain.ResLocData;
import de.yaio.app.core.datadomainservice.TriggeredDataDomainRecalcImpl;
import de.yaio.app.core.dbservice.BaseNodeRepository;
import de.yaio.app.core.node.UrlResNode;
import de.yaio.app.core.nodeservice.UrlResNodeService;
import de.yaio.app.extension.dms.utils.DMSClient;
import de.yaio.app.extension.dms.utils.YaioWebshotClient;
import de.yaio.app.utils.db.DBFilter;
import de.yaio.commons.data.DataUtils;
import de.yaio.commons.io.IOExceptionWithCause;
import de.yaio.services.dms.api.model.StorageResource;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/** 
 * businesslogic for dataDomain: ResContentData (upload url/file to dms)
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Service
public class ResContentDataServiceImpl extends TriggeredDataDomainRecalcImpl implements ResContentDataService {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(ResContentDataServiceImpl.class);

    @Autowired
    protected BaseNodeRepository baseNodeDBService;
    @Autowired
    protected YaioWebshotClient webshotProvider;
    @Autowired
    protected DMSClient dmsProvider;

    @Override
    public Class<?> getRecalcTargetClass() {
        return ResContentData.class;
    }

    @Override
    @Transactional
    public void doRecalcWhenTriggered(final DataDomain datanode) {
        if (datanode == null) {
            return;
        }

        // Check if node is compatibel
        if (!UrlResNode.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        
        // Check if data is valid
        //UrlResNode node = (UrlResNode) BaseNode.findBaseNode(((BaseNode) datanode).getSysUID());
        UrlResNode node = (UrlResNode) datanode;
        if (!UploadWorkflowState.UPLOAD_OPEN.equals(node.getResContentDMSState())) {
            LOGGER.info("no workflow:UPLOAD_OPEN for node:" + datanode.getNameForLogger());
            return;
        }
        if (!UrlResNodeService.CONST_NODETYPE_IDENTIFIER_URLRES.equals(node.getType())) {
            LOGGER.info("no UrlResNodeService for node:" + datanode.getNameForLogger());
            return;
        }

        // Roll
        try {
            // upload
            this.uploadResLocToDMS((ResLocData) datanode);
        } catch (IOExceptionWithCause ex) {
            // error: reset id and set to failed
            LOGGER.info("IOExceptionWithCause while uploading node to dms:" + datanode.getNameForLogger(), ex);
            node.setResContentDMSState(UploadWorkflowState.UPLOAD_FAILED);
        } catch (IOException ex) {
            // error: reset id and set to failed
            LOGGER.error("IOException while uploading node to dms:" + datanode.getNameForLogger(), ex);
            node.setResContentDMSState(UploadWorkflowState.UPLOAD_FAILED);
        }

        // save node
        baseNodeDBService.update(node);
    }

    @Override
    public List<DBFilter> getDBTriggerFilter() {
        List<DBFilter> dbFilters = new ArrayList<DBFilter>();

        // filter upload open only
        String sql = "(resContentDMSState = :eqresContentDMSState)";
        List<DBFilter.Parameter> parameters = new ArrayList<DBFilter.Parameter>();
        parameters.add(new DBFilter.Parameter("eqresContentDMSState", UploadWorkflowState.UPLOAD_OPEN));
        dbFilters.add(new DBFilter(sql, parameters));

        return dbFilters;
    }

    @Override
    public void uploadResLocToDMS(final ResLocData datanode) throws IOExceptionWithCause, IOException {
        if (!ResContentData.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        
        // get image from url
        byte[] response;
        response = webshotProvider.getWebShotFromUrl(datanode.getResLocRef());

        // push image to dms
        InputStream input = new ByteArrayInputStream(response);
        this.uploadResContentToDMS((ResContentData) datanode, datanode.getResLocRef() + ".png", input);
    }

    @Override
    public void uploadResContentToDMS(final ResContentData datanode, final String fileName, 
                                      final InputStream input) throws IOExceptionWithCause, IOException {
        if (!UrlResNode.class.isInstance(datanode)) {
            throw new IllegalArgumentException();
        }
        
        UrlResNode node = (UrlResNode) datanode;
        
        String normfileName = DataUtils.normalizeFileName(fileName);
        String resContentDMSId = node.getResContentDMSId();
        if (StringUtils.isEmpty(resContentDMSId)) {
            // add new file
            resContentDMSId = node.getSysUID() + "-" + "content";
            StorageResource resource = dmsProvider.addContentToDMS(resContentDMSId, normfileName, input);
            node.setResContentDMSId(resource.getDMSId());
        } else {
            // update file
            dmsProvider.updateContentInDMS(resContentDMSId, normfileName, input);
        }

        // everything fine
        node.setResContentDMSType("yaio-dms-service");
        node.setResContentDMSState(UploadWorkflowState.UPLOAD_DONE);
    }

    @Override
    protected BaseNodeRepository getBaseNodeDBService() {
        return baseNodeDBService;
    }
}
