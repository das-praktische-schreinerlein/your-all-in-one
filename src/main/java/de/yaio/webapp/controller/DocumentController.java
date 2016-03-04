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
package de.yaio.webapp.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.yaio.commons.data.DataUtils;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.UrlResNode;
import de.yaio.extension.dms.services.ResDocumentService;
import de.yaio.services.dms.storage.StorageResourceVersion;

/** 
 * controller with Download-Services to export BaseNodes in different 
 * formats (wiki, excel..)
 *  
 * @FeatureDomain                Webservice
 * @package                      de.yaio.webapp.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Controller
@RequestMapping("/dms")
public class DocumentController {
    
    /** API-Version **/
    public static final String API_VERSION = "1.0.0";

    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(DocumentController.class);
   
    @Autowired
    protected ResDocumentService resDocumentService;
    protected FileTypeMap fileTypeMap = FileTypeMap.getDefaultFileTypeMap();
    
    /** 
     * Request to download the resource-content of UrlResNode with sysUID
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     * @param flgEmbed               goal is to embed it (Content-Disposition inline)
     * @param flgIndexData           the extracted metada is requested only
     * @throws IOException           is something failed 
     */
    public void commondRequestResource(final String sysUID, 
                                       final HttpServletResponse response,
                                       final boolean flgEmbed,
                                       final boolean flgIndexData) throws IOException {
        BaseNode baseNode = BaseNode.findBaseNode(sysUID);
        if (sysUID == null) {
            // node not found
            response.setStatus(404);
            response.getWriter().append("node not found");
            return;
        }

        UrlResNode node = (UrlResNode) baseNode;
        InputStream input;
        StorageResourceVersion resVersion;
        if (flgIndexData) {
            // Indexdata
            input = resDocumentService.downloadResIndexFromDMS(node, 0);
            resVersion = resDocumentService.getMetaDataForResIndexFromDMS(node, 0);
        } else {
            // Content
            input = resDocumentService.downloadResContentFromDMS(node, 0);
            resVersion = resDocumentService.getMetaDataForResContentFromDMS(node, 0);
        }
        String normfileName = DataUtils.normalizeFileName(resVersion.getOrigName());

        String fileType = fileTypeMap.getContentType(normfileName);
        MediaType mimeType = MediaType.valueOf(fileType);
        response.setContentType(mimeType.getType());
        if (flgEmbed) {
            response.setHeader("content-Disposition", "inline; filename=" + normfileName);
        } else {
            response.setHeader("content-Disposition", "attachment; filename=" + normfileName);
        }
        IOUtils.copyLarge(input, response.getOutputStream());
    }

    /** 
     * Request to download the resource-content of UrlResNode with sysUID
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/download/{sysUID}")
    public void downloadResource(@PathVariable(value = "sysUID") final String sysUID, 
                                   final HttpServletResponse response) {
        try {
            this.commondRequestResource(sysUID, response, false, false);
        } catch (IOException e) {
            response.setStatus(404);
            e.printStackTrace();
        }
    }

    /** 
     * Request to embed the resource-content of UrlResNode with sysUID
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/embed/{sysUID}")
    public void embedResource(@PathVariable(value = "sysUID") final String sysUID, 
                                   final HttpServletResponse response) {
        try {
            this.commondRequestResource(sysUID, response, true, false);
        } catch (IOException e) {
            response.setStatus(404);
            e.printStackTrace();
        }
    }

    /** 
     * Request to download the resource-index of UrlResNode with sysUID
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/indexdownload/{sysUID}")
    public void downloadIndexResource(@PathVariable(value = "sysUID") final String sysUID, 
                                   final HttpServletResponse response) {
        try {
            this.commondRequestResource(sysUID, response, false, true);
        } catch (IOException e) {
            response.setStatus(404);
            e.printStackTrace();
        }
    }

    /** 
     * Request to embed the resource-index of UrlResNode with sysUID
     * @param sysUID                 sysUID to export
     * @param response               the response-Obj to set contenttype and headers
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/indexembed/{sysUID}")
    public void embedIndexResource(@PathVariable(value = "sysUID") final String sysUID, 
                                   final HttpServletResponse response) {
        try {
            this.commondRequestResource(sysUID, response, true, true);
        } catch (IOException e) {
            response.setStatus(404);
            e.printStackTrace();
        }
    }
}
