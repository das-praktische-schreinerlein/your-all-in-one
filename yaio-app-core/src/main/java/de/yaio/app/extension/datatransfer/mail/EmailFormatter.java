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

import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.node.UrlResNode;
import de.yaio.app.core.nodeservice.UrlResNodeService;
import de.yaio.app.extension.dms.services.ResDocumentService;
import de.yaio.commons.io.IOExceptionWithCause;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.Header;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Set;

/**
 * format emails
 */
@Service
public class EmailFormatter {
    @Autowired
    protected ResDocumentService resDocumentService;

    // Logger
    private static final Logger LOGGER = Logger.getLogger(EmailFormatter.class);

    public void genMetadataForEmailNodes(Set<BaseNode> emailNodes) throws IOExceptionWithCause {
        for (BaseNode node : emailNodes) {
            genMetadataForEmailNode(node);
        }
    }

    public void genMetadataForEmailNode(BaseNode node) throws IOExceptionWithCause {
        try {
            // get content and header
            UrlResNode contentNode = null;
            UrlResNode headerNode = null;
            for (BaseNode subNode : node.getChildNodes()) {
                if (UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILTEXTRES.equals(subNode.getType())) {
                    contentNode = (UrlResNode) subNode;
                } else if (UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILHEADERRES.equals(subNode.getType())) {
                    headerNode = (UrlResNode) subNode;
                } else if (UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILRES.equals(subNode.getType())) {
                    genMetadataForEmailNode(subNode);
                }
            }
            if (contentNode == null) {
                return;
            }

            // create message by header
            InputStream is = resDocumentService.downloadResContentFromDMS(headerNode, 0);
            MimeMessage msg = new MimeMessage(null, is);
            is.close();

            // parse metadata
            Address[] fromAddress = msg.getFrom();
            String from = "unknown";
            if (fromAddress != null && fromAddress.length > 0 ) {
                from = fromAddress[0].toString();
            }
            String subject = msg.getSubject();
            String toList = parseAddresses(msg.getRecipients(RecipientType.TO));
            String ccList = parseAddresses(msg.getRecipients(RecipientType.CC));
            Date sentDate = msg.getSentDate();
            Date receivedDate = msg.getReceivedDate();

            node.setName("Email: " + subject + " [" + from + " -> " + toList + "]");
            StringBuilder nodeDesc = new StringBuilder();
            nodeDesc.append("# Email: " + subject + "\n");
            String id = node.getSysUID();

            // Meta
            StringBuilder meta = new StringBuilder();
            meta.append("\n<!---BOX--->\n" +
                    "## Meta <!---TOGGLER meta" + id + ",text--->\n" +
                    "<!---CONTAINER meta" + id + "--->\n\n");
            meta.append("- subject: " + subject + "\n");
            meta.append("- from: " + from + "\n");
            meta.append("- to: " + toList + "\n");
            meta.append("- cc: " + ccList + "\n");
            meta.append("- sent: " + sentDate + "\n");
            meta.append("- received: " + receivedDate + "\n");
            meta.append("<!---/CONTAINER meta" + id + "--->\n" +
                    "<!---/BOX--->\n");

            // parts
            StringBuilder parts = new StringBuilder();
            StringBuilder content = new StringBuilder();
            parts.append("\n<!---BOX--->\n" +
                    "## Attachments <!---TOGGLER attachments" + id + ",text--->\n" +
                    "<!---CONTAINER attachments" + id + "--->\n");
            for (BaseNode subNode : node.getChildNodes()) {
                parts.append("- [" + subNode.getType() + ": " + ((UrlResNode)subNode).getResLocName() + "](yaiodmsembed:" + ((UrlResNode)subNode).getSysUID() + ")\n");
            }
            parts.append("<!---/CONTAINER attachments" + id + "--->\n" +
                    "<!---/BOX--->\n");

            // content
            if (contentNode != null) {
                is = resDocumentService.downloadResContentFromDMS(contentNode, 0);
                if (is != null) {
                    content.append("\n<!---BOX--->\n" +
                            "## Text <!---TOGGLER content" + id + ",text--->\n" +
                            "<!---CONTAINER content" + id + "--->\n\n" +
                            "```\n");
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(is, writer);
                    content.append(writer.toString().replaceAll("```", "'''"));
                    content.append("\n```\n" +
                            "<!---/CONTAINER content" + id + "--->\n" +
                            "<!---/BOX--->\n");
                    is.close();
                }
            }

            // header
            StringBuilder headers = new StringBuilder();
            headers.append("\n<!---BOX--->\n" +
                    "## Header <!---TOGGLER header" + id + ",text--->\n" +
                    "<!---CONTAINER header" + id + "--->\n");
            Enumeration headerNames = msg.getAllHeaders();
            while(headerNames.hasMoreElements()){
                Header header = (Header)headerNames.nextElement();
                headers.append("- " + header.getName() + ": " + header.getValue() + "\n");
            }
            headers.append("<!---/CONTAINER header" + id + "--->\n" +
                    "<!---/BOX--->\n");

            nodeDesc.append(meta);
            nodeDesc.append(content);
            nodeDesc.append(parts);
            nodeDesc.append(headers);
            node.setNodeDesc(nodeDesc.toString());
            LOGGER.info("updated node metaddata with headersources:" + node.getNameForLogger());
        } catch (MessagingException ex) {
            throw new IOExceptionWithCause("could not convert email to node", node.getNameForLogger(), ex);
        } catch (IOException ex) {
            throw new IOExceptionWithCause("could extract content from email", node.getNameForLogger(), ex);
        }
    }

    /**
     * Returns a list of addresses in String format separated by comma
     *
     * @param address an array of Address objects
     * @return a string represents a list of addresses
     */
    private String parseAddresses(Address[] address) {
        String listAddress = "";

        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                listAddress += address[i].toString() + ", ";
            }
        }
        if (listAddress.length() > 1) {
            listAddress = listAddress.substring(0, listAddress.length() - 2);
        }

        return listAddress;
    }
}