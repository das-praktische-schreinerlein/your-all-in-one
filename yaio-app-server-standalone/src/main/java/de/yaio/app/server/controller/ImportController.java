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
package de.yaio.app.server.controller;

import de.yaio.app.config.ContextHelper;
import de.yaio.app.core.dbservice.BaseNodeRepository;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.jpa.JPAExporter;
import de.yaio.app.extension.datatransfer.common.ExtendedDatatransferUtils;
import de.yaio.app.extension.datatransfer.mail.EmailImporter;
import de.yaio.app.extension.datatransfer.ppl.PPLImporter;
import de.yaio.app.extension.datatransfer.wiki.WikiImportOptions;
import de.yaio.app.extension.datatransfer.wiki.WikiImporter;
import de.yaio.app.server.restcontroller.NodeActionResponse;
import de.yaio.app.server.restcontroller.NodeRestController;
import de.yaio.commons.io.IOExceptionWithCause;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

/** 
 * Upload-Services to import BaseNodes in different 
 * formats (wiki, ppl, excel..)
 *  
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Controller
@RequestMapping("/imports")
public class ImportController {
    
    /** API-Version **/
    public static final String API_VERSION = "1.0.0";

    @Autowired
    protected ConverterUtils converterUtils;
    
    @Autowired
    protected ExtendedDatatransferUtils datatransferUtils;

    @Autowired
    protected EmailImporter mailImporter;

    @Autowired
    protected BaseNodeRepository baseNodeDBService;

    @Autowired
    private ApplicationContext appContext;


    // Logger
    private static final Logger LOGGER = Logger.getLogger(ExportController.class);

    /**
     * Request to import the uploaded wiki-file from param "file" to the 
     * node parentSysUID returning a simple message
     * @param parentSysUID           sysUID to append the new nodes
     * @param file                   the uploaded file stream with the data to import
     * @return                       text-message
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/wiki/{parentSysUID}", 
                    produces = "text/html")
    public String importNodeAsWiki(@PathVariable(value = "parentSysUID") final String parentSysUID,
                                   @RequestParam("file") final MultipartFile file) throws IOException, ParserException {
        NodeActionResponse response = new NodeActionResponse(
                        "ERROR", "node '" + parentSysUID + "' doesnt exists", 
                        null, null, null, null);

        // find the parentnode
        BaseNode node = baseNodeDBService.findBaseNode(parentSysUID);
        if (node == null) {
            return response.getStateMsg();
        }
        
        // check file
        if (!file.isEmpty()) {
            // copy to tmpFile
            File tmpFile = File.createTempFile("upload", "wiki");
            tmpFile.deleteOnExit();
            file.transferTo(tmpFile);

            // read filecontent + delete
            String wikiSrc = PPLImporter.readFromInput(tmpFile);
            tmpFile.delete();

            // read the childnodes only 1 level
            node.initChildNodesFromDB(0);

            // create dummy masternode
            BaseNode masterNode = datatransferUtils.createTemporaryMasternode(
                            parentSysUID, node.getMetaNodePraefix(), node.getMetaNodeNummer());

            // Parser+Options anlegen
            WikiImportOptions inputOptions = new WikiImportOptions();
            inputOptions.setFlgReadList(true);
            inputOptions.setFlgReadUe(true);
            inputOptions.setStrDefaultMetaNodePraefix(masterNode.getMetaNodePraefix());
            WikiImporter wikiImporter = new WikiImporter(inputOptions);
            datatransferUtils.parseNodesFromWiki(wikiImporter, inputOptions, masterNode, wikiSrc);

            // JPA-Exporter
            JPAExporter jpaExporter = getJPAExporter();
            jpaExporter.getMasterNodeResult(masterNode, null);

            // create new response
            response = NodeRestController.createResponseObj(
                            node, "data for node '" + parentSysUID + "' imported", true);
        } else {
            return new NodeActionResponse(
                    "ERROR", 
                    "You failed to upload to node '" 
                        + parentSysUID + "' because the file was empty.", 
                     null, null, null, null).getStateMsg();
        }            
        
        return response.getStateMsg();
    }

    /** 
     * Request to import the uploaded json-file from param "file" to the 
     * node parentSysUID returning a simple message
     * @param parentSysUID           sysUID to append the new nodes
     * @param file                   the uploaded file stream with the data to import
     * @return                       text-message
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/json/{parentSysUID}", 
                    produces = "text/html")
    public String importNodeAsJson(@PathVariable(value = "parentSysUID") final String parentSysUID,
                                   @RequestParam("file") final MultipartFile file) throws IOException, ParserException {
        NodeActionResponse response = new NodeActionResponse(
                        "ERROR", "node '" + parentSysUID + "' doesnt exists", 
                        null, null, null, null);

        // find the parentnode
        BaseNode node = baseNodeDBService.findBaseNode(parentSysUID);
        if (node == null) {
            return response.getStateMsg();
        }
        
        // check file
        if (!file.isEmpty()) {
            // copy to tmpFile
            File tmpFile = File.createTempFile("upload", "json");
            tmpFile.deleteOnExit();
            file.transferTo(tmpFile);

            // read filecontent + delete
            String jsonSrc = PPLImporter.readFromInput(tmpFile);
            tmpFile.delete();

            // read the childnodes only 1 level
            node.initChildNodesFromDB(0);

            // create dummy masternode
            BaseNode masterNode = datatransferUtils.createTemporaryMasternode(
                            parentSysUID, node.getMetaNodePraefix(), node.getMetaNodeNummer());

            // Parser+Options anlegen
            WikiImportOptions inputOptions = new WikiImportOptions();
            inputOptions.setStrDefaultMetaNodePraefix(masterNode.getMetaNodePraefix());
            datatransferUtils.parseValidatedNodesFromJson(inputOptions, masterNode, jsonSrc);

            // JPA-Exporter
            JPAExporter jpaExporter = getJPAExporter();
            jpaExporter.getMasterNodeResult(masterNode, null);

            // create new response
            response = NodeRestController.createResponseObj(
                            node, "data for node '" + parentSysUID + "' imported", true);
       } else {
            return new NodeActionResponse(
                    "ERROR", 
                    "You failed to upload to node '" 
                        + parentSysUID + "' because the file was empty.", 
                     null, null, null, null).getStateMsg();
        }            
        
        return response.getStateMsg();
    }

    /**
     * Request to import the uploaded mail-file from param "file" to the
     * node parentSysUID returning a simple message
     * @param parentSysUID           sysUID to append the new nodes
     * @param file                   the uploaded file stream with the data to import as mail
     * @return                       text-message
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
            value = "/mail/{parentSysUID}",
            produces = "text/html")
    public String importMail(@PathVariable(value = "parentSysUID") final String parentSysUID,
                             @RequestParam("file") final MultipartFile file) throws IOException, IOExceptionWithCause {
        NodeActionResponse response = new NodeActionResponse(
                "ERROR", "node '" + parentSysUID + "' doesnt exists",
                null, null, null, null);

        // find the parentnode
        BaseNode node = baseNodeDBService.findBaseNode(parentSysUID);
        if (node == null) {
            return response.getStateMsg();
        }

        // check file
        if (!file.isEmpty()) {
            // copy to tmpFile
            File tmpFile = File.createTempFile("upload", "json");
            tmpFile.deleteOnExit();
            file.transferTo(tmpFile);

            mailImporter.importMailFiles(parentSysUID, Collections.singletonList(tmpFile), false, null, null);

            // create new response
            response = NodeRestController.createResponseObj(
                    node, "mail appended to '" + parentSysUID + "'", true);
        } else {
            return new NodeActionResponse(
                    "ERROR",
                    "You failed to upload the mail to '"
                            + parentSysUID + "' because the file was empty.",
                    null, null, null, null).getStateMsg();
        }

        return response.getStateMsg();
    }

    @ExceptionHandler(ParserException.class)
    public String handleCustomException(final HttpServletRequest request, final ParserException e,
                                        final HttpServletResponse response) {
        LOGGER.info("ParserException while running request:" + request.toString(), e);
        response.setStatus(SC_BAD_REQUEST);
        return "cant parse uploaded source => " + e.getMessage();
    }

    @ExceptionHandler(IOException.class)
    public String handleCustomException(final HttpServletRequest request, final IOException e,
                                        final HttpServletResponse response) {
        LOGGER.info("IOException while running request:" + request.toString(), e);
        response.setStatus(SC_BAD_REQUEST);
        return "cant handle uploaded file => " + e.getMessage();
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public String handleAllException(final HttpServletRequest request, final Exception e,
                                     final HttpServletResponse response) {
        LOGGER.warn("error while running request:" + request.toString(), e);
        response.setStatus(SC_INTERNAL_SERVER_ERROR);
        return "cant import node";
    }

    protected JPAExporter getJPAExporter() {
        JPAExporter exporter = new JPAExporter();
        ContextHelper.getInstance().autowireService(appContext, exporter);
        return exporter;
    }
}
