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
package de.yaio.app.extension.datatransfer.mail;

import de.yaio.app.core.datadomain.ResIndexData;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.node.UrlResNode;
import de.yaio.app.core.nodeservice.NodeService;
import de.yaio.app.core.nodeservice.UrlResNodeService;
import de.yaio.app.extension.dms.services.ResContentDataService;
import de.yaio.app.extension.dms.services.ResDocumentService;
import de.yaio.commons.io.IOExceptionWithCause;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;

import static de.yaio.app.extension.autoformatter.formatter.BaseNodeDescAutoFormatter.COMMAND_DO_AUTO_GENERATE_DESC;
import static de.yaio.app.extension.autoformatter.formatter.BaseNodeDescAutoFormatter.PROFILE_SHOWCONTENT;

/**
 * convert emails to UrlResNodes
 */
@Service
public class EmailConverter {
    @Autowired
    private ResContentDataService resContentService;
    @Autowired
    protected ResDocumentService resDocumentService;

    // Logger
    private static final Logger LOGGER = Logger.getLogger(EmailConverter.class);

    /**
     * convert emails to UrlResNode and append them to the parent
     * @param parent                 parent to add the emails as UrlResNode
     * @param messages               emails to import
     * @throws IOExceptionWithCause  possible IOExceptions or email not parsable
     */
    public void importEmailsToParent(final BaseNode parent, final Set<Message> messages) throws IOExceptionWithCause {
        for (Message msg : messages) {
            BaseNode newNode = convertEmailToBaseNode(msg);
            newNode.setParentNode(parent);
        }
        parent.recalcData(NodeService.RecalcRecurseDirection.CHILDREN);
    }

    /**
     * convert email to UrlResNode
     * @param msg                    emails to convert
     * @return                       converted email
     * @throws IOExceptionWithCause  possible IOExceptions or email not parsable
     */
    protected BaseNode convertEmailToBaseNode(final Message msg) throws IOExceptionWithCause {
        UrlResNode node = new UrlResNode();
        try {
            node.setEbene(0);
            node.setResLocName("Email");
            String[] header = msg.getHeader("Message-ID");
            if (header != null && header.length > 0) {
                node.setResLocRef(header[0]);
            }
            node.setType(de.yaio.app.core.nodeservice.UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILRES);
            node.setMetaNodeSubType("UrlResNodeMetaNodeSubType." + node.getType());
            node.setState(node.getType());
            node.setName("Email: " + msg.getSubject());
            node.initSysData(true);
            node.setNodeDesc("<!---" + COMMAND_DO_AUTO_GENERATE_DESC + " " + PROFILE_SHOWCONTENT + " --->\n");

            // parse parts
            this.addPartsToBaseNode(node, msg);
            this.addHeaderToBaseNode(node, msg);
            LOGGER.info("converted email to node:" + node.getNameForLogger());
        } catch (MessagingException ex) {
            throw new IOExceptionWithCause("could extract content from multipart", msg, ex);
        }

        return node;
    }

    /**
     * convert email-part to UrlResNode, save it to dms and append it to the parent
     * @param parent                 parent to add the email-part as UrlResNode
     * @param msg                    part of the email to import and append to parent
     * @throws IOExceptionWithCause  possible IOExceptions or email not parsable
     */
    protected void addPartsToBaseNode(final UrlResNode parent, final Part msg) throws IOExceptionWithCause {
        try {
            Object content = msg.getContent();
            String disposition = msg.getDisposition();
            if (content instanceof Multipart) {
                // iterate over parts
                Multipart multipart = (Multipart) content;
                for (int j = 0; j < multipart.getCount(); j++) {
                    BodyPart bodyPart = multipart.getBodyPart(j);
                    this.addPartsToBaseNode(parent, bodyPart);
                }
            } else if (content instanceof Message) {
                // embedded email
                BaseNode subNode = this.convertEmailToBaseNode((Message) content);
                subNode.setParentNode(parent);
            } else if (disposition != null && (Part.ATTACHMENT.equalsIgnoreCase(disposition))) {
                this.addAttachmentToBaseNode(parent, msg);
            } else if (content instanceof String) {
                this.addMailtextToBaseNode(parent, msg);
            }
        } catch (MessagingException ex) {
            throw new IOExceptionWithCause("could extract content from multipart", msg, ex);
        } catch (IOException ex) {
            throw new IOExceptionWithCause("could extract content from part", msg, ex);
        }
    }

    /**
     * convert email-attachment, save it to dms to UrlResNode and append it to the parent
     * @param parent                 parent to add the email-attachment as UrlResNode
     * @param msg                    attachment of the email to import and append to parent
     * @throws IOExceptionWithCause  possible IOExceptions or email not parsable
     */
    protected void addAttachmentToBaseNode(final UrlResNode parent, final Part msg) throws IOExceptionWithCause {
        try {
            UrlResNode subNode;
            subNode = new UrlResNode();
            subNode.setEbene(0);
            subNode.setType(UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILATTACHMENTRES);
            subNode.setMetaNodeSubType("UrlResNodeMetaNodeSubType." + subNode.getType());
            subNode.setState(subNode.getType());
            subNode.initSysData(true);
            subNode.setParentNode(parent);

            DataHandler handler = msg.getDataHandler();
            String fileName = handler.getName();
            fileName = fileName.replaceAll("\\.(?=.*\\.)", "_");
            fileName = fileName.replaceAll("[^a-zA-Z0-9-.]", "_");

            subNode.setName("Attachment: " + fileName);
            subNode.setResLocName(fileName);
            subNode.setResLocRef(fileName);

            // save to dms
            resContentService.uploadResContentToDMS(subNode, fileName, handler.getInputStream());
            subNode.setResIndexDMSState(ResIndexData.IndexWorkflowState.INDEX_OPEN);
        } catch (MessagingException ex) {
            throw new IOExceptionWithCause("could extract content from multipart", msg, ex);
        } catch (IOException ex) {
            throw new IOExceptionWithCause("could extract content from part", msg, ex);
        }
    }

    /**
     * convert email-text/htmltext, save it to dms to UrlResNode and append it to the parent
     * @param parent                 parent to add the email-text/htmltext as UrlResNode
     * @param msg                    text/htmltext of the email to import and append to parent
     * @throws IOExceptionWithCause  possible IOExceptions or email not parsable
     */
    protected void addMailtextToBaseNode(final UrlResNode parent, final Part msg) throws IOExceptionWithCause {
        try {
            UrlResNode subNode;
            subNode = new UrlResNode();
            subNode.setEbene(0);
            subNode.initSysData(true);
            subNode.setParentNode(parent);

            Object content = msg.getContent();
            String contentType = msg.getContentType();
            String fileName;
            if (contentType.contains("text/plain")) {
                subNode.setType(UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILTEXTRES);
                subNode.setMetaNodeSubType("UrlResNodeMetaNodeSubType." + subNode.getType());
                subNode.setState(subNode.getType());
                subNode.setName("Emailtext");
                subNode.setResLocName("Emailtext");
                subNode.setResLocRef("Emailtext");
                fileName = "emailtxtcontent.txt";
            } else if (contentType.contains("text/html")) {
                subNode.setType(UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILHTMLRES);
                subNode.setMetaNodeSubType("UrlResNodeMetaNodeSubType." + subNode.getType());
                subNode.setState(subNode.getType());
                subNode.setName("HtmlEmailtext");
                subNode.setResLocName("HtmlEmailtext");
                subNode.setResLocRef("HtmlEmailtext");
                fileName = "emailhtmlcontent.html";
            } else {
                return;
            }

            // save to dms
            resContentService.uploadResContentToDMS(subNode, fileName,
                    new ByteArrayInputStream(content.toString().getBytes()));
            subNode.setResIndexDMSState(ResIndexData.IndexWorkflowState.INDEX_OPEN);
        } catch (MessagingException ex) {
            throw new IOExceptionWithCause("could extract content from multipart", msg, ex);
        } catch (IOException ex) {
            throw new IOExceptionWithCause("could extract content from part", msg, ex);
        }
    }

    /**
     * convert email-header, save it to dms to UrlResNode and append it to the parent
     * @param parent                 parent to add the email-header as UrlResNode
     * @param msg                    header of the email to import and append to parent
     * @throws IOExceptionWithCause  possible IOExceptions or email not parsable
     */
    protected void addHeaderToBaseNode(final UrlResNode parent, final Message msg) throws IOExceptionWithCause {
        try {
            UrlResNode subNode;
            subNode = new UrlResNode();
            subNode.setEbene(0);
            subNode.initSysData(true);
            subNode.setParentNode(parent);

            StringBuilder content = new StringBuilder();
            Enumeration headerNames = msg.getAllHeaders();
            while(headerNames.hasMoreElements()){
                Header header = (Header)headerNames.nextElement();
                content.append(header.getName() + ": " + header.getValue() + "\n");
            }
            subNode.setType(UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILHEADERRES);
            subNode.setMetaNodeSubType("UrlResNodeMetaNodeSubType." + subNode.getType());
            subNode.setState(subNode.getType());
            subNode.setName("Emailheader");
            subNode.setResLocName("Emailheader");
            subNode.setResLocRef("Emailheader");
            String fileName = "emailheader.eml";

            // save to dms
            resContentService.uploadResContentToDMS(subNode, fileName,
                    new ByteArrayInputStream(content.toString().getBytes()));
            subNode.setResIndexDMSState(ResIndexData.IndexWorkflowState.INDEX_OPEN);
        } catch (MessagingException ex) {
            throw new IOExceptionWithCause("could extract header from email", msg, ex);
        } catch (IOException ex) {
            throw new IOExceptionWithCause("could extract header from part", msg, ex);
        }
    }
}