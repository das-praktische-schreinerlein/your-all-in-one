/*
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
package de.yaio.app.extension.autoformatter.formatter;

import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.node.UrlResNode;
import de.yaio.app.core.nodeservice.UrlResNodeService;
import de.yaio.app.extension.dms.services.ResDocumentService;
import de.yaio.commons.io.IOExceptionWithCause;
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
import java.util.*;

import static de.yaio.app.datatransfer.importer.parser.Parser.CONST_PATTERN_SEG_NAME;

/**
 * format emails
 */
@Service
public class EmailDescAutoFormatter extends BaseNodeDescAutoFormatter {
    @Override
    public void genMetadataForNode(final BaseNode node) throws IOExceptionWithCause {
        for (BaseNode subNode : node.getChildNodes()) {
            if (UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILRES.equals(subNode.getType())) {
                genMetadataForNode(subNode);
            }
        }

        super.genMetadataForNode(node);
    }

    @Override
    protected void generateNameBlock(final BaseNode node, final StringBuilder name)
            throws IOExceptionWithCause{
        UrlResNode headerNode =
                (UrlResNode)getChildNodeByType(node, UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILHEADERRES);
        if (headerNode == null) {
            return;
        }

        try (InputStream isHeader = resDocumentService.downloadResContentFromDMS(headerNode, 0)) {
            // create message by header
            MimeMessage msg = new MimeMessage(null, isHeader);

            // parse metadata
            Address[] fromAddress = msg.getFrom();
            String from = "unknown";
            if (fromAddress != null && fromAddress.length > 0 ) {
                from = fromAddress[0].toString();
            }
            String subject = msg.getSubject();
            String toList = parseAddresses(msg.getRecipients(RecipientType.TO));

            String emailName = "Email: " + formatDate(msg.getSentDate()) + " " + subject + " [" + from + " -> " + toList + "]";
            emailName = emailName.replaceAll("(?!" + CONST_PATTERN_SEG_NAME + ").+", " ");

            node.setName(emailName);
            name.append(emailName);
        } catch (MessagingException ex) {
            throw new IOExceptionWithCause("could not convert email to node", node.getNameForLogger(), ex);
        } catch (IOException ex) {
            throw new IOExceptionWithCause("could extract content from email", node.getNameForLogger(), ex);
        }
    }

    @Override
    protected void generateMetaBlock(final BaseNode node, final StringBuilder meta)
            throws IOExceptionWithCause{
        UrlResNode headerNode =
                (UrlResNode)getChildNodeByType(node, UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILHEADERRES);
        if (headerNode == null) {
            return;
        }

        String id = node.getSysUID();
        try (InputStream isHeader = resDocumentService.downloadResContentFromDMS(headerNode, 0)) {
            // create message by header
            MimeMessage msg = new MimeMessage(null, isHeader);

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

            String name = "Email: " + subject + " [" + from + " -> " + toList + "]";
            name = name.replaceAll("(?!" + CONST_PATTERN_SEG_NAME + ").+", " ");
            node.setName(name);

            // Meta
            meta.append("\n<!---BOX--->\n" +
                    "## Meta <!---TOGGLER meta" + id + ",text--->\n" +
                    "<!---CONTAINER meta" + id + "--->\n\n");
            meta.append("- subject: " + subject + "\n");
            meta.append("- from: " + from + "\n");
            meta.append("- to: " + toList + "\n");
            meta.append("- cc: " + ccList + "\n");
            meta.append("- sent: " + formatDate(sentDate) + "\n");
            meta.append("- received: " + receivedDate + "\n");
            meta.append("<!---/CONTAINER meta" + id + "--->\n" +
                    "<!---/BOX--->\n");
        } catch (MessagingException ex) {
            throw new IOExceptionWithCause("could not convert email to node", node.getNameForLogger(), ex);
        } catch (IOException ex) {
            throw new IOExceptionWithCause("could extract content from email", node.getNameForLogger(), ex);
        }
    }

    @Override
    protected void generateAdditionalBlocks(final BaseNode node, final StringBuilder additional)
            throws IOExceptionWithCause{
        UrlResNode contentNode =
                (UrlResNode)getChildNodeByType(node, UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILTEXTRES);
        if (contentNode == null) {
            return;
        }
        UrlResNode headerNode =
                (UrlResNode)getChildNodeByType(node, UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILHEADERRES);
        if (headerNode == null) {
            return;
        }

        String id = node.getSysUID();
        List<String> profiles = extractProfiles(node, node.getNodeDesc());

        // parts
        StringBuilder parts = new StringBuilder();
        parts.append("\n<!---BOX--->\n" +
                "## Attachments <!---TOGGLER attachments" + id + ",text--->\n" +
                "<!---CONTAINER attachments" + id + "--->\n");
        for (BaseNode subNode : node.getChildNodes()) {
            parts.append("- [" + subNode.getType() + ": " + ((UrlResNode)subNode).getResLocName() + "]" +
                    "(yaiodmsembed:" + subNode.getSysUID() + ")\n");
        }
        parts.append("<!---/CONTAINER attachments" + id + "--->\n" +
                "<!---/BOX--->\n");

        // header
        StringBuilder headers = new StringBuilder();
        try (InputStream isHeader = resDocumentService.downloadResContentFromDMS(headerNode, 0)) {
            // create message by header
            MimeMessage msg = new MimeMessage(null, isHeader);

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
        } catch (MessagingException ex) {
            throw new IOExceptionWithCause("could not convert email to node", node.getNameForLogger(), ex);
        } catch (IOException ex) {
            throw new IOExceptionWithCause("could extract content from email", node.getNameForLogger(), ex);
        }

        // content
        StringBuilder content = new StringBuilder();
        if (!profiles.contains(PROFILE_NODESC.toUpperCase()) &&
                profiles.contains(PROFILE_SHOWCONTENT.toUpperCase())) {
            generateContentBlock(contentNode, false, content);
        }

        additional.append(parts.toString());
        additional.append(content.toString());
        additional.append(headers.toString());
    }

    /**
     * Returns a list of addresses in String format separated by comma
     *
     * @param address an array of Address objects
     * @return a string represents a list of addresses
     */
    private String parseAddresses(final Address[] address) {
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